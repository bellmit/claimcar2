<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!--三者车处理     开始-->
<div class="table_wrap">
	<div class="table_title f14">三者车</div>
	<div class="table_cont table_list ">
		<table class="table table-border table-hover" id="thridCarList">
			<thead>
				<tr class="text-c">
					<th>车牌号码</th>
					<th style="width: 15%">车型名称</th>
					<th>驾驶人</th>
					<th>驾驶人电话</th>
					<th style="width: 15%">损失部位</th>
					<th>估损金额</th>
					<th>出险历史</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody class="" id="thridCarItem" name="thridCarItem">
				<input type="hidden" id="thridCarItemSize"
					value="${fn:length(checkThirdCarVos)}">
				<%-- <input type="hidden" id="YregistNo" name="YregistNo" value="${YcheckCarList[0].registNo}"> --%>
				<c:set var="nodeCode" value="${taskParamVo.subNodeCode}"></c:set>
				<c:set var="statusFlag" value="${taskParamVo.handlerStatus}"></c:set>
				<c:forEach var="checkThirdCarVo" items="${checkThirdCarVos}" varStatus="status">
					<c:set var="inputIdx" value="${status.index}" />
					<%@include file="CheckThirdCarItem.jsp"%>
				</c:forEach>
			</tbody>
			<tr>
				<td><c:if test="${nodeCode eq 'Chk'}">
						<button type="button" class="btn btn-plus Hui-iconfont" id="add"
							onclick="addThirdCar('新增三者车','/claimcar/checkcar/viewThirdCarAdd.do','1')">新增车辆</button>
						<!-- <input type="button" class="btn btn-zd" id="add" value="新增车辆"onclick="addThirdCar('新增三者车','/claimcar/checkcar/viewThirdCarAdd.do','1')"> -->
					</c:if></td>
			</tr>
		</table>
	</div>
</div>
<!--三者车处理     结束-->