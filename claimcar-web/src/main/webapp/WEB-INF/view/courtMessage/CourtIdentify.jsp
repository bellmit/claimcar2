<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!--代理人信息列表-->
<div class="table_wrap">
	<div class="table_title f14">鉴定信息表</div>
	<div class="table_cont table_list ">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th style="width: 15%">鉴定编号</th>
					<th style="width: 15%">鉴定机构</th>
					<th style="width: 15%">鉴定费用</th>
					<th style="width: 15%">鉴定地点</th>
					<th style="width: 15%">鉴定人</th>
					<th style="width: 15%">被鉴定人</th>
					<th style="width: 15%">操作</th>
				</tr>
			</thead>
			<tbody id="checkMainCarTbody">
				<%@include file="CourtIdentify_Tr.jsp" %>
			</tbody>
		</table>
	</div>
</div>
<!--标的车处理     结束-->