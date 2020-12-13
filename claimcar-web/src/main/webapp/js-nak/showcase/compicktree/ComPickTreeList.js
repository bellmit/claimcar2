var setting = {
		edit: {
			enable: true,
			isMove:true,
			showRemoveBtn:false,
			showRenameBtn:false,
			editNameSelectAll: true,
			
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
			onClick: zTreeOnClick
		}
	};	
	function zTreeOnClick(event, treeId, treeNode) {
		$("#comCode").val(treeNode.id);
		$("#comCName").val(treeNode.name);
		$.fn.zTree.destroy("treeDemo");
		$("#comTree").css("display","none");
	};	
	$(function() {
        //comCName是机构名称输入框的id
	$("#comCName").bind(
			'click input propertychange', function() {
			showMenu();
			var zNodes = "";
			if($("#comCName").val() == ""){			
				$.ajax({
					type : "POST",
					url : contextPath + "/company/showTree",
					data : "",
					async : false,
					success : function(obj) {
						if (obj!= "") {
							zNodes = obj;
							var data_str ='<ul id="treeDemo" class="ztree"></ul>';
							$("#comTree").html(data_str);
						}
					}
					
				});
			}else{
				$.ajax({
					type : "POST",
					url : contextPath + "/company/showInputTree/" + $("#comCName").val(),
					data : "",
					async : false,
					success : function(obj) {
						if (obj!= "") {			
							zNodes = obj;
							var data_str ='<ul id="treeDemo" class="ztree"></ul>';
							$("#comTree").html(data_str);
						}else{
							var message='没有数据!请重新输入或选择！';
							var data_str = '<div class="message"  style="color:red;border:1px solid #CCCCCC;padding:5px;border-radius: 5px;"> </div>';
							$("#comTree").html(data_str);
							$(".message").text(message);
							
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
							flag = false;
							bootbox.alert(textStatus + errorThrown);
				}
					
				});
			}
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			
			});
	
		function showMenu(){
			var cityObj = $("#comCName");
			var cityOffset = $("#comCName").offset();
			$("#comTree").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
			$("body").bind("mousedown", onBodyDown);
		}
	
		function hideMenu() {
			$("#comTree").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
		function onBodyDown(event) {
			if (!(event.target.id == "comTree" || $(event.target).parents("#comTree").length>0)) {
				hideMenu();
			}
		}
	
	
});