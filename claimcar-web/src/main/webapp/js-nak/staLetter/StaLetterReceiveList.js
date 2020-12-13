var lastNumberIndex ="";
var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 1,
			"render" : function(data, type, row) {
				return '<input name="checkCode" type="checkbox" value="'
						+ row.id + '">';   //userCode改成row.id
			}
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 1
		}, {
			"data" : "sender",   
			"orderSequence" : [ "asc" ],
			"render" : function(data, type, row) { 
				if(data.read_Flag == "0"){
					return "<a style='font-weight: bolder;' href='"
					+ contextPath
					+ "/staLetter/receiveView/"
					+ row.id
					+ "'>" + data + "</a>";
				}else{
					return "<a  href='"
					+ contextPath
					+ "/staLetter/receiveView/"
					+ row.id
					+ "'>" + data + "</a>";
				}
				
			}
		}, {
			"data" : "theme",   
			"orderSequence" : [ "asc" ],
			"render" : function(data, type, row) { 
			return "<a  href='"
			+ contextPath
			+ "/staLetter/receiveView/"
			+ row.id
			+ "'>" + data + "</a>";
			}
//			"render" : function(data, type, row) {
//				return data + ' (' + row.msgTheme + ')';  //userName改成msgTheme
//			}
		}, {
			"data" : "receiveTime",   //userCode改成msgCode
			"orderSequence" : [ "asc" ]
//			"render" : function(data, type, row) {
//				return data + ' (' + row.msgTime + ')';  //userName改成msgTime
//			}
		}, {
			"data" : null,
			"searchable" : false,
			"orderable" : false
		} ];
function findLastIndex(){
	
	var senderName  = $("#"+lastNumberIndex).parent().next().children().html();
	var mailContent  = $("#"+lastNumberIndex).parent().next().next().children().html();
	//document.getElementById("t").innerHTML = senderName;
	$("#t").val(senderName);
	//document.getElementById("t2").innerHTML = mailContent;
	
}
function rowCallback(row, data, displayIndex, displayIndexFull) {
	lastNumberIndex = displayIndex;
	var url = contextPath + "/staLetter/deleteFromReceive/" + data.id;
	if(data.read_Flag == "0"){
		$('td:eq(1)', row).html("<a style='font-weight: bolder;' id='"+displayIndex+"' href='"
				+ contextPath
				+ "/staLetter/receiveView/"
				+ data.id
				+ "'>" + (displayIndex + 1) + "</a>");
	}else{
		$('td:eq(1)', row).html("<a id='"+displayIndex+"' href='"
				+ contextPath
				+ "/staLetter/receiveView/"
				+ data.id
				+ "'>" + (displayIndex + 1) + "</a>");
	}
	$('td:eq(5)', row)
			.html(
					"<a class='glyphicon glyphicon-eye-open' href='"
							+ contextPath
							+ "/staLetter/receiveView/"
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
//				url : contextPath + "/staLetter/delete/" + id,
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

// 控件事件  —————————————————————————————————————————写信————————btn-create
$("button.btn-create").click(function() {
		location.href = contextPath + "/staLetter/edit/new/?editMode=create";
	});

// 控件事件  —————————————————————————————————————————回复————————btn-reply
$("button.btn-reply").click(function() {
		var id = $('#id').val();
		location.href = contextPath + "/staLetter/edit/"+id+"/?editMode=reply";
	});

// 控件事件 —————————————————————————————————————————转发————————btn-forward
$("button.btn-forward").click(function() {
		var id = $('#id').val();
		location.href = contextPath + "/staLetter/edit/"+id+"/?editMode=forward";
	});

$("button.btn-deleteAll").click(function() {
	    var selectedIds = getSelectedIds();
	    if (selectedIds == "") {
		bootbox.alert("请选择要删除的记录");
		return false;
	      }
	    var url = contextPath + "/staLetter/deleteAll/" + selectedIds;
	    batchDeleteRows("search",url);
	    
	    
});

//这里重写batchDeleteRows（），是要加上location.reload()，这样才能实现删除完刷新
function batchDeleteRows(buttonId, url) {
	findLastIndex();
	bootbox.confirm("确定要删除吗?", function(result) {
		if (result) {
			$.ajax({
				type : "POST",
				url : url,
				data : "",
				async : false,
				success : function(obj) {
					location.reload();   //重要
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
	
$(function() {   // 控件事件“收信件”
	   // window.setInterval("checkMessage()",4000);
		var url = contextPath + "/staLetter/search";
	    $('#resultDataTable').dataTable( {
	        "processing": true,
	        "serverSide": true,
	        "ajax": url,
	        "searching" : false,
			"language" : {
				"lengthMenu" : "每页显示 _MENU_ 条记录",
				"zeroRecords" : "没有检索到数据",
				"info" : "当前为第  _PAGE_ 页 （总共   _PAGES_ 页  _TOTAL_ 条记录）",
				"infoFiltered" : "(filtered from _MAX_ total records)",
				"infoEmpty" : "没有数据",
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

//	$("button.btn-deleteAll").click(function() {
//		var staLetterCodes = getStaLetterCodes();
//		if (staLetterCodes == "") {
//			bootbox.alert("请选择要删除的人员");
//			return false;
//		}
//		bootbox.confirm("确定要删除吗?", function(result) {
//			if (result) {
//				$.ajax({
//					type : "POST",
//					url : contextPath + "/staLetter/deleteAll/" + msgCodes,
//					data : "",
//					async : false,
//					success : function(obj) {
//						if (obj.status == '200') {
//							bootbox.alert("删除成功");
//							$("button.btn-import").click();
//						}
//					},
//					error : function(XMLHttpRequest, textStatus, errorThrown) {
//						flag = false;
//						bootbox.alert(textStatus + errorThrown);
//					}
//				});
//			}
//		});
//	});
function checkMessage(){
	
	    var url = contextPath + "/staLetter/findNewMessage";
		$.post(url,
       	  function(data){
			if(data =='newmsg'){
				//bootbox.alert("您有一条新信息");
				$("#showMsg").show();
			}else{
				//$("#showMsg").hide("slow");
			}

       	  }
	    );  
	}  	