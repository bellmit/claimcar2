<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>重开赔案登记查询</title>
</head>
<body>
<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="endCaseVo.registNo" 
								datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" onchange="change()" />
							</div>
							<label class="form-label col-1 text-c">保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-21" 
								ignore="ignore" errormsg="请输入4到21位数" name="endCaseVo.policyNo" />
							</div>
							<label class="form-label col-1 text-c">立案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" 
								errormsg="请输入4到22位数" name="endCaseVo.claimNo" />
							</div>
						</div>
						<div class="line"></div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">被保险人
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="wfTaskQueryVo.insuredName" />
							</div>
							<label class="form-label col-1 text-c">案件紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box">
								<app:codeSelect 	codeType="MercyFlag" type="select" 
										name="wfTaskQueryVo.mercyFlag" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">
							</label>
							<div class="formControls col-3">
							    <label>
								     <input name="wfTaskQueryVo.querySystem" type="checkbox" value="1">
								              旧理赔案子
								</label>
							</div>
						</div>
						<div class="line"></div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">流入时间</label>
							    <div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin"  
									name="wfTaskQueryVo.taskInTimeStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*"/>
								<span class="datespt">-</span><input type="text" class="Wdate" id="tiDateMax"
									name="wfTaskQueryVo.taskInTimeEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
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
								 		<input type="radio"  name="handleStatus" value="0" onchange="changeHandleStatus(this)" checked="checked">可处理
								 	<label>
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
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handing">&#xe619;</i>可处理</span> 
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
				</div>
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>案件紧急程度</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>历史重开次数</th>
								<th>险种名称</th>
								<th>被保险人</th>
								<th>结案日期</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
			    </div>
			    <div class="tabCon clearfix">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>案件紧急程度</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>历史重开次数</th>
								<th>险种名称</th>
								<th>被保险人</th>
								<th>结案日期</th>
								<th>处理状态</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
			    </div>
			    
			<!--标签页 结束-->
		</div>
	</div>
</div>
	<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
	<script type="text/javascript" src="${ctx }/js/flow/reOpenApp.js"></script>
	<script type="text/javascript">
	var columns0 = [
		       		{
		       			"data":"mercyFlagName"     //案件紧急程度
		       		},{
		       			"data" : "registNo"        //报案号
		       		}, {
		       			"data" : "policyNo"        //保单号
		       		},{
		       			"data" : "serialNo"     //历史重开次数
		       		}, {
		       			"data" : "riskCodeName"  //险种名称
		       		},{
		       			"data" : "insuredName"   //被保险人
		       		},{
		       			"data" : "endCaseTime",    //结案日期
		       			render : function(data, type, row) {
							return DateUtils.cutToDate(data);
						}
		       		}
		       	  ];
	var columns3 = [
		       		{
		       			"data":"mercyFlagName"     //案件紧急程度
		       		},{
		       			"data" : "registNo"        //报案号
		       		}, {
		       			"data" : "remark"        //保单号
		       		},{
		       			"data" : "seriesNo"     //历史重开次数
		       		}, {
		       			"data" : "flag"  //险种名称
		       		},{
		       			"data" : "insuredName"   //被保险人
		       		},{
		       			"data" : "endCaseDate",    //结案日期
		       			render : function(data, type, row) {
							return DateUtils.cutToDate(data);
						}
		       		},{
		       			"data" : "checkStatusName"   //处理状态
		       		}
		       	  ];
	
	function change(){
		if($("input[name='endCaseVo.registNo']").val().length >= 4){
			$("#tiDateMin").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
			$("#tiDateMax").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
			
		}else{
			$("#tiDateMin").attr("datatype","*");
			$("#tiDateMax").attr("datatype","*");
		}
	}
	
	</script>
</body>
</html>