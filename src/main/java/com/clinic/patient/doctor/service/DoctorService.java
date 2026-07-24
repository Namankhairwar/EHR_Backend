package com.clinic.patient.doctor.service;


import com.clinic.patient.applicationCommonFeature.authentication.entity.VerificationToken;
import com.clinic.patient.applicationCommonFeature.authentication.service.EmailService;
import com.clinic.patient.applicationCommonFeature.authentication.service.VerificationTokenService;
import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.appointment.dto.DoctorScheduleDTO;
import com.clinic.patient.appointment.entity.AppointmentTimeDoctor;
import com.clinic.patient.appointment.repositories.AppointmentTimeDoctorRepository;
import com.clinic.patient.doctor.dto.DoctorSummaryDTO;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.doctor.repositories.DoctorRepository;
import com.clinic.patient.user.dto.UserRequestDTO;
import com.clinic.patient.user.dto.UserResponseDTO;
import com.clinic.patient.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Krishana dubey
 *
 * @saveDoctor
 * @getAllDoctors
 * @getDoctorById
 * @deleteDoctor
 * @updateDoctor
 */

@Service
@AllArgsConstructor
@Slf4j
public class DoctorService {


    private final DoctorRepository doctorRepository;
    private final AppointmentTimeDoctorRepository appointmentTimeDoctorRepository;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;


    public ResponseEntity<?> saveDoctor(UserRequestDTO doctor_RequestDTO) {
        Doctor doctor_ehr = new Doctor();
        doctor_ehr.setUser(MAP.map(doctor_RequestDTO, User::new));
        MAP.copyInTheObject(doctor_RequestDTO, doctor_ehr);
        doctor_ehr = doctorRepository.save(doctor_ehr);
        UserResponseDTO userResponseDTO = MAP.map(doctor_RequestDTO, UserResponseDTO::new);
        userResponseDTO.setEhrId(doctor_ehr.getId());
        log.info("Step 3 - User saved");

        VerificationToken verificationToken = verificationTokenService.createVerificationToken(doctor_ehr.getUser());
        String verificationLink = "https://ehrbackend-production-de58.up.railway.app/api/auth/verify?token=" + verificationToken.getToken();
        log.info("Token created: {}", verificationToken.getToken());
        log.info("Verification link: {}", verificationLink);

        try {
            emailService.sendVerificationEmail(
                    doctor_ehr.getUser().getEmail(),
                    doctor_ehr.getUser().getFirstName(),
                    verificationToken.getToken()
            );
            log.info("Email sent successfully");
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userResponseDTO);

        } catch (Exception e) {
            log.error("Failed to send verification email: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Registration successful. Note: Email verification service is currently unreachable. Please use this verification link manually: " + verificationLink);
        }


    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(String id) {
        return doctorRepository.findById(id);
    }

    public void deleteDoctor(String id) {
        doctorRepository.deleteById(id);
    }

    public DoctorSummaryDTO toSummary(Doctor doctor) {
        User user = doctor.getUser();
        Doctor.DoctorProfile profile = doctor.getDoctorProfile();
        return DoctorSummaryDTO.builder()
                .id(doctor.getId())
                .firstName(user != null ? user.getFirstName() : null)
                .lastName(user != null ? user.getLastName() : null)
                .specialization(profile != null ? profile.getSpecialization() : null)
                .aboutDoctor(profile != null ? profile.getAboutDoctor() : null)
                .degrees(profile != null ? profile.getDegrees() : null)
                .build();
    }

    public List<DoctorSummaryDTO> getAllDoctorSummaries() {
        return doctorRepository.findAll().stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    public List<DoctorSummaryDTO> searchDoctorSummaries(String query) {
        return doctorRepository.searchDoctors(query).stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public List<DoctorScheduleDTO> getSchedule(String doctorId) {
        return appointmentTimeDoctorRepository.findAllByDoctor_Id(doctorId).stream()
                .map(s -> new DoctorScheduleDTO(s.getDays(),
                        s.getStartSchedule().format(TIME_FORMAT),
                        s.getEndSchedule().format(TIME_FORMAT)))
                .collect(Collectors.toList());
    }

    /** Replaces the doctor's whole weekly availability with the given entries. */
    @Transactional
    public void setSchedule(String doctorId, List<DoctorScheduleDTO> schedule) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        appointmentTimeDoctorRepository.deleteAllByDoctor_Id(doctorId);

        for (DoctorScheduleDTO dto : schedule) {
            if (dto.getDay() == null) {
                throw new RuntimeException("Each schedule entry needs a day (SUNDAY...SATURDAY)");
            }
            LocalTime start;
            LocalTime end;
            try {
                start = LocalTime.parse(dto.getStartTime(), TIME_FORMAT);
                end = LocalTime.parse(dto.getEndTime(), TIME_FORMAT);
            } catch (Exception e) {
                throw new RuntimeException("Invalid time format for " + dto.getDay() + ". Expected HH:mm:ss");
            }
            if (!start.isBefore(end)) {
                throw new RuntimeException("Start time must be before end time for " + dto.getDay());
            }
            appointmentTimeDoctorRepository.save(AppointmentTimeDoctor.builder()
                    .doctor(doctor)
                    .days(dto.getDay())
                    .startSchedule(start)
                    .endSchedule(end)
                    .build());
        }
    }
}
