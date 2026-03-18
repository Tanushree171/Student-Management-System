package com.university.sms.service;

import com.university.sms.entity.*;
import com.university.sms.exception.ResourceNotFoundException;
import com.university.sms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final StudentRepository studentRepository;
    private static final String UPLOAD_DIR = "uploads/documents/";

    public List<Document> getStudentDocuments(Long studentId) {
        return documentRepository.findByStudentId(studentId);
    }

    public Document uploadDocument(Long studentId, String docType, MultipartFile file) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        Document doc = Document.builder()
                .student(student)
                .docType(docType)
                .filePath(filePath.toString())
                .status(Document.DocStatus.Pending)
                .build();

        return documentRepository.save(doc);
    }

    public List<Document> getPendingDocuments() {
        return documentRepository.findByStatus(Document.DocStatus.Pending);
    }

    public Document approveDocument(Long docId, Long reviewerId) {
        Document doc = documentRepository.findById(docId)
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", docId));
        doc.setStatus(Document.DocStatus.Approved);
        return documentRepository.save(doc);
    }

    public Document rejectDocument(Long docId, Long reviewerId) {
        Document doc = documentRepository.findById(docId)
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", docId));
        doc.setStatus(Document.DocStatus.Rejected);
        return documentRepository.save(doc);
    }
}
