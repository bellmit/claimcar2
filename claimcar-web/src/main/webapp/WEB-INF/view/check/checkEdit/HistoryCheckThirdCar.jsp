<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查勘历史出险</title>
</head>
<body>

      <div class="formtable">
		<!--  <span class="c-primary f-14">查勘历史出险 </span>-->
		<div class="table_con">
			<table class="table table-bordered table-bg" border="1">
				<thead class="text-c">
					<tr>
						<th style="background-color:#D3D3D3">序号&nbsp;&nbsp;</th>
						<th style="background-color:#D3D3D3">报案号&nbsp;&nbsp;</th>
						<th style="background-color:#D3D3D3">保险公司&nbsp;&nbsp;</th>
						<th style="background-color:#D3D3D3">承保地区&nbsp;&nbsp;</th>
						<th style="background-color:#D3D3D3">车辆属性&nbsp;&nbsp;</th>
						<th style="background-color:#D3D3D3">报案时间&nbsp;&nbsp;</th>
						<th style="background-color:#D3D3D3">出险时间&nbsp;&nbsp;</th>
						<th style="background-color:#D3D3D3">出险地点&nbsp;&nbsp;</th>
						<th style="background-color:#D3D3D3">出险经过&nbsp;&nbsp;</th>
						<th style="background-color:#D3D3D3">案件状态&nbsp;&nbsp;</th>
						<th style="background-color:#D3D3D3">保单类型&nbsp;&nbsp;</th>
						<th style="background-color:#D3D3D3">号牌号码&nbsp;&nbsp;</th>
						<th style="background-color:#D3D3D3">号牌种类&nbsp;&nbsp;</th>
						<th style="background-color:#D3D3D3">发动机号&nbsp;&nbsp;</th>
						<th style="background-color:#D3D3D3">VIN码&nbsp;&nbsp;</th>
					</tr>
					<!-- <app:codetrans codeCode="${vo.kindCode}" codeType="KindCode" /> -->
					<c:set var="index" value="0" />
                       <c:forEach var="vo" items="${resVo}" varStatus="status">
	<c:set var="indexa" value="${index + 1}" />
					<tr>
							<td>${indexa}</td>
							<td>${vo.claimNotificationNo }</td>
							<td>${vo.insurerCode}</td>
							<td>${vo.insurerArea}</td>
							<td>${vo.vehicleProperty}</td>
							<td><fmt:formatDate value='${vo.notificationTime}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
							<td><fmt:formatDate value='${vo.lossTime}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
							<td>${vo.lossArea}</td>
							<td>${vo.lossDesc}</td>
							<td>${vo.claimStatus}</td>
							<td>${vo.riskType}</td>
							<td>${vo.licensePlateType}</td>
							<td>${vo.licensePlateNo}</td>
							<td>${vo.engineNo}</td>
							<td>${vo.vin}</td>
						</tr>
					</c:forEach>
				</thead>
			</table>
		</div>
	</div>

</body>
</html>