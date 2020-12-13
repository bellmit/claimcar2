$(function() {
	//页面初始化减损金额显示控制
	var reOpenFlagValue= $("#reOpenFlag").val();
	if("0"==reOpenFlagValue){
	var isDeroFlag =$("input[name='prpLDlossPersTraceMainVo.isDeroFlag']:checked").val();
	if("1"==isDeroFlag){
		$("#isDero").removeClass("hide");
		$("#isSideDero").removeClass("hide");
	}else{
		$("#isDeroAmout").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		$("#isDeroAmout").val("");
		$("input[name='prpLDlossPersTraceMainVo.inSideDeroFlag']").each(function(){
			if($(this).val()=='0'){
				$(this).prop("checked",true);
			}else{
				$(this).prop("checked",false);
			}
			
			
		});
		
		
	}
	
	}
    
	initPersTraceEdit();

	var rule = [{
		ele : "select[name='prpLCheckDutyVo.ciDutyFlag']",
		datatype : "selectMust",
		nullmsg : "交强责任类型不能选择空！",
	}, {
		ele : "select[name='prpLCheckDutyVo.indemnityDuty']",
		datatype : "selectMust",
		nullmsg : "事故责任比例不能选择空！",
	}, {
		ele : "select[name='prpLDlossPersTraceMainVo.caseProcessType']",
		datatype : "selectMust",
		nullmsg : "案件处理类型不能选择空！",
	}, {
		ele : "#intermediaryFlag",
		datatype : "selectMust",
		nullmsg : "定损类别不能选择空！",
	}, {
		ele : "#intermediaryInfoId",
		datatype : "selectMust",
		nullmsg : "定损类别不能选择空！",
	}, {
		ele : "select[name$='.kindCode']",
		datatype : "selectMust",
		nullmsg : "险别不能选择空！",
	}, {
		ele : "input[name='prpLDlossPersTraceMainVo.majorcaseFlag']:first",
		datatype : "checkBoxMust",
		nullmsg : "请选择是否重大赔案上报!",
	}];

	var ajaxEdit = new AjaxEdit($('#persTraceform'));
	ajaxEdit.targetUrl = "/claimcar/persTraceEdit/saveOrSubmit.do";
	ajaxEdit.rules = rule;
	ajaxEdit.beforeSubmit = function(data) {
		layer.load(0, {
			shade : [0.8, '#393D49']
		});
	};
	ajaxEdit.afterSuccess = function(data) {
		// 暂存成功后把显示tab页的名称
		if (data.data.flag == "0") {// save
			$("input[name='prpLClaimTextVo.id']").val(data.data.id);
		} else if (data.data.flag == "1") {// submit
			$("body :input").attr("disabled", "disbaled");
			$("#submitDiv").hide();
			updateOpinionList();// 更新意见列表
		}
		updateFeePayList();// 更新费用信息列表
	};
	// 绑定表单
	ajaxEdit.bindForm();

	// 是否重大赔案
	$("input[name='prpLDlossPersTraceMainVo.majorcaseFlag']").on("change", function() {
		if ($(this).val() == 1) {// 显示重大赔案意见
			$("#MajorcaseOption").show();
			$("textarea[name='prpLDlossPersTraceMainVo.majorcaseOption']").attr("datatype","*");
		} else {
			$("#MajorcaseOption").hide();
			$("textarea[name='prpLDlossPersTraceMainVo.majorcaseOption']").val("").removeAttr("datatype");
		}
	});

	$.Datatype.rate = /^(100|100.0|100.00|[1-9]?\d(\.\d\d?)?)$/;// 验证百分比
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

});

//人伤跟踪减损金额的控制显示
$("[name='prpLDlossPersTraceMainVo.isDeroFlag']").click(function(){
	var isDeroFlag=$("input[name='prpLDlossPersTraceMainVo.isDeroFlag']:checked").val();
	if("1"==isDeroFlag){
		$("#isDero").removeClass("hide");
		$("#isSideDero").removeClass("hide");
		$("#isDeroAmout").attr("datatype","isDeroAmout");
		
	}else{  
		$("#isDero").addClass("hide");
		$("#isSideDero").addClass("hide");
		$("#isDeroAmout").val("");
		$("#isDeroAmout").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		$("input[name='prpLDlossPersTraceMainVo.inSideDeroFlag']").each(function(){
			if($(this).val()=='0'){
				$(this).prop("checked",true);
			}else{
				$(this).prop("checked",false);
			}
			
			
		});
	}
});


