
//选择受伤部位
function check_InjuredParts(element){
	if($(element).prop("checked")){
		add_InjuredPart($(element).val());
	}else{
		del_InjuredPart($(element).val());
	}
}

//新增受伤部位
function add_InjuredPart(injuredPart){
	var indexSeriNo = $("#injuredPart_Tbody tr");
	var injuredPartSize = indexSeriNo.length-1;
	var registNo = $("#registNo").val();
    var params = {"injuredPartSize":injuredPartSize,"registNo":registNo,"injuredPart":injuredPart};
    $.ajax({
		url : "/claimcar/loadAjaxPage/loadInjuredPartTr.ajax",
		type : "post",
		data : params,
		async : false,
		success : function(result) {
			$tbody = $("#injuredPart_Tbody");
			$tbody.find("tr:last").before(result);
			$tbody.find("select.select2").select2();
//			$tbody.find("select[name$='.injuredDiag']").attr("datatype","selectMust");
	    	if($("#woundCode").val()=="02"){
	    		$tbody.find("select[name$='.woundGrade']").attr("datatype","selectMust");
	    	}
		}
	});
}

//删除受伤部位
function del_InjuredPart(injuredPart){
//	var select = $("#injuredPart_Tbody").find("select");
	var indexSeriNo = $("#injuredPart_Tbody tr");
	var proposalPrefix="prpLDlossPersTraceVo.prpLDlossPersInjured.prpLDlossPersExts";
	var injuredPartSize = indexSeriNo.length-1;
	indexSeriNo.each(function(){
		if($(this).find("td select:first").val()==injuredPart){
			var IndexTr = $(this).index();
			delTr(injuredPartSize, IndexTr, "injuredPartDelBtn_", proposalPrefix);
			$(this).find(":input").qtip('destroy', true);
			$(this).remove();
		}
	});
}

//新增住院情况
function add_HospitalCase(element){
	
	var indexSeriNo = $("#hospitalCase_Tbody tr");
	var hospitalCaseSize = indexSeriNo.length;
	var registNo = $("#registNo").val();
	var treatSituation = $("select[name='prpLDlossPersTraceVo.prpLDlossPersInjured.treatSituation']").val();
    var params = {"hospitalCaseSize":hospitalCaseSize,"registNo":registNo};
    var url = "/claimcar/loadAjaxPage/loadHospitalCaseTr.ajax";
    $.post(url,params,function(result){
    	$("#hospitalCase_Tbody").append(result);
    	initHospitalSearch();
    	if(treatSituation == "2"){//案件类型为住院，入院时间和医院必录
    		var $Tr = $("#hospitalCase_Tbody").find("tr:last");
    		$Tr.find("input[name$='.inHospitalDate']").attr("datatype","*");
    		$Tr.find("select.areaselect").attr("datatype","*");
    	}
	});
    
}
//给医院增加下拉框，增加搜索框
function initHospitalSearch() {
	var hospitalAttr = new Array();
	$("[id$='_hospital']").each(function() {
		var hospitalId = $(this).attr("id");
		if (!isBlank(hospitalId)) {
			hospitalAttr = hospitalId.split("_");
		}
		$("#prpDHospitalarea_" + hospitalAttr[1] + "_hospital").select2();
	});
}

//删除住院情况
function del_HospitalCase(element){
	var array = $(element).attr("name").split("_");//下标
	var IndexTr = array[1];
	var indexSeriNo = $("#hospitalCase_Tbody tr");
	var proposalPrefix="prpLDlossPersTraceVo.prpLDlossPersInjured.prpLDlossPersHospitals";
	var $parentTr = $(element).parent().parent();
	var hospitalCaseSize = indexSeriNo.length;
	if(hospitalCaseSize>1){
		delTr(hospitalCaseSize, IndexTr,"hospitalCaseDelBtn_", proposalPrefix);
		$parentTr.find(":input").qtip('destroy', true);
		$parentTr.remove();
	}else{
		layer.alert('不允许删除，最少必须要有一条记录!');
	}
	
}

//新增护理人员
function add_NurseInfo(element){
	var indexSeriNo = $("#nurseInfo_Tbody tr");
	var nurseInfoSize = indexSeriNo.length;
	var registNo = $("#registNo").val();
    var params = {"nurseInfoSize":nurseInfoSize,"registNo":registNo};
    var url = "/claimcar/loadAjaxPage/loadNurseInfoTr.ajax";
    $.post(url,params,function(result){
    	$("#nurseInfo_Tbody").append(result);
	});
}

//删除护理人员
function del_NurseInfo(element){
	var array = $(element).attr("name").split("_");//下标
	var IndexTr = array[1];
	var indexSeriNo = $("#nurseInfo_Tbody tr");
	var proposalPrefix="prpLDlossPersTraceVo.prpLDlossPersInjured.prpLDlossPersNurses";
	var $parentTr = $(element).parent().parent();
	var nurseInfoSize = indexSeriNo.length;
	
	delTr(nurseInfoSize, IndexTr, "nurseInfoDelBtn_", proposalPrefix);
	$parentTr.find(":input").qtip('destroy', true);
	$parentTr.remove();
}


