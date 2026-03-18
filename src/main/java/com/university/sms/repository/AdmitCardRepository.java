package com.university.sms.repository;

import com.university.sms.entity.AdmitCard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AdmitCardRepository extends JpaRepository<AdmitCard, Long> {
    List<AdmitCard> findByStudentIdAndPublishedTrue(Long studentId);
    Optional<AdmitCard> findByStudentIdAndSemesterAndExamType(Long studentId, Integer semester, AdmitCard.ExamType examType);
    List<AdmitCard> findBySemesterAndExamType(Integer semester, AdmitCard.ExamType examType);
}
