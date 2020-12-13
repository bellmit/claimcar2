var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0
		},{
			"data" : "roleCode",
			"orderSequence" : [ "asc" ],
			"render" : function(data, type, row) {
				return data;
			}
		}, {
			"data" : "comCode"
		}, {
			"data" : "roleCName"
		}, {
			"data" : "roleTName"
		}, {
			"data" : "roleEName"
		}, {
			"data" : "validInd",
			"render" : function(data, type, row) { 
				if(data == "0"){
					return "无效";
				}else if(data == "1"){
					return "有效";
				}
			}
		},{
			"data" : null,
			"searchable" : false,
			"orderable" : false
		} ];
function rowCallback(row, data, displayIndex, displayIndexFull) {
		var url = contextPath + "/saa/role/delete/" + data.roleCode;
		$('td:eq(0)', row).html(
				"<input name=\"checkCode\" type=\"checkbox\" value=\"" + data.roleCode
						+ "\">&nbsp;&nbsp;<a href='#'>" + (displayIndex + 1)+"</a>");
		$('td:eq(7)', row)
		.html(
				"&nbsp;<a class='glyphicon glyphicon-eye-open' href='" 
					+ contextPath 
					+ "/saa/role/view/" 
					+ data.roleCode 
					+ "'/>" 
					+ "&nbsp;<a class='glyphicon glyphicon-edit' href='"
					+ contextPath
					+ "/saa/role/edit/"
					+ data.roleCode
					+ "'/>"
					+ "&nbsp;<a class='glyphicon glyphicon-remove' href='#' onclick=\"deleteRows('resultDataTable','"
					+ url + "','" + (displayIndex + 1) 
					+ "');\"/>");

}

// DataTables initialisation
$(function() {
	$("#checkAll").click(function() {
		$("input[type='checkbox']").attr("checked", this.checked);
	});
	// 控件事件
	$("button.btn-create").click(function() {
		location.href = contextPath + "/saa/role/edit/0?editMode=create";
	});
	// 控件事件
	$("button.btn-search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = contextPath + '/saa/role/search?'
						+ $("#form").serialize();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			});
	//编辑角色
	$("button.btn-eidtSaaRole").click(function() {
		var selectedIds = getSelectedIds();
		if (selectedIds == "") {
			bootbox.alert("请选择要编辑的记录");
			return false;
		}
		if (selectedIds.split(",").length > 1) {
			bootbox.alert("只能选择一条记录");
			return false;
		}
		var id = selectedIds;
		location.href = contextPath + "/saa/role/edit/" + id;
	});
	//删除角色
	$("button.btn-deleteSaaRole").click(function() {
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
		var url = contextPath + "/saa/role/delete/" + id;
		//当前对象父亲的下一个兄弟的值
		var siblingVal = "";
		$("input[name='checkCode']:checked").each(function() {
			siblingVal = siblingVal + $(this).parent().children("a").html();
		});
		deleteRows("resultDataTable", url,siblingVal);
	});
	//角色批量删除
	$("button.btn-deleteAll").click(function() {
		var selectedIds = getSelectedIds();
		if (selectedIds == "") {
			bootbox.alert("请选择要批量删除的记录");
			return false;
		}
		var url = contextPath + "/saa/role/deleteAll/" + selectedIds;
		batchDeleteRows("search",url);
	});
});