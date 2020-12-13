<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>核损清单打印</title>
</head>
<body>
	<div class="table_wrap">
	<div class="table_cont table_list">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th>报案号</th>
					<th>车牌号码</th>
					<th>车辆类型</th>
					<th>操作</th>
				</tr>
			</thead>
				<tbody id="propLossTbody">
					<c:forEach var="lossCarMainVo" items="${lossCarMainVos}" varStatus="status">
					<tr class="text-c">
					<td>${lossCarMainVo.registNo}</td>
					<td>${lossCarMainVo.licenseNo}</td>
					<td>${lossCarMainVo.deflossCarType}</td>
					<td><input type='button' class='btn btn-primary' value='打印'  onclick="verifyLossCertifyPrintSon('${lossCarMainVo.id }','${lossCarMainVo.registNo}')"/></td>
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