package com.clinic.patient.user.repositories;

import com.clinic.patient.user.entity.RestrictionGrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestrictionGrantRepository extends JpaRepository<RestrictionGrant, Long> {
    List<RestrictionGrant> findAllByPatient_EhrId(String patientId);
    Optional<RestrictionGrant> findByPatient_EhrIdAndAccessor_EhrId(String patientId, String accessorId);
}
