package com.clinic.patient.doctor.service;


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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class DoctorService {


    private final DoctorRepository doctorRepository;
    private final AppointmentTimeDoctorRepository appointmentTimeDoctorRepository;

    public UserResponseDTO saveDoctor(UserRequestDTO doctor_RequestDTO) {
        Doctor doctor_ehr = new Doctor();
   doctor_ehr.setUser(MAP.map(doctor_RequestDTO,User::new));
        MAP.copyInTheObject(doctor_RequestDTO,doctor_ehr);
    doctor_ehr=doctorRepository.save(doctor_ehr);
     UserResponseDTO userResponseDTO= MAP.map(doctor_RequestDTO, UserResponseDTO::new);
      userResponseDTO.setEhrId(doctor_ehr.getId());
      return userResponseDTO;
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
                .consultationFee(profile != null ? profile.getConsultationFee() : null)
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
