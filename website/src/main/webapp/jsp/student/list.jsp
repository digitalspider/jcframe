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
        <jc:substring input="GOODMORNING" start="1" end="6"/>

        <table>
            <thead>
                <tr>
                    <th>Student ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Course</th>
                    <th>Year</th>
                    <th colspan="2">Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${beans}" var="bean">
                    <tr>
                        <td><a href="${baseUrl}/show/<c:out value='${bean.id}'/>"><c:out value="${bean.id}"/></td>
                        <td><c:out value="${bean.firstName}" escapeXml="false"/></td>
                        <td><c:out value="${bean.lastName}" escapeXml="false"/></td>
                        <td><c:out value="${bean.course}" escapeXml="false"/></td>
                        <td><c:out value="${bean.year}" escapeXml="false"/></td>
                        <td><a href="${baseUrl}/edit/<c:out value='${bean.id}'/>">Update</a></td>
                        <td><a href="${baseUrl}/delete/<c:out value='${bean.id}'/>">Delete</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <p>
            <a href="${baseUrl}/insert">Add Student</a>
        </p>
    </body>
</html>