<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>结案信息列表</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />

</head>
<body>
  <div class="table_cont">
  <div class="table_wrap">
     <div class="table_title f14">损失赔偿情况列表</div>
			<table class="table table-border" border="1" id="">
				<thead class="text-c">
					<th>损失赔偿序号</th>
					<th>追偿/清付标志</th>
					<th>赔偿险种代码</th>
					<th>损失赔偿类型</th>
					<th>赔偿责任比例</th>
					<th>赔偿金额</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="subrogation" items="${resultList.subrogationClaimCloseDataVo.claimCloseOverDataList }" varStatus="status">
						<tr>
							<td>${subrogation.serialNo }</td>
							<td>${subrogation.recoveryOrPayFlag}</td>
							<td>${subrogation.coverageCode }</td>
							<td>${subrogation.claimFeeType }</td>
							<td>${subrogation.liabilityRate}</td>
							<td>${subrogation.claimAmount }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>	
		</div>
		
		<div class="table_wrap">
     <div class="table_title f14">追偿/清付信息列表</div>
			<table class="table table-border" border="1" id="">
				<thead class="text-c">
					<th>追偿/清付序号</th>
					<th>追偿/清付标志</th>
					<th>追偿/清付类型</th>
					<th>追偿/清付人</th>
					<th>结算码</th>
					<th>追偿/清付金额</th>
					<th>清付备注</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="subrogation" items="${resultList.subrogationClaimCloseDataVo.closeRecoveryOrPayDataList }" varStatus="status">
						<tr>
							<td>${subrogation.serialNo }</td>
							<td>${subrogation.recoveryOrPayFlag}</td>
							<td>${subrogation.recoveryOrPayType }</td>
							<td>${subrogation.recoveryOrPayMan }</td>
							<td>${subrogation.recoveryCode}</td>
							<td>${subrogation.recoveryOrPayAmount }</td>
							<td>${subrogation.recoveryRemark }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>	
		</div>
	
	</div>
</body>
</html>
