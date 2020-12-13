/** 框架JS文件，所有系统通用，仅在claimcar-main项目中修改* */

// 查勘单证打印
$(function() {
	// 点击栏目标题
	$(".table_title").click(function() {
		var a = $(this).next();
		if (a.css("display") == "none") {
			$(this).next().show();
			$(this).removeClass('table_close');
		} else {
			$(this).next().hide();
			$(this).addClass('table_close');
		}
	});
	
	//禁止后退键  作用于IE、Chrome
	$(document).keydown(banBackSpace);
	/*//禁止后退键 作用于Firefox、Opera
	$(document).onkeypress(banBackSpace);*/
	
	// select2插件引用-要求：需要使用select2的控件的class属性包含“select2”
	$(".select2").select2({

	});
	
	$('.formtable').find('.select-box').each(function(){
		var selectDiv = $(this).parent('.form_input ');
		selectDiv.addClass('selectDiv');
	})
	$('.formtable').find('.input-text').each(function(){
		var selectDiv = $(this).parent('.form_input ');
		selectDiv.addClass('selectDiv');
	})
	$('.formtable').find('.Wdate').each(function(){
		var selectDiv = $(this).parent('.form_input ');
		selectDiv.addClass('selectDiv');
	})
	$('.formtable').find('.textarea').each(function(){
		var selectDiv = $(this).parent('.form_input ');
		selectDiv.addClass('selectDiv');
	})
});

//处理键盘事件
function banBackSpace(e){
	 var target = e.target ;
     var tag = e.target.tagName.toUpperCase();
     if(e.keyCode == 8){
      if((tag == 'INPUT' && !$(target).attr("readonly"))||(tag == 'TEXTAREA' && !$(target).attr("readonly"))){
       if((target.type.toUpperCase() == "RADIO") || (target.type.toUpperCase() == "CHECKBOX")){
        return false ;
       }else{
        return true ; 
       }
      }else{
       return false ;
      }
     }
}

function reloadRow() {
	$(".select2").select2({

	});
}

function ValidForm(selector) {
	this.selector = selector;
	this.rules = {};
	this.checkSuccess = null;
}

ValidForm.prototype.bindForm = function() {
	var innerForm = $(this.selector);
	var innerFormValidRules = this.rules;
	var innerFormCheckSuccess = this.checkSuccess;

	var validDataOpt = {
		label : ".form_label",
		showAllError : true,// 所以校验一起执行
		ajaxPost : false,
		ignoreHidden : true,
		tiptype : function(msg, o, cssctl) {
			// msg：提示信息;
			// o:{obj:*,type:*,curform:*},
			// obj指向的是当前验证的表单元素（或表单对象），type指示提示的状态，值为1、2、3、4，
			// 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, curform为当前form对象;
			// cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
			if (!o.obj.is("form")) {// 验证表单元素时o.obj为该表单元素，全部验证通过提交表单时o.obj为该表单对象;
				if (o.type == 3) {
					o.obj.attr("title", msg);
					$(o.obj).qtip({ // Grab some elements to apply the tooltip
						// to
						hide : false,
						overwrite : false,
						content : {
							text : msg
						},
						position : {
							my : 'top left', // Position my top left...
							at : 'bottom left', // at the bottom right of...
							target : $(o.obj)
						// my target
						},
						show : {
							event : false,
							ready : true,
							tips : false,
						}
					});
				} else {
					$(o.obj).qtip('destroy');
				}
			} else {// 全部验证通过
				layer.msg('ALL OK');
			}
		},
		beforeCheck : function(curform) {
		},
		beforeSubmit : function(curform) {
		},
		callback : function(curform) {//

			innerFormCheckSuccess();
			return false;
		}
	};
	var fromValid = innerForm.Validform(validDataOpt);
	if (innerFormValidRules != null)
		fromValid.addRule(innerFormValidRules);
};

function bindValidForm(form, checkSuccess) {
	var validFrom = new ValidForm(form);
	validFrom.checkSuccess = checkSuccess;
	validFrom.bindForm();
}

/**
 * 多个元素必填其中一个校验
 * 
 * @param fmElement
 *            form表单jquery对象 $(form)
 * @param name
 *            元素 name 属性 可以为多个
 * @returns {Boolean} 全为空 返回 false， 其中一个不为空 返回 true
 */
function notNullOne(fmElement) {
	var t = arguments.length;
	for (var i = 1; i < t; i++) {
		var val = $(fmElement).find("[name='" + arguments[i] + "']").val();
		if (val != null && val.trim() != '') {
			return true;
		}
	}
	return false;
}

/** liuping 如果文本超过指定长度，进行截取处理 */
function shortTxt(text, length) {
	if (text == null) {
		return null;
	}
	text = text.replace(/&nbsp;/g, " ");
	var showHtml = text;
	if (text.length > length + 1) {
		showHtml = "<span title='" + text + "'>" + text.substring(0, length)
				+ "...</span>";
	}
	return showHtml;
}

