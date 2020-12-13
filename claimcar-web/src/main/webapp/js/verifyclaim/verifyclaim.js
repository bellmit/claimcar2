var reasons = "" ;
var idx;
var footFlag=0;
$(document).ready(function(){
	var isRefuse = $("#isRefuse").val();
	if("1" == isRefuse){
		$("#isRefuse").val("2");
		alert("该案件为结案拒赔案件!");
		
	}
	
	//理算的减损类型和欺诈类型赋值给核赔
	var impairmentType=$("#impairmentType").val();
	if(!isBlank(impairmentType) && impairmentType!=undefined){
		$("#impairmenttypesid option[value='"+impairmentType+"']").attr("selected","selected");
	}
	var fraudType=$("#fraudType").val();
	if(!isBlank(fraudType) && fraudType!=undefined){
		$("#fraudtypeid option[value='"+fraudType+"']").attr("selected","selected");
	}
	
});


/*
 * 核赔
 */
//车物
var prevValue = "";//之前值
var nowValue="";//目前输入值

//人伤
var prevValues = "";//之前值
var nowValues = "";//目前输入值

//车物减损金额

$("#carCisderoVerifyAmout").change(function(){
	var pisderoAmout=$("#personPisderoVerifyAmout").val();
	nowValue=$(this).val();
	prevValue=$(this).attr("oldValue");
	 if(pisderoAmout==0 && prevValue==0 && nowValue>0){
			var htmlstr="<option value=''></option>"+
				"<option value='2'>协谈减损</option>"+
			    "<option value='1'>拒赔减损</option>";
			$("#impairmenttypesid").html(htmlstr);
		}else if (prevValue>0 && nowValue==0 && pisderoAmout==0){
			var htmls ="<option value=''></option>";
			$("#impairmenttypesid").html(htmls);
			$("#fraudtypeid").html(htmls);
		}
	 $(this).attr("oldValue",nowValue);
});


//人伤减损金额

$("#personPisderoVerifyAmout").change(function(){
	var carCisderoAmout=$("#carCisderoVerifyAmout").val();
	nowValues=$(this).val();
	prevValues=$(this).attr("oldValue");
	 if(carCisderoAmout==0 && prevValues==0 && nowValues>0){
		 var htmlstr="<option value=''></option>"+
			 "<option value='2'>协谈减损</option>"+
			"<option value='1'>拒赔减损</option>";
			$("#impairmenttypesid ").html(htmlstr);
		}else if (prevValues>0 && nowValues==0 && carCisderoAmout==0){
			var htmls ="<option value=''></option>";
			$("#impairmenttypesid").html(htmls);
			$("#fraudtypeid").html(htmls);
		}
	 $(this).attr("oldValue",nowValues);
});



//欺诈类型
$("#impairmenttypesid").click(function(){
	var fraudtype=$("#impairmenttypesid option:selected").val();
	var htmlstr="<option value=''></option>"+
	"<option value='1'>故意虚构保险标的</option>"+
	"<option value='2'>编造未曾发生的保险事故</option>"+
	"<option value='3'>编造虚假的事故原因</option>"+
	"<option value='4'>夸大损失程度</option>"+
	"<option value='5'>故意造成保险事故</option>";
	if(fraudtype=='1'){
		$("#fraudtypeid").html(htmlstr);
	}else{
		$("#fraudtypeid").html("");
	}
});



