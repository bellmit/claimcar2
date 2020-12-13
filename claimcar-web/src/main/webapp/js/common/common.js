/** 框架JS文件，所有系统通用，仅在claimcar-main项目中修改* */
// 查勘单证打印

$(function() {
	//页面车牌号码的验证
	$.Datatype.carLicenseNo =  function(gets,obj,curform,regxp){
		var licenseNo = $(obj).val();
			//var Expression = /^(WJ|[\u0391-\uFFE5]{1})[A-Z0-9]{5,}[\u0391-\uFFE5]{0,1}$/;
			var Expression = /^(WJ[\u0391-\uFFE5]{0,1}|[\u0391-\uFFE5]{1}|[A-Z]{2})领{0,1}[A-Z0-9]{5,}[\u0391-\uFFE5]{0,1}$/;
			var embassyExpression = /^[A-Z0-9]{5,10}使$/; //使馆车匹配
			//var Expression =/^(WJ[\u0391-\uFFE5]{0,1}|[\u0391-\uFFE5]{1})[A-Z0-9]{5,}[\u0391-\uFFE5]{0,1}$/;
			if(licenseNo.length>11 || licenseNo.length < 6){
				return false;
				
			}else{
				if(!Expression.test(licenseNo) && !embassyExpression.test(licenseNo)){
					return false;
				}
			}
			};

			
	//人伤减损金额的验证
	$.Datatype.isDeroAmout =  function(gets,obj,curform,regxp){
		var isDeroAmout=$(obj).val();
		var Expression1=/^[1-9]{1}[0-9]*$/;
		var Expression2=/^[1-9]{1}[0-9]*\.[0-9]{1,2}$/;
		var Expression3=/^0\.[0-9]{0,1}[1-9]{1}$/;
		var Expression4=/^0\.[1-9]{1}[0-9]{0,1}$/;
		if(!Expression1.test(isDeroAmout) && !Expression2.test(isDeroAmout) && !Expression3.test(isDeroAmout)
				&& !Expression4.test(isDeroAmout)){
			return false;
		}
	};
	//审核减损金额校验
	$.Datatype.isDeroVerifyAmout =  function(gets,obj,curform,regxp){
		var isDeroAmout=$(obj).val();
		var Expression1=/^[1-9]{1}[0-9]*$/;
		var Expression2=/^[1-9]{1}[0-9]*\.[0-9]{1,2}$/;
		var Expression3=/^0\.[0-9]{0,1}[1-9]{1}$/;
		var Expression4=/^0\.[0-9]{1}[0-9]{0,1}$|^0{1}$/;//支持调整减损金额为0
		if(!Expression1.test(isDeroAmout) && !Expression2.test(isDeroAmout) && !Expression3.test(isDeroAmout)&&
				!Expression4.test(isDeroAmout)){
			return false;
		}
	};
	
	//理算减损金额的验证
	$.Datatype.isDeroAmoutCom =  function(gets,obj,curform,regxp){
		var isDeroAmout=$(obj).val();
		var Expression1=/^[1-9]{1}[0-9]*$/;
		var Expression2=/^[1-9]{1}[0-9]*\.[0-9]{1,2}$/;
		var Expression3=/^0\.[0-9]{1,2}$/;
		var Expression4=/^0$/;
		if(!Expression1.test(isDeroAmout) && !Expression2.test(isDeroAmout) && !Expression3.test(isDeroAmout) && !Expression4.test(isDeroAmout)){
			return false;
		}
	};
	

	//查询页面车牌号码的验证
	$.Datatype.carStrLicenseNo=function(gets,obj,curform,regxp){
		   var carLicenseNo=$(obj).val();
		  if(carLicenseNo.length>11){
		    		
		     return false;
		    }
	};  
    
	
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
	});
	$('.formtable').find('.input-text').each(function(){
		var selectDiv = $(this).parent('.form_input ');
		selectDiv.addClass('selectDiv');
	});
	$('.formtable').find('.Wdate').each(function(){
		var selectDiv = $(this).parent('.form_input ');
		selectDiv.addClass('selectDiv');
	});
	$('.formtable').find('.textarea').each(function(){
		var selectDiv = $(this).parent('.form_input ');
		selectDiv.addClass('selectDiv');
	});
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
		disabled,handlerStatus) {
	var areaDiv = $("#" + areaId);
	$("#" + targetElmId).val(lowerCode);
	$.ajax({
		url : '/claimcar/areaSelect/getAllArea.do?areaCode=' + lowerCode
				+ '&showLevel=' + showLevel + '&targetElmId=' + targetElmId
				+ '&clazz=' + clazz + '&disabled=' + disabled +'&handlerStatus=' + handlerStatus,
		dataType : "json",// 返回json格式的数据
		type : 'get',
		success : function(json) {// json是后端传过来的值
			areaDiv.empty();
			$.each(json, function(index, item) {
				areaDiv.append(item.html);
			});
			if(document.getElementById("nodeCode")){
				if("Regis"==$("#nodeCode").val()){
					getDamageAddress();
				}
			}
			
		},
		error : function() {
			layer.msg("获取地址数据异常");
		}
	});
}

