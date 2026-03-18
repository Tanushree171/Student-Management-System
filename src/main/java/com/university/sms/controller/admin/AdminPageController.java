package com.university.sms.controller.admin;

import com.university.sms.dto.response.ApiResponse;
import com.university.sms.entity.*;
import com.university.sms.service.*;
import com.university.sms.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPageController {

    private final AdminService adminService;
    private final AnnouncementService announcementService;
    private final NotificationService notificationService;
    private final FeeService feeService;
    private final ForumService forumService;
    private final DocumentService documentService;
    private final JwtUtil jwtUtil;

    private void addCommonAttributes(HttpServletRequest request, Model model) {
        String token = extractToken(request);
        if (token != null) {
            model.addAttribute("userId", jwtUtil.extractUserId(token));
            model.addAttribute("fullName", jwtUtil.extractFullName(token));
            model.addAttribute("role", jwtUtil.extractRole(token));
            model.addAttribute("unreadNotifications",
                    notificationService.getUnreadCount(jwtUtil.extractUserId(token)));
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

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        Map<String, Object> stats = adminService.getDashboardStats();
        model.addAttribute("stats", stats);
        model.addAttribute("recentStudents", adminService.getAllStudents(PageRequest.of(0, 10)).getContent());
        return "admin/dashboard";
    }

    @GetMapping("/students")
    public String students(HttpServletRequest request, Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false) String search) {
        addCommonAttributes(request, model);
        Page<Student> students;
        if (search != null && !search.isBlank()) {
            students = adminService.searchStudents(search, PageRequest.of(page, 20));
        } else {
            students = adminService.getAllStudents(PageRequest.of(page, 20));
        }
        model.addAttribute("students", students);
        model.addAttribute("search", search);
        return "admin/students/list";
    }

    @GetMapping("/students/new")
    public String addStudent(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "admin/students/add";
    }

    @GetMapping("/students/import")
    public String importStudents(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "admin/students/import";
    }

    @GetMapping("/students/documents")
    public String studentDocuments(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        model.addAttribute("documents", documentService.getPendingDocuments());
        return "admin/students/documents";
    }

    @GetMapping("/courses")
    public String courses(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "admin/courses/list";
    }

    @GetMapping("/attendance")
    public String attendance(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "admin/attendance/upload";
    }

    @GetMapping("/results")
    public String results(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "admin/results/manage";
    }

    @GetMapping("/backlogs")
    public String backlogs(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "admin/results/backlogs";
    }

    @GetMapping("/admit-cards")
    public String admitCards(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "admin/exams/admit-cards";
    }

    @GetMapping("/exam-forms")
    public String examForms(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "admin/exams/forms";
    }

    @GetMapping("/exam-schedule")
    public String examSchedule(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "admin/exams/schedule";
    }

    @GetMapping("/fees/structure")
    public String feeStructure(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "admin/fees/structure";
    }

    @GetMapping("/fees/records")
    public String feeRecords(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "admin/fees/records";
    }

    @GetMapping("/fees/reports")
    public String feeReports(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        model.addAttribute("totalCollected", feeService.getTotalCollected());
        model.addAttribute("totalPending", feeService.getTotalPendingAmount());
        model.addAttribute("overdueCount", feeService.getOverdueCount());
        return "admin/fees/reports";
    }

    @GetMapping("/announcements")
    public String announcements(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        model.addAttribute("announcements", announcementService.getAllActive());
        return "admin/announcements/list";
    }

    @GetMapping("/announcements/new")
    public String newAnnouncement(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "admin/announcements/new";
    }

    @GetMapping("/forum")
    public String forumModeration(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        model.addAttribute("threads", forumService.getThreads(PageRequest.of(0, 50)).getContent());
        return "admin/forum/moderate";
    }

    @GetMapping("/users")
    public String users(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "admin/settings/users";
    }

    @GetMapping("/settings")
    public String settings(HttpServletRequest request, Model model) {
        addCommonAttributes(request, model);
        return "admin/settings/system";
    }
}
