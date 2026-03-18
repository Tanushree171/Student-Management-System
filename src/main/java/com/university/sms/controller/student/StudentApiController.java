package com.university.sms.controller.student;

import com.university.sms.dto.response.*;
import com.university.sms.entity.*;
import com.university.sms.service.*;
import com.university.sms.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentApiController {

    private final StudentService studentService;
    private final FeeService feeService;
    private final DocumentService documentService;
    private final ForumService forumService;
    private final NotificationService notificationService;
    private final AnnouncementService announcementService;
    private final JwtUtil jwtUtil;

    private Long getUserId(HttpServletRequest request) {
        String token = extractToken(request);
        return jwtUtil.extractUserId(token);
    }

    private Long getStudentId(HttpServletRequest request) {
        Long userId = getUserId(request);
        return studentService.getStudentByUserId(userId).getId();
    }

    private String extractToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new RuntimeException("No JWT token found");
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile(HttpServletRequest request) {
        StudentProfileResponse profile = studentService.getProfile(getUserId(request));
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved", profile));
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse> getDashboardStats(HttpServletRequest request) {
        DashboardStatsResponse stats = studentService.getDashboardStats(getUserId(request));
        return ResponseEntity.ok(ApiResponse.success("Dashboard stats retrieved", stats));
    }

    @GetMapping("/attendance/summary")
    public ResponseEntity<ApiResponse> getAttendanceSummary(HttpServletRequest request) {
        List<AttendanceSummaryResponse> summary = studentService.getAttendanceSummary(getUserId(request));
        return ResponseEntity.ok(ApiResponse.success("Attendance summary retrieved", summary));
    }

    @GetMapping("/results/{semester}")
    public ResponseEntity<ApiResponse> getResults(HttpServletRequest request,
                                                   @PathVariable Integer semester) {
        SemesterResultResponse results = studentService.getResults(getUserId(request), semester);
        return ResponseEntity.ok(ApiResponse.success("Results retrieved", results));
    }

    @GetMapping("/fees/pending")
    public ResponseEntity<ApiResponse> getPendingFees(HttpServletRequest request) {
        List<FeeRecord> fees = feeService.getPendingFees(getStudentId(request));
        return ResponseEntity.ok(ApiResponse.success("Pending fees retrieved", fees));
    }

    @GetMapping("/fees/history")
    public ResponseEntity<ApiResponse> getFeeHistory(HttpServletRequest request) {
        List<FeeRecord> fees = feeService.getFeeHistory(getStudentId(request));
        return ResponseEntity.ok(ApiResponse.success("Fee history retrieved", fees));
    }

    @PostMapping("/fees/pay")
    public ResponseEntity<ApiResponse> payFee(@RequestParam Long feeRecordId,
                                               @RequestParam String paymentMode) {
        FeeRecord record = feeService.makePayment(feeRecordId, paymentMode);
        return ResponseEntity.ok(ApiResponse.success("Payment successful", record));
    }

    @GetMapping("/documents")
    public ResponseEntity<ApiResponse> getDocuments(HttpServletRequest request) {
        List<Document> docs = documentService.getStudentDocuments(getStudentId(request));
        return ResponseEntity.ok(ApiResponse.success("Documents retrieved", docs));
    }

    @PostMapping("/documents/upload")
    public ResponseEntity<ApiResponse> uploadDocument(HttpServletRequest request,
                                                       @RequestParam String docType,
                                                       @RequestParam MultipartFile file) {
        try {
            Document doc = documentService.uploadDocument(getStudentId(request), docType, file);
            return ResponseEntity.ok(ApiResponse.success("Document uploaded", doc));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Upload failed: " + e.getMessage()));
        }
    }

    @GetMapping("/forum/threads")
    public ResponseEntity<ApiResponse> getForumThreads(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Page<ForumThread> threads = forumService.getThreads(PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success("Threads retrieved", threads));
    }

    @PostMapping("/forum/threads")
    public ResponseEntity<ApiResponse> createThread(HttpServletRequest request,
                                                     @RequestParam String title,
                                                     @RequestParam String body,
                                                     @RequestParam(required = false) String category) {
        ForumThread thread = forumService.createThread(title, body, category, getUserId(request));
        return ResponseEntity.ok(ApiResponse.success("Thread created", thread));
    }

    @PostMapping("/forum/threads/{id}/reply")
    public ResponseEntity<ApiResponse> replyToThread(HttpServletRequest request,
                                                      @PathVariable Long id,
                                                      @RequestParam String body) {
        ForumReply reply = forumService.addReply(id, body, getUserId(request));
        return ResponseEntity.ok(ApiResponse.success("Reply posted", reply));
    }

    @PostMapping("/feedback/submit")
    public ResponseEntity<ApiResponse> submitFeedback(HttpServletRequest request,
                                                       @RequestParam Long courseId,
                                                       @RequestParam Long facultyId,
                                                       @RequestParam Integer rating,
                                                       @RequestParam(required = false) String comment,
                                                       @RequestParam(defaultValue = "false") Boolean anonymous) {
        // TODO: Implement feedback submission with window check
        return ResponseEntity.ok(ApiResponse.success("Feedback submitted successfully"));
    }
}
