<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>被代位查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />

</head>
<body>

	<div class="page_wrap">
		<div class="table_title">查询条件</div>
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form"
						method="post">
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c">
							 	报案号
							</label>
							<div class="formControls col-3 mt-5">
								<input type="text" id="registNo" class="input-text" dataType="*" name="registNo" value="" />
							</div>
							<label class="form-label col-2 text-c"> 保单号</label>
							<div class="formControls col-3 mt-5">
							<input type="text" class="input-text" id="policyNo" name="policyNo" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 追偿方保险公司</label>
							<div class="formControls col-3 mt-5">
								 <app:codeSelect
										codeType="DWInsurerCode" type="select" id="insurerCode"
										name="insurerCode" lableType="code-name"/>
							</div>
							<label class="form-label col-2 text-c"> 追偿方承保地区 </label>
							<div class="formControls col-3 mt-5" >
								<span class="select-box">
									<app:codeSelect codeType="DWInsurerArea" type="select" id="areaCode" name="areaCode" />
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 锁定时间</label>
							<div class="formControls col-3 mt-5">
								<input type="text" class="Wdate" id="rgtDateMin" 
									name="lockedTimeStart"  value="<fmt:formatDate value='${lockedTimeStart}' pattern='yyyyMMddHHMM'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'rgtDateMax\')||\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd HH:mm'})" />
									<span class="datespt">-</span>
									<input type="text" class="Wdate" id="rgtDateMax"
									name="lockedTimeEnd"  value="<fmt:formatDate value='${lockedTimeEnd}' pattern='yyyyMMddHHMM'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'rgtDateMin\')}',maxDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd HH:mm'})" />
								
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
		<div class="table_title">报案信息</div>
			<table id="resultDataTable"
				class="table table-border table-hover data-table" cellpadding="0"
				cellspacing="0">
				<thead>
					<tr class="text-c">
						<th>序号</th>
						<th>追偿方保险公司</th>
						<th>追偿方承保地区</th>
						<th>追偿方保单险种类型</th>
						<th>追偿方保单号</th>
						<th>追偿方报案号</th>
						<th>结算码</th>
						<th>锁定时间</th>
						<th>案件状态</th>
						<th>结算码状态</th>
					</tr>
				</thead>
				<tbody>
				
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
		       		{"data" : "coverageTypeName"}, //追偿方保单险种类型
		       		{"data" : "policyNo"}, //追偿方保单号
		       		{"data" : "claimNotificationNo"}, //追偿方报案号
		       		{"data" : "recoveryCode"}, //结算码
		       		{"data" : "lockedTime"}, //锁定时间
		       		{"data" : "claimStatusName"}, //案件状态
		       		{"data" : "recoveryCodeStatusName"} //结算码状态
		       	];
		       	function rowCallback(row, data, displayIndex, displayIndexFull) {
		       		$('td:eq(0)', row).html(displayIndex + 1);
					
		       	}
		       	
		       	function search(){
		    		
		    		if((isBlank($("#insurerCode").val()))&&(isBlank($("#areaCode").val()))&&(isBlank($("#claimCode").val()))&&(isBlank($("#registNo").val()))&&(isBlank($("#confirmSequenceNo").val()))
		    				&&(isBlank($("#rgtDateMin").val()))&&(isBlank($("#rgtDateMax").val()))){
		    			layer.msg("查询条件不可以全部为空");
		    			return ;
		    		}
		       		var ajaxList = new AjaxList("#resultDataTable");
		       		ajaxList.targetUrl = '/claimcar/subrogationQuery/beSubrogationSeach';
		       		ajaxList.postData = $("#form").serializeJson();
		       		ajaxList.columns = columns;
		       		ajaxList.rowCallback = rowCallback;
		       		ajaxList.query();
		       	}
		</script>
</body>
</html>
