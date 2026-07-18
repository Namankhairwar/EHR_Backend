package com.clinic.patient.report.repositories;

import com.clinic.patient.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByPatient_EhrIdOrderByDateTimeDesc(String patientId);
    List<Report> findAllByDoctor_IdOrderByDateTimeDesc(String doctorId);
}
