<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
	<c:when test="${LoadPagePath=='lossperson/common/casualtyInfoView/CasualtyInfo_TabCon.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/common/casualtyInfoView/CasualtyInfo_TabCon.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/common/casualtyInfoEdit/CasualtyInfo_TabCon.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/common/casualtyInfoEdit/CasualtyInfo_TabCon.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/common/casualtyInfoEdit/TabCon_InjuredPart_Tr.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/common/casualtyInfoEdit/TabCon_InjuredPart_Tr.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/common/casualtyInfoEdit/TabCon_HospitalCase_Tr.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/common/casualtyInfoEdit/TabCon_HospitalCase_Tr.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/persTraceEdit/PersTraceEdit_ChargeDialog.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/persTraceEdit/PersTraceEdit_ChargeDialog.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/common/casualtyInfoEdit/TabCon_NurseInfo_Tr.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/common/casualtyInfoEdit/TabCon_NurseInfo_Tr.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/common/casualtyInfoEdit/TabCon_RaiseInfo_Tr.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/common/casualtyInfoEdit/TabCon_RaiseInfo_Tr.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/persTraceEdit/PersTraceEdit_Charge_Tr.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/persTraceEdit/PersTraceEdit_Charge_Tr.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/persTraceEdit/casualtyInfo/SubmitPersTraceNext.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/persTraceEdit/casualtyInfo/SubmitPersTraceNext.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/persTraceVerify/casualtyInfo/SubmitPLVerify.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/persTraceVerify/casualtyInfo/SubmitPLVerify.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/common/casualtyInfoEdit/TabCon_FeeDialog.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/common/casualtyInfoEdit/TabCon_FeeDialog.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/common/casualtyInfoEdit/TabCon_TraceRecord_Tr.jspf'}">
		<c:forEach var="persTraceFee" items="${persTraceFeeVos }" varStatus="recordStatus">
			<c:set var="traceRecordSize" value="${size+recordStatus.index}" />
			<%@include file="/WEB-INF/view/lossperson/common/casualtyInfoEdit/TabCon_TraceRecord_Tr.jspf"%>
		</c:forEach>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/feeModifyRecord/FeeModifyRecord.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/feeModifyRecord/FeeModifyRecord.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/persTraceChargeAdjust/SubmitChargeMod.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/persTraceChargeAdjust/SubmitChargeMod.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/persTracePLBig/SubmitPLBig.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/persTracePLBig/SubmitPLBig.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/persTraceEdit/PersTraceEdit_Opinion_Tr.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/persTraceEdit/PersTraceEdit_Opinion_Tr.jspf"%>
	</c:when>
	<c:when test="${LoadPagePath=='lossperson/persTraceEdit/PersTraceEdit_Charge_Tr.jspf'}">
		<%@include file="/WEB-INF/view/lossperson/persTraceEdit/PersTraceEdit_Charge_Tr.jspf"%>
	</c:when>
	<c:otherwise>
		<jsp:include page="/WEB-INF/view/${LoadPagePath }"/>
	</c:otherwise>
</c:choose>