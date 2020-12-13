$(function() {
	//页面初始化减损金额显示控制
	var reOpenFlagValue= $("#reOpenFlag").val();
	if("0"==reOpenFlagValue){
	var isDeroFlag =$("input[name='prpLDlossPersTraceMainVo.isDeroFlag']:checked").val();
	var isDeroVerifyAmout=$("#isDeroVerifyAmout").val();
	if("1"==isDeroFlag){
		$("#isDero").removeClass("hide");
		if(isBlank(isDeroVerifyAmout)){
			$("#isDeroVerifyAmout").val($("#isDeroAmout").val());
		}
	}else{
		$("#isDeroVerifyAmout").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		$("#isDeroAmout").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
	}
	
	
	$("input[name='prpLDlossPersTraceMainVo.isDeroFlag']").click(function(){
		var isDeroFlag1 =$("input[name='prpLDlossPersTraceMainVo.isDeroFlag']:checked").val();
		if("1"==isDeroFlag1){
			$("#isDero").removeClass("hide");
			$("#isDeroVerifyAmout").attr("datatype","isDeroVerifyAmout");
			$("#isDeroAmout").attr("datatype","isDeroAmout");
		}else{
			$("#isDero").addClass("hide");
			$("#isDeroVerifyAmout").val("");
			$("#isDeroAmout").val("");
			$("#isDeroVerifyAmout").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
			$("#isDeroAmout").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		}
	});
	}
	
	initPersTraceVerfiy();

	if ($("#handlerStatus").val() == '3') {// 处理完成的节点禁用编辑
		$("body :input").attr("disabled", "disabled");
	}

	$("input[name='auditOpinion']").change(function() {
		$("inout[name='prpLClaimTextVo.description']").val($(this).parent().text());
	});

	var rule = [{
		ele : "input[name='prpLClaimTextVo.opinionCode']:first",
		datatype : "checkBoxMust"
	}];

	$.Datatype.d = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;// 验证数字保留2位小数
	$.Datatype.checkBoxMust = function(gets, obj, curform, regxp) {
		var need = 1, numselected = curform.find("input[name='" + obj.attr("name") + "']:checked").length;
		return numselected >= need ? true : "请至少选择" + need + "项！";
	};

	var ajaxEdit = new AjaxEdit($('#PLverifyForm'));
	ajaxEdit.targetUrl = "/claimcar/persTraceVerify/saveOrSubmit.do";
	ajaxEdit.rules = rule;
	ajaxEdit.beforeSubmit = function(data) {
		var checkAmountFlag = false;
		$("input[name$='.veriDefloss']").each(function() {
			var defloss = 0.00;// 定损金额
			var realFee = 0.00;// 索赔金额
			if (!isBlank($(this).val())) {
				var array = $(this).attr("id").split("_");
				var tabPageNo = array[0];
				var traceRecordSize = array[2];
				defloss = parseFloat($(this).val());
				realFee = parseFloat($("#" + tabPageNo + "_veriRealFee_" + traceRecordSize + "").val());
				if (defloss > realFee) {
					$(this).addClass("Validform_error");
					$(this).focus();
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
		if (data.data == "save") {
			layer.msg("暂存成功");
		} else {
			$("body :input").attr("disabled", "disbaled");
			$("#submitDiv").hide();
			updateOpinionList();
		}
	};

	// 绑定表单
	ajaxEdit.bindForm();

	// 计算公式
	$("body").on("change", ".multiplication", function() {
		var tabPageNo = $(this).attr("id").split("_")[0];
		var traceRecordSize = $(this).attr("id").split("_")[2];
		var amount = $("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].prpLDlossPersTraceFees[" + traceRecordSize + "].veriUnitAmount']").val();
		var quanti = $("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].prpLDlossPersTraceFees[" + traceRecordSize + "].veriQuantity']").val();

		var veriUnitAmount = 0.00;
		var veriQuantity = 0.00;
		if (!isBlank(amount)) {
			veriUnitAmount = parseFloat(amount);
			$("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].prpLDlossPersTraceFees[" + traceRecordSize + "].veriUnitAmount']").val(veriUnitAmount.toFixed(2));
		} else {
			$("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].prpLDlossPersTraceFees[" + traceRecordSize + "].veriUnitAmount']").val("");
		}
		if (!isBlank(quanti)) {
			veriQuantity = parseFloat(quanti);
		}

		var feeTypeCode = $("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].prpLDlossPersTraceFees[" + traceRecordSize + "].feeTypeCode']").val();
		if (feeTypeCode == "8") {// 残疾赔偿金--X残疾系数
			var $Rate = $("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].prpLDlossPersTraceFees[" + traceRecordSize + "].veriWoundRate']");
			var Rate = $Rate.val();
			var veriWoundRate = 0.00;
			if (!isBlank(Rate)) {
				veriWoundRate = parseFloat(Rate).toFixed(2);
				$Rate.val(veriWoundRate);
			}
			if (isBlank(amount) || isBlank(quanti) || isBlank(Rate)) {
				$("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].prpLDlossPersTraceFees[" + traceRecordSize + "].veriDefloss']").val("");
			} else {
				$("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].prpLDlossPersTraceFees[" + traceRecordSize + "].veriDefloss']").val(veriUnitAmount * veriQuantity * veriWoundRate);
			}
		} else {
			if (isBlank(amount) || isBlank(quanti)) {
				$("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].prpLDlossPersTraceFees[" + traceRecordSize + "].veriDefloss']").val("");
			} else {
				$("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].prpLDlossPersTraceFees[" + traceRecordSize + "].veriDefloss']").val(veriUnitAmount * veriQuantity);
			}
		}
		changeVeriDefLoss(tabPageNo);
	});

	// 减损金额
	$("body").on("change", ".detractionfee", function() {
		var tabPageNo = $(this).attr("id").split("_")[0];
		var traceRecordSize = $(this).attr("id").split("_")[2];
		var realFeeVal = $("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].prpLDlossPersTraceFees[" + traceRecordSize + "].veriRealFee']").val();
		var deflossVal = $("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].prpLDlossPersTraceFees[" + traceRecordSize + "].veriDefloss']").val();

		var veriRealFee = 0.00;
		var veriDefloss = 0.00;
		if (!isBlank(realFeeVal)) {
			veriRealFee = parseFloat(realFeeVal);
		}
		if (!isBlank(deflossVal)) {
			veriDefloss = parseFloat(deflossVal);
		}

		var veriDetractionfee = veriRealFee - veriDefloss;// 索赔金额-定损金额
		if (isBlank(realFeeVal) || isBlank(deflossVal)) {
			$("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].prpLDlossPersTraceFees[" + traceRecordSize + "].veriDetractionFee']").val("");
		} else {
			$("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].prpLDlossPersTraceFees[" + traceRecordSize + "].veriDetractionFee']").val(veriDetractionfee.toFixed(2));
		}
		changeVeriDetractionFee(tabPageNo);// 减损金额总计
	});

	sumAllVariyFee();
});

// 初始化页面
function initPersTraceVerfiy() {
	if ($("#intermediaryFlag").val() == '1') {// 公估定损
		$(".intermediary").show();
		$(".persTracePerson").hide();
	} else {
		$(".intermediary").hide();
		$(".persTracePerson").show();
	}
}

// 注销或激活人员
function ActiveOrCancel(element) {
	var tabPageNo = $(element).attr("id").split("_")[0];
	var validFlag = $("#" + tabPageNo + "_validFlag").val();

	if (validFlag == "1") {
		layer.confirm('您确定要对当前人员的数据做注销吗？', {
			btn : ['是', '否']
		// 按钮
		}, function(index) {
			$("#" + tabPageNo + "_validFlag").val("0");
			var id = $("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].id']").val();
			$.post("/claimcar/persTraceEdit/ActiveOrCancel.do?id=" + id + "&validFlag=0", function(jsonData) {
				if (jsonData.data == "0") {
					layer.alert("注销失败！");
				} else {
					$("#persInfoTab").find("span").eq(tabPageNo).attr("style", "background:gray");
					$("#" + tabPageNo + "_validFlag").val("0");
					$(element).text("激活");
					layer.close(index);
				}
			});
			layer.close(index);
		}, function() {
		});
	} else {
		var id = $("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].id']").val();
		$.post("/claimcar/persTraceEdit/ActiveOrCancel.do?id=" + id + "&validFlag=1", function(jsonData) {
			if (jsonData.data == "0") {
				layer.alert("激活失败！");
			} else {
				var endFlag = $("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].endFlag']:checked").val();
				if (endFlag == "1") {
					$("#persInfoTab").find("span").eq(tabPageNo).attr("style", "background:green");
				} else {
					$("#persInfoTab").find("span").eq(tabPageNo).removeAttr("style");
				}

				$("#" + tabPageNo + "_validFlag").val("1");
				$(element).text("注销");
				layer.close(index);
			}
		});
	}
}

function submitNextNode(element) {
	var currentNode = $("#flowNodeCode").val();
	var auditStatus = $(element).attr("id");// 提交动作
	var isPLBigEnd = $("#isPLBigEnd").val();
	var majorcaseFlag = $("#majorcaseFlag").val();
	
	var maxLevel = $("#maxLevel").val();
	var verifyLevel = $("#verifyLevel").val();
	var currentLevel = currentNode.split("_")[1].substring(2);
	var reOpenFlag=$("#reOpenFlag").val();//是否重开标志0重开前，1-重开后
	var isDeroVerifyAmout="";//审核减损金额
	var isDeroAmout="";//减损金额
	var isDeroFlag=$("input[name='prpLDlossPersTraceMainVo.isDeroFlag']:checked").val();
	if(reOpenFlag=='0'){
		isDeroVerifyAmout=$("#isDeroVerifyAmout").val();
		isDeroAmout=$("#isDeroAmout").val();
		if(auditStatus=="backPLNext"){//人伤费用审核退回人伤后续跟踪，审核减损金额，置为空
			if(isDeroFlag=='1'){
			 $("#isDeroVerifyAmout").val("");
			 $("#isDeroVerifyAmout").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
			}
		}else{
			if(isDeroFlag=='1'){
				$("#isDeroVerifyAmout").attr("datatype","isDeroVerifyAmout");
			}
			
		}
	}
	
	
	if(maxLevel==null || maxLevel=="" || verifyLevel==null || verifyLevel==""){
		layer.msg("提交失败！人伤审核等级为空，请联系管理员。");
		return;
	}
	
	if (auditStatus == 'save') {
		$("#PLverifyForm").submit();
	} else {
		if (currentNode.indexOf("PLCharge") != -1 && auditStatus == "submitCharge" && majorcaseFlag == "1" && isPLBigEnd != "Y") {// 人伤大案审核没有通过不允许费用审核通过
			layer.msg("人伤大案审核未通过，费用审核不允许通过！");
			return;
		}
		if ($("input[name='prpLClaimTextVo.opinionCode']:checked").val() == "1" && auditStatus == "backPLNext") {// 同意定损且退回后续跟踪
			layer.msg("选择同意定损时不能退回后续跟踪！");
			return;
		} else if ($("input[name='prpLClaimTextVo.opinionCode']:checked").val() != "1" && (auditStatus == "submitVerify" || auditStatus == "submitCharge")) {
			layer.msg("审核通过时只能选择同意定损！");
			return;
		}
		if (auditStatus == 'audit' && parseInt(currentLevel) == 12) {// 提交上级
			layer.msg("已经处于最高级别，不能提交上级！");
			return;
		} else if (auditStatus == 'backLower') {// 退回下级
//			if(parseInt(currentLevel) < 9 &&  parseInt(currentLevel) == parseInt(maxLevel)){
//				layer.msg("已经处于最低级别，不能退回下级");
//				return;
//			}
			if (parseInt(verifyLevel) > 8 && parseInt(currentLevel) == parseInt(maxLevel)) {// 审核级别大于分公司最高级最低级是分公司最低级
				layer.msg("已经处于最低级别，不能退回下级");
				return;
			}else if (parseInt(currentLevel) < 9 && parseInt(currentLevel) == parseInt(verifyLevel)) {
				layer.msg("已经处于最低级别，不能退回下级");
				return;
			}
			else{
				loadSubmitPLVerify(element);
			}
		} else if (auditStatus == 'backPLNext' && parseInt(currentLevel) > parseInt(maxLevel)) {// 退回后续跟踪
			layer.msg("总公司级别不能直接退回人伤后续跟踪，请退回下级！");
			return;
		} else if (auditStatus == 'submitVerify' || auditStatus == 'submitCharge') {// 审核通过
			if (parseInt(currentLevel) < parseInt(verifyLevel)) {
				layer.msg("审核权限不够，请提交上级！");
				return;
			}
			if(parseInt(currentLevel) < 9 && $("#isRemark").val() == "1"){
				layer.msg("该案件需要提交至总公司审核，请提交上级！(人伤若有总公司审核通过后，那么下次的审核一定要经过总公司且只能总公司审核通过)");
				return;
			}
			
			//案件存在减损金额，请提交总公司审核！（是否减损为是，并且减损金额和审核减损金额都大于0必须提交到总公司）
			if(isDeroFlag=='1' && parseInt(currentLevel) < 9 && (Number(isDeroVerifyAmout))>0  && (Number(isDeroAmout)) >0){
				layer.msg("案件存在减损金额，请提交总公司审核！");
				return;
			}

			if (!findLawSuit()) {
				return false;
			}
			//比较定损总金额和核损总金额
			var sumdefLoss = parseFloat(0);
			var sumVeriDefloss = parseFloat(0);
			$("input[name$='_sumdefLoss']").each(function(){
				sumdefLoss+=parseFloat($(this).val());
			});
			$("input[name$='.sumVeriDefloss']").each(function(){
				sumVeriDefloss+=parseFloat($(this).val());
			});
			if(sumVeriDefloss>sumdefLoss){
				var index = layer.confirm('审核金额高于跟踪金额，请确认是否提交？', {
					btn : ['是', '否']// 按钮
				}, function() {
					layer.close(index);
					loadSubmitPLVerify(element);
				}, function() {
					layer.close(index);
				});
			}else{
				loadSubmitPLVerify(element);
			}
		}else{
			if (auditStatus == 'audit' && !findLawSuit()) {
				return false;
			}
			loadSubmitPLVerify(element);
		}
	}
}

function loadSubmitPLVerify(element){
	var taskId = $("#flowTaskId").val();
	var registNo = $("#registNo").val();
	var traceMainId = $("#traceMainId").val();
	var currentNode = $("#flowNodeCode").val();
	var currentName = $("#flowNodeName").val();
	var auditStatus = $(element).attr("id");// 提交动作
	
	var params = {
			"traceMainId" : traceMainId,
			"taskId" : taskId,
			"registNo" : registNo,
			"currentNode" : currentNode,
			"currentName" : currentName,
			"auditStatus" : auditStatus
		};
		$.ajax({
			url : "/claimcar/loadAjaxPage/loadSubmitPLVerify.ajax",
			type : "post",
			data : params,
			async : false,
			success : function(result) {
				layer.open({
					title : "人伤审核任务提交",
					type : 1,
					skin : 'layui-layer-rim', // 加上边框
					area : ['60%', '50%'], // 宽高
					async : false,
					content : result,
					yes : function(index, layero) {
						var html = layero.html();
						$("#hideDiv").empty();
						$("#hideDiv").append(html);
						$("#hideDiv").hide();
						layer.close(index);

						$("#PLverifyForm").submit();
					}
				});
			},
			error: function(result){
				var jsondata = eval('(' + result.responseText + ')');
				layer.alert(jsondata.msg);
			}
		});
}

// 计算所有审核费用总和
function sumAllVariyFee() {
	$("input[name='tabPageNo']").each(function() {
		changeVeriReportFee($(this).val());
		changeVeriRealFee($(this).val());
		changeVeriDefLoss($(this).val());
		changeVeriDetractionFee($(this).val());
	});
	calSumVeriChargeFee();
}

// 审核费用金额合计
function calSumVeriChargeFee() {
	var items = $("#chargeTbody input[name$='.veriChargeFee']");
	var sumFee = parseFloat(0.00);
	var flag = false;
	$(items).each(function() {
		var chargeFee = $(this).val();
		if (!isBlank(chargeFee)) {
			flag = true;
			chargeFee = parseFloat(chargeFee);
			$(this).val(chargeFee.toFixed(2));
		}
		sumFee = sumFee + chargeFee;
	});
	if (flag) {
		$("#sumVeriChargeFee").val(sumFee);
	} else {
		$("#sumVeriChargeFee").val("");
	}

}

// 计算审核估损金额总计
function changeVeriReportFee(tabPageNo) {
	// var tabPageNo = $(element).attr("id").split("_")[0];
	var sumVeriReportFee = 0.00;
	var flag = false;
	$("input[id^='" + tabPageNo + "_veriReportFee_']").each(function() {
		var veriReportFee = 0.00;
		if (!isBlank($(this).val())) {
			veriReportFee = parseFloat($(this).val());
			$(this).val(veriReportFee.toFixed(2));
			flag = true;
		}
		sumVeriReportFee += veriReportFee;
	});
	if (flag) {
		$("#" + tabPageNo + "_sumVeriReportFee").val(sumVeriReportFee.toFixed(2));
	} else {
		$("#" + tabPageNo + "_sumVeriReportFee").val("");
	}
}

// 计算审核索赔金额总计
function changeVeriRealFee(tabPageNo) {
	// var tabPageNo = $(element).attr("id").split("_")[0];
	var sumVeriRealFee = 0.00;
	var flag = false;
	$("input[id^='" + tabPageNo + "_veriRealFee_']").each(function() {
		var veriRealFee = 0.00;
		if (!isBlank($(this).val())) {
			veriRealFee = parseFloat($(this).val());
			$(this).val(veriRealFee.toFixed(2));
			flag = true;
		}
		sumVeriRealFee += veriRealFee;
	});
	if (flag) {
		$("#" + tabPageNo + "_sumVeriRealFee").val(sumVeriRealFee.toFixed(2));
	} else {
		$("#" + tabPageNo + "_sumVeriRealFee").val("");
	}
}

// 计算审核定损金额总计
function changeVeriDefLoss(tabPageNo) {
	var sumVeriDefloss = 0.00;
	var flag = false;
	$("input[id^='" + tabPageNo + "_veriDefloss_']").each(function() {
		var veriDefloss = 0.00;
		if (!isBlank($(this).val())) {
			veriDefloss = parseFloat($(this).val());
			$(this).val(veriDefloss.toFixed(2));
			flag = true;
		}
		sumVeriDefloss += veriDefloss;
	});
	if (flag) {
		$("#" + tabPageNo + "_sumVeriDefloss").val(sumVeriDefloss.toFixed(2));
	} else {
		$("#" + tabPageNo + "_sumVeriDefloss").val("");
	}
}

// 计算审核减损金额总计
function changeVeriDetractionFee(tabPageNo) {
	var sumVeriDetractionFee = 0.00;
	var flag = false;
	$("input[id^='" + tabPageNo + "_veriDetractionFee_']").each(function() {
		var veriDetractionFee = 0.00;
		if (!isBlank($(this).val())) {
			veriDetractionFee = parseFloat($(this).val());
			$(this).val(veriDetractionFee.toFixed(2));
			flag = true;
		}
		sumVeriDetractionFee += veriDetractionFee;
	});
	if (flag) {
		$("#" + tabPageNo + "_sumVeriDetractionFee").val(sumVeriDetractionFee.toFixed(2));
	} else {
		$("#" + tabPageNo + "_sumVeriDetractionFee").val("");
	}
}

function changeAuditOpinion() {
	var auditOpinion = $("input[name='prpLClaimTextVo.opinionCode']:checked")[0].nextSibling.nodeValue;
	$("textarea[name='prpLClaimTextVo.remark']").val(auditOpinion);
	initClass($("input[name='prpLClaimTextVo.opinionCode']:checked").val());
}

//优化—人伤费用审核、人伤跟踪审核处理界面，审核意见处选择“同意定损”不能点击【回退下级】，选择“价格异议”、“信息不充分”不能点击【提交上级】。----1513
function initClass(value) {
	if (value == "1") {// 同意定损
		setClass($("#backLower"));
		setOnclinck($("#audit"));
	} else if (value == "2" || value == "3") {//价格异议,信息不充分
		setClass($("#audit"));
		setOnclinck($("#backLower"));
	} else{
		setOnclinck($("#backLower"));
		setOnclinck($("#audit"));
	}
}
function setClass(element){
	$(element).removeClass("btn btn-primary radius").addClass("btn btn-disabled radius").removeAttr("onclick");
}
function setOnclinck(element){
	$(element).removeClass("btn btn-disabled radius").addClass("btn btn-primary radius").attr("onclick","submitNextNode(this)");
}

/**
 * 新需求，在人伤后续跟踪审核或者人伤费用审核时，若定损金额修改，对应审核的金额都要逐一修改，可设定一个“同跟踪”按键，自动刷新最新跟踪金额。
 */
function syncVerifyFee(tabPageNo) {//ReportFee
	setVeriFee(tabPageNo, "ReportFee");
	setVeriFee(tabPageNo, "RealFee");
	setVeriFee(tabPageNo, "Defloss");
	setVeriFee(tabPageNo, "DetractionFee");
	setVeriFee(tabPageNo, "UnitAmount");
	setVeriFee(tabPageNo, "Quantity");
	
	//更新 - 计算所有审核费用总和
	sumAllVariyFee();
}

function setVeriFee(tabPageNo,obj){
	$("input[id^='" + tabPageNo + "_"+obj+"_']").each(function() {
		$("#"+$(this).attr("id").replace(obj,"veri"+obj)+"").val($(this).val());
	});
}

/**	
 * 更新意见列表
 */
function updateOpinionList() {
	var persTraceMainId = $("#traceMainId").val();
	var registNo = $("#registNo").val();
	var params = {
		"persTraceMainId" : persTraceMainId,
		"registNo" : registNo
	};
	$.ajax({
		url : "/claimcar/loadAjaxPage/updateOpinionList.ajax",
		type : "post",
		data : params,
		success : function(htmlData) {
			$("#opinionListTbody").empty();
			$("#opinionListTbody").append(htmlData);
		}
	});
}

function findLawSuit() {
	var caseProcessType = $("input[name = 'prpLDlossPersTraceMainVo.caseProcessType']").val();
	if ((caseProcessType == '04' || caseProcessType == '13') && !checkLawSuit ()) {
		layer.msg("请点击上方诉讼按钮，并录入诉讼信息");
		return false;
	}
	return true;
}