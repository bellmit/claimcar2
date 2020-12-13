<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>德联易控检测信息</title>
		<link href="../css/DeflossEdit.css" rel="stylesheet" type="text/css">
		<script type="text/javascript">
		$(function(){
			$.Huitab("#tab_demo .tabBar span","#tab_demo .tabCon","current","click","0");
			
			});
		</script>
		 <style type="text/css">
			.text-r{
				background-color: #F5F5F5
			}
			</style> 
	</head>
	<body>
		<div>
			<!--选项卡一 -->
			<div><label class="form_label col-4" style="top: -25px;">&nbsp;&nbsp;&nbsp;<font size="5" color="red">*</font><font size="5" color="blue">德联易控检测信息</font><font size="5" color="red">*</font></label></div>
			<div class="fixedmargin page_wrap">
			<div class="table_cont">	
			<!-- 基本信息 -->
			<%@include file="ceResultInfo_baseInfo.jsp"%>
			<!--减损统计汇总 -->
			<%@include file="ceResultInfo_priceSummaryInfo.jsp"%>
			<!-- 配件减损情况 -->
			<%@include file="ceResultInfo_sparepartInfo.jsp"%>
			<!-- 工时减损情况-->
			<%@include file="ceResultInfo_laborInfo.jsp"%>
			<!-- 辅料减损情况 -->
			<%@include file="ceResultInfo_smallSparepartInfo.jsp"%>
			<!-- 欺诈风险列表 -->
			<%@include file="ceResultInfo_fraudRiskInfo.jsp"%>
			<!--操作不规范列表-->
			<%@include file="ceResultInfo_nonStandardOperationInfo.jsp"%>
         </div>
         </div>
     </div>
	<script type="text/javascript" src="/claimcar/js/deflossEdit/DeflossEditRow.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript">
	
	</script>
	</body>
	
</html>