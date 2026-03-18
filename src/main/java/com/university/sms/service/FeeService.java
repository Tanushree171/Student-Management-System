package com.university.sms.service;

import com.university.sms.entity.*;
import com.university.sms.exception.ResourceNotFoundException;
import com.university.sms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeeService {

    private final FeeRecordRepository feeRecordRepository;
    private final FeeStructureRepository feeStructureRepository;
    private final StudentRepository studentRepository;

    public List<FeeRecord> getPendingFees(Long studentId) {
        return feeRecordRepository.findByStudentIdAndStatus(studentId, FeeRecord.FeeStatus.Pending);
    }

    public List<FeeRecord> getFeeHistory(Long studentId) {
        return feeRecordRepository.findByStudentId(studentId);
    }

    public FeeRecord makePayment(Long feeRecordId, String paymentMode) {
        FeeRecord record = feeRecordRepository.findById(feeRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("FeeRecord", "id", feeRecordId));

        record.setPaidAmount(record.getFeeStructure().getAmount());
        record.setPaidDate(LocalDate.now());
        record.setPaymentMode(paymentMode);
        record.setTransactionId("TXN" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        record.setStatus(FeeRecord.FeeStatus.Paid);

        return feeRecordRepository.save(record);
    }

    public BigDecimal getTotalPendingAmount() {
        return feeRecordRepository.getTotalPendingAmount();
    }

    public BigDecimal getTotalCollected() {
        return feeRecordRepository.getTotalCollected();
    }

    public long getOverdueCount() {
        return feeRecordRepository.countByStatus(FeeRecord.FeeStatus.Overdue);
    }
}
