//--------------------------------------------------------------------------------//
//--------------check主表单-------------
var ajaxEdit;
var vinNoFlag =true;
$(function() {
	// 1.控件初始化
	initCheck();
	displayDisasterTwo();
	//
	$.Datatype.certiNo = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;// 身份证验证
	$.Datatype.sum = /^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/;//金额，
	$.Datatype.number = /^[0-9]*[1-9][0-9]*$/;//正整数
	$.Datatype.age = /^(?:[1-9][0-9]?|1[01][0-9]|120)$/;//年龄
	$.Datatype.licenNo=/^([\u4e00-\u9fa5-A-Z]{1}[A-Z]{1}[A-Z_0-9]{5})|(新000000)$/;//车牌号
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
			vinNoFlag = false;
			//return false;
		}else{
			vinNoFlag = true;
		}
    };
    $.Datatype.selectMust = function(gets,obj,curform,regxp){
		var code = $(obj).val();
		if(isBlank(code)){
			return false;
		}
    };
    
	ajaxEdit = new AjaxEdit($('#saveMain'));
	ajaxEdit.targetUrl = "/claimcar/check/saveCheck.do";
	ajaxEdit.afterSuccess = after;
	ajaxEdit.beforeSubmit = before;//在验证成功后，表单提交前执行的函数
	// 绑定表单
	ajaxEdit.bindForm();
	
	
	$("select[name$='scheduledUsercode']").each(function(){
	    $(this).attr("datatype","selectMust");
	  });
	 $("select[name='lossChargeVo.kindCode']").attr("datatype","selectMust");
	
	initDutyRate($("#policyType_tr1").val());
	reconcileChange();
	
	for(var i=0;i<$("#personLossSize").val();i++){
		var name = "select[name='checkPersonVos["+i+"].lossPartyId']";
		setLossItemType(i,$(name).val());
	};
	
});

//--------------check主表单-------------

//初始化
function initCheck(){
	var status = $("#status");
	var flowTaskId = $("#flowTaskId").val();
	if (Number(status.val())==0) {//接收未处理任务
		var teRe=$("#tempRegistFlag");
		var reTf=$("#registTaskFlag");
		var isMobileCase=$("#isMobileCase").val();
		if("1" == isMobileCase || "2" == isMobileCase || "3" == isMobileCase){
			mobileCaseTips(isMobileCase);
		}else if(Number(teRe.val())==1||Number(reTf.val())!=1){
			reportCancel();
		}else{//接受任务
			checkAcceptTask();
		}
	}else if(Number(status.val())==3){
		initThirdBus();
	}else if(Number(status.val())==9){
		reportCancel();
	}
	
	//-------------------------
	// 2.初始化下拉框 // 单车事故 // 公估查勘 // 是否重大赔案上报
	initSelectSin();
	
	initPropRadio($("#propLossSize").val());
//	initPersonRadio($("#personLossSize").val());
	changeSingle();//是否单车事故change事件
	damageSelect($("#damageNames"));// 出险原因
	initCheckBox();//初始化免赔率的值
	ciIndemDuty();//事故责任比例选择有责时，交强险必选选择有责
	disTwChange();
	
	//-------------------------
	kindSelect(Number(status.val()));

	//巨灾、收款人信息、设置免赔率、查勘扩展信息默认隐藏
	initNextClass();
	
	//免责原因
	$("#noDutyReason").hide();
	var noDutyFlag=$("input[name='checkVo.noDutyFlag']:checked");
	if(noDutyFlag.val()!="1" || noDutyFlag.val()!="0"){
		$("input[name='checkVo.noDutyFlag'][value='0']").prop("checked",true);
	}

	// 查勘指标信息按钮
	$("#showIndex").removeAttr("disabled");
}

function mobileCaseTips(isMobileCase){
	if("1"==isMobileCase){
		layer.alert("该案件是移动查勘案件，移动端暂存之前理赔不能操作！");
	}else if("2"==isMobileCase){
		layer.alert("该案件已推送至车童系统，理赔不能操作！");
	}else if("3"==isMobileCase){
		layer.alert("该案件已推送至民太安系统，理赔不能操作！");
	}else{
		layer.alert("该案件已推送至第三方系统，理赔不能操作！");
	}
	
	$("body textarea").attr("disabled", "disabled");
	$("body input").each(function() {
		$(this).attr("disabled", "disabled");
	});
//	   layer.open({type: 1,skin: 'layui-layer-molv', // 样式类名
//			  closeBtn: 0, // 不显示关闭按钮
//			  shift: 2,shadeClose: false, // 开启遮罩关闭
//			  content: $("id")'<br><h4>&nbsp;&nbsp;&nbsp;&nbsp;该案件为移动端案件，移动端暂存之前理赔不能操作！<h4><br> '
//	   });
	}
function reportCancel(){
   layer.open({type: 1,skin: 'layui-layer-demo', // 样式类名
		  closeBtn: 0, // 不显示关闭按钮
		  shift: 2,shadeClose: false, // 开启遮罩关闭
		  content: '<br><br><h4><font color="red">该案件为临时报案案件或者已注销案件，不能操作！</font><h4><br><br>'
   });
}

function initSelectSin(){
	var sif=$("input[name='checkVo.singleAccidentFlag']:checked");
	var repf = $("input[name='checkVo.majorCaseFlag']:checked");
	var damTy = $("#damType");
//	if (sif.val()=="1") {
//		damTy.val("01");
//	}
	if (sif.val()=="0") {
		damTy.val("02");
	}
	
	// 公估查勘,代位求偿 // 公估查勘
//	var ckcla=("option:selected",$("#checkClass"));
//	var mef = $("#mediaryFee");
//	if (Number(ckcla.val())==0) {
//		mef.hide();
//	} else {
//		mef.show();
//	}
	
	//大案审核页面的是否重大赔案上报默认为是
	var nodeCode = $("#nodeCode").val();
	var majorText = "0";
	if(nodeCode!="Chk"&&nodeCode!="ChkRe"){
		$("input[name='checkVo.majorCaseFlag'][value='1']").prop("checked",true);
		majorText = "1";
	}
	// 是否重大赔案上报
	var clText=$("#claimTexts");
	var maText=$("#majorText");
	if(repf.val()=="1" || majorText == "1"){
		clText.show();
		maText.attr("datatype","*1-299");
	}else{
		clText.hide();
		maText.removeAttr("datatype");
	}
	
}

function changeSingle() {
	var sinf=$("input[name='checkVo.singleAccidentFlag']:checked");
	var damTy=$("#damType");
	if (sinf.val() == "0") {
		var tempV = $("input[name='damTypeVal']").val();
		if(tempV==="01"){
			tempV = "02";
		}
		damTy.val(tempV);
		damTy.find("option[value='01']").attr("disabled", "disabled");
	} else {
		damTy.find("option").removeAttr("disabled");
	}
	// 出险原因
	damageSelect($("#damageNames"));
}

