<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title></title>
		<base target="_self">
		<script type="text/javascript">
</script>
</head>
<body>
<form  id="mapform"  method="post"  name="fm" > 
	<input type="hidden" id="regionCode"       	  value="${res.regionCode}" > 
	<input type="hidden" id="damageAddress"       	  value="${res.damageAddress}" > 
	<input type="hidden" id="lngXlatY"       	  value="${res.lngXlatY}" > 
	<input type="hidden" id="item"       	  value="${item}" > 
	<input type="hidden" id="selfDefinareaCode"        value="${res.selfDefinareaCode}" > 
</form>

<script type="text/javascript">
$(function(){
	var item = $("#item").val();
	var regionCode = $("#regionCode").val();
	var damageAddress = $("#damageAddress").val();
	var lngXlatY = $("#lngXlatY").val();
	var selfDefinareaCode =$("#selfDefinareaCode").val();
	parent.getMapInfo(item,regionCode,damageAddress,lngXlatY,selfDefinareaCode);
	var index = parent.layer.getFrameIndex(window.name);// 获取窗口索引
	parent.layer.close(index);// 执行关闭	
});
</script>
</body>
</html>
