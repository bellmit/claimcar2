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
							<th>人员姓名</th>
							<th>证件类型</th>
							<th>证件号码</th>
							<th>风险类型代码</th>
							<th>风险信息来源</th>
							<th>风险信息入库时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="prpLAntiFraudVo" items="${prpLAntiFraudVos}">
							<tr class="text-c">
								<td>${prpLAntiFraudVo.reportNo}</td>
								<td>${prpLAntiFraudVo.antiFraudName}</td>
								<td><app:codetrans codeType="CertifyTypeForRisk" codeCode="${prpLAntiFraudVo.certiType}" /></td>
								<td>${prpLAntiFraudVo.certiCode}</td>
								<td><app:codetrans codeType="PersonRiskType" codeCode="${prpLAntiFraudVo.riskType}" /></td>
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
			<div class="table_title f14">出险车辆相关信息</div>
			<div class="table_cont " style="magrn-top:20px">
				<table class="table table-border table-hover">
					<thead>
						<tr class="text-c">
							<th>出险车辆号牌号码</th>
							<th>出险车辆号牌种类代码</th>
							<th>出险车辆发动机号</th>
							<th>出险车辆VIN码</th>
							<th>车辆型号</th>
							<th>出险驾驶员姓名</th>
							<th>出险驾驶员证件类型</th>
							<th>出险驾驶员证件号码</th>
							<th>出险车辆驾驶证号码</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="prpLVehicleInfoVo" items="${vehicleInfoVos}">
							<tr class="text-c">
								<td>${prpLVehicleInfoVo.carMark}</td>
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
								<td>${prpLVehicleInfoVo.engineNo}</td>
								<td>${prpLVehicleInfoVo.vinNo}</td>
								<td>${prpLVehicleInfoVo.vehocleModel}</td>
								<td>${prpLVehicleInfoVo.driverName}</td>
								<td><app:codetrans codeType="CertifyTypeForRisk" codeCode="${prpLVehicleInfoVo.certiType}" /></td>
								<td>${prpLVehicleInfoVo.certiCode}</td>
								<td>${prpLVehicleInfoVo.driverLicenseNo}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="table_title f14">出险伤员信息</div>
			<div class="table_cont " style="magrn-top:20px">
				<table class="table table-border table-hover">
					<thead>
						<tr class="text-c">
							<th>人员属性</th>
							<th>伤亡人员姓名</th>
							<th>伤亡证件类型</th>
							<th>伤亡人员证件号码</th>
							<th>伤亡人员医疗类型</th>
							<th>伤情类别代码</th>
							<th>伤亡详情</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="prpLCasualtyInforVo" items="${prpLCasualtyInforVos}">
							<tr class="text-c">
								<td><app:codetrans codeType="PersonPropertyCode" codeCode="${prpLCasualtyInforVo.personProperty}" /></td>
								<td>${prpLCasualtyInforVo.personName}</td>
								<td><app:codetrans codeType="CertifyTypeForRisk" codeCode="${prpLCasualtyInforVo.csCertiType}" /></td>
								<td>${prpLCasualtyInforVo.csCertiCode}</td>
								<td><app:codetrans codeType="InjuredPersonType" codeCode="${prpLCasualtyInforVo.csMedicalType}" /></td>
								<td><app:codetrans codeType="WoundCode" codeCode="${prpLCasualtyInforVo.medicalType}" /></td>
								<td><input type="button" class="btn btn-secondary"
									onclick="showInjuredDetails('${prpLCasualtyInforVo.upperId}')"
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
		function showInjuredDetails(upperId) {
			index = layer
					.open({
						type : 2,
						title : '伤亡详情',
						//closeBtn : 2,
						//shadeClose : true,
						scrollbar : false,
						area : [ '90%', '80%' ],
						content : "/claimcar/realTimeQueryAction/showInjuredDetails.do?upperId="+ upperId,
						scrollbar : true
					});

		}
		
	</script>

</body>
</html>