/** liuping 将一个对象禁用几秒* */
function disabledSec(obj, sec) {
	$(obj).attr({
		"disabled" : "true"
	});
	setTimeout(function() {
		$(obj).removeAttr("disabled");
	}, sec * 1000);
}

/** liuping判断元素是否为空* */
function isEmpty(field) {
	if (field == "undefined" || field.val() == "" || field.val() == null
			|| field.val() == "null" || field == "" || field == null
			|| field == "null") {
		return true;
	} else {
		return false;
	}
}
/** liuping判断字符串是否为空* */
function isBlank(value) {
	if (value == null || value == "undefined" || value == "" || value == "null") {
		return true;
	} else {
		return false;
	}
}

// 根据下级代码得到全部地区信息
function getAllAreaInfo(lowerCode, areaId, targetElmId, showLevel, clazz,
		disabled) {
	var areaDiv = $("#" + areaId);
	$("#" + targetElmId).val(lowerCode);
	$.ajax({
		url : '/claimcar/areaSelect/getAllArea.do?areaCode=' + lowerCode
				+ '&showLevel=' + showLevel + '&targetElmId=' + targetElmId
				+ '&clazz=' + clazz + '&disabled=' + disabled,
		dataType : "json",// 返回json格式的数据
		type : 'get',
		success : function(json) {// json是后端传过来的值
			areaDiv.empty();
			$.each(json, function(index, item) {
				areaDiv.append(item.html);
			});
		},
		error : function() {
			layer.msg("获取地址数据异常");
		}
	});
}

// 省市区联动查询
function changeArea(obj, targetElmId, level) {
	/*
	 * if(writeCheckArea&&typeof(writeCheckArea)=="function"&&level==3&&areaFlag){
	 * writeCheckArea($(obj).val(),level); }
	 */
	var thisValue = $(obj).val();
	var targetElm = $("#" + targetElmId);
	// 将目标Elm赋值选中的地区
	targetElm.val(thisValue);
	var isHospital = $(obj).attr("isHospital");

	// 查找下级，并为下级赋值
	var childElm = $("#" + targetElmId + "_lv" + (level + 1));

	if (childElm != null && childElm.length > 0 && level < 3) {

		$
				.ajax({
					url : '/claimcar/areaSelect/getChlidArea.do?upperCode='
							+ thisValue,
					dataType : "json",// 返回json格式的数据
					type : 'get',
					success : function(json) {// json是后端传过来的值
						childElm.empty();
						$.each(json, function(index, item) {
							childElm.append("<option value='" + item.areaCode
									+ "'>" + item.areaName + "</option>");
						});
						targetElm.val(childElm.val());
						if (level == 1) {// 切换省得时候，区县也要重新load
							var child3Elm = $("#" + targetElmId + "_lv3");
							if (child3Elm != null) {
								changeArea(childElm, targetElmId, 2);
							}
						}
						if (isHospital && "Y" == isHospital) {
							setHospital(targetElmId, childElm.val());
						}
					},
					error : function() {
						layer.msg("获取地址数据异常");
					}
				});
	}

	if (isHospital == "Y" && level == 2) {
		setHospital(targetElmId, targetElm.val());
	}
}

function setHospital(targetElmId, areaCode) {
	var childElm = $("#" + targetElmId + "_hospital");
	var targetElm = $("#" + targetElmId + "_hospitalCode");
	var targetElmName = $("#" + targetElmId + "_hospitalName");
	$.ajax({
		url : '/claimcar/areaSelect/getChlidHospital.do?areaCode=' + areaCode,
		dataType : "json",// 返回json格式的数据
		type : 'get',
		success : function(json) {// json是后端传过来的值
			childElm.empty();
			$.each(json, function(index, item) {
				childElm.append("<option value='" + item.hospitalCode + "'>"
						+ item.hospitalName + "</option>");
			});
			targetElm.val(childElm.val());
			targetElmName.val(childElm.find("option:first").text());
		},
		error : function() {
			layer.msg("获取地址数据异常");
		}
	});
}

function changeHospital(obj, targetElmId) {
	var targetElm = $("#" + targetElmId + "_hospitalCode");
	var targetElmName = $("#" + targetElmId + "_hospitalName");
	var hospitalCode = $(obj).val();
	var hospitalName = $(obj).find("option:selected").text();
	targetElm.val(hospitalCode);
	targetElmName.val(hospitalName);
}

//保单批改纪录
var reasonIdx;
function viewEndorseInfo(registNo) {
	var url = "/claimcar/policyView/viewEndorseInfo.do?registNo=" + registNo;
	if (reasonIdx != null) {
		layer.close(reasonIdx);
	} else {
		reasonIdx = layer.open({
			type : 2,
			title : false,
			shade : false,
			offset : [ '50px', '0' ],
			area : [ '60%', '40%' ],
			content : [ url, "no" ],
			end : function() {
				reasonIdx = null;
			}
		});
	}
}

