package com.university.sms.service;

import com.university.sms.dto.response.*;
import com.university.sms.entity.*;
import com.university.sms.exception.ResourceNotFoundException;
import com.university.sms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final ResultRepository resultRepository;
    private final CourseRepository courseRepository;
    private final FeeRecordRepository feeRecordRepository;
    private final FeeStructureRepository feeStructureRepository;

    public Student getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "userId", userId));
    }

    public StudentProfileResponse getProfile(Long userId) {
        Student student = getStudentByUserId(userId);
        User user = student.getUser();

        return StudentProfileResponse.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .fullName(student.getFullName())
                .email(user.getEmail())
                .phone(student.getPhone())
                .address(student.getAddress())
                .dob(student.getDob() != null ? student.getDob().toString() : null)
                .gender(student.getGender() != null ? student.getGender().name() : null)
                .campus(student.getCampus())
                .program(student.getProgram() != null ? student.getProgram().getName() : null)
                .programCode(student.getProgram() != null ? student.getProgram().getCode() : null)
                .batchYear(student.getBatchYear())
                .currentSemester(student.getCurrentSemester())
                .photoUrl(student.getPhotoUrl())
                .build();
    }

    public DashboardStatsResponse getDashboardStats(Long userId) {
        Student student = getStudentByUserId(userId);

        // Overall attendance
        long totalClasses = attendanceRepository.countTotalByStudent(student.getId());
        long presentClasses = attendanceRepository.countPresentByStudent(student.getId());
        double attendancePercent = totalClasses > 0 ? (presentClasses * 100.0 / totalClasses) : 0;

        // SGPA for current semester
        BigDecimal sgpa = resultRepository.calculateSGPA(student.getId(), student.getCurrentSemester());
        if (sgpa == null) sgpa = BigDecimal.ZERO;

        // Upcoming fee
        List<FeeRecord> pendingFees = feeRecordRepository.findByStudentIdAndStatus(
                student.getId(), FeeRecord.FeeStatus.Pending);
        BigDecimal totalPending = BigDecimal.ZERO;
        String dueDateStr = "";
        for (FeeRecord fr : pendingFees) {
            totalPending = totalPending.add(fr.getFeeStructure().getAmount());
            if (fr.getFeeStructure().getDueDate() != null) {
                dueDateStr = fr.getFeeStructure().getDueDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
            }
        }

        // Activity count (courses enrolled)
        List<Course> courses = courseRepository.findByProgramIdAndSemester(
                student.getProgram().getId(), student.getCurrentSemester());

        String semLabel = student.getCurrentSemester() + getSemesterSuffix(student.getCurrentSemester()) + " Semester";

        return DashboardStatsResponse.builder()
                .attendancePercentage(Math.round(attendancePercent * 100.0) / 100.0)
                .currentSemester(student.getCurrentSemester())
                .upcomingFeeAmount(totalPending)
                .feeDueDate(dueDateStr)
                .activityCount(courses.size())
                .sgpa(sgpa.setScale(2, RoundingMode.HALF_UP))
                .sgpaSemesterLabel(semLabel)
                .build();
    }

    public List<AttendanceSummaryResponse> getAttendanceSummary(Long userId) {
        Student student = getStudentByUserId(userId);
        List<Course> courses = courseRepository.findByProgramIdAndSemester(
                student.getProgram().getId(), student.getCurrentSemester());

        List<AttendanceSummaryResponse> summaries = new ArrayList<>();
        for (Course course : courses) {
            long total = attendanceRepository.countTotalByStudentAndCourse(student.getId(), course.getId());
            long present = attendanceRepository.countPresentByStudentAndCourse(student.getId(), course.getId());
            double percent = total > 0 ? (present * 100.0 / total) : 0;

            summaries.add(AttendanceSummaryResponse.builder()
                    .courseId(course.getId())
                    .courseCode(course.getCourseCode())
                    .courseName(course.getCourseName())
                    .totalClasses(total)
                    .presentClasses(present)
                    .percentage(Math.round(percent * 100.0) / 100.0)
                    .build());
        }
        return summaries;
    }

    public SemesterResultResponse getResults(Long userId, Integer semester) {
        Student student = getStudentByUserId(userId);
        List<Result> results = resultRepository.findPublishedResults(student.getId(), semester);

        List<SemesterResultResponse.CourseResult> courseResults = results.stream()
                .map(r -> SemesterResultResponse.CourseResult.builder()
                        .courseCode(r.getCourse().getCourseCode())
                        .courseName(r.getCourse().getCourseName())
                        .internalMarks(r.getInternalMarks())
                        .externalMarks(r.getExternalMarks())
                        .total(r.getTotal())
                        .grade(r.getGrade())
                        .gradePoint(r.getGradePoint())
                        .isBacklog(r.getIsBacklog())
                        .build())
                .toList();

        BigDecimal sgpa = resultRepository.calculateSGPA(student.getId(), semester);
        BigDecimal cgpa = resultRepository.calculateCGPA(student.getId());

        return SemesterResultResponse.builder()
                .semester(semester)
                .courses(courseResults)
                .sgpa(sgpa != null ? sgpa.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO)
                .cgpa(cgpa != null ? cgpa.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO)
                .build();
    }

    private String getSemesterSuffix(int sem) {
        return switch (sem) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
}
