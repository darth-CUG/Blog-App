<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create New Post</title>
<style type="text/css">
	
body {
  font-family: Arial, sans-serif; /* Good, common font */
  background-color: #F0EBE3; /* Lightest background */
  margin: 0;
  padding: 20px;
}

h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333; /* Darker text for better contrast */
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-control {
  width: 100%; /* Match full width of the form */
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 3px;
  background-color: #F6F5F2; /* Lighter background for input fields */
  font-size: 16px;
}

.form-control:focus {
  border-color: #ccc; /* Change border color on focus */
  outline: none; /* Remove default outline */
}

textarea.form-control {
  height: 120px; /* Adjust textarea height as needed */
}

.create-post-btn {
  background-color: #F3D0D7; /* Light pink button color */
  color: #fff; /* White text */
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
  margin-top: 15px; /* Add some margin above the button */
}

.create-post-btn:hover {
  background-color: #e6a9b7; /* Darken pink on hover */
}


	
</style>
</head>
	

<body>
  <h2>New Post</h2>

  <form:form method="POST" action="createNewPost/user/{userId}/category/{categoryId}" modelAttribute="postForm">
    <div class="form-group">
      <label for="title">Title:</label>
      <form:input path="title" type="text" required="true" class="form-control" />
    </div>

    <div class="form-group">
      <label for="content">Content:</label>
      <form:input path="content" required="true" class="form-control" rows="5"></form:input>
    </div>

    <label for="imageName">Image Name:</label>
    <form:input path="imageName" type="text" required="true" class="form-control" />
    <br>

    <label for="category">Category:</label>
    <form:input path="category" type="text" required="true" class="form-control" />
    <br>

    <label for="user">Written By:</label>
    <form:input path="user" type="text" required="true" class="form-control" />
    <br>

    <input type="submit" class="create-post-btn" value="Post it!" />
  </form:form>


</body>
</html>