$(function(){
	//理算核赔通过管控内部减损不能编辑
	var verifyClaimCompleteFlag=$("#verifyClaimCompleteFlag").val();
	if(verifyClaimCompleteFlag=='1'){
		$("#personPisderoVerifyAmout").attr("readonly","readonly");
		$("#carCisderoVerifyAmout").attr("readonly","readonly");
	}
	lossNameSet();
	//反洗钱可疑特征显示-理算意见
	var prplcomContextVoflag=$("#prplcomContextVoflag").val();
	if(prplcomContextVoflag=='1'){
		$("#LSTable").removeClass('hide');
	}
	//反洗钱可疑特征显示-核赔意见
	var prplcomContextVoHflag=$("#prplcomContextVoHflag").val();
	if(prplcomContextVoHflag=='1'){
		$("#LSHTable").removeClass('hide');
	}else{
		$("#causes").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
	}
	var status=$("input[name='wfTaskVo.handlerStatus']").val();
	var node=$("input[name='wfTaskVo.nodeCode']").val();
	var flowTaskId=$("input[name='wfTaskVo.taskId']").val();
	if(status!="2"){
		$("#vc_return").hide();
		$("#vc_audit").hide();
		$("#vc_adopt").hide();
	}
	if($("#riskCode").val()=="1101"){
		if($("input[name='wfTaskVo.subNodeCode']").val()=="VClaim_CI_LV10"){
			$("#vc_audit").hide();
		}
	}else{
		if($("input[name='wfTaskVo.subNodeCode']").val()=="VClaim_BI_LV12"){
			$("#vc_audit").hide();
		}
	}
	
	
	acceptTask(status,node,flowTaskId);
	initCheckBox();
	hadTextClick();
	handle_Text($("input[name='uwNotionVo.VerifyText']:checked"));
	
	
	ajaxEdit = new AjaxEdit($('#vclaimform'));
	ajaxEdit.targetUrl = "/claimcar/verifyClaim/saveVerifyClaim.do";
	ajaxEdit.afterSuccess = after;
	ajaxEdit.beforeSubmit = before;//在验证成功后，表单提交前执行的函数
	// 绑定表单
	ajaxEdit.bindForm();
	
	$.Datatype.checkBoxMust = function(gets, obj, curform, regxp) {
		var need = 1, numselected = curform.find("input[name='" + obj.attr("name") + "']:checked").length;
		return numselected >= need ? true : "请至少选择" + need + "项！";
	};
	
//	var deductOffALL = 0;
//	$("[name$='deductOffAmt']").each(function(){
//		if($(this).val()!=null&&$(this).val()!=""){
//			deductOffALL+=parseFloat($(this).val());
//		}
//	});
//	$("span#deductOffALL").text(deductOffALL.toFixed(2));
});
//财产名称截取显示并赋值
function lossNameSet(){
	
	$("span[id^='VMlossName']").each(function(){
		var lossName=$(this).text();
		if(!isBlank(lossName) && lossName.length>5){
			var shortLossName=lossName.substring(0,5)+"......";
			$(this).attr("title",lossName);
			$(this).text(shortLossName);
		}
	});
}

/**
 * 核赔提交保存核赔意见信息
 * @param saveType
 */
function save(saveType) {
	
	if(saveType=='vc_adopt'){
		var isMinorInjuryCases=$("#isMinorInjuryCases").val();
		if(isMinorInjuryCases=='1'){
			alert('该案件为小额人伤案件');
			}
	}
	
	//如果是核陪通过，则要验证反洗钱是否要查看
	var claimNo=$("#claimNo").val();
	var sign="0";
	var seeflag=$("#fxqSignShow").val();
	if(seeflag=='1'){
	  if(!isBlank(claimNo)){
		
		$.ajax({
			url : "/claimcar/verifyClaim/repairMoney.ajax?claimNo="+claimNo, // 后台校验
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			async : false,
			success : function(jsonData) {// 回调方法，可单独定义
				var result=eval(jsonData);
				sign=result.data;
		
				}
		});
		}
	  if(sign=="1"){
		  alert("请复核案件的反洗钱信息!");
		    return false;
	   }
	  }	
	
	//给隐藏域赋值
	
	
	$("input[name='saveType']").val(saveType);
	$("input[name='uwNotionVo.handle']").val(saveType);
	var handle = "0";
	if(saveType=="vc_adopt"){
		handle = "1";
	}
	$("input[name='uwNotionMainVo.handle']").val(handle);
	
	$("#vclaimform").submit();
}



function before() {
	if(!checkIsderoAmout()){
		layer.msg("减损类型为拒赔减损，欺诈类型不能为空！");
		return false;
	}
	
	if(!checkimpairmenttype()){
		layer.msg("人伤减损金额或车物减损金额不全为0时，减损类型不允许为空！");
		return false;
	}
	//校验
//	layer.load(0, {shade : [ 0.8, '#393D49' ]});
}

function isInvalidQS(){
	var result = true;
	var claimNo = $("input[name='uwNotionMainVo.claimNo']").val();
	var goUrl = "/claimcar/subrogationSH/isInvalidQS.ajax";
	$.ajax({
		url : goUrl,
		type : 'post',
		dataType : 'json',
		data : {"claimNo" : claimNo},
		async : false,
		success : function(jsonData) {// 回调方法，可单独定义
			result = eval(jsonData).data;
		},
		error : function(){
			result = true;
		}
	});
	return result;
}

