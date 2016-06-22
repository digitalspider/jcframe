<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
    <head>
        <title>Javacloud Login</title>
    </head>

    <body>
        <h2>Login</h2>
<%
String redirectUrl = "";
String refererUrl = request.getHeader("Referer");
if (refererUrl!=null) {
    String url = request.getRequestURL().toString();
    String contextUrl = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
    redirectUrl = "?redirect="+refererUrl.substring((""+contextUrl+"/jc").length());
}
%>

        <form id="loginForm" method="POST" action="<%=request.getContextPath()%>/jc/login/login<%=redirectUrl%>">
            <table>
                <tr>
                    <td>Username</td>
                    <td><input type="text" name="j_username" placeholder="Username" /></td>
                </tr>
                <tr>
                    <td>Password</td>
                    <td><input type="password" name="j_password" placeholder="Password" /></td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" value="Login" />
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
