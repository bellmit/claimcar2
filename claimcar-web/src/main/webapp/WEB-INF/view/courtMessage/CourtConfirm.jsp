<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!--代理人信息列表-->
<div class="table_wrap">
	<div class="table_title f14">司法确认列表</div>
	<div class="table_cont table_list ">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th style="width: 15%">申请人证件号</th>
					<th style="width: 15%">申请人</th>
					<th style="width: 15%">申请时间</th>
					<th style="width: 15%">受理法院</th>
					<th style="width: 15%">案号</th>
					<th style="width: 15%">状态</th>
					<th style="width: 15%">操作</th>
				</tr>
			</thead>
			<tbody id="checkMainCarTbody">
				<c:forEach var="prplCourtComfirm" items="${courtMessageVo.prpLCourtConfirms}" varStatus="status">
					<%@include file="CourtConfirm_Tr.jsp" %>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<!--标的车处理     结束-->