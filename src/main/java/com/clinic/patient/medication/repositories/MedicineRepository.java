package com.clinic.patient.medication.repositories;

import com.clinic.patient.medication.entity.DietInstruction;
import com.clinic.patient.medication.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine,Long> {

    Page<Medicine> findAllByPatient_EhrId(Long patientId, Pageable pageable);

    Optional<Medicine> getMedicinesById(long id);

    Page<Medicine> findAllByPatient_EhrIdOrderByPrescribedOn(long id, Pageable pageable);

    java.util.List<Medicine> findAllByPatient_EhrId(Long patientId);

    @Transactional
  default  Optional<Medicine> saveMedicine(Medicine entity){
      Medicine save = save(entity);
      return Optional.of(save);
  }
}
