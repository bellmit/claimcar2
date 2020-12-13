<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!--标的车处理  开始-->
<div class="table_wrap">
	<div class="table_title f14">文件信息列表</div>
	<div class="table_cont table_list ">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th style="width: 15%">报案号</th>
					<th style="width: 15%">文件名称</th>
					<th style="width: 15%">上传时间</th>
					<th style="width: 15%">文件地址</th>
					<th style="width: 15%">文件类型</th>
					<th style="width: 15%">当事人证件号</th>
				</tr>
			</thead>
			<tbody id="checkMainCarTbody">
				<c:forEach var="prpLCourtFile" items="${courtMessageVo.prpLCourtFiles}" varStatus="status">
					<%@include file="CourtFile_Tr.jsp" %>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<!--标的车处理     结束-->