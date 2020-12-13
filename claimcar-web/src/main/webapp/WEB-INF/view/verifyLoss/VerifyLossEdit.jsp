<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>核损处理</title>
		
		<script type="text/javascript">
		$(function(){
			$.Huitab("#tab_demo .tabBar span","#tab_demo .tabCon","current","click","0");
			
			});
		</script>
		<style type="text/css">
			.text-r{
				background-color: #F5F5F5
			}
			.select-acc select{
			  height: 28px;
			  line-height: 28px;
			  margin-top: 4px;
			  border-color: #666;
			}
			.part-changes{position: relative;}
			.Ispart-changes{position: absolute;top: 5px;left:10px;}
			.marginR0 span.radio-box{margin-right:0;min-width: 50px;}
			
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
			<a class="btn  btn-primary" onclick="viewEndorseInfo('${lossCarMainVo.registNo }')">保单批改记录</a>
			<a class="btn  btn-primary" onclick="viewPolicyInfo('${lossCarMainVo.registNo }')">出险保单</a>
			<a class="btn  btn-primary" onclick="imageMovieUpload('${taskVo.taskId}')">信雅达影像上传</a>
			<input type="hidden" id="flowTaskId" value="${taskVo.taskId}" disabled="disabled">
			<a class="btn btn-primary" onclick="NuclearInventoryLoss('${lossCarMainVo.id}','${lossCarMainVo.registNo}')">单证打印</a>
			<a class="btn btn-primary" onclick="createRegistMessage('${lossCarMainVo.registNo }','${taskVo.subNodeCode}')">案件备注</a>
		    <a class="btn  btn-primary" onclick="seeUpdateMessage('${lossCarMainVo.registNo}')">查看估损更新轨迹</a>
			<a class="btn  btn-primary" onclick="chargemessage('${lossCarMainVo.registNo}')">费用信息</a>
			<a class="btn btn-primary" onclick="showFlow('${taskVo.flowId}')">流程查询</a>
			<a class="btn btn-primary" onclick="caseDetails('${lossCarMainVo.registNo}')">历史出险信息</a>
			<a class="btn  btn-danger" onclick="viewBigOpinion('${lossCarMainVo.registNo}', '${taskVo.subNodeCode}')">大案预报</a>
			<a class="btn  btn-primary" onclick="checkSeeMessage('${lossCarMainVo.registNo}')">查勘详细信息</a>
			<c:if test="${assessSign eq '1' }">
		    <a class="btn btn-primary" href="javascript:;" onclick="assessorView('${lossCarMainVo.registNo}')">公估费查看</a>
		    </c:if>
		    <a class="btn  btn-primary" onclick="lawsuit('${lossCarMainVo.registNo }' ,'${taskVo.subNodeCode}')">诉讼</a>
	        <a class="btn  btn-primary" target="_blank" onclick="openTaskEditWin('定损轨迹','/claimcar/defloss/deflossHisView.do?defLossMainId=${lossCarMainVo.id}')">车辆定损轨迹</a>
			<a class="btn  btn-primary" onclick="payCustomSearch('N')">收款人账户信息维护</a>
			<a class="btn  btn-primary" onclick="imageMovieScan('${lossCarMainVo.registNo}')">信雅达影像查看</a>
			<c:choose>
			<c:when test="${taskVo.handlerStatus eq '3' }">
			<a class="btn  btn-primary" onclick="verifyLossCertifyPrintJump('${lossCarMainVo.registNo}')">核损清单打印</a>
			</c:when>
			<c:otherwise>
			  <a class="btn  btn-disabled">核损清单打印</a>
			</c:otherwise>
			</c:choose>
			<c:choose>
			<c:when test="${compensateSign eq '1'}">
			<a class="btn btn-primary" onclick="AdjustersPrintJump('${lossCarMainVo.registNo}')">赔款理算书打印</a>
			</c:when>
			<c:otherwise>
			<a class="btn  btn-disabled" >赔款理算书打印</a>
			</c:otherwise>
	        </c:choose>
	        <c:choose>
			<c:when test="${compensateSign eq '1'}">
			<a class="btn  btn-primary"  onclick="certifyPrintPayFeeJump('${lossCarMainVo.registNo}')">赔款收据打印</a>
			</c:when>
			<c:otherwise>
			<a class="btn  btn-disabled" >赔款收据打印</a>
			</c:otherwise>
			</c:choose>
			<a class="btn btn-danger" href="javascript:;" onclick="createRegistRisk()">风险提示信息</a>
	        <a class="btn  btn-primary" onclick="certifyList('${lossCarMainVo.registNo}','${taskVo.taskId}')">索赔清单</a>
	        <a class="btn  btn-primary" onclick="reportPrint('${lossCarMainVo.registNo}')">代抄单打印</a>
	        <a class="btn  btn-primary" onclick="cedlinfoshow('${lossCarMainVo.registNo}')">德联易控检测信息</a>
	        <c:if test="${registVo.isQuickCase eq '1' && !empty lossCarMainVo.id && lossCarMainVo.deflossCarType eq '1' && existEndCase eq 'N'}">
				<a class="btn btn-primary" onclick="openPhotoVerify('VLoss')">快赔照片审核不通过</a>
			</c:if>
			<c:if test="${policeInfoFlag eq '1'}">
				<a class="btn btn-primary" onclick="policeInfoShow('${lossCarMainVo.registNo}')">预警信息</a>
				<a class="btn  btn-primary" id="comCheckPic" name="comCheckPic" onclick="comCheckPic()">影像比对</a>
			</c:if>
			<c:if test="${roleFlag eq '1' }">		
				<a class="btn btn-primary"  onclick="ruleView('${lossCarMainVo.registNo}','${lossCarMainVo.riskCode}','${ruleNodeCode}','${lossCarMainVo.licenseNo}','${taskVo.upperTaskId}')">ILOG规则信息查看</a>
			</c:if>
			<a class="btn btn-primary" id="surveyButton" onclick="createSurvey('${lossCarMainVo.registNo}','${taskVo.flowId}','${taskVo.taskId}','${taskVo.nodeCode}','${taskVo.handlerUser}');">调查</a>
			<a class="btn  btn-primary" onclick="lookLawSuit('${lossCarMainVo.registNo}')">查看高院信息</a>
			<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
			<a class="btn  btn-primary"  onclick="warnView('${lossCarMainVo.registNo}')">山东预警推送</a>
		</div>
		<br/>
		<br/>
		<br/>
		<div class="fixedmargin page_wrap">
			<div id="tab_demo" class="HuiTab">
				<div class="tabBar cl">
					<span>当前定损</span>
					<span>损失信息</span>
				</div>
			
					<!--选项卡一 -->
					<div class="tabCon">
					<form  id="verifyform" role="form" method="post"  name="fm" >
						<!-- 车辆信息 -->
						<%@include file="VerifyLoss_CarInfo.jspf" %>
						<!-- 定损信息 -->
						<%@include file="VerifyLoss_Info.jspf" %>
						
						 <!--代为求偿  -->
						<c:if test="${lossCarMainVo.deflossCarType eq '1'}">
							<%@include file="VerifyLoss_Subrogation.jspf" %>
						</c:if> 
						<br/>
						
						<div id="refreshDiv">
							<%@include file="VerifyLoss_allLoss.jsp" %>	
						</div>
						<!-- 定损意见 -->
						<%@include file="VerifyLoss_Opinion.jspf" %>  
								
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
			<div class="text-c" id="buttonDiv">
				<br/>
				<c:if test="${taskVo.handlerStatus !='3' }">
					<input class="btn btn-primary " id="save" onclick="save(this,'save')" type="submit" value="暂存">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input class="btn btn-primary btn-agree" id="submit" onclick="save(this,'submitVloss')" type="submit" value="审核通过">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<c:if test="${commonVo.currencyLevel < 10}"><!-- 最高级不显示 提交上级 -->
						<input class="btn btn-primary btn-agree" id="audit" onclick="save(this,'audit')" type="submit" value="提交上级">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:if>
					<c:if test="${commonVo.lowerButton}">
						<input class="btn btn-primary btn-noagree" id="backLower" onclick="save(this,'backLower')" type="submit" value="退回下级">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:if>	
					<c:if test="${commonVo.currencyLevel < 9}"><!-- 分公司才显示 退回定损 -->
						<input class="btn btn-primary btn-noagree" id="backLoss" onclick="save(this,'backLoss')" type="submit" value="退回定损">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:if>
					<c:if test="${commonVo.reLossFlag!='2' }"> 
						<input class="btn btn-primary" id="reLoss" onclick="save(this,'toReLoss')" type="submit" value="复检">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:if>
					<c:if test="${commonVo.recheckExist eq false }"> 
						<input class="btn btn-primary btn-noagree" id="recheck" onclick="save(this,'toRecheck')" type="submit" value="复勘">
					</c:if>
				</c:if>
			</div>
		</div>
		</div>
	<script type="text/javascript" src="/claimcar/js/verifyLoss/VerifyLoss.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript" src="${ctx}/js/persTraceEdit/PersTraceEdit.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/jquery-companySide-comparePic.js"></script>
	
	<script type="text/javascript">
	$(function() {
		createRegistRisk();
		getGBStatus($("#flowTaskId").val());
	});
</script>
	</body>

</html>