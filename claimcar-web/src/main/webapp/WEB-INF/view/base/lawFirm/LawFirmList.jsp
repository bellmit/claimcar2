<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>律师事务所查询</title>
</head>
<body>

<div class="page_wrap" >
    <!--查询条件 开始-->
    <div class="table_wrap">
        <div class="table_cont pd-10">
            <form id="forms" class="form-horizontal" role="form" method="post">
                <div class="formtable f_gray4">
                     <div class="row mb-3 cl">
					        <label class="form_label col-2">律师事务所代码</label>
						    <div class="form_input col-2">
							    <input type="text" class="input-text" name="prpdLawFirmVo.lawFirmCode" />
						    </div>
						    <label class="form_label col-4">律师事务所名称</label>
						  	<div class="form_input col-2">
								<input type="text" class="input-text" name="prpdLawFirmVo.lawFirmName" />
							</div>
					</div>
					<div class="line mt-10 mb-10"></div>
					<div class="row cl">
							<span class="col-12 text-c">
							   <button class="btn btn-primary btn-outline btn-search" id="search" type="button" disabled>查询</button>
							   <button class="btn btn-primary btn-outline btn-noPolicy" onclick="add()" type="button">增加</button>
							   <button class="btn btn-primary btn-outline " onclick="importFile()" type="button">导入</button>
							   <button class="btn btn-primary btn-outline " id="exportFile"  type="button">导出</button>
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
									<th>律师事务所代码</th>
									<th>律师事务所名称</th>
									<th>律师事务所地址</th>
									<th>律师事务所电话</th>
									<th>负责人</th>
									<th>联系人</th>
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

    <script type="text/javascript">

	var columns = [
		       		{
		       			"data" :"id",
		       			"orderable" : true,
		       			"targets" : 0
		       		}, {
		       			"data" : "lawFirmCode"
		       		}, {
		       			"data" : "lawFirmName"
		       		}, {
		       			"data" : "lawFirmAddress"
		       		}, {
		       			"data" : "mobileNo"
		       		},{
		       			"data" : "principal"
		       		},{
		       			"data" : "contacts"
		       		},{
		       			"data" : null
		       		}
		       	  ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {	
		//$('td:eq(0)', row).html("<a  onclick='showView("+data.id+");'>"+data.id+"</a>");
		$('td:eq(7)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick=modifLawFirm('"+data.id+"','0');>修改</button>");
		$('td:eq(0)', row).html("<a onclick=modifLawFirm('"+data.id+"','1');>"+data.id+"</a>");
    }
	
	function modifLawFirm(lid,sign){
	     index=layer.open({
		    type: 2,
		    title: "律师事务所维护",
		    shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['950px', '550px'],
		    content:"/claimcar/lawFirm/lawFirmEdit.do?lawFirmId="+lid+"&sign="+sign,
		    end:function(){
		    	$("#search").click();
		    }
		});
	}
	
	function add(){
		index=layer.open({
		    type: 2,
		    title: "律师事务所维护",
		    shadeClose: true,
		    scrollbar: true,
		    skin: 'yourclass',
		    area: ['950px', '550px'],
		    content:"/claimcar/lawFirm/lawFirmAdd.do",
		    end:function(){
		    	$("#search").click();
		    }
		});
	}
	
	$(function(){
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = "/claimcar/lawFirm/findLawFirm.do";
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		$("#search").click(function(){
// 			alert("23456");
			ajaxList.postData=$("#forms").serializeJson();
			ajaxList.query();
		});
	});
	
	function importFile(){
		index = layer.open({
			type : 2,
			title : "导入",
			shadeClose : true,
			scrollbar : false,
			skin : 'yourclass',
			area : [ '800px', '350px' ],
			content : [ "/claimcar/lawFirm/importInit.do", "no" ]
		}); 
	}
	
	$(function (){
		$('#exportFile').click(function(){

// 			$.ajax({
// 					url: "/claimcar/lawFirm/exportExcel.do",
// 					type:"post",
					
// 					dataType:"json",
// 					success:function(result){
// 						if (result.data == "4") {
// 							layer.msg("Excel已成功导出到D盘！");
// 					}else if(result.data == "5") {
// 							layer.msg("Excel导出失败！");
// 					}
// 				}
// 			 });  	
				
	    var relatedUrl = "/claimcar/lawFirm/exportExcel.do";
        window.open(relatedUrl);
		});
	});
	
	
	</script>
</body>
</html>