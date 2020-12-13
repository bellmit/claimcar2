<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>账号信息查询</title>
</head>
<body>
    <div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">计算书号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="" />
							</div>
							<label class="form-label col-1 text-c">立案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name=""   />
							</div>
							<label class="form-label col-1 text-c">被保险人
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="" "/>
							</div>
						</div>
						<div class="line"></div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">申请日期</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate"  id="dateMin" name=  
								 value="<fmt:formatDate value='${applyTimeStart}' pattern='yyyy-MM-dd'/>"
								onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'dateMax\')||\'%y-%M-%d\'}'})">
								 到
								 <input type="text" class="Wdate"  id="dateMax" name=""  
								 value="<fmt:formatDate value='${applyTimeEnd}' pattern='yyyy-MM-dd'/>"
								 onfocus="WdatePicker({minDate:'#F{$dp.$D(\'dateMin\')}',maxDate:'%y-%M-%d'})" >
							</div>
							<label class="form-label col-1 text-c">操作人</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name=""   />
							</div>
							
						</div>
						<div class="line"></div>
						<div class="row mb-8 cl">
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-5">
								<div class="radio-box">
								 	<label><!-- payBankTaskFlag -->
								 		<input type="radio"  name="payBankTaskFlag"  onchange="changePayBankTaskFlag(this)" value="0" checked="checked">未处理
								 	<label>
								 </div>
								 <div class="radio-box">
									<label>
										<input type="radio"  name="payBankTaskFlag" onchange="changePayBankTaskFlag(this)" value="1">已处理
									</label>
								 </div>
								 		
							</div>
						</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search"
									id="search" type="button" disabled>查询</button>
							</span><br />
						</div>
						
					</form>
				</div>
			</div>
			
			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl"> 
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handing">&#xe619;</i>未处理</span> 
					<span onclick="changeHandleTab(1)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
					
				</div>
				<!-- 未处理 -->
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>计算书号</th>
								<th>立案号</th>
								<th>被保险人</th>
								<th>开户银行</th>
								<th>开户人</th>
								<th>银行账号</th>
								<th>修改原因</th>
								<th>申请日期</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!-- 已处理 -->
				<div class="tabCon clearfix">
					<table id="DataTable_1" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>计算书号</th>
								<th>立案号</th>
								<th>被保险人</th>
								<th>开户银行</th>
								<th>开户人</th>
								<th>银行账号</th>
								<th>修改原因</th>
								<th>申请日期</th>
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
	
	<script type="text/javascript">
	var column = [
		       		{
		       			"data":"compensateNo"     //计算书号
		       		}, {
		       			"data" : "claimNo"        //立案号
		       		}, {
		       			"data" : ""        //被保险人
		       		}, {
		       			"data" : ""     //开户银行
		       		}, {
		       			"data" : ""  //开户人
		       		},{
		       			"data" : ""   //银行账号
		       		},{
		       			"data" : ""   //修改原因
		       		}
		       		,{
		       			"data" : "",     //申请日期
		       			"orderable" : true,
		       			render : function(data, type, row) {
							return DateUtils.cutToMinute(data);
						}
		       		}
		       	  ];
	
	function ajaxSubmit(tableId){
		var ajaxList = new AjaxList("#"+tableId+"");
		ajaxList.targetUrl = '/claimcar/payBank/search.do';
		ajaxList.postData=$("#form").serializeJson();
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.query();
	}
	
	function search(){
		var payBankTaskFlag=$("input[name='payBankTaskFlag']:checked").val();
		
		if(isBlank(payBankTaskFlag)){
			layer.msg("请选择任务状态");
			return false;
		}
		if(payBankTaskFlag=='0'){
			ajaxSubmit("DataTable_"+payBankTaskFlag);
		}else if(payBankTaskFlag=='1'){
			ajaxSubmit("DataTable_"+payBankTaskFlag);
		}
	}
	
	function changeHandleTab(val){
		$("input[name='recPayTaskFlag']").each(
			function(){
				if( $(this).val()==val){
					$(this).prop("checked",true);
					var handleStatus=$("input[name='payBankTaskFlag']:checked").val();
					var tBody=$("#DataTable_"+handleStatus).find("tbody");
					if(!(tBody.children().length>0)){
						layer.load();
						setTimeout(function(){ 
							search();
							layer.closeAll('loading');
						},0.5*1000); 
						
					}
				}
			});
	}
	
	/**切换选择的任务状态*/
	function changePayBankTaskFlag(obj){
		var payBankTaskFlagRadio=form.payBankTaskFlag;
		var selectIdx=0;
		for(var i=0;i<payBankTaskFlagRadio.length;i++){
			if(payBankTaskFlagRadio[i].checked==true){
				selectIdx=i;
				break;
			}
		}
		$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", selectIdx);
	}
</body>
</html>