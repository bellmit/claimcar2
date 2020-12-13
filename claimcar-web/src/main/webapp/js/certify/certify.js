var vinNoFlag =true;
$(function() {
	$("input[name$=isCheck]").each(function(){
     var values=$(this).val();
     if(values=='1'){
    	 $(this).prop("checked",true);
     }else{
    	 $(this).prop("checked",false);
     }
     
	}
		
	);
	
	//拒赔
	var isBInotpayFlag1=$("[name='prpLCertifyMainVo.isSYFraud']:checked").val();
	var isCInotpayFlag1=$("[name='prpLCertifyMainVo.isJQFraud']:checked").val();
	if(isBInotpayFlag1=='1' || isCInotpayFlag1=='1'){
		$("#payCause").removeClass("hide");
	}else{
		$("select[name='prpLCertifyMainVo.newNotpaycause']").val("");
	}
	var notpayCause1= $("[name='prpLCertifyMainVo.newNotpaycause']").val();
	if(notpayCause1=='5'){
		$("#otherPayCause").removeClass("hide");
	}else{
		$("input[name='prpLCertifyMainVo.othernotPaycause']").val("");
	}
	
	$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon",
			"current", "click", "0");
	$.Datatype.letterAndNumber=/^[A-Za-z0-9]+$/;//字母和数字
	$.Datatype.vinNo = function(gets,obj,curform,regxp){
        //参数gets是获取到的表单元素值，
        //obj为当前表单元素，
        //curform为当前验证的表单，
        //regxp为内置的一些正则表达式的引用。
        //return false表示验证出错，没有return或者return true表示验证通过。
		var code = $(obj).val();
		if(code.length==0){
			return false;
		}
		if(!validVINCode(code)){
			vinNoFlag =false;
			//return false;
		}else{
			vinNoFlag =true;
		}
    };
	createRegistRisk();
	ciIndemDuty();
	validFraud();
	showRefuseReason();
	//initClaimEndTime();
	var status = $("#handlerStatus").val();
	if(status == "3"){//已处理
		//$(".btn").attr("class","btn  btn-disabled mb-10");
		$(":input").prop("readOnly",true);
		$(":input").prop("disabled",true);
		$(".btn").prop("disabled",false);
		$(":checkbox").prop("disabled",true);
		$("#save").hide();
		$("#tempSave").hide();
		/*if($("#certifyMakeup").val() == "yes"){
			$("#certifyUpload").attr("disabled",false);
			$("#certifyList").attr("disabled",false);
			$("#certifyUpload").attr("class","btn  btn-primary mb-10");
			$("#certifyList").attr("class","btn  btn-primary mb-10");
		}*/
	}else{
		if($("#reCaseFlag").val() == "1"){//重开赔案
			$(".addLoss").show();
		}
	}
	//索赔清单
	$("#certifyList").click(function(){
		var registNo = $("#registNo").val();
		var taskId = $("#taskId").val();
		var certifyMakeup = $("#certifyMakeup").val();
		certifyList(registNo,taskId,certifyMakeup);
	});
	//单证上传
	$("#certifyUpload").click(function(){
		var hasCertify = $("#hasCertify").val();
		var taskId = $("#taskId").val();
		if(typeof hasCertify == 'undefined'){
			layer.alert("未勾选单证！");
			return;
		}
		uploadCertifys(taskId);
	});
	//影像查看
	$("#certifyView").click(function(){
		var registNo = $("#registNo").val();
		viewCertifys(registNo);
	});
	//承保影像查看
	$("#policyCertify").click(function(){
		var policyNo = $("#policyNo").val();
		viewCertifys(policyNo);
	});
	//关联保单影像查看
	$("#policyNoLinkCertify").click(function(){
		var policyNoLink = $("#policyNoLink").val();
		viewCertifys(policyNoLink);
	});
	//案件备注
	$("#messageInfo").click(function(){
		var registNo = $("#registNo").val();
		var nodeCode = $("#nodeCode").val();
		 createRegistMessage(registNo, nodeCode);
	});
	//诉讼
	$("#lawsuit").click(function(){
		var registNo = $("#registNo").val();
		var nodeCode = $("#nodeCode").val();
		lawsuit(registNo,nodeCode);
	});
	//风险提示
	$("#riskInfo").click(function(){
		createRegistRisk();
	});
	//收款人信息
	$("#payCustom").click(function(){
		payCustomSearch('N');
	});
	//页面刷新
	$("#refresh").click(function(){
		layer.load();
		location.reload();
	});
	
	//是否拒赔
	$("[name='prpLCertifyMainVo.isSYFraud']").click(function(){
		var isBInotpayFlag= $(this).val();
		var isCInotpayFlag=$("[name='prpLCertifyMainVo.isJQFraud']:checked").val();
		if(isBInotpayFlag=='1'){
			$("#payCause").removeClass("hide");
		}else if((isBInotpayFlag=='0' && isCInotpayFlag=='0') ||(isBInotpayFlag=='0' && isBlank(isCInotpayFlag)) ){
			$("#payCause").addClass("hide");
			$("#otherPayCause").addClass("hide");
			$("select[name='prpLCertifyMainVo.newNotpaycause']").val("");
			$("input[name='prpLCertifyMainVo.othernotPaycause']").val("");
		}
	});
	
	$("[name='prpLCertifyMainVo.isJQFraud']").click(function(){
		var isCInotpayFlag2= $(this).val();
		var isBInotpayFlag2=$("[name='prpLCertifyMainVo.isSYFraud']:checked").val();
		if(isCInotpayFlag2=='1'){
			$("#payCause").removeClass("hide");
		}else if((isBInotpayFlag2=='0' && isCInotpayFlag2=='0') || (isCInotpayFlag2=='0' && isBlank(isBInotpayFlag2))){
			$("#payCause").addClass("hide");
			$("#otherPayCause").addClass("hide");
			$("select[name='prpLCertifyMainVo.newNotpaycause']").val("");
			$("input[name='prpLCertifyMainVo.othernotPaycause']").val("");
		}
	});
	$("[name='prpLCertifyMainVo.newNotpaycause']").click(function(){
		var notpayCause=$(this).val();
		if(notpayCause=='5'){
			$("#otherPayCause").removeClass("hide");
			$("#otherNotpayCause").attr("datatype","*");
		}else{
			$("#otherPayCause").addClass("hide");
			$("#otherNotpayCause").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
			$("input[name='prpLCertifyMainVo.othernotPaycause']").val("");
		}
	});
	
	$("input[name='prpLCertifyMainVo.manCompenCause']").click(function(){
		var str=",";
		$("input[name='prpLCertifyMainVo.manCompenCause']:checked").each(function(){
			str=str+$(this).val()+",";
		});
		
		if(str.indexOf('4')==-1){
			$("#otherManCompenCause").addClass("hide");
			$("#compensateCause").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
			$("input[name='prpLCertifyMainVo.otherManCompenCause']").val("");
		}else{
			$("#otherManCompenCause").removeClass("hide");
			$("#compensateCause").attr("datatype","*");
		}
		
	});
	var cause=$("#manCompenCause").val();
	if(!isBlank(cause)){
		if(cause.indexOf('4')==-1){
			$("#otherManCompenCause").removeClass("hide");
		}
	}
});

