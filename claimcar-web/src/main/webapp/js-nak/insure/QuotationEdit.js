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
   $("button.btn-return").click(function() {
		bootbox.confirm("确定要返回吗?", function(result) {
			if (result) {
				history.go(-1);
			}
		});
	});
    $("button.btn-create.first").click(function(){
    	var rowSize = $("#resultDataTable tr").length
    	var tr = '<tr><td><input id="emailAddress" name="emailAddress" class="form-control" type="text" value="" /></td><td><input id="emailAddress" name="emailAddress" class="form-control" type="text" value="" /></td><td><input id="emailAddress" name="emailAddress" class="form-control" type="text" value="" /></td><td><input id="emailAddress" name="emailAddress" class="form-control" type="text" value="" /></td><td><input id="emailAddress" name="emailAddress" class="form-control" type="text" value="" /></td><td><input id="emailAddress" name="emailAddress" class="form-control" type="text" value="" /></td><td><input id="emailAddress" name="emailAddress" class="form-control" type="text" value="" /></td><td><input id="emailAddress" name="emailAddress" class="form-control" type="text" value="" /></td></tr>';
    	$(".last.first").before(tr);
    });
    $("button.btn-create.second").click(function(){
    	var rowSize = $("#resultDataTable tr").length
    	var tr = '<tr><td><textarea id="emailAddress" name="emailAddress" class="form-control" type="text" value="" /></td><td><textarea id="emailAddress" name="emailAddress" class="form-control" type="textarea" value="" /></td></tr>';
    	$(".last.second").before(tr);
    });
	//3.页面校验
	//校验规则
	var rules = {
		userCode : "required",
		userName : "required",
		emailAddress : {
			email : true, 
		},
	};
	//校验提示
	var messages = {
		emailAddress : { 
			email : 'Please enter your real email address'
		}
	};

	//4.Ajax表单操作相关
	var ajaxEdit = new AjaxEdit("#form");
	ajaxEdit.rules = rules;
	ajaxEdit.messages = messages;
	ajaxEdit.targetUrl = contextPath + "/archives/save"; 
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