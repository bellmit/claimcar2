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
			<div class="table_title f14">伤亡详情</div>
			<div class="table_cont ">
				<table class="table table-border table-hover">
					<thead>
						<tr class="text-c">
							<th>受伤部位</th>
							<th>伤残程度代码</th>
							<th>死亡时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="prpLInjuredDetailsVo" items="${prpLInjuredDetailsVos}">
							<tr class="text-c">
								<td>${prpLInjuredDetailsVo.injuryPart}</td>
								<td><app:codetrans codeType="DamageLevelCode" codeCode="${prpLInjuredDetailsVo.injuryLevelCode}" /></td>
								<td>${prpLInjuredDetailsVo.deathTime}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>

</body>
</html>