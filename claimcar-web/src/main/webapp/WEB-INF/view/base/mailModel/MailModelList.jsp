<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<html>
<head>
<meta charset="utf-8">
<title>邮件模板查询</title>
</head>
<body>
<div class="page_wrap" id="fu">
    <!--查询条件 开始-->
    <div class="table_wrap">
        <div class="table_cont pd-10">
            <form id="forms" class="form-horizontal" role="form" method="post">
                <div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c">邮件模板类型</label>
							<div class="formControls col-2">
							    <span class="select-box">
								    <app:codeSelect type="select" codeType="MailModelType" name="PrpLMailModelVo.modelType" value="${prpLMailModel.ModelType}" />
							    </span>
							</div>
					        <label for="factoryType" class="form-label col-1 text-c">业务节点</label>
							<div class="formControls col-2">
							    <span class="select-box">
								    <app:codeSelect type="select" codeType="MailNode" name="PrpLMailModelVo.systemNode" value="${prpLMailModel.SystemNode}" />
							    </span>
							</div>
					    </div>
					    <div class="line mt-10 mb-10"></div>
					    <div class="row mb-3 cl">
					        <label class="form_label col-2">模板名称</label>
						    <div class="form_input col-2">
							    <input type="text" class="input-text" name="PrpLMailModelVo.modelName" />
						    </div>
						    <label class="form_label col-1">有无效标志</label>
						  	<div class="form_input col-2">
								<span class="select-box">
									<app:codeSelect codeType="validFlag" id="validFlag" name="PrpLMailModelVo.validFlag" type="select"/>
								</span>
							</div>
						</div>
						<div class="row cl">
							<span class="col-12 text-c">
							   <button class="btn btn-primary btn-outline btn-search" id="search" type="button" disabled>查询</button>
							   <button class="btn btn-primary btn-outline btn-noPolicy" onclick="add()" type="button">增加</button>
							</span>
						</div>
				</div>
			</form>
			
		</div>
	</div>
	
	<!--案查询条件 结束-->
	
	<!--标签页 开始-->
	    <div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabCon clearfix">
							<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
							<thead>
								<tr class="text-c">
									<th>序号</th>
									<th>模板名称</th>
									<th>归属机构</th>
									<th>模板类型</th>
									<th>业务节点</th>
									<th>编辑</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody class="text-c">
								<!-- 动态生成表格 -->
							</tbody>
						</table>
				</div>
			</div>
		</div>
	<!--标签页 结束-->
	
</div>

<!--
	<script src="/js/policyQuery/CheckList.js"></script>
	-->
	<script type="text/javascript">

	var columns = [
		       		{
		       			"data" :"id",
		       			"orderable" : true,
		       			"targets" : 0
		       		}, {
		       			"data" : "modelName"
		       		}, {
		       			"data" : "comCodeName"
		       		}, {
		       			"data" : "modelTypeName"
		       		}, {
		       			"data" : "systemNodeName"
		       		},{
		       			"data" : null
		       		},{
		       			"data" : null
		       		}
		       	  ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {
		    $('td:eq(0)', row).html("<a  onclick='showView("+data.id+");'>"+data.id+"</a>");
			$('td:eq(5)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick='modifMsgModel("+data.id+");'>修改</button>");
			if(data.validFlag=='1'){
				$('td:eq(6)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick='activOrCancel("+data.id+",0);'>注销</button> <button class='btn btn-primary btn-outline btn-search' onclick='deleteMsgModel("+data.id+");'>删除</button>");
			}else{
				$('td:eq(6)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick='activOrCancel("+data.id+",1," + data.comCode + "," + data.modelType +");'>激活</button> <button class='btn btn-primary btn-outline btn-search' onclick='deleteMsgModel("+data.id+");'>删除</button>");
			}
		
	}	
	
	function showView(mid){
		var index=2;
		index=layer.open({
			closeBtn:1, //显示关闭按钮
		    type: 2,
		    title: "邮件模板详细信息",
		    shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['1000px', '550px'],
		    content:"/claimcar/mailModel/mailModelEdit.do?mid="+mid+"&index="+index,
		});
	}
	
	function modifMsgModel(mid){
		 index=layer.open({
		    type: 2,
		    title: "邮件模板信息维护",
		    shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['1000px', '550px'],
		    content:"/claimcar/mailModel/mailModelEdit.do?mid="+mid+"",
		    end:function(){
		    	$("#search").click();
		    }
		});
	}
	
	function add(){
		index=layer.open({
		    type: 2,
		    title: "邮件模板信息维护",
		    shadeClose: true,
		    scrollbar: true,
		    skin: 'yourclass',
		    area: ['1000px', '550px'],
		    content:"/claimcar/mailModel/mailModelEdit.do",
		    end:function(){
		    	$("#search").click();
		    }
		});
	}
	
	function deleteMsgModel(mid){
		layer.confirm('您确认要删除该邮件模板吗？', {
		    btn: ['确认','取消'] //按钮
		}, function(){
		    $.ajax({
		    	url:"/claimcar/mailModel/deleteMailModel.do?mid="+mid+"",
		    	type:"post",
		    	data:{"mid":mid},
		    	dataType:"json",
		    	 //content:"/claimcar/msgModel/deleteMsgModel.do?mid="+mid+"",
		    	success:function(result){
		    		if(result.status=="200"){
		    			layer.msg("删除成功！");
		    			$("#search").click();
		    		}
		    	}
		    });
		});
	}
	
	function activOrCancel(id,validFlag,comCode,modelType){
		if(validFlag=='0'){
			var msg="您确定注销该邮件模板吗?";
		}else{
			var msg="您确定激活该邮件模板吗?";
		}
			
		layer.confirm(msg,{
			btn:['确认','取消']
		},function(){
			if(validFlag == 0){
				activeOrCancelModel(validFlag,id);
			} else{
				var params = {
						"modelType" :modelType,
					    "comCode":comCode, 
					    "id":id
			          };

				$.ajax({
						url : "/claimcar/mailModel/isVaildMailModel.ajax", // 后台校验
						type : 'post', // 数据发送方式
						dataType : 'json', // 接受数据格式
						data : params, // 要传递的数据
						async : false,
						success : function(result) {// 回调方法，可单独定义
							if(result.data == "1"){
								layer.alert("同一模板归属机构下只能存在有效的“车物” 和 “人伤”邮件模板各一条");
							} else {
								activeOrCancelModel(validFlag,id);	
							}
						}		
				});	
			} 
		});
	}
	
	function activeOrCancelModel(validFlag,id){
		 $.ajax({
		    	url:"/claimcar/mailModel/activOrCancel.do?id="+id+"&validFlag="+validFlag,
		    	type:"post",
		    	dataType:"json",
		    	success:function(result){
		    		if(result.status=="200"){
		    			layer.msg(result.data);
		    			$("#search").click();
		    		}
		    	}
		    });
	}	
	$(function(){
		$("#validFlag").find("option").each(function() {
			if ($(this).val() == "") {
				$(this).text("所有");
			}
		});
		
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = "/claimcar/mailModel/findMailModel.do";
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		$("#search").click(function(){
			ajaxList.postData=$("#forms").serializeJson();
			ajaxList.query();
		});
	});
	</script>
</body>
</html>

