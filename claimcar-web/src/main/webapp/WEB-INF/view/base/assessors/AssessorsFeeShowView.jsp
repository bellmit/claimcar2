<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>公估费查看</title>
</head>
<body>
	<div class="table_wrap">
	<div class="table_cont table_list">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th>任务号</th>
					<th>报案号</th>
					<th>计算书号</th>
					<th>公估机构</th>
					<th>任务详情</th>
					<th>费用金额</th>
				</tr>
			</thead>
				<tbody id="propLossTbody">
					<c:forEach var="assessorFeeVo" items="${prpLAssessorFeeVos}" varStatus="status">
					<tr class="text-c">
						<td><a  onclick="IntermFeeButton('${assessorFeeVo.taskId}')">${assessorFeeVo.taskNumber}</a></td>
						<td>${assessorFeeVo.registNo}</td>
						<td>${assessorFeeVo.compensateNo}</td>
						<td>${assessorFeeVo.intermname}</td>
						<td>${assessorFeeVo.taskDetail}</td>
						<td>${assessorFeeVo.amount}
					</tr>
					</c:forEach>
				</tbody>
			</table>
	</div>
</div>

	<script type="text/javascript">
		
	</script>
</body>
</html>