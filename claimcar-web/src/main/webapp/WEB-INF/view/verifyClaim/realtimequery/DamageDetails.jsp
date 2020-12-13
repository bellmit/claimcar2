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
			<div class="table_title f14">车损信息</div>
			<div class="table_cont ">
				<table class="table table-border table-hover">
					<tbody>
						<c:forEach var="prpLDamageInfoVo" items="${prpLDamageInfoVos}">
							<tr class="text-c">
								<td width="50%">车辆损伤部位</td>
								<td width="50%">${prpLDamageInfoVo.lossPartCode}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>

</body>
</html>