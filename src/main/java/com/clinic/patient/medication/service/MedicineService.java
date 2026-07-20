package com.clinic.patient.medication.service;

import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.doctor.repositories.DoctorRepository;
import com.clinic.patient.medication.dto.DietInstructionResponse;
import com.clinic.patient.medication.dto.MedicineRequest;
import com.clinic.patient.medication.dto.MedicineResponse;
import com.clinic.patient.medication.entity.DietInstruction;
import com.clinic.patient.medication.entity.Medicine;
import com.clinic.patient.medication.repositories.DietInstructionRepository;
import com.clinic.patient.medication.repositories.MedicineRepository;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final DietInstructionRepository dietInstructionRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    // getting the medicine page
    public Page<MedicineResponse> getAllMedicineByPatientId(String id,int number , int size,String property){
        Pageable pageable = PageRequest.of(number, size, Sort.by(property).descending());
        return medicineRepository.findAllByPatient_EhrIdOrderByPrescribedOn(id, pageable)
                .map(t -> MAP.map(t, MedicineResponse::new));
    }


    public Medicine getMedicalRecord(long id){
        return medicineRepository.getMedicinesById(id).orElseThrow();
    }


    public Page<MedicineResponse> getAllMedicineByPatientIdActive(String id,int number , int size,String property){
        List<Medicine> allMedicines = medicineRepository.findAllByPatient_EhrId(id);
        List<Medicine> activeMedicines = allMedicines.stream()
                .filter(m -> m.getDetails() != null && m.getDetails().stream().anyMatch(d -> Boolean.TRUE.equals(d.getIsActive())))
                .collect(Collectors.toList());
        return paginateAndMap(activeMedicines, number, size, property);
    }


    public Page<MedicineResponse> getAllMedicineByPatientIdNotActive(String id,int number , int size,String property){
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

   public void save(MedicineRequest request){
       Medicine medicine = new Medicine();
       if (request.getPrescribedOn() != null && !request.getPrescribedOn().isEmpty()) {
           medicine.setPrescribedOn(LocalDate.parse(request.getPrescribedOn()));
       } else {
           medicine.setPrescribedOn(LocalDate.now());
       }
       medicine.setDetails(request.getDetails());
       
       if (request.getPrescribedBy() != null) {
           Doctor doctor = doctorRepository.findById(request.getPrescribedBy())
                   .orElseThrow(() -> new RuntimeException("Doctor not found"));
           medicine.setPrescribedBy(doctor);
       }
       
       if (request.getPatient() != null) {
           User patient = userRepository.findById(request.getPatient())
                   .orElseThrow(() -> new RuntimeException("Patient not found"));
           medicine.setPatient(patient);
       }
       
       if (request.getDiet() != null) {
           DietInstruction diet = request.getDiet();
           diet.setPatientId(medicine.getPatient());
           diet.setPrescribeBy(medicine.getPrescribedBy());
           medicine.setDiet(diet);
       }
       
       medicineRepository.save(medicine);
   }

   public DietInstructionResponse getDietByPatient(String id){
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
