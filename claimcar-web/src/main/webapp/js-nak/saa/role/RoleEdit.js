$(function() {
	//编辑页面加载所有功能代码
//	$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	//1.控件初始化
	$(".form_date").datetimepicker({
		format : "yyyy-mm-dd",
		language : "zh-CN",
		autoclose : true,
		todayBtn : true,
		todayHighlight : true,
		showMeridian : false,
		minView : "month",
		pickerPosition : "bottom-left",
	});
	//2.控件事件
	$("button.btn-return").click(function() {
		history.go(-1);
	});
	//3.页面校验
	//校验规则
	var rules = {
		roleCName : "required",
		comCode : "required",
	};
	$("#checkAll").click(function() {
		$("input[type='checkbox']").attr("checked", this.checked);
	});
	//校验提示

	//4.Ajax表单操作相关
	var ajaxEdit = new AjaxEdit("#form");
	ajaxEdit.rules = rules;
	ajaxEdit.targetUrl = contextPath + "/saa/role/save"; 
	ajaxEdit.beforeSubmit=function(){
		var selectIds = "";
		$("input[name='checkCode']:checked").each(function() {
			selectIds = selectIds + $(this).val() + ",";
		});
		if (selectIds != "") {
			selectIds = selectIds.substr(0, selectIds.length - 1);
		}
		$("#nodes1").val(selectIds);
	}; 
	ajaxEdit.afterSuccess=function(){
		history.go(-1); 
	}; 
	ajaxEdit.afterFailure=function(){
		alert("Failure");
	}; 
	//绑定表单
	ajaxEdit.bindForm();

});