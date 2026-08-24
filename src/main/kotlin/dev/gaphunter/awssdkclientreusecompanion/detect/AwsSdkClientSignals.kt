package dev.gaphunter.awssdkclientreusecompanion.detect

/**
 * AWS SDK for Java 2.x service client class names this plugin
 * recognizes -- matched by simple name only, so it works whether the
 * real AWS SDK jar is on the classpath or not. Not exhaustive: the
 * most commonly used services only.
 */
object AwsSdkClientSignals {
    val CLIENT_CLASS_NAMES = setOf(
        "S3Client",
        "S3AsyncClient",
        "DynamoDbClient",
        "DynamoDbAsyncClient",
        "DynamoDbEnhancedClient",
        "SqsClient",
        "SqsAsyncClient",
        "SnsClient",
        "SnsAsyncClient",
        "LambdaClient",
        "LambdaAsyncClient",
        "SecretsManagerClient",
        "SsmClient",
        "KmsClient",
        "SesClient",
        "SesV2Client",
        "CloudWatchClient",
        "EventBridgeClient",
    )
}
