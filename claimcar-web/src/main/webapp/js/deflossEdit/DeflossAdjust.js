
$(function(){
	var ajaxEdit = new AjaxEdit($('#lossAdjustform'));
	var lossId=$("#lossId").val();
	var deflossFlag = $("#deflossFlag").val();
	if(deflossFlag == "add"){
		ajaxEdit.targetUrl = "/claimcar/carLossAdjust/carAdditionLaunch.do?lossId="+lossId;
	}else{
		ajaxEdit.targetUrl = "/claimcar/carLossAdjust/carModifyLaunch.do?lossId="+lossId;
	}
	ajaxEdit.afterSuccess = successLaunch;
	ajaxEdit.bindForm();//绑定表单
});


function successLaunch(data){
	var result = eval(data);
	if (result.status == "200") {
		if(result.data !="ok"){
			layer.msg(result.data);
			return  false;
		}
	}
	var registNo= $("#registNo").val();
	layer.confirm('提交成功,报案号：'+registNo, {
		btn: ['返回'] //按钮
	}, function(){
		window.location.reload();
	});
	//parent.window.location.reload();
	
}
function launchDefloss(){
	$("#lossAdjustform").submit();
}








	