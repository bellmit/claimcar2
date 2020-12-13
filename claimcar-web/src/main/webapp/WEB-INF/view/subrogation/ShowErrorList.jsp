<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>此报案号没有对应信息,请重新录入</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />

</head>
<body>
   
	<script type="text/javascript">
			$(document).ready(function(){
				layer.confirm('此报案号没有对应信息,请重新录入', {
					btn : [ '确定', '取消' ]
				},
				function() {
					var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					parent.layer.close(index); //再执行关闭 
				}, function() {
					var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					parent.layer.close(index); //再执行关闭 
				});
				
			});
			</script>
</body>
</html>
