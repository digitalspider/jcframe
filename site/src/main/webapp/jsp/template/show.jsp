<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Student <c:out value="${page.id}" /></title>
</head>
<body>
    <a href="${beanUrl}/list">List</a>
    &nbsp;|&nbsp;
    <a href="${beanUrl}/edit/<c:out value="${bean.id}" />">Edit</a>
    <div>
        <label for="id">Student ID</label>
        <div><c:out value="${bean.id}" /></div>
    </div>
    <div>
        <label for="firstName">First Name</label>
        <div><c:out value="${bean.title}" /></div>
    </div>
    <div>
        <label for="lastName">Last Name</label>
        <div><c:out value="${bean.description}" /></div>
    </div>
    <div>
        <label for="course">Course</label>
        <div><c:out value="${bean.content}" /></div>
    </div>
    <div>
        <label for="year">Year</label>
        <div><c:out value="${bean.url}" /></div>
    </div>
</body>
</html>