<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>添加被保险人</title>
<!-- <script type="text/javascript" src="lib/html5.js"></script> -->
		<!-- <script type="text/javascript" src="lib/respond.min.js"></script> -->
		<!-- <script type="text/javascript" src="lib/PIE-2.0beta1/PIE_IE678.js"></script> -->
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
						<div class="row mb-3 cl">
							<label for="factoryType" class="form_label col-2">代理人名称</label>
					 		<div class="form_input col-3">
					 			<input id="agentName" type="text" class="input-text"   value="${agentName }" readonly="readonly"/>
					 		</div>
							<label for="factoryCode" class="form_label col-2">代理人代码</label>
							<div class="form_input col-3">
								<input id="agentCodeInsured" type="text" class="input-text" name="prpLInsuredFactoryVo.agentCode" value="${agentCode }" readonly="readonly"/>
								<input id="agentIdInsured" type="hidden" class="input-text"  value="${agentId }" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label for="factoryType" class="form_label col-2">被保险人名称</label>
					 		<div class="form_input col-3">
					 			<input  type="text" class="input-text"  name="prpLInsuredFactoryVo.insuredName" />
					 		</div>
							<label for="factoryCode" class="form_label col-2">被保险人代码</label>
							<div class="form_input col-3">
								<input  type="text" class="input-text"  name="prpLInsuredFactoryVo.insuredCode" />
							</div>
							<div class="form_input col-1">
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
									<th>被保险人名称</th>
									<th>被保险人代码</th>
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
		       		{"data" : "insuredName"},
		       		{"data" : "insuredCode"},
		       		{"data" : "insuredCode"}
		       	  ];
	function rowCallback(row, data, displayIndex, displayIndexFull) {
		/* var agentCode = $("#agentCodeInsured").val();
		var agentId = $("#agentIdInsured").val(); */
		$('td:eq(2)', row).html("<button class='btn btn-primary' onclick=ChooseInsuredItem('"+data.insuredName+"','"+data.insuredCode+"');>选择</button>");
	}	

	$(function() {
		$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon",
				"current", "click", "0");
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = "/claimcar/manager/findPrpcmain.do";
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		$("#search").click(function() {
            if(!checkNull($("input[name='prpLInsuredFactoryVo.insuredName']").val())  &&
				!checkNull($("input[name='prpLInsuredFactoryVo.insuredCode']").val())) {
                layer.alert("请至少录入被保险人名称或被保险人代码", {icon: 0});
                return false;
            }
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.query();
		});
	});
		
	function ChooseInsuredItem(insuredName,insuredCode) {
		var agentCode = $("#agentCodeInsured").val();
		var agentId = $("#agentIdInsured").val();
		
		var $tbody = $("#InsuredTbody", parent.document);
		var $insuredSize = $("#InsuredSize", parent.document);// 条数
		var insuredSize = parseInt($insuredSize.val(), 10);
		var params = {
			"insuredSize" : insuredSize,
			"agentCode"  :  agentCode,
			"agentId"    :  agentId,
			"insuredName"    :  insuredName,
			"insuredCode"    :  insuredCode,
		};
		var url = "/claimcar/manager/addInsuredFactoryItem.ajax";
		$.post(url, params, function(result) {
			$tbody.append(result);
			$insuredSize.val(insuredSize + 1);
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index);
			//$tbody.find('tr:last').find('select').select2();
			//initInsuredInfoSelect2("insuredId"+(insuredSize),agentCode,2);
		});
		/*var factoryid = $("input[name='repairFactoryVo.id']", parent.document).val();
		var factoryCode = $("input[name='repairFactoryVo.factoryCode']", parent.document).val();
		var factoryName = $("input[name='repairFactoryVo.factoryName']", parent.document).val();
		if(factoryid == null || factoryid == ''){
			layer.alert("请先维护修理厂信息并保存！");
			return;
		}
		var $tbody = $("#agentTbody",parent.document);
		var $agentSize = $("#agentSize", parent.document);// 条数
		var agentSize = parseInt($agentSize.val(), 10);
		var params = {
			"agentSize" : agentSize,
			"factoryid" : factoryid,
			"factoryCode" : factoryCode,
			"factoryName" : factoryName,
			"agentName" : agentName,
			"agentCode" : agentCode,
		};
		var url = "/claimcar/manager/addAgentFactoryItem.ajax";
		$.post(url, params, function(result) {
			$tbody.append(result);
			$agentSize.val(agentSize + 1);
			$tbody.find('tr:last').find('select').select2();
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index);
			//parent.layer.closeAll();
			
		});*/
		

	}
	</script>
	<script type="text/javascript" src="/claimcar/lib/Validform/5.3.2/Validform.js"></script>
	<script type="text/javascript" src="/claimcar/lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
	<script type="text/javascript" src="/claimcar/js/common/AjaxList.js"></script>
	
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
