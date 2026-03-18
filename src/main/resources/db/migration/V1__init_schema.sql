-- ============================================
-- Student Management System — Full Schema
-- ============================================

-- Users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Programs table
CREATE TABLE programs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) NOT NULL UNIQUE,
    duration_years INT,
    department VARCHAR(100)
);

-- Students table
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    student_code VARCHAR(20) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    dob DATE,
    gender VARCHAR(10),
    phone VARCHAR(15),
    address TEXT,
    campus VARCHAR(50),
    program_id BIGINT,
    batch_year INT,
    current_semester INT,
    photo_url VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (program_id) REFERENCES programs(id)
);

-- Admin profiles table
CREATE TABLE admin_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    department VARCHAR(100),
    designation VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Courses table
CREATE TABLE courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(150) NOT NULL,
    program_id BIGINT,
    semester INT NOT NULL,
    credits INT NOT NULL,
    course_type VARCHAR(10),
    FOREIGN KEY (program_id) REFERENCES programs(id)
);

-- Faculty-Course mapping
CREATE TABLE faculty_course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    faculty_user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    academic_year VARCHAR(10),
    FOREIGN KEY (faculty_user_id) REFERENCES users(id),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Attendance table
CREATE TABLE attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    date DATE NOT NULL,
    status VARCHAR(5) NOT NULL,
    marked_by BIGINT,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id),
    FOREIGN KEY (marked_by) REFERENCES users(id)
);

-- Results table
CREATE TABLE results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    semester INT NOT NULL,
    academic_year VARCHAR(10),
    internal_marks DECIMAL(5,2),
    external_marks DECIMAL(5,2),
    total DECIMAL(5,2),
    grade VARCHAR(5),
    grade_point DECIMAL(3,2),
    is_backlog BOOLEAN DEFAULT FALSE,
    published BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Fee Structure table
CREATE TABLE fee_structure (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    program_id BIGINT NOT NULL,
    semester INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    due_date DATE,
    academic_year VARCHAR(10),
    FOREIGN KEY (program_id) REFERENCES programs(id)
);

-- Fee Records table
CREATE TABLE fee_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    fee_structure_id BIGINT NOT NULL,
    paid_amount DECIMAL(10,2),
    paid_date DATE,
    payment_mode VARCHAR(30),
    transaction_id VARCHAR(100),
    status VARCHAR(10) NOT NULL DEFAULT 'Pending',
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (fee_structure_id) REFERENCES fee_structure(id)
);

-- Admit Cards table
CREATE TABLE admit_cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    semester INT NOT NULL,
    exam_type VARCHAR(20) NOT NULL,
    published BOOLEAN DEFAULT FALSE,
    exam_schedule_json TEXT,
    FOREIGN KEY (student_id) REFERENCES students(id)
);

-- Exam Forms table
CREATE TABLE exam_forms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    semester INT NOT NULL,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(10) NOT NULL DEFAULT 'Pending',
    subjects_json TEXT,
    FOREIGN KEY (student_id) REFERENCES students(id)
);

-- Announcements table
CREATE TABLE announcements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    body TEXT,
    category VARCHAR(30),
    target_role VARCHAR(20),
    target_program_id BIGINT,
    posted_by BIGINT,
    file_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    scheduled_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (target_program_id) REFERENCES programs(id),
    FOREIGN KEY (posted_by) REFERENCES users(id)
);

-- Documents table
CREATE TABLE documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    doc_type VARCHAR(50) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(10) NOT NULL DEFAULT 'Pending',
    reviewed_by BIGINT,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (reviewed_by) REFERENCES users(id)
);

-- Feedback table
CREATE TABLE feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    faculty_user_id BIGINT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    is_anonymous BOOLEAN DEFAULT FALSE,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id),
    FOREIGN KEY (faculty_user_id) REFERENCES users(id)
);

-- Forum Threads table
CREATE TABLE forum_threads (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    body TEXT,
    category VARCHAR(30),
    author_id BIGINT NOT NULL,
    is_pinned BOOLEAN DEFAULT FALSE,
    is_flagged BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id)
);

-- Forum Replies table
CREATE TABLE forum_replies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    thread_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    body TEXT,
    upvotes INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (thread_id) REFERENCES forum_threads(id),
    FOREIGN KEY (author_id) REFERENCES users(id)
);

