<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Student <c:out value="${bean.id}" /></title>
</head>
<body>
    <a href="${beanUrl}/list">List</a>
    &nbsp;|&nbsp;
    <a href="${beanUrl}/edit/<c:out value="${bean.id}" />">Edit</a>
<div class="fieldrow" id="fieldrow_id" name="fieldrow_id">
  <label for="id">Student ID</label>
  <div class="field" id="id" name="id"><c:out value="${bean.id}" /></div>
</div>
<div class="fieldrow" id="fieldrow_year" name="fieldrow_year">
  <label for="year">Year</label>
  <div class="field" id="year" name="year"><c:out value="${bean.year}" /></div>
</div>
<div class="fieldrow" id="fieldrow_firstName" name="fieldrow_firstName">
  <label for="firstName">FirstName</label>
  <div class="field" id="firstName" name="firstName"><c:out value="${bean.firstName}" /></div>
</div>
<div class="fieldrow" id="fieldrow_lastName" name="fieldrow_lastName">
  <label for="lastName">LastName</label>
  <div class="field" id="lastName" name="lastName"><c:out value="${bean.lastName}" /></div>
</div>
<div class="fieldrow" id="fieldrow_course" name="fieldrow_course">
  <label for="course">Course</label>
  <div class="field" id="course" name="course"><c:out value="${bean.course}" /></div>
</div>

</body>
</html>