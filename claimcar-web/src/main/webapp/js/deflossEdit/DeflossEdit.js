var rowNum =10;//table 页面保留行数
var vinNoFlag = true;
var licenType;
$(function(){
	
	//拒赔
	var isBInotpayFlag1=$("[name='lossCarMainVo.isBInotpayFlag']:checked").val();
	var isCInotpayFlag1=$("[name='lossCarMainVo.isCInotpayFlag']:checked").val();
	if(isBInotpayFlag1=='1' || isCInotpayFlag1=='1'){
		$("#payCause").removeClass("hide");
	}
	var notpayCause1= $("[name='lossCarMainVo.notpayCause']:checked").val();
	if(notpayCause1=='5'){
		$("#otherPayCause").removeClass("hide");
	}
	$.Datatype.rate = /^(100|100.0|100.00|[1-9]?\d(\.\d\d?)?)$/;// 验证百分比
	$.Datatype.licenseNo = /^([\u4e00-\u9fa5-A-Z]{1}[A-Z]{1}[A-Z_0-9]{5})|(新000000)$/;//车牌号码
	$.Datatype.letterAndNumber=/^[A-Za-z0-9]+$/;//字母和数字
	$.Datatype.vinMotuo = /^[A-HJ-NPR-Z0-9]{1,}$/;
	$.Datatype.vinNo = function(gets,obj,curform,regxp){
        //参数gets是获取到的表单元素值， obj为当前表单元素， curform为当前验证的表单，
        //regxp为内置的一些正则表达式的引用。 return false表示验证出错，没有return或者return true表示验证通过。
		var code = $(obj).val();
		if(code.length==0){
			return false;
		}
		if(!validVINCode(code)){
			vinNoFlag = false;
			//return false;
		}else{
			vinNoFlag = true;
			if($(obj).attr("id")=="frameNo" && $("#vinNo").val()==""){
				$("#vinNo").val(code);
			}
			if($(obj).attr("id")=="vinNo" && $("#frameNo").val()==""){
				$("#frameNo").val(code);
			}
		}
    };
    $.Datatype.selectMust = function(gets,obj,curform,regxp){
		var code = $(obj).val();
		if(isBlank(code)){
			return false;
		}
    };
    
    $.Datatype.checkBoxMust = function(gets, obj, curform, regxp) {
		var need = 1, numselected = curform.find("input[name='" + obj.attr("name") + "']:checked").length;
		return numselected >= need ? true : "请至少选择" + need + "项！";
	};
	
	var rule = [ {
		ele : "input[name='lossCarMainVo.lossPart']:first",
		datatype : "checkBoxMust",
		nullmsg : "损失部位至少选择一项！",
	}];
	
 	var ajaxEdit = new AjaxEdit($('#defossform'));
	ajaxEdit.targetUrl = "/claimcar/defloss/saveDefloss.do"; 
	
	ajaxEdit.beforeCheck = beforeCheck;
	ajaxEdit.beforeSubmit = saveBeforeCheck;
//	ajaxEdit.rules = rule;
	ajaxEdit.afterSuccess = saveDeflossAfter;
	//绑定表单
	ajaxEdit.bindForm();
	
	init();
	initLossTyPe();
    //修理厂查询按钮
	$("#check").click(function(){
		var comCode = $("#makeCom").val();
		var checkAddressCode = $("#checkAddressCode").val();
		if($("#checkbox").is(":checked")){
			return;
		}
		//更改修理厂必须清空精友所有的配件信息
		var existLoss;
		var lossMainId = $("#lossMainId").val();
		var params = {"lossMainId" : lossMainId};
		$.ajax({
			url : "/claimcar/defloss/existLoss.ajax",
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async : false,
			success : function(jsonData) {
				existLoss = eval(jsonData).data;
			}
		});
		if(existLoss){
			layer.msg("如需修改修理厂，需进精友系统删除所有配件信息！");
		}else{
			layer.open({
			    type: 2,
			    title:"选择修理厂",
			    closeBtn: 1,
//			    shadeClose: true,
			    scrollbar: false,
			    skin: 'yourclass',
			    area: ['90%', '80%'],
			    content:"/claimcar/defloss/findViewList.do?comCode="+comCode+"&checkAddressCode="+checkAddressCode,
			});
		}
		
	});
	//var isclaimSelf = $("input[name='lossCarMainVo.isClaimSelf']:checked").val();
	$("input[name='subrogationMain.subrogationFlag']").click(function(){
		var flag = $("input[name='subrogationMain.subrogationFlag']:checked").val(); 
		if(flag == "1"){
			$("#subRogationDiV").css('display','block'); 
		}else{
			$("#subRogationDiV").css('display','none'); 
		}
	});
	if($("#currentNode").val()=="DLChk"){
		$("#vinNo").removeAttr("datatype");
		$("#frameNo").removeAttr("datatype");
	}
	//是否拒赔
	$("[name='lossCarMainVo.isBInotpayFlag']").click(function(){
		var isBInotpayFlag= $(this).val();
		var isCInotpayFlag=$("[name='lossCarMainVo.isCInotpayFlag']:checked").val();
		if(isBInotpayFlag=='1'){
			$("#payCause").removeClass("hide");
		}else if(isBInotpayFlag=='0' && isCInotpayFlag=='0'){
			$("#payCause").addClass("hide");
		}
	});
	
	$("[name='lossCarMainVo.isCInotpayFlag']").click(function(){
		var isCInotpayFlag2= $(this).val();
		var isBInotpayFlag2=$("[name='lossCarMainVo.isBInotpayFlag']:checked").val();
		if(isCInotpayFlag2=='1'){
			$("#payCause").removeClass("hide");
		}else if(isBInotpayFlag2=='0' && isCInotpayFlag2=='0'){
			$("#payCause").addClass("hide");
		}
	});
	$("[name='lossCarMainVo.notpayCause']").click(function(){
		var notpayCause=$(this).val();
		if(notpayCause=='5'){
			$("#otherPayCause").removeClass("hide");
			$("#otherNotpayCause").attr("datatype","*");
		}else{
			$("#otherPayCause").addClass("hide");
			$("#otherNotpayCause").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		}
	});
	if($("#currentNode").val()=="DLChk"){
		if(!$("#jyButton").hasClass("btn-disabled")){
			$("#jyButton").removeAttr("disabled");
		}
	}
});

function supply(lossMainId){
var url="/claimcar/yjclaimcar/supplyInfoEdit.do?lossMainId="+lossMainId;
	
	layer.open({
		type : 2,
		title : '阳杰配件下单',
		shade : false,
		area: ['95%', '80%'],
		content :url,
		end : function() {
			
		}
	});
}

