package com.university.sms.controller;

import com.university.sms.service.NotificationService;
import com.university.sms.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SharedApiController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse> getNotifications(@RequestParam Long userId) {
        return ResponseEntity.ok(ApiResponse.success("Notifications retrieved",
                notificationService.getUserNotifications(userId)));
    }

    @PutMapping("/notifications/{id}/read")
    public ResponseEntity<ApiResponse> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read"));
    }
}
