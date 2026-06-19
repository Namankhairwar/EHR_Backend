package com.clinic.patient.user.repositories;
import com.clinic.patient.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Transactional
  default User saveUser(User user){
      User u = save(user);
        System.out.println(u.getEhrid());
        return u;
    }



}
