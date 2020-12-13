<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>人伤大案审核</title>
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

.intermediary {
	display: none;
}

.table_cont .table tbody .Bth {
	border-bottom: 0;
	border-top: 1px solid #ddd;
}

.table tbody tr:first-child .Bth {
	border-top: 0;
}

.tabBar span.current{
	color: #ff0000;
}

.btn {margin-bottom:5px;}
</style>
</head>
<body>
    <input class="btn btn-primary radius" type="hidden" id="registNo"  value="${registNo }" />
	<div class="top_btn">
		<a class="btn btn-primary" onclick="viewEndorseInfo('${prpLDlossPersTraceMainVo.registNo}')">保单批改记录</a>
		<a class="btn btn-primary" onclick="viewPolicyInfo('${prpLDlossPersTraceMainVo.registNo}')">出险保单</a>
        <a class="btn btn-primary" onclick="caseDetails('${prpLDlossPersTraceMainVo.registNo}')">历史出险信息</a>
		<a class="btn btn-danger" href="javascript:;" onclick="createRegistRisk()">风险提示信息</a>
		<a class="btn  btn-primary" onclick="imageMovieUpload('${taskVo.taskId}')">信雅达影像上传</a>
		<a class="btn  btn-primary" onclick="createRegistMessage('${prpLDlossPersTraceMainVo.registNo  }', '${flowNodeCode }')">案件备注</a>
		<a class="btn  btn-danger" onclick="viewBigOpinion('${prpLDlossPersTraceMainVo.registNo  }', '${flowNodeCode }')">大案上报</a> 
		<a class="btn btn-primary" onclick="lawsuit('${prpLDlossPersTraceMainVo.registNo}','${flowNodeCode}')">诉讼</a>
	    <a class="btn  btn-primary" onclick="chargemessage('${prpLDlossPersTraceMainVo.registNo}')">费用信息</a> 
		<a class="btn  btn-primary" onclick="Findpeople('${prpLDlossPersTraceMainVo.id}')">费用修改记录</a>
		<a class="btn  btn-primary" onclick="loss('${prpLDlossPersTraceMainVo.registNo }')">定损详细信息</a>
        <a class="btn  btn-primary" onclick="checkSeeMessage('${prpLDlossPersTraceMainVo.registNo}')">查勘详细信息</a>
		<a class="btn  btn-primary" onclick="openTaskEditWin('报案详细信息','/claimcar/regist/edit.do?flowId=${taskVo.flowId}&flowTaskId=${taskVo.taskId}&taskInKey=${taskVo.taskInKey}&handlerIdKey=${taskVo.handlerIdKey}&registNo=${taskVo.registNo}')">报案详细信息</a>
		<a class="btn  btn-danger" onclick="padPayTaskVlaid('${prpLDlossPersTraceMainVo.registNo }')">申请垫付</a>
		<c:if test="${types=='true' }">
			<a class="btn  btn-danger" onclick="openTaskEditWin('预付任务发起','/claimcar/prePay/prePayApply.do?registNo=${prpLDlossPersTraceMainVo.registNo }');">申请预付</a>
		</c:if>
		<c:if test="${types=='false' }">
			<a class="btn  btn-disabled">申请预付</a>
		</c:if>
			<a class="btn btn-primary " onclick="showFlow('${taskVo.flowId}')">流程查询</a>
			<a class="btn  btn-primary" onclick="imageMovieScan('${prpLDlossPersTraceMainVo.registNo}')">信雅达影像查看</a>
	
	</div>
	<br/>
	<br/>
	<form name="fm" id="PLBigForm" class="form-horizontal" role="form">
		<div class="fixedmargin page_wrap">
			<!-- 基本信息 -->
			<%@include file="PersTracePLBig_BaseInfo.jspf"%>
			<!-- 伤亡人员处理信息 -->
			<%@include file="PersTracePLBig_CasualtyInfo.jspf"%>
			<!-- 费用赔款信息 -->
			<%@include file="PersTracePLBig_FeePayInfo.jspf"%>
			<!-- 意见列表 -->
			<%@include file="PersTracePLBig_OpinionList.jspf"%>
			<div id="hideDiv" style="display: none"></div>
			<c:choose>
				<c:when test="${handlerStatus==3 }">
				</c:when>
				<c:otherwise>
					<div id="submitDiv" class="text-c">
						<input class="btn btn-primary radius" type="button" id="save" onclick="submitNextNode(this)" value="暂存" />
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input class="btn btn-primary radius" type="button" id="audit" onclick="submitNextNode(this)" value="提交" />
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</form>
	<script type="text/javascript" src="${ctx}/js/persTracePLBig/PersTracePLBig.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
    <script type="text/javascript">
	$(function() {
		createRegistRisk();
	
	});
</script>
</body>
</html>