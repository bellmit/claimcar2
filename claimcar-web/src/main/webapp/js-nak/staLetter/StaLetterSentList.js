var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0,
			"render" : function(data, type, row) {
				return '<input name="checkCode" type="checkbox" value="'
						+ row.id + '">';   //userCode改成msgCode
			}
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 1
		}, {
			"data" : "receiver",   //userCode改成msgCode
			//"orderSequence" : [ "asc" ],
			"render" : function(data, type, row) { 
				return "<a  href='"
						+ contextPath
						+ "/staLetter/sentView/"
						+ row.id
						+ "'>" + data + "</a>";
			}
		}, {
			"data" : "theme",   //userCode改成msgCode
			//"orderSequence" : [ "asc" ],
			"render" : function(data, type, row) { 
				return "<a  href='"
						+ contextPath
						+ "/staLetter/sentView/"
						+ row.id
						+ "'>" + data + "</a>";
			}
//			"render" : function(data, type, row) {
//				return data + ' (' + row.msgTheme + ')';  //userName改成msgTheme
//			}
		}, {
			"data" : "sendTime",   //userCode改成msgCode
//			//"orderSequence" : [ "asc" ],
//			"render" : function(data, type, row) {
//				return data + ' (' + row.msgTime + ')';  //userName改成msgTime
//			}
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false
		} ];
//function rowCallback(row, data, displayIndex, displayIndexFull) {
//	$('td:eq(1)', row).html(displayIndex + 1);
//	$('td:eq(5)', row)
//			.html(
//					"<a class='glyphicon glyphicon-eye-open' href='"
//							+ contextPath
//							+ "/staLetter/view/"      
//							+ data.id
//							+ "'/>"
//							+ "&nbsp;<a class='glyphicon glyphicon-remove' href='#' onclick=\"deleteRows('"
//							+ (displayIndex + 1) + "','" + data.id
//							+ "');\"/>");
////	                        + "&nbsp;<a class='glyphicon glyphicon-remove' href='#' onclick=\"deleteRows('resultDataTableSent','"
////	                        + (displayIndex + 1) + "','"  + data.id
////                        	+ "');\"/>");
//
//}

//user里的rowCallback的改动
function rowCallback(row, data, displayIndex, displayIndexFull) {
	var url = contextPath + "/staLetter/deleteFromSent/" + data.id;
	$('td:eq(1)', row).html("<a  href='"
			+ contextPath
			+ "/staLetter/receiveView/"
			+ data.id
			+ "'>" + (displayIndex + 1) + "</a>");
	$('td:eq(5)', row)
			.html(
					"<a class='glyphicon glyphicon-eye-open' href='"
							+ contextPath
							+ "/staLetter/sentView/"
							+ data.id
							+ "'/>"
							+ "&nbsp;<a class='glyphicon glyphicon-remove' href='#' onclick=\"deleteRows('resultDataTable','"
							 + url + "','" + (displayIndex + 1) 
							+ "');\"/>");

}

//function deleteRows(displayIndex, id) {
//	bootbox.confirm("确定要删除吗?", function(result) {
//		if (result) {
//			$.ajax({
//				type : "POST",
//				url : contextPath + "/staLetter/deleteFromSent/" + id,
//				data : "",
//				async : false,
//				success : function(obj) {
//					if (obj.status == '200') {
//						bootbox.alert("删除成功");
//						$(".jqueryDataTable").find("tr").eq(displayIndex)
//								.remove();
//					}
//				},
//				error : function(XMLHttpRequest, textStatus, errorThrown) {
//					flag = false;
//					bootbox.alert(textStatus + errorThrown);
//				}
//			});
//		}
//	});
//}

//
// DataTables initialisation
//
$("#checkAll").click(function() {
		$("input[type='checkbox']").attr("checked", this.checked);
	});

$("button.btn-deleteAll").click(function() {
    var selectedIds = getSelectedIds();
    if (selectedIds == "") {
	bootbox.alert("请选择要删除的记录");
	return false;
      }
    var url = contextPath + "/staLetter/deleteAllSent/" + selectedIds;
    batchDeleteRows("search",url);
    
    
});

//这里重写batchDeleteRows（），是要加上location.reload()，这样才能实现删除完刷新
function batchDeleteRows(buttonId, url) {
bootbox.confirm("确定要删除吗?", function(result) {
	if (result) {
		$.ajax({
			type : "POST",
			url : url,
			data : "",
			async : false,
			success : function(obj) {
				location.reload();
				if (obj.status == '200') {
					bootbox.alert("删除成功");
					$("#" + buttonId).click();
				}else if(obj.status == '500'){
					bootbox.alert(obj.statusText);
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

//// 控件事件  —————————————————————————————————————————写信————————btn-create
//$("button.btn-create").click(function() {
//		location.href = contextPath + "/staLetter/edit/new/?editMode=create";
//	});
//
//// 控件事件  —————————————————————————————————————————回复————————btn-reply
//$("button.btn-reply").click(function() {
//		var id = $('#id').val();
//		location.href = contextPath + "/staLetter/edit/"+id+"/?editMode=reply";
//	});
//
//// 控件事件 —————————————————————————————————————————转发————————btn-forward
//$("button.btn-forward").click(function() {
//		var id = $('#id').val();
//		location.href = contextPath + "/staLetter/edit/"+id+"/?editMode=forward";
//	});


//$(function() {
//	
//	
//	
//	$("button.btn-search").click(
//			
//			function() {
//				var ajaxList = new AjaxList("#resultDataTableSent");
//				ajaxList.targetUrl = contextPath + '/staLetter/search?'
//						+ $("#form").serialize();
//				ajaxList.columns = columns;
//				ajaxList.rowCallback = rowCallback;
//				ajaxList.query();
//			});
	
$(function() {   // 控件事件“发件箱查信”
		var url = contextPath + "/staLetter/sentSearch";
	    $('#resultDataTable').dataTable( {
	        "processing": true,
	        "serverSide": true,
	        "ajax": url,
	        "searching" : false,
			"language" : {
				"lengthMenu" : "每页显示 _MENU_ 条记录",
				"zeroRecords" : "没有检索到数据",
				"info" : "当前为第  _PAGE_ 页 （总共   _PAGES_ 页  _TOTAL_ 条记录）",
				"infoEmpty" : "没有数据",
				"infoFiltered" : "(filtered from _MAX_ total records)",
				"processing" : "数据加载中...",
				"decimal" : ".",
				"thousands" : ",",
				"paginate" : {
					"first" : "首页",
					"previous" : "前页",
					"next" : "后页",
					"last" : "尾页"
				}
			},
	        "columns": columns,
	        "rowCallback": rowCallback
	    } );
	});
	
	// 控件事件“已发送” ————————————————转移到StaLetterSentList.js中
//	$("button.btn-sent").click(
//			function() {
//				var ajaxList = new AjaxList("#resultDataTableSent");
//				ajaxList.targetUrl = contextPath + '/staLetter/sent?'
//						+ $("#form").serialize();
//				ajaxList.columns = columns;
//				ajaxList.rowCallback = rowCallback;
//				ajaxList.query();
//			});

//	// 控件事件
//	$("button.btn-import").click(
//			function() {
//				var ajaxList = new AjaxList("#resultDataTableSent");
//				ajaxList.targetUrl = contextPath + '/staLetter/import?'
//						+ $("#form").serialize();
//				ajaxList.columns = columns;
//				ajaxList.rowCallback = rowCallback;
//				ajaxList.query();
//			});