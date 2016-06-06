<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Student <c:out value="${student.id}" /></title>
</head>
<body>
    <a href="${baseUrl}/list">List</a>
    &nbsp;|&nbsp;
    <a href="${baseUrl}/edit/<c:out value="${bean.id}" />">Edit</a>
    <div>
        <label for="id">Student ID</label>
        <div><c:out value="${bean.id}" /></div>
    </div>
    <div>
        <label for="firstName">First Name</label>
        <div><c:out value="${bean.firstName}" /></div>
    </div>
    <div>
        <label for="lastName">Last Name</label>
        <div><c:out value="${bean.lastName}" /></div>
    </div>
    <div>
        <label for="course">Course</label>
        <div><c:out value="${bean.course}" /></div>
    </div>
    <div>
        <label for="year">Year</label>
        <div><c:out value="${bean.year}" /></div>
    </div>
</body>
</html>