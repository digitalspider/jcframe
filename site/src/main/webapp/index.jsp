<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
    <head>
        <title>Javacloud Website</title>
    </head>

    <body>
    <h2><a href="<%=request.getContextPath()%>/login.jsp">Login</a></h2>

	<h2>Controllers in this site</h2>
	<ul>
		<li><a href="<%=request.getContextPath()%>/jc/student">student controller</a> - <a href="<%=request.getContextPath()%>/student.json">/student.json</a></li>
		<li><a href="<%=request.getContextPath()%>/jc/user">user controller</a> - <a href="<%=request.getContextPath()%>/user.json">/user.json</a></li>
		<li><a href="<%=request.getContextPath()%>/jc/page">page controller</a> - <a href="<%=request.getContextPath()%>/jc/page.json">/page.json</a></li>
	</ul>

    </body>
</html>