function passValid(regNo){
	var result = true;
	var params = {"registNo" : regNo};
	var url = "/claimcar/verifyClaim/passValid.ajax";
	$.ajax({
		url : url, // 
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(jsonData) {// 回调方法，可单独定义
			result = eval(jsonData).data;
		},
		error : function(){
			result = false;
			layer.alert("工作流有调查节点未处理完成的话，不能核赔通过!");
			return false;
		}
	});
	return result;
}


/**
 * 校验减损金额与欺诈类型是否匹配
 */
function checkIsderoAmout(){
	var pisderoAmoutid =$("#personPisderoVerifyAmout").val();
	var cisderoAmoutid =$("#carCisderoVerifyAmout").val();
	var fraudtypeid =$("#fraudtypeid").val();
	var impairmenttypesid = $("#impairmenttypesid").val();
	
	if(pisderoAmoutid > 0 || cisderoAmoutid > 0){
		if (impairmenttypesid == 1 && (fraudtypeid == null || fraudtypeid == '')) {
			return false;
		} else {
			return true;
		}
	} else {
		return true;
	}
}

/**
 * 人伤减损金额或车物减损金额不全为0时，减损类型不允许为空！
 */
function checkimpairmenttype(){
	var pisderoAmoutid =$("#personPisderoVerifyAmout").val();
	var cisderoAmoutid =$("#carCisderoVerifyAmout").val();
	var impairmenttypesid = $("#impairmenttypesid").val();
	
	if(pisderoAmoutid > 0 || cisderoAmoutid > 0){
		if (impairmenttypesid == null || impairmenttypesid=='' || impairmenttypesid=="") {
			return false;
		} else {
			return true;
		}
	} else {
		return true;
	}
}

/**	 提交上级、退回、审核通过 	**/
function after(result) {
	//保存成功，返回主表id到页面
	$("input[name='uwNotionMainVo.id']").val(result.data);
	var saveType = $("input[name='saveType']").val();
//	var params = {"taskId" : $("input[name='wfTaskVo.taskId']").val(),
//			"action" : saveType, "uwNotionMainId" : result.data};
	var taskId = $("input[name='wfTaskVo.taskId']").val();
	var uwNotionMainId = result.data;
	var title = "";
	
	if(saveType=="vc_return"){
		title = "核赔退回";
	}else if(saveType=="vc_audit"){
		title = "提交上级";
	}else{
		title = "核赔通过提交";
		var reNo = $("#registNo").val();
		// 核赔的时候需要管控，如果说有工作流有调查节点未处理完成的话，不能核赔通过
		var type = $("input[name='uwNotionMainVo.policyType']").val();
		if(saveType=="vc_adopt"&&(type=="1"||type=="2")){//核赔通过
			if(passValid(reNo)){
				layer.alert("工作流有调查节点未处理完成，不能核赔通过!");
				return false;
			}	
			if(!isInvalidQS()){
				layer.alert("上海代位求偿案件的平台结算码状态已改变,请退回理算处理！");
				return false;
			}
	   }
	}
	var pisderoAmout=$("#pisderoAmout").val();//人伤减损金额
	var cisderoAmout=$("#carCisderoVerifyAmout").val();//车物减损审核金额
	var impairmentType = $("#impairmenttypesid").val();
	var fraudType = $("#fraudtypeid").val();
	var url = "/claimcar/verifyClaim/loadSubmitPage.ajax?taskId="+taskId+"&action="+saveType+"&uwNotionMainId="+uwNotionMainId+"&pisderoAmout="+pisderoAmout+"&cisderoAmout="+cisderoAmout+"&fraudType="+fraudType+"&impairmentType="+impairmentType;
	idx = layer.open({
		type : 2,
		title : title,
		area : [ '70%', '55%' ],
		fix : true, // 不固定
		maxmin : false,
		content : url,
		cancel: function(index){ 
			location.reload(); 
			}, 
	});
	
//	var url = "/claimcar/verifyClaim/loadSubmitPage.ajax";
//	$.post(url, params, function(result) {
//		// 页面层
//		idx = layer.open({
//			title : title,
//			type : 1,
//			//skin : 'layui-layer-rim', // 加上边框
//			area : [ '60%', '50%' ], // 宽高
//			async : false,
//			content : result,
//			yes : function(index, layero) {
//				// $("#PLverifyForm").submit();
//			}
//		});
//	});
}