//初始化 页面
function init(){
	var isMobileCase=$("#isMobileCase").val();
	if("1" == isMobileCase){
		layer.alert("该案件为移动端案件，移动端暂存之前理赔不能操作！");
		$("body textarea").attr("disabled", "disabled");
		$("body input").each(function() {
			$(this).attr("disabled", "disabled");
		});
	}
	initLossPart();
	$("select[name='lossCarMainVo.cetainLossType']").attr("datatype", "selectMust");
	$("#repairFactoryType").attr("disabled","disabled");
	var certainType = $("#certainType").val();
	if(certainType=="02" || certainType=="03"){
		$("#jySumList").css('display','none'); 
		$("#otherSumList").css('display','block'); 
		$("#jyCertain").css("display","none");
	}else{
		$("#jySumList").css('display','block'); 
		$("#otherSumList").css('display','none'); 
		if(certainType=="05"){
			$("#jyCertain").css("display","none");
			$("#jy_sumRescueFee").attr("readonly","readonly");
		}else if(certainType=="06"){
			$("#jyCertain").css("display","none");
			$("#jy_sumComFee").attr("readonly",false);
			$("#jy_sumMatFee").attr("readonly",false);
			$("#jy_sumRepairFee").attr("readonly",false);
			$("#jy_sumOutFee").attr("readonly",false);
			$("#jy_sumRemnant").attr("readonly",false);
		}else{
			$("#jyCertain").css("display","block");
		}
	}
	//定损方式 标的车没有”被代为定损" 下拉框禁止使用某些选项
	if($("#deflossCarType").val() == "1"){
		$("#certainType").find("option[value='04']").attr("disabled","disabled");
		var carAmount = $("#carAmount").val();
		var theftAmount = $("#theftAmount").val();
		if(isBlank(carAmount)){
			$("#certainType").find("option[value='02']").attr("disabled","disabled");
		}
		if(isBlank(theftAmount)){
			$("#certainType").find("option[value='03']").attr("disabled","disabled");
		}
//		if($("#reportType").val()=="1"){//单商业报案
//			var kindMap = $("#kindMap").val();
//			if(kindMap.indexOf("A")==-1 || kindMap.indexOf("G")==-1 ||kindMap.indexOf("F")==-1 
//					|| kindMap.indexOf("L")==-1 ||kindMap.indexOf("Z")==-1){
//				
//				$("#certainType").find("option[value='01']").attr("disabled","disabled");
//			}
//		}
		//if(isBlank(carAmount) && isBlank(theftAmount)){
		//	$("#certainType").find("option[value='01']").attr("disabled","disabled");
		//}
	}
	
	//<option value='01'>车损</option><option value='02'>玻璃单独破碎</option><option value='03'>全车盗抢</option>
	 //<option value='04'>车身划痕</option><option value='05'>爆炸</option><option value='06'>自燃</option>
	 //<option value='07'>其他</option></select>
	if($("#deflossCarType").val() == "1"){
		var kindMap = $("#kindMap").val();
		var riskcode = $("#riskCode").val();
		if('1230' == riskcode){  //新险种-机动车商业险
			if(kindMap.indexOf("E")==-1){
				$("#lossFeeType").find("option[value='05']").attr("disabled","disabled");
			}
			if(kindMap.indexOf("L")==-1){
				$("#lossFeeType").find("option[value='04']").attr("disabled","disabled");
			}
			if(kindMap.indexOf("A")==-1){
				$("#lossFeeType").find("option[value='01']").attr("disabled","disabled");
				$("#lossFeeType").find("option[value='02']").attr("disabled","disabled");
				$("#lossFeeType").find("option[value='03']").attr("disabled","disabled");
				$("#lossFeeType").find("option[value='06']").attr("disabled","disabled");
				$("#lossFeeType").find("option[value='07']").attr("disabled","disabled"); // 涉水
			}
			if(kindMap.indexOf("W1")==-1){
				$("#lossFeeType").find("option[value='08']").attr("disabled","disabled"); // 车轮单独损失
			}
		}else if('1231' == riskcode){ //新险种-特种车商业险     新险种-摩托车商业险
			$("#lossFeeType").find("option[value='02']").attr("disabled","disabled");
			$("#lossFeeType").find("option[value='04']").attr("disabled","disabled");
			$("#lossFeeType").find("option[value='05']").attr("disabled","disabled");
			$("#lossFeeType").find("option[value='06']").attr("disabled","disabled");
			$("#lossFeeType").find("option[value='07']").attr("disabled","disabled");
			if(kindMap.indexOf("A")==-1){
				$("#lossFeeType").find("option[value='01']").attr("disabled","disabled");
			}
			if(kindMap.indexOf("G")==-1){
				$("#lossFeeType").find("option[value='03']").attr("disabled","disabled");
			}
			if(kindMap.indexOf("W1")==-1){
				$("#lossFeeType").find("option[value='08']").attr("disabled","disabled"); // 车轮单独损失
			}
		}else if('1232' == riskcode){
			$("#lossFeeType").find("option[value='02']").attr("disabled","disabled");
			$("#lossFeeType").find("option[value='04']").attr("disabled","disabled");
			$("#lossFeeType").find("option[value='05']").attr("disabled","disabled");
			$("#lossFeeType").find("option[value='06']").attr("disabled","disabled");
			$("#lossFeeType").find("option[value='07']").attr("disabled","disabled");
			$("#lossFeeType").find("option[value='08']").attr("disabled","disabled"); // 车轮单独损失
			if(kindMap.indexOf("A")==-1){
				$("#lossFeeType").find("option[value='01']").attr("disabled","disabled");
			}
			if(kindMap.indexOf("G")==-1){
				$("#lossFeeType").find("option[value='03']").attr("disabled","disabled");
			}
		}else if('1233' == riskcode){
			$("#lossFeeType").find("option[value='02']").attr("disabled","disabled");
			$("#lossFeeType").find("option[value='03']").attr("disabled","disabled");
			$("#lossFeeType").find("option[value='04']").attr("disabled","disabled");
			$("#lossFeeType").find("option[value='05']").attr("disabled","disabled");
			$("#lossFeeType").find("option[value='06']").attr("disabled","disabled");
			$("#lossFeeType").find("option[value='07']").attr("disabled","disabled");
			if(kindMap.indexOf("A")==-1){
				$("#lossFeeType").find("option[value='01']").attr("disabled","disabled");
			}
			if(kindMap.indexOf("W1")==-1){
				$("#lossFeeType").find("option[value='08']").attr("disabled","disabled"); // 车轮单独损失
			}
		}else{   // 旧险种逻辑不变
			if(kindMap.indexOf("A")==-1){
				$("#lossFeeType").find("option[value='01']").attr("disabled","disabled");
			}
			
			if(kindMap.indexOf("E")==-1){
				$("#lossFeeType").find("option[value='05']").attr("disabled","disabled");
			}
			if(kindMap.indexOf("G")==-1){
				$("#lossFeeType").find("option[value='03']").attr("disabled","disabled");
			}
			if(kindMap.indexOf("F")==-1){
				$("#lossFeeType").find("option[value='02']").attr("disabled","disabled");
			}
			if(kindMap.indexOf("L")==-1){
				$("#lossFeeType").find("option[value='04']").attr("disabled","disabled");
			}
			if(kindMap.indexOf("Z")==-1){
				$("#lossFeeType").find("option[value='06']").attr("disabled","disabled");
			}
			//2020条款新加的损失类别，针对旧险种逻辑默认不可选
			$("#lossFeeType").find("option[value='07']").attr("disabled","disabled"); // 涉水
			$("#lossFeeType").find("option[value='08']").attr("disabled","disabled"); // 车轮单独损失
		}
	}
	
	if($("#deflossCarType").val() == "1"){
		//新险种，是否玻璃单独破碎和是否属于无法找到第三方必填，非新险种非必填
		var riskcode = $("#riskCode").val();
		if(riskcode == '1230' || riskcode == '1231' || riskcode == '1232' || riskcode == '1233'){
			
		}else{
			$("#isGlassBrokenClass").hide();
			$("#isNotFindThirdClass").hide();
			if(riskcode == '1101'){
				$("#isGlassBroken").hide();
				$("#isNotFindThird").hide();
			}
		}
	}
	
 	//已处理 身份证隐藏账号
	var handlerIdNo= $("#defIdNo").val();
	if(isBlank(handlerIdNo) && $("#handleStatus").val()=='3'){
		handlerIdNo = handlerIdNo.substr(0,6)+"************";
		$("#handlerIdNo").text(handlerIdNo);
	}
	//费用隐藏 超过10条展示更多按钮
	hiddenData("chargeTbody");
	
	//配件  超过10条展示更多按钮
	hiddenData("componentBody");
	hiddenData("materialBody");
	hiddenData("outRepairBody");
	hiddenData("repairBody");
	//代为展示
	subrogationClick();
	initAcceptDef();
	isMajorCase();
	isWaterFlooded();
	setVinChange($("select[name='carInfoVo.licenseType']"));
	//三者车无损失时 不校验车架号 VIN码
	noLossChange();
	
	//vin初始化带入车架号的值
	if($("#handleStatus").val() == "2" && !isBlank($("#frameNo").val())){
		$("#vinNo").val($("#frameNo").val());
	}
}

function setVinChange(obj){	
	licenType = $("option:selected", $(obj)).val();
	var vinType = "vinNo";
	if("07"==licenType||"08"==licenType||"09"==licenType||"10"==licenType
			||"11"==licenType||"12"==licenType||"14"==licenType||"17"==licenType
			||"19"==licenType||"21"==licenType){
		vinType = "vinMotuo";
	}
	$("#frameNo").removeAttr("datatype").attr("datatype",vinType).removeClass("Validform_error").qtip('destroy',true);
	$("#vinNo").removeAttr("datatype").attr("datatype",vinType).removeClass("Validform_error").qtip('destroy',true);
}

//证件类型为身份证时
function changeIdentifyType(){
	if($("#certainType").val()!="05"){
		if ($("#identifyType").val() == "1") {
			$("#identifyNo").attr("datatype", "idcard");
		} else if($("#identifyType").val() == "99"){
			$("#identifyNo").attr("datatype", "*");
			$("#identifyNo").removeClass("Validform_error").qtip('destroy',true);
		} else {
			$("#identifyNo").attr("datatype", "letterAndNumber");
		}
	}
	
}

// 是否重大赔案上报
function isMajorCase(){
	var isMajor = $("input[name='lossCarMainVo.isMajorCase']:checked");
	if(isMajor.val() == "1"){
		$("#claimTexts").show();
		if($("#handleStatus").val() == "2" && $("#currentNode").val() == "DLCar"){
			isMajorSelectValid();
		}
	}else{
		$("#claimTexts").hide();
	}
}

function isMajorSelectValid(){
	var params = {"registNo" : $("#registNo").val()};
	$.ajax({
		url : "/claimcar/defloss/isMajorSelectValid.ajax",
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(jsonData) {//data返回true-存在不能点是
//			layer.alert("======jsonData====="+eval(jsonData).data);
			if (eval(jsonData).data) {
				var isc=$("input[name='lossCarMainVo.isMajorCase'][value='0']");
				isc.prop("checked",true);
				layer.msg("查勘已经发起大案审核，定损不能再次发起！");
				$("#claimTexts").hide();
			}else{
				$("#claimTexts").show();
			}
		}
	});
}

/**
 * 未接收处理和已完成处理
 */
