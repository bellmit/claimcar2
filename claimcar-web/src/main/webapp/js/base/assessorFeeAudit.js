var ajaxEdit;
$(function() {
	ajaxEdit = new AjaxEdit($('#fm'));
	ajaxEdit.targetUrl = "/claimcar/assessors/submitAssessorFeeAudit.do";
	ajaxEdit.rules = null;
	ajaxEdit.beforeSubmit = function(data) {
		layer.load(0, {
			shade : [0.8, '#393D49']
		});
	};
	ajaxEdit.afterSuccess = function(data) {
		$("body :input").attr("disabled", "disbaled");
		$("#EXButton").removeAttr("disabled");
		$("#submitDiv").hide();
	};
	// 绑定表单
	ajaxEdit.bindForm();

	if ($("#handlerStatus").val() == "3") {
		$("body :input").attr("disabled", "disbaled");
		$("#EXButton").removeAttr("disabled");
	}
	$("input[name$='remark']").attr("readonly","readonly");
	
	

});

function submitNextNode(element) {
	var a=$(element).val();
   if(a=='退回申请'){
	   $("#assRemark").removeAttr("datatype");
   }
	var auditStatus = $(element).attr("id");// 提交动作
	$("#audit").val(auditStatus);
	$("#fm").submit();
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