package cdk.trigger;

import java.util.List;

@lombok.Builder
@lombok.Data
public class TriggerLambdaResponse {

    // list of pipeline names to update and trigger
    private List<String> pipelines;
}
