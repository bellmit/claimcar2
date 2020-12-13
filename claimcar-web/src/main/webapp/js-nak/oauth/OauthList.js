var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0,
			"render" : function(data, type, row) {
				return '<input name="checkCode" type="checkbox" value="'
						+ row.clientId + '">';
			}
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 1
		}, {
			"data" : "clientId",
			"orderSequence" : [ "asc" ],
			"render" : function(data, type, row) { 
				return "<a  href='"
						+ contextPath
						+ "/oauth/view/"
						+ data
						+ "'>" + data  + "</a>";
			}
		}, {
			"data" : "clientSecret"
		},{
			"data" : null,
			"searchable" : false,
			"orderable" : false
		} ];
function rowCallback(row, data, displayIndex, displayIndexFull) {
	var url = contextPath + "/oauth/delete/" + data.clientId;
	$('td:eq(1)', row).html("<a  href='"
			+ contextPath
			+ "/oauth/view/"
			+ data.clientId
			+ "'>" + (displayIndex + 1) + "</a>");
	$('td:eq(4)', row)
			.html(
					"<a class='glyphicon glyphicon-edit' href='"
							+ contextPath
							+ "/oauth/edit/"
							+ data.userCode
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
		location.href = contextPath + "/oauth/edit/new/?editMode=create";
	});
	// 控件事件
	$("button.btn-search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = contextPath + '/oauth/search?'
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
		var url = contextPath + "/oauth/deleteAll/" + selectedIds;
		batchDeleteRows("search",url);
	});
	
	

});
	


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	