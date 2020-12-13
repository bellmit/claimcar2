<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>代查勘处理信息</title>
</head>
<body>
<div class="formtable" border="0">
<div class="table_cont table_list">
			<table class="table table-bordered table-bg" >
				<thead class="text-c">
					<tr>
					 <th style="width: 30%;">任务类型</th>
					 <th>处理机构</th>
					 <th>处理人</th>
					</tr>
				</thead>
				<div class=""></div>
				<tbody class="text-c">
				<c:forEach var="vo" items="${taskQueryList}" varStatus="status">
	               <tr>
					<td>${vo.dcheckTaskType }</td>
					<td>${vo.dcheckHandlerCom }</td>
					<td>${vo.dcheckHandlerName }</td>
				   </tr>
					
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<br/>
<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
<script type="text/javascript">
function closeLayer(){
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);	
}
</script>
</body>
</html>