//新增被抚养人员
function add_RaiseInfo(element){
	var indexSeriNo = $("#raiseInfo_Tbody tr");
	var raiseInfoSize = indexSeriNo.length;
	var registNo = $("#registNo").val();
    var params = {"raiseInfoSize":raiseInfoSize,"registNo":registNo};
    var url = "/claimcar/loadAjaxPage/loadRaiseInfoTr.ajax";
    $.post(url,params,function(result){
    	$("#raiseInfo_Tbody").append(result);
	});
}

//删除被抚养人员
function del_RaiseInfo(element){
	var array = $(element).attr("name").split("_");//下标
	var IndexTr = array[1];
	var indexSeriNo = $("#raiseInfo_Tbody tr");
	var proposalPrefix="prpLDlossPersTraceVo.prpLDlossPersInjured.prpLDlossPersRaises";
	var $parentTr = $(element).parent().parent();
	var raiseInfoSize = indexSeriNo.length;
	
	delTr(raiseInfoSize, IndexTr, "raiseInfoDelBtn_", proposalPrefix);
	$parentTr.find(":input").qtip('destroy', true);
	$parentTr.remove();
}

//本次跟踪记录
function initRecordFeeType(element){
	var RecordFeeTypeStr =getRecordFeeTypes();
	var registNo = $("#registNo").val();
	var params = {
		"RecordFeeTypeStr" : RecordFeeTypeStr,
		"registNo" : registNo
	};
	
	$.ajax({
		url : "/claimcar/loadAjaxPage/initRecordFeeType.ajax",
		type : "post",
		data : params,
		async : false, 
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

//已选中的跟踪费用名称
function getRecordFeeTypes(tabPageNo){
	
	var feeTypeStr = "";
	$("#traceRecord_Tbody").find("select[name$='feeTypeCode']").each(function(){
		feeTypeStr+=$(this).val()+",";
	});
	feeTypeStr = feeTypeStr.substring(0, feeTypeStr.length-1);
	return feeTypeStr;
}

//选中费用名称
function setFeeType(element){
	var $tbody = $("#traceRecord_Tbody");
	var indexSeriNo = $("#feeTab").find("input[name='feeCheckFlag']:checked");
	var param = "";
	var registNo = $("#registNo").val();
	var comCode = $("#comCode").val();
	for(var i = 0 ; i < indexSeriNo.length; i++){
		if(i == 0){
			param = indexSeriNo.val();
		} else {
			param = $(indexSeriNo[i]).val() + "," + param; 
		}
	}
	var FeeSize = $tbody.find("tr").length;
	var verifyFeeSize = $tbody.find("tr[name='viewVerify']").length;
	var size = parseInt(FeeSize,10)-parseInt(verifyFeeSize,10);
	var params = {"size":size,"feeTypes":param,"registNo":registNo};
	var url = "/claimcar/loadAjaxPage/loadFeeTr.ajax";
	$.post(url,params, function(result){
		$tbody.append(result);
		if ($("input[name='prpLDlossPersTraceVo.endFlag']:checked").val() == '1') {// 初始化是否跟踪完毕选择是的时候伤情类型必填
			$tbody.find("input.detractionfee").removeAttr("ignore");//索赔金额和定损金额必录
		}else{
			$tbody.find("input[name$='.reportFee']").removeAttr("ignore");// 估损金额必录
			$tbody.find("input[name$='.realFee']").attr("ignore", "ignore");
			$tbody.find("input[name$='.defloss']").attr("ignore", "ignore");
			$tbody.find("input.detractionfee").attr("ignore", "ignore");//索赔金额和定损金额费
		}
		if(comCode.substring(0, 2) == "22" ){
			$tbody.find("select.select2").select2();
		}
	});
	closePop();
}

function closePop(){
    layer.close(layIndex);
}

//删除跟踪记录
function del_TraceRecord(element){
	var array = $(element).attr("name").split("_");//下标
	var IndexTr = array[1];
	var indexSeriNo = $("#traceRecord_Tbody button");
	var proposalPrefix="prpLDlossPersTraceVo.prpLDlossPersTraceFees";
	var $parentTr = $(element).parent().parent();
	var $verifyTr = $parentTr.next("tr");
	var traceRecordSize = indexSeriNo.length;
	
	var status = $("input[name='prpLDlossPersTraceVo.prpLDlossPersTraceFees["+IndexTr+"].status']").val();
	if(status=="1"){
		layer.msg("费用审核通过的费用不支持删除操作！");
		return false;
	}
	
	if(traceRecordSize>1){
		delTr(traceRecordSize, IndexTr, "traceRecordDelBtn_", proposalPrefix);
		$parentTr.find(":input").qtip('destroy', true);
		$parentTr.remove();
		if($verifyTr.attr("name")=="viewVerify"){
			$verifyTr.remove();
		}
		changeReportFee();
		changeRealFee();
		changeDefLoss();
		changeDetractionfee();
	}else{
		layer.msg("不能删除，本次跟踪记录最少保留一条记录！");
		return false;
	}
}

//跟踪意见
function addTraceContent(element) {
	var myDate = new Date();
	layer.prompt({
		title : '跟踪意见',
		formType : 2
	// prompt风格，支持0-2
	}, function(text) {
		$("textarea[name='prpLDlossPersTraceVo.operatorDesc']").append(myDate.getFullYear() + "年" + (myDate.getMonth() + 1) + "月" + myDate.getDate() + "日  " + $("#userName").val()+" ：" + text+"\n");
		layer.msg('成功');
	});
}