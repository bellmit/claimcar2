<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>照片审核</title>
</head>
<body>
<div class="table_wrap table_cont ">
<form id="PhotoVerifyForm" role="form" method="post">
	<input type="hidden" name="registNo" value="${registNo}"/>
	<input type="hidden" name="mainId" value="${mainId}"/>
	<input type="hidden" name="nodeCode" value="${nodeCode}"/>
	<input type="hidden" name="offLineHanding" value="${offLineHanding}"/>
<div class="form_input col-4 text-c">
	<app:codeSelect codeType="PhotoStatus" name="photoStatus" value="${photoStatus }" datatype="*" type="checkbox"/>
</div>
</form>
</div>
<div class="col-2"></div>
<div >
	<input class="btn btn-primary "  onclick="submit()" type="submit" value="确定">
</div>
<script type="text/javascript">
$(function(){
	var ajaxEdit = new AjaxEdit($('#PhotoVerifyForm'));
	var nodeCode = $("input[name='nodeCode']").val();
	if(nodeCode == "Check"){
		ajaxEdit.targetUrl = "/claimcar/check/PhotoVerify.do"; 
	}else{
		ajaxEdit.targetUrl = "/claimcar/defloss/PhotoVerify.do"; 
	}
	
	//ajaxEdit.afterSuccess = photoVerifyAfter;
	//绑定表单
	ajaxEdit.bindForm();
});

function submit(){
	$('#PhotoVerifyForm').submit();
}
</script>
</body>
</html>