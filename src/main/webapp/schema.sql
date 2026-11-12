CREATE DATABASE IF NOT EXISTS smart_school_pro;
USE smart_school_pro;

-- 1. Users & RBAC Authentication Table (Admin, Teacher, Parent)
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('admin', 'teacher', 'parent') NOT NULL,
    otp_code VARCHAR(6),
    otp_expiry TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Students Table
CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(20) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    class_name VARCHAR(30) NOT NULL,
    section VARCHAR(10) NOT NULL,
    parent_name VARCHAR(100) NOT NULL,
    parent_phone VARCHAR(20) NOT NULL,
    parent_email VARCHAR(100),
    qr_code_data VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Teachers Table
CREATE TABLE IF NOT EXISTS teachers (
    teacher_id VARCHAR(20) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    subject VARCHAR(50) NOT NULL,
    assigned_class VARCHAR(30) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    schedule_slot VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Dual Attendance Table (Students & Staff)
CREATE TABLE IF NOT EXISTS attendance (
    att_id INT AUTO_INCREMENT PRIMARY KEY,
    person_id VARCHAR(20) NOT NULL,
    person_type ENUM('student', 'teacher') NOT NULL,
    att_date DATE NOT NULL,
    status ENUM('Present', 'Absent', 'Late') NOT NULL,
    mode ENUM('QR_Gate', 'Manual') DEFAULT 'Manual',
    scan_time TIME,
    UNIQUE KEY unique_daily_att (person_id, att_date)
);

-- 5. Fees Ledger Table
CREATE TABLE IF NOT EXISTS fees (
    receipt_no VARCHAR(20) PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    fee_category VARCHAR(60) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_mode VARCHAR(30) NOT NULL,
    status ENUM('PAID', 'PENDING') DEFAULT 'PAID',
    paid_date DATE NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- 6. Examinations & Report Cards Table
CREATE TABLE IF NOT EXISTS exams (
    exam_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    class_term VARCHAR(50) NOT NULL,
    math_marks INT DEFAULT 0,
    science_marks INT DEFAULT 0,
    english_marks INT DEFAULT 0,
    social_marks INT DEFAULT 0,
    computer_marks INT DEFAULT 0,
    total_marks INT DEFAULT 0,
    percentage DECIMAL(5,2) DEFAULT 0.00,
    grade VARCHAR(25),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- 7. Digital Circulars & Notice Board
CREATE TABLE IF NOT EXISTS circulars (
    circular_id INT AUTO_INCREMENT PRIMARY KEY,
    target_audience VARCHAR(50) NOT NULL,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    posted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed Default Demo Accounts & Records
INSERT IGNORE INTO users (full_name, email, password_hash, role) VALUES
('Principal Admin', 'admin@smartschool.pro', 'admin123', 'admin'),
('Srinivas Rao', 'teacher01@smartschool.pro', 'teach123', 'teacher'),
('Kiran Parent', 'parent@smartschool.pro', 'parent123', 'parent');

INSERT IGNORE INTO students (student_id, full_name, class_name, section, parent_name, parent_phone, parent_email, qr_code_data) VALUES
('STU-101', 'Kiran Varma', 'Class 10', 'A', 'Venkatesh Varma', '9848011223', 'parent@smartschool.pro', 'STU-101'),
('STU-102', 'Sneha Nair', 'Class 9', 'B', 'Ramesh Nair', '9848044556', 'sneha.parent@gmail.com', 'STU-102');

INSERT IGNORE INTO teachers (teacher_id, full_name, subject, assigned_class, phone, schedule_slot) VALUES
('TCH-101', 'Srinivas Rao', 'Mathematics', 'Class 10 - A', '9848012345', '09:00 AM - 01:00 PM'),
('TCH-102', 'Anitha Devi', 'Computer Science', 'Class 9 - B', '9848054321', '10:00 AM - 02:30 PM');