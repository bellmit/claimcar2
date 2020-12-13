<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>保单信息</title>
</head>
<body>
	<div class="table_wrap">
	<input type="hidden" id="checkMainId" value="${checkId}">
	<input type="hidden" id="lossFeeSum" value="${lossFees}">
	<div class="table_cont table_list">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th>保单号</th>
					<th>车牌号</th>
					<th>车型名称</th>
					<th>被保险人</th>
					<th>客户等级</th>
					<th>起保日期</th>
					<th>车终保日期</th>
					<th>保单类型</th>
				</tr>
			</thead>
				<tbody id="propLossTbody">
					<input type="hidden" id=checkId value="${checkId}">
					<c:forEach var="policyInfo" items="${policyInfos}" varStatus="status">
					<tr class="text-c">
					<td><a id="policyNo_${status.index}" data-hasqtip="0" target="_blank"
								href="/claimcar/policyView/policyView.do?registNo=${policyInfo.registNo}">${policyInfo.policyNo}</a>
								<div class="hide" id="RiskKindLayer_${status.index}" style="display: none">
									<table class="table table-border table-hover">
										<thead>
											<tr>
												<th style="white-space: nowrap;">代码</th>
												<th style="white-space: nowrap;">险别名称</th>
												<th style="white-space: nowrap;">保险金额</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="cItemKinds" items="${policyInfo.prpCItemKinds}" varStatus="status">
												<tr>
													<td>${cItemKinds.kindCode}</td>
													<td nowrap="nowrap">${cItemKinds.kindName}</td>
													<td>${cItemKinds.amount}</td>
												</tr>
											</c:forEach>

										</tbody>
									</table>
								</div>
							</td>

							<!-- <td><button class="btn btn-secondary">显示险别信息</button> </td> -->
							<td>${policyInfo.prpCItemCars[0].licenseNo}</td>
							<td>${policyInfo.prpCItemCars[0].brandName}</td>
							<td>${policyInfo.prpCInsureds[0].insuredName}</td>
							<td>VIP客户</td>
							<td><app:date date='${policyInfo.startDate}'/></td>
							<td><app:date date='${policyInfo.endDate}'/></td>
							<td>
								<c:choose>
									<c:when test="${policyInfo.riskCode eq '1101'}">
									    		交强
									    		<input type="hidden" id="CIPolicyNo"
											value="${policyInfo.policyNo}" />
									</c:when>
									<c:otherwise>
									       		商业
									       		<input type="hidden" id="BIPolicyNo"
											value="${policyInfo.policyNo}" />
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</div>
</div>

	<script type="text/javascript">
		//qtip显示险别信息
		$(function() {
			$("#policyNo_1").qtip({
				content : {
					title : "险别信息",
					text : $("#RiskKindLayer_1"),
					button : true
				},
				hide : {
					fixed : true,
					delay : 300
				},
				style : {
					classes : 'qtip-blue'
				}
			});
		});
		$(function() {
			$("#policyNo_0").qtip({
				content : {
					title : "险别信息",
					text : $("#RiskKindLayer_0"),
					button : true
				},
				hide : {
					fixed : true,
					delay : 300
				},
				style : {
					classes : 'qtip-blue'
				}
			});
		});
	</script>
</body>
</html>