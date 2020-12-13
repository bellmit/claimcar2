var saveTypes;
$(function() {
	$("#appli").addClass("btn  btn-primary"); 
	initCheck();
	
	$.Datatype.certiNo = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;// 身份证验证
	$.Datatype.sum = /^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/;// 金额，
	$.Datatype.number = /^[0-9]*[1-9][0-9]*$/;// 正整数
	$.Datatype.age = /^(?:[1-9][0-9]?|1[01][0-9]|120)$/;// 年龄
	$.Datatype.selectMust = function(gets,obj,curform,regxp){
		var code = $(obj).val();
		if(isBlank(code)){
			return false;
		}
    };
    
  //初始化判断是否 申请垫付已提交
	var padPayID = $("#padPayID").val();
	if(padPayID == null && padPayID == ""){
		endTask();
		return false;
	}

    $("select[name$='personName']").each(function(){
    	$(this).attr("datatype","selectMust");
    });
	var ajaxEdit = new AjaxEdit($('#padPay_form'));
	ajaxEdit.targetUrl = "/claimcar/padPay/savePadPay";
	ajaxEdit.afterSuccess = after;
	ajaxEdit.beforeSubmit = before;// 在验证成功后，表单提交前执行的函数
	// 绑定表单
	ajaxEdit.bindForm();

});

function savePadPay(saveType) {
	$("#padPay_form").submit();
	saveTypes = saveType;
}

//检验涉恐人员
function vaxInfor(){
	var registNo=$("#registNo").val();
	var policyNo=$("#policyNo").val();
	var nameList='';
	var params = {"policyNo" :policyNo,
			      "registNo":registNo
			     };
    $.ajax({
		url : "/claimcar/padPay/vaxInfor.ajax", // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(jsonData) {// 回调方法，可单独定义
		    var result = eval(jsonData);
			if (result.status == "200" ) {
			 nameList=result.data;
			}
		}
	});
	
	
	if(!isBlank(nameList)){
		alert(nameList+"已被冻结,案件不能提交");
		
		return false;
	}
	return true;


}

function submitPadPay(flowTaskId){
	var falg = before();
	if(!falg){
		return false;
	}else{
		$("#padPay_form").submit();
		initPadPaySubmit(flowTaskId);
	}
}

/** 垫付申请提交(发起) * */



function advPadPay() {
	// $("#padPay_form").submit();
	/*
	 * （一）驾驶人未取得驾驶资格的；
		（二）驾驶人醉酒的；
		（三）被保险机动车被盗抢期间肇事的；
		（四）被保险人故意制造交通事故的。
	 */
	layer.confirm('垫付仅限于交强险免责情况，如：1、无驾驶资格；2、醉酒；'+
			'3、盗抢期间肇事；4、故意制造。是否确定发起垫付任务？', {
		btn : [ '确定', '取消' ]
	}, function(index) {
		advPadPayTask();
	}, function() {
		layer.close();
	});

}
var regNo=$("#registNo").val();
var claimNo=$("#claimNo").val();

function advPadPayTask() {
	var regNo=$("#registNo").val();
	var claimNo=$("#claimNo").val();
	var params = {
		"registNo" : regNo,
		"claimNo" : claimNo
	};
	$.ajax({	
		
		url : "/claimcar/padPay/advPadPay.ajax",
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(jsonData) {
			// eval(jsonData).data == "ok"
			if (eval(jsonData).status == "200" && eval(jsonData).data == "ok") {
				layer.confirm('垫付申请提交成功！', {
					btn : [ '确定' ],
				    closeBtn: 0,
				// 按钮
				}, function(idx) { 
					// window.location.reload();
					 $("#flowIDX").val(1);   //防止重复提交
					layer.close(idx);
					endTask();
				});
			} else {
				layer.alert("提交失败！错误信息：" + eval(jsonData).data);
			}
		},
		error : function(jsonData) {
			layer.alert("提交失败！错误信息：" + eval(jsonData).data);
		}
	});
}

function endTask(){
	$("body textarea").eq(1).attr("disabled", "disabled");
	$("body :input").each(function() {
		$(this).attr("disabled", "disabled");
	});
}

