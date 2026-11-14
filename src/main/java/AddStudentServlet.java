import com.school.util.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/AddStudentServlet")
public class AddStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String roll = req.getParameter("rollNumber");
        String name = req.getParameter("name");
        String cls = req.getParameter("className");
        String sec = req.getParameter("section");
        try (Connection conn = DBGet()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO students (student_id, full_name, class_name, section, parent_name, parent_phone, parent_email, qr_code_data) VALUES (?, ?, ?, ?, 'Parent', '9999999999', 'parent@school.com', ?) ON DUPLICATE KEY UPDATE full_name=VALUES(full_name), class_name=VALUES(class_name), section=VALUES(section)"
            );
            ps.setString(1, roll);
            ps.setString(2, name);
            ps.setString(3, cls);
            ps.setString(4, sec);
            ps.setString(5, roll);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect("generate-idcards.html?id=" + roll + "&name=" + java.net.URLEncoder.encode(name, "UTF-8") + "&class=" + java.net.URLEncoder.encode(cls + "-" + sec, "UTF-8"));
    }
    private Connection DBGet() throws Exception {
        return DBConnect.getConnection();
    }
}
