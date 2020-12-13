<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>短信模板维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
   <input type="hidden" name="Index" id="Index" value="${Index}"/>
    <input type="hidden" name="flag" id="flag" value="${flag}"/>
        <!-- 短信模板维护开始 -->
        <form  id="MsgModelEditForm">
            <div class="table_cont">
			<div class="table_wrap">
				<div class="table_title f14">模板信息</div>
				    <div class="table_cont">
				        <div class="formtable">
					<!-- 隐藏域 -->
						<input type="hidden" name="SysMsgModelVo.id" value="${SysMsgModel.id}"/>
					<!-- 隐藏域 -->
					
					<div class="row cl">
							<label class="form_label col-2">模板代码：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"
									name="SysMsgModelVo.modelCode" value="${SysMsgModel.modelCode}" datatype="*" />
							</div>
							<span class="c-red col-1">*</span> 
							<label class="form_label col-2">模板名称：</label>
							<div class="form_input col-2">
							    <input type="text" class="input-text"
							        name="SysMsgModelVo.modelName" value="${SysMsgModel.modelName }" datatype="*" nullmsg="请写模板名称！"/>
							</div>
							<span class="c-red col-1">*</span>
					</div>
					
					<div class="row cl">
							<label class="form_label col-2">模板类型：</label>
							<div class="form_input col-2">
								<span class="select-box"> 
									<app:codeSelect codeType="MsgModelType" name="SysMsgModelVo.modelType"
											 type="select" clazz="must" value="${SysMsgModel.modelType}"/>
								</span>
							</div>
							<span class="c-red col-1">*</span> 
							<label class="form_label col-2">发送类型：</label>
							<div class="form_input col-2">
								<span class="select-box"> 
									<app:codeSelect codeType="SendType" name="SysMsgModelVo.sendType"
											 type="select" clazz="must" value="${SysMsgModel.sendType}" />
								</span>
							</div>
							<span class="c-red col-1">*</span>
					</div>
					
					<div class="row cl">
							<label class="form_label col-2">发送时间类型：</label>
							<div class="form_input col-2">
								<span class="select-box"> 
									<app:codeSelect codeType="TimeType" name="SysMsgModelVo.timeType"
											 type="select" clazz="must" value="${SysMsgModel.timeType}"/>
								</span>
							</div>
							<span class="c-red col-1">*</span> 
							<label class="form_label col-2">案件类型：</label>
							<div class="form_input col-2">
								<input type="hidden" name="SysMsgModelVo.systemCode" value="1">
								<span class="select-box"> 
									<app:codeSelect codeType="CaseType" name="SysMsgModelVo.caseType"
											 type="select" clazz="must" value="${SysMsgModel.caseType}" />
								</span>
							</div>
							<span class="c-red col-1">*</span>
					</div>
					
					<div class="row cl">
							<label class="form_label col-2">发送节点：</label>
							<div class="form_input col-2">
								<span class="select-box"> 
									<app:codeSelect codeType="SystemNode" name="SysMsgModelVo.systemNode"
											 type="select" clazz="must" value="${SysMsgModel.systemNode}"/>
								</span>
							</div>
							<span class="c-red col-1">*</span> 
							<label class="form_label col-2">模板归属机构：</label>
							<div class="form_input col-2">
								<span class="select-box"> 
									<app:codeSelect codeType="ComCodeLv2" name="SysMsgModelVo.comCode" id="comCode" 
											 type="select" clazz="must" value="${SysMsgModel.comCode}"/>
								</span>
							</div>
					</div>
					
					<div class="row cl">
							<label class="form_label col-2">有效状态：</label>
							<div class="form_input col-2">
								<span class="select-box"> 
									<app:codeSelect codeType="validFlag" name="SysMsgModelVo.validFlag"
											 type="select" clazz="must" value="${SysMsgModel.validFlag}"/>
								</span>
							</div>
							<span class="c-red col-1">*</span> 
					</div>
				</div>
			</div>
			
			<div class="table_title f14">模板内容：</div>
			
			<div class="table_cont">
				    <!-- 模板参数 -->
				    <%@include file="MsgModelEdit_Param.jspf"%>
					<div class="row cl">
					<label class="form_label col-1">模板内容：</label>
					    <div class="form_label col-10">
							    <textarea class="textarea" name="SysMsgModelVo.content" placeholder="编辑短信内容..." datatype="*,*0-500" 
							     nullmsg="请填写0到500位任意字符！" oldtitle="请填写0到500位任意字符！" id="content">${SysMsgModel.content }</textarea>
							</div>
					</div>
					
					
				
			</div>
			</div>
			
			<div class="btn-footer clearfix text-c">
				<a class="btn btn-primary" id="submit" >保存</a>
				<a class="btn btn-primary" onclick="closeLayer()" >关闭</a>
			</div>
		</div>
	</form>
	
	
	<script type="text/javascript">	
	
	$("#submit").click(function(){	//提交表单
		
		$("#MsgModelEditForm").submit();
				
		});
	
	$(function (){
		
		var ajaxEdit = new AjaxEdit($('#MsgModelEditForm'));
		ajaxEdit.targetUrl = "/claimcar/msgModel/saveMsgModel.do"; 
		ajaxEdit.afterSuccess=function(result){
			if(result.status=="200"){
				$("input").attr("disabled", "disabled");
				$("select").attr("disabled", "disabled");
				$("#submit").attr("style","display: none;");
		        $("#content").attr("disabled", "disabled");
			}	
		}; 
		//绑定表单
		ajaxEdit.bindForm();
		
				
	});

	function closeLayer(){
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index);
	}
	//判断是否是查看短信信息，数据只能设为设为只读
	var	index=$("#Index").val();
	$(function() {
		$("#comCode option[value='00030000']").remove();
		$("#comCode option[value='00040000']").remove();
		$("#comCode option[value='00050000']").remove();
	if(index==2){
		$("input").attr("disabled", "disabled");
		$("select").attr("disabled", "disabled");
		$("#submit").attr("style","display: none;");
        $("#content").attr("disabled", "disabled");
	}
	});
	
	function setTemplateContent(field){
		if(index != 2){
			var contentField=document.getElementById("content");
			contentField.value=contentField.value+field.innerText;
		}
	}
</script>
</body>
</html>