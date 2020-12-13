<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>修理厂查询</title>
</head>
<body>
<!--维修厂信息显示   开始-->
				<h3 class="panel-title text-c">
						维修厂信息
				</h3>
				<div class="table_cont ">
					<div class="formtable">
						<div class="row cl">
							<label class="form_label col-2">归属机构</label>
					 		<div class="form_input col-3">
					 			<input type="text" class="input-text" disabled="true" value="${prpdRepairFactoryVo.comCode}" >
					 		</div>
					 		<span style="color: red" class="col-1">&nbsp;*</span>
							<label class="form_label col-1">修理厂类型</label>
					 		<div class="form_input col-3">
					 			<input type="text" class="input-text" disabled="true" value="${prpdRepairFactoryVo.factoryType}" >
					 		</div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">修理厂代码</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" disabled="true" value="${prpdRepairFactoryVo.factoryCode}" >
							</div>			
							<label class="form_label col-2">修理厂名称</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" disabled="true" value="${prpdRepairFactoryVo.factoryName}">
							</div>	
							<span style="color: red" class="col-1">&nbsp;*</span>
						</div>
						<div class="row cl">
							<label class="form_label col-2">维修厂地址</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" disabled="true" value="${prpdRepairFactoryVo.address}" >
							</div>
							<label class="form_label col-2">维修厂电话</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" disabled="true" value="${prpdRepairFactoryVo.telNo}" >
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-2">联系人</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" disabled="true" value="${prpdRepairFactoryVo.linker}" >
							</div>
							<label class="form_label col-2">联系人手机</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" disabled="true" value="${prpdRepairFactoryVo.mobile}" >
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-2">工时折扣</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" disabled="true" value="${prpdRepairFactoryVo.hourRate}" >
							</div>
							<span style="color: red" class="col-1">&nbsp;*</span>
							<label class="form_label col-1">配件折扣</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" disabled="true" value="${prpdRepairFactoryVo.compRate}" >
							</div>	
							<span style="color: red" class="col-1">&nbsp;*</span>
						</div>	
						<div class="row mb-5 cl">
					 		<label class="form_label col-2">有效无效标志</label>
					 		<div class="form_input col-3">
					 			<input type="text" class="input-text" disabled="true" value="${prpdRepairFactoryVo.validStatus}" >
					 		</div>					 		
					 	</div>
					 	<div class="row cl">
					 		<label class="form_label col-2">备注</label>
							<div class="form_input col-6">
								<textarea  class="textarea" disabled="true" value="${prpdRepairFactoryVo.remark}" placeholder="备注点什么..."></textarea>
							</div>
						</div>
						<!--撑开页面  开始  -->
						<div class="row cl"><div class="form_input col-6"></div></div>
						<div class="row cl"><div class="form_input col-6"></div></div>
						<!-- 结束 -->				
						<div class="row">
							<span class="col-offset-5">
							   <button class="btn btn-danger fl c2" type="button" id="close">关闭</button>
							 </span>
						</div>
						<div class="row cl"><div class="form_input col-6"></div></div>	
			     </div>
		</div>
<!--维修厂信息显示    结束-->
<script type="text/javascript">
	$("#close").click(function(){
		var a = parent.window.no();
		parent.layer.close(a);// 执行关闭
	});
</script>
</body>