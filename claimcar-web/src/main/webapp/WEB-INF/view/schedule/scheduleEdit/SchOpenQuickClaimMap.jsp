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
    <c:forEach items="${res}" varStatus="index" var="scheduleItem">
       <input type="hidden" id="nodeType"         value="${scheduleItem.nodeType}" > 
       <c:if test="${scheduleItem.nodeType eq 'Check'}">
       <input type="hidden" id="checkComCode"         value="${scheduleItem.scheduleObjectId}" > 
       <input type="hidden" id="checkUserCode"        value="${scheduleItem.nextHandlerCode}" > 
       <input type="hidden" id="checkComName"         value="${scheduleItem.scheduleObjectName}" > 
       <input type="hidden" id="checkUserName"        value="${scheduleItem.nextHandlerName}" > 
      <input type="hidden" id="cselfDefinAreaCode"      value="${scheduleItem.selfDefinAreaCode}" > 
	  <input type="hidden" id="crelateHandlerName"       	  value="${scheduleItem.relateHandlerName}" > 
	  <input type="hidden" id="crelateHandlerMobile"       	  value="${scheduleItem.relateHandlerMobile}" > 
	  <input type="hidden" id="cisComuserCode"       	  value="${scheduleItem.isComuserCode}" >  
       </c:if>
       <c:if test="${scheduleItem.nodeType eq 'PLoss'}">
        <input type="hidden" id="pLossComCode"       value="${scheduleItem.scheduleObjectId}" > 
       <input type="hidden" id="pLossUserCode"        value="${scheduleItem.nextHandlerCode}" > 
       <input type="hidden" id="pLossComName"       value="${scheduleItem.scheduleObjectName}" > 
       <input type="hidden" id="pLossUserName"        value="${scheduleItem.nextHandlerName}" > 
        <input type="hidden" id="pselfDefinAreaCode"      value="${scheduleItem.selfDefinAreaCode}" > 
	  <input type="hidden" id="prelateHandlerName"       	  value="${scheduleItem.relateHandlerName}" > 
	  <input type="hidden" id="prelateHandlerMobile"       	  value="${scheduleItem.relateHandlerMobile}" > 
	  <input type="hidden" id="pisComuserCode"       	  value="${scheduleItem.isComuserCode}" >  
       </c:if>
       <c:if test="${scheduleItem.nodeType eq 'DLCar' || scheduleItem.nodeType eq 'DLProp'}">
       <input type="hidden" id="lossComCode"       value="${scheduleItem.scheduleObjectId}" > 
       <input type="hidden" id="lossUserCode"        value="${scheduleItem.nextHandlerCode}" > 
       <input type="hidden" id="lossComName"       value="${scheduleItem.scheduleObjectName}" > 
       <input type="hidden" id="lossUserName"        value="${scheduleItem.nextHandlerName}" > 
        <input type="hidden" id="lselfDefinAreaCode"      value="${scheduleItem.selfDefinAreaCode}" > 
	  <input type="hidden" id="lrelateHandlerName"       	  value="${scheduleItem.relateHandlerName}" > 
	  <input type="hidden" id="lrelateHandlerMobile"       	  value="${scheduleItem.relateHandlerMobile}" > 
	  <input type="hidden" id="lisComuserCode"       	  value="${scheduleItem.isComuserCode}" >  
       </c:if>
       <input type="hidden" id="regionCode"       	  value="${scheduleItem.regionCode}" > 
	   <input type="hidden" id="damageAddress"       	  value="${scheduleItem.damageAddress}" > 
	   <input type="hidden" id="cityCode"       	  value="${scheduleItem.cityCode}" > 
	 <input value="${scheduleItem.callNumber}" id='callNumber' type='hidden'/>
	
	</c:forEach>
</form>

<script type="text/javascript">
$(function(){
	var nodeType = $("#nodeType").val();
	var returnData;
	var selfDefinAreaCode = $("#cselfDefinAreaCode").val();
	var relateHandlerName = $("#crelateHandlerName").val();
	var relateHandlerMobile = $("#crelateHandlerMobile").val();
	var isComuserCode = $("#cisComuserCode").val();
	
	var cityCode = $("#cityCode").val();
	//var pselfDefinAreaCode = $("#pselfDefinAreaCode").val();
	var prelateHandlerName = $("#prelateHandlerName").val();
	var prelateHandlerMobile = $("#prelateHandlerMobile").val();
	var pisComuserCode = $("#pisComuserCode").val();
	
	var lselfDefinAreaCode = $("#lselfDefinAreaCode").val();
	var lrelateHandlerName = $("#lrelateHandlerName").val();
	var lrelateHandlerMobile = $("#lrelateHandlerMobile").val();
	var lisComuserCode = $("#lisComuserCode").val();
	var callNumber = $("#callNumber").val();
	if(nodeType == "Check" || nodeType =="PLoss"){
 
		var checkComCode = $("#checkComCode").val();
		var checkUserCode = $("#checkUserCode").val();
		var pLossComCode = $("#pLossComCode").val();
		var pLossUserCode = $("#pLossUserCode").val();
		
		var checkComName = $("#checkComName").val();
		var checkUserName = $("#checkUserName").val();
		var pLossComName = $("#pLossComName").val();
		var pLossUserName = $("#pLossUserName").val();
		
		var regionCode = $("#regionCode").val();
		var areaAddress = $("#damageAddress").val();
		
		
		
		returnData = {
				"nodeType" : nodeType,
				"checkComCode" : checkComCode,
				"checkUserCode" : checkUserCode,
				"pLossComCode" : pLossComCode,
				"pLossUserCode" : pLossUserCode,
				"checkComName" : checkComName,
				"checkUserName" : checkUserName,
				"pLossComName" : pLossComName,
				"pLossUserName" : pLossUserName,
				"regionCode" : regionCode,
				"areaAddress" : areaAddress,
				"selfDefinAreaCode" : selfDefinAreaCode,
				"relateHandlerName" : relateHandlerName,
				"relateHandlerMobile" : relateHandlerMobile,
				"isComuserCode" : isComuserCode,
				"prelateHandlerName" : prelateHandlerName,
				"prelateHandlerMobile" : prelateHandlerMobile,
				"pisComuserCode" : pisComuserCode,
				"cityCode" :cityCode,
				"callNumber" :callNumber,
			};
	}else{
		var lossComCode = $("#lossComCode").val();
		var lossUserCode = $("#lossUserCode").val();
		var lossComName = $("#lossComName").val();
		var lossUserName = $("#lossUserName").val();
		
		var regionCode = $("#regionCode").val();
		var areaAddress = $("#damageAddress").val();
		returnData = {
				"nodeType" : nodeType,
				"lossComCode" : lossComCode,
				"lossUserCode" : lossUserCode,
				"lossComName" : lossComName,
				"lossUserName" : lossUserName,
				"regionCode" : regionCode,
				"areaAddress" : areaAddress,
				"selfDefinareaCode" : lselfDefinAreaCode,
				"lrelateHandlerName" : lrelateHandlerName,
				"lrelateHandlerMobile" : lrelateHandlerMobile,
				"lisComuserCode" : lisComuserCode,
				"cityCode" :cityCode,
				"callNumber" :callNumber
			};
	}
	parent.getMapInfo(returnData);
	var index = parent.layer.getFrameIndex(window.name);// 获取窗口索引
	parent.layer.close(index);// 执行关闭	
	});
</script>
</body>
</html>
