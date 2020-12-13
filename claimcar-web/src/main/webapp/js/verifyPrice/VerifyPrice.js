var rowNum =10;//table 页面保留行数
$(function(){
	changeOpinion(false);
	
	hiddenData("componentBody");
	hiddenData("materialBody");
	//审核没权限，审核通过按钮置灰
	if($("#verifyPassFlag").val() == "false" && !isEmpty($("#submit"))){
		$("#submit").attr("disabled","disabled");
		$("#submit").addClass("btn-disabled");
	}
	
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
	
	//快处快赔不进精友，核价金额手动更改
	if($("#certainType").val()=="06"){
		$("#jyCertain").css("display","none");
	}
	
	var ajaxEdit = new AjaxEdit($('#verifyPform'));
	ajaxEdit.targetUrl = "/claimcar/defloss/addVerifyPrice.do"; 
	ajaxEdit.beforeSubmit= beforeSaveCheck;
	ajaxEdit.afterSuccess = saveVerifyPriceAfter;
	//绑定表单
	ajaxEdit.bindForm();	
});
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

function save(obj,saveType){
	disabledSec(obj,1);
	
	if(saveType =="submitVprice" && $("#verifyPassFlag").val() == "false"){
		layer.msg("总金额超过您的权限，请提交上级！");
		return;
	}
	var sumVeripLoss = parseFloat($("#sumVeripLoss").val());
	var sumCompFee = parseFloat($("#sumCompFee").val());
	var sumMatFee = parseFloat($("#sumMatFee").val());
	var sumManageFee=parseFloat($("#sumManageFee").val());
	var sumRemnant = parseFloat($("input[name='lossCarMainVo.sumRemnant']").val());
	if(saveType!="save" && parseFloat(sumVeripLoss.toFixed(2)) > parseFloat((sumCompFee + sumMatFee - sumRemnant + sumManageFee).toFixed(2))){
		layer.msg("核价总金额不能大于定损总金额");
		return;
	}
	var code = $("input[name='claimTextVo.opinionCode']:checked").val();
	//意见选择非"同意核价"，审核通过时，审核状态作为不同意核价
	if(!isBlank(code) && code!="a" && saveType == "submitVloss" ){
		$("#saveType").val("noPassVerip");
	}else{
		$("#saveType").val(saveType);
	}
	$("#verifyPform").submit();
}

function beforeSaveCheck(){
	saveType = $("#saveType").val();
	if(saveType!="save"){
		var opinionCode = $("input[name='claimTextVo.opinionCode']:checked").val();
		if(opinionCode== null ){
			layer.msg("请录入核价意见");
			return false;
		}
		var chkFlag =false;
		$('#contentDiv input[name="claimTextVo.contentCode"]:checked').each(function(){
			chkFlag = true ;
		});
		if(!chkFlag){
			layer.msg("请勾选意见内容！");
			return false; 
		}
		//审核通过,校验 核价总金额已达到车损险保额X%
		if(saveType =="submitVprice"){
			var deflossCarType = $("#deflossCarType").val();//标的车
			if(deflossCarType=="1"){
				var carAmount = parseFloat($("#carAmount").val());
				var sumpaidDef =  parseFloat($("#sumpaidDef").val());
				var otherAmount = parseFloat($("#otherAmount").val());
				var sumLossFee = parseFloat($("#sumVeripLoss").val());
				sumLossFee = sumLossFee + sumpaidDef + otherAmount;
				var rate = Math.round(sumLossFee*100/carAmount,2);
				if(rate > 60){
					if(!window.confirm("核价总金额已达到车损险保额"+rate+"%")){
						return false;;
					}
				} 
			}
		}
	}
}; 

function saveVerifyPriceAfter(result){
	var saveType = $("#saveType").val();
	var currentNode = $("#currentNode").val();
	var dataArr = result.data.split(",") ;
	var lossMainId = dataArr[0];
	var flowTaskId = dataArr[1];
	
	if(saveType=="save"){
		layer.confirm('暂存成功', {
			btn: ['确定'] //按钮
		}, function(index){
			layer.close(index);
			//window.location.reload();
		});
	}else{
		var url = "/claimcar/defloss/submitNextPage.do?lossMainId="+lossMainId+"&currentNode="+currentNode+
					"&flowTaskId="+flowTaskId ;
		layer.open({
			type: 2,
			closeBtn:0,
			title: "核价提交",
			area: ['75%', '50%'],
			fix: true, //不固定
			maxmin: false,
			content: url,
	 	});
	}
}