function after(result) {
	// 校验之后，保存或者提交
	if (saveTypes == "save") {
		layer.confirm('暂存成功！',{
			btn: ['确定'] //按钮
		}, function(){
			window.location.reload();
		});
	}
	if (saveTypes == "advPadPay") {
		advPadPay();
	}
	if (saveTypes == "submitPadPay") {
		/*//判断是否人员是否被冻结
		if(!vaxInfor()){
			window.location.reload();
			return false;
		   }*/
		
		
		initPadPaySubmit();
	}
}

function initPadPaySubmit() {
	
	
	var flowTaskId=$("#flowTaskId").val();
	// 提交
	var url = "/claimcar/padPay/initPadPaySubmit.do?flowTaskId="+flowTaskId;
	layer.open({
		type : 2,
		title : "垫付提交",
		area : [ '65%', '40%' ],
		fix : true, // 不固定
		maxmin : false,
		content : url,
	});
}

function before() {
	// 校验
	var noticeDate = $("#noticeDate").val();
	var damageTime = $("#damageTime").val();
	var result = compareFullDate(noticeDate,damageTime);
	if(result==-1){
		layer.alert("通知日期不能早于出险日期!");
		return false;
	}
	
	//垫付金额之和不能超过交强险赔付限额10000元，否则提示“垫付医疗赔付限额为10000元,你已经超限额！”；
	var sum = 0;
	var costSum =$("#person_table input[name$='costSum']");
	$(costSum).each(function(){
		sum=Number(sum)+Number($(this).val());
	});

	//判断是否2020年综改之后的保单，调整垫付限额18000,原来是10000
	var padLimitAmount =$("#padLimitAmount").val();
	if(sum>padLimitAmount){
		layer.alert("垫付医疗赔付限额为"+ padLimitAmount +"元,已经超限额！");
		return false;
	}
	//客户识别
	/*if(sum>=10000){
		var clickFlag = $("#appli").attr("clickFlag");
		if(clickFlag==null||clickFlag==""){
			$("#appli").focus();
			layer.msg("因合规要求，请先在垫付页面进行客户识别操作！");
			return false;
		}
	}	*/
	var flagChange = true;
	$("input[name$='payeeName']").each(function(){
		if($(this).val()==""){
			flagChange = false;
		}
	});
	if(!flagChange){
		layer.msg("请添加收款人信息");
		return false;
	}
	
	//收款人为非被保险人，请填写例外原因。
	var flag = true;
	$("input[name$='payObjectKind']").each(function(){
		var idx = $(this).attr("name").substring(15,16);
		var ba = "input[name='padPayPersonVo["+idx+"].otherFlag']:checked";
		var na = "select[name='padPayPersonVo["+idx+"].otherCause']";
		$(ba).val();
		$(na).val();
		if($(ba).val()=="1"){
			 if($(this).val()!="2" && isBlank($(na).val())){
			layer.alert("收款人为非被保险人，请填写例外原因");
			flag =false;
		}else{
			$(na).val();
			$(na).attr("disbaled","disbaled");
		}
		}
	});
	if(!flag){
		return false;
	}
	
	return true;
}

//function editCause(obj,suffix) {
//	var 
//	var cause = $("select[name='padPayPersonVo["+suffix+"].otherCause']");
////	$("input[name='padPayPersonVo[" + index + "].personAge']").val("");
//	if(Number($(obj).val()) == 0){
//		$(cause).val("");
//		$(cause).attr("disabled","disabled");
//	}else{
//		$(cause).removeAttr("disabled","disabled");
//	}
//	/*牛强改*/
///*	if (Number($(obj).val())==0) {
//		$(cause).val("");
//		$(cause).each(function(index) {
//			$(cause).attr("disabled", "disabled");
//		});
//	}else{
//		$(cause).each(function(index) {
//			$(cause).removeAttr("disabled", "disabled");
//		});
//	}*/
//}
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

function submit_padPay(taskId,padId,level,nextUserCode,nextComCode) {
	var params = {"taskId" : taskId,"padId" : padId, 
				 "level" : level, "nextUserCode" : nextUserCode,
				 "nextComCode" : nextComCode};
	$.ajax({
		url : "/claimcar/padPay/submit_padPay.ajax",
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(jsonData) {
			if (eval(jsonData).status == "200") {
				layer.confirm('提交成功！', {
					btn : [ '确定' ]
				// 按钮
				}, function(index) {
					//var indexs = parent.layer.getFrameIndex(window.name); // 获取窗口索引
					parent.window.location.reload();
				});
			}else{
				layer.alert("提交失败！");
			}
		},
		error : function() {
			layer.alert("提交失败！");
		}
	});
}


