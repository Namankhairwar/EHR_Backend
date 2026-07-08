package com.clinic.patient.medication.controller;

import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.medication.dto.MedicineRequest;
import com.clinic.patient.medication.dto.MedicineResponse;
import com.clinic.patient.medication.entity.Medicine;
import com.clinic.patient.medication.service.MedicineService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("api")
@AllArgsConstructor
public class MedicineController {

    private MedicineService medicineService;
    // from doctor patient medicine id
    // create update
    @GetMapping("patients/{patient_id}/medical-records")
    public ResponseEntity<?> getAllMedicineByPatient(@PathVariable("patient_id") long patient_id,
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


    @GetMapping("patients/{patientId}/medications")
    public ResponseEntity<?> getMedicinalInformationByidGoingOn(@PathVariable("patientId") long id,
                                                       @RequestParam(defaultValue = "0")int page_no ,
                                                       @RequestParam(defaultValue = "5") int size,
                                                       @RequestParam(defaultValue = "prescribedOn") String property){
        return new ResponseEntity<>
                (medicineService.getAllMedicineByPatientIdActive(id,page_no,size,property)
                        ,HttpStatusCode.valueOf(200));
    }

    @GetMapping("patients/{patientId}/medication/history")
    public ResponseEntity<?> getMedicinalInformationByidGoingOff(@PathVariable("patientId") long id,
                                                                @RequestParam(defaultValue = "0")int page_no ,
                                                                @RequestParam(defaultValue = "5") int size,
                                                                @RequestParam(defaultValue = "prescribedOn") String property){
        return new ResponseEntity<>
                (medicineService.getAllMedicineByPatientIdNotActive(id,page_no,size,property)
                        ,HttpStatusCode.valueOf(200));
    }

    @PostMapping("patients/medications")
    public ResponseEntity<Void> addMedication( MedicineRequest request){
        medicineService
                .save(MAP.map(request,Medicine::new));
        return ResponseEntity.ok().build();
    }

    @GetMapping("patients/{patientId}/diet-instructions")
    public ResponseEntity<?> getDietByPatient(@PathVariable("patientId") long id){
        return ResponseEntity.ok(medicineService.getDietByPatient(id));
    }


    @PutMapping("patients/{medication_id}/medications")
    public ResponseEntity<Void> updateDetails(@RequestBody MedicineRequest medicineRequest , @PathVariable("medication_id") long id){
        medicineService.updateByPatient(medicineRequest,id);
        return ResponseEntity.ok().build();
    }


}
