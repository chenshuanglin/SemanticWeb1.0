<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tushu2</title>
<link href="style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
function $$(id)
{
	return document.getElementById(id);
}
function Show(id)
{
	$$(id).style.display="block";
}
function Close(id)
{
	$$(id).style.display="none";
}
</script>
</head>

<body>
<div id="containt">
<div class="box"><img src="img/logo.png" width="85%" height="85%"/></div>
<form action="search" method="get">
<input class="swap" type="text" maxlength="100" name="searchMessage">
<input class="wap" type="submit" value="search" id="sou" >
</form>
<div id="center">
	<p>当前:</p>
</div>

</div>
<div id="down">
	<address></address>
</div>
</body>
</html>
