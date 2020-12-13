<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>山东预警推送详情</title>
</head>
<body>
	<div class="tabbox">
		<div id="tab-system" class="HuiTab">
			<div class="tabCon clearfix">
				<table class="table table-border table-hover data-table"
					cellpadding="0" cellspacing="0">
					<thead class="text-c">
						<tr>
							<th>序号</th>
							<th>预警话术</th>
						</tr>
					</thead>
					<tbody class="text-c">
						<c:forEach var="PrpLwarnInfoVo" items="${warnInfoDetails}"
							varStatus="wanStatus">
							<tr>
								<td>${wanStatus.count}</td>
								<td>${PrpLwarnInfoVo.warnMessage}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>