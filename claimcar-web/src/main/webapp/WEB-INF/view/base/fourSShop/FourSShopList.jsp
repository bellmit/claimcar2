<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>4S店信息查询</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont pd-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label for="factoryType" class="form_label col-2">归属机构</label>
					 		<div class="form_input col-2">
					 			<span class="select-box">
									<app:codeSelect codeType="ComCode" name="prpLFourSShopInfoVo.ComCode"
											 type="select" /> 
								</span>
					 		</div>
							<label class="form_label col-2">出单合作店名称：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text" name="prpLFourSShopInfoVo.fourSShopName" />
							</div>
							<label class="form_label col-2">修理厂名称：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text" name="prpLFourSShopInfoVo.factoryName" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form_label col-2">出单合作店等级：</label>
							<div class="form_input col-2">
								<span class="select-box"> 
									<app:codeSelect codeType="FourSLevel" name="prpLFourSShopInfoVo.foursLevel"
											 type="select"/>
								</span>
							</div>
							<label class="form_label col-2">送修支持：</label>
							<div class="form_input col-3">
								<app:codeSelect codeType="YN10" type="radio" name="prpLFourSShopInfoVo.sendRepair" value="${prpLFourSShopInfo.sendRepair}"/>
							</div>
							<label class="form_label col-1">有无效标志：</label>
							<div class="form_input col-2">
								<span class="select-box">
									<select class=" select must" style="vertical-align:middle" 
										name="prpLFourSShopInfoVo.validStatus" >
										<option value="1" selected>有效</option>
										<option value="2">无效</option>
									</select>
								</span>
							</div>
						</div>
						<div class="line mt-10 mb-10"></div>
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
									<th>出单合作店</th>
									<th>合作起始时间</th>
									<th>出单合作店等级</th>
									<th>所属省市区</th>
									<th>修理厂名称</th>
									<th>修理厂地址</th>
									<th>送修支持</th>
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
		       			"data" : "fourSShopName"
		       		}, {
		       			"data" : "startTime"
		       		}, {
		       			"data" : "foursLevelName"
		       		}, {
		       			"data" : "areaCodeName"
		       		},{
		       			"data" : "factoryName"
		       		},{
		       			"data" : "pushRepairAddress"
		       		}
		       		,{
		       			"data" : "sendRepairName"
		       		}
		       		,{
		       			"data" : null
		       		}
		       	  ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {		
			$('td:eq(0)', row).html("<a  onclick='showView("+data.id+");'>"+data.id+"</a>");
			$('td:eq(8)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick='modifFourS("+data.id+");'>修改</button>"
									+"<br><button class='btn btn-primary btn-outline btn-search mt-5'  onclick='deleteFourS("+data.id+");'>删除</button>");
		
	}	
	
	
	function showView(fid){
		index=layer.open({
		    type: 2,
		    title: "4S店详细信息",
		    shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['1000px', '550px'],
		    content:"/claimcar/fourSShop/fourSShopEdit.do?fourSId="+fid+"&flag=s",
		});
	}
	
	function modifFourS(fid){
		index=layer.open({
		    type: 2,
		    title: "4S店信息维护",
		    shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['1000px', '550px'],
		    content:"/claimcar/fourSShop/fourSShopEdit.do?fourSId="+fid+"",
		    end:function(){
		    	$("#search").click();
		    }
		});
	}
	
	function add(){
		index=layer.open({
		    type: 2,
		    title: "4S店信息维护",
		    shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['1000px', '550px'],
		    content:"/claimcar/fourSShop/fourSShopEdit.do",
		    end:function(){
		    	$("#search").click();
		    }
		});
	}
	
	function deleteFourS(fid){
		layer.confirm('您确认要删除该4S店信息吗？', {
		    btn: ['确认','取消'] //按钮
		}, function(){
		    $.ajax({
		    	url:"/claimcar/fourSShop/fourSShopDelete.do",
		    	type:"post",
		    	data:{"fid":fid},
		    	dataType:"json",
		    	success:function(result){
		    		if(result.status=="200"){
		    			layer.msg("删除成功！");
		    			$("#search").click();
		    		}
		    	}
		    });
		});
	}
	
	
		
	$(function(){
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = "/claimcar/fourSShop/fourSShopFind.do";
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		$("#search").click(function(){
			ajaxList.postData=$("#form").serializeJson();
			ajaxList.query();
		});
	});
	
	
	
	
	</script>
</body>
</html>
