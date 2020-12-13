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
				</div>
			
			<!--选项卡一 -->
			<div class="tabCon">
			<c:choose>
			<c:when test="${!empty prplTestinfoMainVoRs}">
			<c:forEach var="prplTestinfoMainVo1" items="${prplTestinfoMainVoRs}" varStatus="status">
			<div class="table_wrap">
			<c:choose>
			<c:when test="${status.index eq '0' }">
			<div class="table_title f14">
			</c:when>
			<c:otherwise>
			<div class="table_title f14 table_close">
			</c:otherwise>
			</c:choose>
			<c:choose>
			<c:when test="${prplTestinfoMainVo1.lossType eq '1' }">标的车(${prplTestinfoMainVo1.vehicleRegistionNumber})</c:when>
			<c:otherwise>三者车(${prplTestinfoMainVo1.vehicleRegistionNumber})</c:otherwise>
			</c:choose>
			</div>
			<c:choose>
			<c:when test="${status.index eq '0' }">
			<div class="table_cont basicInforoVer" style="display: block;">
			</c:when>
			<c:otherwise>
			<div class="table_cont basicInforoVer" style="display: none;">
			</c:otherwise>
			</c:choose>
			
			<div class="formtable">	
			<!-- 基本信息 -->
			<%@include file="ControlExpertView1_RegistBaiscInfo.jsp"%>
			<!-- 配件列表 -->
			<%@include file="ControlExpertView1_RegistPartsInfo.jsp"%>
			<!-- 工时列表 -->
			<%@include file="ControlExpertView1_RegistLaborInfo.jsp"%>
			<!-- 欺诈风险列表 -->
			<%@include file="ControlExpertView1_RegistFraudRiskInfo.jsp"%>
			<!-- 风险点列表 -->
			<%@include file="ControlExpertView1_RegistRiskPointInfo.jsp"%>
			<!--操作不规范列表 -->
			<%@include file="ControlExpertView1_RegistOperationInfo.jsp"%>
			<!--价格合计列表 -->
			<%@include file="ControlExpertView1_RegistSumPrice.jsp"%>
          <div class="text-c">
            <label class="form_label col-0">检测结果：</label>
				<div class="form_input col-2">
					<input type="text" value="${prplTestinfoMainVo1.claimResult}"  readonly="readonly" style="width:150px;hight:45px;"/>
		        </div>
		        </br>
         </div>
         </div>
         </div>
         </div>
         </c:forEach>
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
			<c:when test="${!empty prplTestinfoMainVoCs}">
			<c:forEach var="prplTestinfoMainVo2" items="${prplTestinfoMainVoCs}" varStatus="status">
			<div class="table_wrap">
			<c:choose>
			<c:when test="${status.index eq '0' }">
			<div class="table_title f14">
			</c:when>
			<c:otherwise>
			<div class="table_title f14 table_close">
			</c:otherwise>
			</c:choose>
			<c:choose>
			<c:when test="${prplTestinfoMainVo2.lossType eq '1' }">标的车(${prplTestinfoMainVo2.vehicleRegistionNumber})</c:when>
			<c:otherwise>三者车(${prplTestinfoMainVo2.vehicleRegistionNumber})</c:otherwise>
			</c:choose>
			</div>
			<c:choose>
			<c:when test="${status.index eq '0' }">
			<div class="table_cont basicInforoVer" style="display: block;">
			</c:when>
			<c:otherwise>
			<div class="table_cont basicInforoVer" style="display: none;">
			</c:otherwise>
			</c:choose>
			<div class="formtable">	
			<!-- 基本信息 -->
			<%@include file="ControlExpertView1_CheckBaiscInfo.jsp"%>
			<!-- 配件列表 -->
			<%@include file="ControlExpertView1_CheckPartsInfo.jsp"%>
			<!-- 工时列表 -->
			<%@include file="ControlExpertView1_CheckLaborInfo.jsp"%>
			<!-- 欺诈风险列表 -->
			<%@include file="ControlExpertView1_CheckFraudRiskInfo.jsp"%>
			<!-- 风险点列表 -->
			<%@include file="ControlExpertView1_CheckRiskPointInfo.jsp"%>
			<!--操作不规范列表 -->
			<%@include file="ControlExpertView1_CheckOperationInfo.jsp"%>
			<!--价格合计列表 -->
			<%@include file="ControlExpertView1_CheckSumPrice.jsp"%>
         <div class="text-c">
         <label class="form_label col-0">检测结果：</label>
							<div class="form_input col-2">
								<input type="text" value="${prplTestinfoMainVo2.claimResult}"  readonly="readonly" style="width:150px;hight:45px;"/>
							</div>
							</br>
		</div>				
         </div>
        </div>
        </div>
        
        
          </c:forEach>
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