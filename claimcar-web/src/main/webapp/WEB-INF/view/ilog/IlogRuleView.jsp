<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<html>
<head>
<title>ILOG规则详情</title>
</head>
<body>
	<c:choose>
		<c:when test="${flag  eq  '1'}">
			<div class="view-page">
				<!-- 基本信息 -->
				<%@include file="jspf/BaseRuleInfo.jspf"%>
				<c:choose>
					<c:when test="${not empty vPriceDetailInfoVoList }">
						<%@include file="jspf/VPriceRuleInfo.jspf"%>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${not empty vLCarDetailInfoVoList}">
						<%@include file="jspf/VLCarRuleInfo.jspf"%>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${not empty vLPropDetailInfoVoList}">
						<%@include file="jspf/VLPropRuleInfo.jspf"%>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${not empty pLVerifyDetailInfoVoList}">
						<%@include file="jspf/PLVerifyRuleInfo.jspf"%>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${not empty certiDetailInfoVoList}">
						<%@include file="jspf/CertiRuleInfo.jspf"%>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${not empty compeDetailInfoVoList}">
						<%@include file="jspf/CompeRuleInfo.jspf"%>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${not empty vClaimDetailInfoVoList }">
						<%@include file="jspf/VClaimRuleInfo.jspf"%>
					</c:when>
				</c:choose>
			</div>
		</c:when>
		<c:otherwise>
			<div style="text-align: center;">
				<font size='3' color='#333'>无ILOG交互信息</font>
			</div>
		</c:otherwise>
	</c:choose>
</body>
<script type="text/javascript" src="/claimcar/js/common/common.js"></script>
<script type="text/javascript">
	
</script>
</html>