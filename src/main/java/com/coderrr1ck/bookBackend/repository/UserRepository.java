package com.coderrr1ck.bookBackend.repository;

import com.coderrr1ck.bookBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByIdAndIsActive(UUID uuid,boolean isActive);
    Optional<User> findByEmailAndIsActive(String email, boolean isActive);
}
