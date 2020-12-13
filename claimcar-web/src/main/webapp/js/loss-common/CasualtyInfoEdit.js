$(function() {
	layer.config({
		extend : 'extend/layer.ext.js'
	});
	initCasualtyInfoPage();

	$.Datatype.age = /^(?:0|[1-9][0-9]?|1[01][0-9]|120)$/;// 年龄
	$.Datatype.month = /^(0|[[1-9]|1[0-1])$/;// 月份
	$.Datatype.d = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;// 验证数字保留2位小数
	$.Datatype.selectMust = function(gets, obj, curform, regxp) {
		var code = $(obj).val();
		if (isBlank(code)) {
			return false;
		}
	};
	$.Datatype.checkBoxMust = function(gets, obj, curform, regxp) {
		var need = 1, numselected = curform.find("input[name='" + obj.attr("name") + "']:checked").length;
		return numselected >= need ? true : "请至少选择" + need + "项！";
	};

	var rule = [{
		ele : "select[name='prpLDlossPersTraceVo.prpLDlossPersInjured.treatSituation']",
		datatype : "selectMust",
		nullmsg : "案件类型不能选择空！",
	}, {
		ele : "select[name='prpLDlossPersTraceVo.prpLDlossPersInjured.lossItemType']",
		datatype : "selectMust",
		nullmsg : "人员属性不能选择空！",
	}, {
		ele : "select[name='prpLDlossPersTraceVo.prpLDlossPersInjured.certiType']",
		datatype : "selectMust",
		nullmsg : "证件类型不能选择空！",
	}, {
		ele : "select[name='prpLDlossPersTraceVo.traceForms']",
		datatype : "selectMust",
		nullmsg : "跟踪形式不能选择空！",
	}, {
		ele : "#serialNoId",
		datatype : "selectMust",
		nullmsg : "车牌号码不能选择空！",
	}, {
		ele : "input[name='prpLDlossPersTraceVo.prpLDlossPersInjured.injuryPart']:first",
		datatype : "checkBoxMust",
		nullmsg : "受伤部位至少选择一项！",
	}];

	var ajaxEdit = new AjaxEdit($('#casualtyform'));
	ajaxEdit.targetUrl = "/claimcar/persTraceEdit/saveCasualtyInfo.do";
	ajaxEdit.rules = rule;
	ajaxEdit.beforeCheck = function(data) {
	};
	ajaxEdit.beforeSubmit = function(data) {
		var checkAmountFlag = false;
		$("input[name$='.defloss']").each(function() {
			var defloss = 0.00;// 定损金额
			var realFee = 0.00;// 索赔金额
			if (!isBlank($(this).val())) {
				var name = this.name;
				var traceRecordSize = parseInt(name.substring(name.indexOf("[") + 1, name.indexOf("]")));
				defloss = parseFloat($(this).val());
				realFee = parseFloat($("input[name='prpLDlossPersTraceVo.prpLDlossPersTraceFees[" + traceRecordSize + "].realFee']").val());
				if (defloss > realFee) {
					$(this).addClass("Validform_error").focus();
					checkAmountFlag = true;
					return;
				}
			}
		});
		if (checkAmountFlag) {
			layer.msg("定损金额不能大于索赔金额！");
			return false;
		}
		layer.load(0, {
			shade : [0.8, '#393D49']
		});
	};
	ajaxEdit.afterSuccess = function(data) {
		// 暂存成功后把显示tab页的名称
		parent.addTab(data.datas.bussTaskId, data.datas.personName, data.datas.saveType);
	};

	// 绑定表单
	ajaxEdit.bindForm();

	// 计算公式
	$("#traceRecord_Tbody").on("change", "input.multiplication", function() {
		var name = this.name;
		var traceRecordSize = parseInt(name.substring(name.indexOf("[") + 1, name.indexOf("]")));
		var comCode = $("#comCode").val();
		var $defloss = $("input[name='prpLDlossPersTraceVo.prpLDlossPersTraceFees[" + traceRecordSize + "].defloss']");
		var $amount = $("input[name='prpLDlossPersTraceVo.prpLDlossPersTraceFees[" + traceRecordSize + "].unitAmount']");
		var $quanti = $("input[name='prpLDlossPersTraceVo.prpLDlossPersTraceFees[" + traceRecordSize + "].quantity']");
		var amount = $amount.val();
		var quanti = $quanti.val();

		var unitAmount = 0.00;
		var quantity = 0;
		if (!isBlank(amount)) {
			unitAmount = parseFloat(amount).toFixed(2);
			$amount.val(unitAmount);
		} else {
			$amount.val("");
		}
		if (!isBlank(quanti)) {
			quantity = parseFloat(quanti);
		}
		var feeTypeCode = $("input[name='prpLDlossPersTraceVo.prpLDlossPersTraceFees[" + traceRecordSize + "].feeTypeCode']").val();
		if ((comCode.substring(0, 2) != '22' && feeTypeCode == "8") || (comCode.substring(0, 2) == '22' && feeTypeCode == "6")) {// 残疾赔偿金--X残疾系数
			var $Rate = $("input[name='prpLDlossPersTraceVo.prpLDlossPersTraceFees[" + traceRecordSize + "].woundRate']");
			var Rate = $Rate.val();
			var woundRate = 0.00;
			if (!isBlank(Rate)) {
				woundRate = parseFloat(Rate).toFixed(2);
				$Rate.val(woundRate);
			}
			if (isBlank(amount) || isBlank(quanti) || isBlank(Rate)) {
				$defloss.val("");
			} else {
				$defloss.val(unitAmount * quantity * woundRate);
			}

		} else {
			if (isBlank(amount) || isBlank(quanti)) {
				$defloss.val("");
			} else {
				$defloss.val(unitAmount * quantity);
			}
		}
		changeDefLoss();
	});

	// 减损金额
	$("#traceRecord_Tbody").on("change", "input.detractionfee", function() {
		var name = this.name;
		var traceRecordSize = parseInt(name.substring(name.indexOf("[") + 1, name.indexOf("]")));
		var realFeeVal = $("input[name='prpLDlossPersTraceVo.prpLDlossPersTraceFees[" + traceRecordSize + "].realFee']").val();
		var deflossVal = $("input[name='prpLDlossPersTraceVo.prpLDlossPersTraceFees[" + traceRecordSize + "].defloss']").val();

		var realFee = 0.00;
		var defloss = 0.00;
		if (!isBlank(realFeeVal)) {
			realFee = parseFloat(realFeeVal);
		}
		if (!isBlank(deflossVal)) {
			defloss = parseFloat(deflossVal);
		}

		var detractionfee = realFee - defloss;// 索赔金额-定损金额
		if (isBlank(realFeeVal) || isBlank(deflossVal)) {
			$("input[name='prpLDlossPersTraceVo.prpLDlossPersTraceFees[" + traceRecordSize + "].detractionfee']").val("");
		} else {
			$("input[name='prpLDlossPersTraceVo.prpLDlossPersTraceFees[" + traceRecordSize + "].detractionfee']").val(detractionfee.toFixed(2));
		}
		changeDetractionfee();// 减损金额总计
	});
});

// 页面初始化
function initCasualtyInfoPage() {
	// 初始化是否跟踪完毕
	if ($("input[name='prpLDlossPersTraceVo.endFlag']:checked").val() == '1') {
		$("input.detractionfee").removeAttr("ignore");// 索赔金额和定损金额必录
		$("select[name='prpLDlossPersTraceVo.prpLDlossPersInjured.woundCode']").attr("datatype", "selectMust");// 伤情类型
		$("select[name='prpLDlossPersTraceVo.prpLDlossPersInjured.personSex']").attr("datatype", "selectMust");// 性别
//		$("#injuredCertiCode").removeAttr("ignore");
		
	} else {
		$("input[name$='.reportFee']").removeAttr("ignore");// 估损金额必录
		$("input[name$='.realFee']").attr("ignore", "ignore");
		$("input[name$='.defloss']").attr("ignore", "ignore");
		$("input.detractionfee").attr("ignore", "ignore");// 索赔金额和定损金额费
		$("select[name='prpLDlossPersTraceVo.prpLDlossPersInjured.woundCode']").removeAttr("datatype");
		$("select[name='prpLDlossPersTraceVo.prpLDlossPersInjured.personSex']").removeAttr("datatype");
//		$("#injuredCertiCode").attr("ignore","ignore");
		
	}
	$("input[id='chkComName']").attr("ignore","ignore");
	setMustChkComCode();// 鉴定机构和组织结构代码

	// 案件类型
	var treatSituation = $("select[name='prpLDlossPersTraceVo.prpLDlossPersInjured.treatSituation']").val();
	if (treatSituation == "2") {// 案件类型是住院的时候入院时间和医院必录
		$("input[name$='.inHospitalDate']").attr("datatype", "*");
		$("select.areaselect").attr("datatype", "*");
	}

	// 人员属性
	var $lossFeeType = $("select[name='prpLDlossPersTraceVo.prpLDlossPersInjured.lossItemType']");
	var lossFeeType = $lossFeeType.val();
	var hasDriver = $("#hasDriver").val();
	var hasPassenger = $("#hasPassenger").val();
	var injuredCertiType = $("#injuredCertiType").val();
	if (hasDriver == "0") {// 没保本车司机
		$lossFeeType.find("option[value='3']").attr("disabled", "disabled");
	}
	if (hasPassenger == "0") {// 没保本车乘客
		$lossFeeType.find("option[value='2']").attr("disabled", "disabled");
	}
	if (lossFeeType == '1') {// 第三者伤亡人员不能选择标的车
		if ($("#serialNoId").val() == '1')
			$("#serialNoId").val("");
		$("#serialNoId").find("option").each(function() {
			if ($(this).val() == '1') {
				$(this).attr("disabled", "disabled");
			} else {
				$(this).removeAttr("disabled");
			}
		});
	} else if (lossFeeType == '2' || lossFeeType == '3') {// 车上人员默认标的车且不能选择其他
		$("#serialNoId").val("1");
		$("#serialNoId").find("option").each(function() {
			if ($(this).val() == '1') {
				$(this).removeAttr("disabled");
			} else {
				$(this).attr("disabled", "disabled");
			}
		});
	}

	if ($("#woundCode").val() == "02") {// 伤残
		$("#injuredPart_Tbody").find("select[name$='.woundGrade']").attr("datatype", "selectMust");
	} else if ($("#woundCode").val() == "03") {// 死亡
		$("#deathTimeDiv").show();
		$("#deathTime").attr("datatype","*");
	}

	if (injuredCertiType=="1" || injuredCertiType=="3") {
		$("#injuredCertiCode").attr("datatype", "idcard");
	} else {
		$("#injuredCertiCode").attr("datatype", "*");
	}

	// $("#injuredPart_Tbody
	// select[name$='.injuredDiag']").attr("datatype","selectMust");

	$("#caseProcessType").find("option[value='04']").attr("disabled", "disabled");
	// 人伤处理证件类型，组织机构代码、税务登记证、营业执照去掉
	$("#injuredCertiType").find("option").each(function() {
		if ($(this).val() == '72')
			$(this).remove();
		if ($(this).val() == '73')
			$(this).remove();
	});

	$("#hospitalCase_Tbody").on("mouseover", "select", function() {
		var $this = $(this);
		if ($this.data('data-bind-title')) {
			return;
		}

		$this.data('data-bind-title', true).find('option').each(function() {
			var $option = $(this);
			var _text = $.trim($option.text());
			if (_text.length > 0 && !$option.attr('title')) {
				$option.attr('title', _text);
			}
		});
	});

	var $personName = $("#injuredPersonName");
	if ($personName.length > 0) {
		$personName.editableSelect({
			effects : 'slide',
			onSelect : function(list_item) {
				// 'this' is a reference to the instance of EditableSelect
				// object, so you have full access to everything there
				// alert('List item text: '+ list_item.val());
//				alert('List item text: '+ this.val());
				$('#tracePersonName').val(this.val());
				$('#chkPersonID').val(list_item.val());
				if(!isBlank(this.val())){
					this.removeClass("Validform_error").qtip('destroy', true);
				}
			}
		});
		$("#injuredPersonName").val($("#tracePersonName").val()).attr("datatype","*");
	}
}

// 暂存人伤信息
function saveCasualtyInfo() {
//	idcardValidate();
	if (checkUnallowedChars()) {
		layer.alert("伤者姓名中不允许录入半角空格、圆角空格、圆角斜杠、半角斜杠、半角反斜杠、圆角反斜杠、半角#、圆角#、￥、圆角＄、半角$、半角星号*、圆角星号×、半角&、圆角＆、中文冒号：、英文冒号:，请知悉！");
		return false;
	}
	appraisaValidate();
	var flag = false;
	$("select[name$='.injuredDiag']").each(function() {
		if (isBlank($(this).val())) {
			flag = true;
			return;
		}
	});
	if (flag) {
		layer.msg("受伤部位中的伤情诊断不能为空！");
		return;
	}
	if(!checkCertifyNo()){
		return;
	}
	
	//入院时间不能早于出险时间
	//入院时间
	var rgtDateMin = $("#rgtDateMin").val();
	//出险时间
	var damageTime = $("#damageTIme").val();
	
	if(rgtDateMin < damageTime){
		layer.alert(" '入院时间' 不能早于 '出险时间'！");
		return;
	}
	
	
	$("#casualtyform").submit();
}
//当是发起索赔时，证件号码必录
function idcardValidate(){
	if ($("input[name='prpLDlossPersTraceVo.endFlag']:checked").val() == '1') {
		$("#injuredCertiCode").removeAttr("ignore");
	}else{
		$("#injuredCertiCode").attr("ignore","ignore");
	}
}

// 伤残机构验证
function appraisaValidate(){
	if ($("input[name='prpLDlossPersTraceVo.endFlag']:checked").val() == '1' && $("#woundCode").val() == "02"){
		$("#chkComCode").attr("datatype", "*");
		$("#chkComName_appraisa").attr("datatype","*");
		$("#chkComCode").removeAttr("ignore","ignore");
		$("#chkComName_appraisa").removeAttr("ignore","ignore");
	}else{
		$("#chkComCode").attr("ignore","ignore");
		$("#chkComName_appraisa").attr("ignore","ignore");
	} 
}
// 伤者姓名
function changePersonName(element) {
	var injuredName = $(element).val();
	$("#tracePersonName").val(injuredName);
}
// 证件类型
function changeInjuredCertiType() {
	var certiType = $("#injuredCertiType").val();

	if (certiType=='1' || certiType=='3') {
		$("#injuredCertiCode").attr("datatype", "idcard").removeClass("Validform_error").qtip('destroy', true);
		//$("#injuredCertiCode").attr("ignore","ignore");
	} else {
		$("#injuredCertiCode").attr("datatype", "*").removeClass("Validform_error").qtip('destroy', true);
		//$("#injuredCertiCode").attr("ignore","ignore");
	}
}

// 人员属性
function changeLossItemType(element) {
	var lossFeeType = $(element).val();
	$("input[name='prpLDlossPersTraceVo.lossFeeType']").val(lossFeeType);

	if (lossFeeType == '1') {// 第三者伤亡人员不能选择标的车
		if ($("#serialNoId").val() == '1')
			$("#serialNoId").val("");
		$("#serialNoId").find("option").each(function() {
			if ($(this).val() == '1') {
				$(this).attr("disabled", "disabled");
			} else {
				$(this).removeAttr("disabled");
			}
		});
	} else if (lossFeeType == '2' || lossFeeType == '3') {// 车上人员默认标的车且不能选择其他
		$("#serialNoId").val("1");
		$("#serialNoId").find("option").each(function() {
			if ($(this).val() == '1' || $(this).val() == '') {
				$(this).removeAttr("disabled");
				$("#licenseNo").val($(this).text());
			} else {
				$(this).attr("disabled", "disabled");
			}
		});
	}
}

// 证件号码修改
function changeCertiCode(obj) {
	toUpperValue(obj);
	checkCertifyNo();
	var certiType = $("#injuredCertiType").val();
	if (certiType != '1' && certiType != '3')
		return;

	var certiCode = $("#injuredCertiCode").val().replace(/(\s*$)/g,"");
	$("#injuredCertiCode").val(certiCode);
	var birthDay = getBirthdayByIdCard(certiCode);
	var age = jsGetAge(birthDay);
	var sex = jsGetSex(certiCode);

	$("input[name='prpLDlossPersTraceVo.prpLDlossPersInjured.personAge']").val(age);
	$("select[name='prpLDlossPersTraceVo.prpLDlossPersInjured.personSex']").val(sex);

}

function checkCertifyNo(){
	 var certifyNo = $("#injuredCertiCode").val();
	 var certifyType = $("#injuredCertiType").val();
	 var regs = new RegExp("^(.{9}|.{18})$");
	 //证件类型为组织机构代码时，证件号码只能是8位或者18位
	 if(certifyType == "10"){
		 if(!regs.test(certifyNo)){
			 layer.alert("请录入正确的9位或18位证件号码!"); 
			 return false;
		 }
	 }
	 return true;
}

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
		birthdayValue = birthdayValue + '-' + val.charAt(8) + val.charAt(9) + '-' + val.charAt(10) + val.charAt(11);
	}
	if (18 == val.length) { // 18位身份证号码
		birthdayValue = val.charAt(6) + val.charAt(7) + val.charAt(8) + val.charAt(9) + '-' + val.charAt(10) + val.charAt(11) + '-' + val.charAt(12) + val.charAt(13);
	}
	/*
	 * if(isValidDateTime(birthdayValue) === false){ birthdayValue = ""; }
	 */
	return birthdayValue;
}

