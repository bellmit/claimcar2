//----------------------------------------------//
/**
 * 车辆处理JQuery ----luwei
 */
var ajaxEdit;
var vinNoFlag =true;
var licenType;
$(function() {
	// 1.控件初始化
	// $(".saveCar").Validform();
	// 2.控件事件
	// 下拉框动态赋值
	
	if(isBlank($("#IindemnityDutyRates").val())){
		$("#IindemnityDutyRates").val("0.0");
	}
		
	$("#Iindemnitydutys").change(function() {
		var duty = $("#Iindemnitydutys").val();
		var dutyRate = $("#IindemnityDutyRates");
		if (isBlank(duty)) {
			dutyRate.val("0");
		} else if (duty == 0) {
			dutyRate.val("100");
		} else if (duty == 1) {
			dutyRate.val("70");
		} else if (duty == 2) {
			dutyRate.val("50");
		} else if (duty == 3) {
			dutyRate.val("30");
		} else if (duty == 4) {
			dutyRate.val("0");
		}
	});
	
	
	/*$("tbody#thridCarItem [name='initThirdCarsss']").each(function(){
		//$(this).removeAttr("disabled");
		$(this).attr("disabled",false);
	});*/
	
	//除正在处理
	var status = $("#carStatus").val();
	var nodeCode = $("#carNodeCode").val();
	if (nodeCode!="Chk" || status!="2") {
		$("body :input").each(function() {
			$(this).attr("disabled", "disabled");
		});
		
		//隐藏暂存按钮
		var sci=$("#saveCarInfo");
		sci.removeClass("btn btn-primary");
		sci.addClass("hide");
		
		$("#closeCarInfo").removeAttr("disabled");
		$("input[name='initThirdCarsss']").removeAttr("disabled");
		
		var faBu=$("#factoryButton");
		faBu.removeClass("btn btn-secondary");
		faBu.addClass("btn btn-disabled");
		faBu.removeAttr("onclick");
	}
	
	var rule = [{
		ele : "input[name='checkCarVo.lossPart']:first",
		datatype : "checkBoxMust",
		nullmsg : "损失部位至少选择一项！",
	}];
	
	$.Datatype.amount = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;//金额，
	$.Datatype.age = /^(?:[1-9][0-9]?|1[01][0-9]|120)$/;//年龄
	$.Datatype.licenNo=/^(WJ|[\u0391-\uFFE5]{1})[A-Z0-9]{5,}[\u0391-\uFFE5]{0,1}$/;//车牌号
//	$.Datatype.licenNo=/^([\u4e00-\u9fa5-A-Z]{1}[A-Z]{1}[A-Z_0-9]{5})|(新000000)$/;//车牌号
	$.Datatype.letterAndNumber=/^[A-Za-z0-9]+$/;//字母和数字
	$.Datatype.vinMotuo = /^[A-HJ-NPR-Z0-9]{1,}$/;
	$.Datatype.checkBoxMust = function(gets, obj, curform, regxp) {
		var need = 1, 
		numselected = curform.find("input[name='" + obj.attr("name") + "']:checked").length;
		return numselected >= need ? true : "请至少选择" + need + "项！";
	};
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
			if($(obj).attr("id")=="frameNo" && $("#vinNo").val()==""){
				$("#vinNo").val(code);
			}
			if($(obj).attr("id")=="vinNo" && $("#frameNo").val()==""){
				$("#frameNo").val(code);
			}
		}
    };
	
	var saveType;
	if(isBlank($("#carId").val())){
		saveType ="add";
	}else{
		saveType ="modify";
	}
	// Ajax表单操作相关
	ajaxEdit = new AjaxEdit($('#saveCar'));
	ajaxEdit.targetUrl = "/claimcar/checkcar/saveCheckCar.do";
	ajaxEdit.rules = rule;
	/*ajaxEdit.beforeSubmit = function (){//在验证成功后，表单提交前执行的函数
		layer.load(0, {
			shade : [0.8, '#393D49']
		});
	};*/
	ajaxEdit.beforeSubmit = before;//在验证成功后，表单提交前执行的函数
	ajaxEdit.afterSuccess = function(JsonData) {//保存成功 回调函数
		var result = eval(JsonData);
		if (result.status == "200") {
			// 保存成功，关闭页面
			var index = parent.layer.getFrameIndex(window.name);// 获取窗口索引
			layer.alert("保存成功",{shadeClose: false,closeBtn: 0},function(){
				if(result.datas.serialNo==1){
					parent.$("#indemnityDutyRate").val(result.datas.mainCarDuty);
					parent.$("#indemnityDutyRateValue").val(result.datas.mainCarDutyRate);
					//parent.$("#indemnityDutyRate").change();
				}
				//刷新当前操作的该条数据
				parent.loadReplaceCarTr(result.datas.carId,result.datas.serialNo,saveType);
				
				//刷新财产和人伤的损失方。。
				parent.refreshPropKey($("#carRegistNo").val());
				
				parent.layer.close(index);// 执行关闭
			});
		}
	};
	ajaxEdit.error=function(){
		layer.alert("保存失败！");
	};
	ajaxEdit.bindForm();//绑定表单
	
	
	// 查勘损失标记信息
	checkInfo();
	if(status=="2"){
		initKindSelect();
	}
	//年龄显示以数据库值为准，初始化无需重新根据身份证计算带出。
	//changeIdentifyType($("#identifyTypeB"));
	var identifyType = $("option:selected", $("#identifyTypeB")).val();
	var idN = $("#identifyNumberB");
	if (identifyType=="1"){
		idN.removeAttr("datatype").attr("datatype","idcard");
		//identifyBlur();
		//drivingFocus();
	}else if (identifyType=="10"){
		idN.removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
	} else if(identifyType=="99"){
		idN.removeAttr("datatype").attr("datatype","*");
		idN.removeClass("Validform_error").qtip('destroy',true);
	} else{
		idN.removeAttr("datatype").attr("datatype", "letterAndNumber");
		idN.removeClass("Validform_error").qtip('destroy',true);
	}
	if(isBlank($("#IindemnityDutyRates").val())){
		$("#IindemnityDutyRates").val("0.0");
	};
	
