<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://javacloud.com.au/taglib" prefix="jc" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Show All Students</title>
    </head>
    <body>
        <a href="${baseUrl}">Home</a>&nbsp;|&nbsp;
        <% if (request.getUserPrincipal()==null) { %>
            <strong><a href="<%=request.getContextPath()%>/login.jsp">Login</a></strong>
        <% } else { %>
            <strong><a href="<%=request.getContextPath()%>/logout.jsp">Logout</a></strong>
        <% } %>&nbsp;|&nbsp;
        <a href="${beanUrl}/list">List</a>&nbsp;|&nbsp;
        <a href="${beanUrl}/list/1">Page1</a>&nbsp;|&nbsp;
        <a href="${beanUrl}/list/2">Page2</a>&nbsp;|&nbsp;
        <a href="${beanUrl}/find/id/1">Find ID 1</a>&nbsp;|&nbsp;
        <a href="${beanUrl}/config/order/id/desc">ID Desc</a>&nbsp;|&nbsp;
        <a href="${beanUrl}/config/order/id/asc">ID ASC</a>&nbsp;|&nbsp;
        <a href="${beanUrl}/config/order/id">ID Toggle</a>&nbsp;|&nbsp;
        <a href="${beanUrl}/config/limit/2">Limit 2</a>&nbsp;|&nbsp;
        <a href="${beanUrl}/config/limit">Limit Reset</a>&nbsp;|&nbsp;
        <a href="${beanUrl}.json">JSON</a>&nbsp;|&nbsp;
        <br/>

        <p>Showing ${beans.size()}/${beancount}</p>
        <table>
        <thead>
          <tr>
    <th><a href="${beanUrl}/config/order/id">Student ID</a></th>
    <th><a href="${beanUrl}/config/order/firstName">FirstName</a></th>
    <th><a href="${beanUrl}/config/order/lastName">LastName</a></th>
    <th><a href="${beanUrl}/config/order/course">Course</a></th>
    <th><a href="${beanUrl}/config/order/year">Year</a></th>

            <th colspan="2">Action</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${beans}" var="bean">
            <tr>
              <td><a href="${beanUrl}/show/<c:out value='${bean.id}'/>"><c:out value="${bean.id}" /></a></td>
              <td><c:out value="${bean.firstName}" /></td>
              <td><c:out value="${bean.lastName}" /></td>
              <td><c:out value="${bean.course}" /></td>
              <td><c:out value="${bean.year}" /></td>

              <td><a href="${beanUrl}/edit/<c:out value='${bean.id}'/>">Update</a></td>
              <td><a href="${beanUrl}/delete/<c:out value='${bean.id}'/>">Delete</a></td>
            </tr>
          </c:forEach>
        </tbody>
        </table>

        <p>
            <a href="${beanUrl}/insert">Add Student</a>
        </p>
    </body>
</html>