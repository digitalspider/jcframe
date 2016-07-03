<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Add New Page</title>

<script>
function addLookup(selectName) {
    var selectActual = document.getElementById(selectName);
    var selectLookup = document.getElementById(selectName+"-lookup");
    for (var i=selectLookup.options.length; --i>=0; ) {
        var option = selectLookup.options[i];
        if (option.selected) {
            selectActual.appendChild(option);
        }
    }
    // Select all remaining options
    for (var i=selectActual.options.length; --i>=0; ) {
       var option = selectActual.options[i];
       option.selected=true;
    }
}
function removeLookup(selectName) {
    var selectActual = document.getElementById(selectName);
    var selectLookup = document.getElementById(selectName+"-lookup");
    for (var i=selectActual.options.length; --i>=0; ) {
        var option = selectActual.options[i];
        if (option.selected) {
            selectLookup.appendChild(option);
            option.selected=false;
        }
    }
    // Select all remaining options
    for (var i=selectActual.options.length; --i>=0; ) {
       var option = selectActual.options[i];
       option.selected=true;
    }
}
</script>

</head>
<body>
    <a href="${baseUrl}">Home</a>&nbsp;|&nbsp;
    <% if (request.getUserPrincipal()==null) { %>
        <strong><a href="<%=request.getContextPath()%>/login.jsp">Login</a></strong>
    <% } else { %>
        <strong><a href="<%=request.getContextPath()%>/logout.jsp">Logout</a></strong>
    <% } %>&nbsp;|&nbsp;
    <a href="${beanUrl}/list">List</a>&nbsp;|&nbsp;
	<form action="${beanUrl}" method="post">
		<table border="1">
<tr>
<td class="fieldrow" id="fieldrow_id" name="fieldrow_id">
  <label for="id">Page ID</label>
</td>
<td>
  <input type="text" id="id" name="id" value='<c:out value="${bean.id}" />' placeholder="Page ID" readonly="readonly" />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_title" name="fieldrow_title">
  <label for="title">Title</label>
</td>
<td>
  <input type="text" id="title" name="title" value='<c:out value="${bean.title}" />' placeholder="Title"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_url" name="fieldrow_url">
  <label for="url">Url</label>
</td>
<td>
  <input type="text" id="url" name="url" value='<c:out value="${bean.url}" />' placeholder="Url"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_description" name="fieldrow_description">
  <label for="description">Description</label>
</td>
<td>
  <input type="text" id="description" name="description" value='<c:out value="${bean.description}" />' placeholder="Description"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_content" name="fieldrow_content">
  <label for="content">Content</label>
</td>
<td>
  <input type="text" id="content" name="content" value='<c:out value="${bean.content}" />' placeholder="Content"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_tags" name="fieldrow_tags">
  <label for="tags">Tags</label>
</td>
<td>
  <input type="text" id="tags" name="tags" value='<c:out value="${bean.tags}" />' placeholder="Tags"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_type" name="fieldrow_type">
  <label for="type">Type</label>
</td>
<td>
  <input type="text" id="type" name="type" value='<c:out value="${bean.type}" />' placeholder="Type"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_status" name="fieldrow_status">
  <label for="status">Status</label>
</td>
<td>
  <input type="text" id="status" name="status" value='<c:out value="${bean.status}" />' placeholder="Status"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_author" name="fieldrow_author">
  <label for="author">Author</label>
</td>
<td>
  <!-- Cloudflare setting -->
  <!--email_off-->
  <select name="author">
    <option value="0">Select Author Id...</option>
    <c:forEach items='${lookupMap.get("author")}' var="lookupBean">
      <option value='<c:out value="${lookupBean.id}"/>'
      <c:if test="${bean.author.id == lookupBean.id}">selected="true"</c:if>
      ><c:out value="${lookupBean.displayValue}"/> [<c:out value="${lookupBean.id}"/>]</option>
    </c:forEach>
  </select>
  <!--/email_off-->
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_parent" name="fieldrow_parent">
  <label for="parent">Parent Page</label>
</td>
<td>
  <!-- Cloudflare setting -->
  <!--email_off-->
  <select name="parent">
    <option value="0">Select Parent Page Id...</option>
    <c:forEach items='${lookupMap.get("parent")}' var="lookupBean">
      <option value='<c:out value="${lookupBean.id}"/>'
      <c:if test="${bean.parent.id == lookupBean.id}">selected="true"</c:if>
      ><c:out value="${lookupBean.displayValue}"/> [<c:out value="${lookupBean.id}"/>]</option>
    </c:forEach>
  </select>
  <!--/email_off-->
</td>
</tr>
		</table>
        <div>
			<input class="button" type="submit" value="Submit">
			<c:choose>
                <c:when test="${bean.id==null}">
                    <input class="button" type="button" onclick="window.location='${beanUrl}/list';return false;" value="Cancel" />
                </c:when>
                <c:otherwise>
                    <input class="button" type="button" onclick="window.location='${beanUrl}/show/<c:out value="${bean.id}" />';return false;" value="Cancel" />
                </c:otherwise>
            </c:choose>
		</div>
	</form>
</body>
</html>