function damageSelect(obj){
//	var sinf=$("input[name='checkVo.singleAccidentFlag']:checked");
	var damCo = $("option:selected", $(obj));
//	if(sinf.val()=="1"){
//		$(obj).val("DM01");
//		$(obj).find("option[value!='DM01']").attr("disabled",true);
//	}else{
//		$(obj).find("option").each(function(){
//			$(this).removeAttr("disabled");
//		});
//	}

	var dam_other=$("#dam_other");
	if(damCo.val()!="DM99"){
		dam_other.val("");
		dam_other.find("option").each(function(){
			$(this).attr("disabled",true);
		});
		dam_other.hide();
	}else{
		dam_other.find("option").each(function(){
			$(this).removeAttr("disabled");
		});
		dam_other.show();
	}
	var dam_div=$("#dam_val");
	if($(obj).val()=="DM12"){
		dam_div.show();
	}else{
		dam_div.hide();
	}
}

function initPropRadio(propSize){
	// 财产
	var propChk=$("input[name='checkVo.isPropLoss'][value='0']");
	var prChk=$("input[name='checkVo.isPropLoss'][value='1']");
	if (Number(propSize)==0){
		propChk.prop("checked",true);
		prChk.removeAttr("checked");
	}else{
		prChk.prop("checked","checked");
		propChk.removeAttr("checked");
	}
}
function initPersonRadio(personSize){
	// 人伤
	var chkPer=$("input[name='checkVo.isPersonLoss'][value='0']");
	var chkPe=$("input[name='checkVo.isPersonLoss'][value='1']");
	if (Number(personSize)==0){
		chkPer.prop("checked",true);
		chkPe.removeAttr("checked");
	}else{
		chkPe.prop("checked",true);
		chkPer.removeAttr("checked");
	}
}

/** 未处理任务接收
 */
function checkAcceptTask() {
	if(!isEmpty($("#oldClaim"))){
		layer.confirm('该案件为旧理赔迁移案件，是否继续处理?', {
			btn : [ '确定', '取消' ]
		},function(){
			acceptTask();
		},function(){
			$("body textarea").attr("disabled", "disabled");
			$("body input").each(function(){
				$(this).attr("disabled", "disabled");
			});
		});
	}else{
		acceptTask();
	}
	
	
}

function acceptTask(){
	var url_ac="/claimcar/check/acceptCheckTask.do?flowTaskId=";
	var url_in="/claimcar/check/initCheck.do?flowTaskId=";
	var flowTaskId = $("#flowTaskId").val();
	layer.confirm('是否确定接收此任务?', {
		btn : [ '确定', '取消' ]
	}, function(index) {
		layer.load(0, {shade : [0.8, '#393D49']});
		$.post(url_ac+Number(flowTaskId), function(jsonData) {
			if (jsonData.status != "200") {
				layer.alert("接收任务失败！");
				$("body textarea").eq(1).attr("disabled", "disabled");
				$("body input").each(function() {
					$(this).attr("disabled", "disabled");
				});
			} else {
				var url=url_in+ eval(jsonData).data;
				location.href = url;
				layer.close(index);
			}
		});
	},function(){
		$("body textarea").attr("disabled", "disabled");
		$("body input").each(function(){
			$(this).attr("disabled", "disabled");
		});
	});
}

/** 查勘基本信息校验
 */
function checkInfoValid() {
	//查勘地点
//	var chkAr=$("#checkAddress");
//	if (isBlank(chkAr.val())) {
//		layer.alert("请填写查勘地点！");
//		return false;
//	}
	// 是否互碰自赔
	var isSelf=$("input[name='checkVo.isClaimSelf']:checked");
//	var dutyRate=$("#indemnityDutyRate");
	var losTy=$("input[name='checkVo.lossType']:checked");
	var chkProp=$("input[name='checkVo.isPropLoss']:checked");
	var chkPer=$("input[name='checkVo.isPersonLoss']:checked");
	if (Number(isSelf.val())==1) {
		// 事故责任比例不能为全责或者无责、空
//		if (dutyRate.val()=="0") {
//			layer.alert("互碰自赔案件,事故责任比例不能选全责!");
//			return false;
//		} else if (dutyRate.val()=="4") {
//			layer.alert("互碰自赔案件,事故责任比例不能选无责!");
//			return false;
//		}
		// 互陪自赔案件不能选择全损
		if (losTy.val()=="1") {
			layer.alert("互陪自赔案件不能选择全损");
			return false;
		}
	}
	// 财产
	if (isBlank(chkProp.val())) {
		layer.alert("请选择是否包含财损！");
		return false;
	}
	// 人伤
	if (isBlank(chkPer.val())) {
		layer.alert("请选择是否包含人伤！");
		return false;
	}
	return true;
}

//单车事故--》事故类型
var singleFlag = $("input[name='checkVo.singleAccidentFlag']");
singleFlag.click(function (){
	if($(this).val()=="1"){
		$("#damType").val("01");
	}
});

/** 责任比例比较
 */
function compareDutyRate(field){
	var rate = parseFloat($(field).val());
	var indemnityDuty = $("#indemnityDutyRate").val();
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
		  $("#indemnityDutyRateValue").val("0.0");
	  }
	if(indemnityDuty != "3" && rate < mixRate) {
		layer.alert("责任比例不能小于"+rateName+"比例"+mixRate+"%");
		changeDuty();
		
	}
}

function changeDuty() {
	var duty = $("#indemnityDutyRate").val();
	var dutyVa=$("#indemnityDutyRateValue");
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
	ciIndemDuty();
}

