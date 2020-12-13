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
							<th>车架号</th>
							<th>号牌种类</th>
							<th>号牌号码</th>
							<th>风险类型代码</th>
							<th>风险信息来源</th>
							<th>风险信息入库时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="prpLAntiFraudVo" items="${prpLAntiFraudVos}">
							<tr class="text-c">
								<td>${prpLAntiFraudVo.reportNo}</td>
								<td>${prpLAntiFraudVo.vinNo}</td>
								<c:if test="${prpLAntiFraudVo.vehicleType != '99' && prpLAntiFraudVo.vehicleType != '51' && prpLAntiFraudVo.vehicleType != '52'}">
			  						<td><app:codetrans codeType="LicenseKindCode" codeCode="${prpLAntiFraudVo.vehicleType}" /></td>
								</c:if>
								<c:if test="${prpLAntiFraudVo.vehicleType == '99'}">
			  						<td>其他</td>
								</c:if>
								<c:if test="${prpLAntiFraudVo.vehicleType == '51'}">
			  						<td>大型新能源汽车</td>
								</c:if>
								<c:if test="${prpLAntiFraudVo.vehicleType == '52'}">
			  						<td>小型新能源汽车</td>
								</c:if>
								<td>${prpLAntiFraudVo.carMark}</td>
								<td><app:codetrans codeType="VehicleRiskType" codeCode="${prpLAntiFraudVo.riskType}" /></td>
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
							<th width="8%">报案号</th>
							<th width="8%">保险公司</th>
							<th width="8%">承保地区</th>
							<th width="8%">报案时间</th>
							<th width="8%">出险时间</th>
							<th width="12%">出险地点</th>
							<th width="12%">出险经过</th>
							<th width="8%">案件状态代码</th>
							<th width="8%">保单类型代码</th>
							<th width="12%">理赔编码</th>
							<th width="8%">核损总金额</th>
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
								<td><app:codetrans codeType="PolicyTypeCode" codeCode="${prpLBasicsInfoVo.riskType}" /></td>
								<td>${prpLBasicsInfoVo.claimQueryNo}</td>
								<td>${prpLBasicsInfoVo.sumUnderDefLoss}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="table_title f14">车辆基本信息</div>
			<div class="table_cont " style="magrn-top:20px">
				<table class="table table-border table-hover">
					<thead>
						<tr class="text-c">
							<th>车辆属性</th>
							<th>号牌种类</th>
							<th>号牌号码</th>
							<th>发动机号</th>
							<th>车架号</th>
							<th>车损信息</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="prpLVehicleInfoVo" items="${vehicleInfoVos}">
							<tr class="text-c">
								<td><app:codetrans codeType="ItemType" codeCode="${prpLVehicleInfoVo.vehicleProperty}" /></td>
								<c:if test="${prpLVehicleInfoVo.vehicleType != '99' && prpLVehicleInfoVo.vehicleType != '51' && prpLVehicleInfoVo.vehicleType != '52'}">
			  						<td><app:codetrans codeType="LicenseKindCode" codeCode="${prpLVehicleInfoVo.vehicleType}" /></td>
								</c:if>
								<c:if test="${prpLVehicleInfoVo.vehicleType == '99'}">
			  						<td>其他</td>
								</c:if>
								<c:if test="${prpLVehicleInfoVo.vehicleType == '51'}">
			  						<td>大型新能源汽车</td>
								</c:if>
								<c:if test="${prpLVehicleInfoVo.vehicleType == '52'}">
			  						<td>小型新能源汽车</td>
								</c:if>
								<td>${prpLVehicleInfoVo.carMark}</td>
								<td>${prpLVehicleInfoVo.engineNo}</td>
								<td>${prpLVehicleInfoVo.vinNo}</td>
								<td><input type="button" class="btn btn-secondary"
									onclick="showDamageInfoDetails('${prpLVehicleInfoVo.upperId}')"
									value="详情"></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="table_title f14">财产损失基本信息</div>
			<div class="table_cont " style="magrn-top:20px">
				<table class="table table-border table-hover">
					<thead>
						<tr class="text-c">
							<th>损失财产名称</th>
							<th>财产属性</th>
							<th>核损金额（财产）</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="prpLPropertyLossVo" items="${prpLPropertyLossVos}">
							<tr class="text-c">
								<td>${prpLPropertyLossVo.protectName}</td>
								<td><app:codetrans codeType="PropertyAttrCode" codeCode="${prpLPropertyLossVo.protectProperty}" /></td>
								<td>${prpLPropertyLossVo.underDefLoss}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

		</div>

	</div>
	<script type="text/javascript">
		function showDamageInfoDetails(upperId) {
			index = layer
					.open({
						type : 2,
						title : '车损信息',
						//closeBtn : 2,
						//shadeClose : true,
						scrollbar : false,
						area : [ '90%', '80%' ],
						content : "/claimcar/realTimeQueryAction/showDamageInfoDetails.do?upperId="+ upperId,
						scrollbar : true
					});

		}
		
	</script>

</body>
</html>