function reloadInfo(){
	location.reload();
}
//定损追加
	$("#lossAdd").click(function(){
		var registNo = $("#registNo").val();
		var url="/claimcar/defloss/LossAddInit.do?registNo="+registNo;
		FaIndex=layer.open({
			type : 2,
			area : [ '90%', '100%' ],
			fix : true,
			maxmin : true,
			title:"定损追加",
			content : url
		});
	});
function saveCertify(type){
	var isBInotpayFlag1=$("[name='prpLCertifyMainVo.isSYFraud']:checked").val();
	var isCInotpayFlag1=$("[name='prpLCertifyMainVo.isJQFraud']:checked").val();
	var newNotpaycause=$("select[name='prpLCertifyMainVo.newNotpaycause']").val();
	if(isBInotpayFlag1=='1' || isCInotpayFlag1=='1'){
		if(isBlank(newNotpaycause)){
			layer.alert("拒赔原因不能为空！");
			return false;
		}
	}
	$("#saveType").val(type);
	var rules = {};
	var ajaxEdit = new AjaxEdit($('#editform'));
	ajaxEdit.beforeCheck = function() {// 校验之前
	};
	ajaxEdit.beforeSubmit = function() {// 提交前补充操作
		var hasCertify = $("#hasCertify").val();
		if(!checkSubrogation()){
			return false;
		}
		if(typeof hasCertify == 'undefined'){
			layer.alert("请勾选至少一条索赔清单信息！");
			return false;
		}
		if(!checkCustomClaimTime()){
			layer.alert("客户索赔时间不能早于报案时间");
			return false;
		}
		if(!checkDuty()){
			layer.alert("总的事故责任比例必须为100%");
			return false;
		}
		var flag = checkLawSuit ();
		console.log("flag", flag);
		if ( $("input[name = 'prpLCertifyMainVo.lawsuitFlag']:checked").val() == '1' && !flag) {
			layer.msg("请点击上方诉讼按钮，并录入诉讼信息");
			return false;
		}
		var saveType = $("#saveType").val();
		if(saveType == "submit"){
			var customClaimTime = $("#customClaimTime").val();
			if(customClaimTime == ''){
				layer.alert("客户索赔时间不能为空");
				return false;
			}
			var sign="";
		if(saveType == "submit"){
			var registNo=$("#registNo").val();
			params1={"registNo":registNo};
			$.ajax({
				url : "/claimcar/compensate/validCaseState.ajax", // 后台校验
			    type : 'post', // 数据发送方式
			    dataType : 'json', // 接受数据格式
			    data : params1, // 要传递的数据
			    async : false,
			   success : function(jsonData) {// 回调方法，可单独定义
				   
				var result = eval(jsonData);
			     if (result.status == "200" ){
				    sign =result.data;
			     }
			   }
					
			 });
			
			if(sign=='0'){
				alert("快赔案件核损金额客户尚未确认！");
				return false;
			}else if(sign=='1'){
				alert("快赔案件客户不同意核损金额，已转线下处理，请核实！");
				return false;
			}
			var autosign='3';
			$.ajax({
				url : "/claimcar/compensate/validAutoCaseState.ajax", // 后台校验
			    type : 'post', // 数据发送方式
			    dataType : 'json', // 接受数据格式
			    data : params1, // 要传递的数据
			    async : false,
			   success : function(jsonData) {// 回调方法，可单独定义
				var result = eval(jsonData);
			     if (result.status == "200" ){
			    	 autosign =result.data;
			     }
			   }
					
			 });
			
			if(autosign=='0'){
				alert("自助理赔案件核损金额客户尚未确认！");
				return false;
			}else if(autosign=='1'){
				alert("自助理赔案件客户不同意核损金额，已转线下处理，请核实！");
				return false;
			}
			
		}
			
			var res = false;
			$(".lossItemCode").each(function(){
				//因格式不一致，为了兼容旧单证编码，故此改动为由于新单证编码
				var itemCode="";
				var array=this.value.split("_");
				if(array.length>1){
					itemCode=array[1];
				}else{
					itemCode=this.value;
				}
				if(itemCode == 'C0101'){//机动车辆保险/小额赔案索赔申请书
					res = true;
				}
			});
			if(!res){
				layer.alert("《机动车辆保险/小额赔案索赔申请书》为必须勾选的单证！");
				return false;
			}
		}
		if(!vinNoFlag){
			if(!window.confirm("VIN码不符合校验规则，请提交电子联系单进行特批处理!")){
				return false;
			}
		}
//		if($("input[name='subrogationMain.subrogationFlag']:checked").val() =="1"){
//			var isLock = $("#isLock").val();
//			if(isLock == "N"){
//				layer.alert("此案件为代位求偿案件但未锁定对方，不允许提交！");
//				return false;
//			}
//		}
	};
	ajaxEdit.targetUrl = "/claimcar/certify/saveCertifyMain.do";
	ajaxEdit.rules = rules;
	ajaxEdit.afterSuccess = function(data) {// 操作成功后操作
		if(data.data != ""){
			if (data.data == "NoPassPlatform"){
				index = layer.open({
					type : 2,
					title : "补传平台",
					closeBtn : 0,
					shadeClose : false,
					scrollbar : false,
					skin : 'yourclass',
					area : [ '800px', '600px' ],
					content : [ "/claimcar/certify/initNoPassPlatformNodeBeforeCertify.do?registNo="+$("#registNo").val() ]
				});
				return false;
			}else{
				layer.alert(data.data);
			}
		}else{
			layer.confirm("操作成功", {
				btn: ['确定'] //按钮
			}, function(index){
				location.reload();
				layer.load();
			});
		}
	};
	// 绑定表单
	ajaxEdit.bindForm();
	if("submit"==type){
		//单证提交时提示"发票金额为0，理算环节将不会赔付，是否继续提交？"点击是可继续提交，点击否，单证不提交，可返回修改
		var lossSumVeriLossFee = 0;
		var flag = "1";
		$("[name$='invoiceFee']").each(function(){
			var newValue = $.trim(this.value);
			if(newValue == 0 && newValue !=null && newValue != undefined && newValue!=""){
				var name = this.name;
				var lossName = name.split(".")[0];
				lossName = lossName+".sumVeriLossFee";
				lossSumVeriLossFee = $("[name='"+lossName+"']").val();
				if(Number(lossSumVeriLossFee) > 0){
					flag = "0";
				}
				var lossPersTraceName = name.split("[")[0];
				if(lossPersTraceName == "lossPersTraceList"){
					lossName = name.split(".")[0]+".sumVeriDefloss";
					lossSumVeriLossFee = $("[name='"+lossName+"']").val();
					if(Number(lossSumVeriLossFee) > 0){
						flag = "0";
					}
				}
				
			}
			if(flag == "0"){
				return false;
			}
		});
		if(lossSumVeriLossFee > 0){
			layer.confirm('发票金额为0，理算环节将不会赔付，是否继续提交?', {
				  btn: ['是','否'] //按钮
				}, function(){
					layer.closeAll();
					$("#editform").submit();
				}, function(){
					layer.closeAll();
				});
		}else{
			$("#editform").submit();
		}
	}else{
		$("#editform").submit();
	}
}