-- Notifications table
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message VARCHAR(255) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ============================================
-- INDEXES
-- ============================================
CREATE INDEX idx_students_program ON students(program_id);
CREATE INDEX idx_students_batch ON students(batch_year);
CREATE INDEX idx_attendance_student ON attendance(student_id);
CREATE INDEX idx_attendance_course ON attendance(course_id);
CREATE INDEX idx_attendance_date ON attendance(date);
CREATE INDEX idx_results_student ON results(student_id);
CREATE INDEX idx_results_semester ON results(semester);
CREATE INDEX idx_fee_records_student ON fee_records(student_id);
CREATE INDEX idx_fee_records_status ON fee_records(status);
CREATE INDEX idx_announcements_active ON announcements(is_active);
CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_forum_threads_created ON forum_threads(created_at);

-- ============================================
-- SEED DATA
-- ============================================

-- Super Admin (password: admin123)
INSERT INTO users (username, email, password_hash, role, is_active, created_at) VALUES
('superadmin', 'admin@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_SUPER_ADMIN', TRUE, CURRENT_TIMESTAMP);

-- Faculty (password: faculty123)
INSERT INTO users (username, email, password_hash, role, is_active, created_at) VALUES
('dr.sharma', 'sharma@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_FACULTY', TRUE, CURRENT_TIMESTAMP),
('dr.patel', 'patel@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_FACULTY', TRUE, CURRENT_TIMESTAMP);

-- HOD (password: hod123)
INSERT INTO users (username, email, password_hash, role, is_active, created_at) VALUES
('hod.cse', 'hod.cse@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_HOD', TRUE, CURRENT_TIMESTAMP);

-- Registrar (password: registrar123)
INSERT INTO users (username, email, password_hash, role, is_active, created_at) VALUES
('registrar', 'registrar@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_REGISTRAR', TRUE, CURRENT_TIMESTAMP);

-- Student users (password: student123)
INSERT INTO users (username, email, password_hash, role, is_active, created_at) VALUES
('STU2024001', 'rahul.kumar@student.university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_STUDENT', TRUE, CURRENT_TIMESTAMP),
('STU2024002', 'priya.singh@student.university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_STUDENT', TRUE, CURRENT_TIMESTAMP),
('STU2024003', 'amit.verma@student.university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_STUDENT', TRUE, CURRENT_TIMESTAMP);

-- Admin profiles
INSERT INTO admin_profiles (user_id, department, designation) VALUES
(1, 'Administration', 'Super Administrator'),
(2, 'Computer Science', 'Associate Professor'),
(3, 'Computer Science', 'Assistant Professor'),
(4, 'Computer Science', 'Head of Department'),
(5, 'Registrar Office', 'Registrar');

-- Programs
INSERT INTO programs (name, code, duration_years, department) VALUES
('B.Tech Computer Science & Engineering', 'BTCSE', 4, 'Computer Science'),
('B.Tech Electronics & Communication', 'BTECE', 4, 'Electronics'),
('MBA Business Administration', 'MBA', 2, 'Management'),
('M.Tech Computer Science', 'MTCSE', 2, 'Computer Science');

-- Students
INSERT INTO students (user_id, student_code, full_name, dob, gender, phone, address, campus, program_id, batch_year, current_semester, photo_url) VALUES
(6, 'STU2024001', 'Rahul Kumar', '2004-05-15', 'Male', '9876543210', '123 Main Street, Delhi', 'Main Campus', 1, 2024, 3, NULL),
(7, 'STU2024002', 'Priya Singh', '2004-08-22', 'Female', '9876543211', '456 Park Avenue, Mumbai', 'Main Campus', 1, 2024, 3, NULL),
(8, 'STU2024003', 'Amit Verma', '2004-01-10', 'Male', '9876543212', '789 Lake Road, Bangalore', 'South Campus', 2, 2024, 3, NULL);

-- Courses (Semester 3 - B.Tech CSE)
INSERT INTO courses (course_code, course_name, program_id, semester, credits, course_type) VALUES
('CS301', 'Data Structures & Algorithms', 1, 3, 4, 'Theory'),
('CS302', 'Database Management Systems', 1, 3, 4, 'Theory'),
('CS303', 'Operating Systems', 1, 3, 4, 'Theory'),
('CS304', 'Computer Networks', 1, 3, 3, 'Theory'),
('CS305', 'DS Lab', 1, 3, 2, 'Lab'),
('CS306', 'DBMS Lab', 1, 3, 2, 'Lab');

-- Courses (Semester 3 - B.Tech ECE)
INSERT INTO courses (course_code, course_name, program_id, semester, credits, course_type) VALUES
('EC301', 'Digital Signal Processing', 2, 3, 4, 'Theory'),
('EC302', 'Analog Communication', 2, 3, 4, 'Theory'),
('EC303', 'Control Systems', 2, 3, 3, 'Theory');

-- Faculty-Course mapping
INSERT INTO faculty_course (faculty_user_id, course_id, academic_year) VALUES
(2, 1, '2025-26'),
(2, 2, '2025-26'),
(3, 3, '2025-26'),
(3, 4, '2025-26');

