package com.school.dao;

import com.school.model.Student;
import com.school.util.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // 1. New Student Registration Query
    public boolean addStudent(Student student) {
        boolean isSuccess = false;
        String sql = "INSERT INTO students(roll_number, name, class_name, section) VALUES(?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getRollNumber());
            ps.setString(2, student.getName());
            ps.setString(3, student.getClassName());
            ps.setString(4, student.getSection());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                isSuccess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    // 2. Fetch All Students List Query
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student s = new Student();
                s.setStudentId(rs.getInt("student_id"));
                s.setRollNumber(rs.getString("roll_number"));
                s.setName(rs.getString("name"));
                s.setClassName(rs.getString("class_name"));
                s.setSection(rs.getString("section"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

