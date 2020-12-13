<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>代位追偿回馈确认查询</title>

</head>
<body>

	<div class="page_wrap">
		<div class="table_title">代位追偿回馈确认查询</div>
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 报案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="registNo" datatype="n" maxlength="22" style="width:97%"/>
							</div>
							<label class="form-label col-2 text-c">结算码 </label>
								<div class="formControls col-3">
								<input type="text" class="input-text" name="recoveryCode" style="width:97%"/>
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
				<tr class="text-c">
					<th>报案号</th>
					<th>保单号</th>
					<th>报案时间</th>
					<th>追偿方</th>
					<th>车牌号码</th>
					<th>结算码</th>
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
		{"data" : "policyNo"},
		{"data" : "reportTime"},
		{"data" : "insuredCodeName"},
		{"data" : "licenseNo"},
		{"data" : "recoveryCode"}
	];

	function rowCallback(row, data, displayIndex, displayIndexFull) {
		var parmas = "'"+data.registNo +"','"+data.recoveryCode+"'";

		$('td:eq(0)', row).html("<a href='javascript:' onclick=showRecoveryData("+parmas+")>"+data.registNo+"</a>");

	}
	
	function search(){
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = '/claimcar/subrogationEdit/recoverRetrunSearch.do';
		ajaxList.postData = $("#form").serializeJson();
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.query();
	}
	
	function  showRecoveryData(registNo,recoveryCode){
		var goUrl ="/claimcar/subrogationEdit/recoveryData.do?registNo="+registNo+"&recoveryCode="+recoveryCode+"";
		openTaskEditWin("追偿信息",goUrl);
	}
	

	</script>
</body>
</html>
