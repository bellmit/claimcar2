<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>结算码查询</title>
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
								<input type="text" class="input-text" id="registNo" datatype="*" name="registNo" />
							<font color="red">*</font>
							</div>
							<label class="form-label col-2 text-c"> 保单号</label>
							<div class="formControls col-3 mt-5">
							<input type="text" class="input-text"  name="policyNo" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 对方保险公司</label>
							<div class="formControls col-3 mt-5">
							 <app:codeSelect
										codeType="DWInsurerCode" type="select" id="oppoentInsureCode"
										name="insurerCode" lableType="code-name"/>
							</div>
							<label class="form-label col-2 text-c"> 对方承保地区 </label>
							<div class="formControls col-3 mt-5">
								<span class="select-box">
									<app:codeSelect codeType="DWInsurerArea" type="select" id="areaCode" name="oppoentAreaCode" />
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 险别</label>
							<div class="formControls col-3 mt-5">
								<span class="select-box"> 
									<select name="riskCodeSub" id="riskCodeSub"  class="select">
										<option value='11'>交强险</option>
										<option value='12'>商业险</option>
									</select>
								</span>
							</div>
							<label class="form-label col-2 text-c"> 对方报案号</label>
							<div class="formControls col-3 mt-5">
								<input type="text" class="input-text" name="oppoentRegistNo" />
							
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 对方保单险种类型</label>
							<div class="formControls col-3 mt-5">
								<select name="oppentPolicyType" id="coverageType" class="select">
										<option value='3'>商业车损险</option>
									</select>
							</div>
							<label class="form-label col-2 text-c"> 本方代付/清付标志</label>
							<div class="formControls col-3 mt-5">
								<span class="select-box"> 
									<select name="recoverStatus" id="recoverStatus" class="select">
										<option value='2'>被追偿</option>
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
						<th>结算码</th>
						<th>结算码状态</th>
						<th>结算码失效时间</th>
						<th>结算码失效原因</th>
						<th>本方追偿状态</th>
						<th>对方报案号</th>
						<th>对方保单险种类型</th>
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
			
			//1-交强险  2-商业三者险    3-商业车损险

			$("#riskCodeSub").change(function (){
				if($("#riskCodeSub").val()=='11'){
					$("#coverageType").find("option").remove();
					$("#coverageType").append("<option value='3'>商业车损险</option>");
					$("#recoverStatus").find("option").remove();
					$("#recoverStatus").append("<option value='2'>被追偿</option>");
				}else{
					$("#coverageType").find("option").remove();
					$("#coverageType").append("<option value='1'>交强险</option><option value='2'>商业三者险</option>");
					$("#recoverStatus").find("option").remove();
					$("#recoverStatus").append("<option value='1'>追偿</option><option value='2'>被追偿</option>");
				}
			});
			
			$("#recoverStatus").change(function (){
				if($("#recoverStatus").val()=='2'){
					$("#coverageType").find("option").remove();
					$("#coverageType").append("<option value='3'>商业车损险</option>");
				}else{
					$("#coverageType").find("option").remove();
					$("#coverageType").append("<option value='1'>交强险</option><option value='2'>商业三者险</option>");
				}
			});
		});

		var columns = [ 
			{"data" : null},
			{"data" : "recoveryCode"}, //保险公司
			{"data" : "recoveryCodeStatusName"}, //承保地区
			{"data" : "failureTime"}, //追偿方保单险种类型
			{"data" : "failureCauseName"}, //追偿方保单号
			{"data" : "recoverStatusName"}, //追偿方报案号
			{"data" : "claimNotificationNo"}, //结算码
			{"data" : "coverageTypeName"} //结算码状态
		];
		function rowCallback(row, data, displayIndex, displayIndexFull) {
			$('td:eq(0)', row).html(displayIndex + 1);
	
		}
			
		function search(){
			var ajaxList = new AjaxList("#resultDataTable");
			ajaxList.targetUrl = '/claimcar/subrogationQuery/recoverySeach';
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.columns = columns;
			ajaxList.rowCallback = rowCallback;
			ajaxList.query();
		}
		</script>
</body>
</html>
