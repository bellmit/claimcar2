<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<tr class="text-c">
	<td>${prplCourtMediation.mediationnum}</td>
	<td><app:codetrans codeType="mediationtype" codeCode="${prplCourtMediation.mediationtype}" /></td>
	<td>${prplCourtMediation.mediation}</td>
	<td>${prplCourtMediation.handler}</td>
	<td>${prplCourtMediation.mediationaddr}</td>
	<td>${prplCourtMediation.mediationstatus}</td>
	<td>
		<input type="button" class="btn btn-secondary" href="javascript:;" id="chkReOrChkBig"
			onclick="showCourtView('${prplCourtMediation.id}',7)" inputIdx="-1" value="详情">
	</td>
</tr>