// 初始化页面
function initPersTraceEdit() {

	if ($("#registTaskFlag").val() == "7") {
		layer.alert("该案件已注销，不能继续操作！");
		$(":input").attr("disabled", "disabled");
	}

	if ($("#handlerStatus").val() == '3' || $("#handlerStatus").val() == '9') {// 处理完成的节点禁用编辑
		$("body :input").attr("disabled", "disabled");
		$("#cancelPerson").attr("disabled", "disabled");
		$("#cancelPerson").removeClass("btn-primary");
		$("#cancelPerson").addClass("btn-disabled");
	}

	if ($("#flowNodeCode").val() == "PLFirst") {// 首次跟踪
		if ($("#tempRegistFlag").val() == "1") {// 临时报案
			layer.alert("该案件为无保单报案，转为正式保单报案后才能进行人伤处理!");
			$(":input").attr("disabled", "disabled");
		} else {
			acceptPersTraceTask();
		}
		createRegistRisk();
	}

	// 案件处理类型
	if ($("#flowNodeCode").val() == "PLFirst") {// 首次人伤跟踪
		if ($("#caseProcessType").val() == '04') {// 其他环节选择诉讼
			$("#caseProcessType").attr("disabled", "disabled");
		} else {
			$("#caseProcessType").find("option[value='04']").attr("disabled", "disabled");
		}
	} else {
		$("#caseProcessType").find("option[value='10']").attr("disabled", "disabled");
	}
	// 伤情类别
	$("select[name$='prpLDlossPersInjured.woundCode']").each(function() {
		var woundCode = $(this).val();
		var tabPageNo = this.id.split("_")[0];
		if (woundCode == "03") {// 选中的是亡
			$(".deathTime_" + tabPageNo).show();
		} else {
			$(".deathTime_" + tabPageNo).hide();
		}
	});

	if ($("#flowNodeCode").val() == "PLNext") {// 后续跟踪公估姓名，身份证和电话号码只读
		$("input[name='prpLDlossPersTraceMainVo.plfName']").attr("readonly", "readonly").attr("style", "border: none");
		if(!isBlank($("input[name='prpLDlossPersTraceMainVo.plfCertiCode']").val())){
			$("input[name='prpLDlossPersTraceMainVo.plfCertiCode']").attr("readonly", "readonly").attr("style", "border: none");
		}
		$("input[name='prpLDlossPersTraceMainVo.plfPhone']").attr("style", "border: none").attr("style", "border: none");
	}
}

