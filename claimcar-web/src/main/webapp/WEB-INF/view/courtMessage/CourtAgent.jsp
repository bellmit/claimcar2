<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!--代理人信息列表-->
<div class="table_wrap">
	<div class="table_title f14">代理人信息列表</div>
	<div class="table_cont table_list ">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th style="width: 15%">申请人</th>
					<th style="width: 15%">申请人证件号</th>
					<th style="width: 15%">代理人职务</th>
					<th style="width: 15%">代理人证件类型</th>
					<th style="width: 15%">代理人证件号码</th>
					<th style="width: 15%">代理人手机号码</th>
					<th style="width: 15%">当事人证件号码</th>
				</tr>
			</thead>
			<tbody id="checkMainCarTbody">
				<%@include file="CourtAgent_Tr.jsp" %>
			</tbody>
		</table>
	</div>
</div>