// 省市区联动查询
function changeArea(obj, targetElmId, level,handlerStatus) {
	/*
	 * if(writeCheckArea&&typeof(writeCheckArea)=="function"&&level==3&&areaFlag){
	 * writeCheckArea($(obj).val(),level); }
	 */
	var thisValue = $(obj).val();
	var targetElm = $("#" + targetElmId);
	// 将目标Elm赋值选中的地区
	var lv2value=$("#damageAreaCode_lv2").val();
	var selectName1=$("#damageAreaCode_lv1").attr("name");
	if(selectName1=='feeStandrad' && isBlank(lv2value)){
		targetElm.val($("#damageAreaCode_lv1").val());
	}else{
		targetElm.val(thisValue);
	}
	
	 
	var isHospital = $(obj).attr("isHospital");
	//伤残鉴定机构
	var isAppraisa =$(obj).attr("isAppraisa");
	//收款人维护页面的维护银行地区
	var isAreaCode =$(obj).attr("isAreaCode");
	var selectName=$(obj).attr("name");
	// 查找下级，并为下级赋值
	var childElm = $("#" + targetElmId + "_lv" + (level + 1));
	var urlhtp="";
	var date=new Date().getTime();
	if('Y'==isAreaCode){
	    urlhtp='/claimcar/areaSelect/getChlidOldArea.do?upperCode=' + thisValue+"&date="+date;
	}else{
		urlhtp='/claimcar/areaSelect/getChlidArea.do?upperCode=' + thisValue +'&handlerStatus=' + handlerStatus+"&date="+date;
	}
	//url='/claimcar/areaSelect/getChlidArea.do?upperCode=';
	if (childElm != null && childElm.length > 0 && level < 3) {
		$.ajax({
					url : urlhtp,
					dataType : "json",// 返回json格式的数据
					type : 'get',
					async:false,
					success : function(json) {// json是后端传过来的值
						childElm.empty();
						$.each(json, function(index, item) {
							if(selectName=='feeStandrad' && index==0){
								childElm.append("<option value=''></option>");
							}
							childElm.append("<option value='" + item.areaCode
									+ "'  title ='"+ item.areaName +"'>" + item.areaName + "</option>");
						});
						targetElm.val(childElm.val());
						if (level == 1) {// 切换省得时候，区县也要重新load
							var child3Elm = $("#" + targetElmId + "_lv3");
							if (child3Elm != null) {
								changeArea(childElm, targetElmId, 2,handlerStatus);
							}
						}
						//医院
						if (isHospital&&"Y"==isHospital) {
							setHospital(targetElmId, childElm.val());
							
						}
						
						//伤残鉴定机构
						if(isAppraisa&&"Y"==isAppraisa){
							setAppraisa(targetElmId, childElm.val());
						
						}
						/*//收款人账户维护银行地区
						if(isAreaCode&&"Y"==isAreaCode){
							setAreaCode(targetElmId, childElm.val());
						}*/
						
					},
					error : function() {
						layer.msg("获取地址数据异常");
					}
				});
	}
   
	if (isHospital == "Y" && level == 2) {
		setHospital(targetElmId, targetElm.val());
	}
	if (isAppraisa == "Y" && level == 2) {
		setAppraisa(targetElmId, targetElm.val());
	}
	/*if (isAreaCode == "Y" && level == 2) {
		setAreaCode(targetElmId, targetElm.val());
	}*/
	//关地图
	if(targetElmId=="switchMapProvinceCityAreaCode"){
		$("input[name='selfCheck']").prop("disabled",false);
		$("input[name='selfCheck']").attr("checked",false);
		 $("#check1").empty();
		 html = "<input class='text-c input-text' type='text'  value='' id='userName'  name='prpLScheduleTaskVo.scheduledUsername'/>"+
		 	"<input value='' id='userCode' name='prpLScheduleTaskVo.scheduledUsercode' type='hidden'/>";
		 $("#check1").append(html);
	 
	 	$("input[name='selfPloss']").prop("disabled",false);
	 	$("input[name='selfPloss']").attr("checked",false);
	 	$("#ploss1").empty();
		var html = "<input class='text-c input-text' type='text'  value='' id='personUserName'  name='prpLScheduleTaskVo.personScheduledUsername'/>"+
	 	"<input value='' id='personUserCode' name='prpLScheduleTaskVo.personScheduledUsercode' type='hidden'/>";
		$("#ploss1").append(html);
		
	}
	
}

