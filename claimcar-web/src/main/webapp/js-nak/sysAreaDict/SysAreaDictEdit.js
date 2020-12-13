

$(function() {
	
	// 2.控件事件
	$("button.btn-return").click(function() {
		history.go(-1);
	});

	// 3.页面校验
	// 校验规则
	var rules = {
							
		areaCode : "required",
		areaName : "required"	
	};

	

	// 4.Ajax表单操作相关
	var ajaxEdit = new AjaxEdit("#form");
	ajaxEdit.rules = rules;
	ajaxEdit.targetUrl = contextPath + "/sysAreaDict/save";
	ajaxEdit.afterSuccess = function() {
		history.go(-1);
		// bootbox.alert("Success");
	};
	// 绑定表单
	ajaxEdit.bindForm();
});