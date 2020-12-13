/**
 * 页面加载进来需要做的一些事情
 */

$(function() {
	$.Datatype.amount = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;// 金额验证
	//initCheckAndDef();// 初始页面是否弹出是否接受任务框
	initPropPage();
	defaultRecyclePrice();
	$(":checkBox").each(
			function(i) {// 初始化是否损余回收按钮 1选中，0 未选中
				var ch = $(":checkBox:eq(" + i + ")").val();
				// 1 损余回收 0 否
				if (ch == '1') {
					$(":checkBox:eq(" + i + ")").attr("checked", "checked");
					// 如果勾选了损余回收的话
					$(":checkBox:eq(" + i + ")").parent().parent().next().find(
							":first-child").attr("readonly", "readonly");

				}
			});

	var ajaxEdit = new AjaxEdit($('#defossform'));
	ajaxEdit.targetUrl = "/claimcar/proploss/saveOrUpdatePropCertainLoss.do";
	ajaxEdit.beforeSubmit = saveBeforeCheck;
	ajaxEdit.afterSuccess = saveDeflossAfter;
	ajaxEdit.bindForm();// 绑定表单
	$.Datatype.amount = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;// 金额验证
});

function saveBeforeCheck() {
	var itmes = $("#subRiskTbody :input[name$='recycleFlag']");
	var recycleFlag = "0";
	$(itmes).each(function(i) {
		if ($(this).is(":checked")) {
			recycleFlag = "1";
		}
	});
	$("input[name='prpLdlossPropMainVo.recycleFlag']").val(recycleFlag);

	//是否车物减损
	var isWhethertheloss = $("input[name='prpLdlossPropMainVo.isWhethertheloss']:checked");
	if(isEmpty(isWhethertheloss)){
		layer.msg("必须录入车物减损标志！");
		isWhethertheloss.focus();
		return false;
	}
	
	var a = true;
	$("input[name$='sumVeriLoss']").each(
			function(i) {

				var sumVeriLoss = $(
						"input[name='lossPropMainVo.prpLdlossPropFees[" + i
								+ "].sumVeriLoss'").val();// 核损损失项目值

				var sumDefloss = $(
						"input[name='prpLdlossPropMainVo.prpLdlossPropFees["
								+ i + "].sumDefloss'").val();

				if (parseFloat(sumDefloss) > parseFloat(sumVeriLoss)) {

					layer.msg('项目损失项定损金额不能大于回退的核损金额！', {
						icon : 1,
						time : 5000
					// 2秒关闭（如果不配置，默认是3秒）
					});
					a = false;

				}

				// prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size
				// }].sumDefloss
			});
	return a;

}

function calSumChargeFee(field, checkFlag) {
	var items = $("#chargeTbody input[name$='chargeFee']");
	var sumFee = parseFloat(0.00);
	$(items).each(function() {
		var chargeFee = $(this).val();
		if (chargeFee == "" || chargeFee == " " || chargeFee == null || isNaN(chargeFee)) {
			chargeFee = parseFloat(0.00);
		}
		sumFee = sumFee + parseFloat(chargeFee);
	});

	$("#sumChargeFee").val(sumFee);
}

/**
 * 定损提交成功后返回
 */
function saveDeflossAfter(result) {

	var saveType = $("#saveType").val();
	var currentNode = $("#currentNode").val();
	
	var data = result.data;
	if(data.error=="1"){
		layer.msg(data.message);
		return false;
	}
	
	var lossMainId = data.id;// 当前ID
	var flowTaskId = data.flowTaskId;// 工作流ID
	if (saveType == "save") {
		layer.confirm('暂存成功', {
			btn: ['确定'] //按钮
		}, function(){
			window.location.reload();
		});
	} else {
		var url = "/claimcar/proploss/submitNextPage.do?lossMainId="
				+ lossMainId + "&currentNode=" + currentNode + "&flowTaskId="
				+ flowTaskId;
		layer.open({
			type : 2,
			closeBtn:0,
			title : "定损提交",
			area : [ '75%', '50%' ],
			fix : true, // 不固定
			maxmin : false,
			content : url,
		});
	}
}

