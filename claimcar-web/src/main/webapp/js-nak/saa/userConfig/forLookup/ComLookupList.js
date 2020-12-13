var columns1 = [
		{
			"data" : null,
			"searchable" : false,
			"orderable" : false,
			"targets" : 0
		}, {
			"data" : "comCode",
			"orderSequence" : [ "asc" ]
		}, {
			"data" : "comCName",
			"orderSequence" : [ "asc" ]
		}];
function rowCallback(row, data, displayIndex, displayIndexFull) {
	$('td:eq(0)', row).html(
			"<input name=\"comCheckCode\" type=\"checkbox\" value=\"" + data.comCode
					+ ',' +data.comCName + "\">&nbsp;&nbsp;" + (displayIndex + 1));
}
// init comCheckAll bind
$("#comCheckAll").click(function() {
	var checkFlag = this.checked;
	$("[name = comCheckCode]:checkbox").each(function() {
		this.checked = checkFlag;
	});
});
//机构范围复选框取值
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
$("button.btn-comLookup").click(function() {
	var selectedIds = getSelectedIds("comCheckCode");
	if (selectedIds == "") {
		bootbox.alert("请选择部门");
		return false;
	}
	var comList = selectedIds.split(",");
	//偶数
	var  evenNumber = "";
	//奇数
	var  oddNumber = "";
	for(var i=0;i<comList.length;i++){
		if(i % 2 ==0){
			if(i == (comList.length-2)){
				evenNumber += comList[i];
			}else{
				evenNumber += comList[i] + ",";
			}
		}else{
			if(i == (comList.length-1)){
				oddNumber += comList[i];
			}else{
				oddNumber += comList[i] + ",";
			}
		}
	}
	lookupValues("permitComCode",evenNumber);
	lookupValues("permitComName",oddNumber);
	//关闭对话框
	$("#closeDialog1").click();
});	
//
// DataTables initialisation
//
//模态对话框大小设置
$(function() {
	$("button.btn-search1").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable1");
				ajaxList.targetUrl = contextPath + '/saauserconfig/comLookupSearch?'
						+ $("#form1").serialize();
				ajaxList.columns = columns1;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			});
});	