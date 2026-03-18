package com.university.sms.repository;

import com.university.sms.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    Optional<Program> findByCode(String code);
}
