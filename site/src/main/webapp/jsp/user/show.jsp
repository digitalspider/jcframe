<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User <c:out value="${bean.id}" /></title>
</head>
<body>
    <a href="${beanUrl}/list">List</a>
    &nbsp;|&nbsp;
    <a href="${beanUrl}/edit/<c:out value="${bean.id}" />">Edit</a>
<div class="fieldrow" id="fieldrow_id" name="fieldrow_id">
  <label for="id">User ID</label>
  <div class="field" id="id" name="id"><c:out value="${bean.id}" /></div>
</div>
<div class="fieldrow" id="fieldrow_email" name="fieldrow_email">
  <label for="email">Email</label>
  <div class="field" id="email" name="email"><c:out value="${bean.email}" /></div>
</div>
<div class="fieldrow" id="fieldrow_mobile" name="fieldrow_mobile">
  <label for="mobile">Mobile</label>
  <div class="field" id="mobile" name="mobile"><c:out value="${bean.mobile}" /></div>
</div>
<div class="fieldrow" id="fieldrow_lastname" name="fieldrow_lastname">
  <label for="lastname">Lastname</label>
  <div class="field" id="lastname" name="lastname"><c:out value="${bean.lastname}" /></div>
</div>
<div class="fieldrow" id="fieldrow_image" name="fieldrow_image">
  <label for="image">Image</label>
  <div class="field" id="image" name="image"><c:out value="${bean.image}" /></div>
</div>
<div class="fieldrow" id="fieldrow_url" name="fieldrow_url">
  <label for="url">Url</label>
  <div class="field" id="url" name="url"><c:out value="${bean.url}" /></div>
</div>
<div class="fieldrow" id="fieldrow_token" name="fieldrow_token">
  <label for="token">Token</label>
  <div class="field" id="token" name="token"><c:out value="${bean.token}" /></div>
</div>
<div class="fieldrow" id="fieldrow_type" name="fieldrow_type">
  <label for="type">Type</label>
  <div class="field" id="type" name="type"><c:out value="${bean.type}" /></div>
</div>
<div class="fieldrow" id="fieldrow_tags" name="fieldrow_tags">
  <label for="tags">Tags</label>
  <div class="field" id="tags" name="tags"><c:out value="${bean.tags}" /></div>
</div>
<div class="fieldrow" id="fieldrow_mdate" name="fieldrow_mdate">
  <label for="mdate">Mdate</label>
  <div class="field" id="mdate" name="mdate"><c:out value="${bean.mdate}" /></div>
</div>
<div class="fieldrow" id="fieldrow_username" name="fieldrow_username">
  <label for="username">Username</label>
  <div class="field" id="username" name="username"><c:out value="${bean.username}" /></div>
</div>
<div class="fieldrow" id="fieldrow_status" name="fieldrow_status">
  <label for="status">Status</label>
  <div class="field" id="status" name="status"><c:out value="${bean.status}" /></div>
</div>
<div class="fieldrow" id="fieldrow_description" name="fieldrow_description">
  <label for="description">Description</label>
  <div class="field" id="description" name="description"><c:out value="${bean.description}" /></div>
</div>
<div class="fieldrow" id="fieldrow_firstname" name="fieldrow_firstname">
  <label for="firstname">Firstname</label>
  <div class="field" id="firstname" name="firstname"><c:out value="${bean.firstname}" /></div>
</div>
<div class="fieldrow" id="fieldrow_cdate" name="fieldrow_cdate">
  <label for="cdate">Cdate</label>
  <div class="field" id="cdate" name="cdate"><c:out value="${bean.cdate}" /></div>
</div>

</body>
</html>