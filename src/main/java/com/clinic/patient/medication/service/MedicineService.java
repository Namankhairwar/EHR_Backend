package com.clinic.patient.medication.service;

import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.medication.dto.DietInstructionResponse;
import com.clinic.patient.medication.dto.MedicineRequest;
import com.clinic.patient.medication.dto.MedicineResponse;
import com.clinic.patient.medication.entity.DietInstruction;
import com.clinic.patient.medication.entity.Medicine;
import com.clinic.patient.medication.repositories.DietInstructionRepository;
import com.clinic.patient.medication.repositories.MedicineRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final DietInstructionRepository dietInstructionRepository;

    // getting the medicine page
    @SuppressWarnings("unchecked")
    public Page<MedicineResponse> getAllMedicineByPatientId(long id,int number , int size,String property){
        Sort sort =  Sort.by(property).descending();
        Pageable pageable = PageRequest.of(number, size, sort);
        return (Page<MedicineResponse>)
                medicineRepository.
                        findAllByPatient_EhrIdOrderByPrescribedOn(id,pageable).
                        stream()
                        .map(t-> MAP.map(t,MedicineResponse::new)
                        )
                ;
    }


    public Medicine getMedicalRecord(long id){
        return medicineRepository.getMedicinesById(id).orElseThrow();
    }


    public Page<MedicineResponse> getAllMedicineByPatientIdActive(long id,int number , int size,String property){
        List<Medicine> allMedicines = medicineRepository.findAllByPatient_EhrId(id);
        List<Medicine> activeMedicines = allMedicines.stream()
                .filter(m -> m.getDetails() != null && m.getDetails().stream().anyMatch(d -> Boolean.TRUE.equals(d.getIsActive())))
                .collect(Collectors.toList());
        return paginateAndMap(activeMedicines, number, size, property);
    }


    public Page<MedicineResponse> getAllMedicineByPatientIdNotActive(long id,int number , int size,String property){
        List<Medicine> allMedicines = medicineRepository.findAllByPatient_EhrId(id);
        List<Medicine> inactiveMedicines = allMedicines.stream()
                .filter(m -> m.getDetails() == null || m.getDetails().stream().noneMatch(d -> Boolean.TRUE.equals(d.getIsActive())))
                .collect(Collectors.toList());
        return paginateAndMap(inactiveMedicines, number, size, property);
    }

    private Page<MedicineResponse> paginateAndMap(List<Medicine> medicines, int page, int size, String property) {
        if ("prescribedOn".equals(property)) {
            medicines.sort((m1, m2) -> {
                if (m1.getPrescribedOn() == null && m2.getPrescribedOn() == null) return 0;
                if (m1.getPrescribedOn() == null) return 1;
                if (m2.getPrescribedOn() == null) return -1;
                return m2.getPrescribedOn().compareTo(m1.getPrescribedOn());
            });
        }
        int start = Math.min(page * size, medicines.size());
        int end = Math.min(start + size, medicines.size());
        List<Medicine> pagedList = medicines.subList(start, end);
        
        List<MedicineResponse> content = pagedList.stream()
                .map(t -> MAP.map(t, MedicineResponse::new))
                .collect(Collectors.toList());
                
        Pageable pageable = PageRequest.of(page, size, Sort.by(property).descending());
        return new PageImpl<>(content, pageable, medicines.size());
    }

   public void save(Medicine medicine){
      medicineRepository.save(medicine);
   }

   public DietInstructionResponse getDietByPatient(long id){
       DietInstruction diet = dietInstructionRepository.findFirstByPatientId_EhrIdOrderByIdDesc(id)
               .orElseThrow(() -> new RuntimeException("No diet instruction found for this patient"));
       return MAP.map(diet, DietInstructionResponse::new);
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
