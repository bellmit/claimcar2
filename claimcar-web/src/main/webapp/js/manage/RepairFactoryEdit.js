/**
 * 
 */
$(function() {
	var powerFlag = $("#powerFlag").val();
	if(powerFlag != "1"){//没有权限
		$("#phoneTable input").attr("readOnly", true);
		$("#phoneTable button").prop("disabled", true);
	}
	var Index = $("#index").val();
	if (Index == "0") {
		$("input").attr("readOnly", true);
		$("textarea").attr("readOnly", true);
		$("button").prop("disabled", true);
		$("select").prop("disabled", true);
		$("a").prop("disabled", true);
		$("#repairHideOrShow").hide();
	}

	$.Datatype.rate = /^(0\.\d{0,4}|1(\.0{0,4})?)$/;
	$.Datatype.sum = /^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/;// 金额，
	$.Datatype.selectMust = function(gets, obj, curform, regxp) {
		var code = $(obj).val();
		if (isBlank(code)) {
			return false;
		}
	};
	$("select[name='repairFactoryVo.comCode']").attr("datatype", "selectMust");
	  $("select[name^='agentname']").each(function(){
		  $(this).select2();
		   
		  });
	//  initInsuredInfoSelect2("userCodeAjax","J10000006",1);
	  
	  
});

// /** 增加 */
// function addRepairItem() {
// 	var $tbody = $("#reTbody");
// 	var $repairSize = $("#repairSize");// 条数
// 	var repair = parseInt($repairSize.val(), 10);
// 	var params = {
// 		"repairSize" : repair,
// 	};
// 	var url = "/claimcar/manager/addRepairItem.ajax";
// 	$.post(url, params, function(result) {
// 		$tbody.append(result);
// 		$repairSize.val(repair + 1);
// 	});
// }

/** 增加手机号 */
function addPhoneItem() {
	var $agentSize = $("#agentSize");// 修理厂手机号码条数
	var agentSize = parseInt($agentSize.val());
	if(agentSize > 0){
		layer.alert("已经有代理人信息，不能添加推送修手机号码!");
		return false;
	}
	var $tbody = $("#phoneTbody");
	var $phoneSize = $("#phoneSize");// 条数
	var phone = parseInt($phoneSize.val(), 10);
	var params = {
		"phoneSize" : phone,
	};
	var url = "/claimcar/manager/addPhoneItem.ajax";
	$.post(url, params, function(result) {
		$tbody.append(result);
		$phoneSize.val(phone + 1);
	});
}

//删除可修品牌Tr
function delRepairTr(element) {
	var index = $(element).attr("name").split("_")[1];// 下标
	var proposalPrefix = "repairBrandVo";
	var $parentTr = $(element).parent().parent();
	var $repairSize = $("#repairSize");// 财产项条数
	var size = parseInt($repairSize.val(), 10);// 原财产项条数

	$repairSize.val(size - 1);// 删除一条
	delTr(size, index, "delRepairTr_", proposalPrefix);
	$parentTr.remove();
}

/*删除代理人*/
function delPhoneTr(obj){
	var size = parseInt($("#phoneSize").val(),10);
	var index = $(obj).attr("name").split("_")[1]; //下标
	var btnPrefix = "delPhonetr_";
	var  inputPrefix = "repairPhoneVo";
	var $parentTr = $(obj).parent().parent();  //提前获取Tr
	
	$("#phoneSize").val(size-1);// 删除一条
	delTr(size, index, btnPrefix, inputPrefix);
	$parentTr.remove();
}

$("#re").click(function() {
	$(".input-text").val("");
	$("select").val("1");
	$(".textarea").val("");
});

function setSelectCode(obj, idx) {
	var selectName = "input[name='repairBrandVo[" + idx + "].brandCode']";
	var hideName = "input[name='repairBrandVo[" + idx + "].brandName']";
	$(selectName).val($(obj).val().split(",")[0]);
	var valName = $("option:selected", $(obj)).text().split("-")[1];
	$(hideName).val(valName);
}

//异步请求校验
$("#factoryCode").change(function() {
	if ($("#factoryCode").val() == "") {
		return;
	}
	var id=$("#repairId").val();
	if(id==undefined || id=="undefined"){
	   id=null;
	}
	$.ajax({
		url : "/claimcar/manager/repairFactoryVerify.do",
		type : "post",
		data : {
			"factoryCode" : $("#factoryCode").val(),
			"id":id
		},
		dataType : "json",
		success : function(result) {
			var $result = result.data;
			if ($result == "1") {
				$("#factoryCode").removeClass("Validform_error");
				return;
			} else {
				$("#factoryCode").val("");
				$("#factoryCode").addClass("Validform_error");
				layer.msg("该修理厂代码已经存在！");
				return;
			}
		}
	});
});

