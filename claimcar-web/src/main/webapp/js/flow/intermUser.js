var index, index2;
var Prov, City;
var Bname, BQuery;
var BankNumber, BankOutlets, provCode, cityCode, bankKindCode;
var reIntermMainVo = null;

$(function() {
	var comCode=$("#comCode").val();
	var gradeId=$("#gradeId").val();
	initGetUserCodeSelect2("userCodeAjax",comCode,'2',1,"");
	$("#s2id_userCodeAjax").click(function(){
		var comCodeSelected=$("#comCode").val();
		if(comCodeSelected == ''){
			layer.alert("请先选中机构");
			return false;
		}
	});
	checkStatus();
	var id = $("input[name='prpdIntermMainVo.Id']").val();
	var ajaxEdit = new AjaxEdit($('#IntermEditForm'));
	ajaxEdit.targetUrl = "/claimcar/manager/existIntermCode.do";
	ajaxEdit.afterSuccess = function(returnData) {
		var exist = returnData.data;
		// 去掉这个公估机构代码唯一的管控
		/*if(exist == '1' && id == ""){
			layer.msg("已存在该机构代码！");
		}else{*/
			var params = $("#IntermEditForm").serialize();
			$.ajax({
				url: "/claimcar/manager/saveIntermInfo.do",
				type : 'post',
				dataType : 'json',
				data : params,
				async : false,
				success : function(result){
					if(result.status == 500){
						layer.alert(result.statusText);
					}else{
						var prpdIntermMain = eval(result);// 接收ajax返回的IntermMainVo，用于判断是否允许编辑公估服务页面
						if (prpdIntermMain != null) {
							$("#prpdIntermMainID").val(prpdIntermMain.data);
						}
						$("input").attr("disabled", "disabled");
						$("select").attr("disabled", "disabled");
						$("textarea").attr("disabled", "disabled");
						$(".btn").attr("style", "display:none");
						$("#bankInfoC").removeClass("btnClass").removeAttr("style");
						$("#info").removeClass("btnClass").removeAttr("style");
						$("#flag11").val("2");
					}
				
				}
			});
//		}
	};
	// 绑定表单
	ajaxEdit.bindForm();
});

function getBankNumTab(event, bankNumber, bankOutlets) {// 点击选中行号信息行

	$($(event).children()[0]).children().each(function() {
		if (this.type == "radio") {
			if (!this.checked) {
				this.checked = true;
			} else {
				this.checked = false;
			}
		}
	});
	$("#NumBankQueryT").val(bankOutlets);
	BankNumber = bankNumber;
	BankOutlets = bankOutlets;
	// alert(BankNumber);
};

function layerShowServ(title, id) {// 弹出资费信息页面
	var intermID = $("#prpdIntermMainID").val();
	if (intermID == "") {
		layer.msg("请先保存公估机构信息再设置资费标准！");
	} else {
		index = layer.open({
			type : 1,
			title : title,
			area : [ '900px', '350px' ],
			fix : false, // 不固定
			maxmin : false,
			content : $("#" + id)
		});
	}

};

function layerHidden() {// 资费信息界面关闭
	// 关闭后已添加的行内容disable，防止重复提交
	$("select[name='prpdIntermServerVo.ServiceType']").attr("disabled",
			"disabled");
	$("input[name='prpdIntermServerVo.FeeStandard']").attr("disabled",
			"disabled");
	$(".delbtn").attr("disabled", "disabled");
	layer.close(index);
}

$("#submit").click(function() { // 提交表单
	$("#IntermEditForm").submit();
});


$("#IntermBankName").click(function() {
	layer.msg("请点击银行信息按钮设置");
});
$("#IntermBankAccountNo ").click(function() {
	layer.msg("请点击银行信息按钮设置");
});
$("#IntermBankAccountNo").click(function() {
	layer.msg("请点击银行信息按钮设置");
});

function layerShowBankInfo() {
	var id = $("input[name='prpdIntermMainVo.Id']").val();
	var url = '/claimcar/manager/intermediaryEdit_Bank.do?Id=' + id;
	layer.open({
		type : 2,
		area : [ '900px', '345px' ],
		fix : false, // 不固定
		maxmin : false,
		shadeClose : true,
		scrollbar : true,
		skin : 'yourclass',
		title : "银行信息",
		content : url,
		end : function() {

		}
	});
};
// 增加员工信息
function addIntermUserTr(intermId) {
	var selectedContent=$("#intermUserCode").val();
	var selectedItem =$("#intermUserCode").find("option[value='"+selectedContent+"']").text();
	var userCode = selectedItem.split("-")[0];
	var userName = selectedItem.split("-")[1];
	// 增加人员参数
	var $table_cont = $("#intermUserTbody");
	var $intermUser_size = $("#intermUserSize");// 人员条数
	var intermSize = parseInt($intermUser_size.val(), 10);
	var params = {
		"per_index" : intermSize,
		"intermSize" : intermSize,
		"userCode" : userCode,
		"userName" : userName,
		
	};
	// 获取页面intermCode 防止添加相同的员工信息
	var arr = new Array(intermSize);
	var flagPreventRepetition = false ;
	for(var i=0;i<arr.length;i++){
		arr[i]=$("#interUserCodeAA"+i).val();
		if(arr[i] ==userCode ){
			flagPreventRepetition = true;
		}
	}
	
	if(selectedContent == null || selectedContent == ""){
		layer.alert("请选择要增加的人员信息！");
		return false;
	}else if(flagPreventRepetition){
		layer.alert("不能重复添加人员信息！");
		 return false;
	}else{
		var url = "/claimcar/manager/addPersRow.ajax";
		$.post(url, params, function(result) {
			$table_cont.append(result);
			$intermUser_size.val(intermSize + 1);
		});
	}
};
function intermUserDelete111(element) {
	
	var index = $(element).attr("name").split("_")[1];// 下标
	var intermUserPrefix = "prpdIntermUserVo";
	var $parentTr = $(element).parent().parent();
	var size = parseInt($("#intermUserSize").val(),10);
	
	$("#intermUserSize").val(size - 1);// 删除一条
	delTr($("#intermUserSize"), index, "delIntermUser_", intermUserPrefix);
	
	$parentTr.remove();
};

function checkStatus(){
	var flag = $("#flag").val();
	if(flag == 'look'){
		$("input").attr("disabled", "disabled");
		$("select").attr("disabled", "disabled");
		$("textarea").attr("disabled", "disabled");
		$(".btn").attr("style", "display:none");
		$("#bankInfoC").removeClass("btnClass").removeAttr("style");
		$("#info").removeClass("btnClass").removeAttr("style");
		$("#flag11").val("2");
	}
}

function insertIntermName(){
	var code = $("#intermCode").val();
	var code_Name =$("#intermCode").find("option[value='"+code+"']").text();
	var name = code_Name.split("-")[1];
	$("#intermName").val(name);
}