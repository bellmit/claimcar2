/**
 * 
 */
function setComValue( treeNode) {
	$("#upperComCode").val(treeNode.id);
	$("#upperComCName").val(treeNode.name);
	$.ajax({
		type : "POST",
		url : contextPath + "/company/findSub/"+ $("#upperComCode").val(),
		async: false,  
		success : function(obj) {
			if(obj==$("#comCName").val()){
				$("#comFullName").val($("#comCName").val());
			}else{
				$("#comFullName").val(obj+$("#comCName").val());
			}
		}
	});
}
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
		comCode : "required",
		comCName : "required",
		"prpDcompany.comCode" : "required",
		
	};
	//上级机构输入框添加机构选择树控件
	$("#upperComCName").bind(
			'click input propertychange', function() {
				showComCodeTree("upperComCName"," setComValue( treeNode)");
			});
	
	//4.Ajax表单操作相关
	var ajaxEdit = new AjaxEdit("#form");
	ajaxEdit.rules = rules;
	ajaxEdit.targetUrl = contextPath + "/company/save"; 
	ajaxEdit.afterSuccess=function(){
		history.go(-1); 
		//bootbox.alert("Success");
	}; 
//	ajaxEdit.afterFailure=function(){
//		alert("Failure");
//	}; 
	//绑定表单
	ajaxEdit.bindForm();
	
	
	//机构地址输入框改成地址下拉框控件
	showAddress("addressCName");
});
