<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>待结算金额确认</title>
</head>
<body>
	<div class="page_wrap">
		<div class="table_title">查询条件</div>
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<div class="row mb-3 cl">
							
							<label class="form-label col-2 text-c">结算码 </label>
								<div class="formControls col-3 mt-5">
								<input type="text" class="input-text" name="subrogationQueryVo.recoveryCode" style="width:97%"/>
							</div>
							<label class="form-label col-2 text-c"> 结算码状态</label>
							<div class="formControls col-3 mt-5">
								<span class="select-box"> 
									<select name="subrogationQueryVo.recoveryCodeStatus" class="select">
										<option value=''></option>
										<option value='4'>待结算</option>
										<option value='6'>零结算</option>
										<option value='7'>开始结算</option>
										<option value='10'>待支付</option>
									</select>
								</span>
							</div>
							
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 对方保险公司</label>
							<div class="formControls col-3 mt-5">
								<span class="select-box"> 
							 		<app:codeSelect codeType="DWInsurerCode" type="select" id="insurerCode"
										name="subrogationQueryVo.insurerCode" lableType="code-name"/>
								</span>		
							</div>
							<label class="form-label col-2 text-c"> 对方承保地区 </label>
							<div class="formControls col-3 mt-5">
								<span class="select-box"> 
									<app:codeSelect codeType="DWInsurerArea" type="select" id="areaCode" name="subrogationQueryVo.areaCode" />
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 本方代付/清付标志</label>
							<div class="formControls col-3 mt-5">
								<span class="select-box"> 
									<select name="subrogationQueryVo.recoverStatus" class="select">
										<option value='1'>追偿</option>
										<option value='2'>清付</option>
									</select>
								</span>
							</div>
							<label class="form-label col-2 text-c"> 代付/清付险别</label>
							<div class="formControls col-3 mt-5">
								<span class="select-box"> 
									<app:codeSelect codeType="DWCoverageType" type="select" id="comCode"
										name="subrogationQueryVo.coverageType" lableType="code-name"/>
								</span>
							</div>
						</div>
						
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c">追偿金额</label>
							<div class="formControls col-3 mt-5">
							<input type="text" class="input-text" name="subrogationQueryVo.recoveryAmountMin" style="width:45%"/>
									到
								<input type="text" class="input-text" name="subrogationQueryVo.recoveryAmountMax" style="width:45%"/>
							</div>
								<label class="form-label col-2 text-c">清付金额</label>
								<div class="formControls col-3 mt-5">
									<input type="text" class="input-text" name="subrogationQueryVo.payAmountMin" style="width:45%"/>
									到
								<input type="text" class="input-text" name="subrogationQueryVo.payAmountMax" style="width:45%"/>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c">结算起始时间</label>
							<div class="formControls col-3 mt-5">
							<input type="text" class="Wdate" id="payStartTime" 
									name="subrogationQueryVo.payStartTime"  value="<fmt:formatDate value='${lockedTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'%y-%M-%d'})" />
									到
									<input type="text" class="Wdate" id="payEndTime"
									name="subrogationQueryVo.payEndTime"  value="<fmt:formatDate value='${lockedTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'%y-%M-%d'})" />
								
							</div>
								<label class="form-label col-2 text-c">跨省标志</label>
								<div class="formControls col-3 mt-5">
									<span class="select-box"> 
									<select name="subrogationQueryVo.acrossProvinceFlag" class="select">
										<option></option>
										<option value='1'>本省</option>
										<option value='2'>跨省</option>
									</select>
								</span>
							</div>
						</div>
						<div class="line"></div>
					
						<div class="text-c">
							<input type="submit" class="btn btn-primary btn-outline btn-search" id="button" value="查询" disabled/>
							</span><br />
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!--案查询条件 结束-->
	<div class="page_wrap">
	<div class="table_title">清算查询结果</div>
		<table id="resultDataTable" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
			<thead>
				<tr class="text-c">
					<th>序号</th>
					<th>清算码</th>
					<th>清算码状态</th>
					<th>清算起始时间</th>
					<th>追偿方保险公司</th>
					<th>追偿方承保地区</th>
					<th>责任对方保险公司</th>
					<th>责任对方承保地区</th>
					<th>追偿/清付险种</th>
					<th>追偿金额</th>
					<th>清付金额</th>
					<th>清算金额</th>
				</tr>
			</thead>
			<tbody class="text-c">
			</tbody>
		</table>
	</div>
		<script type="text/javascript">
	$(function() {
		bindValidForm($('#form'),search);
	});
	var columns = [ 
		{"data" : null},
		{"data" : null},
		{"data" : "accountsNoStatusName"},
		{"data" : "accountsStartDate"}, 
		{"data" : "recoverCompanyCodeName"},
		{"data" : "recoverAreaCodeName"}, 
		{"data" : "compensateComCodeName"}, 
		{"data" : "compensationAreaCodeName"}, 
		{"data" : "coverageCodeName"},
		{"data" : "recoverAmount"},
		{"data" : "compensateAmount"},
		{"data" : "accountAmount"}
		
	];

	function rowCallback(row, data, displayIndex, displayIndexFull) {
		$('td:eq(0)', row).html(displayIndex + 1);
		var parmas = "'"+data.accountsNo +"'";

		$('td:eq(1)', row).html("<a href='javascript:' onclick=showQSConfirmInfo("+parmas+")>"+data.accountsNo+"</a>");
	}
	
	function search(){
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = '/claimcar/subrogationEdit/amountSearch';
		ajaxList.postData = $("#form").serializeJson();
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.query();
	}
	
	function showQSConfirmInfo(accountsNo) {
		var parmas = ""+accountsNo +"";
		var goUrl="/claimcar/subrogationEdit/showQsConfirmInfo.do?accountsNo="+parmas;
		
		openTaskEditWin("代位求偿待清算金额确认信息",goUrl);
	}
		
	</script>
</body>
</html>
