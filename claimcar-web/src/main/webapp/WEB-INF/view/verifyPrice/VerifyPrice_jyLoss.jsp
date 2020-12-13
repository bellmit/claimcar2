<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<div id="jyCertain">
	<input type="hidden" id="refreshFlag" value="${refreshFlag}">
	<input type ="hidden" id="offsetVeriRate_new" value="${offsetVeriRate }">
	<input type ="hidden" id="offsetVeri_new" value="${offsetVeri }">
	<input type="hidden" id="jyFlag" value="${lossCarMainVo.flag }" />
	<input class="btn btn-zd fl" type="button" value="进入配件系统"
		id="jyButton" onclick="isSaveInfoBeforeEnter(${lossCarMain.id})" />
	<!-- 零部件更换费用清单 -->
	<%@include file="VerifyPrice_Component.jspf"%>
	<!-- 零部件辅料费用清单 -->
	<%@include file="VerifyPrice_Material.jspf"%>
</div>
<!-- 金额合计 -->
<%@include file="VerifyPrice_SumList.jspf"%>
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