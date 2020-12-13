<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>德联检测信息</title>
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
			
			.table_sub th, .table_sub td {
					padding: 8px;
					line-height: 15px;
				}
			</style> 
	</head>
	<body>
		<div>
			<div id="tab_demo" class="HuiTab">
				<div class="tabBar cl">
					<span>报案</span>
					<span>查勘</span>
					<span>定损</span>
				</div>
			
			<!--选项卡一 -->
			<div class="tabCon">
			<c:choose>
			<c:when test="${!empty prplTestinfoMainVo1.registNo}">
			<!-- 基本信息 -->
			<%@include file="ControlExpertView_RegistBaiscInfo.jsp"%>
			<!-- 配件列表 -->
			<%@include file="ControlExpertView_RegistPartsInfo.jsp"%>
			<!-- 工时列表 -->
			<%@include file="ControlExpertView_RegistLaborInfo.jsp"%>
			<!-- 欺诈风险列表 -->
			<%@include file="ControlExpertView_RegistFraudRiskInfo.jsp"%>
			<!-- 风险点列表 -->
			<%@include file="ControlExpertView_RegistRiskPointInfo.jsp"%>
			<!--操作不规范列表 -->
			<%@include file="ControlExpertView_RegistOperationInfo.jsp"%>
			<!--价格合计列表 -->
			<%@include file="ControlExpertView_RegistSumPrice.jsp"%>
          <div class="text-c">
            <label class="form_label col-0">检测结果：</label>
				<div class="form_input col-2">
					<input type="text" value="${prplTestinfoMainVo1.claimResult}"  readonly="readonly" style="width:150px;hight:45px;"/>
		        </div>
		        </br>
         </div>
         </c:when>
         <c:otherwise>
         <div style="text-align:center;">
         <font size='3' color='#333' >无检测数据</font>
         </div>
         </c:otherwise>
         </c:choose>
         </div>
	     	  	
						
			<!-- 选项卡二 -->
			<div class="tabCon">
			<c:choose>
			<c:when test="${!empty prplTestinfoMainVo2.registNo}">
			<!-- 基本信息 -->
			<%@include file="ControlExpertView_CheckBaiscInfo.jsp"%>
			<!-- 配件列表 -->
			<%@include file="ControlExpertView_CheckPartsInfo.jsp"%>
			<!-- 工时列表 -->
			<%@include file="ControlExpertView_CheckLaborInfo.jsp"%>
			<!-- 欺诈风险列表 -->
			<%@include file="ControlExpertView_CheckFraudRiskInfo.jsp"%>
			<!-- 风险点列表 -->
			<%@include file="ControlExpertView_CheckRiskPointInfo.jsp"%>
			<!--操作不规范列表 -->
			<%@include file="ControlExpertView_CheckOperationInfo.jsp"%>
			<!--价格合计列表 -->
			<%@include file="ControlExpertView_CheckSumPrice.jsp"%>
         <div class="text-c">
         <label class="form_label col-0">检测结果：</label>
							<div class="form_input col-2">
								<input type="text" value="${prplTestinfoMainVo2.claimResult}"  readonly="readonly" style="width:150px;hight:45px;"/>
							</div>
							</br>
         </div>
         </c:when>
         <c:otherwise>
         <div style="text-align:center;">
         <font size='3' color='#333' >无检测数据</font>
         </div>
         </c:otherwise>
         </c:choose>
         </div>

	
	      <!-- 选项卡三 -->
			<div class="tabCon">
			<c:choose>
			<c:when test="${!empty prplTestinfoMainVo.registNo}">
			<!-- 基本信息 -->
			<%@include file="ControlExpertView_BaiscInfo.jsp"%>
			<!-- 配件列表 -->
			<%@include file="ControlExpertView_PartsInfo.jsp"%>
			<!-- 工时列表 -->
			<%@include file="ControlExpertView_LaborInfo.jsp"%>
			<!-- 欺诈风险列表 -->
			<%@include file="ControlExpertView_FraudRiskInfo.jsp"%>
			<!-- 风险点列表 -->
			<%@include file="ControlExpertView_RiskPointInfo.jsp"%>
			<!--操作不规范列表 -->
			<%@include file="ControlExpertView_OperationInfo.jsp"%>
			<!--价格合计列表 -->
			<%@include file="ControlExpertView_SumPrice.jsp"%>
         <div class="text-c">
         <label class="form_label col-0">检测结果：</label>
							<div class="form_input col-2">
								<input type="text" value="${prplTestinfoMainVo.claimResult}"  readonly="readonly" style="width:150px;hight:45px;"/>
							</div>
							</br>
         </div>
         </c:when>
         <c:otherwise>
         <div style="text-align:center;">
         <font size='3' color='#333' >无检测数据</font>
         </div>
         </c:otherwise>
         </c:choose>
         </div>			
		
		</div>	
		</div>
	<script type="text/javascript" src="/claimcar/js/deflossEdit/DeflossEditRow.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript">
	
	</script>
	</body>
	
</html>