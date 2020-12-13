<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>核损处理</title>
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
		<!--  车俩损失-->
			<div class="table_wrap">		
				<div class="table_title f14">车辆损失</div>
				<div class="table_cont table_list">
					<table class="table table-border">
						<thead class="text-c">
							<tr>
								<th>序号</th>
								<th>损失方</th>
								<th>车牌号</th>
								<th>定损金额</th>
								<th>核损金额</th>
								<th>残值金额</th>
								<th>折残后核损金额</th>
								<th>定损员</th>
								<th>核损员</th>	
								<th>查看</th>
							</tr>
						</thead>
						<tbody class="text-c">
							<c:forEach var="lossCarMain" items="${lossCarMainVo }" varStatus="status">
								<tr>
									<td>${status.index+1 }</td>
									<td>
										<c:choose>
											<c:when test="${lossCarMain.deflossCarType eq '1'}"> 主车</c:when>
											<c:otherwise>三者车</c:otherwise>
										</c:choose>
									</td>
									<td>${lossCarMain.licenseNo }</td>
									<td>${lossCarMain.sumLossFee }</td>
									<td>${lossCarMain.sumVeriLossFee}</td>
									<td>${lossCarMain.sumVeripRemnant }</td>
									<td>${lossCarMain.sumVeriLossFee}</td>
									<td>${lossCarMain.handlerName }</td>
									<td>${lossCarMain.underWriteName }</td>
									<td>
										<a target="_self"  onclick="vLossView('car','${lossCarMain.id }')"  href="javascript:void(0)">查看</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<!--  车俩损失结束-->
<!-- 财产损失 -->
			
<div class="table_wrap">
	<div class="table_title f14">财产损失</div>
	<div class="table_cont table_list">
		<table class="table table-border">
			<thead class="text-c">
				<tr>
					<th>序号</th>
					<th>损失方</th>
					<th>定损金额</th>
					<th>核损金额</th>
					<th>残值金额</th>
					<th>折残后核损金额</th>
					<th>定损员</th>
					<th>核损员</th>
					<th>查看</th>
				</tr>
			</thead>
				<tbody class="text-c">
					<c:forEach var="lossPropMain" items="${propMainVos }" varStatus="status">
						<tr>
							<td>${status.index+1 }</td>
							<td>${lossPropMain.license }</td>
							<td>${lossPropMain.sumDefloss }</td>
							<td>${lossPropMain.sumVeriLoss}</td>
							<td>${lossPropMain.sumVeriRemnant }</td>
							<td>${lossPropMain.sumVeriLoss }</td>
							<td>${lossPropMain.handlerName }</td>
							<td>${lossPropMain.underWriteName }</td>
							
							<td>
								<a target="_self"  onclick="vLossView('prop','${lossPropMain.id }')"  href="javascript:void(0)">查看</a>
							</td>
						</tr>
					</c:forEach>	
				</tbody>
		</table>
	</div>
</div>
			
			<!-- 财产损失 -->
			<!--  车俩损失-->
	<div class="table_wrap">		
		<div class="table_title f14">人伤损失</div>
		<div class="table_cont table_list">
			<table class="table table-border">
				<thead class="text-c">
					<tr>
						<th>序号</th>
						<th>姓名</th>
						<th>身份证</th>
						<th>核损金额</th>
						<th>定损员</th>
						<th>核损员</th>
						<th>查看</th>
					</tr>
				</thead>
				<tbody class="text-c">
					<c:forEach var="lossPersTrace" items="${prpLDlossPersTraceMainVos}" varStatus="status">
						<tr>
							<td>${status.index+1 }</td>
							<td>${lossPersTrace.prpLDlossPersInjured.personName}</td>
							<td>${lossPersTrace.prpLDlossPersInjured.certiCode}</td>
							<td>${lossPersTrace.sumVeriDefloss}</td>
							<td><app:codetrans codeType="UserCode" codeCode="${lossPersTrace.operatorCode}"/></td>
							<td><app:codetrans codeType="UserCode" codeCode="${lossPersTrace.undwrtCode}"/></td>
							<%-- <td>${lossPersTrace.operatorName }</td>
							<td>${lossPersTrace.underwriteName }</td> --%>
							<td><a target="_self"  onclick="PLChargeView('${lossPersTrace.registNo }')"  href="javascript:void(0)">查看</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
			<!--  车俩损失结束-->
	<script type="text/javascript" src="/claimcar/js/deflossEdit/DeflossEdit.js"></script>
	<script type="text/javascript" src="/claimcar/js/deflossEdit/DeflossEditRow.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	
	</body>

</html>