// 人伤编辑界面回调
function addTab(persTraceId, personName, saveType) {
	var topTab = $('#persInfoTab');
	var tabBar = topTab.find('.tabBar');
	var persTracesNum = parseInt($("#persTracesNum").val(), 10);
	var tabPageNo = 0;
	if (saveType == "add") {
		tabPageNo = persTracesNum;
	} else {
		tabPageNo = tabPageIndex;
	}
	var flowNodeCode = $("#flowNodeCode").val();

	var registNo = $("input[name='prpLDlossPersTraceMainVo.registNo']:first").val();
	var params = {
		"persTraceId" : persTraceId,
		"tabPageNo" : tabPageNo,
		"registNo" : registNo,
		"flowNodeCode" : flowNodeCode
	};
	$.ajax({
		url : "/claimcar/loadAjaxPage/loadCasualtyTabCon.ajax",
		type : "post",
		data : params,
		async : false,
		success : function(result) {
			if (saveType == "add") {
				if (tabPageNo == 0) {
					topTab.find("h1").remove();
				}
				var tabCon = "<div class=\"tabCon\">"+result+"</div>";
				$("#persInfoTab").append(tabCon);
				$("#persTracesNum").val(persTracesNum + 1);// 伤亡人员数量+1

				var endFlag = $("#persInfoTab").find("input[name$='.endFlag']").eq(persTracesNum).val();
				if (endFlag == '1') {
					tabBar.append("<span style='background: #1A6FB8;'>" + personName + "</span>");
				} else {
					tabBar.append("<span>" + personName + "</span>");
				}

				layer.closeAll('iframe');
				$.Huitab("#persInfoTab .tabBar span", "#persInfoTab .tabCon", "current", "click", persTracesNum);
			} else {
				var tabCon = "<div class=\"tabCon\">"+result+"</div>";
				topTab.find(".tabCon").eq(tabPageIndex).replaceWith(tabCon);
				var endFlag = $("#persInfoTab").find("input[name$='.endFlag']").eq(tabPageIndex).val();
				if (endFlag == '1') {
					topTab.find("span").eq(tabPageIndex).replaceWith("<span style='background: #1A6FB8;'>" + personName + "</span>");
				} else {
					topTab.find("span").eq(tabPageIndex).replaceWith("<span>" + personName + "</span>");
				}

				var endFlag = $("input[name='prpLDlossPersTraceVos[" + tabPageIndex + "].endFlag']:checked").val();
				if (endFlag == "1") {
					$("#persInfoTab").find("span").eq(tabPageNo).attr("style", "background: #1A6FB8;");
				}

				layer.closeAll('iframe');
				$.Huitab("#persInfoTab .tabBar span", "#persInfoTab .tabCon", "current", "click", tabPageIndex);
			}
		}
	});
}

// 新增编辑弹窗
var tabPageIndex = 0;
function openTabWin(tabPageNo, saveType) {
	var traceMainId = $("#traceMainId").val();
	var persTracesNum = parseInt($("#persTracesNum").val(), 10);
	var persTraceId = "";
	var titleText = "";
	var caseProcessType = $("#caseProcessType").val();
	if (caseProcessType == "10") {// 无需赔付
		layer.msg("无需赔付案件不允许增加人员！");
		return;
	}

	if (saveType == "add") {
		tabPageNo = persTracesNum;
		titleText = "新增伤亡人员信息";
	} else {
		tabPageIndex = tabPageNo;
		persTraceId = $("input[name='prpLDlossPersTraceVos[" + tabPageNo + "].id']").val();
		titleText = "编辑伤亡人员信息";
	}
	var reconcileFlag = $("#reconcileFlag").val();
	var checkDate = $("#checkDate").val();
	var registNo = $("input[name='prpLDlossPersTraceMainVo.registNo']:first").val();

	var params = "?traceMainId=" + traceMainId + "&registNo=" + registNo + "&tabPageNo=" + tabPageNo + "&persTraceId=" + persTraceId + "&reconcileFlag=" + reconcileFlag + "&checkDate=" + checkDate;
	var url = "/claimcar/loadAjaxPage/loadTabWin.ajax";
	var index = layer.open({
		type : 2,
		title : titleText,
		area : [ '90%', '95%' ],
		fix : true, // 固定
		maxmin : true, // 开启最大化最小化按钮
		content : url + params
	});
//	layer.full(index);
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
					$("#" + tabPageNo + "_editCasualtyBtn").hide();
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
					$("#persInfoTab").find("span").eq(tabPageNo).attr("style", "background: #1A6FB8;");
				} else {
					$("#persInfoTab").find("span").eq(tabPageNo).removeAttr("style");
				}

				$("#" + tabPageNo + "_validFlag").val("1");
				$("#" + tabPageNo + "_editCasualtyBtn").show();
				$(element).text("注销");
			}
		});
	}
}

// 接收任务
function acceptPersTraceTask() {
	var handlerStatus = $("#handlerStatus").val();
	if (handlerStatus == '0') {// 未接收
		if(!isEmpty($("#oldClaim"))){
			layer.confirm('该案件为旧理赔迁移案件，是否继续处理?', {
				btn : [ '确定', '取消' ]
			},function(){
				acceptTask();
			},function(){
				$("body :input").each(function() {
					$(this).attr("disabled", "disabled");
					$("#acceptTask").removeAttr("disabled");
				});
				$("#cancelPerson").attr("disabled", "disabled");
				$("#cancelPerson").removeClass("btn-primary");
				$("#cancelPerson").addClass("btn-disabled");
			});
		}else{
			acceptTask();
		}
	} else {
		$("#acceptTask").attr("disabled", "disabled");
	}
}

