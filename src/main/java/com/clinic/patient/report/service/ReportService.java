package com.clinic.patient.report.service;

import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.applicationCommonFeature.state.Role;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.doctor.service.DoctorService;
import com.clinic.patient.notification.service.NotificationService;
import com.clinic.patient.report.dto.ReportAccessRequestDto;
import com.clinic.patient.report.dto.ReportAccessRequestResponseDto;
import com.clinic.patient.report.dto.ReportRequestDto;
import com.clinic.patient.report.dto.ReportResponseDto;
import com.clinic.patient.report.entity.Report;
import com.clinic.patient.report.entity.ReportAccessRequest;
import com.clinic.patient.report.repositories.ReportAccessRequestRepository;
import com.clinic.patient.report.repositories.ReportRepository;
import com.clinic.patient.report.state.ReportAccessStatus;
import com.clinic.patient.applicationCommonFeature.state.Role;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final DoctorService doctorService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ReportAccessRequestRepository reportAccessRequestRepository;

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

        for (String patientId : request.getPatientIds()) {
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

    /**
     * Doctors may only read a patient's reports after the patient approved
     * their access request; admins and the patient themselves are always allowed.
     */
    private void checkReportAccess(String patientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }
        User currentUser = userRepository.findByEmail((String) authentication.getPrincipal())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (currentUser.getRole() == Role.ADMIN || currentUser.getEhrId().equals(patientId)) {
            return;
        }
        boolean hasAccess = currentUser.getRole() == Role.DOCTOR && reportAccessRequestRepository
                .findByDoctor_IdAndPatient_EhrId(currentUser.getEhrId(), patientId)
                .map(req -> req.getStatus() == ReportAccessStatus.APPROVED)
                .orElse(false);
        if (!hasAccess) {
            throw new AccessDeniedException("Access denied. You do not have approved report access to this patient's reports.");
        }
    }

    public List<ReportResponseDto> getReportsByPatientId(String patientId) {
        checkReportAccess(patientId);

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
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        checkReportAccess(report.getPatient().getEhrId());
        return report;
    }

    @Transactional
    public void requestAccess(ReportAccessRequestDto dto) {
        Doctor doctor = doctorService.getDoctorById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        User patient = userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Optional<ReportAccessRequest> existing = reportAccessRequestRepository
                .findByDoctor_IdAndPatient_EhrId(dto.getDoctorId(), dto.getPatientId());

        ReportAccessRequest request;
        if (existing.isPresent()) {
            request = existing.get();
            request.setStatus(ReportAccessStatus.PENDING);
            request.setRequestedAt(LocalDateTime.now());
        } else {
            request = ReportAccessRequest.builder()
                    .doctor(doctor)
                    .patient(patient)
                    .status(ReportAccessStatus.PENDING)
                    .requestedAt(LocalDateTime.now())
                    .build();
        }
        reportAccessRequestRepository.save(request);

        String doctorName = doctor.getUser().getFirstName() + " " + doctor.getUser().getLastName();
        String message = "Dr. " + doctorName + " has requested access to view your medical reports.";
        notificationService.createNotification(patient, message);
    }

    /** Only the patient the request targets (or an admin) may respond to it. */
    private void checkRequestOwnership(ReportAccessRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("Not authenticated");
        }
        User currentUser = userRepository.findByEmail((String) authentication.getPrincipal())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (currentUser.getRole() != Role.ADMIN
                && !request.getPatient().getEhrId().equals(currentUser.getEhrId())) {
            throw new AccessDeniedException("You can only respond to your own report access requests");
        }
    }

    @Transactional
    public void approveAccess(Long requestId) {
        ReportAccessRequest request = reportAccessRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        checkRequestOwnership(request);
        request.setStatus(ReportAccessStatus.APPROVED);
        request.setRespondedAt(LocalDateTime.now());
        reportAccessRequestRepository.save(request);

        String patientName = request.getPatient().getFirstName() + " " + request.getPatient().getLastName();
        String message = "Patient " + patientName + " has approved your request to view their medical reports.";
        notificationService.createNotification(request.getDoctor().getUser(), message);
    }

    @Transactional
    public void rejectAccess(Long requestId) {
        ReportAccessRequest request = reportAccessRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        checkRequestOwnership(request);
        request.setStatus(ReportAccessStatus.REJECTED);
        request.setRespondedAt(LocalDateTime.now());
        reportAccessRequestRepository.save(request);

        String patientName = request.getPatient().getFirstName() + " " + request.getPatient().getLastName();
        String message = "Patient " + patientName + " has rejected your request to view their medical reports.";
        notificationService.createNotification(request.getDoctor().getUser(), message);
    }

    public List<ReportAccessRequestResponseDto> getRequestsByPatient(String patientId) {
        return reportAccessRequestRepository.findAllByPatient_EhrId(patientId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ReportAccessRequestResponseDto> getRequestsByDoctor(String doctorId) {
        return reportAccessRequestRepository.findAllByDoctor_Id(doctorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ReportAccessRequestResponseDto mapToDto(ReportAccessRequest request) {
        ReportAccessRequestResponseDto dto = new ReportAccessRequestResponseDto();
        dto.setId(request.getId());
        dto.setPatientId(request.getPatient().getEhrId());
        dto.setPatientName(request.getPatient().getFirstName() + " " + request.getPatient().getLastName());
        dto.setDoctorId(request.getDoctor().getId());
        dto.setDoctorName(request.getDoctor().getUser().getFirstName() + " " + request.getDoctor().getUser().getLastName());
        dto.setStatus(request.getStatus());
        dto.setRequestedAt(request.getRequestedAt());
        dto.setRespondedAt(request.getRespondedAt());
        return dto;
    }
}