function initAcceptDef(){
	if($("#handleStatus").val()=='3'){//已处理
		$(":input").attr("disabled","disabled");
		$(":input[type='button']").addClass("btn-disabled");
		$(".nodisabled").removeAttr("disabled");
		$(".nodisabled").removeClass("btn-disabled");
		//已经处理打开进入配件系统（定损查勘接口）
		if($("#jy2Flag").val()=='1'){
			$("#jyButton").removeAttr("disabled");
			$("#jyButton").removeClass("btn-disabled");
		}
		return ;
	}
	//复检
	if($("#currentNode").val()=="DLChk"){//页面信息不能改
		$("#defloss_inputDiv :input[type!='hidden']").attr("disabled","disabled");
		$("#defloss_subFeeDiv :input").attr("disabled","disabled");
		$("#defloss_subFeeDiv :input[type='button']").addClass("btn-disabled");
		if($("#chargeTbodyButton").length>0){
			$("#chargeTbodyButton").removeAttr("disabled");
			$("#chargeTbodyButton").removeClass("btn-disabled");
		}
//		alert(123);
		$("#handlerName").removeAttr("disabled");
		$("#defIdNo").removeAttr("disabled");
		
		$("input[name='subrogationMain.subrogationFlag']").attr("disabled","disabled");
		$("#subRogationDiV :input").attr("disabled","disabled");
		if($("#handleStatus").val()!='3'){
			$("#description").removeAttr("disabled");
		}
	}
	
	//退回定损 只能修改定损信息中的【损失程度】、【定损日期】、【定损地点】、【推修时间】、【预计修理完毕日期】和【损失部位】
//	if($("#veriPriceFlag").val() == "2" || $("#underWriteFlag").val() == "2"){
//		$("#defloss_inputDiv :input[type!='hidden']").attr("disabled",true);
//		$("#carInfo_lossLevel").removeAttr("disabled");
//		$("input[name='lossCarMainVo.deflossDate']").removeAttr("disabled");
//		$("input[name='lossCarMainVo.defSite']").removeAttr("disabled");
//		$("input[name='lossCarMainVo.sendDate']").removeAttr("disabled");
//		$("input[name='lossCarMainVo.estimatedDate']").removeAttr("disabled");
//		$("input[name='lossCarMainVo.lossPart']").removeAttr("disabled");
//		$("#allLossPart").removeAttr("disabled");
//	}
	//修改定损 定损方式  不做任何管控
	//不允许修改修理厂信息（包括修理厂名称、修理厂代码、修理厂类型、修理厂电话）
//	if($("#currentNode").val() == "DLCarMod"){
//		$("#carInfo_Div :input[type!='hidden']").attr("disabled",true);
//		$("#deflossInfo :input[type!='hidden']").attr("disabled",true);
		
//		$("#defloss_inputDiv :input[type!='hidden']").attr("disabled","disabled");
//		$("#certainType").attr("disabled","disabled");
//		$("#factoryCode").attr("disabled","disabled");
//		$("#check").attr("disabled","disabled");
//		$("#factoryName").attr("disabled","disabled");
//		$("#repairFactoryType").attr("disabled","disabled");
//		$("input[name='lossCarMainVo.factoryMobile']").attr("disabled","disabled");
//	}
	
	var acceptFlag = $("#acceptFlag").val();//是否接收
	//节点是定损，剔除退回，并且未接收
	if(acceptFlag=='0'){
		if(!isEmpty($("#oldClaim"))){
			layer.confirm('该案件为旧理赔迁移案件，是否继续处理?', {
				btn : [ '确定', '取消' ]
			},function(){
				accaptTask();
			},function(){
				$(":input").attr("disabled","disabled");
				$(":input[type='button']").addClass("btn-disabled");
				$(".nodisabled").removeAttr("disabled");
				$(".nodisabled").removeClass("btn-disabled");
			});
		}else{
			accaptTask();
		}
	}
}

function accaptTask(){
	layer.confirm('是否确定接收此任务?', {
		btn: ['确定','取消'] //按钮
	}, function(index){
		layer.load(0, {shade : [0.8, '#393D49']});
		var flowTaskId=$("#flowTaskId").val();
		var registNo = $("#registNo").val();
		$.ajax({
			url : "/claimcar/defloss/acceptDefloss.do?flowTaskId="+flowTaskId+"&registNo="+registNo, // 后台处理程序
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			//data : params, // 要传递的数据
			async:false, 
			success : function(jsonData) {// 回调方法，可单独定义
				if(jsonData.data == "0"){
					layer.msg("接收任务失败！");
					$(":input").attr("disabled","disabled");
					$(":input[type='button']").addClass("btn-disabled");
					$(".nodisabled").removeAttr("disabled");
					$(".nodisabled").removeClass("btn-disabled");
				}else{
					window.location.reload();
					layer.close(index); 
				}
			}
		});
	}, function(){
		$(":input").attr("disabled","disabled");
		$(":input[type='button']").addClass("btn-disabled");
		$(".nodisabled").removeAttr("disabled");
		$(".nodisabled").removeClass("btn-disabled");
	});
}

//隐藏更多行
function hiddenData(bodyid){
	var bodyDivId = "#"+bodyid;
	var tbody =$(bodyDivId);
	var num = rowNum - 1;
	tbody.find("tr :gt("+num+")").each(function(){
		this.style.display = "none";
		
	});//materialBDiv
	
	if(tbody.find("tr").size()> rowNum){
		var buttonId ="#"+bodyid+"Button";
		$(buttonId).css('display','block');
	}
}

//费用功能显示更多
function showMore(objName){
	var bodyDivId="#"+objName;
	var buttonId ="#"+objName+"Button";
	$(buttonId).css('display','none');
	
	var num = rowNum - 1;
	$(bodyDivId).find("tr :gt("+num+")").each(function(){
		$(this).removeAttr("style"); 
	});
}

function noLossChange(){
	changeIdentifyType();
	if($("#certainType").val()=="05"){
		$("input[name='carInfoVo.engineNo']").removeAttr("datatype");
		$("input[name='carInfoVo.driveName']").removeAttr("datatype");
		//不管是否无损失都管控drivingLicenseNo，identifyNo必录
		$("input[name='lossCarMainVo.lossPart']:eq(0)").removeAttr("datatype");
		//alert($("input[name='lossCarMainVo.lossPart']:eq(0)").attr("datatype"));
		$("#description").removeAttr("datatype");
		
		$("#frameNo").removeClass("Validform_error");
		$("#vinNo").removeClass("Validform_error");
		$("input[name='carInfoVo.engineNo']").removeClass("Validform_error");
		$("input[name='carInfoVo.driveName']").removeClass("Validform_error");

		$("input[name='lossCarMainVo.lossPart']:eq(0)").removeClass("Validform_error");
//		$("#identifyNo").removeAttr("datatype");
		$("#description").removeClass("Validform_error");
		
		
		$("#frameNo").qtip('destroy',true);
		$("#vinNo").qtip('destroy',true);
		$("input[name='carInfoVo.engineNo']").qtip('destroy',true);
		$("input[name='carInfoVo.driveName']").qtip('destroy',true);
		
		$("#description").qtip('destroy',true);
		$("input[name='lossCarMainVo.lossPart']:eq(0)").qtip('destroy',true);
		//如果是三者车无损失，则车架号和VIN码不必录
		if($("#deflossCarType").val() == "3"){
			$("#frameNo").attr("ignore","ignore");
			$("#vinNo").attr("ignore","ignore");
		}
	}else{
		$("input[name='carInfoVo.engineNo']").attr("datatype","*");
		$("input[name='carInfoVo.driveName']").attr("datatype","*");
		$("#description").attr("datatype","*");
		$("input[name='lossCarMainVo.lossPart']:eq(0)").attr("datatype","*");
		if($("#deflossCarType").val() == "3"){
			$("#frameNo").removeAttr("ignore");
			$("#vinNo").removeAttr("ignore");
		}
	}
}

/**
 * 校验前控制
 */
