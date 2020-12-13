$(function() {
	var thItem = $("#thridCarItem input[type='button']");
	var status = $("#status");
	var nodeCode = $("#nodeCode").val();
	//
	if (nodeCode != "Chk" && status.val() == "2") {
		// 复勘,大案审核
		initChkReBu();
	} else {
		thItem.each(function() {
			$(this).removeAttr("disabled");
		});
	}
	// 复勘
/*	if (nodeCode == "ChkRe") {
		var logOut = $("#logOut");
		logOut.removeClass("btn btn-primary");
		logOut.addClass("btn btn-disabled");
		logOut.attr("disabled", "disabled");
		// $("#logOut").removeClass("btn btn-primary");
		// $("#logOut").addClass("btn btn-disabled");
		$("#relevance").attr("disabled", "disabled");
	}*/

	if (nodeCode != "Chk") {
		thItem.each(function() {
			$(this).removeAttr("disabled");
		});
	}
});

// ------------------------------------------------复勘----大案审核---------------
function chkBigSaves(codeName, saveType) {
	var url_chkS = "/claimcar/check/initChkBigSubmit.do?codeName=";
	var chId = $("#checkId");
	var flTaId = $("#flowTaskId");
	if (!chkReValid(codeName, $("#repairFeesss").val())) {
		return false;
	}
	var params = {
		"checkId" : $("#checkId").val(),
		"flowTaskId" : $("#flowTaskId").val(),
		"codeName" : codeName,
		"contextBig" : $("#verifyCheckContext").val(),
		"deflossRepairType" : $("#deflossRepairType").val(),
		"repairFee" : $("input[name='checkTaskVo.repairFee']").val(),
		"saveType" : saveType,
		"isTimeout" : $("input[name='checkTaskVo.isTimeout']:checked").val()
	};
	$.ajax({
		url : "/claimcar/check/chkBigSave.do",
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		success : function(jsonData) {
			if (eval(jsonData).status == "200") {
				layer.msg("保存成功！");
				if (saveType == "submit") {
					// 提交
					var url = url_chkS + codeName + "&checkId=" + chId.val()
							+ "&flowTaskId=" + flTaId.val();
					layer.open({
						type : 2,
						title : "大案审核提交",
						area : [ '70%', '50%' ],
						fix : true, // 不固定
						maxmin : false,
						content : url,
					});
				} else {
					layer.alert("暂存成功！");
				}
			}
		},
		error : function() {
			layer.alert("操作失败！");
		}
	});
}

function chkReValid(codeName, repairFee) {// 校验
	if (codeName != "Chk" && codeName != "ChkRe") {// 大案审核
		if ($("#verifyCheckContext").val() == "") {
			return false;
		}
		var Regx = /^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/;
		if (!isBlank(repairFee) && !Regx.test(repairFee)) {
			return false;
		}
	}
	return true;
}

// 大案审核提交
function chkBigSubmit(checkId, flowTaskId, codeName) {
	var chkBigUser = $("select[name='chkBigUser']").val();
	var sendMail = $("input[name='sendMail']").val();
	if("ChkRe"==codeName){
		chkBigUser = $("select[name='chkReUser']").val();
	}
	var params = {
		"checkId" : checkId,
		"flowTaskId" : flowTaskId,
		"codeName" : codeName,
		"chkBigUser" : chkBigUser,
		"sendMail" : sendMail
	};
	$.ajax({
		url : "/claimcar/check/chkBigSubmit.do",
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(jsonData) {
			if (eval(jsonData).status == "200") {
				var resultData = eval(jsonData).data;
				layer.confirm(resultData, {
					btn : [ '关闭' ]
				// 按钮
				}, function(index) {
					layer.close(index);
					var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
					window.parent.location.reload();// 刷新
					window.parent.close(index); // 执行关闭
					// parent.layer.close(index);
				});
			}
		},
		error : function() {
			layer.alert("提交失败！");
		}
	});
}

function clickSendEMail(obj){
	 var $radio = $(obj);
	 if ($radio.data('waschecked') == true){
        $radio.prop('checked', false);
        $radio.data('waschecked',false);
        $("input[name$='sendMail']").val(0);
    } else {
        $radio.prop('checked', true);
        $radio.data('waschecked', true);
        $("input[name$='sendMail']").val(1);
    }
}

// 取消大案审核提交，返回
function chkBigCancel(element) {
	var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	// window.parent.location.reload();// 刷新父窗口
	parent.layer.close(index);// 执行关闭
}