function acceptTask(){
	layer.confirm('是否确定接收此任务?', {
		btn : ['确定', '取消']
	// 按钮
	}, function(index) {
		var load = layer.load(0, {shade : [0.8, '#393D49']});
		var params = {
			"flowTaskId" : $("#flowTaskId").val(),
			"registNo" : $("input[name='prpLCheckDutyVo.registNo']").val()
		};
		$.ajax({
			url : "/claimcar/persTraceEdit/acceptPersTraceTask.do",
			type : "post",
			data : params,
			async : false,
			success : function(jsonData) {
				if (jsonData.data == "noAccept") {
					layer.msg("接收任务失败！");
					$("body :input").attr("disabled", "disbaled");
					$("#acceptTask").removeAttr("disabled");
				} else {
					$("body :input").removeAttr("disabled");
					$("input[name^='injuredParts']").attr("disabled", "disbaled");
					$("input[name$='endFlag']").attr("disabled", "disbaled");
					$("#acceptTask").hide();
					$("#traceMainId").val(jsonData.datas.traceMainId);
					layer.close(load);
					window.location.reload();//刷新
				}
				layer.close(index);
			},
			error : function(){
				layer.close(load);
				layer.alert("接收任务失败！");
			}
		});
	}, function() {
		$("body :input").each(function() {
			$(this).attr("disabled", "disabled");
			$("#acceptTask").removeAttr("disabled");
		});
		$("#cancelPerson").attr("disabled", "disabled");
		$("#cancelPerson").removeClass("btn-primary");
		$("#cancelPerson").addClass("btn-disabled");
	});
}


// 提交
function submitNextNode(element) {
	
	var currentNode = $("#flowNodeCode").val();
	var currentName = $("#flowNodeName").val();
	var auditStatus = $(element).attr("id");// 提交动作
	var caseProcessType = $("#caseProcessType").val();// 案件处理类型
	var majorcaseFlag = $("input[name='prpLDlossPersTraceMainVo.majorcaseFlag']:checked").val();// 重大赔案
	var reportType = $("input[name='prpLDlossPersTraceMainVo.reportType']").val();// 报案类型
	var ciDutyFlag = $("select[name='prpLCheckDutyVo.ciDutyFlag']").val();// 交强险责任类型
	var indemnityDuty = $("select[name='prpLCheckDutyVo.indemnityDuty']").val();// 事故责任比例
	var traceMainId = $("#traceMainId").val();
	var flowTaskId = $("#flowTaskId").val();
	var isDeroFlag = $("input[name='prpLDlossPersTraceMainVo.isDeroFlag']:checked").val();
	var sumChargeFee = $("input[name='prpLDlossPersTraceMainVo.sumChargeFee']").val();
    //如果提交人伤跟踪审核，则定损金额必须为空；
	if(auditStatus=='subPLVerify'){
		var i='0';
		$("input[name$='sumdefLoss']").each(function(index,item){
			//_endFlag
			var sumdeflossName=item.name;
			var arry=sumdeflossName.split("_");
			var endFlag=$("#"+arry[0]+"_endFlag").val();//页面每个伤亡人员是否跟踪完毕对应的值
			var sumdefLossValue=item.value;
			if(endFlag=='0'){
				if(sumdefLossValue!='' && sumdefLossValue!=null && sumdefLossValue!="" && sumdefLossValue!="undefined" && sumdefLossValue!="null"){
					i='1';
					return false;
				}
			}
			
			});
		
		if(i=='1'){
			layer.alert("提交人伤跟踪审核,是否跟踪完毕为否时,对应伤亡人员的定损金额必须为空!");
			return false;
		}
		
	}
	
	// 事故责任比例选择无责，交强险责任不能选择有责,事故责任选择...，交强险责任不能选择无责
	if (reportType == '3') {
		if ((ciDutyFlag == '1' && indemnityDuty == '4') || (ciDutyFlag == '0' && indemnityDuty != '4')) {
			layer.msg("交强和商业责任类型不一致，请检查！");
			return false;
		}
	}

	if (auditStatus == 'save') {
		$("#persTraceform").submit();
	} else {
		if (caseProcessType == "10") {// 无需赔付
			if(!checkCaseProcessType(auditStatus)) return false;
		} else if (!checkSubmitNextNode(auditStatus)) {
			return false;
		}

		// if (!checkSubmitNextNode(auditStatus)) {
		// return false;
		// }		

		

		var params = {
			"traceMainId" : traceMainId,
			"currentNode" : currentNode,
			"currentName" : currentName,
			"auditStatus" : auditStatus,
			"majorcaseFlag" : majorcaseFlag,
			"caseProcessType" : caseProcessType,
			"flowTaskId"	:	flowTaskId,
			"isDeroFlag"	:	isDeroFlag,
			"sumChargeFee"	:	sumChargeFee
		};

		$.ajax({
			url : "/claimcar/loadAjaxPage/loadSubmitPersTraceNext.ajax",
			type : "post",
			data : params,
			async : false,
			success : function(htmlData) {
				layer.open({
					title : "人伤跟踪任务提交",
					type : 1,
					skin : 'layui-layer-rim', // 加上边框
					area : ['60%', '60%'], // 宽高
					content : htmlData,
					yes : function(index, layero) {
						var html = layero.html();
						$("#hideDiv").empty();
						$("#hideDiv").append(html);
						$("#hideDiv").hide();
						layer.close(index);

						$("#persTraceform").submit();
					}
				});
			}
		});

	}
}

