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
<div class="top_btn">
     <a class="btn btn-primary" 
				onclick="AdjustersPrintT('${registNo }','${compensateNo }')">赔款理算书打印</a>
				
				<c:if test="${!empty flowTaskId }">
				<a class="btn btn-primary"
onclick="openWinCom1('理算(商业)处理','/claimcar/compensate/compensateEdit.do?flowTaskId='+${flowTaskId } )" >进入商业险理算</a>
				</c:if>
	
</div>  <!-- +'&flowTaskId='+ ${flowTaskId } +'&taskInKey='+ ${taskInkey } +'&registNo='+ ${registNo } -->
<br/>
<br/>	
			<div class="table_wrap">
            	<div class="table_title f14">下一个节点任务</div>
				<div class="table_cont ">
					 <table class="table table-border table-hover">
					 		<thead>
					 			<tr>
					 				<th>序号</th>
					 				<th>任务名称</th>
					 				<th>业务号</th>
					 				<th>标的车牌号</th>
					 				<th>提交人</th>
					 				<th>提交时间</th>
					 				<th>指定处理人</th>
					 			</tr>
					 		</thead>
					 		<tbody>
					 				 <tr>
					 					<td> ${status.index+1}</td>
					 					<td> ${wfTaskVo.taskName}</td>
					 					<td> ${wfTaskVo.registNo}
					 						<input type="hidden" id="taskInKey" value="${wfTaskVo.taskInKey }">
					 						<input type="hidden" id="nodeCode" value="${wfTaskVo.subNodeCode }">
					 						<input type="hidden" id="flowTaskId" value="${wfTaskVo.taskId }">
					 						
					 					</td>
					 					<td> ${itemName}</td>
					 					<td> <app:codetrans codeType="UserCode" codeCode="${wfTaskVo.taskInUser}"/></td>
					 					<td><fmt:formatDate value="${wfTaskVo.taskInTime}" pattern="yyyy-MM-dd HH:mm"/></td>
					 					<td><app:codetrans codeType="UserCode" codeCode="${wfTaskVo.assignUser }"/> </td>
					 				 </tr>
					 		</tbody>
					 	<%-- 	<a class="btn btn-primary"
				onclick="AdjustersPrint('${prpLCompensate.registNo}','${prpLCompensate.compensateNo}')">赔款理算书打印</a> --%>
					 </table>

					 <div class="text-c">
						<br/>
						<input class="btn btn-primary" id="return" onClick="returnWin()" type="submit" value="返回">
					</div>
				</div>
			</div>
     <script type="text/javascript" src="/claimcar/js/compensate/compensateEdit.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
			<script type="text/javascript">
				function openWin(){
				 	var nodeCode =$("#nodeCode").val();
					var goUrl= "";
					var param =$("#flowTaskId").val();
					var title="";
					goUrl ="/claimcar/verifyClaim/verifyClaimEdit.do";
					title ="核赔任务处理";
					
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