//伤残鉴定机构省市区联动查询

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
				childElm.append("<option value='" + item.hospitalCode + "' title = '"+ item.hospitalName +"'>"
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
//伤残鉴定机构
function setAppraisa(targetElmId,areaCode) {
	
	var childElm = $("#" + targetElmId + "_appraisa");
	var targetElm = $("#" + targetElmId + "_appraisaCode");
	var targetElmName = $("#" + targetElmId + "_appraisaName");
	$.ajax({
		url : "/claimcar/areaSelect/getChlidAppraisa.do?areaCode=" + areaCode,
		dataType : "json",// 返回json格式的数据
		type : 'post',
		success : function(json) {// json是后端传过来的值
			childElm.empty();
			$.each(json, function(index, item) {
				childElm.append("<option value='" + item.appraisaCode + "' title = '"+ item.appraisaName +"'>"
						+ item.appraisaName + "</option>");
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
//	$(obj).find("option").each(function(){
//		var a = $(this).text();
//		$(this).attr("title",a);
//	});
	targetElm.val(hospitalCode);
	targetElmName.val(hospitalName);
}

//伤残鉴定机构
function changeAppraisa(obj, targetElmId) {
	 
	var targetElm = $("#" + targetElmId + "_appraisaCode");
	var targetElmName = $("#" + targetElmId + "_appraisaName");
	var appraisaCode = $(obj).val();
	var appraisaName = $(obj).find("option:selected").text();
//	$(obj).find("option").each(function(){
//		var a = $(this).text();
//		$(this).attr("title",a);
//	});
	targetElm.val(appraisaCode);
	targetElmName.val(appraisaName);
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
			content : [ url, "yes" ],
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
			area : [ '85%', '60%' ],
			content : url,
			scrollbar: true,
			end : function() {
				policy_index = null;
			}
		});
	}
}

//德联易控信息
var ce_index;
function cedlinfoshow(registNo) {
	var url = "/claimcar/dlResultInfo/infoShow.do?registNo=" + registNo;
	if (ce_index != null) {
		layer.close(ce_index);
	} else {
		ce_index = layer.open({
			type : 2,
			title : false,
			shade : false,
			area : [ '85%', '60%' ],
			content : url,
			scrollbar: true,
			end : function() {
				ce_index = null;
			}
		});
	}
}
//德联易控详情
var celt_index;
function dlinfoShow(id){
	var url = "/claimcar/dlResultInfo/ceResultInfo.do?id=" + id;
	if (celt_index != null) {
		parent.layer.close(celt_index);
	} else {
		celt_index = parent.layer.open({
			type : 2,
			title : false,
			shade : false,
			area : [ '90%', '85%' ],
			content : url,
			scrollbar: true,
			end : function() {
				celt_index = null;
			}
		});
	}
}

//反欺诈评分信息
var score_index;
function scoreInfo(registNo) {
	var url = "/claimcar/scoreAction/scoreView.do?registNo=" + registNo;
	if (score_index != null) {
		layer.close(score_index);
	} else {
		score_index = layer.open({
			type : 2,
			title : '反欺诈评分信息',
			shade : false,
			area : [ '85%', '80%' ],
			content : url,
			scrollbar: false,
			end : function() {
				score_index = null;
			}
		});
	}
}

//本案件下，公估费费用查看yzy
var assessor_index;
function assessorView(registNo) {
	var url = "/claimcar/assessors/assessorShowView.do?registNo=" + registNo;
	if (assessor_index != null) {
		layer.close(assessor_index);
	} else {
		assessor_index = layer.open({
			type : 2,
			title : '公估费查看',
			shade : false,
			area : [ '80%', '60%' ],
			content : url,
			end : function() {
				assessor_index = null;
			}
		});
	}
}

//核损清单打印跳转
var policy_index;
function verifyLossCertifyPrintJump(registNo) {
	var url = "/claimcar/certifyPrintSearch/verifyLossCarListButton.do?registNo=" + registNo;
	if (policy_index != null) {
		layer.close(policy_index);
	} else {
		policy_index = layer.open({
			type : 2,
			title : false,
			shade : false,
			area : [ '80%', '60%' ],
			content : url,
			end : function() {
				policy_index = null;
			}
		});
	}
}


//赔款计算书打印跳转
var policy_index;
function AdjustersPrintJump(registNo) {
	var url = "/claimcar/certifyPrintSearch/compensateInfoListButton.do?registNo=" + registNo;
	if (policy_index != null) {
		layer.close(policy_index);
	} else {
		policy_index = layer.open({
			type : 2,
			title : false,
			shade : false,
			area : [ '80%', '60%' ],
			content : url,
			end : function() {
				policy_index = null;
			}  
		});
	}
}

//赔款收据打印跳转
var policy_index;
function certifyPrintPayFeeJump(registNo) {
	var url = "/claimcar/certifyPrintSearch/compensateInfoListNoteButton.do?registNo=" + registNo;
	if (policy_index != null) {
		layer.close(policy_index);
	} else {
		policy_index = layer.open({
			type : 2,
			title : false,
			shade : false,
			area : [ '90%', '60%' ],
			content : url,
			end : function() {
				policy_index = null;
			}  
		});
	}
}

/** 显示风险提示信息页面 */
var riskIndex = null;
function createRegistRisk() {
	var registNo = $("#registNo").val();// 获取界面上隐藏域中的报案号

/*	if(nodecode =="Regis" && isBlank(registNo)){
		  var CIPolicyNo = $("#CIPolicyNo").val();
		  var BIPolicyNo = $("#BIPolicyNo").val();
	      params="?CIPolicyNo=" + CIPolicyNo + "&BIPolicyNo=" + BIPolicyNo;
		}else{
			params = "?registNo=" + registNo;
		}*/
	var leftVal = $(document.body).width() - 260;
	var flowNodeCode =$("#flowNodeCode").val();//获取界面上隐藏域中的节点
    var policyNos = "";
	$("input[name='checkCode']:checked").each(function() {
		policyNos = policyNos + $(this).val() + ",";
	});
	 
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
							+ "&damageTime=" + $("#damageTime").val()+ "&flowNodeCode=" + flowNodeCode, "no" ],
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
					maxmin : true,
					offset : [ '0', '26%' ],
					area : [ '850px', '500px' ],
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
		title : "调查任务发起",
		closeBtn : 0,
		shadeClose : true,
		scrollbar : false,
		skin : 'yourclass',
		area : [ '1100px', '500px' ],
		content : [ "/claimcar/survey/initSurvey.do" + urls ]
	});
}

/** 多保单关联与取消 */
var rela_index = null;
function relationshipList(registNo,relationFlag) {
	/*
	 * var url = "/claimcar/regist/relationshipList.do?registNo=" +
	 * registNo+"&nodeCode="+"Chk";
	 */
	var url = "/claimcar/regist/relationshipList.do?registNo="
			+ registNo + "&flag=check"+"&relationFlag="+relationFlag;
	if (rela_index != null) {
		layer.close(rela_index);
	} else {
		rela_index = layer.open({
			type : 2,
			title : '多保单关联与取消',
			shade: [0.8, '#393D49'],
			area : [ '100%', '85%' ],
			content : url,
			end : function() {
				rela_index = null;
			}
		});
	}
}

/**收款人账户信息查询*/
function payCustQuery(registNo){
	var lawsuitFlag  = $("input[name$='lawsuitFlag']:checked").val();
	var url="/claimcar/payCustom/payCustomQueryList.do?registNo="+registNo+"&lawsuitFlag="+lawsuitFlag+"";
	if(payIndex==null){
		payIndex=layer.open({
		    type: 2,
		    title: '收款人查询',
		    shade: false,
		    area: ['95%', '80%'],
		    content:url,
		    end:function(){
		    	payIndex=null;

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
	var lawsuitFlag = 0;//诉讼
	$("[name='prpLCompensate.lawsuitFlag']").each(function(){
		if($(this).prop("checked")==true){
			lawsuitFlag = $(this).val();
		}
	});
	if(flag=='N'||flag=='S'){
		url = "/claimcar/payCustom/payCustomEdit.do?registNo=" + registNo
		+ "&flag=" + flag + "&nodeCode=" + nodeCode + "" + "&payId="
		+ payId + ""+"&lawsuitFlag="+lawsuitFlag;
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
			area : [ '90%', '85%' ],
			content : url,
			end : function() {
				pIndex = null;
			}
		});
	}

}
function payCustomOpenFind(flag, payId,lawsuitFlag) {
	// flag N-普通维护 Y-反洗钱信息补充 S-查勘环节（收款人类型固定并查询带入承保人信息）V-收款人信息查看（只读）
	/*$("input[name$='payObjectKind']").each(function(){
		var idx = $(this).attr("name").split("_")[0];
	var payId=*/
    var registNo = $("#registNo").val();// 从当前页获取报案号
	var url = "";
	var nodeCode = $("#nodeCode").val();// 从当前页获取节点信息
	if(flag=='N'||flag=='S'){
		url = "/claimcar/payCustom/payCustomEdit.do?registNo=" + registNo
		+ "&flag=" + flag + "&nodeCode=" + nodeCode + "" + "&payId="
		+ payId + ""+"&lawsuitFlag="+lawsuitFlag;
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
			area : [ '90%', '85%' ],
			content : url,
			end : function() {
				pIndex = null;
			}
		});
	}

}

