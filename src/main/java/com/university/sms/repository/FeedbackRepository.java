package com.university.sms.repository;

import com.university.sms.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByStudentId(Long studentId);
    List<Feedback> findByCourseId(Long courseId);
    List<Feedback> findByFacultyId(Long facultyId);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}
