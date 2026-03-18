package com.university.sms.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginResponse {
    private String token;
    private String username;
    private String role;
    private String fullName;
    private Long userId;
    private String redirectUrl;
}
