var index2 = null;
 $(function(){
	 //已处理时进行的相关操作
	var status=$("#status").val();
	var errorType=$("#errorType").val();
	if(status=='0'){
		$("input").attr("disabled","true");
		$("select").attr("disabled","true");
		$("#searchAocount").removeAttr("onclick");
	}else{
		if(errorType=='3'){
			$("#MoneysaveBtn").removeAttr("style");
			$("#PaysaveBtn").removeAttr("style");
			
		}else{
			$("#saveBtn").removeAttr("style");
		}
	}
	
	
 var ajaxEdit = new AjaxEdit($('#saveBankInfo'));
    ajaxEdit.targetUrl = "/claimcar/assessors/sendAssAccountToPayment.do";
    //ajaxEdit.afterSuccess = after;
	 ajaxEdit.afterSuccess = function(result){
		var text = eval(result).statusText;  
		
		 var verifyStatus = text;
	
		layer.confirm(verifyStatus, {
			closeBtn: 0,btn: ['确定'] // 按钮
		  
		}, function(index){
			layerCancelBank();
			
		});
	}; 
	
	// 绑定表单
	ajaxEdit.bindForm();
	
 }); 
 
 function layerShowBankNum(){//弹出检索行号页面
  
		var Prov = $("#intermBankProvCity_lv1  option:selected").text();
		var City = $("#intermBankProvCity_lv2  option:selected").text();
		var Bname = $("#BankName  option:selected").text();
		var BQuery = $("#BankQueryText").val();
		$("#Province").val($("#intermBankProvCity_lv1  option:selected").val());
		$("#City").val($("#intermBankProvCity_lv2  option:selected").val());

		if (Prov == "" || Bname == "" || City == "") {
			layer.msg("请完善开户省市及银行信息！");

		} else {
			url = "/claimcar/assessors/findBankNoQueryList.do";
			if (index2 == null) {
				index2 = layer.open({
					type : 2,
					title : '检索行号',
					shade : false,
					area: ['900px', '350px'],
					content : url,
					end : function() {
						index2 = null;
					}
				});
			}
		}

 }
 
	
function layerCancelBank(){//点击取消关闭银行信息界面并清空输入信息
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		    parent.layer.close(index); //再执行关闭           	
	}
 


 function checkSpecialCharactorOfAccountName1(str){
	//var pattern = "^‘'！!#$%*+，,-./:：=？?@【】[\]_`·{|}~ ";//特殊字符
	var pattern = "^'!#$%*+,-/:=?@[\]_`{|}~ ……‘！#￥%*+，-。/：；=？@【、】——{|}~ ";//特殊字符
	if(typeof(str) === "undefined" || str === null || str===""){
		return false;
	}
	for(var i = 0,size = pattern.length;i<size;i++){
		var reg = pattern.substring(i, i+1);
		if(str.indexOf(reg)>-1){
			layer.alert("收款方户名包含特殊字符“"+reg+"”,请核实！");
			return false;
		}
	}
	//对于&quot和&lt和&gt不能同时出现
	var regArr = new Array();
	regArr.push("&quot");
	regArr.push("&lt");
	regArr.push("&gt");
	for(var i = 0,size = regArr.length;i<size;i++){
		var reg = regArr[i];
		if(str.toLowerCase().indexOf(reg)>-1){
			//进行转义提示
			//reg = "\\"+reg;
			alert("收款方户名包含特殊字符“"+reg+"”,请核实！");
			return false;
		}
	}
	return true;
}
 function checkSpecialCharactorOfAccountName(element){
		var str = $(element).val();
		//var pattern = "^‘'！!#$%*+，,-./:：=？?@【】[\]_`·{|}~ ";//特殊字符
		var pattern = "^'!#$%*+,-/:=?@[\]_`{|}~ ……‘！#￥%*+，-。/：；=？@【、】——{|}~ ";//特殊字符
		if(typeof(str) === "undefined" || str === null || str===""){
			return false;
		}
		for(var i = 0,size = pattern.length;i<size;i++){
			var reg = pattern.substring(i, i+1);
			if(str.indexOf(reg)>-1){
				layer.alert("收款方户名包含特殊字符“"+reg+"”,请核实！");
				return false;
			}
		}
		//对于&quot和&lt和&gt不能同时出现
		var regArr = new Array();
		regArr.push("&quot");
		regArr.push("&lt");
		regArr.push("&gt");
		for(var i = 0,size = regArr.length;i<size;i++){
			var reg = regArr[i];
			if(str.toLowerCase().indexOf(reg)>-1){
				//进行转义提示
				//reg = "\\"+reg;
				alert("收款方户名包含特殊字符“"+reg+"”,请核实！");
				return false;
			}
		}
		return true;
	}
 
 function checkSpecialCharactorOfRemark1(str){
		//var pattern = "^‘'！!#$%*+，,-./:：=？?@【】[\]_`·{|}~ ";//特殊字符
		var pattern = "^'!#$%*+,-./:=?@[\]_`{|}~ ……‘！#￥%*+，-。/：；=？@【、】——·{|}~ ";//特殊字符
		if(typeof(str) === "undefined" || str === null || str===""){
			return false;
		}
		for(var i = 0,size = pattern.length;i<size;i++){
			var reg = pattern.substring(i, i+1);
			if(str.indexOf(reg)>-1){
				layer.alert("摘要包含特殊字符“"+reg+"”,请核实！");
				return false;
			}
		}
		//对于&quot和&lt和&gt不能同时出现
		var regArr = new Array();
		regArr.push("&quot");
		regArr.push("&lt");
		regArr.push("&gt");
		for(var i = 0,size = regArr.length;i<size;i++){
			var reg = regArr[i];
			if(str.toLowerCase().indexOf(reg)>-1){
				//进行转义提示
				//reg = "\\"+reg;
				alert("摘要包含特殊字符“"+reg+"”,请核实！");
				return false;
			}
		}
		return true;
	}
 function checkSpecialCharactorOfRemark(element){
		var str = $(element).val();
		//var pattern = "^‘'！!#$%*+，,-./:：=？?@【】[\]_`·{|}~ ";//特殊字符
		var pattern = "^'!#$%*+,-./:=?@[\]_`{|}~ ……‘！#￥%*+，-。/：；=？@【、】——·{|}~ ";//特殊字符
		if(typeof(str) === "undefined" || str === null || str===""){
			return false;
		}
		for(var i = 0,size = pattern.length;i<size;i++){
			var reg = pattern.substring(i, i+1);
			if(str.indexOf(reg)>-1){
				layer.alert("摘要包含特殊字符“"+reg+"”,请核实！");
				return false;
			}
		}
		//对于&quot和&lt和&gt不能同时出现
		var regArr = new Array();
		regArr.push("&quot");
		regArr.push("&lt");
		regArr.push("&gt");
		for(var i = 0,size = regArr.length;i<size;i++){
			var reg = regArr[i];
			if(str.toLowerCase().indexOf(reg)>-1){
				//进行转义提示
				//reg = "\\"+reg;
				alert("摘要包含特殊字符“"+reg+"”,请核实！");
				return false;
			}
		}
		return true;
	}
 
 
