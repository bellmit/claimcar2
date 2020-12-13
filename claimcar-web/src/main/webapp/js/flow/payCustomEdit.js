/**
 * 收款人信息维护js
 */

var provN, cityN, provC, cityC, bname, bankOutlets, bQuery, provCode, cityCode, bankKindCode;
var layIndex;
var ajaxEdit;
var ajaxEditCust;	
$(function (){
	var compensateNo = parent.$("[name='prpLCompensate.compensateNo']").val();
	$("#compensateNo").attr("value",compensateNo);
	var flags = $("#flags").val();
	if(flags=='Y'){
		$("select").attr("disabled","disabled");
	}
	//初始化时，如果被保险人为修理厂，则修理厂名称标签显示，否则隐藏；
	var index=$("select[id='payObjectKind']").find("option:selected").val();
	if(index!='6'){
		$("#repairfactoryName").hide();
		$("#payeeName").removeAttr("readonly");
		$("#accountNo").removeAttr("readonly");
		}else{
			$("#payeeName").attr("readonly","readonly");
			$("#accountNo").attr("readonly","readonly");
		}
	//addAML();//判断是否加载页面
	initCerti();
	payObjectKindChange();//例外原因
	changeValidOfCertifyNo();// 身份证号机构号校验的改变
	findCInsuredInfo();//判断是否为被保险人并带入承保人信息
	$("#payCustomProvCity_lv1").css("width","34%");
	$("#payCustomProvCity_lv2").css("width","49%");
	$("#payCustomProvCity_lv2").attr("class","areaselect");
	
	ajaxEdit = new AjaxEdit($('#PayCustomEditFormFrost'));
	ajaxEdit.targetUrl = "/claimcar/payCustom/savePayCustomInfo.do"; 
	ajaxEdit.afterSuccess = after;
	
	// 绑定表单
	ajaxEdit.bindForm();
	
	ajaxEditCust = new AjaxEdit($('#PayCustomEditForm'));
	ajaxEditCust.targetUrl = "/claimcar/payCustom/getCustRiskInfoOrSave.do";
	ajaxEditCust.beforeSubmit = saveBeforeCheck;
	ajaxEditCust.afterSuccess = afterCust;
	
	// 绑定表单
	ajaxEditCust.bindForm();
				
});

function after(result) {
	var nodeCode = $("#nodeCode").val();
	var payId=eval(result).data;
	if(payId!=null){
		showPayResult(payId,nodeCode);
	}
	/* if(eval(result).data!=null&&$("#nodeCode").val()=="Chk"){
		//将获取的保存成功的VoID传给页面显示
		showPayResult(eval(result).data,$("#nodeCode").val());
	}else if(eval(result).data!=null&&$("#nodeCode").val()=="DLProp"){
		
	} */
	if(nodeCode=="lawFirmNode"){
		parent.$("[name='prpdLawFirmVo.payCustomId']").val(eval(result).data);
	}
	//保存成功，关闭当前窗口
	
	closeLayer();
}

function saveBeforeCheck(){
	var payObjectKind = $("[name='prpLPayCustomVo.payObjectKind']");
	var otherCause = $("#otherCause");
	if(payObjectKind.val() != 2 && isEmpty(otherCause)){
		otherCause.focus();
		layer.msg("收款人非被保险人必须录入例外原因！");
		return false;
	}
}


