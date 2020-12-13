<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<div>
	注销时间：<fmt:formatDate value="${prpLRegistVo.cancelTime}" type="both"/><br/>
	<c:set var="cancelUser">
		<app:codetrans codeType="UserCode" codeCode="${prpLRegistExtVo.cancelUser}"/> 
	</c:set>
	注销人员：${prpLRegistExtVo.cancelUser}&nbsp;&nbsp;&nbsp;${cancelUser}<br/>
	注销原因：
	<c:set var="cancelReason">
		<app:codetrans codeType="ReportCancel" codeCode="${prpLRegistExtVo.cancelReason}"/>
	</c:set>
	${cancelReason}

</div>
