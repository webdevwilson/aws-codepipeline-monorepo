package cdk;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ConfigurationFileTest {

    @Test
    public void testSerialization() {
        ConfigurationFile config = ConfigurationFile.read("{\"pipelines\": []}");
        assertNotNull(config.getPipelines());
        assertEquals(0, config.getPipelines().length);

        ConfigurationFile.Pipeline p = ConfigurationFile.Pipeline.builder()
                .pipelineName("bar")
                .branch("master")
                .ignoreFiles(new String[] {"*.pdf"})
                .changeMatchExpressions(new String[]{"bar/*", "common/*"}).build();

        ConfigurationFile.Pipeline p2 = ConfigurationFile.Pipeline.builder()
                .pipelineName("foo")
                .branch("master")
                .ignoreFiles(new String[] {"*.pdf"})
                .changeMatchExpressions(new String[]{"foo/*", "common/*"}).build();
        config.setPipelines(
                new ConfigurationFile.Pipeline[]{
                        p,
                        p2
                }
        );

        config = ConfigurationFile.read(config.toJson());

        ConfigurationFile.Pipeline[] pipelines = config.getPipelines();
        assertEquals(pipelines[0].getPipelineName(), "bar");
        assertEquals(pipelines[0].getIgnoreFiles()[0], "*.pdf");
        assertEquals(pipelines[0].getChangeMatchExpressions()[0], "bar/*");
        assertEquals(pipelines[0].getChangeMatchExpressions()[1], "common/*");

        assertEquals(pipelines[1].getPipelineName(), "foo");
        assertEquals(pipelines[1].getIgnoreFiles()[0], "*.pdf");
        assertEquals(pipelines[1].getChangeMatchExpressions()[0], "foo/*");
        assertEquals(pipelines[1].getChangeMatchExpressions()[1], "common/*");
    }
}