// 增加人员损失信息
function addPadPayTr(registNo,handlerStatus) {
	
    var $table_cont = $("#person_table");
	var $personVo_size = $("#personVo_size");// 人员条数
	var personSize = parseInt($personVo_size.val(), 10);
	var params = {
		"per_index" : personSize,
		"personVoSize" : personSize,
		"registNo" : registNo,
		"claimNo" : $("#claimNo").val(),
		"handlerStatus" : handlerStatus,
	};
	var url = "/claimcar/padPay/addPersRow.ajax";
	$.post(url, params, function(result) {
		$table_cont.append(result);
		$personVo_size.val(personSize + 1);
		$("#personVo_size").val(personSize + 1);
	});
	
	//给医院机构增加搜索框
	initHospitalSearch();
}

function delPayInfo2(element, per_index) {
	var index = $(element).attr("name").split("_")[1];// 下标
	var proposalPrefix = "padPayPersonVo";
//	var parentTr = $(element).parent().parent();
	var perTrSize = $("#personVo_size");//
	var perTrSizeInt = parseInt(perTrSize.val(), 10);//
	perTrSize.val(perTrSizeInt - 1);// 删除一条
	
	//var id=document.getElementById("tttt_"+(per_index-1));
	//var div_id = $("#tttt_" +per_index);
	//alert(id);
	//delTr(perTrSizeInt, index, "padPayPer_", proposalPrefix);
    
	var tableDiv = $("#ttttt_" + per_index);
	tableDiv.remove();
	
//	// 调整序号
//	var items = $("button[name^='padPayPer_']");
//	items.each(function() {
//		name = this.name;
//		var idx = name.split("_")[1];
//		var idx_ = parseInt(idx) + 1;
//		//$(xuhao_).next().next().html(idx_);
//		// 调整tbody index
//		var tb_one = Number(Number(index)-1);
//		$(this).parent().parent().parent().attr("id", "padPayTbOne_" + tb_one);
//		$(this).parent().parent().parent().attr("id", "padPayTbTwe_" + tb_one);
//	});

	/*var j = Number(index);
	var id=document.getElementById("#tttt_"+(j+1));
	var div_id = $("#tttt_" +per_index);
	alert(id);*/
	/*for (var i=j+1 ; i < perTrSizeInt; i++) {
		var div_id = $("#tttt_" + (i-1));
		alert(div_id.val(i));
	}*/
/*	var j = Number(index) + 1;
	alert(j);
	for (var i=j ; i <= perTrSizeInt; i++) {

		var div_id = $("#ttttt_" + i);
		var divId = Number(Number(i) - 1);
		div_id.attr("id", "#ttttt_" + divId);

		var table_id = $("#padPay_" + i);
		table_id.prop("id", "#padPay_" + divId);
		
		//var tr_Id=$("#");
		$(element).attr("padPayPer_"+i,"padPayPer_"+(i-1));
		
		var tbOne_ = $("#padPayTbOne_" + i);
		tbOne_.attr("id","#padPayTbOne_" + divId);
		
		var tbTwe_ = $("#padPayTbTwe_" + i);
		tbTwe_.attr("id","#padPayTbTwe_" + divId);
	}*/
}

// 删除信息行
function delPayInfo(element, per_index) {
	var $personVo_size = $("#personVo_size");// 人员条数
	var personSize = parseInt($personVo_size.val(), 10);
	if (personSize == 1) {
		layer.alert("最少存在一条人员损失信息！");
		return false;
	}
	delTr(chargeSize, index, "lossChargeVo_", proposalPrefix);
	var perId = $("input[name='padPayPersonVo[" + per_index + "].id']").val();
	
	
	$("#personVo_size").val(personSize - 1);
	$("#padPay_" + per_index).html(" ");
	$("#ttttt_" + per_index).hide();
	if (perId != null) {
		params = {"id" : perId};
		$.ajax({
			url : "/claimcar/padPay/dropPersonLoss.ajax",
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async : false,
			success : function(jsonData) {
				if (eval(jsonData).status == "200") {
					layer.msg("删除成功！");
				}
			}
		});
	}
	//调整序号
	var items = $("button[name^='padPayPer_']");
	items.each(function(){
		name = this.name;
		var idx= name.split("_")[1];
		var idx_ = parseInt(idx) +1;
		$(this).parent().next().html(idx_);
		//调整tbody index
		$(this).parent().parent().parent().attr("id","padPayTbOne_"+idx);
		$(this).parent().parent().parent().attr("id","padPayTbTwe_"+idx);
	});
}