function payCustomSearch(flag){
	// flag N-普通维护 Y-反洗钱信息补充 S-查勘环节（收款人类型固定并查询带入承保人信息）
    var registNo = $("#registNo").val();// 从当前页获取报案号
	var nodeCode = $("#nodeCode").val();// 从当前页获取节点信息
	var lawsuitFlag = 0;//诉讼
	$("[name='prpLCompensate.lawsuitFlag']").each(function(){
		if($(this).prop("checked")==true){
			lawsuitFlag = $(this).val();
		}
	});
	var url = "/claimcar/payCustom/payCustomSearchList.do?registNo=" + registNo
	+ "&flag=" + flag + "&nodeCode=" + nodeCode + "&lawsuitFlag="+lawsuitFlag;
	var search_index=null;
	if (search_index == null) {//防止打开多个页面
		search_index = layer.open({
			type : 2,
			title : '收款人账户信息查询',
			shade : false,
			area : [ '90%', '90%' ],
			content : url,
			end : function() {
				search_index = null;
			}
		});
	}


}
/**理算或预付反洗钱信息补录
 * 
 */
var pIndex = null;
function CustomOpen(flag,element) {
	
	var payId=$(element).parent().parent().find("td input[name$='payeeId']").val();
	
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
function CustompadpayOpen(flag,element) {
   
	var payId=$(element).parent().parent().parent().parent().find("tbody input[name$='payeeId']").val();
	
	
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
		parent.parent.initPayCustom();
	}
}

