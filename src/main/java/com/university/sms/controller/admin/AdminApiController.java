package com.university.sms.controller.admin;

import com.university.sms.dto.response.ApiResponse;
import com.university.sms.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminApiController {

    private final AdminService adminService;
    private final AnnouncementService announcementService;
    private final DocumentService documentService;
    private final FeeService feeService;
    private final ForumService forumService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse> getDashboardStats() {
        Map<String, Object> stats = adminService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success("Stats retrieved", stats));
    }

    @GetMapping("/students")
    public ResponseEntity<ApiResponse> getStudents(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(ApiResponse.success("Students retrieved",
                    adminService.searchStudents(search, PageRequest.of(page, size))));
        }
        return ResponseEntity.ok(ApiResponse.success("Students retrieved",
                adminService.getAllStudents(PageRequest.of(page, size))));
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<ApiResponse> deactivateStudent(@PathVariable Long id) {
        adminService.deactivateStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deactivated"));
    }

    @PostMapping("/announcements")
    public ResponseEntity<ApiResponse> createAnnouncement(@RequestBody com.university.sms.entity.Announcement announcement) {
        return ResponseEntity.ok(ApiResponse.success("Announcement created",
                announcementService.create(announcement)));
    }

    @PutMapping("/announcements/{id}")
    public ResponseEntity<ApiResponse> updateAnnouncement(@PathVariable Long id,
                                                            @RequestBody com.university.sms.entity.Announcement announcement) {
        return ResponseEntity.ok(ApiResponse.success("Announcement updated",
                announcementService.update(id, announcement)));
    }

    @DeleteMapping("/announcements/{id}")
    public ResponseEntity<ApiResponse> deleteAnnouncement(@PathVariable Long id) {
        announcementService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Announcement deleted"));
    }

    @PutMapping("/documents/{id}/approve")
    public ResponseEntity<ApiResponse> approveDocument(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Document approved",
                documentService.approveDocument(id, null)));
    }

    @PutMapping("/documents/{id}/reject")
    public ResponseEntity<ApiResponse> rejectDocument(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Document rejected",
                documentService.rejectDocument(id, null)));
    }

    @GetMapping("/fees/report")
    public ResponseEntity<ApiResponse> feeReport() {
        Map<String, Object> report = Map.of(
                "totalCollected", feeService.getTotalCollected(),
                "totalPending", feeService.getTotalPendingAmount(),
                "overdueCount", feeService.getOverdueCount()
        );
        return ResponseEntity.ok(ApiResponse.success("Fee report", report));
    }
}
