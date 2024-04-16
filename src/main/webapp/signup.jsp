<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign Up</title>
</head>
<body>
    <h1>Sign Up</h1>
    <form action="/ServerApp/signup" method="post">
        Username: <input type="text" name="username"><br>
        Password: <input type="password" name="password"><br>
        Email: <input type="email" name="email"><br>
        <input type="submit" value="Sign Up">
    </form>
    <p>Already have an account? <a href="index.jsp">Go back to the main page</a>.</p>
</body>
</html>
