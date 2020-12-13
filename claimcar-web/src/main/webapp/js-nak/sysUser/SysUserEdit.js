/**
 * 
 */
$(function() {
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
		userCode : "required",
		userName : "required",
		email : {
			email : true, 
		},
	};
	//校验提示
	var messages = {
		email : { 
			email : 'Please enter your real email '
		}
	};

	//4.Ajax表单操作相关
	//var ajaxEdit = new AjaxEdit("#edit-profile");
	var ajaxEdit = new AjaxEdit("#form");
	ajaxEdit.rules = rules;
	ajaxEdit.messages = messages;
	//ajaxEdit.targetUrl = contextPath + "/sysUser/updateProfile"; 
	ajaxEdit.targetUrl = contextPath + "/sysUser/save";
	ajaxEdit.afterSuccess=function(){
		history.go(-1); 
	}; 
	//绑定表单
	ajaxEdit.bindForm();

});