//	var carOwner=$("#carOwner");
//	var brNam=$("#brandName");
//	var serialNo=$("#checkCarType").val();
//	if(Number(serialNo)==1){
//		carOwner.attr("datatype","*").attr("nullmsg","请填写车主！");
//		brNam.attr("datatype","*").attr("nullmsg","请填写车型名称！");
//	}else{
//		carOwner.removeAttr("datatype").removeAttr("nullmsg");
//		brNam.removeAttr("datatype").removeAttr("nullmsg");
//	}
	setVinChange($("select[name='checkCarVo.prpLCheckCarInfo.licenseType']"));
	
	//vin码初始化带入车架号的值
	if(nodeCode=="Chk" && status=="2" && !isBlank($("#frameNo").val())){
		$("#vinNo").val($("#frameNo").val());
	}
	initLossPart();
});

function setVinChange(obj){
	licenType = $("option:selected", $(obj)).val();
	var vinType = "vinNo";
	if("07"==licenType||"08"==licenType||"09"==licenType||"10"==licenType
			||"11"==licenType||"12"==licenType||"14"==licenType||"17"==licenType
			||"19"==licenType||"21"==licenType){
		vinType = "vinMotuo";
	}
	$("#frameNo").removeAttr("datatype").attr("datatype",vinType);
	$("#vinNo").removeAttr("datatype").attr("datatype",vinType);
}

function initLossPart(){
	$("input:checkbox[name$='lossPart']").each(function(){
		var val = $(this).val();
		if(val == "12" || val == '13'){
			$(this).parents("label").hide();
		}
	});
}

/**
 * 表单提交--->保存车损
 */
function saveCarInfos(){
	var frameNo = $("#frameNo").val();
	var vinNo = $("#vinNo").val();
	var brandName = $("#brandName").val();
	var carSerialNo = $("#carSerialNo").val();
	var enrollDate = $("#enrollDate").val();
	var acceptLicenseDate = $("#acceptLicenseDate").val();
	var comCode = $("#comCode").val();
	
	//海南地区案件 车型名称，三者车 初登日期和领证日期不能为空
	if(comCode.slice(0, 2) == 20){
		//车型名称不能为空
		if(brandName.match(/^[ ]*$/)){
			layer.alert("海南地区案件 “车型名称” 不能为空！！");
			return;
		}
		//车辆属性 为 “三者车” 时，初登日期和初次领证日期不能为空
		if(carSerialNo != "1"){
			if(enrollDate.match(/^[ ]*$/)){
				layer.alert("海南地区案件三者车的 “初登日期” 不能为空！！");
				return;
			}
			if(acceptLicenseDate.match(/^[ ]*$/)){
				layer.alert("海南地区案件三者车的 “初次领证日期” 不能为空！！");
				return;
			}
		}
	}
	
	if(!validVINCode(frameNo) || !validVINCode(vinNo)){
		vinNoFlag =false;
	}
	if(!("07"==licenType||"08"==licenType||"09"==licenType||"10"==licenType
			||"11"==licenType||"12"==licenType||"14"==licenType||"17"==licenType
			||"19"==licenType||"21"==licenType)){
		if(!vinNoFlag){
			layer.confirm('VIN码不符合平台校验规则，请联系平台进行电子联系单特批处理！如VIN码有误会影响后续单证处理！', {
				btn : [ '确定', '取消' ]
				}, function(index) {
					$("#saveCar").submit();
					});
		}else{
			$("#saveCar").submit();
		}
	}else{
		$("#saveCar").submit();
	}
}

