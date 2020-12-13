<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>锁定取消</title>

</head>
<body>

	<div class="page_wrap">
		<div class="table_title">锁定取消</div>
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 报案号 </label>
							<div class="formControls col-3 mt-5">
								<input type="text" class="input-text" id="registNo" name="registNo" maxlength="40" style="width:97%"/>
							</div>
							<label class="form-label col-2 text-c">结算码 </label>
								<div class="formControls col-3 mt-5">
								<input type="text" class="input-text" id="recoveryCode" name="recoveryCode"  maxlength="40" style="width:97%"/>
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
	<div class="table_title">已锁定信息列表</div>
		<table id="resultDataTable" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
			<thead>
				<tr>
					<th>本方报案号</th>
					<th>结算码</th>
					<th>责任对方保单险种类型</th>
					<th>责任对方车车牌号码</th>
					<th>责任对方车车牌种类</th>
					<th>结算码状态</th>
					<th>锁定取消时间</th>
					<th>锁定取消原因</th>
				</tr>
			</thead>
		</table>

	</div>
		<script type="text/javascript">
	$(function() {
		bindValidForm($('#form'),search);
	});

	var columns = [
		{"data" : "registNo"}, 
		{"data" : "recoveryCode"},
		{"data" : "coverageTypeName"}, 
		{"data" : "oppoentLicensePlateNo"}, 
		{"data" : "oppoentLicensePlateTypeName"},
		{"data" : "recoveryCodeStatusName"},
		{"data" : "failureTime"}, 
		{"data" : "failureCauseName"} 
	];

	function rowCallback(row, data, displayIndex, displayIndexFull) {
		var parmas = "'"+data.registNo +"','"+data.recoveryCode +"'";
		$('td:eq(1)', row).html("<a href='javascript:' onclick=showLockedData("+parmas+")>"+data.recoveryCode+"</a>");

	}
	
	function search(){
		if(isBlank($("#recoveryCode").val()) && isBlank($("#registNo").val())){
			layer.msg("报案号和结算码至少录入一个！");
			return ;
		}
		
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = '/claimcar/subrogationEdit/lockCancelSerach.do';
		ajaxList.postData = $("#form").serializeJson();
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.query();
	}
	
	function showLockedData(registNo,recoveryCode){
		var goUrl ="/claimcar/subrogationEdit/lockededData.do?registNo="+registNo+"&recoveryCode="+recoveryCode;
		
		openTaskEditWin("锁定案件信息",goUrl);
	}
	</script>
</body>
</html>
