<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>代位案件理赔信息查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />

</head>
<body>
  <div class="table_cont">
  <div class="table_wrap">
     <div class="table_title f14">报案信息列表</div>
			<table class="table table-border" border="1" id="">
				<thead class="text-c">
					<th>序号</th>
					<th>三者车辆号牌号码</th>
					<th>三者车辆号牌种类代码</th>
					<th>三者车辆驾驶员姓名</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="subrogation" items="${resultList.subrogationReportDataVo.thirdVehicleDataList }" varStatus="status">
						<c:if test="${flags =='1' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.carMark }</td>
								<td>${subrogation.vehicleType}</td>
								<td>${subrogation.driverName }</td>
							</tr>
						</c:if>
						<c:if test="${flags =='2' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.licensePlateNo }</td>
								<td>${subrogation.licensePlateType}</td>
								<td>${subrogation.driverName }</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>	
		</div>
		
		<div class="table_wrap">
     <div class="table_title f14">报案信息列表</div>
			<table class="table table-border" border="1" id="">
				<thead class="text-c">
					<th>序号</th>
					<th>损失项赔偿类型</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="subrogation" items="${resultList.subrogationReportDataVo.lossDataList }" varStatus="status">
						<c:if test="${flags =='1' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.claimFeeType }</td>
							</tr>
						</c:if>
						<c:if test="${flags =='2' }">
							<tr>
								<td>${status.index+1 }</td>
								<td>${subrogation.lossFeeType }</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>	
		</div>
	</div>
</body>
</html>
