package cdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@lombok.Data
@lombok.NoArgsConstructor
public class ConfigurationFile {

    private Pipeline[] pipelines;

    ConfigurationFile(final List<Pipeline> pipelines) {
        this.pipelines = pipelines.toArray(new Pipeline[pipelines.size()]);
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Builder
    public static class Pipeline {
        private String branch;

        private String[] changeMatchExpressions;

        private String[] ignoreFiles;

        private String pipelineName;
    }

    @lombok.SneakyThrows
    public void writeToFile(final String filePath) {
        new ObjectMapper().writeValue(new File(filePath), this);
    }

    @lombok.SneakyThrows
    public String toJson() {
        return new ObjectMapper().writeValueAsString(this);
    }

    @lombok.SneakyThrows
    public static ConfigurationFile read(final String json) {
        return new ObjectMapper().readValue(json, ConfigurationFile.class);
    }
}
