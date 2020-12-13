<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
    <title>理算任务处理</title>
    <style>
		.btn {margin-bottom:5px;}
	</style>
</head>
<body>
    <div class="fixedmargin page_wrap">
    	<!-- 按钮组 -->
      	<%@include file="CompensateEdit_Buttons.jsp" %>
      	<p>
        <form  id="compEditForm" role="form" method="post"  name="fm" >
        	<div class="table_cont">
            <!-- 计算书基本信息 -->
       		<%@include file="CompensateEdit_CompInfo.jsp" %>
       		<!-- 反洗钱信息 -->
       		<%@include file="CompensateEdit_AntiMoney.jsp" %>
       		<!-- 反洗钱可疑交易特征 -->
       		<%@include file="CompensateEdit_AntiContext.jsp" %>
			<c:if test="${flag eq '2' }">
	       		<!-- 设置免赔率 -->
	       		<%@include file="CompensateEdit_Franchise.jsp" %>
       		</c:if>
       		<c:if test="${dutyShowFlag eq 'Y' }">
       		<!-- 车辆责任比例展示-->
       		<%@include file="CompensateEdit_CarCheckDuty.jsp"%>
       		</c:if>
            <!-- 交强预付赔款明细  交强预付费用明细-->
            <%@include file="CompensateEdit_PayTables.jsp" %>
            <!-- 车辆、财产、人伤损失信息 -->
            <div id="bujimian">
            <%@include file="CompensateEdit_LossTables.jsp" %>
            </div>
            <!-- 费用赔款信息  收款人信息 -->
            <%@include file="CompensateEdit_PayInfos.jsp" %>
            <!-- 险别金额汇总表 -->
            <c:if test="${flag eq '2' }">
            <input type="button" class="btn btn-primary mt-5 mb-5" id="KindAmtSum"  value="显示险别金额汇总">
            </c:if>
            <!-- TODO 通过ajax请求向该div append处理好的CompensateEdit_KindAmt.jsp-->
            <div id="CompensateEdit_KindAmt"></div>
            <!-- 险别赔付  理算意见 -->
            <%@include file="CompensateEdit_CompResult.jsp" %>
            </div>
            <div id="hideDiv"></div>
        </form>

	<c:if test="${compWfFlag ne '2' }">
    <div class="text-c">
        <br/>
        <a class="btn btn-primary " id="pend" onclick="saveComp('save')" >暂存</a>
        <a class="btn btn-primary ml-5" id="save" onclick="saveComp('submitLoss')">提交</a>
    </div>
    </c:if>
    <div id="compWfBtnDiv" class="text-c hide">
    	<a class="btn btn-primary " id="writeOff" onclick="submitCompWf()" >冲销</a>
    </div>
    <!-- 案件备注功能隐藏域 -->
    <input type="hidden" id="nodeCode" value="${flowId }">
    <input type="hidden" id="flag" value="${flag }">
    <input type="hidden" id="compWfFlag" value="${compWfFlag }">
    <input type="hidden" id="handlerStatus" value="${ prpLWfTaskVo.handlerStatus }">
    <!-- 理算冲销使用和初始化 -->
    <input type="hidden" id="workStatus" value="${workStatus }">
    <input type="hidden" id="payId" value="${prpLId}">
    <input type="hidden" id="endCaseFlag" value="${endCaseFlag}">
    <!-- 上海保单校验隐藏域 -->
    <input type="hidden" id="comCode" value="${prpLWfTaskVo.comCode}">
    <input type="hidden" id="claimNo" value="${claimNo}">
    <input type="hidden" id="policyNo" value="${policyNo}">
    <input type="hidden" id="compensateNo" value="${prpLCompensate.compensateNo}">
    <input type="hidden"  id="registNo"  value="${prpLWfTaskVo.registNo}" />
    
    <input type="hidden" id="licenseNoSummary" value="${licenseNoSummary }">
	<!-- 代位求偿锁定标志 -->
	<input type="hidden" id="subrogationLock" value="${subrogationLock}"/>	
    <!-- 山东影像对比 -->
	<input type="hidden" id="carRiskUserName" value="${carRiskUserName}"/>
	<input type="hidden" id="carRiskPassWord" value="${carRiskPassWord}"/>
	<input type="hidden" id="claimPeriod" value="${claimPeriod}"/>
	<input type="hidden" id="comparePicURL" value="${comparePicURL}"/>
	<input type="hidden" id="claimSequenceNo" value="${claimSequenceNo}"/>
	<input type="hidden" id="riskcode1" value="${riskCode}"/>
    </div>

	<script type="text/javascript" src="/claimcar/js/compensate/compensateEdit.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/jquery-companySide-comparePic.js"></script>
	<script type="text/javascript">
	
	</script>
	<script type="text/javascript">
	$(function() {
		createRegistRisk();
	
	});
</script>
</body>

</html>