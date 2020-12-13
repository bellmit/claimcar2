
/**	   增加功能 模块	*/
var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0
		},{
			"data" : "id.codeCode"
		}, {
			"data" : "id.codeType"
		}, {
			"data" : "codeName"
		}, {
			"data" : "isValid",
			"render" : function(data, type, row) { 
					if(data == "N"){
						return "无效";
					}else if(data == "Y"){
						return "有效";
					}
				}
		},  {
			"data" : "remark"
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false
		} ];

/**		删除功能 模块*/
function rowCallback(row, data, displayIndex, displayIndexFull) {
	$('td:eq(0)', row).html(
			"&nbsp;&nbsp;<input name=\"checkCode\" type=\"checkbox\" value=\"" + data.id.codeCode+"&"+data.id.codeType
					+ "\">" + (displayIndex + 1));

	$('td:eq(6)', row)    
			.html("<a class='glyphicon glyphicon-eye-open' href='"
					+ contextPath
					+ "/sysCodeDict/view/" + data.id.codeCode+"/"+data.id.codeType+ "'/>"+
					"&nbsp;&nbsp;<a class='glyphicon glyphicon-edit' href='"
							+ contextPath
							+ "/sysCodeDict/edit/"
							+ data.id.codeCode+"/"+data.id.codeType
							+ "'/>"
							+ "&nbsp;&nbsp;<a class='glyphicon glyphicon-remove' href='#' onclick=\"deleteRow('"
							+ (displayIndex + 1) + "','" + data.id.codeCode+"','"+data.id.codeType + "');\"/>");

}
/** 			为删除模块 写的相关函数*/
function deleteRow(displayIndex, codeCode, codeType) {
	bootbox.confirm("确定要删除吗?", function(result) {
		if (result) {
			$.ajax({
				type : "GET",
				url : contextPath + "/sysCodeDict/delete/" + codeCode + "/" + codeType,
				data : "",
				async : false,
				success : function(obj) {
					if (obj.status == '200') {
						bootbox.alert("删除成功");
						$("#resultDataTable").find("tr").eq(displayIndex)
								.remove();
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					flag = false;
					bootbox.alert(textStatus + errorThrown);
				}
			});
		}
	});
}

//
// DataTables initialisation
//
$(function() {
	

	$("#checkAll").click(function() {
		$("input[type='checkbox']").attr("checked", this.checked);
	});
	// 控件事件
	$("button.btn-create").click(function() {
		location.href = contextPath + "/sysCodeDict/edit/new/new/?editMode=create";
	});
	// 控件事件
	$("button.btn-search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = contextPath + '/sysCodeDict/search?'
						+ $("#form").serialize();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			});
	$("button.btn-deleteAll").click(function() {
		var selectedIds = getSelectedIds();
		if (selectedIds == "") {
			bootbox.alert("请选择要删除的记录");
			return false;
		}
		var url = contextPath + "/sysCodeDict/deleteAll/" + selectedIds;
		batchDeleteRows("search", url);
	});
});