var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0
		}, {
			"data" : "id"
		}, {
			"data" : "userCode"
		}, {
			"data" : "saaRole.roleCode"
		}, {
			"data" : "startDate"
		}, {
			"data" : "endDate"
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false
		} ];
function rowCallback(row, data, displayIndex, displayIndexFull) {
	var url = contextPath + "/saa/user/delete/" + data.id;
	$('td:eq(0)', row).html(
			"<input name=\"checkCode\" type=\"checkbox\" value=\"" + data.id
					+ "\">&nbsp;&nbsp;<a  href='" + contextPath
					+ "/saa/user/view/" + data.id + "'>" + (displayIndex + 1)
					+ "</a>");
	$('td:eq(6)', row)
			.html("&nbsp;<a class='glyphicon glyphicon-eye-open' href='" 
					+ contextPath 
					+ "/saa/user/view/" 
					+ data.id 
					+ "'/>" 
					+ "&nbsp;<a class='glyphicon glyphicon-edit' href='" 
					+ contextPath 
					+ "/saa/user/edit/" 
					+ data.id 
					+ 
					"'/>" 
					+ "&nbsp;<a class='glyphicon glyphicon-remove' href='#' onclick=\"deleteRows('resultDataTable','" 
					+ url + "','" + (displayIndex + 1) 
					+ "');\"/>");


}

//时间框样式
$(".form_date").datetimepicker(
		{
			format : "yyyy-mm-dd",
			language : "zh-CN",
			autoclose : true,
			todayBtn : true,
			todayHighlight : true,
			showMeridian : false,
			minView : "month",
			pickerPosition : "bottom-left",
		
		});

$(function() {
	// 控件事件
	$("button.btn-create").click(function() {
		location.href = contextPath + "/saa/user/edit/0?editMode=create";
	});
	// 控件事件
	$("button.btn-search").click(
		function() {
			var ajaxList = new AjaxList("#resultDataTable");
			ajaxList.targetUrl = contextPath + '/saa/user/search?'
					+ $("#form").serialize();
			ajaxList.columns = columns;
			ajaxList.rowCallback = rowCallback;
			ajaxList.query();
	});
	//地址功能编辑
	$("button.btn-edit").click(function() {
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
		location.href = contextPath + "/saa/user/edit/" + id;
	});
	$("button.btn-deleteAll").click(function() {
		var selectedIds = getSelectedIds();
		if (selectedIds == "") {
			bootbox.alert("请选择要删除的记录");
			return false;
		}
		var url = contextPath + "/saa/user/deleteAll/" + selectedIds;
		batchDeleteRows("search",url);
	});

});
