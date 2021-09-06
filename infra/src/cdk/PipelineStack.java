package cdk;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.codebuild.Project;
import software.amazon.awscdk.services.codepipeline.Artifact;
import software.amazon.awscdk.services.codepipeline.Pipeline;
import software.amazon.awscdk.services.codepipeline.PipelineProps;
import software.amazon.awscdk.services.codepipeline.StageOptions;
import software.amazon.awscdk.services.codepipeline.actions.*;

import java.util.Collections;

public class PipelineStack extends Stack {

    @lombok.Builder
    public static final class Props {
        private ConfigurationFile.Pipeline pipeline;
        private GithubSource githubSource;
        private StackProps stackProps;
        private Stack applicationStack;
    }

    @lombok.Getter
    private final Pipeline pipeline;

    public PipelineStack(final Construct scope, final String id, final PipelineStack.Props props) {
        super(scope, id, props.stackProps);

        // build pipeline
        this.pipeline = new Pipeline(this, this.getArtifactId() + "Pipeline", PipelineProps.builder()
                .pipelineName(props.pipeline.getPipelineName())
                .build());

        // add source stage
        final Artifact sourceArtifact = Artifact.artifact("Source");
        pipeline.addStage(sourceStage(sourceArtifact, props.githubSource));

        // add build stage
        final Artifact buildArtifact = Artifact.artifact("Build");
        pipeline.addStage(buildStage(sourceArtifact, buildArtifact));

        // add deploy stage

    }

    private StageOptions sourceStage(final Artifact sourceArtifact, final GithubSource source) {
        final GitHubSourceAction githubSource = new GitHubSourceAction(GitHubSourceActionProps.builder()
                .actionName("github")
                .owner(source.getOwner())
                .repo(source.getRepo())
                .branch(source.getBranch())
                .trigger(GitHubTrigger.NONE)
                .oauthToken(source.getOauthToken())
                .output(sourceArtifact)
                .build());

        final StageOptions stage = StageOptions.builder()
                .stageName("source")
                .actions(Collections.singletonList(githubSource))
                .build();

        return stage;
    }

    private StageOptions buildStage(final Artifact source, final Artifact buildOutput) {
        final CodeBuildAction buildAction = new CodeBuildAction(CodeBuildActionProps.builder()
                .actionName("build")
                .input(source)
                .outputs(Collections.singletonList(buildOutput))
                .build());

        final StageOptions stage = StageOptions.builder()
                .stageName("build")
                .actions(Collections.singletonList(buildAction))
                .build();

        return stage;
    }
}