function chkReSaveOrSubmit(saveType) {
	var url_chkS = "/claimcar/check/initChkBigSubmit.do?codeName=";
	var node = "ChkRe";
	var chId = $("#checkId").val();
	var taskId = $("#flowTaskId").val();
	var params = {
		"checkId" : $("#checkId").val(),
		"flowTaskId" : $("#flowTaskId").val(),
		"codeName" : "ChkRe",
		"contextBig" : $("#contexts").val(),
		"deflossRepairType" : "",
		"repairFee" : "",
		"saveType" : saveType
	};
	$.ajax({
		url : "/claimcar/check/chkBigSave.do",
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		success : function(jsonData) {
			if (eval(jsonData).status == "200") {
				//layer.msg("保存成功！");
				if (saveType == "submitChkRe") {
					// 提交
					var url = url_chkS + node + "&checkId=" + chId + "&flowTaskId=" + taskId;
					layer.open({
						type : 2,
						title : "复勘提交",
						area : [ '75%', '40%' ],
						fix : true, // 不固定
						maxmin : false,
						content : url,
					});
				}else{
					layer.confirm('暂存成功！', {
						btn: ['确定'] //按钮
					}, function(){
						window.location.reload();
					});
				}
			}
		},
		error : function() {
			layer.alert("操作失败！");
		}
	});
}

//------------------------------------------
function initThirdBus(){
	$("body textarea").eq(1).attr("disabled", "disabled");
	$("body :input").each(function() {
		$(this).attr("disabled", "disabled");
	});
	var saBu=$("#chkReOrChkBig");
	var clBu=$("#colseCheckButton");
	saBu.removeAttr("disabled");
	clBu.removeAttr("disabled");
	
	//隐藏暂存和提交按钮
	var chkSave=$("#chkSave");
	var checkSubmit=$("#checkSubmit");
	chkSave.removeClass("btn btn-primary");
	chkSave.addClass("hide");
	checkSubmit.removeClass("btn btn-primary");
	checkSubmit.addClass("hide");
	
	//复勘
	var crs=$("#chkReSave");
	var crsm=$("#chkReSubmit");
	crs.removeClass("btn btn-primary");
	crs.addClass("hide");
	crsm.removeClass("btn btn-success");
	crsm.addClass("hide");
	$("#printButton").removeClass("btn btn-disabled");
	$("#printButton").addClass("hide");
	
	var cbs=$("#chkBigSave");
	var cbsm=$("#chkBigSubmit");
	cbs.removeClass("btn btn-primary");
	cbs.addClass("hide");
	cbsm.removeClass("btn btn-primary");
	cbsm.addClass("hide");
	
	//多保单关联与取消
	var res=$("#relationship");
	res.removeClass("btn btn-primary");
	res.addClass("btn btn-disabled");
	res.removeAttr("onclick");
}

function initChkReBu(){
	$("body :input").each(function() {
		$(this).attr("disabled", "disabled");
	});
	var defRe=$("#deflossRepairType");
	var verCon=$("#verifyCheckContext");
	var refee=$("input[name='checkTaskVo.repairFee']");
	defRe.removeAttr("disabled");
	refee.removeAttr("disabled");
	verCon.removeAttr("disabled");
	
	//复勘
	$("#contexts").removeAttr("disabled");
	$("#chkReSave").removeAttr("disabled");
	$("#chkReSubmit").removeAttr("disabled");
	$("#chkReOrChkBig").removeAttr("disabled");
	
	var chkBs=$("#chkBigSave");
	var chkBsm=$("#chkBigSubmit");
	chkBs.removeAttr("disabled");
	chkBsm.removeAttr("disabled");
	$("#chkBigUser").removeAttr("disabled");
	$("#chkBigSubmit").removeAttr("disabled");
	$("#chkBigCancel").removeAttr("disabled");
	
	defTyChange();
	
	//公共按钮
	$("#surveyButton").removeAttr("onclick");
	$("#relevance").removeAttr("href");
	
	var resh=$("#relationship");
	resh.removeClass("btn btn-primary");
	resh.addClass("btn btn-disabled");
	resh.removeAttr("onclick");
	
	//
	$("input[name='checkTaskVo.isTimeout']").removeAttr("disabled");
}

function defTyChange(){
	var defTy=$("#deflossRepairType");
	defTy.find("option[value='04']").attr("disabled","disabled");
	defTy.find("option[value='05']").attr("disabled","disabled");
}