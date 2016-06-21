<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Student <c:out value="${bean.id}" /></title>
</head>
<body>
    <a href="${baseUrl}">Home</a>&nbsp;|&nbsp;
    <% if (request.getUserPrincipal()==null) { %>
        <strong><a href="<%=request.getContextPath()%>/login.jsp">Login</a></strong>
    <% } else { %>
        <strong><a href="<%=request.getContextPath()%>/logout.jsp">Logout</a></strong>
    <% } %>&nbsp;|&nbsp;
    <a href="${beanUrl}/list">List</a>&nbsp;|&nbsp;
    <a href="${beanUrl}/edit/<c:out value="${bean.id}" />">Edit</a>&nbsp;|&nbsp;
    <a href="${beanUrl}/delete/<c:out value="${bean.id}" />">Delete</a>
    <table border="1">
<tr>
<td class="fieldrow" id="fieldrow_id" name="fieldrow_id">
  <label for="id">Student ID</label>
</td>
<td>
  <div class="field" id="id" name="id"><a href="${beanUrl}/show/<c:out value='${bean.id}'/>"><c:out value="${bean.id}" /></a></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_firstName" name="fieldrow_firstName">
  <label for="firstName">FirstName</label>
</td>
<td>
  <div class="field" id="firstName" name="firstName"><c:out value="${bean.firstName}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_lastName" name="fieldrow_lastName">
  <label for="lastName">LastName</label>
</td>
<td>
  <div class="field" id="lastName" name="lastName"><c:out value="${bean.lastName}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_course" name="fieldrow_course">
  <label for="course">Course</label>
</td>
<td>
  <div class="field" id="course" name="course"><c:out value="${bean.course}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_year" name="fieldrow_year">
  <label for="year">Year</label>
</td>
<td>
  <div class="field" id="year" name="year"><c:out value="${bean.year}" /></div>
</td>
</tr>
    </table>
    <input class="button" type="button" onclick="window.location='${beanUrl}/edit/<c:out value="${bean.id}" />';return false;" value="Edit" />
</body>
</html>