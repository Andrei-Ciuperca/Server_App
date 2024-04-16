package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:postgresql://ep-steep-lake-a2je07qn.eu-central-1.aws.neon.tech/Users?sslmode=require";
    private static final String DB_USER = "Users_owner";
    private static final String DB_PASSWORD = "HfyqslCu5W2R";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load PostgreSQL JDBC driver.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        String newUsername = request.getParameter("username");

        if (updateProfile(email, newUsername)) {
            session.setAttribute("username", newUsername);
            response.sendRedirect("profile.jsp?success=true");
        } else {
            response.sendRedirect("profile.jsp?error=true");
        }
    }

    private boolean updateProfile(String email, String newUsername) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String updateQuery = "UPDATE users SET username = ? WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                statement.setString(1, newUsername);
                statement.setString(2, email);
                int rowsUpdated = statement.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
