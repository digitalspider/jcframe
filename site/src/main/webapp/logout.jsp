<%
String redirectUrl = "";
String refererUrl = request.getHeader("Referer");
if (refererUrl!=null) {
    String url = request.getRequestURL().toString();
    String contextUrl = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
    redirectUrl = "?redirect="+refererUrl.substring((""+contextUrl+"/jc").length());
}
%>
<script type="text/javascript">
window.location.href="<%=request.getContextPath()%>/jc/login/logout<%=redirectUrl%>"
</script>