// 出险保单
var policy_index;
function viewPolicyInfo(registNo) {
	var url = "/claimcar/policyView/viewPolicyInfo.do?registNo=" + registNo;
	if (policy_index != null) {
		layer.close(policy_index);
	} else {
		policy_index = layer.open({
			type : 2,
			title : false,
			shade : false,
			offset : [ '50px', '0' ],
			area : [ '77%', '55%' ],
			content : [ url, "no" ],
			end : function() {
				policy_index = null;
			}
		});
	}
}

/** 显示风险提示信息页面 */
var riskIndex = null;
function createRegistRisk() {
	var leftVal = $(document.body).width() - 260;
	var flowNodeCode =$("#flowNodeCode").val();//获取界面上隐藏域中的节点
    var policyNos = "";
	$("input[name='checkCode']:checked").each(function() {
		policyNos = policyNos + $(this).val() + ",";
	});
	var registNo = $("#registNo").val();// 获取界面上隐藏域中的报案号
	if (riskIndex == null) {
		riskIndex = layer.open({
			type : 2,
			title : '报案风险提示信息',
			shade : false,
			offset : [ '0', leftVal ],
			area : [ '260px', '300px' ],
			time: 6000, //6秒后自动关闭
			content : [
					// "/claimcar/registCommon/registRiskInfo.do?registNo=4000000201612030000036"
					// +
					"/claimcar/registCommon/registRiskInfo.do?registNo="
							+ registNo + "&policyNos=" + policyNos
							+ "&damageTime=" + $("#damageTime").val(), "no" ] + "&flowNodeCode=" + flowNodeCode,
			end : function() {
				riskIndex = null;
			}
		});
	}

}
/**
 * 查看案件是否注销
 */
function checkCaseCancel(registNo) {
	var reMsg = "N";
	$.ajax({
		url : "/claimcar/sysMsg/checkCaseCancel.do",
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : {
			"registNo" : registNo
		}, // 要传递的数据
		success : function(res) {
			reMsg = res.data;
		}
	});
	return reMsg;

}
/** 显示案件备注信息页面 */
var rmIndex = null;
function createRegistMessage(bussNo, nodeCode, msgId) {

	if (isBlank(bussNo)) {
		layer.msg("未提供报案号无法使用案件备注");
	} else {
		var caseFlag = checkCaseCancel(bussNo);
		if (caseFlag == "N") {
			var url = "/claimcar/sysMsg/initRegistMsg.do?bussNo=" + bussNo
					+ "&nodeCode=" + nodeCode + "&msgId=" + msgId;
			if (rmIndex == null) {
				rmIndex = layer.open({
					type : 2,
					title : '案件备注',
					shade : false,
					offset : [ '0', '30%' ],
					area : [ '800px', '300px' ],
					content : url,
					end : function() {
						rmIndex = null;
					}
				});
			}
		} else {
			layer.msg("案件已被注销无法使用案件备注");
		}

	}

}
/** 调查 */
function createSurvey(registNo, flowId, flowTaskId, nodeCode, handlerUser) {
	var urls = "?registNo=" + registNo + "&flowId=" + flowId + "&nodeCode="
			+ nodeCode + "&handlerUser=" + handlerUser + "&flowTaskId="
			+ flowTaskId;

	index = layer.open({
		type : 2,
		title : "调查处理",
		closeBtn : 0,
		shadeClose : true,
		scrollbar : false,
		skin : 'yourclass',
		area : [ '1100px', '550px' ],
		content : [ "/claimcar/survey/initSurvey.do" + urls ]
	});
}

/** 多保单关联与取消 */
var rela_index = null;
function relationshipList(registNo) {
	/*
	 * var url = "/claimcar/regist/relationshipList.do?registNo=" +
	 * registNo+"&nodeCode="+"Chk";
	 */
	var url = "/claimcar/regist/relationshipList.do?registNo="
			+ registNo + "&flag=check";
	if (rela_index != null) {
		layer.close(rela_index);
	} else {
		rela_index = layer.open({
			type : 2,
			title : '多保单关联与取消',
			shade : false,
			area : [ '100%', '85%' ],
			content : url,
			end : function() {
				rela_index = null;
			}
		});
	}
}