//事故责任比例选择有责时，交强险必选选择有责
function ciIndemDuty() {
	var duty = $("#indemnityDutyRate").val();
	var ciDuty=$("#ciIndemDuty");
//	var ciDuf=$("select[name='checkDutyVo.ciDutyFlag']");
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

/** 页面跳转 */
var layIndex;
function caseDetails(title, url, id) {
	layIndex = layer.open({
		title : title,
		type : 2,
		area : [ '90%', '90%' ],
		fix : false, // 固定
		maxmin : true,
		content : url
	});
}

/** 车损处理 */
var carTr_index;
var thridCarItemSize;
function checkCarEdit(element) {
	var index = $(element).parent().parent().index()-1;
	carTr_index = index;
	thridCarItemSize = $("#thridCarItemSize").val();
	// 初始化
	// $(".saveform").Validform();
	// 页面校验
	if (!checkInfoValid()) {
		return false;
	}

	//更新报案出险经过说明
	updateDangerRemark($("#registNo").val(),$("#dangerRemark").val());
	// 更新基本信息
	var params = $("#saveMain").serialize();
	var load_index = layer.load(0, {shade : [ 0.8, '#393D49' ]});
	$.ajax({
		url : "/claimcar/check/updateCheckMain.do", // 后台处理程序
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(jsonData) {// 回调方法，可单独定义
			var result = eval(jsonData);
			//alert("保存或者更新主表成功-----" + result.status);
			if (result.status == "200") {
				layer.close(load_index);
				var carId;
				if (index == "-1") {// 标的车
					carId = $("#mainCarId").val();
				} else {
					//var idx = "#" + "thCarId" + Number(index);
					var idx = "input[name='checkThirdCarVos["+ Number(index) +"].carid']";
					carId = $(idx).val();// 获取三者车的id
				}
				layerIndex = layer.open({
					title : '车辆处理',
					type : 2,
					area : [ '90%', '95%' ],
					fix : true, // 固定
					maxmin : true,
					content : '/claimcar/checkcar/viewCarLossEdit.do/' + carId
					+'/'+$("#nodeCode").val()+'/'+$("#status").val()
				});
			}
		}
	});
}

//车损暂存回调
function loadReplaceCarTr(carId,serialNo,saveType) {
	/*if(saveType=="add"){
		carTr_index = Number(carTr_index)+1;
	}*/
	var thItSize=$("#thridCarItemSize");
	var thItem=$("#thridCarItem");
	var params = {"carId" : carId,"carTr_index" : carTr_index,
				  "saveType" : saveType,"nodeCode" : $("#nodeCode").val(),
				  "status" : $("#status").val()};
	var url = "/claimcar/checkcar/loadReplaceCarTr.ajax";
	thItSize.val(thridCarItemSize);
	$.post(url, params, function(result) {
		if(saveType=="add"){
			thItem.append(result);
			thItSize.val(Number(thridCarItemSize)+1);
		}else{
			serialNo = parseInt(serialNo); 
			if(serialNo==1){
				$("#checkMainCarTbody").find("tr").eq(0).replaceWith(result);
			}else{
				thItem.find("tr").eq(carTr_index).replaceWith(result);
			}
		}
	});
	//新增打印按钮
	$("#printButton").removeClass("btn btn-disabled").addClass("btn btn-primary");
}

/** 刷新财产、人伤的损失方
 */
function refreshPropKey(registNo){
	var url_re="/claimcar/checkcar/refreshPropKey.ajax";
	var options = "";
	var params = {"registNo" : registNo};
	$.ajax({
		url : url_re, // 后台处理程序
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false, 
		success : function(jsonData) {// 回调方法，可单独定义
			$.each(eval(jsonData).data, function(key, value) {
				options = options + "<option value='"+key+"'>"+value+"</option>";
			});
			var propItems =$("#propLossTbody select[name$='lossPartyId']");
			$(propItems).each(function(){
				var selected = $(this).val();
				$(this).find("option").remove();//先删除
				$(this).append(options);//在重新增加
				$(this).find("option[value="+selected+"]").attr("selected","selected");
			});
			var personItems =$("#personLossTbody select[name$='lossPartyId']");
			$(personItems).each(function(){
				var selected = $(this).val();
				$(this).find("option").remove();//先删除
				$(this).append(options);//在重新增加
				$(this).find("option[value="+selected+"]").attr("selected","selected");
			});
		}
	});
}

//车辆详情
function checkCarView(carId) {
	var url='/claimcar/checkcar/viewCarLossEdit.do/';
	layerIndex = layer.open({
		title : '车辆处理',
		type : 2,
		area : [ '90%', '95%' ],
		fix : true, // 固定
		maxmin : true,
		content : url+carId+'/'+'chkRe'+'/'+'3'
	});
}

/** 新增三者车 */
function addThirdCar(title, url, id) {
	var sinAf=$("input[name='checkVo.singleAccidentFlag']:checked");
	var reN=$("#registNo");
	var noC=$("#nodeCode");
	if (sinAf.val() == "1") {
		layer.alert("选择为单车事故，不允许录入三者车信息!");
		return false;
	}
	thridCarItemSize = $("#thridCarItemSize").val();
	carTr_index = thridCarItemSize;
	layer.open({
		title : title,
		type : 2,
		area : [ '90%', '95%' ],
		fix : true, // 固定
		maxmin : true,
		content : url+"/"+reN.val()+"/"+noC.val(),
	});
}

/*** 删除新增的三者车*/
function dropThirdCar(element) {
	var index = $(element).parent().parent().index()-1;
	var thCar_name = "input[name='checkThirdCarVos["+index+"].scheduleitem']";
	var thCarReId = $(thCar_name).val();
	var licenseNo_name = "input[name='checkThirdCarVos["+index+"].prpLCheckCarInfo.licenseNo']";
	var thLicenseNo = $(licenseNo_name).val();
	var thCarId = $("input[name='checkThirdCarVos["+index+"].carid']").val();
//	var serialNo = $("input[name='checkThirdCarVos["+index+"].serialNo']").val();
	
	//调度注销的三者车辆可以删除
	var returnVal = false;//初始化不可删除
	$.ajax({
		url : "/claimcar/checkcar/delThirdCarValid.do", // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : {"thCarId" : thCarId}, // 要传递的数据
		async : false, 
		success : function(jsonData) {// 回调方法，可单独定义
			returnVal = eval(jsonData).data;
		}
	});
	
	//!isBlank(thCarReId)&&
	if (!returnVal) {// 来源id不为空不能删除
		layer.alert("调度查勘的车辆不允许删除！");
		return false;
	}
	//如果财产或人伤存在该车辆作为损失方，需先删除财产或人伤的数据
	/*if(!pItems_keyVlaid(serialNo,thLicenseNo)){
		return false;
	}*/
	
	layer.confirm("确定要删除 ["+thLicenseNo+"] 车辆吗?",{btn:['确定']}, function(indexs) {
		var params = {"thCarId" : thCarId};
		$.ajax({
			url : "/claimcar/checkcar/dropThirdCar",
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			success : function(jsonData) {
				if (eval(jsonData).status == "200") {
					layer.msg("删除成功！");
					var proposalPrefix = "checkThirdCarVos";
					var $parentTr = $(element).parent().parent();
					var $thCarSize = $("#thridCarItemSize");//
					var thCarSize = parseInt($thCarSize.val(), 10);//
					$thCarSize.val(thCarSize - 1);// 删除一条
					delTr(thCarSize, index, "delThirdCarTr_",proposalPrefix);
					$parentTr.remove();
					// $("#thridCarItem").find.eq(element).removeAttr();
					// window.location.reload();// 刷新父窗口
				}
			},
			error : function() {
				layer.msg("删除失败！");
			}
		});
	});	
	
}

//如果财产或人伤存在该车辆作为损失方，需先删除财产或人伤的数据
function pItems_keyVlaid(serialNo,thLicenseNo){
	var propItems_selKey =$("#propLossTbody select[name$='lossPartyId']");
	//var party_key=$(propItems_selKey).find("option[selected]");
	$(propItems_selKey).each(function(){
		if($(this).find("option[selected]").val()==serialNo){
			layer.alert("财产或人伤存在--"+thLicenseNo+"--为损失方，请先删除财产或人伤的数据！");
			return false;
		}
	});
	var personItems =$("#personLossTbody select[name$='lossPartyId']");
	$(personItems).each(function(){
		var par_key=$(this).find("option[selected='selected']").val();
		if(par_key==serialNo){
			layer.alert("财产或人伤存在--"+thLicenseNo+"--为损失方，请先删除财产或人伤的数据！");
			return false;
		}
	});
	return true;
}

//更新报案出险经过说明
function updateDangerRemark(registNo,dangerRemark){
	var url_up="/claimcar/check/updateDangerRemark.ajax";
	var params = {"registNo" : registNo,"dangerRemark" : dangerRemark,};
	$.ajax({
		url : url_up,
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		success : function(jsonData) {
			if (eval(jsonData).status == "200") {
				//layer.alert(eval(jsonData));
			}
		}
	});
}

/** 增加财产损失项 */
function addPropItem(nodeCode) {
	var $tbody = $("#propLossTbody");
	var $propLossSize = $("#propLossSize");// 财产项条数
	var propSize = parseInt($propLossSize.val(), 10);
	var registNo = $("#registNo").val();
	var params = {
		"propSize" : propSize,
		"registNo" : registNo,
		"nodeCode" : nodeCode,
		"lossStr" : $("#PropSunSelect").val()
	};
	var url = "/claimcar/check/addPropItem.ajax";
	$.post(url, params, function(result) {
		$tbody.append(result);
		$propLossSize.val(propSize + 1);
	});
	initPropRadio(propSize+1);
}

/**
 * 删除财产损失项
 * @param element
 */
function delPropItem(element) {
	var index = $(element).attr("name").split("_")[1];// 下标
	var proposalPrefix = "checkPropVos";
	var $parentTr = $(element).parent().parent();
	var $propLossSize = $("#propLossSize");// 财产项条数
	var propLossSize = parseInt($propLossSize.val(), 10);// 原财产项条数

	//var propRrsourceId = $("#" + "propItemId" + index).val();
	var reId = "input[name='checkPropVos[" + index + "].scheduleitem']";
	var propName = "input[name='checkPropVos[" + index + "].id']";
	var propRrsourceId = $(reId).val();
	var propId = $(propName).val();
	initPropRadio(propLossSize-1);
	if (propRrsourceId == "" && propId != "") {
		// 来源id为空，可以删除
		$propLossSize.val(propLossSize - 1);// 删除一条
		delTr(propLossSize, index, "delPropItem_", proposalPrefix);
		$parentTr.remove();
		dropPropLoss(propId);
	} else if (propRrsourceId == "" && propId == "") {
		// id和、来源id为空，可以直接删除
		$propLossSize.val(propLossSize - 1);// 删除一条
		delTr(propLossSize, index, "delPropItem_", proposalPrefix);
		$parentTr.remove();
	} else {
		layer.alert("不允许删除调度查勘的财产损失项！");
	}
}
function dropPropLoss(propId) {
	var params = {
		"propId" : propId
	};
	$.ajax({
		url : "/claimcar/check/dropPropLoss.ajax",
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		success : function(jsonData) {
			if (eval(jsonData).status == "200") {
				// layer.alert("删除成功！");
			}
		}
	});
}
function dropPersonLoss(personId) {
	var params = {
		"personId" : personId
	};
	$.ajax({
		url : "/claimcar/check/dropPersonLoss.ajax",
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		success : function(jsonData) {
			if (eval(jsonData).status == "200") {
				// layer.alert("删除成功！");
			}
		}
	});
}

/**增加人伤损失项*/
function addPersonItem(nodeCode) {
	var $tbody = $("#personLossTbody");
	var personSize = $("#personLossSize").val();// 财产项条数
//	var personSize = parseInt($personLossSize.val(), 10);
	var registNo = $("#registNo").val();
	var params = {
		"personSize" : personSize,
		"registNo" : registNo,
		"nodeCode" : nodeCode
	};
	var url = "/claimcar/check/addPersonItem.ajax";
	$.post(url, params, function(result) {
		$tbody.append(result);
		$("#personLossSize").val(personSize + 1);
//		$personLossSize.val(personSize + 1);
		reconcileChange();
	});
	initPersonRadio(personSize+1);
	setLossItemType(personSize,2);
}

/**删除人伤损失项
 * @param element
 */
function delPersonItems(element) {
	var index = $(element).attr("name").split("_")[1];// 下标
	var proposalPrefix = "checkPersonVos";
	var $parentTr = $(element).parent().parent();
	var $personLossSize = $("#personLossSize");// 人伤项条数
	var personLossSize = parseInt($personLossSize.val(), 10);// 原人伤项条数
	initPersonRadio(personLossSize-1);
	//var personId = $("#" + "personId" + index).val();
	var personName = "input[name='checkPersonVos[" + index + "].id']";
	var personId = $(personName).val();
	if (personId == "") {
		// 来源id为空，可以删除
		$personLossSize.val(personLossSize - 1);// 删除一条
		delTr(personLossSize, index, "delPersonItem_", proposalPrefix);
		$parentTr.remove();
	} else {
		// id不为空，后台删除
		$personLossSize.val(personLossSize - 1);// 删除一条
		delTr(personLossSize, index, "delPersonItem_", proposalPrefix);
		$parentTr.remove();
		dropPersonLoss(personId);
	}
}

/*** 人伤title*/
$("#person_title").click(function() {
	var person_cont = $("#person_cont");
	if (person_cont.css("display") == "none") {
		person_cont.show();
		$(this).removeClass('table_close');
	} else {
		person_cont.hide();
		$(this).addClass('table_close');
	}
});

$("input[name='checkVo.damageTypeCode']").click(function(){
	if($(this).val()=="01"){
		$("input[name='checkVo.damageCode']").val("DM01");
	}
});

/**
 * 查勘类别-->公估查勘-->公估费
 */
//$("#checkClass").change(function(){
//	var meFee=$("#mediaryFee");
//	if($(this).val()=="0"){
//		meFee.hide();
//	}else{
//		meFee.show();
//	}
//});

//公估费(互碰自赔--》公估费的险别只有交强险)
function kindSelect(status){
	var claimSelf = $("input[name='checkVo.isClaimSelf']:checked");
	if(status!=0){
		// 点击互碰自赔，校验是否是单商业报案， 是-->不能点击是
		var params = {"registNo" : $("#registNo").val()};
		if(claimSelf.val()=="1"){
			$.ajax({
				url : "/claimcar/check/policyInfoValid.ajax",
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				data : params, // 要传递的数据
				async : false,
				success : function(jsonData) {
					if (eval(jsonData).status == "200") {
						if(eval(jsonData).data!="ok"){
							var isc=$("input[name='checkVo.isClaimSelf'][value='0']");
							isc.prop("checked",true);
							layer.alert(eval(jsonData).data);
						}
					}
				}
			});
		}
	}
	
	var loChKi=$("select[name='lossChargeVo.kindCode']");
	var daN=$("#damageName");
	if(claimSelf=="1"){
		loChKi.find("option[value='']").removeAttr("disabled");
		loChKi.find("option[value='BZ']").removeAttr("disabled");
		loChKi.find("option[value!='BZ']").attr("disabled","disabled");
	}else{
		loChKi.find("option[value='BZ']").attr("disabled","disabled");
		loChKi.find("option[value!='BZ']").removeAttr("disabled");
		
		if(daN.val()=="DM02"){
			loChKi.val("F");
		}else if(daN.val()=="DM03"){
			loChKi.val("L");
		}else if(daN.val()=="DM04"){
			loChKi.val("G");
		}else if(daN.val()=="DM05"){
			loChKi.val("Z");
		}
	}
}

/**是否重大赔案上报*/
$("input[name='checkVo.majorCaseFlag']").click(function (){
	var clT=$("#claimTexts");
	if($(this).val()=="1"){
		clT.show();
	}else{
		clT.hide();
	}
});
/**是否免责情形*/
function changeNoDuty(){
	var noDutyFlag = $("input[name='checkVo.noDutyFlag']:checked").val();
	if(noDutyFlag=="1"){
		$("#noDutyReason").show();
	}else{
		
		$("#noDutyReason").hide();
	}
}

function before() {
	//更新报案出险经过说明
	updateDangerRemark($("#registNo").val(),$("#dangerRemark").val());
	
	// 查勘基本信息
	if (!checkInfoValid()) {
		return false;
	}
	
	if(!checkSubrogation()){
		return false;
	}
	
	// 暂存校验
	if (!saveValids()) {
		return false;
	}
	// 查勘时间校验
	if(!checkReportTime()){
		return false;
	}
	if(!vinNoFlag){
		if(!window.confirm("VIN码不符合校验规则，请提交电子联系单进行特批处理!")){
			return false;
		}
	}
	//后台校验，，暂存车损是否处理，责任比例之和
	var params = $("#saveMain").serialize();
	var returnflag =true;
	$.ajax({
		url : "/claimcar/check/validCheck.do", // 后台校验
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
	
	if(!returnflag){
		return false;
	}
}

//代为案件
function checkSubrogation(){
	if($("input[name='checkVo.isSubRogation']:checked").val() =="1"){
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
		var items =$("#subrogationCarTable select[name $='serialNo']");
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
	}
	
	return true;
}


/**
 * 查勘暂存,提交
 */
function checkSave(saveType) {
	if (checkUnallowedChars()) {
		layer.alert("人员伤亡姓名中不允许录入半角空格、圆角空格、圆角斜杠、半角斜杠、半角反斜杠、圆角反斜杠、半角#、圆角#、￥、圆角＄、半角$、半角星号*、圆角星号×、半角&、圆角＆、中文冒号：、英文冒号:，请知悉！");
		return false;
	}
	
	$("input[name='saveType']").val(saveType);
	$("#saveMain").submit();
}

function after(result) {
	var text = eval(result).statusText;
	if(!isBlank(text)&&text!=0){//返回代位ID
		$("input[name='subrogationMain.id']").val(text);
	}
	var checkId = eval(result).data;
	var saveType = $("input[name='saveType']").val();
	
	var registNo = $("#registNo").val();
	//保存完成之后异步刷新财产和人伤
	$.ajax({
		url : "/claimcar/check/refreshProp.ajax", //后台刷新
		type : 'post', // 数据发送方式
		data : {"checkId" : checkId,"registNo" : registNo}, // 要传递的数据
		async : false, 
		success : function(htmlData) {// 回调方法，可单独定义
			$("#propLossTbody").empty();
			$("#propLossTbody").append(htmlData);
			return false;
		},
		error : function(htmlData){
			layer.alert("刷新财产失败！");
			return false;
		}
	});
//	var personSize = $("#personLossSize").val();// 人伤项条数
	$.ajax({
		url : "/claimcar/check/refreshPerson.ajax", //后台刷新
		type : 'post', // 数据发送方式
		data : {"checkId" : checkId,"registNo" : registNo}, // 要传递的数据
		async : false, 
		success : function(htmlData) {// 回调方法，可单独定义
			$("#personLossTbody").empty();
			$("#personLossTbody").append(htmlData);
			
			//
			reconcileChange();
//			$("#personLossSize").val(personSize);
			return false;
		},
		error : function(htmlData){
			layer.alert("刷新人伤失败！");
			return false;
		}
	});
	
	// 校验之后，保存或者提交
	if (saveType == "save") {
		layer.confirm('暂存成功！', {
			closeBtn: 1,btn: ['确定'] // 按钮
		}, function(index){
//			window.location.reload();
			layer.close(index);
			location.reload();
		});
	} else {
		initCheckSubmit(checkId);// 查勘提交
	}
}

/**
 * 查勘提交汇总
 */
function initCheckSubmit(checkId) {
	//巨灾
	var dis_one=$("#disaster_one").val();
	var dis_two=$("#disaster_two").val();
	var coinsFlag = $("#coinsFlag").val();
	var isCoinsFlag = $("#isCoinsFlag").val();
	var payrefFlag = $("#payrefFlag").val();
	var damC=$("#damageNames").val();
	if((damC=="DM07"||damC=="DM60"||damC=="DM06"||damC=="DM64"||damC=="DM65")
			&&dis_one==""&&dis_two==""){
		layer.alert("出险原因与巨灾代码相符，请录入巨灾代码！");
		return false;
	}
	
	//如果共保单未实收保费，查勘不能提交
	if(isCoinsFlag == "1" && payrefFlag=="0"){
		layer.alert("共保单尚未实收，查勘不能提交！");
		return false;
	}
	
	var mcf=$("input[name='checkVo.majorCaseFlag']:checked");
	// 暂存成功,创建duty表，总损失金额，刷新立案
	var params = {"registNo" : $("#registNo").val()};
	var returnflag = true;
	$.ajax({
		url : "/claimcar/check/saveSuccess.do", // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(jsonData) {// 回调方法，可单独定义
			var result = eval(jsonData);
			if (result.status == "200") {
				if (Number(result.data) > 50000 && mcf.val()=="0") {
					layer.alert("损失金额超过5万需要大案上报！",{closeBtn: 1},function(index){
//						window.location.reload();
						layer.close(index);
					});
					returnflag = false;
				}
			}
		},
		error : function(jsonData) {
			layer.alert("暂存错误！---");
			returnflag = false;
		}
	});
	if(!returnflag){
		return false;
	}
	
	//1-后台校验，该案件符合互碰自赔条件，是否需要互碰自赔？2-标的车承保承保车上货物险
	var returnData =true;
	$.ajax({
		url : "/claimcar/check/validCheckClaim.do", // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false, 
		success : function(jsonData) {// 回调方法，可单独定义
			var result = eval(jsonData);
			if (result.status == "200") {
				if(result.data !="ok"){
					layer.alert(result.data,{closeBtn: 1},function(index){
//						window.location.reload();
						layer.close(index);
					});
					returnData = false;
				}
			}
		}
	});
	if(!returnData){
		return false;
	}
	
	//提交
	var url = "/claimcar/check/initCheckSubmit.do?checkId="+checkId;
	layer.open({
		type : 2,
		closeBtn: 0, // 不显示关闭按钮
		title : "查勘提交",
		area : [ '75%', '60%' ],
		fix : true, // 不固定
		maxmin : false,
		content : url,
	});
	
//	$.post(url, {"checkId":checkId}, function(result) {
//		// 页面层
//		layer.open({
//			title : "查勘提交",
//			type : 1,
////			closeBtn: 1, // 不显示关闭按钮
//			//skin : 'layui-layer-rim', // 加上边框
//			area : [ '75%', '60%' ], // 宽高
////			async : false,
//			fix : false, // 不固定
//			content : result,
//			yes : function(index, layero) {
//				// $("#PLverifyForm").submit();
//			},
//		});
//	});

}


// 查勘确定提交
function checkSubmit() {
	
	var isEnd = true;
	$("select[name$='scheduledUsercode']").each(function(){
	    if(isBlank($(this).val())){
	    	isEnd = false;
	    }
	 });
	if(!isEnd){
		layer.alert("请确定是否选择指定处理人！");
    	return false;
	}
	
	var flowTaskId = parent.window.getFlowTaskId();
	var flowId = parent.window.getFlowId();
	$("#chMitDiv input[name='sub_flowTaskId']").val(flowTaskId);
	$("#chMitDiv input[name='sub_flowId']").val(flowId);
	
	var params = $("#chMitForm").serialize();
	var load_index = layer.load(0, {time: 300*2000},{shade: 0});
	$.ajax({
		url : "/claimcar/check/checkSubmit.do",
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : true,
		success : function(jsonData) {
			//关闭
			layer.close(load_index);
			if (eval(jsonData).status == "201") {
				viewLoss(eval(jsonData).data,eval(jsonData).statusText);
			}else if(eval(jsonData).status == "200"){
				layer.confirm('报案号:' + eval(jsonData).statusText, {
					closeBtn: 1,
					btn : [ '关闭' ] //按钮
				}, function(index){
					layer.close(index);
					var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
					window.parent.location.reload();//刷新
					window.parent.close(index); // 执行关闭
					//parent.layer.close(index);
				});
			}else{
				layer.alert(eval(jsonData).statusText);
			};
			if(jsonData.datas!=null){
				layer.alert(jsonData.datas.info);
			}
		},
		error : function(jsonData) {
			layer.close(load_index);
			layer.alert(eval(jsonData).statusText);
		},
	});
}

function viewLoss(idList,text){
	var nextUrl = "/claimcar/check/checkNextEdit.do?idList="+idList;
	layer.open({
     	type: 2,
//     	closeBtn: 0,
     	title: "提交成功！报案号："+text,
     	area: ['100%', '100%'],
    	fix: true, // 不固定
     	maxmin: false,
    	content: nextUrl,
    	end : function() {
    		window.parent.location.reload();//刷新
		}
 	});
}

// 暂存校验
function saveValids() {
	//是否现场调解校验
	var reconcileFlag = $("input[name='checkVo.reconcileFlag']:checked").val();
	var persHandleType = $("select[name='checkVo.persHandleType']").val();
	if(Number($("#personLossSize").val())>0&&persHandleType!="10"&&reconcileFlag==null){
		layer.alert("包含人伤,请选择是否现场调解！");
		return false;
	}
	
	// 单车事故(否)
	var singleFlag = $("input[name='checkVo.singleAccidentFlag']:checked").val();
	if (singleFlag=="0"&&Number($("#thridCarItemSize").val())==0) {
		layer.alert("非单车事故，必须录入三者车信息！");
		return false;
	}
	// 单车事故(是)
	if (singleFlag=="1"&&Number($("#thridCarItemSize").val())>0) {
		layer.alert("单车事故，不允许录入三者车信息！");
		return false;
	}
	
	// 财损
	var propEmpty = $("input[name='checkVo.isPropLoss']:checked");
	if (propEmpty.val()=="1"&&Number($("#propLossSize").val())==0) {
		layer.alert("包含财损,请录入财产损失信息！");
		return false;
	}
	// 人伤
	var isPerson = $("input[name='checkVo.isPersonLoss']:checked");
	if (isPerson.val()=="1" && Number($("#personLossSize").val())==0) {
		layer.alert("包含人伤,请录入人伤损失信息！");
		return false;
	}
	// 是否重大赔案上报
	var claimsReport = $("input[name='checkVo.claimsReportFlag']:checked");
	if (claimsReport.val()=="1"&&$("#lossFee").val()==""){
		layer.alert("重大赔案上报，标的车不能损失为空！");
		return false;
	}
	/*// 事故责任对应的比例值只允许向上修改
	var old = $("#oldIndemnityDutyRateValue").val();
	var now = $("#indemnityDutyRateValue").val();
	if (Number(now) < Number(old)) {
		layer.alert("事故责任对应的比例值只允许向上修改！");
		return false;
	}*/
	//单交强报案
	//var idr=$("#indemnityDutyRate").val();
	var cid=$("#ciIndemDuty").val();
	var thCarSize=$("#thridCarItemSize").val();
	var propSize=$("#propLossSize").val();
	if(cid!=""&&cid=="2"&&Number(thCarSize)>0){
		layer.alert("单交强报案，且交强险无责，不允许录入三者车信息！");
		return false;
	}
	if(cid!=""&&cid=="2"&&Number(propSize)>0){
		layer.alert("单交强报案，且交强险无责，不允许录入财产信息！");
		return false;
	}
	//是否现场调解
	var recon=$("input[name='checkVo.reconcileFlag']:checked");
	var perSize=Number($("#personLossSize").val());
	if(recon.val()=="1"&&perSize==0){
		layer.alert("是现场调解，人伤信息至少存在一条！");
		return false;
	}
//	var subRo = $("input[name='checkVo.isSubRogation']:checked");
//	var carSize =$("#subrogationCarSize");//
//	var perSize =$("#subrogationPerSize");//
//	if(Number(subRo.val())==1&&carSize.val()==0&&perSize.val()==0){
//		layer.alert("代位求偿案件，责任对方信息机动车或非机动必须至少有一项录入!");
//		return false;
//	}
	if(!vinNoFlag){
		window.alert("VIN码不符合校验规则，请提交电子联系单进行特批处理。");
	}
	return true;
}

function checkColse(index) {
	layer_close();
	/*var indexs = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	parent.layer.close(indexs);// 执行关闭
*/}

//财产，人伤 损失方名称
function setSelectName(obj, inputId) {
	var valName = $("option:selected", $(obj)).text();
	$("#" + inputId).val(valName);
	//
	var val = $("option:selected", $(obj)).val();
	setLossItemType(inputId.split("_")[1], val);
}

function setLossItemType(idx, val) {
	var name = "select[name='checkPersonVos[" + idx + "].personProp']";
	if (Number(val) != 1) {// 地面或者三者车
		$(name).find("option[value='1']").removeAttr("disabled");
		$(name).find("option[value='2']").attr("disabled", "disabled");
		$(name).find("option[value='3']").attr("disabled", "disabled");
	} else {
		$(name).find("option[value='1']").attr("disabled", "disabled");
		$(name).find("option[value='2']").removeAttr("disabled");
		$(name).find("option[value='3']").removeAttr("disabled");
	}
}

//事故责任比例(供layer.open页面使用)
function getIndemnityDutyRate() {
	return $("#indemnityDutyRate").val();
}
// 是否重大赔案上报(供layer页面使用)
function getReportFlag() {
	var maf=$("input[name='checkVo.majorCaseFlag']:checked");
	return maf.val();
}

function getClaimSelf(){
	return $("input[name='checkVo.isClaimSelf']:checked").val();
}

function getDamageCode(){
	return $("#damageNames").val();
}

//
function getFlowTaskId() {
	var flowTaskId = $("#flowTaskId").val();
	return flowTaskId;
}
function getFlowId() {
	var flowId = $("#flowId").val();
	return flowId;
}

function getSingle(){
	var single=$("input[name='checkVo.singleAccidentFlag']:checked");
	return single.val();
}
function getAccType(){
	return $("#damType").val();
}

//巨灾
function disaster_ones(){
	var disaster=$("#disaster_one").val();
	var name =$("#disaster_one").find("option[value='"+disaster+"']").text();
	$("#disaster_oneName").val(name.split("-")[1]);
	var disasterOneCode = name.split("-")[0];
	clearDisasterTwo();
	initDisasterTwoInfo(disasterOneCode);
}
function disaster_twos(){
	var disaster=$("#disaster_two").find("option:selected").text();
	$("#disaster_twoName").val(disaster.split("-")[1]);
}
function disTwChange(){
	var disaster = $("#disaster_two");
	attrDisaster(disaster, '002');
	attrDisaster(disaster, '004');
	attrDisaster(disaster, '006');
	attrDisaster(disaster, '007');
}

function attrDisaster(element,val){
	var option = "option[value='"+val+"']";
	$(element).find(option).attr("disabled","disabled");
}

function displayDisasterTwo() {
	var code = $("input[name='lastSelectCode']").val();
	var name = $("#disaster_twoName").val();
	if (name != null && name.length > 0) {
		$("#disaster_two").append("<option value='"+code+"' selected>"+code+"-"+name+"</option>");
	}
}

/**
 * 当巨灾一级代码发生变化时，将已选择的巨灾二级代码信息清空
 */
function clearDisasterTwo() {
	$("#disaster_two").val("");
	$("#disaster_two").text("");
	$("#disaster_twoName").val("");
}

function initDisasterTwoInfo(disasterOneCode) {
	$("#disaster_two").empty();
	$("#disaster_two").append("<option value=''></option>");
	$.ajax({
		url : "/claimcar/regist/getDisasterTwoInfo.ajax?disasterOneCode=" + disasterOneCode, // 从再保获取巨灾二级代码
		type : 'post', // 数据发送方式
		success : function(ajaxResult) {// 回调方法，可单独定义
			if (ajaxResult.status == 200 && ajaxResult.data != null) {
				for (var i = 0; i < ajaxResult.data.length; i++) {
					var codeinfo = ajaxResult.data[i];
					var codeArr = codeinfo.split("-");
					$("#disaster_two").append("<option value='"+codeArr[0]+"'>"+codeinfo+"</option>");
				}
			}
		}
	});
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

function initCheckBox() {
	var ischeck = $("input[name$='isCheck']");
	ischeck.each(function(index) {
		if (ischeck.eq(index).val() == "1") {
			ischeck.eq(index).attr("checked", "checked");
		}
	});
}

//刷新公估费的收款人选项
function refreshPayCustom(registNo) {
	var params = {"registNo" : registNo};
	$.ajax({
		url : '/claimcar/check/refreshPayCustom.ajax',
		data : params,
		type : 'post',
		async : true,
		success : function(result) {// json是后端传过来的值
			$.each(eval(result).data, function(key, value) {
				options = options + "<option value='"+key+"'>"+value+"</option>";
			});
			var propItems =$("#lossChargeTr select[name='lossChargeVo.receiver']");
			$(propItems).each(function(){
				var selected = $(this).val();
				$(this).find("option").remove();//先删除
				$(this).append(options);//在重新增加
				$(this).find("option[value="+selected+"]").attr("selected","selected");
			});
		},
	});
}


// ------------------------------------------

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

// 风险提示
function historyDanger(carId) {
	var url = "/claimcar/checkcar/CheckThirdInfo.ajax?carId=" + carId;
	layer.open({
		type : 2,
		title : "历史出险",
		shadeClose : true,
		scrollbar : false,
		skin : 'yourclass',
		area : [ '80%', '60%' ],
		content : url,
		end : function() {
		}
	});
}

function initNextClass() {
	setNextClass($("#disaster_title"));
	setNextClass($("#payInfo_title"));
	setNextClass($("#claimDeduct_title"));
	setNextClass($("#checkExtInfo"));
}

/**
 * setNextClass
 * @param element
 */
function setNextClass(element) {
	$(element).next().hide();
	$(element).addClass('table_close');
}

function initDutyRate(policyType){
	if(policyType=="1"){//单交强
		$("#dutyRateDiv_tr1").hide();
		$("#dutyRateDiv_tr2").show();
	}
	if(policyType=="2"){//单商业
		$("#dutyRateDiv_tr1").show();
		$("#dutyRateDiv_tr2").hide();
	}
	if(policyType=="3"){//交强商业
		$("#dutyRateDiv_tr1").show();
		$("#dutyRateDiv_tr2").show();
	}
}

function per_change(obj,idx){
	var name = "input[name='checkPersonVos["+idx+"].idNo']";
	var recon = $("input[name='checkVo.reconcileFlag']:checked");
//	layer.alert($(obj).val()+"---"+$(recon).val());
	if($(obj).val()=="1"&&$(recon).val()=="1"){
		$(name).removeAttr("datatype");
		$(name).attr("datatype","idcard");
	}
	if($(obj).val()!="1"&&$(recon).val()=="1"){
		$(name).removeAttr("datatype");
		$(name).attr("datatype","*");
	}
	if($(obj).val()=="1"&&$(recon).val()=="0"){
		$(name).removeAttr("datatype");
		$(name).attr("datatype","*0-0|idcard");
	}
	if($(obj).val()!="1"&&$(recon).val()=="0"){
		$(name).removeAttr("datatype");
	}
}

function reconcileChange(){
	var obj = $("input[name='checkVo.reconcileFlag']:checked");
	if($(obj).val()=="1"||$(obj).val()==1){
		
		$("input[name$='personName']").each(function(){
		    $(this).removeAttr("datatype").attr("datatype","*");
		});
		
		var size = parseInt($("#personLossSize").val(),10);
		/*for (var i = 0; i < size; i ++) {
			var name = "input[name='checkPersonVos["+i+"].lossFee']";
			$(name).removeAttr("datatype").attr("datatype","amount");
		};*/
		
//		$("input[name$='lossFee']").each(function(){
//			$(this).removeAttr("datatype").attr("datatype","amount");
//		});
	}else{
//		$("input[name$='idNo']").each(function(){
//			setQtip($(this));
//		});
		$("input[name$='personName']").each(function(){
			setQtip($(this));
		});
//		$("input[name$='lossFee']").each(function(){
//			setQtip($(this));
//		});
		
		var size = parseInt($("#personLossSize").val(),10);
		/*for (var i = 0; i < size; i ++) {
			var name = "input[name='checkPersonVos["+i+"].lossFee']";
			setQtip($(name));
		};*/
	}
	$("input[name$='idNo']").each(function(){
//	    $(this).removeAttr("datatype").attr("datatype","*");
		var idx = $(this).attr("name").split("[")[1].split("]")[0];
		var identifyTypeElement = $("select[name='checkPersonVos["+idx+"].identifyType']");
		per_change(identifyTypeElement,idx);
	});
	selectMust(obj);
}
function reconcileClick(obj){
	 var domName = $(obj).attr('name');
     var $radio = $(obj);
     if("undefined" == typeof $radio.data('waschecked')){
    	 $("input:radio[name='" + domName + "']").data('waschecked',false);
     }
     // if this was previously checked
     if ($radio.data('waschecked') == true){
         $radio.prop('checked', false);
         $("input:radio[name='" + domName + "']").data('waschecked',false);
         //$radio.data('waschecked', false);
     } else {
         $radio.prop('checked', true);
         $("input:radio[name='" + domName + "']").data('waschecked',false);
         $radio.data('waschecked', true);
     }
     reconcileChange();
}

function changePersHandleType(obj){
	var persHandleType = $(obj).val();
	//如果人伤无需赔付，则是否现场调解置空,人伤信息也无需必录
	if(persHandleType == "10"){
		var element = $("input[name='checkVo.reconcileFlag']:checked");
		var domName = element.attr('name');
		element.prop('checked', false);
        $("input:radio[name='" + domName + "']").data('waschecked',false);
        reconcileChange();
	}
}

function selectMust(obj){
	var radioVal = $(obj).val();
	if(radioVal==1 || radioVal=="1"){
		$("select[name$='personProp']").each(function(){
		    setSelectMust($(this));
		});
		$("select[name$='personSex']").each(function(){
		    setSelectMust($(this));
		});
		$("select[name$='identifyType']").each(function(){
		    setSelectMust($(this));
		});
		$("select[name$='personPayType']").each(function(){
		    setSelectMust($(this));
		});
		$("select[name$='checkDispose']").each(function(){
		    setSelectMust($(this));
		});
	}else{
		$("select[name$='personProp']").each(function(){
		    setQtip($(this));
		});
		$("select[name$='personSex']").each(function(){
			setQtip($(this));
		});
		$("select[name$='identifyType']").each(function(){
			setQtip($(this));
		});
		$("select[name$='personPayType']").each(function(){
		    setQtip($(this));
		});
		$("select[name$='checkDispose']").each(function(){
			setQtip($(this));
		});
	}
}

/**
 * setQtip
 * @param element
 */
function setQtip(element){
	$(element).removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
}

/**
 * setSelectMust
 * @param element
 */
function setSelectMust(element){
	$(element).attr("datatype","selectMust");
}


function checkReportTime(){
	var rt = $("#reportTime").val();
	var reportTime = new Date(rt);
	reportTime.setHours(reportTime.getHours()-14);
	var ct = $("#checkTimeMainBasic").val();
	ct = ct.replace(/-/g,'/');
	var checkTime = new Date(ct);
	if(reportTime.getTime()>checkTime.getTime()){
		layer.alert("查勘日期不能早于报案日期 ！");
		return false;
	}
	return true;
}

var PhotoVerify_index = null;
function openPhotoVerify(nodeCode){
	var mainId = $("#checkId").val();
	var registNo = $("#registNo").val();
	var url = "/claimcar/defloss/openPhotoVerify.do?registNo=" + registNo+"&mainId="+mainId+"&nodeCode="+nodeCode;
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


/**
 * 展示查勘员定损指标信息
 */
function showCheckIndex(checkerCode) {
	console.log('checkerCode', checkerCode);
	var url="/claimcar/check/checkIndexInit.do?userCode="+checkerCode;
	layer.open({
		type : 2,
		title : '查勘定损指标信息',
		shade : false,
		area: ['80%', '50%'],
		content :url,
		end : function() {

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
	var hasUnallowedChars = false;
	$("input[name^='checkPersonVos'][name$='personName']").each(function() {
		var arr = [];
		var curValue = $(this).val();
		if (curValue != undefined && curValue.length != 0) {
			arr = curValue.split(reg);
		}
		var newValue = arr.join('');
		if (newValue.length < curValue.length) {
			hasUnallowedChars = true;
			return false;// 退出循环
		}
	});
	return hasUnallowedChars; // 方法返回值
}