package com.brianvi.spring_auth.user.repository;

import com.brianvi.spring_auth.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByDisplayName(String displayName);
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String verificationCode);
}
