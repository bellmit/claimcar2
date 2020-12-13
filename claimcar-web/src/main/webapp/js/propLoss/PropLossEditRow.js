var layIndex;
var feeStander = new Array();

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
/**
 * 赔款费率点击触发函数
 */

function initChargeType(element){
	var intermFlag = $("input[name='prpLdlossPropMainVo.interMediaryFlag']").val();
	var chargeCodeStr =getChargeTypes("chargeCode");
	var params = {
		"chargeCodes" : chargeCodeStr,"intermFlag" : intermFlag
	};
	
	$.ajax({
		url : "/claimcar/proploss/initChargeType.ajax",
		type : "post",
		async: false,
		data : params,
		success : function(htmlData){
			layIndex=layer.open({
			    type: 1,
			    skin: 'layui-layer-rim', //加上边框
			    area: ['230px', '270px'], //宽高
			    content: htmlData
			});
		}
	});
}


function delCharge(element){
	//showMore(1);
	var index = $(element).attr("name").split("_")[1];//下标
	var proposalPrefix="lossChargeVos";
	var $parentTr = $(element).parent().parent();
	var $chargeSize =$("#chargeSize") ;//附加险条数
	var chargeSize = parseInt($chargeSize.val(),10);//原附加险条数
	$chargeSize.val(chargeSize-1);//删除一条
	delTr(chargeSize, index, "lossChargeVo_", proposalPrefix);
	$parentTr.remove();
	calSumChargeFee(null,false);//合计金额重新计算
	calSumVeriFee();//计算核损金额
}

function changeServerType(file){
	var serverType = $(file).val();
	var pre = $(file).attr("name").split("]")[0];//下标
	var index = pre.split("[")[1];//下标 
	var chargeStandard ="#chargeStandard_"+index+"";
	$(chargeStandard).text(feeStander[serverType]);
	$("#chargeTbody input[name='lossChargeVos["+index+"].chargeStandard']").val(feeStander[serverType]);
}

function getChargeTypes(rowType){
	var chargeCodeStr = "";
	var checkFlagEle ;
	if(rowType == "subRisk"){
		checkFlagEle =$("#subRiskTbody input[name$='subRiskKindCode']");
	}else if(rowType == "chargeCode"){
		checkFlagEle =$("#chargeTbody input[name$='chargeCode']");
	}
	$(checkFlagEle).each(function(){
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
	//showMore(1);
	
	var indexSeriNo = $("#chargeTab [name='chargeCheckFlag']:checked");
	var intermCode = $("input[name='prpLdlossPropMainVo.interMediaryinfoId']").val();
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
	var stander =0;
//	var stander = feeStander[1];//默认财产定损
	var params = {"size":size,"chargeTypes":param,"registNo":registNo,"intermCode":intermCode,"feeStandard":stander};
	var url = "/claimcar/proploss/loadChargeTr.ajax";
	$.post(url,params, function(result){
		$tbody.append(result);
		$chargeSize.val(size + count );//重新附加险条数
	});
	closePop();
}


function closePop(){
    layer.close(layIndex);
}

function calSumVeriFee() {
	var items = $("#chargeTbody input[name$='veriChargeFee']");
	var sumFee = parseFloat(0.00);
	$(items).each(function() {
		var chargeFee = $(this).val();
		if (chargeFee == "") {
			chargeFee = parseFloat(0.00);
		}
		sumFee = sumFee + parseFloat(chargeFee);
	});

	$("#sumVeriFee").val(sumFee);
}





