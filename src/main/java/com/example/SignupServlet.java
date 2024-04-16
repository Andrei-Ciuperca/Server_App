package com.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;


public class SignupServlet extends HttpServlet {
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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        boolean isValid = validateSignup(username, password, email);

        if (isValid) {
            boolean success = saveToDatabase(username, hashPassword(password), email);
            if (success) {
                request.getSession().setAttribute("signupSuccess", true);
                response.sendRedirect("signup-success.jsp");
            } else {
                response.getWriter().println("Error: Failed to save user data to the database.");
            }
        } else {
            response.getWriter().println("Error: Invalid data or username/email already exists. Please check your inputs and try again.");
        }
    }

    private boolean validateSignup(String username, String password, String email) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Check if username is already used
            String checkUsernameQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement checkUsernameStatement = connection.prepareStatement(checkUsernameQuery)) {
                checkUsernameStatement.setString(1, username);
                try (ResultSet resultSet = checkUsernameStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        // Username already exists
                        return false;
                    }
                }
            }

            // Check if email is already used
            String checkEmailQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
            try (PreparedStatement checkEmailStatement = connection.prepareStatement(checkEmailQuery)) {
                checkEmailStatement.setString(1, email);
                try (ResultSet resultSet = checkEmailStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        // Email already exists
                        return false;
                    }
                }
            }

            // Add other validation logic if needed
            // For simplicity, we'll just check if username, password, and email are not empty
            return username != null && !username.isEmpty() &&
                    password != null && !password.isEmpty() &&
                    email != null && !email.isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Hashing algorithm not found.");
        }
    }

    private boolean saveToDatabase(String username, String hashedPassword, String email) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, email);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
