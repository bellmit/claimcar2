<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>查勘费查看</title>
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
					<th>查勘机构</th>
					<th>任务详情</th>
					<th>费用金额</th>
				</tr>
			</thead>
				<tbody id="propLossTbody">
					<c:forEach var="checkFeeVo" items="${prpLCheckFeeVos}" varStatus="status">
					<tr class="text-c">
						<td><a  onclick="CheckFeeButton'${checkFeeVo.taskId}')">${checkFeeVo.taskNumber}</a></td>
						<td>${checkFeeVo.registNo}</td>
						<td>${checkFeeVo.compensateNo}</td>
						<td>${checkFeeVo.checkname}</td>
						<td>${checkFeeVo.taskDetail}</td>
						<td>${checkFeeVo.amount}
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