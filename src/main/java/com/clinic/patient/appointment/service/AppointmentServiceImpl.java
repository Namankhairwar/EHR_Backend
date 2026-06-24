package com.clinic.patient.appointment.service;

import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.appointment.dto.AppointmentRequestDTO;
import com.clinic.patient.appointment.dto.AppointmentResponseDTO;
import com.clinic.patient.appointment.entity.Appointment;
import com.clinic.patient.appointment.repositories.AppointmentRepository;
import com.clinic.patient.appointment.state.AppointmentStatus;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.doctor.repositories.DoctorRepository;
import com.clinic.patient.user.entity.Patient;
import com.clinic.patient.user.repositories.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  PatientRepository patientRepository,
                                  DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto) {

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appointment = MAP.map(dto, Appointment::new);

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);

        return mapToResponse(saved);
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        return mapToResponse(appointment);
    }

    @Override
    public AppointmentResponseDTO rescheduleAppointment(Long appointmentId, AppointmentRequestDTO dto) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getRescheduleHistory() == null) {
            appointment.setRescheduleHistory(new ArrayList<>());
        }

        appointment.getRescheduleHistory().add(appointment.getAppointmentTime());

        appointment.setAppointmentTime(dto.getAppointmentTime());

        if (dto.getDurationMinutes() != null) {
            appointment.setDurationMinutes(dto.getDurationMinutes());
        }

        if (dto.getReason() != null) {
            appointment.setReason(dto.getReason());
        }

        appointment.setStatus(AppointmentStatus.RESCHEDULED);

        return mapToResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponseDTO cancelAppointment(Long appointmentId, AppointmentRequestDTO dto) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(dto.getCancellationReason());

        return mapToResponse(appointmentRepository.save(appointment));
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByPatient(
            Long patientId,
            AppointmentStatus status,
            Boolean upcoming
    ) {

        List<Appointment> appointments;

        if (status != null) {
            appointments = appointmentRepository.findByPatient_IdAndStatus(patientId, status);
        } else if (Boolean.TRUE.equals(upcoming)) {
            appointments = appointmentRepository.findByPatient_IdAndAppointmentTimeAfter(
                    patientId,
                    LocalDateTime.now()
            );
        } else {
            appointments = appointmentRepository.findByPatient_Id(patientId);
        }

        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByDoctor(
            Long doctorId,
            AppointmentStatus status,
            LocalDate date
    ) {

        List<Appointment> appointments;

        if (status != null) {
            appointments = appointmentRepository.findByDoctor_IdAndStatus(doctorId, status);
        } else if (date != null) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();

            appointments = appointmentRepository.findByDoctor_IdAndAppointmentTimeBetween(
                    doctorId,
                    start,
                    end
            );
        } else {
            appointments = appointmentRepository.findByDoctor_Id(doctorId);
        }

        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AppointmentResponseDTO mapToResponse(Appointment appointment) {

        AppointmentResponseDTO response = MAP.map(appointment, AppointmentResponseDTO::new);

        response.setPatientId(appointment.getPatient().getId());
        response.setPatientName(
                        appointment.getPatient().getFirstName() + " " +
                        appointment.getPatient().getLastName()
        );

        response.setDoctorId(appointment.getDoctor().getId());

        if (appointment.getDoctor().getUser() != null) {
            response.setDoctorName(
                    appointment.getDoctor().getUser().getFirstName() + " " +
                            appointment.getDoctor().getUser().getLastName()
            );
        }

        return response;
    }
}