//勾选免赔率
function isCheck(element){
	
	var value = $(element).attr("value");
	if(value=="0"||value==""){
		$(element).attr("value","1");
	}else{
		$(element).attr("value","0");
	}
}

//客户索赔时间不能早于报案时间
function checkCustomClaimTime(){  
    var customClaimTime=$("#customClaimTime").val();
    var customClaim=new Date(customClaimTime.replace("-", "/").replace("-", "/"));  
    var reportTime=$("#reportTime").val();  
    var report=new Date(reportTime.replace("-", "/").replace("-", "/")); 
    if(customClaim == '' || report == ''){
   	    return true;
    }
    if(report >	customClaim){  
        return false;  
    }  
    return true;  
}

//初始化索赔材料收集齐全时间
function initClaimEndTime(){
	var directFlag = true;
	$("#certifyItems").find("input:radio:checked").each(function(){
		if($(this).val() == "0"){
			directFlag = false;
			return false;
		}
	});
	if(directFlag){
		$("#claimEndTime").val(getNowFormatDate());
	}else{
		$("#claimEndTime").val("");
	}
}

function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
}


function initCertifyItmes(){
	var tbody = $("#prpLCertifyItems");
	var registNo =$("#registNo").val() ;
	var params = {"registNo":registNo};
	var url = "/claimcar/certify/loadCertifyItems.ajax";
	$.post(url,params, function(result){
		tbody.empty();
		tbody.append(result);
	});
}

