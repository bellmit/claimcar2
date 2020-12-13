<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>查勘信息列表</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />

</head>
<body>
  <div class="table_cont">
  <div class="table_wrap">
     <div class="table_title f14">车辆查勘情况列表</div>
			<table class="table table-border" border="1" id="">
				<thead class="text-c">
					<th>序号</th>
					<th>出险车辆号牌号码</th>
					<th>出险车辆号牌种类</th>
					<th>出险车辆发动机号</th>
					<th>出险车辆VIN码</th>
					<th>出险车辆厂牌型号</th>
					<th>出险驾驶员姓名</th>
					<th>车辆属性</th>
					<th>现场类别</th>
					<th>估损金额</th>
					<th>查勘地点</th>
					<th>查勘情况说明</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="subrogation" items="${resultList.subrogationCheckDataVo.checkCarDataList }" varStatus="status">
						<c:if test="${flags =='1' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.carMark }</td>
								<td>${subrogation.vehicleType}</td>
								<td>${subrogation.engineNo }</td>
								<td>${subrogation.rackNo }</td>
								<td>${subrogation.vehicleModel}</td>
								<td>${subrogation.driverName }</td>
								<c:set var="vehicleProperty">
									<app:codetrans codeType="LossTargetType" codeCode="${subrogation.vehicleProperty }"/>
								</c:set>
								<td>${vehicleProperty }</td>
	
									<c:if test="${subrogation.fieldType==1}"><td>无现场</td></c:if>
									<c:if test="${subrogation.fieldType==2}"><td>第一现场</td></c:if>
									<c:if test="${subrogation.fieldType==3}"><td>第二现场</td></c:if>
	
								<td>${subrogation.estimateAmount}</td>
								<td>${subrogation.checkAddr }</td>
								<td>${subrogation.checkDes }</td>
							</tr>
							</c:if>
							<c:if test="${flags =='2' }">
								<tr>
									<td>${status.index+1 }</td>
									<td>${subrogation.licensePlateNo }</td>
									<td>${subrogation.licensePlateType}</td>
									<td>${subrogation.engineNo }</td>
									<td>${subrogation.vIN }</td>
									<td>${subrogation.model}</td>
									<td>${subrogation.driverName }</td>
									<c:set var="vehicleProperty">
										<app:codetrans codeType="LossTargetType" codeCode="${subrogation.vehicleProperty }"/>
									</c:set>
									<td>${vehicleProperty }</td>
		
										<c:if test="${subrogation.fieldType==1}"><td>无现场</td></c:if>
										<c:if test="${subrogation.fieldType==2}"><td>第一现场</td></c:if>
										<c:if test="${subrogation.fieldType==3}"><td>第二现场</td></c:if>
		
									<td>${subrogation.estimatedLossAmount}</td>
									<td>${subrogation.checkAddr }</td>
									<td>${subrogation.checkDesc }</td>
								</tr>
						
						</c:if>
					</c:forEach>
				</tbody>
			</table>	
		</div>
		
		<div class="table_wrap">
     <div class="table_title f14">财产查勘情况列表</div>
			<table class="table table-border" border="1" id="">
				<thead class="text-c">
					<th>序号</th>
					<th>损失财产名称</th>
					<th>损失描述</th>
					<th>损失数量</th>
					<th>估损金额</th>
					<th>财产属性</th>
					<th>现场类别</th>
					<th>查勘地点</th>
					<th>查勘情况说明</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="subrogation" items="${resultList.subrogationCheckDataVo.checkPropDataList }" varStatus="status">
						<c:if test="${flags =='1' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.protectName }</td>
								<td>${subrogation.lossDesc}</td>
								<td>${subrogation.lossNum }</td>
								<td>${subrogation.estimateAmount }</td>
								<td>${subrogation.protectProperty}</td>
								<td>${subrogation.fieldType }</td>
								<td>${subrogation.checkAddr }</td>
								<td>${subrogation.checkDes}</td>
							</tr>
						</c:if>
						<c:if test="${flags =='2' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.protectName }</td>
								<td>${subrogation.lossDesc}</td>
								<td>${subrogation.lossNum }</td>
								<td>${subrogation.estimatedLossAmount }</td>
								<td>${subrogation.protectProperty}</td>
								<td>${subrogation.fieldType }</td>
								<td>${subrogation.checkAddr }</td>
								<td>${subrogation.checkDesc}</td>
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
					<th>伤亡类型</th>
					<th>估损金额</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="subrogation" items="${resultList.subrogationCheckDataVo.checkPersonDataList }" varStatus="status">
						<c:if test="${flags =='1' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.personPayType }</td>
								<td>${subrogation.estimateAmount }</td>
							</tr>
						</c:if>
						<c:if test="${flags =='2' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.personPayType }</td>
								<td>${subrogation.estimatedLossAmount }</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>	
		</div>
	</div>
</body>
</html>
