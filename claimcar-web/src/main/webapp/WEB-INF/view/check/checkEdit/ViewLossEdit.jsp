<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>定损处理</title>
</head>
<body>
	<div class="table_wrap">
	<div class="table_cont table_list">
		<%-- <p>提交成功！报案号：${wfTaskVoList[0].registNo}</p> --%>
			<table class="table table-border">
				<thead>
					<tr class="text-c">
						<th>序号</th>
						<th>任务类型</th>
						<th>损失项</th>
						<th>业务号</th>
						<th>提交人</th>
						<th>指定处理人</th>
						<th>处理</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="wfTaskVo" items="${wfTaskVoList}" varStatus="status">
						<tr class="text-c">
							<input type="hidden" value="${wfTaskVo.taskId}">
							<td>${status.index+1}</td>
							<c:choose>
								<c:when test="${wfTaskVo.subNodeCode eq 'DLCar'}">
									<td>车辆定损</td>
								</c:when>
								<c:when test="${wfTaskVo.subNodeCode eq 'DLProp'}">
									<td>财产定损</td>
								</c:when>
								<c:otherwise>
									<td>大案审核一级</td>
								</c:otherwise>
							</c:choose>
							<td>${wfTaskVo.itemName}</td>
							<td>${wfTaskVo.registNo}</td>
							<td><app:codetrans codeType="UserCode" codeCode="${wfTaskVo.taskInUser}"/></td>
							<td><app:codetrans codeType="UserCode" codeCode="${wfTaskVo.assignUser}"/></td>
							<td>
								<%-- <input type="button" class="btn btn-secondary btn-kk"
								onclick="openLossTaskWin('${wfTaskVo.subNodeCode','${wfTaskVo.taskId')"
								value="处理"> --%>
								<%-- <a class="btn btn-secondary" style="margin-left: 45%;" href="javascript:;" 
								onclick="openLossTaskWin('${wfTaskVo.subNodeCode','${wfTaskVo.taskId')">处理</a> --%>
								<%-- <a target="_blank" class="btn btn-secondary" href="javascript:;" onclick="openLossTaskWin('${wfTaskVo.subNodeCode}','${wfTaskVo.taskId}')">处理任务</a> --%>
								<c:if test="${wfTaskVo.subNodeCode ne 'ChkBig_LV1'}">
									<a href="javascript:;" onclick="openLossTaskWin('${wfTaskVo.subNodeCode}','${wfTaskVo.taskId}')">处理任务</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<!-- <input type="button" class="btn btn-secondary btn-kk" onclick="checkCancel()" value="关闭"> -->
		<br/>
		<!-- <a class="btn btn-secondary" style="margin-left: 45%;" href="javascript:;" onclick="checkCancel()">关闭</a> -->
	</div>
</div>
<script type="text/javascript" src="/claimcar/js/checkEdit/check.js"></script>
<script type="text/javascript" src="/claimcar/h-ui/js/H-ui.admin.js"></script>
<!-- <script type="text/javascript" src="/claimcar/js/quickclaim/quickclaim.js"></script> -->
	<script type="text/javascript">
		//定损任务处理
		function openLossTaskWin(node, taskId) {
			var title = "";
			var goUrl = "";
			if ("DLProp" == node) {
				title = "财产定损";
				goUrl = "/claimcar/proploss/initPropCertainLoss.do?flowTaskId="
						+ taskId;
			} else if ("DLCar" == node) {
				title = "车辆定损";
				goUrl = "/claimcar/defloss/preAddDefloss.do?flowTaskId="
						+ taskId;
			} else {
				title = "大案审核处理";
				goUrl = "/claimcar/check/initCheck.do?flowTaskId="
						+ taskId;
			}
			openWinCom(title, goUrl);
		}
	</script>
</body>
</html>