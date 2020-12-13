<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>追偿任务查询</title>
</head>
<body>
    <div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<input type="hidden"  name="nodeCode"  value="${nodeCode }" >
						<input type="hidden"  name="queryRange"  value="1" > 
						
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">立案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="claimNo" 
								datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数"/>
							</div>
							<label class="form-label col-1 text-c">保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-21" 
								ignore="ignore" errormsg="请输入4到21位数" name="policyNo" />
							</div>
							<label class="form-label col-1 text-c">报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" 
								errormsg="请输入4到22位数" name="registNo" />
							</div>
						</div>
						<div class="line"></div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">流入时间</label>
							    <div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin"
									name="taskInTimeStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*"/>
								<span class="datespt">-</span><input type="text" class="Wdate" id="tiDateMax"
									name="taskInTimeEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*"/>
								<font color="red">*</font>
							</div>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> 
									<app:codeSelect codeType="ComCode" name="comCode" 
											 type="select"  />
								</span>
							</div>
							<label class="form-label col-1 text-c">案件紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box">
								<app:codeSelect 	codeType="MercyFlag" type="select" 
										name="mercyFlag" lableType="code-name" />
								</span>
							</div>
						</div>
						<div class="line"></div>
						<div class="row mb-8 cl">
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-5">
								<div class="radio-box">
								 	<label><!-- recPayTaskFlag -->
								 		<input type="radio"  name="handleStatus" value="0" onchange="changeHandleStatus(this)" checked="checked">未接收
								 	<label>
								 </div>
								 <div class="radio-box">
									<label>
										<input type="radio"  name="handleStatus" onchange="changeHandleStatus(this)" value="2">正在处理
									</label>
								 </div>
								 <div class="radio-box">
									<label>
								 	 	<input type="radio"  name="handleStatus" onchange="changeHandleStatus(this)" value="3">已处理
								 	</label> 
								 </div>				
							</div>
						</div>
						
						<div class="line"></div>
						<div class="row">
								<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search"  type="submit" disabled>
								<i class="Hui-iconfont  Hui-iconfont-search2"></i>查询</button>
							</span><br />
						</div>
						
					</form>
				</div>
			</div>
			<!--案查询条件 结束-->


			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl"> 
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handing">&#xe619;</i>未接收</span> 
					<span onclick="changeHandleTab(2)"><i class="Hui-iconfont handout">&#xe619;</i>正在处理</span>
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
				</div>
				<!--未接收-->
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>案件紧急程度</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>立案号</th>
								<th>追偿来源</th>
								<th>流入时间</th>
								<th>未处理原因</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!-- 正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_2" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>案件紧急程度</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>立案号</th>
								<th>追偿类型</th>
								<th>计划追偿总金额</th>
								<th>追偿来源</th>
								<th>流入时间</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!--已处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" >
						<thead>
							<tr>
								<tr>
								<th>案件紧急程度</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>立案号</th>
								<th>追偿类型</th>
								<th>计划追偿总金额</th>
								<th>追偿来源</th>
								<th>流入时间</th>
							</tr>
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
	//var handleStatus=$("input[name='handleStatus']:checked").val();
	var columns0 = [
		       		{
		       			"data":"mercyFlagName"     //案件紧急程度
		       		},{
		       			"data" : "registNoHtml"        //报案号
		       		},{
		       			"data" : "policyNoHtml"        //保单号
		       		}, {
		       			"data" : "claimNo"        //立案号
		       		},{
		       			"data" : "taskInNodeName"   //追偿来源
		       		},{
		       			"data" : "taskInTime",    //流入时间
		       			"orderable" : true,
		       			render : function(data, type, row) {
							return DateUtils.cutToMinute(data);
						}
		       		},{
		       			"data" : "unHandleBtn"     //未处理原因
		       		}
		       	  ];
	
	var columns2 = [
		       		{
		       			"data":"mercyFlagName"     //案件紧急程度
		       		},{
		       			"data" : "registNoHtml"        //报案号
		       		}, {
		       			"data" : "policyNoHtml"        //保单号
		       		}, {
		       			"data" : "claimNo"        //立案号
		       		}, {
		       			"data" : "replevyType"     //追偿类型
		       		}, {
		       			"data" : "sumPlanReplevy"  //计划追偿总金额
		       		},{
		       			"data" : "taskInNodeName"   //追偿来源
		       		},{
		       			"data" : "taskInTime",     //流入时间
		       			"orderable" : true,
		       			render : function(data, type, row) {
							return DateUtils.cutToMinute(data);
						}
		       		}
		       	  ];
	
	var columns3 = [
		       		{
		       			"data":"mercyFlagName"     //案件紧急程度
		       		},{
		       			"data" : "registNoHtml"        //报案号
		       		}, {
		       			"data" : "policyNoHtml"        //保单号
		       		}, {
		       			"data" : "claimNo"        //立案号
		       		}, {
		       			"data" : "replevyType"     //追偿类型
		       		}, {
		       			"data" : "sumPlanReplevy"  //计划追偿总金额
		       		},{
		       			"data" : "taskInNodeName"   //追偿来源
		       		},{
		       			"data" : "taskInTime",     //流入时间
		       			"orderable" : true,
		       			render : function(data, type, row) {
							return DateUtils.cutToMinute(data);
						}
		       		}
		       	  ];
	
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {
		   //$('td:eq(8)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick='modification("+data.id+");'>录入</button>");
		if(data.replevyType == 1){
			$('td:eq(4)',row).html("自追偿");
		}else if(data.replevyType == 2){
			$('td:eq(4)',row).html("委托追偿");
		}else if(data.replevyType == 3){
			$('td:eq(4)',row).html("垫付追偿");
		}else if(data.replevyType == 4){
			$('td:eq(4)',row).html("盗抢追偿");
		}else if(data.replevyType == 5){
			$('td:eq(4)',row).html("非机动车代位求偿");
		}
		
		if(data.claimNo!=null){
			  $('td:eq(3)',row).html("<a onclick=prePayToclaimView('"+data.claimNo+"','"+data.registNo+"');>"+data.claimNo+"</a>");
		}
	}
	   
	function search(){
		var handleStatus=$("input[name='handleStatus']:checked").val();
		
		if(isBlank(handleStatus)){
			layer.msg("请选择任务状态");
			return false;
		}
		var ajaxList = new AjaxList("#DataTable_"+handleStatus);
		ajaxList.targetUrl = 'search.do';// + $("#form").serialize();
		ajaxList.postData = $("#form").serializeJson();
		ajaxList.rowCallback = rowCallback;
		
		if(handleStatus=='0'){
			ajaxList.columns = columns0;
		}else if(handleStatus=='2'){
			ajaxList.columns = columns2;
		}else if(handleStatus=='3'){
			ajaxList.columns = columns3;
		}
		ajaxList.query();
	}
	$("[name='registNo']").change(function(){
		if($("input[name='registNo']").val().length >= 4){
			$("[name='taskInTimeStart']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
			$("[name='taskInTimeEnd']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
			
		}else if($("input[name='registNo']").val().length == 0){
			$("[name='taskInTimeStart']").attr("datatype","*");
			$("[name='taskInTimeEnd']").attr("datatype","*");
		}
	});
	
	</script>
</body>
</html>