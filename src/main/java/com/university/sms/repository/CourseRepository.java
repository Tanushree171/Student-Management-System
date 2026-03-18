package com.university.sms.repository;

import com.university.sms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCode(String courseCode);
    List<Course> findByProgramIdAndSemester(Long programId, Integer semester);
    List<Course> findByProgramId(Long programId);
    List<Course> findBySemester(Integer semester);
}
