
var rowNum =10;//table 页面保留行数
$(function(){
	changeOpinion(true);
	
	if($("#handleStatus").val()=='3'){//已处理
		$(":input[type!='button']").attr("disabled","disabled");
		//已经处理打开进入配件系统（定损查勘接口）
		if($("#jy2Flag").val()!='1'){
			$("#jyButton").attr("disabled","disabled");
			$("#jyButton").addClass("btn-disabled");
		}
		/*$("#jyButton").attr("disabled","disabled");
		$("#jyButton").addClass("btn-disabled");*/
	}
	var riskCode = $("#lossCarMainRiskCode").val();
	var deflossCarType = $("#deflossCarType").val();
	if(riskCode == '1101' || deflossCarType == "3"){
		$("#isGlassBrokenAndThird").hide();
	}
	//隐藏更多的行
	hiddenData("componentBody");
	hiddenData("materialBody");
	hiddenData("outRepairBody");
	hiddenData("repairBody");
	hiddenData("chargeTbody");
	// $(".editFileRadio").attr("disabled","true");//这里
	$("input[name='subrogationMain.subrogationFlag']").attr("disabled","disabled");
	
	//审核没权限，审核通过按钮置灰
	if($("#verifyPassFlag").val() == "false" && !isEmpty($("#submit"))){
		$("#submit").attr("disabled","disabled");
		$("#submit").addClass("btn-disabled");
	}
	
	//初始化时附加险总金额赋值
	var sumRiskFee = parseFloat(0.00);
	$("#subRiskTbody input[name$='veriSubRiskFee']").each(function(){
		var veriSubRiskFee=$(this).val();
		if(veriSubRiskFee =="" || isNaN(veriSubRiskFee) ){
			veriSubRiskFee=parseFloat(0.00);
		}
		sumRiskFee = sumRiskFee + parseFloat(veriSubRiskFee);
	});
	$("#sumVeriSubRiskFee").val(sumRiskFee);
	//ignore="ignore"
	if($("#cetainLossType").val()=="05"){//定损方式是无损失时，放开残值金额和合计录入
		$("#sumVeriCompFee").removeAttr("readonly");
		$("#sumVeriCompFee").attr("datatype","amount");
		$("#sumVeriCompFee").attr("ignore","ignore");
		
		$("#sumVeriMatFee").removeAttr("readonly");
		$("#sumVeriMatFee").attr("datatype","amount");
		$("#sumVeriMatFee").attr("ignore","ignore");
		
		$("#sumVeriRepairFee").removeAttr("readonly");
		$("#sumVeriRepairFee").attr("datatype","amount");
		$("#sumVeriRepairFee").attr("ignore","ignore");
		if($("#sumVeriOutFee").length>0){
			$("#sumVeriOutFee").removeAttr("readonly");
			$("#sumVeriOutFee").attr("datatype","amount");
			$("#sumVeriOutFee").attr("ignore","ignore");
		}
		$("#sumVeriRemnant").removeAttr("readonly");
		$("#sumVeriRemnant").attr("datatype","amount");
		
		$("#sumVeriLossFee").removeAttr("readonly");
		$("#sumVeriOutFee").attr("datatype","amount");
	}
	
	var ajaxEdit = new AjaxEdit($('#verifyform'));
	ajaxEdit.targetUrl = "/claimcar/defloss/addVerifyLoss.do";
	ajaxEdit.beforeSubmit= saveCheck;
	ajaxEdit.afterSuccess = saveVerifyLossAfter;
	
	//绑定表单
	ajaxEdit.bindForm();
	
	
	
});

