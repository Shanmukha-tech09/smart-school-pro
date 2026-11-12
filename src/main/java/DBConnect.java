import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
    private static Connection conn = null;

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                // Load MySQL JDBC Driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // MySQL Database URL & Credentials (Password set to shannuu2609)
                String url = "jdbc:mysql://localhost:3306/school_attendance_db?useSSL=false&allowPublicKeyRetrieval=true";
                String user = "root"; 
                String password = "shannuu2609"; 
                
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("✅ Database Connected Successfully!");
            }
        } catch (Exception e) {
            System.out.println("❌ Database Connection Failed!");
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        getConnection();
    }
}