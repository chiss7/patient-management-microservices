package org.chis.patientservice.service;

import lombok.RequiredArgsConstructor;
import org.chis.patientservice.dto.PatientRequestDTO;
import org.chis.patientservice.dto.PatientResponseDTO;
import org.chis.patientservice.exception.EmailAlreadyExistsException;
import org.chis.patientservice.exception.PatientNotFoundException;
import org.chis.patientservice.grpc.BillingServiceGrpcClient;
import org.chis.patientservice.kafka.KafkaProducer;
import org.chis.patientservice.kafka.PatientEventType;
import org.chis.patientservice.mapper.PatientMapper;
import org.chis.patientservice.model.Patient;
import org.chis.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(PatientMapper::toDTO)
                .toList();
    }

    @Transactional
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.email())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists: " + patientRequestDTO.email());
        }
        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(), patient.getName(), patient.getEmail());
        kafkaProducer.sendEvent(patient, PatientEventType.CREATED);
        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with Id: " + id));
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.email(), id)) {
            throw new EmailAlreadyExistsException("A patient with this email already exists: " + patientRequestDTO.email());
        }

        patient.setName(patientRequestDTO.name());
        patient.setAddress(patientRequestDTO.address());
        patient.setEmail(patientRequestDTO.email());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.dateOfBirth()));
        Patient updatedPatient = patientRepository.save(patient);
        kafkaProducer.sendEvent(updatedPatient, PatientEventType.UPDATED);
        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID id) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with Id: " + id));
        patientRepository.delete(patient);
        kafkaProducer.sendEvent(patient, PatientEventType.DELETED);
    }
}
