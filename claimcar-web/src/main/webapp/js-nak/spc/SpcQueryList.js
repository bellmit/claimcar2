var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0
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
	var url = contextPath + "/spcQuery/delete/" + data.id;
	$('td:eq(0)', row).html('&nbsp;&nbsp;<input name="checkCode" type="checkbox" value="'
			+ data.id + '">&nbsp;&nbsp;' + (displayIndex + 1));
	$('td:eq(3)', row)
			.html(
					"<a class='glyphicon glyphicon-edit' href='"
							+ contextPath
							+ "/spcQuery/edit/"
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
	
	function showModalDialog(href,s){
		var $modal = $("#ajax-showUpload");
		$modal.load(href,s, function(){
		      $modal.modal();
		 });
	}
	
	// 控件事件
	$("button.btn-create").click(function() {
		location.href = contextPath + "/spcQuery/edit/new?editMode=create";
	});
	// 控件事件
	$("button.btn-search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = contextPath + '/spcQuery/search?'
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
		var url = contextPath + "/spcQuery/deleteAll/" + selectedIds;
		batchDeleteRows("search",url);
	});
	$("button.btn-export").click(function() {
		var selectedIds = getSelectedIds();
		if (selectedIds == "") {
			bootbox.alert("请选择要导出的记录");
			return false;
		}
		if(selectedIds.indexOf(",") != -1){
			bootbox.alert("只能选择一条记录");
			return false;
		}
		open(contextPath + "/spcQuery/export/" + selectedIds);
	});
	$("button.btn-import").click(function() {

		bootbox.confirm("导入会删除原有页面编号数据，确定要导入吗?", function(result) {
			if (result) { 
				showModalDialog(contextPath + "/spcQuery/showUpload/one.dialog",'','status:no;dialogRight:0;dialogTop:200px;dialogWidth:600px;resizable:yes;help:no');
			}
		});

	});
	$("button.btn-backup").click(function() {
		open(contextPath + "/spcQuery/backup");
	});
	$("button.btn-recover").click(function() {
		bootbox.confirm("恢复会删除原有页面编号数据，确定要恢复吗?", function(result) {
			if (result) { 
				showModalDialog(contextPath + "/spcQuery/showUpload/all.dialog",'','status:no;dialogRight:0;dialogTop:200px;dialogWidth:600px;resizable:yes;help:no');
			}
		});
	});
});
