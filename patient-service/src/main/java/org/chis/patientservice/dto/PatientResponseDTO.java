package org.chis.patientservice.dto;

public record PatientResponseDTO (
    String id,
    String name,
    String email,
    String address,
    String dateOfBirth
) {

}