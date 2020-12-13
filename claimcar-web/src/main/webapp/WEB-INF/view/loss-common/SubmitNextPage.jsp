<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
	<body>
		<div>
			<div class="table_wrap">
				<div class="table_title f14">提交下一节点</div>
				<div class="table_cont ">
					<form  id="nodeform" action="/claimcar/defloss/submitNextNode.do" role="form" method="post"  name="fm" >
					<input type="hidden" name="nextVo.taskInKey" value="${nextVo.taskInKey}" />
					<input type="hidden" name="nextVo.registNo" value="${nextVo.registNo}" />
					<input type="hidden" name="nextVo.currentNode" value="${nextVo.currentNode}" />
					<input type="hidden" name="nextVo.flowTaskId" value="${nextVo.flowTaskId}" />
					<input type="hidden" name="nextVo.taskInUser" value="${nextVo.taskInUser}" />
					<input type="hidden" name="nextVo.comCode" value="${nextVo.comCode}" />
					<input type="hidden" name="nextVo.autoPriceFlag" value="${nextVo.autoPriceFlag}" />
					<input type="hidden" name="nextVo.autoLossFlag" value="${nextVo.autoLossFlag}" />
					<input type="hidden" name="nextVo.endFlag" value="${nextVo.endFlag}" />
					<input type="hidden" name="nextVo.maxLevel" value="${nextVo.maxLevel}" />
					<input type="hidden" name="nextVo.verifyLevel" value="${nextVo.verifyLevel}" />
					<input type="hidden" name="nextVo.assignUser" value="${nextVo.assignUser}" />
					<input type="hidden" name="nextVo.assignCom" value="${nextVo.assignCom}" />
					<input type="hidden" name="nextVo.notModPrice" value="${nextVo.notModPrice}" />
					<input type="hidden" id="auditStatus" value="${auditStatus}" />
					
					<c:choose>
						<c:when test="${nextVo.autoPriceFlag =='1' && nextVo.autoLossFlag =='2' && !empty nextNodeMap }">
						 	<app:codeSelect codeType="nextNode" style="width:40%" type="select" name="nextVo.finalNextNode"  clazz="hidden must" dataSource ="${nextNodeMap }"/>
						</c:when>
					</c:choose>
					 <table class="table table-striped table-border table-hover">
					   <c:choose>
					     <c:when test="${auditStatus =='toRecheck' || auditStatus =='toReLoss'}">
					        <thead>
					 			<tr class="text-c">
					 				<th style="width: 30%">当前任务环节</th>
					 				<th style="width: 30%">提交任务路径</th>
					 				<th style="width: 40%">处理人</th>
					 			</tr>
					 		</thead>	
					 		<tbody class="text-c">
					 		        <td style="width: 30%">${nextVo.currentName } </td>
					 		        <td style="width: 30%">
					 					<app:codeSelect codeType="nextNode" style="width:40%" type="select" 
					 					name="nextVo.finalNextNode" clazz="must" dataSource ="${nextNodeMap }"/>
					 				</td>
					 				<td  style="width: 40%">
									<app:codeSelect id="assignUser" codeType="UserCode"  onclick="selectUser()" 
									dataSource="${userMap}"  type="select" clazz="must" value=""/>
								</td>
							</tbody>
					     </c:when>
					     <c:otherwise>
					 		<thead>
					 			<tr class="text-c">
					 				<th>当前任务环节</th>
					 				<th>提交任务路径</th>
					 			</tr>
					 		</thead>	
					 		<tbody>
					 			<c:choose>
					 				
					 				<c:when test="${nextVo.autoPriceFlag =='1' && nextVo.autoLossFlag =='1'}">
					 					<tr class="text-c">
					 					<td>${nextVo.currentName } </td>
					 					<td><select> 
					 								<option>自动核价核损 </option>
					 							</select>
					 					</td>
					 					</tr>
					 				</c:when>
					 				<c:when test="${nextVo.autoPriceFlag =='1'}">
					 					<tr class="text-c">
					 					<td>${nextVo.currentName } </td>
					 					<td><select> 
					 								<option>自动核价 </option>
					 							</select>
					 					</td>
					 					</tr>
					 				</c:when>
					 				<c:when test="${nextVo.autoLossFlag =='1'}">
					 					<tr class="text-c">
					 					<td>${nextVo.currentName } </td>
					 					<td><select> 
					 								<option>自动核损</option>
					 							</select>
					 					</td>
					 					</tr>
					 				</c:when>
					 				<c:when test="${nextVo.endFlag =='1'}">
					 					<tr class="text-c">
					 					<td>${nextVo.currentName } </td>
					 					<td><select> 
					 								<option>审核通过</option>
					 							</select>
					 					</td>
					 					</tr>
					 				</c:when>
					 				<c:when test="${!empty nextNodeMap }">
					 				<tr class="text-c">
					 				<td>${nextVo.currentName }</td>
					 				<td>
					 					<app:codeSelect codeType="nextNode" style="width:40%" type="select" name="nextVo.finalNextNode" clazz="must" dataSource ="${nextNodeMap }"/>
					 				</td>
					 				</tr>
					 				</c:when>
					 			</c:choose>
					 		</tbody>
					 	  </c:otherwise>
					 	</c:choose>
					 		
					 </table>
					<div class="text-c">
						<br/>
						<input class="btn btn-primary" id="submitNext" type="submit" value="提交">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input class="btn btn-primary" id="return"  type="button" value="返回">
					</div>
				</form>
				</div>
			</div>
		</div>
		<script type="text/javascript" >
		$(function(){//Google浏览器 如果用form.submit 按钮名称不能用submit
			    var auditStatus = $("#auditStatus").val();
			    var assignUser = $("input[name='nextVo.assignUser']").val();
		        if(auditStatus == 'toRecheck' || auditStatus == 'toReLoss'){
		        	$("#assignUser").find("option").each(function(){
						if($(this).val()==assignUser){
							$(this).attr("selected","selected");
						}
					});
			        selectUser();
		        }
				$("#submitNext").click(function(){
					//	disabledSec($("#submitNext"),5);
						$("#submitNext").attr("disabled","disabled");
						$("#submitNext").addClass("btn-disabled");
						layer.load(0, {shade : [ 0.8, '#393D49' ]});//防止重复提交 
						$("#nodeform").submit();
						
				});
				
				$("#return").click(function(){
					var index = parent.layer.getFrameIndex(window.name); 
					parent.layer.close(index);	
				});
		});
	    
		function selectUser(){
			var assignUser = $("#assignUser").val();
        	$("input[name='nextVo.assignUser']").val(assignUser);
		}
		
		</script>
	</body>
</html>