<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title></title>
		<base target="_self">
		<script type="text/javascript">
		//alert(window.opener.document.URL);//无
	/*  window.opener.document.all("prpLdeflossMain.id").value='${prpLdeflossMain.id}';
	 if( window.opener.document.getElementById("prpLdefLossThirdParty.id")!=null){
	    window.opener.document.getElementById("prpLdefLossThirdParty.id").value='${prpLdeflossMain.prpLthirdPartId}';
	 }
	 if(window.opener.document.getElementById("prpLdeflossMain.prpLthirdPartId")!=null){
	    window.opener.document.getElementById("prpLdeflossMain.prpLthirdPartId").value='${prpLdeflossMain.prpLthirdPartId}';
	 } */
</script>
</head>
<body>
正在访问精友系统
<!-- <iframe name="JYIfm" src="_blank" style="width: 100%;height: 800px"></iframe>
	<form  id="jyform"  method="post"  name="fm" target="JYIfm"> -->
	<form  id="jyform"  method="post"  name="fm" > 
		<input type="hidden" class="common"  id="id" name="fittingVo.lossCarId"       value="${fittingVo.lossCarId}" > 
		<input type="hidden" class="common"   name="fittingVo.operateType"       value="${fittingVo.operateType}" > 
		<input type="hidden" class="common"   name="fittingVo.registNo"          value="${fittingVo.registNo}" > 
		<input type="hidden" class="common"   name="fittingVo.systemAreaCode"    value="${fittingVo.systemAreaCode}" > 
		<input type="hidden" class="common"   name="fittingVo.localAreaCode"     value="${fittingVo.localAreaCode}" >
		<input type="hidden" class="common"   name="fittingVo.localAreaName"     value="${fittingVo.localAreaName}" >
		<input type="hidden" class="common"   name="fittingVo.operatorCode"      value="${fittingVo.operatorCode}">
		<input type="hidden" class="common"   name="fittingVo.nodeCode"      value="${fittingVo.nodeCode}">

</form>

<script type="text/javascript">

fm.action="/claimcar/defLossFittings/queryFittingSys.do";
fm.submit();


function createRow(){
	//alert("createRow");
	 window.opener.clearTableRow("#componentBody");
	 window.opener.clearTableRow("#materialBody");
	 window.opener.clearTableRow("#repairBody");
	 window.opener.clearTableRow("#outRepairBody");
}

function insertTableRowFM(id,html){
//	alert("insertTableRowFM");
	window.opener.insertTableRow(id,html);

}


function ttt(){
	//alert(1);
}
function close(){
	window.close(); 
}

</script>
</body>
</html>
