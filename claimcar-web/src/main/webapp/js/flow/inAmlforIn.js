$(function (){
	
	changeCustomerType();
	changeConsistent();
	
	$("#insured").find("select").attr("disabled", true);
	$("#insured").find("input").attr("disabled", true);
	 
	$("#favoreel").find("#favoreelCertifyType").find("option").each(function(){
		if('10'!=$(this).val()&&'00'!=$(this).val()){
			$(this).remove();
		}
	});
	var verifySign=$("#verifySign").val();
	//核陪页面查看反洗钱补录信息
	if(verifySign=='1'){
		$("input").attr("disabled","disabled");
		$("select").attr("disabled","disabled");
		$("a[type='submit']").removeAttr("onclick");
	}
	
	ajaxEdit = new AjaxEdit($('#prpLPayFxqCustomFormFrost'));
	ajaxEdit.targetUrl = "/claimcar/payCustom/saveMoneyInfo.do"; 
	ajaxEdit.afterSuccess = after;
	
	// 绑定表单
	ajaxEdit.bindForm();
				
});



function closeLayer(){
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);	
}


function inAmlforIn(){
	$("#prpLPayFxqCustomFormFrost").submit();
}
 
function changeCustomerType(){
	var customerType = $("[name='prpLFxqFavoreeVo.customerType']:checked");
	if(customerType.prop("checked")==true&&customerType.val()=="1"){
		$("#favoreel").find("input[name='prpLFxqFavoreeVo.favoreeName']").attr("disabled",true);
		$("#favoreel").find("input[name='prpLFxqFavoreeVo.favoreeAdress']").attr("disabled",true);
		$("#favoreel").find("input[name='prpLFxqFavoreeVo.favoreeIdentifyCode']").attr("disabled",true);
		$("#favoreen").find("input").removeAttr("disabled");
		$("#favoreel").attr("style","display:none");
		$("#favoreen").removeAttr("style");
		$("#favoreel").find("input").removeAttr("dataType").removeAttr("nullmsg").removeClass("Validform_error").qtip('destroy',true);	
		$("#favoreel").find("#favoreelInsureRelation").removeAttr("name");
		$("#favoreel").find("#favoreelCertifyType").removeAttr("name");
		$("#favoreel").find("#dgDateMin").removeAttr("name");
		$("#favoreel").find("#dgDateMax").removeAttr("name");
		$("#favoreen").find("#favoreenInsureRelation").attr("name","prpLFxqFavoreeVo.favoreelInsureRelation");
		$("#favoreen").find("#favoreenCertifyType").attr("name","prpLFxqFavoreeVo.favoreeCertifyType");
		$("#favoreen").find("#dgDateMin").attr("name","prpLFxqFavoreeVo.favoreeCertifyStartDate");
		$("#favoreen").find("#dgDateMax").attr("name","prpLFxqFavoreeVo.favoreeCertifyEndDate");
		$("#favoreen").find("input[name='prpLFxqFavoreeVo.favoreeName']").attr("dataType","*").attr("nullmsg","请填写受益人姓名（自然人）");
		$("#favoreen").find("input[name='prpLFxqFavoreeVo.favoreenPhone']").attr("dataType","*").attr("nullmsg","请填写联系方式");
		$("#favoreen").find("input[name='prpLFxqFavoreeVo.favoreenProfession']").attr("dataType","*").attr("nullmsg","请填写职业");
		$("#favoreen").find("input[name='prpLFxqFavoreeVo.favoreeIdentifyCode']").attr("dataType","*").attr("nullmsg","请填写证件号码");
		$("#favoreen").find("input[name='prpLFxqFavoreeVo.favoreeCertifyStartDate']").attr("dataType","*").attr("nullmsg","日期不能为空");
		$("#favoreen").find("input[name='prpLFxqFavoreeVo.favoreeCertifyEndDate']").attr("dataType","*").attr("nullmsg","日期不能为空");
		$("#favoreen").find("input[name='prpLFxqFavoreeVo.favoreenAdress']").attr("dataType","*").attr("nullmsg","请填写地址");	
	}else{
		$("#favoreen").find("input[name='prpLFxqFavoreeVo.favoreeName']").attr("disabled",true);
		$("#favoreen").find("input[name='prpLFxqFavoreeVo.favoreeAdress']").attr("disabled",true);
		$("#favoreen").find("input[name='prpLFxqFavoreeVo.favoreeIdentifyCode']").attr("disabled",true);
		$("#favoreel").find("input").removeAttr("disabled");
		$("#favoreen").attr("style","display:none");
		$("#favoreel").removeAttr("style");
		$("#favoreen").find("input").removeAttr("dataType").removeAttr("nullmsg").removeClass("Validform_error").qtip('destroy',true);	
		$("#favoreen").find("#favoreenInsureRelation").removeAttr("name");
		$("#favoreen").find("#favoreenCertifyType").removeAttr("name");
		$("#favoreen").find("#dgDateMin").removeAttr("name");
		$("#favoreen").find("#dgDateMax").removeAttr("name");
		$("#favoreel").find("#favoreelInsureRelation").attr("name","prpLFxqFavoreeVo.favoreelInsureRelation");
		$("#favoreel").find("#favoreelCertifyType").attr("name","prpLFxqFavoreeVo.favoreeCertifyType");
		$("#favoreel").find("#dgDateMin").attr("name","prpLFxqFavoreeVo.favoreeCertifyStartDate");
		$("#favoreel").find("#dgDateMax").attr("name","prpLFxqFavoreeVo.favoreeCertifyEndDate");
		$("#favoreel").find("input[name='prpLFxqFavoreeVo.favoreeName']").attr("dataType","*").attr("nullmsg","请填写受益人名称（法人）");
		$("#favoreel").find("input[name='prpLFxqFavoreeVo.favoreeAdress']").attr("dataType","*").attr("nullmsg","请填写单位地址");
		$("#favoreel").find("input[name='prpLFxqFavoreeVo.favoreelBusinessArea']").attr("dataType","*").attr("nullmsg","请填写经营范围");
		$("#favoreel").find("input[name='prpLFxqFavoreeVo.favoreeCertifyStartDate']").attr("dataType","*").attr("nullmsg","日期不能为空");
		$("#favoreel").find("input[name='prpLFxqFavoreeVo.favoreeCertifyEndDate']").attr("dataType","*").attr("nullmsg","日期不能为空");
		changeIdentify();
	}
}

