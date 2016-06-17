<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://javacloud.com.au/taglib" prefix="jc" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Show All ${beanName}s</title>
    </head>
    <body>
        <a href="${baseUrl}">Home</a>&nbsp;|&nbsp;
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
##FIELDHEADERS##
            <th colspan="2">Action</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${beans}" var="bean">
            <tr>
##FIELDS##
              <td><a href="${beanUrl}/edit/<c:out value='${bean.id}'/>">Update</a></td>
              <td><a href="${beanUrl}/delete/<c:out value='${bean.id}'/>">Delete</a></td>
            </tr>  </c:forEach></tbody>
        </table>

        <p>
            <a href="${beanUrl}/insert">Add ${beanName}</a>
        </p>
    </body>
</html>