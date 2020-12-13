<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<tr class="text-c">
	<td><fmt:formatDate  value="${prpLCourtClaim.ladate}" pattern="yyyy-MM-dd"/></td>
	<td>${prpLCourtClaim.laaydm}</td>
	<td>${prpLCourtClaim.jbfydm}</td>
	<td>${prpLCourtClaim.jbfymc}</td>
	<td>${prpLCourtClaim.labdje}</td>
	<td><app:codetrans codeType="ISValid" codeCode="${prpLCourtClaim.issqtj}" /></td>
	<td>
		<input type="button" class="btn btn-secondary" href="javascript:;" id="chkReOrChkBig"
			onclick="showCourtView('${prpLCourtClaim.id}',4)" inputIdx="-1" value="详情">
	</td>
</tr>