<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Excel导入导出处理</title>
</head>
<body>
    <form>
		<input type="hidden" id="infomsg" name="infomsg" value="${infomsg }">
	</form>
	<script type="text/javascript">
		$(document).ready(function() {
			var infomsg = document.getElementById("infomsg").value;
			if (infomsg == "4") {
				layer.alert("Excel已成功导出到D盘！", function(){
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
				parent.layer.close(index); //再执行关闭 
				});
			/* 	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
				parent.layer.close(index); //再执行关闭  */
			} else if(infomsg == "5"){
				layer.alert("Excel导出失败！", function(){
					var index = parent.layer.getFrameIndex(window.name); 
						parent.layer.close(index);
				});
			} 
		
		});
	</script>
</body>
</html>
</html>