-- Sample Attendance for Rahul Kumar (student_id=1, CS301)
INSERT INTO attendance (student_id, course_id, date, status, marked_by) VALUES
(1, 1, '2026-01-06', 'P', 2), (1, 1, '2026-01-07', 'P', 2), (1, 1, '2026-01-08', 'A', 2),
(1, 1, '2026-01-09', 'P', 2), (1, 1, '2026-01-10', 'P', 2), (1, 1, '2026-01-13', 'P', 2),
(1, 1, '2026-01-14', 'P', 2), (1, 1, '2026-01-15', 'A', 2), (1, 1, '2026-01-16', 'P', 2),
(1, 1, '2026-01-17', 'P', 2), (1, 1, '2026-01-20', 'P', 2), (1, 1, '2026-01-21', 'P', 2),
(1, 1, '2026-01-22', 'P', 2), (1, 1, '2026-01-23', 'A', 2), (1, 1, '2026-01-24', 'P', 2),
(1, 1, '2026-01-27', 'P', 2), (1, 1, '2026-01-28', 'P', 2), (1, 1, '2026-01-29', 'P', 2),
(1, 1, '2026-01-30', 'A', 2), (1, 1, '2026-01-31', 'P', 2), (1, 1, '2026-02-03', 'P', 2),
(1, 1, '2026-02-04', 'P', 2), (1, 1, '2026-02-05', 'P', 2), (1, 1, '2026-02-06', 'A', 2),
(1, 1, '2026-02-07', 'P', 2), (1, 1, '2026-02-10', 'P', 2), (1, 1, '2026-02-11', 'P', 2);

-- Attendance for CS302
INSERT INTO attendance (student_id, course_id, date, status, marked_by) VALUES
(1, 2, '2026-01-06', 'P', 2), (1, 2, '2026-01-08', 'P', 2), (1, 2, '2026-01-10', 'P', 2),
(1, 2, '2026-01-13', 'A', 2), (1, 2, '2026-01-15', 'P', 2), (1, 2, '2026-01-17', 'P', 2),
(1, 2, '2026-01-20', 'P', 2), (1, 2, '2026-01-22', 'A', 2), (1, 2, '2026-01-24', 'P', 2),
(1, 2, '2026-01-27', 'P', 2), (1, 2, '2026-01-29', 'P', 2), (1, 2, '2026-01-31', 'P', 2),
(1, 2, '2026-02-03', 'P', 2), (1, 2, '2026-02-05', 'A', 2), (1, 2, '2026-02-07', 'P', 2),
(1, 2, '2026-02-10', 'P', 2), (1, 2, '2026-02-12', 'P', 2), (1, 2, '2026-02-14', 'P', 2);

-- Attendance for CS303
INSERT INTO attendance (student_id, course_id, date, status, marked_by) VALUES
(1, 3, '2026-01-07', 'P', 3), (1, 3, '2026-01-09', 'P', 3), (1, 3, '2026-01-14', 'A', 3),
(1, 3, '2026-01-16', 'P', 3), (1, 3, '2026-01-21', 'A', 3), (1, 3, '2026-01-23', 'P', 3),
(1, 3, '2026-01-28', 'P', 3), (1, 3, '2026-01-30', 'A', 3), (1, 3, '2026-02-04', 'P', 3),
(1, 3, '2026-02-06', 'P', 3), (1, 3, '2026-02-11', 'P', 3), (1, 3, '2026-02-13', 'P', 3);

-- Results (Semester 1 & 2 for Rahul)
INSERT INTO results (student_id, course_id, semester, academic_year, internal_marks, external_marks, total, grade, grade_point, is_backlog, published) VALUES
(1, 1, 3, '2025-26', 38, 55, 93, 'A+', 9.00, FALSE, TRUE),
(1, 2, 3, '2025-26', 35, 48, 83, 'A', 8.50, FALSE, TRUE),
(1, 3, 3, '2025-26', 30, 42, 72, 'B+', 8.00, FALSE, TRUE),
(1, 4, 3, '2025-26', 40, 58, 98, 'O', 10.00, FALSE, TRUE),
(1, 5, 3, '2025-26', 22, 38, 60, 'B', 7.50, FALSE, TRUE),
(1, 6, 3, '2025-26', 42, 50, 92, 'A+', 9.00, FALSE, TRUE);

-- Fee Structure
INSERT INTO fee_structure (program_id, semester, category, amount, due_date, academic_year) VALUES
(1, 3, 'Tuition', 45000.00, '2026-06-30', '2025-26'),
(1, 3, 'Examination', 5200.00, '2026-06-30', '2025-26'),
(1, 3, 'Library', 3000.00, '2026-06-30', '2025-26'),
(1, 3, 'Laboratory', 6000.00, '2026-06-30', '2025-26');

