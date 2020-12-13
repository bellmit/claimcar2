<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>调度注销</title>
</head>
<body>
<form action="#" id="editform" >
	<div class="page_wrap">
		<!-- 查勘任务改派  开始 -->
		<div class="table_wrap">
			<div class="table_title f14">请选择需要注销的定损调度任务</div>
			<div class="table_cont ">
				<table class="table table-border table-bordered table-hover"
					cellpadding="0" cellspacing="0">
					<thead>
						<tr class="text-c">
							<th style="width: 6%">选择</th>
							<th style="width: 16%">调度任务号</th>
							<th style="width: 26%">损失项</th>
							<th style="width: 16%">调度状态</th>
							<th style="width: 16%">机构/组别</th>
							<th style="width: 16%">查勘人员</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${scheduleDefLossVos}" varStatus="index" var="scheduleDefLossVo">
						    <c:if test="${!(scheduleDefLossVo.serialNo == 1 && scheduleDefLossVo.deflossType == '1')}">
							<tr class="text-c">
								<td><input name="dLossCheck" type="checkbox" id="${scheduleDefLossVo.flag}" /></td>
								<td>${scheduleDefLossVo.id}</td>
								<td>
									<c:choose>
								    	<c:when test="${scheduleDefLossVo.deflossType eq '1'}">
								    		车辆定损：${scheduleDefLossVo.itemsName}（${scheduleDefLossVo.itemsContent}）
								    	</c:when>
								    	<c:otherwise>
								    		财产定损：${scheduleDefLossVo.itemsContent}（${scheduleDefLossVo.licenseNo}）
								    	</c:otherwise>
									</c:choose>
								</td>
								<td><app:codetrans codeType="ScheduleStatus" codeCode="${scheduleDefLossVo.scheduleStatus}"/></td>
								<td><app:codetrans codeType="ComCode" codeCode="${scheduleDefLossVo.scheduledComcode}"/></td>
								<td><app:codetrans codeType="UserCode" codeCode="${scheduleDefLossVo.scheduledUsercode}"/></td>
							</tr>
							</c:if>
						</c:forEach>
						<!-- <tr class="text-c">
							<td><input type="checkbox" /></td>
							<td>6000435</td>
							<td><input type="text" class="input-text" value="6000435" /></td>
							<td><input type="text" class="input-text" value="6000435" /></td>
							<td><input type="text" class="input-text" value="6000435" /></td>
							<td>李四</td>
						</tr> -->
					</tbody>
				</table>
				<!-- sss -->
			</div>
		</div>
		
		<div class="table_wrap">
			<div class="table_cont ">
				<div class="formtable">
					<div class="row  cl">
						<label class="form_label col-2">注销原因</label>
						<div class="form_input col-3">
							<span class="select-box"> 
							<app:codeSelect id="cancelCode" name="prpLScheduleTaskVo.cancelOrReassignCode" codeType="ScheduleCancel" type="select" clazz="must" />
							</span>
						</div>
						<label class="form_label col-2">备注</label>
						<div class="form_input col-3">
							<input type="text" id="cancelRemark" maxlength="20" name="prpLScheduleTaskVo.cancelOrReassignContent" class="input-text" value="" />
						</div>
					</div>
					<!--  -->
				</div>
			</div>
		</div>
		<br />
		
		<input id="registNo" value="${scheduleDefLossVos[0].registNo}" type="hidden" />
		<input id="workStatus" type="hidden" name="workStatus" value="${workStatus}" />
		<!-- 工作流参数隐藏域开始 -->
		<input id="flowId" type="hidden" name="submitVo.flowId" value="${flowId}" />
		<input id="flowTaskId" type="hidden" name="submitVo.flowTaskId" value="${flowTaskId}" />
		<!-- 工作流参数隐藏域开始 -->
		
		<!--  -->
		<div class="line" color="red"></div>
		<!-- 底部按钮 -->
		<div class="btn-footer clearfix text-c">
				<c:if test="${workStatus ne '9'}">
					<input class="btn btn-primary btn-cancelBtn" type="button" value="注销"></input>
				</c:if>
				<c:if test="${dlCarFlags eq '0'}">
					<input class="btn btn-primary" onclick="goBack()" type="button" value="返回"></input>
				</c:if>
			</div>
		<br /> <br /> <br /> <br />
	</div>
