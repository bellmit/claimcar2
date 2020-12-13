var layIndex;
//var feeStander = new Array();
//$(function(){
//	var intermStanders = $("#intermStanders").val();
//	if(!isBlank(intermStanders)){
//		var str = intermStanders.split(",");
//		for (var k=0;k<str.length ;k++ ){
//			var keyValues = str[k].split("-");
//			var key = keyValues[0];
//			var value = keyValues[1];
//			feeStander[key] = value;
//		}
//	}
//	
//});

function initSubRisk(element){
	var kindCodes =getChargeTypes("subRisk");
	var registNo = $("#registNo").val();
	var params = {
		"registNo" : registNo,"kindCodes":kindCodes
	};
	$.ajax({
		url : "/claimcar/defloss/initSubRisk.ajax",
		type : "post",
		data : params,
		async: false,
		success : function(htmlData){
			//initPopover(element, htmlData);
			layIndex=layer.open({
			    type: 1,
			    skin: 'layui-layer-rim', //加上边框
			    area: ['230px', '270px'], //宽高
			    content: htmlData
			});
		}
	});
}

function closePop(){
    layer.close(layIndex);
}
/**
 * 增加一行或多行附加险
 */
function setSubRisk(){
	var $tbody = $("#subRiskTbody");
	var indexSeriNo = $("#subRiskTab [name='subRiskCheckFlag']:checked");
	var param = "";
	var count = indexSeriNo.length;
	for(var i = 0 ; i < indexSeriNo.length; i++){
		if(i == 0){
			param = indexSeriNo.val();
		} else {
			param = $(indexSeriNo[i]).val() + "," + param; 
		}
	}
	var registNo = $("#registNo").val();
	var $subRiskSize =$("#subRiskSize") ;//附加险条数
	var deviceStr = $("#deviceMap").val();
	var size = parseInt($subRiskSize.val(),10);
	var params = {"size":size,"registNo":registNo,"deviceStr" :deviceStr,"kindCodes":param};
	var url = "/claimcar/defloss/loadSubRiskTr.ajax";
	$.post(url,params, function(result){
		$tbody.append(result);
		$subRiskSize.val(size + count );//重新附加险条数
	});
	closePop();
}
/**
 * 删除附加险
 * @param element
 */
function delSubRisk(element){
	var index = $(element).attr("name").split("_")[1];//下标
	var proposalPrefix="lossCarMainVo.prpLDlossCarSubRisks";
	var $parentTr = $(element).parent().parent();
	var $subRiskSize =$("#subRiskSize") ;//附加险条数
	var subRiskSize = parseInt($subRiskSize.val(),10);//原附加险条数
    var	subRiskFee=$("#subRiskTbody input[name='lossCarMainVo.prpLDlossCarSubRisks["+index +"].subRiskFee']").val();//删除的附加险金额
    var sumSubRiskFee= $("#sumSubRiskFee").val();//附加险总金额
    if(subRiskFee == "" || isNaN(subRiskFee)){
		subRiskFee = parseFloat(0.00);
	}
  $("#sumSubRiskFee").val( parseFloat(sumSubRiskFee)-parseFloat(subRiskFee));
    
	$subRiskSize.val(subRiskSize-1);//删除一条
	
	delTr(subRiskSize, index, "subRiskVo_", proposalPrefix);
	$parentTr.remove();
}


