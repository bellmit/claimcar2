$(function() {
	layer.config({
		extend : 'extend/layer.ext.js'
	});
	var rule = [{
		ele : "input[name='claimNoArr']",
		datatype : "checkBoxMust"
	}];

	var ajaxEdit = new AjaxEdit($('#prePayform'));
	ajaxEdit.targetUrl = "/claimcar/prePay/prePayTaskApply.do";
	ajaxEdit.rules = rule;
	ajaxEdit.beforeSubmit = function(data) {
		layer.load(0, {
			shade : [0.8, '#393D49']
		});
	};
	ajaxEdit.afterSuccess = function(data) {
		location.reload();
	};
	// 绑定表单
	ajaxEdit.bindForm();

	$.Datatype.checkBoxMust = function(gets, obj, curform, regxp) {
		var need = 1, numselected = curform.find("input[name='" + obj.attr("name") + "']:checked").length;
		return numselected >= need ? true : "请至少选择" + need + "项！";
	};

});

function prePayTaskLaunch() {
	
	var claimNoArr = $("input[name='claimNoArr']:checked");
	if (claimNoArr.length > 0) {
		$.ajax({
			url : "/claimcar/prePay/loadSubmitNext.ajax",
			type : "post",
			async : false, 
			success : function(htmlData){
				layer.open({
					title : "提示信息",
					type : 1,
					skin : 'layui-layer-rim', // 加上边框
					area : ['60%', '50%'], // 宽高
					content : htmlData,
					yes : function(index, layero) {
						var html = layero.html();
						/*$("#hideDiv").empty();
						$("#hideDiv").append(html);
						$("#hideDiv").hide();*/
						layer.close(index);

						$("#prePayform").submit();
					}
				});
			}
		});
	} else {
		layer.msg("请选择需要进行预付的保单号");
	}

}