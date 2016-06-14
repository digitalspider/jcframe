<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Add New User</title>
</head>
<body>
	<form action="${beanUrl}" method="post">
		<fieldset>
<div class="fieldrow" id="fieldrow_id" name="fieldrow_id">
  <label for="id">User ID</label>
  <input type="text" id="id" name="id" value='<c:out value="${bean.id}" />' placeholder="User ID" readonly="readonly"/>  />
</div>
<div class="fieldrow" id="fieldrow_email" name="fieldrow_email">
  <label for="email">Email</label>
  <input type="text" id="email" name="email" value='<c:out value="${bean.email}" />' placeholder="Email"  />
</div>
<div class="fieldrow" id="fieldrow_mobile" name="fieldrow_mobile">
  <label for="mobile">Mobile</label>
  <input type="text" id="mobile" name="mobile" value='<c:out value="${bean.mobile}" />' placeholder="Mobile"  />
</div>
<div class="fieldrow" id="fieldrow_lastname" name="fieldrow_lastname">
  <label for="lastname">Lastname</label>
  <input type="text" id="lastname" name="lastname" value='<c:out value="${bean.lastname}" />' placeholder="Lastname"  />
</div>
<div class="fieldrow" id="fieldrow_image" name="fieldrow_image">
  <label for="image">Image</label>
  <input type="text" id="image" name="image" value='<c:out value="${bean.image}" />' placeholder="Image"  />
</div>
<div class="fieldrow" id="fieldrow_password" name="fieldrow_password">
  <label for="password">Password</label>
  <input type="text" id="password" name="password" value='<c:out value="${bean.password}" />' placeholder="Password"  />
</div>
<div class="fieldrow" id="fieldrow_url" name="fieldrow_url">
  <label for="url">Url</label>
  <input type="text" id="url" name="url" value='<c:out value="${bean.url}" />' placeholder="Url"  />
</div>
<div class="fieldrow" id="fieldrow_token" name="fieldrow_token">
  <label for="token">Token</label>
  <input type="text" id="token" name="token" value='<c:out value="${bean.token}" />' placeholder="Token"  />
</div>
<div class="fieldrow" id="fieldrow_type" name="fieldrow_type">
  <label for="type">Type</label>
  <input type="text" id="type" name="type" value='<c:out value="${bean.type}" />' placeholder="Type"  />
</div>
<div class="fieldrow" id="fieldrow_tags" name="fieldrow_tags">
  <label for="tags">Tags</label>
  <input type="text" id="tags" name="tags" value='<c:out value="${bean.tags}" />' placeholder="Tags"  />
</div>
<div class="fieldrow" id="fieldrow_mdate" name="fieldrow_mdate">
  <label for="mdate">Mdate</label>
  <input type="text" id="mdate" name="mdate" value='<c:out value="${bean.mdate}" />' placeholder="Mdate"  />
</div>
<div class="fieldrow" id="fieldrow_username" name="fieldrow_username">
  <label for="username">Username</label>
  <input type="text" id="username" name="username" value='<c:out value="${bean.username}" />' placeholder="Username"  />
</div>
<div class="fieldrow" id="fieldrow_status" name="fieldrow_status">
  <label for="status">Status</label>
  <input type="text" id="status" name="status" value='<c:out value="${bean.status}" />' placeholder="Status"  />
</div>
<div class="fieldrow" id="fieldrow_description" name="fieldrow_description">
  <label for="description">Description</label>
  <input type="text" id="description" name="description" value='<c:out value="${bean.description}" />' placeholder="Description"  />
</div>
<div class="fieldrow" id="fieldrow_firstname" name="fieldrow_firstname">
  <label for="firstname">Firstname</label>
  <input type="text" id="firstname" name="firstname" value='<c:out value="${bean.firstname}" />' placeholder="Firstname"  />
</div>
<div class="fieldrow" id="fieldrow_cdate" name="fieldrow_cdate">
  <label for="cdate">Cdate</label>
  <input type="text" id="cdate" name="cdate" value='<c:out value="${bean.cdate}" />' placeholder="Cdate"  />
</div>

		</fieldset>
	</form>
</body>
</html>