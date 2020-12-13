<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>案件列表</title>
<style>
.table-border tr td{border-bottom:2px solid green }
</style>
</head>
<body>
	<div class="page_wrap">
		<div class="table_wrap">
			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabCon clearfix table_cont table_list">
					<table id="DataTable_0" class="table table-border table-hover data-table table-striped" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>业务环节</th>
								<th>业务号</th>
								<th>操作人员</th>
								<th>提交人员</th>
								<th>流入时间</th>
								<th>流出时间</th>
								<th>处理时长</th>
								<th>任务状态</th>
								<th>注销标志</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="result" items="${resultList}">
				                 <tr class="text-c">
				                    <td>${result.taskName}</td>
				                    <td>${result.taskInKey}</td>
				                    <td>
				                    <c:if test="${result.handlerStatus eq '2'}">
				                    <app:codetrans codeType="UserCode" codeCode="${result.handlerUser}" />
				                    </c:if>
				                    <c:if test="${result.handlerStatus eq '3' || result.handlerStatus eq '9'}">
				                    <app:codetrans codeType="UserCode" codeCode="${result.taskOutUser}" />
				                    </c:if>
				                    </td>
				                    <td><app:codetrans codeType="UserCode" codeCode="${result.taskInUser}" /></td>
                				    <td> <fmt:formatDate value="${result.taskInTime}" pattern="yyyy-MM-dd  HH:mm:ss"/> </td>
				                    <td> <fmt:formatDate value="${result.taskOutTime}" pattern="yyyy-MM-dd  HH:mm:ss"/> </td>
				                    <c:set var="time" value="${result.taskOutTime.time - result.taskInTime.time}"></c:set>
				                    <c:set var="timeHour" value="${time%(1000*60*60*24)}"></c:set>
				                    <c:set var="timeMinutes" value="${time%(1000*60*60)}"></c:set>
				                    <c:set var="timeSeconds" value="${time%(1000*60)}"></c:set>
				                    <c:set var="numberDay" value="${1000*60*60*24}"></c:set>
				                    <c:set var="numberHour" value="${1000*60*60}"></c:set>
				                    <c:set var="numberMinutes" value="${1000*60}"></c:set>
				                    <c:set var="numberSeconds" value="${1000}"></c:set>
				                    <td><c:if test="${time>0}">
				                    <fmt:formatNumber type="number" value="${(time-time/numberDay)/numberDay > 1 ? (time-time/numberDay)/numberDay-0.5:0}" maxFractionDigits="0"/>天
				                    <fmt:formatNumber type="number" value="${(timeHour-timeHour/numberHour)/numberHour > 1 ? (timeHour-timeHour/numberHour)/numberHour-0.5:0}" maxFractionDigits="0"/>时
				                    <fmt:formatNumber type="number" value="${(timeMinutes-timeMinutes/numberMinutes)/numberMinutes > 1 ? (timeMinutes-timeMinutes/numberMinutes)/numberMinutes-0.5:0}" maxFractionDigits="0"/>分
				                    <fmt:formatNumber type="number" value="${(timeSeconds-timeSeconds/numberSeconds)/numberSeconds}" maxFractionDigits="0"/>秒
				                    </c:if>
				                    </td>
				                    <td><app:codetrans codeType="HandlerStatus" codeCode="${result.handlerStatus}" /></td>
				                    <td><c:if test="${result.handlerStatus == '9'}">是</c:if>
				                        <c:if test="${result.handlerStatus != '9'}">否</c:if>
				                    </td>
								</tr>
							</c:forEach>
						</tbody>		
					</table>
				</div>
			</div>
			<!--标签页 结束-->
		</div>
	</div>
	<script type="text/javascript">
	$(function() {
		$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
		bindValidForm($('#form'),null);
	});
	</script>
</body>
</html>
