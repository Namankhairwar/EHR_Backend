package com.clinic.patient.user.repositories;
import com.clinic.patient.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    @Transactional
  default User saveUser(User user){
      User u = save(user);
        System.out.println(u.getEhrId());
        return u;
    }

    @Query("SELECT u FROM User u WHERE u.role = com.clinic.patient.applicationCommonFeature.state.Role.PATIENT AND (" +
            "LOWER(u.email) LIKE LOWER(CONCAT(:query, '%')) OR " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT(:query, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT(:query, '%')) OR " +
            "LOWER(u.ehrId) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<User> searchPatients(@Param("query") String query);
}

