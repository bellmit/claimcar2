<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<input type="hidden" id="cetainLossType_Loss" value="${lossCarMainVo.cetainLossType}">
<input type="hidden" id="refreshFlag" value="${refreshFlag}">
<!--代为求偿  -->
<c:if test="${lossCarMainVo.deflossCarType eq '1'}">
	<%@include file="DeflossEdit_Subrogation.jspf" %>
</c:if>
<div id="jyCertain"> 
	<input class="btn btn-zd fl" type="button" id="jyButton" value="进入配件系统"  onclick="isSaveInfoBeforeEnter()"/>
	<!-- 零部件更换费用清单 -->
	<%@include file="DeflossEdit_Component.jspf" %>
	<!-- 零部件修理费用清单 -->
	<%@include file="DeflossEdit_Material.jspf" %>
	<!-- 修理费用清单 -->
	<%@include file="DeflossEdit_Repair.jspf" %>
	<!-- 外修费用清单 -->
	<%@include file="DeflossEdit_OuterRepair.jspf" %>
</div>
<!-- 金额合计 -->
<%@include file="DeflossEdit_SumList.jspf" %>
<div id="defloss_subFeeDiv">
<c:if test="${lossCarMainVo.deflossCarType eq '1'}">
	<!-- 附加险 -->
	<div id="defloss_SubRiskDiv">
		<%@include file="DeflossEdit_SubRisk.jspf" %>
	</div>
</c:if>
<!-- 费用赔款信息 -->
<%@include file="../loss-common/FeePayInfo.jspf" %> 
</div>
<script type="text/javascript">
var refreshFlag = $("#refreshFlag").val();
if(refreshFlag=="1"){
	subrogationClick();
	var certainType = $("#cetainLossType_Loss").val();
	if(certainType=="02" || certainType=="03"){
		$("#jySumList").css('display','none'); 
		$("#otherSumList").css('display','block'); 
		$("#jyCertain").css("display","none");
	}else{
		$("#jySumList").css('display','block'); 
		$("#otherSumList").css('display','none'); 
		if(certainType=="05"){
			$("#jyCertain").css("display","none");
			$("#jy_sumRescueFee").attr("readonly","readonly");
		}else{
			$("#jyCertain").css("display","block");
		}
	}
}
		
</script>
<style type="text/css">
	.aleft{
		border-left:2px solid #91c9f9;
	} 
</style>
