<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<c:if test="${empty sysUtiBulletinVoList }">
	<div class="noticecont">
		<img src="/claimcar/images/zwnr.png" />
	</div>
</c:if>
<c:forEach var="sysUtiBulletinVo" items="${sysUtiBulletinVoList }" varStatus="status">
	<div class="noticelist clearfix" id="SyssysUtiBulletinRow">
		<div class="fl s_img">
			<img src="/claimcar/images/notice.png" />
		</div>
		<div class="fl notice">
			<span>${sysUtiBulletinVo.title }</span><br>
			<span class="f_gray4">
			<fmt:formatDate value="${sysMsgContent.createDate }" pattern="yyyy-MM-dd" />
			</span>
		</div>
	</div>
</c:forEach>
