<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>财产核损</title>


<style type="text/css">
.text-r {
	background-color: #F5F5F5
}

.btnClass {
	background-color: gray
}

.tableoverlable label.form_label.col-1 {
	width: 9.3333%;
	padding-right: 0;
}

.tableoverlable .form_input.col-3 {
	width: 23%;
	margin-left: 1%;
}

#subRiskTbody td  .radio-box {
	text-align: center;
}

.feehesun input {
	width: 49%;
	margin-right: 1%;
	float: left
}

.btn {margin-bottom:5px;}

</style>
<script type="text/javascript">
	$(function() {
		//切换选项卡
		$.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current",
				"click", "0");
		//根据后台recycleFlag传递过来的值确定 checkBox是否被选中
		$(":checkBox").each(function(i) {
			var ch = $(":checkBox:eq(" + i + ")").val();
			//1 损余回收 0 否
			if (ch == '0') {//不是损失回收
				$(":checkBox:eq(" + i + ")").attr({
					checked : "false",
					disabled : "disabled"
				});
			} else {//后台传过来的是值是损余回收
				$(":checkBox:eq(" + i + ")").attr({
					checked : "checked",
					disabled : "disabled"
				});
			}
		});
	});

	//计算 财产损失项目总和 
	function calSumVeriLoss() {
		var items = $("#tab_propVerify input[name$='veriLossQuantity']");//确定总共有多少行
		var sum = 0;
		$(items).each(
				function(i) {
					//获取数量  获取单价 获取残值 计算总价
					var sumPrice = 0;
					//数量
					var number = $(
							"input[name='prpLdlossPropMainVo.prpLdlossPropFees["
									+ i + "].veriLossQuantity']").val();
					if (isNaN(number)) {
						number = 0;
					}
					//单价
					var price = $(
							"input[name='prpLdlossPropMainVo.prpLdlossPropFees["
									+ i + "].veriUnitPrice']").val();
					if (isNaN(price)) {
						price = 0.00;
					}
					//获取残值
					var recyclePrice = $(
							"input[name='prpLdlossPropMainVo.prpLdlossPropFees["
									+ i + "].recyclePrice']").val();
					if (isNaN(recyclePrice)) {
						recyclePrice = 0.00;
					}

					sumPrice = number * price - recyclePrice;
					if (sumPrice <= 0) {
						sumPrice = 0.00;
					}
					//设置单元总和
					$(
							"input[name='prpLdlossPropMainVo.prpLdlossPropFees["
									+ i + "].sumVeriLoss']").val(
							parseFloat(sumPrice));
					sum = sum + parseFloat(sumPrice);
				});
		$("#SumVeriLoss").val(sum);
	}
</script>

