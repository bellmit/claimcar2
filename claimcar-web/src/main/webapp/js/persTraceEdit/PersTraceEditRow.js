// 显示更多
function showMoreChange(){
	$("#chargeTbody").find("tr :gt(9)").each(function(){
		$(this).removeAttr("style"); 
		if($(this).attr("id")=="morebutton"){
			this.remove();
		}
	});
}

function initChargeType(element){
	var intermFlag = $("#intermediaryFlag").val();
	var chargeCodeStr =getChargeTypes("chargeCode");
	var params = {
		"chargeCodes" : chargeCodeStr,"intermFlag" : intermFlag
	};
	
	$.ajax({
		url : "/claimcar/loadAjaxPage/initChargeType.ajax",
		type : "post",
		data : params,
		async : false, 
		success : function(htmlData){
			layIndex=layer.open({
			    type: 1,
			    skin: 'layui-layer-rim', // 加上边框
			    area: ['230px', '270px'], // 宽高
			    content: htmlData
			});
		}
	});
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
	showMoreChange();
	
	var indexSeriNo = $("#chargeTab [name='chargeCheckFlag']:checked");
	var intermCode = $("#intermediaryInfoId").val();
	var param = "";
	var registNo = $("input[name='prpLDlossPersTraceMainVo.registNo']").val();
	var count = indexSeriNo.length;
	for(var i = 0 ; i < indexSeriNo.length; i++){
		if(i == 0){
			param = indexSeriNo.val();
		} else {
			param = $(indexSeriNo[i]).val() + "," + param; 
		}
	}
	
	var $chargeSize =$("#chargeSize") ;// 附加险条数
	var size = parseInt($chargeSize.val(),10);
	
	var params = {"size":size,"chargeTypes":param,"registNo":registNo,"intermCode":intermCode};
	var url = "/claimcar/loadAjaxPage/loadChargeTr.ajax";
	$.post(url,params, function(result){
		$tbody.append(result);
		$chargeSize.val(size + count );// 重新附加险条数
		$tbody.find("select[name$='.kindCode']").attr("datatype","selectMust");
	});
	closePop();
}


function delCharge(element){
	showMoreChange();
	var index = $(element).attr("name").split("_")[1];// 下标
	var proposalPrefix="lossChargeVos";
	var $parentTr = $(element).parent().parent();
	var $chargeSize =$("#chargeSize") ;// 附加险条数
	var chargeSize = parseInt($chargeSize.val(),10);// 原附加险条数
	
	var status = $("input[name='lossChargeVos["+index+"].status']").val();
	if(status=="1"){
		layer.msg("费用审核通过的费用不支持删除操作！");
		return false;
	}
	$chargeSize.val(chargeSize-1);// 删除一条
	
	delTr(chargeSize, index, "lossChargeVo_", proposalPrefix);
	
	$parentTr.find(":input").qtip('destroy', true);
	$parentTr.remove();
	
	calSumChargeFee(null,false);// 合计金额重新计算
}	

function changeServerType(file){
	var serverType = $(file).val();
	var pre = $(file).attr("name").split("]")[0];// 下标
	var index = pre.split("[")[1];// 下标
	var chargeStandard ="#chargeStandard_"+index+"";
	$(chargeStandard).text(feeStander[serverType]);
	$("#chargeTbody input[name='lossChargeVos["+index+"].chargeStandard']").val(feeStander[serverType]);
}
