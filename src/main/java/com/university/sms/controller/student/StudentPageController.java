package com.university.sms.controller.student;

import com.university.sms.dto.response.*;
import com.university.sms.entity.*;
import com.university.sms.security.JwtUtil;
import com.university.sms.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudentPageController {

    private final StudentService studentService;
    private final AnnouncementService announcementService;
    private final NotificationService notificationService;
    private final FeeService feeService;
    private final ForumService forumService;
    private final DocumentService documentService;
    private final JwtUtil jwtUtil;

    private void addCommonAttributes(HttpServletRequest request, Model model) {
        String token = extractToken(request);
        if (token != null) {
            Long userId = jwtUtil.extractUserId(token);
            String fullName = jwtUtil.extractFullName(token);
            String role = jwtUtil.extractRole(token);
            long unreadCount = notificationService.getUnreadCount(userId);

            model.addAttribute("userId", userId);
            model.addAttribute("fullName", fullName);
            model.addAttribute("role", role);
            model.addAttribute("unreadNotifications", unreadCount);

            try {
                StudentProfileResponse profile = studentService.getProfile(userId);
                model.addAttribute("student", profile);
            } catch (Exception e) {
                // Not a student user
            }
        }
    }

    private String extractToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) return cookie.getValue();
            }
        }
        return null;
    }

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "welcome";
    }

    @GetMapping("/student/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        String token = extractToken(request);
        Long userId = jwtUtil.extractUserId(token);

        DashboardStatsResponse stats = studentService.getDashboardStats(userId);
        List<AttendanceSummaryResponse> attendance = studentService.getAttendanceSummary(userId);
        Page<Announcement> announcements = announcementService.getAnnouncementsForRole(
                "ROLE_STUDENT", PageRequest.of(0, 8));

        model.addAttribute("stats", stats);
        model.addAttribute("attendanceSummary", attendance);
        model.addAttribute("announcements", announcements.getContent());

        return "student/dashboard";
    }

    @GetMapping("/student/attendance")
    public String attendance(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        String token = extractToken(request);
        Long userId = jwtUtil.extractUserId(token);

        List<AttendanceSummaryResponse> attendance = studentService.getAttendanceSummary(userId);
        model.addAttribute("attendanceSummary", attendance);

        return "student/attendance";
    }

    @GetMapping("/student/results")
    public String results(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        String token = extractToken(request);
        Long userId = jwtUtil.extractUserId(token);

        Student student = studentService.getStudentByUserId(userId);
        SemesterResultResponse results = studentService.getResults(userId, student.getCurrentSemester());
        model.addAttribute("results", results);
        model.addAttribute("totalSemesters", student.getCurrentSemester());

        return "student/results";
    }

    @GetMapping("/student/fees")
    public String fees(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        String token = extractToken(request);
        Long userId = jwtUtil.extractUserId(token);
        Long studentId = studentService.getStudentByUserId(userId).getId();

        model.addAttribute("pendingFees", feeService.getPendingFees(studentId));
        model.addAttribute("feeHistory", feeService.getFeeHistory(studentId));

        return "student/fees";
    }

    @GetMapping("/student/admit-card")
    public String admitCard(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "student/admit-card";
    }

    @GetMapping("/student/exam-form")
    public String examForm(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "student/exam-form";
    }

    @GetMapping("/student/documents")
    public String documents(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        String token = extractToken(request);
        Long userId = jwtUtil.extractUserId(token);
        Long studentId = studentService.getStudentByUserId(userId).getId();

        model.addAttribute("documents", documentService.getStudentDocuments(studentId));
        return "student/documents";
    }

    @GetMapping("/student/feedback")
    public String feedback(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "student/feedback";
    }

    @GetMapping("/student/forum")
    public String forum(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        Page<ForumThread> threads = forumService.getThreads(PageRequest.of(0, 20));
        model.addAttribute("threads", threads.getContent());
        return "student/forum";
    }
}
