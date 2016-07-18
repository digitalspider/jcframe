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

<title>${action} ${beanName}</title>

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
##FIELDS##
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