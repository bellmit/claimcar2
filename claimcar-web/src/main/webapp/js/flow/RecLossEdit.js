$(function() {
	var status = $("#handlerStatus").val();
	if(status == "3"){//已处理
		$(":input").prop("readOnly",true);
		$(":input").prop("disabled",true);
		$(":checkbox").prop("disabled",true);
		$("input[name='inputDetailBtn']").prop("disabled",false);
		$("input[name='inputDetailBtn']").val("查看");
		$("#saveBtn").hide();
		$("#submitBtn").hide();
	}
	/*$("#checkAll").click(function() {
		$(".subBox").prop("checked", this.checked);
	});
	var $subBox = $(".subBox");
	$subBox.click(function() {
		$("#checkAll").prop("checked",
				$subBox.length == $(".subBox:checked").length ? true : false);
	});*/
	calAllSumRecLossFee();
});


// 计算总的回收金额
function calAllSumRecLossFee() {
	var sumRecLossFee = parseFloat("0");
	var recLossFeeObj = $(".recLossFee");
	recLossFeeObj.each(function(){
		if($(this).val() != ""){
			sumRecLossFee += parseFloat($(this).val());
		}
	});
	$("#allSumRecLossFee").val(sumRecLossFee);
}

function saveRecloss(saveType) {
	$("#saveType").val(saveType);
	if (saveType == "2") {
		var isAllReced = true;
		$(".recLossInd").each(function(){
			if($(this).val() == "否"){
				isAllReced = false;
				return false;
			}
		});
		if (!isAllReced) {// 未全部回收完毕弹出确认框
			layer.confirm("当前换件没有全部回收，请确定是否继续提交！", {
				btn: ['确定','取消'] //按钮
			}, function(index){
				layer.close(index); 
				save();
			},function(){
			});
		}else{
			save();
		}
	}else{
		save();
	}
}

function save(){
	var rules = {};
	var ajaxEdit = new AjaxEdit($('#recLossform'));
	ajaxEdit.beforeCheck = function() {// 校验之前
	};
	ajaxEdit.beforeSubmit = function() {// 提交前补充操作
		/*if(!checkDate()){
			return false;
		}*/
		/*var $kindCode = $(".kindCode:selected");
		if($kindCode.length > 0){
			var kc = $kindCode[0].value;
			for(var i = 1; i< $kindCode.length;i ++){
				if($kindCode[i].value != kc){
					return false;
					break;
				}
			}
		}*/
		//return true;
	};
	ajaxEdit.targetUrl = "/claimcar/recLoss/saveAll.do";
	ajaxEdit.rules = rules;
	ajaxEdit.afterSuccess = function(data) {// 操作成功后操作
		if(data.data != ""){
			layer.alert(data.data);
		}else{
			layer.confirm("操作成功", {
				btn: ['确定'] //按钮
			}, function(index){
				location.reload();
				layer.load();
			});
		}
	};
	// 绑定表单
	ajaxEdit.bindForm();

	$("#recLossform").submit();
}


//校验日期
function checkDate(){
	var damageTime = $("#damageTime").val();
	var recLossTime = $("#recLossTime").val();
	if(compareFullDate(recLossTime,damageTime)<0){
		layer.msg("损余回收日期不能早于出险时间");
		return false;
	}
	return true;
}
//比较两个日期字符串
//date1=date2则返回0 , date1>date2则返回1 , date1<date2则返回-1
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

//录入损余回收明细
function inputDetail(taskId,title){
	var url = "/claimcar/recLoss/recLossInit.do?flowTaskId="+taskId;
	layer.open({
		type : 2,
		area : [ '100%', '100%' ],
		fix : true, // 不固定
		maxmin : true,
		content : url,
		title : title
	});
}

//删除行
function deleteTr(field){
	var order =  getElementOrder(field);
	if(order == -1){
		layer.alert("不能全部删除回收信息");
		return false;
	}
	document.getElementById("table").deleteRow(order);
	calAllSumRecLossFee();
}

function getElementOrder(field) {
	var i = 0;
	var order = 0;
	var elements = document.getElementsByName(field.name);
	if(elements.length == 1){
		return -1;
	}
	for (i = 0; i < elements.length; i++) {
		order++;
		if (elements[i] == field) {
			break;
		}
	}
	return order;
}

