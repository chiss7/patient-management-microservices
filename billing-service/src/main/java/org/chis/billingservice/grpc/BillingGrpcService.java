package org.chis.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(BillingRequest request, StreamObserver<BillingResponse> responseObserver) {
        log.info("Creating billing account for request: {}", request.toString());

        if (request.getPatientId().isBlank()) {
            throw new IllegalArgumentException("Patient ID is required");
        }
        if (request.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        // business logic
        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("123")
                .setStatus("ACTIVE")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
