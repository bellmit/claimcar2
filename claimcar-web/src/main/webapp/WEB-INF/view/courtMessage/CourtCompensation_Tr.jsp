<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<tr class="text-c">
	<td>${prpLCourtCompensation.dsr}</td>
	<td>${prpLCourtCompensation.ylf}</td>
	<td>${prpLCourtCompensation.zyhsbz}</td>
	<td>${prpLCourtCompensation.yyf}</td>
	<td>${prpLCourtCompensation.hxzlf}</td>
	<td>${prpLCourtCompensation.zrf}</td>
	<td>
		<input type="button" class="btn btn-secondary" href="javascript:;" id="chkReOrChkBig"
			onclick="showCourtView('${prpLCourtCompensation.id}',3)" inputIdx="-1" value="详情">
	</td>
</tr>