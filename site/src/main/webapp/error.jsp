<%@page language="java" pageEncoding="UTF-8" isErrorPage="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
    <head>
        <title>Javacloud Error</title>
    </head>

    <body>
    <strong><a href="<%=request.getContextPath()%>/jc">Home</a></strong>

    <p><b>Message:</b> ${e.getMessage()}</p>
    <p><b>StackTrace:</b> <pre>${pageContext.out.flush()}${e.printStackTrace(pageContext.response.writer)}</pre></p>
    </body>
</html>
