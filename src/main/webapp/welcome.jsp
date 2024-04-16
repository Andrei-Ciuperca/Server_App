<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
    <h1>Welcome to Your Application</h1>
    <p>Hello, <%= session.getAttribute("loggedInUser") %>! You are now logged in.</p>
    <p>Click <a href="profile.jsp">here</a> to modify your profile.</p>
    <p><a href="index.jsp">Logout</a></p>
</body>
</html>
