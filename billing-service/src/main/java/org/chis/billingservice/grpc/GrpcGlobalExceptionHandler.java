package org.chis.billingservice.grpc;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcAdvice
public class GrpcGlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GrpcGlobalExceptionHandler.class);

    @GrpcExceptionHandler(IllegalArgumentException.class)
    public Status handleInvalidArgument(IllegalArgumentException e) {
        log.warn("Invalid argument: {}", e.getMessage());
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e);
    }

    @GrpcExceptionHandler(Exception.class)
    public Status handleGenericException(Exception e) {
        log.error("Unexpected error: {}", e.getMessage(), e);
        return Status.INTERNAL.withDescription("An unexpected error occurred").withCause(e);
    }
}
