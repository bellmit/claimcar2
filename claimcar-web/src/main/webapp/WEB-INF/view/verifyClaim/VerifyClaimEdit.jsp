<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
    <title>核赔任务处理</title>
</head>
<body>
    	<!-- 按钮组 -->
      <%@include file="VerifyClaimEdit_Buttons.jspf" %>
      <br/>
      <div class="fixedmargin page_wrap">
      	<div class="table_cont">
      
      	<input type="hidden" id="hh" value="${hh}">
      	
		<form id="vclaimform" role="form" method="post" name="fm">
			<input type="hidden" name="taskType" value="${policyType}">
			<input type="hidden" name="wfTaskVo.handlerStatus" value="${wfTaskVo.handlerStatus}">
			<input type="hidden" name="wfTaskVo.taskId" value="${wfTaskVo.taskId}">
			<input type="hidden" name="wfTaskVo.nodeCode" value="${wfTaskVo.nodeCode}">
			<input type="hidden" name="wfTaskVo.subNodeCode" value="${wfTaskVo.subNodeCode}">
			<input type="hidden" name="wfTaskVo.handlerStatus" value="${wfTaskVo.handlerStatus}">
			<input type="hidden" name="wfTaskVo.flowId" value="${wfTaskVo.flowId}">
			<input type="hidden" name="wfTaskVo.registNo" value="${wfTaskVo.registNo}">
			<input type="hidden" name="wfTaskVo.taskInNode" value="${wfTaskVo.taskInNode}">
			<input type="hidden"  name="prplcomContextVo.id" value="${prplcomContextVo.id}"/>
			<input type="hidden"  id="fxqSignShow" value="${fxqSignShow}"/>

			<!--  -->
			<input type="hidden" name="uwNotionMainVo.id" value="${uwNotionMainVo.id}">
			<input type="hidden" name="uwNotionMainVo.taskId" value="${wfTaskVo.taskId}">
			<input type="hidden" name="uwNotionMainVo.compensateNo" value="${wfTaskVo.handlerIdKey}">
			<input type="hidden" name="prplcomContextVo.compensateNo" value="${wfTaskVo.handlerIdKey}"/>
			<input type="hidden" name="uwNotionMainVo.claimNo" id="claimNo" value="${wfTaskVo.claimNo}">
			<input type="hidden" name="uwNotionMainVo.policyType" value="${policyType}">
			<input type="hidden" name="uwNotionMainVo.policyNo" value="${policyInfo.policyNo}">
			<input type="hidden" name="uwNotionMainVo.registNo" value="${wfTaskVo.registNo}">
			<input type="hidden" name="uwNotionMainVo.handle" value="${uwNotionMainVo.handle}">
			<input type="hidden" name="uwNotionMainVo.taskInNode" value="${uwNotionMainVo.taskInNode}">
			<input type="hidden" name="remark" value="${uwNotionMainVo.remark}">
			<input type="hidden" id="riskCode" value="${policyInfo.riskCode}">
			<input type="hidden" name="uwNotionVo.handle" value="">
			<input type="hidden" name="uwNotionVo.amount" value="${ve_amount}">
			<input type="hidden" id="registNo" value="${wfTaskVo.registNo}">
			<input type="hidden" name="wfTaskVo.ywTaskType" value="${wfTaskVo.ywTaskType}">
			<input type="hidden" name="amount" value="1111">
            <input type="hidden" name="payIds" id="payIds" value="">
            <input type="hidden" id="isRefuse" value="${isRefuse }">
            <input type="hidden" value="${pisderoAmout}" id="pisderoAmout"/>
			<input type="hidden" value="${cisderoAmout}" id="cisderoAmout"/>
			<input type="hidden" value="${verifyClaimCompleteFlag}" id="verifyClaimCompleteFlag"/>
		    <!-- 山东影像对比 -->
			<input type="hidden" id="carRiskUserName" value="${carRiskUserName}"/>
			<input type="hidden" id="carRiskPassWord" value="${carRiskPassWord}"/>
			<input type="hidden" id="claimPeriod" value="${claimPeriod}"/>
			<input type="hidden" id="comparePicURL" value="${comparePicURL}"/>
			<input type="hidden" id="claimSequenceNo" value="${claimSequenceNo}"/> 
		    <!-- 山东影像对比 -->
			<input type="hidden" id="carRiskUserName" value="${carRiskUserName}"/>
			<input type="hidden" id="carRiskPassWord" value="${carRiskPassWord}"/>
			<input type="hidden" id="claimPeriod" value="${claimPeriod}"/>
			<input type="hidden" id="comparePicURL" value="${comparePicURL}"/>
			<input type="hidden" id="claimSequenceNo" value="${claimSequenceNo}"/>
			<!-- 核赔基本信息 -->
			<%@include file="VerifyClaimEdit_PolicyInfo.jspf"%>
			
			<!-- 出险信息 -->
			<%@include file="VerifyClaimEdit_DamageInfo.jspf"%>
			
			
            
			<!-- 计算书基本信息 -->
			<c:if test="${(policyType eq '1')||(policyType eq '2')}">
				<%@include file="VerifyClaimEdit_BasicInfo.jspf"%>
			</c:if>

			<!-- 设置免赔率(商业实际赔或冲销) -->
			<c:if test="${policyType eq '2'}">
				<%@include file="VerifyClaimEdit_Franchise.jspf"%>
			</c:if>

			<!-- 商业预付赔款、预付费用明细 -->
			<!-- 交强预付赔款、预付费用明细 -->
			<c:if test="${(policyType eq '1')||(policyType eq '2')}">
				<%@include file="VerifyClaimEdit_PrePayP.jspf"%>
				<%@include file="VerifyClaimEdit_PrePayF.jspf"%>
				<%@include file="VerifyClaimEdit_PadPayInfo.jspf"%>
			</c:if>

			<!-- 车辆、财产、人伤损失信息 (交强、商业的  -- > 实赔、冲销 ) -->
			<c:if test="${(policyType eq '1')||(policyType eq '2')}">
				<%@include file="VerifyClaimEdit_LossTables.jspf"%>
				<!-- 费用赔款信息  收款人信息 -->
				<%@include file="VerifyClaimEdit_PayInfos.jspf"%>
			</c:if>

			<!-- 垫付，，基本信息 -->
			<c:if test="${policyType eq '5'}">
				<%@include file="VerifyClaimEdit_PadPayBasicInfo.jspf"%>
				<%@include file="VerifyClaimEdit_PadPayPerson.jspf"%>
			</c:if>

			<!-- 险别赔付  核赔意见 -->
			<c:if test="${(policyType eq '1')||(policyType eq '2')}">
				<%@include file="VerifyClaimEdit_VerifyResult.jspf"%>
			</c:if>

			<!-- 3-交强预付、冲销核赔，4-商业预付、冲销核赔 -->
			<c:if test="${(policyType eq '3')||(policyType eq '4')}">
				<%@include file="VerifyClaimEdit_PrePayIndemnity.jspf"%>
				<%@include file="VerifyClaimEdit_PrePayFee.jspf"%>
				<div class="table_wrap">
					<div class="table_title f14">预支付抢救费报告</div>
					<div class="formtable">
						<div class="col-12">
							<textarea class="textarea h100" id="contexts" maxlength="250" name="compensateVo.rescueReport" 
							 readonly="readonly"> ${compeVo.rescueReport}</textarea>
						</div>
					</div>
				</div>
			</c:if>
            <br>
  <c:if test="${taskType eq '1' || taskType eq '2'}">
	<div class="table_wrap">
	<div class="table_title f14">${nodeTextTitle}<font class="must">*</font></div>
	<div class="formtable">
	<textarea class="textarea" style="height: 100px; margin-top: 6px"
					name="nodeText"  value="" id="nodeText" 
					readonly="readonly">${nodeText}</textarea>
				
	</div>