/** 收款人账户信息维护 */
var pIndex = null;
function payCustomOpen(flag, payId) {
	// flag N-普通维护 Y-反洗钱信息补充 S-查勘环节（收款人类型固定并查询带入承保人信息）
	/*$("input[name$='payObjectKind']").each(function(){
		var idx = $(this).attr("name").split("_")[0];
	var payId=*/
	
    var registNo = $("#registNo").val();// 从当前页获取报案号
	var url = "";
	var nodeCode = $("#nodeCode").val();// 从当前页获取节点信息
	if(flag=='N'){
		url = "/claimcar/payCustom/payCustomEdit.do?registNo=" + registNo
		+ "&flag=" + flag + "&nodeCode=" + nodeCode + "" + "&payId="
		+ payId + "";
	}else{
		
		url = "/claimcar/payCustom/btnPayCustomEdit.do?registNo=" + registNo
		+ "&flag=" + flag + "&nodeCode=" + nodeCode + "" + "&payId="
		+ payId + "";
	}
	var title = '收款人账户信息维护';
	if(flag=="Y"){
		title = '反洗钱信息补录';
	}
	if (pIndex == null) {
		pIndex = layer.open({
			type : 2,
			title : title,
			shade : false,
			area : [ '1100px', '500px' ],
			content : url,
			end : function() {
				pIndex = null;
			}
		});
	}

}
/**理算或预付反洗钱信息补录
 * 
 */
var pIndex = null;
function CustomOpen(flag,tBodyId,nameId,index) {
	 //"input[name='padPayPersonVo["+idx+"].otherFlag']";
	
	var payId=$("#"+tBodyId+" input[name='"+nameId+"["+index+"].payeeId']").val();
	
   if(isBlank(payId)){
		 layer.alert("请录入收款人信息! ");
		    return false;
			
			}
	
 
  payCustomOpen(flag,payId);   
	
}
/**垫付页面反洗钱信息补录
 * 
 */
var pIndex = null;
function CustompadpayOpen(flag,nameId,index) {
	 //"input[name='padPayPersonVo["+idx+"].otherFlag']";
	var payId=$("input[name='"+nameId+"["+index+"].payeeId']").val();
	if(isBlank(payId)){
		 layer.alert("请录入收款人信息! ");
		    return false;
			
			}
	 payCustomOpen(flag,payId);   
	
}

/** 收款人信息添加成功后在主页显示添加的信息 */
function showPayResult(payId, nodeCode) {
	if (nodeCode == "DLProp") {
		replacePropPay(payId);
	} else if (nodeCode == "Chk") {
		replaceCheckPayInfo(payId);
	}else if(nodeCode == "Certi"){
		parent.initPayCustom();
	}
}

/** 大案预报功能 */
var big_index;
function viewBigOpinion(registNo, nodeCode) {
	var url = "/claimcar/check/viewBigOpinion.do?registNo=" + registNo
			+ "&nodeCode=" + nodeCode;
	if (big_index != null) {
		layer.close(big_index);
	} else {
		big_index = layer.open({
			type : 2,
			title : false,
			shade : false,
			offset : [ '50px', '15px' ],
			area : [ '90%', '30%' ],
			content : url,
			end : function() {
				big_index = null;
			}
		});
	}

}
// 诉讼按钮
function lawsuit(registNo, nodeCode) {
	var urls = "?registNo=" + registNo + "&nodeCode=" + nodeCode;
	layer.open({
		type : 2,
		title : "诉讼",
		shade : false,
		shadeClose : true,
		scrollbar : false,
		skin : 'yourclass',
		area : [ '1100px', '600px' ],
		content : [ "/claimcar/lawSuit/lawSuitEdit.do" + urls ]
	});
}

// 索赔清单
function certifyList(registNo, taskId ,certifyMakeup) {
	if (isBlank(registNo) || isBlank(taskId)) {
		layer.msg("报案号或者任务ID不能为空！");
	} else {
		var url = "/claimcar/certify/certifyListEdit.do?registNo="
				+ registNo + "&taskId=" + taskId +"&certifyMakeup="+certifyMakeup;
		if (rmIndex == null) {
			rmIndex = layer.open({
				type : 2,
				title : '索赔清单',
				shade : false,
				area : [ '800px', '500px' ],
				content : url,
				end : function() {
					rmIndex = null;
				}
			});
		}
	}
}
// 资料上传
function uploadCertifys(registNo,handlerStatus,subNodeCode) {
	if (isBlank(registNo)) {
		layer.alert("报案号不能为空");
		return;
	}
	var url = "/claimcar/imgManager/uploadCertify.do?registNo=" + registNo
	+"&handlerStatus="+handlerStatus+"&subNodeCode="+subNodeCode;
	window.open(url, "资料上传");
}

// 影像查看
function viewCertifys(bussNo) {
	if (isBlank(bussNo)) {
		layer.alert("业务号不能为空");
		return;
	}
	var url = "/claimcar/imgManager/viewImg.do?bussNo=" + bussNo;
	window.open(url); 
	//openTaskEditWin("影像查看", url);
}