function initPayCustom(){
	var tbody = $("#payCustomDiv");
	var registNo =$("#registNo").val() ;
	var params = {"registNo":registNo};
	var url = "/claimcar/certify/loadPayCustom.ajax";
	$.post(url,params, function(result){
		tbody.empty();
		tbody.append(result);
	});
}

/** 责任比例比较
 */
function compareDutyRate(field,index){
	var rate = parseFloat($(field).val());
	if(index == '-1'){
		var indemnityDuty = $("#Iindemnityduty");
	}else{
		var indemnityDuty = $(".Iindemnitydutys:eq("+index+")");
	}
	var minRate = 0;
	var maxRate = 0;
	var rateName ="";
	if(indemnityDuty.val() == "0"){
		if(rate != 100){
			layer.alert("全责的责任比例必须为100%");
			changeDuty(indemnityDuty,index);
			return false;
		}
	  }else if(indemnityDuty.val() == "1"){
		minRate = 0;
	    maxRate = 100;
		rateName ="主责";
	  }else if(indemnityDuty.val() == "2"){
		minRate = 0;
		maxRate = 100;
		rateName ="同责";
	  }else if(indemnityDuty.val() == "3"){
		minRate = 0;
		maxRate = 100;
		rateName ="次责";
	  }else if(indemnityDuty.val() == "4"){
		  if(rate != 0){
			  layer.alert("无责的责任比例必须为0%");
			  changeDuty(indemnityDuty,index);
			  return false;
		  }
	  }
	  if(rate >= maxRate || rate <= minRate) {
		layer.alert(rateName+"的责任比例必须在"+minRate+"%~"+maxRate+"%之间");
		changeDuty(indemnityDuty,index);
	  }
}

