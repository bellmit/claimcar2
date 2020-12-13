
	function rowCallback(row, data, displayIndex, displayIndexFull) {
		var handleStatus=$("input[name='handleStatus']:checked").val();
		if(handleStatus=='0'){
			$('td:eq(1)', row).html("<a onclick=reOpenAppTaskVlaid('"+data.registNo+"');>"+data.registNo+"</a>");
			if(data.policyNo!=null){
				$('td:eq(2)',row).html("<a href=/claimcar/policyView/policyView.do?registNo="+data.registNo+" target='_blank'>"+data.policyNo+"</a>");
			}
		}else{
			$('td:eq(1)', row).html("<a onclick=reOpenAppTaskVlaid('"+data.endCaseNo+"');>"+data.registNo+"</a>");
			if(data.remark!=null){
				$('td:eq(2)',row).html("<a href=/claimcar/policyView/policyView.do?registNo="+data.registNo+" target='_blank'>"+data.remark+"</a>");
			}
		}
		
		if(data.policyNo!=null){
			$('td:eq(2)',row).html("<a href=/claimcar/policyView/policyView.do?registNo="+data.registNo+" target='_blank'>"+data.policyNo+"</a>");
		}
	}
	function search(){
		var handleStatus=$("input[name='handleStatus']:checked").val();
		if(isBlank(handleStatus)){
			layer.msg("请选择任务状态");
			return false;
		}
		var ajaxList = new AjaxList("#DataTable_"+handleStatus);
		ajaxList.targetUrl = '/claimcar/reOpen/reOpenAppSearch.do';
		ajaxList.postData = $("#form").serializeJson();
		ajaxList.rowCallback = rowCallback;
		if(handleStatus=='0'){
			ajaxList.columns = columns0;
		}else if(handleStatus=='3'){
			ajaxList.columns = columns3;
		}
		ajaxList.query();
}
	/** 异步校验（发起重开赔案任务）**/
	function reOpenAppTaskVlaid(num){
		var handleStatus=$("input[name='handleStatus']:checked").val();
		var reOpenApp_url = "/claimcar/reOpen/reOpenAppInit.do?num="+num+"&handleStatus="+handleStatus+"";
		var url = "/claimcar/reOpen/reOpenValid.do";
		var returnflag = true;
//		var params = {
//				"registNo" : num
//			};
//		if(handleStatus == '0'){
//			$.ajax({
//				url : url, // 后台校验
//				type : 'post', // 数据发送方式
//				dataType : 'json', // 接受数据格式
//				data : params, // 要传递的数据
//				async : false,
//				success : function(jsonData) {// 回调方法，可单独定义	
//					var result = eval(jsonData);
//					if (result.status == "200") {
//						if (result.data != "ok") {
//							layer.alert(result.data);
//							returnflag = false;
//						}
//					}
//				}
//			});
//		}
		
		if (!returnflag) {
			return false;
		} else {
			openTaskEditWin("重开赔案任务发起",reOpenApp_url);
		}
	}