<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>三者车信息</title>

</head>
<body>
	<div class="page_wrap">
		<div class="table_title">代位求偿锁定报案三者车信息列表</div>
			<input type="hidden" name="lockedPolicyVo.registNo"  id ="registNo" value="${lockedPolicyVo.registNo }">
			<input type="hidden" name="lockedPolicyVo.claimSequenceNo" id ="claimSequenceNo" value="${lockedPolicyVo.claimSequenceNo }">
			<input type="hidden" name="lockedPolicyVo.policyNo" id ="oppoentPolicyNo" value="${lockedPolicyVo.policyNo }">
			<input type="hidden" name="lockedPolicyVo.coverageType" id ="coverageType" value="${lockedPolicyVo.coverageType }">
		<div class="table_cont">
			<table class="table table-border" border="1" id="">
				<thead class="text-c">
					<th>序号</th>
					<th>责任对方报案号</th>
					<th>责任对方三者车号牌号码</th>
					<th>责任对方三者车号牌种类</th>
					<th>责任对方三者车发动机号</th>
					<th>责任对方三者车VIN码</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="carVo" items="${thirdPartyList }" varStatus="status">
						<tr>
							<td>${status.index+1 }</td>
							<td>${claimNotificationNo }</td>
							<td>${carVo.licensePlateNo }</td>
							<td>${carVo.licensePlateType }</td>
							<td>${carVo.engineNo }</td>
							<td>${carVo.vin }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>	
		</div>
	</div>	
	<script type="text/javascript" src="/claimcar/js/subrogation/lockConfirm.js"></script>	
</body>
</html>
