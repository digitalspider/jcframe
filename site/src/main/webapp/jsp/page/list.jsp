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
        <a href="${baseUrl}">Home</a>&nbsp;|&nbsp;
        <% if (request.getUserPrincipal()==null) { %>
            <strong><a href="<%=request.getContextPath()%>/login.jsp">Login</a></strong>
        <% } else { %>
            <strong><a href="<%=request.getContextPath()%>/logout.jsp">Logout</a></strong>
        <% } %>&nbsp;|&nbsp;
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
    <th><a href="${beanUrl}/config/order/id">Page ID</a></th>
    <th><a href="${beanUrl}/config/order/title">Title</a></th>
    <th><a href="${beanUrl}/config/order/url">Url</a></th>
    <th><a href="${beanUrl}/config/order/description">Description</a></th>
    <th><a href="${beanUrl}/config/order/content">Content</a></th>
    <th><a href="${beanUrl}/config/order/tags">Tags</a></th>
    <th><a href="${beanUrl}/config/order/type">Type</a></th>
    <th><a href="${beanUrl}/config/order/status">Status</a></th>
    <th><a href="${beanUrl}/config/order/authorId">Author</a></th>
    <th><a href="${beanUrl}/config/order/parentId">Parent Page</a></th>

            <th colspan="2">Action</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${beans}" var="bean">
            <tr>
              <td><a href="${beanUrl}/show/<c:out value='${bean.id}'/>"><c:out value="${bean.id}" /></a></td>
              <td><c:out value="${bean.title}" escapeXml="false"/></td>
              <td><c:out value="${bean.url}" /></td>
              <td><c:out value="${bean.description}" escapeXml="false"/></td>
              <td><c:out value="${bean.content}" escapeXml="false"/></td>
              <td><c:out value="${bean.tags}" /></td>
              <td><c:out value="${bean.type}" /></td>
              <td><c:out value="${bean.status}" /></td>
              <td><a href="${baseUrl}/user/show/<c:out value='${bean.authorId.id}'/>"><c:out value="${bean.authorId.displayValue}" /></a></td>
              <td><a href="${baseUrl}/page/show/<c:out value='${bean.parentId.id}'/>"><c:out value="${bean.parentId.displayValue}" /></a></td>

              <td><a href="${beanUrl}/edit/<c:out value='${bean.id}'/>">Update</a></td>
              <td><a href="${beanUrl}/delete/<c:out value='${bean.id}'/>">Delete</a></td>
            </tr>
          </c:forEach>
        </tbody>
        </table>

        <p>
            <a href="${beanUrl}/insert">Add Page</a>
        </p>
    </body>
</html>