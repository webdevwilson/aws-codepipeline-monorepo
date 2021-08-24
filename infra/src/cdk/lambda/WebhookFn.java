package cdk.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.kohsuke.github.GHEventPayload;

public class WebhookFn implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent> {

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
        System.out.println("request body: " + body);
        final Gson gson = new GsonBuilder().setPrettyPrinting().setExclusionStrategies(exclusionStrategy).create();
        final GHEventPayload.Push pushEvent = gson.fromJson(body, GHEventPayload.Push.class);
        System.out.println("commit id:" + pushEvent.getHead());

        final APIGatewayV2ProxyResponseEvent resp = new APIGatewayV2ProxyResponseEvent();
        resp.setStatusCode(200);
        resp.setBody("OK");
        return resp;
    }
}
