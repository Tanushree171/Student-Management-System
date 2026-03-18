package com.university.sms.repository;

import com.university.sms.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByStudentIdAndSemester(Long studentId, Integer semester);
    List<Result> findByStudentId(Long studentId);
    List<Result> findByCourseIdAndPublishedTrue(Long courseId);
    List<Result> findByStudentIdAndIsBacklogTrue(Long studentId);

    @Query("SELECT r FROM Result r WHERE r.student.id = :studentId AND r.semester = :semester AND r.published = true")
    List<Result> findPublishedResults(@Param("studentId") Long studentId, @Param("semester") Integer semester);

    @Query("SELECT AVG(r.gradePoint) FROM Result r WHERE r.student.id = :studentId AND r.semester = :semester AND r.published = true")
    java.math.BigDecimal calculateSGPA(@Param("studentId") Long studentId, @Param("semester") Integer semester);

    @Query("SELECT AVG(r.gradePoint) FROM Result r WHERE r.student.id = :studentId AND r.published = true")
    java.math.BigDecimal calculateCGPA(@Param("studentId") Long studentId);

    List<Result> findByCourseIdAndSemester(Long courseId, Integer semester);
}
