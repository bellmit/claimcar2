
$(function(){//页面进来需要加载的选项
	initBtn();
	initRecly();
	initCheckAndDef();
	initVerirescueFee();
	calSumLossFee();
	var ajaxEdit = new AjaxEdit($('#verifyform'));
	//财产核损保存和暂存
	ajaxEdit.targetUrl = "/claimcar/proploss/savePropVerifyLoss.do";
	ajaxEdit.beforeCheck=SaveOrOther;//校验前调用(表单点击函数已经促发)
	ajaxEdit.beforeSubmit= saveCheck;//表单提交前调用
	ajaxEdit.afterSuccess = saveVerifyLossAfter;//后台返回成功后调用
	
	//绑定表单
	ajaxEdit.bindForm();
	$.Datatype.amount =  /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;//金额验证

});

function initCheckAndDef(){
	if($("#handlerStatus").val()=='3'){//已处理
		$("body textarea").eq(0).attr("disabled","disabled");
		$("#op").prop("disabled","disabled");
		$("body input").each(function(){
			$(this).attr("disabled","disabled");
			if($(this).attr("type") == "button" || $(this).attr("type") == "submit"){
				$(this).addClass("btn-disabled");
			}
		}); 
	}
}


function SaveOrOther(){//校验前调用的方法
	var submitType=$("#saveType").val();
	var bl = true;
	if(submitType=="save"){//移除校验
		$("#op").removeAttr("datatype");
	}else{
		$("#op").attr("datatype","*");
		var opinionCode = $("input[name='op']:checked").val();
		if(opinionCode == null ){
			layer.msg("请选择核损意见");
			$("input[name='op']").attr("datatype","*");
			return false;
		}
	}
}



/*
 * 初始化损余回收按钮
 */

function initRecly(){
	$(":checkBox").each(function(i){//初始化是否损余回收按钮 1选中，0 未选中
		var ch=$(":checkBox:eq("+i+")").val();//i+1
		var ch1=$(":checkBox:eq("+i+1+")").val();//i+1
		//1 损余回收 0 否
		if(ch=='1'){
			$(":checkBox:eq("+i+")").attr("checked","checked");//定损按钮
//			$(":checkBox:eq("+i+1+")").attr("checked","checked");//核损按钮
			//设置核损残值金额。

//			//如果勾选了损余回收的话 
//			$(":checkBox:eq("+i+")").parent().parent().next().find(":first-child").attr("readonly","readonly");
		}else{
			if(parseInt(i)%2 == 0){
				$(":checkBox:eq("+i+")").removeAttr("checked");
			}else{
				$(":checkBox:eq("+i+")").removeAttr("checked");
				$(":checkBox:eq("+i+")").removeClass("radio-box").removeAttr("disabled");
			}
		}
		
		
		//如果是损失回收。则显示后面的输入框
	});
		
	}


function calSumLossFee(){
	
	var $items =$("#tab_propVerify tr");//确定总共有多少行
	var sum =0;
	var length=($items.length-1)/2;
	
	for(var i=0;i<length;i++){
		var sumPrice=0;

		var number=$("input[name='lossPropMainVo.prpLdlossPropFees["+i+"].veriLossQuantity']").val();
		if(isNaN(number)||number ==""){
			number=0;
		}
		//单价
		var price=$("input[name='lossPropMainVo.prpLdlossPropFees["+i+"].veriUnitPrice']").val();
		if(isNaN(price)||price==""){
			price=0;
		}
		//获取残值
		var recyclePrice=$("input[name='lossPropMainVo.prpLdlossPropFees["+i+"].veriRecylePrice']").val();
		if(isNaN(recyclePrice)||recyclePrice==""){
			recyclePrice=$("#propFees_"+i+"_recyclePrice").val();
		}
		
		sumPrice=number*price-recyclePrice;
		if(sumPrice<=0){
//			layer.msg("核损残值金额不能大于该财产总金额！");
			recyclePrice = $("#propFees_"+i+"_recyclePrice").val();
			$("input[name='lossPropMainVo.prpLdlossPropFees["+i+"].veriRecylePrice']").val(recyclePrice);
			sumPrice = parseFloat(number * price - recyclePrice).toFixed(2);
		}
		//设置单元总和
		$("input[name='lossPropMainVo.prpLdlossPropFees["+i+"].sumVeriLoss']").val(sumPrice);
		sum=sum+parseFloat(sumPrice);
		
	}
	
	$("#sumVeriLoss").val(sum);
}



function save1(saveType){
	if(saveType =="submitVloss" && $("#verifyPassFlag").val() == "false"){
		layer.msg("总金额超过您的权限，请提交上级！");
		return;
	}
	$("#saveType").val(saveType);
	$("#verifyform").submit();
}

/**
 * 表单异步提交前校验
 */
