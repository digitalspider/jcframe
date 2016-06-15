<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Add New Page</title>
</head>
<body>
	<form action="${beanUrl}" method="post">
		<fieldset>
<div class="fieldrow" id="fieldrow_id" name="fieldrow_id">
  <label for="id">Page ID</label>
  <input type="text" id="id" name="id" value='<c:out value="${bean.id}" />' placeholder="Page ID" readonly="readonly" />
</div>
<div class="fieldrow" id="fieldrow_title" name="fieldrow_title">
  <label for="title">Title</label>
  <input type="text" id="title" name="title" value='<c:out value="${bean.title}" />' placeholder="Title"  />
</div>
<div class="fieldrow" id="fieldrow_url" name="fieldrow_url">
  <label for="url">Url</label>
  <input type="text" id="url" name="url" value='<c:out value="${bean.url}" />' placeholder="Url"  />
</div>
<div class="fieldrow" id="fieldrow_content" name="fieldrow_content">
  <label for="content">Content</label>
  <input type="text" id="content" name="content" value='<c:out value="${bean.content}" />' placeholder="Content"  />
</div>
<div class="fieldrow" id="fieldrow_type" name="fieldrow_type">
  <label for="type">Type</label>
  <input type="text" id="type" name="type" value='<c:out value="${bean.type}" />' placeholder="Type"  />
</div>
<div class="fieldrow" id="fieldrow_tags" name="fieldrow_tags">
  <label for="tags">Tags</label>
  <input type="text" id="tags" name="tags" value='<c:out value="${bean.tags}" />' placeholder="Tags"  />
</div>
<div class="fieldrow" id="fieldrow_authorId" name="fieldrow_authorId">
  <label for="authorId">Author</label>
  <!-- Cloudflare setting -->
  <!--email_off-->
  <select name="authorId">
    <option value="0">Select Author Id...</option>
    <c:forEach items='${lookupMap.get("authorId")}' var="lookupBean">
      <option value='<c:out value="${lookupBean.id}"/>'
      <c:if test="${bean.authorId.id == lookupBean.id}">selected="true"</c:if>
      ><c:out value="${lookupBean.id}"/> - <c:out value="${lookupBean.displayValue}"/></option>
    </c:forEach>
  </select>
  <!--/email_off-->
</div>
<div class="fieldrow" id="fieldrow_parentId" name="fieldrow_parentId">
  <label for="parentId">Parent Page</label>
  <!-- Cloudflare setting -->
  <!--email_off-->
  <select name="parentId">
    <option value="0">Select Parent Page Id...</option>
    <c:forEach items='${lookupMap.get("parentId")}' var="lookupBean">
      <option value='<c:out value="${lookupBean.id}"/>'
      <c:if test="${bean.parentId.id == lookupBean.id}">selected="true"</c:if>
      ><c:out value="${lookupBean.id}"/> - <c:out value="${lookupBean.displayValue}"/></option>
    </c:forEach>
  </select>
  <!--/email_off-->
</div>
<div class="fieldrow" id="fieldrow_description" name="fieldrow_description">
  <label for="description">Description</label>
  <input type="text" id="description" name="description" value='<c:out value="${bean.description}" />' placeholder="Description"  />
</div>
<div class="fieldrow" id="fieldrow_status" name="fieldrow_status">
  <label for="status">Status</label>
  <input type="text" id="status" name="status" value='<c:out value="${bean.status}" />' placeholder="Status"  />
</div>

		</fieldset>
        <div>
			<input value="Submit" type="submit">
		</div>
	</form>
</body>
</html>