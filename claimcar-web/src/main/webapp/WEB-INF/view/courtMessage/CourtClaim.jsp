<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!--代理人信息列表-->
<div class="table_wrap">
	<div class="table_title f14">立案信息列表</div>
	<div class="table_cont table_list ">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th style="width: 15%">立案日期</th>
					<th style="width: 15%">立案案由代码</th>
					<th style="width: 15%">经办法院代码</th>
					<th style="width: 15%">经办法院名称</th>
					<th style="width: 15%">立案标的金额</th>
					<th style="width: 15%">是否诉前调解</th>
					<th style="width: 15%">操作</th>
				</tr>
			</thead>
			<tbody id="checkMainCarTbody">
				<c:forEach var="prpLCourtClaim" items="${courtMessageVo.prpLCourtClaims}" varStatus="status">
						<%@include file="CourtClaim_Tr.jsp" %>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<!--标的车处理     结束-->