function beforeCheck(){
	//无损失 不控制
	if($("#certainType").val()!="05"){
		if($("#saveType").val() != "save"){
			$("#description").attr("datatype","*");
		}
		var certainType = $("#certainType").val();
		if(certainType=="01" || certainType=="04" ){//修复定损 和被代位协议定损
			if($("#jy2Flag").val()=="0"){
				$("#factoryCode").attr("datatype","*");
			}
		}
		$("#indemnityDuty").attr("datatype","selectMust");
		
		//标的车，无损失不管控，损失类别必录  && $("#certainType").val()!="05"
		if($("#deflossCarType").val() == "1"){
			var kindMap = $("#kindMap").val();
			if(kindMap.indexOf("A")!=-1 || kindMap.indexOf("G")!=-1 ||kindMap.indexOf("F")!=-1 
					|| kindMap.indexOf("L")!=-1 ||kindMap.indexOf("Z")!=-1){
				$("#lossFeeType").attr("datatype","selectMust");
			}
		}
	}
	var other_sumRemnant = $("#other_sumRemnant").val();
	if(!isBlank(other_sumRemnant)){
		$("input[name='lossCarMainVo.sumRemnant']").val(other_sumRemnant);
	}
	
}
function saveBeforeCheck(){
	var saveType = $("#saveType") .val();
	if(!validMustCheck()){
		return false;
	}

	var frameNo = $("#frameNo").val();
	var vinNo = $("#vinNo").val();
	if(!validVINCode(frameNo) || !validVINCode(vinNo)){
		vinNoFlag =false;
	}
	if(!("07"==licenType||"08"==licenType||"09"==licenType||"10"==licenType
		||"11"==licenType||"12"==licenType||"14"==licenType||"17"==licenType
		||"19"==licenType||"21"==licenType)){
		if(!vinNoFlag){
			if(!window.confirm("VIN码不符合平台校验规则，请联系平台进行电子联系单特批处理！如VIN码有误会影响后续单证处理，请确定是否继续提交?")){
				return false;;
			}
		}
	}

	setHiddenValue(saveType);
	var returnFlag =true;
	
	var deflossCarType = $("#deflossCarType").val();
	if(deflossCarType=="1"){
		//是否互碰自赔
		var isClaimSelf = $("input[name='lossCarMainVo.isClaimSelf']:checked");
		if(isEmpty(isClaimSelf)){
			layer.msg("必须录入互碰自赔标志！");
			isWhethertheloss.focus();
			return false;
		}
		//是否单证齐全
		var directFlag = $("input[name='lossCarMainVo.directFlag']:checked");
		if(isEmpty(directFlag)){
			layer.msg("必须录入单证齐全标志！");
			directFlag.focus();
			return false;
		}
		//是否有营业资格证
		var isBusinesscarFlag = $("input[name='lossCarMainVo.isBusinesscarFlag']:checked");
		if(isEmpty(isBusinesscarFlag)){
			layer.msg("必须录入营业车资格证标志！");
			isBusinesscarFlag.focus();
			return false;
		}
		//是否有特种车操作证
		var isSpecialcarFlag = $("input[name='lossCarMainVo.isSpecialcarFlag']:checked");
		if(isEmpty(isSpecialcarFlag)){
			layer.msg("必须录入特种车操作证标志！");
			isSpecialcarFlag.focus();
			return false;
		}
		
		var isGlassBroken = $("input[name='lossCarMainVo.isGlassBroken']:checked");
		var isNotFindThird = $("input[name='lossCarMainVo.isNotFindThird']:checked");
		var riskcode = $("#riskCode").val();
		if(riskcode == '1230' || riskcode == '1231' || riskcode == '1232' || riskcode == '1233'){
			if(isEmpty(isGlassBroken)){
				layer.msg("必须录入是否玻璃单独破碎！");
				isGlassBroken.focus();
				return false;
			}
			
			if(isEmpty(isNotFindThird)){
				layer.msg("必须录入是否属于无法找到第三方！");
				isNotFindThird.focus();
				return false;
			}
		}
	}else if(deflossCarType=="3"){
		//是否无责代赔
		var isNodutypayFlag = $("input[name='lossCarMainVo.isNodutypayFlag']:checked");
		if(isEmpty(isNodutypayFlag)){
			layer.msg("必须录入无责代赔标志！");
			isNodutypayFlag.focus();
			return false;
		}
		var biPolicyNo = $("input[name='carInfoVo.biPolicyNo']").val();
		var biInsureComCode = $("select[name='carInfoVo.biInsureComCode']").find("option:selected").val();
		var ciPolicyNo = $("input[name='carInfoVo.ciPolicyNo']").val();
		var ciInsureComCode = $("select[name='carInfoVo.ciInsureComCode']").find("option:selected").val();
		if(isBlank(biPolicyNo)||isBlank(biInsureComCode)||isBlank(ciPolicyNo)||isBlank(ciInsureComCode)){
			layer.msg("三者车承保公司、三者车保单号未录入！");
		}	
	}
	
	//是否车物减损
	var isWhethertheloss = $("input[name='lossCarMainVo.isWhethertheloss']:checked");
	if(isEmpty(isWhethertheloss)){
		layer.msg("必须录入车物减损标志！");
		isWhethertheloss.focus();
		return false;
	}	
	
	//代为案件
	if($("input[name='subrogationMain.subrogationFlag']:checked").val() =="1"){
		if($("#subrogationCarSize").val()==0 && $("#subrogationPerSize").val()==0){
			layer.msg("代位求偿案件 责任方为机动车或责任方为非机动车至少录入一项");
			$("input[name='subrogationMain.subrogationFlag']").focus();
			return false;
		}
		//车牌号不能重复
		var items =$("#subrogationCarTable select[name $='serialNo']");
		var linceNoArray =new Array();
		$(items).each(function(){
			var pre = $(this).attr("name").split("]")[0];//下标
			var index = pre.split("[")[1];//下标
			var linceNo = $(this).find("option:selected").text();
			linceNoArray[index]= linceNo;
//			alert(122);
			$("#subrogationCarTable input[name='subrogationMain.prpLSubrogationCars["+index+"].licenseNo']").val(linceNo);
		});
		
		linceNoArray=linceNoArray.sort();
		for(var i=0;i<linceNoArray.length;i++){
			if (linceNoArray[i]==linceNoArray[i+1]){ 
				layer.msg("代位求偿信息中车牌号："+linceNoArray[i]+"重复");
				$("#subrogationCarTable select[name='subrogationMain.prpLSubrogationCars["+i+"].serialNo']").focus();
				return false;
			}
		}

		
		//保单未承保车损险，也未承保全面型车损险则不能做代位求偿案件
		var registNo = $("#registNo").val();
		if(!checkSubrogationFlag(registNo)){
			return false;
		}
	}
	//推定全损 只能是车损或者盗抢,自燃
	//04 车身划痕  02 玻璃单独破碎   01车损  06	自燃   03 全车盗抢
	var certainType = $("#certainType").val();//定损方式
	var lossFeeType = $("#lossFeeType").val();//损失方式 
	
	var deflossCarType = $("#deflossCarType").val();//标的车
	if(deflossCarType=="1"){
		if(certainType=="02"){//推定全损
			if(lossFeeType=="02" || lossFeeType=="04"){
				layer.msg("推定全损损失类别不能选择车身划痕和 玻璃单独破碎 ！");
				return false;
			}
		}else if(certainType=="03"){//全车盗抢
			if(lossFeeType!="03"){
				layer.msg("定损方式是全车盗抢，损失类别只能选择全车盗抢！");
				return false;
			}
		}
	}
	
	//校验附加险
	var deviceMap = $("#deviceMap").val();
	var itemNameArray = new Array();
	if(!isBlank(deviceMap)){
		$("#subRiskDiv input[name$='kindCode']").each(function(){
			if($(this).val()=="X"){
				var item = $(this).parents("tr").find("select[name$='itemName']").val();
				itemNameArray.push(item);
			}
		});
		
		itemNameArray=itemNameArray.sort();
		for(var i=0;i<itemNameArray.length;i++){
			if (itemNameArray[i]==itemNameArray[i+1]){ 
				layer.msg("新增设备有重复的损失名称！");
				return false;
			}
		} 
	}
	
	
	if(saveType != "save"){
		
		var params = $("#defossform").serialize();
		$.ajax({
			url : "/claimcar/defloss/validDefloss.do", // 后台处理程序
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async:false, 
			success : function(jsonData) {// 回调方法，可单独定义
				var result = eval(jsonData);
				if (result.status == "200") {
					if(result.data !="ok"){
						layer.msg(result.data);
						returnFlag = false;
					}
				}
			}
		});
		
		if(!returnFlag){
			return false;
		}
		
		//提示修理厂是否发送短信  以后可能需要做
//		var factoryMobile = $("input[name='lossCarMainVo.factoryMobile']").val();
//		if(isBlank(factoryMobile)){
//			if(!window.confirm("修理厂电话为空，将不会向修理厂发送短信，请确认是否继续！")){
//				return false;
//			}
//		}
		
		//定损合计金额和施救费都为0，则提示“请确定是否继续提交？”，
		var sumRescueFee = parseFloat(0.00);
		var sumLossFee = parseFloat(0.00);
		var certainType = $("#certainType").val();
		if(certainType =='02' || certainType =='03'){
			sumLossFee= parseFloat($("#other_sumLossFee").val());
		}else{
			sumLossFee= parseFloat($("#jy_sumLossFee").val());
		}
		
//		sumLossFee = parseFloat($("input[name='lossCarMainVo.sumLossFee']").val());
		sumRescueFee = parseFloat($("input[name='lossCarMainVo.sumRescueFee']").val());
		if(sumRescueFee==0.0 && sumLossFee ==0.00){
			if(!window.confirm("定损合计金额和施救费都为0，请确定是否继续提交?")){
				return false;
			}
		}
		
		//针对标的车的车损险，系统校验定损总金额是否大于等于车损险保额的60%，点击提交会给出提示，“定损金额已达到车损险保额X%”软控制
		var deflossCarType = $("#deflossCarType").val();//标的车
		var carAmount = parseFloat($("#carAmount").val());
		var sumpaidDef =  parseFloat($("#sumpaidDef").val());
		sumLossFee = sumLossFee + sumpaidDef;
		if(deflossCarType=="1"){
			var rate = Math.round(sumLossFee*100/carAmount,2);
			if(rate > 60){
				if($("#certainType").val()=='01'){//定损金额达到车损保额60%建议推定全损
					if(!window.confirm("定损金额达到车损险保额"+rate+"%建议推定全损")){
						return false;;
					}
				}else{
					if(!window.confirm("定损金额已达到车损险保额"+rate+"%")){
						return false;;
					}
				}
			} 
		}		
	}
	//定损环节VIN码标的车必录，三者车定损方式无损失可不必录，若录入则必须符合VIN码校验规则
	/*if(!("07"==licenType||"08"==licenType||"09"==licenType||"10"==licenType
			||"11"==licenType||"12"==licenType||"14"==licenType||"17"==licenType
			||"19"==licenType||"21"==licenType) && 
			!($("#deflossCarType").val()=="3"&&$("#certainType").val()=="05")){
		if(!vinNoFlag){
			if(!window.confirm("VIN码不符合平台校验规则，请联系平台进行电子联系单特批处理！如VIN码有误会影响后续单证处理！")){
				return false;
			}
		}
	}*/
	if(!checkCertifyNo()){
		return false;
	}
}; 

