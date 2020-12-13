<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>警保联动</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 报案人姓名</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="respUserName" value="" />
							</div>
							<label class="form-label col-1 text-c"> 报案人电话 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="respUserPhone" value="" />
							</div>
							<label class="form-label col-1 text-c"> 驾驶员姓名 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="driverName" value="" />
							</div>
						</div>

						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 标的车车牌号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="hphm" value="" />
							</div>
							<label class="form-label col-1 text-c"> 出险时间 </label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="accidentTimeStart"  value="<fmt:formatDate value='${timeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*" />
									 <span class="datespt">-</span> 
								<input type="text" class="Wdate" id="tiDateMax"  name="accidentTimeEnd"    value="<fmt:formatDate value='${timeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*" /> 
									<font color="red">*</font>
							</div>

						</div>

						<div class="line"></div>
						<div class="row cl text-c">
							<span class="col-offset-8 col-4">
								<button class="btn btn-primary btn-outline btn-search" disabled type="submit">
									<i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询
								</button> 
							</span>
						</div>
					</form>
				</div>
			</div>
			<br />
			<!-- 查询条件结束 -->
             <div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabCon clearfix table_cont table_list">
						<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
							<thead>
								<tr class="text-c">
									<th>案件编码</th>
									<th>报案人姓名</th>
									<th>报案人联系方式</th>
									<th>案件类型</th>
									<th>出险时间</th>
									<th>驾驶员姓名</th>
									<th>驾驶员联系方式</th>
									<th>标的车车牌</th>
									<th>案件状态</th>
									<th>是否报案</th>
								</tr>
							</thead>
							<tbody class="text-c">
							</tbody>
						</table>
						<!--table   结束-->
						<div class="row text-c">
							<br />
						</div>
					</div>
				</div>
			</div>
			<!--标签页 开始-->
			
		</div>
	</div>
<script type="text/javascript">

$(function() {
	$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
	$("#intermCode").attr("datatype","selectMust");
	$.Datatype.selectMust = function(gets, obj, curform, regxp) {
		var code = $(obj).val();
		if (isBlank(code)) {
			return false;
		}
	};
	bindValidForm($('#form'), search);
	
	
});

	var columns = [ {
		"data" : "caseId"
	}, // 案件编码
	{
		"data" : "respUserName"
	}, //报案人姓名
	{
		"data" : "respUserPhone"
	}, //报案人电话
	{
		"data" : "caseType"
	}, //案件类型
	{
		"data" : "accidentTime"
	}, //出险时间
	{
		"data" : "driverName"
	},//驾驶人姓名
	{
		"data" : "phone"
	//驾驶人电话
	}, {
		"data" : "hphm"
	//标的车车牌
	}, {
		"data" : "statusName"
	//案件状态
	}, {
		"data" : "isResp"
	//是否报案
	} ];

	function rowCallback(row, data, displayIndex, displayIndexFull) {
		$('td:eq(0)', row).html("<a  onclick='openWinInfo("+data.caseId+");'>"+data.caseId+"</a>");
	}
	function search() {
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = '/claimcar/policyLink/policyLinkQuery.do';
		ajaxList.postData = $("#form").serializeJson();
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.query();
	}
	
	function openWinInfo(caseId){
		url="/claimcar/policyLink/policyLinkShowList.do?caseId="+caseId;
		openTaskEditWin("警保信息展示", url, null);
		}
	
</script>

</body>
</html>