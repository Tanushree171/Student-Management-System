package com.university.sms.repository;

import com.university.sms.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentCode(String studentCode);
    Optional<Student> findByUserId(Long userId);
    List<Student> findByProgramId(Long programId);
    List<Student> findByCurrentSemester(Integer semester);
    List<Student> findByBatchYear(Integer batchYear);

    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.studentCode) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Student> searchStudents(@Param("search") String search, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE " +
           "(:programId IS NULL OR s.program.id = :programId) AND " +
           "(:semester IS NULL OR s.currentSemester = :semester) AND " +
           "(:batchYear IS NULL OR s.batchYear = :batchYear) AND " +
           "(:campus IS NULL OR s.campus = :campus)")
    Page<Student> findByFilters(@Param("programId") Long programId,
                                @Param("semester") Integer semester,
                                @Param("batchYear") Integer batchYear,
                                @Param("campus") String campus,
                                Pageable pageable);

    long countByProgramId(Long programId);
}