-- Fee Records
INSERT INTO fee_records (student_id, fee_structure_id, paid_amount, paid_date, payment_mode, transaction_id, status) VALUES
(1, 1, NULL, NULL, NULL, NULL, 'Pending'),
(1, 2, NULL, NULL, NULL, NULL, 'Pending'),
(1, 3, 3000.00, '2026-01-15', 'Online', 'TXN20260115001', 'Paid'),
(1, 4, NULL, NULL, NULL, NULL, 'Pending');

-- Announcements
INSERT INTO announcements (title, body, category, target_role, posted_by, is_active, created_at) VALUES
('Mid-Semester Exam Schedule Published', 'The mid-semester examination schedule for all programs has been published. Please check the examination section for details. The exams will commence from March 25, 2026.', 'Examination Notice', 'ROLE_STUDENT', 1, TRUE, '2026-03-10 09:00:00'),
('Library Hours Extended', 'The university library will now remain open until 10 PM on weekdays effective from March 15, 2026. Students are encouraged to utilize the extended hours for their exam preparation.', 'General Announcement', NULL, 1, TRUE, '2026-03-08 14:30:00'),
('Placement Drive - TCS & Infosys', 'TCS and Infosys will be conducting campus placement drives on April 5-6, 2026. Eligible students from B.Tech (all branches) with CGPA >= 7.0 are requested to register through the placement portal before March 28, 2026.', 'Department Notice', 'ROLE_STUDENT', 4, TRUE, '2026-03-05 11:00:00'),
('Fee Payment Deadline Extended', 'The last date for fee payment for the current semester has been extended to June 30, 2026. Students who fail to pay by the deadline will incur a late fee of Rs. 500.', 'General Announcement', NULL, 5, TRUE, '2026-03-01 10:00:00'),
('Workshop on AI & Machine Learning', 'A two-day workshop on Artificial Intelligence and Machine Learning will be conducted on March 20-21, 2026 in the CS Department Auditorium. Registration is open for all CS and IT students.', 'Department Notice', 'ROLE_STUDENT', 2, TRUE, '2026-02-28 16:00:00'),
('Sports Day Celebration', 'Annual Sports Day will be celebrated on March 15, 2026. All students are encouraged to participate. Registration forms available at the Sports Department office.', 'General Announcement', NULL, 1, TRUE, '2026-02-25 09:00:00');

-- Forum threads
INSERT INTO forum_threads (title, body, category, author_id, is_pinned, created_at) VALUES
('Tips for Data Structures exam preparation', 'Hey everyone! The mid-sem exams are approaching. Lets share tips and resources for DS preparation. I found this great YouTube playlist that covers all topics.', 'Academic', 6, TRUE, '2026-03-12 10:30:00'),
('Best laptop under 50K for coding?', 'Looking for recommendations for a good laptop for programming. Budget is around 50K. Need good RAM and SSD for running IDEs smoothly.', 'General', 7, FALSE, '2026-03-11 15:45:00'),
('Lost ID Card near Library', 'I lost my university ID card somewhere near the library yesterday evening. If anyone finds it, please contact me. Student code: STU2024003.', 'General', 8, FALSE, '2026-03-10 09:15:00');

-- Forum replies
INSERT INTO forum_replies (thread_id, author_id, body, upvotes, created_at) VALUES
(1, 7, 'Thanks for sharing! I also recommend the NPTEL lectures on Data Structures. They cover everything in the syllabus.', 5, '2026-03-12 11:00:00'),
(1, 8, 'Practice problems on LeetCode and GeeksforGeeks helped me a lot in previous semesters.', 3, '2026-03-12 12:30:00'),
(2, 6, 'I would recommend the ASUS VivoBook or HP Pavilion series. Both have good specs in that range.', 2, '2026-03-11 16:00:00');

-- Notifications
INSERT INTO notifications (user_id, message, is_read, created_at) VALUES
(6, 'Mid-semester exam schedule has been published. Check the examination section.', FALSE, '2026-03-10 09:05:00'),
(6, 'Your library fee payment of Rs. 3000 has been received successfully.', TRUE, '2026-01-15 10:00:00'),
(6, 'New announcement: Fee payment deadline extended to June 30, 2026.', FALSE, '2026-03-01 10:05:00'),
(6, 'Workshop on AI & ML - Registration open. Limited seats available.', FALSE, '2026-02-28 16:05:00'),
(7, 'Mid-semester exam schedule has been published.', FALSE, '2026-03-10 09:05:00'),
(8, 'Mid-semester exam schedule has been published.', FALSE, '2026-03-10 09:05:00');
