package cdk;

import software.amazon.awscdk.core.SecretValue;

@lombok.Builder
@lombok.Data
public class GithubSource {

    private final String owner;

    private final String repo;

    private final String branch;

    private final SecretValue oauthToken;
}