$("#submit").click(function() {
	var damageAreaCode = $("#damageAreaCode_lv3").val();
	if (isBlank(damageAreaCode)) {
		layer.msg('请录入修理厂的省市区', {
			time : 1500, //20s后自动关闭
		});
		return false;
	}
	/*  if(warmTip()){
		return false;
	}  */
	//发送ajaxEdit
	var ajaxEdit = new AjaxEdit($('#form'));
	ajaxEdit.beforeSubmit = saveBeforeCheck;
	ajaxEdit.targetUrl = "/claimcar/manager/repairFactorySava.do";
	/*ajaxEdit.afterFailure = function(result){
		
	}*/
	ajaxEdit.afterSuccess = function(result) {
		if(result.data == "2"){
			layer.alert(result.statusText);
		}else if(result.statusText == ""){
			layer.alert("修理厂代码重复！");
		}else{
			layer.alert("保存成功！", function() {
				//closeAlert();
				//window.location.reload();
				window.location.href = "/claimcar/manager/factoryId.do?Id=" + result.statusText + "&index=1"; 
			});
		}
	};
	//绑定表单
	ajaxEdit.bindForm();
	$("#form").submit();
});

function closeAlert() {
	var index = parent.layer.getFrameIndex(window.name);//获取窗口索引
	parent.layer.close(index);// 执行关闭
}

function warmTips() {
	var telNo = $("#telNo").val();
	// var factoryName=$("#factoryName").val();
	var agentCode = $("#agentCode").val();

	if (!isBlank(agentCode)) {
		var options = "";
		var params = {
			"agentCode" : agentCode,
		};
		var url = "/claimcar/manager/searchPersonalInsurInfo.do";
		$.post(url, params, function(result) {
			$.each(eval(result).data, function(key, value) {
				options = options + "<option value='" + key + "'>" + key + "-"
						+ value + "</option>";
			});
			var code = $("#personalInsurCode");
			var selected = code.val();
			code.find("option").remove();//先删除
			code.append(options);//在重新增加
			code.find("option[value=" + selected + "]").attr("selected",
					"selected");

			//$("#personalInsurCode").val(result);
			//$("#personalInsurCode").attr("dataSource",result);
		});
		if (isBlank(telNo)) {
			layer.alert("是否要推送修？如果是，建议在“修理厂电话”录入手机号!");
		}
	}
}

//增加代理人
function addAgentItem() {
	var $phoneSize = $("#phoneSize");// 修理厂手机号码条数
	var phoneSize = parseInt($phoneSize.val());
	if(phoneSize > 0){
		layer.alert("已经有推送修手机号码，不能添加代理人信息!");
		return false;
	}
	var factoryid = $("input[name='repairFactoryVo.id']").val();
/*	var factoryCode = $("input[name='repairFactoryVo.factoryCode']").val();
	var factoryName = $("input[name='repairFactoryVo.factoryName']").val();*/
	
	if(factoryid == null || factoryid == ''){
		layer.alert("请先维护修理厂信息并保存！");
		return;
	}
	
	//增加弹出框
	var url = '/claimcar/manager/showPrpLAgentFactory.ajax';
	index1 = layer.open({
		type : 2,
		area : [ '900px', '345px' ],
		fix : false, // 不固定
		maxmin : false,
		shade : [0.3],
		scrollbar : true,
		title : "添加代理人",
		content : url,
	});

}



function Choose(agentName,agentCode) {
	var factoryid = $("input[name='repairFactoryVo.id']", parent.document).val();
	var factoryCode = $("input[name='repairFactoryVo.factoryCode']", parent.document).val();
	var factoryName = $("input[name='repairFactoryVo.factoryName']", parent.document).val();
	if(factoryid == null || factoryid == ''){
		layer.alert("请先维护修理厂信息并保存！");
		return;
	}
	var $tbody = $("#agentTbody",parent.document);
	var $agentSize = $("#agentSize", parent.document);// 条数
	var agentSize = parseInt($agentSize.val(), 10);
	var params = {
		"agentSize" : agentSize,
		"factoryid" : factoryid,
		"factoryCode" : factoryCode,
		"factoryName" : factoryName,
		"agentName" : agentName,
		"agentCode" : agentCode,
	};
	var url = "/claimcar/manager/addAgentFactoryItem.ajax";
	$.post(url, params, function(result) {
		$tbody.append(result);
		$agentSize.val(agentSize + 1);
		$tbody.find('tr:last').find('select').select2();
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index);
		//parent.layer.closeAll();
		
	});
	

}

