var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0,
			"render" : function(data, type, row) {
				return '<input name="checkCode" type="checkbox" value="'
						+ row.userCode + '">';
			}
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 1
		}, {
			"data" : "userCode",
			"orderSequence" : [ "asc" ],
			"render" : function(data, type, row) { 
				return "<a  href='"
						+ contextPath
						+ "/user/view/"
						+ data
						+ "'>" + data  + "</a>";
			}
		}, {
			"data" : "userName"
		}, {
			"data" : "prpDcompany.comCode"
		}, {
			"data" : "prpDcompany.comCName"
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false
		} ];
function rowCallback(row, data, displayIndex, displayIndexFull) {
	var url = contextPath + "/user/delete/" + data.userCode;
	$('td:eq(1)', row).html(displayIndex + 1);
	$('td:eq(6)', row)
			.html(
					"<a class='glyphicon glyphicon-eye-open' href='"
					+ contextPath
					+ "/user/view/"
					+ data.userCode
					+ "'/>"+"&nbsp;&nbsp;<a class='glyphicon glyphicon-edit' href='"
							+ contextPath
							+ "/user/edit/"
							+ data.userCode
							+ "'/>"
							+ "&nbsp;<a class='glyphicon glyphicon-remove' href='#' onclick=\"deleteRows('resultDataTable','"
							 + url + "','" + (displayIndex + 1) 
							+ "');\"/>");

}

function setComValue( treeNode) {
	$("#comCode").val(treeNode.id);
	$("#comCName").val(treeNode.name);
}
//
// DataTables initialisation
//
$(function() {
	// 控件事件
	$("button.btn-create").click(function() {
		location.href = contextPath + "/user/edit/new/?editMode=create";
	});
	// 控件事件
	$("button.btn-search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = contextPath + '/user/search?'
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
		var url = contextPath + "/user/deleteAll/" + selectedIds;
		batchDeleteRows("search",url);
	});
	//zTree选择树放到了公用部分ZTreeCode.jspf,调用方法详解 ZTreeCode.js
	$("#comCName").bind(
			'click input propertychange', function() {
				showComCodeTree("comCName"," setComValue( treeNode)");
			});
	

});
	


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	