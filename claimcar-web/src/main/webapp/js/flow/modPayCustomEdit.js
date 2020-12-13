/**
 * 收款人信息维护js
 */

var provN,cityN,provC,cityC,bname,bankOutlets,bQuery;
	
var ajaxEdit;
	
$(function (){
	setPayObjectKind();
	addAML();//判断是否加载反洗钱页面
	changeValidOfCertifyNo();// 身份证号机构号校验的改变
//	findCInsuredInfo();//判断是否为被保险人并带入承保人信息
	var errorType = $("input[name='payBankVo.errorType']").val();
	if(errorType == 3){
		$("#submit").hide();
	}else{
		$("#bankroll").hide();
		$("#payment").hide();
		
	}
	$("#payCustomProvCity_lv1").css("width","34%");
	$("#payCustomProvCity_lv2").css("width","49%");
	$("#payCustomProvCity_lv2").attr("class","areaselect");
	ajaxEdit = new AjaxEdit($('#PayCustomEditForm'));
//	ajaxEdit.targetUrl = "/claimcar/payCustom/savePayCustomInfo.do";
	ajaxEdit.targetUrl = "/claimcar/accountInfo/saveAccountInfo.do";
	ajaxEdit.beforeSubmit = saveBeforeCheck;
	ajaxEdit.afterSuccess = function(result){
		var verifyStatus = eval(result).data;
		var text = eval(result).statusText;
		if(verifyStatus=="3"){
			verifyStatus = text + "且自动审核完成！";
		}else{
			verifyStatus = text + "已提交至审核，请通知审核岗处理！";
		}
		layer.confirm(verifyStatus, {
			closeBtn: 0,btn: ['确定'] // 按钮
		}, function(index){
			closeLayer();
		});
	};
	
	// 绑定表单
	ajaxEdit.bindForm();
});

function after(result) {
	
	var verifyStatus = eval(result).data;
	var text = eval(result).statusText;
	
//	submit_Acc();
	layer.alert("========"+verifyStatus+"====="+text+"========");
	
//	var nodeCode = $("#nodeCode").val();
//	var payId=eval(result).data;
//	if(payId!=null){
//		showPayResult(payId,nodeCode);
//	}
	/* if(eval(result).data!=null&&$("#nodeCode").val()=="Chk"){
		//将获取的保存成功的VoID传给页面显示
		showPayResult(eval(result).data,$("#nodeCode").val());
	}else if(eval(result).data!=null&&$("#nodeCode").val()=="DLProp"){
		
	} */
	if(nodeCode=="lawFirmNode"){
		parent.$("[name='prpdLawFirmVo.payCustomId']").val(eval(result).data);
	}
	//保存成功，关闭当前窗口
//	closeLayer();
}

function saveBeforeCheck(){
	var payeeName = $("#payeeName").val();
	var accountNo = $("#accountNo").val();
	var summary = $("input[name='prpLPayCustomVo.summary']").val();
	if(!checkSpecialChar(payeeName,"payeeName")){
		return false;
	}
	if(!checkSpecialChar(summary,"summary")){
		return false;
	}
	if(!checkSpecialByAccount(accountNo)){
		return false;
	}
	if(!checkCertifyNo()){
		return false;
	}
	
	
}

function save(saveType){
	if(saveType=="bankroll"){
		$("input[name='payBankVo.isAutoPay']").val("1");
	}else if(saveType=="payment"){
		$("input[name='payBankVo.isAutoPay']").val("0");
	}
	
	
	var payeeName = $("#payeeName").val();
	var accountNo = $("#accountNo").val();
	var paycustId=$("input[name='prpLPayCustomVo.id']").val();//原银行信息Id
	var payCompensateNo = $("input[name='payBankVo.compensateNo']").val();
	if(accountNo.length > 32){
		layer.alert("收款人账号最大为32位");
		return false;
	}
	params1={"payeeName":payeeName,
			"accountNo":accountNo,
			"paycustId":paycustId,
			"compensateNo":payCompensateNo
			};
	$.ajax({
		url : "/claimcar/payCustom/vaildAccoundNo.ajax", // 后台校验
	    type : 'post', // 数据发送方式
	    dataType : 'json', // 接受数据格式
	    data : params1, // 要传递的数据
	    async : false,
	   success : function(jsonData) {// 回调方法，可单独定义
		   var sign="";
		   var array=new Array();
	       var result = eval(jsonData);
	       
		if (result.status == "200" ) {
		   var statusText1=result.statusText;
		   array=statusText1.split(",");
			  sign =result.data;
			if(sign=='1'){
				var index=layer.confirm("系统中报案号{"+array[0]+"}账号{"+accountNo+"}户名{"+array[1]+"}已有支付成功信息,与当前户名{"+payeeName+"}不一致,请确认是否继续修改该账号的户名?",{
					btn:['是','否']
				},function(index){
					     $("#PayCustomEditForm").submit();
				         layer.close(index);
				},function(index){
						 layer.close(index);
				});
					
			}else{
				$("#PayCustomEditForm").submit();
			}
		}else{
		   var statusText1=result.statusText;
		   layer.alert(statusText1);
		   }
	     }
	 });
	
	
	
}

