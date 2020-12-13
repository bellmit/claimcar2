<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>理赔计算书打印</title>
</head>
<body>
	<div class="table_wrap">
	<div class="table_cont table_list">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th>报案号</th>
					<th>立案号</th>
					<th>计算书号</th>
					<th>操作</th>
				</tr>
			</thead>
				<tbody id="propLossTbody">
					<c:forEach var="compensateVo" items="${compensateList}" varStatus="status">
					<tr class="text-c">
					<td>${compensateVo.registNo}</td>
					<td>${compensateVo.claimNo}</td>
					<td>${compensateVo.compensateNo}</td>
					<td><input type='button' class='btn btn-primary' value='打印'  onclick="AdjustersPrintSon('${compensateVo.registNo }','${compensateVo.compensateNo}')"/></td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
	</div>
</div>

	<script type="text/javascript">
	
	</script>
</body>
</html>