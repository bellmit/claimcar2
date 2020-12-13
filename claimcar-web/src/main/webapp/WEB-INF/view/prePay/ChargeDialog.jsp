<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>费用类型</title>
<style type="text/css">
.text-r {
	background-color: #F5F5F5
}
</style>
</head>
<body>
	<div id="chargeDiv">
		<div class="table_cont">
			<table class="table table-border" id="chargeTab">
				<thead>
					<tr>
						<th width="20%" align="center"></th>
						<th width="80%" style="border-right: 0px;">费用类型</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="codeDictVo" items="${sysCodes }">
						<tr style="cursor: pointer;">
							<td align="center">
								<input name="chargeCheckFlag" type="checkbox" value="${codeDictVo.codeCode }">
							</td>
							<td align="left">${codeDictVo.codeName }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="text-c">
			<button type="button" onclick="setCharge();" class="btn btn-primary">确定</button>
			<button type="button" onclick="closePop();" class="btn btn-default">关闭</button>
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			$("#chargeTab tbody tr").click(function(e) {
				if (e.target.type != 'checkbox') {
					var cb = $(this).find("input[type=checkbox]");
					cb.trigger('click');
				}
			});
		});
	</script>
</body>
</html>