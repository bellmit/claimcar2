var saaFiledColumns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0
		}, {
			"data" : "saaFactor.factorCode"
		}, {
			"data" : "fieldCode"
		}, {
			"data" : "entityCode"
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false
		} ];

function filedRowCallback(row, data, displayIndex, displayIndexFull) {
		var url = contextPath + "/saafield/delete/" + data.id;
		$('td:eq(0)', row).html(
				"<input name=\"checkCode\" type=\"checkbox\" value=\"" + data.id
						+ "\">&nbsp;&nbsp;<a href='#'>" + (displayIndex + 1)+"</a>");
		$('td:eq(4)', row)
		.html(
				"<a class='glyphicon glyphicon-edit' href='"
						+ contextPath
						+ "/saafield/edit/"
						+ data.id
						+ "'/>"
						+ "&nbsp;<a class='glyphicon glyphicon-remove' href='#' onclick=\"deleteRows('resultSaaFiledDataTable','"
						 + url + "','" + (displayIndex + 1) 
						+ "');\"/>");

}
//权限因子弹出对话框
function factorLookupDialog(){
		var $modal = $('#ajax-factorModal');
		$('body').modalmanager('loading');
		$modal.load(contextPath + "/saauserconfig/factorLookupList.dialog", '', function(){
		      $modal.modal();
		    });
};
// DataTables initialisation
$(function() {
	$("#checkAll").click(function() {
		$("input[type='checkbox']").attr("checked", this.checked);
	});
	// 控件事件
	$("button.btn-create").click(function() {
		location.href = contextPath + "/saafield/edit/new/?editMode=create";
	});
	// 控件事件
	$("button.btn-search").click(
			function() {
				var ajaxList = new AjaxList("#resultSaaFiledDataTable");
				ajaxList.targetUrl = contextPath + '/saafield/search?'
						+ $("#saafieldForm").serialize();
				ajaxList.columns = saaFiledColumns;
				ajaxList.rowCallback = filedRowCallback;
				ajaxList.query();
			});
	//编辑权限因子
	$("button.btn-eidtSaaField").click(function() {
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
		location.href = contextPath + "/saafield/edit/" + id;
	});
	//删除权限因子
	$("button.btn-deleteSaaField").click(function() {
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
		var url = contextPath + "/saafield/delete/" + id;
		//当前对象父亲的孩子a
		var siblingVal = "";
		$("input[name='checkCode']:checked").each(function() {
			siblingVal = siblingVal + $(this).parent().children("a").html();
		});
		deleteRows("resultSaaFiledDataTable", url,siblingVal);
	});
	//权限因子批量删除
	$("button.btn-deleteAll").click(function() {
		var selectedIds = getSelectedIds();
		if (selectedIds == "") {
			bootbox.alert("请选择要批量删除的记录");
			return false;
		}
		var url = contextPath + "/saafield/deleteAll/" + selectedIds;
		batchDeleteRows("search",url);
	});
});