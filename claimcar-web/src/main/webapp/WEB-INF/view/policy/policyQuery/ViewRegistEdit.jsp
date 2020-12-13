<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>报案处理</title>
</head>
<body>
	<div class="table_wrap">
	<div class="table_cont table_list">
		<label class="form_label col-10 text-c">保单号为<font class="c-red">${policyNo }</font>的保单，有以下${fn:length(wfTaskVos)}条未录完的报案</label>
		<table class="table table-border">
			<thead>
				<tr class="text-c">
					<th>报案时间</th>
					<th>报案类型</th>
					<th>报案号</th>
					<th>操作员</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach  var="wfTaskVo" items="${wfTaskVos}" varStatus="status">
				<tr class="text-c">
					<input type="hidden" value="${wfTaskVo.taskId}">
					<td><fmt:formatDate value="${wfTaskVo.taskInTime}" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" /></td>
					<td>正式报案</td>
					<td>${wfTaskVo.registNo}</td>
					<td><app:codetrans codeType="UserCode" codeCode="${wfTaskVo.taskInUser }"/></td>
					<td>
						<a href="javascript:;" onclick="openRegisTaskWin('${wfTaskVo.registNo}','${wfTaskVo.flowId}','${wfTaskVo.taskId}')">继续报案</a>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

	<script type="text/javascript">

		function openRegisTaskWin(registNo,flowId, taskId) {
			var goUrl = "/claimcar/regist/edit.do?flowId="+flowId+"&flowTaskId="+taskId+"&taskInKey="+registNo+"&handlerIdKey="+registNo+"&registNo="+registNo;
			//openTaskEditWin('报案处理',goUrl,true);
			parent.location.href = goUrl;
		}
	</script>
</body>
</html>