/**
 * 提交规则校验
 * 
 * @param auditStatus
 * @returns {Boolean}
 */
function checkSubmitNextNode(auditStatus) {
	var isChecked = $("#isChecked").val();
	if (isChecked == "N") {
		layer.msg("查勘未完成，不能提交下一环节！");
		return false;
	}

	var reconcileFlag = $("#reconcileFlag").val();
	var sumCheckLossFee = $("#sumCheckLossFee").val();// 查勘估损总金额
	if (reconcileFlag == "1") {// 是现场调解
		var sumReportFee = 0;

		$("input[name$='sumReportFee']").each(function() {
			sumReportFee += parseFloat($(this).val());
		});

		if ($("#flowNodeCode").val()=='PLFirst'&&sumCheckLossFee != sumReportFee) {
			layer.msg("跟踪人员估损总金额和查勘环节的估损总金额不一致,请检查！");
		}
	}

	if (auditStatus == "subPLNext" || auditStatus == "subPLVerify") {// 提交后续跟踪或跟踪审核
		if (!hasValidPerson(auditStatus)) {// 至少一条有效的人员伤亡信息
			return false;
		}
	} else {// 提交到费用审核
		// 不能存在未跟踪完毕的伤亡人员
		if (!hasEndFlag()) {
			return false;
		}
		
		//案件类型为诉讼判决或诉讼调解，请录入诉讼信息
		if(!lawFlag()){
		   return false;
		}
		
	}

	if (!checkMajorCaseFlag()) {
		return false;
	}

	return true;
}

//提交费用审核时，是否案件类型为诉讼判决或者诉讼调解时，诉讼信息不能为空
function lawFlag(){
	var caseType=$("#caseProcessType").val();
	var registNo=$("#registNo").val();
	var lawSign="1";//诉讼信息标志0-无诉讼信息，1-有诉讼信息
	var params={
		"registNo":registNo
	};
	if(caseType=='04' || caseType=='13'){
		$.ajax({
			url : "/claimcar/persTraceEdit/lawFlag.do",
			type : "post",
			data : params,
			async : false,
			success : function(jsonData) {
				if (jsonData.data =="0") {
					lawSign="0";
				}
				
			}
		});
		
	}
	if(lawSign=='0'){
		layer.msg("案件类型为诉讼判决或诉讼调解，请录入诉讼信息！");
		return false;
	}else{
		return true;
	}
	
}



