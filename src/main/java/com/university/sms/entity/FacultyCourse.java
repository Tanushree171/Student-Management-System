package com.university.sms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "faculty_course")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FacultyCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_user_id", nullable = false)
    private User faculty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "academic_year", length = 10)
    private String academicYear;
}
