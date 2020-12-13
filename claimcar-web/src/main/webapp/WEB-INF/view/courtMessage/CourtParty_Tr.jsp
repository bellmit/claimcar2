<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<tr class="text-c">
	<td>${prpLCourtParty.name}</td>
	<td><app:codetrans codeType="PersonIdType" codeCode="${prpLCourtParty.personidtype}" /></td>
	<td>${prpLCourtParty.personID}</td>
	<td><app:codetrans codeType="PersonType" codeCode="${prpLCourtParty.persontype}" /></td>
	<td>${prpLCourtParty.phone}</td>
	<td>${prpLCourtParty.email}</td>
	<td>
		<input type="button" class="btn btn-secondary" href="javascript:;" id="chkReOrChkBig"
			onclick="showCourtView('${prpLCourtParty.id}',1)" inputIdx="-1" value="详情">
	</td>
</tr>