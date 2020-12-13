<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>自动理算账号修改查询</title>
</head>
<body>
<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">业务号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="prpLWfTaskQueryVo.compensateNo" 
								datatype="*4-24" ignore="ignore" errormsg="请输入4到24位数"  />
							</div>
							<label class="form-label col-1 text-c">立案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" 
								ignore="ignore" errormsg="请输入4到22位数" name="prpLWfTaskQueryVo.claimNo" />
							</div>
							<label class="form-label col-1 text-c">被保险人
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"   name="prpLWfTaskQueryVo.insuredName" />
							</div>
						</div>
						<!-- <div class="line"></div> -->
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">申请日期</label>
							    <div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin"  
									name="prpLWfTaskQueryVo.taskInTimeStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*"/>
								<span class="datespt">-</span><input type="text" class="Wdate" id="tiDateMax"
									name="prpLWfTaskQueryVo.taskInTimeEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*"/>
								<font color="red">*</font>
							</div>
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-3">
								<div class="radio-box">
								 	<label><!-- recPayTaskFlag -->
								 		<input type="radio"  name="handleStatus" value="0" onchange="changeHandleStatus(this)" checked="checked">未处理
								 	<label>
								 </div>
								 <div class="radio-box">
									<label>
										<input type="radio"  name="handleStatus" onchange="changeHandleStatus(this)" value="3">已处理
									</label>
								 </div>
								 			
							</div>
						</div>
						<!-- <div class="line"></div> -->
						<div class="row mb-8 cl">
							
						</div>
						<input type="hidden" name="prpLWfTaskQueryVo.includeLower" value="${isVerify}" />
						
						<div class="line"></div>
						<div class="row">
								<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search"  disabled type="submit">
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
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handing">&#xe619;</i>未处理</span> 
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
				</div>
				<div class="tabCon clearfix table_count table_list">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>业务号</th>
								<th>开户银行</th>
								<th>开户人</th>
								<th>银行账户</th>
								<th>修改原因</th>
								<th>申请日期</th>
								<th>收付原因</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
			    </div>
			    <div class="tabCon clearfix table_count table_list">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>业务号</th>
								<th>开户银行</th>
								<th>开户人</th>
								<th>银行账户</th>
								<th>修改原因</th>
								<th>申请日期</th>
								<th>收付原因</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
			    </div>
			    
			<!--标签页 结束-->
		</div>
	</div>
</div>
<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
<%-- <script type="text/javascript" src="${ctx }/js/common/AjaxList.js"></script> --%>
<script type="text/javascript">
var columns0 = [
	       		{
	       			"data":"compensateNo"    //计算书号
	       		},{
	       			"data" : "bankNameName"  //开户银行
	       		}, {
	       			"data" : "accountName"   //开户人
	       		},{
	       			"data" : "accountNo"     //银行账户
	       		},{
	       			"data" : "errorMessage"  //修改原因
	       		},{
	       			"data" : "appTime"       //申请日期
	       		},{
	       			"data" : "payTypeName"   //收付原因
	       		}
	       	  ];
var columns3 = [
	       		{
	       			"data":"compensateNo"     //计算书号
	       		},{
	       			"data" : "bankNameName"   //开户银行
	       		}, {
	       			"data" : "accountName"    //开户人
	       		},{
	       			"data" : "accountNo"      //银行账户
	       		},{
	       			"data" : "errorMessage"   //修改原因
	       		},{
	       			"data" : "appTime"        //申请日期
	       		},{
	       			"data" : "payTypeName"    //收付原因
	       		}
	       	  ];
	       	  
function rowCallback(row, data, displayIndex, displayIndexFull) {
	var handleStatus=$("input[name='handleStatus']:checked").val();
	var isVerify = $("input[name='prpLWfTaskQueryVo.includeLower']").val();
	if(isVerify != "1" && handleStatus=='0'){
		$('td:eq(0)', row).html("<a onclick=accountInfo('"+data.compensateNo+"','"+data.registNo+"','"+data.flag+"','"+data.chargeCode+"','"+data.payType+"','"+data.accountId+"');>"+data.compensateNo+"</a>");
	}else{
		$('td:eq(0)', row).html("<a onclick=accountInfo('"+data.compensateNo+"','"+data.registNo+"','"+data.payeeId+"','"+data.chargeCode+"','"+data.payType+"','"+data.accountId+"');>"+data.compensateNo+"</a>");
	}
}

function search(){
	var handleStatus=$("input[name='handleStatus']:checked").val();
	if(isBlank(handleStatus)){
		layer.msg("请选择任务状态");
		return false;
	}
	var ajaxList = new AjaxList("#DataTable_"+handleStatus);
	ajaxList.targetUrl = '/claimcar/accountQueryAction/returnTticketInfoSearch.do';
	ajaxList.postData = $("#form").serializeJson();
	ajaxList.rowCallback = rowCallback;
	if(handleStatus=='0'){
		ajaxList.columns = columns0;
	}else if(handleStatus=='3'){
		ajaxList.columns = columns3;
	}
	ajaxList.query();
}

function accountInfo(compensateNo,registNo,payeeId,chargeCode,payType,oldAccountId){
	var taskName = "理赔账户信息修改处理";
	if($("input[name='prpLWfTaskQueryVo.includeLower']").val()=="1"){
		taskName = "账号修改审核处理";
	}
	var handleStatus=$("input[name='handleStatus']:checked").val();
	var isVerify = $("input[name='prpLWfTaskQueryVo.includeLower']").val();
	if(isBlank(isVerify)){
		isVerify = "0";
	}
	var url = "/claimcar/accountInfo/accountInfoInit.do?compensateNo="+compensateNo+"&handleStatus="
			+handleStatus+"&registNo="+registNo+"&payeeId="+payeeId+"&isVerify="+isVerify+"&payType="+payType+"&oldAccountId="+oldAccountId;
	openTaskEditWin(taskName,url);
}
</script>
</body>
</html>