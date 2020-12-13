
var rowNum =10;//table 页面保留行数
$(function (){
	
	calSumChargeFee();
	
	$.Datatype.selectMust = function(gets,obj,curform,regxp){
		var code = $(obj).val();
		if(isBlank(code)){
			return false;
		}
    };
    
    setKindSelect();
});

function initChargeType(){
	var intermFlag = $("input[name='lossCarMainVo.intermFlag']").val();
	var chargeCodeStr = getChargeTypes("chargeCode");
	var params = {
		"chargeCodes" : chargeCodeStr,"intermFlag" : intermFlag
	};
//	var loadi = layer.load();//防止网络慢，点击两次增加按钮
	$.ajax({
		url : "/claimcar/disaster/initChargeType.ajax",
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
	
	var params = {"size":size,"chargeTypes":param,"registNo":registNo,};
	var url = "/claimcar/disaster/loadChargeTr.ajax";
	$.post(url,params, function(result){
		$tbody.append(result);
		$chargeSize.val(size + count );//重新附加险条数
		setKindSelect();
	});
	closePop();
}

function setKindSelect(){
	$("select[name$='.kindCode']").each(function(){
	    $(this).attr("datatype","selectMust");
	});
}

//费用功能显示更多
function showMore(objName){
	var bodyDivId="#"+objName;
	var buttonId ="#"+objName+"Button";
	$(buttonId).css('display','none');
	
	var num = rowNum - 1;
	$(bodyDivId).find("tr :gt("+num+")").each(function(){
		$(this).removeAttr("style"); 
	});
}

function closePop(){
    layer.close(layIndex);
}

//费用赔款信息 合计	
function calSumChargeFee(){
	var items =$("#chargeTbody input[name$='chargeFee']");
	var sumFee = Number(0.0);
	$(items).each(function(){
		var chargeFee = $(this).val();
		if(chargeFee == "" || isNaN(chargeFee)){
			chargeFee = Number(0.0);
		}
		sumFee += Number(chargeFee);
	});
	
	$("#sumChargeFee").val(sumFee);
}

//收款人选择界面打開
var payIndex=null;
function showPayCust(element){
	var registNo = $("#registNo").val();
	var tdName = $(element).attr("name");
	var url="/claimcar/payCustom/payCustomList.do?registNo="+registNo+"&tdName="+tdName+"";
	if(payIndex==null){
		payIndex=layer.open({
		    type: 2,
		    title: '收款人选择',
		    shade: false,
		    area: ['1000px', '500px'],
		    content:url,
		    end:function(){
		    	payIndex=null;
		    }
		});
	}
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

function setCharge2(){
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