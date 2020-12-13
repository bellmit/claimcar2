<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>修改轨迹</title>
	</head>
	<body>
		<div>
			<div class="table_wrap">
            	<div class="table_title f14">修改轨迹</div>
            	<div class="f14">（报案号：${registNo}）</div>
				<div class="table_cont">
					 <table class="table table-border table-hover"  style="white-space:nowrap">
					 		<thead>
					 			<tr class="text-c">
					 				<th>修改人</th>
					 				<th>修改人姓名</th>
					 				<th>修改时间</th>
					 				<th>操作类型</th>
					 				<th>修改前保单信息</th>
					 				<th>修改后保单信息</th>
					 			</tr>
					 		</thead>
					 		<tbody>
					 			<c:forEach items="${relationshipHisVos}" var="relationshipHisVo">
					 				<tr class="text-c">
					 					<td>${relationshipHisVo.createUser}</td>
					 					<td><app:codetrans codeType="UserCode" codeCode="${relationshipHisVo.createUser}"/></td>
					 					<td><fmt:formatDate value="${relationshipHisVo.createTime}" type="both"/></td>
					 					<td><app:codetrans codeType="RelateOperatType" codeCode="${relationshipHisVo.operationtype}"/></td>
					 					<td>${relationshipHisVo.operationbefore}</td>
					 					<td>${relationshipHisVo.operationafter}</td>
					 				</tr>
					 			</c:forEach>
					 		</tbody>
					 </table>
				</div>
			</div>
		</div>
	</body>
</html>