<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!--代理人信息列表-->
<div class="table_wrap">
	<div class="table_title f14">赔偿信息表</div>
	<div class="table_cont table_list ">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th style="width: 15%">当事人证件号</th>
					<th style="width: 15%">医疗费</th>
					<th style="width: 15%">住院伙食补助费</th>
					<th style="width: 15%">营养费</th>
					<th style="width: 15%">后续治疗费</th>
					<th style="width: 15%">整容费</th>
					<th style="width: 15%">操作</th>
				</tr>
			</thead>
			<tbody id="checkMainCarTbody">
				<c:forEach var="prpLCourtCompensation" items="${courtMessageVo.prpLCourtCompensations}" varStatus="status">
					<%@include file="CourtCompensation_Tr.jsp" %>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<!--标的车处理     结束-->