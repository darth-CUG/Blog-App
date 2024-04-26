<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #F0EBE3;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        .welcome-container {
            background-color: #F6F5F2;
            padding: 50px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            text-align: center;
            width:80%;
        }

        .welcome-container h1 {
            margin-bottom: 20px;
            color: #F3D0D7;
            font-size: 2em;
        }

     
        .welcome-container button {
            background-color: #F3D0D7;
            color: #fff;
            padding: 10px 20px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            margin-top: 10px;
        }

        .welcome-container button:hover {
            background-color: #e0b2be;
        }
    </style>
</head>
<body>
    <div class="welcome-container">
        <h1>Welcome!</h1>
        
        <button>Explore More</button>
    </div>
</body>
</html>
