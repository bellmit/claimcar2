<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!--标的车处理  开始-->
<div class="table_wrap">
	<div class="table_title f14">标的车</div>
	<div class="table_cont table_list ">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th style="width: 15%">车牌号码</th>
					<th style="width: 15%">车型名称</th>
					<th style="width: 15%">车主</th>
					<th style="width: 15%">电话</th>
					<th style="width: 15%">损失部位</th>
					<th style="width:15%">估损金额</th>
					<th style="width: 10%">操作</th>
				</tr>
			</thead>
			<tbody id="checkMainCarTbody">
				<c:set var="nodeCode" value="${taskParamVo.subNodeCode}"></c:set>
				<c:set var="status" value="${taskParamVo.handlerStatus}"></c:set>
				<%@include file="CheckMainCar_Tr.jsp" %>
			</tbody>
		</table>
	</div>
</div>
<!--标的车处理     结束-->