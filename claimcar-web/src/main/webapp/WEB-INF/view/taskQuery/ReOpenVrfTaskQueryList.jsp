<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>重开赔案审核查询</title>
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
							<label class="form-label col-1 text-c">报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="registNo" 
								datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数"/>
							</div>
							<label class="form-label col-1 text-c">保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-21" 
								ignore="ignore" errormsg="请输入4到21位数" name="policyNo" />
							</div>
							<label class="form-label col-1 text-c">立案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" 
								errormsg="请输入4到22位数" name="claimNo" />
							</div>
						</div>
						<div class="line"></div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">被保险人
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="insuredName" />
							</div>
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
								<th>立案号</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>险种名称</th>
								<th>被保险人</th>
								<th>提交人</th>
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
								<th>立案号</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>险种名称</th>
								<th>被保险人</th>
								<th>提交人</th>
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
								<th>立案号</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>险种名称</th>
								<th>被保险人</th>
								<th>处理人</th>
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
	var columns0 = [
		       		{
		       			"data":"claimNoHtml"     //立案号
		       		},{
		       			"data" : "registNo"       //报案号
		       		},{
		       			"data" : "policyNo"       //保单号
		       		},{
		       			"data" : "riskCodeName"       //险种名称
		       		},{
		       			"data" : "insuredName"    //被保险人
		       		},{
		       			"data" : "taskInUserName"    //提交人
					},{
						"data" : "unHandleBtn"    //未处理原因
					}
		       	  ];
	var columns2 = [
		       		{
		       			"data":"claimNoHtml"     //立案号
		       		},{
		       			"data" : "registNo"       //报案号
		       		},{
		       			"data" : "policyNo"       //保单号
		       		},{
		       			"data" : "riskCodeName"       //险种名称
		       		},{
		       			"data" : "insuredName"    //被保险人
		       		},{
		       			"data" : "taskInUserName"    //提交人
					}
		       	  ];
	var columns3 = [
		       		{
		       			"data":"claimNoHtml"     //立案号
		       		},{
		       			"data" : "registNo"       //报案号
		       		},{
		       			"data" : "policyNo"       //保单号
		       		},{
		       			"data" : "riskCodeName"       //险种名称
		       		},{
		       			"data" : "insuredName"    //被保险人
		       		},{
		       			"data" : "taskInUserName"    //处理人
					}
		       	  ];
	function rowCallback(row, data, displayIndex, displayIndexFull) {
		if(data.riskCode == '1101' && data.policyNoLink != null){
			$('td:eq(2)', row).html(data.policyNoLink);
		}else{
			$('td:eq(2)', row).html(data.policyNo);
		}
		
		$('td:eq(1)',row).html("<a onclick=claimToRegistView('"+data.registNo+"');>"+data.registNo+"</a>");
		
		if(data.policyNo!=null){
			$('td:eq(2)',row).html("<a href=/claimcar/policyView/policyView.do?registNo="+data.registNo+" target='_blank'>"+data.policyNo+"</a>");
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
	
	function claimToRegistView(registNo){
	     var title='报案处理';
	     var url='/claimcar/regist/edit.do?registNo='+registNo;
	      openTaskEditWin(title,url);
     }
	</script>
</body>
</html>