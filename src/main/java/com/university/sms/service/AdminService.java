package com.university.sms.service;

import com.university.sms.entity.*;
import com.university.sms.exception.ResourceNotFoundException;
import com.university.sms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final FeeRecordRepository feeRecordRepository;
    private final DocumentRepository documentRepository;
    private final AnnouncementRepository announcementRepository;
    private final CourseRepository courseRepository;
    private final ResultRepository resultRepository;
    private final ProgramRepository programRepository;
    private final PasswordEncoder passwordEncoder;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", studentRepository.count());
        stats.put("totalCourses", courseRepository.count());
        stats.put("pendingDues", feeRecordRepository.getTotalPendingAmount());
        stats.put("pendingDocuments", documentRepository.countByStatus(Document.DocStatus.Pending));
        stats.put("activeAnnouncements", announcementRepository.countByIsActiveTrue());
        stats.put("totalPrograms", programRepository.count());
        return stats;
    }

    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public Page<Student> searchStudents(String search, Pageable pageable) {
        return studentRepository.searchStudents(search, pageable);
    }

    public Page<Student> filterStudents(Long programId, Integer semester,
                                         Integer batchYear, String campus, Pageable pageable) {
        return studentRepository.findByFilters(programId, semester, batchYear, campus, pageable);
    }

    public Student createStudent(Student student, String email, String password) {
        User user = User.builder()
                .username(student.getStudentCode())
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .role(User.Role.ROLE_STUDENT)
                .isActive(true)
                .build();
        user = userRepository.save(user);
        student.setUser(user);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student updated) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        existing.setFullName(updated.getFullName());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        existing.setCampus(updated.getCampus());
        existing.setCurrentSemester(updated.getCurrentSemester());
        return studentRepository.save(existing);
    }

    public void deactivateStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        student.getUser().setIsActive(false);
        userRepository.save(student.getUser());
    }

    public void resetStudentPassword(Long studentId, String newPassword) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        User user = student.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