//增加修理品牌
function addRepairItem(agentId) {
    var factoryid = $("input[name='repairFactoryVo.id']").val();

    if(factoryid == null || factoryid == ''){
        layer.alert("请先维护修理厂信息并保存！");
        return;
    }

    //增加弹出框
    var url = '/claimcar/manager/showRepair.ajax';
    index1 = layer.open({
        type : 2,
        area : [ '900px', '345px' ],
        fix : false, // 不固定
        maxmin : false,
        shade : [0.3],
        scrollbar : true,
        title : "添加可修品牌",
        content : url,
    });
}

/**
 * 选择修理品牌
 * @param agentName
 * @param agentCode
 */
function chooseBrand(brandName,brandCode) {
    console.log(brandName  +  brandCode);
    // var brandId = $("input[name='repairBrandVo.id']", parent.document).val();
    // var brandCode = $("input[name='repairBrandVo.brandCode']", parent.document).val();
    // var brandName = $("input[name='repairBrandVo.brandName']", parent.document).val();
    // if(brandId == null || brandId == ''){
    //     layer.alert("请先维护修理厂信息并保存！");
    //     return;
    // }
    var $tbody = $("#reTbody",parent.document);
    var $repairSize = $("#repairSize", parent.document);// 条数
    var repairSize = parseInt($repairSize.val(), 10);
    var arrayNaAndCo=new Array();//品牌名称与品牌代码
    $tbody.find("input[name$='brandName']").each(function(){
    	arrayNaAndCo.push($(this).val()+"_"+$(this).parent().parent().find("input[name$='brandCode']").val());
    	console.log($(this).val()+"_"+$(this).parent().parent().find("input[name$='brandCode']").val());
    });
    var flag='0';
    if(arrayNaAndCo.length>0){
    	for(var i=0;i<arrayNaAndCo.length;i++){
    		var array=arrayNaAndCo[i].split("_");
    		if(array!=null && array.length>1){
    			if(brandName==array[0] && brandCode==array[1]){
    				flag='1';
    				break;
    			}
    		}
    	}
    }
    if(flag=='1'){
    	layer.alert("该可修品牌已存在，不能重复添加！");
    	return false;
    }
    
    
    var params = {
        "repairSize" : repairSize,
        // "brandId" : brandId,
        "brandCode" : brandCode,
        "brandName" : brandName,
        // "agentName" : agentName,
        // "agentCode" : agentCode
    };
    var url = "/claimcar/manager/addBrandItem.ajax";
    $.post(url, params, function(result) {
        $tbody.append(result);
        $repairSize.val(repairSize + 1);
        $tbody.find('tr:last').find('select').select2();
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index);
    });


}

function agentCodeChange(obj, index) {
	var agentCode =$(obj).val();
	var agentName = $("option:selected", $(obj)).text().split("-")[1];
	$("input[name='agentFactoryVo[" + index + "].agentCode']").val(agentCode);
	$("input[name='agentFactoryVo[" + index + "].agentName']").val(agentName);
}

/*删除代理人*/
function delAgentItem(obj){
	var size = parseInt($("#agentSize").val(),10);
	var index = $(obj).attr("name").split("_")[1]; //
	var btnPrefix = "delAgentItem_";
	var  inputPrefix = "agentFactoryVo";
	var $parentTr = $(obj).parent().parent();  //提前获取Tr
	
	$("#agentSize").val(size-1);
	delTr(size, index, btnPrefix, inputPrefix);
	$parentTr.remove();
}


function saveAgentInfo(){
	layer.alert("可以哟");
}

