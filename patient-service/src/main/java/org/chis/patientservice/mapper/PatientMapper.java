package org.chis.patientservice.mapper;

import org.chis.patientservice.dto.PatientRequestDTO;
import org.chis.patientservice.dto.PatientResponseDTO;
import org.chis.patientservice.model.Patient;

import java.time.LocalDate;

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

    public static Patient toModel(PatientRequestDTO patientRequestDTO) {
        if (patientRequestDTO == null) return null;

        return Patient.builder()
                .name(patientRequestDTO.name())
                .email(patientRequestDTO.email())
                .address(patientRequestDTO.address())
                .dateOfBirth(LocalDate.parse(patientRequestDTO.dateOfBirth()))
                .registeredDate(LocalDate.parse(patientRequestDTO.registeredDate()))
                .build();
    }
}
