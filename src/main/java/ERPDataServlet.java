import com.school.util.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/erp")
public class ERPDataServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String module = req.getParameter("module");
        resp.setContentType("application/json");

        try (Connection conn = DBConnect.getConnection()) {
            if ("teacher".equals(module)) {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO teachers (teacher_id, full_name, subject, assigned_class, phone, schedule_slot) VALUES (?, ?, ?, ?, ?, ?)"
                );
                ps.setString(1, req.getParameter("teacherId"));
                ps.setString(2, req.getParameter("fullName"));
                ps.setString(3, req.getParameter("subject"));
                ps.setString(4, req.getParameter("assignedClass"));
                ps.setString(5, req.getParameter("phone"));
                ps.setString(6, req.getParameter("slot"));
                ps.executeUpdate();

            } else if ("fee".equals(module)) {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO fees (receipt_no, student_id, student_name, fee_category, amount, payment_mode, status, paid_date) VALUES (?, ?, ?, ?, ?, ?, 'PAID', CURDATE())"
                );
                ps.setString(1, req.getParameter("receiptNo"));
                ps.setString(2, req.getParameter("studentId"));
                ps.setString(3, req.getParameter("studentName"));
                ps.setString(4, req.getParameter("category"));
                ps.setDouble(5, Double.parseDouble(req.getParameter("amount")));
                ps.setString(6, req.getParameter("mode"));
                ps.executeUpdate();

            } else if ("exam".equals(module)) {
                int math = Integer.parseInt(req.getParameter("math"));
                int sci = Integer.parseInt(req.getParameter("science"));
                int eng = Integer.parseInt(req.getParameter("english"));
                int soc = Integer.parseInt(req.getParameter("social"));
                int comp = Integer.parseInt(req.getParameter("computer"));
                int total = math + sci + eng + soc + comp;
                double pct = (total / 500.0) * 100.0;
                String grade = pct >= 90 ? "A+" : pct >= 75 ? "A" : pct >= 60 ? "B" : "C";

                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO exams (student_id, student_name, class_term, math_marks, science_marks, english_marks, social_marks, computer_marks, total_marks, percentage, grade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                ps.setString(1, req.getParameter("studentId"));
                ps.setString(2, req.getParameter("studentName"));
                ps.setString(3, req.getParameter("classTerm"));
                ps.setInt(4, math);
                ps.setInt(5, sci);
                ps.setInt(6, eng);
                ps.setInt(7, soc);
                ps.setInt(8, comp);
                ps.setInt(9, total);
                ps.setDouble(10, pct);
                ps.setString(11, grade);
                ps.executeUpdate();

            } else if ("circular".equals(module)) {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO circulars (target_audience, title, message) VALUES (?, ?, ?)"
                );
                ps.setString(1, req.getParameter("target"));
                ps.setString(2, req.getParameter("title"));
                ps.setString(3, req.getParameter("message"));
                ps.executeUpdate();
            }

            resp.getWriter().write("{\"status\":\"success\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
