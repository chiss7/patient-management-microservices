package org.chis.patientservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chis.patientservice.model.Patient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEvent(Patient patient, PatientEventType eventType) {
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType(eventType.name())
                .build();

        try {
            kafkaTemplate.send("patient", patient.getId().toString(), event.toByteArray());
            log.info("Sent Kafka event: [{}] for patient: {}", eventType, patient.getId());
        } catch (Exception e) {
            log.error("Error sending Kafka event: [{}] for patient: {} - {}", eventType, patient.getId(), e.getMessage());
        }
    }
}