/** 大案预报功能 */
var big_index;
function viewBigOpinion(registNo,nodeCode) {
	//alert("pp"+registNo+","+nodeCode);
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


/**
 * 是否出庭应诉”点选为“否”时，未出庭原因必录
 * “是否出庭应诉”点选为“是”和“答辩状”时，未出庭原因不允许录入
 */
function isToCourtListener(formId) {
	var isToCourt = $("#"+formId+" [name = 'prpLLawSuitVo.isToCourt'] option:selected").val();
	console.log('formId', formId);
	if (isToCourt == '1') {
		$("#"+formId+" [name = 'prpLLawSuitVo.unCourtReason']").attr("datatype","*,*0-500");
		$("#"+formId+" [name = 'prpLLawSuitVo.unCourtReason']").removeAttr("disabled", "disabled")
	} else {
		$("#"+formId+" [name = 'prpLLawSuitVo.unCourtReason']").removeAttr("datatype","*,*0-500");
		$("#"+formId+" [name = 'prpLLawSuitVo.unCourtReason']").attr("disabled", "disabled")
	}
}

/**
 * 更改处理方式响应
 * 我司出庭人员：录入框，自由录入, 处理方式为“自办”时必填，处理方式为“普通代理”、“风险代理”选项时不允许录入
 */
function changeHandleType(formId) {
	var handleType = $("#"+formId+" [name='prpLLawSuitVo.handleType'] option:selected").val();
	console.log('formId', formId);
	// 自办
	if(handleType == 0) {
		$("#"+formId+" [name='prpLLawSuitVo.toCourtPerson']").attr("datatype","*");
		$("#"+formId+" [name='prpLLawSuitVo.toCourtPerson']").removeAttr("disabled","disabled");
		// 律师费
		$("#"+formId+" [name='prpLLawSuitVo.attorneyfee']").attr("disabled", "disabled");
		$("#"+formId+" [name='prpLLawSuitVo.attorneyfee']").removeAttr("datatype","/^[0-9]+(\\.[0-9]{2})?$/");
		// 律师事务所名称
		$("#"+formId+" [name='prpLLawSuitVo.firmname']").attr("disabled", "disabled");
		$("#"+formId+" [name='prpLLawSuitVo.firmname']").removeAttr("datatype","*");
		// 案件经办律师
		$("#"+formId+" [name='prpLLawSuitVo.lawyers']").attr("disabled", "disabled");
		$("#"+formId+" [name='prpLLawSuitVo.lawyers']").removeAttr("datatype","*");
	} else {
		// 3.处理方式点选为“自办”时，律师费、律师事务所名称、案件经办律师不允许录入
		$("#"+formId+" [name='prpLLawSuitVo.toCourtPerson']").removeAttr("datatype","*");
		$("#"+formId+" [name='prpLLawSuitVo.toCourtPerson']").attr("disabled","disabled");
		// 律师费
		$("#"+formId+" [name='prpLLawSuitVo.attorneyfee']").removeAttr("disabled", "disabled");
		$("#"+formId+" [name='prpLLawSuitVo.attorneyfee']").attr("datatype","/^[0-9]+(\\.[0-9]{2})?$/");
		// 律师事务所名称
		$("#"+formId+" [name='prpLLawSuitVo.firmname']").removeAttr("disabled", "disabled");
		$("#"+formId+" [name='prpLLawSuitVo.firmname']").attr("datatype","*");
		// 案件经办律师
		$("#"+formId+" [name='prpLLawSuitVo.lawyers']").removeAttr("disabled", "disabled");
		$("#"+formId+" [name='prpLLawSuitVo.lawyers']").attr("datatype", "*");
	}
}


/**
 * 检查是否录入诉讼信息
 * @Param registNo
 */
function checkLawSuit() {
	var registNo = $("#registNo").val();
	var flag = false;
	$.ajax({
		url : "/claimcar/lawSuit/findLawSuit.ajax",
		type : 'post', // 数据发送方式
		data: {"registNo": registNo}, // 要传递的数据
		async : false,
		success : function(result) {
			console.log("result", result);
			if (result.data == 0) {
				flag =  false;
			} else {
				flag = true;
			}
		}
	});
	return flag;
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
function uploadCertifys(taskId) {
	if (isBlank(taskId)) {
		layer.alert("工作流taskId不能为空");
		return;
	}
	var url = "/claimcar/imgManager/uploadCertify.do?taskId=" + taskId;
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
}

//公估费资料上传
function uploadAssessorsFee(bussNo,handlerStatus){
	if (isBlank(bussNo)) {
		layer.alert("任务号不能为空");
		return;
	}
	var url = "/claimcar/imgManager/uploadAssessorsFee.do?bussNo=" + bussNo
	+"&handlerStatus="+handlerStatus;
	window.open(url, "公估费资料上传");
}
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
				area : [ '98%', '98%' ],
				title:'电子地图',
				fix : true, // 固定
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
				var userInfo = encodeURI(params);
				return {
					comCode : comCode,
					userInfo : userInfo,
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
    var goUrl ="/claimcar/defloss/findsDefloss.do?registNo="+registNo;
	openTaskEditWin("定损详细信息",goUrl);
}

function vloss(registNo){
	var index="VLoss";//核损
	var goUrl ="/claimcar/defloss/findDefloss.do?registNo="+registNo+"&index="+index;
	openTaskEditWin("核损详细信息",goUrl);
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
//报案记录打印
function reportPrint(registNo){
	var mainId="";
	var compensateNo="";
	var index="A";
	certifyPrintType(mainId,registNo,compensateNo,index);
	
}

// 查勘单证打印
function CheckPrint(registNo, ul) {
	var mainId="";
	var compensateNo="";
	var index="B";
	certifyPrintType(mainId,registNo,compensateNo,index);
	
}


//赔款收据打印
function certifyPrintPayFee(registNo1,compensateNo1,mainId){
	
	var registNo=registNo1;
	
	var compensateNo=compensateNo1;
	var index="G";
    certifyPrintType(mainId,registNo,compensateNo,index);
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
	var compensateNo="";
	var index="C";
	certifyPrintType(mainId,registNo,compensateNo,index);
	
}

//核损清单
function verifyLossCertifyPrint(mainId,registNo){
	var compensateNo="";
	var index="D";
	certifyPrintType(mainId,registNo,compensateNo,index);
}

//页面子页面的核损清单按钮打印
function verifyLossCertifyPrintSon(mainId,registNo){
	var compensateNo="";
	var index="D";
	certifyPrintTypeSon(mainId,registNo,compensateNo,index);
}
//赔款理算书
function AdjustersPrint(registNo,compensateNo){
	var mainId="";
	var index="E";
	certifyPrintType(mainId,registNo,compensateNo,index);
	
}
//子页面赔款理算书打印按钮
function AdjustersPrintSon(registNo,compensateNo){
	var mainId="";
	var index="E";
	certifyPrintTypeSon(mainId,registNo,compensateNo,index);
}

//子页面赔款收据打印按钮
function certifyPrintPayFeeSon(registNo,compensateNo,mainId){
	
	
	var index="G";
	certifyPrintTypeSon(mainId,registNo,compensateNo,index);
}

//赔款理算书 弹窗 牛强2017-2-27
function AdjustersPrintT(registNo,compensateNo){
	var mainId="";
	var index="e";
	certifyPrintTypeCopy(mainId,registNo,compensateNo,index);
	
}
//赔款理算书附页
function AdjustersPrints(registNo,compensateNo){
	var mainId="";
	var index="F";
	certifyPrintType(mainId,registNo,compensateNo,index);
	
}

//预付垫付计算书
function AdjustersPrintPrePad(registNo,compensateNo){
	var mainId="";
	var index="P";
	certifyPrintType(mainId,registNo,compensateNo,index);
	
}

//历史出险信息
function caseDetails(registNo){
	
	var nodecode=$("#nodeCode").val();
	var params = "";
	if(nodecode =="Regis" && isBlank(registNo)){
	  var CIPolicyNo = $("#CIPolicyNo").val();
	  var BIPolicyNo = $("#BIPolicyNo").val();
      params="?CIPolicyNo=" + CIPolicyNo + "&BIPolicyNo=" + BIPolicyNo;
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
//	return "没写入不能提交";
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
		goUrl ="/claimcar/persTraceVerify/personTraceVerify.do?flowTaskId="+id;
		title ="人伤费用审核查看";
		
	}

	parent.openTaskEditWin(title,goUrl);
}

//人伤费用审核
function PLChargeView(registNo){
	var url="/claimcar/persTraceVerify/pLChargeView.do";
	var params = {
			"registNo" : registNo,
		};
	var taskId="";
	var type="person";
$.ajax({
		
		url : url, // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(result) {// 回调方法，可单独定义	
		
			if (result.status == "200") {
				    taskId=result.data;
					
					
			}
		}
	});
 
  vLossView(type,taskId);
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
//	var params1 ="";
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
	var compensateNo="";
	var index="D";
	certifyPrintType(mainId,registNo,compensateNo,index);
	
}

function openOrCloseTitle(e,id,index){
var registNo=$("#registNo").val();
var params = {
		"registNo" : registNo,
		"id":id,
		"flag":"1"
	};
$.ajax({
	url : "/claimcar/certify/reqPhotoInfo.do", // 后台处理程序
	type : 'post', // 数据发送方式
	dataType : 'json', // 接受数据格式
	data : params, // 要传递的数据
	async:false, 
	success : function(jsonData) {// 回调方法，可单独定义
		if(jsonData.status=="100"){
			var arrays=eval(jsonData.data);
			var str="";
			for(var i in arrays){
				str=str+"<tr>"+
						 "<td width='100px'>"+arrays[i].lossItemName+"</td>"+
				         "<td width='100px'>"+arrays[i].mustUpload+"</td>"+
				         "<td width='100px'>"+arrays[i].provideInd+"</td>"+
				         "<td width='100px'>"+arrays[i].imgNumber+"</td>"+
				         "<tr>";
				
			}
			$("#"+index).html(str);
			 var a = $(e).next();
				if (a.css("display") == "none") {
					$(e).next().show();
					$(e).removeClass('t_close');
				} else {
					$(e).next().hide();
					$(e).addClass('t_close');
				}
			
		}
	}
});
   
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

/*公估费查看按钮*/
function IntermFeeButton(taskId){

	if (isBlank(taskId)) {
		layer.alert("任务号不能为空");
		return;
	}
	 var goUrl = "/claimcar/assessors/assessorsFeeEntry.do?taskId=" + taskId;
	 openWinCom('公估费查看', goUrl);
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

/*查勘费录入*/
function checkFee(taskId){

	if (isBlank(taskId)) {
		layer.alert("任务号不能为空");
		return;
	}
	 var goUrl = "/claimcar/checkfee/checksFeeEntry.do?taskId=" + taskId;
		openTaskEditWin('查勘费录入', goUrl);
}

/*查勘费查看按钮*/
function CheckFeeButton(taskId){

	if (isBlank(taskId)) {
		layer.alert("任务号不能为空");
		return;
	}
	 var goUrl = "/claimcar/checkfee/checksFeeEntry.do?taskId=" + taskId;
	 openWinCom('查勘费查看', goUrl);
}


/*查勘费审核*/
function checkVeri(taskId){
	
	if (isBlank(taskId)) {
		layer.alert("任务号不能为空");
		return;
	}
	 var goUrl = "/claimcar/checkfee/checksFeeAudit.do?taskId=" + taskId;
		openTaskEditWin('查勘费审核', goUrl);
}




function appraisaInforEdit(){
	var goUrl = "/claimcar/manager/appraisaInforList.do";
	openTaskEditWin('伤残鉴定机构维护', goUrl);
}

function claimCancelByOne(registNo, taskId,claimNo) {
	var url = "/claimcar/claim/claimCancelApplyByOne.do?registNo=" + registNo
	+ "&taskId=" + taskId+"&claimNo="+claimNo;
	var goUrl ="/claimcar/claim/claimCancelInit.do?claimNo="+claimNo+"&taskId=c"+"&workStatus=c&handlerIdKey=c";
	
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
	var url = "/claimcar/claimRecover/claimCancelApplyByOne.do?registNo=" + registNo + "&taskId=" + taskId+ "&claimNo=" + claimNo;
	var goUrl ="/claimcar/claimRecover/claimCancelInit.do?claimNo="+claimNo+"&taskId=c"+"&workStatus=c&handlerIdKey=c";
	$.ajax({
		url : url,
		dataType : "json",// 返回json格式的数据
		type : 'get',
		success : function(result) {// json是后端传过来的值
			if(result.statusText=="1"){
				openTaskEditWin("立案注销拒赔申请处理",goUrl);
				/*
				$.ajax({
					 url : '/claimcar/claimRecover/claimCancelInit.do?claimNo='+claimNo+'&taskId='+taskId+"&workStatus=c&handlerIdKey=c",
					 type : 'post', // 数据发送方式
					success : function(res) {//json是后端传过来的值
						openTaskEditWin("立案注销拒赔处理",goUrl);
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
			*/}else{
				layer.alert("该案件不能发起");
			}
		},
		error : function() {
			layer.msg("获取数据异常");
		}
	});

}

function certifyPrintType(mainId,registNo,compensateNo,index){
	
	var url="/claimcar/certifyPrintSearch/certifyprintType.do?mainId="+mainId+"&registNo="+registNo+"&compensateNo="+compensateNo+"&index="+index;
	 layer.open({
		type : 2,
		skin: 'layui-layer-rim', //加上边框
		title : "单证打印",
		shade : false,
		area : [ '32%', '19%' ],
		content : [ url, "no" ],
		end : function() {
			
		}
	});

}
//子页面单证打印
function certifyPrintTypeSon(mainId,registNo,compensateNo,index){
	
	var url="/claimcar/certifyPrintSearch/certifyprintType.do?mainId="+mainId+"&registNo="+registNo+"&compensateNo="+compensateNo+"&index="+index;
	 parent.layer.open({
		type : 2,
		skin: 'layui-layer-rim', //加上边框
		title : "单证打印",
		shade : false,
		area : [ '32%', '19%' ],
		content : [ url, "no" ],
		end : function() {
			
		}
	});

}
//牛强2017-2-27 修改弹窗大小
function certifyPrintTypeCopy(mainId,registNo,compensateNo,index){
	
	var url="/claimcar/certifyPrintSearch/certifyprintType.do?mainId="+mainId+"&registNo="+registNo+"&compensateNo="+compensateNo+"&index="+index;
	layer.open({
		type : 2,
		title : false,
		shade : false,
		area : [ '40%', '40%' ],
		content : [ url, "no" ],
		end : function() {
			
		}
	});
}
//恢复查询按钮
function recoverSearch(){
	$(".btn-search").removeClass("btn-disabled");
	$(".btn-search").addClass("btn-primary");
	$(".btn-search").prop("disabled",false);
}

window.onload=recoverSearch;


function payHisShow(id,hisType){
	var url="/claimcar/endCase/showPayHisEdit.do?otherId="+id+"&hisType="+hisType;
	var leftVal = $(document.body).width() - 320;
	layer.open({
		type : 2,
		title : '支付历史轨迹',
		shade : false,
		offset : [ '40%', leftVal ],
		area: ['320px', '195px'],
		content : [ url],
		end : function() {
			
		}
	});
	
}

function paycustomMoney(claimNo,registNo){
	
	var pid='';
	var signFlag='SY';//反洗钱查看标志
	url = "/claimcar/payCustom/addRowInfo.do?claimNo=" + claimNo
	+ "&registNo=" + registNo + "&pid=" + pid  + "&signFlag=" + signFlag ;
	
	title = '反洗钱复核';

if (pIndex == null) {
	pIndex = layer.open({
		type : 2,
		title : title,
		shade : false,
		area : [ '90%', '85%' ],
		content : url,
		end : function() {
			pIndex = null;
		}
	});
}
}
//子页面按钮的标签页函数
function openWinCom(title,url,isreplace){
	var bStop=false;
	var bStopIndex=0;
	var _href=url;
	var _titleName=title+"T";
try{
	var topWindow=$(window.parent.parent.parent.document);
	var show_navLi=topWindow.find("#min_title_list li");
	show_navLi.each(function() {
		if(isreplace&&isreplace==true){
			if($(this).hasClass("active")){
				bStop=true;
				bStopIndex=show_navLi.index($(this));
				$(this).first().attr("title",_titleName);
				$(this).first().html(_titleName);
				return false;
			}
		}else if($(this).find('span').text()==_titleName){
			bStop=true;
			bStopIndex=show_navLi.index($(this));
			return false;
		}
	});
	if(!bStop){
		parent.parent.parent.creatIframe(_href,_titleName);
		parent.parent.parent.min_titleList();
	}else{
		show_navLi.removeClass("active").eq(bStopIndex).addClass("active");
		var iframe_box=topWindow.find("#iframe_box");
		iframe_box.find(".show_iframe").hide().eq(bStopIndex).show().find("iframe").attr("src",_href);
	}
}catch(e){
	location.href=_href;
	return;
}
}

//子页面按钮的标签页函数 三级页面
function openWinCom1(title,url,isreplace){
	var bStop=false;
	var bStopIndex=0;
	var _href=url;
	var _titleName=title+"T";
try{
	var topWindow=$(window.parent.parent.parent.parent.document);
	var show_navLi=topWindow.find("#min_title_list li");
	show_navLi.each(function() {
		if(isreplace&&isreplace==true){
			if($(this).hasClass("active")){
				bStop=true;
				bStopIndex=show_navLi.index($(this));
				$(this).first().attr("title",_titleName);
				$(this).first().html(_titleName);
				return false;
			}
		}else if($(this).find('span').text()==_titleName){
			bStop=true;
			bStopIndex=show_navLi.index($(this));
			return false;
		}
	});
	if(!bStop){
		parent.parent.parent.parent.creatIframe(_href,_titleName);
		parent.parent.parent.parent.min_titleList();
	}else{
		show_navLi.removeClass("active").eq(bStopIndex).addClass("active");
		var iframe_box=topWindow.find("#iframe_box");
		iframe_box.find(".show_iframe").hide().eq(bStopIndex).show().find("iframe").attr("src",_href);
	}
}catch(e){
	location.href=_href;
	return;
}
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

//定损 ，核价，核损-德联易控返回信息查看lossCarMainId--对应车辆的车辆定损主表Id
function controlExpertView(registNo,licenseNo,lossCarMainId){
	var url="/claimcar/defloss/controlExpert.do?registNo="+registNo+"&licenseNo="+licenseNo+"&lossCarMainId="+lossCarMainId;
	
	layer.open({
		type : 2,
		title : '德联检测信息',
		shade : false,
		area: ['90%', '90%'],
		content :encodeURI(encodeURI(url)),
		end : function() {
			
		}
	});
}
	//查勘-德联易控返回信息查看
	function controlExpertViewOfcheck(registNo){
		var url="/claimcar/defloss/controlExpertViewOfcheck.do?registNo="+registNo;
		layer.open({
			type : 2,
			title : '德联检测信息',
			shade : false,
			area: ['90%', '90%'],
			content :url,
			end : function() {
				
			}
		});
		
	}
	
	function lookLawSuit(registNo){
		var loss_url = "/claimcar/courtMessage/initLitigation.do?registNo=" + registNo + "&status=1";
		openTaskEditWin("高院信息", loss_url);
	}

	function comCheckPic(registNo){
		var comparePicURL = $("#comparePicURL").val();
		var username = $("#carRiskUserName").val();
		var password = $("#carRiskPassWord").val();
		var claimSequenceNo = $("#claimSequenceNo").val();
		var claimPeriod = $("#claimPeriod").val();
		$("#comCheckPic").patternMath(comparePicURL, username, password, claimSequenceNo, claimPeriod);	
	}
	
	//关闭当前弹出页并刷新父页面
	function closeLocalLayer(){
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.location.reload();
		parent.layer.close(index);	
	}	
	
/**
 * 山东预警信息展示
 * @param flowId
 */
function policeInfoShow(registNo){
	var params = "?registNo=" + registNo;
	layer.open({
		type: 2,
		title: '预警信息',
		shadeClose : true,
		scrollbar : false,
		area : [ '85%', '60%' ],
		content : [ "/claimcar/sdpolice/sdpoliceInfoList.do" + params ]
	});
}



//规则信息查看
function ruleView(registNo,riskCode,ruleNode,licenseNo,taskId){
	var url="/claimcar/lIlogRule/ruleView.do?registNo="+registNo+"&riskCode="+riskCode+"&ruleNode="+ruleNode+"&licenseNo="+licenseNo+"&taskId="+taskId;
	
	layer.open({
		type : 2,
		title : 'ILOG规则信息查看',
		shade : false,
		area: ['80%', '80%'],
		content :encodeURI(encodeURI(url)),
		end : function() {
			
		}
	});
}

//规则明细查看
var ruleDetailInfo;
function openRuleDetailInfo(ruleId,serialNo,ruleNode){
	var url="/claimcar/lIlogRule/ruleDetailInfo.do?ruleId="+ruleId+"&serialNo="+serialNo+"&ruleNode="+ruleNode;
	if(ruleDetailInfo!= null){
		layer.close(ruleDetailInfo);
	}else{
		layer.open({
			title : '规则明细查看',
			type : 2,
			area : [ '40%', '40%' ],
			fix : true, // 固定
			maxmin : true,
			content : [ url, "yes" ],
			end : function() {
				ruleDetailInfo = null;
			}
		});
	}
}
function checkSubrogationFlag(registNo){
	//保单未承保车损险，也未承保全面型车损险则不能做代位求偿案件
	var checkSubrogationFlag = "1";
	$.ajax({
		url: "/claimcar/defloss/checkSubrogationFlag.do?registNo="+registNo,
		type:'post',
		async : false,
		success : function(jsonData) {// 回调方法，可单独定义
			if(jsonData.data=="0"){
				layer.msg(jsonData.statusText);
				checkSubrogationFlag = "0";
			}
		}
	});
	if(checkSubrogationFlag == "0"){
		return false;
	}else{
		return true;
	}
}

/**
 * 转换大写
 * @author ly
 * @param obj
 */
function toUpperValue(obj)  
{  
	obj.value = obj.value.toUpperCase();
}

//精友2定损查看接口
function jyViewData(registNo,id){
	var strURL ="/claimcar/defloss/jyViewData.do?registNo="+registNo+"&id="+id; 
	window.open(strURL, "定损查看","width=1010,height=670,top=0,left=0,toolbar=0,location=0,directories=0,menubar=0,scrollbars=1,resizable=1,status=0");
}

/**
 * 根据flowTaskId获取案件是否从联、从共案件
 * @param flowTaskId
 */
function getGBStatus(flowTaskId) {
	var result = null;
	if (flowTaskId != null) {
		$.ajax({
			url : "/claimcar/check/queryFlowTaskIdIsGB.ajax", // 后台校验
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : {
				'flowTaskId' : flowTaskId
			},
			async : false,
			traditional : true,
			success : function(res) {// 回调方法，可单独定义
				if (res.statusText == 'Y') {
					if (res.status == "1" || res.status == "2") {
						//1，2，表示正在处理，页面给提示
						layer.confirm(res.data,{btn : ['确定']},function(index){
							layer.close(index);
							resule = res;
						});
					} else {
						//0，第一次，在接收中处理；3，结束，不提示
						result = res;
					}
				} else {
					result = res;
				}
			}
		});
		return result;
	}
}
//判断查勘自助案件有没有接收
function checkAcceptSelfCase(registNo){
	var flags = "1";
	var url = "/claimcar/handoverTask/checkAcceptSelfCase.do";
	var params = {
		"registNo" : registNo
	};
	$.ajax({
		url : url,
		data : params,
		type : 'post',
		async : false,
		success : function(result) {// json是后端传过来的值
			if(result.data == "0"){
				layer.alert(result.statusText);
				flags = "0";
		    	return false;
			}
			
		},
		error : function() {
			layer.alert(result.statusText);
			flags = "0";
	    	return false;
		}
	});
	if(flags == "0"){
		return false;
	}else{
		return true;
	}
}

/**
 * 判断空值
 * @param val
 * @returns {boolean}
 */
function checkNull(val) {
    var str = val.replace(/(^\s*)|(\s*$)/g, '');
    console.log('str.length', str.length);
    if (str == '' || str == undefined || str == null || str.length == 0) {
        return false;
    } else {
        return true;
    }
}

/**
 * 影像图片查看
 */
function imageMovieScan(bussNo){
	  if (isBlank(bussNo)) {
			layer.alert("业务号不能为空");
			return;
		}
	  var url="/claimcar/yxImageUpload/urlReqQueryParam?bussNo="+bussNo;
	  window.open(url);

}

/**
 * 影像上传
 */
function imageMovieUpload(bussNo){
	  if (isBlank(bussNo)) {
			layer.alert("业务号不能为空");
			return;
		}
	  var url="/claimcar/yxImageUpload/urlReqUploadParam?bussNo="+bussNo;
	  window.open(url);
	  
}

function imageAssessorsFeeUpload(bussNo,handlerStatus){
	  if (isBlank(bussNo)) {
			layer.alert("业务号不能为空");
			return;
		}
	  var url="/claimcar/imgManager/uploadAssessorsFee?bussNo="+bussNo+"&handlerStatus="+handlerStatus;
	  window.open(url);
	  
}
//查勘费资料上传
function imageCheckFeeUpload(bussNo,handlerStatus){
	  if (isBlank(bussNo)) {
			layer.alert("业务号不能为空");
			return;
		}
	  var url="/claimcar/imgManager/uploadChecksFee?bussNo="+bussNo+"&handlerStatus="+handlerStatus;
	  window.open(url);
	  
}
function policyImage(bussNo){
	 var prpUrl = $("#prpUrl").val();
	 if (isBlank(bussNo)) {
		 layer.alert("保单号不能为空");
		 return;
	 }
	 window.open(prpUrl+"/image/ImageJump?bussNo="+bussNo+"&code=ECM0001&type=C");
}
function showInfo(id){
var url="/claimcar/yjclaimcar/showYjComInfo.do?id="+id;
	
	layer.open({
		type : 2,
		title : '阳杰报价信息查看',
		shade : false,
		area: ['90%', '80%'],
		content :url,
		end : function() {
			
		}
	});
}
/**
 * 人伤赔偿标准
 */
function feeImageView(){
	var loss_url = "/claimcar/payfeeInfo/feeStandardDetailViewList.do";
	openTaskEditWin("人伤赔偿标准", loss_url);
}

/**
 * 展示山东风景预警信息
 */
function warnView (registNo){
	if(isBlank(registNo)){
		layer.msg("报案号不能为空！");
	}else{
		var url = "/claimcar/warnAction/viewCheck.do?registNo="+registNo;
		layer.open({
			type : 2,
			title : '山东预警推送信息',
			shade : false,
			area: ['80%', '70%'],
			content :url,
			end : function() {
				
			}
		});
	}
	
}

/**
 * 展示反平台欺诈信息展示
 */
function realtimequeryView (registNo){
	if(isBlank(registNo)){
		layer.msg("报案号不能为空！");
	}else{
		var url = "/claimcar/realTimeQueryAction/realTimeQueryView.do?registNo="+registNo;
		layer.open({
			type : 2,
			title : '反平台欺诈信息',
			shadeClose : true,
			scrollbar : false,
			area: ['90%', '90%'],
			content :url,
			end : function() {
				
			}
		});
	}
	
}
