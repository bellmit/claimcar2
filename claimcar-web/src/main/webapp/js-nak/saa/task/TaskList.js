var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0
		}, {
			"data" : "taskCode"
		}, {
			"data" : "groupName"
		}, {
			"data" : "taskCName"
		}, {
			"data" : "taskTName"
		}, {
			"data" : "taskEName"
		}, {
			"data" : "url"
		}, {
			"data" : "validInd",
			"render" : function(data, type, row) { 
				if(data == "0"){
					return "无效";
				}else if(data == "1"){
					return "有效";
				}
			}
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false
		}];
function rowCallback(row, data, displayIndex, displayIndexFull) {
	var url = contextPath + "/saa/task/delete/" + data.taskCode;
	$('td:eq(0)', row).html(
			"<input name=\"checkCode\" type=\"checkbox\" value=\"" + data.taskCode
					+ "\">&nbsp;&nbsp;<a  href='" + contextPath
					+ "/saatask/view/" + data.taskCode + "'>" + (displayIndex + 1)
					+ "</a>");
	$('td:eq(8)', row)
			.html("&nbsp;<a class='glyphicon glyphicon-eye-open' href='" 
					+ contextPath 
					+ "/saa/task/view/" 
					+ data.taskCode 
					+ "'/>" 
					+ "&nbsp;<a class='glyphicon glyphicon-edit' href='" 
					+ contextPath 
					+ "/saa/task/edit/" 
					+ data.taskCode 
					+ 
					"'/>" 
					+ "&nbsp;<a class='glyphicon glyphicon-remove' href='#' onclick=\"deleteRows('resultDataTable','" 
					+ url + "','" + (displayIndex + 1) 
					+ "');\"/>");

}

//
// DataTables initialisation
//
$(function() {
	// 控件事件
	$("button.btn-create").click(function() {
		location.href = contextPath + "/saa/task/edit/0?editMode=create";
	});
	// 控件事件
	$("button.btn-search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = contextPath + '/saa/task/search?'
						+ $("#form").serialize();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			});
	//地址功能编辑
	$("button.btn-eidtSaaTask").click(function() {
		var selectedIds = getSelectedIds();
		if (selectedIds == "") {
			bootbox.alert("请选择要编辑的记录");
			return false;
		}
		if (selectedIds.split(",").length > 1) {
			bootbox.alert("只能选择一条记录");
			return false;
		}
		var id = selectedIds.split(",")[0];
		location.href = contextPath + "/saa/task/edit/" + id;
	});
	//地址功能删除
	$("button.btn-deleteSaaTask").click(function() {
		var selectedIds = getSelectedIds();
		if (selectedIds == "") {
			bootbox.alert("请选择要删除的记录");
			return false;
		}
		if (selectedIds.split(",").length > 1) {
			bootbox.alert("只能选择一条记录");
			return false;
		}
		var id = selectedIds;
		var url = contextPath + "/saa/task/delete/" + id;
		//当前对象父亲的下一个兄弟的值
		var siblingVal = "";
		$("input[name='checkCode']:checked").each(function() {
			siblingVal = siblingVal + $(this).parent().children("a").html();
		});
		deleteRows('resultDataTable', url,siblingVal);
	});
	//地址功能批量删除
	$("button.btn-deleteAll").click(function() {
		var selectedIds = getSelectedIds();
		if (selectedIds == "") {
			bootbox.alert("请选择要删除的记录");
			return false;
		}
		var url = contextPath + "/saa/task/deleteAll/" + selectedIds;
		batchDeleteRows("search",url);
	});
//	//功能代码树状展示
//	$("button.btn-taskCodeTree").click(function() {
//		location.href = contextPath + "/saatask/taskCodeTree";
//	});
});