<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<c:choose>
    <c:when test="${bean.id==null}">
        <c:set var="action" value="Add"/>
    </c:when>
    <c:otherwise>
        <c:set var="action" value="Edit"/>
    </c:otherwise>
</c:choose>

<title>${action} User</title>

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
  <label for="id">User ID</label>
</td>
<td>
  <input type="text" id="id" name="id" value='<c:out value="${bean.id}" />' placeholder="User ID" readonly="readonly" />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_username" name="fieldrow_username">
  <label for="username">Username</label>
</td>
<td>
  <input type="text" id="username" name="username" value='<c:out value="${bean.username}" />' placeholder="Username"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_email" name="fieldrow_email">
  <label for="email">Email</label>
</td>
<td>
  <input type="text" id="email" name="email" value='<c:out value="${bean.email}" />' placeholder="Email"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_firstname" name="fieldrow_firstname">
  <label for="firstname">Firstname</label>
</td>
<td>
  <input type="text" id="firstname" name="firstname" value='<c:out value="${bean.firstname}" />' placeholder="Firstname"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_lastname" name="fieldrow_lastname">
  <label for="lastname">Lastname</label>
</td>
<td>
  <input type="text" id="lastname" name="lastname" value='<c:out value="${bean.lastname}" />' placeholder="Lastname"  />
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
<td class="fieldrow" id="fieldrow_description" name="fieldrow_description">
  <label for="description">Description</label>
</td>
<td>
  <input type="text" id="description" name="description" value='<c:out value="${bean.description}" />' placeholder="Description"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_image" name="fieldrow_image">
  <label for="image">Image</label>
</td>
<td>
  <input type="text" id="image" name="image" value='<c:out value="${bean.image}" />' placeholder="Image"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_mobile" name="fieldrow_mobile">
  <label for="mobile">Mobile</label>
</td>
<td>
  <input type="text" id="mobile" name="mobile" value='<c:out value="${bean.mobile}" />' placeholder="Mobile"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_pages" name="fieldrow_pages">
  <label for="pages">Pages</label>
</td>
<td>
  <input type="text" id="pages" name="pages" value='<c:out value="${bean.pages}" />' placeholder="Pages" readonly="readonly" />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_password" name="fieldrow_password">
  <label for="password">Password</label>
</td>
<td>
  <input type="password" id="password" name="password" value='<c:out value="${bean.password}" />' placeholder="Password"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_roles" name="fieldrow_roles">
  <label for="roles">Roles</label>
</td>
<td>
  <!-- Cloudflare setting -->
  <!--email_off-->
  <select id="roles" name="roles" style="float: left" size="10" multiple="true">
    <c:forEach items='${bean.roles}' var="lookupBean">
      <option value='<c:out value="${lookupBean.id}"/>' selected="true"><c:out value="${lookupBean.displayValue}"/> [<c:out value="${lookupBean.id}"/>]</option>
    </c:forEach>
  </select>
  <div id="selectors" style="float: left">
    <a href="javascript:addLookup('roles')">&lt;</a>
    <a href="javascript:removeLookup('roles')">&gt;</a>
  </div>
  <select id="roles-lookup" name="roles-lookup" style="float: left" size="10" multiple="true">
    <c:forEach items='${lookupMap.get("roles")}' var="lookupBean">
      <c:if test="${!bean.roles.contains(lookupBean)}">
      <option value='<c:out value="${lookupBean.id}"/>'><c:out value="${lookupBean.displayValue}"/> [<c:out value="${lookupBean.id}"/>]</option>
      </c:if>
    </c:forEach>
  </select>
  <!--/email_off-->
</td>
</tr><tr>
<td class="fieldrow" id="fieldrow_status" name="fieldrow_status">
  <label for="status">Status</label>
</td>
<td>
  <input type="text" id="status" name="status" value='<c:out value="${bean.status}" />' placeholder="Status"  />
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
<td class="fieldrow" id="fieldrow_token" name="fieldrow_token">
  <label for="token">Token</label>
</td>
<td>
  <input type="text" id="token" name="token" value='<c:out value="${bean.token}" />' placeholder="Token"  />
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