function afterCust(jsonData) {
	// 回调方法，可单独定义
    var result = eval(jsonData);
	if (result.status == "200" ) {
		msg =result.data;
		if("N"==msg){
			//layer.alert(msg.msgType);
			//return true;
			//payCustomSubmit();
			closeLayer();
		}/*else if("1"==msg.msgType){//黑名单
			
			index =layer.confirm(msg.msgInfo, {
			    btn: ['是','否'] //按钮
			}, function(index){
				//跳转新增风险交易界面
				getCustomerFxUrl();
				layer.close(index);
				closeLayer();
			}, function(){
				layer.confirm('输入客户名称，若发现是客户与恐怖分子重名的场景，可点击解冻按钮，需填写解决理由后，可继续理赔操作', {
				    btn: ['确定'] //按钮
				}, function(index){
					layer.close(index);
					closeLayer();
				});
				layer.alert("输入客户名称，若发现是客户与恐怖分子重名的场景，可点击解冻按钮，需填写解决理由后，可继续理赔操作");
				closeLayer();
			});
			flag = "1";
			//return false;
		}else if("2"==msg.msgType){
			layer.confirm(msg.msgInfo, {
			    btn: ['确定'] //按钮
			}, function(index){
				layer.close(index);
				closeLayer();
			});
		}else if("-1"==msg.msgType){
			flag = "1";
			layer.confirm(msg.msgInfo, {
			    btn: ['确定'] //按钮
			}, function(index){
				layer.close(index);
				closeLayer();
			});
			//payCustomSubmit();
			//return false;
		}else if("3"==msg.msgType){
			closeLayer();
		}*/
		
	}else{
		layer.alert(result.statusText);
	}
		

}