</head>
<body>
	<div class="top_btn">
	<a class="btn btn-primary" onclick="viewEndorseInfo('${prpLdlossPropMainVo.registNo}')">保单批改记录</a>
	<a class="btn btn-primary" onclick="viewPolicyInfo('${prpLdlossPropMainVo.registNo}')">出险保单</a>
	<a class="btn  btn-primary" onclick="chargemessage('${prpLdlossPropMainVo.registNo}')">费用信息</a>
	<a class="btn  btn-primary" onclick="seeUpdateMessage('${prpLdlossPropMainVo.registNo}')">查看估损更新轨迹</a>	
		 <a class="btn btn-primary" onclick="caseDetails('${lossPropMainVo.registNo}')">历史出险信息</a> <a class="btn btn-danger" href="javascript:;" onclick="createRegistRisk()">风险提示信息</a>
		<a class="btn  btn-primary" onclick="createRegistMessage('${lossPropMainVo.registNo }', '${taskVo.nodeCode}')">案件备注</a>
		<c:choose>
		<c:when test="${taskVo.handlerStatus !='3'}">
		<a class="btn btn-primary" onclick="certifyList('${prpLdlossPropMainVo.registNo}','${taskVo.taskId}')">索赔清单</a> 
		</c:when>
		<c:otherwise>
		<a class="btn  btn-primary" onclick="certifyList('${prpLdlossPropMainVo.registNo}','${taskVo.taskId}','yes')">索赔清单</a>
		</c:otherwise>
		</c:choose>
		
	    <a class="btn btn-primary" onclick="lawsuit('${lossPropMainVo.registNo }','${taskVo.nodeCode}')">诉讼</a>
		<a class="btn  btn-primary" onclick="viewBigOpinion('${lossPropMainVo.registNo }','${claimText.nodeCode}')">大案预报</a> 
		<a class="btn  btn-primary" onclick="payCustomSearch('N')">收款人账户信息维护</a>
		<%-- <a class="btn btn-primary" id="logOut" onclick="claimCancel('${prpLdlossPropMainVo.registNo}','${taskVo.taskId}')">注销/拒赔</a>
		<a class="btn btn-primary" id="logOut" onclick="claimCancelRecover('${prpLdlossPropMainVo.registNo}','${taskVo.taskId}')">注销/拒赔恢复</a> --%>
	    <a class="btn  btn-primary" onclick="checkSeeMessage('${prpLdlossPropMainVo.registNo}')">查勘详细信息</a>
	    <a class="btn  btn-primary" onclick="imageMovieUpload('${taskVo.taskId}')">信雅达影像上传</a>
	    <input type="hidden" id="flowTaskId" value="${taskVo.taskId}" disabled="disabled">
	    <a class="btn  btn-primary" onclick="imageMovieScan('${lossPropMainVo.registNo}')">信雅达影像查看</a>
	    <a class="btn btn-primary" onclick="showFlow('${taskVo.flowId}')">流程查询</a>
	    <a class="btn  btn-primary" onclick="lookLawSuit('${prpLdlossPropMainVo.registNo}')">查看高院信息</a>
	    <a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
	    <a class="btn  btn-primary"  onclick="warnView('${prpLdlossPropMainVo.registNo}')">山东预警推送</a>
	    <c:choose>
	    <c:when test="${lossCarSign eq '1'}">
			<a class="btn  btn-primary" onclick="verifyLossCertifyPrintJump('${prpLdlossPropMainVo.registNo}')">核损清单打印</a>
			</c:when>
			<c:otherwise>
			  <a class="btn  btn-disabled">核损清单打印</a>
			</c:otherwise>
			</c:choose>
			
			<c:choose>
			<c:when test="${compensateSign eq '1'}">
			<a class="btn btn-primary" onclick="AdjustersPrintJump('${lossPropMainVo.registNo}')">赔款理算书打印</a>
			</c:when>
			<c:otherwise>
			<a class="btn  btn-disabled" >赔款理算书打印</a>
			</c:otherwise>
	        </c:choose>
	        <c:choose>
			<c:when test="${compensateSign eq '1'}">
			<a class="btn  btn-primary"  onclick="certifyPrintPayFeeJump('${lossPropMainVo.registNo}')">赔款收据打印</a>
			</c:when>
			<c:otherwise>
			<a class="btn  btn-disabled" >赔款收据打印</a>
			</c:otherwise>
			</c:choose>
			<c:if test="${assessSign eq '1' }">
		    <a class="btn btn-primary" href="javascript:;" onclick="assessorView('${lossPropMainVo.registNo}')">公估费查看</a>
		    </c:if>
			<a class="btn  btn-primary" onclick="reportPrint('${lossPropMainVo.registNo}')">代抄单打印</a>
			<c:if test="${roleFlag eq '1' }">		
				<a class="btn btn-primary"   onclick="ruleView('${taskVo.registNo}','${lossCarMainVo.riskCode}','${ruleNodeCode}','${taskVo.itemName}','${taskVo.upperTaskId}')">ILOG规则信息查看</a>
			</c:if>	
		<a class="btn btn-primary" id="surveyButton" onclick="createSurvey('${lossPropMainVo.registNo}','${taskVo.flowId}','${taskVo.taskId}','${taskVo.nodeCode}','${taskVo.handlerUser}');">调查</a>		
	</div>
	<br/>
	<div class="fixedmargin page_wrap">
		<div id="tab_demo" class="HuiTab">
			<div class="tabBar cl">
				<span>当前定损</span><span>损失信息</span>
			</div>
			<!--选项卡一 -->
			<div class="tabCon">
				<form action="" method="post" id="verifyform" role="form">
					<!-- 基本信息 -->
					<%@include file="PropVerify_BaseInfo.jspf"%>
					<!-- 受损财产信息 -->
					<%@include file="PropVerify_Item.jspf"%>
					<!-- 费用赔款信息 -->
					<%@include file="PropVerify_Fee.jspf"%>
					<%@include file="PropVerify_Other.jspf"%>
			</div>

			<!-- 选项卡二 -->
			<div class="tabCon">
				<!--车俩损失损失列表-->
				<%@include file="../../propLoss/lossInfoView/carLossListInfo.jspf"%>
				<!-- 财产损失损失列表 -->
				<%@include file="../../propLoss/lossInfoView/propLossListInfo.jspf"%>
				<!--人伤损失列表 -->
				<%@include file="../../propLoss/lossInfoView/personLossListInfo.jspf"%>
			</div>
		</div>
		<c:if test="${lossPropMainVo.handlerStatus!='3' }">
			<div class="text-c" id="buttonDiv">
				<br /> 
				<input class="btn btn-primary " id="save" onclick="save1('save')" type="button" value="暂存">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
				<input class="btn btn-primary " id="submit" onclick="save1('submitVloss')" type="button" value="审核通过"> 
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
				<input class="btn btn-primary " id="audit" onclick="save1('audit')" type="button" value="提交上级">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input class="btn btn-primary " id="backLower" onclick="save1('backLower')" type="button" value="退回下级">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input class="btn btn-primary " id="backLoss" onclick="save1('backLoss')" type="button" value="退回定损">
			</div>
		</c:if>
		</form>
	</div>
	</div>
	<script type="text/javascript"
		src="/claimcar/js/propVerify/PropVerify.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript">
	$(function() {
		createRegistRisk();
		getGBStatus($("#flowTaskId").val());
	});
</script>
</body>

</html>