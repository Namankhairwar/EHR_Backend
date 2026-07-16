package com.clinic.patient.appointment.service;

import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.appointment.dto.AppointmentRequestDTO;
import com.clinic.patient.appointment.dto.AppointmentResponseDTO;
import com.clinic.patient.appointment.entity.AppointmentBook;
import com.clinic.patient.appointment.entity.AppointmentTime;
import com.clinic.patient.appointment.repositories.AppointmentRepository;
import com.clinic.patient.appointment.state.AppointmentStatus;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.doctor.service.DoctorService;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.repositories.UserRepository;
import com.clinic.patient.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public ResponseEntity<?> createAppointment(AppointmentRequestDTO appointmentRequestDTO){
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(appointmentRequestDTO.getDoctorId());
        if(doctorOpt.isEmpty()){
            return ResponseEntity.badRequest().body("Doctor does not exist with this id");
        }

        Optional<User> patientOpt = userRepository.findById(appointmentRequestDTO.getPatientId());
        if(patientOpt.isEmpty()){
            return ResponseEntity.badRequest().body("Patient does not exist with this id");
        }

        AppointmentTime appointmentTime = MAP.map(appointmentRequestDTO, AppointmentTime::new);
        AppointmentBook appointmentBook = MAP.map(appointmentRequestDTO, AppointmentBook::new);
        appointmentBook.setPatient(patientOpt.get());
        appointmentBook.setDoctor(doctorOpt.get());
        appointmentBook.setAppointmentTime(appointmentTime);
        appointmentBook.setStatus(AppointmentStatus.SCHEDULED);

        appointmentRepository.save(appointmentBook);

        // Notify Patient
        String doctorName = doctorOpt.get().getUser().getFirstName() + " " + doctorOpt.get().getUser().getLastName();
        String message = "Your appointment with Dr. " + doctorName + " has been scheduled for " + appointmentTime.getDate() + " at " + appointmentTime.getStartTime();
        notificationService.createNotification(patientOpt.get(), message);

        return mapping(appointmentBook, patientOpt.get(), doctorOpt.get());
    }

    public ResponseEntity<AppointmentResponseDTO> mapping(AppointmentBook appointmentBook, User user, Doctor doctor){
        AppointmentResponseDTO dto = MAP.map(appointmentBook, AppointmentResponseDTO::new);
        if (appointmentBook.getAppointmentTime() != null) {
            MAP.copyInTheObject(appointmentBook.getAppointmentTime(), dto);
        }
        dto.setPatientId(user.getEhrId());
        dto.setDoctorId(doctor.getId());
        return ResponseEntity.ok(dto);
    }

    @Transactional
    public ResponseEntity<?> updateAppointment(long id, AppointmentRequestDTO appointmentRequestDTO){
        Optional<AppointmentBook> appointmentById = appointmentRepository.getAppointmentById(id);
        if(appointmentById.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        AppointmentBook appointment = appointmentById.get();
        MAP.copyInTheObject(appointmentRequestDTO, appointment);
        
        if (appointment.getAppointmentTime() != null) {
            MAP.copyInTheObject(appointmentRequestDTO, appointment.getAppointmentTime());
        }
        
        appointmentRepository.save(appointment);

        // Notify Patient
        String doctorName = appointment.getDoctor().getUser().getFirstName() + " " + appointment.getDoctor().getUser().getLastName();
        String message = "Your appointment with Dr. " + doctorName + " has been rescheduled to " + appointment.getAppointmentTime().getDate() + " at " + appointment.getAppointmentTime().getStartTime();
        notificationService.createNotification(appointment.getPatient(), message);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> listAllAppointmentByPatient(long id , int size, int page, String sortBy){
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        List<AppointmentBook> allByPatientId = appointmentRepository.getAllByPatient_EhrId(id, pageable);
        List<AppointmentResponseDTO> list = allByPatientId.stream()
                .map(t -> {
                    AppointmentResponseDTO dto = MAP.map(t, AppointmentResponseDTO::new);
                    if (t.getAppointmentTime() != null) {
                        MAP.copyInTheObject(t.getAppointmentTime(), dto);
                    }
                    dto.setPatientId(t.getPatient().getEhrId());
                    dto.setDoctorId(t.getDoctor().getId());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    public ResponseEntity<?> getAppointmentById(long id){
        Optional<AppointmentBook> appointment = appointmentRepository.getAppointmentById(id);
        if(appointment.isEmpty()){
            return ResponseEntity.badRequest().body("not found this appointment refresh to see the updates");
        }
        AppointmentBook app = appointment.get();
        AppointmentResponseDTO dto = MAP.map(app, AppointmentResponseDTO::new);
        if (app.getAppointmentTime() != null) {
            MAP.copyInTheObject(app.getAppointmentTime(), dto);
        }
        dto.setPatientId(app.getPatient().getEhrId());
        dto.setDoctorId(app.getDoctor().getId());
        return new ResponseEntity<>(dto, HttpStatusCode.valueOf(200));
    }

    public int listCountTodayActiceAppointment(long id){
        return appointmentRepository.countByPatient_EhrIdAndStatus(id, AppointmentStatus.SCHEDULED);
    }

    public List<AppointmentResponseDTO> listAllTodayActiceAppointment(long id){
        return appointmentRepository.getAllByPatient_EhrIdAndStatus(id, AppointmentStatus.SCHEDULED)
                .stream().map(t -> {
                    AppointmentResponseDTO dto = MAP.map(t, AppointmentResponseDTO::new);
                    if (t.getAppointmentTime() != null) {
                        MAP.copyInTheObject(t.getAppointmentTime(), dto);
                    }
                    dto.setPatientId(t.getPatient().getEhrId());
                    dto.setDoctorId(t.getDoctor().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseEntity<Void> cancelAppointment(long id){
        Optional<AppointmentBook> appointmentById = appointmentRepository.getAppointmentById(id);
        if(appointmentById.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        AppointmentBook appointment = appointmentById.get();
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        // Notify Patient
        String doctorName = appointment.getDoctor().getUser().getFirstName() + " " + appointment.getDoctor().getUser().getLastName();
        String message = "Your appointment with Dr. " + doctorName + " on " + appointment.getAppointmentTime().getDate() + " has been CANCELLED.";
        notificationService.createNotification(appointment.getPatient(), message);

        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<Void> deleteAppointment(long id) {
        Optional<AppointmentBook> appointmentById = appointmentRepository.getAppointmentById(id);
        if (appointmentById.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        appointmentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
