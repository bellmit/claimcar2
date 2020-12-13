<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>详细信息</title>
<!-- 查看估损更新轨迹 信息-->
</head>
<script type="text/javascript">
	$(function() {
		$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current",
				"click", "0");
		layer.config({
			extend : 'extend/layer.ext.js'
		});
	});
</script>
<body>
	<div id="tab-system" class="HuiTab">
		<div class="tabBar f_gray4 cl">
	
		
			<span><i class="Hui-iconfont handun">&#xe619;</i>交强</span>
			 <span><i class="Hui-iconfont handing">&#xe619;</i>商业</span>
		
			</div>
		<div class="tabCon table_list">
			<!-- 交强   开始 -->
			<!-- <div class="tabCon clearfix">-->
			<table class="table table-border table-hover" cellpadding="0"
				cellspacing="0">
				<thead>

					<%-- <tr>
					    <th Colspan="1">报案号</th>
						<th Colspan="1"><u>${registNo}</u></th>
						<th Colspan="1">保单号</th>
						<th Colspan="1"><u>${policyNo1}</u></th>
						<th Colspan="2">保单类型</th>
						<th Colspan="1"><u>交强</u></th>
					    <th Colspan="5"></th>
					</tr>
 --%>               <div class="row mb-3 cl">
							<label class="form-label col-2 text-c "> 报案号</label>
							<div class="formControls col-2">
								<u>${registNo}</u>
							</div>
							<label class="form-label col-2 text-c"> 保单号</label>
                            <div class="formControls col-2">
								<u>${policyNo1}</u>
							</div>
							<label class="form-label col-2 text-c"> 保单类型</label>
							<div class="formControls col-2">
								<u>交强</u>
							</div>
							</div>
							<hr>
					<tr>
						<th width="9%">车牌号码&nbsp;&nbsp;</th>
					    <th width="9%">出险原因&nbsp;&nbsp;</th>
					 	<th width="9%">出险时间&nbsp;&nbsp;</th>
					 	<th width="9%">出险地点&nbsp;&nbsp;</th>
					 	<th width="9%">立案时间&nbsp;&nbsp;</th>
					 	<th width="9%">结案时间&nbsp;&nbsp;</th>
					 	<th width="9%">已决金额&nbsp;&nbsp;</th>
					 	<th width="9%">未决金额&nbsp;&nbsp;</th>
					 	<th width="9%">赔案状态&nbsp;&nbsp;</th>
					 	<th width="9%">案件类型&nbsp;&nbsp;</th>
					 	<th width="10%">流程图</th>
					 </tr>
				</thead>
				<c:forEach var="vo" items="${prpLClaimSummaryList}">
				
						<c:if test="${vo.riskCode == '1101'}">
							<tr>
								
								<td width="9%">${vo.licenseNo}</td>
								<td width="9%"><app:codetrans codeCode="${vo.damageCode}" codeType="DamageCode" /></td>
								<td width="9%"><fmt:formatDate value='${vo.damageTime}' pattern='yyyy-MM-dd'/></td>
								<td width="9%">${vo.damageAddress}</td>
								<td width="9%"><fmt:formatDate value='${vo.claimTime}' pattern='yyyy-MM-dd'/></td>
								<td width="9%"><fmt:formatDate value='${vo.endCaseTime}' pattern='yyyy-MM-dd'/></td>
								<td width="9%">${vo.realPay}</td>
								<td width="9%">${vo.willPay}</td>
								<c:choose>
								<c:when test="${vo.caseStatus eq 'N'}">
								<td width="9%">正常处理</td>
								</c:when>
								<c:when test="${vo.caseStatus eq 'C'}">
								<td width="9%">注销</td>
								</c:when>
								<c:when test="${vo.caseStatus eq 'E'}">
								<td width="9%">完成</td>
								</c:when>
								<c:otherwise>
								<td width="9%"></td>
								</c:otherwise>
								</c:choose>
								<td width="9%">自赔</td>
								<td width="10%"><input  class="btn btn-ml-5" onclick="seePhoto('${vo.flowId}')" type="button"  value="..."/></td>
								
							</tr>
						</c:if>
			
				</c:forEach>

			</table>
			<!--table   结束-->

		</div>
		<!--</div>  -->
		<div class="tabCon table_list">
			<!-- 商业   开始 -->
			<!--<div class="tabCon clearfix">-->
			<table class="table table-border table-hover" cellpadding="0"
				cellspacing="0">
				<thead>

					<%-- <tr>
					    <th Colspan="1">报案号</th>
						<th Colspan="1"><u>${registNo}</u></th>
						<th Colspan="1">保单号</th>
						<th Colspan="1"><u>${policyNo2}</u></th>
						<th Colspan="2">保单类型</th>
						<th Colspan="1"><u>商业</u></th>
						<th Colspan="5"></th>
						
					</tr>
 --%>     <div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 报案号</label>
							<div class="formControls col-2">
								<u>${registNo}</u>
							</div>
							<label class="form-label col-2 text-c"> 保单号</label>
                            <div class="formControls col-2">
								<u>${policyNo2}</u>
							</div>
							<label class="form-label col-2 text-c"> 保单类型</label>
							<div class="formControls col-2">
								<u>商业</u>
							</div>
							</div>
							<hr>
					<tr>
						
					 	<th width="9%">车牌号码&nbsp;&nbsp;</th>
					    <th width="9%">出险原因&nbsp;&nbsp;</th>
					 	<th width="9%">出险时间&nbsp;&nbsp;</th>
					 	<th width="9%">出险地点&nbsp;&nbsp;</th>
					 	<th width="9%">立案时间&nbsp;&nbsp;</th>
					 	<th width="9%">结案时间&nbsp;&nbsp;</th>
					 	<th width="9%">已决金额&nbsp;&nbsp;</th>
					 	<th width="9%">未决金额&nbsp;&nbsp;</th>
					 	<th width="9%">赔案状态&nbsp;&nbsp;</th>
					 	<th width="9%">案件类型&nbsp;&nbsp;</th>
					 	<th width="10%">流程图&nbsp;&nbsp;</th>
					</tr>
				</thead>
				<c:forEach var="vo1" items="${prpLClaimSummaryList}">
				       <c:if test="${vo1.riskCode != '1101'}">
							<tr>
								<td width="9%">${vo1.licenseNo}</td>
								<td width="9%"><app:codetrans codeCode="${vo1.damageCode}" codeType="DamageCode" /></td>
								<td width="9%"><fmt:formatDate value='${vo1.damageTime}' pattern='yyyy-MM-dd'/></td>
								<td width="9%">${vo1.damageAddress}</td>
								<td width="9%"><fmt:formatDate value='${vo1.claimTime}' pattern='yyyy-MM-dd'/></td>
								<td width="9%"><fmt:formatDate value='${vo1.endCaseTime}' pattern='yyyy-MM-dd'/></td>
								<td width="9%">${vo1.realPay}</td>
								<td width="9%">${vo1.willPay}</td>
								<c:choose>
								<c:when test="${vo1.caseStatus eq 'N'}">
								<td width="9%">正常处理</td>
								</c:when>
								<c:when test="${vo1.caseStatus eq 'C'}">
								<td width="9%">注销</td>
								</c:when>
								<c:when test="${vo1.caseStatus eq 'E'}">
								<td width="9%">完成</td>
								</c:when>
								</c:choose>
								<td width="9%">自赔</td>
								<td width="10%"><input  class="btn btn-ml-5" onclick="seePhoto('${vo1.flowId}')" type="button"  value="..."/></td>
							</tr>
						</c:if>
				
				</c:forEach>

			</table>
			<!--table   结束-->



		</div>
	</div>
</body>
</html>