/**
 * 计算周岁。
 * 
 * @param strBirthday
 * @returns {*}
 */
function jsGetAge(strBirthday) {
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
		returnAge = 0;// 同年 则为0岁
	} else {
		var ageDiff = nowYear - birthYear; // 年之差
		if (ageDiff > 0) {
			if (nowMonth == birthMonth) {
				var dayDiff = nowDay - birthDay;// 日之差
				if (dayDiff < 0) {
					returnAge = ageDiff - 1;
				} else {
					returnAge = ageDiff;
				}
			} else {
				var monthDiff = nowMonth - birthMonth;// 月之差
				if (monthDiff < 0) {
					returnAge = ageDiff - 1;
				} else {
					returnAge = ageDiff;
				}
			}
		} else {
			returnAge = -1;// 返回-1 表示出生日期输入错误 晚于今天
		}
	}
	return returnAge;// 返回周岁年龄
}

/**
 * 计算性别
 * 
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

// 计算估损金额总计
function changeReportFee() {
	var sumReportFee = 0.00;
	var flag = false;
	$("input[name$='.reportFee']").each(function() {
		var reportFee = 0.00;
		if (!isBlank($(this).val())) {
			reportFee = parseFloat($(this).val());
			$(this).val(reportFee.toFixed(2));
			flag = true;
		}
		sumReportFee += reportFee;
	});
	if (flag) {
		$("#sumReportFee").val(sumReportFee.toFixed(2));
	} else {
		$("#sumReportFee").val("");
	}
}

// 计算索赔金额总计
function changeRealFee() {
	var sumRealFee = 0.00;
	var flag = false;
	$("input[name$='.realFee']").each(function() {
		var realFee = 0.00;
		if (!isBlank($(this).val())) {
			realFee = parseFloat($(this).val());
			$(this).val(realFee.toFixed(2));
			flag = true;
		}
		sumRealFee += realFee;
	});
	if (flag) {
		$("#sumRealFee").val(sumRealFee.toFixed(2));
	} else {
		$("#sumRealFee").val("");
	}
}

// 计算定损金额总计
function changeDefLoss() {
	var sumdefLoss = 0.00;
	var flag = false;
	$("input[name$='.defloss']").each(function() {
		var defloss = 0.00;
		if (!isBlank($(this).val())) {
			defloss = parseFloat($(this).val());
			$(this).val(defloss.toFixed(2));
			flag = true;
		}
		$(this).removeClass("Validform_error");
		sumdefLoss += defloss;
	});
	if (flag) {
		$("#sumdefLoss").val(sumdefLoss.toFixed(2));
	} else {
		$("#sumdefLoss").val("");
	}
}

// 计算减损金额总计
function changeDetractionfee() {
	var sumDetractionFee = 0.00;
	var flag = false;
	$("input[name$='.detractionfee']").each(function() {
		var detractionFee = 0.00;
		if (!isBlank($(this).val())) {
			detractionFee = parseFloat($(this).val());
			$(this).val(detractionFee.toFixed(2));
			flag = true;
		}
		sumDetractionFee += detractionFee;
	});
	if (flag) {
		$("#sumDetractionFee").val(sumDetractionFee.toFixed(2));
	} else {// 减损金额全为空
		$("#sumDetractionFee").val("");
	}
}
// 案件类型
function changeTreatSituation(element) {
	var treatSituation = $(element).val();
	$("#woundCode").val("");
	if (treatSituation == "2") {// 案件类型是住院的时候入院时间和医院必录
		$("input[name$='.inHospitalDate']").attr("datatype", "*");
		$("select.areaselect").attr("datatype", "*");
		$("select[id^='chkComName']").removeAttr("datatype");
		$("#woundCode").find("option[value='01']").removeAttr("disabled").qtip('destroy', true);
		$("#woundCode").find("option[value='02']").removeAttr("disabled").qtip('destroy', true);
		$("#woundCode").find("option[value='03']").attr("disabled","disabled");
		$("#woundCode").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
	}else if(treatSituation == "3"){//案件类型是死亡则伤情类别只能选死亡
		$("#woundCode").find("option[value='01']").attr("disabled","disabled");
		$("#woundCode").find("option[value='02']").attr("disabled","disabled");
		$("#woundCode").find("option[value='03']").removeAttr("disabled").qtip('destroy', true);
		$("#woundCode").attr("datatype", "*");
	}else {
		$("input[name$='.inHospitalDate']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("select.areaselect").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("#woundCode").find("option[value='01']").removeAttr("disabled").qtip('destroy', true);
		$("#woundCode").find("option[value='02']").removeAttr("disabled").qtip('destroy', true);
		$("#woundCode").find("option[value='03']").attr("disabled","disabled");
		$("#woundCode").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
	}
	$("#woundCode").trigger("change");
}

// 从事行业
function changeTicCode(element) {
	var ticCode = $("input[name='prpLDlossPersTraceVo.prpLDlossPersInjured.ticName']").eq(0);
	var ticName = $(element).find("option:checked").text();
	ticCode.val(ticName.split("-")[1]);
}

// 车牌号
function changeSerialNo(element) {
	var lencenNo = $(element).find("option:checked").text();
	$("input[name='prpLDlossPersTraceVo.prpLDlossPersInjured.licenseNo']").val(lencenNo);
}

// 伤情类别
function changeWoundCode(element) {
	var woundCode = $(element).val();
	if (woundCode == '02') {// 伤残
		$("#deathTime").val("").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("#deathTimeDiv").hide();
		$("#injuredPart_Tbody").find("select[name$='.woundGrade']").attr("datatype", "selectMust");
	} else if (woundCode == '03') {// 死亡
		$("#deathTime").attr("datatype","*");
		$("#deathTimeDiv").show();
		$("#injuredPart_Tbody").find("select[name$='.woundGrade']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
	} else {
		$("#deathTime").val("").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("#deathTimeDiv").hide();
		$("#injuredPart_Tbody").find("select[name$='.woundGrade']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
	}
	setMustChkComCode();
}

// 是否发起索赔
function changeEndFlag(element) {
	
	if ($(element).val() == '1') {
		$("#woundCode").attr("datatype", "selectMust");// 伤情类型
	//	$("input[name='prpLDlossPersTraceVo.prpLDlossPersInjured.personAge']").removeAttr("ignore");// 伤者年龄
		$("select[name='prpLDlossPersTraceVo.prpLDlossPersInjured.personSex']").attr("datatype", "selectMust");// 性别
		$(".detractionfee").removeAttr("ignore");// 索赔金额和定损金额必录
	//	$("#injuredCertiCode").removeAttr("ignore");//当是否发起索赔选是的时候，身份证号必录
		
		
		// 估损金额跟踪完毕非必录
		$("input[name$='.reportFee']").attr("ignore", "ignore").removeClass("Validform_error").qtip('destroy', true);
	} else {
		$("select[name='prpLDlossPersTraceVo.prpLDlossPersInjured.woundCode']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
	//	$("input[name='prpLDlossPersTraceVo.prpLDlossPersInjured.personAge']").attr("ignore", "ignore").removeClass("Validform_error").qtip('destroy', true);
		$("select[name='prpLDlossPersTraceVo.prpLDlossPersInjured.personSex']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("input[name$='.reportFee']").removeAttr("ignore");// 估损金额必录
		$("input[name$='.realFee']").attr("ignore", "ignore").removeClass("Validform_error").qtip('destroy', true);
		$("input[name$='.defloss']").attr("ignore", "ignore").removeClass("Validform_error").qtip('destroy', true);
		$(".detractionfee").attr("ignore", "ignore").removeClass("Validform_error").qtip('destroy', true);// 索赔金额和定损金额费必录
	//	$("#injuredCertiCode").attr("ignore","ignore");
		
	}
	setMustChkComCode();
}

// 鉴定机构和组织机构代码是否必录
function setMustChkComCode() {
	if ($("input[name='prpLDlossPersTraceVo.endFlag']:checked").val() == '1' && $("#woundCode").val() == "02") {// 跟踪完毕且伤残鉴定机构和组织机构代码必录
		$("#chkComName").attr("datatype", "*");
		$("#chkComCode").attr("datatype", "*");
		$("#chkComName_appraisa").attr("datatype","*");
		$("#chkComName_lv1").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("#chkComName_lv2").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
	} else {// 没有跟踪完毕鉴定机构不必录
		$("#chkComName").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("#chkComCode").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("#chkComName_appraisa").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("#chkComName_lv1").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("#chkComName_lv2").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
	}
}

// 同查勘
function syncChkPerson() {
	var chkPersonId = $("#chkPersonID").val();
	var params = {
		"chkPersonId" : chkPersonId
	};
	$.ajax({
		url : "/claimcar/loadAjaxPage/loadChkPerson.ajax",
		type : "get",
		data : params,
		dataType : "json",
		async : false,
		success : function(result) {
			$("#serialNoId").val(result.lossPartyId).removeClass("Validform_error").qtip('destroy', true);// 带出查勘损失方
			$("#licenseNo").val($("#serialNoId").find("option:selected").text());
			$("#lossItemType").val(result.personProp).removeClass("Validform_error").qtip('destroy', true);// 人员属性
			$("#lossFeeType").val(result.personProp);// 人员属性
			$("#tracePersonName").val(result.personName);// 人员姓名
			$("#personSex").val(result.personSex).removeClass("Validform_error").qtip('destroy', true);// 人员性别
			$("#injuredCertiType").val(result.identifyType).removeClass("Validform_error").qtip('destroy', true);// 证件类型
			$("#injuredCertiCode").val(result.idNo).removeClass("Validform_error").qtip('destroy', true);// 证件号码
			$("#personAge").val(result.personAge).removeClass("Validform_error").qtip('destroy', true);// 年龄
			$("#ticCode").val(result.ticCode).removeClass("Validform_error").qtip('destroy', true);// 从事行业
			$("#ticName").val($("#ticCode").find("option:selected").text());// 从事行业
			$("#woundCode").val(result.personPayType).removeClass("Validform_error").qtip('destroy', true);// 伤亡类型
			if (!isBlank(result.injuredPart)) {
				del_InjuredPart(result.injuredPart);
				$("input[name='prpLDlossPersTraceVo.prpLDlossPersInjured.injuryPart']:checkbox[value='" + result.injuredPart + "']").prop("checked", true);
				$("input[name='prpLDlossPersTraceVo.prpLDlossPersInjured.injuryPart']").removeClass("Validform_error").qtip('destroy', true);// 伤亡类型
				add_InjuredPart(result.injuredPart);
			}
			if (result.personProp == '1') {// 第三者伤亡人员不能选择标的车
				if ($("#serialNoId").val() == '1')
					$("#serialNoId").val("");
				$("#serialNoId").find("option").each(function() {
					if ($(this).val() == '1') {
						$(this).attr("disabled", "disabled");
					} else {
						$(this).removeAttr("disabled");
					}
				});
			} else if (result.personProp == '2' || result.personProp == '3') {// 车上人员默认标的车且不能选择其他
				$("#serialNoId").val("1");
				$("#serialNoId").find("option").each(function() {
					if ($(this).val() == '1' || $(this).val() == '') {
						$(this).removeAttr("disabled");
						$("#licenseNo").val($(this).text());
					} else {
						$(this).attr("disabled", "disabled");
					}
				});
			}
			if (result.identifyType == "1") {
				$("#injuredCertiCode").attr("datatype", "idcard");
				$("#injuredCertiCode").attr("ignore","ignore");
				
			} else {
				$("#injuredCertiCode").attr("datatype", "n");
				$("#injuredCertiCode").attr("ignore","ignore");
			}
		}
	});
}

/**
 * 检查特殊字符 半角空格、圆角空格、圆角斜杠、半角斜杠、半角反斜杠、圆角反斜杠、半角#、圆角#、人民币符号、
 * 圆角＄、半角$、半角星号*、圆角星号×、半角&、圆角＆、中文冒号：、英文冒号:
 * \\表示一个\
 */
function checkUnallowedChars() {
	var reg = new RegExp(/[ 　／/\\＼#＃￥＄$*＊×&＆：:]/);
	var arr = [];
	var curValue = $("#injuredPersonName").val();
	var newValue = '';
	if (curValue != undefined && curValue.length != 0) {
		arr = curValue.split(reg);
		newValue = arr.join('');
		return newValue.length < curValue.length;
	} else {
		curValue = $("input[name='prpLDlossPersTraceVo.prpLDlossPersInjured.personName']").val();
		if (curValue != undefined && curValue.length != 0) {
			arr = curValue.split(reg);
			newValue = arr.join('');
			return newValue.length < curValue.length;
		} else {
			// 伤者姓名为空
			return false;
		}
	}
}
