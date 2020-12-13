<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>查勘登记</title>
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
		<input type="hidden" id="flowTaskId" value="${taskParamVo.taskId}" disabled="disabled">
		<a class="btn btn-primary" id="checkRegistMsg">案件备注</a>
		<c:if test="${assessSign eq '1' }">
		<a class="btn btn-primary" href="javascript:;" onclick="assessorView('${checkVo.registNo}')">公估费查看</a>
		</c:if>
		
		
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
<!--   	<a class="btn  btn-primary" onclick="viewCertifys('${checkVo.registNo}')">影像查看</a>    -->
		<a class="btn  btn-primary" onclick="imageMovieScan('${checkVo.registNo}')">信雅达影像查看</a>
		<a class="btn  btn-primary" onclick="cedlinfoshow('${checkVo.registNo}')">德联易控检测信息</a>
		<a class="btn  btn-primary" onclick="imageMovieUpload('${taskParamVo.taskId}')">信雅达影像上传</a>
		<a class="btn  btn-primary" onclick="reportPrint('${checkVo.registNo}')">代抄单打印</a>
		<c:if test="${registVo.isQuickCase eq '1' && !empty checkVo.id}">
		<a class="btn btn-primary" onclick="openPhotoVerify('Check')">快赔照片审核不通过</a>
		</c:if>
		<a class="btn btn-primary" id="surveyButton" onclick="createSurvey('${checkVo.registNo}','${taskParamVo.flowId}','${taskParamVo.taskId}','${taskParamVo.nodeCode}','${taskParamVo.handlerUser}');">调查</a>
		<a class="btn  btn-primary" onclick="scoreInfo('${checkVo.registNo}')">反欺诈评分</a>
		<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
		<a class="btn  btn-primary"  onclick="warnView('${checkVo.registNo}')">山东预警推送</a>
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
			<input type="hidden" id="oldClaim"  value="${oldClaim}">
			<input type="hidden" id="isMobileCase"  value="${isMobileCase}">
			<input type="hidden" id="nodeCode" name="nodeCode" value="${taskParamVo.subNodeCode}">
			<input type="hidden" id="registNo" name="checkVo.registNo" value="${checkVo.registNo}">
			<input type="hidden" id="checkId" name="checkVo.id" value="${checkVo.id}">
			<input type="hidden" name="checkVo.checkClass" value="${checkVo.checkClass}">
			<input type="hidden" id="checkTaskId" name="checkTaskVo.id" value="${checkTaskVo.id}">
			<input type="hidden" id="damageTime"  value="${registVo.damageTime}">
            
			<%-- <input type="hidden" id="urlParam" value="${taskParamVo.urlParam}"> --%>
            
			<input type="hidden" id="status" value="${taskParamVo.handlerStatus}">
			<input type="hidden" id="handlerIdKey" value="${taskParamVo.handlerIdKey}">
			<input type="hidden" id="flowTaskId"  name="flowTaskId" value="${taskParamVo.taskId}">
			<input type="hidden" id="flowId" value="${taskParamVo.flowId}">
			<input type="hidden" id="loss" value="${loss}">
			<input type="hidden" id="reportType" value="${registVo.reportType}">
			<!-- 山东影像对比 -->
			<input type="hidden" id="carRiskUserName" value="${carRiskUserName}"/>
			<input type="hidden" id="carRiskPassWord" value="${carRiskPassWord}"/>
			<input type="hidden" id="claimPeriod" value="${claimPeriod}"/>
			<input type="hidden" id="comparePicURL" value="${comparePicURL}"/>
			<input type="hidden" id="claimSequenceNo" value="${claimSequenceNo}"/>
			<input type="hidden" id="coinsFlag" value="${coinsFlag}"/>
			<input type="hidden" id="payrefFlag" value="${payrefFlag}"/>
			<input type="hidden" id="isCoinsFlag" value="${isCoinsFlag}"/>
			<!-- 报案信息 -->
			<%@include file="CheckRegistView.jspf"%>

			<!-- 查勘基本信息 -->
			<%@include file="CheckBasicInfo.jspf"%>

			<!--标的车处理 -->
			<%@include file="CheckMainCar.jsp"%>

			<!--三者车处理    -->
			<%@include file="CheckThirdCar.jsp"%>

			<!--财产损失处理 -->
			<%@include file="CheckPropLoss.jspf"%>

			<!--人员伤亡处理-->
			<%@include file="CheckPersonLoss.jspf"%>

			<!-- 巨灾信息-->
			<%@include file="CheckDisaster.jspf"%>

			<!-- 收款人信息 -->
			<%@include file="CheckPayeeInfo.jsp"%>

			<!-- 代位-->
			<%@include file="CheckSubrogation.jspf"%>

			<!-- 设置免赔率 -->
			<c:if test="${policyType ne '1'}">
				<%@include file="CheckExcess.jspf"%>
			</c:if>

			<input type="hidden" id="kindMap" value="${kindMap}">
			<!-- 公估费 -->
			<%-- <%@include file="CheckAssessorFee.jspf"%> --%>

			<!-- 查勘扩展信息  -->
			<%@include file="CheckExtInfo.jspf"%>
			
			<!-- 费用赔款信息 -->
			<%@include file="CheckFeePayInfo.jspf"%>

			<!-- 查勘意见 -->
			<%@include file="CheckClaimText.jspf"%>

			<br/>
			<!-- 底部按钮 -->
			<div class="btn-footer clearfix" style="margin-left: 40%;">
				<input type="hidden" name="saveType" value="${saveType}">
				<c:choose>
					<c:when test="${taskParamVo.subNodeCode eq 'Chk'}">
						<input class="btn btn-primary btn-kk" type="button" id="chkSave" onclick="checkSave('save')" value="暂存">
						<input class="btn btn-success btn-kk" type="button" id="checkSubmit" onclick="checkSave('submitCheck')" value="提交">
					</c:when>
					<c:when test="${taskParamVo.subNodeCode eq 'ChkRe'}">
						<input class="btn btn-primary btn-kk" type="button" id="chkReSave" onclick="chkReSaveOrSubmit('save')" value="暂存">
						<input class="btn btn-success btn-kk" type="button" id="chkReSubmit" onclick="chkReSaveOrSubmit('submitChkRe')" value="提交">
					</c:when>
					<c:otherwise>
						<input class="btn btn-primary btn-kk" type="button" id="chkBigSave" 
						onclick="chkBigSaves('${taskParamVo.subNodeCode}','save')" value="暂存"/>
						<input class="btn btn-primary btn-kk" type="button" id="chkBigSubmit"
						onclick="chkBigSaves('${taskParamVo.subNodeCode}','submit')" value="提交"/>
					</c:otherwise>
				</c:choose>
				<!-- <input type="button" class="btn btn-disabled" value="关闭" id="colseCheckButton" onclick="checkColse(this)"> -->
				<input type="button" class="btn btn-disabled" id="printButton" value="打印" onclick="">
			</div>

		</form>
		</div>
		<%-- <input hidden="hidden" id="registNo" value="${taskParamVo.registNo}"/>
		<input hidden="hidden" id="flowId" value="${taskParamVo.flowId}"/>
		<input hidden="hidden" id="nodeCode" value="${taskParamVo.nodeCode}"/> --%>
		<input hidden="hidden" id="handlerUser" value="${taskParamVo.handlerUser}"/>
		<%-- <input hidden="hidden" id="handleStatus" value="${taskParamVo.handleStatus}"/> --%>
	</div>

	<script type="text/javascript" src="/claimcar/js/checkEdit/check.js"></script>
	<script type="text/javascript" src="/claimcar/js/checkEdit/chkre.js"></script>
	<script type="text/javascript" src="/claimcar/js/checkEdit/subrogation.js"></script>
	<script type="text/javascript" src="/claimcar/js/checkEdit/checkfee.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/jquery-companySide-comparePic.js"></script>
	<script type="text/javascript">
		$(function() {
			createRegistRisk();
			$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon","current", "click", "0");
			getGBStatus($("#flowTaskId").val());
		});
		$("#checkRegistMsg").click(function checkRegistMsgInfo() {//打开案件备注之前检验报案号和节点信息是否为空
			var registNo = $("#registNo").val();
			var nodeCode = $("#nodeCode").val();
			createRegistMessage(registNo, nodeCode);
		});
	</script>
</body>
</html>