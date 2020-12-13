<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>垫付任务申请登记</title>
<style>
	.btn {margin-bottom:5px;}
</style>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<!-- 按钮组 -->
		<div class="top_btn">
			<div class="top_btn">
				<a class="btn btn-primary"
				   onclick="viewEndorseInfo('${claimVo.registNo}')">保单批改记录</a>
				<a class="btn btn-primary"
					onclick="viewPolicyInfo('${claimVo.registNo}')">保单详细信息</a>
					<a class="btn  btn-primary" onclick="openTaskEditWin('报案详细信息','/claimcar/regist/edit.do?registNo=${claimVo.registNo}')">报案详细信息</a>
					<a class="btn  btn-primary" onclick="checkSeeMessage('${cMainVo.registNo}')">查勘详细信息</a> <a
					class="btn btn-primary" onclick="loss('${cMainVo.registNo}')">定损详细信息</a>
				<a class="btn  btn-primary" onclick="Searchpeople('${cMainVo.registNo}')">人伤跟踪信息</a>
		<c:choose>
			<c:when test="${wfTaskVo.handlerStatus =='2' || wfTaskVo.handlerStatus == '0'}">
				<a class="btn  btn-primary" onclick="padPayCancel('${wfTaskVo.taskId}')">垫付注销</a>
			</c:when>
			<c:otherwise>
				<a class="btn  btn-disabled">垫付注销 </a>
			</c:otherwise>
		</c:choose>
				<c:choose>
					<c:when test="${empty padPayMainVo.compensateNo}">
						<a class="btn btn-disabled">历次审核意见</a>
					</c:when>
					<c:otherwise>
						<a class="btn btn-primary"
							onclick="historyMessage('${wfTaskVo.registNo}','${padPayMainVo.compensateNo}')">历次审核意见</a>
				</c:otherwise>
			</c:choose>  
				
				<a class="btn  btn-primary" onclick="imageMovieUpload('${wfTaskVo.taskId}')">信雅达影像上传</a>
				<a class="btn btn-primary" id="lawsuit" onclick="lawsuit('${claimVo.registNo}', '${wfTaskVo.nodeCode }')">诉讼</a>
				<a class="btn  btn-primary" onclick="seeUpdateMessage('${cMainVo.registNo}')">查看估损更新轨迹</a> <a class="btn btn-primary" onclick="createRegistMessage('${claimVo.registNo}','PadPay');">案件备注</a>
				<a class="btn btn-primary" onclick="payCustomSearch('N')">收款人信息维护</a>
				<c:if test="${handlerStatus eq '2' || handlerStatus eq '3' }">
					<a class="btn  btn-primary" onclick="AdjustersPrintPrePad('${padPayMainVo.registNo }','${padPayMainVo.compensateNo }')">打印垫付申请书</a>
				</c:if>
				<c:if test="${handlerStatus ne '2' && handlerStatus ne '3' }">
					<a class="btn  btn-disabled" >打印垫付申请书</a>
				</c:if>
				<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
			</div>
			<br />
		</div>
		<br />
		<p>
		
		<form id="padPay_form" role="form" method="post" name="fm">
			<div class="table_cont">
			<input type="hidden" name="flowTaskId" id="flowTaskId" value="${wfTaskVo.taskId}">
			<input type="hidden" id="nodeCode" value="${wfTaskVo.subNodeCode}">
			<input type="hidden" id="status" value="${wfTaskVo.handlerStatus}">
			<input type="hidden" id="workStatus" value="${wfTaskVo.workStatus}">
		    <input type="hidden" value="${wfTaskVo.flowId}">
			<input type="hidden" id="claimNo" value="${claimVo.claimNo}">
			<input type="hidden" id="registNo" value="${claimVo.registNo}">
			<input type="hidden" id="policyNo" value="${padPayMainVo.policyNo}">
			<input type="hidden" id="padLimitAmount" value="${padLimitAmount}">

			<input type="hidden" id="flowIDX" value="0">
			
			<!-- 基本信息 -->
			<%@include file="PadPayEdit_BasicInfo.jspf"%>
			<!-- 反洗钱信息 -->
       		<%@include file="CompensateEdit_AntiMoney.jsp" %>

			<div class="table_wrap" id="paymentVo">
				<div class="table_title f14">人员损失信息 </div>
				<div class="table_cont" id="person_table">
					<input type="hidden" id="personVo_size" value="${fn:length(padPayPersonVos)}">
					<c:set var="personVoSize" value="${fn:length(padPayPersonVos)}" />
                    
			<button type="button" class="btn btn-plus Hui-iconfont Hui-iconfont-add" onclick="addPadPayTr('${cMainVo.registNo}','${wfTaskVo.handlerStatus}')"></button>
						
					<!--人员损失信息-->
					<%@include file="PadPayEdit_PersonTr.jsp"%>
				</div>
			</div>

				<div class="table_wrap">
					<div class="table_title f14">
						备注信息<font class="must">*</font>
					</div>
					<div class="formtable">
						<div class="col-12">
							<textarea class="textarea h100" id="contexts" maxlength="250"
								name="padPayMainVo.remark" datatype="*1-250" nullmsg="请输入备注信息！"
								placeholder="请输入...">${padPayMainVo.remark}</textarea>
						</div>
					</div>
				</div>

				<div class="table_wrap">
					<div class="table_title f14">预支付抢救费报告</div>
					<div class="formtable">
						<div class="col-12">
							<textarea class="textarea h100" id="contexts" maxlength="250"name="padPayMainVo.rescueReport" 
							datatype="*1-250">${padPayMainVo.rescueReport}</textarea>
						</div>
					</div>
				</div>
			</div>
		</form>
		<br/><br/><br/>
		<div class="text-c mt-10" >
			<input class="btn btn-primary ml-5" onclick="savePadPay('save')" type="submit" value="暂存">
			<c:choose>
				<c:when test="${empty wfTaskVo.taskId}">
					<input class="btn btn-primary ml-5" id="dianfuId" onclick="savePadPay('advPadPay')" type="submit" value="垫付申请提交">
				</c:when>
				<c:otherwise>
					<input class="btn btn-primary ml-5" onclick="savePadPay('submitPadPay')" type="submit" value="垫付处理提交">
				</c:otherwise>
			</c:choose>
			<!-- <input class="btn btn-primary ml-5" id="" onclick="cancelPadPay()" type="button" value="返回"> -->
		</div>
		<br/><br/>
		<!-- 案件备注功能隐藏域 -->
		<input type="hidden" id="nodeCode" value="${wfTaskVo.subNodeCode}"> <input
			type="hidden" id="registNo" value="${param.registNo}"> <input
			type="hidden" id="flag" value="${flag }">
	</div>

	<script type="text/javascript" src="/claimcar/js/padPay/padPay.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript">
	$(function() {
		$.Huitab("#tab-system .tabBarspan", "#tab-system .tabCon",
				"current", "click", "0");
	});
	</script>
</body>

</html>