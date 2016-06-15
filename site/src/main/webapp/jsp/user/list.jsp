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
    <th><a href="${beanUrl}/config/order/id">User ID</a></th>
    <th><a href="${beanUrl}/config/order/email">Email</a></th>
    <th><a href="${beanUrl}/config/order/mobile">Mobile</a></th>
    <th><a href="${beanUrl}/config/order/lastname">Lastname</a></th>
    <th><a href="${beanUrl}/config/order/image">Image</a></th>
    <th><a href="${beanUrl}/config/order/url">Url</a></th>
    <th><a href="${beanUrl}/config/order/token">Token</a></th>
    <th><a href="${beanUrl}/config/order/type">Type</a></th>
    <th><a href="${beanUrl}/config/order/tags">Tags</a></th>
    <th><a href="${beanUrl}/config/order/username">Username</a></th>
    <th><a href="${beanUrl}/config/order/status">Status</a></th>
    <th><a href="${beanUrl}/config/order/description">Description</a></th>
    <th><a href="${beanUrl}/config/order/firstname">Firstname</a></th>
    <th colspan="2">Action</th>
  </tr>
</thead>
<tbody>
  <c:forEach items="${beans}" var="bean">
    <tr>
      <td><a href="${beanUrl}/show/<c:out value='${bean.id}'/>"><c:out value="${bean.id.displayValue}" /></a></td>
      <td><c:out value="${bean.email}" /></td>
      <td><c:out value="${bean.mobile}" /></td>
      <td><c:out value="${bean.lastname}" /></td>
      <td><c:out value="${bean.image}" /></td>
      <td><c:out value="${bean.url}" /></td>
      <td><c:out value="${bean.token}" /></td>
      <td><c:out value="${bean.type}" /></td>
      <td><c:out value="${bean.tags}" /></td>
      <td><c:out value="${bean.username}" /></td>
      <td><c:out value="${bean.status}" /></td>
      <td><c:out value="${bean.description}" /></td>
      <td><c:out value="${bean.firstname}" /></td>
      <td><a href="${beanUrl}/edit/<c:out value='${bean.id}'/>">Update</a></td>
      <td><a href="${beanUrl}/delete/<c:out value='${bean.id}'/>">Delete</a></td>
    </tr>  </c:forEach></tbody>
</table>

        <p>
            <a href="${beanUrl}/insert">Add User</a>
        </p>
    </body>
</html>