function refreshFee(){
	var registNo = $("#registNo").val();
	var lossMainId = $("#lossMainId").val();
	$.ajax({
		url : "/claimcar/defloss/refreshFee.ajax", //后台刷新
		type : 'post', // 数据发送方式
		data : {"lossMainId" : lossMainId,"registNo" : registNo,"operateType" : "verifyPrice"}, // 要传递的数据
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
		//进精友前 把意见清空
		var opinionCode = $("input[name='claimTextVo.opinionCode']");
		opinionCode.attr("checked",false);  
		var lossMainId = $("#lossMainId").val();
		var currentNode = $("#currentNode").val();
			var strURL ="/claimcar/defloss/enterFittingSys.do?carMainId="+lossMainId+"&operateType=verifyPrice&nodeCode="+currentNode; 
		//var strURL ="/claimcar/defLossFittings/queryDefLoss.do?lossNo="+lossMainId+"&operateType=verifyPrice";
		//strURL="/claimcar/defloss/verifyPriceTest.do";
		window.open(strURL, "打开配件系统",'width=1010,height=670,top=0,left=0,toolbar=0,location=0,directories=0,menubar=0,scrollbars=1,resizable=1,status=0');
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

function changeOpinion(flag){
	var code = $("input[name='claimTextVo.opinionCode']:checked").val();
	if(flag){
		var items = $("#contentDiv").children("div");
		$(items).each(function(){
			$(this).css('display','none');
			$(this).find("input[type='checkbox']").removeAttr("checked");
		});
		$("#description").val("");
	}
	
	var blockDiv ="#content_"+code;
	$(blockDiv).css('display','block');
	
	$("#buttonDiv").find("input[type='submit']").removeAttr("disabled");
	$("#buttonDiv").find("input[type='submit']").removeClass("btn-disabled");
	
	if(code=="a"){//同意 btn-noagree btn-agree
		$("#buttonDiv .btn-noagree").attr("disabled","disabled");
		$("#buttonDiv .btn-noagree").addClass("btn-disabled");
	}else{
		$("#audit").attr("disabled","disabled");
		$("#audit").addClass("btn-disabled");
	}
	
	//审核没权限，审核通过按钮置灰
	if($("#verifyPassFlag").val() == "false"){
		$("#submit").attr("disabled","disabled");
		$("#submit").addClass("btn-disabled");
	}
	
}

function contentClick(){
	var content = "";
    $('input[name="claimTextVo.contentCode"]:checked').each(function(){
    	content += $(this).parent().text()+",";
    });
    var sumVeriFee = $("#sumVeripLoss").val();
    if($("#jyFlag").val()=="0"){
    	sumVeriFee = $("#newSumLossFee").val(); 
    }
   
    var c_index = content.lastIndexOf(',');
    content = content.substring(0, c_index);
    
    content +="(核价金额:"+sumVeriFee+")";
    $("#description").val(content);
}

function exportComp	(btn){
	location.href="/claimcar/defloss/exportComp.do?id="+btn;
}
//核价处理页面零配件更换次数的链接
var Index = null;
function LinkCase(compCode,frameNo) {
	var url = "/claimcar/defloss/linkCase.do?compCode=" + compCode +"&frameNo=" +frameNo;
	if (Index == null) {
		index = layer.open({
			type : 2,
			title : "*相关报案号*",
			shadeClose : true,
			scrollbar : false,
			skin : 'yourclass',
			area : [ '50%', '50%' ],
			content : url,
			end : function() {
				Index = null;
			}
		});
	}
}


function countVeriPLoss(){
	var sumVeripCompFee = parseFloat(0);
	var sumVeripMatFee = parseFloat(0);
	var sumVeripRemnant = parseFloat(0);
	
	if(!isBlank($("#sumVeripCompFee").val())){
		sumVeripCompFee = parseFloat($("#sumVeripCompFee").val());
	}
	if(!isBlank($("#sumVeripMatFee").val())){
		sumVeripMatFee = parseFloat($("#sumVeripMatFee").val());
	}
	if(!isBlank($("#sumVeripRemnant").val())){
		sumVeripRemnant = parseFloat($("#sumVeripRemnant").val());
	}
	
	var sumVeripLoss = sumVeripCompFee+sumVeripMatFee-sumVeripRemnant;
	$("#sumVeripLoss").val(sumVeripLoss);
}