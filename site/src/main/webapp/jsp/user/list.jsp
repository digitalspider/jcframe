<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://javacloud.com.au/taglib" prefix="jc" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Show All Users</title>
    </head>
    <body>
        <a href="<%=request.getContextPath()%>">Home</a>&nbsp;|&nbsp;
        <a href="${baseUrl}/list/1">Page1</a>&nbsp;|&nbsp;
        <a href="${baseUrl}/list/2">Page2</a>&nbsp;|&nbsp;
        <a href="${baseUrl}/config/order/id/desc">ID Desc</a>&nbsp;|&nbsp;
        <a href="${baseUrl}/config/order/id/asc">ID ASC</a>&nbsp;|&nbsp;
        <a href="${baseUrl}/config/order/id">ID Toggle</a>&nbsp;|&nbsp;
        <a href="${baseUrl}/config/limit/2">Limit 2</a>&nbsp;|&nbsp;
        <a href="${baseUrl}/config/limit">Limit Reset</a>&nbsp;|&nbsp;
        <a href="${baseUrl}.json">JSON</a>&nbsp;|&nbsp;
        <br/>

        <p>Showing ${beans.size()}/${beancount}</p>

        <table>
            <thead>
                <tr>
                    <th>User ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Username</th>
                    <th>URL</th>
                    <th>Mobile</th>
                    <th>Type</th>
                    <th>Tags</th>
                    <th>Status</th>
                    <th colspan="2">Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${beans}" var="bean">
                    <tr>
                        <td><a href="${baseUrl}/show/<c:out value='${bean.id}'/>"><c:out value="${bean.id}" escapeXml="false"/></a></td>
                        <td><c:out value="${bean.firstname}"/></td>
                        <td><c:out value="${bean.lastname}"/></td>
                        <td><c:out value="${bean.email}"/></td>
                        <td><c:out value="${bean.username}"/></td>
                        <td><c:out value="${bean.url}"/></td>
                        <td><c:out value="${bean.mobile}"/></td>
                        <td><c:out value="${bean.type}" /></td>
                        <td><c:out value="${bean.tags}" /></td>
                        <td><c:out value="${bean.status}" /></td>
                        <td><a href="${baseUrl}/edit/<c:out value='${bean.id}' />">Update</a></td>
                        <td><a href="${baseUrl}/delete/<c:out value='${bean.id}'/>">Delete</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <p>
            <a href="${baseUrl}/insert">Add User</a>
        </p>
    </body>
</html>