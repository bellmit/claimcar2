<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>费用信息</title>
</head>
<body>
	<div class="formtable">
		<!--  <span class="c-primary f-14">*费用信息* </span>-->
		<div class="table_con">
			<table class="table table-bordered table-bg" border="1">
				<thead class="text-c">
					<tr>
						<th style="background-color:#D2E9FF">损失方&nbsp;&nbsp;</th>
						<th style="background-color:#D2E9FF">损失类型&nbsp;&nbsp;</th>
						<th style="background-color:#D2E9FF">险别&nbsp;&nbsp;</th>
						<th style="background-color:#D2E9FF">费用类型&nbsp;&nbsp;</th>
						<th style="background-color:#D2E9FF">费用金额&nbsp;&nbsp;</th>
						<th style="background-color:#D2E9FF">核损费用金额&nbsp;&nbsp;</th>
						<th style="background-color:#D2E9FF">收款人&nbsp;&nbsp;</th>
						<th style="background-color:#D2E9FF">是否核损通过&nbsp;&nbsp;</th>
					</tr>

					<c:forEach var="vo" items="${lossChargeList}">
						<tr>
							<td><c:choose>
									<c:when test="${ vo.businessType == 'DLCar' && vo.serialNo == '1'}">标的车(${vo.license})</c:when>
									<c:when test="${ vo.businessType == 'DLCar' && vo.serialNo != '0' && vo.serialNo != '1'}">三者车(${vo.license})</c:when>
									<c:when test="${vo.businessType == 'Check'}">查勘（公估费）</c:when>
									<c:when test="${vo.businessType == 'PLoss'}">人伤</c:when>
									<c:when test="${vo.businessType == 'DLProp'}">${vo.license}</c:when>
									<c:otherwise></c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${vo.businessType == 'PLoss'}">人伤</c:when>
									<c:when test="${vo.businessType == 'DLCar'}">车</c:when>
									<c:when test="${vo.businessType == 'DLProp'}">财</c:when>
									<c:otherwise>--</c:otherwise>
								</c:choose></td>
							<td>${vo.kindCode}<app:codetrans codeCode="${vo.kindCode}"
									codeType="KindCode" riskCode="${riskCode }"  /></td>
							<td>${vo.chargeName}</td>
							<td>${vo.chargeFee}</td>
							<td>${vo.veriChargeFee}</td>
							<td>${vo.receiver}</td>
							<td><c:choose>
							<c:when test="${vo.isVerify == '1' && vo.businessType == 'PLoss'}">是</c:when> 
							<c:when test="${vo.isVerify=='0' && vo.businessType == 'PLoss' }">否</c:when>
							<c:when test="${vo.isVerify == '1' && vo.businessType == 'DLCar'}">是</c:when>
							 <c:when test="${vo.isVerify == '0' && vo.businessType == 'DLCar'}">否</c:when>
							 <c:when test="${vo.isVerify == '1' && vo.businessType == 'DLProp'}">是</c:when>	
							 <c:when test="${vo.isVerify == '0' && vo.businessType == 'DLProp'}">否</c:when>	
							 <c:otherwise>--</c:otherwise></c:choose>	
									</td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan=2 style="background-color:#D2E9FF">费用合计: <c:set var="Sum1" value="0" /> <c:forEach
								items="${lossChargeList}" var="vo1">
								<c:set var="Sum1" value="${Sum1 + vo1.chargeFee}" /></td>
							</c:forEach><td colspan=2><u>${Sum1}</u></td>
						<td colspan=2 style="background-color:#D2E9FF">核损费用合计: <c:set var="Sum2" value="0" /> <c:forEach
								items="${lossChargeList}" var="vo2">
								<c:set var="Sum2" value="${Sum2 + vo2.veriChargeFee}" /></td>
							</c:forEach><td colspan=2> <u>${Sum2}</u></td>
					</tr>
					<tr>
						<td colspan=8></td>

					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>
</html>