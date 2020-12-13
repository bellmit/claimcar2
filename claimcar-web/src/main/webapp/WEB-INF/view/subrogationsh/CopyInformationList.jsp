<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>代位求偿抄回信息查询</title>
</head>
<body>
	<div class="page_wrap">
		<div class="table_title">代位求偿抄回信息查询</div>
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<input type="hidden" name="subrogationSHQueryVo.comCode" value="${comCode}">
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c">报案号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="subrogationSHQueryVo.registNo"  maxlength="22"/>
							</div>
							<label class="form-label col-2 text-c">立案号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="subrogationSHQueryVo.claimNo"  maxlength="22"/>
							</div>
						</div>

						<div class="line"></div>
						<div class="row">
							<span class="col-offset-5 col-2">
								<button class="btn btn-disabled btn-outline btn-search" disabled type="button" onclick="search()">
								<i class="Hui-iconfont  Hui-iconfont-search2"></i>  查询</button>
							</span><br />
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!--案查询条件 结束-->
	
	<div class="page_wrap">
			<div class="table_title">代位求偿抄回信息</div>
			<table id="resultDataTable" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
			<thead>
				<tr class="text-c">
					<th>结算码</th>
					<th>结算码状态</th>
					<th>锁定时间</th>
					<th>追偿/清付险种</th>
					<th>追偿方保险公司</th>
					<th>追偿方保单险种类型</th>
					<th>被追偿方保险公司</th>
					<th>更多信息</th>
				</tr>
			</thead>
			<tbody class="text-c">
			</tbody>
		</table>
	</div>
	<script type="text/javascript">

	var columns = [ 
		{"data" : "recoveryCode"}, //清算码
		{"data" : "recoveryCodeStatusName"}, //清算码状态-
		{"data" : "lockedTime"}, //锁定时间
		{"data" : "coverageCodeName"}, //追偿/清付险种-
		{"data" : "insurerCodeName"}, //追偿方保险公司-
		{"data" : "coverageTypeName"}, //追偿方保单险种类型-
		{"data" : "berecoveryInsurerCodeName"}, //追偿方保险公司-
		{"data" : null} //更多信息
	];

	function rowCallback(row, data, displayIndex, displayIndexFull) {
		$('td:eq(7)', row).html("<a onclick=DWMoreMessage('"+data.recoveryCode+"');>查看</a>");
	}
	
	function search(){
		if(isBlank($("input[name='subrogationSHQueryVo.registNo']").val()) 
				&& isBlank($("input[name='subrogationSHQueryVo.claimNo']").val())){
			layer.msg("查询条件不能都为空！");
			return false;
		};
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = '/claimcar/subrogationSH/copyInformationSearch.do';
		ajaxList.postData = $("#form").serializeJson();
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.query();
	}
	
	function DWMoreMessage(recoveryCode){
		var url="/claimcar/subrogationSH/DWMoreMessage.do?recoveryCode="+recoveryCode;
		layer.open({
			type : 2,
			title : false,
			shade : false,
			area : [ '95%', '90%' ],
			content : [ url],
			end : function() {
				
			}
		}); 
	
	}
	</script>
</body>
</html>
