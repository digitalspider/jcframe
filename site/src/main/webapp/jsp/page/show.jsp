<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Page <c:out value="${bean.id}" /></title>
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
  <label for="id">Page ID</label>
</td>
<td>
  <div class="field" id="id" name="id"><a href="${beanUrl}/show/<c:out value='${bean.id}'/>"><c:out value="${bean.id}" /></a></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_title" name="fieldrow_title">
  <label for="title">Title</label>
</td>
<td>
  <div class="field" id="title" name="title"><c:out value="${bean.title}" escapeXml="false"/></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_url" name="fieldrow_url">
  <label for="url">Url</label>
</td>
<td>
  <div class="field" id="url" name="url"><c:out value="${bean.url}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_description" name="fieldrow_description">
  <label for="description">Description</label>
</td>
<td>
  <div class="field" id="description" name="description"><c:out value="${bean.description}" escapeXml="false"/></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_content" name="fieldrow_content">
  <label for="content">Content</label>
</td>
<td>
  <div class="field" id="content" name="content"><c:out value="${bean.content}" escapeXml="false"/></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_tags" name="fieldrow_tags">
  <label for="tags">Tags</label>
</td>
<td>
  <div class="field" id="tags" name="tags"><c:out value="${bean.tags}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_type" name="fieldrow_type">
  <label for="type">Type</label>
</td>
<td>
  <div class="field" id="type" name="type"><c:out value="${bean.type}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_status" name="fieldrow_status">
  <label for="status">Status</label>
</td>
<td>
  <div class="field" id="status" name="status"><c:out value="${bean.status}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_author" name="fieldrow_author">
  <label for="author">Author</label>
</td>
<td
  <div class="field" id="author" name="author"><a href="${baseUrl}/user/show/<c:out value='${bean.author.id}'/>"><c:out value="${bean.author.displayValue}" /></a></div>
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_cdate" name="fieldrow_cdate">
  <label for="cdate">Created Date</label>
</td>
<td>
  <div class="field" id="cdate" name="cdate"><c:out value="${bean.cdate}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_mdate" name="fieldrow_mdate">
  <label for="mdate">Last Modified Date</label>
</td>
<td>
  <div class="field" id="mdate" name="mdate"><c:out value="${bean.mdate}" /></div>
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_parent" name="fieldrow_parent">
  <label for="parent">Parent Page</label>
</td>
<td
  <div class="field" id="parent" name="parent"><a href="${baseUrl}/page/show/<c:out value='${bean.parent.id}'/>"><c:out value="${bean.parent.displayValue}" /></a></div>
</td>
</tr>

    </table>
    <input class="button" type="button" onclick="window.location='${beanUrl}/edit/<c:out value="${bean.id}" />';return false;" value="Edit" />
</body>
</html>