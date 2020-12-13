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
		upperId : "required",
		systemCode : "required",
		menuCName : "required",
	};

	//4.Ajax表单操作相关
	var ajaxEdit = new AjaxEdit("#editForm");
	ajaxEdit.rules = rules;
	ajaxEdit.targetUrl = contextPath + "/menu/save"; 
	ajaxEdit.afterSuccess=function(){
		$("#closeButton").click();
//		parent.location.reload(); 
		//bootbox.alert("Success"); 
	}; 
//	ajaxEdit.afterFailure=function(){  
//		alert("Failure");
//	}; 
	//绑定表单
	ajaxEdit.bindForm();

});