function payCustomSubmit(){
	//校验摘要管控
//	var str = $("[name='prpLPayCustomVo.summary']").val();
	var payeeName = $("#payeeName").val();
	var accountNo = $("#accountNo").val();
//	if(!checkSpecialChar(str,"summary")){
//		return false;
//	}
	if(!checkSpecialChar(payeeName,"payeeName")){
		return false;
	}
	if(!checkSpecialByAccount(accountNo)){
		return false;
	}
	//校验是否黑名单之类
	/*var certifyNo = $("[name='prpLPayCustomVo.certifyNo']").val();
	var id = $("[name='prpLPayCustomVo.id']").val();
	if(!getCustRiskInfo(payeeName,certifyNo,id)){
		alert("12223");
		return false;
	}*/
	// 如果当前案件为诉讼案件  
	var lawSuitFlag = parent.$("[name='prpLCertifyMainVo.lawsuitFlag']:checked").val();
	var disFlag = $("#certiNotModiPay").val();// 判断该收款人是否已经交互收付  交互后控件被disable 在提交前需要恢复
	if(lawSuitFlag=='1'&&disFlag=='Y'){
		$("#mainInfo input").prop("disabled",false);//所有主页面的信息提交前取消disable
		$("#mainInfo select").prop("disabled",false);//所有主页面的信息提交前取消disable
	}
	
	if($("[name='prpLPayCustomVo.payObjectKind']").val()=="2"){
		//alert("收款人类型为被保险人");
		if($("[name='prpLPayCustomVo.payeeName']").val()==$("[name='insuredName']").val()){
			//alert("收款人户名和被保险人一致");
			//此处校验收款人类型为被保险人时户名和被保险人需要一致
			//提交
			 validAccountIsSame(payeeName,accountNo,null,null);
		//	$("#PayCustomEditForm").submit();
		}else{
			layer.msg("收款人为被保险人时银行户名需与被保险人一致");
		}
	}else{
		 validAccountIsSame(payeeName,accountNo,null,null);
	//	$("#PayCustomEditForm").submit();
	}
	if(lawSuitFlag=='1'&&disFlag=='Y'){
		$("#mainInfo input").prop("disabled",true);//恢复disable
		$("#mainInfo select").prop("disabled",true);//恢复disable
		$("[name='prpLPayCustomVo.summary']").prop("disabled",false);
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

//被保险人不用录入例外原因
function payObjectKindChange(){
	var value = $("[name='prpLPayCustomVo.payObjectKind']").val();
	if(value == 2){
		$("#otherCause").val("");
		$("#otherCause").attr("disabled","disabled");
		$("#otherCauseTemp").removeAttr("disabled");
	}else{
		$("#otherCause").removeAttr("disabled");
		$("#otherCauseTemp").attr("disabled","disabled");
	}
}


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

    if (startD != "" && endD != "" && d1 >= d2) {
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

    if (startD != "" && endD != "" && d1 >= d2) {
	  layer.msg("开始时间不能大于结束时间！");  
	  $("[name='prpLPayCustomVo.certifyEndDate']").val(null);
	 }
 });
 
 function findCInsuredInfo(){//收款人类型为被保险人时查詢代入数据
	 var flag = $("input[name='flag']").val();
	 var certifyNo = $("[name='prpLPayCustomVo.certifyNo']").val();
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
						if(isBlank(certifyNo)){
							$("[name='prpLPayCustomVo.certifyNo']").val(cNo);
						}
						$("[name='prpLPayCustomVo.payeeName']").val(iName);
					}
					//$("[name='prpLPayCustomVo.PermitNo']").val(identifyNumber); //在打开AML事件中判断和赋值
				}
			}
		});
	}
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
	 if($("[name='prpLPayCustomVo.payObjectKind']").val()=="4" 		 
		 ||$("[name='prpLPayCustomVo.certifyType']").val()!="01"){
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
	var viewFlag = $("#viewFlag").val();
	if(flag=='Y'){//flag为是否为补充反洗钱流程的标志，Y-是，N-否
		$("#mainInfo input").prop("readonly",true);//所有主页面的信息不允许修改
		//读取收款人性质 个人or机构显示相应的反洗钱信息页面
		if(viewFlag!='1'){
			var pid = $("[name='prpLPayCustomVo.id']").val();
			var registNo = $("#registNo").val();
			var claimNo='';
			var params = {
					"pid" : pid,
					"registNo" : registNo,
					"claimNo":claimNo
			};
			var url = "/claimcar/payCustom/addRowInfo.ajax";
			$.post(url, params, function(result) {
				$("#AML").append(result);
				$("#nationality").select2();
				$("[name='prpLFxqFavoreeVo.favoreenNatioNality']").select2();
			});
		}

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
	
	if($("[name='prpLPayCustomVo.payObjectKind']").val()=="2"){
		if($("[name='prpLPayCustomVo.payeeName']").val()==$("[name='insuredName']").val()){
			//此处校验收款人类型为被保险人时户名和被保险人需要一致
			//提交
			$.ajax({
				url : "/claimcar/payCustom/modPayCustomInfo.do", // 后台处理程序
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				data : params, // 要传递的数据
				async:false, 
				success : function(result) {// 回调方法，可单独定义
					saveAcc();
					after(result);
				}
			});
		}else{
			layer.msg("收款人为被保险人时银行户名需与被保险人一致");
		}
	}else{
		//提交
		$.ajax({
			url : "/claimcar/payCustom/modPayCustomInfo.do", // 后台处理程序
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async:false, 
			success : function(result) {// 回调方法，可单独定义
				saveAcc();
				after(result);
			}
		});
	}
}

function saveAcc(){
	 var params = $("#PayCustomEditForm").serialize();
	 $.ajax({
			url : "/claimcar/accountInfo/saveData.do", // 后台处理程序
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async:false, 
			success : function(result) {// 回调方法，可单独定义
				if (result.status == "200") {
					messageText = '操作成功';
					layer.msg(messageText);
					 $("#save").hide();
					 $("#mod").hide(); 
				} else {
					success= false;
					messageText = '操作失败：' + result.statusText;
					layer.alert(messageText);
				}
			}
		});

}

function initCerti(){
	var disFlag = $("#certiNotModiPay").val();// 判断该收款人是否已经交互收付  交互后不可修改
	if(disFlag=='Y'){
		$("#mainInfo input").prop("disabled",true);//所有主页面的信息不允许修改
		$("#mainInfo select").prop("disabled",true);//所有主页面的信息不允许修改
		// 如果当前案件为诉讼案件  
		var lawSuitFlag = parent.$("[name='prpLCertifyMainVo.lawsuitFlag']:checked").val();
		if(lawSuitFlag=='1'){
			$("[name='prpLPayCustomVo.summary']").prop("disabled",false);
		}else{
			$("div.btn-footer").hide();
		}
	}
}

/**
 * 
 * @param str
 * @returns {Boolean}
 */
function checkSpecialChar(str,flag){
	//var pattern = "^‘'！!#$%*+，,-./:：=？?@【】[\]_`·{|}~ ";//特殊字符
	var pattern;
	if(flag=="payeeName"){
        pattern = "^'!#$%*+,/:=?@[\]_`{|}~ ……‘！#￥%*+，。/：；=？@【】——{|}~ ";//特殊字符
    }else{
        pattern = "^'!#$%*+,./:=?@[\]_`{|}~ ……‘！#￥%*+，-。/：；=？@【】——{|}~ ";//特殊字符
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

/**
 * 冻结解冻
 * @returns {Boolean}
 */
function payCustomFrost(flags){
	
	$("#frostFlag").val(flags);
	$("#frostFlags").val("2");//统一赋值为2
	//校验摘要管控
//	var str = $("[name='prpLPayCustomVo.summary']").val();
	var payeeName = $("#payeeName").val();
	var accountNo = $("#accountNo").val();
//	if(!checkSpecialChar(str,"summary")){
//		return false;
//	}
	if(!checkSpecialChar(payeeName,"payeeName")){
		return false;
	}
	if(!checkSpecialByAccount(accountNo)){
		return false;
	}
	// 如果当前案件为诉讼案件  
	var lawSuitFlag = parent.$("[name='prpLCertifyMainVo.lawsuitFlag']:checked").val();
	var disFlag = $("#certiNotModiPay").val();// 判断该收款人是否已经交互收付  交互后控件被disable 在提交前需要恢复
	if(lawSuitFlag=='1'&&disFlag=='Y'){
		$("#mainInfo input").prop("disabled",false);//所有主页面的信息提交前取消disable
		$("#mainInfo select").prop("disabled",false);//所有主页面的信息提交前取消disable
	}
	
	if($("[name='prpLPayCustomVo.payObjectKind']").val()=="2"){
		//alert("收款人类型为被保险人");
		if($("[name='prpLPayCustomVo.payeeName']").val()==$("[name='insuredName']").val()){
			//alert("收款人户名和被保险人一致");
			//此处校验收款人类型为被保险人时户名和被保险人需要一致
			//提交
			if(flags=="1"){//冻结不用输入原因
				$("#reason").removeAttr("datatype");
			}
			$("#PayCustomEditFormFrost").submit();
		}else{
			layer.msg("收款人为被保险人时银行户名需与被保险人一致");
		}
	}else{
		if(flags=="1"){//冻结不用输入原因
			$("#reason").removeAttr("datatype");
		}
		$("#PayCustomEditFormFrost").submit();
	}
	if(lawSuitFlag=='1'&&disFlag=='Y'){
		$("#mainInfo input").prop("disabled",true);//恢复disable
		$("#mainInfo select").prop("disabled",true);//恢复disable
		$("[name='prpLPayCustomVo.summary']").prop("disabled",false);
	}
}

/**
 * 保存
 * @returns {Boolean}
 */
function getCustRiskInfo(){
	//反洗钱提交时，去除页面中所有下拉框的disabled属性
	var flagss = $("#flags").val();
	if(flagss=='Y'){
		$("select").prop("disabled",false);
	}
	//校验是否黑名单之类
    var payeeName = $("#payeeName").val();
	var certifyNo = $("[name='prpLPayCustomVo.certifyNo']").val();
	var id = $("[name='prpLPayCustomVo.id']").val();
	var paysign='0';
	/*新增账户信息提交时，如系统已支付信息中有最近一条数据账号一致，
	用户名不一致，弹出确认框“系统中{报案号}账号{账号}户名{户名}已有支付成功信息，
	与当前户名{当前户名}不一致，请确认是否修改该账号的户名！”，软控制。*/
	var accountNo = $("#accountNo").val();//收款人账号
	if(accountNo.length > 32){
		layer.alert("收款人账号最大为32位");
		return false;
	}
    var msg = "";
    var params = {
        "payeeName": payeeName,
			      "certifyNo":certifyNo,
			      "id":id
	                };

	//校验摘要管控
//	var str = $("[name='prpLPayCustomVo.summary']").val();
	var payeeName = $("#payeeName").val();
	var accountNo = $("#accountNo").val();
//	if(!checkSpecialChar(str,"summary")){
//		return false;
//	}
	if(!checkSpecialChar(payeeName,"payeeName")){
		return false;
	}
	if(!checkSpecialByAccount(accountNo)){
		return false;
	}
	if(!checkCertifyNo()){
		return false;
	}
	// 如果当前案件为诉讼案件  
	var lawSuitFlag = parent.$("[name='prpLCertifyMainVo.lawsuitFlag']:checked").val();
	var disFlag = $("#certiNotModiPay").val();// 判断该收款人是否已经交互收付  交互后控件被disable 在提交前需要恢复
	if(lawSuitFlag=='1'&&disFlag=='Y'){
		$("#mainInfo input").prop("disabled",false);//所有主页面的信息提交前取消disable
		$("#mainInfo select").prop("disabled",false);//所有主页面的信息提交前取消disable
	}
	
	if($("[name='prpLPayCustomVo.payObjectKind']").val()=="2"){
		//alert("收款人类型为被保险人");
		if($("[name='prpLPayCustomVo.payeeName']").val()==$("[name='insuredName']").val()){
			//alert("收款人户名和被保险人一致");
			//此处校验收款人类型为被保险人时户名和被保险人需要一致
			//提交
			if(!isBlank(accountNo)){
                params1 = {
                    "payeeName": payeeName,
						"accountNo":accountNo,
						 "paycustId":id
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
								layer.close(index);
                                    // validAccountIsSame(payeeName,accountNo,lawSuitFlag,disFlag);
									$("#PayCustomEditForm").submit();
								/*$("#PayCustomEditForm").submit();
								if(lawSuitFlag=='1'&&disFlag=='Y'){
										$("#mainInfo input").prop("disabled",true);//恢复disable
										$("#mainInfo select").prop("disabled",true);//恢复disable
										$("[name='prpLPayCustomVo.summary']").prop("disabled",false);
								}*/
							},function(index){
									 layer.close(index);
							});
								
						}else{
                                // validAccountIsSame(payeeName,accountNo,lawSuitFlag,disFlag);
								$("#PayCustomEditForm").submit();
							// $("#PayCustomEditForm").submit();
						}
				}else{
					var statusText1=result.statusText;
					layer.alert(statusText1);
				}
		    }
				 });
				
			   }else{
                //validAccountIsSame(payeeName,accountNo,lawSuitFlag,disFlag);
				$("#PayCustomEditForm").submit();
				   /*$("#PayCustomEditForm").submit();
					if(lawSuitFlag=='1'&&disFlag=='Y'){
						$("#mainInfo input").prop("disabled",true);//恢复disable
						$("#mainInfo select").prop("disabled",true);//恢复disable
						$("[name='prpLPayCustomVo.summary']").prop("disabled",false);
					}*/
			   }
			
			//$("#PayCustomEditForm").submit();
		}else{
			layer.msg("收款人为被保险人时银行户名需与被保险人一致");
		}
	}else{
		if(!isBlank(accountNo)){
            params1 = {
                "payeeName": payeeName,
					"accountNo":accountNo,
					"paycustId":id
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
                                // validAccountIsSame(payeeName,accountNo,lawSuitFlag,disFlag);
								$("#PayCustomEditForm").submit();
							/*$("#PayCustomEditForm").submit();
							if(lawSuitFlag=='1'&&disFlag=='Y'){
									$("#mainInfo input").prop("disabled",true);//恢复disable
									$("#mainInfo select").prop("disabled",true);//恢复disable
									$("[name='prpLPayCustomVo.summary']").prop("disabled",false);
							}
								layer.close(index);*/
						},function(index){
								 layer.close(index);
						});
							
					}else{
                            // validAccountIsSame(payeeName,accountNo,null,null);
							$("#PayCustomEditForm").submit();
					//	$("#PayCustomEditForm").submit();
					}
				}else{
					var statusText1=result.statusText;
					layer.alert(statusText1);
				}
			   }
			 });
			
		   }else{
            // validAccountIsSame(payeeName,accountNo,lawSuitFlag,disFlag);
			$("#PayCustomEditForm").submit();
			  /* $("#PayCustomEditForm").submit();
				if(lawSuitFlag=='1'&&disFlag=='Y'){
					$("#mainInfo input").prop("disabled",true);//恢复disable
					$("#mainInfo select").prop("disabled",true);//恢复disable
					$("[name='prpLPayCustomVo.summary']").prop("disabled",false);
				}*/
		   }
		
		
	}
	
}

