# AWS SDK Client Reuse Companion

Gutter warning icon on an AWS SDK for Java 2.x service client
(`S3Client`, `DynamoDbClient`, `SqsClient`, `SnsClient`,
`LambdaClient`, and similar) built via
`XxxClient.builder().build()` inside a regular method body — AWS's
own documentation states service clients are thread-safe and meant to
be created once and reused; a client built inside a method means a
brand new client (and its own connection pool) gets created on every
call, a real, documented performance footgun.

## Why it exists

Building a client inside a request handler or a Lambda function body
is an easy, natural-looking mistake — the code compiles fine and works
correctly, it's just needlessly slow (every call pays connection-setup
cost) and wastes resources. AWS documents the singleton-client pattern
explicitly, but nothing in the IDE flags a client built the wrong way
today.

## Why built this way

- **100% static text/PSI analysis** — matches the client class name by
  simple text, so it works whether the real AWS SDK jar is on the
  classpath or not. Java and Kotlin.

## v0.1 scope — stated honestly, not exhaustively

Only flags the direct `.builder().build()` chain — a builder assigned
to an intermediate variable before `.build()` is called isn't
specially traced. Never flags a build call inside a constructor or a
field/property initializer (legitimate "create once" locations).

## Usage

Open any Java/Kotlin file using an AWS SDK v2 service client. A client
built inside a regular method shows a warning icon.

## Enterprise / Team Licensing

Need enterprise features, custom rules, or team licensing? Contact us at
**gaphunterlabs@gmail.com**.

## Development

```
./gradlew test           # unit tests
./gradlew buildPlugin    # generates build/distributions/*.zip
./gradlew verifyPlugin   # checks compatibility against real IDEs
```

## License

Apache-2.0. See `LICENSE`.
