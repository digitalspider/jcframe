<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
    <head>
        <title>Javacloud Login</title>
    </head>

    <body>
        <h2>Login</h2>

        <form id="loginForm" method="POST" action="<%=request.getContextPath()%>/jc/user/login">
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
