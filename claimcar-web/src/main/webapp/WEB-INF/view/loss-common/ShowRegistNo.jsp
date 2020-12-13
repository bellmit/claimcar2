<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>报案号展示</title>
</head>
<body>
<div class="formtable">
		<div class="table_con">
			<table class="table table-bordered table-bg" border="1">
				<thead class="text-c">
					<tr>
						<th style="background-color:#D3D3D3">报案号&nbsp;&nbsp;</th>
						
					</tr>

					<c:forEach var="vo" items="${registNoList}">
						<tr>
							<td>${vo}</td>
						</tr>
						</c:forEach>
						<tr>
							<td></td>
						</tr>	
				</thead>
			</table>
		</div>
	</div>
</body>
</html>