/**
 * 点击暂存和提交时 促发的函数
 */
function save1(saveType) {

	$("#saveType").val(saveType);
	$('#defossform').submit();
	if(saveType=="submitLoss"){
		var lossType = $("#lossType").val();// 获取损失方
		var amount = parseFloat(0);
		if(isBlank($("#amount").val())){
			amount = parseFloat($("#amount").val());
		}
		var sum = parseFloat($("#SumDefloss").val());
		var haveKindD = $("#haveKindD").val();
		if (lossType == "1" && haveKindD =="1") {
			if (amount > sum) {
				layer.msg('财产定损总金额超过了该报案车上货物责任险保额!');
			}
		}
	}
}

/** 初始化页面 */
function initPropPage() {
	var status = $("#handleStatus").val();
	var sumDefloss = $("input[name='prpLdlossPropMainVo.sumDefloss']").val();
	var sumLossFee = $("input[name='prpLdlossPropMainVo.sumLossFee']").val();
	var defRescueFee = $("input[name='prpLdlossPropMainVo.defRescueFee']").val();
	if(sumDefloss == "") $("input[name='prpLdlossPropMainVo.sumDefloss']").val(0);
	if(sumLossFee == "") $("input[name='prpLdlossPropMainVo.sumLossFee']").val(0);
	if(defRescueFee == "") $("input[name='prpLdlossPropMainVo.defRescueFee']").val(0);
	if (Number(status)==3) {// 已处理
		$("body textarea,button,select").each(function() {
			$(this).attr("disabled", "disabled");
		});
		$("body input").each(function() {
			$(this).attr("disabled", "disabled");
				if ($(this).attr("type") == "button"
					|| $(this).attr("type") == "submit") {
//					$(this).addClass("btn-disabled");
					$(this).hide();
			}
		});
	}else{
		//接收未处理任务
		initCheckAndDef();
	}
}

/** 接收未处理任务 页面校验函数 */
function initCheckAndDef() {
	var acceptFlag = $("#acceptFlag").val();
	if (acceptFlag == '0') {
		if(!isEmpty($("#oldClaim"))){
			layer.confirm('该案件为旧理赔迁移案件，是否继续处理?', {
				btn : [ '确定', '取消' ]
			},function(){
				acceptTask();
			},function(){
				$("body textarea").eq(1).attr("disabled", "disabled");
				$("body input").each(function() {
					$(this).attr("disabled", "disabled");
				});
			});
		}else{
			acceptTask();
		}		
	}
}

