<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>附加险险种</title>
		<style type="text/css">
			.text-r{
				background-color: #F5F5F5
			}
			
		</style>
	</head>
	<body>
		<div id="subRiskDiv">
			<div class="table_cont">
				<table class="table table-border" id="subRiskTab">
					<thead>
						<tr>
							<th width="20%" align="center"></th>
							<th width="80%" style="border-right: 0px;">险别</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var ="itemKind" items="${itemKinds }">
							<tr style="cursor: pointer;">
							<td align="center"><input name="subRiskCheckFlag" type="checkbox" value="${itemKind.kindCode }"></td>
							<td align="left">${itemKind.kindName }</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="text-c">
				<button type="button" onclick="setSubRisk();" class="btn btn-primary">确定</button>
				<button type="button" onclick="closePop();" class="btn btn-default">关闭</button>
			</div>
		</div>
<script type="text/javascript">
	$(function() {
		$("#subRiskTab tbody tr").click(function(e) {
			if (e.target.type != 'checkbox') {
				var cb = $(this).find("input[type=checkbox]");
				cb.trigger('click');
			}
		});
	});
</script>		
		
	</body>

</html>