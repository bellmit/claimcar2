<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>结案详情页面</title>
<style>
	.btn {margin-bottom:5px;}
</style>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<!-- 按钮组 -->
		<div class="top_btn">
			<a class="btn  btn-primary" onclick="viewEndorseInfo('${endCaseVo.registNo}')">保单批改记录</a>
		    <a class="btn btn-primary" id="viewPolicy">出险保单</a>
		    <a class="btn btn-primary" id="displayFlow" onclick="showFlow('${claimVo.flowId}')">查看流程图信息</a>
		    <a class="btn btn-primary" onclick="createRegistMessage('${endCaseVo.registNo}','EndCase','')">案件备注</a>
		    <a class="btn btn-primary" onclick="lawsuit('${endCaseVo.registNo}','EndCase')">诉讼</a>
		    <a class="btn btn-primary" onclick="createRegistRisk()">风险提示</a>
		    <c:choose>
	        <c:when test="${lossCarSign eq '1'}">
			<a class="btn  btn-primary" onclick="verifyLossCertifyPrintJump('${endCaseVo.registNo}')">核损清单打印</a>
			</c:when>
			<c:otherwise>
			  <a class="btn  btn-disabled">核损清单打印</a>
			</c:otherwise>
			</c:choose>
			<c:choose>
			<c:when test="${compensateSign eq '1'}">
			<a class="btn btn-primary" onclick="AdjustersPrintJump('${endCaseVo.registNo}')">赔款理算书打印</a>
			</c:when>
			<c:otherwise>
			<a class="btn  btn-disabled" >赔款理算书打印</a>
			</c:otherwise>
	        </c:choose>
	        <c:choose>
			<c:when test="${compensateSign eq '1'}">
			<a class="btn  btn-primary"  onclick="certifyPrintPayFeeJump('${endCaseVo.registNo}')">赔款收据打印</a>
			</c:when>
			<c:otherwise>
			<a class="btn  btn-disabled" >赔款收据打印</a>
			</c:otherwise>
			</c:choose>
			<c:if test="${assessSign eq '1' }">
		    <a class="btn btn-primary" href="javascript:;" onclick="assessorView('${endCaseVo.registNo}')">公估费查看</a>
		    </c:if>
			<a class="btn  btn-primary" onclick="reportPrint('${endCaseVo.registNo}')">代抄单打印</a>
			<a class="btn  btn-primary" onclick="scoreInfo('${endCaseVo.registNo}')">反欺诈评分</a>
			<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
			<a class="btn  btn-primary"  onclick="warnView('${endCaseVo.registNo}')">山东预警推送</a>
		</div>
		<br/>
		<div class="table_cont">
			<form  id="defossform" role="form" method="post"  name="fm" >
				<input type="hidden" id="registNo" value="${endCaseVo.registNo}">
				<!-- <div class="table_cont"> -->
				
				<!-- 基本信息 -->
				<%@include file="EndCaseEdit_BasicInfo.jspf"%>
				
				<!-- 理赔车辆 -->
				<%@include file="EndCaseEdit_CarInfo.jspf"%>
				
				<!-- 交强险信息 -->
				<%@include file="EndCaseEdit_CI_Info.jspf"%>
			
				<!-- 商业险信息 -->
				<%@include file="EndCaseEdit_BI_Info.jspf"%>
				
			</form>
		</div>
		<br/><br/><br/>
		<div class="text-c mt-10">
			<!-- <input class="btn btn-primary ml-5" id="" onclick="" type="button" value="关闭"> -->
		</div>
		<br/><br/>
	</div>

	<script type="text/javascript">
		$(function() {
			//
		});

	</script>
	<script type="text/javascript" src="/claimcar/js/endcase/endcase.js"></script>
	<script type="text/javascript">
	$(function() {
		createRegistRisk();
	
	});
</script>
</body>

</html>