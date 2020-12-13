<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>定损处理</title>
		<style type="text/css">
			.text-r{
				background-color: #F5F5F5
			}
			</style>
	</head>
	<body>
		<div class="table_wrap">
			<div class="table_title f14">定损轨迹列表</div>
			<div class="table_cont " style="overflow-x: scroll;">
				<table class="table table-border" border="1" id="hisTable">
					<thead class="text-c">
						<th>序号</th>
						<th>项目</th>
						<c:forEach var="column"  items="${deflossHisMap.column}" varStatus="columnStatus">
							<c:set var="columnNum" value="${columnStatus.count}"/>
							<th class="bgc_tt" >${column[1]}</th>
						</c:forEach>
					</thead>
					<c:forEach var="row" items="${deflossHisMap.row}" varStatus="rowStatus">
						<tr >
							<td>${rowStatus.index+1}</td>
							<td>${row[0]}</td>
							<c:forEach var="element"   begin="1" end="${columnNum}" varStatus="elementStatus">
								<td>${row[elementStatus.index]}</td>
							</c:forEach>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</body>
</html>