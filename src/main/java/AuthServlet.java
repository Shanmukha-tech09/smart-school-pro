import com.school.util.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        resp.setContentType("application/json");

        try (Connection conn = DBConnect.getConnection()) {
            if ("login".equals(action)) {
                String email = req.getParameter("email");
                String pass = req.getParameter("password");
                String role = req.getParameter("role");

                PreparedStatement ps = conn.prepareStatement(
                    "SELECT user_id, full_name, role FROM users WHERE email=? AND password_hash=? AND role=?"
                );
                ps.setString(1, email);
                ps.setString(2, pass);
                ps.setString(3, role);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    HttpSession session = req.getSession();
                    session.setAttribute("userId", rs.getInt("user_id"));
                    session.setAttribute("userName", rs.getString("full_name"));
                    session.setAttribute("userRole", rs.getString("role"));
                    resp.getWriter().write("{\"status\":\"success\",\"role\":\"" + rs.getString("role") + "\"}");
                } else {
                    resp.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid credentials or role!\"}");
                }

            } else if ("register".equals(action)) {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users (full_name, email, password_hash, role) VALUES (?, ?, ?, ?)"
                );
                ps.setString(1, req.getParameter("fullName"));
                ps.setString(2, req.getParameter("email"));
                ps.setString(3, req.getParameter("password"));
                ps.setString(4, req.getParameter("role"));
                ps.executeUpdate();
                resp.getWriter().write("{\"status\":\"success\",\"message\":\"Account created!\"}");

            } else if ("sendOtp".equals(action)) {
                String email = req.getParameter("email");
                String otp = EmailOTPUtil.generateOTP();

                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE users SET otp_code=?, otp_expiry=DATE_ADD(NOW(), INTERVAL 10 MINUTE) WHERE email=?"
                );
                ps.setString(1, otp);
                ps.setString(2, email);
                if (ps.executeUpdate() > 0) {
                    EmailOTPUtil.sendEmail(email, "Smart School PRO - Password Reset OTP", "Your 6-digit OTP is: " + otp);
                    resp.getWriter().write("{\"status\":\"success\",\"message\":\"OTP sent!\"}");
                } else {
                    resp.getWriter().write("{\"status\":\"error\",\"message\":\"Email not registered!\"}");
                }

            } else if ("resetPassword".equals(action)) {
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE users SET password_hash=?, otp_code=NULL WHERE email=? AND otp_code=? AND otp_expiry > NOW()"
                );
                ps.setString(1, req.getParameter("newPassword"));
                ps.setString(2, req.getParameter("email"));
                ps.setString(3, req.getParameter("otp"));
                if (ps.executeUpdate() > 0) {
                    resp.getWriter().write("{\"status\":\"success\",\"message\":\"Password updated!\"}");
                } else {
                    resp.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid or expired OTP!\"}");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
