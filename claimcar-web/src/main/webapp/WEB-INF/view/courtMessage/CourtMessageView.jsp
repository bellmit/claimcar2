<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>诉讼信息</title>
<link href="../css/checkMainEdit.css" rel="stylesheet" type="text/css">

<style>
.reportInfo{}

.btn {margin-bottom:5px;}

</style>
</head>
<body id="kl">
	<%-- <%@include file="CheckMainEdit_RiskDialog.jspf"%> --%>
	<div class="top_btn">
		<a class="btn btn-primary" href="javascript:;" onclick="viewEndorseInfo('${checkVo.registNo}')">保单批改记录</a>
		<a class="btn btn-primary" href="javascript:;" onclick="viewPolicyInfo('${checkVo.registNo}')">出险保单</a>
		<a class="btn btn-primary" href="javascript:;" onclick="uploadCertifys('${taskParamVo.taskId}')">资料上传</a>
		<a class="btn btn-primary" id="checkRegistMsg">案件备注</a>
		<c:if test="${assessSign eq '1' }">
		<a class="btn btn-primary" href="javascript:;" onclick="assessorView('${checkVo.registNo}')">公估费查看</a>
		</c:if>
		<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
		
		<%-- <a class="btn btn-primary" id="logOut" onclick="claimCancel('${checkVo.registNo}','${taskParamVo.taskId}')">注销/拒赔</a> --%>
		<%-- 		    <a class="btn btn-primary" id="logOut" onclick="claimCancelRecover('${checkVo.registNo}','${taskParamVo.taskId}')">注销/拒赔恢复</a>
		
		<c:choose>
			<c:when test="${taskParamVo.handlerStatus eq '3'}">
				<a class="btn btn-primary" id="logOut" onclick="claimCancel('${checkVo.registNo}','${taskParamVo.taskId}')">注销/拒赔</a>
			</c:when>
			<c:otherwise>
				<a class="btn btn-primary" id="logOut" onclick="claimCancel('${checkVo.registNo}','${taskParamVo.taskId}')">注销/拒赔</a>
			</c:otherwise>
		</c:choose> --%>
		<%-- <c:choose>
		<c:when test="${taskParamVo.subNodeCode == 'ChkRe'}">
		<a class="btn btn-disabled">注销/拒赔</a>
		</c:when>
		<c:otherwise>
		<a class="btn btn-primary" id="logOut" onclick="claimCancel('${checkVo.registNo}','${taskParamVo.taskId}')">注销/拒赔</a>
		</c:otherwise>
	   </c:choose> --%>
	    <%-- <a class="btn btn-primary" id="logOut" onclick="claimCancelRecover('${checkVo.registNo}','${taskParamVo.taskId}')">注销/拒赔恢复</a> --%>
		<%-- <a class="btn btn-primary" id="logOut" onclick="claimCancelRecover('${checkVo.registNo}','${taskParamVo.taskId}')">注销/拒赔恢复</a> --%>
		<%--<a class="btn btn-primary" onclick="certifyPrint1('${checkVo.registNo}','${taskParamVo.handlerStatus}',this)">单证打印</a>--%>
		<c:choose>
			<c:when test="${(taskParamVo.handlerStatus eq '3') || (taskParamVo.nodeCode eq 'ChkBig') || (taskParamVo.handlerStatus eq '2' && taskParamVo.subNodeCode eq 'ChkRe')}">
			<a class="btn btn-primary" onclick="CheckPrint('${checkVo.registNo}','checkTask.ajax')">单证打印</a>
				
			</c:when>
			<c:otherwise>
				<a class="btn btn-disabled">单证打印</a>
			</c:otherwise>
		</c:choose>
	   <!-- (taskParamVo.handlerStatus eq '3') || (taskParamVo.nodeCode eq 'ChkBig') || -->
		<a class="btn  btn-primary" onclick="seeUpdateMessage('${checkVo.registNo}')">查看估损更新轨迹</a> 
		<a class="btn btn-primary" onclick="caseDetails('${checkVo.registNo}')">历史出险信息</a>
		
		<!-- 大案预报  -->
		<c:choose>
			<c:when test="${textSize eq 0}">
				<a class="btn btn-disabled">大案预报</a>
			</c:when>
			<c:otherwise>
				<a class="btn btn-primary" onclick="viewBigOpinion('${checkVo.registNo}','${taskParamVo.subNodeCode}')">大案预报</a>
			</c:otherwise>
		</c:choose>
		<!-- 多保单关联与取消  -->
		<c:choose>
			<c:when test="${(registVo.tempRegistFlag ne '1')&&(registVo.registTaskFlag ne '7')&&(taskParamVo.handlerStatus eq '2' && taskParamVo.subNodeCode ne 'ChkRe')}">
					<button onclick="relationshipList('${checkVo.registNo}','check')" type="button" class="btn btn-primary">多保单关联与取消</button>
			</c:when>
			<c:otherwise>
					&nbsp;<input value="多保单关联与取消" type="button"
					class="btn btn-disabled" />
			</c:otherwise>
		</c:choose>
		
		<br/>
		<a class="btn btn-primary" href="javascript:;" onclick="certifyList('${checkVo.registNo}','${taskParamVo.taskId}')">索赔清单</a>
		<a class="btn btn-danger" href="javascript:;" onclick="createRegistRisk()">风险提示信息</a>
		<a class="btn  btn-primary" onclick="viewCertifys('${checkVo.registNo}')">影像查看</a>
		<a class="btn  btn-primary" onclick="reportPrint('${checkVo.registNo}')">代抄单打印</a>
		<a class="btn  btn-primary" onclick="cedlinfoshow('${checkVo.registNo}')">德联易控检测信息</a>
		<c:if test="${registVo.isQuickCase eq '1' && !empty checkVo.id}">
		<a class="btn btn-primary" onclick="openPhotoVerify('Check')">快赔照片审核不通过</a>
		</c:if>
		<a class="btn btn-primary" id="surveyButton" onclick="createSurvey('${checkVo.registNo}','${taskParamVo.flowId}','${taskParamVo.taskId}','${taskParamVo.nodeCode}','${taskParamVo.handlerUser}');">调查</a>
		<a class="btn  btn-primary" onclick="scoreInfo('${checkVo.registNo}')">反欺诈评分</a>
		<c:if test="${policeInfoFlag eq '1'}">
			<a class="btn btn-primary" onclick="policeInfoShow('${checkVo.registNo}')">预警信息</a>		
			<a class="btn  btn-primary" id="comCheckPic" name="comCheckPic" onclick="comCheckPic()">影像比对</a>
		</c:if>
	</div>
	<br/>
	<br/>
		<a class="btn  btn-primary" id="comCheckPic" name="comCheckPic" onclick="comCheckPic()">比对影像</a>
	</div><br/>
	
	
	<div class="fixedmargin page_wrap">
		<div class="table_cont">
		<form id="saveMain" class="saveform" role="form" method="post" action="#">
			<!-- 案件信息 -->
			<%@include file="CourtRegist.jspf"%>

			<!-- 事故基本信息 -->
			<%@include file="CourtAccident.jspf"%>
			
			<!--当事人信息 -->
			<%@include file="CourtParty.jsp"%>
			
			<!--赔偿信息 -->
			<%@include file="CourtCompensation.jsp"%>
			
			<!--网上立案信息-->
			<%@include file="CourtClaim.jsp"%>
			
			<!-- 诉前调解信息-->
			<%@include file="CourtLitigation.jsp"%>
			
			<!-- 司法确认信息 -->
			<%@include file="CourtConfirm.jsp"%>
			
			<!-- 文件信息 -->
			<%@include file="CourtFile.jsp"%>
			
			 <!--代理人信息 -->
			<%@include file="CourtAgent.jsp"%>
			
			
			<!--鉴定表信息  -->
			<%@include file="CourtIdentify.jsp"%>
			 
			<!-- 调解信息 -->
			<%@include file="CourtMediation.jsp"%> 

		</form>
		</div>
		<%-- <input hidden="hidden" id="registNo" value="${taskParamVo.registNo}"/>
		<input hidden="hidden" id="flowId" value="${taskParamVo.flowId}"/>
		<input hidden="hidden" id="nodeCode" value="${taskParamVo.nodeCode}"/> --%>
		<input hidden="hidden" id="handlerUser" value="${taskParamVo.handlerUser}"/>
		<%-- <input hidden="hidden" id="handleStatus" value="${taskParamVo.handleStatus}"/> --%>
	</div>

	<script type="text/javascript" src="/claimcar/js/court/court.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/jquery-companySide-comparePic.js"></script>
	<script type="text/javascript">
		$(function() {
			$("body textArea").attr("disabled","disabled");
		});
	</script>
</body>
</html>