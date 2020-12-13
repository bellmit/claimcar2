<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!--代理人信息列表-->
<div class="table_wrap">
	<div class="table_title f14">当事人信息表</div>
	<div class="table_cont table_list ">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th style="width: 15%">姓名</th>
					<th style="width: 15%">证件类型</th>
					<th style="width: 15%">证件号</th>
					<th style="width: 15%">当事人类型</th>
					<th style="width: 15%">手机号码</th>
					<th style="width: 15%">电子邮箱</th>
					<th style="width: 15%">操作</th>
				</tr>
			</thead>
			<tbody id="checkMainCarTbody">
				<c:forEach var="prpLCourtParty" items="${courtMessageVo.prpLCourtPartys}" varStatus="status">
					<%@include file="CourtParty_Tr.jsp" %>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<!--标的车处理     结束-->