//package com.clinic.patient.appointment.repositories;
//
//import com.clinic.patient.appointment.entity.AppointmentBook;
//import com.clinic.patient.appointment.state.AppointmentStatus;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface AppointmentRepository extends JpaRepository<AppointmentBook, Long> {
//
//
//    Optional<AppointmentBook> getAppointmentById(Long id);
//
//
//  int countAppointmentsByPatient_EhrIdAndStatusOrderByAppointmentTimeDesc(Long id, AppointmentStatus status);
//
//    List<AppointmentBook> getAllByPatient_EhrIdAndStatusOrderByAppointmentTimeDesc(Long patientId, AppointmentStatus status);
//
//
//    List<AppointmentBook> getAllByPatient_EhrId(Long patientEhrId, Pageable pageable);
//}
