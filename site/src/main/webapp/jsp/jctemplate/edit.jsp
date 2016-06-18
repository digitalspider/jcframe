<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Add New ${beanName}</title>
</head>
<body>
	<form action="${beanUrl}" method="post">
		<fieldset>
##FIELDS##
		</fieldset>
        <div>
			<input class="button" type="submit" value="Submit">
			<input class="button" type="button" onclick="window.location='${beanUrl}';return false;" value="Cancel" />
		</div>
	</form>
</body>
</html>