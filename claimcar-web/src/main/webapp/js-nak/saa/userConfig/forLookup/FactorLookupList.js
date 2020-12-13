var columns3 = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 1
		}, {
			"data" : "factorCode",
			"orderSequence" : [ "asc" ]
		}, {
			"data" : "factorDesc",
			"orderSequence" : [ "asc" ]
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false
		}];
function rowCallback(row, data, displayIndex, displayIndexFull) {
	$('td:eq(0)', row).html(displayIndex + 1);
	$('td:eq(3)', row).html(
			"<a class='glyphicon glyphicon-check' href='javascript:void(0)' title='选择带回' onclick=\"factorLookupValues('"+ data.id + "','" + data.factorCode + "','" + data.factorDesc + "');\"/>");
}
function factorLookupValues(factorId,factorCode,factorDesc){
	//员工业务权限配置页面权限因子查找带回回填数据
	if($("input[name='factorList[0].factorId']").length > 0){
			$("input[name='factorList["+factorIndex+"].factorId']").val(factorId);
			$("input[name='factorList["+factorIndex+"].factorCode']").val(factorCode);
			$("input[name='factorList["+factorIndex+"].factorDesc']").val(factorDesc);
	}else if($("#factorCodeVal1").length > 0){//因子属性管理列表页面查找带回回填数据
		$("#factorCodeVal1").val(factorCode);
	}if($("#factorCodeVal2").length > 0 && $("#factorIdVal2").length > 0 ){//因子属性管理编辑页面查找带回回填数据
		$("#factorCodeVal2").val(factorCode);
		$("#factorIdVal2").val(factorId);
	}
	//关闭对话框
	$("#closeDialog3").click();
	
}
//
// DataTables initialisation
//
//模态对话框大小设置
$(function() {
	$("button.btn-search3").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable3");
				ajaxList.targetUrl = contextPath + '/saauserconfig/factorLookupSearch?'
						+ $("#form3").serialize();
				ajaxList.columns = columns3;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			});
});	