/*// 照片上传
function uploadImages(registNo, id, subNodeCode, handlerStatus) {
	if (handlerStatus == '0' || handlerStatus == '9' || handlerStatus == '1') {// 未暂存任务不能上传图片
		layer.alert("任务未暂存不能上传图片");
		return;
	}
	// imgType 0查勘;1定损车损;2定损物损;3人伤;4复勘;5复检;6车损损余回收;7物损损余回收;
	var imgType = '-1';
	if (subNodeCode == 'Chk') {
		imgType = '0';
	} else if (subNodeCode == "DLCar" || subNodeCode == "DLCarMod"
			|| subNodeCode == "DLCarAdd") {
		imgType = '1';
	} else if (subNodeCode == "DLProp" || subNodeCode == "DLProp"
			|| subNodeCode == "DLPropAdd") {
		imgType = '2';
	} else if (subNodeCode == "PLFirst" || subNodeCode == "PLNext") {
		imgType = '3';
	} else if (subNodeCode == "ChkRe") {
		imgType = '4';
	} else if (subNodeCode == "DLChk") {
		imgType = '5';
	} else if (subNodeCode == "RecLossCar") {
		imgType = '6';
	} else if (subNodeCode == "RecLossProp") {
		imgType = '7';
	}
	if (imgType == '-1') {
		layer.alert("图片上传节点有误:" + subNodeCode);
		return;
	}
	var url = "/claimcar/imgManager/uploadCertify.do?registNo=" + registNo
			+ "&id=" + id + "&imgType=" + imgType +"&handlerStatus="+handlerStatus;
	alert(url);
	window.open(url, "照片上传");
}
*/
/** 异步校验（发起垫付任务）* */
function padPayTaskVlaid(regNo) {
	var url = "/claimcar/padPay/padPayInit.do?registNo=" + regNo;
	var returnFlag = true;
	var params = {"registNo" : regNo};
	$.ajax({
		url : "/claimcar/padPay/padPayTaskVlaid.do", // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(jsonData) {// 回调方法，可单独定义
			var result = eval(jsonData);
			if (result.status == "200" && result.data != "ok") {
				layer.alert(result.data);
				returnFlag = false;
				return false;
			}
		}
	});

	if(returnFlag){
		openTaskEditWin("垫付任务发起", url);
	}else{
		return false;
	}
}

/** 刷新查勘收款人展示信息 */
function replaceCheckPayInfo(payId) {
	var url = "/claimcar/check/loadPayCusInfo.ajax";
	var params = {
		"payCustomId" : payId
	};
	$.ajax({
		url : url,
		data : params,
		type : 'post',
		async : false,
		success : function(result) {// json是后端传过来的值
			parent.$("#payInfos").replaceWith(result); // 替换
			var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
			parent.layer.close(index);// 执行关闭
		},
		error : function() {
			layer.msg("获取地址数据异常");
		}
	});
}

/** 更新财产定损收款人下拉框 */
function replacePropPay(payId) {
	var options = "";
	var url = "/claimcar/proploss/loadPayCusInfo.ajax";
	var params = {
		"payId" : payId
	};
	$.ajax({
		url : url,
		data : params,
		type : 'post',
		async : true,
		success : function(result) {
			if (eval(result).status == "200") {// 成功
				$.each(eval(result).data, function(key, value) {
					options = options + "<option value='" + key + "'>" + value
							+ "</option>";
				});
				var propPayItem = $("#chargeTbody select[name$='receiver']");
				$(propPayItem).each(
						function() {
							var selected = $(this).val();
							$(this).find("option").remove();// 先删除
							$(this).append(options);// 在重新增加
							$(this).find("option[value=" + selected + "]")
									.attr("selected", "selected");
						});
			}
		},
		error : function() {
			layer.msg("获取地址数据异常");
		}
	});
}

// 注销/拒赔
function claimCancel(registNo, taskId) {
	if (isBlank(registNo)) {
		layer.alert("报案号不能为空");
		return;
	}
	var url = "/claimcar/claim/claimCancelApply.do?registNo=" + registNo
			+ "&taskId=" + taskId;
	if (rmIndex == null) {
		rmIndex = layer.open({
			type : 2,
			title : '立案注销/拒赔发起',
			shade : false,
			area : [ '100%', '60%' ],
			content : url,
			end : function() {
				rmIndex = null;
			}
		});
	}

}

/**
 * 流程图查询
 * 
 * @param flowId
 */
function showFlow(flowId) {
	if (isBlank(flowId)) {
		layer.alert("报案号不能为空");
		return;
	}
	var goUrl = "/claimcar/flowQuery/showFlow.do?flowId=" + flowId;
	openTaskEditWin('流程图', goUrl);
}

// 打开移动查勘地图 damage-出险地， check-约定查勘地
function openMap(item, areaCode, address, nodeCode,provinceCode,cityCode) {
	var url = "/claimcar/fixedPosition/openQuickClaimMapBefore.ajax";
	var params = {
		"item" : item,
		"areaCode" : areaCode,
		"address" : address,
		"node" : nodeCode,
		"provinceCode" : provinceCode,
		"cityCode" : cityCode
	};

	$.ajax({
		url : url,
		type : "post",
		data : params,
		async : false,
		success : function(htmlData) {
			var url = htmlData.data;
			// console.log(url);
			layer.open({
				type : 2,
				area : [ '100%', '100%' ],
				fix : true, // 不固定
				maxmin : true,
				content : url
			});
		}
	});
}