//提示修理厂是否发送短信
function checkMobile(){
	var factoryMobile = $("input[name='lossCarMainVo.factoryMobile']").val();
	if(isBlank(factoryMobile)){
//		layer.confirm("修理厂电话为空，将不会向修理厂发送短信，请确认是否继续！", {
//			btn: ['确定','取消'] //按钮
//		}, function(index){
//			layer.close(index); 
//			return true;
//		}, function(index){
//			return false;
//			layer.close(index); 
//		});
	}
}
/**
 * 保存后方法
 * @param result
 */
function saveDeflossAfter(result){
	var jyFlag = $("#jyFlag").val();
	var saveType = $("#saveType").val();
	var currentNode = $("#currentNode").val();
	var dataArr = result.data.split(",") ;
//	var lossMainId = dataArr[0];
//	var flowTaskId = dataArr[1];
	var flowTaskId = $("#flowTaskId").val();;
	var lossMainId = $("#lossMainId").val();
	var certainType = $("#certainType").val();//定损方式
	
	refreshFee();
	
	if(certainType=="06"){
		$("#jyCertain").css("display","none");
		$("#jy_sumComFee").attr("readonly",false);
		$("#jy_sumMatFee").attr("readonly",false);
		$("#jy_sumRepairFee").attr("readonly",false);
		$("#jy_sumOutFee").attr("readonly",false);
		$("#jy_sumRemnant").attr("readonly",false);
	}
	
	if(jyFlag=="1"){
		$("#lossMainId").val(lossMainId);
		if($("#currentNode").val()=="DLChk"){
			var strURL ="/claimcar/defloss/enterFittingSys.do?carMainId="+lossMainId+"&operateType=DLChk&nodeCode="+currentNode; 
		}else{
			var strURL ="/claimcar/defloss/enterFittingSys.do?carMainId="+lossMainId+"&operateType=certa&nodeCode="+currentNode; 
		}
		window.open(strURL, "打开配件系统","width=1010,height=670,top=0,left=0,toolbar=0,location=0,directories=0,menubar=0,scrollbars=1,resizable=1,status=0");
	}else{
		if(saveType=="save"){
			layer.confirm('暂存成功', {
				btn: ['确定'] //按钮
			}, function(index){
				layer.close(index);
//				window.location.reload();
			});
		}else{
			//如果案件标的车未承保涉水险，但配件勾选了涉水，定损提交时提示"案件未承保涉水险，
			//配件已勾选涉水，理算环节涉水配件将不会赔付，是否继续提交？"
			var kindCodeX2 = $("#kindCodeX2").val();
			if(kindCodeX2=="0"){//案件标的车未承保涉水险
				$.ajax({
					url : "/claimcar/defloss/checkKindCode.ajax", //后台刷新
					type : 'post', // 数据发送方式
					data : {"lossMainId" : lossMainId}, // 要传递的数据
					success : function(resultData) {// 回调方法，可单独定义
						if("1" == resultData.statusText){//配件勾选了涉水
							layer.confirm('案件未承保涉水险，配件已勾选涉水，理算环节涉水配件将不会赔付，是否继续提交?', {
								  btn: ['是','否'] //按钮
								}, function(){
									layer.closeAll();
									var url = "/claimcar/defloss/submitNextPage.do?lossMainId="+lossMainId+"&currentNode="+currentNode+
									"&flowTaskId="+flowTaskId ;
									layer.open({
										type: 2,
										closeBtn:0,
										title: "定损提交",
										area: ['75%', '50%'],
										fix: true, //不固定
										maxmin: false,
										content: url,
								 	});
								}, function(){
									layer.closeAll();
								});
						}else{
							var url = "/claimcar/defloss/submitNextPage.do?lossMainId="+lossMainId+"&currentNode="+currentNode+
							"&flowTaskId="+flowTaskId ;
							layer.open({
								type: 2,
								closeBtn:0,
								title: "定损提交",
								area: ['75%', '50%'],
								fix: true, //不固定
								maxmin: false,
								content: url,
						 	});
						}
					}
				});
			}else{
				var url = "/claimcar/defloss/submitNextPage.do?lossMainId="+lossMainId+"&currentNode="+currentNode+
				"&flowTaskId="+flowTaskId ;
				layer.open({
					type: 2,
					closeBtn:0,
					title: "定损提交",
					area: ['75%', '50%'],
					fix: true, //不固定
					maxmin: false,
					content: url,
			 	});
			}
			
		}
	}	
		
}

function refreshLossInfo(){
	var registNo = $("#registNo").val();
	var lossMainId = $("#lossMainId").val();
	$.ajax({
		url : "/claimcar/defloss/refreshLossInfo.ajax", //后台刷新
		type : 'post', // 数据发送方式
		data : {"lossMainId" : lossMainId,"registNo" : registNo}, // 要传递的数据
		async : false, 
		success : function(htmlData) {// 回调方法，可单独定义
			$("#refreshLossInfo").empty();
			$("#refreshLossInfo").append(htmlData);
			return false;
		},
		error : function(htmlData){
			layer.alert("刷新失败！");
			return false;
		}
	});
}

function refreshFee(){
	var registNo = $("#registNo").val();
	var lossMainId = $("#lossMainId").val();
	$.ajax({
		url : "/claimcar/defloss/refreshFee.ajax", //后台刷新
		type : 'post', // 数据发送方式
		data : {"lossMainId" : lossMainId,"registNo" : registNo,"operateType" : "certa"}, // 要传递的数据
		async : false, 
		success : function(htmlData) {// 回调方法，可单独定义
			$("#refreshDiv").empty();
			$("#refreshDiv").append(htmlData);
			return false;
		},
		error : function(htmlData){
			layer.alert("刷新失败！");
			return false;
		}
	});
}

function save(obj,saveType,jyFlag){
	var selfClaimFlag=$("#selfClaimFlag").val();
	var selfDlossAmout=$("#selfDlossAmout").val();
	var sumlossFee;
	var certainType = $("#certainType").val();
	if(saveType=='submitLoss' && selfClaimFlag=='1' && selfDlossAmout!='0'){
		if(certainType=="02" || certainType=="03"){
			sumlossFee=$("#other_sumLossFee").val();
		}else{
			sumlossFee=$("#jy_sumLossFee").val();
		}
		if(selfDlossAmout!=sumlossFee){
		   alert("定损金额与客户确认金额不一致，请核实！");
		}
	}
	var deflossCarType=$("#deflossCarType").val();//是否标的车yzy
	var signIndex=$("#signIndex").val();
	var certainType=$("#certainType").val();//定损方式
	/*var frameNo = $("#frameNo").val();
	var vinNo = $("#vinNo").val();
	if(!validVINCode(frameNo) || !validVINCode(vinNo)){
		vinNoFlag =false;
	}*/
	if(!isEmpty($("#jy_sumRemnant")) && !isEmpty($("#jy_sumLossFee")) && ($("#jy_sumRemnant").val()>0 || $("#jy_sumLossFee").val()>0)){
	   }else{
		   var radio = $("[name='lossCarMainVo.isClaimSelf']");
		   var isClaimSelf;
		   for (i=0; i<radio.length; i++) {  
		        if (radio[i].checked) {  
		            isClaimSelf = radio[i].value;
		        } 
		   }
		   
		   if(signIndex != null && signIndex!= "" && deflossCarType=='1' && isClaimSelf != '1'){
				  if(certainType!='05'){
					  layer.msg(signIndex);
					 return false;
				   }
			  }
	   }
	if(isBlank(jyFlag)){
		$("#jyFlag").val("");
	}
	
	if($("#currentNode").val()=="DLChk" && saveType !="save"){
		saveType ="submitReLoss";
	}
	
	$("#saveType").val(saveType);
	
	//定损环节VIN码标的车必录，三者车定损方式无损失可不必录，若录入则必须符合VIN码校验规则
	/*if(!("07"==licenType||"08"==licenType||"09"==licenType||"10"==licenType
			||"11"==licenType||"12"==licenType||"14"==licenType||"17"==licenType
			||"19"==licenType||"21"==licenType)){
		if(!vinNoFlag){
			layer.confirm('VIN码不符合平台校验规则，请联系平台进行电子联系单特批处理！如VIN码有误会影响后续单证处理！', {
				btn : [ '确定', '取消' ]
				}, function(index) {
					$("#defossform").submit();
					});
		}else{
			$("#defossform").submit();
		}
	}else{
		$("#defossform").submit();
	}*/
	$("#defossform").submit();
//	$(obj).attr({"disabled":"true"});
}