function acceptTask(){
	layer.confirm('是否确定接收此任务?', {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function(index) {
		layer.load(0, {shade : [0.8, '#393D49']});
		var flowTaskId = $("#flowTaskId").val();
		$.post("/claimcar/proploss/acceptDefloss?flowTaskId="
				+ flowTaskId, function(jsonData) {
			if (jsonData.data != "1") {
				layer.alert("接收任务失败," + jsonData.data);
				$("body textarea").eq(1).attr("disabled", "disabled");
				$("body input").each(function() {
					$(this).attr("disabled", "disabled");
				});
			} else {
				layer.close(index);
				window.location.reload();
			}
		});

	}, function() {
		$("body textarea").eq(1).attr("disabled", "disabled");
		$("body input").each(function() {
			$(this).attr("disabled", "disabled");
		});
	});
}
		
/**
 * 计算 财产损失项目总和
 */
function calSumLossFee() {
	var items = $("#tab_prop input[name$='lossItemName']");// 确定总共有多少行
	var sum = 0;
	$(items).each(function(i) {
		// 获取数量 获取单价 获取残值 计算总价
		var sumPrice = parseFloat(0.00);
		// 数量
		var number = $("input[name='prpLdlossPropMainVo.prpLdlossPropFees["+ i + "].lossQuantity']").val();
			if (isNaN(number)||number=="") {
				
				number = 0;
			}
		// 单价
		var price = $("input[name='prpLdlossPropMainVo.prpLdlossPropFees["+ i + "].unitPrice']").val();
			if (isNaN(price)||price=="") {
				price = 0;
			}
		// 获取残值
		var recyclePrice = $("input[name='prpLdlossPropMainVo.prpLdlossPropFees["+ i + "].recyclePrice']").val();
			if (isNaN(recyclePrice)||recyclePrice=="") {
				recyclePrice = 0;
			}
			sumPrice = parseFloat(number * price - recyclePrice).toFixed(2);
				if (sumPrice <0) {
					layer.msg("残值金额不能大于该财产总金额！");
					recyclePrice =0;
					$("input[name='prpLdlossPropMainVo.prpLdlossPropFees["+ i + "].recyclePrice']").val(0);
					sumPrice = parseFloat(number * price - recyclePrice).toFixed(2);
				}
			// 设置单元总和
			$("input[name='prpLdlossPropMainVo.prpLdlossPropFees["+ i + "].sumDefloss']").val(sumPrice);
				sum = sum + parseFloat(sumPrice);
			});
	$("#SumDefloss").val(sum);
}

// 选择损余回收 单选按钮时 后面的 的文本框变为 只读,取消选择的时候后面文本框变为可操作
function recycleFlag(item) {
	var ch = $(item).attr("name").split("]")[0];
	var index = ch.split("[")[1];
	// 用name选择器获取
	if (typeof ($(item).attr("checked")) == "undefined") {
		$(item).attr("checked", "checked");
		$(
				"input[name='prpLdlossPropMainVo.prpLdlossPropFees[" + index
						+ "].recyclePrice']").attr("readonly", "readonly");
		$(
				"input[name='prpLdlossPropMainVo.prpLdlossPropFees[" + index
						+ "].recyclePrice']").val("0");
		$(
				"input[name='prpLdlossPropMainVo.prpLdlossPropFees[" + index
						+ "].recycleFlag']").val("1");
	} else {
		$(
				"input[name='prpLdlossPropMainVo.prpLdlossPropFees[" + index
						+ "].recyclePrice']").removeAttr("readonly");
		$(item).removeAttr("checked");
		$(
				"input[name='prpLdlossPropMainVo.prpLdlossPropFees[" + index
						+ "].recycleFlag']").val("0");
	}

	var items = $("#tab_prop input[name$='lossItemName']");// 确定总共有多少行
	var sum = 0;
	$(items).each(
			function(i) {
				// 获取数量 获取单价 获取残值 计算总价
				var sumPrice = 0;
				// 数量
				var number = $(
						"input[name='prpLdlossPropMainVo.prpLdlossPropFees["
								+ i + "].lossQuantity']").val();
				if (isNaN(number)) {
					number = 0;
				}
				// 单价
				var price = $(
						"input[name='prpLdlossPropMainVo.prpLdlossPropFees["
								+ i + "].unitPrice']").val();
				if (isNaN(price)) {
					price = 0;
				}
				// 获取残值
				var recyclePrice = $(
						"input[name='prpLdlossPropMainVo.prpLdlossPropFees["
								+ i + "].recyclePrice']").val();
				if (isNaN(recyclePrice)) {
					recyclePrice = 0;
				}

				sumPrice = number * price - recyclePrice;
				if (sumPrice <= 0) {
					sumPrice = 0;
				}
				// 设置单元总和
				$(
						"input[name='prpLdlossPropMainVo.prpLdlossPropFees["
								+ i + "].sumDefloss']").val(sumPrice);
				sum = sum + parseFloat(sumPrice);
			});
	$("#SumDefloss").val(sum);
}

function checkDate() {
	var deflossDate = $("#deflossDate").val();// 定损日期
	var damageDate = $("#damageDate").val();// 出险日期
	if (compareFullDate(deflossDate, damageDate) < 0) {
		layer.alert("定损日期不能早于出险时间");
		return false;
	}
	return true;
}

/*
 * 比较两个字符串日期
 */
function compareFullDate(date1, date2) {
	var strValue1 = date1.split('-');
	var date1Temp = new Date(strValue1[0], parseInt(strValue1[1], 10) - 1,
			parseInt(strValue1[2], 10));

	var strValue2 = date2.split('-');
	var date2Temp = new Date(strValue2[0], parseInt(strValue2[1], 10) - 1,
			parseInt(strValue2[2], 10));
	if (date1Temp.getTime() == date2Temp.getTime())
		return 0;
	else if (date1Temp.getTime() > date2Temp.getTime())
		return 1;
	else
		return -1;
}

var layIndex;
function initSubRisk(element) {
	var params = {
		"riskCode" : "1101"
	};
	$.ajax({
		url : "/claimcar/defloss/initSubRisk.ajax",
		type : "post",
		data : params,
		success : function(htmlData) {
			// initPopover(element, htmlData);
			layIndex = layer.open({
				type : 1,
				skin : 'layui-layer-rim', // 加上边框
				area : [ '230px', '270px' ], // 宽高
				content : htmlData
			});
		}
	});
}

function closePop() {
	layer.close(layIndex);
}
function setSubRisk() {

	var $tbody = $("#subRiskTbody"); 
	// 获取当前行号
	var size = $("#propFeeSize").val();
	// 获取当前有多少行
	var registNo = $("#registNo").val();
	//能否发起损余回收标志
	var flag = $("#flag").val();

	var params = {
		"registNo" : registNo,
		"size" : size,
		"flag" : flag
	};
	var url = "/claimcar/proploss/initProp_Item.ajax";
	$.post(url, params, function(result) {
		$tbody.append(result);
		$("#propFeeSize").val(parseInt(size) + 1);

	});
	defaultRecyclePrice();
}

function delSubRisk(element) {
	// 判断被点击删除损失项核损小计是否不为null不为空代表是定损修改或者核损退回来的损失项。为null代表是新增的可以删除

	var index = $(element).attr("name").split("_")[1];// 下标

	// 获取损失项目的核损小计
	var f = $("#prpLdlossPropFees_" + index+ "_sumVeriLoss").val();
	if (f != null) {
		layer.msg('该任务为退回定损不能删除原来损失项！', {
			icon : 1,
			time : 5000
		// 2秒关闭（如果不配置，默认是3秒）
		});
		return false;
	}

	var proposalPrefix = "prpLdlossPropMainVo.prpLdlossPropFees";
	var $parentTr = $(element).parent().parent();
	var $propFeeSize = $("#propFeeSize");// 附加险条数
	var propFeeSize = parseInt($propFeeSize.val(), 10);// 原附加险条数
	$propFeeSize.val(parseInt(propFeeSize) - 1);// 删除一条

	delTr(propFeeSize, index, "prpLdlossPropFeeVo_", proposalPrefix);

	$parentTr.remove();
	calSumLossFee();// 重新计算
}

$("#returnMain").click(function() {
	history.back();

});

function delInfor() {
	layer.msg('该任务为退回定损不能删除原来损失项！', {
		icon : 1,
		time : 5000
	// 2秒关闭（如果不配置，默认是3秒）
	});
}

$("#claimRemark").click(function() {
	var bussNo = $("#registNo").val();
	createRegistMessage(bussNo, "DLProp","");
});


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


function defaultFee(element){
	var defaultFee = $(element).val();
	if(defaultFee == ""){
		$(element).val(0);
	}
}

function defaultRecyclePrice(){
	$("input[name$='recyclePrice']").each(function(){
		if($(this).val()==""){
			$(this).val(0);
		}
	})
	
}