/**
 * 
 * @param idName select对象id的名称
 * @param comCode 机构代码
 * @param minimumInputLength 查询关键字最小长度
 * @param gradeId 权限id
 */
function initGetUserCodeSelect2(idName, comCode,minimumInputLength,gradeId) {
	$("#" + idName).select2({
		ajax : {
			url : "/claimcar/select/getUserCode.do",
			dataType : 'json',
			delay : 250,
			data : function(params) {
				return {
					comCode : comCode,
					userInfo : params,
					gradeId : gradeId
				};
			},
			results : function(data) {
				var resultData = eval(data);
				return {
					results : resultData
				};
			},
			cache : true
		},
		minimumInputLength : minimumInputLength
	// 最少输入长度
	});
}


/** 查勘详细信息 **/
function viewCheckInfo(registNo){
//	String params = {"registNo" : registNo};
//	$.ajax({
//		
//	});
//	String url = "/claimcar/check/viewCheckInfo.do?registNo=" + registNo;
//	openTaskEditWin("查勘详细信息",url);
}

function loss(registNo){
	var goUrl ="/claimcar/defloss/findDefloss.do?registNo="+registNo;
	openTaskEditWin("定损详细信息",goUrl);
}

// 注销/拒赔恢复
function claimCancelRecover(registNo, taskId) {
	if (isBlank(registNo)) {
		layer.alert("报案号不能为空");
		return;
	}
	var url = "/claimcar/claimRecover/claimCancelApply.do?registNo="
			+ registNo + "&taskId=" + taskId;
	if (rmIndex == null) {
		rmIndex = layer.open({
			type : 2,
			title : '立案注销/拒赔发起',
			shade : false,
			area : [ '100%', '60%' ],
			content : url,
			end : function() {
				rmIndex = null;
			}
		});
	}

}

// 费用信息
var Index = null;
function chargemessage(registNo) {
	var url = "/claimcar/charge/chargemessage.ajax?registNo=" + registNo;
	if (Index == null) {
		index = layer.open({
			type : 2,
			title : "*费用赔款信息*",
			shadeClose : true,
			scrollbar : false,
			skin : 'yourclass',
			area : [ '80%', '60%' ],
			content : url,
			end : function() {
				Index = null;
			}
		});
	}
}
//人伤单证打印
var Index = null;
function peoplesPrint(registNo) {
	var url = "/claimcar/defloss/peoplePrint.do?registNo=" + registNo;
	if (Index == null) {
		index = layer.open({
			type : 2,
			title : "*人伤单证打印*",
			shadeClose : true,
			scrollbar : false,
			skin : 'yourclass',
			area : [ '60%', '40%' ],
			content : url,
			end : function() {
				Index = null;
			}
		});
	}
}
// 查看估损更新轨迹
var Index = null;
function seeUpdateMessage(registNo) {
	var url = "/claimcar/update/updateMessage.ajax?registNo=" + registNo;
	if (Index == null) {
		index = layer.open({
			type : 2,
			title : "*查看估损更新轨迹信息*",
			shadeClose : true,
			scrollbar : false,
			skin : 'yourclass',
			area : [ '80%', '80%' ],
			content : url,
			end : function() {
				Index = null;
			}
		});
	}
}
// 历次审核意见
var Index = null;
function historyMessage(registNo,compensateNo) {

	var url = "/claimcar/history/historyMessage.ajax?registNo="
			+ registNo + "&compensateNo=" + compensateNo;
	if (Index == null) {
		index = layer.open({
			type : 2,
			title : "历次审核意见信息",
			shadeClose : true,
			scrollbar : false,
			skin : 'yourclass',
			area : ['70%','40%'],
			content : url,
			end : function() {
				Index = null;
			}
		});
	}
}

// 查勘单证打印
function CheckPrint(registNo, ul) {
	var params = "?registNo=" + registNo;
	var url = "/claimcar/certifyPrint/" + ul;
	var index = layer.open({
		type : 2,
		title : "单证打印",
		maxmin : true, // 开启最大化最小化按钮
		content : url + params
	});
	layer.full(index);

}
//预付注销
function prePayCancel(taskId) {
	if (isBlank(taskId)) {
		layer.alert("此案件不能注销！");
		return;
	}else{
		layer.confirm('是否注销此任务?', {
			btn: ['确定','取消'] //按钮
		}, function(index){
			$.ajax({
				url : "/claimcar/prePay/prePayCancel.do?taskId="+taskId, 
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				//data : params, // 要传递的数据
				async:false, 
				success : function(jsonData) {// 回调方法，可单独定义
					if(jsonData.status=="200"){
						
						layer.msg("预付注销成功！");
						$(":input").attr("disabled","disabled");
						$(":input[type='button']").addClass("btn-disabled");
						$(".nodisabled").removeAttr("disabled");
						$(".nodisabled").removeClass("btn-disabled");
						$("#prePayCancel").prop("disabled",true);
					}
				}
			});
		});
	}
}

