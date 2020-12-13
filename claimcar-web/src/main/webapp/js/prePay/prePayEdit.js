$(function() {
	
	initMust();

	$("#appli").addClass("btn  btn-primary"); 
	if ($("#handlerStatus").val() == '3'||$("#handlerStatus").val()=='9') {// 处理完成或注销的节点禁用编辑
		$("body :input").attr("disabled", "disabled");
		$("#submitDiv").hide();
		$("input[name$='showButton']").each(function(){
			$(this).removeAttr("disabled");
		});
	}
	
	otherFlagCon();
	
	var rule = [{
		ele : "select[name$='.lossType']",
		datatype : "selectMust"
	}, {
		ele : "select[name$='.chargeCode']",
		datatype : "selectMust"
	}
	];
	
    
    var ajaxEdit = new AjaxEdit($('#prePayForm'));
    
	ajaxEdit.targetUrl ="/claimcar/prePay/saveOrSubmit.do";
	ajaxEdit.rules = rule;
	ajaxEdit.beforeSubmit = function(data) {
		
		layer.load(0, {
			shade : [0.8, '#393D49']
		});
		
	};
	ajaxEdit.afterSuccess = function(data) {
		location.reload();
		window.location.reload();
	};
	// 绑定表单
	ajaxEdit.bindForm();

//	$.Datatype.checkBoxMust = function(gets, obj, curform, regxp) {
//		var need = 1, numselected = curform.find("input[name='" + obj.attr("name") + "']:checked").length;
//		return numselected >= need ? true : false;
//	};
	$.Datatype.selectMust = function(gets, obj, curform, regxp) {
		var code = $(obj).val();
		if (isBlank(code)) {
			return false;
		}
	};
	
	$("body").bind("change",".payAmt",function(){
		var sumAmt = parseFloat(0.00);
		$(".payAmt").each(function(){
			var payAmt = $(this).val();
			if(isNaN(payAmt)||isBlank(payAmt)){
				payAmt = 0;
			}
			sumAmt += parseFloat(payAmt);
		});
		$("input[name='compensateVo.sumAmt']").val(sumAmt.toFixed(2));
	});
	$("input[name='file']").removeAttr("disabled");
});

// 新增费用信息行
function addPayInfo(tbodyName, sizeName,handlerStatus) {
	var load = layer.load(0, {shade : [0.8, '#393D49']});
	var $tbody = $("#" + tbodyName + "");
	var size = parseInt($("#" + sizeName + "").val());
	var registNo = $("#registNo").val();
	var policyNo = $("#policyNo").val();
   
	// var flag = $("#flag").val();
	var params = {
		"registNo" : registNo,
		"policyNo" : policyNo,
		"size" : size,
		"bodyName" : tbodyName,
		"handlerStatus":handlerStatus,
	// "flag" : flag,
	};
	var url = "/claimcar/prePay/addPreRow.ajax";
	$.post(url, params, function(result) {
		$tbody.append(result);
		$tbody.find("td .select2:last").select2();
		$tbody.find("select[name$='.lossType']").attr("datatype","selectMust");
		$tbody.find("select[name$='.chargeCode']").attr("datatype","selectMust");
		//$tbody.find("input[name$='.otherFlag']").attr("datatype","checkBoxMust");
		
		$("#" + sizeName + "").val(size + 1);
		layer.close(load);
	});
}
// 删除信息行
function delPayInfo(element, sizeName, proposalPrefix) {
	var $parentTr = $(element).parent().parent();
	var array = $(element).attr("name").split("_");
	var delBtnName = array[0];
	var IndexTr = array[1];
	var size = parseInt($("#" + sizeName + "").val());
	delTr(size, IndexTr, delBtnName + "_", proposalPrefix);
	$parentTr.remove();
	$("#" + sizeName + "").val(size - 1);// 删除一条
	//计算总预付金额
	var items = $("#IndemnityTbody input[name$='payAmt']");
	var sumFee = parseFloat(0.00);
	$(items).each(function() {
		var chargeFee = $(this).val();
		if (chargeFee == "") {
			chargeFee = parseFloat(0.00);
		}
		sumFee = sumFee + parseFloat(chargeFee);
	});
	var items2 = $("#FeeTbody input[name$='payAmt']");
	$(items2).each(function() {
		var chargeFee = $(this).val();
		if (chargeFee == "") {
			chargeFee = parseFloat(0.00);
		}
		sumFee = sumFee + parseFloat(chargeFee);
	});
	$("input[name='compensateVo.sumAmt']").val(sumFee);
}

