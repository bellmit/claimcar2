<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
	<title></title>
	<base target="_self">
	<meta name="referrer" content="no-referrer|no-referrer-when-downgrade|origin|origin-when-crossorigin|unsafe-url">
</head>
<body>
正在访问影像系统
<form id="jyform" method="post" name="fm">
	<input type="hidden" class="common" id="url" name="url" value="${url}">


</form>

<script type="text/javascript">
	var url = $("#url").val();
	fm.action = url;
	fm.submit();
</script>
</body>
</html>