//定损清单
function SetlossPrint(mainId,registNo){
	var params = "?mainId="+mainId+"&registNo=" + registNo;
	var url = "/claimcar/certifyPrint/lossCarInfo.ajax";
	var index = layer.open({
		type : 2,
		title : "定损清单",
		maxmin : true, // 开启最大化最小化按钮
		content : url + params
	});
	layer.full(index);
}
//核损清单
function verifyLossCertifyPrint(mainId,registNo){
	var params = "?mainId="+mainId+"&registNo=" + registNo;
	var url = "/claimcar/certifyPrint/verifyLossCarInfo.ajax";
	var index = layer.open({
		type : 2,
		title : "核损清单",
		maxmin : true, // 开启最大化最小化按钮
		content : url + params
	});
	layer.full(index);
}

//赔款理算书
function AdjustersPrint(registNo,compensateNo){
	var params = "?registNo=" + registNo+"&compensateNo="+compensateNo;
	var url = "/claimcar/certifyPrint/compensateInfo.ajax";
	var index = layer.open({
		type : 2,
		title : "赔款理算书",
		maxmin : true, // 开启最大化最小化按钮
		content : url + params
	});
	layer.full(index);
}
//历史出险信息
function caseDetails(registNo){
	var nodecode=$("#nodeCode").val();
	var params="";
	if(nodecode=="Regis" && isBlank(registNo)){
	var CIPolicyNo=$("#CIPolicyNo").val();
	var BIPolicyNo=$("#BIPolicyNo").val();
    params="?CIPolicyNo=" + CIPolicyNo+"&BIPolicyNo" + BIPolicyNo;
	}else{
		params = "?registNo=" + registNo;
	}
	var url = "/claimcar/registCommon/claimSummary.ajax";
	layer.open({
     	type: 2,
     	title:"历史出险信息",
     	shadeClose : true,
		scrollbar : false,
     	area: ['95%', '70%'],
        content: url + params
 	});
	return "没写入不能提交";
};
//查看流程图
function seePhoto(flowId){
	if (isBlank(flowId)) {
		layer.alert("报案号不能为空");
		return;
	}
	 var url = "/claimcar/flowQuery/showFlow.do?flowId=" + flowId;
	openTaskEditWin("流程图", url);
}

/**
 * 定损查看
 * @param type 
 * @param id
 */
function deflossView(type,id){
	var goUrl= "";
	var title="";
	if(type=="car"){
		goUrl ="/claimcar/defloss/deflossView.do?lossId="+id;
		title ="车辆定损查看";
	}else if(type=="prop"){
		goUrl ="/claimcar/proploss/deflossView.do?lossId="+id;
		title ="财产定损查看";
	}else if(type=="person"){
		goUrl ="/claimcar/loadAjaxPage/loadPersonInfo.ajax?persTraceId="+id;
		title ="人伤跟踪查看";
	}
	parent.openTaskEditWin(title,goUrl);
}

/**
 * 核损查看
 * @param type 
 * @param id
 */
function lossView(type,id){
	var goUrl= "";
	var title="";
	if(type=="car"){
		goUrl ="/claimcar/defloss/lossView.do?flowTaskId="+id;
		title ="车辆核损查看";
	}else if(type=="prop"){
		goUrl ="/claimcar/proploss/initPropVerifyLoss.do?lossId="+id;
		title ="财产核损查看";
	}else if(type=="person"){
		goUrl ="/claimcar/persTraceVerify/personTraceVerify.do?persTraceId="+id;
		title ="人伤跟踪查看";
	}
	parent.openTaskEditWin(title,goUrl);
}

/**
 * 核损查看
 * @param type 
 * @param id
 */
function vLossView(type,id){
	var goUrl= "";
	var title="";
	if(type=="car"){
		goUrl ="/claimcar/defloss/VerifyLossView.do?lossId="+id;
		title ="车辆核损查看";
	}else if(type=="prop"){
		goUrl ="/claimcar/proploss/propVerifyLossView.do?lossId="+id;
		title ="财产核损查看";
	}else if(type=="person"){
		goUrl ="/claimcar/persTraceVerify/personTraceVerify.do?persTraceId="+id;
		title ="人伤跟踪查看";
	}
	parent.openTaskEditWin(title,goUrl);
}

