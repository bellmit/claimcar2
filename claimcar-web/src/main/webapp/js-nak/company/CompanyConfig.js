var flag = false;
var setting = {
		edit: {
			enable: true,
			isMove:true,
			showRemoveBtn:false,
			showRenameBtn:false,
			//removeTitle:"删除",
			//renameTitle:"重命名",
			editNameSelectAll: true,
			
		},
		view: {
			selectedMulti: false
		},
		async: {
			enable: true,
			url:getAsyncUrl,
			//autoParam:["id","pId", "name", "level=companyLevel"],
			autoParam:["id","pId", "name"],
			otherParam:{"otherParam":"zTreeAsyncTest"},
			dataFilter: filter
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		check: {
			enable: false
		},
		callback: {
			//beforeDrag: beforeDrag,
			//beforeDrop: beforeDrop,
			onClick: zTreeOnClick,
			//onAsyncError: zTreeOnAsyncError,
			//onAsyncSuccess: zTreeOnAsyncSuccess,
			onDrag: zTreeOnDrag,
			onDrop: zTreeOnDrop
		}
	};
	function getAsyncUrl(treeId, treeNode) {
    return contextPath + "/company/show" ;
	//	return contextPath + "/company/show/" + "92";
	};

	function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType,isCopy) {
		if(flag){
			var data = "";
			for(var i = 0; i <treeNodes.length; i++){
				if(targetNode != null){
					data = "id=" + treeNodes[i].id + "&pId=" + (treeNodes[i].pId == null ? 0 : treeNodes[i].pId) + "&targetId=" + targetNode.id 
								+ "&targetPId=" + (targetNode.pId == null ? treeNodes[i].id : targetNode.pId) + "&moveType=" + moveType;
				}else{
					data = "id=" + treeNodes[i].id + "&pId=" + treeNodes[i].id + "&moveType=noTarget";
				}
				console.log(data);
				$.ajax({
					type : "POST",
					url : contextPath + "/company/update.do",
					data : data,
					async : false,
					success : function(obj) {
						if (obj.status == '200') {
							console.log("调整成功");
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						flag = false;
						bootbox.alert(textStatus + errorThrown);
					}
				});
			}
			flag = false;
		}
	};
	function zTreeOnDrag(event, treeId, treeNodes) {
		flag = true;
	};
	function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	    alert(msg);
	};
	function zTreeOnAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
	    alert(XMLHttpRequest);
	};
	
	function zTreeOnClick(event, treeId, treeNode) {
		if(treeNode.actionUrl != ""){
			var $modal = $('#ajax-modal');
		    //open(treeNode.actionUrl);
//			frames["info"].location.href = contextPath + "/menu/edit/" + treeNode.id+".iframe";
			$('body').modalmanager('loading');
			$modal.load(contextPath + "/company/edit/" + treeNode.id + ".dialog", '', function(){
			      $modal.modal();
			    });
		}
	};
	
	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
		}
		return childNodes;
	}

	function beforeDrag(treeId, treeNodes) {
		for (var i=0,l=treeNodes.length; i<l; i++) {
			if (treeNodes[i].drag === false) {
				return false;
			}
		}
		return true;
	}
	function beforeDrop(treeId, treeNodes, targetNode, moveType) {
		return targetNode ? targetNode.drop !== false : true;
	}
	$(document).ready(function(){
		$.fn.zTree.init($("#treeDemo"), setting);
		
		$("button.btn-reset").click(function() {
			bootbox.confirm("确定要重置吗?", function(result) {
				if (result) {
					$.ajax({
						type : "POST",
						url : contextPath + "/company/resetDisplayNo/" + $("#systemCode").val(),
						data : "",
						async : false,
						success : function(obj) {
							if (obj.status == '200') {
								bootbox.alert("重置成功");
								$("#systemCode").change();
							}
						},
						error : function(XMLHttpRequest, textStatus, errorThrown) {
							flag = false;
							bootbox.alert(textStatus + errorThrown);
						}
					});
				}
			});
		});
	});