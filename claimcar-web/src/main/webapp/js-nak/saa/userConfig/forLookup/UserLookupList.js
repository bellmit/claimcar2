var columns2 = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0
		}, {
			"data" : "userCode",
			"orderSequence" : [ "asc" ]
		}, {
			"data" : "userName",
			"orderSequence" : [ "asc" ]
		}];
function rowCallback(row, data, displayIndex, displayIndexFull) {
	$('td:eq(0)', row).html(
			"<input name=\"userCheckCode\" type=\"checkbox\" value=\"" + data.userCode
					+ ',' + data.userName + "\">&nbsp;&nbsp;" + (displayIndex + 1));
}
//init userCheckAll bind
$("#userCheckAll").click(function() {
	var checkFlag = this.checked;
	$("[name = userCheckCode]:checkbox").each(function() {
		this.checked = checkFlag;
	});
});
//员工范围复选框取值
function getSelectedIds(checkCode) {
	var selectIds = "";
	$("input[name="+"'"+checkCode+"'"+"]:checked").each(function() {
		selectIds = selectIds + $(this).val() + ",";
	});
	if (selectIds != "") {
		selectIds = selectIds.substr(0, selectIds.length - 1);
	}
	return selectIds;
}
//机构选择带回
$("button.btn-userLookup").click(function() {
	var selectedIds = getSelectedIds("userCheckCode");
	if (selectedIds == "") {
		bootbox.alert("请选择人员");
		return false;
	}
	var userList = selectedIds.split(",");
	//偶数
	var  evenNumber = "";
	//奇数
	var  oddNumber = "";
	for(var i=0;i<userList.length;i++){
		if(i % 2 ==0){
			if(i == (userList.length-2)){
				evenNumber += userList[i];
			}else{
				evenNumber += userList[i] + ",";
			}
		}else{
			if(i == (userList.length-1)){
				oddNumber += userList[i];
			}else{
				oddNumber += userList[i] + ",";
			}
		}
	}
	lookupValues("permitUserCode",evenNumber);
	lookupValues("permitUserName",oddNumber);
	//关闭对话框
	$("#closeDialog2").click();
});	
//
// DataTables initialisation
//
//模态对话框大小设置
$(function() {
	$("button.btn-search2").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable2");
				ajaxList.targetUrl = contextPath + '/saauserconfig/userLookupSearch?'
						+ $("#form2").serialize();
				ajaxList.columns = columns2;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			});
});	