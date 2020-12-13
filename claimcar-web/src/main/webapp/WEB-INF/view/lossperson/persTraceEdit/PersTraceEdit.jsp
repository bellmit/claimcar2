<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>人伤跟踪</title>
<script type="text/javascript">
	$(function() {
		$.Huitab("#persInfoTab .tabBar span", "#persInfoTab .tabCon", "current", "click", "0");
		layer.config({
			extend : 'extend/layer.ext.js'
		});
	});
</script>
<style type="text/css">
.text-r {
	background-color: #F5F5F5
}

.give-space {
	width: 96%;
}

.multiplication {
	background-color: #efefef;
	border-color: #000000;
	border-style: solid;
	border-top-width: 0px;
	border-right-width: 0px;
	border-bottom-width: 1px;
	border-left-width: 0px;
}

.tips {
	font-size: 14px;
	padding: 0;
	margin: 0;
	text-align: center;
	color: #f00;
	margin-bottom: 20px;
}

.tabBar span.current{
	color: #ff0000;
}

.btn {margin-bottom:5px;}
</style>
</head>
<body>
	<div class="top_btn">
		<a class="btn btn-primary" onclick="viewEndorseInfo('${prpLCheckDutyVo.registNo}')">保单批改记录</a>
		<a class="btn btn-primary" onclick="viewPolicyInfo('${prpLCheckDutyVo.registNo}')">出险保单</a>
		<a class="btn btn-primary" onclick="caseDetails('${prpLCheckDutyVo.registNo}')">历史出险信息</a>
	    <a class="btn btn-danger" href="javascript:;" onclick="createRegistRisk()">风险提示信息</a>
		<a class="btn  btn-primary" onclick="createRegistMessage('${prpLCheckDutyVo.registNo }', '${flowNodeCode }')">案件备注</a>
		<a class="btn  btn-danger" onclick="viewBigOpinion('${prpLCheckDutyVo.registNo }', '${flowNodeCode }')">大案上报</a> 
	    <a class="btn  btn-primary" onclick="Findpeople('${prpLDlossPersTraceMainVo.id}')">费用修改记录</a>
		<a class="btn  btn-primary" onclick="imageMovieScan('${prpLCheckDutyVo.registNo}')">信雅达影像查看</a>
		<c:choose>
			<c:when test="${taskVo.handlerStatus != '3'}">
				<a class="btn btn-disabled">单证打印</a>
			</c:when>
			<c:otherwise>
				<a class="btn  btn-primary" onclick="peoplesPrint('${prpLCheckDutyVo.registNo}')">单证打印</a>
			</c:otherwise>
		</c:choose>
		<a class="btn  btn-primary" onclick="imageMovieUpload('${taskVo.taskId}')">信雅达影像上传</a>
		<input type="hidden" id="flowTaskId" value="${taskVo.taskId}" disabled="disabled">
		<a class="btn btn-primary" onclick="lawsuit('${prpLCheckDutyVo.registNo }','${flowNodeCode}')">诉讼</a>
	    <input type="hidden" name="prpLCheckDutyVo.registNo" value="${prpLCheckDutyVo.registNo }">
	    <input type="hidden" id="flowNodeCode" value="${flowNodeCode}">
	    <a class="btn btn-primary" onclick="payCustomSearch('N')">收款人账户信息维护</a>
		<a class="btn  btn-primary" onclick="loss('${prpLDlossPersTraceMainVo.registNo}')">定损详细信息</a>
        <a class="btn  btn-primary" onclick="checkSeeMessage('${prpLDlossPersTraceMainVo.registNo}')">查勘详细信息</a>		<a class="btn  btn-primary" onclick="openTaskEditWin('报案详细信息','/claimcar/regist/edit.do?flowId=${taskVo.flowId}&flowTaskId=${taskVo.taskId}&taskInKey=${taskVo.taskInKey}&handlerIdKey=${taskVo.handlerIdKey}&registNo=${taskVo.registNo}')">报案详细信息</a>
        <c:if test="${types=='true' && registVo.isGBFlag!='2' && registVo.isGBFlag!='4'}">
			<a class="btn  btn-danger" onclick="openTaskEditWin('预付任务发起','/claimcar/prePay/prePayApply.do?registNo=${prpLCheckDutyVo.registNo }');">申请预付</a>
		</c:if>
		<c:if test="${types=='false' }">
			<a class="btn  btn-disabled">申请预付</a>
		</c:if>
		
		<a class="btn btn-primary" onclick="showFlow('${taskVo.flowId}')">流程查询</a>

		<c:choose>
			<c:when test="${taskVo.handlerStatus != '3'}">
				<a class="btn btn-primary" href="javascript:;"
					onclick="certifyList('${prpLDlossPersTraceMainVo.registNo}','${taskVo.taskId}')">索赔清单</a>
			</c:when>
			<c:otherwise>
				<a class="btn btn-primary" href="javascript:;"
					onclick="certifyList('${prpLDlossPersTraceMainVo.registNo}','${taskVo.taskId}','yes')">索赔清单</a>
			</c:otherwise>
		</c:choose>
		<a class="btn btn-primary" onclick="appraisaInforEdit()">伤残鉴定机构维护</a>
        <c:if test="${assessSign eq '1' }">
		<a class="btn btn-primary" href="javascript:;" onclick="assessorView('${prpLDlossPersTraceMainVo.registNo}')">公估费查看</a>
		</c:if>
		<a class="btn  btn-primary" onclick="reportPrint('${prpLDlossPersTraceMainVo.registNo}')">代抄单打印</a>
		<input value="人伤注销" id="cancelPerson" type="button" class="btn  btn-primary" onclick="cancelPerson('${prpLDlossPersTraceMainVo.registNo}','${taskVo.taskId}','${prpLDlossPersTraceMainVo.id }')">
		<a class="btn btn-primary" id="surveyButton" onclick="createSurvey('${prpLDlossPersTraceMainVo.registNo}','${taskVo.flowId}','${taskVo.taskId}','${taskVo.nodeCode}','${taskVo.handlerUser}');">调查</a>	
		<a class="btn  btn-primary" onclick="lookLawSuit('${taskVo.registNo}')">查看高院信息</a>
		<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
		<a class="btn  btn-primary"  onclick="warnView('${prpLDlossPersTraceMainVo.registNo}')">山东预警推送</a>
	</div>
	<br/>
	<br/>
	<form name="fm" id="persTraceform" class="form-horizontal" role="form">
		<div class="fixedmargin page_wrap">
			<!-- 基本信息 -->
			<%@include file="PersTraceEdit_BaseInfo.jspf"%>
			<!-- 伤亡人员处理信息 -->
			<%@include file="PersTraceEdit_CasualtyInfo.jspf"%>
			<!-- 费用赔款信息 -->
			<%@include file="PersTraceEdit_FeePayInfo.jspf"%>
			<!-- 跟踪意见 -->
			<%@include file="PersTraceEdit_TraceOp.jspf"%>
			<!-- 意见列表 -->
			<%@include file="PersTraceEdit_OpinionList.jspf"%>
			<div id="hideDiv" style="display: none"></div>
			<c:choose>
				<c:when test="${handlerStatus==3 || handlerStatus==9}">
				</c:when>
				<c:otherwise>
					<div id="submitDiv" class="text-c">
						<input class="btn btn-primary radius" type="button" id="save" onclick="submitNextNode(this)" type="button" value="暂存" />
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input class="btn btn-primary radius" type="button" id="subPLNext" onclick="submitNextNode(this)" value="提交人伤后续跟踪" />
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input class="btn btn-primary radius" type="button" id="subPLVerify" onclick="submitNextNode(this)" value="提交人伤跟踪审核">
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input class="btn btn-primary radius" type="button" id="subPLCharge" onclick="submitNextNode(this)" value="提交人伤费用审核" />
						<c:if test="${flowNodeCode =='PLFirst'&&handlerStatus=='0'}">
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input class="btn btn-primary radius" type="button" id="acceptTask" onclick="acceptPersTraceTask();" value="接受任务" />
						</c:if>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</form>
	<script type="text/javascript" src="${ctx}/js/persTraceEdit/PersTraceEdit.js"></script>
	<script type="text/javascript" src="${ctx}/js/persTraceEdit/PersTraceEditRow.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript">
	$(function() {
		createRegistRisk();
		getGBStatus($("#flowTaskId").val());
	});
</script>
</body>
</html>