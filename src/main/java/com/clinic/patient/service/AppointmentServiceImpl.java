package com.clinic.patient.service;

import com.clinic.patient.models.AppointmentRequestDTO;
import com.clinic.patient.models.AppointmentResponseDTO;
import com.clinic.patient.entities.Appointment;
import com.clinic.patient.entities.Doctor;
import com.clinic.patient.entities.Patient;
import com.clinic.patient.role.AppointmentStatus;
import com.clinic.patient.repositories.AppointmentRepository;
import com.clinic.patient.repositories.DoctorRepository;
import com.clinic.patient.repositories.PatientRepository;
import com.clinic.patient.repositories.AppointmentService;
import org.springframework.stereotype.Service;

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

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentTime(dto.getAppointmentTime())
                .duration(dto.getDuration())
                .reason(dto.getReason())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        return mapToResponse(appointmentRepository.save(appointment));
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setDuration(dto.getDuration());
        appointment.setReason(dto.getReason());

        if (dto.getStatus() != null) {
            appointment.setStatus(dto.getStatus());
        }

        return mapToResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponseDTO cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELLED);

        return mapToResponse(appointmentRepository.save(appointment));
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    private AppointmentResponseDTO mapToResponse(Appointment appointment) {
        return AppointmentResponseDTO.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatient().getId())
                .patientName(
                        appointment.getPatient().getFirstName() + " " +
                                appointment.getPatient().getLastName()
                )
                .doctorId(appointment.getDoctor().getDoctorId())
                .doctorName(appointment.getDoctor().getName())
                .appointmentTime(appointment.getAppointmentTime())
                .duration(appointment.getDuration())
                .reason(appointment.getReason())
                .status(appointment.getStatus())
                .build();
    }
}
