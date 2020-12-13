/**
 * Ajax模式List页面支持JS
 */
function AjaxList(selector) {
	this.selector = selector;
	this.targetUrl = null;
	this.formId="form";
	this.columns = {};
	this.rowCallback = {};
	this.datatable = {};

	// init checkAll bind
	$("#checkAll").click(function() {
		var checkFlag = this.checked;
		$("[name = checkCode]:checkbox").each(function() {
			this.checked = checkFlag;
		});
	});
	
	$.extend( $.fn.dataTable.defaults, {
		"bStateSave": false,//状态保存
		 "processing": false,
		"serverSide" : true,
		"searching": false,
		"ordering": false,
		"destroy" : true,
		"scrollX": true,
		"pagingType" : "full_numbers",
		"language" : {
			"lengthMenu" : "每页显示 _MENU_ 条记录",
			"zeroRecords" : "没有找到匹配的记录",
			"info" : "当前第  _PAGE_ 页 （共   _PAGES_ 页  _TOTAL_ 条记录）",
			"emptyTable" : "没有检索到数据",
			"infoEmpty" : "没有数据",
			"infoFiltered" : "(filtered from _MAX_ total records)",
			"processing" : "数据加载中...",
			"decimal" : ",",
			"thousands" : "",
			"paginate" : {
				"first" : "第一页",
				"previous" : "上一页",
				"next" : "下一页",
				"last" : "最后一页"
			}
		},
		"dom": 'rt<"bottom"iflp<"clear">>'
	} );
}
(function($){  
    $.fn.serializeJson=function(){  
        var serializeObj={};  
        var array=this.serializeArray();   
        $(array).each(function(){  
            if(serializeObj[this.name]){  
                if($.isArray(serializeObj[this.name])){  
                    serializeObj[this.name].push(this.value);  
                 }else{  
                     serializeObj[this.name]=[serializeObj[this.name],this.value];  
                 }  
             }else{  
                 serializeObj[this.name]=this.value;   
             }  
         });  
         return serializeObj;  
     };  
 })(jQuery);  

AjaxList.prototype.query = function() {
 //data: $("#"+innerFormId).serializeJson(),
	
	var innerDataTable = $(this.selector);
	var innerTargetUrl = this.targetUrl;
	var innerColumns = this.columns;
	var innerRowCallback = this.rowCallback; 
	var innerPaging = this.paging;
	var postData = this.postData;
	if(this.postData==null){//get 方式
		this.datatable = innerDataTable.dataTable({
			"paging" : innerPaging,
			"ajax" :  innerTargetUrl, 
			"ajax" :{url:innerTargetUrl, type: "GET",error : handleAjaxError}, 
			"columns" : innerColumns,
			"rowCallback" : innerRowCallback
		}); 
	}else{
		this.datatable = innerDataTable.dataTable({
			"paging" : innerPaging,
			"ajax" :{url:innerTargetUrl, type: "POST",data:postData,error : handleAjaxError}, 
			"columns" : innerColumns,
			"rowCallback" : innerRowCallback
		}); 
	}
	
};
function handleAjaxError( xhr, textStatus, error ) {
	if (xhr.status == '401' && error == 'Unauthorized' ) {
		layer.alert( '没有授权，不能访问' );
	}else {
		var jsonObj = xhr.responseText;
		try{
			var textMap=eval("("+jsonObj+")")
			layer.alert(textMap.msg);
		}catch(e) {
			//console.log(jsonObj);
		}
	}
}


function getSelectedIds() {
	var selectIds = "";
	$("input[name='checkCode']:checked").each(function() {
		selectIds = selectIds + $(this).val() + ",";
	});
	if (selectIds != "") {
		selectIds = selectIds.substr(0, selectIds.length - 1);
	}
	return selectIds;
}