//关闭
function closeCarLoss(nodeCode){
	if(nodeCode=="Chk"){
		saveCarInfos();
	}
	var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	parent.layer.close(index);// 执行关闭
} 

/**
 * 责任比例比较
 */
function compareDutyRate(field){
	var rate = parseFloat($(field).val());
	var indemnityDuty = $("#Iindemnitydutys").val();
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
	  }else if(indemnityDuty == "4"){
		  $("#IindemnityDutyRates").val("0.0");
	  }
	if(indemnityDuty != "3" && rate < mixRate) {
		layer.alert("责任比例不能小于"+rateName+"比例"+mixRate+"%");
		changeDuty();
		
	}
}

function changeDuty() {
	var duty = $("#Iindemnitydutys").val();
	var dutyRate = $("#IindemnityDutyRates");
	if (isBlank(duty)) {
		duty.val("");
	} else if (duty=="0"||duty==0) {
		dutyRate.val("100");
	} else if (duty == "1"||duty==1) {
		dutyRate.val("70");
	} else if (duty == "2"||duty==2) {
		dutyRate.val("50");
	} else if (duty == "3"||duty==3) {
		dutyRate.val("30");
	} else if (duty == "4"||duty==4) {
		dutyRate.val("0");
	}
}

function initKindSelect(){
	var damageName = parent.window.getDamageCode();
	var kindCode=$("select[name='checkCarVo.kindCode']");
	var kindCode_A =$("#kindCode_A").val();
//	var carSerialNo=$("#carSerialNo").val();
	if(parent.window.getClaimSelf()!="1"){
		if(damageName=="DM02"){
			kindCode.val("F");
		}else if(damageName=="DM03"){
			kindCode.val("L");
		}else if(damageName=="DM04"){
			kindCode.val("G");
		}else if(damageName=="DM05"){
			kindCode.val("Z");
		}else{
			if(kindCode_A=="A"){
				kindCode.val("A");
			}else{
				kindCode.val("A1");
			}
		}
	}
//	kindSelect(carSerialNo);
}

//险别选择
function kindSelect(serialNo){
	var kindCode=$("select[name='checkCarVo.kindCode']");
	if(Number(serialNo)==1){
		if(parent.window.getClaimSelf()=="1"){
			kindCode.find("option[value='BZ']").removeAttr("disabled");
			kindCode.val("BZ");
			kindCode.find("option[value!='BZ']").attr("disabled","disabled");
		}else{
			kindCode.find("option[value='BZ']").attr("disabled","disabled");
			kindCode.find("option[value!='BZ']").removeAttr("disabled");
		}
	}
}

// -----------------------------------------------------------

function before() {
	//查勘信息
	var daf=$("input[name='checkCarVo.lossFlag']:checked");
	var damageFlag=daf.val();
	var lossFee=$("#lossFee");
	if(damageFlag=="0"){//有损失的必录项
		lossFee.removeAttr("datatype");
		lossFee.attr("datatype","*0-0|amount");
		if(isBlank(lossFee.val())){
			layer.alert("有损失，请录入估损金额！");
			return false;
		}
	}else{
		lossFee.removeAttr("datatype");
//		if(isBlank(lossFee.val())){
//			lossFee.val("0");
//		};
		if(isBlank(lossFee.val())||lossFee.val()!=0||lossFee.val()!="0"){
			lossFee.val("0");
		}
	}
	
	var ref=$("#rescueFee");
	var refv=ref.val();
	if(isBlank(refv)){
		ref.val("0");
	}
	// 车损保存校验
	if (!saveValid(damageFlag)) {
		return false;
	}
	
	//新增校验
	/*var add=$("input[name='addCarFlag']");
	var addv=add.val();
	if(addv=="addCarFlag"){
		
	}*/
	/*if(!("07"==licenType||"08"==licenType||"09"==licenType||"10"==licenType
			||"11"==licenType||"12"==licenType||"14"==licenType||"17"==licenType
			||"19"==licenType||"21"==licenType)){
		if(!vinNoFlag){
			if(!window.confirm("VIN码不符合平台校验规则，请联系平台进行电子联系单特批处理！如VIN码有误会影响后续单证处理！")){
				return false;
			}
		}
	}*/
	
	if(!saveCarValid()){
		return false;
	}
	if(!checkIdentifyNumber()){
		return false;
	}
}

