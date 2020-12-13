<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>德联检测信息</title>
</head>
<body>
	<div class="table_wrap">
	<div class="table_cont table_list">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th>节点</th>
					<th>车牌号</th>
					<th>反馈时间</th>
					<th>结果</th>
				</tr>
			</thead>
				<tbody id="propLossTbody">
					<c:forEach var="prplcecheckResult" items="${prplcecheckResultVos}" varStatus="status">
					<tr class="text-c">
                     <td><font size="2" color="purple">${prplcecheckResult.operateName}</font></td>
					 <td><font size="2" color="purple">${prplcecheckResult.licensePlateNo}</font></td>
					 <td><font size="2" color="purple"><fmt:formatDate  value="${prplcecheckResult.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></font></td>
					 <td><a class="btn" onclick="dlinfoShow('${prplcecheckResult.id}')"><font size="3">详情</font></a></td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
	</div>
</div>

	<script type="text/javascript">
		$(function() {
			
		});
		function dlinfoShow(id){
			
		}
	</script>
</body>
</html>