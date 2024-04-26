<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>Login</title>
<style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #F0EBE3;
        }

        .login-container {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            background-color: #F6F5F2;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .login-form {
            background-color: #FFEFEF;
            padding: 30px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            width:50%;
        }

        .login-form h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #F3D0D7;
        }

        .login-form label {
            display: block;
            margin-bottom: 10px;
            color: #F3D0D7;
        }

        .login-form input[type="email"],
        .login-form input[type="password"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #F3D0D7;
            border-radius: 3px;
            margin-bottom: 15px;
        }

        .login-form button {
            background-color: #DAC0A3;
            color: #fff;
            padding: 10px 20px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }

        .login-form button:hover {
            background-color: #e0b2be;
        }
        
        .remember-me-label {
  			color: #F3D0D7;
            float: right;		
		}
		
		.remember-me-checkbox {
  			color: #F3D0D7;
		}
		
    </style>
</head>
<body>
	<div class="login-container">
	<form:form method="POST" action="login" modelAttribute="request" class="login-form" >
	<h2>Login</h2>
	   
        	<label>Username : </label>   
            <form:input path="username" type="email" required="true" placeholder="Enter your username" /> 
            
            <label>Password : </label>   
            <form:input path="password" type="password" required="true" placeholder="Enter your password" /> <br> 
            
            <button type="submit">Login</button> 
             
            <label class="remember-me-label">
  			<input type="checkbox" checked="checked" class="remember-me-checkbox"> Remember me   
			</label>
            
            <button type="button" class="cancelbtn"> Cancel</button> 
            
            <a href="#" style="color: #F3D0D7; float: right;">Forgot password? </a>   
   </form:form>
   </div>

</body>
</html>  