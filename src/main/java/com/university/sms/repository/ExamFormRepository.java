package com.university.sms.repository;

import com.university.sms.entity.ExamForm;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ExamFormRepository extends JpaRepository<ExamForm, Long> {
    List<ExamForm> findByStudentId(Long studentId);
    Optional<ExamForm> findByStudentIdAndSemester(Long studentId, Integer semester);
    List<ExamForm> findByStatus(ExamForm.FormStatus status);
    long countByStatus(ExamForm.FormStatus status);
}