function changeDuty(field,index) {
	var duty = $(field).val();
	if(index == "-1"){
		var dutyVa=$("#IindemnityDutyRate");
		ciIndemDuty();
	}else{
		var dutyVa=$(".IindemnityDutyRates:eq("+index+")");
	}
	if (isBlank(duty)) {
		dutyVa.val("");
	} else if (duty == "0") {
        dutyVa.val("100");
	} else if (duty == "1") {
		dutyVa.val("70");
	} else if (duty == "2") {
		dutyVa.val("50");
	} else if (duty == "3") {
		dutyVa.val("30");
	} else if (duty == "4") {
		dutyVa.val("0");
	}
}

//事故责任比例选择有责时，交强险必选选择有责
function ciIndemDuty() {
	var ciDuty=$("#ciIndemDuty");
	var duty = $("#Iindemnityduty").val();
	if (duty!= "4") {
		ciDuty.val("1");
		ciDuty.find("option[value='1']").removeAttr("disabled");
		ciDuty.find("option[value='0']").attr("disabled", "disabled");
	} else {
		ciDuty.val("0");
		ciDuty.find("option[value='0']").removeAttr("disabled");
		ciDuty.find("option[value='1']").attr("disabled", "disabled");
	}
}

//事故比例校验
function checkDuty(){
	var mainDutyVa = $("#IindemnityDutyRate").val();
	var sumRate = parseFloat(mainDutyVa);
	$(".IindemnityDutyRates").each(function(){
		sumRate = sumRate + parseFloat($(this).val());
	});
	if(sumRate != 100){
		return false;
	}
	return true;
	/*
	if($("#isClaimSelf").val() == "1"){
    	layer.alert("互碰自赔案件不可以选无责");
    	return false;
	}*/
}


