package com.university.sms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 100)
    private String department;

    @Column(length = 100)
    private String designation;
}
