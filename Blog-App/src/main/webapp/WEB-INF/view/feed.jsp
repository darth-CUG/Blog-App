<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Post Feed</title>

<style>
* {
  box-sizing: border-box;
}

body {
  font-family: Arial;
  padding: 20px;
  background: #f1f1f1;
}

/* Header/Blog Title */
.header {
  padding: 30px;
  font-size: 40px;
  text-align: center;
  background: white;
}
/* Create two unequal columns that floats next to each other */
/* Left column */
.leftcolumn {
  float: left;
  width: 75%;
}

/* Right column */
.rightcolumn {
  float: left;
  width: 25%;
  padding-left: 20px;
}

/* Fake image */
.postsByFollowingUser {
  background-color: #aaa;
  width: 100%;
  padding: 20px;
}

/* Add a card effect for articles */
.card {
  background-color: white;
  padding: 20px;
  margin-top: 20px;
}
.leftcolumnimage {
	padding: 0px;
	text-align: right;
}
.rightcolumnimage {
	text-align: centre;
}
/* Clear floats after the columns */
.row:after {
  content: "";
  display: table;
  clear: both;
}
/* Footer */
.footer {
  padding: 20px;
  text-align: center;
  background: #ddd;
  margin-top: 20px;
}

/* Responsive layout - when the screen is less than 800px wide, make the two columns stack on top of each other instead of next to each other */
@media screen and (max-width: 800px) {
  .leftcolumn, .rightcolumn {
    width: 100%;
    padding: 0;
  }
</style>

</head>


<body>
	<div class="header">
  		<h2>Feed</h2>
	</div>
	
	
	<div class="row">
		<div class="leftcolumn">
			<c:forEach items="${postsByLikedCategories}" var="post">
		    	<div class="card">
			      <a href="/singlePost/${post.postId}"> <h2>${post.postTitle}</h2> </a>
			      <p>${post.postContent}</p>
			      <div class="leftcolumnimage"><img src="/images/${post.postImageName}" alt="${post.postImageName}" width="300" height="250"> </div>
			      <p>Added: ${post.postAddedDate}</p>
			      <p>Category: ${post.categoryTitle}</p>
			      <p>Author: ${post.writtenBy}</p>
			      <p>Comments: ${post.commentsCount}</p>
			      <p>Likes: ${post.likesCount}</p>
		    	</div>
	    	</c:forEach>
  	</div>
  		<div class="rightcolumn">
  		<div class="card">
		    <h3>Posts By Folowing</h3>
  			<c:forEach items="${postsByFollowing}" var="post">
  				<div class="postsByFollowingUser">
  					<h2>${post.postTitle}</h2>
				    <div class="rightcolumnimage"><img src="/images/${post.postImageName}" alt="${post.postImageName}" width="120" height="120"></div>
				    <p>Category: ${post.categoryTitle}</p>
				    <p>Author: ${post.writtenBy}</p>
				    <p>Added: ${post.postAddedDate}</p>
  				</div><br>
  				
  			</c:forEach>
  			
   
      	</div>
      </div>
	</div>
  
  <c:if test="${totalPages > 1}">
    <ul class="pagination">
      <c:if test="${currentPage > 1}">
        <li><a href="?pageNo=${currentPage - 1}">Previous</a></li>
      </c:if>
      <c:forEach begin="1" end="${totalPages}" var="pageNumber">
        <li <c:if test="${currentPage == pageNumber}">class="active"</c:if>><a href="?pageNo=${pageNumber-1}">${pageNumber}</a></li>
      </c:forEach>
      <c:if test="${currentPage < totalPages}">
        <li><a href="?pageNo=${currentPage + 1}">Next</a></li>
      </c:if>
    </ul>
  </c:if>
  
  <div class="footer">
  <h2>Footer</h2>
</div>

</body>
</html>
