$(function() {
	initPersTracePLBig();

	var ajaxEdit = new AjaxEdit($('#PLBigForm'));
	ajaxEdit.targetUrl = "/claimcar/persTracePLBig/saveOrSubmit.do";
	ajaxEdit.beforeSubmit = function(data) {
		layer.load(0, {
			shade : [0.8, '#393D49']
		});
	};
	ajaxEdit.afterSuccess = function(data) {
		// 暂存成功后把显示tab页的名称
		if (data.data == "save") {
			layer.msg("暂存成功");
		} else {
			$("body :input").attr("disabled", "disbaled");
			$("#submitDiv").hide();
			var resultData = eval(data).data;
			layer.confirm(resultData, {
				btn : [ '关闭' ]
			// 按钮
			}, function(index) {
				layer.close(index);
			});
		}
	};

	// 绑定表单
	ajaxEdit.bindForm();

});

function clickSendMail(obj){
	 var $radio = $(obj);
	 if ($radio.data('waschecked') == true){
         $radio.prop('checked', false);
         $radio.data('waschecked',false);
         $("input[name$='sendMail']").val(0);
     } else {
         $radio.prop('checked', true);
         $radio.data('waschecked', true);
         $("input[name$='sendMail']").val(1);
     }
}

// 初始化页面
function initPersTracePLBig() {
	if ($("#handlerStatus").val() == '3') {// 处理完成的节点禁用编辑
		$("body :input").attr("disabled", "disabled");
	}

	if ($("#intermediaryFlag").val() == '1') {// 公估定损
		$(".intermediary").show();
		$(".persTracePerson").hide();
	} else {
		$(".intermediary").hide();
		$(".persTracePerson").show();
	}
}

function submitNextNode(element) {
	var registNo = $("#registNo").val();
	var currentNode = $("#flowNodeCode").val();
	var currentName = $("#flowNodeName").val();
	var auditStatus = $(element).attr("id");// 提交动作
	
	//所有人员合计总估损金额
	var allSumReportFee = 0;
	$("input[name$='sumReportFee']").each(function(){
		var tabPageNo = $(this).attr("name").split("_")[0];
		var validFlag = $("#"+tabPageNo+"_validFlag").val();
		var sumReportFee = $("input[name='"+tabPageNo+"_sumReportFee']").val();
		if(validFlag =='1'){
			allSumReportFee += parseFloat(sumReportFee);
		}
	});
	
	//所有人员合计总定损金额
	var allSumdefLoss = 0;
	$("input[name$='sumdefLoss']").each(function(){
		var tabPageNo = $(this).attr("name").split("_")[0];
		var validFlag = $("#"+tabPageNo+"_validFlag").val();
		var sumdefLoss = $("input[name='"+tabPageNo+"_sumdefLoss']").val();
		if(validFlag =='1'){
			allSumdefLoss += parseFloat(sumdefLoss);
		}
	});

	if (auditStatus == 'save') {
		$("#PLBigForm").submit();
	} else {
		var params = {
			"currentNode" : currentNode,
			"currentName" : currentName,
			"auditStatus" : auditStatus,
			"allSumReportFee" : allSumReportFee,
			"allSumdefLoss" : allSumdefLoss,
			"registNo" : registNo,
		};
		
		$.ajax({
			url : "/claimcar/loadAjaxPage/loadSubmitPLBig.ajax",
			type : "post",
			data : params,
			async : false,
			success : function(result) {
				// 页面层
				layer.open({
					title : "人伤审核任务提交",
					type : 1,
					skin : 'layui-layer-rim', // 加上边框
					area : ['480px', '320px'], // 宽高
					content : result,
					yes : function(index, layero) {
						var html = layero.html();
						$("#hideDiv").empty();
						$("#hideDiv").append(html);
						$("#hideDiv").hide();
						layer.close(index);

						$("#PLBigForm").submit();
					}
				});
			}
		});
		
		
	}
}
