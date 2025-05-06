package org.chis.patientservice.service;

import lombok.RequiredArgsConstructor;
import org.chis.patientservice.dto.PatientResponseDTO;
import org.chis.patientservice.mapper.PatientMapper;
import org.chis.patientservice.model.Patient;
import org.chis.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(PatientMapper::toDTO)
                .toList();
    }
}
