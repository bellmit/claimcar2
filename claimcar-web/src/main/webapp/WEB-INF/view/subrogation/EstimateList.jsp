<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>定核损信息列表</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />

</head>
<body>
  <div class="table_cont">
  <div class="table_wrap">
     <div class="table_title f14">车辆损失情况列表</div>
			<table class="table table-border" border="1" id="">
				<thead class="text-c">
					<th>序号</th>
					<th>出险车辆号牌号码</th>
					<th>出险车辆号牌种类</th>
					<th>出险车辆发动机号</th>
					
					<th>出险车辆VIN码</th>
					<th>车辆型号</th>
					<th>出险驾驶员证件类型</th>
					<th>出险驾驶员证件号码</th>
					<th>核损金额</th>
					<th>车辆属性(本车/三者车)</th>
					<th>是否盗抢</th>
					<th>现场类别</th>
					<th>残值回收预估金额</th>
					<th>是否修理或更换配件</th>
					<th>修理机构名称</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="subrogation" items="${resultList.subrogationEstimateDataVo.carDataList }" varStatus="status">
						<c:if test="${flags =='1' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.licensePlateNo }</td>
								<td>${subrogation.licensePlateType}</td>
								<td>${subrogation.engineNo }</td>
								<td>${subrogation.vIN }</td>
								<td>${subrogation.model}</td>
								<td>${subrogation.certiType }</td>
								
								<td>${subrogation.certiCode }</td>
								<td>${subrogation.underDefLoss}</td>
								<c:set var="vehicleProperty">
									<app:codetrans codeType="LossTargetType" codeCode="${subrogation.vehicleProperty }"/>
								</c:set>
								<td>${vehicleProperty}</td>
								<c:set var="fieldType">
									<app:codetrans codeType="YN01" codeCode="${subrogation.fieldType }"/>
								</c:set>
								<td>${fieldType }</td>
								<td>${subrogation.remnant }</td>
								<c:set var="isChangeOrRepair">
									<app:codetrans codeType="YN01" codeCode="${subrogation.isChangeOrRepair }"/>
								</c:set>
								<td>${isChangeOrRepair }</td>
								<td>${subrogation.repairFactoryName }</td>
							</tr>
						</c:if>
						<c:if test="${flags =='2' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.carMark }</td>
								<td>${subrogation.vehicleType}</td>
								<td>${subrogation.engineNo }</td>
								<td>${subrogation.rackNo }</td>
								<td>${subrogation.vehicleModel}</td>
								<td>${subrogation.certiType }</td>
								
								<td>${subrogation.certiCode }</td>
								<td>${subrogation.underDefLoss}</td>
								<c:set var="vehicleProperty">
									<app:codetrans codeType="LossTargetType" codeCode="${subrogation.vehicleProperty }"/>
								</c:set>
								<td>${vehicleProperty}</td>
								<c:set var="fieldType">
									<app:codetrans codeType="YN01" codeCode="${subrogation.fieldType }"/>
								</c:set>
								<td>${fieldType }</td>
								<td>${subrogation.remnant }</td>
								<c:set var="isChangeOrRepair">
									<app:codetrans codeType="YN01" codeCode="${subrogation.isChangeOrRepair }"/>
								</c:set>
								<td>${isChangeOrRepair }</td>
								<td>${subrogation.repairFactoryName }</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>	
		</div>
		
		<div class="table_wrap">
     <div class="table_title f14">财产损失情况列表</div>
			<table class="table table-border" border="1" id="">
				<thead class="text-c">
					<th>序号</th>
					<th>损失财产名称</th>
					<th>损失描述</th>
					<th>核损金额</th>
					<th>财产属性(本车财产/外车财产)</th>
					<th>现场类别</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="subrogation" items="${resultList.subrogationEstimateDataVo.propDataList }" varStatus="status">
						<c:if test="${flags =='1' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.protectName }</td>
								<td>${subrogation.lossDesc}</td>
								<td>${subrogation.underDefLoss }</td>
								<td>${subrogation.protectProperty }</td>
								<td>${subrogation.filedType}</td>
							</tr>
						</c:if>
						<c:if test="${flags =='2' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.protectName }</td>
								<td>${subrogation.lossDesc}</td>
								<td>${subrogation.underDefLoss }</td>
								<td>${subrogation.protectProperty }</td>
								<td>${subrogation.filedType}</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>	
		</div>
		<div class="table_wrap">
     <div class="table_title f14">人员查勘情况列表</div>
			<table class="table table-border" border="1" id="">
				<thead class="text-c">
					<th>序号</th>
					<th>伤亡人员姓名</th>
					<th>伤情类别</th>
					<th>伤残程度</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="subrogation" items="${resultList.subrogationEstimateDataVo.personDataList }" varStatus="status">
						<c:if test="${flags =='1' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.personName }</td>
								<td>${subrogation.injuryType }</td>
								<td>${subrogation.injuryLevel }</td>
							</tr>
						</c:if>
						<c:if test="${flags =='2' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.personName }</td>
								<td>${subrogation.injuryType }</td>
								<td>${subrogation.injuryLevel }</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>	
		</div>
	</div>
</body>
</html>