function acceptTask(status,node,flowTaskId){
	//理算冲销核赔页面，是否发起追 偿应不能修改
	var taskInNode = $("input[name='uwNotionMainVo.taskInNode']").val();
	if(isBlank(taskInNode)){
		taskInNode = $("input[name='wfTaskVo.taskInNode']").val();
	}
	var recover = $("input[name='uwNotionMainVo.recoveries']");
	if(taskInNode == "CompeWfBI" || taskInNode == "CompeWfCI"){
		recover.attr("disabled","disabled");
	}else{
		recover.removeAttr("disabled");
	}
//	if(node!="VClaim"){
//		layer.open({type: 1,skin: 'layui-layer-demo', // 样式类名
//			  closeBtn: 0, // 不显示关闭按钮
//			  shift: 2,shadeClose: false, // 开启遮罩关闭
//			  content: '<br><br><h4><font color="red">该案件不是核赔节点任务，不能操作！</font><h4><br><br>'
//	   });
//		return false;
//	}
	if(status=="3"){
		$("body :input").each(function() {
			$(this).attr("disabled", "disabled");
		});
		$("#returnBtn").removeClass("btn btn-primary").addClass("hide");
		$("#auditBtn").removeClass("btn btn-primary ml-5").addClass("hide");
		$("#adoptBtn").removeClass("btn btn-primary").addClass("hide");
	}
}

function initCheckBox() {
	var ischeck = $("input[name$='isCheck']");
	ischeck.each(function(index) {
		if (ischeck.eq(index).val() == "1") {
			ischeck.eq(index).attr("checked", "checked");
		}
	});
}

//勾选免赔率
function isCheck(element) {
	var value = $(element).attr("value");
	var val = "0";
	if(value == "0" || value == ""){
		val = "1";
	}
	$(element).attr("value",val);
}

function setDuty() {

}

//新增费用赔款信息或收款人信息行
function addPayInfo(tbodyName, sizeName) {
	var $tbody = $("#" + tbodyName + "");
	//var size = $("#"+sizeName+"").val();
	var flag = $("#flag").val();
	var params = {
		"bodyName" : tbodyName,
		"flag" : flag,
	};
	var url = "/claimcar/verifyClaim/addRowInfo.ajax";
	$.post(url, params, function(result) {
		$tbody.append(result);
		//$("#"+sizeName+"").val(size + 1);
	});
}
//删除费用赔款信息或收款人信息行
function delPayInfo(element, sizeName) {
	var $parentTr = $(element).parent().parent();
	//var size = $("#"+sizeName+"").val();
	//$("#"+sizeName+"").val(size - 1);// 删除一条
	$parentTr.remove();
}

function hadTextClick(element) {
	var content = "";
    $('input[name="handleContent"]:checked').each(function(){
    	content += $(this).parent().text()+" , ";
    });
//    var amount = $("#ve_amount").val();
    if($("input[name='wfTaskVo.handlerStatus']").val()=="3"){
    	$("#handleText").val($("input[name='remark']").val());
    }else{
    	$("#handleText").val(content);
    }
}

function submitTaskBefore(){
	var compeWfZeroFlag = "0";
	var taskInNode = parent.$("[name='wfTaskVo.ywTaskType']").val();
	var registNo = parent.$("[name='uwNotionMainVo.registNo']").val();
	var claimNo = parent.$("[name='uwNotionMainVo.claimNo']").val();
	var riskCode = parent.$("#riskCode").val();
	var existCompInFlag = false;
	// 理算冲销选择0结案时需判断是否有未完成的理算任务
	if(taskInNode == "CompeWfCI" || taskInNode == "CompeWfBI"){
	  $.ajax({
		url : "/claimcar/verifyClaim/existCompIn.ajax?registNo="+registNo+"&riskCode="+riskCode+"&claimNo="+claimNo, // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		async : false,
		success : function(jsonData) {// 回调方法，可单独定义
			existCompInFlag=eval(jsonData.data);
			if(existCompInFlag){
				layer.msg("有未完成的任务节点或案件已结案,不能冲销0结案",{icon: 6,offset:'30px'});
			}
		}
	  });
	}
	if(!existCompInFlag && (taskInNode == "CompeWfCI" || taskInNode == "CompeWfBI")){
		var conIndx = layer.confirm('是否理算冲销核赔0结案？', {
			  btn: ['是','否'] //按钮
			}, function(){
				compeWfZeroFlag = "1";
				submitTask(compeWfZeroFlag);
				layer.close(conIndx);
			}, function(){
				compeWfZeroFlag = "0";
				submitTask(compeWfZeroFlag);
				layer.close(conIndx);
			});
	}else{
		submitTask(compeWfZeroFlag);
	}

}

