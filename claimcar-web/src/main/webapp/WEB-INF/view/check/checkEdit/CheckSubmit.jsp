<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE HTML>
<html>
<head>
<title>查勘提交</title>
</head>
<body>
	<form id="chMitForm" role="form" method="post" name="fm">
	<div class="table_wrap" id="chMitDiv">
	<input type="hidden" name="sub_checkId" value="${checkId}">
	<input type="hidden" name="sub_flowTaskId" value="">
	<input type="hidden" name="sub_flowId" value="">
	<input type="hidden" name="sub_LossFees" value="${lossFees}">
	<input type="hidden" name="sub_majorFlag" value="${majorFlag}">
	<%-- <input type="hidden" id="checkAreaCode" value="${checkAreaCode}">
	<input type="hidden" id="checkAddress" value="${checkAddress}">
	<input type="hidden" id="registNo" value="${registNo}">
	<input type="hidden" id="lngXlatY" value="${lngXlatY}"> --%>
	<div class="table_cont table_list">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<%-- <c:if test="${lossFees>=50000}">处理人</c:if> --%>
					<!-- <th style="width: 15%"></th>
					<th style="width: 15%"></th>
					<th style="width: 20%"></th>
					<th style="width: 20%"></th>
					<th style="width: 30%"></th> -->
					
					<th style="width: 10%">当前节点</th>
					<th style="width: 15%">损失项</th>
					<th style="width: 20%">损失名称</th>
					<th style="width: 15%">提交到(下一节点)</th>
					<!-- <th style="width: 15%">处理岗</th> -->
					<th style="width: 20%">指定处理人</th>
					<th style="width: 15%">状态</th>
				</tr>
			</thead>
			<tbody id="propLossTbody">
				<c:if test="${(lossFees>=50000) || (majorFlag eq '1')}">
					<tr class="text-c">
						<td>查勘</td>
						<td></td>
						<td></td>
						<td>大案审核一级</td>
						<td class="text-r">
							<%-- <app:codeSelect codeType="UserCode" dataSource="${userMap}" name="user_chkBig"
								type="select" clazz="must" value="" /> --%>
							<%-- <select id="" class="select-box" style="width: 80%">
				 				<option>大案审核处理人</option>
				 				<option value="${defLossList[0].showMap}">${defLossList[0].showMap}</option>
				 			</select> --%>
						</td>
						<td>未发起大案审核</td>
					</tr>
				</c:if>
				
				<input type="hidden" id=checkLossMap value="${checkLossMap}">
				<c:forEach var="defLoss" items="${defLossList}" varStatus="status">
					<tr class="text-c">
					<c:set var="pe_Idx" value="${status.index}" />
					<input type="hidden" name="defLoss[${pe_Idx}].deflossType" value="${defLoss.deflossType}">
					<input type="hidden" name="defLoss[${pe_Idx}].serialNo" value="${defLoss.serialNo}">
					<input type="hidden" name="defLoss[${pe_Idx}].taskFlag" value="${defLoss.taskFlag}">
						<td>查勘</td>
						<td>${defLoss.itemsName}</td>
						<td>${defLoss.itemsContent}</td>
						<td>
							<c:if test="${defLoss.deflossType eq '1'}">车辆定损</c:if>
							<c:if test="${defLoss.deflossType eq '2'}">财产定损</c:if>
						</td>
						<c:choose>
							<c:when test="${defLoss.taskFlag eq '1'}">
								<td>
									<app:codetrans codeType="UserCode" codeCode="${defLoss.scheduledUsercode}"/>
									<input type="hidden" name="defLoss[${pe_Idx}].scheduledUsercode" value="${defLoss.scheduledUsercode}">
								</td>
							</c:when>
							<c:otherwise>
								<td class="text-r">
									<app:codeSelect name="defLoss[${pe_Idx}].scheduledUsercode" codeType="UserCode" 
									dataSource="${defLoss.showMap}"  type="select" clazz="must" value=""/>
								</td>
							</c:otherwise>
						</c:choose>
						<%-- <td class="text-r">
				 			<app:codeSelect name="defLoss.scheduledUsercode" codeType="ItemType" dataSource="${defLoss.showMap}"  type="select" clazz="must" value="" /> 
				 			${defLoss.scheduledUsercode}
						</td> --%>
						<td>
							<!-- <input type="button" class="btn btn-secondary" href="javascript:;" onclick="openCheckMap();" value="定损人员"> -->
							<c:choose>
							<c:when test="${defLoss.taskFlag eq '1'}">
								已生成定损任务
							</c:when>
							<c:otherwise>
								未生成定损任务
							</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br/><br/><br/>
		<input type="button" class="btn btn-primary text-c" style="margin-left: 40%;" onclick="checkSubmit()" value="提交">
		<input type="button" class="btn btn-disabled btn-kk" onclick="checkCancel()" value="返回">
		<!-- <input type="button" class="btn btn-primary" onclick="openLossTaskWin('DLCar','1521')" value="返回" /> -->
		<br/><br/>
	</div>
</div>
</form>
<script type="text/javascript" src="/claimcar/js/checkEdit/check.js"></script>
<script type="text/javascript" src="/claimcar/js/quickclaim/quickclaim.js"></script>
	<script type="text/javascript">
		function checkCancel(){
			var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			//window.parent.close(index); // 执行关闭
			parent.layer.close(index);
		}
		function openCheckMap() {

			/* var url = "/claimcar/mobilcheck/manualScheduleBefore.ajax";
			
			var registNo = $("#registNo").val();
			var checkAreaCode = $("#checkAreaCode").val();
			var checkAddress = $("#checkAddress").val();
			var lngXlatY = $("#lngXlatY").val();
			
			var params = {
					"registNo" 		 	: registNo,
					"checkAreaCode"	 	: checkAreaCode,
					"checkAddress"	    : checkAddress,
					"lngXlatY"          : lngXlatY
				};
			
			$.ajax({
				url : url,
				type : "post",
				data : params,
				async : false,
				success : function(htmlData) {
					var url = htmlData.data;
					layer.open({
					    type: 2,
					    area: ['90%', '80%'],
					    fix: false, //不固定
					    maxmin: true,
					    content: url
					});
				}
			}); */
		}

		function getMapInfo(returnData) {
			alert(returnData.lossUserName);
		}
	</script>
</body>
</html>