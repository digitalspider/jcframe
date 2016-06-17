<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Page <c:out value="${bean.id}" /></title>
</head>
<body>
    <a href="${beanUrl}/list">List</a>
    &nbsp;|&nbsp;
    <a href="${beanUrl}/edit/<c:out value="${bean.id}" />">Edit</a>
<div class="fieldrow" id="fieldrow_id" name="fieldrow_id">
  <label for="id">Page ID</label>
  <div class="field" id="id" name="id"><c:out value="${bean.id}" /></div>
</div>
<div class="fieldrow" id="fieldrow_title" name="fieldrow_title">
  <label for="title">Title</label>
  <div class="field" id="title" name="title"><c:out value="${bean.title}" /></div>
</div>
<div class="fieldrow" id="fieldrow_url" name="fieldrow_url">
  <label for="url">Url</label>
  <div class="field" id="url" name="url"><c:out value="${bean.url}" /></div>
</div>
<div class="fieldrow" id="fieldrow_description" name="fieldrow_description">
  <label for="description">Description</label>
  <div class="field" id="description" name="description"><c:out value="${bean.description}" /></div>
</div>
<div class="fieldrow" id="fieldrow_content" name="fieldrow_content">
  <label for="content">Content</label>
  <div class="field" id="content" name="content"><c:out value="${bean.content}" /></div>
</div>
<div class="fieldrow" id="fieldrow_tags" name="fieldrow_tags">
  <label for="tags">Tags</label>
  <div class="field" id="tags" name="tags"><c:out value="${bean.tags}" /></div>
</div>
<div class="fieldrow" id="fieldrow_type" name="fieldrow_type">
  <label for="type">Type</label>
  <div class="field" id="type" name="type"><c:out value="${bean.type}" /></div>
</div>
<div class="fieldrow" id="fieldrow_status" name="fieldrow_status">
  <label for="status">Status</label>
  <div class="field" id="status" name="status"><c:out value="${bean.status}" /></div>
</div>
<div class="fieldrow" id="fieldrow_authorId" name="fieldrow_authorId">
  <label for="authorId">Author</label>
  <div class="field" id="authorId" name="authorId"><c:out value="${bean.authorId}" /></div>
</div>
<div class="fieldrow" id="fieldrow_parentId" name="fieldrow_parentId">
  <label for="parentId">Parent Page</label>
  <div class="field" id="parentId" name="parentId"><c:out value="${bean.parentId}" /></div>
</div>
<div class="fieldrow" id="fieldrow_cdate" name="fieldrow_cdate">
  <label for="cdate">Created Date</label>
  <div class="field" id="cdate" name="cdate"><c:out value="${bean.cdate}" /></div>
</div>
<div class="fieldrow" id="fieldrow_mdate" name="fieldrow_mdate">
  <label for="mdate">Last Modified Date</label>
  <div class="field" id="mdate" name="mdate"><c:out value="${bean.mdate}" /></div>
</div>

</body>
</html>