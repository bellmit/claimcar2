<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>锁定报案信息</title>

</head>
<body>
	<div class="page_wrap">
		<div class="table_title">代位求偿锁定报案信息列表</div>
		<form id="lockform" name="form" class="form-horizontal" role="form" method="post">
			<input type="hidden" name="lockedPolicyVo.registNo"  id ="registNo" value="${lockedPolicyVo.registNo }">
			<input type="hidden" name="lockedPolicyVo.claimSequenceNo" id ="claimSequenceNo" value="${lockedPolicyVo.claimSequenceNo }">
			<input type="hidden" name="lockedPolicyVo.policyNo" id ="oppoentPolicyNo" value="${lockedPolicyVo.policyNo }">
			<input type="hidden" name="lockedPolicyVo.coverageType" id ="coverageType" value="${lockedPolicyVo.coverageType }">
		<div class="table_cont">
			<table class="table table-border" border="1" id="">
				<thead class="text-c">
					<th>选择</th>
					<th>理赔编码</th>
					<th>报案号</th>
					<th>责任对方保单号</th>
					<th>责任对方报案号</th>
					<th>责任对方车牌号码</th>
					<th>出险时间</th>
					<th>出险地点	</th>
					<th>出险经过</th>
					<th>案件状态</th>
					<th>三者车辆信息</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="lockedNotifyVo" items="${lockedNotifyList }" varStatus="status">
						<tr>
							<td><input type="checkBox" name="checkboxAssess"></td>
							<td>${lockedNotifyVo.claimSequenceNo }</td>
							<td>${lockedPolicyVo.registNo }</td>
							<td>${lockedPolicyVo.policyNo }</td>
							<td>${lockedNotifyVo.claimNotificationNo }</td>
							<td>${lockedPolicyVo.licensePlateNo }</td>
							<td>${lockedNotifyVo.lossTime }</td>
							<td>${lockedNotifyVo.lossArea }</td>
							<td>${lockedNotifyVo.lossDesc }</td>
							<td><app:codetrans codeType="ClaimStatus" codeCode="${lockedNotifyVo.claimStatus }"/> </td>
							<td><a href='javascript:' onclick="showThirdCarData('${lockedNotifyVo.claimNotificationNo}')">三者车辆信息</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>	
		</div>
		<div>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkBox" id="checkAll">全部选择
		</div>
		<div class="text-c">
			<input type="button" class="btn btn-primary" onclick="lockSave()"  value="锁定确认" />
		</div>
		</form>
	</div>	
	<script type="text/javascript" src="/claimcar/js/subrogation/lockConfirm.js"></script>	
	<script type="text/javascript" src="/claimcar/js/common/AjaxEdit.js"></script>
</body>
</html>
