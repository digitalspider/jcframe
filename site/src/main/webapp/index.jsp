<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
    <head>
        <title>Javacloud Home</title>
    </head>

    <body>
    <% if (request.getUserPrincipal()==null) { %>
        <strong><a href="<%=request.getContextPath()%>/login.jsp">Login</a></strong>
    <% } else { %>
        <strong><a href="<%=request.getContextPath()%>/jc/login/logout">Logout</a></strong>
    <% } %>

    <c:choose>
        <c:when test="${beantypes!=null}">
            <h2>Controllers in this site</h2>
            <ul>
            <c:forEach items="${beantypes}" var="beanype">
                <li><a href="${baseUrl}/${beanype}">${beanype} controller</a> - <a href="${baseUrl}/${beanype}.json">/${beanype}.json</a></li>
            </c:forEach>
            </ul>
        </c:when>
        <c:otherwise>
            <% response.sendRedirect(request.getContextPath()+"/jc"); %>
        </c:otherwise>
    </c:choose>

    </body>
</html>
