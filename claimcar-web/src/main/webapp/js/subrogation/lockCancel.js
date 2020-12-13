
$(function(){
	var ajaxEdit = new AjaxEdit($('#lockCancelform'));
	ajaxEdit.targetUrl = "/claimcar/subrogationEdit/lockedCancel.do";
	ajaxEdit.afterSuccess = successLockCancel;
	ajaxEdit.bindForm();//绑定表单
//	alert($("#recoveryCodeStatus").val());
	if($("#recoveryCodeStatus").val()!="1"){
		var recoveryName = $("#recoveryName").val();
		layer.msg("该案件已处理或"+recoveryName);
	}
	
});


function successLockCancel(data){
	var result = eval(data);
	if (result.status == "200") {
		layer.confirm('锁定取消成功', {
			btn: ['返回'] //按钮
		}, function(){
			//alert(window.location);
			window.location.reload();
			//TODO;
//			history.go(-1);
		});
	}
}

function lockCancel(){
	
	var failureCause =$("#failureCause").val();
	if(isBlank(failureCause)){
		layer.msg("请选择取消原因");
		return false;
	}
	$("#lockCancelform").submit();
}









	