package com.university.sms.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceSummaryResponse {
    private Long courseId;
    private String courseCode;
    private String courseName;
    private long totalClasses;
    private long presentClasses;
    private double percentage;
}
