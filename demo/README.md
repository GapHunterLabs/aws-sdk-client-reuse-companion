# Demo data for screenshots

`OrderService.java` — the constructor builds `sharedClient` once (not
flagged), `uploadReceipt` builds a brand new client on every call
(flagged).

## How to get the screenshot

1. `./gradlew runIde` from `aws-sdk-client-reuse-companion`, open this
   `demo/` folder as the project.
2. Full Screen, open `OrderService.java` — a warning icon should
   appear on the `S3Client.builder().build()` call in `uploadReceipt`
   only.
3. Screenshot with both methods visible, save into
   `aws-sdk-client-reuse-companion/docs/screenshots/`. Close the
   sandbox.
