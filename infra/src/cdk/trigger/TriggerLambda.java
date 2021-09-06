package cdk.trigger;

import cdk.ConfigurationFile;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.kohsuke.github.GHEventPayload;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

public class TriggerLambda implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent> {

    public static final String ENV_CONFIG_FILE_BUCKET = "CONFIG_FILE_BUCKET";

    public static final String ENV_CONFIG_FILE_KEY = "CONFIG_FILE_KEY";

    // do not deserialize nodeId, it causes issues
    private final ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(final FieldAttributes fieldAttributes) {
            return fieldAttributes.getName() == "nodeId";
        }

        @Override
        public boolean shouldSkipClass(final Class<?> aClass) {
            return false;
        }
    };


    @Override
    public APIGatewayV2ProxyResponseEvent handleRequest(final APIGatewayV2ProxyRequestEvent evt, final Context context) {
        System.out.println("Received event: " + evt);
        final String body = evt.getBody();
        final GHEventPayload.Push pushEvent = parseBody(body);

        // get the configuration from the s3 bucket
        final String configFileBucket = System.getenv(ENV_CONFIG_FILE_BUCKET);
        final String configFileKey = System.getenv(ENV_CONFIG_FILE_KEY);
        final ConfigurationFile config = getConfigurationFile(configFileBucket, configFileKey);

        // loop through and get paths that were changed
        for(final GHEventPayload.Push.PushCommit commit : pushEvent.getCommits()) {
            for(final String path : commit.getModified()) {
                System.out.println("\t" + path);
            }
        }

        final APIGatewayV2ProxyResponseEvent resp = new APIGatewayV2ProxyResponseEvent();
        resp.setStatusCode(200);
        resp.setBody("OK");
        return resp;
    }

    GHEventPayload.Push parseBody(final String body) {
        final Gson gson = new GsonBuilder().setPrettyPrinting().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return fieldAttributes.getName() == "nodeId";
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).create();
        return gson.fromJson(body, GHEventPayload.Push.class);
    }

    ConfigurationFile getConfigurationFile(final String bucket, final String key) {
        final S3Client s3 = S3Client.create();
        final ResponseBytes<GetObjectResponse> response = s3.getObjectAsBytes(GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());
        final String json = "";

        return ConfigurationFile.read(json);
    }
}