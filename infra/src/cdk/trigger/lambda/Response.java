package cdk.trigger.lambda;

import java.util.List;

@lombok.Builder
@lombok.Data
public class Response {

    // list of pipeline names to update and trigger
    private List<String> pipelines;
}
