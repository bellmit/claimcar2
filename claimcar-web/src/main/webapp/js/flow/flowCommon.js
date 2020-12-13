/**
 * 工作流公用js
 */

$(function() {
		$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
		bindValidForm($('#form'),search);
	});

/**
 * 打开任务编辑窗口
 */

/**切换选择的任务类型*/
function changeHandleStatus(obj){
	var handleStatusRadio=form.handleStatus;
	var status=$(obj).val();
	if(status=='3'){
		$("select[id='vsorting']").append("<option value='2'>核损通过时间</option>");
	}else{
		$("select[id='vsorting'] option[value='2']").remove();
	}
	
	var selectIdx=0;
	for(var i=0;i<handleStatusRadio.length;i++){
		if(handleStatusRadio[i].checked==true){
			selectIdx=i;
			break;
		}
	}
	$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", selectIdx);
}
/**文本框选中同时，文本框前radio被选中*/
function radioChecked(event){
	var val = $(event).prop("name");
	$("input[name='keyProperty']").each(
		function(){
			if( $(this).val()==val){
				$(this).prop("checked",true);
			}
		});
}

function changeHandleTab(val){
	if(val=='3'){
		$("select[id='vsorting']").append("<option value='2'>核损通过时间</option>");
	}else{
		$("select[id='vsorting'] option[value='2']").remove();
	}
	$("input[name='handleStatus']").each(
		function(){
			if( $(this).val()==val){
				$(this).prop("checked",true);
				var handleStatus=$("input[name='handleStatus']:checked").val();
				var tBody=$("#DataTable_"+handleStatus).find("tbody");
				if(!(tBody.children().length>0)){
					layer.load();
					setTimeout(function(){ 
						$('#form').submit();
						layer.closeAll('loading');
					},0.5*1000); 
					
				}
			}
});
	/* $("input[name='workStatus']").each(
			function(){
				if( $(this).val()==val){
					$(this).prop("checked",true);
					search();
				}
			}); */
}
//未处理原因按钮
function modification(urls){
	index=layer.open({
	    type: 2,
	    title: "未处理原因",
	    shade: false,
	    skin: 'yourclass',
	    area: ['710px', '295px'],
	    content:["/claimcar/common/init.do"+urls]
	});
}

/**
 * 工作流的字段链接
 * @param claimNo
 * @param registNo
 * @returns {Boolean}
 */
function prePayToclaimView(claimNo,registNo){
	var sign='0';//商业.交强标志位。0商业，1交强
	var returnflag=true;
	var partclaimNo=claimNo.substring(11,15); 
	var title="";
	if(partclaimNo=='1101'){
		title="立案(交强)处理";
		sign='1';
	}else{
		title="立案(商业)处理";
	}
	var url1="/claimcar/prePay/prePayToClaimView.do?registNo="+registNo+"&sign="+sign;
	var taskId="";
	$.ajax({
		url : url1, // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		async : false,
		success : function(result) {// 回调方法，可单独定义	
		
			if (result.status == "200") {
				    taskId=result.data;
				    if(taskId != null && taskId != "undefined" && taskId != "" && taskId != "null"){
				    	returnflag = false;
				    }
					
			}
		}
	});
	if(returnflag){
		return false;
	}
	var url2="/claimcar/claim/claimView.do?claimNo="+claimNo+"&flowTaskId="+taskId;
    openTaskEditWin(title,url2);
	
}

/**
 * 工作流的字段链接
 * @param registNo
 * @returns {Boolean}
 */
function thisToCompenView(registNo,compensateNo){
	var sign='0';//商业.交强标志位。0商业，1交强
	var returnflag=true;
	var partclaimNo=compensateNo.substring(11,15); 
	var title="";
	if(partclaimNo=='1101'){
		title="理算(交强)处理";
		sign='1';
	}else{
		title="理算(商业)处理";
	}
	var url1="/claimcar/compensate/thisToCompenView.do?registNo="+registNo+"&sign="+sign;
	var taskId="";
	$.ajax({
		url : url1, // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		async : false,
		success : function(result) {// 回调方法，可单独定义	
		
			if (result.status == "200") {
				    taskId=result.data;
				    
				    if(taskId != null && taskId != "undefined" && taskId != "" && taskId != "null"){
				    	
				    	returnflag = false;
				    }
					
					
			}
		}
	});
	
	if(returnflag){
		
		return false;
	}

	var url2="/claimcar/compensate/compensateEdit.do?flowTaskId="+taskId;
	
    openTaskEditWin(title,url2);
}
