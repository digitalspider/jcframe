<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://javacloud.com.au/taglib" prefix="jc" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Show All Pages</title>
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

        <table>
            <thead>
                <tr>
                    <th>Page ID</th>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Content</th>
                    <th>URL</th>
                    <th>Type</th>
                    <th>Tags</th>
                    <th>Status</th>
                    <th>Author Id</th>
                    <th>Parent Id</th>
                    <th colspan="2">Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${beans}" var="bean">
                    <tr>
                        <td><a href="${baseUrl}/show/<c:out value='${bean.id}'/>"><c:out value="${bean.id}" /></a></td>
                        <td><c:out value="${bean.title}" escapeXml="false"/></td>
                        <td><c:out value="${bean.description}" escapeXml="false"/></td>
                        <td><c:out value="${bean.content}" escapeXml="false"/></td>
                        <td><c:out value="${bean.url}" escapeXml="false"/></td>
                        <td><c:out value="${bean.type}" /></td>
                        <td><c:out value="${bean.tags}" /></td>
                        <td><c:out value="${bean.status}" /></td>
                        <td><a href="${contextUrl}/user/show/<c:out value='${bean.authorId.id}'/>"><c:out value="${bean.authorId.id}" /> - <c:out value="${bean.authorId.displayValue}" /></a></td>
                        <td><a href="${contextUrl}/page/show/<c:out value='${bean.parentId.id}'/>"><c:out value="${bean.parentId.id}" /> - <c:out value="${bean.parentId.displayValue}" /></a></td>
                        <td><a href="${baseUrl}/edit/<c:out value='${bean.id}'/>">Update</a></td>
                        <td><a href="${baseUrl}/delete/<c:out value='${bean.id}'/>">Delete</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <p>
            <a href="${baseUrl}/insert">Add Page</a>
        </p>
    </body>
</html>