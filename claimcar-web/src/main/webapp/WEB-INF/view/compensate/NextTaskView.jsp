<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
	<body>
		<div>
			<div class="table_wrap">
				<div class="table_title f14">提交下一节点</div>
				<div class="table_cont ">
					<form  id="nodeform" action="/claimcar/compensate/submitCompensateEdit.do" role="form" method="post"  name="fm" >
					<input type="hidden" name="nextVo.taskInKey" value="${nextVo.taskInKey}" />
					<input type="hidden" name="nextVo.registNo" id="registNo" value="${registNo}" />
					<input type="hidden" id="compensateNo" value="${compensateNo}" />
					<%-- <input type="hidden" name="nextVo.assignUser" value="${nextVo.assignUser}" /> --%>
					<input type="hidden" name="nextVo.currentNode" value="${nextVo.currentNode}" />
					<input type="hidden" name="nextVo.flowTaskId" value="${nextVo.flowTaskId}" />
					<input type="hidden" name="nextVo.flowId" value="${nextVo.flowId}" />
					<input type="hidden" name="nextVo.nextNode" value="${nextVo.nextNode}" />
					<input type="hidden" name="nextVo.taskInUser" value="${nextVo.taskInUser}" />
					<input type="hidden" name="nextVo.comCode" value="${nextVo.comCode}" />
					<input type="hidden" name="nextVo.submitLevel" value="${nextVo.submitLevel}" />
					<input type="hidden" name="autoVerifyFlag" value="${autoVerifyFlag}" />
					<table class="table table-striped table-border table-hover">
					 		<thead>
					 			<tr class="text-c">
					 				<th>当前环节</th>
					 				<th>下个环节</th>
					 				<!-- <th>人员代码</th> -->
					 			</tr>
					 		</thead>	
					 		<tbody>
			 					<tr class="text-c">
			 						<td>理算</td>
					 				<td>
					 				<c:if test="${autoVerifyFlag eq 'true' }">
					 					自动核赔结案
					 				</c:if>
					 				<c:if test="${autoVerifyFlag ne 'true' }">
					 					${nextNodeMap }
					 					<app:codeSelect codeType="nextNode" style="width:40%" type="select" name="nextVo.finalNextNode" clazz="must" dataSource ="${nextNodeMap }"/>
					 				</c:if>
					 				</td>
					 				<%-- <td><app:codetrans codeType="UserCode" codeCode="${nextVo.assignUser }"/></td> --%>
					 			</tr>
					 		</tbody>
					 		
					 </table>
					<div class="text-c">
						<br/>
						<input class="btn btn-primary  mr-20" id="submitNext" type="button" value="任务提交">
						<input class="btn btn-primary" id="return" type="button" value="取消任务提交">
					</div>
				</form>
				</div>
			</div>
		</div>
		
		<script type="text/javascript" >
		$(function(){
				$("#submitNext").click(function(){
					var index='3';
					var registNo=$("#registNo").val();
					params1={"registNo":registNo};
					$.ajax({
						url : "/claimcar/compensate/validCaseState.ajax", // 后台校验
					    type : 'post', // 数据发送方式
					    dataType : 'json', // 接受数据格式
					    data : params1, // 要传递的数据
					    async : false,
					   success : function(jsonData) {// 回调方法，可单独定义
						   var sign="";
						var result = eval(jsonData);
					     if (result.status == "200" ){
						    sign =result.data;
							if(sign=='0'){
								index='0';
							}
							if(sign=='1'){
								index='1';
							}
					     }
					   }
							
					 });
					
					if(index=='0'){
						alert("快赔案件核损金额客户尚未确认！");
						return false;
					}else if(index=='1'){
						alert("快赔案件客户不同意核损金额，已转线下处理，请核实！");
						return false;
					}
						$("#submitNext").prop("disabled",true);
						//disabledSec($("#submitNext"),20);
						$("#nodeform").submit();
						layer.load(0, {shade : [ 0.8, '#393D49' ]});//防止重复提交 
				});
				
				$("#return").click(function(){
					parent.location.reload();
					var index = parent.layer.getFrameIndex(window.name); 
					parent.layer.close(index);	
				});
		});
		
	
		</script>
	</body>
</html>