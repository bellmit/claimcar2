<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<style> 
body {
    min-width: 110px;
}
</style>
</head>
	<body>
		<table border="1" class="table table-border" style="width:299px">
			<thead class="text-c">
				<tr>
					<th>支付状态</th>
					<th>处理时间</th>
				</tr>
			</thead>
			<tbody class="text-c">
				<c:forEach var="PrpLPayHisVo" items="${PrpLPayHisVoList}" varStatus="status">
					<tr>
						<td>
							<c:choose>
								<c:when test="${PrpLPayHisVo.payStatus eq '1'}">
										已支付
								</c:when>
								<c:when test="${PrpLPayHisVo.payStatus eq '3'}">
										已退票
								</c:when>
								<c:when test="${PrpLPayHisVo.payStatus eq '2'}">
										未支付
								</c:when>
							</c:choose>
						</td>
						<c:set var="inputTime">
								<fmt:formatDate value="${PrpLPayHisVo.inputTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</c:set>
						<td>${inputTime }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
</body>
</html>