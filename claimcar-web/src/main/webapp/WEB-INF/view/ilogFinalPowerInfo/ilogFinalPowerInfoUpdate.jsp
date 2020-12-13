<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>ILOG兜底权限更新</title>
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
							 <input type="hidden" value="${ILOGFinalPowerInfoVo.gradePower}" id="gradePower" />
							 <input type="hidden" value="${ILOGFinalPowerInfoVo.powerLevel}" id="powerLevel" />
							<label class="form_label col-1">用户工号</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="userCode"
									name="ILOGFinalPowerInfoVo.userCode"
									value="${ILOGFinalPowerInfoVo.userCode}"
									style="width: 95%" nullmsg="请输入用户代码！" maxlength="10" onfocus="radioChecked(1)"/>
									<font class="must">*</font>
							</div>
							<label class="form_label col-1">用户名称</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="userName" maxlength="30"
									name="ILOGFinalPowerInfoVo.userName" style="width: 95%"
									value="${ILOGFinalPowerInfoVo.userName}" nullmsg="请输入用户名称！" onfocus="radioChecked(2)"/>
								<font class="must">*</font>
							</div>
							<label class="form_label col-1">岗位金额</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="gradeAmount"
									name="ILOGFinalPowerInfoVo.gradeAmount"
									value="${ILOGFinalPowerInfoVo.gradeAmount}" nullmsg="请输入岗位金额！"
									errormsg="请输入正确的岗位金额！"  ignore="ignore" 
									onfocus="radioChecked(5)" /> <font class="must">*</font>
							</div>
							<%-- <label class="form_label col-1">更新日期</label>
							<div class="form_input col-3">
								<input type="text" class="Wdate" id="updateTime"
									name="ILOGFinalPowerInfoVo.updateTime"
									value="<app:date date='${ILOGFinalPowerInfoVo.updateTime}'/>"
									style="width: 95%"
									onfocus="WdatePicker({maxDate:'%y-%M-%d'})" />
							</div> --%>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">岗位权限</label>
							<div class="form_input col-3">
							    <select id="first" onChange="change()" style="height:28px;width:240px"  name="gradePower" >
							<!--         <option value="${ILOGFinalPowerInfoVo.gradePower}" selected="selected"></option>    -->
							        <option value="CarLoss">车辆定损</option>
							        <option value="PropLoss">财产定损</option>
							        <option value="PersonLoss">人伤损失</option>
							        <option value="PeopChk">查勘</option>
							        <option value="CompenSate">理算</option>						        
							    </select>		 
							</div>
							<label class="form_label col-1">权限等级</label>
							<div class="form_input col-3">
								<select id="second"  style="height:28px;width:240px" name="powerLevel" >
								    <option value="carloss1">定损岗一级</option>
								    <option value="proploss1">定损岗一级</option>
   								    <option value="peoploss1">人伤岗一级</option>
   								    <option value="peoploss2">人伤岗二级</option>
   								    <option value="peopchk1">查勘岗一级</option>
   								    <option value="peopchk2">查勘岗二级</option>
								    <option value="comp1">理算岗一级</option>
								    
								   
								</select>	 
							</div>
							<label class="form_label col-1">备注</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="remark"
									name="ILOGFinalPowerInfoVo.remark" style="width: 95%"
									value="${ILOGFinalPowerInfoVo.remark}" />
								<font class="must">*</font>
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">二级机构</label>
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
						</div>
					
						<div class="line"></div>
						<div class="row">
							<input class="btn btn-primary ml-15" type="button" id="save" onclick="saveIlogs()" value="保存"/>
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

   
	var ajaxEdit;
	$(function() {
		// Ajax表单操作相关
		ajaxEdit = new AjaxEdit($('#saveIlog'));
		ajaxEdit.targetUrl = "/claimcar/ilogfinalpower/ilogFinalPowerInfoSave.do";
		ajaxEdit.beforeSubmit = updateTable;
		ajaxEdit.afterSuccess = function(JsonData) {//保存成功 回调函数
			var result = eval(JsonData);
			var index = parent.layer.getFrameIndex(window.name);// 获取窗口索引
			if (result.status == "200") {
				// 保存成功，关闭页面
				layer.alert("更新成功", {
					shadeClose : false,
					closeBtn : 0
				}, function() {
					parent.layer.close(index);// 执行关闭
				});
			}
		};
		ajaxEdit.error = function() {
			layer.alert("保存失败！");
		};
		ajaxEdit.bindForm();//绑定表单

		//获取后台传过来的 gradepower和 powerlevel
		$(document).ready(
				function() {
					var gradepower = $("#gradePower").val();
					$("#first option[value='" + gradepower + "']").attr("selected", "selected");
				});
		$(document).ready(
				function() {
					var powerlevel = $("#powerLevel").val();
					$("#second option[value='" + powerlevel + "']").attr("selected", "selected");
				});
	});

	//二级联动方法
	function change() {
		var x = document.getElementById("first");
		var y = document.getElementById("second");
		y.options.length = 0; // 清除second下拉框的所有内容  
		if (x.selectedIndex == 0) {
			y.options.add(new Option("定损岗一级", "carloss1", "0", false, true)); // 默认选中第一个

		}

		if (x.selectedIndex == 1) {
			y.options.add(new Option("定损岗一级", "proploss1", "0", false, true));
		}
		if (x.selectedIndex == 2) {
			y.options.add(new Option("人伤岗一级", "peoploss1", "0", false, true));
			y.options.add(new Option("人伤岗二级", "peoploss2", "1"));
		}
		if (x.selectedIndex == 3) {
			y.options.add(new Option("查勘岗一级", "peopchk1", "0", false, true));
			y.options.add(new Option("查勘岗二级", "peopchk2", "1"));
		}
		if (x.selectedIndex == 4) {
			y.options.add(new Option("理算岗一级", "comp1", "0", false, true));
		}
	}

	/**
	 * 	表单提交--->保存更改
	 */
	function saveIlogs() {
		$("#saveIlog").submit();
	}

	function radioChecked(val) {
		$("input[name='ILOGFinalPowerInfoVo.checkFlag']").each(function() {
			if ($(this).val() == val) {
				$(this).prop("checked", true);
			}
		});
	}

	//检验非空
	function updateTable() {
		if (checkBeforeQuery()) {
			layer.msg("用户代码，用户名称，岗位权限，权限等级，岗位金额，二级机构 都不为空！");
			return;
		}
	}

	//校验查询条件
	function checkBeforeQuery() {
		var checkedIndex = $("input[name='ILOGFinalPowerInfoVo.checkFlag']")
				.filter(":checked").val();
		var flag = false;
		if (checkedIndex == 1) {
			var userCode = $.trim($("#userCode").val());
			if (userCode == "") {
				flag = true;
			}
		}
		if (checkedIndex == 2) {
			var userName = $.trim($("#userName").val());
			if (userName == "") {
				flag = true;
			}
		}
		if (checkedIndex == 3) {
			var gradePower = $.trim($("#gradePower").val());
			if (gradePower == "") {
				flag = true;
			}
		}
		if (checkedIndex == 4) {
			var powerLevel = $.trim($("#powerLevel").val());
			if (powerLevel == "") {
				flag = true;
			}
		}
		if (checkedIndex == 5) {
			var gradeAmount = $.trim($("#gradeAmount").val());
			if (gradeAmount == "") {
				flag = true;
			}
		}
		if (checkedIndex == 6) {
			var branchComcode = $.trim($("#branchComcode").val());
			if (branchComcode == "") {
				flag = true;
			}
		}
		return flag;
	};
</script>

</body>
</html>