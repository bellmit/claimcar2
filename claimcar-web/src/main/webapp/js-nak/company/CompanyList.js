var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0,
			"render" : function(data, type, row) {
				return '<input name="checkCode" type="checkbox" value="'
						+ row.comCode + '">';
			}
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 1
		}, {
			"data" : "comCode",
			"orderSequence" : [ "asc" ],
			"render" : function(data, type, row) {
				return "<a  href='"
				+ contextPath
				+ "/company/view/"
				+ data
				+ "'>" + data + "</a>";
			}
		}, {
			"data" : "comCName"
		}, {
			"data" : "phoneNumber"
		}, {
			"data" : "addressCName"
		},  {
			"data" : null,
			"searchable" : false,
			"orderable" : false
		} ];
function rowCallback(row, data, displayIndex, displayIndexFull) {
	var url = contextPath + "/company/delete/" + data.comCode;
	$('td:eq(1)', row).html(displayIndex + 1);
	$('td:eq(6)', row)
			.html("<a class='glyphicon glyphicon-eye-open' href='"
					+ contextPath
					+ "/company/view/"
					+ data.comCode
					+ "'/>"+"&nbsp;&nbsp;<a class='glyphicon glyphicon-edit' href='"
							+ contextPath
							+ "/company/edit/"
							+ data.comCode
							+ "'/>"
							+ "&nbsp;<a class='glyphicon glyphicon-remove' href='#'  onclick=\"deleteRows('resultDataTable','"
							 + url + "','" + (displayIndex + 1) 
							+ "');\"/>");
}

function deleteRows(displayIndex, comCode) {
	bootbox.confirm("确定要删除吗?", function(result) {
		if (result) {
			$.ajax({
				type : "POST",
				url : contextPath + "/company/delete/" + comCode,
				data : "",
				async : false,
				success : function(obj) {
					if (obj.status == '200') {
						bootbox.alert("删除成功");
						$(".jqueryDataTable").find("tr").eq(displayIndex)
								.remove();
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					flag = false;
					bootbox.alert(textStatus + errorThrown);
				}
			});
		}
	});
}

function setComValue( treeNode) {
	$("#comCode").val(treeNode.id);
	$("#comCName").val(treeNode.name);
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
		location.href = contextPath + "/company/edit/new/?editMode=create";
	});
	// 控件事件
	$("button.btn-tree").click(function() {
		location.href = contextPath + "/company/config";
	});
	// 控件事件
	$("button.btn-search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = contextPath + '/company/search?'
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
		var url = contextPath + "/company/deleteAll/" + selectedIds;
		batchDeleteRows("search",url);
	});
	//zTree选择树放到了公用部分ZTreeCode.jspf,调用方法详解 ZTreeCode.js
	$("#comCName").bind(
			'click input propertychange', function() {
				showComCodeTree("comCName"," setComValue( treeNode)");
			});
	
});