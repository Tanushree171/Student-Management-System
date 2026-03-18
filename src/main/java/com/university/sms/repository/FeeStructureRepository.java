package com.university.sms.repository;

import com.university.sms.entity.FeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {
    List<FeeStructure> findByProgramIdAndSemester(Long programId, Integer semester);
    List<FeeStructure> findByProgramId(Long programId);
    List<FeeStructure> findByAcademicYear(String academicYear);
}
