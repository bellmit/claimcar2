<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>预付冲销发起查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">报案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="wfTaskQueryVo.registNo" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" />
							</div>
							<label class="form-label col-1 text-c">保单号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="wfTaskQueryVo.policyNo" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数" />
							</div>
							<label class="form-label col-1 text-c"> 车牌号码 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="wfTaskQueryVo.licenseNo" value="" onfocus="radioChecked(this)" datatype="carStrLicenseNo" ignore="ignore" />
							</div>
						</div>

						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">立案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="compensateVo.claimNo" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" />
							</div>
							<label class="form-label col-1 text-c">预付号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="compensateVo.compensateNo" datatype="*4-22" ignore="ignore" errormsg="请输入4到21位数" />
							</div>
							<label class="form-label col-1 text-c"> 被保险人名称 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="wfTaskQueryVo.insuredName" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">预付类型</label>
							<div class="formControls col-3">
								<span class="select-box"> 
									<app:codeSelect codeType="PrePayType" type="select" name="wfTaskQueryVo.prePayType" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect codeType="ComCodeSelect" type="select" id="comCode" name="compensateVo.comCode" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">案件紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect codeType="MercyFlag" type="select" name="wfTaskQueryVo.mercyFlag" lableType="code-name" />
								</span>
							</div>

						</div>

						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">核赔通过时间 </label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="rgtDateMin" name="wfTaskQueryVo.underwriteDateStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'rgtDateMax\')||\'%y-%M-%d\'}'})" datatype="*" />
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="rgtDateMax" name="wfTaskQueryVo.underwriteDateEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'rgtDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*" />
								<font color="red">*</font>
							</div>
							<label class="form-label col-1 text-c">险种代码</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect codeType="RiskCode" type="select" id="riskCode" name="compensateVo.riskCode" lableType="code-name" />
								</span>
							</div>
						</div>
						<div class="line"></div>
						<br /> <br />
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search" type="submit" disabled>
									<i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询
								</button> <!-- <button class="btn btn-primary">
									查询
								</button> -->
							</span><br />
						</div>
						<br />
					</form>
				</div>
			</div>
			<!--案查询条件 结束-->

			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl">
					<span onclick="changeHandleTab(2)"><i class="Hui-iconfont handing">&#xe619;</i>待申请任务</span>
				</div>
				<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_Pay" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
						<thead>
							<tr>
								<th>紧急程度</th>
								<th>任务类型</th>
								<th>报案号</th>
								<th>预付号</th>
								<th>立案号</th>
								<th>预付赔款金额</th>
								<th>预付费用金额</th>
								<th>核赔通过时间</th>
								<th>提交人</th>
								<th>承保机构</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
			<!--标签页 结束-->

		</div>
	</div>
	<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>

	<script type="text/javascript">
		var columns = [{
			"data" : "mercyFlagName"
		}, {
			"data" : null
		}, {
			"data" : "registNo"
		}, //报案号
		{
			"data" : "compensateNo"
		}, //预付号
		{
			"data" : "claimNo"
		}, //立案号
		{
			"data" : "sumPay"
		}, //预付赔款金额
		{
			"data" : "sumFee"
		}, //预付费用金额
		{
			"data" : "underwriteDate"
		}, //核赔通过时间
		{
			"data" : "createUserName"
		}, //提交人
		{
			"data" : "comCodePlyName"
		} //承保机构

		];
		function rowCallback(row, data, displayIndex, displayIndexFull) {

			if (data.riskCode == "1101") {
				$('td:eq(1)', row).html("交强预付");
			} else {
				$('td:eq(1)', row).html("商业预付");
			}

			var url = "/claimcar/prePay/prePayWriteOff.do?registNo=" + data.registNo + "&prePayNo=" + data.compensateNo + "&claimNo=" + data.claimNo;
			$('td:eq(2)', row).html("<a  onclick='openTaskEditWin(\"预付冲销发起\",\"" + url + "\");'>" + data.registNo + "</a>");
			$('td:eq(3)', row).html("<a  onclick='openTaskEditWin(\"预付冲销发起\",\"" + url + "\");'>" + data.compensateNo + "</a>");
			if(data.claimNo!=null){
				  $('td:eq(4)',row).html("<a onclick=prePayToclaimView('"+data.claimNo+"','"+data.registNo+"');>"+data.claimNo+"</a>");
				}
		}
		function search() {
			var ajaxList = new AjaxList("#DataTable_Pay");
			ajaxList.targetUrl = '/claimcar/padpay/prePayWriteOffFinds.do';
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.columns = columns;
			ajaxList.rowCallback = rowCallback;
			ajaxList.query();
		}
		$("[name='registNo']").change(function(){
			if($("input[name='registNo']").val().length >= 4){
				$("[name='wfTaskQueryVo.underwriteDateStart']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
				$("[name='wfTaskQueryVo.underwriteDateEnd']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
				
			}else if($("input[name='registNo']").val().length == 0){
				$("[name='wfTaskQueryVo.underwriteDateStart']").attr("datatype","*");
				$("[name='wfTaskQueryVo.underwriteDateEnd']").attr("datatype","*");
			}
		});
		
		
		
	</script>
</body>
</html>
