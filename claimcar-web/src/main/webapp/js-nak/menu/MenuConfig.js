var flag = false;
var zTreeNode = new Array();
var setting = {
		edit: {
			enable: true,
			isMove:true,
			showRemoveBtn:true,
			showRenameBtn:false,
			//removeTitle:"删除",
			//renameTitle:"重命名",
			editNameSelectAll: true,
			
		},
		view: {
			selectedMulti: false,
			addHoverDom: addHoverDom,
			removeHoverDom: removeHoverDom,
			fontCss:  setFontCss
		},
		async: {
			enable: true,
			url:getAsyncUrl,
			autoParam:["id","pId", "name", "level=menuLevel"],
			otherParam:{"otherParam":"zTreeAsyncTest"},
			dataFilter: filter
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		check: {
			//enable: true
		},
		callback: {
			//beforeDrag: beforeDrag,
			//beforeDrop: beforeDrop,
			beforeRemove: beforeRemove,
//			beforeClick: zTreeBeforeClick,
			onClick: zTreeOnClick,
			//onAsyncError: zTreeOnAsyncError,
			//onAsyncSuccess: zTreeOnAsyncSuccess,
			onDrag: zTreeOnDrag,
			onDrop: zTreeOnDrop,
			onRemove: onRemove
		}
	};

	function getAsyncUrl(treeId, treeNode) {
	    return contextPath + "/menu/show/" + $("#systemCode").val();
	};
	
//	var lastTreeNode;
	function addHoverDom(treeId, treeNode) {
//		if(lastTreeNode != null){
//			$("#addBtn_"+lastTreeNode.tId).unbind().remove();
//			$("#"+lastTreeNode.tId + "_remove").unbind().remove();
//		}
		lastTreeNode = treeNode;
		var sObj = $("#" + treeNode.tId + "_span");
		if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
		var addStr = "<img src='../img/menu/3.png' id='addBtn_" + treeNode.tId
			+ "' title='add' onfocus='this.blur();'/>";
		sObj.after(addStr);
		var btn = $("#addBtn_"+treeNode.tId);
		var newCount = 1;
		btn.bind("click", function(){
			$("#addBtn_"+treeNode.tId).unbind().remove();
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			$.ajax({
				type : "POST",
				url : contextPath + "/menu/add.do",
				data : {
					systemCode:$("#systemCode").val(),
					id : treeNode.id,
					name : treeNode.name,
					level : treeNode.level
				},
				async : false,
				success : function(obj) {
					if (obj.status == '200') {
						newTreeNode = obj.data;
						zTree.addNodes(treeNode, newTreeNode);
						if(!treeNode.isParent){
							treeNode.isParent = true;
							treeNode.icon = null;
							treeNode.iconOpen("../img/menu/1_open.png");
							treeNode.iconClose("../img/menu/1_close.png");
							zTree.updateNode(treeNode);
						}
					}
				},
			});
		});
	};
	
	function removeHoverDom(treeId, treeNode) {
		$("#addBtn_"+treeNode.tId).unbind().remove();
//		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	};
	
	function beforeRemove(treeId, treeNode) {
//		if(treeNode.isParent){
//			bootbox.confirm("你确定要删除该节点及其下面的子节点吗？",function(result){
//				
//			});
//		}
	};
	
	function onRemove(e, treeId, treeNode) {
		$.ajax({
			type : "POST",
			url : contextPath + "/menu/delete.do",
			data : {
				id : treeNode.id,
				systemCode : $("#systemCode").val()
			},
			async : false,
			success : function() {
				if (obj.status == '200') {
					console.log("调整成功");
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				bootbox.alert(textStatus + errorThrown);
			}
		});
	};
	
	function setFontCss(treeId, treeNode) {
		if(!treeNode.isParent)
			return {'color':'red','font-weight':'bold'};
		else
			return {'font-weight':'bold','font-size':'20px'};
	}
	
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
					url : contextPath + "/menu/update.do",
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
		$("#addBtn_"+treeNode.tId).unbind().remove();
		$("#"+treeNode.tId + "_remove").unbind().remove();
		$.ajax({
			type : "POST",
			url : contextPath + "/menu/edit/",
			data : {
				id:treeNode.id
			},
			async : false,
			success : function(smcMenu) {
				if (smcMenu.status == '200') {
					$("#id").val(smcMenu.data.id);
					$("#systemCode2").val(smcMenu.data.systemCode);
					$("#displayNo").val(smcMenu.data.id);
					$("#menuLevel").val(smcMenu.data.menuLevel);
					$("#version").val(smcMenu.data.version);
					$("#insertTimeForHis").val(smcMenu.data.insertTimeForHis);
					$("#upperId").val(smcMenu.data.upperId);
					$("#menuCName").val(smcMenu.data.menuCName);
					$("#menuEName").val(smcMenu.data.menuEName);
					$("#actionURL").val(smcMenu.data.actionURL);
					$("#target").val(smcMenu.data.target);
					$("#style").val(smcMenu.data.style);
					$("#validInd").val(smcMenu.data.validInd);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				flag = false;
				bootbox.alert(textStatus + errorThrown);
			}
		});
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
//		var treeNode = new Array();
		
		$("#systemCode").change(function(){
			$.ajax({
				type : "POST",
				url : contextPath + "/menu/setTreeNode.do",
				data : {
					systemCode:$("#systemCode").val()
				},
				async : false,
				success : function(obj) {
					if (obj.status == '200') {
						zTreeNode = obj.data;
					}
				},
			});
			$.fn.zTree.init($("#treeDemo"), setting,zTreeNode);
		});
		$("#systemCode").change();
		
		$("button.btn-reset").click(function() {
			bootbox.confirm("确定要重置吗?", function(result) {
				if (result) {
					$.ajax({
						type : "POST",
						url : contextPath + "/menu/resetDisplayNo/" + $("#systemCode").val(),
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
		
		$("button.btn-refresh").click(function() {
			var treeNode2 = new Array();
			$.ajax({
				type : "POST",
				url : contextPath + "/menu/setTreeNode.do",
				data : {
					systemCode:$("#systemCode").val()
				},
				async : false,
				success : function(obj) {
					if (obj.status == '200') {
						treeNode2 = obj.data;
						zTreeNode = treeNode2;
						bootbox.alert("刷新成功");
					}
				},
			});
			$.fn.zTree.init($("#treeDemo"), setting,treeNode2);
		})
		var ajaxEdit = new AjaxEdit("#editForm");
		ajaxEdit.targetUrl = contextPath + "/menu/save"; 
		ajaxEdit.bindForm();
	});