package org.chis.patientservice.service;

import lombok.RequiredArgsConstructor;
import org.chis.patientservice.dto.PatientRequestDTO;
import org.chis.patientservice.dto.PatientResponseDTO;
import org.chis.patientservice.exception.EmailAlreadyExistsException;
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

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.email())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists: " + patientRequestDTO.email());
        }
        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDTO(patient);
    }
}
