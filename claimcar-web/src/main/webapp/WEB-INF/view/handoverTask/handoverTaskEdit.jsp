<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>平级移交处理</title>
</head>
<body>
	<div class="page_wrap">
	<form action="#"  id="form">
	<input type="hidden" id="flowId" name="wfTaskSubmitVo.flowId" value="${prpLWfTaskVo.flowId}" />
	<input type="hidden" id="flowTaskId" name="wfTaskSubmitVo.flowTaskId" value="${prpLWfTaskVo.taskId}" />
	<input type="hidden" id="subNodeCode" name="wfTaskSubmitVo.currentNode" value="${prpLWfTaskVo.subNodeCode}">
	<input type="hidden" id="assignUser"  value="${prpLWfTaskVo.assignUser}">
	<input type="hidden" id="handlerUser"  value="${prpLWfTaskVo.handlerUser}">
	<input type="hidden" id="handlerStatus"  value="${prpLWfTaskVo.handlerStatus}">
	<input type="hidden" id="userCode"  value="${userCode}">
	<input type="hidden" id="gradeId"  value="${gradeId}">
	<input type="hidden" id="registNo" name="registNo" value="${prpLWfTaskVo.registNo}">
	<input type="hidden" id="queryRange" value="${queryRange}">
	<input type="hidden" id="assignComs" name="assignComs" value="${comCode}">
	<div class="table_wrap">
		<div class="table_title f14">移交目标</div>
		<div class="table_cont ">
			<div class="formtable">
				<div class="row  cl">
					<label class="form_label col-3">报案号:</label>
					<div class="form_input col-2">
					<input type="text" class="input-text" style="width: 95%"  readOnly value="${prpLWfTaskVo.registNo}"/>
					</div>
					<label class="form_label col-2">环节:</label>
					<div class="form_input col-2">
					<input type="text" class="input-text" maxlength="50"  readOnly value="<app:codetrans codeType="FlowNode" codeCode="${prpLWfTaskVo.subNodeCode}" />" />
					</div>
				</div>
				<div class="row cl">
					<label class="form_label col-3">目标人员机构:</label>
					<div class="form_input col-2">
					<span class="select-box"> 
					 <app:codeSelect codeType="ComCodeLv2" type="select" id="comCode"
							name="wfTaskSubmitVo.assignCom" lableType="code-name" onchange="comCodeChange()" value="${comCode}"/>
					</span>
					</div>
					<label class="form_label col-2">目标人员:</label>
					<div class="form_input col-2" id="userCodeDiv">
					<span class="select-box"> 
					<input type="hidden" id="userCodeAjax" name="wfTaskSubmitVo.assignUser">
                    </span>
					</div>
				</div>
				<c:if test="${itemName != null}">
				<div class="row cl">
					<label class="form_label col-3">损失方</label>
					<div class="form_input col-2">
					<input type="text" class="input-text" style="width: 95%" readOnly  value="${itemName}" />
					<input type="hidden" id="licenseNo" name="wfTaskSubmitVo.licenseNo" value="${licenseNo}">
					</div>
			    </div>
				</c:if>
				</div>
				
			</div>
		</div>
		
		<!-- 移交原因 开始 -->
		<div class="table_wrap">
		    <div class="table_title f14">移交原因</div>
			<div class="table_cont table_list ">
			<div class="row cl">
				<textarea class="textarea h100" id="description"
						name="handoverTaskReason" datatype="*" nullmsg="请输入移交原因 ！" maxlength="150"
						placeholder="请输入...">${claimTextVo.description}</textarea>
			    <font class="must">*</font>
			</div>
			</div>
		</div>
		<!-- 移交原因 结束 -->
		
		<!-- 底部按钮 -->
		<br /> <br /> <br />
		<!-- 底部按钮 -->
		<div class="btn-footer clearfix text-c">
			<input type="button" class="btn btn-primary" id="handoverButton"
				onClick="taskHandover()"  value="任务移交" />
		</div>
		<br /> <br /> <br /> <br />
		 </form>
	</div>
	<script type="text/javascript">
	$(function(){
		var comCode=$("#comCode").val();
		var gradeId=$("#gradeId").val();
		initGetUserCodeSelect2("userCodeAjax",comCode,1,gradeId);
		$("#s2id_userCodeAjax").click(function(){
			var comCodeSelected=$("#comCode").val();
			if(comCodeSelected == ''){
				layer.alert("请先选中机构");
				return false;
			}
		});
		var queryRange = $("#queryRange").val();
		if(queryRange == "0"){
			$("#comCode").attr("disabled",true);
		}
	});
	 
	//机构变化清空用户选择框
	function comCodeChange(){
		var comCode=$("#comCode").val();
		var gradeId=$("#gradeId").val();
		if(comCode == ''){
			comCode = 'nocomCode';//机构没有英文代码，设置这个能保证目标人员不会查出任何信息
		}
		initGetUserCodeSelect2("userCodeAjax",comCode,1,gradeId);
	}
	
	//任务移交
	function taskHandover(){
		var rules = {};
		var ajaxEdit = new AjaxEdit($('#form'));
		ajaxEdit.beforeCheck = function() {// 校验之前
		};
		ajaxEdit.beforeSubmit = function() {// 提交前补充操作
			var handlerStatus = $("#handlerStatus").val();
			var assignUser = $("#assignUser").val();	
		    if(handlerStatus == '2'){
		    	assignUser = $("#handlerUser").val();
			}
			var comCode = $("#comCode").val();//任务原属人员
		    var handOverUserCode = $("#userCodeAjax").val();//移交人员
		    var userCode = $("#userCode").val();//当前登录人员
		    if(comCode == "" || handOverUserCode ==""){
		    	layer.alert("目标人员机构和目标人员不能为空！");
		    	return false;
		    }
		    if(assignUser == handOverUserCode){
		    	layer.alert("该任务已经在该用户下，不需要移交！");
		    	return false;
		    }
		    if(userCode == handOverUserCode){
		    	layer.alert("不可以分配给当前处理人！");
		    	return false;
		    }
		    //判断查勘自助案件没有接受前不能平级移交start
		    var subNodeCode = $("#subNodeCode").val();
		    var registNo = $("#registNo").val();
		    if(subNodeCode == "Chk" && (!checkAcceptSelfCase(registNo))){
		    	return false;
		    }
		    //end
		};
		ajaxEdit.targetUrl = "/claimcar/handoverTask/handoverTaskSubmit.do";
		ajaxEdit.rules = rules;
		ajaxEdit.afterSuccess = function(data) {// 操作成功后操作
			if(data.data != ""){
				layer.alert(data.data);
			}else{
				layer.alert("操作成功");
				$("#handoverButton").hide();
			    $(":input").prop("readOnly",true);
				$(":input").prop("disabled",true);
			}
		};
		// 绑定表单
		ajaxEdit.bindForm();

		$("#form").submit();
	}
	</script>
</body>
</html>