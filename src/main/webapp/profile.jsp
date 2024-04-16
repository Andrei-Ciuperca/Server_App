<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Profile</title>
</head>
<body>
    <h1>Profile Management</h1>
    <form action="profile" method="post">
        Username: <input type="text" name="username" value="${sessionScope.username}"><br>
        Password: <input type="password" name="password"><br>
        Email: <input type="email" name="email" value="${sessionScope.email}"><br>
        <input type="submit" value="Update Profile">
    </form>
</body>
</html>
