package com.clinic.patient.medication.repositories;

import com.clinic.patient.medication.entity.DietInstruction;
import com.clinic.patient.medication.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine,Long> {

    Page<Medicine> findAllByPatient_Id(Long patientId, Pageable pageable);

    Optional<Medicine> getMedicinesById(long id);

    Page<Medicine> findAllByPatient_IdOrderByPrescribedOn(long id, Pageable pageable);

    Page<Medicine> findAllByPatient_IdAndDetailsIsActiveTrue(Long patientId, Boolean detailsIsActive, Pageable pageable);




  default  Optional<Medicine> saveMedicine(Medicine entity){
      Medicine save = save(entity);
      return Optional.of(save);
  }

    DietInstruction getDietByPatient_id(long id);
}
