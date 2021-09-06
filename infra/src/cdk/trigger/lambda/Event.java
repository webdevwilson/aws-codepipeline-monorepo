package cdk.trigger.lambda;

import java.util.List;

/**
 * TriggerLambdaEvent is passed to the lambda on a commit
 */
@lombok.Data
@lombok.Builder
public class Event {

    // paths modified in the last commit
    private List<String> modifiedPaths;

    // commit id of the commit
    private String commitId;

    private String branch;
}