//return false;——跳出所有循环；相当于 javascript 中的 break 效果。
//return true;——跳出当前循环，进入下一个循环；相当于 javascript 中的 continue 效果
// '02'==玻璃单独破碎 F  '04'==车身划痕 L'06'==自燃Z
 
function validMustCheck(){
	//附加险选择停驶险 修理完毕日期必录
	var kindTFlag =true;
	if($("#subRiskTbody").length>0){
		var lossFeeType = $("#lossFeeType").val();
		//lossCarMainVo.prpLDlossCarSubRisks[${status.index + size  }].kindCode
		var items =$("#subRiskTbody input[name$='subRiskKindCode']");
		$(items).each(function(){
			if($(this).val()=='T'){
				var estimatedDate = $("input[name='lossCarMainVo.estimatedDate']").val();
				if(isBlank(estimatedDate)){
					layer.msg("选择停驶险，预计修理完成日期必录");
					kindTFlag = false;
					return false;
				}
			}
			if($("#certainType").val() =='01'){
				if($(this).val()=='F' && lossFeeType=="02"){
					layer.msg("损失类型是玻璃单独破碎，附件险不能选择玻璃单独破碎险！");
					kindTFlag = false;
					return false;
				}
				
				if($(this).val()=='L' && lossFeeType=="04"){
					layer.msg("损失类型是车身划痕，附件险不能选择车身划痕险！");
					kindTFlag = false;
					return false;		
				}
				
				if($(this).val()=='Z' && lossFeeType=="06"){
					layer.msg("损失类型是自燃，附件险不能选择自燃损失险险！");
					kindTFlag = false;
					return false;
				}
			}
		});
	}
	if(!kindTFlag){
		return false;
	}
	//日期校验
	if(!checkDate()){
		return false;
	}
	
	//互碰自赔为是，事故责任责任比例不能为全责
	var isclaimSelf = $("input[name='lossCarMainVo.isClaimSelf']:checked").val();
	var indemnityDuty = $("#indemnityDuty").val();
	if(isclaimSelf=="1" && indemnityDuty=="0"){
		layer.msg("互碰自赔案件事故责任责任比例不能为全责");
		return false;
	}
	
	
	return true;
}


function setHiddenValue(saveType){
	var certainType = $("#certainType").val();
	var sumRescueFee = parseFloat(0.00);//施救金额
	
	if(certainType=="02" || certainType=="03" ){//推定全损 和盗抢折旧
		var sumLossFee = parseFloat(0.00);
		if(!isBlank($("#other_sumRescueFee").val())){
			sumRescueFee = parseFloat($("#other_sumRescueFee").val());
		}
		
		if(isBlank($("#otherFee").val())){
			$("#otherFee").val(0.00);
		}
		if(isBlank($("#other_sumRemnant").val())){
			$("#other_sumRemnant").val(0.00);
		}
		
		if(!isBlank($("#other_sumLossFee").val())){
			sumLossFee = parseFloat($("#other_sumLossFee").val());
		}
		$("input[name='lossCarMainVo.sumLossFee']").val(sumLossFee);
	}else if(certainType !="06"){//删除推定全损中的总定损金额和残值，另一种方法是把name属性去掉，但是选择其他是得加回来。
		$("input[name='lossCarMainVo.sumRemnant']").val("");
		$("input[name='lossCarMainVo.sumLossFee']").val("");
		$("#other_sumRemnant").val("");
		if(!isBlank($("#jy_sumRescueFee").val())){
			sumRescueFee = parseFloat($("#jy_sumRescueFee").val());
		}
//		if(!isBlank($("#jy_sumLossFee").val())){
//			sumLossFee = parseFloat($("#jy_sumLossFee").val());
//		}
		
	}else{//快出快赔
		sumRescueFee = parseFloat($("#jy_sumRescueFee").val());
	}
	
	
//	$("input[name='lossCarMainVo.sumRemnant']").val(sumRemnant);
	
	$("input[name='lossCarMainVo.sumRescueFee']").val(sumRescueFee);
	if(certainType=="05"){//无损失
		$("input[name='lossCarMainVo.sumLossFee']").val(parseFloat(0.00));
		$("input[name='lossCarMainVo.sumRemnant']").val(parseFloat(0.00));
		$("#other_sumRemnant").val(parseFloat(0.00));
	}
	
	
	
}

/*function chRepairFactory(){
	var repairFactoryType = $("#repairFactoryType").val();
	if(!isBlank(repairFactoryType)){
		$("#repairFactoryType").removeClass("Validform_error");
	}
	$("input[name='lossCarMainVo.repairFactoryType']").val(repairFactoryType); 
}
*/
//附加险总金额赋值
function upperCase(){
	var sumRiskFee = parseFloat(0.00);
	$("#subRiskTbody input[name$='subRiskFee']").each(function(){
		var subRiskFee=$(this).val();
		if(subRiskFee == "" || isNaN(subRiskFee)){
			subRiskFee = parseFloat(0.00);
		}
	
		sumRiskFee = sumRiskFee + parseFloat(subRiskFee);
	
	});
	
	$("#sumSubRiskFee").val(sumRiskFee);
		
	
}

//进精友  
function isSaveInfoBeforeEnter(){
	
	var defSite = $("#defSite").val();
	var comCode = $("#comCode").val();
	var deflossCarType = $("#deflossCarType").val();
	var acceptlicensedate = $("#acceptlicensedate").val();
	var enrollDate = $("#enrollDate").val();
	//海南地区案件定损地点不能为空
	if(comCode.slice(0,2) == 20 && $("#handleStatus").val() != '3'){
		//判断是否三者车，若是，增加条件，初次登记日期不能为空
		if ("1"!=deflossCarType){
			if(acceptlicensedate.match(/^[ ]*$/)){
			layer.alert("海南地区案件三者车的 “初次领证日期” 不能为空！！");
				return;
			}
		}
		//三者车，初登日期可能为空，所以判断初登时期有没有为空
		if(enrollDate.match(/^[ ]*$/)){
			layer.alert("海南地区案子 “初登日期” 不能为空！！");
			return;
		}
		if(defSite.match(/^[ ]*$/)){
			layer.alert("海南地区案子 “定损地点” 不能为空！！");
			return;
		}
	}
	if($("#handleStatus").val()=='3'){//已处理
		//定损查看
		var jyRegistNo = $("#jyRegistNo").val();
		var jyId = $("#jyId").val();
		jyViewData(jyRegistNo,jyId);
	}else{
		showMore("componentBody");
		showMore("materialBody");
		showMore("outRepairBody");
		showMore("repairBody");
		$("#jyFlag").val("1");
		$("#description").attr("ignore","ignore");//提交之前对定损意见是否为空不做判断
		save($("#jyButton"),"save","jyFlag");
		$("#description").removeAttr("ignore");
	}

}
//清除定损清单数据 table下的所有行
function clearTableRow(tablename){
	var tbody =$(tablename);
	tbody.find("tr").remove();
}
//插入最新的精友数据
function insertTableRow(tablename,componentStr){
	var tbody = $(tablename);
	tbody.append(componentStr);
}

//费用赔款信息 合计	
function calSumChargeFee(field,checkFlag){
	var items =$("#chargeTbody input[name$='chargeFee']");
	var sumFee = parseFloat(0.00);
	$(items).each(function(){
		var chargeFee = $(this).val();
		if(chargeFee == "" || isNaN(chargeFee)){
			chargeFee = parseFloat(0.00);
		}
		sumFee =sumFee + parseFloat(chargeFee);
	});
	
	$("#sumChargeFee").val(sumFee);
}

// 定损合计	
function calSumLoss(field){
	if(isNaN($(field).val())){
		return false;
	}
	
	var actualValue = parseFloat(0.00);
	if(!isNaN($("#actualValue").val()) && $("#actualValue").val()!=""){
		actualValue = parseFloat($("#actualValue").val());
	}
	//标的车才校验实际价值不超过保额
	if(field.name == "lossCarMainVo.actualValue"){
		var deflossCarType = $("#deflossCarType").val();
		var certainType = $("#certainType").val();
		if(deflossCarType == "1"){
			if(certainType == "02"){//推定全损
				var carAmount = parseFloat($("#carAmount").val());
				if(actualValue > carAmount){
					layer.msg("实际价值不能超过车损险保额");
					$("#actualValue").val("");
					return ;
				}
			}else if(certainType == "03"){
				var theftAmount = parseFloat($("#theftAmount").val());
				if(actualValue > theftAmount){
					layer.msg("实际价值不能超过盗抢险保额");
					$("#actualValue").val("");
					return ;
				}
			}
		}
	}
	
	var sumRemnant = parseFloat(0.00);
	if(!isNaN($("#other_sumRemnant").val()) && $("#other_sumRemnant").val()!=""){
		sumRemnant = parseFloat($("#other_sumRemnant").val());
	}
	
	var otherFee = parseFloat(0.00);
	if(!isNaN($("#otherFee").val()) && $("#otherFee").val()!=""){
		otherFee = parseFloat($("#otherFee").val());
	}
	
	var sumLossFee = parseFloat(parseFloat(actualValue-sumRemnant+otherFee).toFixed(2));
	if(sumLossFee<0){
		layer.msg("合计金额不能小于0");
		$("#other_sumLossFee").val("");
		$(field).val("");
		return;
	}
	$("#other_sumLossFee").val(actualValue-sumRemnant+otherFee);
}

