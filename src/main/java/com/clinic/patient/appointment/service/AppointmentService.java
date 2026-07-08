package com.clinic.patient.appointment.service;

import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.appointment.dto.AppointmentRequestDTO;
import com.clinic.patient.appointment.dto.AppointmentResponseDTO;
import com.clinic.patient.appointment.entity.Appointment;
import com.clinic.patient.appointment.repositories.AppointmentRepository;
import com.clinic.patient.appointment.state.AppointmentStatus;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.doctor.service.DoctorService;
import com.clinic.patient.user.entity.Patient;
import com.clinic.patient.user.service.PatientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public ResponseEntity<?> createAppointment(AppointmentRequestDTO appointmentRequestDTO){
        return getResponseEntity(appointmentRequestDTO);
    }

    private ResponseEntity<?> getResponseEntity(AppointmentRequestDTO appointmentRequestDTO) {
        Optional<Doctor> doctor = doctorService.getDoctorById(appointmentRequestDTO.getDoctorId());
        if(doctor.isEmpty()){
            return ResponseEntity.ofNullable("Doctor does not exist with this id");
        }
        Doctor doctor1 = doctor.get();
        Optional<Patient> patient = patientService.getPatientById(appointmentRequestDTO.getPatientId());
        if(patient.isEmpty()){
            return ResponseEntity.ofNullable("patient does not exist with this id");
        }
        Patient patient1= patient.get();
        Appointment appointment = MAP.map(appointmentRequestDTO,Appointment::new);
        appointmentRepository.save(appointment);

        return ResponseEntity.ok(mapping(appointment,doctor1,patient1));
    }

    public ResponseEntity<?> updateAppointment(long id, AppointmentRequestDTO appointmentRequestDTO){
        Optional<Appointment> appointmentById = appointmentRepository.getAppointmentById(id);
        if(appointmentById.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        MAP.copyInTheObject(appointmentById.get(),appointmentRequestDTO);
        appointmentRepository.save(appointmentById.get());
        return ResponseEntity.ok().build();
    }

    private AppointmentResponseDTO mapping(Appointment appointment,Doctor doctor1 , Patient patient1){
        AppointmentResponseDTO responseDTO = MAP.map(appointment,AppointmentResponseDTO::new);
        MAP.copyInTheObject(doctor1,responseDTO.getDoctor(),"user","doctorProfile");
        MAP.copyInTheObject(patient1,responseDTO.getPatient(),"allergies","user");
        return responseDTO;
    }
    public ResponseEntity<?> listAllAppointmentByPatient(long id , int size, int page, String sortBy){
        Sort by = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(page,size,by);
        List<Appointment> allByPatientId = appointmentRepository.getAllByPatient_Id(id, pageable);
        // converting in appointmentResponse
        return ResponseEntity.ok(allByPatientId.stream().map(t->mapping(t,t.getDoctor(),t.getPatient())));
    }
    //builder is going to be required bcz response cannot include the patient complete and doctor complete details
    public ResponseEntity<?> getAppointmentById(long id){
        Optional<Appointment> appointment = appointmentRepository.getAppointmentById(id);
        if(appointment.isEmpty()){
            return ResponseEntity.ofNullable("not found this appointment refresh to see the updates");
        }
        return new ResponseEntity<>(mapping(appointment.orElse(null), appointment.get().getDoctor(), appointment.get().getPatient()), HttpStatusCode.valueOf(201));
    }

    public int listCountTodayActiceAppointment(long id){
        return appointmentRepository.countAppointmentsByPatient_IdAndStatus_SCHEDULEDOrderByAppointmentTimeDesc(id);
    }

    @SuppressWarnings("unchecked")
    public List<AppointmentResponseDTO> listAllTodayActiceAppointment(long id){
        return (List<AppointmentResponseDTO>) appointmentRepository.getAllByPatient_IdAndStatus_SCHEDULEDOrderByAppointmentTimeDesc(id)
                .stream().map(t-> mapping(t,t.getDoctor(),t.getPatient()));
    }



    // cancel and delete query logic

    /**
     *
     * @param id
     * @return ResponseEntity<AppointmentResponseDTO>
     * @Author Krishna Dubey
     * @Feature CancelAppointment
     */
    public ResponseEntity<Void> cancelAppointment(long id){
        Optional<Appointment> appointmentById = appointmentRepository.getAppointmentById(id);
        if(appointmentById.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        appointmentById.get().setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointmentById.get());
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<Void> deleteAppointment(long id){
        Optional<Appointment> appointmentById = appointmentRepository.getAppointmentById(id);
        if(appointmentById.isEmpty()){
          return  ResponseEntity.notFound().build();

        }

        appointmentRepository.deleteById(id);
      return  ResponseEntity.ok().build();
    }


















}