function save(obj,saveType){
	disabledSec(obj,1);
	if($("#cetainLossType").val()=="05"){//定损方式是无损失时，放开残值金额和合计录入
		if($("#sumVeriRemnant").val()>0 || $("#sumVeriRescueFee").val()>0){
			layer.msg("无损失,残值金额和施救费金额必须为0!");
			return;
		}
		
		if($("#sumVeriCompFee").val()>0 || $("#sumVeriMatFee").val()>0 || $("#sumVeriRepairFee").val()>0){
			layer.msg("无损失,零部件更换,修理和辅料金额必须为0!");
			return;
		}
		
		if($("#sumVeriOutFee").length>0){
			if(!isBlank($("#sumVeriOutFee")) && $("#sumVeriOutFee").val()>0 ){
				layer.msg("无损失，外修金额必须为0!");
				return;
			}
		}
		
		$("#sumVeriRemnant").removeAttr("readonly");
		$("#sumVeriLossFee").removeAttr("readonly");
	}
	
	//核损金额总金额小于定损总金额
	var sumVeriLossFee = parseFloat($("#sumVeriLossFee").val());
	var sumLossFee = parseFloat($("#sumLossFee").val());
	if(saveType!="save" && sumVeriLossFee>sumLossFee){
		layer.msg("核损总金额不能大于定损总金额！");
		clearText();
		return ; 
	}
	
	if(saveType =="submitVloss" && $("#verifyPassFlag").val() == "false"){
		layer.msg("总金额超过您的权限，请提交上级！");
		return;
	}
	
	// 勾选复检不允许退回定损
	if (saveType == "backLoss" && $("#carReCheckFlag").is(":checked")) {
		layer.msg("零部件已勾选复检，若要退回定损务必先进入配件系统取消复检！");
		return ;
	}
	
//	if(saveType =="toReLoss" && $("#verifyPassFlag").val() == "false"){
//		layer.msg("总金额超过您的权限，不能发起复检,请取消！");
//		return;
//	}
	
	$("#saveType").val(saveType);
	$("#verifyform").submit();
}

function saveCheck(){
	var saveType = $("#saveType").val();
	var jyFlag = $("#jyFlag").val();
	if(saveType!="save" && jyFlag!=='1'){
		var opinionCode = $("input[name='claimTextVo.opinionCode']:checked").val();
		if(opinionCode == null ){
			layer.msg("请录入核损意见");
			return false;
		}
		var chkFlag = false;
		$('#contentDiv input[name="claimTextVo.contentCode"]:checked').each(function(){
			chkFlag = true ;
		});
		if(!chkFlag){
			layer.msg("请勾选意见内容！");
			return false; 
		}
		
		//审核通过,校验 核损总金额已达到车损险保额X%
		if(saveType =="submitVloss"){
			var deflossCarType = $("#deflossCarType").val();//标的车
			if(deflossCarType=="1"){
				var carAmount = parseFloat($("#carAmount").val());
				var sumpaidDef =  parseFloat($("#sumpaidDef").val());
				var sumLossFee = parseFloat($("#sumVeriLossFee").val());
				sumLossFee = sumLossFee + sumpaidDef ;
				var rate = Math.round(sumLossFee*100/carAmount,2);
				if(rate > 60){
					if(!window.confirm("核损总金额已达到车损险保额"+rate+"%")){
						return false;;
					}
				} 
			}
		}
	}
	
	if(!validFee()){
		return false; 
	}
	
	//暂存不校验，改为提交的时候校验
	//核损金额总金额小于定损总金额
	/*var sumVeriLossFee = parseFloat($("#sumVeriLossFee").val());
	var sumLossFee = parseFloat($("#sumLossFee").val());
	if(sumVeriLossFee>sumLossFee){
		layer.msg("核损总金额不能大于定损总金额！");
		return false; 
	}*/
	
	
}; 

/**
 * 保存后方法
 * @param result
 */
