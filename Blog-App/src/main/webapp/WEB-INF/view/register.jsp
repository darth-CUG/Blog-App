<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #F0EBE3;
        }

        .register-container {
            background-color: #F6F5F2;
            padding: 30px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            margin: 50px auto;
            max-width: 400px; /* Optional: Set a maximum width */
        }

        .register-container h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #F3D0D7;
        }

        .register-container label {
            display: block;
            margin-bottom: 5px;
            color: #333;
        }

        .register-container input[type="text"],
        .register-container input[type="email"],
        .register-container input[type="password"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 3px;
            margin-bottom: 15px;
            color: #F3D0D7;
        }

        .register-container input[type="submit"] {
            background-color: #F3D0D7;
            color: #fff;
            padding: 10px 20px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            margin-top: 10px;
        }

        .register-container input[type="submit"]:hover {
            background-color: #e0b2be;
        }
    </style>
</head>
<body>
    <div class="register-container">
        <h2>Register</h2>
        <form:form method="POST" action="register"  modelAttribute="userForm">  
            <label for="name">Name:</label>
            <form:input path="name" type="text" required="true" /> <br>
            
            <label for="email">Email:</label>
            <form:input path="email" type="email" required="true" /> <br>
            
            <label for="password">Password:</label>
            <form:input path="password" type="password" required="true" /> <br>
            
            <label for="about">About Me:</label>
            <form:input path="about" type="text" /> <br>
            
            <input type="submit" value="Register Me" />
		</form:form>
    </div>
</body>
</html>