</form>
	<script type="text/javascript">
		$(function(){
			
			var workStatus = $("#workStatus").val();
			if(workStatus == "9"){
				$("input").attr("disabled", "disabled");
			}
			
			$("input.btn-cancelBtn").click(function() {
				layer.load();
				if ($("input[name='dLossCheck']:checked").length == 0) {
					layer.alert("请选择需要注销的定损任务。");
					layer.closeAll('loading');
					return false;
				}
				
				var dLossIds = "";
				
				$("input[name='dLossCheck']:checked").each(function() {
					if (dLossIds.length > 0) {
						dLossIds = dLossIds + "," + $(this).attr("id");
					} else {
						dLossIds = $(this).attr("id");
					}
				});
				
				var cancelCode = $("#cancelCode").val();
				var cancelRemark = $("#cancelRemark").val();				
				var params = {
					"dLossIds"		:	dLossIds,
					"flowId"		:	$("#flowId").val(),
					"flowTaskId"	:	$("#flowTaskId").val(),
					"registNo"		:	$("#registNo").val(),
					"cancelCode"    :   cancelCode,
					"cancelRemark"  :   cancelRemark
				};
				
				var url = "/claimcar/schedule/logoutSubmit.ajax";
				
				/* $.post(url, params, function(result) {
					//alert(result);
					if (result == '1') {
						layer.alert("注销成功");
						location.href = "/claimcar/schedule/schLogout?registNo=" + $("#registNo").val() + "&flowId=" + $("#flowId").val() + "&taskId=" + $("#flowTaskId").val();
					} else if (result == '2') {
						layer.alert("注销失败，部分任务可能已被接收");
					} else if (result == '9') {
						layer.alert("数据处理异常，请刷新后重试");
					}
				}); */
				$.ajax({
					url : url,
					type : "post",
					data : params,
					async : false,
					success : function(result) {
						if (result == '1') {
							layer.confirm("注销成功", {
								btn: ['确定'] //按钮
							}, function(index){
								location.reload();
								//layer.load();
							});
							//location.href = "/claimcar/schedule/schLogout?registNo=" + $("#registNo").val() + "&flowId=" + $("#flowId").val() + "&taskId=" + $("#flowTaskId").val();
						} else if (result == '2') {
							layer.alert("注销失败，部分任务可能已被接收");
						}else if (result == '3') {
							layer.alert("如果车辆有人伤或者财产损失则不能注销");
						} else if (result == '4') {
							layer.alert("生成理算任务不能注销");
						} else if (result == '5') {
							layer.alert("已预付的任务不能注销");
						}else if (result == '9') {
							layer.alert("数据处理异常，请刷新后重试");
						} else if(result == '7'){
							layer.alert("除当前任务之外，没有其他可操作任务，禁止注销！");
						}
					},error : function() {
						layer.msg("Ajax提交错误");
					},
					complete : function(){
						layer.closeAll('loading');
					}
				});
				
				/* var cancelReason = "";
				$("#cancelOut select").each(function() {
					cancelReason = $(this).val();
				});
				if (cancelReason == "") {
					layer.msg("请选择注销原因。");
					return false;
				}
				$("#cancelOut select").each(function() {
					$(this).attr("disabled",true);
				});
				var $divIn = $("#cancelIn");
				$(this).removeClass("btn-primary");
				$(this).removeClass("btn");
				$(this).addClass("hide");
				var registNo = $("#registNo").val();
				
				
				var params = {
					"cancelReason" : cancelReason,
					"registNo" : registNo,
				};
				var url = "/claimcar/regist/reportCancel.ajax";
				$.post(url, params, function(result) {
					$divIn.append(result);
				}); */
			});
			
		});
		function goBack(){
			location.href = "/claimcar/schedule/preScheduleEdit.do?flowTaskId="+$("#flowTaskId").val() +"&registNo=" + $("#registNo").val();
		}
	</script>
</body>
</html>