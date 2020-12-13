<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>代位求偿锁定查询</title>

</head>
<body>

	<div class="page_wrap">
		<div class="table_title">查询</div>
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 报案号 </label>
							<div class="formControls col-3 mt-5">
								<input type="text" class="input-text" name="registNo" datatype="n" maxlength="22" style="width:97%"/>
							</div>
							<label class="form-label col-2 text-c"> 险种代码 </label>
								<div class="formControls col-3 mt-5">
								<span class="select-box"> 
									<app:codeSelect codeType="CarRiskCode" type="select" id="riskCode"
										name="riskCode" lableType="code-name" clazz="must" />
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 责任对方车辆车牌号码 </label>
							<div class="formControls col-3 mt-5">
								<input type="text" class="input-text" id="oppoentLincenseNo" name="oppoentLincenseNo" style="width:97%"
									datatype="*" errormsg="请输入正确车牌号" />
								<font color="red">*</font>
							</div>
							<label class="form-label col-2 text-c"> 责任对方车辆车牌种类 </label>
							<div class="formControls col-3 mt-5">
								<span class="select-box"> 
									<app:codeSelect codeType="LicenseKindCode" type="select" id="oppoentLincenseType" clazz="must" name="oppoentLincenseType" lableType="code-name" />
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 责任对方车辆发动机号 </label>
							<div class="formControls col-3 mt-5">
								<input type="text" class="input-text" datatype="*" name="oppoentEngineNo" />
							</div>
							
							<label class="form-label col-2 text-c"> 责任对方车辆VIN码 </label>
							<div class="formControls col-3 mt-5">
								<input type="text" class="input-text" datatype="*" id="oppoentVinNo" name="oppoentVinNo" style="width:97%"/>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 责任对方保险公司 </label>
							<div class="formControls col-3 mt-5">
								<span class="select-box"> 
									<app:codeSelect codeType="DWInsurerCode" clazz="must" type="select" id="oppoentInsureCode"
										name="oppoentInsureCode" lableType="code-name"  />
								</span>
							</div>
							<label class="form-label col-2 text-c"> 责任对方承保地区 </label>
							<div class="formControls col-3 mt-5">
								<span class="select-box">
									<app:codeSelect codeType="DWInsurerArea" id="oppoentAreaCode" clazz="must" name="oppoentAreaCode" type="select"/>
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 责任对方保单号</label>
							<div class="formControls col-3 mt-5">
								<input type="text" class="input-text"  id="oppoentPolicyNo" name="oppoentPolicyNo" style="width:97%"/>
							</div>
							<label class="form-label col-2 text-c">责任对方报案号 </label>
							<div class="formControls col-3 mt-5">
								<input type="text" class="input-text"  id="oppoentRegistNo"  datatype="*" name="oppoentRegistNo" style="width:97%"/>
							</div>
						</div>
						<div class="line"></div>
					
						<div class="text-c">
							<input type="submit" class="btn btn-primary btn-outline btn-search" id="button" value="锁定查询" disabled/>
							<input type="button" class="btn btn-primary" id="button" value="重置" />
							</span><br />
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!--案查询条件 结束-->
	<div class="page_wrap">
	<div class="table_title">锁定查询信息列表</div>
		<table id="resultDataTable" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
			<thead class="text-c">
				<tr>
					<th>责任对方保险公司</th>
					<th>责任对方承保地区</th>
					<th>责任对方保单险种类型</th>
					<th>责任对方保单号</th>
					<th>商三限额</th>
					<th>是否承保商三不计免赔</th>
					<th>责任对方车车牌号码</th>
					<th>责任对方车牌种类</th>
					<th>责任对方发动机号</th>
					<th>责任对方VIN码</th>
					<th>匹配次数</th>
					<th>锁定报案信息</th>
				</tr>
			</thead>
			<tbody class="text-c">
			
			</tbody>
		</table>

	</div>
		<script type="text/javascript">
	$(function() {
		//
		bindValidForm($('#form'),search);
		$.Datatype.licenseNo = /^[\u4e00-\u9fa5-A-Z]{1}[A-Z]{1}[A-Z_0-9]{5}$/;
	});
	
	var columns = [ 
		{"data" : "insurerCodeName"}, //保险公司
		{"data" : "insurerAreaName"}, //承保地区
		{"data" : "coverageTypeName"}, //保单险种类型
		{"data" : "policyNo"},
		{"data" : "limitAmount"}, //商三限额
		{"data" : "isInsuredCAName"}, //是否承保商三不计免赔
		{"data" : "licensePlateNo"},
		{"data" : "licensePlateTypeName"},
		{"data" : "engineNo"},
		{"data" : "vin"},
		{"data" : "matchTimes"},
		{"data" : null}
	];

	function rowCallback(row, data, displayIndex, displayIndexFull) {
		var parmas = "'"+data.registNo +"','"+data.claimSequenceNo +"','"+data.policyNo+"','"+data.coverageType+"'";
		$('td:eq(11)', row).html("<a href='javascript:' onclick=showLockedNotifyData("+parmas+")>锁定</a>");

	}
	
	function search(){
		
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = '/claimcar/subrogationEdit/lockConfirmSerach.do';
		ajaxList.postData = $("#form").serializeJson();
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.query();
	}
	
	function showLockedNotifyData(registNo,claimSequenceNo,oppoentPolicyNo,coverageType){
		var goUrl ="/claimcar/subrogationEdit/lockedNotify.do?registNo="+registNo+"&claimSequenceNo="
				+claimSequenceNo+"&oppoentPolicyNo="+oppoentPolicyNo+"&coverageType="+coverageType+"";
		openTaskEditWin("锁定报案信息",goUrl);
	}
	</script>
</body>
</html>