function payCustomSubmit(){
	if($("[name='prpLPayCustomVo.payObjectKind']").val()=="2"){
		//alert("收款人类型为被保险人");
		if($("[name='prpLPayCustomVo.payeeName']").val()==$("[name='insuredName']").val()){
			//alert("收款人户名和被保险人一致");
			//此处校验收款人类型为被保险人时户名和被保险人需要一致
			//证件类型隐藏域赋值，判断收款人性质，个人-身份证01，机构-其他99
			var identType = $("#PayObjectType").val();
			if(identType=="1"){
				$("input[name='prpLPayCustomVo.certifyType']").val("01");
			}else{
				$("input[name='prpLPayCustomVo.certifyType']").val("99");
			}
			//提交
			$("#PayCustomEditForm").submit();
		}else{
			layer.msg("收款人为被保险人时银行户名需与被保险人一致");
		}
	}else{
		//证件类型隐藏域赋值，判断收款人性质，个人-身份证01，机构-其他99
		var identType = $("#PayObjectType").val();
		if(identType=="1"){
			$("input[name='prpLPayCustomVo.certifyType']").val("01");
		}else{
			$("input[name='prpLPayCustomVo.certifyType']").val("99");
		}
		//提交
		$("#PayCustomEditForm").submit();
	}
	
}
function closeLayer(){
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);	
}

