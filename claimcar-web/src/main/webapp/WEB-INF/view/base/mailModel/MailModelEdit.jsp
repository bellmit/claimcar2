<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>邮件模板维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
   <input type="hidden" name="Index" id="Index" value="${Index}"/>
    <input type="hidden" name="flag" id="flag" value="${flag}"/>
        <!-- 短信模板维护开始 -->
        <form  id="MailModelEditForm">
            <div class="table_cont">
			<div class="table_wrap">
				<div class="table_title f14">模板信息</div>
				    <div class="table_cont">
				        <div class="formtable">
					<!-- 隐藏域 -->
						<input type="hidden" name="PrpLMailModelVo.id" value="${prpLMailModel.id}"/>
						<input type="hidden" id="isParentCom" value="${isParentCom}"/>
					<!-- 隐藏域 -->
					
					<div class="row cl">
							<label class="form_label col-2">模板代码：</label>
							<div class="form_input col-3">
								<input type="text" class="input-text"
									name="PrpLMailModelVo.modelCode" value="${prpLMailModel.modelCode}" datatype="*" maxlength="20" nullmsg="请输入模板代码！" />
								<font class="must">*</font>
							</div>
							<label class="form_label col-2">模板名称：</label>
							<div class="form_input col-3">
							    <input type="text" class="input-text"
							        name="PrpLMailModelVo.modelName" value="${prpLMailModel.modelName }" datatype="*" maxlength="50" nullmsg="请写模板名称！"/>
								<font class="must">*</font>
							</div>
					</div>
					
					<div class="row cl">
							<label class="form_label col-2">模板类型：</label>
							<div class="form_input col-3">
								<span class="select-box"> 
									<app:codeSelect codeType="MailModelType" name="PrpLMailModelVo.modelType"
											 type="select" clazz="must" value="${prpLMailModel.modelType}" />
								<font class="must">*</font>
								</span>
							</div>
							<label class="form_label col-2">模板归属机构：</label>
							<div class="form_input col-3">
								<span class="select-box"> 
								<c:choose>
									<c:when test="${isParentCom == '1'}">	
										<app:codeSelect codeType="ComCodeLv2" name="PrpLMailModelVo.comCode" 
											 type="select" clazz="must" value="${prpLMailModel.comCode}"/>
									</c:when>
									<c:otherwise>
										<app:codeSelect codeType="ComCodeLv2" name="PrpLMailModelVo.comCode" id="comCode" 
											 type="select" clazz="must" value="${userComCode}" />
									</c:otherwise>
								</c:choose>
								<font class="must">*</font>
								</span>
							</div>
					</div>
					<div class="row cl">
							<label class="form_label col-2">发送节点：</label>
							<div class="form_input col-3">
								<span class="select-box"> 
									<app:codeSelect codeType="MailNode" name="PrpLMailModelVo.systemNode"
											 type="select" clazz="must" value="${prpLMailModel.systemNode}"/>
								<font class="must">*</font>
								</span>
							</div>
							<label class="form_label col-2">有效状态：</label>
							<div class="form_input col-3">
								<span class="select-box"> 
									<app:codeSelect codeType="validFlag" name="PrpLMailModelVo.validFlag"
											 type="select" clazz="must" value="${prpLMailModel.validFlag}"/>
								<font class="must">*</font>
								</span>
							</div>
					</div>
					
				</div>
			</div>
			
			<div class="table_title f14">模板内容：</div>
			
			<div class="table_cont">
				    <!-- 模板参数 -->
				    <%@include file="MailModelEdit_Param.jspf"%>
					<div class="row cl">
					<label class="form_label col-1">模板内容：</label>
					    <div class="form_label col-10">
							    <textarea class="textarea" name="PrpLMailModelVo.content" placeholder="编辑短信内容..." datatype="*,*0-500" 
							     nullmsg="请填写0到500位任意字符！" oldtitle="请填写0到500位任意字符！" id="content">${prpLMailModel.content }</textarea>
								<font class="must">*</font>
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
		var validFlag = $("select[name='PrpLMailModelVo.validFlag']").val();
		if(validFlag == '0'){
			$("#MailModelEditForm").submit();
		}else{
			var params = {
					"modelType" :$("select[name='PrpLMailModelVo.modelType']").val(),
				    "comCode":$("select[name='PrpLMailModelVo.comCode']").val(), 
				    "id":$("input[name='PrpLMailModelVo.id']").val()
		          };
			$.ajax({
					url : "/claimcar/mailModel/isVaildMailModel.ajax", // 后台校验
					type : 'post', // 数据发送方式
					dataType : 'json', // 接受数据格式
					data : params, // 要传递的数据
					async : false,
					success : function(result) {// 回调方法，可单独定义
						if(result.data == "1"){
							layer.alert("同一模板归属机构下只能存在有效的“车物” 和 “人伤”邮件模板各一条");
						} else {
	
							$("#MailModelEditForm").submit();
							
						}
					}
					
			});
			
		}
	});
	$(function (){
		$("select[name='PrpLMailModelVo.modelType']").change(function(){
			var value = $(this).val();
			$("select[name='PrpLMailModelVo.systemNode']").val(value);
		});
		$("select[name='PrpLMailModelVo.systemNode']").change(function(){
			var value = $(this).val();
			$("select[name='PrpLMailModelVo.modelType']").val(value);
		});
		var ajaxEdit = new AjaxEdit($('#MailModelEditForm'));
		ajaxEdit.targetUrl = "/claimcar/mailModel/saveMailModel.do"; 
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
	//判断是否是查看模板信息，数据只能设为只读
	var	index=$("#Index").val();
	var	isParentCom=$("#isParentCom").val();
	$(function() {
		$("#comCode option[value='00030000']").remove();
		$("#comCode option[value='00040000']").remove();
		$("#comCode option[value='00050000']").remove();
	if(index==2){
		$("input").attr("disabled", "disabled");
		$("select").attr("disabled", "disabled");
		$("#submit").attr("style","display: none;");
        $("#content").attr("disabled", "disabled");
	}else if(isParentCom==0){
		$("#comCode").attr("disabled", "disabled");
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