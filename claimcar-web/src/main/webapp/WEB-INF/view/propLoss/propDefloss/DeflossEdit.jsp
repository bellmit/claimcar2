<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>财产定损</title>
<style type="text/css">
.text-r {
	background-color: #F5F5F5
}

.tableoverlable label.form_label.col-1 {
	width: 9.3333%;
	padding-right: 0;
}

.tableoverlable .form_input.col-3 {
	width: 23%;
	margin-left: 1%;
	position: relative;
}

.tableoverlable .form_input.col-3 font.must {
	position: absolute;
	right: 0;
}

#subRiskTbody td  .radio-box {
	text-align: center;
}

.btn {margin-bottom:5px;}
</style>

</head>
<body>
	<div class="top_btn">
		<a class="btn btn-primary" onclick="viewEndorseInfo('${prpLdlossPropMainVo.registNo}')">保单批改记录</a>
		<a class="btn btn-primary" onclick="viewPolicyInfo('${prpLdlossPropMainVo.registNo}')">出险保单</a>
		<a class="btn  btn-primary" onclick="imageMovieUpload('${wfTaskVo.taskId}')">信雅达影像上传</a>
		<input type="hidden" id="flowTaskId" value="${wfTaskVo.taskId}" disabled="disabled">
		<a class="btn btn-primary" id="claimRemark" onclick="createRegistMessage('${prpLdlossPropMainVo.registNo}', '${nodeCode}')">案件备注</a>
		<a class="btn btn-primary" onclick="lawsuit('${prpLdlossPropMainVo.registNo}','${nodeCode}')">诉讼</a>
		<%-- <a class="btn btn-primary" id="logOut" onclick="claimCancel('${prpLdlossPropMainVo.registNo}','${prpLdlossPropMainVo.flowTaskId}')">注销/拒赔</a>
		<a class="btn btn-primary" id="logOut" onclick="claimCancelRecover('${prpLdlossPropMainVo.registNo}','${prpLdlossPropMainVo.flowTaskId}')">注销/拒赔恢复</a> --%>
	    <a class="btn btn-primary" onclick="viewBigOpinion('${prpLdlossPropMainVo.registNo}','${nodeCode}')">大案预报</a>		
		<a class="btn  btn-primary" onclick="checkSeeMessage('${prpLdlossPropMainVo.registNo}')">查勘详细信息</a>
		<c:choose>
		<c:when test="${wfTaskVo.handlerStatus!='3'}">
		<a class="btn btn-primary" onclick="certifyList('${prpLdlossPropMainVo.registNo}','${prpLdlossPropMainVo.flowTaskId}')">索赔清单</a>
		</c:when>
		<c:otherwise>
		<a class="btn btn-primary" onclick="certifyList('${prpLdlossPropMainVo.registNo}','${prpLdlossPropMainVo.flowTaskId}','yes')">索赔清单</a>
		</c:otherwise>
		</c:choose>
		
		<a class="btn btn-danger mb-5" onclick="createRegistRisk()">风险提示信息</a>
		<a class="btn btn-primary" onclick="payCustomSearch('N')">收款人账户信息维护</a>
		<a class="btn  btn-primary" onclick="seeUpdateMessage('${prpLdlossPropMainVo.registNo}')">查看估损更新轨迹</a>
	    <a class="btn btn-primary" onclick="caseDetails('${prpLdlossPropMainVo.registNo}')">历史出险信息</a>
		<a class="btn btn-primary" onclick="showFlow('${wfTaskVo.flowId}')">流程查询</a>
		<a class="btn  btn-primary" onclick="imageMovieScan('${prpLdlossPropMainVo.registNo}')">信雅达影像查看</a>
		
		<c:if test="${assessSign eq '1' }">
		<a class="btn btn-primary" href="javascript:;" onclick="assessorView('${prpLdlossPropMainVo.registNo}')">公估费查看</a>
		</c:if>
		<a class="btn  btn-primary" onclick="reportPrint('${prpLdlossPropMainVo.registNo}')">代抄单打印</a>
		<a class="btn btn-primary" id="surveyButton" onclick="createSurvey('${prpLdlossPropMainVo.registNo}','${wfTaskVo.flowId}','${wfTaskVo.taskId}','${wfTaskVo.nodeCode}','${wfTaskVo.handlerUser}');">调查</a>
		<a class="btn  btn-primary" onclick="lookLawSuit('${wfTaskVo.registNo}')">查看高院信息</a>
		<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
		<a class="btn  btn-primary"  onclick="warnView('${prpLdlossPropMainVo.registNo}')">山东预警推送</a>
	</div>
	<br>
	<div class="fixedmargin page_wrap">
	<form action="" method="post" id="defossform">
		<input type="hidden" name="nodeCode" value="${wfTaskVo.nodeCode}">
		<input type="hidden" id="nodeCode" name="subNodeCode" value="${wfTaskVo.subNodeCode}">
		<input type="hidden" name="status" value="${wfTaskVo.handlerStatus}">
		<input type="hidden" value="${wfTaskVo.showInfoXML}">
		<input type="hidden" value="${wfTaskVo.handlerIdKey}">
		<input type="hidden" value="${wfTaskVo.itemName}">
		<input type="hidden" name="registNo" value="${wfTaskVo.registNo}">
		<input type="hidden" value="${sindex}" id="sindex">
		<div id="tab-system" class="HuiTab">
			<div class="tabBar f_gray4 cl">
				<span>当前定损</span> <span>损失信息</span>
			</div>

			<!--选项卡一 -->
			<div class="tabCon clearfix">
					<!-- 基本信息 -->
					<%@include file="DeflossEdit_BaseInfo.jspf"%>
					<!-- 受损财产信息 -->
					<%@include file="DeflossEdit_DamageInfo.jspf"%>
					<!-- 费用赔款信息 -->
					<%@include file="../../loss-common/PropEdit_FeePayInfo.jspf"%>
					<!-- 施救情况 -->
					<%@include file="DeflossEdit_RescueInfo.jspf"%>
					<!-- 定损意见 -->
					<%@include file="DeflossEdit_Opinion.jspf"%>
					<!-- 意见列表 -->
					<%@include file="../../loss-common/propOpinionList.jspf"%>
			</div>

			<!-- 选项卡二 -->
			<div class="tabCon clearfix">
				<!--车俩损失损失列表-->
				<%@include file="../../propLoss/lossInfoView/carLossListInfo.jspf"%>
				<!-- 财产损失损失列表 -->
				<%@include file="../../propLoss/lossInfoView/propLossListInfo.jspf"%>
				<!--人伤损失列表 -->
				<%@include file="../../propLoss/lossInfoView/personLossListInfo.jspf"%>
			</div>
		</div>
		<div class="text-c">
			<br/>
			<input class="btn btn-primary " id="pend" onclick="save1('save')" type="button" value="暂存">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input class="btn btn-primary " id="save" onclick="save1('submitLoss')" type="button" value="提交">
		</div>
	</form>
	</div>
	<script type="text/javascript">
	$(function() {
		//切换选项卡 
		//$.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current","click", "0");
		//$.Huitab("#tab_prCon .tabBar span","#tab_prCon .tabCon","current","click","1");
	});
	</script>
	<script type="text/javascript" src="/claimcar/js/propLoss/PropLossEditRow.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript" src="/claimcar/js/propLoss/PropLossEdit.js"></script>
	<script type="text/javascript">
	$(function() {
		var	sindex=$("#sindex").val();
	    if(sindex=="1"){
			$("a").attr("class","btn btn-disabled");
			$("a").removeAttr("onclick");
		}else{
			createRegistRisk();	
		}
	    getGBStatus($("#flowTaskId").val());
	});
</script>

</body>

</html>