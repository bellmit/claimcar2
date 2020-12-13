/**
 * 
 */

$(function() {
	//1.控件初始化

	//2.控件事件
	$("button.btn-return").click(function() {
		history.go(-1);
	});
		
	//3.页面校验
	//校验规则
	 
	//4.Ajax表单操作相关
	var ajaxEdit = new AjaxEdit("#form"); 
	ajaxEdit.targetUrl = contextPath + "/formmodel/save"; 
 
	ajaxEdit.afterSuccess=function(XMLHttpRequest, textStatus){
//		history.go(-1); 
		bootbox.alert(XMLHttpRequest.responseText);
		bootbox.alert(textStatus);
	}; 
//	ajaxEdit.afterFailure=function(XMLHttpRequest, textStatus){
//		alert("Failure");
//	}; 
	//绑定表单
	ajaxEdit.bindForm();

});