function initChargeType(element){
	var chargeCodeStr =getChargeTypes("chargeCode");
	var params = {
		"chargeCodes" : chargeCodeStr
	};
	
	$.ajax({
		url : "/claimcar/prePay/initChargeType.ajax",
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
		checkFlagEle =$("#FeeTbody input[name$='chargeCode']");
	}
	checkFlagEle.each(function(){
		if(chargeCodeStr ==""){
			chargeCodeStr =$(this).val();
		}else{
			chargeCodeStr = chargeCodeStr + "," + $(this).val();
		}
		
	});
	return chargeCodeStr;
}

function setCharge(){
	var $tbody = $("#FeeTbody");
	showMoreChange();
	
	var indexSeriNo = $("#chargeTab [name='chargeCheckFlag']:checked");
	var param = "";
	var registNo = $("#registNo").val();
	var policyNo = $("#policyNo").val();
	var count = indexSeriNo.length;
	for(var i = 0 ; i < indexSeriNo.length; i++){
		if(i == 0){
			param = indexSeriNo.val();
		} else {
			param = $(indexSeriNo[i]).val() + "," + param; 
		}
	}
	
	var $chargeSize =$("#feeSize") ;// 附加险条数
	var size = parseInt($chargeSize.val(),10);
	
	var params = {"size":size,"chargeTypes":param,"registNo":registNo,"policyNo":policyNo};
	var url = "/claimcar/prePay/loadChargeTr.ajax";
	$.post(url,params, function(result){
		$tbody.append(result);
		$chargeSize.val(size + count );// 重新附加险条数
		$tbody.find("select[name$='.kindCode']").attr("datatype","selectMust");
	});
	closePop();
}

function closePop(){
    layer.close(layIndex);
}

//显示更多
function showMoreChange(){
	$("#FeeTbody").find("tr :gt(9)").each(function(){
		$(this).removeAttr("style"); 
		if($(this).attr("id")=="morebutton"){
			this.remove();
		}
	});
}

// 收款人选择界面打開
var payIndex = null;
function showPayCust(element) {
	var registNo = $("#registNo").val();
	var tdName = $(element).attr("name");
	var trIndex = tdName.substring(11,12);
	var url = "/claimcar/payCustom/payCustomList.do?registNo=" + registNo + "&tdName=" + tdName + "&compFlag=comp"+"&flag=1";
	if (payIndex == null) {
		payIndex = layer.open({
			type : 2,
			title : '收款人选择',
			shade : false,
			area : ['1000px', '500px'],
			content : url,
			end : function() {
				payIndex = null;
				otherFlagCon();
				paymentVatInvoiceFlagCon(trIndex);
			}
		});
	}
}

function showPayCustForFee(element) {
	var registNo = $("#registNo").val();
	var tdName = $(element).attr("name");
	var trIndex = tdName.substring(11,12);
	var url = "/claimcar/payCustom/payCustomList.do?registNo=" + registNo + "&tdName=" + tdName + "&compFlag=comp";
	if (payIndex == null) {
		payIndex = layer.open({
			type : 2,
			title : '收款人选择',
			shade : false,
			area : ['1000px', '500px'],
			content : url,
			end : function() {
				payIndex = null;
				otherFlagCon();
				chargeVatInvoiceFlagCon(trIndex);
			}
		});
	}
}


//例外原因管控
function otherFlagCon(){
	$("[name$='otherFlag']").each(function(){
		if($(this).prop("checked")==true&&$(this).val()=="1"){
			var payKind = $(this).parents("td").siblings().find("[name$='payObjectKind']").val();
			if(payKind!="2"){
				$otherSel = $(this).parents("td").next().find("select");
				$otherSel.removeAttr("disabled");
			}else{
				layer.msg("被保险人无例外原因");
				$(this).prop("checked",false);
				$(this).parent().siblings().find("[name$='otherFlag']").prop("checked",true);
			}
		}
		if($(this).prop("checked")==true&&$(this).val()=="0"){
			$otherSel = $(this).parents("td").next().find("select");
			$otherSel.attr("disabled","disabled");
			$otherSel.val("");
		}
	});
}

//是否增值专票管控
function paymentVatInvoiceFlagCon(num){
	var payKind = $("input[name$='prePayPVos["+ num +"].payObjectKind']").val();
	
	if(payKind == "6"){
		
		$("input[name$='prePayPVos["+ num +"].vatInvoiceFlag']").removeAttr("disabled","disabled");
		var vatInvoiceFlagVal = $("input[name='prePayPVos["+ num +"].vatInvoiceFlag']:checked").val(); 
		if(vatInvoiceFlagVal == "1"){
			$("#prePayPVosVatTaxRate"+ num).attr("style","");
			$("input[name$='prePayPVos["+ num +"].vatTaxRate']").removeAttr("disabled");
		}else{
			$("#prePayPVosVatTaxRate"+ num).attr("style","display:none");
			$("input[name$='prePayPVos["+ num +"].vatTaxRate']").attr("disabled","disabled");
		}
	}else{
		$("#prePayPVosVatTaxRate"+ num).attr("style","display:none");
		$("input[name$='prePayPVos["+ num +"].vatInvoiceFlag']").attr("disabled","disabled");
		$("input[name$='prePayPVos["+ num +"].vatTaxRate']").attr("disabled","disabled");
	}
	
}

function chargeVatInvoiceFlagCon(num){
	//var payKind = $("input[name$='prePayFVos["+ num +"].payObjectKind']").val();
	
    //if(payKind == "6"){
		
		$("input[name$='prePayFVos["+ num +"].vatInvoiceFlag']").removeAttr("disabled","disabled");
		var vatInvoiceFlagVal = $("input[name='prePayFVos["+ num +"].vatInvoiceFlag']:checked").val(); 
		if(vatInvoiceFlagVal == "1"){
			$("#prePayFVosVatTaxRate"+ num).attr("style","");
			$("input[name$='prePayFVos["+ num +"].vatTaxRate']").removeAttr("disabled");
		}else{
			$("#prePayFVosVatTaxRate"+ num).attr("style","display:none");
			$("input[name$='prePayFVos["+ num +"].vatTaxRate']").attr("disabled","disabled");
		}
	/*}else{
		$("#prePayFVosVatTaxRate"+ num).attr("style","display:none");
		$("input[name$='prePayFVos["+ num +"].vatInvoiceFlag']").attr("disabled","disabled");
		$("input[name$='prePayFVos["+ num +"].vatTaxRate']").attr("disabled","disabled");
	}*/
	
}

function save(auditStatus) {
	$("input[name='submitNextVo.auditStatus']").val(auditStatus);
	var sumAmt = $("input[name='compensateVo.sumAmt']").val();
	
	if(parseFloat(sumAmt)==0){
		layer.msg("本次预付金额合计不能为0");
		return;
	}
	//收款人为非被保险人，请填写例外原因
	var othFlag = true;
	//添加价税分离相关校验
	var vatFlag = true;
	$("input[name^='prePayPVos'][name$='payObjectKind']").each(function(){
		if($(this).val()!="2"){
			var idx = $(this).attr("name").substring(11,12);
			var otherFlag = $("input[name='prePayPVos["+idx+"].otherFlag']:checked").val();
			var otherCause = $("select[name='prePayPVos["+idx+"].otherCause']  option:selected").text();
			if(otherFlag =="1"&& isBlank(otherCause)){
				layer.msg("收款人为非被保险人，请填写例外原因");
				othFlag = false;
			}
		}
		//收款人为修理厂，需要校验发票税率
		if($(this).val()=="6"){
			var vatInvoiceFlag = $("input[name='prePayPVos["+idx+"].vatInvoiceFlag']:checked").val();
			var vatTaxRate = $("select[name='prePayPVos["+idx+"].vatTaxRate']").val();
			if(vatInvoiceFlag =="1"&& isBlank(vatTaxRate)){
				layer.msg("收款人为修理厂，请录入税率");
				vatFlag = false;
			}
		}
		
	});
	
	$("input[name^='prePayFVos'][name$='payObjectKind']").each(function(){
		var idx = $(this).attr("name").substring(11,12);
		//收款人为修理厂，需要校验发票税率
		if($(this).val()=="6"){
			var vatInvoiceFlag = $("input[name='prePayFVos["+idx+"].vatInvoiceFlag']:checked").val();
			var vatTaxRate = $("select[name='prePayFVos["+idx+"].vatTaxRate']").val();
			if(vatInvoiceFlag =="1"&& isBlank(vatTaxRate)){
				layer.msg("收款人为修理厂，请录入税率");
				vatFlag = false;
			}
		}
	});
	
	
	if(!othFlag){
		return;
	}
	if(!vatFlag){
		return false;
	}
	//客户识别
	/*var clickFlag = $("#appli").attr("clickFlag");
	if(parseFloat(sumAmt)>=10000){
		if(clickFlag==null||clickFlag==""){
			$("#appli").focus();
			layer.msg("因合规要求，请先在预付页面进行客户识别操作！");
			return;
		}
	}*/	
	if (auditStatus == "save") {
		$("#prePayForm").submit();
	} else {
    	//提交下一节点时，校验一下本次预付金额合计是否=all(预付赔款) + all(预付费用)
		var sumAmt = $("input[name='compensateVo.sumAmt']").val();
		var sumPayAmt = 0;
		$("input[name^='prePayPVos'][name$='payAmt']").each(function(){
			sumPayAmt = parseFloat(sumPayAmt) + parseFloat($(this).val());
		});
		$("input[name^='prePayFVos'][name$='payAmt']").each(function(){
			sumPayAmt = parseFloat(sumPayAmt) + parseFloat($(this).val());
		});
		if(parseFloat(sumAmt).toFixed(2) != parseFloat(sumPayAmt).toFixed(2)){
			layer.msg("本次预付金额合计不等于所有预付赔款加所有预付费用总和，请检查！");
			return;
		}
		var currentNode = $("#nodeCode").val();
		var riskCode = $("input[name='compensateVo.riskCode']").val();
		var comCode = $("input[name='compensateVo.comCode']").val();
		var compensateNo =$("input[name='compensateVo.compensateNo']").val();
		var registNo = $("input[name='compensateVo.registNo']").val();

		var params = {
			"currentNode" : currentNode,
			"auditStatus" : auditStatus,
			"riskCode" : riskCode,
			"comCode" : comCode,
			"sumAmt" : sumAmt,
			"registNo" : registNo,
			"compensateNo" : compensateNo
		};

		$.ajax({
			url : "/claimcar/prePay/loadSubmitVClaimNext.ajax",
			type : "post",
			data : params,
			async : false,
			success : function(htmlData) {
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
}
//判断赔款金额若小于10000，则反洗钱按钮disabled，否则反之。
/*function sumPayAmt(field) {
	var items = $("#IndemnityTbody input[name$='payAmt']");
	var sumFee = parseFloat(0.00);
	$(items).each(function() {
		var chargeFee = $(this).val();
		if (chargeFee == "") {
			chargeFee = parseFloat(0.00);
		}
		sumFee = sumFee + parseFloat(chargeFee);
	});
	if(sumFee<10000){
		$("#payCustomOpen").attr("disabled", true);
	}else{
		$("#payCustomOpen").attr("disabled", false);
	}
}*/



function changeLossType(element){
	var index = $(element).parent().parent().parent().index()-1;
	var lossType = $(element).val().split("_");
	var $lossName = $("input[name='prePayPVos["+index+"].lossName']");
	$lossName.val($(element).find("option:selected").text());
	
	//人员伤亡不能选财产损失，车财不能选死亡医疗
	if(lossType[0]=="pers"){//人不能选财产
		$("select[name='prePayPVos["+index+"].chargeCode']").find("option").each(function(){
			if($(this).val()=='01'){
				$(this).attr("disabled","disabled");
			}else{
				$(this).removeAttr("disabled");
			}
		});
	}else{//其他不能选择死亡医疗
		$("select[name='prePayPVos["+index+"].chargeCode']").find("option").each(function(){
			if($(this).val()=='02'||$(this).val()=='03'){
				$(this).attr("disabled","disabled");
			}else{
				$(this).removeAttr("disabled");
			}
		});
	}
	
}

/*//页面加载时判断赔款金额若小于10000，则反洗钱按钮disabled
$(function (){
	var items = $("#IndemnityTbody input[name$='payAmt']");
	var sumFee = parseFloat(0.00);
	$(items).each(function() {
		var chargeFee = $(this).val();
		if (chargeFee == "") {
			chargeFee = parseFloat(0.00);
		}
		sumFee = sumFee + parseFloat(chargeFee);
	});
	if(sumFee<10000){
		$("#payCustomOpen").attr("disabled", true);
	}
});*/

//摘要特殊字符校验
function checkSpecialCharactor(element,nowStr){
	var str = $(element).val();
	//var pattern = "^‘'！!#$%*+，,-./:：=？?@【】[\]_`·{|}~ ";//特殊字符
	var pattern = "^'!#$%*+,-./:=?@[\]_`{|}~ ……‘！#￥%*+，-。/：；=？@【、】——·{|}~ ";//特殊字符
	if(typeof(str) === "undefined" || str === null || str===""){
		$(element).val(nowStr);
		return false;
	}
	for(var i = 0,size = pattern.length;i<size;i++){
		var reg = pattern.substring(i, i+1);
		if(str.indexOf(reg)>-1){
			layer.alert("包含特殊字符“"+reg+"”,请核实！");
			$(element).val(nowStr);
			return false;
		}
	}
	//对于&quot和&lt和&gt不能同时出现
	var regArr = new Array();
	regArr.push("&quot");
	regArr.push("&lt");
	regArr.push("&gt");
	for(var i = 0,size = regArr.length;i<size;i++){
		var reg = regArr[i];
		if(str.toLowerCase().indexOf(reg)>-1){
			//进行转义提示
			//reg = "\\"+reg;
			alert("包含特殊字符“"+reg+"”,请核实！");
			$(element).val(nowStr);
			return false;
		}
	}
	return true;
}

function showTaxInfos(index,taxRate,taxVlaue,noTaxVlaue){
	var url = "/claimcar/compensate/showTaxInfos.ajax?index="+index+"&taxRate="+taxRate+"&taxVlaue="+taxVlaue+"&noTaxVlaue="+noTaxVlaue;
	 layer.open({
		type : 2,
		area : [ '400px', '200px' ],
		fix : false, // 不固定
		maxmin : false,
		shadeClose : true,
		scrollbar : true,
		skin : 'yourclass',
		title : "税收信息",
		content : [ url, "no" ],
	});
}

function initMust(){
	$("select[name$='invoiceType']").each(function(){
		$(this).attr("datatype","selectMust");
	});
}

function importExcel(compensateNo,bussType,feeCode,payId,payName,indexNo,bussId) {
	
	//添加附件start
	var formData = new FormData();
    for(var i=0;i<$("#Upfile"+indexNo)[0].files.length;i++) {  //循环获取上传个文件
        formData.append("file", $("#Upfile"+indexNo)[0].files[i]);
    }
    formData.append("compensateNo",compensateNo);
    formData.append("bussType",bussType);
    formData.append("feeCode",feeCode);
    formData.append("payId",payId);
    formData.append("payName",payName);
    formData.append("bussId",bussId);
    $.ajax({
        "url": "/claimcar/bill/importExcel.ajax", 
        "data" : formData,
        "dataType":"json",
        "type": "post",
        "contentType" : false, //上传文件一定要是false
        "processData":false,
        "success" : function(json) {
        	
			if (json.status !='500') {
				layer.msg("上传发票成功！",{icon:6},3000);
			}else{
				layer.msg(json.statusText,{icon:5},3000);
			}
		}
    });
   //添加附件end
}