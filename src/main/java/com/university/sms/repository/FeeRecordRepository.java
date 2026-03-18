package com.university.sms.repository;

import com.university.sms.entity.FeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface FeeRecordRepository extends JpaRepository<FeeRecord, Long> {
    List<FeeRecord> findByStudentId(Long studentId);
    List<FeeRecord> findByStudentIdAndStatus(Long studentId, FeeRecord.FeeStatus status);
    List<FeeRecord> findByStatus(FeeRecord.FeeStatus status);

    @Query("SELECT COALESCE(SUM(f.feeStructure.amount - COALESCE(f.paidAmount, 0)), 0) FROM FeeRecord f WHERE f.status <> 'Paid'")
    BigDecimal getTotalPendingAmount();

    @Query("SELECT COALESCE(SUM(f.paidAmount), 0) FROM FeeRecord f WHERE f.status = 'Paid'")
    BigDecimal getTotalCollected();

    long countByStatus(FeeRecord.FeeStatus status);
}