/**
 * 校验无需赔付规则
 * 
 * @param auditStatus
 * @returns {Boolean}
 */
function checkCaseProcessType(auditStatus) {
	var isChecked = $("#isChecked").val();
	if (isChecked == "N") {
		layer.msg("查勘未完成，不能提交下一环节！");
		return false;
	}

	if (auditStatus == "subPLNext" || auditStatus == "subPLVerify") {// 提交后续跟踪或跟踪审核
		layer.msg("无需赔付案件不能提交到后续跟踪或跟踪审核！");
		return false;
	} else {// 提交到费用审核
		// 不能存在有效的人员伤亡信息
		if (!hasValidPerson(0)) {
			return false;
		}
		
		//案件类型为诉讼判决或诉讼调解，请录入诉讼信息
		if(!lawFlag()){
		   return false;
		}
	}

	var majorcaseFlag = $("input[name='prpLDlossPersTraceMainVo.majorcaseFlag']:checked").val();// 重大赔案
	if(majorcaseFlag=="1"){
		layer.msg("无需赔付案件不能发起大案审核");
		return false;
	}
	
	if(parseFloat($("#sumChargeFee").val())>0){
		layer.msg("无需赔付案件费用赔款费用合计不能大于0!");
		return false;
	}
	
	return true;
}

/**
 * 检查是否符合提交大案规则
 * 新增需求:本案人伤案件进行两次大案上报（重开赔案），但第二次大案上报时未累加第一次大案上报的金额。
 * 后台异步查询该案件下的所有人员合计总估损金额、所有人员合计总定损金额
 * @returns {Boolean}
 */
function checkMajorCaseFlag(){
	var allSumReportFee = 0;// 所有人员合计总估损金额
	var allSumdefLoss = 0;// 所有人员合计总定损金额
	var registNo = $("input[name='prpLDlossPersTraceMainVo.registNo']:first").val();
	var params = {"registNo" : registNo,};
	$.ajax({
		url : "/claimcar/persTraceEdit/getAllSumFee.ajax",
		type : "post",
		data : params,
		async : false,
		success : function(htmlData) {
			allSumReportFee = eval(htmlData).data;
			allSumdefLoss = eval(htmlData).statusText;
		}
	});
	var majorcaseFlag = $("input[name='prpLDlossPersTraceMainVo.majorcaseFlag']:checked").val();// 重大赔案
//	layer.alert(majorcaseFlag == "0" && (Number(allSumReportFee) > 200000 || Number(allSumdefLoss) > 200000));
	if (isBlank(majorcaseFlag)) {
		layer.msg("请选择是否重大赔案上报!");
	} else if (majorcaseFlag == "0" && (Number(allSumReportFee) > 200000 || Number(allSumdefLoss) > 200000)) {
		layer.msg("该案件下的人伤跟踪任务满足重大案件上报条件，请输入重大赔案意见!");
		return false;
	} else {
		return true;
	}
}

/**
 * 检查是否符合提交大案规则
 * 
 * @returns {Boolean}
 */
function checkMajorcaseFlag2() {
	var majorcaseFlag = $("input[name='prpLDlossPersTraceMainVo.majorcaseFlag']:checked").val();// 重大赔案
	// 所有人员合计总估损金额
	var allSumReportFee = 0;
	$("input[name$='sumReportFee']").each(function() {
		var tabPageNo = $(this).attr("name").split("_")[0];
		var validFlag = $("#" + tabPageNo + "_validFlag").val();
		var sumReportFee = $("input[name='" + tabPageNo + "_sumReportFee']").val();
		if (validFlag == '1') {
			allSumReportFee += parseFloat(sumReportFee);
		}
	});

	// 所有人员合计总定损金额
	var allSumdefLoss = 0;
	$("input[name$='sumdefLoss']").each(function() {
		var tabPageNo = $(this).attr("name").split("_")[0];
		var validFlag = $("#" + tabPageNo + "_validFlag").val();
		var sumdefLoss = $("input[name='" + tabPageNo + "_sumdefLoss']").val();
		if (validFlag == '1') {
			allSumdefLoss += parseFloat(sumdefLoss);
		}
	});
	if (isBlank(majorcaseFlag)) {
		layer.msg("请选择是否重大赔案上报!");
	} else if (majorcaseFlag == "0" && (allSumReportFee > 200000 || allSumdefLoss > 200000)) {
		layer.msg("该人伤跟踪任务满足重大案件上报条件，请输入重大赔案意见!");
		return false;
	} else {
		return true;
	}
}

