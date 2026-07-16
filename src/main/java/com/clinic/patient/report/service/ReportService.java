package com.clinic.patient.report.service;

import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.doctor.service.DoctorService;
import com.clinic.patient.report.dto.ReportRequestDto;
import com.clinic.patient.report.dto.ReportResponseDto;
import com.clinic.patient.report.entity.Report;
import com.clinic.patient.report.repositories.ReportRepository;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.repositories.UserRepository;
import com.clinic.patient.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final DoctorService doctorService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public void addReport(ReportRequestDto request) {
        Doctor doctor = doctorService.getDoctorById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        byte[] fileBytes = null;
        if (request.getFileBase64() != null && !request.getFileBase64().isEmpty()) {
            fileBytes = Base64.getDecoder().decode(request.getFileBase64());
        }

        LocalDate date = null;
        if (request.getDateTime() != null && !request.getDateTime().isEmpty()) {
            date = LocalDate.parse(request.getDateTime(), DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        }

        String doctorName = doctor.getUser().getFirstName() + " " + doctor.getUser().getLastName();

        for (Long patientId : request.getPatientIds()) {
            User patient = userRepository.findById(patientId)
                    .orElseThrow(() -> new RuntimeException("Patient not found for ID: " + patientId));

            Report report = Report.builder()
                    .patient(patient)
                    .doctor(doctor)
                    .dateTime(date)
                    .info(request.getInfo())
                    .desc(request.getDesc())
                    .conclusion(request.getConclusion())
                    .fileData(fileBytes)
                    .fileName(request.getFileName())
                    .fileType(request.getFileType())
                    .build();

            reportRepository.save(report);

            // Trigger notification
            String message = "Dr. " + doctorName + " has added a new medical report for you: " + request.getInfo();
            notificationService.createNotification(patient, message);
        }
    }

    public List<ReportResponseDto> getReportsByPatientId(long patientId) {
        return reportRepository.findAllByPatient_EhrIdOrderByDateTimeDesc(patientId).stream()
                .map(r -> {
                    ReportResponseDto dto = MAP.map(r, ReportResponseDto::new);
                    dto.setPatientId(r.getPatient().getEhrId());
                    dto.setPatientName(r.getPatient().getFirstName() + " " + r.getPatient().getLastName());
                    dto.setDoctorId(r.getDoctor().getId());
                    dto.setDoctorName(r.getDoctor().getUser().getFirstName() + " " + r.getDoctor().getUser().getLastName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Report getReportFile(long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }
}
