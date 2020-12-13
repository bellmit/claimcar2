<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>调查查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet"  />
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
					<input type="hidden"  name="nodeCode"  value="${nodeCode }" >
					<input type="hidden"  name="subNodeCode"  value="${subNodeCode }" >
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="registNo" value="" />
							</div>
							<label class="form-label col-1 text-c">立案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="claimNo" value="" />
							</div>
							<label class="form-label col-1 text-c">保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数" name="policyNo"  value="" />
							</div>
						</div>
						
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">任务生成时间</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin"
									name="taskInTimeStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax"
									name="taskInTimeEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*"/>
								<font color="red">*</font>
							</div>
						</div> 
						<div class="row mb-6 cl">
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-6">
								<div class="radio-box">
									<label><input type="radio" name="handleStatus"
										value="0" onchange="changeHandleStatus(this)"
										checked="checked" /> 未接收</label>
								</div>
								<div class="radio-box">
									<label><input type="radio" name="handleStatus"
										value="2" onchange="changeHandleStatus(this)" />正在处理</label>
								</div>
								<div class="radio-box">
									<label><input type="radio" name="handleStatus"
										value="3" onchange="changeHandleStatus(this)" />已处理</label>
								</div>
							</div>
						</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search"  type="submit" disabled>
								<i class="Hui-iconfont  Hui-iconfont-search2"></i>  查询</button>
							</span><br />
						</div>
					</form>
				</div>
			</div>
						
			<!--案查询条件 结束-->


			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl"> 
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handun">&#xe619;</i>未接收</span> 
					<span onclick="changeHandleTab(2)"><i class="Hui-iconfont handing">&#xe619;</i>正在处理</span>
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
				</div>
				<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>报案号</th>
								<th>机构</th>
								<th>出险时间</th>
								<th>报案时间</th>
								<th>任务生成时间</th>
								<th>被保险人</th>
								<th>发起人</th>
								<th>处理人</th>
								<th>任务提交时间</th>
								<th>状态</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!-- 已处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_2" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>报案号</th>
								<th>机构</th>
								<th>出险时间</th>
								<th>报案时间</th>
								<th>任务生成时间</th>
								<th>被保险人</th>
								<th>发起人</th>
								<th>处理人</th>
								<th>任务提交时间</th>
								<th>状态</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!--注销-->
				<div class="tabCon clearfix">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" >
						<thead>
							<tr class="text-c">
								<th>报案号</th>
								<th>机构</th>
								<th>出险时间</th>
								<th>报案时间</th>
								<th>任务生成时间</th>
								<th>被保险人</th>
								<th>发起人</th>
								<th>处理人</th>
								<th>任务提交时间</th>
								<th>状态</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
			<!--标签页 结束-->
		</div>
	</div>

	<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
	<script type="text/javascript">
		//----------正在处理  --- start
		var columns = [ 
			{"data" : "registNo"}, //报案号
			{"data" : "comCodeName"}, //机构
	        {"data" : "damageTime"}, //出险时间
	        {"data" : "reportTime"}, //报案时间
			{"data" : "taskInTime"}, //任务生成时间
			{"data" : "insuredName"}, //被保险人
			{"data" : "taskInUserName"}, //发起人
			{"data" : "handlerUserName"}, //处理人
			{"data" : "handlerTime"}, //处理时间
			{"data" : "handlerStatusName"} //处理时间
			];

		function rowCallback(row, data, displayIndex, displayIndexFull) {
 			$('td:eq(0)', row).html("<a onclick=\"openTaskEditWin('调查处理','/claimcar/survey/init.do?flowTaskId="+data.taskId+"')\">"+data.registNo+"</a>");
 		}
		function search() {
			var handleStatus = $("input[name='handleStatus']:checked").val();

			if (isBlank(handleStatus)) {
				layer.msg("请选择任务状态");
				return false;
			}

			var ajaxList = new AjaxList("#DataTable_" + handleStatus);
			ajaxList.targetUrl = 'search.do';// + $("#form").serialize();
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.rowCallback = rowCallback;

			if (handleStatus != '5') {
				ajaxList.columns = columns;
			} 

			ajaxList.query();
		}

	</script>
</body>
</html>
