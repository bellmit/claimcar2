<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<link href="h-ui/css/H-ui.min.css" rel="stylesheet" type="text/css" />
		<link href="h-ui/css/H-ui.admin.css" rel="stylesheet" type="text/css" />
		<title>无权限登录该页面</title>
	</head>
	<body>
		<header class="Hui-header cl">
			<a class="Hui-logo l" title="H-ui.admin v2.3" href="#"><img
					src="images/logo.png" /></a>
		</header>
		<div class="Hui-article-box">
			<b>您的权限不足，不能查看该页面！</b>
			<div><a href="logout.do">退出系统</a></div>
		</div>
	</body>
	<script type="text/javascript" src="h-ui/js/H-ui.js"></script>
	<script type="text/javascript" src="h-ui/js/H-ui.admin.js"></script>
</html>
