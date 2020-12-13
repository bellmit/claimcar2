<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>平台反欺诈信息</title>
</head>
<body>
	<div class="tabbox">
		<div id="tab-system" class="HuiTab">
			<div class="table_title f14">反欺诈风险信息列表</div>
			<div class="table_cont ">
				<table class="table table-border table-hover">
					<thead>  
						<tr class="text-c">
							<th>报案号</th>
							<th>电话号码</th>
							<th>风险类型代码</th>
							<th>风险信息来源</th>
							<th>风险信息入库时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="prpLAntiFraudVo" items="${prpLAntiFraudVos}">
							<tr class="text-c">
								<td>${prpLAntiFraudVo.reportNo}</td>
								<td>${prpLAntiFraudVo.reportPhoneNo}</td>
								<td><app:codetrans codeType="PhoneRiskType" codeCode="${prpLAntiFraudVo.riskType}" /></td>
								<td><app:codetrans codeType="RiskSourceType" codeCode="${prpLAntiFraudVo.riskSource}" /></td>
								<td><fmt:formatDate value="${prpLAntiFraudVo.antiFraudTime}" pattern="yyyy-MM-dd"/></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="table_title f14">理赔基本信息</div>
			<div class="table_cont " style="magrn-top:20px">
				<table class="table table-border table-hover">
					<thead>
						<tr class="text-c">
							<th width="10%">报案号</th>
							<th width="10%">保险公司</th>
							<th width="10%">承保地区</th>
							<th width="10%">报案时间</th>
							<th width="10%">出险时间</th>
							<th width="20%">出险地点</th>
							<th width="20%">出险经过</th>
							<th width="10%">案件状态代码</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="prpLBasicsInfoVo" items="${prpLBasicsInfoVos}">
							<tr class="text-c">
								<td>${prpLBasicsInfoVo.reportNo}</td>
								<td><app:codetrans codeType="OrganizationCode" codeCode="${prpLBasicsInfoVo.claimcomPany}" /></td>
								<td><app:codetrans codeType="DWInsurerArea" codeCode="${prpLBasicsInfoVo.insurerArea}" /></td>
								<td><fmt:formatDate value="${prpLBasicsInfoVo.reportTime}" pattern="yyyy-MM-dd"/></td>
								<td><fmt:formatDate value="${prpLBasicsInfoVo.accidentTime}" pattern="yyyy-MM-dd"/></td>
								<td>${prpLBasicsInfoVo.accidentPlace}</td>
								<td>${prpLBasicsInfoVo.accidentDescription}</td>
								<td><app:codetrans codeType="ClaimStatus" codeCode="${prpLBasicsInfoVo.claimStatus}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="table_title f14">报案电话集合</div>
			<div class="table_cont " style="magrn-top:20px">
				<table class="table table-border table-hover">
					<tbody>
						<c:forEach var="prpLPropertyLossVo" items="${prpLPropertyLossVos}">
							<tr class="text-c">
								<td width="50%">报案电话</td>
								<td width="50%">${prpLPropertyLossVo.reportPhoneNo}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

		</div>

	</div>
</body>
</html>