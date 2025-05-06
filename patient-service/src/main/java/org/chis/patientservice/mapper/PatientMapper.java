package org.chis.patientservice.mapper;

import org.chis.patientservice.dto.PatientResponseDTO;
import org.chis.patientservice.model.Patient;

public class PatientMapper {
    public static PatientResponseDTO toDTO(Patient patient) {
        return new PatientResponseDTO(
                patient.getId().toString(),
                patient.getName(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getDateOfBirth().toString()
        );
    }
}