//代为案件
function checkSubrogation(){
	if($("input[name='subrogationMain.subrogationFlag']:checked").val() =="1"){
		if($("#reportType").val()==2){//单交强报案 不能代位
			layer.msg("单交强案件不能选择代位求偿！");
			$("input[name='checkVo.isSubRogation']").focus();
			return false;
		}
		
		var dutyRate = $("#indemnityDutyRate").val();
		var rateValue = $("#indemnityDutyRateValue").val();
		if(dutyRate=='0' || parseFloat(rateValue)==100){
			layer.msg("代位求偿案件事故责任比例不能为全责或100%");
			$("input[name='checkVo.isSubRogation']").focus();
			return false;
		}
		if($("#subrogationCarSize").val()==0 && $("#subrogationPerSize").val()==0){
			layer.msg("代位求偿案件 责任方为机动车或责任方为非机动车至少录入一项");
			$("input[name='checkVo.isSubRogation']").focus();
			return false;
		}
		//车牌号不能重复
		var items =$("#subrogationCarTable select[name $='licenseNo']");
		var linceNoArray =new Array();
		var retflag = true;
		var focus_index = "";
		var lowIndex=0;
		$(items).each(function(){
			var pre = $(this).attr("name").split("]")[0];//下标
			var index = pre.split("[")[1];//下标
			$("#subrogationCarTable input[name='subrogationMain.prpLSubrogationCars["+index+"].licenseNo']").val($(this).find("option:selected").text());
			if($(this).val()=="0"){
				focus_index = index;
				retflag = false;
			}else{
				linceNoArray[lowIndex]= $(this).find("option:selected").text();
				lowIndex++;
			}
		});
		if(!retflag){
			layer.msg("请选择车牌号码");
			$("#subrogationCarTable select[name='subrogationMain.prpLSubrogationCars["+focus_index+"].serialNo']").focus();
			return false;
		}
		
		linceNoArray=linceNoArray.sort();
		for(var i=0;i<linceNoArray.length;i++){
			if (linceNoArray[i]==linceNoArray[i+1]){ 
				layer.msg("代位求偿信息中"+linceNoArray[i]+"车牌号重复");
				$("#subrogationCarTable select[name='subrogationMain.prpLSubrogationCars["+i+"].serialNo']").focus();
				return false;
			}
		}
		var registNo = $("#registNo").val();
		if(!checkSubrogationFlag(registNo)){
			return false;
		}
	}
	
	return true;
}

function validFraud(){
	var isFraud = $("input[name='prpLCertifyMainVo.isFraud']:checked").val();
	var fraudLogo = $("select[name='prpLCertifyMainVo.fraudLogo']").val();
	var reOpenCase = $("#reCaseFlag").val();   //重开标志
	var isFraudOrigin = $("#isFraudOrigin").val();
	if("1" == isFraud){ //显示欺诈信息
		$("#fraudInfo").show();
	}else{
		$("#fraudInfo").hide();
	}
	if("1" == reOpenCase && "1" == isFraudOrigin ){  //重开赔案之前 欺诈案件 不能修改欺诈信息
		$("input[name='prpLCertifyMainVo.isFraud']").prop("disabled",true);
		$("select[name='prpLCertifyMainVo.fraudLogo']").prop("disabled",true);
		$("select[name='prpLCertifyMainVo.fraudType']").prop("disabled",true);
		$("input[name='prpLCertifyMainVo.fraudRecoverAmount']").prop("disabled",true);
		if(fraudLogo=="02"){
			$("input[name='prpLCertifyMainVo.isJQFraud']:first").prop("checked",true);
			$("input[name='prpLCertifyMainVo.isJQFraud']").prop("disabled",true);
			$("input[name='prpLCertifyMainVo.isSYFraud']:first").prop("checked",true);
			$("input[name='prpLCertifyMainVo.isSYFraud']").prop("disabled",true);
		}
	}else if("1" == isFraud){
		if(fraudLogo=="01"||fraudLogo=="02"){  //欺诈金额必录
			$("input[name='prpLCertifyMainVo.fraudRecoverAmount']").attr("datatype","amount");
		}else{
			$("input[name='prpLCertifyMainVo.fraudRecoverAmount']").removeAttr("datatype");
		}
		if(fraudLogo=="02"&&isFraud=="1"){ //欺诈标志为欺诈拒绝赔付  是否拒赔为 是 不能修改
			$("input[name='prpLCertifyMainVo.isJQFraud']:first").prop("checked",true);
			//$("input[name='prpLCertifyMainVo.isJQFraud']").prop("disabled",true);
			$("input[name='prpLCertifyMainVo.isSYFraud']:first").prop("checked",true);
			//$("input[name='prpLCertifyMainVo.isSYFraud']").prop("disabled",true);
			showRefuseReason();
		}/*else {
			$("input[name='prpLCertifyMainVo.isJQFraud']").removeProp("disabled");
			$("input[name='prpLCertifyMainVo.isSYFraud']").removeProp("disabled");
		}	*/	
	}else{
		//欺诈为否 清空欺诈信息
		$("select[name='prpLCertifyMainVo.fraudLogo']").val("");
		$("select[name='prpLCertifyMainVo.fraudType']").val("");
		$("input[name='prpLCertifyMainVo.fraudRecoverAmount']").val("");
		$("input[name='prpLCertifyMainVo.fraudRecoverAmount']").removeAttr("datatype");
	}
}

