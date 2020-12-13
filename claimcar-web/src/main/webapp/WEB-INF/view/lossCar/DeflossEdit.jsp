<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>定损处理</title>
		<link href="../css/DeflossEdit.css" rel="stylesheet" type="text/css">
		<script type="text/javascript">
		$(function(){
			$.Huitab("#tab_demo .tabBar span","#tab_demo .tabCon","current","click","0");
			
			});
		</script>
		<style type="text/css">
			.text-r{
				background-color: #F5F5F5
			}
			 
			.table_sub th, .table_sub td {
					padding: 8px;
					line-height: 15px;
				}
			.btn {margin-bottom:5px;}
			</style>
	</head>
	<body>
		<div class="top_btn">
		    <input type="hidden" value="${selfClaimFlag}" id="selfClaimFlag">
		    <input type="hidden" value="${selfDlossAmout}" id="selfDlossAmout">
		    <input type="hidden" value="${signIndex}" id="signIndex">
		    <input type="hidden" value="${oldClaim}" id="oldClaim">
		    <input type="hidden" value="${jy2Flag}" id="jy2Flag">
		    <input type="hidden" value="${isMobileCase}" id="isMobileCase">
			<input type="hidden" id="isQuickCase" value="${registVo.isQuickCase}"/>
		    <input type="hidden" value="${lossCarMainVo.deflossCarType}" id="deflossCarType">
		    <input type="hidden" value="${sign}" id="sign">
		    <input type="hidden" value="${sindex}" id="sindex">
			<input type="hidden" value="${kindCodeX2}" id="kindCodeX2">
			<input type="hidden" value="${lossCarMainVo.comCode}" id="comCode">
			<input type="hidden" value="${riskCode}" id="riskCode1">
			
		    <!-- 山东影像对比 -->
			<input type="hidden" id="carRiskUserName" value="${carRiskUserName}"/>
			<input type="hidden" id="carRiskPassWord" value="${carRiskPassWord}"/>
			<input type="hidden" id="claimPeriod" value="${claimPeriod}"/>
			<input type="hidden" id="comparePicURL" value="${comparePicURL}"/>
			<input type="hidden" id="claimSequenceNo" value="${claimSequenceNo}"/>
			<input type="hidden" value="${lossCarMainVo.registNo}" id="jyRegistNo">
			<input type="hidden" value="${lossCarMainVo.id}" id="jyId">
			<a class="btn  btn-primary" onclick="viewEndorseInfo('${lossCarMainVo.registNo }')">保单批改记录</a>
			<a class="btn  btn-primary" onclick="viewPolicyInfo('${lossCarMainVo.registNo }')">出险保单</a>
			<a class="btn  btn-primary" onclick="imageMovieUpload('${taskVo.taskId}')">信雅达影像上传</a>
					<input type="hidden" id="flowTaskId" value="${taskVo.taskId}" disabled="disabled">
			<a class="btn btn-primary" id="checkRegistMsg" onclick="createRegistMessage('${lossCarMainVo.registNo }', '${taskVo.subNodeCode}')">案件备注</a>
			<a class="btn btn-primary" onclick="lawsuit('${lossCarMainVo.registNo }' ,'${taskVo.subNodeCode}')">诉讼</a>
		<%-- 	<a class="btn btn-primary" id="logOut" onclick="claimCancel('${lossCarMainVo.registNo}','${taskVo.taskId}')">注销/拒赔</a>
		    <a class="btn btn-primary" id="logOut" onclick="claimCancelRecover('${lossCarMainVo.registNo}','${taskVo.taskId}')">注销/拒赔恢复</a> --%>
			<c:choose>
				<c:when test="${taskVo.handlerStatus !='2' && taskVo.handlerStatus !='3'}">
					<a class="btn btn-disabled" >单证打印</a>
				</c:when>
				<c:otherwise>
					<a class="btn btn-primary"  onclick="SetlossPrint('${lossCarMainVo.id}','${lossCarMainVo.registNo}')">单证打印</a>
				</c:otherwise>
			</c:choose> 
			
			<a class="btn  btn-primary" onclick="seeUpdateMessage('${lossCarMainVo.registNo}')">查看估损更新轨迹</a>
			<a class="btn  btn-primary" onclick="showFlow('${taskVo.flowId}')">流程查询</a>
			 <a class="btn btn-primary" onclick="caseDetails('${lossCarMainVo.registNo}')">历史出险信息</a>
			<a class="btn  btn-danger"  onclick="viewBigOpinion('${lossCarMainVo.registNo }','${taskVo.subNodeCode}')">大案预报</a>
			<a class="btn  btn-primary" onclick="checkSeeMessage('${lossCarMainVo.registNo}')">查勘详细信息</a>
			<c:choose>
			<c:when test="${taskVo.handlerStatus !='3'}">
			<a class="btn  btn-primary" onclick="certifyList('${lossCarMainVo.registNo}','${taskVo.taskId}')">索赔清单</a>
			</c:when>
			<c:otherwise>
			<a class="btn  btn-primary" onclick="certifyList('${lossCarMainVo.registNo}','${taskVo.taskId}','yes')">索赔清单</a>
			</c:otherwise>
			
			</c:choose>
			
			<a class="btn btn-danger" href="javascript:;" onclick="createRegistRisk()">风险提示信息</a>
			<a class="btn btn-primary" onclick="payCustomSearch('N')">收款人账户信息维护</a>
			 <c:choose>
		 <c:when test="${!empty lossCarMainVo.defEndDate }">
				<a class="btn  btn-primary" target="_blank" onclick="openTaskEditWin('定损轨迹','/claimcar/defloss/deflossHisView.do?defLossMainId=${lossCarMainVo.id}')">车辆定损轨迹</a>
		</c:when>
		<c:otherwise>
		<a class="btn  btn-disabled">车辆定损轨迹</a>
		</c:otherwise>		
		 </c:choose>
			<a class="btn  btn-primary" onclick="imageMovieScan('${lossCarMainVo.registNo}')">信雅达影像查看</a>
		<c:if test="${assessSign eq '1' }">
		<a class="btn btn-primary" href="javascript:;" onclick="assessorView('${lossCarMainVo.registNo}')">公估费查看</a>
		</c:if>
		<a class="btn  btn-primary" onclick="reportPrint('${lossCarMainVo.registNo}')">代抄单打印</a>
		<a class="btn  btn-primary" onclick="cedlinfoshow('${lossCarMainVo.registNo}')">德联易控检测信息</a>
		<c:if test="${registVo.isQuickCase eq '1' && !empty lossCarMainVo.id && lossCarMainVo.deflossCarType eq '1' && existEndCase eq 'N'}">
			<a class="btn btn-primary" onclick="openPhotoVerify('DLoss')">快赔照片审核不通过</a>
		</c:if>
		<a class="btn btn-primary" id="surveyButton" onclick="createSurvey('${lossCarMainVo.registNo}','${taskVo.flowId}','${taskVo.taskId}','${taskVo.nodeCode}','${taskVo.handlerUser}');">调查</a>
		<a class="btn  btn-primary" onclick="scoreInfo('${lossCarMainVo.registNo}')">反欺诈评分</a>
		<c:if test="${policeInfoFlag eq '1'}">
			<a class="btn btn-primary" onclick="policeInfoShow('${lossCarMainVo.registNo}')">预警信息</a>
			<a class="btn  btn-primary" id="comCheckPic" name="comCheckPic" onclick="comCheckPic()">影像比对</a>
		</c:if>
		<a class="btn  btn-primary" onclick="lookLawSuit('${lossCarMainVo.registNo}')">查看高院信息</a>
		<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
		<a class="btn  btn-primary"  onclick="warnView('${lossCarMainVo.registNo}')">山东预警推送</a>
		</div>
		<br/>
		<div class="fixedmargin page_wrap">
			<div id="tab_demo" class="HuiTab">
				<div class="tabBar cl">
					<span>当前定损</span>
					<span>损失信息</span>
				</div>
			
					<!--选项卡一 -->
					<div class="tabCon">
					<form  id="defossform" role="form" method="post"  name="fm" >
						<div id="defloss_inputDiv">
						<!-- 出险车辆信息 -->
						<%@include file="DeflossEdit_CarInfo.jspf" %>
						<%--  <!-- 推送修信息 -->
						<%@include file="DeflossEdit_SendRepair.jspf" %> --%>
							<!-- 定损信息 -->
							<div id="refreshLossInfo">
								<c:choose>
									<c:when test="${lossCarMainVo.deflossCarType eq '1'}" >
										<%@include file="DeflossEdit_Info_Main.jsp" %>
									</c:when>
									<c:otherwise>
										<%@include file="DeflossEdit_Info_Third.jsp" %>
									</c:otherwise>
								</c:choose>
							</div>
						</div> 
						<br/>
						<div id="refreshDiv">
							<%@include file="DeflossEdit_AllLoss.jsp" %>
						</div>
						<!-- 定损意见 -->
							<%@include file="DeflossEdit_Opinion.jspf" %> 
					</form>
						<!-- 意见列表 -->
						<%@include file="../loss-common/DeflossEdit_OpinionList.jspf" %>
				   </div>
				  	
						
					<!-- 选项卡二 -->
					<div class="tabCon">
							<!--车俩损失损失列表-->
							<%@include file="../loss-common/LossCarListInfo.jspf"%>	
							<!-- 财产损失损失列表 -->
							<%@include file="../loss-common/LossPropListInfo.jspf"%>
							<!--人伤损失列表 -->
							<%@include file="../loss-common/LossPersonListInfo.jspf"%>
					</div>
			</div>	
			<div class="text-c">
				<br/>
				<c:if test="${taskVo.handlerStatus !='3' }">
					<input class="btn btn-primary " id="pend" onclick="save(this,'save')" type="submit" value="暂存">
					&nbsp;&nbsp;&nbsp;
					<input class="btn btn-primary " id="save" onclick="save(this,'submitLoss')" type="submit" value="提交">
				</c:if>
			</div>
		</div>
		</div>
	<script type="text/javascript" src="/claimcar/js/deflossEdit/DeflossEdit.js"></script>
	<script type="text/javascript" src="/claimcar/js/deflossEdit/DeflossEditRow.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/jquery-companySide-comparePic.js"></script>
	<script type="text/javascript">
	var sign1=$("#sign").val();
	if(sign1!='1'){
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
	}
	</script>
	</body>
	
</html>