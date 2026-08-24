package com.acmecorp.orders;

public class OrderService {

    private final S3Client sharedClient;

    // Built once, in the constructor -- not flagged.
    OrderService() {
        this.sharedClient = S3Client.builder().build();
    }

    // Built again on every call inside a regular method -- flagged.
    void uploadReceipt(String orderId) {
        S3Client client = S3Client.builder().build();
        client.putObject(request -> request.bucket("receipts").key(orderId));
    }
}
