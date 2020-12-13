/**
 * 
 */
function setComValue( treeNode) {
	$("#comCode").val(treeNode.id);
	$("#comCName").val(treeNode.name);
}
$(function() {
	$("#comCName").bind(
			'click input propertychange', function() {	
				showComCodeTree("comCName"," setComValue( treeNode)");
			});
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
		"prpDcompany.comCode" : "required",
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
	var ajaxEdit = new AjaxEdit("#form");
	ajaxEdit.rules = rules;
	ajaxEdit.messages = messages;
	ajaxEdit.targetUrl = contextPath + "/user/save"; 
	ajaxEdit.afterSuccess=function(){
		history.go(-1); 
		//bootbox.alert("Success");
	}; 
//	ajaxEdit.afterFailure=function(){
//		alert("Failure");
//	}; 
	//绑定表单
	ajaxEdit.bindForm();

});