function calsuRiskFee(element){
	var pre = $(element).attr("name").split("]")[0];//下标
	var index = pre.split("[")[1];//下标
	var unitAmount = $("input[name='lossCarMainVo.prpLDlossCarSubRisks["+index+"].unitAmount']").val();
	var count = element.value;
	if(count =="" || isNaN(count)){
		$("input[name='lossCarMainVo.prpLDlossCarSubRisks["+index+"].subRiskFee']").val("");
		return false;
	}
	var kindCode = $("input[name='lossCarMainVo.prpLDlossCarSubRisks["+index+"].kindCode']").val();
	var sumFee = parseFloat(0);
	var riskcode=$("#riskCode1").val();
	if(kindCode=='C' || kindCode=='RF'){//代步车费用特约条款
		if(!isBlank(riskcode) && (riskcode=='1230' || riskcode=='1231' || riskcode=='1232' || riskcode=='1233') && kindCode=='RF'){
			sumFee = parseFloat(count)*parseFloat(unitAmount);
		}else{
			sumFee = parseFloat(count-1)*parseFloat(unitAmount);
		}
		
		if(sumFee<0){
			sumFee = 0.00;
		}
	}else{//机动车停驶损失险
		sumFee = parseFloat(count)*parseFloat(unitAmount);
	}
	
	$("input[name='lossCarMainVo.prpLDlossCarSubRisks["+index+"].subRiskFee']").val(sumFee);
	
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

/**
 *费用赔款信息初始化选择费用类型
 * ☆yangkun(2016年1月20日 下午3:21:35): <br>
 */
function initChargeType(element){
	var intermFlag = $("input[name='lossCarMainVo.intermFlag']").val();
	var chargeCodeStr =getChargeTypes("chargeCode");
	var params = {
		"chargeCodes" : chargeCodeStr,"intermFlag" : intermFlag
	};
//	var loadi = layer.load();//防止网络慢，点击两次增加按钮
	$.ajax({
		url : "/claimcar/defloss/initChargeType.ajax",
		type : "post",
		data : params,
		async: false,
		success : function(htmlData){
//			layer.close(loadi);//关闭加载层
			layIndex=layer.open({
				type: 1,
				area: ['230px', '270px'], //宽高
				content: htmlData
			});
		}
	});
}
 
function getChargeTypes(rowType){
	var chargeCodeStr = "";
	var checkFlagEle = null;
	if(rowType == "subRisk"){
		checkFlagEle =$("#subRiskTbody input[name$='subRiskKindCode']");
	}else if(rowType == "chargeCode"){
		checkFlagEle =$("#chargeTbody input[name$='chargeCode']");
	}
	var deviceStr = $("#deviceMap").val();
	$(checkFlagEle).each(function(){
		if(rowType == "subRisk" && !isBlank(deviceStr) && $(this).val() =="X"){
			return;
		}
		
		if(chargeCodeStr ==""){
			chargeCodeStr =$(this).val();
		}else{
			chargeCodeStr = chargeCodeStr + "," + $(this).val();
		}
		
	});
	return chargeCodeStr;
}

function setCharge(){
	var $tbody = $("#chargeTbody");
	showMore("chargeTbody");
	
	var indexSeriNo = $("#chargeTab [name='chargeCheckFlag']:checked");
	var intermCode = $("input[name='lossCarMainVo.intermCode']").val();
	var param = "";
	var registNo = $("#registNo").val();
	var count = indexSeriNo.length;
	for(var i = 0 ; i < indexSeriNo.length; i++){
		if(i == 0){
			param = indexSeriNo.val();
		} else {
			param = $(indexSeriNo[i]).val() + "," + param; 
		}
	}
	
	var $chargeSize =$("#chargeSize") ;//附加险条数
	var size = parseInt($chargeSize.val(),10);
	var stander = 0;
//	if(!isBlank(intermCode)){
//		stander = feeStander[1];//默认车辆定损
//	}
	var veriChargeFlag = "0";
	if($("#veriChargeFlag").length>0){
		veriChargeFlag =$("#veriChargeFlag").val();
	}
	var params = {"size":size,"chargeTypes":param,"registNo":registNo,"intermCode":intermCode,"feeStandard":stander,"veriChargeFlag":veriChargeFlag};
	
	var url = "/claimcar/defloss/loadChargeTr.ajax";
	$.post(url,params, function(result){
		$tbody.append(result);
		$chargeSize.val(size + count );//重新附加险条数
	});
	closePop();
}


function delCharge(element){
	showMore("chargeTbody");
	var index = $(element).attr("name").split("_")[1];//下标
	var proposalPrefix="lossChargeVos";
	var $parentTr = $(element).parent().parent();
	var $chargeSize =$("#chargeSize") ;//附加险条数
	var chargeSize = parseInt($chargeSize.val(),10);//原附加险条数
	$chargeSize.val(chargeSize-1);//删除一条
	
	delTr(chargeSize, index, "lossChargeVo_", proposalPrefix);
	$parentTr.remove();
	
	calSumChargeFee(null,false);//合计金额重新计算
}	

function changeServerType(file){
	var serverType = $(file).val();
	var pre = $(file).attr("name").split("]")[0];//下标
	var index = pre.split("[")[1];//下标 
	var chargeStandard ="#chargeStandard_"+index+"";
	$(chargeStandard).text(feeStander[serverType]);
	$("#chargeTbody input[name='lossChargeVos["+index+"].chargeStandard']").val(feeStander[serverType]);
}

function initSubrogationCar(){//subrogationPersTbody
	var tbody = $("#subrogationCarTable");
	var carSize =$("#subrogationCarSize") ;
	var size = parseInt(carSize.val(),10);
	var registNo =$("#registNo").val() ;// 
	
	$.post("/claimcar/defloss/validThirdCar.do?registNo="+registNo, function(jsonData) {
		if(jsonData.data != "ok"){
			layer.msg(jsonData.data);
			return false;
		}else{
			var params = {"size":size,"registNo":registNo};
			var url = "/claimcar/defloss/loadSubrationCar.ajax";
			$.post(url,params, function(result){
				tbody.append(result);
				reloadRow2(size);
				
				carSize.val(size + 1 );//重新附加险条数
			});
		}
	});
}

function reloadRow2 (size){
	var div ="#subrogationCarTbody_"+size+" .select2";
	//alert(div);
	$(div).select2({
		
	});
}

function delSubrogationCar(element){
	var index = $(element).attr("name").split("_")[1];//下标
	var proposalPrefix="subrogationMain.prpLSubrogationCars";
	var parentTr = $(element).parent().parent();
	var carSize =$("#subrogationCarSize") ;//附加险条数
	var carSizeInt = parseInt(carSize.val(),10);//原附加险条数
	carSize.val(carSizeInt-1);//删除一条
	
	delTr(carSizeInt, index, "subrogationCar_", proposalPrefix);
	for(var i=0;i<2;i++){
		var parentTr1 = parentTr.next();
		parentTr1.remove();
	}
	parentTr.remove();
	//删除tbody
	$("#subrogationCarTbody_"+index).remove();
	
	//调整序号
	var items = $("button[name^='subrogationCar_']");
	items.each(function(){
		name = this.name;
		var idx= name.split("_")[1];
		var idx_ = parseInt(idx) +1;
		$(this).parent().next().html(idx_);
		//调整tbody index
		$(this).parent().parent().parent().attr("id","subrogationCarTbody_"+idx);
	});
	
	
}

function initSubrogationPers(){//subrogationPerTbody
	var tbody = $("#subrogationPerTbody");
	var carSize =$("#subrogationPerSize") ;// 
	var registNo =$("#registNo").val() ;// 
	var size = parseInt(carSize.val(),10);
	var params = {"size":size,"registNo":registNo};
	var url = "/claimcar/defloss/loadSubrationPers.ajax";
	$.post(url,params, function(result){
		tbody.append(result);
		carSize.val(size + 1 );//重新附加险条数
	});
}

function delSubrogationPers(element){
	var index = $(element).attr("name").split("_")[1];//下标
	var proposalPrefix="subrogationMain.prpLSubrogationPersons";
	var parentTr = $(element).parent().parent();
	var $subRiskSize =$("#subrogationPerSize") ;//附加险条数
	var subRiskSize = parseInt($subRiskSize.val(),10);//原附加险条数
	$subRiskSize.val(subRiskSize-1);//删除一条
	
	delTr(subRiskSize, index, "subrogationPer_", proposalPrefix);
	
	for(var i=0;i<3;i++){
		var parentTr1 = parentTr.next();
		parentTr1.remove();
	}
	parentTr.remove();
	parentTr.remove();
	
	//调整序号
	var items = $("button[name^='subrogationPer_']");
	items.each(function(){
		name = this.name;
		var idx= name.split("_")[1];
		idx = parseInt(idx) +1;
		$(this).parent().next().html(idx);
	});
}


/**
 * 增加一行或多行附加险
 */
function setOutRepair(){
	var $tbody = $("#outRepairBody");
	var veriFeeFlag = "0";
	if($("#veriFeeFlag").length>0){
		veriFeeFlag =$("#veriFeeFlag").val();
	}
	var $subRiskSize =$("#outRepairSize") ;//附加险条数
	var size = parseInt($subRiskSize.val(),10);
	var params = {"size":size,"veriFeeFlag" :veriFeeFlag};
	var url = "/claimcar/defloss/loadOutRepairTr.ajax";
	$.post(url,params, function(result){
		$tbody.append(result);
		$subRiskSize.val(size+1);//重新附加险条数
	});
	closePop();
}

function delOutRepair(element){
	var index = $(element).attr("name").split("_")[1];//下标
	var proposalPrefix="prpLDlossCarRepairs";
	var $parentTr = $(element).parent().parent();
	var $chargeSize =$("#outRepairSize") ;//附加险条数
	var chargeSize = parseInt($chargeSize.val(),10);//原附加险条数
	$chargeSize.val(chargeSize-1);//删除一条  prpLDlossCarRepairs[${repariIndex }].sumDefLoss
	var outFee=$("#outRepairBody input[name='prpLDlossCarRepairs["+index +"].sumDefLoss']").val();//删除的外修金额
	var sumOutFee= $("#jy_sumOutFee").val();//外修总金额
	if(outFee == "" || isNaN(outFee)){
		outFee = parseFloat(0.00); //prpLDlossCarRepairs[0].sumDefLoss
	}
	$("#jy_sumOutFee").val(parseFloat(sumOutFee)-parseFloat(outFee));// 外修总金额
	//合计金额 减去删除的金额
	var sumLossFee= $("#jy_sumLossFee").val();
	$("#jy_sumLossFee").val(parseFloat(sumLossFee)-parseFloat(outFee));// 外修总金额
	
	delTr(chargeSize, index, "outRepairVo_", proposalPrefix);
	$parentTr.remove();
	
	//calSumChargeFee(null,false);//合计金额重新计算
}	