//定损方式改变
function changeCertainLoss(){
	
	var certainType = $("#certainType").val();//定损方式
	var deflossCarType = $("#deflossCarType").val();//损失类别
	var actualValuePo = $("#actualValuePo").val();//数据库中的实际价值
	var certainTypePo = $("#certainLossTypePo").val();//已暂存的定损方式
	
	if($("#defloss_SubRiskDiv").length>0){
		$("#defloss_SubRiskDiv").css("display","block");
	}
	
	if(!isEmpty($("#jy_sumRemnant")) && !isEmpty($("#jy_sumLossFee")) 
				&& ($("#jy_sumRemnant").val()>0 || $("#jy_sumLossFee").val()>0)){
		if(certainType=="02" || certainType=="03" || certainType == "05" 
			|| (certainType=="06" && (certainTypePo=="01" || certainTypePo=="04"))){
			layer.msg("精友定损有金额，不能更改定损方式");
			 $("#certainType").val(certainTypePo);//置为原来的定损方式
			return ;
		}
	}
	//如果由快处快赔改成修复定损或被代为定损（需要进精友），则各金额置0
	if(certainTypePo=="06" && (certainType=="01" || certainType=="04")){
		$("#jy_sumComFee").val(parseFloat(0));
		$("#jy_sumMatFee").val(parseFloat(0));
		$("#jy_sumRepairFee").val(parseFloat(0));
		$("#jy_sumOutFee").val(parseFloat(0));
		$("#jy_sumRemnant").val(parseFloat(0));
		$("#jy_sumLossFee").val(parseFloat(0));
	}
	
	if(certainType=="02" || certainType=="03" ){//推定全损 和盗抢折旧
		$("#jySumList").css('display','none'); //精友汇总定损金额
		$("#otherSumList").css('display','block'); //其他方式汇总定损金额
		$("#jyCertain").css("display","none");//定损明细模块
		
		$("#jy_sumComFee").attr("readonly",true);
		$("#jy_sumMatFee").attr("readonly",true);
		$("#jy_sumRepairFee").attr("readonly",true);
		$("#jy_sumOutFee").attr("readonly",true);
		$("#jy_sumRemnant").attr("readonly",true);
		
		if(deflossCarType == "1"){//标的车
			if(certainType=="02"){//推定全损
				if(certainTypePo !="02"){
					var carAmount = $("#carAmount").val();
					$("#actualValue").val(carAmount);
				}else{
					$("#actualValue").val(actualValuePo);
				}
			}else if(certainType=="03"){//盗抢折旧
				if(certainTypePo !="03"){
					var theftAmount =$("#theftAmount").val();
					$("#actualValue").val(theftAmount);
				}else{
					$("#actualValue").val(actualValuePo);
				}
			}
			calSumLoss($("#actualValue"));
		}
		//施救金额
		$("#jy_sumRescueFee").removeClass("Validform_error");
		$("#jy_sumRescueFee").qtip('destroy',true);
		$("#jy_sumRescueFee").val("");
		
	}else if(certainType=="06"){//快处快赔
		$("#jySumList").css('display','block'); 
		$("#otherSumList").css('display','none');
		
		$("#otherSumList input").each(function(){
			$(this).val("");
			$(this).removeClass("Validform_error");
			$(this).qtip('destroy',true);
		});
		
		$("#jy_sumRescueFee").val("0");
		$("#jy_sumLossFee").val("0");
		$("#jyCertain").css("display","none");
		$("#jy_sumComFee").attr("readonly",false);
		$("#jy_sumMatFee").attr("readonly",false);
		$("#jy_sumRepairFee").attr("readonly",false);
		$("#jy_sumOutFee").attr("readonly",false);
		$("#jy_sumRemnant").attr("readonly",false);
		$("#jy_sumRescueFee").attr("readonly",false);
		
		//删除附加险条数，不显示附加险
		$("#subRiskTbody :button").each(function(){
			delSubRisk(this);
		});
		if($("#defloss_SubRiskDiv").length>0){
			$("#defloss_SubRiskDiv").css("display","none");
		}
	}else {//修复定损 和被代位协议定损 ,无损失
		$("#jySumList").css('display','block'); 
		$("#otherSumList").css('display','none');
		
		$("#jy_sumComFee").attr("readonly",true);
		$("#jy_sumMatFee").attr("readonly",true);
		$("#jy_sumRepairFee").attr("readonly",true);
		$("#jy_sumOutFee").attr("readonly",true);
		$("#jy_sumRemnant").attr("readonly",true);
		
		$("#otherSumList input").each(function(){
			$(this).val("");
			$(this).removeClass("Validform_error");
			$(this).qtip('destroy',true);
		});
		
		if(certainType=="05"){//无损失
			$("#jy_sumRescueFee").val("0");
			$("#jy_sumLossFee").val("0");
			$("#jyCertain").css("display","none");
			$("#jy_sumRescueFee").attr("readonly","readonly");
			
			//删除附加险条数，无损失不显示附加险
			$("#subRiskTbody :button").each(function(){
				delSubRisk(this);
			});
			if($("#defloss_SubRiskDiv").length>0){
				$("#defloss_SubRiskDiv").css("display","none");
			}
		}else{
			$("#jyCertain").css("display","block");
			$("#jy_sumRescueFee").removeAttr("readonly");
		}
	}
	if(certainType=="02" || certainType=="03" || certainType=="05"){
		$("#factoryCode").removeAttr("datatype");
		$("#factoryCode").removeClass("Validform_error");
		$("#factoryCode").qtip('destroy',true);
	}
	
	noLossChange();
	if($("#certainType").val()=="05"){
		$("#carInfo_Div input[type='text']").blur();
	}
	
}

//推修时间不能早于出险时间。
//预计修理完毕日期  不能早于推修时间。
//定损日期不能早于出险时间。
function checkDate(){
	var estimatedDate = $("#estimatedDate").val();//预计修理完成日期
	var sendDate =$("#sendDate").val();//送修日期
	var deflossDate = $("#deflossDate").val();//定损日期
	var damageDate = $("#damageDate").val();//出险日期
	if(sendDate!=""){
		if(compareFullDate(sendDate,damageDate)<0){
			layer.msg("推修时间不能早于出险时间");
			return false;
		}
		if(estimatedDate!=""){
			if(compareFullDate(estimatedDate,sendDate)<0){
				layer.msg("预计修理完毕日期 不能早于推修时间");
				return false;
			}
		}
	}
	if(compareFullDate(deflossDate,damageDate)<0){
		layer.msg("定损日期不能早于出险时间");
		return false;
	}
	return true;
}

//比较两个日期字符串
//date1=date2则返回0 , date1>date2则返回1 , date1<date2则返回-1
function compareFullDate(date1,date2){	
	var strValue1=date1.split('-');
	var date1Temp=new Date(strValue1[0],parseInt(strValue1[1],10)-1,parseInt(strValue1[2],10));
	
	var strValue2=date2.split('-');
	var date2Temp=new Date(strValue2[0],parseInt(strValue2[1],10)-1,parseInt(strValue2[2],10));
	if(date1Temp.getTime()==date2Temp.getTime())
		return 0;
	else if(date1Temp.getTime()>date2Temp.getTime())
		return 1;
	else
		return -1;
}
/**
 * 责任比例赋值
 */
function changeDuty(){
	var indemnityDutyRate = $("#indemnityDuty").val();
	if(indemnityDutyRate == null || indemnityDutyRate == ""){
		$("#indemnityDutyRate").val("");
	  }else if(indemnityDutyRate == "0"){
		  $("#indemnityDutyRate").val("100");
	  }else if(indemnityDutyRate == "1"){
		  $("#indemnityDutyRate").val("70");
	  }else if(indemnityDutyRate == "2"){
		  $("#indemnityDutyRate").val("50");
	  }else if(indemnityDutyRate == "3"){
		  $("#indemnityDutyRate").val("30");
	  }else if(indemnityDutyRate == "4"){
		  $("#indemnityDutyRate").val("0");
	  }
}
/**
 * 责任比例比较
 */
function compareDutyRate(field){
	var rate = parseFloat($(field).val());
	var indemnityDuty = $("#indemnityDuty").val();
	var mixRate = 0;
	var rateName ="";
	if(indemnityDuty == "0"){
		mixRate = 100;
		rateName ="全责";
	  }else if(indemnityDuty == "1"){
		mixRate = 70;
		rateName ="主责";
	  }else if(indemnityDuty == "2"){
		mixRate = 50;
		rateName ="同责";
	  }else if(indemnityDuty == "3"){
		  mixRate = 30;
		  rateName ="次责";
	  }
	if(rate < mixRate) {
		layer.msg("责任比例不能小于"+rateName+"比例"+mixRate+"%");
		changeDuty();
		
	}
}