/**
 * 至少有一条有效的伤亡人员信息
 * 
 * @returns {Boolean}
 */
function hasValidPerson(status) {
	var persTracesNum = $("#persInfoTab input[name$='.validFlag']").length;// 人数
	if (persTracesNum > 0) {
		var flag = false;
		$("#persInfoTab input[name$='.validFlag']").each(function() {
			if ($(this).val() == '1') {// 至少一条有效
				flag = true;
				return false;
			}
		});
		if (!flag&&status!=0) {//正常案件
			layer.msg("无有效伤亡人员信息，只能选择提交人伤费用审核环节，提交之前请检查是否有费用赔款信息需要录入。");
			return false;
		} else if(flag&&status==0){//无需赔付案件
			layer.msg("无需赔付案件不能存在有效的伤亡人员信息！");
			return false;
		}else{
			return true;
		}
	} else if(status == 0){//无需赔付伤亡人员数为0
		return true;
	}else {
		layer.msg("请至少录入一条伤亡人员信息！");
		return false;
	}
}

/**
 * 是否存在未完成的人伤跟踪
 * 
 * @returns {Boolean}
 */
function hasEndFlag() {
	var persTracesNum = $("#persInfoTab input[name$='.validFlag']").length;// 人数

	if (persTracesNum > 0) {
		var hasEndFlag = false;
		$("input[name$='.endFlag']").each(function() {
			var index = this.id.split("_")[0];
			var validFlag = $("#" + index + "_validFlag").val();
			if (validFlag == "1" && $(this).val() == '0') {// 有效的人伤任务且没有跟踪完毕
				hasEndFlag = true;
				return false;
			}
		});
		if (hasEndFlag) {
			layer.msg("存在未完成的人伤跟踪，不允许提交费用审核！");
			return false;
		} else {
			return true;
		}
	} else {
		layer.msg("请至少录入一条伤亡人员信息！");
		return false;
	}
}