</div>
<br/>
</c:if>
           <!-- 反洗钱可疑交易特征 -->
           <c:if test="${fxqSignShow eq '1' }">
            <%@include file="VerifyClaimEdit_AntiContext.jsp"%>
            </c:if>
             <!-- 反洗钱风险信息 -->
           <c:if test="${fxqSignShow eq '1' }">
            <%@include file="VerifyClaimEdit_AntiSafetInfo.jsp"%>
            </c:if>
         <!-- 注销、拒赔原因 -->
			<c:if test="${taskType eq '6'}">
		<div class="table_wrap">
	          <div class="table_title f14">注销/拒赔原因</div>
	          <div class="formtable">
		      <div class="row cl mb-5">
			  <label class="form_label col-1">任务类型</label>
			  <div class="form_input col-2">
				<!-- <input type="text" class="input-text" placeholder="任务类型自定义标签" /> -->
				<app:codeSelect codeType="DealReason" type="select" name="dealReasoon" id="dealReasoon" value="${prpLcancelTraceVo.dealReasoon }" disabled="true"/>
				
				<!-- <font class="must">*</font> -->
				<c:if test="${(Status=='0') && (tuiHui != '1') }">
					<input type="hidden" name="applyReason" value="${prpLcancelTraceVo.applyReason }" >
					<input type="hidden" name="dealReasoon" value="${prpLcancelTraceVo.dealReasoon }" >
					<input type="hidden" name="remarks" value="${prpLcancelTraceVo.remarks }" >
					<input type="hidden" name="swindleSum" value="${prpLcancelTraceVo.swindleSum }" >
					<input type="hidden" name="swindleType" value="${prpLcancelTraceVo.swindleType }" >
					<input type="hidden" name="swindleReason" value="${prpLcancelTraceVo.swindleReason }" >
				</c:if>
			</div>
			<label class="form_label col-1">原因</label>
			<div class="form_input col-2">
				<!-- <input type="text" class="input-text" placeholder="原因自定义标签" /> -->
				<app:codeSelect codeType="ApplyReason" type="select" id="applyReason" name="applyReason" value="${prpLcancelTraceVo.applyReason }" disabled="true"/>
				
				<!-- <font class="must">*</font> -->
			</div>
			<c:if test="${tuiHui eq '1'}">
			<input type="hidden" name="dealReasoon" value="${prpLcancelTraceVo.dealReasoon }" readonly="readonly">
			</c:if>
			
			<label class="form_label col-1">备注</label>
			<div class="form_input col-4">
				 <input type="text" class="input-text" name="remarks" value="${prpLcancelTraceVo.remarks }" id="remarks" readonly="readonly"/> 
			</div>
			
	  </div>
	  <c:if test="${prpLcancelTraceVo.dealReasoon eq '3' || prpLcancelTraceVo.dealReasoon eq '4'}">
	 <div class="row cl mb-5">
			
			<label class="form_label col-1">欺诈标志</label>
			<div class="form_input col-2">
				<!-- <input type="text" class="input-text" placeholder="原因自定义标签" /> -->
				<app:codeSelect codeType="SwindleReason" id="swindleReason" type="select" name="swindleReason" value="${prpLcancelTraceVo.swindleReason }" disabled="true"/>
		
			</div>
			<label class="form_label col-1">欺诈类型</label>
			<div class="form_input col-2">
				<!-- <input type="text" class="input-text" placeholder="原因自定义标签" /> -->
				<app:codeSelect codeType="SwindleType" id="swindleType" type="select" name="swindleType" value="${prpLcancelTraceVo.swindleType }" disabled="true"/>
				
			</div>
			<label class="form_label col-2">欺诈挽回损失金额</label>
			<div class="form_input col-2">
				<input type="text" class="input-text" id="swindleSum" placeholder="欺诈挽回损失金额" name="swindleSum" value="${prpLcancelTraceVo.swindleSum }" readonly="readonly"/> 
				<%-- <app:codeSelect codeType="DealReason" type="select" name="swindleSum" id="dealReasoon" value="${prpLcancelTraceVo.dealReasoon }"/>
				 --%>
			</div>
			</div>
			</c:if>
	  
      </div>
    </div>
    
      </c:if>

            
			
			<!-- 审批意见 -->
			<%@include file="VerifyClaimEdit_VerifyAdvice.jspf"%>

		</form>
		</div>
		<div class="text-c">
        <br/><br/>
        <!-- viewSubmit('return') -->
        <input class="btn btn-primary" type="button" id="vc_return" onclick="save('vc_return')" value="退回">
        <!-- viewSubmit('audit') -->
        <input class="btn btn-primary ml-5" type="button" id="vc_audit" onclick="save('vc_audit')" value="提交上级">
    	<input class="btn btn-primary ml-5" type="button" id="vc_adopt" onclick="save('vc_adopt')" value="核赔通过">
    	<input type="hidden" name="saveType" value="">
    </div><br/>
    <!-- 案件备注功能隐藏域 -->
    <input type="hidden" id="nodeCode" value="VClaim">
    <input type="hidden" id="registNo" value="${param.registNo}">
    <input type="hidden" id="flag" value="${flag}">
    </div>

	<script type="text/javascript" src="/claimcar/js/verifyclaim/verifyclaim.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/jquery-companySide-comparePic.js"></script>
	<script type="text/javascript">
	
	

	</script>
</body>

</html>