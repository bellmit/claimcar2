$(function() {
	var status = $("#handlerStatus").val();
	if(status == "3"){//已处理
		$(":input").prop("readOnly",true);
		$(":input").prop("disabled",true);
		$(":checkbox").prop("disabled",true);
		$("#saveBtn").hide();
		$("#submitBtn").hide();
	}
	var $subBox = $(".subBox");
	$("#checkAll").prop("checked",
			$subBox.length == $(".subBox:checked").length ? true : false);
	$("#checkAll").click(function() {
		$(".subBox").prop("checked", this.checked);
	});
	$subBox.click(function() {
		$("#checkAll").prop("checked",
				$subBox.length == $(".subBox:checked").length ? true : false);
	});
});


// 计算总的回收金额
function calSumRecLossFee() {
	var sumRecLossFee = parseFloat("0");
	var recLossFeeObj = $(".recLossFee");
	recLossFeeObj.each(function(){
		if($(this).val() != ""){
			sumRecLossFee += parseFloat($(this).val());
		}
	});
	$("#sumRecLossFee").val(sumRecLossFee);
}

//选中险种变化
function selKindCode(e){
	$("#kindName").val($(e).html());
}

//保存损余回收明细
function saveReclossDetails(){
	calSumRecLossFee();
	var rules = {};
	var ajaxEdit = new AjaxEdit($('#recLossform'));
	ajaxEdit.beforeCheck = function() {// 校验之前
	};
	ajaxEdit.beforeSubmit = function() {// 提交前补充操作
		var $kindCode = $(".kindCode:selected");
		if($kindCode.length > 0){
			var kc = $kindCode[0].value;
			for(var i = 1; i< $kindCode.length;i ++){
				if($kindCode[i].value != kc){
					return false;
					break;
				}
			}
		}
	};
	ajaxEdit.targetUrl = "/claimcar/recLoss/save.do";
	ajaxEdit.rules = rules;
	ajaxEdit.afterSuccess = function(data) {// 操作成功后操作
		var flowTaskId = $("#flowTaskId").val();
		var sumRecLossFee = $("#sumRecLossFee").val();
		if($("#checkAll").is(':checked')){
			parent.$("#"+flowTaskId+"Ind").val("是");
		}else{
			parent.$("#"+flowTaskId+"Ind").val("否");
		}
		parent.$("#"+flowTaskId+"Fee").val(sumRecLossFee);
		parent.calAllSumRecLossFee();
		layer.confirm("操作成功", {
			btn: ['确定'] //按钮
		}, function(index){
			var index = parent.layer.getFrameIndex(window.name);// 获取窗口索引
			parent.layer.close(index);// 执行关闭	
		});
	};
	// 绑定表单
	ajaxEdit.bindForm();
	$("#recLossform").submit();
}