function validAccountIsSame(payeeName,accountNo,lawSuitFlag,disFlag){
	$.ajax({
		url : "/claimcar/payCustom/validAccountIsSame.ajax", // 后台校验
	    type : 'post', // 数据发送方式
	    dataType : 'json', // 接受数据格式
	    data : {
	    	'payeeName':payeeName,
	    	'accountNo':accountNo
	    }, // 要传递的数据
	    async : false,
	   success : function(jsonData) {// 回调方法，可单独定义
		   var sign="";
		   var array=new Array();
	       var result = eval(jsonData);
	    if (result.status == "200" ) {
			  var statusText1=result.statusText;
			  sign =result.data;
			if(sign=='1'){
				var index=layer.confirm("银行账号:{" + accountNo + "}在收付系统已存在,且户名为{" + statusText1 + "},与您当前录入信息不一致，请核实确认是否将收付收款人户名更新为当前录入的户名：{"+payeeName+"}",
                        {
                            btn: ['是', '否']
				},function(index){
					$("#PayCustomEditForm").submit();
					if(lawSuitFlag=='1'&&disFlag=='Y'){
							$("#mainInfo input").prop("disabled",true);//恢复disable
							$("#mainInfo select").prop("disabled",true);//恢复disable
							$("[name='prpLPayCustomVo.summary']").prop("disabled",false);
					}
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

/**
 * 获取地址链接
 * @returns {Boolean}
 */
function getCustomerFxUrl(){
	$.ajax({
		url : "/claimcar/compensate/getFxUrl.ajax", // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		async : false,
		success : function(jsonData) {// 回调方法，可单独定义
			window.open(jsonData.data);
		    var result = eval(jsonData);
			if (result.status == "200" ) {
				nameList=result.data;
				
			}
		}
	});
}

function payCustomFreeFrost(flags){
	//layer.alert("21312");
	/*layer.prompt({
		title : '解冻原因',
		formType : 2
	// prompt风格，支持0-2
	}, function(text) {
		$("textarea[name='prpLDlossPersTraceVo.operatorDesc']").append(myDate.getFullYear() + "年" + (myDate.getMonth() + 1) + "月" + myDate.getDate() + "日  " + $("#userName").val()+" ：" + text+"\n");
		layer.msg('成功');
		$("#reason").val(text);
		//payCustomFrost(flags);
	});*/
	$("#reason").removeClass("hidden");
	$resDiv = $("#frostReason");
	layer.open({
		  type: 1,
		  shade: false,
		  area: ['400px', '200px'],
		  title: "解冻原因", 
		  closeBtn: true,
		  content: $resDiv,
		  btn: ['确定'],
		  yes: function(index){
			  layer.close(index);
			payCustomFrost(flags);
			$("#reason").addClass("hidden");
		  },cancel: function(index){
			  layer.close(index);
			  $("#reason").addClass("hidden");
		  }
		});
	
}
