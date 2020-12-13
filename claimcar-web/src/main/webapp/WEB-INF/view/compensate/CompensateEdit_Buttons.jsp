<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<div class="top_btn">
	<a class="btn  btn-primary " onclick="viewEndorseInfo('${prpLWfTaskVo.registNo}')">保单批改记录</a>
	<a class="btn  btn-primary " onclick="viewPolicyInfo('${prpLWfTaskVo.registNo}')">保单详细信息</a>
	<a class="btn  btn-primary "
		onclick="openTaskEditWin('报案详细信息','/claimcar/regist/edit.do?registNo=${prpLWfTaskVo.registNo}')">报案详细信息</a>
	<a class="btn  btn-primary " onclick="checkSeeMessage('${prpLWfTaskVo.registNo}')">查勘详细信息</a> 
	<a class="btn  btn-primary " onclick="loss('${prpLWfTaskVo.registNo}')">定损详细信息</a> 
	<a class="btn  btn-primary " onclick="Searchpeople('${prpLWfTaskVo.registNo}')">人伤跟踪信息</a>
	<c:choose>
		<c:when test="${empty prpLCompensate.compensateNo}">
			<a class="btn btn-disabled ">历次审核意见</a>
		</c:when>
		<c:otherwise>
			<a class="btn btn-primary "
				onclick="historyMessage('${prpLCompensate.registNo}','${prpLCompensate.compensateNo}')">历次审核意见</a>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${empty prpLWfTaskVo.taskId}">
			<a class="btn btn-disabled ">信雅达影像上传</a>
		</c:when>
		<c:otherwise>
			<a class="btn  btn-primary" onclick="imageMovieUpload('${prpLWfTaskVo.taskId}')">信雅达影像上传</a>
		</c:otherwise>
	</c:choose>
	<a class="btn  btn-primary " onclick="lawsuits('${prpLWfTaskVo.registNo}','Compe')">诉讼</a> 
	<a class="btn btn-danger " href="javascript:;" onclick="createRegistRisk()">风险提示信息</a> 
	<a class="btn  btn-primary " id="compeRegistMsg">案件备注</a>
	<c:choose>
     <c:when test="${compensateSign eq '1' && prpLWfTaskVo.handlerStatus eq '3'}">
    <a class="btn btn-primary " onclick="AdjustersPrintJump('${prpLCompensate.registNo}')">赔款理算书打印</a>
	</c:when>
	 <c:otherwise>
		<a class="btn  btn-disabled " >赔款理算书打印</a>
	 </c:otherwise>
      </c:choose>
	   <c:choose>
		<c:when test="${compensateSign eq '1' && prpLWfTaskVo.handlerStatus eq '3'}">
		<a class="btn  btn-primary "  onclick="certifyPrintPayFeeJump('${prpLCompensate.registNo}')">赔款收据打印</a>
		</c:when>
		<c:otherwise>
		<a class="btn  btn-disabled " >赔款收据打印</a>
		</c:otherwise>
		</c:choose>
		
	<c:choose>		
		<c:when test="${prpLWfTaskVo.handlerStatus !='3'}">
			<a class="btn btn-disabled ">赔款理算书附页打印</a>
		</c:when>
		<c:otherwise>
			<a class="btn btn-primary " onclick="AdjustersPrints('${prpLCompensate.registNo}','${prpLCompensate.compensateNo}')">赔款理算书附页打印</a>
		</c:otherwise>
	</c:choose>
	<a class="btn  btn-primary " onclick="seeUpdateMessage('${prpLCompensate.registNo}')">查看估损更新轨迹</a>
	<c:choose>
		<c:when test="${prpLWfTaskVo.handlerStatus !='3'}">
			<input class="btn btn-primary " style="width: 60px"
				id="CompCancel" onclick="compCancel()" value="注销">
		</c:when>
		<c:otherwise>
			<a class="btn  btn-disabled ">注销</a>
		</c:otherwise>
	</c:choose>
	 <c:choose>
	       <c:when test="${lossCarSign eq '1'}">
			<a class="btn  btn-primary " onclick="verifyLossCertifyPrintJump('${prpLWfTaskVo.registNo}')">核损清单打印</a>
			</c:when>
			<c:otherwise>
			  <a class="btn  btn-disabled ">核损清单打印</a>
			</c:otherwise>
			</c:choose>
	<a class="btn  btn-primary" onclick="imageMovieScan('${prpLWfTaskVo.registNo}')">信雅达影像查看</a>
	<a class="btn  btn-primary " onclick="payCustQuery('${prpLWfTaskVo.registNo}')">收款人查询</a>
	<a class="btn  btn-primary " id="certi" onclick="backCerti('${prpLWfTaskVo.registNo}')">退回单证</a>
	<c:if test="${assessSign eq '1' }">
	<a class="btn btn-primary " href="javascript:;" onclick="assessorView('${prpLWfTaskVo.registNo}')">公估费查看</a>
	</c:if>
	<a class="btn  btn-primary " onclick="reportPrint('${prpLWfTaskVo.registNo}')">代抄单打印</a>
	<a class="btn btn-primary mb-10" id="surveyButton" onclick="createSurvey('${prpLWfTaskVo.registNo}','${prpLWfTaskVo.flowId}','${prpLWfTaskVo.taskId}','${prpLWfTaskVo.nodeCode}','${prpLWfTaskVo.handlerUser}');">调查</a>
	<c:if test="${policeInfoFlag eq '1'}">
		<a class="btn btn-primary" onclick="policeInfoShow('${prpLWfTaskVo.registNo}')">预警信息</a>	
		<a class="btn  btn-primary " id="comCheckPic" name="comCheckPic" onclick="comCheckPic()">影像比对</a>
	</c:if>
	<c:if test="${roleFlag eq '1' }">		
		<a class="btn btn-primary"  onclick="ruleView('${prpLCompensate.registNo}','${prpLCompensate.riskCode}','${prpLWfTaskVo.nodeCode}','','${prpLWfTaskVo.upperTaskId}')">ILOG规则信息查看</a>
	</c:if>
	<c:if test="${prpLCMain.coinsFlag  != '0' && not empty prpLCMain.coinsFlag}">
		<a class="btn  btn-primary " onclick="coinsInfo()">联共保分摊信息</a>
	</c:if>
	<a class="btn  btn-primary" onclick="lookLawSuit('${prpLWfTaskVo.registNo}')">查看高院信息</a>
	<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
	<a class="btn  btn-primary"  onclick="warnView('${prpLWfTaskVo.registNo}')">山东预警推送</a>
</div>
<br />
<br />
<br />
