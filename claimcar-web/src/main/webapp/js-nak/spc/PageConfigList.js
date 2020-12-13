var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0,
			"render" : function(data, type, row) {
				return '<input name="checkCode" type="checkbox" value="'
						+ row.id + '">';
			}
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 1
		}, {
			"data" : "pageCode",
			"orderSequence" : [ "asc" ] 
		}, {
			"data" : "description",
			"orderSequence" : [ "asc" ] 
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false
		}];
function rowCallback(row, data, displayIndex, displayIndexFull) {
	var url = contextPath + "/spc/delete/" + data.id;
	$('td:eq(1)', row).html((displayIndex + 1));
	$('td:eq(4)', row)
			.html(
					"<a class='glyphicon glyphicon-edit' href='"
							+ contextPath
							+ "/spc/edit/"
							+ data.id
							+ "'/>"
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
		location.href = contextPath + "/spc/edit/new?editMode=create";
	});
	// 控件事件
	$("button.btn-search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = contextPath + '/spc/search?'
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
		var url = contextPath + "/spc/deleteAll/" + selectedIds;
		batchDeleteRows("search",url);
	});
});
