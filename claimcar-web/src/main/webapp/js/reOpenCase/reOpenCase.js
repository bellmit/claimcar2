$(function() {
	var handleStatus=$("#handleStatus").val();
	if(handleStatus=="3"){
		$("#btnDiv").hide();
		$("#openReason").attr("disabled", "true");
	}
	layer.config({
		extend : 'extend/layer.ext.js'
	});
	var rule = [{
		ele : "input[name='claimNoArr']",
		datatype : "checkBoxMust"
	}];
    var registNo = $("input[name='registNo']");
	var ajaxEdit = new AjaxEdit($('#form'));
	ajaxEdit.targetUrl = "/claimcar/reOpen/appSubmit.do";
	ajaxEdit.rules = rule;
	ajaxEdit.beforeSubmit = function(data) {
		layer.load(0, {
			shade : [0.8, '#393D49']
		});
	};
	ajaxEdit.afterSuccess = function(data) {
		if(data.status=="200"){
			$("#btnDiv").hide();
			$("#openReason").attr("disabled", "disbaled");
		}	
		//location.reload();
	};
	// 绑定表单
	ajaxEdit.bindForm();

	$.Datatype.checkBoxMust = function(gets, obj, curform, regxp) {
		var need = 1, numselected = curform.find("input[name='" + obj.attr("name") + "']:checked").length;
		return numselected >= need ? true : "请至少选择" + need + "项！";
	};

});
        
		
		// 返回
		function cancelReOpen() {
			var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
			parent.layer.close(index);// 执行关闭
		};
		
		$("input[name='prpLUserHolidayVo.checkStatus']").each(function(){
			if($(this).prop("checked")==true){
				var radio=$("this").val();
			}
		});
		//提交界面初始化
		function initReOpenSubmit() {
			var claimNoArr = $("input[name='claimNoArr']:checked");
			if (claimNoArr.length > 0) {
				$.ajax({
					url : "/claimcar/reOpen/initReOpenSubmit.ajax",
					type : "post",
					async : false, 
					success : function(htmlData){
						layer.open({
							title : "提示信息",
							type : 1,
							skin : 'layui-layer-rim', // 加上边框
							area : ['65%', '50%'], // 宽高
							content : htmlData,
							yes : function(index, layero) {
								var html = layero.html();
								layer.close(index);

								$("#form").submit();
							}
						});
					}
				});
			} else {
				layer.msg("请选择需要发起重开赔案的立案号");
			}
		}
		
		function lookReCaseHis(claimNo){
			index=layer.open({
			    type: 2,
			    title: "赔案信息表的查看页面",
			    shadeClose: true,
			    scrollbar: false,
			    skin: 'yourclass',
			    area: ['1000px', '550px'],
			    content:"/claimcar/reOpen/reOpenCaseView.do?claimNo="+claimNo+"",
			});
		}