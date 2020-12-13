<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>详细信息</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<!--案查询条件 结束-->
			<form id="form" name="form" class="form-horizontal" role="form">
				<input type="hidden" name="handle" value="1"> <input
					type="hidden" name="registNo" value="${registNo}" /> <input
					type="hidden" name="policyNoCI" value="${policyNo_CI}" /> <input
					type="hidden" name="policyNoBI" value="${policyNo_BI}" />
			</form>
			<div class="row mb-3 cl">
				<label class="form-label col-1 text-c ">报案号</label>
				<div class="formControls col-3">
					<u>${registNo}</u>
				</div>
				<label class="form-label col-1 text-c">保单号</label>
				<div class="formControls col-6">
					<c:if test="${!empty policyNo_CI}">
						交强：${policyNo_CI}
					</c:if>&nbsp;&nbsp;&nbsp;
					<c:if test="${!empty policyNo_BI}">
						商业：${policyNo_BI}
					</c:if>
				</div>
			</div>
			<hr>
			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl">
					<span onclick="changeHandleTab(1)"><i class="Hui-iconfont handing">&#xe619;</i>交强</span>
					<span onclick="changeHandleTab(2)"><i class="Hui-iconfont handing">&#xe619;</i>商业</span>
				</div>
				<!--交强-->
				<div class="tabCon clearfix table_cont table_list">
					<table id="DataTable_1" class="table table-border data-table" cellpadding="0" cellspacing="0">
						<thead>
							<tr class="text-c">
								<th>车牌号码</th>
								<th>出险原因</th>
								<th>出险时间</th>
								<th>出险地点</th>
								<th>立案时间</th>
								<th>结案时间</th>
								<th>已决金额</th>
								<th>未决金额</th>
								<th>赔案状态</th>
								<th>案件类型</th>
								<th>流程图</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
				<!-- 商业-->
				<div class="tabCon clearfix table_cont table_list">
					<table id="DataTable_2" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
						<thead>
							<tr class="text-c">
								<th>车牌号码</th>
								<th>出险原因</th>
								<th>出险时间</th>
								<th>出险地点</th>
								<th>立案时间</th>
								<th>结案时间</th>
								<th>已决金额</th>
								<th>未决金额</th>
								<th>赔案状态</th>
								<th>案件类型</th>
								<th>流程图</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
			</div>
			<!--标签页 结束-->

		</div>
	</div>
	<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
	<script type="text/javascript" src="/claimcar/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="${ctx }/js/common/AjaxList.js"></script> 
	<script type="text/javascript">
		$(function() {
			changeHandleTab("1");
		});

		function changeHandleTab(val) {
			$("input[name='handle']").val(val);
			layer.load();
			setTimeout(function() {
				$('#form').submit(), layer.closeAll('loading');
			}, 0.5 * 1000);
		};

		function rowCallback(row, data, displayIndex, displayIndexFull) {
			if(data.caseStatus=="N"){
				$('td:eq(8)', row).html("正常处理");
			}else if(data.caseStatus=="C"){
				$('td:eq(8)', row).html("注销");
			}else if(data.caseStatus=="E"){
				$('td:eq(8)', row).html("完成");
			}else{
				
			}
			$('td:eq(9)', row).html("自赔");
			$('td:eq(10)', row).html("<a class='' onClick=seePhoto('"+data.flowId+"');>查看</a>");
		}

		var columns = [ {
			"data" : "licenseNo"
		}, //车牌号码
		{
			"data" : "damageCodeName"
		}, //出险原因
		{
			"data" : "damageTime"
		}, //出险时间
		{
			"data" : "damageAddress"
		},//出险地点
		{
			"data" : "claimTime"
		}, //立案时间
		{
			"data" : "endCaseTime"
		}, //结案时间
		{
			"data" : "realPay"
		}, //已决金额
		{
			"data" : "willPay"
		}, //未决金额
		{
			"data" : "caseStatus"
		}, //赔案状态
		{
			"data" : "caseStatus"
		}, //案件类型
		{
			"data" : "caseStatus"
		} //流程图
		];

		function search() {
			var handle = $("input[name='handle']").val();
			if (isBlank(handle)) {
				return false;
			}
			var table = "#DataTable_"+handle;
			var ajaxList = new AjaxList(table);
			ajaxList.targetUrl = '/claimcar/registCommon/historySearch.do';//
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.rowCallback = rowCallback;
			ajaxList.columns = columns;

			ajaxList.query();
		}
	</script>
</body>
</html>
