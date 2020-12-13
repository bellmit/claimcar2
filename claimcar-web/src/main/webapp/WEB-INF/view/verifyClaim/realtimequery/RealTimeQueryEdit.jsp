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
			<div class="table_title f14">车辆信息</div>
			<div class="table_cont ">
				<table class="table table-border table-hover">
					<thead>
						<tr class="text-c">
							<th>属性</th>
							<th>车架号</th>
							<th>号牌号码</th>
							<th>号牌种类</th>
							<th>交互时间</th>
							<th>结果</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="prpLVehicleInfoVo" items="${vehicleInfoVos}">
							<tr class="text-c">
								<td><app:codetrans codeType="ItemType" codeCode="${prpLVehicleInfoVo.vehicleProperty}" /></td>
								<td>${prpLVehicleInfoVo.vinNo}</td>
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
								<td><fmt:formatDate value="${prpLVehicleInfoVo.changeTime}" pattern="yyyy-MM-dd"/></td>
								<td><input type="button" class="btn btn-secondary"
									onclick="showVehicleInfodetails('${registNo}','${prpLVehicleInfoVo.upperId}')"
									value="详情"></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="table_title f14">人员信息</div>
			<div class="table_cont " style="magrn-top:20px">
				<table class="table table-border table-hover">
					<thead>
						<tr class="text-c">
							<th>属性</th>
							<!-- <th>驾驶证</th> -->
							<th>证件类型</th>
							<th>证件号码</th>
							<th>交互时间</th>
							<th>结果</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="prpLCasualtyInforVo" items="${perosonInfoVos}">
							<tr class="text-c">
								<td><app:codetrans codeType="PersonPropertyCode" codeCode="${prpLCasualtyInforVo.personProperty}" /></td>
								<!-- <td>---</td> -->
								<td><app:codetrans codeType="CertifyTypeForRisk" codeCode="${prpLCasualtyInforVo.csCertiType}" /></td>
								<td>${prpLCasualtyInforVo.csCertiCode}</td>
								<td><fmt:formatDate value="${prpLCasualtyInforVo.changeTime}" pattern="yyyy-MM-dd"/></td>
								<td><input type="button" class="btn btn-secondary"
									onclick="showPersonInfodetails('${registNo}','${prpLCasualtyInforVo.upperId}')"
									value="详情"></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="table_title f14">报案电话</div>
			<div class="table_cont " style="magrn-top:20px">
				<table class="table table-border table-hover">
					<thead>
						<tr class="text-c">
							<th>电话号码</th>
							<th>交互时间</th>
							<th>结果</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="prpLAntiFraudVo" items="${reportPhoneInfoVos}">
							<tr class="text-c">
								<td>${prpLAntiFraudVo.reportPhoneNo}</td>
								<td><fmt:formatDate value="${prpLAntiFraudVo.changeTime}" pattern="yyyy-MM-dd"/></td>
								<td><input type="button" class="btn btn-secondary"
									onclick="showReportPhoneInfodetails('${registNo}','${prpLAntiFraudVo.upperId}')"
									value="详情"></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

		</div>

	</div>
	<script type="text/javascript">
		function showVehicleInfodetails(registNo, upperId) {
			index = layer
					.open({
						type : 2,
						title : '车辆信息详情',
						//closeBtn : 2,
						//shadeClose : true,
						scrollbar : false,
						area : [ '95%', '95%' ],
						content : "/claimcar/realTimeQueryAction/vehicleInfoDetils.do?registNo="+ registNo+ "&upperId="+ upperId,
						scrollbar : true
					});

		}
		
		function showPersonInfodetails(registNo, upperId) {
			index = layer
					.open({
						type : 2,
						title : '人员信息详情',
						//closeBtn : 2,
						//shadeClose : true,
						scrollbar : false,
						area : [ '95%', '95%' ],
						content : "/claimcar/realTimeQueryAction/personInfoDetils.do?registNo="
								+ registNo
								+ "&upperId="
								+ upperId,
						scrollbar : true
					});

		}
		
		function showReportPhoneInfodetails(registNo, upperId) {
			index = layer
					.open({
						type : 2,
						title : '报案电话信息详情',
						//closeBtn : 2,
						//shadeClose : true,
						scrollbar : false,
						area : [ '95%', '95%' ],
						content : "/claimcar/realTimeQueryAction/reportPhoneInfoDetils.do?registNo="
								+ registNo
								+ "&upperId="
								+ upperId,
						scrollbar : true
					});

		}
	</script>

</body>
</html>