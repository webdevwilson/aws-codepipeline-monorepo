package cdk.trigger;

import cdk.ConfigurationFile;
import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.LambdaRestApiProps;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.RetentionDays;

import software.amazon.awscdk.services.s3.assets.Asset;
import software.amazon.awscdk.services.s3.assets.AssetProps;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TriggerStack extends Stack {

    public TriggerStack(final Construct scope, final String id, final ConfigurationFile configFile) {
        super(scope, id, null);

        // Write config to local
        final String configFilePath = String.join(File.pathSeparator,
                Arrays.asList(System.getProperty("java.io.tmpdir"), "pipelineConfig.json")
        );
        configFile.writeToFile(configFilePath);

        // Upload file to the bucket
        final Asset configFileAsset = new Asset(this, "configFile", AssetProps.builder()
                .path(configFilePath)
                .build()
        );

        final LayerVersion layer = new LayerVersion(this, "fnLayer", LayerVersionProps.builder()
                .code(Code.fromAsset("target/deps"))
                .compatibleRuntimes(Arrays.asList(Runtime.JAVA_8))
                .build()
        );

        // The code that defines your stack goes here
        final Map<String, String> env = new HashMap<>();
        env.put("CONFIG_FILE_BUCKET", configFileAsset.getS3BucketName());
        env.put("CONFIG_FILE_KEY", configFileAsset.getS3ObjectKey());

        final Function fn = new Function(this, "fn", FunctionProps.builder()
                .functionName(id)
                .runtime(Runtime.JAVA_8)
                .code(Code.fromAsset("target/lambda-mvn-cdk-infra-0.1.jar"))
                .handler("cdk.trigger.lambda.WebhookFn")
                .layers(Arrays.asList(layer))
                .memorySize(1024)
                .environment(env)
                .timeout(Duration.seconds(30))
                .logRetention(RetentionDays.ONE_WEEK)
                .build());

        // Allow lambda to read from the bucket
        configFileAsset.grantRead(fn.getRole());

        new LambdaRestApi(this, id + "restApi", LambdaRestApiProps.builder()
                .handler(fn)
                .build()
        );


    }

}