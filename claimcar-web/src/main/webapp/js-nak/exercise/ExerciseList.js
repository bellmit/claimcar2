
/**	   增加功能 模块	*/
var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0
		},
		// {
		// "data" : "id",
		// "orderSequence" : [ "asc" ]
		// },
		{
			"data" : "testerName"
		}, {
			"data" : "testerSex",
			"render" : function(data, type, row) {
				if (row.testerSex == 1) {
					return "男";

				} else {
					return "女";
				}
			}

		}, {
			"data" : "testerAge",
			"class" : "right"
		}, {
			"data" : "testerIp"
		}, {
			"data" : "testDate",
			"render" : function(data, type, row) {
				return data.substring(0, 10);
			},
			"class" : "right"
		}, {
			"data" : "testScore",
			"class" : "right"
		}, {
			"data" : "testSummary"
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false
		} ];

/**		删除功能 模块*/
function rowCallback(row, data, displayIndex, displayIndexFull) {
	var url = contextPath + "/exercise/delete/" + data.id;
	$('td:eq(0)', row).html(
			"<input name='checkCode' type='checkbox' value='" + data.id
					+ "'/>&nbsp;&nbsp;" + (displayIndex + 1));

	$('td:eq(8)', row)
			.html("<a class='glyphicon glyphicon-eye-open' href='"
					+ contextPath
					+ "/exercise/view/" + data.id 
					+ "'/>"+
					"&nbsp;&nbsp;<a class='glyphicon glyphicon-edit' href='"
							+ contextPath
							+ "/exercise/edit/"
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
	// 1.控件初始化
	$(".form_date").datetimepicker({
		format : "yyyy-mm-dd",
		language : "zh-CN",
		autoclose : true,
		todayBtn : true,
		todayHighlight : true,
		showMeridian : false,
		minView : "month",
		pickerPosition : "bottom-left",
	});

	$("#checkAll").click(function() {
		$("input[type='checkbox']").attr("checked", this.checked);
	});
	// 控件事件
	$("button.btn-create").click(function() {
		location.href = contextPath + "/exercise/edit/0?editMode=create";
	});
	// 控件事件
	$("button.btn-search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = contextPath + '/exercise/search';
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
		var url = contextPath + "/exercise/deleteAll/" + selectedIds;
		batchDeleteRows("search", url);
	});
});