
$(function(){
	var ajaxEdit = new AjaxEdit($('#confirmform'));
	ajaxEdit.targetUrl = "/claimcar/subrogationEdit/qsConfirm.do";
	ajaxEdit.afterSuccess = recoveryConfirm;
	ajaxEdit.bindForm();//绑定表单
	
});


function recoveryConfirm(data){
	var result = eval(data);
	if (result.status == "200") {
		layer.alert('待清付金额确认成功！', {closeBtn: 0}, function(index){
			$("#button").removeClass("btn btn-primary").addClass("btn btn-disabled");
			$("#button").removeAttr("type").attr("type","button");
			layer.close(index);
		});
//		layer.confirm('待清付金额确认成功', {
//			btn: ['返回'] //按钮
//		}, function(){
//			//alert(window.location);
//			//TODO;
//			history.go(-1);
//		});
	}
}

function lockCancel(){
	
	$("#confirmform").submit();
}









	