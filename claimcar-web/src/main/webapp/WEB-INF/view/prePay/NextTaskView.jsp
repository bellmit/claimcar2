<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
					 		<thead>
					 			<tr>
					 				<th>序号</th>
					 				<th>任务名称</th>
					 				<th>业务号</th>
					 				<th>标的名称</th>
					 				<th>提交人</th>
					 				<th>提交时间</th>
					 				<th>指定处理人</th>
					 				<th>处理</th>
					 			</tr>
					 		</thead>
					 		<tbody>
					 			<c:forEach var="wfTaskVo" items="${wfTaskVoList}" varStatus="status">
					 				 <tr>
					 					<td> ${status.index+1}</td>
					 					<td> ${wfTaskVo.taskName}</td>
					 					<td> ${wfTaskVo.registNo}
					 						<input type="hidden" id="taskInKey" value="${wfTaskVo.taskInKey }">
					 						<input type="hidden" id="nodeCode" value="${wfTaskVo.subNodeCode }">
					 						<input type="hidden" id="flowTaskId" value="${wfTaskVo.taskId }">
					 						
					 					</td>
					 					<td> ${wfTaskVo.itemName}</td>
					 					<td> ${wfTaskVo.taskInUser}</td>
					 					<td><fmt:formatDate value="${wfTaskVo.taskInTime}" pattern="yyyy-MM-dd HH:mm"/></td>
					 					<td> ${wfTaskVo.assignUser}</td>
					 					<td>
						 				 	<c:if test="${wfTaskVo.subNodeCode ne 'END'}">  <%-- 建议加入一个变量 判定是否有权限或者下一个节点是END--%>
						 						<a target="_blank"  onClick='openWin()'  >处理任务</a>
						 					</c:if>	
					 					</td>
					 				 </tr>
								</c:forEach>
					 		</tbody>
					 </table>
					 <div class="text-c">
						<br/>
						<input class="btn btn-primary" id="return" onClick="returnWin()" type="submit" value="返回">
					</div>
				</div>
			</div>
			<script type="text/javascript">
			</script>
</body>
</html>
