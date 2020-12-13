<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="prpLCourtIdentify" items="${courtMessageVo.prpLCourtIdentifys}" varStatus="status">
<tr class="text-c">
	<td>${prpLCourtIdentify.appraisalno}</td>
	<td>${prpLCourtIdentify.appraisal}</td>
	<td>${prpLCourtIdentify.appraisalsum}</td>
	<td>${prpLCourtIdentify.appraisaladdr}</td>
	<td>${prpLCourtIdentify.appraiser}</td>
	<td>${prpLCourtIdentify.bappraiser}</td>
	<td>
		<input type="button" class="btn btn-secondary" href="javascript:;" id="chkReOrChkBig"
			onclick="showCourtView('${prpLCourtIdentify.id}',2)" inputIdx="-1" value="详情">
	</td>
</tr>
</c:forEach>