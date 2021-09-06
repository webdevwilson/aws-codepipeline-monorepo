package cdk.trigger.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Arrays;

public class Fn implements RequestHandler<Event, Response> {

    @Override
    public Response handleRequest(final Event evt, final Context context) {
        System.out.println("Received event: " + evt);

        return Response.builder().pipelines(Arrays.asList("app1", "app2")).build();
    }
}