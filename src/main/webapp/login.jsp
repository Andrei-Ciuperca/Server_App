<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h1>Login</h1>
    <form action="login" method="post">
        Username or Email: <input type="text" name="username_or_email"><br>
        Password: <input type="password" name="password"><br>
        <input type="submit" value="Login">
    </form>
    <p>New user? <a href="signup.jsp">Sign up here</a>.</p>
    <p>Forgot your password? <a href="forgotPassword.jsp">Reset it here</a>.</p>
</body>
</html>
