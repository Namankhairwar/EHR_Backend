package com.clinic.patient.medication.controller;

import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.medication.dto.DietInstructionRequest;
import com.clinic.patient.medication.entity.DietInstruction;
import com.clinic.patient.medication.repositories.DietInstructionRepository;
import com.clinic.patient.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
@RequestMapping("api")
@AllArgsConstructor
public class DietInstructionController {
    private final DietInstructionRepository dietInstructionRepository;

    @PostMapping("patient/{patient_id}/diet_instruction")
    public ResponseEntity<?> addDietInstruction(@PathVariable("patient_id") long patient_id
            , @RequestBody DietInstructionRequest dietInstructionRequest){
        DietInstruction dietInstruction = MAP.map(dietInstructionRequest , DietInstruction::new);
        User user = new User();
        user.setEhrId(patient_id);
        dietInstruction.setPatientId(user);
        User user2 = new User();
        user2.setEhrId(dietInstructionRequest.getPrescribeBy());
        Doctor doctor = new Doctor();
        doctor.setId(dietInstructionRequest.getPrescribeBy());
        doctor.setUser(user2);
        dietInstruction.setPrescribeBy(doctor);

        dietInstructionRepository.save(dietInstruction);
        return ResponseEntity.ok().build();
    }


    @GetMapping("patient/{dietId}/diet_instruction")
    public ResponseEntity<?> getDietInstruction(@PathVariable("dietId") long dietId){
        Optional<DietInstruction> dietInstruction =dietInstructionRepository.findById(dietId);
         DietInstruction d = dietInstruction.orElseThrow(()-> new RuntimeException("This diet does not exist"));
        return ResponseEntity.ok(d);
    }

    @PutMapping("patient/{patient_id}/{dietId}/diet_instruction")
    public ResponseEntity<?> updateDietInstruction(@PathVariable("patient_id") long patient_id,
            @PathVariable("dietId") long dietId
            , @RequestBody DietInstructionRequest dietInstructionRequest){
        Optional<DietInstruction> dietInstruction =dietInstructionRepository.findById(dietId);
        DietInstruction diet = dietInstruction.orElseThrow(()-> new RuntimeException("This diet does not exist"));
        MAP.copyInTheObject(dietInstructionRequest,diet);
        dietInstructionRepository.save(diet);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("patient/{patient_id}/diet_instruction")
    public ResponseEntity<?> deleteDietInstruction(@PathVariable("patient_id") long patient_id,
                                                   @RequestParam("dietId") long dietId){
        Optional<DietInstruction> dietInstruction =dietInstructionRepository.findById(dietId);
        DietInstruction diet = dietInstruction.orElseThrow(()-> new RuntimeException("This diet does not exist"));
        dietInstructionRepository.delete(diet);
        return ResponseEntity.ok().build();
    }
}
