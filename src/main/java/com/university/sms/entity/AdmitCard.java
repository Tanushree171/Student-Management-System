package com.university.sms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admit_cards")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdmitCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private Integer semester;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type", nullable = false, length = 20)
    private ExamType examType;

    @Column(nullable = false)
    private Boolean published = false;

    @Column(name = "exam_schedule", columnDefinition = "TEXT")
    private String examScheduleJson;

    public enum ExamType {
        Regular, Supplementary
    }
}
