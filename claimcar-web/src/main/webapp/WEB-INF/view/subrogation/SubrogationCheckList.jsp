<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>案件互审</title>

</head>
<body>

	<div class="page_wrap">
		<div class="table_title">案件互审</div>
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<div class="row mb-3 cl">
							
							<label class="form-label col-2 text-c">结算码 </label>
								<div class="formControls col-3 mt-5">
								<input type="text" class="input-text" id="accountsNo" datatype="*" name="accountsNo"  style="width:97%"/>
							</div>
							<label class="form-label col-2 text-c"> 互审状态</label>
							<div class="formControls col-3 mt-5">
								<span class="select-box"> <app:codeSelect
										codeType="CheckStats" type="select" id="checkStats"
										name="checkStats" lableType="code-name" />
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							
							<label class="form-label col-2 text-c">审核方类型</label>
								<div class="formControls col-3 mt-5">
								<span class="select-box">
									 <select name="checkOwnType"  class="select" style="width:97%">
										 <option value='1'>追偿方</option>
										 <option value='2'>责任对方</option>
									 </select>
								</span>
							</div>
							<label class="form-label col-2 text-c">追偿起始时间</label>
							<div class="formControls col-3 mt-5">
							<input type="text" class="Wdate" id="rgtDateMin" 
									name="recoverDStart" 
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'rgtDateMax\')||\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd HH:mm'})" />
									到
									<input type="text" class="Wdate" id="rgtDateMax"
									name="RecoverDEnd"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'rgtDateMin\')}',maxDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd HH:mm'})" />
								
							</div>
						</div>
						<div class="line"></div>
					
						<div class="text-c">
							<input type="submit" class="btn btn-primary" id="button" value="互审查询" />
							<input type="reset" class="btn btn-primary" id="button" value="重置" />
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
					<th>序号</th>
					<th>结算码</th>
					<th>追偿起始时间</th>
					<th>追偿方保险公司报案号</th>
					<th>追偿方保险公司</th>
					<th>追偿方承保地区</th>
					<th>责任对方报案号</th>
					<th>责任对方保险公司</th>
					
					<th>责任对方承保地区</th>
					<th>追偿/清付险种</th>
					<th>互审状态</th>
					<th>操作</th>
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
		{"data" : "recoveryCode"}, //结算码
		{"data" : "recoverDStart"}, //追偿起始时间
		{"data" : "recoverReportNo"}, //追偿方保险公司报案号
		{"data" : "recoverComCodeName"}, //追偿方保险公司
		{"data" : "recoverAreaCodeName"}, //追偿方承保地区
		{"data" : "compensateReportNo"}, //责任对方报案号
		{"data" : "compensateComCodeName"}, //责任对方保险公司
		{"data" : "compensateAreaCodeName"}, //责任对承保地区
		{"data" : "recoverReportNo"}, //追偿/清付险种
		{"data" : "checkStatsName"},//互审状态
		{"data" : null}
	];

	function rowCallback(row, data, displayIndex, displayIndexFull) {
		$('td:eq(0)', row).html(displayIndex + 1);
		$('td:eq(11)', row).html("<button class='"+"btn btn-primary"+"' onclick=showCheck('"+data.recoveryCode+"','"+data.recoverReportNo+"','"+data.compensateReportNo+"')>"+"互审信息</button>");
		
		/* if(data==null){
			layer.alert("hdashkdsa");
		} */
	}
	
	function search(){
		/* if(isBlank($("#accountsNo").val())){
			layer.msg("结算码 不能为空");
			return ;
		} */
		
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = '/claimcar/subrogationEdit/sCheckQueryList';
		ajaxList.postData = $("#form").serializeJson();
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.query();
	}
	function showCheck(a,b,c){

		var url = "?recoveryCode="+a+"&recoverReportNo="+b+"&compensateReportNo="+c;
		var goUrl ="/claimcar/subrogationEdit/showCheck.do" +url;
		openTaskEditWin("案件互审信息",goUrl);
		/* layer.open({
			
		    type: 2,
		    shade: false,
			shadeClose : true,
			scrollbar : true,
		    skin: 'yourclass',
		    area: ['1220px', '600px'],
		     content:"/claimcar/subrogationEdit/showCheck.do"+url,
		}); */
		
	}
	
	</script>
</body>
</html>
