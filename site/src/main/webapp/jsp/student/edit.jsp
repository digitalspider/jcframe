<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Add New Student</title>
</head>
<body>
	<form action="${beanUrl}" method="post">
		<fieldset>
			<div>
				<label for="id">Student ID</label>
				<input type="text" name="id" value="<c:out value="${bean.id}" />"
					readonly="readonly" placeholder="Student ID" />
			</div>
			<div>
				<label for="firstName">First Name</label>
				<input type="text" name="firstName" value="<c:out value="${bean.firstName}" />"
					placeholder="First Name" />
			</div>
			<div>
				<label for="lastName">Last Name</label>
				<input type="text" name="lastName" value="<c:out value="${bean.lastName}" />"
					placeholder="Last Name" />
			</div>
			<div>
				<label for="course">Course</label>
				<input type="text" name="course" value="<c:out value="${bean.course}" />" placeholder="Course" />
			</div>
			<div>
				<label for="year">Year</label>
				<input type="text" name="year" value="<c:out value="${bean.year}" />" placeholder="Year" />
			</div>
			<div>
				<input type="submit" value="Submit" />
			</div>
		</fieldset>
	</form>
</body>
</html>