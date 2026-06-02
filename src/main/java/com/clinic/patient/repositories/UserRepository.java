package com.clinic.patient.repositories;
import com.clinic.patient.entities.User;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Transactional
  default User saveUser(User user){
      User u = save(user);
        System.out.println(u.getId());
        return u;
    }
}
