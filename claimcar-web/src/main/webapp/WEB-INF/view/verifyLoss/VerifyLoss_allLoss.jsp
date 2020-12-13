<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<input type="hidden" id="refreshFlag" value="${refreshFlag}">
<c:if test="${lossCarMainVo.cetainLossType eq '01' || lossCarMainVo.cetainLossType eq '04' }"> 
	<div id="jyCertain"> 
		<input type ="hidden" id="offsetVeriRate_new" value="${offsetVeriRate }"/>
		<input type ="hidden" id="offsetVeri_new" value="${offsetVeri }"/>
		<input type="hidden" id="jyFlag" value="${lossCarMainVo.flag }" />
		<input class="btn btn-zd fl" type="button" value="进入配件系统" id="jyButton" onclick="isSaveInfoBeforeEnter(${lossCarMain.id})"/>
		<!-- 零部件更换费用清单 -->
		<%@include file="VerifyLoss_Component.jspf" %>
		<!-- 零部件修理费用清单 -->
		<%@include file="VerifyLoss_Material.jspf" %>
		<!-- 修理费用清单 -->
		<%@include file="VerifyLoss_Repair.jspf" %>
		<!-- 外修费用清单 -->
		<%@include file="VerifyLoss_OuterRepair.jspf" %>
	</div>
	</c:if>
	<!-- 金额合计 -->
	<%@include file="VerifyLoss_SumList.jspf" %>
	<c:if test="${lossCarMainVo.deflossCarType eq '1'}">
	<!-- 附加险 -->
		<%@include file="VerifyLoss_SubRisk.jspf" %> 
	</c:if>
	<!-- 费用赔款信息 -->
	<%@include file="VerifyLoss_FeeInfo.jspf" %>

<script type="text/javascript">
	var refreshFlag = $("#refreshFlag").val();
	if(refreshFlag=="1"){
		var offsetVeri = $("#offsetVeri_new").val();
		var offsetVeriRate = $("#offsetVeriRate_new").val();
		if(offsetVeri!="" && offsetVeri!=undefined){
			$("#offsetVeri").html(offsetVeri);
			if(offsetVeriRate>20){
				$("#offsetVeriRate").html("<font color='red'>"+offsetVeriRate+"%</font>");
			}else{
				$("#offsetVeriRate").html(offsetVeriRate+"%");
			}
		}
	}
		
</script>