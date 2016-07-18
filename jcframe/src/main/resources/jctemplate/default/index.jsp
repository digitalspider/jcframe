<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://javacloud.com.au/taglib" prefix="jc" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>${beanName} Home</title>
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

        <c:choose>
            <c:when test="${beans.size()>0}">
                <c:set var="bean" value="${beans.get(0)}"/>
                <div id="content">
                    <c:out value="${bean.displayValue}"/>
                </div>
            </c:when>
            <c:otherwise>
                <script type="text/javascript">window.location.href="${beanUrl}/list"</script>
            </c:otherwise>
        </c:choose>
    </body>
</html>