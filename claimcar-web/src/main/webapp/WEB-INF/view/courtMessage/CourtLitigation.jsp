<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!--代理人信息列表-->
<div class="table_wrap">
	<div class="table_title f14">诉前调解列表</div>
	<div class="table_cont table_list ">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th style="width: 15%">调解字号</th>
					<th style="width: 15%">起诉人</th>
					<th style="width: 15%">被起诉人</th>
					<th style="width: 15%">进入调解日期</th>
					<th style="width: 15%">调解完成日期</th>
					<th style="width: 15%">调解状态</th>
					<th style="width: 15%">操作</th>
				</tr>
			</thead>
			<tbody id="checkMainCarTbody">
				<c:forEach var="prpLCourtLitigation" items="${courtMessageVo.prpLCourtLitigations}" varStatus="status">
					<%@include file="CourtLitigation_Tr.jsp" %>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<!--标的车处理     结束-->