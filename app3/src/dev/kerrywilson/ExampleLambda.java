package dev.kerrywilson;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

public class ExampleLambda implements RequestHandler<Map<String, String>, String> {

    @Override
    public String handleRequest(final Map<String, String> eventMap, final Context context) {
        System.out.println("Received event: " + eventMap);
        return null;
    }
}