package com.clinic.patient.report.controller;

import com.clinic.patient.report.dto.ReportAccessRequestDto;
import com.clinic.patient.report.dto.ReportAccessRequestResponseDto;
import com.clinic.patient.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports/access")
@RequiredArgsConstructor
public class ReportAccessController {

    private final ReportService reportService;

    @PostMapping("/request")
    public ResponseEntity<Void> requestAccess(@RequestBody ReportAccessRequestDto dto) {
        reportService.requestAccess(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/approve/{requestId}")
    public ResponseEntity<Void> approveAccess(@PathVariable("requestId") Long requestId) {
        reportService.approveAccess(requestId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reject/{requestId}")
    public ResponseEntity<Void> rejectAccess(@PathVariable("requestId") Long requestId) {
        reportService.rejectAccess(requestId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ReportAccessRequestResponseDto>> getRequestsForPatient(@PathVariable("patientId") String patientId) {
        return ResponseEntity.ok(reportService.getRequestsByPatient(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ReportAccessRequestResponseDto>> getRequestsForDoctor(@PathVariable("doctorId") String doctorId) {
        return ResponseEntity.ok(reportService.getRequestsByDoctor(doctorId));
    }
}
