<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>已调度未处理任务查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet"  />
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form"
						method="post">
						
						<input type="hidden" name="nodeCode" value="${nodeCode }"/>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> <input
								type="radio" name="keyProperty" value="registNo" /> 报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="registNo" value=""
									onfocus="radioChecked(this)" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" />
							</div>
							<label class="form-label col-1 text-c"> <input
								type="radio" name="keyProperty" value="policyNo" />保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="policyNo" value=""
									onfocus="radioChecked(this)" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数" />
							</div>
							<label class="form-label col-1 text-c"> <input
								type="radio" name="keyProperty" value="licenseNo" />车牌号码
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="licenseNo" value=""
									onfocus="radioChecked(this)" datatype="carStrLicenseNo" ignore="ignore" />
							</div>
						</div>
						<div class="row mb-3 cl">
								<div>
								<label class="form-label col-1 text-c">报案时间</label>
								<div class="formControls col-3">
									<input type="text" class="Wdate" id="rgtDateMin"
										name="reportTimeStart"
										value="<fmt:formatDate value='${reportTimeStart}' pattern='yyyy-MM-dd'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'rgtDateMax\')||\'%y-%M-%d\'}'})"
										datatype="*" /> <span class="datespt">-</span> <input
										type="text" class="Wdate" id="rgtDateMax" name="reportTimeEnd"
										value="<fmt:formatDate value='${reportTimeEnd}' pattern='yyyy-MM-dd'/>"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'rgtDateMin\')}',maxDate:'%y-%M-%d'})"
										datatype="*" /> <font color="red">*</font>
								</div>
							</div>
							<label class="form-label col-1 text-c">提交人</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="taskInUser" />
							</div>
							<label class="form-label col-1 text-c"> <input
								type="radio" name="keyProperty" value="insuredName" />被保险人
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="insuredName"
									onfocus="radioChecked(this)" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<div>
								<label class="form-label col-1 text-c">流入时间</label>
								<div class="formControls col-3">
									<input type="text" class="Wdate" id="tiDateMin"
										name="taskInTimeStart"
										value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})"
										datatype="*" /> <span class="datespt">-</span> <input
										type="text" class="Wdate" id="tiDateMax" name="taskInTimeEnd"
										value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})"
										datatype="*" /> <font color="red">*</font>
								</div>
							</div>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="ComCodeSelect" type="select" id="comCode" 
										name="comCode" lableType="code-name" />
								</span>
							</div>
				 				<div>
								<label class="form-label col-1 text-c">联系电话</label>
								<div class="formControls col-3">
									<input type="text" class="input-text" name="reporterPhone"
										datatype="n" ignore="ignore" />
								</div>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">查询范围</label>
							<div class="formControls col-3">
								<span class="select-box">
								 <app:codeSelect codeType="QueryRange" type="select" name="queryRange" lableType="code-name"  />
								  </span>
							</div>
							<label class="form-label col-1 text-c">接收人类型</label>
							<div class="formControls col-5">
							<label><input type="checkbox" name="acceptUserType" value="">司内查勘员 </label>
							<label><input type="checkbox" name="acceptUserType" value="">公估机构 </label>
							 <small>（未控制）</small>
							</div>
						</div>
					<div class="row mb-6 cl" style="display: none">
					<label class="form-label col-1 text-c">任务状态</label>
					<div class="formControls col-6">
						<div class="radio-box">
							<label><input type="radio" name="handleStatus" value="Chk" onchange="changeHandleStatus(this)"
								checked="checked" /> 查勘未处理任务</label>
						</div>
						<div class="radio-box">
							<label><input type="radio" name="handleStatus" value="PLFirst" onchange="changeHandleStatus(this)" />人伤跟踪未处理任务</label>
						</div>
					</div>
				</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search" type="submit" disabled>
									<i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询
								</button>
							</span><br />
						</div>
					</form>
				</div>
			</div>
			<!--案查询条件 结束-->


			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl">
					<span id="tabBar_0" onclick="changeHandleTab('Chk')"><i class="Hui-iconfont handun">&#xe619;</i>查勘未处理任务</span> 
					<span id="tabBar_2" onclick="changeHandleTab('PLFirst')"><i class="Hui-iconfont handun">&#xe619;</i>人伤跟踪未处理任务</span>
				</div>
				<!--未处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_Chk" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>承保机构</th>
								<th>损失项</th>
								<th>被保险人</th>
								<th>接收人</th>
								<th>接收人电话</th>
								<th>报案时间</th>
								<th>流入时间</th>
								<th>第一次调度时间</th>
								<th>最后一次调度时间</th>
								<th>超时时长（分钟）</th>
								<th>提交人</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_PLFirst"
						class="table table-border table-hover data-table" cellpadding="0">
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>承保机构</th>
								<th>主车牌号码</th>
								<th>被保险人</th>
								<th>接收人</th>
								<th>接收人电话</th>
								<th>报案时间</th>
								<th>流入时间</th>
								<th>第一次调度时间</th>
								<th>最后一次调度时间</th>
								<th>超时时长（分钟）</th>
								<th>提交人</th>
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
		function rowCallback(row, data, displayIndex, displayIndexFull) {
		}
		var columns = [ 
				{"data" : "bussTagHtml"}, //业务标识
				{"data" : "registNoHtml"}, //报案号
				{"data" : "comCodePlyName"}, //承保机构
				{"data" : "licenseNo"}, //损失项或主车牌号码
				{"data" : "insuredName"}, //被保险人
				{"data" : "assignUserName"}, //接收人
				{"data" : "assignUserPhone"}, //接收人电话
				{"data" : "reportTime"}, //报案时间
				{"data" : "taskInTime"}, //流入时间
				{"data" : "schedFirstTime"}, //第一次调度时间
				{"data" : "schedLastTime"}, //最后一次调度时间
				{"data" : "overTime"}, //超时时长
				{"data" : "taskInUserName"} //提交人
		];

		function search() {
			var handleStatus = $("input[name='handleStatus']:checked").val();

			if (isBlank(handleStatus)) {
				layer.msg("请选择任务状态");
				return false;
			}

			var ajaxList = new AjaxList("#DataTable_" + handleStatus);
			ajaxList.targetUrl = 'searchSchedUnWork.do';// + $("#form").serialize();
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.rowCallback = rowCallback;
			ajaxList.columns = columns;
			
			//layer.msg('功能开发中..');
			ajaxList.query();
		}
	</script>
</body>
</html>