function save(saveType){
	//修改后的户名与之前的不一致，则需要走退票审核，否则不走审核0-不需要，1-需要
	var oldAccountName=$("#oldAccountName").val();
	var accountname=$("#accountname").val();//收款人账户
	var accountNo=$("#account").val();//收款人账号
	var remark=$("#remark").val();//摘要信息
	if(!checkSpecialCharactorOfAccountName1(accountname)){
		return false;
	}
	if(!checkSpecialByAccountNo1(accountNo)){
		return false;
	}
	
	if(!checkSpecialCharactorOfRemark1(remark)){
		return false;
	}
	if(!checkCertifyNo()){
		return false;
	}
	
	if(oldAccountName==accountname){
		$("#isNeedAudit").val("0");
	}else{
		$("#isNeedAudit").val("1");
	}
	
	if(saveType=='0'){
		$("#isSendMoney").val("1");
	}else if(saveType=='1'){
		$("#isSendMoney").val("0");
	
	}else{
		
	}
	 $("#saveBankInfo").submit();
	
}

function checkSpecialByAccountNo1(str){
 	if(typeof(str) === "undefined" || str === null || str===""){
		return false;
	}
 	var regs = new RegExp("-{2,}");
	if(regs.test(str)){  
        layer.alert("收款人账号只能输入数字和-，且-不能连续输入!"); 
        return false;
    }  
 	//str = str.replace(/-/g,"");
 	str = str.replace(new RegExp('-','gi'),'');
	var reg = new RegExp("^[0-9]*$");
	if(!reg.test(str)){  
        layer.alert("收款人账号只能输入数字和-，且-不能连续输入!");  
        return false;
    } 
	return true;
}
function checkSpecialByAccountNo(element){
 	var str = $(element).val();
 	if(typeof(str) === "undefined" || str === null || str===""){
		return false;
	}
 	var regs = new RegExp("-{2,}");
	if(regs.test(str)){  
        layer.alert("收款人账号只能输入数字和-，且-不能连续输入!"); 
        return false;
    }  
 	//str = str.replace(/-/g,"");
 	str = str.replace(new RegExp('-','gi'),'');
	var reg = new RegExp("^[0-9]*$");
	if(!reg.test(str)){  
        layer.alert("收款人账号只能输入数字和-，且-不能连续输入!");  
        return false;
    } 
	return true;
}


function cleanBankNum(){//切换归属地或银行后，清空原来的行号信息，要求重新检索
	var text = "";
	$("#BankNumber").val(text);
	$("#BankOutlets").val(text);
	$("#BankQueryText").val(text);
	
}

$("#intermBankProvCity_lv1").change(function(){
	cleanBankNum();
});
$("#intermBankProvCity_lv2").change(function(){
	cleanBankNum();
});
$("#BankName").change(function(){
	cleanBankNum();
});

function checkCertifyNo(){
	 var certifyNo = $("#certifyNo").val();
	 var regs = new RegExp("^(.{9}|.{18})$");
	 //证件类型为组织机构代码时，证件号码只能是8位或者18位
	 if(!regs.test(certifyNo)){
		 layer.alert("请录入正确的9位或18位组织机构代码!"); 
		 return false;
	 }
	 return true;
}