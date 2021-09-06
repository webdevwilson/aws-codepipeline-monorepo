package cdk;

import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.constructs.Construct;

public class EcsStack extends Stack {

    public EcsStack(@Nullable Construct scope, @Nullable String id, @Nullable StackProps props) {
        super(scope, id, props);

        // configure ecs stack
    }
}
