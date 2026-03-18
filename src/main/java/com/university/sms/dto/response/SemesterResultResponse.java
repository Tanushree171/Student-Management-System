package com.university.sms.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SemesterResultResponse {
    private Integer semester;
    private List<CourseResult> courses;
    private BigDecimal sgpa;
    private BigDecimal cgpa;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CourseResult {
        private String courseCode;
        private String courseName;
        private BigDecimal internalMarks;
        private BigDecimal externalMarks;
        private BigDecimal total;
        private String grade;
        private BigDecimal gradePoint;
        private Boolean isBacklog;
    }
}
