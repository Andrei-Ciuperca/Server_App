package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet("/resetPassword")
public class ForgotPasswordServlet extends HttpServlet {
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
        String email = request.getParameter("email");

        if (resetPassword(email)) {
            // Password reset successful, redirect to a success page
            response.sendRedirect("passwordResetSuccess.jsp");
        } else {
            // Password reset failed, redirect back to the forgot password page with an error message
            response.sendRedirect("forgotPassword.jsp?error=invalidEmail");
        }
    }

    private boolean resetPassword(String email) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Check if the email exists in the database
            String checkEmailQuery = "SELECT * FROM users WHERE email = ?";
            try (PreparedStatement checkEmailStatement = connection.prepareStatement(checkEmailQuery)) {
                checkEmailStatement.setString(1, email);
                try (ResultSet resultSet = checkEmailStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Generate a random password reset token (for demonstration purposes)
                        String resetToken = UUID.randomUUID().toString();

                        // Update the user's record in the database with the reset token
                        String updateTokenQuery = "UPDATE users SET reset_token = ? WHERE email = ?";
                        try (PreparedStatement updateTokenStatement = connection.prepareStatement(updateTokenQuery)) {
                            updateTokenStatement.setString(1, resetToken);
                            updateTokenStatement.setString(2, email);
                            int rowsUpdated = updateTokenStatement.executeUpdate();
                            return rowsUpdated > 0;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
