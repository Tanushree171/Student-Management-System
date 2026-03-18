package com.university.sms.service;

import com.university.sms.entity.*;
import com.university.sms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setIsRead(true);
            notificationRepository.save(n);
        });
    }

    public void createNotification(Long userId, String message) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Notification notification = Notification.builder()
                    .user(user)
                    .message(message)
                    .isRead(false)
                    .build();
            notificationRepository.save(notification);
        }
    }

    public void notifyAllStudents(String message) {
        List<User> students = userRepository.findByRole(User.Role.ROLE_STUDENT);
        for (User student : students) {
            createNotification(student.getId(), message);
        }
    }
}
