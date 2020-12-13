/**
 * 编辑页面功能代码树形结构参数设置
 */
var setting = {
		check:{
			enable: true
		},
 		view: {
			selectedMulti: true,
			showIcon: false,
			fontCss : {color:"#428bca"}
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};
//增加行全局参数
var tr = "";
//增加权限因子对应的行数全局参数
var factorIndex = "";
//机构范围弹出对话框
function comLookupDialog(){
		var $modal = $('#ajax-comModal');
		$('body').modalmanager('loading');
		$modal.load(contextPath + "/saauserconfig/comLookupList.dialog", '', function(){
		      $modal.modal();
		    });
};
//员工范围弹出对话框
function userLookupDialog(){
		var $modal = $('#ajax-userModal');
		$('body').modalmanager('loading');
		$modal.load(contextPath + "/saauserconfig/userLookupList.dialog", '', function(){
		      $modal.modal();
		    });
};
//数据范围弹出对话框
function factorLookupDialog(currentFactorIndex){
	    factorIndex = currentFactorIndex;
		var $modal = $('#ajax-factorModal');
		$('body').modalmanager('loading');
		$modal.load(contextPath + "/saauserconfig/factorLookupList.dialog", '', function(){
		      $modal.modal();
		    });
};
//机构范围和员工范围查找带回(回填值)
function lookupValues(id,val){
	$("#"+id).html(val);
}
//生成增加行	
function createRow(index){
	var tr1 = '<tr><td><input id="serialNo" name="serialNo" class="form-control" readonly="readonly" type="text" value="'+(index+1)+'" /></td>' 
		+ '<td><input id="factorList['+index+'].factorCode" name="factorList['+index+'].factorCode" class="form-control" type="text" required="required" value="" style="width:160px;float:left;"/><a class="glyphicon glyphicon-search"  style="float:right;" href="javascript:void(0)" onclick="factorLookupDialog('+index+');" title="查找带回"></a></td>' 
		+ '<td><input id="factorList['+index+'].factorDesc" name="factorList['+index+'].factorDesc" class="form-control" type="text" required="required" value=""/></td>' 
		+ '<td><input id="factorList['+index+'].factorId" name="factorList['+index+'].factorId" class="form-control" type="text" readonly="readonly" value=""/></td>' 
		+ '<td><input id="factorList['+index+'].value" name="factorList['+index+'].value" class="form-control" type="text" required="required" value=""/></td>' 
		+ '<td><input id="factorList['+index+'].dept" name="factorList['+index+'].dept" class="form-control" type="text" required="required" value=""/></td>' 
		+ '<td><a class="glyphicon glyphicon-remove form-control" title="删除" href="javascript:void(0)" onclick="deleteAddRows('+"'resultDataTable'"+','+(index+1)+');"/></td></tr>';
	tr = tr1;
}
//删除增加的行
function deleteAddRows(tabId,displayIndex) {
	bootbox.confirm("确定要删除吗?", function(result) {
		if (result) {
			bootbox.alert("删除成功");
			$("#" + tabId).find("tr").eq(displayIndex).remove();
		}
	});
}
$(function() {
	//编辑页面加载所有险类险种
	$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	//增加行
	 $("button.btn-first").click(function(){
			 var rowSize = $("#resultDataTable tr").length;//增加之前读取的行数
			 var lines = $("#addRows").val();//用户输入的行数
			//rowSize大于等于2且为空增加1行（下标为rowSize-2），即默认为增加一行
			 if(lines == ""){
				 createRow(rowSize-2);
				 $(".last.first").before(tr);
			 }
			//rowSize大于等于2且为1增加1行（下标为rowSize-2）
			 if(lines == "1"){
				 createRow(rowSize-2);
				 $(".last.first").before(tr);
			 }
			//读取增加的行数（下标为rowSize-2+i）(如输入2，则i取值范围为i={0,1})
			 if(lines != "" && lines != "1"){
				var i= parseInt(lines);
	    		for(var j=0;j<i;j++){
	    			createRow(rowSize-2+j);
	    			$(".last.first").before(tr);
	    		}
			 }
	    });
	//控件事件（返回）
	$("button.btn-return").click(function() {
		history.go(-1);
	});
	// 控件事件(保存)
	//1、页面校验
	//2、校验规则
	/**var rules = {
		roleCName : "required",
		comCode : "required",
	};*/
	//3、校验提示
	//4.Ajax表单操作相关
	var ajaxEdit = new AjaxEdit("#editForm");
	//ajaxEdit.rules = rules;
	ajaxEdit.targetUrl = contextPath + "/saauserconfig/userConfig"; 
	ajaxEdit.beforeSubmit=function(){
		//提交之前查找所有选中的复选框
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var nodes = treeObj.getCheckedNodes(true);
		//var riskCodesArray = new Array(nodes.length);//任务ID数组
		var riskCodesStr = "";//任务ID字符串
	    if(nodes != null || nodes.length != 0){
	    	for(var i=0; i<nodes.length;i++){
	    		if(!nodes[i].isParent){//只需要提交险种代码（riskCode）
		    		if(i==(nodes.length-1)){
		    			riskCodesStr += nodes[i].id;
		    		}else{
		    			riskCodesStr += nodes[i].id + ",";
		    		}
	    		}
	    	}
        }
	    $("#nodes").val(riskCodesStr);
	}; 
	ajaxEdit.afterSuccess=function(){
		history.go(-1); 
		//bootbox.alert("Success");
	}; 
//		ajaxEdit.afterFailure=function(){
//			alert("Failure");
//		}; 
	//绑定表单
	ajaxEdit.bindForm();
});