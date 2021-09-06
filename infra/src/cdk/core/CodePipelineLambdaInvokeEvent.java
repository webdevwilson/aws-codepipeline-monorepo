package cdk.core;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;

import java.util.Map;

/**
 * Event represents the event passed to lambda from codepipeline. Documented at
 * https://docs.aws.amazon.com/codepipeline/latest/userguide/actions-invoke-lambda-function.html#actions-invoke-lambda-function-json-event-example
 *
 * Lazily fetching values from it to avoid the issue where the lambda fails and we cannot catch the error b/c we
 * need the job id to report back to codepipeline.
 */
public class CodePipelineLambdaInvokeEvent {

    private final Map<String, Object> job;

    public CodePipelineLambdaInvokeEvent(final Map<String, Object> event) {
        this.job = (Map<String, Object>)event.get("CodePipeline.job");
    }

    public String id() {
        return this.job.get("id").toString();
    }

    private Map<String, Object> data() {
        return (Map<String, Object>)job.get("data");
    }

    public AwsCredentials artifactCredentials() {
        final Map<String, String> creds = (Map<String, String>)data().get("artifactCredentials");
        return AwsSessionCredentials.create(
                creds.get("accessKeyId"),
                creds.get("secretAccessKey"),
                creds.get("sessionToken")
        );
    }
}
