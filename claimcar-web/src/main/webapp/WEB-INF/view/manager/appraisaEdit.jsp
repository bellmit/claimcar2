<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>伤残鉴定机构编辑</title>
</head>
<body>
	<div class="page_wrap" id="fu">
		
		<div class="table_wrap">
			<div class="table_cont mb-10">
				<form id="form" class="form-horizontal" role="form" method="post">
				
	               <div class="table_title f14">伤残鉴定机构信息</div>
	               <div class="table_wrap">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
						<input type="hidden" name="prpdAppraisaVo.id" value="${prpdAppraisaVo.id}" />
							<label for=" " class="form_label col-2">鉴定机构名称</label>
							<div class="form_input col-4">
								<input id=" " type="text" class="input-text"
									name="prpdAppraisaVo.appraisaName"  value="${prpdAppraisaVo.appraisaName}" datatype="*" nullmsg="请输入鉴定机构名称" style="width:200px;"/>
									<font class="must">*</font>
							</div>
                               
							<label for=" " class="form_label col-2">组织机构代码</label>
							<div class="form_input col-4">
								<input  type="text" class="input-text"
									name="prpdAppraisaVo.appraisaCode" value="${prpdAppraisaVo.appraisaCode}" datatype="*" id="appraisaCode" nullmsg="请输入鉴定机构代码" style="width:200px;" onchange="checkCertifyNo()"/>
									<font class="must">*</font>
							</div>

							
							
                       <br/>
						<div class="row cl accident-over-add">
						<label class="form_label col-2">联系方式</label>
				        <div class="form_input col-4">
					    <input type="text" class="input-text"
						name="prpdAppraisaVo.telephoneNumber" datatype="m|/^0\d{2,3}-\d{7,8}$/"  ignore="ignore"
						value="${prpdAppraisaVo.telephoneNumber}" style="width:200px;">
						</div>
							<label class="form_label col-2">鉴定机构地址</label>
							<div class="form_input col-4 clearfix">
								<div style="display: inline">
									<app:areaSelect targetElmId="areaCode" areaCode="${prpdAppraisaVo.areaCode}" showLevel="2" style="width:100px;" />
									<font class="must">*</font>
								</div>
								<input type="hidden" id="areaCode" name="prpdAppraisaVo.areaCode" value="${prpdAppraisaVo.areaCode}"/>
							</div>
							
							</div>
							<div class="row cl accident-over-add">
							<label for=" " class="form_label col-2">有效标志</label>
							<div class="form_input col-4">
								<span class="select-box"> 
								<app:codeSelect type="select" codeType="validFlag" name="prpdAppraisaVo.validStatus" 
							    value="${prpdAppraisaVo.validStatus}" clazz="must" style="width:200px;"/>
							    <font class="must">*</font>
								</span>
							</div>
						</div>
						</div>
				</div>
						<!--可修品牌     结束-->
        </div>
		</div>
		<div class="table_wrap">
	  <div class="table_title f14">备注</div>
			   <div class="row cl">
				  <div class="form_input col-12">
					<textarea class="textarea" name="prpdAppraisaVo.remark" placeholder="请输入..." datatype="*0-255">${prpdAppraisaVo.remark}</textarea>
				  </div>
			  </div>
		   </div>
		   <br/>
	
		<!--撑开页面  开始  -->
			<div class="row cl">
				<div class="form_input col-6"></div>
			</div>
			<div class="row cl">
				<div class="form_input col-6"></div>
			</div>
			<!-- 结束 -->
						<p>
						<!-- 结束 -->
			<div class="btn-footer clearfix">
				<a class="btn btn-primary fl" style="margin-left: 40%" id="submit">保存</a>
				<a class="btn btn-primary fl ml-5" onclick="closeAlert()">关闭</a>
			</div><br>
			<div class="row cl">
				<div class="form_input col-6"></div>
			</div>

				</form>
			</div>
	</div>
	
	<script type="text/javascript">
	$(function(){
		
	});

  function closeAlert() {
	  var index = parent.layer.getFrameIndex(window.name);//获取窗口索引
	  parent.layer.close(index);// 执行关闭
}
  
//异步请求校验
	$("#appraisaCode").change(function() {
		if ($("#appraisaCode").val() == "") {
			return;
		}
		$.ajax({
			url : "/claimcar/manager/appraisaVerify.do",
			type : "post",
			data : {
				"appraisaCode" : $("#appraisaCode").val(),
				"sign" :'X'
			},
			dataType : "json",
			success : function(result) {
				var $result = result.data;
				if ($result == "1") {
					$("#appraisaCode").removeClass("Validform_error");
					return;
				} else {
					$("#appraisaCode").val("");
					
					$("#appraisaCode").addClass("Validform_error");
					layer.msg("该鉴定机构代码已经存在！");
					return;
				}
			}
		});
	}); 
	
	$("#submit").click(function() {
		
		var areaCode=$("#areaCode_lv2").val();
		if(isBlank(areaCode)){
			layer.msg('请录入鉴定机构的省市区', {
			    time:1500, //20s后自动关闭
			    }); 
			return false;
		}
		/*  if(warmTip()){
			return false;
		}  */
		//发送ajaxEdit
		var ajaxEdit = new AjaxEdit($('#form'));
		ajaxEdit.targetUrl = "/claimcar/manager/saveOrUpdateAppraisa.do";
		ajaxEdit.beforeSubmit = function(){
			if(!checkCertifyNo()){
				return false;
			}
		};
		ajaxEdit.afterSuccess = function(result) {
			layer.alert("保存成功！",function(){
				
				closeAlert();
			});
		};
		//绑定表单
		ajaxEdit.bindForm();
		$("#form").submit();
	});
	
	function checkCertifyNo(){
		 var certifyNo = $("[name='prpdAppraisaVo.appraisaCode']").val();
		 var regs = new RegExp("^(.{9}|.{18})$");
		 //证件类型为组织机构代码时，证件号码只能是8位或者18位
		 if(!regs.test(certifyNo)){
			 layer.alert("请录入正确的9位或18位组织机构代码!"); 
			 return false;
		 }
		 return true;
	 }
  </script>
	</body>
	</html>
	
