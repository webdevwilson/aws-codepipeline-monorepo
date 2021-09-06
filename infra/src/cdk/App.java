package cdk;

import cdk.trigger.TriggerStack;
import software.amazon.awscdk.core.SecretValue;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(final String[] args) {
        final software.amazon.awscdk.core.App app = new software.amazon.awscdk.core.App();


        // create pipelines and configuration
        final GithubSource githubSource = GithubSource.builder()
                .owner("webdevwilson")
                .repo("aws-codepipeline-monorepo")
                .branch("master")
                .oauthToken(SecretValue.secretsManager("github-token"))
                .build();

//        // add each app as a pipeline
//        final List<ConfigurationFile.Pipeline> pipelineConfigs = new ArrayList<>();
//        Arrays.asList("app1", "app2", "app3").forEach(path -> {
//            final ConfigurationFile.Pipeline pipeline = ConfigurationFile.Pipeline.builder()
//                    .pipelineName(path)
//                    .changeMatchExpressions(new String[]{
//                            path + "/*",
//                            "common/*"
//                    })
//                    .ignoreFiles(new String[]{
//                            "*.md",
//                            "*.pdf"
//                    })
//                    .branch("master")
//                    .build();
//            pipelineConfigs.add(pipeline);
//
//            final Stack appStack = new EcsStack(app, path + "App", StackProps.builder()
//                    .build()
//            );
//
//            // create the pipeline
//            new PipelineStack(app, path + "Pipeline", PipelineStack.Props.builder()
//                    .githubSource(githubSource)
//                    .pipeline(pipeline)
//                    .applicationStack(appStack)
//                    .build());
//        });
//
//        final ConfigurationFile file = new ConfigurationFile(pipelineConfigs);
//
//

        new TriggerStack(app, "triggerStack", TriggerStack.Props.builder()
                .pipelineName("monorepo-trigger")
                .githubSource(githubSource)
                .build());

        app.synth();
    }
}