function saveCheck(){
	var propFeeSize =$("#propFeeSize").val();
	for(var i=0;i<propFeeSize;i++){
		var lossItemName = $("#propFees_"+i+"_lossItemName").val();
		var lossQuantity = $("#propFees_"+i+"_lossQuantity").val();
		var recyclePrice = $("#propFees_"+i+"_recyclePrice").val();
		var unitPrice = $("#propFees_"+i+"_unitPrice").val();
		
		var veriUnitPrice = $("input[name='lossPropMainVo.prpLdlossPropFees["+i+"].veriUnitPrice']").val();
		var veriLossQuantity = $("input[name='lossPropMainVo.prpLdlossPropFees["+i+"].veriLossQuantity']").val();
		var veriRecylePrice = $("input[name='lossPropMainVo.prpLdlossPropFees["+i+"].veriRecylePrice']").val();
		if(parseFloat(veriLossQuantity)>parseFloat(lossQuantity)){
			layer.msg("核损数量不能大于定损数量！");
			$("input[name='lossPropMainVo.prpLdlossPropFees["+i+"].veriLossQuantity']").focus();
			return false;
		}
		//需求改变
//		if(parseFloat(recyclePrice)<parseFloat(veriRecylePrice)){
//			layer.msg("核损残值金额不能大于定损残值金额！");
//			 $("input[name='lossPropMainVo.prpLdlossPropFees["+i+"].veriRecylePrice']").focus();
//			return false;
//		}
		if(parseFloat(veriUnitPrice)>parseFloat(unitPrice)){
			layer.msg("核损单价不能大于定损单价！");
//			$("input[name='lossPropMainVo.prpLdlossPropFees["+i+"].veriUnitPrice']").focus();
			return false;
		}
	}
	
	var sumLossFee=$("input[name$='sumLossFee']").val();//:赔款费用定损金额 
	var sumVeriFee=$("input[name$='sumVeriFee']").val();//赔款费用核损金额
	var sumDefLoss=$("input[name$='sumDefloss']").val();//定损总费用
	var sumVeriLoss=$("input[name$='sumVeriLoss']").val();//定损总核损费用 
	if(parseFloat(sumVeriFee)>parseFloat(sumLossFee)||parseFloat(sumVeriLoss)>parseFloat(sumDefLoss)){
		layer.msg("核损总金额不能大于定损总金额！");
		return false;
	}
	if(parseFloat(sumVeriLoss)>parseFloat(sumDefLoss)){
		layer.msg("核损赔款费用不能大于定损赔款费用！");
		return false;
	}
	
	var lossChargeVosSize = $("#lossChargeVosSize").val();
	for(var i=0;i<lossChargeVosSize;i++){
		var veriChargeFee = $("input[name='lossChargeVos["+i+"].veriChargeFee']").val();//核损费用
		var chargeFeeId = $("#lossChargeVos_"+i+"_chargeFeeId").val();//定损费用
		if(parseFloat(veriChargeFee)>parseFloat(chargeFeeId)){
			layer.msg("核损赔款费用不能大于定损赔款费用！");
			return false;
		}
	}
	//核损施救费不能大于定损施救费
	var defRescueFee = $("input[name='lossPropMainVo.defRescueFee']").val();
	var verirescueFee = $("input[name='lossPropMainVo.verirescueFee'").val();
	if(parseFloat(defRescueFee)<parseFloat(verirescueFee)){
		layer.msg("核损施救费用不能大于定损施救费用！");
		return false;
	}
	//获取是 暂存 还是提交按钮。暂存时不需要校验是否输入核损意见
	
	
}

/**
 * 表单异步提交后的校验。
 */
function saveVerifyLossAfter(result){
	var saveType = $("#saveType").val();//获取提交方式
	var currentNode = $("#currentNode").val();//获取当前节点名称
	var dataArr = result.data.split(",") ;
	var lossMainId = dataArr[0];//主表ID
	var flowTaskId = dataArr[1];//工作流ID
	if(saveType=="save"){//暂存
		layer.confirm('暂存成功', {
			btn: ['确定'] //按钮
		}, function(){
			window.location.reload();
		});
	}else{//暂存其他按钮
		var url = "/claimcar/proploss/submitNextPage.do?lossMainId="+lossMainId+"&currentNode="+currentNode+
					"&flowTaskId="+flowTaskId+"&saveType="+saveType;
		layer.open({
			type: 2,
			closeBtn:0,
			title: "核损提交",
			area: ['75%', '50%'],
			fix: true, //不固定
			maxmin: false,
			content: url,
	 	});
	}

}

function changeOp(){
	var code=$("input[name='claimTextVo.opinionCode']:checked").val();
	var opinion;
	if(code == 'a'){
		opinion = "核损同意：";
	}else if(code == 'b'){
		opinion = "价格异议：";
	}else if(code == 'c'){
		opinion = "信息不充分：";
	}else{
		opinion = "其他：";
	}
	var sumVeriLoss = $("#sumVeriLoss").val();
	var opinionText = opinion;
		opinionText = opinion+"("+"核损金额："+sumVeriLoss+")";
	document.getElementById("op").value=opinionText;
	//将光标移到 op上去
	$("#op").focus();
	//每次点击的时候去除 几个按钮的 btnClass
//	var $opinionCode=$("input[name$='opinionCode']");
//	if(opinion=="核损同意:"){
//		$opinionCode.val("a");
//	}else if(opinion=="价格异议:"){
//		$opinionCode.val("b");
//	}else if(opinion=="信息不充分:"){
//		$opinionCode.val("c");
//	}else{
//		$opinionCode.val("d");
//	}
	
	
	$("#op").removeClass("Validform_error");
	$("#op").qtip('destroy',true);
	initBtn();
}

