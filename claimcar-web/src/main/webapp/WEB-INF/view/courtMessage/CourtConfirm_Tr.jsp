<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<tr class="text-c">
	<td>${prplCourtComfirm.zjhm}</td>
	<td>${prplCourtComfirm.sqr}</td>
	<td> <fmt:formatDate  value="${prplCourtComfirm.sqsj}" pattern="yyyy-MM-dd"/></td>
	<td>${prplCourtComfirm.slfy}</td>
	<td>${prplCourtComfirm.ah}</td>
	<td>${prplCourtComfirm.sqzt}</td>
	<td>
		<input type="button" class="btn btn-secondary" href="javascript:;" id="chkReOrChkBig"
			onclick="showCourtView('${prplCourtComfirm.id}',6)" inputIdx="-1" value="详情">
	</td>
</tr>