<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<c:if test="${empty sysMsgContents }">
	<div class="noticecont">
		<img src="/claimcar/images/zwnr.png" />
	</div>
</c:if>
<c:forEach var="sysMsgContent" items="${sysMsgContents }" varStatus="status">
	<div class="noticelist clearfix" id="SysMsgRow">
		<div class="fl s_img">
			<img src="/claimcar/images/notice.png" />
		</div>
		<div class="fl notice">
			<span>${sysMsgContent.msgContents }</span><br>
			<span class="f_gray4">
			<fmt:formatDate value="${sysMsgContent.createDate }" pattern="yyyy-MM-dd" />
			<app:codetrans codeType="ComCode" codeCode="${sysMsgContent.userComCode }" />
			<app:codetrans codeType="UserCode" codeCode="${sysMsgContent.createUser }" /> 回复了你
			<a href="javascript:createRegistMessage('${sysMsgContent.bussNo }','','${sysMsgContent.id }')">查看</a>
			</span>
		</div>
	</div>
</c:forEach>
