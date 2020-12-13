var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0
		}, {
			"data" : "userCode",
			"orderSequence" : [ "asc" ],
			"render" : function(data, type, row) {
				return data + ' (' + row.userName + ')';
			}
		}, {
			"data" : "userName"
		}, {
			"data" : "prpDcompany.comCode"
		}, {
			"data" : "prpDcompany.comCName"
		}];
function rowCallback(row, data, displayIndex, displayIndexFull) {
	$('td:eq(0)', row).html(
			"<input name=\"checkCode\" type=\"checkbox\" value=\"" + data.userCode
					+ "\">&nbsp;&nbsp;" + (displayIndex + 1));
}

//DataTables initialisation
$(function() {
	// 控件事件
	$("button.btn-create").click(function() {
		location.href = contextPath + "/user/edit/new/?editMode=create";
	});
	// 控件事件(搜索)
	$("button.btn-search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = contextPath + '/saauserconfig/search?'
						+ $("#form").serialize();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			});
	// 控件事件(进入员工-角色配置列表页面)
	$("button.btn-updateUserRoleList").click(function() {
		var selectedIds = getSelectedIds();
		if (selectedIds == "") {
			bootbox.alert("请选择要配置的员工角色的记录");
			return false;
		}
		if (selectedIds.split(",").length > 1) {
			bootbox.alert("只能选择一条记录");
			return false;
		}
		var userCode = selectedIds;
		location.href = contextPath + "/saauserconfig/updateUserRoleList/" + userCode;
	});
	// 控件事件(员工-角色展示)
	$("button.btn-viewRoles").click(function() {
		var selectedIds = getSelectedIds();
		if (selectedIds == "") {
			bootbox.alert("请选择要展示的员工角色的记录");
			return false;
		}
		if (selectedIds.split(",").length > 1) {
			bootbox.alert("只能选择一条记录");
			return false;
		}
		var userCode = selectedIds;
		location.href = contextPath + "/saauserconfig/userPowerlist/" + userCode;
	});	
	$("button.btn-deleteAll").click(function() {
		var selectedIds = getSelectedIds();
		if (selectedIds == "") {
			bootbox.alert("请选择要删除的记录");
			return false;
		}
		var url = contextPath + "/user/deleteAll/" + selectedIds;
		batchDeleteRows("search",url);
	});
});