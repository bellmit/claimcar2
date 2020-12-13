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
	//3.页面校验
	//校验规则
	var rules = {
		idOrName : "required",
		labelText : "required",
		dataType : "required",
		queryType : "required",
	};

	//4.Ajax表单操作相关
	var ajaxEdit = new AjaxEdit("#editForm");
	ajaxEdit.rules = rules;
	ajaxEdit.targetUrl = contextPath + "/spc/updateAttr"; 
	ajaxEdit.afterSuccess=function(){
		$("#closeButton").click();
		autoPreview();
//		parent.location.reload(); 
		//bootbox.alert("Success"); 
	}; 
//	ajaxEdit.afterFailure=function(){  
//		alert("Failure");
//	}; 
	//绑定表单
	ajaxEdit.bindForm();
	
	$("button.btn-create").click(function(){
    	var rowSize = $("#inputTable tbody tr").length - 1;
    	var tr = '<tr><td><input id="inputList[' + rowSize + '].key" name="inputList[' + rowSize + '].key" class="form-control" type="text" value="" /></td><td><input id="inputList[' + rowSize + '].value" name="inputList[' + rowSize + '].value" class="form-control" type="text" value="" /></td></tr>';
    	$(".last.second").before(tr);
    });
	$("button.btn-createButton").click(function(){
		var rowSize = $("#buttonTable tbody tr").length - 1;
		var tr = '<tr>'
		+ '<td><input id="buttonList[' + rowSize + '].selected" name="buttonList[' + rowSize + '].selected" class="form-control checkbox-inline" type="checkbox" value="1"/></td>'
		+ '<td><input id="buttonList[' + rowSize + '].buttonName" name="buttonList[' + rowSize + '].buttonName" class="form-control" type="text" /></td>'
		+ '<td><input id="buttonList[' + rowSize + '].buttonClass" name="buttonList[' + rowSize + '].buttonClass" class="form-control" type="text" /></td>'
		+ '<td><textarea id="buttonList[' + rowSize + '].buttonClick" name="buttonList[' + rowSize + '].buttonClick" class="form-control"></textarea></td>'
		+ '</tr>';
		$(".last.three").before(tr);
	});
	
	$("#inputType").change(function(){
		if($(this).val() == "input" || $(this).val() == "date"){
			$("#inputTypeDiv").css("display","none");
			$("#codeSelectDiv").css("display","none");
		}else if($(this).val() == "codeSelect"){
			$("#inputTypeDiv").css("display","none");
			$("#codeSelectDiv").css("display","block");
		}else{
			$("#codeSelectDiv").css("display","none");
			$("#inputTypeDiv").css("display","block");
		}
	});
	$("#inputType").change();

});