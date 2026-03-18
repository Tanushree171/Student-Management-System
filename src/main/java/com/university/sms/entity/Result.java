package com.university.sms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "results")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private Integer semester;

    @Column(name = "academic_year", length = 10)
    private String academicYear;

    @Column(name = "internal_marks")
    private BigDecimal internalMarks;

    @Column(name = "external_marks")
    private BigDecimal externalMarks;

    @Column
    private BigDecimal total;

    @Column(length = 5)
    private String grade;

    @Column(name = "grade_point")
    private BigDecimal gradePoint;

    @Column(name = "is_backlog")
    private Boolean isBacklog = false;

    @Column
    private Boolean published = false;
}