function saveVerifyLossAfter(result){
	var saveType = $("#saveType").val();
	var currentNode = $("#currentNode").val();
	var dataArr = result.data.split(",") ;
	var lossMainId = dataArr[0];
	var flowTaskId = dataArr[1];
	var jyFlag = $("#jyFlag").val();
	if(jyFlag=="1"){
		$("#jyFlag").val('0');
		var flowTaskId = $("#flowTaskId").val();
		var lossMainId = $("#lossMainId").val();
		var currentNode = $("#currentNode").val();
		var strURL ="/claimcar/defloss/enterFittingSys.do?carMainId="+lossMainId+"&operateType=verifyLoss&nodeCode="+currentNode ; 
		window.open(strURL, "打开配件系统",'width=1010,height=670,top=0,left=0,toolbar=0,location=0,directories=0,menubar=0,scrollbars=1,resizable=1,status=0');
		
	}
	else if(saveType=="save"){
		layer.confirm('暂存成功', {
			btn: ['确定'] //按钮
		}, function(index){
			layer.close(index);
			//window.location.reload();
		});
	}else if(saveType == "toRecheck"){//复勘
		var lossMainId = $("#lossMainId").val();
		var flowTaskId = $("#flowTaskId").val();
		var url = "/claimcar/defloss/submitNextPage.do?lossMainId="+lossMainId+"&auditStatus="+saveType+
			"&flowTaskId="+flowTaskId ;
		layer.open({
			type: 2,
			closeBtn:0,
			title: "核损提交",
			area: ['75%', '50%'],
			fix: true, //不固定
			maxmin: false,
			content: url
		});
	}else{
		var url = "/claimcar/defloss/submitNextPage.do?lossMainId="+lossMainId+"&currentNode="+currentNode+
					"&flowTaskId="+flowTaskId ;
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

//进精友前先较验 
function isSaveInfoBeforeEnter(){

	if($("#handleStatus").val()=='3'){//已处理
		//定损查看
		var jyRegistNo = $("#registNo").val();
		var jyId = $("#lossMainId").val();
		jyViewData(jyRegistNo,jyId);
	}else{
		showMore("componentBody");
		showMore("materialBody");
		showMore("outRepairBody");
		showMore("repairBody");
		//进精友前 把意见清空
		var opinionCode = $("input[name='claimTextVo.opinionCode']");
		opinionCode.attr("checked",false);  
		$("#jyFlag").val('1');
		$("#verifyform").submit();
	}
}
//清除定损清单数据 table下的所有行
function clearTableRow(tablename){
	var tbody =$(tablename);
	tbody.find("tr").remove();
}
//插入最新的精友数据
function insertTableRow(tablename,componentStr){
	var tbody = $(tablename);
	tbody.append(componentStr);
}

//进入精友刷新 也要调用此处 复检是根据代码
function changeOpinion(initFlag){
	var code = $("input[name='claimTextVo.opinionCode']:checked").val();
	if(isBlank(code)){
		return;
	}
	
	if(initFlag == false){
		var items = $("#contentDiv").children("div");
		$(items).each(function(){
			$(this).css('display','none');
			$(this).find("input[type='checkbox']").removeAttr("checked");
		});
		$("#description").val("");
	}
	
	var blockDiv ="#content_"+code;
	$(blockDiv).css('display','block');
	var reLossFlag = $("#reLossFlag").val();//0 初始化  1 已发起复检 2核价不同意 
	$("#buttonDiv").find("input[type='submit']").removeAttr("disabled");
	$("#buttonDiv").find("input[type='submit']").removeClass("btn-disabled");
	
	if($("#carReCheckFlag").is(":checked")){//是否复检标志
		if($("#reLoss").length>0 && reLossFlag!='1'){//复检按钮存在 并且没有发起过复检 只能操作复检和暂存按钮
			$("#buttonDiv").find("input[type='submit']").attr("disabled","disabled");
			$("#buttonDiv").find("input[type='submit']").addClass("btn-disabled");
			$("#reLoss").removeAttr("disabled");
			$("#reLoss").removeClass("btn-disabled");
			$("#save").removeAttr("disabled");
			$("#save").removeClass("btn-disabled");
			return ;
		}
		
		$("#reLoss").addClass("btn-agree");
	}else{
		$("#reLoss").attr("disabled","disabled");
		$("#reLoss").addClass("btn-disabled");
		$("#reLoss").removeClass("btn-agree");
	}
	
//	if($("#reLoss").length>0){//!=undefined
//		$("#reLoss").attr("disabled","disabled");
//		$("#reLoss").addClass("btn-disabled");
//	}
	
	if(code=="a"){//核损同意 btn-noagree btn-agree
		$("#buttonDiv .btn-noagree").attr("disabled","disabled");
		$("#buttonDiv .btn-noagree").addClass("btn-disabled");
	}else{
		$("#buttonDiv .btn-agree").attr("disabled","disabled");
		$("#buttonDiv .btn-agree").addClass("btn-disabled");
	}
	//审核没权限，审核通过按钮置灰
	if($("#verifyPassFlag").val() == "false"){
		$("#submit").attr("disabled","disabled");
		$("#submit").addClass("btn-disabled");
	}
	
}


function refreshFee(){
	var registNo = $("#registNo").val();
	var lossMainId = $("#lossMainId").val();
	$.ajax({
		url : "/claimcar/defloss/refreshFee.ajax", //后台刷新
		type : 'post', // 数据发送方式
		data : {"lossMainId" : lossMainId,"registNo" : registNo,"operateType" : "verifyLoss"}, // 要传递的数据
		async : false, 
		success : function(htmlData) {// 回调方法，可单独定义
			$("#refreshDiv").empty();
			$("#refreshDiv").append(htmlData);
			return false;
		},
		error : function(htmlData){
			layer.alert("刷新失败！");
			return false;
		}
	});
}

function contentClick(){
	var content = "";
    $('input[name="claimTextVo.contentCode"]:checked').each(function(){
    	content += $(this).parent().text()+",";
    });
	var sumVeriSubRiskFee =$("#sumVeriSubRiskFee").val();
    var sumVeriFee = $("#sumVeriLossFee").val();
//    if($("#jyFlag").val()=="0"){
//    	sumVeriFee = $("#newSumLossFee").val();
//    }
    
    var c_index = content.lastIndexOf(',');
    content = content.substring(0, c_index);
    if(sumVeriSubRiskFee=="" || isNaN(sumVeriSubRiskFee)){
    	sumVeriSubRiskFee= parseFloat(0.00);
    }
    if(sumVeriFee=="" || isNaN(sumVeriFee)){
    	sumVeriFee= parseFloat(0.00);
    }
 
    var sum=parseFloat(sumVeriFee)+parseFloat(sumVeriSubRiskFee);
   
    var opinionCode = $("input[name='claimTextVo.opinionCode']:checked").val();
    
    if(opinionCode =="a"){
    	content +="(核损金额:"+sum+")";
    }
    $("#description").val(content);
}

//费用赔款信息 合计	
function calSumVeriChargeFee(field){
	var pre = $(field).attr("name").split("]")[0];
	var index = pre.split("[")[1];//下标
	var objName = "#lossChargeVos_"+index+"_chargeFee";
	var oldChargeFee = parseFloat($(objName).val());
	var veriChargeFee = parseFloat($(field).val());
	if(isNaN(veriChargeFee)){
		return;
	}
	if(veriChargeFee>oldChargeFee){
		layer.msg("核损金额不能大于定损金额");
		$(field).val("");
		return;
	}
	
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

function validFee(){
	var retflag = true;
	var items =$("#subRiskTbody input[name$='veriSubRiskFee']");
	$(items).each(function(){
		var subRiskFee = $(this).val();
		var name = $(this).attr("name");
		var pre = name.split("]")[0];
		var index = pre.split("[")[1];//下标
		
		var objName =  "#prpLDlossCarSubRisks_"+index+"_subRiskFee";
		var oldRiskFee = parseFloat($(objName).val());
		if(subRiskFee>oldRiskFee){
			var feeObj = "#prpLDlossCarSubRisks_"+index+"_kindName";
			layer.msg($(feeObj).val()+"的核损金额不能大于定损金额");
			retflag = false; 
			return false;
		}
	});
	
	if(!retflag){
		return false;
	}
	
	var items =$("#chargeTbody input[name$='veriChargeFee']");
	$(items).each(function(){
		var veriChargeFee = $(this).val();
		var name = $(this).attr("name");
		var pre = name.split("]")[0];
		var index = pre.split("[")[1];//下标
		
		var objName = "#lossChargeVos_"+index+"_chargeFee";
		var oldChargeFee = parseFloat($(objName).val());
		if(veriChargeFee>oldChargeFee){
			var feeObj = "#lossChargeVos_"+index+"_chargeName";
			layer.msg( $(feeObj).val()+"的核损金额不能大于定损金额");
			$("#sumVeriChargeFee").focus();
			retflag = false; 
			return false;
		}
	});
	
	return retflag;
}

//附加险校验

function checkSubRiskFee(field){
	var pre = $(field).attr("name").split("]")[0];
	var index = pre.split("[")[1];//下标
	var objName = "#prpLDlossCarSubRisks_"+index+"_subRiskFee";
	var oldFee = parseFloat($(objName).val());
	var subRiskFee = parseFloat($(field).val());
	//附加险总金额赋值
	var sumRiskFee = parseFloat(0.00);
	$("#subRiskTbody input[name$='veriSubRiskFee']").each(function(){
		var veriSubRiskFee=$(this).val();
		if(veriSubRiskFee =="" || isNaN(veriSubRiskFee) ){
			veriSubRiskFee=parseFloat(0.00);
		}
		sumRiskFee = sumRiskFee + parseFloat(veriSubRiskFee);
	});
	 
	$("#sumVeriSubRiskFee").val(sumRiskFee);
	//当附加险改变时，让核损意见为不选中状态
    $("input[name$='opinionCode']").each(function(){
    	$(this).prop("checked",false);
    });
   
	
	if(isNaN(subRiskFee)){
		return;
	}
	if(subRiskFee>oldFee){
		layer.msg("核损金额不能大于定损金额");
		$(field).val("");
		return;
	}
}

//定损合计	
function calSumLoss(){
	var actualValue = parseFloat(0.00);
	if($("#veriActualValue").val()!=""){
		actualValue = parseFloat($("#veriActualValue").val());
	}
	
	var sumRemnant = parseFloat(0.00);
	if($("#sumVeriRemnant").val()!=""){
		sumRemnant = parseFloat($("#sumVeriRemnant").val());
	}
	
	var otherFee = parseFloat(0.00);
	if($("#veriOtherFee").val()!=""){
		otherFee = parseFloat($("#veriOtherFee").val());
	}
	
	var sumLossFee = actualValue-sumRemnant+otherFee ;
	if(sumLossFee<0){
		layer.msg("合计金额不能小于0");
		$("#sumVeriLossFee").val("");
		return;
	}
	$("#sumVeriLossFee").val(actualValue-sumRemnant+otherFee);
	//推定全损 修改金额时重新录入核损意见
	var opinionCode = $("input[name='claimTextVo.opinionCode']");
	opinionCode.attr("checked",false);  
	$("#description").val("");
}

//隐藏更多行
function hiddenData(bodyid){
	var bodyDivId = "#"+bodyid;
	var tbody =$(bodyDivId);
	var num = rowNum - 1;
	tbody.find("tr :gt("+num+")").each(function(){
		this.style.display = "none";
		
	});//materialBDiv
	
	if(tbody.find("tr").size()> rowNum){
		var buttonId ="#"+bodyid+"Button";
		$(buttonId).css('display','block');
	}
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

function calSumOutRepiar(field){
	if(isNaN($(field).val())){
		return false;
	}
	var sumDefLoss = 0;
	$("#outRepairBody [name$='sumVeriLoss']").each(function(){
		if($(this).val()!=null&&$(this).val()!=""){
			sumDefLoss += parseFloat($(this).val());
		}
	});

	$("#sumVeriOutFee").val(sumDefLoss);
	calJySum();
}


function calJySum(){
	var sumComFee = parseFloat(0);
	var sumMatFee = parseFloat(0);
	var sumRepairFee = parseFloat(0);
	var sumOutFee = parseFloat(0);
	var sumRemnant = parseFloat(0);
	var sumVeriSubRiskFee = parseFloat(0);
	
	if(!isBlank($("#sumVeriCompFee").val())){
		sumComFee = parseFloat($("#sumVeriCompFee").val());
	}
	
	if(!isBlank($("#sumVeriMatFee").val())){
		sumMatFee = parseFloat($("#sumVeriMatFee").val());
	}
	
	if(!isBlank($("#sumVeriRepairFee").val())){
		sumRepairFee = parseFloat($("#sumVeriRepairFee").val());
	}
	
	if(!isBlank($("#sumVeriOutFee").val())){
		sumOutFee = parseFloat($("#sumVeriOutFee").val());
	}
	
	if(!isBlank($("#sumVeriRemnant").val())){
		sumRemnant = parseFloat($("#sumVeriRemnant").val());
	}
	
	if(!isBlank($("#sumVeriSubRiskFee").val())){
		sumVeriSubRiskFee = parseFloat($("#sumVeriSubRiskFee").val());
	}
	
	var sumDefLoss = sumComFee + sumMatFee + sumRepairFee + sumOutFee + sumVeriSubRiskFee -sumRemnant;
	
	$("#sumVeriLossFee").val(sumDefLoss); 
	
}
//推定全损 修改金额时重新录入核损意见
function  clearText(){
	var opinionCode = $("input[name='claimTextVo.opinionCode']");
	opinionCode.attr("checked",false);  
	$("#description").val("");
}

function countVeriLoss(){
	var sumVeriCompFee = parseFloat(0);
	var sumVeriMatFee = parseFloat(0);
	var sumVeriRepairFee = parseFloat(0);
	var sumVeriOutFee = parseFloat(0);
	var sumVeriRemnant = parseFloat(0);
	
	if(!isBlank($("#sumVeriCompFee").val())){
		sumVeriCompFee = parseFloat($("#sumVeriCompFee").val());
	}
	if(!isBlank($("#sumVeriMatFee").val())){
		sumVeriMatFee = parseFloat($("#sumVeriMatFee").val());
	}
	if(!isBlank($("#sumVeriRepairFee").val())){
		sumVeriRepairFee = parseFloat($("#sumVeriRepairFee").val());
	}
	if(!isBlank($("#sumVeriOutFee").val())){
		sumVeriOutFee = parseFloat($("#sumVeriOutFee").val());
	}
	if(!isBlank($("#sumVeriRemnant").val())){
		sumVeriRemnant = parseFloat($("#sumVeriRemnant").val());
	}
	
	var sumVeriLossFee = sumVeriCompFee+sumVeriMatFee+sumVeriRepairFee+sumVeriOutFee-sumVeriRemnant;
	$("#sumVeriLossFee").val(sumVeriLossFee);
}

var PhotoVerify_index = null;
function openPhotoVerify(nodeCode){
	var lossMainId = $("#lossMainId").val();
	var registNo = $("#registNo").val();
	var offLineHanding = $("input[name='lossCarMainVo.offLineHanding']:checked").val();
	var url = "/claimcar/defloss/openPhotoVerify.do?registNo=" + registNo+"&mainId="+lossMainId+"&offLineHanding="+offLineHanding+"&nodeCode="+nodeCode;
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
