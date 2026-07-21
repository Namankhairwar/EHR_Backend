package com.clinic.patient.appointment.repositories;

import com.clinic.patient.appointment.entity.AppointmentBook;
import com.clinic.patient.appointment.state.AppointmentStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentBook, Long> {

    default Optional<AppointmentBook> getAppointmentById(Long id) {
        return findById(id);
    }

    int countByPatient_EhrIdAndStatus(String patientId, AppointmentStatus status);

    List<AppointmentBook> getAllByPatient_EhrIdAndStatus(String patientId, AppointmentStatus status);

    List<AppointmentBook> getAllByPatient_EhrId(String patientEhrId, Pageable pageable);

    List<AppointmentBook> findAllByDoctor_IdAndAppointmentTime_Date(String doctorId, LocalDate date);

    List<AppointmentBook> findAllByDoctor_Id(String doctorId);
}