//修理厂按钮
function factoryCheck(){
	var url="/claimcar/defloss/findViewList.do";
	layer.open({
	    type: 2,title:"选择修理厂",closeBtn: 1,
	    scrollbar: false,skin: 'yourclass',
	    area: ['90%', '70%'],content:url,
	});
}

function saveValid(damageFlag){
	//标的车的车主不能为空
	var no=$("#carSerialNo");
	var serNo=no.val();
	var daf=$("#carCheckInfo input[name='checkCarVo.damageFlag']:checked");
	//单车事故，事故类型非单方事故，录三者车，不允许录入有损失
	var single=parent.window.getSingle();
	var accType=parent.window.getAccType();
	if(Number(serNo)!=1&&Number(single)==1
			&&accType!="01"&&damageFlag=="0"){
		layer.alert("单车事故,非单方事故，三者车不允许录入有损失！");
		daf.val("1");
		return false;
	}
//	//重大赔案上报
//	var ref = parent.window.getReportFlag();
//	var fee=$("#lossFee");var lof=fee.val();
//	if(Number(ref)==1&&(lof==""||lof==0)){
//		layer.alert("重大案件上报，车辆损失估损金额不能为空！");
//		return false;
//	}
//	var daf=$("input[name='checkCarVo.lossFlag']:checked");
//	if(Number(ref)==1&&Number(daf.val())==1){
//		layer.alert("重大案件上报，车辆损失不能选择无损失！");
//		return false;
//	}
	//
	var identifyTypeB = $("#identifyTypeB").val();
	if(!identifyTypeB == "99"){//如果证件类型为其他
		var idN=$("#identifyNumberB");
		if(!checkIdNew(idN.val())){
			return false;
		}	
	}
	return true;
}

function saveCarValid(){
	// 3.页面校验
	//异步校验，后台校验，，车牌号和车架号不能与已有的相同
	var reNo=$("#carRegistNo").val();var liNo=$("#licenseNo").val();
	var frNo=$("#frameNo").val();var vinNo=$("#vinNo").val();var carId=$("#carId").val();
	var idNoType=$("#identifyTypeB").val();var idNo = $("#identifyNumberB").val();var driverLicenseNo = $("#drivingLicenseNo").val();
	var licenseType =$("#licenseType").val();
	var carKind = $("#carKindCode").val();
	var seriNo =  $("#carSerialNo").val();
	var url ="/claimcar/checkcar/carSaveValid.do";
	var params = {"registNo" : reNo,"licenseNo" : liNo,
			"frameNo" : frNo,"vinNo":vinNo,"carId" : carId,
			"idNoType" : idNoType,"idNo" : idNo,
			"licenseType": licenseType,
			"drivingLicenseNo":driverLicenseNo,
			"carKind":carKind,
			"seriNo":seriNo,
			"checkTime" : $("#checkTime").val()};
	var returnflag = true;
	$.ajax({
		url : url, // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false, 
		success : function(jsonData) {// 回调方法，可单独定义
			var result = eval(jsonData);
			if (result.status == "200") {
				if(result.data !="ok"){
					layer.alert(result.data);
					returnflag = false;
				}
			}
		}
	});
	return returnflag;
}

/**证件号验证*/
function checkIdNew(value){
	//必须为15位或18位
	/*if(value.length!=15 && value.length!=18){
		return false;
	}*/
	//必须为数字或字母
	var Regx = /(^[A-Za-z0-9]+$)/ ;
	if(!isBlank(value)&&!Regx.test(value)){
		layer.alert("证件号码必须为数字或字母！");
		return false;
	}
	//不能出现15位及以上的连续字符
	var count = 0;
	for(var i=0;i<15;i++){
		if(value.substring(0,1)==value.substring(i,i+1)){
			count++;
		};
	}
	if(!isBlank(value)&&count>=15){
		layer.alert("证件号码不能出现15位及以上的连续字符！");
		return false;
	}
	return true;
}

