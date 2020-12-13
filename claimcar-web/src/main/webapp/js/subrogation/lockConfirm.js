
$(function(){
	var ajaxEdit = new AjaxEdit($('#lockform'));
	ajaxEdit.targetUrl = "/claimcar/subrogationEdit/lockedConfirm.do";
	ajaxEdit.afterSuccess = successLockform;
	ajaxEdit.bindForm();//绑定表单
	
	$("#checkAll").click(function(){
		var flag = $("#checkAll").is(":checked");
		var itmes = $("input[name='checkboxAssess']");
		itmes.each(function(){
			//$(this).attr('checked',flag);//这个多次点击 就没反应
			$(this).prop("checked",flag);
			
		});
	});
});


function successLockform(data){
	var result = eval(data);
	if (result.status == "200") {
		/*if(result.data !="ok"){
			layer.msg(result.data);
			return  false;
		}*/
		var recoveryCode= result.data;
		layer.confirm('锁定确认操作成功<br/> 结算码：'+recoveryCode, {
			btn: ['返回'] //按钮
		}, function(){
			window.location.reload();
		});
	}
	
	//parent.window.location.reload();
	
}

function lockSave(){
	var itmes = $("input[name='checkboxAssess']");
	var checkFlag = false;
	itmes.each(function(){
		if($(this).is(':checked')){
			checkFlag = true;
		}
	});
	
	if(!checkFlag){
		layer.msg("请选择要进行锁定确认的记录！");
		return false;
	}
	$("#lockform").submit();
}

function showThirdCarData(claimNotificationNo) {
	var registNo = $("#registNo").val();
	var claimSequenceNo = $("#claimSequenceNo").val();
	var oppoentPolicyNo = $("#oppoentPolicyNo").val();
	var coverageType = $("#coverageType").val();
	
	var goUrl ="/claimcar/subrogationEdit/lockedThirdParty.do?registNo="+registNo+"&claimSequenceNo="+claimSequenceNo+
		"&oppoentPolicyNo="+oppoentPolicyNo+"&coverageType="+coverageType+"&claimNotificationNo="+claimNotificationNo;
	openTaskEditWin("锁定三者车辆信息",goUrl);
}







	