package cdk;

import software.amazon.awscdk.core.SecretValue;
import software.amazon.awscdk.services.codepipeline.Artifact;
import software.amazon.awscdk.services.codepipeline.actions.GitHubSourceAction;
import software.amazon.awscdk.services.codepipeline.actions.GitHubSourceActionProps;
import software.amazon.awscdk.services.codepipeline.actions.GitHubTrigger;

@lombok.Builder
@lombok.Data
public class GithubSource {

    private final String owner;

    private final String repo;

    private final String branch;

    private final SecretValue oauthToken;

    public GitHubSourceAction getGitHubSourceAction(final Artifact artifact) {
        return this.getGitHubSourceAction(artifact, GitHubTrigger.WEBHOOK);
    }

    public GitHubSourceAction getGitHubSourceAction(final Artifact artifact, final GitHubTrigger trigger) {
        return new GitHubSourceAction(GitHubSourceActionProps.builder()
                .actionName("github")
                .branch(branch)
                .owner(owner)
                .repo(repo)
                .trigger(trigger)
                .output(artifact)
                .oauthToken(oauthToken)
                .build());
    }
}
