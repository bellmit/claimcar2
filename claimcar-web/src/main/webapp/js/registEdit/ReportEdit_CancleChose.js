function closeLayer(){
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	window.parent.location.reload();
	parent.layer.close(index);	
	
}

//报案注销
function registCancle(){ 
	var cancelReason = "";
	$("#cancelOut select").each(function() {
		cancelReason = $(this).val();
	});
	if (cancelReason == "") {
		layer.msg("请选择注销原因！");
		return false;
	}
	$("#centain").removeAttr("onclick");
	var registNos = $("#registNo").val();
	$.ajax({
		url : "/claimcar/regist/reportCancels.do?registNo="+registNos, // 后台处理程序
		type : 'post', // 数据发送方式
		success : function(jsonData) {// 回调方法，可单独定义
			if(jsonData.status==200){
				if(jsonData.data==1){					
					$("#cancelOut select").each(function() {
						$(this).attr("disabled",true);
					});
										
					var registNo = $("#registNo").val();
										
					var params = {
						"cancelReason" : cancelReason,
						"registNo" : registNo
					};
					var url = "/claimcar/regist/reportCancel.ajax";
					$.post(url, params, function(result) {
						layer.msg('注销成功!',{time: 1000},function(index){
							layer.close(index);
							closeLayer();
						});						
					});				
			}else if(jsonData.data==0){
				layer.alert("已立案不能报案注销");
				}
			}	
		}
	});
};
function claimCancle(){
	var claimNo="";
	var CIclaimNo=$("#CIclaimNo").val();
	var BIclaimNo=$("#BIclaimNo").val();
	var sign=$("input[type='radio']:checked").val();
	if(sign=='1'){
		claimNo=CIclaimNo;
	}else if(sign=='2'){
		claimNo=BIclaimNo;
	}
	
	if(isBlank(claimNo)){
		alert("请选择一个立案注销！");
		return false;
	}
	var title='立案注销拒赔申请处理';
	var url='/claimcar/claim/claimCancelInit.do?claimNo='+claimNo+"&taskId=c"+"&workStatus=c"+"&handlerIdKey=c";
	openWinCom(title,url);
}