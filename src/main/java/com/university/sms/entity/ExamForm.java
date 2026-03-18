package com.university.sms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_forms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExamForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private Integer semester;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private FormStatus status = FormStatus.Pending;

    @Column(name = "subjects_json", columnDefinition = "TEXT")
    private String subjectsJson;

    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
    }

    public enum FormStatus {
        Pending, Approved, Rejected
    }
}