function changeConsistent(){
	var Consistent = $("[name='prpLPayFxqCustomVo.isConsistent']:checked");
	if(Consistent.prop("checked")==true&&Consistent.val()=="1"){
		$("#payAccountNo").find("input").attr("value","");
		$("#payAccountNo").attr("style","display:none");
		$("#payAccountNo").find("input").removeAttr("dataType").removeAttr("nullmsg").removeClass("Validform_error").qtip('destroy',true);
	}else{
		$("#payAccountNo").removeAttr("style");
		$("#payAccountNo").find("input").attr("dataType","*").attr("nullmsg","请填写缴费账户");
	}
}

function changeIdentify(){
	var select = $("#favoreel").find("#favoreelCertifyType");
	if(select.val()!="10"&&select.val()!="00"){
		select.val("00");
	}
	if('10'==select.val()){
		$("#shxy").attr("style","display:none");
		$("#zzjg").removeAttr("style");
		$("#shxy").find("input").attr("disabled",true);
		$("#zzjg").find("input").removeAttr("disabled");
		$("#shxy").find("input").removeAttr("dataType").removeAttr("nullmsg").removeClass("Validform_error").qtip('destroy',true);
		$("#zzjg").find("input[name='prpLFxqFavoreeVo.favoreeIdentifyCode']").attr("dataType","*").attr("nullmsg","请填写组织机构代码");
		$("#zzjg").find("input[name='prpLFxqFavoreeVo.favoreelBusinessCode']").attr("dataType","*").attr("nullmsg","请填写营业执照编码");
		$("#zzjg").find("input[name='prpLFxqFavoreeVo.favoreelRevenueRegistNo']").attr("dataType","*").attr("nullmsg","请填写税务登记号码");		
	}else if('00'==select.val()){
		$("#zzjg").attr("style","display:none");
		$("#shxy").removeAttr("style");
		$("#zzjg").find("input").attr("disabled",true);
		$("#shxy").find("input").removeAttr("disabled");
		$("#zzjg").find("input").removeAttr("dataType").removeAttr("nullmsg").removeClass("Validform_error").qtip('destroy',true);
		$("#shxy").find("input[name='prpLFxqFavoreeVo.favoreeIdentifyCode']").attr("dataType","*").attr("nullmsg","请填写统一社会信用代码");		
	}
}


function after(result){
		var sstatus=eval(result).status;
		var sstatusText=eval(result).statusText;
		if(sstatus=='200'){
			layer.msg(statusText,4000);
			$("input").attr("readonly","readonly");
			$("button").attr("disabled","disabled");
			$("select").prop("disabled",true);
			$("#centain").removeAttr("onclick");
			$("#centain").removeClass("btn-primary");
			$("#centain").addClass("btn-disabled");
			window.location.reload();
			closeLayer();
		}else{
			alert(statusText);
			closeLayer();
		}
}