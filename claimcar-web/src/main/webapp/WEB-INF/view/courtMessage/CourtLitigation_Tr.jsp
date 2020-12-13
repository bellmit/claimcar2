<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<tr class="text-c">
	<td>${prpLCourtLitigation.tjzh}</td>
	<td>${prpLCourtLitigation.qsr}</td>
	<td>${prpLCourtLitigation.bqsr}</td>
	<td><fmt:formatDate  value="${prpLCourtLitigation.jrtjrq}" pattern="yyyy-MM-dd"/></td>
	<td><fmt:formatDate  value="${prpLCourtLitigation.tjwcrq}" pattern="yyyy-MM-dd"/></td>
	<td>${prpLCourtLitigation.tjzt}</td>
	<td>
		<input type="button" class="btn btn-secondary" href="javascript:;" id="chkReOrChkBig"
			onclick="showCourtView('${prpLCourtLitigation.id}',5)" inputIdx="-1" value="详情">
	</td>
</tr>