<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!--代理人信息列表-->
<div class="table_wrap">
	<div class="table_title f14">调解信息列表</div>
	<div class="table_cont table_list ">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th style="width: 15%">调解号</th>
					<th style="width: 15%">调节类型</th>
					<th style="width: 15%">调节机构名称</th>
					<th style="width: 15%">调解员</th>
					<th style="width: 15%">调节地点</th>
					<th style="width: 15%">调节状态</th>
					<th style="width: 15%">操作</th>
				</tr>
			</thead>
			<tbody id="checkMainCarTbody">
				<c:forEach var="prplCourtMediation" items="${courtMessageVo.prpLCourtMediations}" varStatus="status">
					<%@include file="CourtMediation_Tr.jsp" %>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<!--标的车处理     结束-->