//

function changePerName(obj, index) {
	var perName = $("option:selected", $(obj)).val();
	if (perName == "") {
		$("input[name='padPayPersonVo[" + index + "].personAge']").val("");
		$("input[name='padPayPersonVo[" + index + "].personAddress']").val("");
		$("input[name='padPayPersonVo[" + index + "].costSum']").val("");
		$("input[name='padPayPersonVo[" + index + "].personIdfNo']").val("");
		return false;
	}
	params = {"id" : perName};
	$.ajax({
		url : "/claimcar/padPay/getPersonName.ajax",
		type : 'post',
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : true,
		success : function(jsonData) {
			if (eval(jsonData).status == "200") {
				//解析
				$.each(eval(jsonData).data, function(key, value) {
					if(key == "age"){
						$("input[name='padPayPersonVo[" + index + "].personAge']").val(value);
					}else if(key == "sex"){
						$("input[name='padPayPersonVo[" + index + "].personSex']").val(value);
					}else if(key == "licenseNo"){
						$("input[name='padPayPersonVo[" + index + "].licenseNo']").val(value);
					}else if(key == "idNo"){
						$("input[name='padPayPersonVo[" + index + "].personIdfNo']").val(value);
					}else if(key == "fee"){
						$("input[name='padPayPersonVo[" + index + "].costSum']").val(value);
					}
				});
			}
		}
	});
}

// 带出，收款方账号，开户银行
function changeCustom(obj, index) {
	var valName = $("option:selected", $(obj)).val();
	// 带出省份证号，收款方账号，开户银行
	if (valName == "") {
		$("input[name='padPayPersonVo[" + index + "].payeeAccount']").val("");
		$("input[name='padPayPersonVo[" + index + "].bank']").val("");
		return false;
	}
	params = {"id" : valName};
	$.ajax({
		url : "/claimcar/padPay/getPayCustom.ajax",
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : true,
		success : function(jsonData) {
			if (eval(jsonData).status == "200") {
				$.each(eval(jsonData).data, function(key, value) {
					if(key == "accNo"){
						$("input[name='padPayPersonVo[" + index + "].payeeAccount']").val(value);
					}else if(key == "bank"){
						$("input[name='padPayPersonVo[" + index + "].bank']").val(value);
					}
				});
			}
		}
	});
}

//收款人选择界面打開
var payIndex=null;
function showPayCust(element){
	var registNo = $("#registNo").val();
	var tdName = $(element).attr("name");
	var compFlag = "comp";
	var url="/claimcar/payCustom/payCustomList.do?registNo="+registNo+"&tdName="+tdName+"&compFlag="+compFlag+"&flag=1"+"";
	if(payIndex==null){
		payIndex=layer.open({
		    type: 2,
		    title: '收款人选择',
		    shade: false,
		    area: ['70%', '50%'],
		    content:url,
		    end:function(){
		    	payIndex=null;
		    	otherFlagCon();
		    }
		});
	}
}

// 返回
function cancelPadPay() {
	var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	parent.layer.close(index);// 执行关闭
};

$("#lawsuit").click(function(){
	var registNo=$("#registNo").val();
	var nodeCode=$("#nodeCode").val();
	lawsuit(registNo,nodeCode);
});

$("#uploadCertifys").click(function(){
	var taskId=$("#flowTaskId").val();
	uploadCertifys(taskId);
});

function initCheck() {
//	var flowIDX= $("#flowIDX").val();
	//var node = $("#nodeCode").val();
	var status = $("#status").val();
//	layer.alert(flowIDX+"A"+status+"B"+node);
	if (Number(status) == 3) {
		$("body textarea").eq(1).attr("disabled", "disabled");
		$("body :input").each(function() {
			$(this).attr("disabled", "disabled");
		});
		$(".rateBtn").removeAttr("disabled");
	}
	// 例外标志管控
	otherFlagCon();
	// 医院搜索框初始化
	initHospitalSearch();
}

