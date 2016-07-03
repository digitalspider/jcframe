<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Add New Student</title>

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
}
function removeLookup(selectName) {
    var selectActual = document.getElementById(selectName);
    var selectLookup = document.getElementById(selectName+"-lookup");
    for (var i=selectActual.options.length; --i>=0; ) {
        var option = selectActual.options[i];
        if (option.selected) {
            selectLookup.appendChild(option);
        }
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
  <label for="id">Student ID</label>
</td>
<td>
  <input type="text" id="id" name="id" value='<c:out value="${bean.id}" />' placeholder="Student ID" readonly="readonly" />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_firstName" name="fieldrow_firstName">
  <label for="firstName">FirstName</label>
</td>
<td>
  <input type="text" id="firstName" name="firstName" value='<c:out value="${bean.firstName}" />' placeholder="FirstName"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_lastName" name="fieldrow_lastName">
  <label for="lastName">LastName</label>
</td>
<td>
  <input type="text" id="lastName" name="lastName" value='<c:out value="${bean.lastName}" />' placeholder="LastName"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_course" name="fieldrow_course">
  <label for="course">Course</label>
</td>
<td>
  <input type="text" id="course" name="course" value='<c:out value="${bean.course}" />' placeholder="Course"  />
</td>
</tr>
<tr>
<td class="fieldrow" id="fieldrow_year" name="fieldrow_year">
  <label for="year">Year</label>
</td>
<td>
  <input type="text" id="year" name="year" value='<c:out value="${bean.year}" />' placeholder="Year"  />
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