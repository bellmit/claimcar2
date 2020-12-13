<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>核赔通过清单查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<input type="hidden" name="nodeCode" value="${nodeCode }">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 报案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="registNo" value="" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" />
							</div>
							<label class="form-label col-1 text-c"> 立案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="claimNo" value="" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" />
							</div>
							<label class="form-label col-1 text-c"> 被保险人 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="insuredName" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 计算书号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="compensateNo" value="" datatype="n4-24" ignore="ignore" errormsg="请输入4到22位数" />
							</div>
							<label class="form-label col-1 text-c"> 核赔通过时间 </label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="cptDateMin" name="verifyClaimPassTimeStart"
									value="<fmt:formatDate value='${claimPassTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'cptDateMax\')||\'%y-%M-%d\'}'})" datatype="*" />
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="cptDateMax" name="verifyClaimPassTimeEnd"
									value="<fmt:formatDate value='${claimPassTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'cptDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*" />
								<font color="red">*</font>
							</div>
							<label class="form-label col-1 text-c"> 流入时间 </label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="rgtDateMin" name="createTimeStart"
									value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'rgtDateMax\')||\'%y-%M-%d\'}'})"
									datatype="*" />
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="rgtDateMax" name="createTimeEnd"
									value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'rgtDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*" />
								<font color="red">*</font>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 计算书类型 </label>
							<div class="formControls col-3">
								<span class="select-box">
									<select name="compensateType" class="select">
										<option value=''></option>
										<option value='1'>预付</option>
										<option value='2'>垫付</option>
										<option value='3'>理算</option>
										<option value='4'>预付冲销</option>
										<option value='5'>理算冲销</option>
									</select>
								</span>
							</div>
							
							<label class="form-label col-1 text-c">是否自动核赔</label>
							<div class="formControls col-3">
								<span class="select-box"> 
										<select name="autoType" class="select" >
										    <option value=''></option>
											<option value="0">否</option>
											<option value="1">是</option>
										</select>
								</span>
							</div>
								
						</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search" type="submit" disabled>
									<i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询
								</button>
							</span>
							<br />
						</div>
					</form>
				</div>
			</div>
			<!--案查询条件 结束-->
			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabCon clearfix">
					<table id="DataTable" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
						<thead>
							<tr class="text-c">
								<th>报案号</th>
								<th>立案号</th>
								<th>赔款计算书号</th>
								<th>计算书类型</th>
								<th>流入时间</th>
								<th>核赔通过时间</th>
								<th>保单号</th>
								<th>被保险人</th>
								<th>金额</th>
								<th>赔款接收人</th>
								<th>赔款接收人银行</th>
								<th>赔款人接收帐号</th>
								<th>是否已支付</th>
								<th>是否自动核赔</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
				<br/>
				<div class="row text-c">
					<!-- 导出成一个表格 -->
					<button class="btn btn-primary btn-outline btn-report" id="export" onclick="exportExcel()" type="button">导出</button>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var columns = [ {
			"data" : "registNo"
		}, //报案号
		{
			"data" : "claimNo"
		}, //立案号
		{
			"data" : "compensateNo"
		}, //赔款计算书
		{
			"data" : "compensateTypeName"
		}, //计算书类型
		{
			"data" : "createTime"
		}, //流入时间
		{
			"data" : "handleTime"
		}, //核赔通过时间
		{
			"data" : "policyNo"
		}, //保单号
		{
			"data" : "insuredName"
		}, //被保险人
		{
			"data" : "sumRealPay"
		}, //金额
		{
			"data" : "payeeName"
		}, //赔款接收人
		{
			"data" : "bankOutLets"
		}, //赔款接收人银行
		{
			"data" : "accountNo"
		}, //赔款人接收帐号
		{
			"data" : "payStatusName"
		}, //是否已支付
		{
			"data" :null
		} //是否自动核赔
		
		];
		$(function() {
			$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
			bindValidForm($('#form'),search);
			function search() {		
				var ajaxList = new AjaxList("#DataTable");
				ajaxList.targetUrl = '/claimcar/verifyClaimPass/search.do';
				ajaxList.postData = $("#form").serializeJson();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			}
		});
		function rowCallback(row, data, displayIndex, displayIndexFull) {
			if(data.handleUser=='AutoVClaim'){
				$('td:eq(13)',row).html("是");
			}else{
				$('td:eq(13)',row).html("否");
			}

		}
		function exportExcel() {
			window.open("/claimcar/verifyClaimPass/exportExcel.do?" + $('#form').serialize());
		}
	</script>
</body>
</html>