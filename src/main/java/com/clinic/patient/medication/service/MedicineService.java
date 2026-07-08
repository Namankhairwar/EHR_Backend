package com.clinic.patient.medication.service;

import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.medication.dto.MedicineRequest;
import com.clinic.patient.medication.dto.MedicineResponse;
import com.clinic.patient.medication.entity.Medicine;
import com.clinic.patient.medication.repositories.MedicineRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MedicineService {

    private MedicineRepository medicineRepository;


    // getting the medicine page
    @SuppressWarnings("unchecked")
    public Page<MedicineResponse> getAllMedicineByPatientId(long id,int number , int size,String property){
        Sort sort =  Sort.by(property).descending();
        Pageable pageable = PageRequest.of(number, size, sort);
        return (Page<MedicineResponse>)
                medicineRepository.
                        findAllByPatient_IdOrderByPrescribedOn(id,pageable).
                        stream()
                        .map(t-> MAP.map(t,MedicineResponse::new)
                        )
                ;
    }


    public Medicine getMedicalRecord(long id){
        return medicineRepository.getMedicinesById(id).orElseThrow();
    }


    @SuppressWarnings("unchecked")
    public Page<MedicineResponse> getAllMedicineByPatientIdActive(long id,int number , int size,String property){
        Sort sort =  Sort.by(property).descending();
        Pageable pageable = PageRequest.of(number, size, sort);
        return (Page<MedicineResponse>)
                medicineRepository.
                        findAllByPatient_IdAndDetailsIsActiveTrue
                                (id,true,pageable).
                        stream().
                        map(t->MAP.map(t, MedicineResponse::new));
    }


    @SuppressWarnings("unchecked")
    public Page<MedicineResponse> getAllMedicineByPatientIdNotActive(long id,int number , int size,String property){
        Sort sort =  Sort.by(property).descending();
        Pageable pageable = PageRequest.of(number, size, sort);
        return (Page<MedicineResponse>)
                medicineRepository.
                        findAllByPatient_IdAndDetailsIsActiveTrue
                                (id,false,pageable).
                        stream().
                        map(t->MAP.map(t, MedicineResponse::new));
    }

   public void save(Medicine medicine){
      medicineRepository.save(medicine);
   }

   public MedicineResponse getDietByPatient(long id){
        return MAP.
                map(medicineRepository.getDietByPatient_id(id),MedicineResponse::new);
   }
    // update flexible use for diet and medicine request as well use for other updates
   public void updateByPatient(MedicineRequest request, long id){
       Optional<Medicine> medicinesByMedicineId =
               medicineRepository.
                       getMedicinesById(id);
       Medicine medicine=medicinesByMedicineId.orElseThrow();
        MAP.copyInTheObject(request,medicine);
        medicineRepository.save(medicine);
   }

}
