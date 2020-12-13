<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>预警提示信息</title>
</head>
<body>
<div class="formtable" border="0">
<div class="table_cont table_list">
			<table class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
					 <th style="width: 10%;">序号</th>
					 <th>预警内容</th>
					</tr>
				</thead>
				<div class=""></div>
				<tbody class="text-c">
				<c:forEach var="valueStr" items="${warnMessageSet}" varStatus="status">
	              <tr>
					 <td>${status.index+1}</td>
					 <td>${valueStr}</td>
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