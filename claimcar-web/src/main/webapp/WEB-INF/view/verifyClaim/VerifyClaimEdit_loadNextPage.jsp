<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>下一个节点任务</title>
</head>
<body>
	<div class="table_wrap">
		<div class="table_cont table_list">
			<table class="table table-border table-hover">
				<thead>
					<tr class="text-c">
						<th>序号</th>
						<th>任务名称</th>
						<th>报案号</th>
						<!-- <th>标的名称</th> -->
						<th>提交人</th>
						<th>提交时间</th>
						<th>指定处理人</th>
						<th>处理</th>
					</tr>
				</thead>
				<tbody>
						<tr class="text-c">
							<td>1</td>
							<td>${wfTaskVo.taskName}</td>
							<td>${wfTaskVo.registNo}</td>
							<%-- <td>${wfTaskVo.itemName}</td> --%>
							<td>
								<app:codetrans codeType="UserCode" codeCode="${wfTaskVo.taskInUser}"/>
							</td>
							<td>
								<fmt:formatDate value="${wfTaskVo.taskInTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</td>
							<td>
								<app:codetrans codeType="UserCode" codeCode="${wfTaskVo.assignUser}"/>
							</td>
							<td>
								<%-- <a href="" onclick="nextTaskEdit('${wfTaskVo.taskName}','${wfTaskVo.taskId}')">任务处理</a> --%>
							</td>
						</tr>
				</tbody>
			</table>
		</div>
	</div>
	
	<script type="text/javascript">
		function nextTaskEdit(taskName,taskId){
			var goUrl = "/claimcar/verifyClaim/verifyClaimEdit.do?flowTaskId="+taskId;
			var title = taskName+"处理";
			openTaskEditWin(title,goUrl);
		};
	</script>
</body>
</html>
