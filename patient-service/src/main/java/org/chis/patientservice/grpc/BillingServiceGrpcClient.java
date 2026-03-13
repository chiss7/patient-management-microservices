package org.chis.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.chis.patientservice.exception.BillingAccountCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BillingServiceGrpcClient {
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    public BillingServiceGrpcClient(
            @Value("${billing-service.host}") String host,
            @Value("${billing-service.grpc.port}") int port
    ) {
        log.info("Connecting to Billing Service at {}:{}", host, port);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount(String patientId, String name, String email) {
        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(name)
                .setEmail(email)
                .build();

        try {
            BillingResponse response = blockingStub.createBillingAccount(request);
            log.info("Received billing account creation response via gRPC: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Failed to create billing account for patient {}: {}", patientId, e.getMessage());
            throw new BillingAccountCreationException(
                    "Failed to create billing account for patient: " + patientId + ". Reason: " + e.getMessage());
        }
    }
}
