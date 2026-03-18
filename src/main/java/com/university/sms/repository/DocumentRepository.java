package com.university.sms.repository;

import com.university.sms.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByStudentId(Long studentId);
    List<Document> findByStatus(Document.DocStatus status);
    long countByStatus(Document.DocStatus status);
}
