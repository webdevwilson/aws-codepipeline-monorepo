package cdk.trigger.lambda;

import cdk.core.CodePipelineLambdaInvokeEvent;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.services.codepipeline.CodePipelineClient;
import software.amazon.awssdk.services.codepipeline.model.FailureDetails;
import software.amazon.awssdk.services.codepipeline.model.PutJobFailureResultRequest;
import software.amazon.awssdk.services.codepipeline.model.PutJobSuccessResultRequest;

import java.util.Map;

public class Fn implements RequestHandler<Map<String, Object>, Object> {

    @Override
    public Object handleRequest(final Map<String, Object> evt, final Context context) {
        System.out.println("Received event: " + evt);

        // get job information from event
        final CodePipelineClient codePipelineClient = CodePipelineClient.create();
        final CodePipelineLambdaInvokeEvent job = new CodePipelineLambdaInvokeEvent(evt);

        try {

            // get artifact from s3

            // parse artifact

            // write back result artifact

            System.out.printf("[INFO] Success");
            codePipelineClient.putJobSuccessResult(PutJobSuccessResultRequest.builder()
                    .jobId(job.id())
                    .build());
        } catch (final Exception e) {
            System.out.println("[ERROR] " + e);
            codePipelineClient.putJobFailureResult(PutJobFailureResultRequest.builder()
                            .jobId(job.id())
                            .failureDetails(FailureDetails.builder()
                                    .message(e.getMessage())
                                    .build())
                    .build());
        }
        return null;
    }
}