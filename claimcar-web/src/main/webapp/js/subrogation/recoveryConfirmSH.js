
$(function(){
     var accountsNoStatus=$("#accountsNoStatus").val();
	if(accountsNoStatus=='5'){
		$("#button").attr("class","btn btn-disabled");
		$("#button").attr("disabled",true);
	};
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

$("#button").click(function(){
	var index="0";
	var conIndx = layer.confirm('待清付金额是否已确认？', {
		  btn: ['是','否'] //按钮
		}, function(){
			var accountsNo=$("#recoveryCode").val();
			
			index=iscompente(accountsNo,index);
			if(index=='1'){
			layer.msg("已经发生过确认，请不要重复操作!");
			return false;
			}else{
				var accountsNoStatus=$("#accountsNoStatus").val();
				if(accountsNoStatus!='4' && accountsNoStatus!='6'&& accountsNoStatus!='7' && accountsNoStatus!='10'){
					layer.msg("结算码状态为待结算、零结算、开始结算、待支付时才能进行待结算金额确认!",{time:4000});
					return false;
					
				}
				$("#confirmform").submit();
			}
		   
           layer.close(conIndx);
           
		}, function(){
			layer.close(conIndx);
		});
	
});

function iscompente(accountsNo,index){
	
	index="0";//是否已结算标志
	var url = "/claimcar/subrogationEdit/isvalid.ajax";
	var params = {
		"accountsNo":accountsNo
	};
	$.ajax({
		url : url,
		data : params,
		type : 'post',
		async : false,
		success : function(result) {// json是后端传过来的值
			if(eval(result).status == "200"){
				var sign=eval(result).data;
				if(sign=='1'){
					
					index="1";
				}
			}
		}
	});
	return index;
	}






	