<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>定损追加查询</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<input type="hidden" name="deflossFlag" value="add"/>
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label for="registNo" class="form_label col-1">报案号</label>
							<div class="form_input col-3">
								<input id="registNo" type="text" class="input-text" name="registNo" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数"/>
							</div>
							<label for="policyNo" class="form_label col-1">保单号</label>
							<div class="form_input col-3">
								<input id="policyNo" type="text" class="input-text" name="policyNo" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数" />
							</div>
							<label for="licenseNo" class="form_label col-1">车牌号码</label>
							<div class="form_input col-3">
								<input id="licenseNo" type="text" class="input-text" name="licenseNo" datatype="carStrLicenseNo" ignore="ignore" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label for="frameNo" class="form_label col-1">车架号</label>
							<div class="form_input col-3">
								<input id="frameNo" type="text" class="input-text" name="frameNo" datatype="*4-17" ignore="ignore"/>
							</div>
							<label for="insuredName" class="form_label col-1">被保险人</label>
							<div class="form_input col-3">
								<input id="insuredName" type="text" class="input-text" name="insuredName" />
							</div>
							<label for="licenseColor" class="form_label col-1">紧急程度</label>
							<div class="form_input col-3">
								<span class="select-box">
									<select class="select f_gray4" name="mercyFlag" id="mercyFlag">
										<option value=""></option>
										<option value="0">一般</option>
										<option value="1">紧急</option>
									</select>
								</span>
							</div>
						</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search" type="submit" id="search" disabled>
									<i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询
								</button>
							</span>
							<br />
						</div>
					</div>
				</form>
			</div>
		</div>
		<!--案查询条件 结束-->
		<!--标签页 开始-->
		<table id="resultDataTable" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
			<thead>
				<tr>
					<th>业务标识</th>
					<th>报案号</th>
					<th>车牌号码</th>
					<th>被保险人</th>
					<th>车型名称</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<!--table   结束-->
		<!--标签页 结束-->
	</div>
	<script type="text/javascript">
	$(function() {
		bindValidForm($('#form'),search);
	});

	var columns = [ 
		{"data" : "bussTag"}, 
		{"data" : "registNoHtml"}, //报案号
		{"data" : "licenseNo"}, //车牌号码
		{"data" : "insuredName"}, //被保险人
		{"data" : "modelName"} //车型名称
	];

	function rowCallback(row, data, displayIndex, displayIndexFull) {
	}
	
	function search(){
		if(isBlank($("#registNo").val()) && isBlank($("#policyNo").val()) && isBlank($("#licenseNo").val())
				&& isBlank($("#frameNo").val()) && isBlank($("#insuredName").val()) && isBlank($("#mercyFlag").val())){
				layer.msg("至少录入一个查询条件！");
				return;
		}
		
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = '/claimcar/carLossAdjust/forModifySeach';
		ajaxList.postData = $("#form").serializeJson();
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.query();
	}
	</script>
</body>
</html>
