<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title></title>
		<base target="_self">
</head>
<body>
正在访问影像系统
	<form  id="fm"  method="post"  name="fm" > 
		<input type="hidden" class="common"  id="url" name="url"       value="${url}" > 
		<input type="hidden" class="common"  id="data" name="data" > 
</form>

<script type="text/javascript">
var url = $("#url").val();
var arg = [];
arg = url.split("?data=");
$("#data").val(arg[1]);
fm.action=arg[0];
fm.submit();
//window.close(); 
</script>
</body>
</html>
