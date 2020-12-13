<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>可修品牌查询</title>
		<link href="/claimcar/h-ui/css/H-ui.min.css" rel="stylesheet" type="text/css" />
		<link href="/claimcar/h-ui/css/H-ui.admin.css" rel="stylesheet" type="text/css" />
		<link href="/claimcar/skin/default/skin.css" rel="stylesheet" type="text/css" id="skin" />
		<link href="/claimcar/lib/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
		
		<link  href="/claimcar/css/font.css" rel="stylesheet" />
		<link href="/claimcar/css/style.css" rel="stylesheet"  />
		<link  href="/claimcar/css/fee.css"  rel="stylesheet" type="text/css" />
		<link href="/claimcar/plugins/qtip/jquery.qtip.min.css" rel="stylesheet" type="text/css" />
		<link href="/claimcar/plugins/select2-3.4.4/select2.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="/claimcar/lib/jquery/1.9.1/jquery.min.js"></script>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont pd-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row mb-4 cl">
							<label for="factoryType" class="form_label col-2">品牌代码</label>
					 		<div class="form_input col-3">
					 			<input id="brandCode" type="text" class="input-text"  name="prpLRepairBrandVo.brandCode" />
					 		</div>
							<label for="factoryCode" class="form_label col-2">品牌名称</label>
							<div class="form_input col-3">
								<input id="brandName" type="text" class="input-text"  name="prpLRepairBrandVo.brandName" />
							</div>
							<%--<label for="factoryCode" class="form_label col-1">模糊查询</label>--%>
							<%--<div class="form_input col-2">--%>
								<%--<input id="agentCode" type="text" class="input-text"  name="prpLAgentFactoryVo.agentCode" />--%>
							<%--</div>--%>
							<div class="form-input col-1.5">
								<span class="col-offset-4 col-5">
									<button class="btn btn-primary btn-outline btn-search" id="search" type="button" disabled>查询</button>
								</span>
							</div>
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
									<th>品牌代码</th>
									<th>品牌名称</th>
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
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript" src="/claimcar/js/manage/RepairFactoryEdit.js"></script>

	<script type="text/javascript">
	var columns = [
		       		{"data" : "brandCode"},
		       		{"data" : "brandName"},
		       		{"data" : "brandCode"}
		       	  ];
	function rowCallback(row, data, displayIndex, displayIndexFull) {
		$('td:eq(2)', row).html("<button class='btn btn-primary' onclick=chooseBrand('"+data.brandName+"','"+data.brandCode+"');>选择</button>");
	}	


	$(function() {
		$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon",
				"current", "click", "0");
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = "/claimcar/manager/prpLRepairBrandFind.do";
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		$("#search").click(function() {
            if(!checkNull($("#brandCode").val())  && !checkNull($("#brandName").val())) {
                layer.alert("请至少录入一项查询条件", {icon: 0});
                return false;
            }
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.query();
		});
	});
		
	</script>
	<script type="text/javascript" src="/claimcar/lib/Validform/5.3.2/Validform.js"></script>
	<script type="text/javascript" src="/claimcar/lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
	<script type="text/javascript" src="/claimcar/js/common/AjaxList.js"></script>
	<script type="text/javascript" src="/claimcar/lib/layer/v2.1/layer.js"></script>

<!--
	<script type="text/javascript" src="/claimcar/lib/layer/1.9.3/layer.js"></script> -->
	<script type="text/javascript" src="/claimcar/lib/icheck/jquery.icheck.min.js"></script>
	<script type="text/javascript" src="/claimcar/h-ui/js/H-ui.js"></script> 
	<script type="text/javascript" src="/claimcar/h-ui/js/H-ui.admin.js"></script> 
	<script type="text/javascript" src="/claimcar/js/common/common.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/AjaxEdit.js"></script> 
	<script  type="text/javascript" src="/claimcar/lib/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="/claimcar/plugins/qtip/jquery.qtip.js"></script> 
	<script type="text/javascript" src="/claimcar/plugins/select2-3.4.4/select2.js"></script>
</body>
</html>
