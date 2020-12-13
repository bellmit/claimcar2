<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>费用审核修改查询</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<input type="hidden" name="deflossFlag" value="modify" />
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 报案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="registNo" value="" />
							</div>
							<label class="form-label col-1 text-c"> 保单号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数" name="policyNo" value="" />
							</div>
							<label class="form-label col-1 text-c"> 标的车牌号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="*4-7" ignore="ignore" name="licenseNo" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 人员名称 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="personName" value="" />
							</div>
							<label class="form-label col-1 text-c"> 医院名称 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="hospitalName" value="" />
							</div>
							<label class="form-label col-1 text-c"> 被保险人 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="insuredName" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">住院时间</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="rgtDateMin" name="inHospitalDateStart" value="<fmt:formatDate value='${inHospitalDateStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'rgtDateMax\')||\'%y-%M-%d\'}'})" />
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="rgtDateMax" name="inHospitalDateEnd" value="<fmt:formatDate value='${inHospitalDateEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'rgtDateMin\')}',maxDate:'%y-%M-%d'})" />
							</div>
							<label class="form-label col-1 text-c">险种代码</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect codeType="RiskCode" type="select" id="riskCode" name="riskCode" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect codeType="MercyFlag" type="select" name="mercyFlag" lableType="code-name" />
								</span>
							</div>
						</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search" type="submit" id="search" disabled>
									<i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询
								</button>
							</span> <br />
						</div>
					</div>
				</form>
			</div>
		</div>
		<!--案查询条件 结束-->
		<!--标签页 开始-->
		<div id="tab-system" class="HuiTab">
			<div class="tabCon clearfix">
				<table id="resultDataTable" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
					<thead>
						<tr>
							<th>业务标识</th>
							<th>报案号</th>
							<th>保单号</th>
							<th>标的车车牌号码</th>
							<th>被保险人</th>
							<th>备注</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
		<!--table   结束-->
		<!--标签页 结束-->
	</div>
	<script type="text/javascript">
		$(function() {
			$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
			bindValidForm($('#form'), search);
		});

		var columns = [{
			"data" : "bussTagHtml"
		}, //
		{
			"data" : "registNoHtml"
		}, //报案号
		{
			"data" : "policyNoHtml"
		}, //保单号
		{
			"data" : "licenseNo"
		}, //车牌号码
		{
			"data" : "insuredName"
		}, //被保险人
		{
			"data" : "remark"
		} //备注
		];

		function rowCallback(row, data, displayIndex, displayIndexFull) {
		}

		function search() {
			var ajaxList = new AjaxList("#resultDataTable");
			ajaxList.targetUrl = '/claimcar/persTraceChargeAdjust/ChargeAdjustQuery.do';
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.columns = columns;
			ajaxList.rowCallback = rowCallback;
			ajaxList.query();
		}

		function submitNextNode(element) {

			var indexTr = $(element).parent().parent().index();

			var persTraceMainId = $("input[name='persTraceMainId']").eq(indexTr).val();

			var url = "/claimcar/loadAjaxPage/loadSubmitChargeAdjust.ajax";
			var params = {"persTraceMainId" : persTraceMainId,};
			$.post(url, params, function(result) {
				// 页面层
				layer.open({
					title : "费用审核修改提交",
					type : 1,
					skin : 'layui-layer-rim', // 加上边框
					area : ['520px', '320px'], // 宽高
					async : false,
					content : result,
					yes : function(index, layero) {
						var html = layero.html();
						var params = {
							"persTraceMainId" : persTraceMainId,
						};
						var url = "/claimcar/persTraceChargeAdjust/submitNextNode.do";
						$.post(url, params, function(jsonData) {
							var status = eval(jsonData).status;
							if(status != 200){
								layer.alert("提交失败！"+eval(jsonData).statusText);
							}else{
								layer.alert("提交成功！");
							}
							//search();
							layer.close(index); //关闭页面层
						});
					}
				});
			});
		}
	</script>
</body>
</html>
