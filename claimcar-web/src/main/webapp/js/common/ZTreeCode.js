/*本JS文件仅在 common/ZTreeCode.jspf被引用*/
/**
 * 打开一个机构选择树
 * 例如：showComCodeTree("comName"," setComValue('xxx', treeNode)");
 * @param inputId 代码输入框的ID，用于在这个输入框下面显示树形结构
 * @param callbackMethod 回调方法，里面必须有个参数是treeNode,treeNode是选中树节点的json格式对象数据
 */
function showComCodeTree(inputId, callbackMethod) {
	var searchVal = $("#" + inputId).val();
	var url = contextPath + "/ztree/company?search=" + searchVal;
	showZTree(inputId, url, callbackMethod);
}

/**
 * 从字典表读取树结构
 * @param codeType 代码类型
 * @param inputId 输入框的ID，用于在这个输入框下面显示树形结构
 * @param callbackMethod 回调方法，里面必须有个参数是treeNode,treeNode是选中树节点的json格式对象数据
 */
function showDictTree(codeType,inputId, callbackMethod) {
	var searchVal = $("#" + inputId).val();
	var url = contextPath + "/ztree/codeDict?codeType="+codeType+"&search=" + searchVal;
	showZTree(inputId, url, callbackMethod);
}

//===========ZTree 私有方法部分===========
function showZTree(inputId, url, callbackMethod) {
	
	var setting = {
			data: {simpleData: {enable: true}},
			callback : {
			onClick : function(event, treeId, treeNode){
				eval(callbackMethod);
				$.fn.zTree.destroy("zTreePanelNode");
				$("#zTreePanel").css("display", "none");
			}
		}
	};
	
	$.ajax({
		type : "POST",
		url : url,
		data : "",
		async : false,
		success : function(obj) {
			if (obj != "") {
				zNodes = obj;
				var data_str ='<ul id="zTreePanelNode" class="ztree"></ul>';
				$("#zTreePanel").html(data_str);
			}else{
				var data_str = '<div class="message"  style="border:1px solid #CCCCCC;padding:5px;">没有数据!请重新输入或选择 </div>';
				$("#zTreePanel").html(data_str);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			flag = false;
			bootbox.alert(textStatus + errorThrown);
		}
	});

	$.fn.zTree.init($("#zTreePanelNode"), setting, zNodes);

	var cityObj = $("#" + inputId);
	var cityOffset = $("#" + inputId).offset();
	$("#zTreePanel").css({left : cityOffset.left + "px",top : cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
	$("body").bind("mousedown", hideZTree);

}

function hideZTree(event) {
	if (!(event.target.id == "zTreePanel" || $(event.target).parents("#zTreePanel").length > 0)) {
		$("#zTreePanel").fadeOut("fast");
		$("body").unbind("mousedown", hideZTree);
	}
}
