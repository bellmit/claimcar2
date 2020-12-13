<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>理算信息列表</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />

</head>
<body>
  <div class="table_cont">
  <div class="table_wrap">
     <div class="table_title f14">理算信息列表</div>
			<table class="table table-border" border="1" id="">
				<thead class="text-c">
					<th>序号</th>
					<th>单证名称</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="subrogation" items="${resultList.subrogationDocDataVo }" varStatus="status">
						<tr>
							<td>${status.index+1 }</td>
							<td>${subrogation.docName }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>	
		</div>
		

	</div>
</body>
</html>
