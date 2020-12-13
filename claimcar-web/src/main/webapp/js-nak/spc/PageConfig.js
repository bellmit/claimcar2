//目标配置树
	var setting = {
			edit: {
				enable: true,
				showRemoveBtn: true,
				showRenameBtn: false,
				removeTitle: "删除",
				drag: {
					isCopy: false,
					isMove: true
				}
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeDrag: beforeDrag,
				beforeDrop: beforeDrop,
				beforeRemove: zTreeBeforeRemove,
				onClick: zTreeOnClick,
				onDrop: zTreeOnDrop,
				onRemove: zTreeOnRemove
			}
		};
	function beforeDrag(treeId, treeNodes) {
		for (var i=0,l=treeNodes.length; i<l; i++) {
			if (treeNodes[i].drag === false) {
				return false;
			}
		}
		return true;
	}
	function beforeDrop(treeId, treeNodes, targetNode, moveType) {
		if((targetNode.nodeType == "11" || targetNode.nodeType == "21") && moveType != "inner"){
			return false;
		}else if(moveType == "inner" && targetNode.nodeType != "11" && targetNode.nodeType != "21"){
			return false;
		}
		return targetNode ? targetNode.drop !== false : true;
	}
	function zTreeBeforeRemove(treeId, treeNode) {
		if(treeNode.nodeType == '111' || treeNode.nodeType == '211'){
			return true;
		}
		bootbox.alert("只有查询条件和columns的子节点可以删除");
		return false;
	}
	function zTreeOnClick(event, treeId, treeNode) {
		var $modal = $('#ajax-modal');
		$modal.removeClass();
		if(treeNode.nodeType == '20' || treeNode.nodeType == '22' || treeNode.nodeType == '3'){
			$modal.addClass("modal fade col-lg-8 col-lg-offset-2");
		}else{
			$modal.addClass("modal fade col-lg-4 col-lg-offset-4");
			
		}
		$('body').modalmanager('loading');
		$modal.load(contextPath + "/spc/edit/" + treeNode.id + ".dialog", '', function(){
		      $modal.modal();
		    });
	}
	
	function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType) {
		var data = "name=" + treeNodes[0].name + "&id=" + treeNodes[0].id + "&targetId=" + targetNode.id + "&targetPId=" 
					+ targetNode.pId + "&pageCode=" + targetNode.pageCode + "&moveType=" + moveType;
		console.log(data);
		$.ajax({
			type : "POST",
			url : contextPath + "/spc/updateDisplayNo",
			data : data,
			async : false,
			success : function(obj) {
				if (obj  != '') {
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				bootbox.alert(textStatus + errorThrown);
			}
		});
		autoPreview();
	}
	function zTreeOnRemove(event, treeId, treeNode) {
		$.ajax({
			type : "POST",
			url : contextPath + "/spc/deleteNode/" + treeNode.id,
			data : "",
			async : false,
			success : function(obj) {
				if (obj  != '') {
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				bootbox.alert(textStatus + errorThrown);
			}
		});
		autoPreview();
	}

	//模板树
	var settingModel = {
			edit: {
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: false,
				drag: {
					isCopy: true,
					isMove: false
				}
			},
			view: {
				selectedMulti: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeDrag: beforeDragModel,
				beforeDrop: beforeDropModel,
				onDrop: zTreeOnDropModel
			}
		};

		function beforeDragModel(treeId, treeNodes) {
			for (var i=0,l=treeNodes.length; i<l; i++) {
				if (treeNodes[i].drag === false) {
					return false;
				}
			}
			return true;
		}
		function beforeDropModel(treeId, treeNodes, targetNode, moveType) {
			if((targetNode.nodeType == "11" || targetNode.nodeType == "21") && moveType != "inner"){
				return false;
			}else if(moveType == "inner" && targetNode.nodeType != "11" && targetNode.nodeType != "21"){
				return false;
			}
			return targetNode ? targetNode.drop !== false : true;
		}

		function zTreeOnDropModel(event, treeId, treeNodes, targetNode, moveType) {
			
			if(moveType != "inner"){//直接拖拽到查询条件的子节点前后，将父节点改为查询条件节点。
				targetNode = targetNode.getParentNode();
			}
			var data = "name=" + treeNodes[0].name + "&targetId=" + targetNode.id + "&targetPId=" 
						+ targetNode.pId + "&pageCode=" + targetNode.pageCode 
						+ "&targetNodeType=" + targetNode.nodeType;
			console.log(data + "!!!  " + moveType);
			$.ajax({
				type : "POST",
				url : contextPath + "/spc/addNode",
				data : data,
				async : false,
				success : function(obj) {
					if (obj  != '') {
						treeNodes[0].id = obj.id;
						treeNodes[0].name = obj.name;
						treeNodes[0].nodeType = obj.nodeType;
						treeNodes[0].pageCode = obj.pageCode;
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					bootbox.alert(textStatus + errorThrown);
				}
			});
			autoPreview();
		}
		
		function autoPreview(){
			if($("#autoPreview").is(":checked")){
				$(".btn-preview").click();
			}
		}
		
		$(document).ready(function(){
			$(".btn-preview").click(function(){
				$.ajax({
					type : "POST",
					url : contextPath + "/spc/preview/" + $("#pageCode").val(),
					data : "",
					async : false,
					success : function(obj) {
						if (obj.status= "200") {
							$("#preview").html(obj.statusText);
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						bootbox.alert(textStatus + errorThrown);
					}
				});
			});
			
			$(".initTree").change(function(){
				var pageCode = $("#pageCode").val();
				var description = $("#description").val();
				if(description != '' && pageCode != '' ){
					var zNodes = "";
					$.ajax({
						type : "POST",
						url : contextPath + "/spc/pageConfig",
						data : "pageCode=" + pageCode + "&description=" + description,
						async : false,
						success : function(obj) {
							if (obj!= "") {
								zNodes = obj;
							}
						},
						error : function(XMLHttpRequest, textStatus, errorThrown) {
							bootbox.alert(textStatus + errorThrown);
						}
					});
					$.fn.zTree.init($("#tree"), setting, zNodes);
					autoPreview();
				}
			});
			if($("#pageCode").val() != '' && $("#description").val() != ''){
				$(".initTree").change();
				
			}
			$("#poName").change(function(){
				var zNodesModel = "";
				$.ajax({
					type : "POST",
					url : contextPath + "/spc/changePO/" + $("#poName").val() + "/",
					data : "",
					async : false,
					success : function(obj) {
					if (obj!= "") {
						zNodesModel = obj;
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					bootbox.alert(textStatus + errorThrown);
				}
				});
				$.fn.zTree.init($("#treeModel"), settingModel,zNodesModel);
			});
			if($("#poName").val() != ''){
				$("#poName").change();
			}
			
			$(".btn-saveAsFile").click(function(){
				$.ajax({
					type : "POST",
					url : contextPath + "/spc/saveAsFile/" + $("#pageCode").val(),
					data : "",
					async : false,
					success : function(obj) {
					if (obj!= "") {
						bootbox.alert("文件路径：" + obj.statusText);
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					bootbox.alert(textStatus + errorThrown);
				}
				});
			});
		});