var columns = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0
		}, {
			"data" : "userCode",
			"orderSequence" : [ "asc" ],
			"render" : function(data, type, row) {
				return data;
			}
		}, {
			"data" : "saaRole.roleCName"
		}];
function rowCallback(row, data, displayIndex, displayIndexFull) {
	$('td:eq(0)', row).html(
			"<input name=\"radioCode\" type=\"radio\" value=\"" + data.id
					+ "\">&nbsp;&nbsp;" + (displayIndex + 1));
}
// init checkAll bind
$("#radioAll").click(function() {
	var radioFlag = this.checked;
	$("[name = radioCode]:radio").each(function() {
		this.checked = radioFlag;
	});

});
function getSelectedIds() {
	var selectIds = "";
	$("input[name='radioCode']:checked").each(function() {
		selectIds = selectIds + $(this).val() + ",";
	});
	if (selectIds != "") {
		selectIds.substr(0, selectIds.length - 1);
	}
	return selectIds;
}
//控件事件（返回）
$("button.btn-return").click(function() {
	history.go(-1);
});
//控件事件（业务权限配置）
$("button.btn-viewRoles").click(function() {
	//http://www.jsptz.com/simpleims/saauserconfig/prepareConfig.do?id={id}
	var selectedIds = getSelectedIds();
	if (selectedIds == "") {
		bootbox.alert("请选择要业务权限配置的记录");
		return false;
	}
	var userCode = selectedIds.split(",")[0];
	location.href = contextPath + "/saauserconfig/userPowerConfigList/" + userCode;
});	
// 进入页面异步查询
$(document).ready(function() {
	var userCode = $("#userCode").val();	
	var url = contextPath + "/saauserconfig/viewRolesByUserCode/?userCode=" + userCode;
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
        "columns": columns,
        "rowCallback" :rowCallback
    } );
} );