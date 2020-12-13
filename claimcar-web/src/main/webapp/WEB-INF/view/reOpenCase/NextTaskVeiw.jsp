<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>下一个节点的任务</title>
</head>
<body>
			<div class="table_wrap">
            	<div class="table_title f14">下一个节点任务</div>
				<div class="table_cont ">
					 <table class="table table-border table-hover">
					 	<input type="hidden" id="registNo" value="${registNo }">
					 		<thead class="text-c">
					 			<tr>
					 				<th>序号</th>
					 				<th>任务名称</th>
					 				<th>报案号</th>
					 			</tr>
					 		</thead>
					 		<tbody class="text-c">
					 				 <tr>
					 					<td> 1</td>
					 					<td> ${taskName}</td>
					 					<td> ${registNo}</td>
					 				 </tr>
					 		</tbody>
					 </table>
					 <div class="text-c">
						<br/>
						<input class="btn btn-primary" id="return" onClick="returnWin()" type="submit" value="返回">
					</div>
				</div>
			</div>
			<script type="text/javascript">
				
				function returnWin(){
					parent.location.reload();
					var index = parent.layer.getFrameIndex(window.name); 
					parent.layer.close(index);	
				}
			</script>
</body>
</html>