//单击选中全部则选中所有损失部位
function allCheck(obj){
	if($(obj).is(":checked")){
		$(obj).parent().prevAll().find("input").each(function(){
		//	console.log($(obj).html());
			if($(this).attr("type")=="checkbox"){
				$(this).prop("checked",obj.checked);
			}
		});
		$("input[name='lossCarMainVo.lossPart']").focus();
	}
}
//案件备注功能实现
$("#checkRegistMsg").click(
	function checkRegistMsgInfo(){//打开案件备注之前检验报案号和节点信息是否为空
		var registNo=$("#registNo").val();
		var nodeCode=$("#currentNode").val();
		createRegistMessage(registNo,nodeCode);
		
});

//收款人选择界面打開
var payIndex=null;
function showPayCust(element){
	var registNo = $("#registNo").val();
	var tdName = $(element).attr("name");
	var url="/claimcar/payCustom/payCustomList.do?registNo="+registNo+"&tdName="+tdName+"";
	if(payIndex==null){
		payIndex=layer.open({
		    type: 2,
		    title: '收款人选择',
		    shade: false,
		    area: ['1000px', '500px'],
		    content:url,
		    end:function(){
		    	payIndex=null;
		    }
		});
	}
}

function subrogationClick(){
	var flag = $("input[name='subrogationMain.subrogationFlag']:checked").val(); 
	if(flag == "1"){
		$("#subRogationDiV").css('display','block');
		//责任对方内容（责任方为机动车）
		//车辆VIN码
		$("#subRogationDiV").find("[name$='vinNo']").attr("datatype","vin");
		//发动机号
		$("#subRogationDiV").find("[name$='engineNo']").attr("datatype","*");
		//责任对方内容（责任方为非机动车）的姓名/名称
		$("#subrogationPerTbody").find("[name$='name']").attr("datatype","*");
		
	}else{
		
		$("#subRogationDiV").css('display','none');
		//责任对方内容（责任方为机动车）
		//车辆VIN码
		$("#subRogationDiV").find("[name$='vinNo']").removeAttr("datatype");
		$("#subRogationDiV").find("[name$='vinNo']").removeClass("Validform_error");
		$("#subRogationDiV").find("[name$='vinNo']").qtip('destroy',true);
		//发动机号
		$("#subRogationDiV").find("[name$='engineNo']").removeAttr("datatype");
		$("#subRogationDiV").find("[name$='engineNo']").removeClass("Validform_error");
		$("#subRogationDiV").find("[name$='engineNo']").qtip('destroy',true);
		
		//责任对方内容（责任方为非机动车）的姓名/名称
		$("#subRogationDiV").find("[name$='name']").removeAttr("datatype");
		$("#subRogationDiV").find("[name$='name']").removeClass("Validform_error");
		$("#subRogationDiV").find("[name$='name']").qtip('destroy',true);
	}
}

//发起定损

function submitDloss(){
/*	var persInjuredVo = new Array();
	$("input[name='persInjuredVo']:checked").each(function() {
		persInjuredVo.push($(this).val());
	});*/
	var carVos = new Array();
	$("input[name='carVos']:checked").each(function() {
		carVos.push($(this).val());
	});
	var propVos = new Array();
	$("input[name='propVos']:checked").each(function() {
		propVos.push($(this).val());
	});
	var remarks = $("[name='remarks']").val();

		$.ajax({
			url  : "/claimcar/defloss/AddLossSubmit.do?propVos="+propVos+"&carVos="+carVos,
			type : 'post', // 数据发送方式
			 dataType : 'json', // 接受数据格式
			data : {
				"remarks":remarks
			}, // 要传递的数据
			async:false, 
			traditional: true, 
			success : function(jsonData) {// 回调方法，可单独定义
				if(jsonData.status=="200" && jsonData.statusText==""){
					layer.confirm('发起成功!', {
						btn: ['确定'] //按钮
					}, function(index){
						parent.layer.closeAll();
					});
				}else{
					layer.confirm(jsonData.statusText, {
						btn: ['确定'] //按钮
					}, function(index){
						parent.layer.closeAll();
					});
				}
				
				
				}
		});
	}	 

function calSumOutRepiar(field){
	if(isNaN($(field).val())){
		return false;
	}
	var sumDefLoss = 0;
	$("#outRepairBody [name$='sumDefLoss']").each(function(){
		if($(this).val()!=null&&$(this).val()!=""){
			sumDefLoss += parseFloat($(this).val());
		}
	});

	$("#jy_sumOutFee").val(sumDefLoss);
	calJySum();
}


function calJySum(){
	var sumComFee = parseFloat(0);
	var sumMatFee = parseFloat(0);
	var sumRepairFee = parseFloat(0);
	var sumOutFee = parseFloat(0);
	var sumRemnant = parseFloat(0);
	var certainType = $("#certainType").val();//定损方式
	
	if(!isBlank($("#jy_sumComFee").val())){
		sumComFee = parseFloat($("#jy_sumComFee").val());
	}
	
	if(!isBlank($("#jy_sumMatFee").val())){
		sumMatFee = parseFloat($("#jy_sumMatFee").val());
	}
	
	if(!isBlank($("#jy_sumRepairFee").val())){
		sumRepairFee = parseFloat($("#jy_sumRepairFee").val());
	}
	
	if(!isBlank($("#jy_sumOutFee").val())){
		sumOutFee = parseFloat($("#jy_sumOutFee").val());
	}
	
	if(!isBlank($("#jy_sumRemnant").val())){
		sumRemnant = parseFloat($("#jy_sumRemnant").val());
	}
	
	var sumDefLoss = sumComFee + sumMatFee + sumRepairFee + sumOutFee -sumRemnant;
	
	$("#jy_sumLossFee").val(sumDefLoss); 
	
	if(certainType == "06"){
		$("input[name='lossCarMainVo.sumLossFee']").val(sumDefLoss);
	}
	
}

function initLossPart(){
	$("input:checkbox[name$='lossPart']").each(function(){
		var val = $(this).val();
		if(val == "12" || val == '13'){
			$(this).parents("label").hide();
		}
	});
}


// 是否水淹
function isWaterFlooded(){
	var  isWaterFlooded = $("input[name='lossCarMainVo.isWaterFloaded']:checked");
	if(isWaterFlooded.val() == '1'){
		$('#waterFloodedLevel').show();
	}else{	
		$('#waterFloodedLevel').hide();
	}
}

function checkCertifyNo(){
	 var certifyNo = $("[name='carInfoVo.identifyNo']").val();
	 var certifyType = $("[name='carInfoVo.identifyType']").val();
	 var regs = new RegExp("^(.{9}|.{18})$");
	 //证件类型为组织机构代码时，证件号码只能是8位或者18位
	 if(certifyType == "10"){
		 if(!regs.test(certifyNo)){
			 layer.alert("请录入正确的9位或18位组织机构代码!"); 
			 return false;
		 }
	 }
	 return true;
}

function changeIdentifyNo(obj){
	toUpperValue(obj);
	checkCertifyNo();
}

function initLossTyPe(){
	var deflossCarType = $("#deflossCarType").val();
	var isQuickCase = $("#isQuickCase").val();
	var offLineHanding = null;
	if(deflossCarType == "1"){
		offLineHanding = $("input[name='lossCarMainVo.offLineHanding']:checked").val();
	}else{
		offLineHanding = $("input[name='lossCarMainVo.offLineHanding']").val();
	}
	if(offLineHanding != "0" || isQuickCase != "1"){
		$("#certainType").find("option[value='06']").attr("disabled","disabled");
	}else{
		$("#certainType").find("option[value='06']").removeAttr("disabled").qtip('destroy', true);
	}
}

function changeOffLineHanding(){
	offLineHanding = $("input[name='lossCarMainVo.offLineHanding']:checked").val();
	if(offLineHanding == "1"){
		$("#certainType").find("option[value='06']").attr("disabled","disabled");
	}else{
		$("#certainType").find("option[value='06']").removeAttr("disabled").qtip('destroy', true);
	}
}

var PhotoVerify_index = null;
function openPhotoVerify(nodeCode){
	var lossMainId = $("#lossMainId").val();
	var registNo = $("#registNo").val();
	var offLineHanding = $("input[name='lossCarMainVo.offLineHanding']:checked").val();
	var url = "/claimcar/defloss/openPhotoVerify.do?registNo="+registNo+"&mainId="+lossMainId+"&offLineHanding="+offLineHanding+"&nodeCode="+nodeCode;
	if(PhotoVerify_index == null){//防止打开多个页面
		PhotoVerify_index = layer.open({
			type : 2,
			title : '照片审核',
			shade : false,
			area : [ '30%', '35%' ],
			content : url,
			end : function() {
				PhotoVerify_index = null;
			}
		});
	}
}
function showDlhk(){
	var registNo = $("#jyRegistNo").val();
	var url="/claimcar/defloss/showDlhk.do?registNo="+registNo;
	layer.open({
		title : '阳杰复检信息',
		type : 2,
		area : [ '80%', '80%' ],
		fix : true, // 固定
		maxmin : true,
		content : [ url, "yes" ],
		end : function() {
			
		}
	});
}