//费用赔款信息 合计	
function calSumChargeFee(field){
	var items =$("#chargeTbody input[name$='veriChargeFee']");
	var sumFee = parseFloat(0.00);
	$(items).each(function(){
		var chargeFee = $(this).val();
		if(chargeFee == ""){
			chargeFee = parseFloat(0.00);
		}
		sumFee =sumFee + parseFloat(chargeFee);
	});
	
	$("#sumVeriChargeFee").val(sumFee);
}

function initVerirescueFee(){
	var fee = $("input[name$='lossPropMainVo.verirescueFee']").val();
	if(fee == ""){
		$("input[name$='lossPropMainVo.verirescueFee']").val(0);
	}
}

function initBtn(){
	var currentLevel = parseInt($("#currentNode").val().split("LV")[1]);
	var verifyLevel = parseInt($("#verifyLevel").val());
	var existBackLower = $("#existBackLower").val();
	if(existBackLower=='false'){
		$("#backLower").hide();
	}
	if(currentLevel<verifyLevel){
		$("#submit").addClass("btnClass").attr("disabled",true);
	}
	if(currentLevel>=10){
		$("#audit").hide();
	}
	if(currentLevel <= verifyLevel && verifyLevel <= 8){
		$("#backLower").hide();
	}if(currentLevel <= 8 && verifyLevel > 8){
		$("#backLower").hide();
	}
	if(currentLevel > 8){
		$("#backLoss").hide();
	}
	
	var code=$("input[name='claimTextVo.opinionCode']:checked").val();
	$("#save").removeClass("btnClass").removeAttr("disabled");
	$("#submit").removeClass("btnClass").removeAttr("disabled");
	$("#audit").removeClass("btnClass").removeAttr("disabled");
	$("#backLoss").removeClass("btnClass").removeAttr("disabled");
	$("#backLower").removeClass("btnClass").removeAttr("disabled");
	if(code=="a"){
		//退回下级 退回定损 不可用
		$("#backLower").addClass("btnClass").attr("disabled",true);
		$("#backLoss").addClass("btnClass").attr("disabled",true);
	}else if(code=="b"||code=="c"||code=="d"){//选择 价格异议 信息不充分 其他 则 提交上级、核损通过不能用
		$("#audit").addClass("btnClass").attr("disabled",true);
		$("#submit").addClass("btnClass").attr("disabled",true);
	}
}

//选择损余回收 单选按钮时 后面的 的文本框变为 只读,取消选择的时候后面文本框变为可操作
function recycleFlag(item) {
	var ch = $(item).attr("name").split("]")[0];
	var index = ch.split("[")[1];
	// 用name选择器获取
	if (typeof ($(item).attr("checked")) == "undefined") {
		$(item).attr("checked", "checked");
		$(
				"input[name='lossPropMainVo.prpLdlossPropFees[" + index
						+ "].veriRecylePrice']").attr("readonly", "readonly");
		$(
				"input[name='lossPropMainVo.prpLdlossPropFees[" + index
						+ "].veriRecylePrice']").val("0");
		$(
				"input[name='lossPropMainVo.prpLdlossPropFees[" + index
						+ "].recycleFlag']").val("1");
	} else {
		$(
				"input[name='lossPropMainVo.prpLdlossPropFees[" + index
						+ "].veriRecylePrice']").removeAttr("readonly");
		$(item).removeAttr("checked");
		$(
				"input[name='lossPropMainVo.prpLdlossPropFees[" + index
						+ "].recycleFlag']").val("0");
	}

	var items = $("#tab_propVerify input[id$='lossItemName']");// 确定总共有多少行
	var sum = 0;
	$(items).each(
			function(i) {
				// 获取数量 获取单价 获取残值 计算总价
				var sumPrice = 0;
				// 数量
				var number = $(
						"input[name='lossPropMainVo.prpLdlossPropFees["
								+ i + "].veriLossQuantity']").val();
				if (isNaN(number)) {
					number = 0;
				}
				// 单价
				var price = $(
						"input[name='lossPropMainVo.prpLdlossPropFees["
								+ i + "].veriUnitPrice']").val();
				if (isNaN(price)) {
					price = 0;
				}
				// 获取残值
				var recyclePrice = $(
						"input[name='lossPropMainVo.prpLdlossPropFees["
								+ i + "].veriRecylePrice']").val();
				if (isNaN(recyclePrice)) {
					recyclePrice = 0;
				}

				sumPrice = number * price - recyclePrice;
				if (sumPrice <= 0) {
					sumPrice = 0;
				}
				// 设置单元总和
				$(
						"input[name='lossPropMainVo.prpLdlossPropFees["
								+ i + "].sumVeriLoss']").val(sumPrice);
				sum = sum + parseFloat(sumPrice);
			});
	$("#sumVeriLoss").val(sum);
}
