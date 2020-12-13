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
     <div class="table_title f14">代位信息列表</div>
			<table class="table table-border" border="1" id="">
				<thead class="text-c">
					<th>序号</th>
					<th>责任对方联系人姓名/名称</th>
					<th>责任对方车辆号牌号码</th>
					<th>责任对方车辆号牌种类</th>
					<th>责任对方车辆发动机号</th>
					<th>责任对方车辆VIN码</th>
					
					<th>责任对方商业三者险承保公司</th>
					<th>责任对方商业三者险承保地区（到省市）</th>
					<th>责任对方交强险承保公司</th>
					<th>责任对方交强险承保地区（到省市）</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="subrogation" items="${resultList.subrogationDataVos }" varStatus="status">
						<c:if test="${flags =='1' }">
						<tr>
							<td>${status.index+1 }</td>
							<td>${subrogation.linkerName }</td>
							<td>${subrogation.carMark}</td>
							<td>${subrogation.vehicleType }</td>
							<td>${subrogation.engineNo }</td>
							<td>${subrogation.rackNo }</td>
							<c:set var="caInsurerCode">
								<app:codetrans codeType="DWInsurerCode" codeCode="${subrogation.caInsurerCode }"/>
							</c:set>
							<td>${caInsurerCode }</td>
							<c:set var="caInsurerArea">
								<app:codetrans codeType="DWInsurerArea" codeCode="${subrogation.caInsurerArea }"/>
							</c:set>
							<td>${caInsurerArea }</td>
							<c:set var="iaInsurerCode">
								<app:codetrans codeType="DWInsurerCode" codeCode="${subrogation.iaInsurerCode }"/>
							</c:set>
							<td>${iaInsurerCode }</td>
							<c:set var="iaInsurerArea">
								<app:codetrans codeType="DWInsurerArea" codeCode="${subrogation.iaInsurerArea }"/>
							</c:set>
							<td>${iaInsurerArea }</td>
						</tr>
						</c:if>
						<c:if test="${flags =='2' }">
						<tr>
							<td>${status.index+1 }</td>
							<td>${subrogation.linkerName }</td>
							<td>${subrogation.licensePlateNo}</td>
							<td>${subrogation.licensePlateType }</td>
							<td>${subrogation.engineNo }</td>
							<td>${subrogation.vIN }</td>
							<c:set var="caInsurerCode">
								<app:codetrans codeType="DWInsurerCode" codeCode="${subrogation.caInsurerCode }"/>
							</c:set>
							<td>${caInsurerCode }</td>
							<c:set var="caInsurerArea">
								<app:codetrans codeType="DWInsurerArea" codeCode="${subrogation.caInsurerArea }"/>
							</c:set>
							<td>${caInsurerArea }</td>
							<c:set var="iaInsurerCode">
								<app:codetrans codeType="DWInsurerCode" codeCode="${subrogation.iaInsurerCode }"/>
							</c:set>
							<td>${iaInsurerCode }</td>
							<c:set var="iaInsurerArea">
								<app:codetrans codeType="DWInsurerArea" codeCode="${subrogation.iaInsurerArea }"/>
							</c:set>
							<td>${iaInsurerArea }</td>
						</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>	
		</div>
	</div>
</body>
</html>
