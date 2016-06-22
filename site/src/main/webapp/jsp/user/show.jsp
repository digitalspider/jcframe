<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User <c:out value="${bean.id}" /></title>
</head>
<body>
    <a href="${baseUrl}">Home</a>&nbsp;|&nbsp;
    <% if (request.getUserPrincipal()==null) { %>
        <strong><a href="<%=request.getContextPath()%>/login.jsp">Login</a></strong>
    <% } else { %>
        <strong><a href="<%=request.getContextPath()%>/logout.jsp">Logout</a></strong>
    <% } %>&nbsp;|&nbsp;
    <a href="${beanUrl}/list">List</a>&nbsp;|&nbsp;
    <a href="${beanUrl}/edit/<c:out value="${bean.id}" />">Edit</a>&nbsp;|&nbsp;
    <a href="${beanUrl}/delete/<c:out value="${bean.id}" />">Delete</a>
    <table border="1">
<tr>
<td class="fieldrow" id="fieldrow_id" name="fieldrow_id">
  <label for="id">User ID</label>
</td>
<td>
  <div class="field" id="id" name="id"><a href="${beanUrl}/show/<c:out value='${bean.id}'/>"><c:out value="${bean.id}" /></a></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_username" name="fieldrow_username">
  <label for="username">Username</label>
</td>
<td>
  <div class="field" id="username" name="username"><c:out value="${bean.username}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_email" name="fieldrow_email">
  <label for="email">Email</label>
</td>
<td>
  <div class="field" id="email" name="email"><c:out value="${bean.email}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_firstname" name="fieldrow_firstname">
  <label for="firstname">Firstname</label>
</td>
<td>
  <div class="field" id="firstname" name="firstname"><c:out value="${bean.firstname}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_lastname" name="fieldrow_lastname">
  <label for="lastname">Lastname</label>
</td>
<td>
  <div class="field" id="lastname" name="lastname"><c:out value="${bean.lastname}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_type" name="fieldrow_type">
  <label for="type">Type</label>
</td>
<td>
  <div class="field" id="type" name="type"><c:out value="${bean.type}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_description" name="fieldrow_description">
  <label for="description">Description</label>
</td>
<td>
  <div class="field" id="description" name="description"><c:out value="${bean.description}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_cdate" name="fieldrow_cdate">
  <label for="cdate">Created Date</label>
</td>
<td>
  <div class="field" id="cdate" name="cdate"><c:out value="${bean.cdate}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_image" name="fieldrow_image">
  <label for="image">Image</label>
</td>
<td>
  <div class="field" id="image" name="image"><c:out value="${bean.image}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_mdate" name="fieldrow_mdate">
  <label for="mdate">Last Modified Date</label>
</td>
<td>
  <div class="field" id="mdate" name="mdate"><c:out value="${bean.mdate}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_mobile" name="fieldrow_mobile">
  <label for="mobile">Mobile</label>
</td>
<td>
  <div class="field" id="mobile" name="mobile"><c:out value="${bean.mobile}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_pages" name="fieldrow_pages">
  <label for="pages">Pages</label>
</td>
<td>
  <div class="field" id="pages" name="pages"><a href="${baseUrl}/page/find/author/=<c:out value='${bean.id}'/>">Pages <c:out value="${bean.pages}" /></a></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_roles" name="fieldrow_roles">
  <label for="roles">Roles</label>
</td>
<td
  <div class="field" id="roles" name="roles"><a href="${baseUrl}/role/show/<c:out value='${bean.roles.id}'/>"><c:out value="${bean.roles}" /></a></div>
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_status" name="fieldrow_status">
  <label for="status">Status</label>
</td>
<td>
  <div class="field" id="status" name="status"><c:out value="${bean.status}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_tags" name="fieldrow_tags">
  <label for="tags">Tags</label>
</td>
<td>
  <div class="field" id="tags" name="tags"><c:out value="${bean.tags}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_token" name="fieldrow_token">
  <label for="token">Token</label>
</td>
<td>
  <div class="field" id="token" name="token"><c:out value="${bean.token}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_url" name="fieldrow_url">
  <label for="url">Url</label>
</td>
<td>
  <div class="field" id="url" name="url"><c:out value="${bean.url}" /></div>
</td>
</tr>
    </table>
    <input class="button" type="button" onclick="window.location='${beanUrl}/edit/<c:out value="${bean.id}" />';return false;" value="Edit" />
</body>
</html>