//证件类型为身份证时
function changeIdentifyType(obj){
	var identifyType = $("option:selected", $(obj)).val();
	var idN = $("#identifyNumberB");
	if (identifyType=="1"){
		idN.removeAttr("datatype").attr("datatype","idcard");
		//identifyBlur();
		identifyBlur();
		drivingFocus();
	}else if (identifyType=="10"){
		idN.removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
	} else if(identifyType=="99"){
		idN.removeAttr("datatype").attr("datatype","*");
		idN.removeClass("Validform_error").qtip('destroy',true);
	} else{
		idN.removeAttr("datatype").attr("datatype", "letterAndNumber");
		idN.removeClass("Validform_error").qtip('destroy',true);
	}
}

function checkIdentifyNumber(){
	var identifyNumber = $("#identifyNumberB").val();
	var identifyType = $("option:selected", $("#identifyTypeB")).val();
	var regs = new RegExp("^(.{9}|.{18})$");
	//证件类型为组织机构代码时，证件号码只能是8位或者18位
	if(identifyType=="10"){
		if(!regs.test(identifyNumber)){
			 layer.alert("请录入正确的9位或18位组织机构代码!"); 
			 return false;
		 }
	}
	return true;
}

$("#identifyNumberB").change(function(){
	checkIdentifyNumber();
});

function identifyBlur(){
	var identifyType = $("option:selected", $("#identifyTypeB"));
	var idN = $("#identifyNumberB");
	var drN = $("#drivingLicenseNo");
	var drA = $("#driverAge");
	if (identifyType.val()=="1"){
		// 年龄
		var identifyNumber = idN.val();
		if (identifyNumber != null) {
			drN.val(idN.val());
			var strBirthday = getBirthdayByIdCard(identifyNumber);
			var age = jsGetAge(strBirthday);
			drA.val(age);
			//性别
			var sex = jsGetSex(identifyNumber);
			$("#driverSex").val(sex);
		}
	}
}
function drivingFocus() {
	var identifyType = $("option:selected", $("#identifyTypeB"));
	var drN = $("#drivingLicenseNo");
	var idN = $("#identifyNumberB");
	if (identifyType.val() == "1") {
		drN.val(idN.val());
	}
}

$("#identifyNumberB").blur(function() {
	var identifyType = $("option:selected", $("#identifyTypeB"));
	var idN = $("#identifyNumberB");
	var drN = $("#drivingLicenseNo");
	var drA = $("#driverAge");
	if (identifyType.val()=="1" || identifyType.val()=="3"){
		// 年龄
		var identifyNumber = idN.val();
		if (identifyNumber != null) {
			drN.val(idN.val());
			var strBirthday = getBirthdayByIdCard(identifyNumber);
			var age = jsGetAge(strBirthday);
			drA.val(age);
			//性别
			var sex = jsGetSex(identifyNumber);
			$("#driverSex").val(sex);
		}
	}
});

$("#drivingLicenseNo").focus(function() {
	var identifyType = $("option:selected", $("#identifyTypeB"));
	var drN = $("#drivingLicenseNo");
	var idN = $("#identifyNumberB");
	if (identifyType.val() == "1") {
		drN.val(idN.val());
	}
});


//0-有损失 ，1-无损失
function checkInfo() {
	var carSerialNo = $("#carSerialNo").val();
	var radio = $("#carCheckInfo input[name='checkCarVo.lossFlag']:checked").val();
	if(carSerialNo!=1){//三者车
		if(radio==1){//  0-有损失 ，1-无损失
//			$("#licenseNo").attr("ignore","ignore");
			$("#engineNo").attr("ignore","ignore");
			$("#frameNo").attr("ignore","ignore");
			$("#vinNo").attr("ignore","ignore");
			$("#driverName").removeAttr("datatype");
			$("#drivingLicenseNo").attr("ignore","ignore");
			$("#identifyTypeB").attr("ignore","ignore");
			$("#identifyNumberB").attr("ignore","ignore");
			$("input[name='checkCarVo.prpLCheckCarInfo.phone']").attr("ignore","ignore");
			$("#linkPhoneNumber").attr("ignore","ignore");
			
		}else{
			$("#licenseNo").removeAttr("ignore");
			$("#engineNo").removeAttr("ignore");
			$("#frameNo").removeAttr("ignore");
			$("#vinNo").removeAttr("ignore");
			$("#drivingLicenseNo").removeAttr("ignore");
			$("#driverName").attr("datatype","*1-20");
			$("#identifyTypeB").removeAttr("ignore");
			$("#identifyNumberB").removeAttr("ignore");
			$("input[name='checkCarVo.prpLCheckCarInfo.phone']").removeAttr("ignore");
			$("#linkPhoneNumber").removeAttr("ignore");
		}
	}
	if(radio==1||radio=="1"){
		$("#lossFee").val("0");
	}
}

