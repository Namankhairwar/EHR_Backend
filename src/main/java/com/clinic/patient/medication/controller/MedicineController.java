package com.clinic.patient.medication.controller;

import com.clinic.patient.medication.dto.MedicineRequest;
import com.clinic.patient.medication.dto.MedicineResponse;
import com.clinic.patient.medication.entity.Medicine;
import com.clinic.patient.medication.service.MedicineService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@AllArgsConstructor
public class MedicineController {

    private MedicineService medicineService;
    // from doctor patient medicine id
    // create update
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN') or @accessGuard.isSelf(#patient_id)")
    @GetMapping("patients/{patient_id}/medical-records")
    public ResponseEntity<?> getAllMedicineByPatient(@PathVariable("patient_id") String patient_id,
                                                     @RequestParam(defaultValue = "0")int page_no ,
                                                     @RequestParam(defaultValue = "5") int size,
                                                     @RequestParam(defaultValue = "prescribedOn") String property
    ){
        Page<MedicineResponse> allMedicineByPatientId =
                medicineService.getAllMedicineByPatientId(patient_id, page_no, size,property);
     return  new ResponseEntity<>(allMedicineByPatientId, HttpStatusCode.valueOf(200));
     }

    @GetMapping("patients/medical-records/{recordId}")
    public ResponseEntity<Medicine> getMedicinalInformationByid(@PathVariable("recordId") long id){
        return ResponseEntity.ok(medicineService.getMedicalRecord(id));
    }


    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN') or @accessGuard.isSelf(#id)")
    @GetMapping("patients/{patientId}/medications")
    public ResponseEntity<?> getMedicinalInformationByidGoingOn(@PathVariable("patientId") String id,
                                                       @RequestParam(defaultValue = "0")int page_no ,
                                                       @RequestParam(defaultValue = "5") int size,
                                                       @RequestParam(defaultValue = "prescribedOn") String property){
        return new ResponseEntity<>
                (medicineService.getAllMedicineByPatientIdActive(id,page_no,size,property)
                        ,HttpStatusCode.valueOf(200));
    }

    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN') or @accessGuard.isSelf(#id)")
    @GetMapping("patients/{patientId}/medication/history")
    public ResponseEntity<?> getMedicinalInformationByidGoingOff(@PathVariable("patientId") String id,
                                                                @RequestParam(defaultValue = "0")int page_no ,
                                                                @RequestParam(defaultValue = "5") int size,
                                                                @RequestParam(defaultValue = "prescribedOn") String property){
        return new ResponseEntity<>
                (medicineService.getAllMedicineByPatientIdNotActive(id,page_no,size,property)
                        ,HttpStatusCode.valueOf(200));
    }

    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    @PostMapping("patients/medications")
    public ResponseEntity<Void> addMedication(@RequestBody MedicineRequest request){
        medicineService.save(request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN') or @accessGuard.isSelf(#id)")
    @GetMapping("patients/{patientId}/diet-instructions")
    public ResponseEntity<?> getDietByPatient(@PathVariable("patientId") String id){
        return ResponseEntity.ok(medicineService.getDietByPatient(id));
    }


    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    @PutMapping("patients/{medication_id}/medications")
    public ResponseEntity<Void> updateDetails(@RequestBody MedicineRequest medicineRequest , @PathVariable("medication_id") long id){
        medicineService.updateByPatient(medicineRequest,id);
        return ResponseEntity.ok().build();
    }


}
