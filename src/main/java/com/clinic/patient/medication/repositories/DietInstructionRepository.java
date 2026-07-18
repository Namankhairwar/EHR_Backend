package com.clinic.patient.medication.repositories;

import com.clinic.patient.medication.entity.DietInstruction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DietInstructionRepository extends JpaRepository<DietInstruction,Long> {
    Optional<DietInstruction> findFirstByPatientId_EhrIdOrderByIdDesc(String patientId);
}

