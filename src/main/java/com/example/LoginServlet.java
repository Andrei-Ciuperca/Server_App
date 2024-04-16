package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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
        String usernameOrEmail = request.getParameter("username_or_email");
        String password = request.getParameter("password");

        boolean isValidUser = validateLogin(usernameOrEmail, password);

        if (isValidUser) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", usernameOrEmail);
            response.sendRedirect("welcome.jsp");
        } else {
            response.sendRedirect("login.jsp?error=invalid");
        }
    }

    private boolean validateLogin(String usernameOrEmail, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, usernameOrEmail);
                preparedStatement.setString(2, usernameOrEmail);
                preparedStatement.setString(3, hashPassword(password));
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // User found if result set has at least one row
                }
            }
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
}
