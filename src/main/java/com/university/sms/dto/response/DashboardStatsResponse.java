package com.university.sms.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardStatsResponse {
    private double attendancePercentage;
    private int currentSemester;
    private BigDecimal upcomingFeeAmount;
    private String feeDueDate;
    private int activityCount;
    private BigDecimal sgpa;
    private String sgpaSemesterLabel;
}
