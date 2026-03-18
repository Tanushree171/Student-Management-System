package com.university.sms.controller.auth;

import com.university.sms.dto.request.LoginRequest;
import com.university.sms.dto.response.ApiResponse;
import com.university.sms.dto.response.LoginResponse;
import com.university.sms.entity.User;
import com.university.sms.entity.Student;
import com.university.sms.repository.UserRepository;
import com.university.sms.repository.StudentRepository;
import com.university.sms.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get display name
        String fullName = user.getUsername();
        if (user.getRole() == User.Role.ROLE_STUDENT) {
            Student student = studentRepository.findByUserId(user.getId()).orElse(null);
            if (student != null) {
                fullName = student.getFullName();
            }
        }

        // Generate JWT
        String token = jwtUtil.generateToken(userDetails, user.getId(), user.getRole().name(), fullName);

        // Set HttpOnly cookie
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // set true in production
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(8 * 60 * 60); // 8 hours
        response.addCookie(jwtCookie);

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Determine redirect URL
        String redirectUrl = switch (user.getRole()) {
            case ROLE_STUDENT -> "/welcome";
            case ROLE_FACULTY, ROLE_HOD, ROLE_REGISTRAR, ROLE_SUPER_ADMIN -> "/admin/dashboard";
        };

        LoginResponse loginResponse = LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .fullName(fullName)
                .userId(user.getId())
                .redirectUrl(redirectUrl)
                .build();

        return ResponseEntity.ok(ApiResponse.success("Login successful", loginResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.ok(ApiResponse.success("If the email exists, a reset link has been sent."));
        }
        // TODO: Send password reset email via EmailService
        return ResponseEntity.ok(ApiResponse.success("If the email exists, a reset link has been sent."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String token,
                                                      @RequestParam String newPassword) {
        // TODO: Validate reset token and update password
        return ResponseEntity.ok(ApiResponse.success("Password has been reset successfully."));
    }
}
