
/**	   增加功能 模块	*/
var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0
		},
		{
			"data" : "areaCode"
		}, {
			"data" : "areaName"
		}, {
			"data" : "fullName"
		}, {
			"data" : "areaLevel",
			"render" : function(data, type, row) {
				if (row.areaLevel == 1) {
					return "省级";

				} else  if(row.areaLevel == 2){
					return "市级";
				} else  if(row.areaLevel == 3){
					return "区、县级";
				} else  if(row.areaLevel == 4){
					return "街道级";
				}
			}
		},  {
			"data" : "postCode"
		},  {
			"data" : null,
			"searchable" : false,
			"orderable" : false
		} ];

/**		删除功能 模块*/
function rowCallback(row, data, displayIndex, displayIndexFull) {
	var  url = contextPath + "/sysAreaDict/delete/" +data.areaCode;
	$('td:eq(0)', row).html(
			"&nbsp;&nbsp;<input name=\"checkCode\" type=\"checkbox\" value=\"" + data.areaCode
					+ "\">"+(displayIndex + 1));

	$('td:eq(6)', row)
	.html("<a class='glyphicon glyphicon-eye-open' href='"
			+ contextPath
			+ "/sysAreaDict/view/" + data.areaCode 
			+ " '/> "+
			"&nbsp;&nbsp;<a class='glyphicon glyphicon-edit' href='"
					+ contextPath
					+ "/sysAreaDict/edit/"
					+ data.areaCode
					+ "'/>"
					+ "&nbsp;&nbsp;<a class='glyphicon glyphicon-remove' href='#' onclick=\"deleteRows('resultDataTable','"
					 + url + "','" + (displayIndex + 1) 
					+ "');\"/>");

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
		location.href = contextPath + "/sysAreaDict/edit/new/?editMode=create";
	});
	// 控件事件
	$("button.btn-search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = contextPath + '/sysAreaDict/search?'
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
		var url = contextPath + "/sysAreaDict/deleteAll/" + selectedIds;
		batchDeleteRows("search", url);
	});
});