function showInsuredFac(id,agentCode){
	//var id = $("input[name='prpdIntermMainVo.Id']").val();
	if(id == null || id == ''){
		layer.alert("请先维护代理人信息并保存！");
		return;
	}
	var url = '/claimcar/manager/showInsuredInfo.do?Id=' + id+'&agentCode='+agentCode;
	layer.open({
		type : 2,
		area : [ '900px', '345px' ],
		fix : false, // 不固定
		maxmin : false,
		shade : [0.3],
		scrollbar : true,
		skin : 'yourclass',
		title : "维护被保险人",
		content : url,
		success : function(layerObj,index) {
		/*	debugger;
			layerObj.find('iframe').contents().find('input[type=hidden][id^=insuredId]').each(function(x,obj){
				$(obj).select2({
					ajax : {
						url : "/claimcar/manager/getUserCode.do",
						dataType : 'json',
						delay : 250,
						data : function(params) {
							var userInfo = encodeURI(params);
							return {
								userInfo : userInfo,
								agentCode :agentCode
							};
						},
						results : function(data) {
							var resultData = eval(data);
							return {
								results : resultData
							};
						},
					
					},
					minimumInputLength : 1
				// 最少输入长度
				});
				//$("#s2id_insuredId"+x).val(layerObj.find('iframe').contents().find('input[type=text][id=insuredCode'+x+']').val());
				console.log(layerObj.find('iframe').contents().find('input[type=text][id=insuredCode'+x+']').val());
			});
		
			console.log(layerObj, index);*/
		}
	});
}


function addInsuredItem(agentCode,agentId){
	//增加弹出框
	var url = '/claimcar/manager/showInsured.ajax?agentCode='+agentCode+"&agentId="+agentId;
	layer.open({
		type : 2,
		area : [ '850px', '300px' ],
		fix : false, // 不固定
		maxmin : false,
		shade : [0.3],
		scrollbar : true,
		title : "添加被保险人",
		content : url,
	});
	
	/*var $tbody = $("#InsuredTbody");
	var $insuredSize = $("#InsuredSize");// 条数
	var insuredSize = parseInt($insuredSize.val(), 10);
	
	var params = {
		"insuredSize" : insuredSize,
		"agentCode"  :  agentCode,
		"agentId"    :  agentId,
		"agentName"    :  agentName,
	};
	var url = "/claimcar/manager/addInsuredFactoryItem.ajax";
	$.post(url, params, function(result) {
		$tbody.append(result);
		$insuredSize.val(insuredSize + 1);
		//$tbody.find('tr:last').find('select').select2();
		initInsuredInfoSelect2("insuredId"+(insuredSize),agentCode,2);
	});*/
}



function delInsuredItem(obj){ //删除被保险人
	var size = parseInt($("#InsuredSize").val(),10);
	var index = $(obj).attr("name").split("_")[1]; //
	var btnPrefix = "delInsuredItem_";
	var  inputPrefix = "insuredFactoryVo";
	var $parentTr = $(obj).parent().parent();  //提前获取Tr
	
	$("#InsuredSize").val(size-1);
	delTr(size, index, btnPrefix, inputPrefix);
	$parentTr.remove();
}

function insuredCodeChange(obj,index){
	var insuredCode =$(obj).val();
	var insuredName = $(obj).prev().text().split("-")[1];//$("option:selected", $(obj)).text().split("-")[1];
	$("input[name='insuredFactoryVo[" + index + "].insuredCode']").val(insuredCode);
	$("input[name='insuredFactoryVo[" + index + "].insuredName']").val(insuredName);
}

function initInsuredInfoSelect2(idName,agentCode,minimumInputLength) {
	$("#" + idName).select2({
		ajax : {
			url : "/claimcar/manager/getUserCode.do",
			dataType : 'json',
			delay : 250,
			data : function(params) {
				var userInfo = encodeURI(params);
				return {
					userInfo : userInfo,
					agentCode :agentCode
				};
			},
			results : function(data) {
				var resultData = eval(data);
				return {
					results : resultData
				};
			},
		
		},
		minimumInputLength : minimumInputLength
	// 最少输入长度
	});
	return 1;
}


function saveBeforeCheck(){
	var flags = true;
	if($("#phoneSize").val() > 0){
		var repairPhoneAry = new Array();
		$("#phoneTbody [name$='phoneNumber']").each(function(){
			repairPhoneAry.push($(this).val());
		});
		var phoneArySort = repairPhoneAry.sort();
		for(var i=0;i < phoneArySort.length;i++){
			if (phoneArySort[i]==phoneArySort[i+1]){
				flags = false;
				layer.alert("推送修手机号码:"+phoneArySort[i]+"不能重复,请重新输入!");
			}
		}
	}
    return flags;
}