function showRefuseReason() {
	var reOpenCase = $("#reCaseFlag").val(); // 重开标志
	var jqFraud = $("input[name='prpLCertifyMainVo.isJQFraud']:checked").val();
	var syFraud = $("input[name='prpLCertifyMainVo.isSYFraud']:checked").val();
	var fraudLogo = $("select[name='prpLCertifyMainVo.fraudLogo']").val();//欺诈原因下拉框
	var isFraud = $("input[name='prpLCertifyMainVo.isFraud']:checked").val();//是否欺诈
	
	var jqOrigin = $("#jqRefuse").val();
	var syOrigin = $("#syRefuse").val();
	if(fraudLogo == "02"&&isFraud=="1"){
		//欺诈标志为欺诈拒绝赔付  是否拒赔为 是 不能修改
		layer.msg("欺诈标志为欺诈拒绝赔付，是否拒赔必须为是");
		$("input[name='prpLCertifyMainVo.isJQFraud']:first").prop("checked",true);
		$("input[name='prpLCertifyMainVo.isSYFraud']:first").prop("checked",true);
	}

	/*if ("1" == jqFraud || "1" == syFraud) { //显示录入拒赔信息
		$("textarea[name='prpLCertifyMainVo.fraudRefuseReason']").attr("datatype","*1-249");
		$("#refuseReason").show();
	} else {
		$("textarea[name='prpLCertifyMainVo.fraudRefuseReason']").removeAttr("datatype");
		$("#refuseReason").hide();
	}*/
	if ("1" == reOpenCase && "1"==jqOrigin) { // 重开后，单证拒赔单选按钮置灰，不能选择。
		layer.msg("拒赔案件,重开赔案后不能修改拒赔信息");
		$("input[name='prpLCertifyMainVo.isJQFraud']:first").prop("checked",true);
		//$("input[name='prpLCertifyMainVo.isJQFraud']").prop("disabled", true);
	}
	
	if("1" == reOpenCase && "1"==syOrigin){
		layer.msg("拒赔案件,重开赔案后不能修改拒赔信息");
		$("input[name='prpLCertifyMainVo.isSYFraud']:first").prop("checked",true);
		//$("input[name='prpLCertifyMainVo.isSYFraud']").prop("disabled", true);
	}
	/*if("1" == reOpenCase && "1"==jqOrigin && "1"==syOrigin){
		$("textarea[name='prpLCertifyMainVo.fraudRefuseReason']").prop("readonly", true);
	}*/
}

$("#lossPhoto").click(function(){
	var registNo=$("#registNo").val();
	var params = {
			"registNo" : registNo,
			"flag":"2"
		};
	$.ajax({
		url : "/claimcar/certify/reqPhotoInfo.do", // 后台处理程序
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async:false, 
		success : function(jsonData) {// 回调方法，可单独定义
			if(jsonData.status=="200"){
				var arrays=eval(jsonData.data);
				var str="";
				for(var i in arrays){
					str=str+"<tr>"+
							 "<td width='100px'>"+arrays[i].key+"</td>"+
					         "<td width='100px'>"+arrays[i].value+"</td>"+
					         "<tr>";
				}
				$("#imagesInfo").html(str);
			}
		}
	});
});
