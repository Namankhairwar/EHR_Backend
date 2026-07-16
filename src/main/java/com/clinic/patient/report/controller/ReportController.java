package com.clinic.patient.report.controller;

import com.clinic.patient.report.dto.ReportRequestDto;
import com.clinic.patient.report.dto.ReportResponseDto;
import com.clinic.patient.report.entity.Report;
import com.clinic.patient.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("")
    public ResponseEntity<?> addReport(@RequestBody ReportRequestDto dto) {
        reportService.addReport(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("patient/{patientId}")
    public ResponseEntity<List<ReportResponseDto>> getReportsByPatient(@PathVariable("patientId") long patientId) {
        return ResponseEntity.ok(reportService.getReportsByPatientId(patientId));
    }

    @GetMapping("{id}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable("id") long id) {
        Report report = reportService.getReportFile(id);
        if (report.getFileData() == null) {
            return ResponseEntity.noContent().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (report.getFileType() != null) {
            try {
                mediaType = MediaType.parseMediaType(report.getFileType());
            } catch (Exception ignored) {}
        }

        String fileName = report.getFileName() != null ? report.getFileName() : "report_" + id;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(report.getFileData());
    }
}
