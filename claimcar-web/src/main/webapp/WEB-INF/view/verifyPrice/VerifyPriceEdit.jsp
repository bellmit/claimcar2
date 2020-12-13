
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>核价处理</title>
<style type="text/css">
.text-r {
	background-color: #F5F5F5
}

#content_d  label.check-box {
	margin-right: 20px;
}

.replaceParts {
	overflow: visible;
}

.replacePartsBtn {
	position: relative;
	top: -7px;
}

.formtable .form_input.overTextPre {
	word-break: break-all;
	word-wrap: break-word;
	height: auto;
	line-height: 20px;
}

.btn {margin-bottom:5px;}
</style>
</head>
<body>
	<div class="top_btn">
		<a class="btn btn-primary" onclick="viewEndorseInfo('${lossCarMainVo.registNo }')">保单批改记录</a>
		<a class="btn btn-primary" onclick="viewPolicyInfo('${lossCarMainVo.registNo }')">出险保单</a>
		<a class="btn  btn-primary" onclick="imageMovieUpload('${taskVo.taskId}')">信雅达影像上传</a>
		<input type="hidden" id="flowTaskId" value="${taskVo.taskId}" disabled="disabled">
		<a class="btn btn-primary" onclick="createRegistMessage('${lossCarMainVo.registNo }', '${taskVo.subNodeCode}')">案件备注</a>
		<a class="btn btn-primary" onclick="lawsuit('${lossCarMainVo.registNo }' ,'${taskVo.subNodeCode}')">诉讼</a>
		<a class="btn  btn-primary" onclick="seeUpdateMessage('${lossCarMainVo.registNo}')">查看估损更新轨迹</a> 
		<a class="btn btn-primary" onclick="caseDetails('${lossCarMainVo.registNo}')">历史出险信息</a>
		<a class="btn  btn-primary" onclick="viewBigOpinion('${lossCarMainVo.registNo }','${taskVo.subNodeCode}')">大案预报</a>
		<%-- 	<c:choose>
			<c:when test="${taskVo.handlerStatus eq '3'}">
		<a class="btn btn-disabled">注销/拒赔</a>
		</c:when>
		<c:otherwise>
		<a class="btn btn-primary" id="logOut" onclick="claimCancel('${lossCarMainVo.registNo}','${taskVo.taskId}')">注销/拒赔</a>
		</c:otherwise>
		</c:choose> --%>
		<%-- <a class="btn btn-primary" id="logOut"
			onclick="claimCancelRecover('${lossCarMainVo.registNo}','${taskVo.taskId}')">注销/拒赔恢复</a> --%>
		<a class="btn btn-primary" onclick="checkSeeMessage('${lossCarMainVo.registNo}')">查勘详细信息</a>
		<a class="btn btn-primary" onclick="certifyList('${lossCarMainVo.registNo}','${taskVo.taskId}')">索赔清单</a>
		<a class="btn btn-danger" href="javascript:;" onclick="createRegistRisk()">风险提示信息</a>
		<a class="btn btn-primary" target="_blank" onclick="openTaskEditWin('定损轨迹','/claimcar/defloss/deflossHisView.do?defLossMainId=${lossCarMainVo.id}')">车辆定损轨迹</a>
<!-- 	<a class="btn btn-primary" onclick="viewCertifys('${lossCarMainVo.registNo}')">影像查看</a>    -->
		<a class="btn  btn-primary" onclick="imageMovieScan('${lossCarMainVo.registNo}')">信雅达影像查看</a>
		<a class="btn btn-primary" onclick="showFlow('${taskVo.flowId}')">流程查询</a>
		<c:if test="${assessSign eq '1' }">
		<a class="btn btn-primary" href="javascript:;" onclick="assessorView('${lossCarMainVo.registNo}')">公估费查看</a>
		</c:if>
		<a class="btn btn-primary" onclick="reportPrint('${lossCarMainVo.registNo}')">代抄单打印</a>
		<a class="btn  btn-primary" onclick="cedlinfoshow('${lossCarMainVo.registNo}')">德联易控检测信息</a>
		<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
		<c:if test="${policeInfoFlag eq '1'}">
			<a class="btn btn-primary" onclick="policeInfoShow('${lossCarMainVo.registNo}')">预警信息</a>	
			<a class="btn btn-primary" id="comCheckPic" name="comCheckPic" onclick="comCheckPic()">影像比对</a>
		</c:if>
		<c:if test="${roleFlag eq '1' }">		
			<a class="btn btn-primary"  onclick="ruleView('${lossCarMainVo.registNo}','${lossCarMainVo.riskCode}','${taskVo.nodeCode}','${lossCarMainVo.licenseNo}','${taskVo.upperTaskId}')">ILOG规则信息查看</a>
		</c:if>
		<a class="btn btn-primary" id="surveyButton" onclick="createSurvey('${lossCarMainVo.registNo}','${taskVo.flowId}','${taskVo.taskId}','${taskVo.nodeCode}','${taskVo.handlerUser}');">调查</a>			
		<a class="btn  btn-primary"  onclick="warnView('${lossCarMainVo.registNo}')">山东预警推送</a>
	</div>
	<br />
	<br />
	<div class="fixedmargin page_wrap">
		<form id="verifyPform" role="form" method="post" name="fm">
			<!-- 基本信息 -->
			<%@include file="VerifyPrice_Info.jspf"%>
			<!-- 车辆信息 -->
			<%@include file="VerifyPrice_CarInfo.jspf"%>
			<div id="refreshDiv">
				<%@include file="VerifyPrice_jyLoss.jsp"%>
			</div>
			<!-- 核价意见 -->
			<%@include file="VerifyPrice_Opinion.jspf"%>
			<!-- 意见列表 -->
			<%@include file="../loss-common/DeflossEdit_OpinionList.jspf"%>
		</form>

		<div class="text-c" id="buttonDiv">
			<br />
			<c:if test="${taskVo.handlerStatus !='3' }">
				<input class="btn btn-primary" id="save" onclick="save(this,'save')"
					type="submit" value="暂存">
					&nbsp;&nbsp;&nbsp;
					<input class="btn btn-primary btn-agree" id="submit" onclick="save(this,'submitVprice')" type="submit" value="审核通过" />
					&nbsp;&nbsp;&nbsp;
						
					<c:if test="${commonVo.currencyLevel < 9}">
					<!-- 最高级不显示 提交上级 -->
					<input class="btn btn-primary btn-agree" id="audit"
						onclick="save(this,'audit')" type="submit" value="提交上级">
						&nbsp;&nbsp;&nbsp;
					</c:if>
				<c:if test="${commonVo.lowerButton}">
					<input class="btn btn-primary btn-noagree" id="backLower"
						onclick="save(this,'backLower')" type="submit" value="退回下级">
							&nbsp;&nbsp;&nbsp;
					</c:if>
				<c:if test="${commonVo.currencyLevel <9}">
					<!-- 总公司 不显示 退回定损 -->
					<input class="btn btn-primary btn-noagree" id="backLoss"
						onclick="save(this,'backLoss')" type="submit" value="退回定损">
				</c:if>
			</c:if>
		</div>
	</div>
	<script type="text/javascript"
		src="/claimcar/js/verifyPrice/VerifyPrice.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/jquery-companySide-comparePic.js"></script>
	<script type="text/javascript">
	$(function() {
		createRegistRisk();
		getGBStatus($("#flowTaskId").val());
	});
</script>
</body>
</html>