/** 异步校验（财产定损修改）* */
function dLPropModVlaid(registNo,id,deflossFlag) {
	var loss_url = "/claimcar/propQueryOrLaunch/propModifyLaunchEdit?businessId="+id+"&deflossFlag="+deflossFlag;
	var url = "/claimcar/propQueryOrLaunch/dLPropModVlaid.do";
	var returnflag = true;
	var params = {
		"registNo" : registNo,
		"lossId"   : id
	};
	$.ajax({
		url : url, // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(jsonData) {// 回调方法，可单独定义	
			var result = eval(jsonData);
			if (result.status == "200") {
				if (result.data != "ok") {
					layer.alert(result.data);
					returnflag = false;
				}
			}
		}
	});

	if (!returnflag) {
		return false;
	} else {
		 openTaskEditWin("财产定损修改", loss_url);
	}
}

//费用修改历史纪录
function Findpeople(traceMainId) {
	var params ="?traceMainId=" + traceMainId;
	var url = "/claimcar/loadAjaxPage/loadFeeEditRecord.ajax";
	var index = layer.open({
		type : 2,
		title : "费用修改历史纪录",
		content : url + params
	});
	layer.full(index);
}

//人伤跟踪信息
function Searchpeople(registNo) {
	var params ="?registNo=" + registNo;
	var url = "/claimcar/loadAjaxPage/bigFeeEditRecord.ajax";
	var index = layer.open({
		type : 2,
		title : "人伤跟踪信息",
		content : url + params
	});
	layer.full(index);
}


/**查勘查看信息 **/
function checkSeeMessage(registNo) {
	
	var url = "/claimcar/checkView/checkView.ajax";
	var returnflag = true;
	var taskId ="";
	var params1 ="";
	var params = {
		"registNo" : registNo,
	
	};
	$.ajax({
		
		url : url, // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(result) {// 回调方法，可单独定义	
		
			if (result.status == "200") {
				    taskId=result.data;
					returnflag = false;
					
					
			}
		}
	});
	
	var loss_url = "/claimcar/check/initCheck.do?flowTaskId="+taskId;
	if (returnflag) {
		return false;
	} else {
		
		openTaskEditWin("查勘详细信息", loss_url);
		
	}
}
/** 核损清单**/
function NuclearInventoryLoss(mainId,registNo){
	var params = "?mainId="+mainId+"&registNo=" + registNo;
	var url = "/claimcar/certifyPrint/verifyLossCarInfo.ajax";
	var index = layer.open({
		type : 2,
		title : "核损清单",
		maxmin : true, // 开启最大化最小化按钮
		content : url + params
	});
	layer.full(index);
}

function openOrCloseTitle(e){
	var a = $(e).next();
	if (a.css("display") == "none") {
		$(e).next().show();
		$(e).removeClass('t_close');
	} else {
		$(e).next().hide();
		$(e).addClass('t_close');
	}
}
/*公估费录入*/
function IntermFee(taskId){
	if (isBlank(taskId)) {
		layer.alert("任务号不能为空");
		return;
	}
	 var goUrl = "/claimcar/assessors/assessorsFeeEntry.do?taskId=" + taskId;
		openTaskEditWin('公估费录入', goUrl);
}

/*公估费审核*/
function IntermVeri(taskId){
	if (isBlank(taskId)) {
		layer.alert("任务号不能为空");
		return;
	}
	 var goUrl = "/claimcar/assessors/assessorsFeeAudit.do?taskId=" + taskId;
		openTaskEditWin('公估费审核', goUrl);
}



function claimCancelByOne(registNo, taskId,claimNo) {
	var url = "/claimcar/claim/claimCancelApplyByOne.do?registNo=" + registNo
	+ "&taskId=" + taskId;
	var goUrl ="/claimcar/claim/claimCancelInit.do?claimNo="+claimNo+"&taskId="+taskId;
	
	$.ajax({
		url : url,
		dataType : "json",// 返回json格式的数据
		type : 'get',
		success : function(result) {// json是后端传过来的值
			if(result.statusText=="1"){
				openTaskEditWin("立案注销拒赔处理",goUrl);
			}else{
				layer.alert("该案件不能发起");
			}
		},
		error : function() {
			layer.msg("获取数据异常");
		}
	});
}

//注销/拒赔恢复
function claimCancelRecoverByOne(registNo, taskId,claimNo) {
	var url = "/claimcar/claimRecover/claimCancelApplyByOne.do?registNo=" + registNo + "&taskId=" + taskId;
	$.ajax({
		url : url,
		dataType : "json",// 返回json格式的数据
		type : 'get',
		success : function(result) {// json是后端传过来的值
			if(result.statusText=="1"){
				$.ajax({
					 url : '/claimcar/claimRecover/claimCancelInit.do?claimNo='+claimNo+'&taskId='+taskId,
					 type : 'post', // 数据发送方式
					success : function(res) {//json是后端传过来的值
						reMsg = res.data;
						if(reMsg=="1"){
							layer.alert('发起成功');
						}else{
							layer.alert("该案件不能发起");
						}
						},
					error : function() {
						layer.msg("获取数据异常");
					}
				});
			}else{
				layer.alert("该案件不能发起");
			}
		},
		error : function() {
			layer.msg("获取数据异常");
		}
	});
}
  