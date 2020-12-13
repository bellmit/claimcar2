<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>查勘任务处理查询</title>

</head>
<body>
	
	<form>
		<input type="hidden" id="infomsg" name="infomsg" value="${infomsg }">
	</form>
	<script type="text/javascript">
		$(document).ready(function() {
			var infomsg = document.getElementById("infomsg").value;
			if (infomsg == "1") {
				layer.alert("导入成功", function(){
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
				parent.layer.close(index); //再执行关闭 
				}
);
			/* 	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
				parent.layer.close(index); //再执行关闭  */
			} else if(infomsg == "2"){
				layer.alert("导入失败", function(){
					var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
						parent.layer.close(index); //再执行关闭 
				});
			} else{
				layer.alert("导入失败请查看导入的数据是否正确!", function(){
					var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
						parent.layer.close(index); //再执行关闭 
				});
			}
		
		});
	</script>
</body>

</html>
