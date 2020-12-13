<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>ILOG兜底权限删除</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont mb-10">
				<form id="saveIlog" class="form-horizontal" role="form" >
					<div class="formtable f_gray4">
						<div class="row  cl">
							 <input type="hidden" value="${ILOGFinalPowerInfoVo.id}" name="ILOGFinalPowerInfoVo.id" id="id">
							
							<label class="form_label col-1"><input type="radio" value="1" checked="checked"   name="ILOGFinalPowerInfoVo.checkFlag" id="checkFlag" />用户代码</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="userCode"
									name="ILOGFinalPowerInfoVo.userCode"
									value="${ILOGFinalPowerInfoVo.userCode}"
									style="width: 95%" nullmsg="请输入用户代码！" maxlength="10" onfocus="radioChecked(1)"/>
									<font class="must">*</font>
							</div>
							<label class="form_label col-1"><input type="radio" value="2" checked="checked"   name="ILOGFinalPowerInfoVo.checkFlag" id="checkFlag" />用户名称</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="userName" maxlength="30"
									name="ILOGFinalPowerInfoVo.userName" style="width: 95%"
									value="${ILOGFinalPowerInfoVo.userName}" nullmsg="请输入用户名称！" onfocus="radioChecked(2)"/>
								<font class="must">*</font>
							</div>
							<label class="form_label col-1">更新日期</label>
							<div class="form_input col-3">
								<input type="text" class="Wdate" id="updateTime"
									name="ILOGFinalPowerInfoVo.updateTime"
									value="<app:date date='${ILOGFinalPowerInfoVo.updateTime}'/>"
									style="width: 95%"
									onfocus="WdatePicker({maxDate:'%y-%M-%d'})" />
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1"><input type="radio" value="3" checked="checked"   name="ILOGFinalPowerInfoVo.checkFlag" id="checkFlag" />岗位权限</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="gradePower"
									name="ILOGFinalPowerInfoVo.gradePower" maxlength="30"
									value="${ILOGFinalPowerInfoVo.gradePower}" nullmsg="请填写岗位权限！"
									 style="width: 95%" onfocus="radioChecked(3)" /> <font class="must">*</font>
							</div>	
							<label class="form_label col-1"><input type="radio" value="4" checked="checked"   name="ILOGFinalPowerInfoVo.checkFlag" id="checkFlag" />权限等级</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="powerLevel"
									name="ILOGFinalPowerInfoVo.powerLevel"
									value="${ILOGFinalPowerInfoVo.powerLevel}" nullmsg="请填写权限等级！" 
									errormsg="请输入正确的车架号！"   ignore="ignore" onfocus="radioChecked(4)"/> 
							</div>
							<label class="form_label col-1"><input type="radio" value="5" checked="checked"   name="ILOGFinalPowerInfoVo.checkFlag" id="checkFlag" />岗位金额</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="gradeAmount"
									name="ILOGFinalPowerInfoVo.gradeAmount"
									value="${ILOGFinalPowerInfoVo.gradeAmount}"
									errormsg="请输入正确的岗位金额！"  ignore="ignore" 
									onfocus="radioChecked(5)" /> <font class="must">*</font>
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1"><input type="radio" value="6" checked="checked"   name="ILOGFinalPowerInfoVo.checkFlag" id="checkFlag" />二级机构</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="branchComcode" datatype="*" maxlength="30"
									name="ILOGFinalPowerInfoVo.branchComcode" style="width: 95%"
									value="${ILOGFinalPowerInfoVo.branchComcode}" nullmsg="请填写二级机构！" onfocus="radioChecked(6)"/>
								<font class="must">*</font>
							</div>
							<label class="form_label col-1">三级机构</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="subSidiaryComcode" datatype="*" maxlength="30"
									name="ILOGFinalPowerInfoVo.subSidiaryComcode" style="width: 95%"
									value="${ILOGFinalPowerInfoVo.subSidiaryComcode}" />
								<font class="must">*</font>
							</div>
							<label class="form_label col-1">备注</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="remark"
									name="ILOGFinalPowerInfoVo.remark" style="width: 95%"
									value="${ILOGFinalPowerInfoVo.remark}" />
								<font class="must">*</font>
							</div>
							
						</div>
					
						<div class="line"></div>
						<div class="row">
<!-- 					<input class="btn btn-primary ml-15" type="button" id="save" 
						onclick="saveIlogs()" value="保存"/>    -->
						<input class="btn btn-primary ml-15" type="button" id="delete" style="background:red"
						onclick="deleteIlogs()" value="删除"/>
						</div>
					</div>
				</form>
			</div>
		</div>
	
</div>
<%-- <script type="text/javascript" src="${ctx }/js/common/AjaxList.js"></script> 
<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script> --%>
    <script type="text/javascript" src="/claimcar/lib/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="/claimcar/lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
	<script type="text/javascript" src="/claimcar/lib/Validform/5.3.2/Validform.js"></script>
	
	<script type="text/javascript" src="/claimcar/js/common/AjaxList.js"></script>
	<script type="text/javascript" src="/claimcar/lib/layer/v2.1/layer.js"></script>
	<script type="text/javascript" src="/claimcar/lib/layer/1.9.3/layer.js"></script> 
	<script type="text/javascript" src="/claimcar/lib/icheck/jquery.icheck.min.js"></script>
	<script type="text/javascript" src="/claimcar/h-ui/js/H-ui.js"></script> 
	<script type="text/javascript" src="/claimcar/h-ui/js/H-ui.admin.js"></script> 
	<script type="text/javascript" src="/claimcar/js/common/common.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/AjaxEdit.js"></script> 
	<script  type="text/javascript" src="/claimcar/lib/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="/claimcar/plugins/qtip/jquery.qtip.js"></script> 
	<script type="text/javascript" src="/claimcar/plugins/select2-3.4.4/select2.js"></script>

<script type="text/javascript">

//删除信息
function deleteIlogs(){
	   var id = $("input[name='ILOGFinalPowerInfoVo.id']").val(); 
	   layer.confirm("确定要删除ID: ["+id+"] 信息吗?",{btn:['确定']}, function(indexs) {
		   
	   $.ajax({		  
		       url: "/claimcar/ilogfinalpower/ilogFinalPowerInfoDel.do",
	      dataType: 'json', // 接受数据格式
			  data: {"id" : id}, // 要传递的数据
		   success: function(jsonData) {
			   var index = parent.layer.getFrameIndex(window.name);// 获取窗口索引
				if (eval(jsonData).status == "200") {										
					layer.alert("删除成功",{shadeClose: false,closeBtn: 0},function(){					
						parent.layer.close(index);// 执行关闭
					});
				}
			},
			error : function() {
				       layer.msg("删除失败！");
			}
	   });
	   
  });
} 

</script>

</body>
</html>