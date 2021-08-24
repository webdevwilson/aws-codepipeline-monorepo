package cdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(final String[] args) {
        final software.amazon.awscdk.core.App app = new software.amazon.awscdk.core.App();

        final List<ConfigurationFile.Pipeline> pipelineConfigs = new ArrayList<>();

        // create pipeline stacks
        Arrays.asList("app1", "app2", "app3").forEach(path -> {
            new PipelineStack(app, path);
            pipelineConfigs.add(
                    ConfigurationFile.Pipeline.builder()
                            .pipelineName(path)
                            .changeMatchExpressions(new String[] {
                                    path + "/*",
                                    "common/*"
                            })
                            .ignoreFiles(new String[] {
                                    "*.md",
                                    "*.pdf"
                            })
                            .branch("master")
                            .build()
            );
        });

        final ConfigurationFile file = new ConfigurationFile(pipelineConfigs);

        // create webhook stack
        new WebhookStack(app, "Webhook", file);

        app.synth();
    }
}
