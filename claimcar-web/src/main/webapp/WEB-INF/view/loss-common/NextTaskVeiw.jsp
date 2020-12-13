<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>下一个节点的任务</title>
</head>
<body>
			<div class="table_wrap">
            	<div class="table_title f14">下一个节点任务</div>
				<div class="table_cont ">
					 <table class="table table-border table-hover">
					 	<input type="hidden" id="registNo" value="${registNo }">
					 		<thead>
					 			<tr>
					 				<th>序号</th>
					 				<th>任务名称</th>
					 				<th>报案号</th>
					 				<th>标的名称</th>
					 				<th>提交人</th>
					 				<th>提交时间</th>
					 				<th>指定处理人</th>
					 				<th>处理</th>
					 			</tr>
					 		</thead>
					 		<tbody>
					 			<c:forEach var="wfTaskVo" items="${wfTaskVoList}" varStatus="status">
					 				 <tr>
					 					<td> ${status.index+1}</td>
					 					<td> ${wfTaskVo.taskName}</td>
					 					<td> ${wfTaskVo.registNo}
					 						<input type="hidden" id="taskInKey_${status.index }" value="${wfTaskVo.taskInKey }">
					 						<input type="hidden" id="nodeCode_${status.index }" value="${wfTaskVo.subNodeCode }">
					 						<input type="hidden" id="flowTaskId_${status.index }" value="${wfTaskVo.taskId }">
					 						<input type="hidden" id="handlerIdKey_${status.index }" value="${wfTaskVo.handlerIdKey }">
					 					</td>
					 					<td> ${wfTaskVo.itemName}</td>
					 					<td> <app:codetrans codeType="UserCode" codeCode="${userCode}"/>
					 					</td>
					 					<td><fmt:formatDate value="${wfTaskVo.taskInTime}" pattern="yyyy-MM-dd HH:mm"/></td>
					 					<td> <app:codetrans codeType="UserCode" codeCode="${wfTaskVo.assignUser}"/></td>
					 					<td>
						 				 <c:if test="${wfTaskVo.quickFlag eq '1'}"> <!--  建议加入一个变量 判定是否有权限或者下一个节点是END -->
						 						<a target="_blank"  onClick='openWin(${status.index })'  >处理任务</a>
						 					</c:if>
					 					</td>
					 				 </tr>
								</c:forEach>
					 		</tbody>
					 </table>
					 <div class="text-c">
						<br/>
						<input class="btn btn-primary" id="return" onClick="returnWin()" type="submit" value="返回">
					</div>
				</div>
			</div>
			<script type="text/javascript">
				function openWin(index){
				 	var nodeCode =$("#nodeCode_"+index).val();
					var goUrl= "";
					var param =$("#flowTaskId_"+index).val();
					var title="";
					if(nodeCode=="DLCar" || nodeCode=="DLChk" || nodeCode=="DLCarMod" || nodeCode=="DLCarAdd"){//定损和复检
						goUrl ="/claimcar/defloss/preAddDefloss.do";
						title ="车辆定损任务处理";
						if(nodeCode=="DLChk"){
							title ="复检任务处理";
						}
					}else if(nodeCode.indexOf("VPCar")>=0){//核价
						goUrl ="/claimcar/defloss/preAddVerifyPrice.do";
						title ="核价任务处理";
					}else if(nodeCode.indexOf("VLCar")>=0){//核损
						goUrl ="/claimcar/defloss/preAddVerifyLoss.do";
						title ="核损任务处理";
					}else if(nodeCode=="DLProp"){//定损
						goUrl ="/claimcar/proploss/initPropCertainLoss.do";
						title ="财产定损任务处理";
					}else if(nodeCode.indexOf("VLProp")>=0){//财产核损
						goUrl ="/claimcar/proploss/initPropVerifyLoss.do";
						title ="核损任务处理";
					}else if(nodeCode == "ChkRe"){//复勘处理
						goUrl ="/claimcar/check/initCheck.do";
						title ="复勘处理";
					}else if(nodeCode=="RecLossCar" || nodeCode == "RecLossProp"){//损余回收
						goUrl ="/claimcar/recLoss/init.do";
						title ="损余回收";
						
						var registNo = $("#registNo").val();
						var handlerIdKey = $("#handlerIdKey_"+index).val();
						param = param +"&registNo="+registNo+"&flowId=RecLoss&handlerIdKey="+handlerIdKey;
					}
					goUrl= goUrl+"?flowTaskId="+param;
					
					parent.openTaskEditWin(title,goUrl);
				}
				
				function returnWin(){
					parent.location.reload();
					var index = parent.layer.getFrameIndex(window.name); 
					parent.layer.close(index);	
				}
			</script>
</body>
</html>
