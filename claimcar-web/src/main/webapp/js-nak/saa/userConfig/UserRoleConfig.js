var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0,
			"render" : function(data, type, row) {
				if(row.validInd == "1"){
					return '<input name="checkCode" type="checkbox" checked="checked" value="'
					+ data.gradeId + '">';
				}else{
					return '<input name="checkCode" type="checkbox" value="'
					+ data.gradeId + '">';
				}
			}
		}, {
			"data" : "gradeId",
			"orderSequence" : [ "asc" ]
		}, {
			"data" : "gradeCName"
		}];

// init checkAll bind
$("#checkAll").click(function() {
	var checkFlag = this.checked;
	$("[name = checkCode]:checkbox").each(function() {
		this.checked = checkFlag;
	});

});
//控件事件（返回）
$("button.btn-return").click(function() {
	history.go(-1);
});
// 控件事件(保存)
$("button.btn-updateUserRole").click(function() {
	var selectedIds = getSelectedIds();
	if (selectedIds == "") {
		bootbox.alert("请先选择一条或多条岗位");
		return false;
	}
	var checkCode = selectedIds.split(",");
	//Ajax表单操作相关
	var ajaxEdit = new AjaxEdit("#form");
	ajaxEdit.targetUrl = contextPath + "/saauserconfig/updateUserRole/"+checkCode; 
	ajaxEdit.afterSuccess=function(){
		history.go(-1); 
		//bootbox.alert("Success");
	}; 
	ajaxEdit.bindForm();
});

// 进入页面异步查询
$(document).ready(function() {
	var userCode = $("#userCode1").val();	
	var url = contextPath + "/saauserconfig/prepareUpdateUserRole/?userCode=" + userCode;
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
			"decimal" : ",",
			"thousands" : ".",
			"paginate" : {
				"first" : "首页",
				"previous" : "前页",
				"next" : "后页",
				"last" : "尾页"
			}
		},
        "columns": columns
    } );
} );