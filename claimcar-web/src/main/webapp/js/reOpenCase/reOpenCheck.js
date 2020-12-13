
$(function(){
	initCheckReOpen();
	var ajaxEdit = new AjaxEdit($('#form'));
	ajaxEdit.targetUrl = "/claimcar/reOpen/saveOrUpdata.do"; 
	ajaxEdit.afterSuccess = after;
	//绑定表单
	ajaxEdit.bindForm();
	
});
function submit(auditStatus){
	$("#auditStatus").val(auditStatus);
	$("#form").submit();
}

function initCheckReOpen(){
	if($("#handlerStatus").val()=='3'){//已处理
		$("body textarea").eq(0).attr("disabled","disabled");
		$("body input").each(function(){
			$(this).attr("disabled","disabled");
			if($(this).attr("type") == "button" || $(this).attr("type") == "submit"){
				$(this).addClass("btn-disabled");
			}
		}); 
	}
	var currentLevel = $("#currentNode").val().split("LV")[1];
	if(currentLevel<2){
		$("#submit").hide();
	}else{
		$("#submit").show();
		$("#audit").hide();
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

function after(result){
	var saveType = $("#auditStatus").val();
	var dataArr = result.data.split(",");
	var reOpenId = dataArr[0];//主表ID
	var flowTaskId = dataArr[1];//工作流ID
	var currentNode = $("#currentNode").val();//获取当前节点名称
	if(saveType=="save"){
		window.location.reload();
		layer.msg("暂存成功！");
	}else{
		var url = "/claimcar/reOpen/submitNextPage.do?reOpenId="+reOpenId+"&currentNode="+currentNode+
		"&flowTaskId="+flowTaskId+"&saveType="+saveType;
		layer.open({
			type: 2,
			closeBtn:0,
			title: "重开赔案提交",
			area: ['75%', '50%'],
			fix: true, //不固定
			maxmin: false,
			content: url,
	 	});
		$("#submitDiv").hide();
		$("#openReason").attr("disabled", "disbaled");
	}
}
