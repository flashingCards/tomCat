import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FormHandler extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String DB_USERNAME = "your_username";
    private static final String DB_PASSWORD = "your_password";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get form data
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String language = req.getParameter("language");
        String country = req.getParameter("country");

        // Save form data to database
        saveFormData(name, address, language, country);

        // Get visitor number from database
        int visitorNumber = getVisitorNumber();

        // Display visitor number
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1>Thank you for submitting the form!</h1>");
        out.println("<p>Visitor Number: " + visitorNumber + "</p>");
    }

    private void saveFormData(String name, String address, String language, String country) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "INSERT INTO visitor_info (name, address, language, country) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, language);
            pstmt.setString(4, country);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving form data: " + e.getMessage());
        }
    }

    private int getVisitorNumber() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT COUNT(*) FROM visitor_info";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet result = pstmt.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.err.println("Error getting visitor number: " + e.getMessage());
            return 0;
        }
    }
}