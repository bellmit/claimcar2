<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>风险预警保单信息查询</title>

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
							<label class="form-label col-2 text-c"> 车牌种类 </label>
							<div class="formControls col-3 mt-5">
								<span class="select-box"> 
									<app:codeSelect codeType="LicenseKindCode" type="select" id="licenseType" name="licenseType" lableType="code-name" />
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 车牌号 </label>
							<div class="formControls col-3 mt-5">
								<input type="text" class="input-text" id="licenseNo" name="licenseNo" style="width:97%"
									datatype="carStrLicenseNo" errormsg="请输入正确车牌号" ignore="ignore" />
								<font color="red">*</font>
							</div>
							<label class="form-label col-2 text-c"> 发动机号 </label>
							<div class="formControls col-3 mt-5">
								<input type="text" class="input-text" name="engineNo" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> VIN码 </label>
							<div class="formControls col-3 mt-5">
								<input type="text" class="input-text"  id="vinNo" name="vinNo" style="width:97%"/>
							</div>
							<label class="form-label col-2 text-c"> 险别 </label>
							<div class="formControls col-3 mt-5">
								<span class="select-box"> 
									<select name="riskCodeSub" class="select">
										<option value='11'>交强险</option>
										<option value='12'>商业险</option>
									</select>
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 指定地区 </label>
							<div class="formControls col-3 mt-5">
								<span class="select-box">
									<app:codeSelect codeType="DWInsurerArea" type="select" id="areaCode" name="areaCode" />
								</span>
						</div>
						</div>

						<div class="line"></div>
					
						<div class="text-c">
								<button class="btn btn-primary btn-outline btn-search" disabled>
									查询
								</button>
							</span><br />
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!--案查询条件 结束-->
	<div class="page_wrap">
	<div class="table_title">保单信息</div>
		<table id="resultDataTable" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
			<thead>
				<tr class="text-c">
					<th>序号</th>
					<th>保险公司</th>
					<th>承保地区</th>
					<th>保单险种类型</th>
					<th>商三限额</th>
					<th>是否承保商三不计免赔</th>
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
		{"data" : null},
		{"data" : "insurerCodeName"}, //保险公司
		{"data" : "insurerAreaName"}, //承保地区
		{"data" : "coverageTypeName"}, //保单险种类型
		{"data" : "limitAmount"}, //商三限额
		{"data" : "insuredCAName"} //是否承保商三不计免赔
		
	];

	function rowCallback(row, data, displayIndex, displayIndexFull) {
		$('td:eq(0)', row).html(displayIndex + 1);

	}
	
	function search(){
		//车三项至少有号牌号码&号牌种类 或者 17位VIN码其一
		//风险预警承保信息查询中号牌号码和号牌类型必须同时上传
		if(isBlank($("#vinNo").val()) && (isBlank($("#licenseNo").val()) && isBlank($("#licenseType").val()))){
			layer.msg("车三项至少有号牌号码&号牌种类 或者 17位VIN码其一");
			return ;
		}
		
		if((!isBlank($("#licenseNo").val()) && isBlank($("#licenseType").val())) ||
				(isBlank($("#licenseNo").val()) && !isBlank($("#licenseType").val()))){
			layer.msg("风险预警承保信息查询中号牌号码和号牌类型必须同时上传");
			return ;
		}
		
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = '/claimcar/subrogationQuery/policyNoSeach';
		ajaxList.postData = $("#form").serializeJson();
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.query();
	}
	</script>
</body>
</html>
