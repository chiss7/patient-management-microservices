package org.chis.patientservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.chis.patientservice.dto.PatientRequestDTO;
import org.chis.patientservice.dto.PatientResponseDTO;
import org.chis.patientservice.dto.validators.CreatePatientValidationGroup;
import org.chis.patientservice.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patient", description = "API for managing Patients")
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    @Operation(summary = "Get All Patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        return ResponseEntity.ok().body(patientService.getPatients());
    }

    @PostMapping
    @Operation(summary = "Create a new Patient")
    public ResponseEntity<PatientResponseDTO> createPatient(@RequestBody @Validated({Default.class, CreatePatientValidationGroup.class}) PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok(patientService.createPatient(patientRequestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing Patient")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id, @RequestBody @Validated({Default.class}) PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientRequestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing Patient")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
