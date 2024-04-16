<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Forgot Password</title>
</head>
<body>
    <h1>Forgot Your Password?</h1>
    <p>Please enter your email address below to reset your password.</p>
    <form action="/ServerApp/resetPassword" method="post">
        Email: <input type="email" name="email"><br>
        <input type="submit" value="Reset Password">
    </form>
</body>
</html>
