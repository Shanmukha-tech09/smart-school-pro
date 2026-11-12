package com.school.dao;

import com.school.util.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // Login validation query
    public boolean validateUser(String username, String password) {
        boolean isValid = false;
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                isValid = true; // User exists with matching password
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }
}