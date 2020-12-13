var ajaxEdit;
$(function() {
	/*if(!sumAmountFee()){
		return false;
	}*/
	
	ajaxEdit = new AjaxEdit($('#fm'));
	ajaxEdit.targetUrl = "/claimcar/assessors/saveAssessorFee.do";
	ajaxEdit.rules = null;
	ajaxEdit.beforeSubmit = function(data) {
		layer.load(0, {
			shade : [0.8, '#393D49']
		});
		$("#EXButton").removeAttr("disabled");
	};
	ajaxEdit.afterSuccess = function(data) {
		if (data.data.flag == "0") {// save
			$("input[name='prpLClaimTextVo.id']").val(data.data.id);
		} else if (data.data.flag == "1") {// submit
			$("body :input").attr("disabled", "disbaled");
			$("#EXButton").removeAttr("disabled");
			$("#submitDiv").hide();
		} else if (data.data == "2") {// cancel
			$("body :input").attr("disabled", "disbaled");
			$("#EXButton").removeAttr("disabled");
			$("#submitDiv").hide();
		}
		$("#EXButton").removeAttr("disabled");
	};
	// 绑定表单
	ajaxEdit.bindForm();
	
	
	if($("#handlerStatus").val()=="3"||$("#handlerStatus").val()=="9"){
		$("body :input").attr("disabled", "disbaled");
		$("#EXButton").removeAttr("disabled");
	}
	$("#EXButton").removeAttr("disabled");
	$("input:radio[name='chooseReg']").removeAttr("disabled");
	
});

function submitNextNode(element) {
	var auditStatus = $(element).attr("id");// 提交动作
	/*if(!sumAmountFee()){
		return false;
	}*/
	if(auditStatus == "save"){//暂存
		$("#fm").submit();
	}else if(auditStatus == "submit"){//提交
		ajaxEdit.setTargetUrl("/claimcar/assessors/submitAssessorFee.do");
		$("#fm").submit();
	}else if(auditStatus == "cancel"){//注销
		ajaxEdit.setTargetUrl("/claimcar/assessors/submitCancel.do");
		$("#fm").submit();
	}
}

function layerShow(intermId){
	var flag = "look";
	index=layer.open({
	    type: 2,
	    title: '修改机构信息',
	    closeBtn: 1,
	    shadeClose: true,
	    scrollbar: true,
	    skin: 'yourclass',
	    area: ['1100px', '550px'],
	    content:"/claimcar/manager/intermediaryEdit.do?Id="+intermId+"&flag="+flag,
	    end:function(){
	    }
	});
}

function sumAmountFee(){ //更新总金额
	var itemFees = $("input[name$='amount']");
	var sumAmount=0;
	itemFees.each(function(){
		sumAmount += Number($(this).val());
	});
	$("#sumChargeFee").val(sumAmount);
	var standarMax = $("input[name$='payStandardMax']");
	var maxFee = Number($(standarMax).val());
//	if(maxFee < sumAmount ){
//		layer.alert("费用总金额 " + sumAmount + " 不能大于最高资费标准  " +maxFee );
//		return false;
//	}
//	return true;
	
}

function checkSpecialCharactor(element){
	var str = $(element).val();
	//var pattern = "^‘'！!#$%*+，,-./:：=？?@【】[\]_`·{|}~ ";//特殊字符
	var pattern = "^'!#$%*+,-./:=?@[\]_`{|}~ ……‘！#￥%*+，-。/：；=？@【、】——·{|}~ ";//特殊字符
	if(typeof(str) === "undefined" || str === null || str===""){
		return false;
	}
	for(var i = 0,size = pattern.length;i<size;i++){
		var reg = pattern.substring(i, i+1);
		if(str.indexOf(reg)>-1){
			layer.alert("包含特殊字符“"+reg+"”,请核实！");
			$(element).val("");
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
			$(element).val("");
			return false;
		}
	}
	return true;
}