var index2 = null;
function layerShowBankNum(){//弹出检索行号页面
	
	provN = $("#payCustomProvCity_lv1  option:selected").text();
	cityN = $("#payCustomProvCity_lv2  option:selected").text();
	provC = $("#payCustomProvCity_lv1  option:selected").val();
	cityC = $("#payCustomProvCity_lv2  option:selected").val();
	
	// 主页面隐藏域赋值
	$("#Province").val(provC);
	$("#City").val(cityC);
	$("#ProvinceName").val(provN);
	$("#CityName").val(cityN);
	
	bname = $("#BankName  option:selected").text();
	bQuery = $("#BankQueryText").val();
			
	if(provN==""||cityN==""||provN=="undefined"||cityN=="undefined"){
		layer.msg("请选择省/直辖市");
		
	}else{
		if(bname==""||bname=="undefined"){
			layer.msg("请选择收款人开户行");
		}else{
			url = "/claimcar/payCustom/findBankNoQueryList.do";
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
};

function cleanBankNum(){//切换归属地或银行后，清空原来的行号信息，要求重新检索
	var text = "";
	$("#BankNumber").val(text);
	$("#BankQueryText").val(text);
	$("[name='prpLPayCustomVo.bankOutlets']").val(text);
	
}

$("#payCustomProvCity_lv1").change(function(){
	cleanBankNum();
});
$("#payCustomProvCity_lv2").change(function(){
	cleanBankNum();
});
$("#BankName").change(function(){
	cleanBankNum();
});

 //判断开始时间不能大于结束时间
 $("[name='prpLPayCustomVo.certifyEndDate']").blur(function(){
	 var startD = $("[name='prpLPayCustomVo.certifyStartDate']").val();
	 var endD = $("[name='prpLPayCustomVo.certifyEndDate']").val();
	 var d1 = new Date(startD.replace(/\-/g, "\/"));  
	 var d2 = new Date(endD.replace(/\-/g, "\/"));  

	  if(startD!=""&&endD!=""&&d1 >=d2)  
	 {  
	  layer.msg("开始时间不能大于结束时间！");  
	  $("[name='prpLPayCustomVo.certifyEndDate']").val(null);
	 }
 });
 
 //判断开始时间不能大于结束时间(授办人)
 $("[name='prpLPayCustomVo.authorityEndDate']").blur(function(){
	 var startD = $("[name='prpLPayCustomVo.authorityStartDate']").val();
	 var endD = $("[name='prpLPayCustomVo.authorityEndDate']").val();
	 var d1 = new Date(startD.replace(/\-/g, "\/"));  
	 var d2 = new Date(endD.replace(/\-/g, "\/"));  

	  if(startD!=""&&endD!=""&&d1 >=d2)  
	 {  
	  layer.msg("开始时间不能大于结束时间！");  
	  $("[name='prpLPayCustomVo.certifyEndDate']").val(null);
	 }
 });
 
 function findCInsuredInfo(){//收款人类型为被保险人时查詢代入数据
	 var flag = $("input[name='flag']").val();
	 if($("[name='prpLPayCustomVo.payObjectKind']  option:selected").text()=="被保险人"
	 ||$("[name='prpLPayCustomVo.payObjectKind']").val()=="2"){
	 var registNo = $("[name='registNo']").val();//获取当前节点报案号用于查询
	 $.ajax({
			url:"/claimcar/payCustom/getCInsuredInfo.ajax",
			type:"post",
			data:{"registNo":registNo},
			dataType:"json",
			success:function(result){
				if(result.data!=null){
					var iName = result.data.insuredName;
					//var pType = result.data.identifyType;
					var cNo = result.data.identifyNumber;
					$("[name='insuredName']").val(iName);
					//$("[name='prpLPayCustomVo.payObjectType']").val(pType);
					if(flag!='Y'){
						$("[name='prpLPayCustomVo.payeeName']").val(iName);
						$("[name='prpLPayCustomVo.certifyNo']").val(cNo);
					}
					//$("[name='prpLPayCustomVo.PermitNo']").val(identifyNumber); //在打开AML事件中判断和赋值
				}
			}
		});
	}
 }
 
 function setPayObjectKind(){
	 var obj = $("#payObjectKind").val();
	 $("[name='prpLPayCustomVo.payObjectKind']").val(obj);
	 changeValidOfCertifyNo();
 }
 
 $("[name='prpLPayCustomVo.payObjectKind']").change(function(){
	 findCInsuredInfo();//判断是否为被保险人并带入承保人信息
	 // 切换收款人类型后清空户名和证件号码
	 $("[name='prpLPayCustomVo.certifyNo']").val(null);
	 $("[name='prpLPayCustomVo.payeeName']").val(null);
	 // 判断选择的收款类型是否为公估机构，如果是公估机构，取消身份证号的验证
	 changeValidOfCertifyNo();
 });
 $("[name='prpLPayCustomVo.certifyType']").change(function(){
	 changeValidOfCertifyNo();
 });
 /**
  * 判断选择的收款类型是否为公估机构（或者收款人性质为机构时），如果是公估机构，取消身份证号的验证 
  */
 function changeValidOfCertifyNo(){
	 if($("[name='prpLPayCustomVo.payObjectKind']").val()=="4" ||$("[name='prpLPayCustomVo.certifyType']").val()!="01"){
		 $("[name='prpLPayCustomVo.certifyNo']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		 $("[name='prpLPayCustomVo.certifyNo']").attr("datatype","*");
		 //alert($("[name='prpLPayCustomVo.certifyNo']").attr("datatype"));
	 }else{
		 $("[name='prpLPayCustomVo.certifyNo']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		 $("[name='prpLPayCustomVo.certifyNo']").attr("datatype","idcard");
		 //alert($("[name='prpLPayCustomVo.certifyNo']").attr("datatype"));
	 }
 }
 

 
/* 赔款支付环节（理算、预付、支付、垫付），若选择收款人后，需要补充反洗钱信息时
 * （案件赔付总额大于10000人民币或1000美金），系统带出已录入过的收款人账户信息
 */
function addAML(){
	var flag = $("input[name='flag']").val();
	if(flag=='Y'){//flag为是否为补充反洗钱流程的标志，Y-是，N-否
		$("#mainInfo input").attr("readonly","readonly");//所有主页面的信息不允许修改
		//读取收款人性质 个人or机构显示相应的反洗钱信息页面
		var pid = $("[name='prpLPayCustomVo.id']").val();
		var registNo = $("[name='prpLPayCustomVo.registNo']").val();
		var claimNo='';
		var params = {
			"registNo" : registNo,
			"pid" : pid,
			"claimNo":claimNo
		};
		var url = "/claimcar/payCustom/addRowInfo.ajax";
		$.post(url, params, function(result) {
			$("#AML").append(result);
			$("[name='prpLPayCustomVo.nationality']").select2();
		});
	}
	
}

/*function payCustomSubmit_Acc(){
	if($("[name='prpLPayCustomVo.payObjectKind']").val()=="2"){
		//alert("收款人类型为被保险人");
		if($("[name='prpLPayCustomVo.payeeName']").val()==$("[name='insuredName']").val()){
			//alert("收款人户名和被保险人一致");
			//此处校验收款人类型为被保险人时户名和被保险人需要一致
			//证件类型隐藏域赋值，判断收款人性质，个人-身份证01，机构-其他99
			var identType = $("#PayObjectType").val();
			if(identType=="1"){
				$("input[name='prpLPayCustomVo.certifyType']").val("01");
			}else{
				$("input[name='prpLPayCustomVo.certifyType']").val("99");
			}
			//提交
			$("#PayCustomEditForm").submit();
			saveAcc();
		}else{
			layer.msg("收款人为被保险人时银行户名需与被保险人一致");
		}
	}else{
		//证件类型隐藏域赋值，判断收款人性质，个人-身份证01，机构-其他99
		var identType = $("#PayObjectType").val();
		if(identType=="1"){
			$("input[name='prpLPayCustomVo.certifyType']").val("01");
		}else{
			$("input[name='prpLPayCustomVo.certifyType']").val("99");
		}
		//提交
		$("#PayCustomEditForm").submit();
		saveAcc();
	}
}*/
function submit_Acc(){
	var params = $("#PayCustomEditForm").serialize();
	//证件类型隐藏域赋值，判断收款人性质，个人-身份证01，机构-其他99
	var identType = $("#PayObjectType").val();
	if(identType=="1"){
		$("input[name='prpLPayCustomVo.certifyType']").val("01");
	}else{
		$("input[name='prpLPayCustomVo.certifyType']").val("99");
	}
	//提交
	$.ajax({
		url : "/claimcar/payCustom/modPayCustomInfo.do", // 后台处理程序
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false, 
		success : function(result) {// 回调方法，可单独定义
			saveAcc();
//			after(result);
		}
	});
}

function saveAcc() {
	var params = $("#PayCustomEditForm").serialize();
	$.ajax({
		url : "/claimcar/accountInfo/saveData.do", // 后台处理程序
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(result) {// 回调方法，可单独定义
			if (result.status == "200") {
				messageText = '操作成功';
				layer.msg(messageText);
				$("#save").hide();
				$("#mod").hide();
			} else {
				success = false;
				messageText = '操作失败：' + result.statusText;
				layer.alert(messageText);
			}
		}
	});
}

function checkSpecialChar(str,flag){
	var pattern;
	if(flag=="payeeName"){
		pattern = "^'!#$%*+,-/:=?@[\]_`{|}~ ……‘！#￥%*+，-。/：；=？@【、】——{|}~ ";//特殊字符
	}else{
		pattern = "^'!#$%*+,-./:=?@[\]_`{|}~ ……‘！#￥%*+，-。/：；=？@【、】——·{|}~ ";//特殊字符
	}
	if(typeof(str) === "undefined" || str === null || str===""){
		return false;
	}
	for(var i = 0,size = pattern.length;i<size;i++){
		var reg = pattern.substring(i, i+1);
		if(str.indexOf(reg)>-1){
			layer.alert("包含特殊字符“"+reg+"”,请核实！");
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
			alert("包含特殊字符“"+reg+"”,请核实！");
			return false;
		}
	}
	return true;
}

function checkSpecialByAccount(str){
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

function checkSpecialCharactor(element,flag){
	var str = $(element).val();
	checkSpecialChar(str,flag);
}

function checkSpecialByAccountNo(element){
	var str = $(element).val();
	checkSpecialByAccount(str);
}

function checkCertifyNo(){
	 var certifyNo = $("[name='prpLPayCustomVo.certifyNo']").val();
	 var certifyType = $("[name='prpLPayCustomVo.certifyType']").val();
	 var regs = new RegExp("^(.{9}|.{18})$");
	 //证件类型为组织机构代码时，证件号码只能是9位或者18位
	 if(certifyType == "10"){
		 if(!regs.test(certifyNo)){
			 layer.alert("证件类型为组织机构代码时，证件号码只能是9位或者18位!"); 
			 return false;
		 }
	 }
	 return true;
}
function changeCertifyNo(obj){
	toUpperValue(obj);
	checkCertifyNo();
}