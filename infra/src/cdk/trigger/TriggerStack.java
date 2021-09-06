package cdk.trigger;

import cdk.ConfigurationFile;
import cdk.GithubSource;
import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.LambdaRestApiProps;
import software.amazon.awscdk.services.codebuild.*;
import software.amazon.awscdk.services.codepipeline.*;
import software.amazon.awscdk.services.codepipeline.actions.*;
import software.amazon.awscdk.services.events.targets.CodeBuildProject;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.RetentionDays;

import software.amazon.awscdk.services.s3.IBucket;
import software.amazon.awscdk.services.s3.assets.Asset;
import software.amazon.awscdk.services.s3.assets.AssetProps;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TriggerStack extends Stack {

    @lombok.Builder
    @lombok.Data
    public static final class Props {
        private String jar;
        private String pipelineName;
        private StackProps stackProps;
        private GithubSource githubSource;
    }

    @lombok.Getter
    private final IBucket artifactBucket;

    public TriggerStack(final Construct scope, final String id, Props props) {
        super(scope, id, props.stackProps);

        // Write config to local
//        final String configFilePath = String.join(File.pathSeparator,
//                Arrays.asList(System.getProperty("java.io.tmpdir"), "pipelineConfig.json")
//        );
//        configFile.writeToFile(configFilePath);

        // Add a layer to the lambda that contains the dependencies
        final LayerVersion layer = new LayerVersion(this, "fnLayer", LayerVersionProps.builder()
                .code(Code.fromAsset("target/deps"))
                .compatibleRuntimes(Arrays.asList(Runtime.JAVA_8))
                .build()
        );

        // Environment variables for the lambda function
        final Map<String, String> env = new HashMap<>();
//        env.put("CONFIG_FILE_BUCKET", configFileAsset.getS3BucketName());
//        env.put("CONFIG_FILE_KEY", configFileAsset.getS3ObjectKey());

        // Create the lambda function our pipeline will call
        final Function fn = new Function(this, "fn", FunctionProps.builder()
                .functionName(id)
                .runtime(Runtime.JAVA_8)
                .code(Code.fromAsset(props.jar))
                .handler(cdk.trigger.lambda.Fn.class.getName())
                .layers(Arrays.asList(layer))
                .memorySize(1024)
                .environment(env)
                .timeout(Duration.seconds(30))
                .logRetention(RetentionDays.ONE_WEEK)
                .build());

        // Allow lambda to read from the bucket
//        configFileAsset.grantRead(fn.getRole());

        // create the pipeline that updates application pipelines and triggers them
        final Pipeline pipeline = new Pipeline(this, "triggerPipeline", PipelineProps.builder()
                .pipelineName(props.getPipelineName())
                .build());

        this.artifactBucket = pipeline.getArtifactBucket();

        // create the source stage
        final Artifact sourceArtifact = Artifact.artifact("Source");
        pipeline.addStage(StageOptions.builder()
                .stageName("source")
                .actions(Collections.singletonList(props.githubSource.getGitHubSourceAction(sourceArtifact)))
                .build());

        // create the action that does a cdk synth to manage application pipelines
        final PipelineProject project = new PipelineProject(this, "triggerPipelineCdkBuild",
                PipelineProjectProps.builder()
                .projectName("cdk-synth")
                .buildSpec(BuildSpec.fromSourceFilename("infra/src/cdk/trigger/buildspec.yml"))
                .build());
        final CodeBuildAction cdkSynthAction = new CodeBuildAction(CodeBuildActionProps.builder()
                .actionName("cdk-synth")
                .project(project)
                .input(sourceArtifact)
                .build());

        // invoke the filter lambda
        final Artifact pipelineSelectionArtifact = Artifact.artifact("PipelineSelection");
        final LambdaInvokeAction filterLambdaAction = new LambdaInvokeAction(LambdaInvokeActionProps.builder()
                .actionName("select-pipelines")
                .lambda(fn)
                .inputs(Arrays.asList(sourceArtifact))
                .outputs(Arrays.asList(pipelineSelectionArtifact))
                .build());

        // create the build stage
        pipeline.addStage(StageOptions.builder()
                .stageName("build")
                .actions(Arrays.asList(
                        filterLambdaAction,
                        cdkSynthAction
                ))
                .build()
        );
    }

}