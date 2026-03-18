package com.university.sms.dto.response;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentProfileResponse {
    private Long id;
    private String studentCode;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String dob;
    private String gender;
    private String campus;
    private String program;
    private String programCode;
    private Integer batchYear;
    private Integer currentSemester;
    private String photoUrl;
}