function submitTask(compeWfZeroFlag) {
	var handle=parent.$("input[name='uwNotionVo.handle']").val();
	//ilog, 退回时候先判断，如果两个核赔，其中有 一个已结案理算，则不能再退回定损
	var nextNode = $("input[name='submitNextVo.nextNode']").val();
	var ilogParams = {
			"taskId" : parseFloat($("input[name='submit_taskId']").val()),
			"nextNode" : nextNode
		};
	if("vc_return" == handle && (nextNode == "DLCar" || nextNode == "Certi") ){//选择退回定损
		$.ajax({
			url : '/claimcar/verifyClaim/checkEndCase.do',
			type : 'post', // 数据发送方式
//			dataType : 'json', // 接受数据格式
			data : ilogParams, // 要传递的数据
			async : true,
			success : function(jsonData) {// 回调方法，可单独定义
				var result = eval(jsonData);
				if (result.status == "200") {
					// 成功
					if (result.data == "1") {//可以退
						submitTaskByVClaim(compeWfZeroFlag);
					}else{
						layer.alert(eval(jsonData).statusText,function(index){
							layer.closeAll();
						});
						flags = "0";
					}
					
				} else {
					layer.alert("服务异常！",function(index){
						layer.closeAll();
					});
				}
			},
			error : function(jsonData){
				layer.alert("服务异常！",function(index){
					layer.closeAll();
				});
			}
		});
	}else{
		submitTaskByVClaim(compeWfZeroFlag);
	}
}

/**
 * 展示下一个处理页面
 * @param taskId
 */
