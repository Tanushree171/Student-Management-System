package com.university.sms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "programs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "duration_years")
    private Integer durationYears;

    @Column(length = 100)
    private String department;
}
