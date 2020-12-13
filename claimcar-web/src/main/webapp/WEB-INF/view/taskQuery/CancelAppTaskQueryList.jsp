<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>立案任务查询</title>
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
					<input type="hidden"  name="queryRange"  value="1" >
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">报案号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="registNo" value="" />
							</div>
							
							<label class="form-label col-1 text-c">立案号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="claimNo" value=""/>
							</div>
							<label class="form-label col-1 text-c">保单号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数" name="policyNo"  value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">标的车牌号码</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="carStrLicenseNo" ignore="ignore" name="licenseNo" value="" />
							</div>
							
							<label class="form-label col-1 text-c">立案时间</label>
								<div class="formControls col-3">
									<input type="text" class="Wdate" id="rgtDateMin"
										name="taskInTimeStart"
										value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'rgtDateMax\')||\'%y-%M-%d\'}'})"
										datatype="*" /> <span class="datespt">-</span> <input
										type="text" class="Wdate" id="rgtDateMax" name="taskInTimeEnd"
										value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'rgtDateMin\')}',maxDate:'%y-%M-%d'})"
										datatype="*" /> <font color="red">*</font>
								</div>
							
						</div>
						</div>
						<div class="row mb-6 cl">
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-6">
								<label><input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="0" checked="checked">未处理</label>
								<label><input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="2" >正在处理</label>
								<label><input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="3">已处理</label>
								<label><input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="6">已退回</label> 
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
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handout">&#xe619;</i>未处理</span>
					<span onclick="changeHandleTab(2)"><i class="Hui-iconfont handout">&#xe619;</i>正在处理</span>
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handing">&#xe619;</i>已处理</span> 
				 	<span onclick="changeHandleTab(6)"><i class="Hui-iconfont handout">&#xe619;</i>已退回</span>
				</div>
				<!--可处理-->
				<!-- 已处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>立案号</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>被保险人</th>
								<th>起保日期</th>
								<th>终保日期</th>
								<th>立案时间</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
				<!-- 已处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_2" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>立案号</th>
								<th>报案号</th>
								<th>被保险人</th>
								<th>立案日期</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
				<div class="tabCon clearfix">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>立案号</th>
								<th>报案号</th>
								<th>被保险人</th>
								<th>立案日期</th>
								<th>立案注销日期</th>
								<th>立案注销原因</th>
								<th>处理人</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
				<div class="tabCon clearfix">
					<table id="DataTable_6" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>立案号</th>
								<th>报案号</th>
								<th>被保险人</th>
								<th>立案日期</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
			<!--标签页 结束-->
			
		</div>
	</div>
<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>

	<script type="text/javascript">


		function rowCallback(row, data, displayIndex, displayIndexFull) {
			var handleStatus1=$("input[name='handleStatus']:checked").val();
			$('td:eq(1)',row).html("<a onclick=claimToRegistView('"+data.registNo+"');>"+data.registNo+"</a>");
		     if(handleStatus1=='0'){
		      $('td:eq(2)',row).html("<a href=/claimcar/policyView/policyView.do?registNo="+data.registNo+" target='_blank'>"+data.policyNo+"</a>");
		    } 
			
		}
		
		var columns = [
		   			{"data" : "claimCancelHtml"}, //立案号
		   			{"data" : "registNo"}, //报案号
		   			{"data" : "insuredName"}, //被保险人
		   			{"data" : "claimTime"} //立案日期
		   			];
		var columns1 = [
		   			{"data" : "claimCancelHtml"}, //立案号
		   			{"data" : "registNo"}, //报案号
		   	        
		   			{"data" : "insuredName"}, //被保险人
		   			{"data" : "claimTime"}, //立案日期
		   	        {"data" : "claimCancelTime"}, //立案注销日期
		   			{"data" : "applyReason"}, //立案注销原因
	   				{"data" : "assignUserName"} //处理人--
		   			];
		var columns2 = [
			   			{"data" : "claimCancelHtml"}, //立案号
			   			{"data" : "registNo"}, //报案号
			   			{"data" : "policyNo"}, //保单号
			   			{"data" : "insuredName"}, //被保险人
			   			{"data" : "startDate"}, //立案日期
			   	        {"data" : "endDate"}, //立案注销日期
			   			{"data" : "claimTime"}
			   			];
		function search(){
					var handleStatus=$("input[name='handleStatus']:checked").val();
					if(isBlank(handleStatus)){
						layer.msg("请选择任务状态");
						return false;
					}
					var ajaxList = new AjaxList("#DataTable_"+handleStatus);
					
					if(handleStatus == '0'){
						ajaxList.targetUrl = 'CancelAppTaskQuery.do';// + $("#form").serialize();
					}else{
						ajaxList.targetUrl = 'search.do';// + $("#form").serialize();
					}
					ajaxList.postData = $("#form").serializeJson();
					ajaxList.rowCallback = rowCallback;

					if(handleStatus == '3') {
						ajaxList.columns = columns1;
					}else if(handleStatus == '0'){
						ajaxList.columns = columns2;
					}else{
						ajaxList.columns = columns;
					}
					
					ajaxList.query();
					
			}
		
		function claimToRegistView(registNo){
			var title='报案处理';
			var url='/claimcar/regist/edit.do?registNo='+registNo;
			openTaskEditWin(title,url);
		}
		
		
	</script>
</body>
</html>