/*function allCheck(obj){//单击选中全部则选中所有受损部位
	$(obj).parent().prevAll().find("input").each(function(){
		console.log($(obj).html());
		if($(this).attr("type")=="checkbox"){
			$(this).prop("checked",obj.checked);
		}
	});
}*/
//全车
function allCheck(obj) {
	if ($(obj).is(":checked")) {
		$(obj).parent().prevAll().find("input").each(function() {
			// console.log($(obj).html());
			if ($(this).attr("type") == "checkbox") {
				$(this).prop("checked", obj.checked);
			}
		});
		$("input[name='checkCarVo.lossPart']").focus();
	}
}


//车架号
$("#frameNo").blur(function() {
	if(!isBlank($("#frameNo").val())){
		$("#vinNo").val($("#frameNo").val());
	}
});
//$("#vinNo").blur(function() {
//	if($("#vinNo").val()!=""){
//		$("#frameNo").val($("#vinNo").val());
//	}
//});

//电话
var phone = $("input[name='checkCarVo.prpLCheckCarInfo.phone']");
phone.blur(function() {
	if(!isBlank($(phone).val())){
		$("#linkPhoneNumber").val($(phone).val());
	}
});
$("#linkPhoneNumber").focus(function(){
	var phone = $("input[name='checkCarVo.prpLCheckCarInfo.phone']").val();
	if(!isBlank(phone)){
		$("#linkPhoneNumber").val(phone);
	}
});

// 计算出生日期
function getBirthdayByIdCard(val) {
	var birthdayValue = null;
	if (15 == val.length) { // 15位身份证号码
		birthdayValue = val.charAt(6) + val.charAt(7);
		if (parseInt(birthdayValue) < 10) {
			birthdayValue = '20' + birthdayValue;
		} else {
			birthdayValue = '19' + birthdayValue;
		}
		birthdayValue = birthdayValue + '-' + val.charAt(8) + val.charAt(9)
				+ '-' + val.charAt(10) + val.charAt(11);
	}
	if (18 == val.length) { // 18位身份证号码
		birthdayValue = val.charAt(6) + val.charAt(7) + val.charAt(8)
				+ val.charAt(9) + '-' + val.charAt(10) + val.charAt(11) + '-'
				+ val.charAt(12) + val.charAt(13);
	}
	/*
	 * if(isValidDateTime(birthdayValue) === false){ birthdayValue = ""; }
	 */
	return birthdayValue;
}

/**
 * 计算周岁。
 * @param strBirthday
 * @returns {*}
 */
function jsGetAge(strBirthday) {
	if(isBlank(strBirthday)){
		return "";
	}
	var returnAge;
	var strBirthdayArr = strBirthday.split("-");
	var birthYear = strBirthdayArr[0];
	var birthMonth = strBirthdayArr[1];
	var birthDay = strBirthdayArr[2];

	d = new Date();
	var nowYear = d.getFullYear();
	var nowMonth = d.getMonth() + 1;
	var nowDay = d.getDate();

	if (nowYear == birthYear) {
		returnAge = 0;//同年 则为0岁
	} else {
		var ageDiff = nowYear - birthYear; //年之差
		if (ageDiff > 0) {
			if (nowMonth == birthMonth) {
				var dayDiff = nowDay - birthDay;//日之差
				if (dayDiff < 0) {
					returnAge = ageDiff - 1;
				} else {
					returnAge = ageDiff;
				}
			} else {
				var monthDiff = nowMonth - birthMonth;//月之差
				if (monthDiff < 0) {
					returnAge = ageDiff - 1;
				} else {
					returnAge = ageDiff;
				}
			}
		} else {
			returnAge = -1;//返回-1 表示出生日期输入错误 晚于今天
		}
	}
	return returnAge;//返回周岁年龄
}

/**
 * 计算性别
 * @param strBirthday
 * @returns {String}
 */
function jsGetSex(strBirthday) {
	var returnSex = "";
	if (parseInt(strBirthday.substr(16, 1)) % 2 == 1) {
		returnSex = "1";// 男
	} else {
		returnSex = "2";// 女
	}
	return returnSex;
}