// 医院搜索框初始化
function initHospitalSearch() {
	var hospitalAttr = new Array();
	$("[id$='_hospital']").each(function() {
		var hospitalId = $(this).attr("id");
		if (!isBlank(hospitalId)) {
			hospitalAttr = hospitalId.split("_");
		}
		$("#hospitalArea_" + hospitalAttr[1] + "_hospital").select2();
	});
}

//比较两个日期字符串
//date1=date2则返回0 , date1>date2则返回1 , date1<date2则返回-1
function compareFullDate(date1,date2){	
	var strValue1=date1.split('-');
	var date1Temp=new Date(strValue1[0],parseInt(strValue1[1],10)-1,parseInt(strValue1[2],10));
	
	var strValue2=date2.split('-');
	var date2Temp=new Date(strValue2[0],parseInt(strValue2[1],10)-1,parseInt(strValue2[2],10));
	if(date1Temp.getTime()==date2Temp.getTime())
		return 0;
	else if(date1Temp.getTime()>date2Temp.getTime())
		return 1;
	else
		return -1;
}
//页面加载时判断赔款金额若小于10000，则反洗钱按钮disabled
/*$(function(){
	var items=$("div[name$='costSum']");
	var sumFee = parseFloat(0.00);
	$(items).each(function(){
		var fee=$(this).val();
		if(fee==""){
			fee=parseFloat(0.00);
		}
		sumFee=sumFee+parseFloat(fee);
	});
	if(sumFee<10000){
		$("#payCustomOpen").attr("disabled", true);
	}
});

//判断赔款金额若小于10000，则反洗钱按钮disabled，否则反之。
function sumCost(field) {
	var items=$("input[name$='costSum']");
	var sumFee = parseFloat(0.00);
	$(items).each(function() {
		var fee = $(this).val();
		if (fee == "") {
			fee = parseFloat(0.00);
		}
		sumFee = sumFee + parseFloat(fee);
	});
	if(sumFee<10000){
		$("#payCustomOpen").attr("disabled", true);
	}else{
		$("#payCustomOpen").attr("disabled", false);
	}
}*/
/*//费用金额大于10000时，提醒录入反洗钱信息
function feeInfor(){
	
	var payList="";
	var registNo=$("#registNo").val();
    var arr = new Array();
	var i = 0 ;
	var nameList = new Array();
	$("input[name$='payeeId']").each(function(){
		arr[i] = $(this).val();
		payList+=arr[i]+",";
		i = i +1;
		
	});
	
	var params = {"payeeList" : payList,
			      "registNo":registNo
	                };
    $.ajax({
		url : "/claimcar/padPay/verification.ajax", // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(jsonData) {// 回调方法，可单独定义
		    var result = eval(jsonData);
			if (result.status == "200" ) {
				nameList=result.data;
				
			}
		}
	});
	
	var str="";
	if(!isBlank(nameList)){
		for(var i=0;i<nameList.length;i++){
			str+=nameList[i]+",";
			}
		
		alert("请补录收款人，"+str+"的反洗钱信息");
		
		return false;
	}
}
*/


function isMotified(){
	var handlerStatus=$("#status").val();
	if(handlerStatus == 0){
		return false;
	}else if(handlerStatus == 2 ){
		return true;
	}
	return false;
}
//垫付注销
function padPayCancel(taskId) {
	if (isBlank(taskId)) {
		layer.alert("此案件不能注销！");
		return;
	}else{
		layer.confirm('是否注销此任务?', {
			btn: ['确定','取消'] //按钮
		}, function(index){
			$.ajax({
				url : "/claimcar/padPay/padPayCancel.do?taskId="+taskId, 
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				//data : params, // 要传递的数据
				async:false, 
				success : function(jsonData) {// 回调方法，可单独定义
					if(jsonData.status=="200"){
						
						layer.msg("垫付注销成功！");
						$(":input").attr("disabled","disabled");
						$(":input[type='button']").addClass("btn-disabled");
						$(".nodisabled").removeAttr("disabled");
						$(".nodisabled").removeClass("btn-disabled");
						$("#padPayCancel").prop("disabled",true);
					}
				}
			});
		});
	}
}

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