// 费用赔款信息 合计
function calSumChargeFee(field, checkFlag) {
	var items = $("#chargeTbody input[name$='.chargeFee']");
	// 公估标准和费用金额相等则公估原因不必录，否则必录
	if (checkFlag) {// 是公估类型的
		var index = $(field).parent().parent().index();
		var chargeStandard = $("input[name='lossChargeVos[" + index + "].chargeStandard']").val();// 公估标准
		if ($(field).val() == chargeStandard) {
			$("input[name='lossChargeVos[" + index + "].floatReason']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		} else {
			$("input[name='lossChargeVos[" + index + "].floatReason']").attr("datatype", "*");
		}
	}

	var sumFee = parseFloat(0.00);
	$(items).each(function() {
		var chargeFee = $(this).val();
		if (!isBlank(chargeFee) && !isNaN(chargeFee)) {
			$(this).val(parseFloat(chargeFee).toFixed(2));
		} else {
			chargeFee = parseFloat(0.00);
		}
		sumFee = sumFee + parseFloat(chargeFee);
	});

	$("#sumChargeFee").val(sumFee.toFixed(2));
}

function changeChargeCode(element) {
	var indexTr = $(element).attr("id").split("_")[1];
	var chageName = element.options[element.selectedIndex].text;
	$("input[name='prpLDlossChargeVos[" + indexTr + "].chargeName']").eq(0).val(chageName);
}

function closePop() {
	layer.close(layIndex);
}

// 费用修改历史记录
/*
 * function openFeeEditRecordWin(registNo,traceMainId) { var params =
 * "?traceMainId=" + traceMainId + "&registNo=" + registNo; var url =
 * "/claimcar/loadAjaxPage/loadFeeEditRecord.ajax"; var index =
 * layer.open({ type : 2, title : "费用修改历史纪录", content : url + params });
 * layer.full(index); }
 */

/**
 * 责任比例赋值
 */
function changeDuty() {
	var indemnityDutyRate = $("#indemnityDuty").val();
	if (indemnityDutyRate == null || indemnityDutyRate == "") {
		$("#indemnityDutyRate").val("");
	} else if (indemnityDutyRate == "0") {
		$("#indemnityDutyRate").val("100");
	} else if (indemnityDutyRate == "1") {
		$("#indemnityDutyRate").val("70");
	} else if (indemnityDutyRate == "2") {
		$("#indemnityDutyRate").val("50");
	} else if (indemnityDutyRate == "3") {
		$("#indemnityDutyRate").val("30");
	} else if (indemnityDutyRate == "4") {
		$("#indemnityDutyRate").val("0");
	}
}
/**
 * 责任比例比较
 */
function compareDutyRate(field) {
	var rate = parseFloat($(field).val());
	var indemnityDuty = $("#indemnityDuty").val();
	var mixRate = 0;
	var maxRate = 0;
	var rateName = "";
	if (indemnityDuty == "0") {
		mixRate = 100;
		rateName = "全责";
	} else if (indemnityDuty == "1") {
		mixRate = 70;
		rateName = "主责";
	} else if (indemnityDuty == "2") {
		mixRate = 50;
		rateName = "同责";
	} else if (indemnityDuty == "3") {
		mixRate = 0;
		maxRate = 50;
		rateName = "次责";
		if(maxRate < rate){
			layer.msg("责任比例不能大于" + rateName + "比例" + maxRate + "%");
			changeDuty();
		}
	}
	if (rate < mixRate) {
		layer.msg("责任比例不能小于" + rateName + "比例" + mixRate + "%");
		changeDuty();
	}
}

// 收款人选择界面打開
var payIndex = null;
function showPayCust(element) {
	var registNo = $("input[name='prpLCheckDutyVo.registNo']").val();
	var tdName = $(element).attr("name");
	var url = "/claimcar/payCustom/payCustomList.do?registNo=" + registNo + "&tdName=" + tdName + "";
	if (payIndex == null) {
		payIndex = layer.open({
			type : 2,
			title : '收款人选择',
			shade : false,
			area : ['1000px', '500px'],
			content : url,
			end : function() {
				payIndex = null;
			}
		});
	}
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

/**
 * 更新费用赔款信息列表
 */
function updateFeePayList() {
	var persTraceMainId = $("#traceMainId").val();
	var registNo = $("#registNo").val();
	var params = {
		"persTraceMainId" : persTraceMainId,
		"registNo" : registNo
	};
	$.ajax({
		url : "/claimcar/loadAjaxPage/updateFeePayList.ajax",
		type : "post",
		data : params,
		success : function(htmlData) {
			$("#chargeTbody").empty();
			$("#chargeTbody").append(htmlData);
		}
	});
}

function cancelPerson(registNo,taskId,persTraceMainId) {
	layer.confirm('是否确定注销此任务?', {
		btn : ['确定', '取消']
	// 按钮
	}, function(index) {
		var load = layer.load(0, {shade : [0.8, '#393D49']});
		var params = {
			"persTraceMainId" : persTraceMainId,
			"registNo" : registNo,
			"flowTaskId" : taskId
		};
		$.ajax({
			url : "/claimcar/persTraceEdit/compCancelPerson.do",
			type : "post",
			data : params,
			success : function(resultData) {
				if (resultData.status=="200") {
					layer.confirm('注销成功 !', {
						btn : ['确定', '取消']
					// 按钮
					}, function(index) {
						layer.close(load);
						window.location.reload();//刷新
					}, function() {
						layer.close(load);
						window.location.reload();//刷新
					});
				}else{
					layer.confirm(resultData.statusText, {
						btn : ['确定', '取消']
					// 按钮
					}, function(index) {
						layer.close(load);
						window.location.reload();//刷新
					}, function() {
						layer.close(load);
						window.location.reload();//刷新
					});
				}
			}
		});
	});
}