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
正在打开定损查看界面
<!-- <iframe name="JYIfm" src="_blank" style="width: 100%;height: 800px"></iframe>
	<form  id="jyform"  method="post"  name="fm" target="JYIfm"> -->
	<form  id="jyform"  method="post"  name="fm" > 
		<input type="hidden" class="common"  id="id" name="id" value="${id}" > 
		<input type="hidden" class="common"   name="registNo"  value="${registNo}" > 
</form>

<script type="text/javascript">

fm.action="/claimcar/defLossFittings/queryJyViewData.do";
fm.submit();

function close(){
	window.close(); 
}

</script>
</body>
</html>
