<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="style.css" rel="stylesheet" type="text/css" />
<title>结果</title>
</head>
<body>
<div id="head">
<div class="boxl"><img src="img/logo.png"/></div>
<form action="search" method="get">
<input class="text" type="text" maxlength="100" name="searchMessage">
<input class="" type="submit" value="search" id="sou" >
</form>
<div style="clear:both"></div>
</div>
<div id="resultcontent">
<s:iterator value="list" status="st">
	<s:set name="index" value="#st.getIndex()"/>
<div class="boxs">
	<div class="top">
		<span style="color:red"><s:property value="title"/></span>
		<a href="<s:property value="url"/>" target="_blank"><s:property value="url"/></a>
	</div>
	<div class="texts">
		<s:property value="content"/>
	</div>
</div>
</s:iterator>
<div style="clear:both"></div>
</div>
</body>
</html>