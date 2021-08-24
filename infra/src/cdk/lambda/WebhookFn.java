package cdk.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.kohsuke.github.GHCommit;

public class WebhookFn implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent> {

    @Override
    public APIGatewayV2ProxyResponseEvent handleRequest(final APIGatewayV2ProxyRequestEvent evt, final Context context) {
        System.out.println("Received event: " + evt);
        final String body = evt.getBody();
        System.out.println("request body: " + body);
//        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        final GHCommit commit = gson.fromJson(body, GHCommit.class);
//        System.out.println("commit id:" + commit.getSHA1());
//
        final APIGatewayV2ProxyResponseEvent resp = new APIGatewayV2ProxyResponseEvent();
        resp.setStatusCode(200);
        resp.setBody("OK");
        return resp;
    }
}
