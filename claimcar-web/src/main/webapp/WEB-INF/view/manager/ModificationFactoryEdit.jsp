<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>修理厂信息修改</title>
</head>
<body>
<form name="fm" id="form1" class="form-horizontal" role="form">
<div class="fixedmargin page_wrap" style="margin-top:0px">
					<h3 class="panel-title text-c">
						维修厂信息修改
					</h3>
				<div class="table_cont ">
					<div class="formtable">
						<div class="row cl">
							<label class="form_label col-2">归属机构</label>
					 		<div class="form_input col-3">
					 		<input type="hidden" name="prpdRepairFactoryVo.id" value="${prpdRepairFactoryVo.id}" >
					 			<span class="select-box">
									<select class=" select must" style="vertical-align:middle" 
									name="prpdRepairFactoryVo.comCode" value="${prpdRepairFactoryVo.comCode}">
										<option value="00020000">广东分公司</option>
									</select>
								</span>
					 		</div>
					 		<span style="color: red" class="col-1">&nbsp;*</span>
							<label class="form_label col-1">修理厂类型</label>
					 		<div class="form_input col-3">
					 			<span class="select-box">
									<app:codeSelect codeType="RepairFactoryType" name="prpdRepairFactoryVo.factoryType" clazz="must" type="select" />
								</span>
					 		</div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">修理厂代码</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" readonly style="background-color: #ccc" name="prpdRepairFactoryVo.factoryCode" value="${prpdRepairFactoryVo.factoryCode}" >
							</div>			
							<label class="form_label col-2">修理厂名称</label>
							<div class="form_input col-3">
								<input type="text" class="input-text"  name="prpdRepairFactoryVo.factoryName" value="${prpdRepairFactoryVo.factoryName}"  datatype="s1-60">
							</div>	
							<span style="color: red" class="col-1">&nbsp;*</span>
						</div>
						<div class="row cl">
							<label class="form_label col-2">维修厂地址</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpdRepairFactoryVo.address" value="${prpdRepairFactoryVo.address}" datatype="s0-100">
							</div>
							<label class="form_label col-2">维修厂电话</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpdRepairFactoryVo.telNo" value="${prpdRepairFactoryVo.telNo}" >
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-2">联系人</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpdRepairFactoryVo.linker" value="${prpdRepairFactoryVo.linker}" datatype="s0-30">
							</div>
							<label class="form_label col-2">联系人手机</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpdRepairFactoryVo.mobile" value="${prpdRepairFactoryVo.mobile}" datatype="m" ignore="ignore" >
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-2">工时折扣</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpdRepairFactoryVo.hourRate" value="${prpdRepairFactoryVo.hourRate}" 
										datatype="/^(0\.\d{0,4}|1(\.0{0,4})?)$/">
							</div>
							<span style="color: red" class="col-1">&nbsp;*</span>
							<label class="form_label col-1">配件折扣</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpdRepairFactoryVo.compRate" value="${prpdRepairFactoryVo.compRate}" datatype="/^(0\.\d{0,4}|1(\.0{0,4})?)$/" >
							</div>	
							<span style="color: red" class="col-1">&nbsp;*</span>
						</div>	
						<div class="row mb-5 cl">
					 		<label class="form_label col-2">有效无效标志</label>
					 		<div class="form_input col-3">
					 			<span class="select-box">
									<select class=" select must" style="vertical-align:middle" 
									name="prpdRepairFactoryVo.validStatus" >
										<option value="1">有效</option>
										<option value="2">无效</option>
									</select>
								</span>
					 		</div>					 		
					 	</div>
					 	<div class="row cl">
					 		<label class="form_label col-2">备注</label>
							<div class="form_input col-6">
								<textarea  class="textarea" name="prpdRepairFactoryVo.remark" value="${prpdRepairFactoryVo.remark}" placeholder="备注点什么..." datatype="s0-255"></textarea>
							</div>
						</div>
						<!--撑开页面  开始  -->
						<div class="row cl"><div class="form_input col-6"></div></div>
						<div class="row cl"><div class="form_input col-6"></div></div>
						<!-- 结束 -->				
						<div class="btn-footer clearfix">
							<a class="btn btn-primary fl" style="margin-left:40%" id="submit1">保存</a>
							<a class="btn btn-warning f1" id="re" type="reset">重置</a>
							<a class="btn btn-danger fl cl" id="closeM" >关闭</a>
						</div>	
				 </div>
			     </div>
		</div>
</form>
<script type="text/javascript">
$(function(){
	
	var ajaxEdit = new AjaxEdit($('#form1'));
	ajaxEdit.targetUrl="/claimcar/manager/repairFactoryUpData.do"; 
	ajaxEdit.afterSuccess=function(result){
		var $result=result.data;
		if($result=="1"){
			layer.msg('已成功！', {icon: 1});
		}else{
			layer.msg("操作失败！");
			return;
		}
	}; 
	//绑定表单
	ajaxEdit.bindForm();
});

$("#re").click(function(){
	$(".input-text").val("");
	$("select").val("1");
	$(".textarea").val("");
});
$("#submit1").click(function(){
	//发送ajaxEdit
	$("#form1").submit();
});

$("#closeM").click(function(){
	var a = parent.window.no();
	parent.layer.close(a);// 执行关闭
});
</script>
</body>