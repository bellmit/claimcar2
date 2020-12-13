
$(function() {
	var ajaxEdit = new AjaxEdit($('#prePayForm'));
	
	if($("#workStatus").val()=="6"){//退回到冲销
		ajaxEdit.targetUrl = "/claimcar/prePay/submitPrePayWf.do";
	}else{
		ajaxEdit.targetUrl = "/claimcar/prePay/submitPrePayWriteOff.do";
	}
//	ajaxEdit.rules = rule;
	ajaxEdit.beforeSubmit = function(data) {
		layer.load(0, {
			shade : [0.8, '#393D49']
		});
	};
	ajaxEdit.afterSuccess = function(data) {
		$("submitDiv").remove();
		location.reload();
	};
	// 绑定表单
	ajaxEdit.bindForm();

	$.Datatype.checkBoxMust = function(gets, obj, curform, regxp) {
		var need = 1, numselected = curform.find("input[name='" + obj.attr("name") + "']:checked").length;
		return numselected >= need ? true : "请至少选择" + need + "项！";
	};

});
function calculate(maxValue){
	
	var prePayPFeeAmt = 0;
	$(".payPAmt").each(function(){
		prePayPFeeAmt = parseFloat(prePayPFeeAmt+parseFloat($(this).val()));
	});
	var payFFeeAmt = 0;
	$(".payFAmt").each(function(){
		payFFeeAmt = parseFloat(payFFeeAmt+parseFloat($(this).val()));
	});
}
function mathMul(arg1,arg2)
{
    var m=0,s1=arg1.toString(),s2=arg2.toString();
    try{m+=s1.split(".")[1].length}catch(e){}
    try{m+=s2.split(".")[1].length}catch(e){}
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
}
function submit(){
	var currentNode = $("#nodeCode").val();
	var riskCode = $("input[name='compensateVo.riskCode']").val();
	var comCode = $("input[name='compensateVo.comCode']").val();
	var registNo = $("input[name='compensateVo.registNo']").val();
	var compensateNo = $("input[name='compensateVo.compensateNo']").val();
	var prePayPFeeAmt = 0;
	var flag = true;
	$(".payFAmt,.payPAmt").each(function(){
		var value = $(this).val();
		var maxValue = $(this).attr("max");
		var prepayType = $(this).parent().prev().text();
		if( parseFloat(maxValue) < parseFloat(value)){
			layer.alert(prepayType + "的冲销金额("+ value + ") 不能大于不能最大预付金额("+ maxValue + ")");
			flag = false;
			return false;
		} else{
			prePayPFeeAmt = parseFloat(prePayPFeeAmt + parseFloat($(this).val()));
		}
	});
	if(!flag){
		return;
	} else {
		if(prePayPFeeAmt == 0){
			layer.alert("冲销总金额必须大于0");	
			return false;
		}
	};
	$("input[name='compensateVo.sumAmt']").val(mathMul(prePayPFeeAmt,-1));
	var sumAmt = $("input[name='compensateVo.sumAmt']").val();
	var params = {
			"currentNode" : currentNode,
			"auditStatus" : "submit",
			"riskCode" : riskCode,
			"comCode" : comCode,
			"sumAmt" : sumAmt,
			"registNo" : registNo,
			"compensateNo" : compensateNo,
		};
		$.ajax({
			url : "/claimcar/prePay/loadSubmitVClaimNext.ajax",
			type : "post",
			data : params,
			async : false, 
			success : function(htmlData){
				layer.open({
					title : "提示信息",
					type : 1,
					skin : 'layui-layer-rim', // 加上边框
					area : ['420px', '220px'], // 宽高
					content : htmlData,
					yes : function(index, layero) {
						var html = layero.html();
						$("#hideDiv").empty();
						$("#hideDiv").append(html);
						$("#hideDiv").hide();
						layer.close(index);
						
						$("#prePayForm").submit();
					}
				});
			}
		});
}