function loadNextPage(taskId,mapDatas){
	var obj = JSON.stringify(mapDatas);
	var url = "/claimcar/verifyClaim/loadNextPage.ajax?taskId="+taskId+"&mapDatas="+obj;
	url = url.replace(/"/g,'\'');
	layer.open({
		title : "下一节点任务",
		type : 2,
		area : [ '100%', '100%' ], // 宽高
		async : false,
		content : url,
		yes : function(index, layero) {
		},
		end : function(){
			parent.location.reload();
		}
	});
	
//	$.post(url, {"taskId":taskId}, function(result) {
//		// 页面层
//		layer.open({
//			title : "下一节点任务",
//			type : 1,
//			//skin : 'layui-layer-rim', // 加上边框
//			area : [ '100%', '100%' ], // 宽高
//			async : false,
//			content : result,
//			yes : function(index, layero) {
//				// $("#PLverifyForm").submit();
//			},
//			end : function(){
//				window.location.reload();
//			}
//		});
//	});
}

function verify_close(){
	layer.close(idx);// 执行关闭
}

function handle_Text(element){
	var items = $("#contentDiv").children("div");
	$(items).each(function() {
		
		$(this).css('display', 'none');
		$(this).find("input[type='checkbox']").removeAttr("checked")
		.removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
	});
	$("#description").val("");
	
	var idx_val = $(element).val();
	if(idx_val=="01"){//同意赔付
		setCheckBoxMust($("#handle_1"));
	}else if(idx_val=="02"){
		setCheckBoxMust($("#handle_2"));
	}else if(idx_val=="03"){
		setCheckBoxMust($("#handle_3"));
	}else if(idx_val=="04"){
		setCheckBoxMust($("#handle_4"));
	}else if(idx_val=="99"){
		setCheckBoxMust($("#handle_5"));
	}else if(idx_val=="100"){
		setCheckBoxMust($("#handle_6"));
	}
	if($("input[name='tempComperVal']").val()!="99"){
		initClass(idx_val);
	}
	
}

function setCheckBoxMust(element){
	$(element).show();
	$(element).each(function(){
		$(this).find("input[type='checkbox']").attr("datatype","checkBoxMust");
	});
}

function initClass(idx_val) {
	if (idx_val == "01") {// 同意赔付
		setClass($("#vc_return"));
		setClass($("#vc_audit"));
		setOnclinck($("#vc_adopt"),"save('vc_adopt')");
	} else if (idx_val == "02") {// 超权限，提交上级审核
		setClass($("#vc_return"));
		setClass($("#vc_adopt"));
		setOnclinck($("#vc_audit"),"save('vc_audit')");
	} else{
		setOnclinck($("#vc_return"),"save('vc_return')");
		setClass($("#vc_audit"));
		setClass($("#vc_adopt"));
	}
}

function setClass(element){
	$(element).removeClass("btn btn-primary").addClass("btn btn-disabled").removeAttr("onclick");
}

function setOnclinck(element,name){
	$(element).removeClass("btn btn-disabled").addClass("btn btn-primary").attr("onclick",name);
}

//控制是否有反洗钱特征
function lossHOrNot(value, id){
	if(value==1){
		$("#"+id+"Table").removeClass('hide');
		$("#"+id+"Table").find("input").each(function(){
		$(this).prop("disabled",false);
		});
		$("#causes").attr("datatype","*1-200").addClass("Validform_error");
		$("#ContextVoFlag").val("1");
		
		
	}else{
		$("#"+id+"Table").addClass('hide');
		$("#"+id+"Table").find("input").each(function(){
		$(this).prop("disabled",true);
			});
		$("#causes").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		$("#ContextVoFlag").val("0");
		$("textarea[name='prplcomContextVo.causes']").val("");
	}
	/*$("#"+id).val(value);
	//财产损失增加损失方车牌号
	linceseNoChg("car");
	writeAccidentTypes();*/
}
function submitTaskByVClaim(compeWfZeroFlag) {
	var handle=parent.$("input[name='uwNotionVo.handle']").val();
	var amount=parent.$("input[name='uwNotionVo.amount']").val();
	var verifyText=parent.$("[name='uwNotionVo.VerifyText']:checked").val();
	var handleText=parent.$("#handleText").val();
	var load_idx = layer.load(0, {time: 300*2000},{shade: 0});
	var url = "/claimcar/verifyClaim/verifyClaimSubmit.do";
	// var param = $("#vclaim").serialize();
	var action = $("input[name='action']").val();
	var personPisderoVerifyAmout = parent.$("#personPisderoVerifyAmout").val();//录入框人伤减损金额
	var carCisderoVerifyAmout = parent.$("#carCisderoVerifyAmout").val();//录入框车物减损金额
	var inCompensateNo = parent.$("#inCompensateNo").val();//计算书号
	
	var params = {
		"taskId" : parseFloat($("input[name='submit_taskId']").val()),
		"uwNotionMainId" : $("input[name='submit_uwNotionId']").val(),
		"currentNode" : $("input[name='submitNextVo.currentNode']").val(),
		"nextNode" : $("input[name='submitNextVo.nextNode']").val(),
		"action" : action,
		"compeWfZeroFlag":compeWfZeroFlag, //增加理算冲销0结案标志位
		"payIds": $("#payIds").val(),//页面的所有payeeId
		"handle":handle,
		"amount":amount,
		"verifyText":verifyText,
		"handleText":handleText,
		"personPisderoVerifyAmout":personPisderoVerifyAmout,
		"carCisderoVerifyAmout":carCisderoVerifyAmout,
		"compensateNo":inCompensateNo
	};
	$("input[name='submitTaskName']").removeClass("btn btn-primary").addClass("btn btn-disabled").removeAttr("onclick");
	$.ajax({
		url : url, // 后台保存
		type : 'post', // 数据发送方式
//		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : true,
		success : function(jsonData) {// 回调方法，可单独定义
			var result = eval(jsonData);
			if (result.status == "200") {
				// 成功
				if(action=="vc_adopt"){
					layer.confirm('提交成功！', {
						closeBtn: 0,btn : [ '确定' ]
					}, function(index) {
						layer.close(load_idx);
						layer.close(index);
						parent.location.reload();
					});
				}else{
					layer.close(load_idx);
					loadNextPage(result.data,result.datas);
				}
			} else {
				layer.close(load_idx);
				layer.alert(result.statusText,function(index){
					layer.closeAll();
//					window.location.reload();
				});
			}
		},
		error : function(jsonData){
			layer.close(load_idx);
			layer.alert(eval(jsonData).statusText,function(index){
				layer.closeAll();
			});
		}
	});

}

function coinsInfo(compensateNo,nodeCode){
	var params = {
	"compensateNo":compensateNo,
	"nodeCode":nodeCode
	};
	$.ajax({
	url : "/claimcar/verifyClaim/coinsInfoView.ajax",
	type : "post",
	data : params,
	async: false,
	success : function(htmlData){
		layIndex=layer.open({
		    type: 1,
		    title:"联共保分摊信息",
		    skin: 'layui-layer-rim', //加上边框
		    area: ['80%', '65%'], //宽高
		    content: htmlData
		});
	}
	});
}