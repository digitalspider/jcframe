<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Add New Student</title>
</head>
<body>
    <a href="${baseUrl}">Home</a>&nbsp;|&nbsp;
    <% if (request.getUserPrincipal()==null) { %>
        <strong><a href="<%=request.getContextPath()%>/login.jsp">Login</a></strong>
    <% } else { %>
        <strong><a href="<%=request.getContextPath()%>/logout.jsp">Logout</a></strong>
    <% } %>&nbsp;|&nbsp;
	<form action="${beanUrl}" method="post">
		<fieldset>
<div class="fieldrow" id="fieldrow_id" name="fieldrow_id">
  <label for="id">Student ID</label>
  <input type="text" id="id" name="id" value='<c:out value="${bean.id}" />' placeholder="Student ID" readonly="readonly" />
</div>
<div class="fieldrow" id="fieldrow_firstName" name="fieldrow_firstName">
  <label for="firstName">FirstName</label>
  <input type="text" id="firstName" name="firstName" value='<c:out value="${bean.firstName}" />' placeholder="FirstName"  />
</div>
<div class="fieldrow" id="fieldrow_lastName" name="fieldrow_lastName">
  <label for="lastName">LastName</label>
  <input type="text" id="lastName" name="lastName" value='<c:out value="${bean.lastName}" />' placeholder="LastName"  />
</div>
<div class="fieldrow" id="fieldrow_course" name="fieldrow_course">
  <label for="course">Course</label>
  <input type="text" id="course" name="course" value='<c:out value="${bean.course}" />' placeholder="Course"  />
</div>
<div class="fieldrow" id="fieldrow_year" name="fieldrow_year">
  <label for="year">Year</label>
  <input type="text" id="year" name="year" value='<c:out value="${bean.year}" />' placeholder="Year"  />
</div>

		</fieldset>
        <div>
			<input class="button" type="submit" value="Submit">
			<input class="button" type="button" onclick="window.location='${beanUrl}';return false;" value="Cancel" />
		</div>
	</form>
</body>
</html>