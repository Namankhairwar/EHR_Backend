package com.clinic.patient.report.repositories;

import com.clinic.patient.report.entity.ReportAccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportAccessRequestRepository extends JpaRepository<ReportAccessRequest, Long> {
    List<ReportAccessRequest> findAllByPatient_EhrId(String patientId);
    List<ReportAccessRequest> findAllByDoctor_Id(String doctorId);
    Optional<ReportAccessRequest> findByDoctor_IdAndPatient_EhrId(String doctorId, String patientId);
}
