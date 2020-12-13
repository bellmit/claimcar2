<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>公估费退票查询</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
					<input type="hidden" name="nodeCode" value="">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">业务号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="*4-24" ignore="ignore" errormsg="请输入4到22位数" name="prpLWfTaskQueryVo.bussNo" value="" />
							</div>
							<label class="form-label col-1 text-c">立案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="prpLWfTaskQueryVo.claimNo" value="" />
							</div>
							<label class="form-label col-1 text-c">公估机构</label>
							<div class="formControls col-3">
								<app:codeSelect codeType="" type="select" id="intermCode" name="prpLWfTaskQueryVo.intermCode" lableType="name" value="" dataSource="${dictVos}"/>
								<c:if test="${empty dictVos}"> <select  class="select "   value=""  dataType="selectMust"> </select> </c:if>
                         		<font color="red">*</font>
								
							</div>
						</div>
						<div class="row mb-3 cl">
						   <label class="form-label col-1 text-c">申请日期</label>
							<!-- 时间间隔要求为一个月 -->
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="prpLWfTaskQueryVo.taskInTimeStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*" />
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="prpLWfTaskQueryVo.taskInTimeEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*" /><font color="red">*</font>
								
							</div>
							
							
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-3">
							 	<label>
							 		<input type="radio" name="handleStatus" value="0" onchange="changeHandleStatus(this)" checked="checked">未处理
							 	</label>
							 	<label>
							    	<input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="3">已处理
							    </label>
							   <font color="red">*</font>	
						    </div>
							<label class="form-label col-1 text-c"></label>
							<div class="formControls col-3">
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
								<th>账户名称</th>
								<th>开户银行</th>
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
								<th>账户名称</th>
								<th>开户银行</th>
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
<%-- <script type="text/javascript" src="${ctx }/js/common/AjaxList.js"></script>  --%>
<script type="text/javascript">
var columns0 = [
      			{"data" : null},  //业务号
      			{"data" : "accountName"},//开户名称
      		    {"data" : "bankName"},//开户银行
      		    {"data" : "accountNo"}, //银行账户
      	        {"data" : "remark"},//修改原因
      	        {"data" : "appTime"},//申请日期
      	        {"data" : "payTypeName"}//收付原因
      	        
      			];

   var columns3 = [
      			{"data" :null}, //业务号
      			{"data" : "accountName"},//开户名称
      		    {"data" : "bankName"},//开户银行
      		    {"data" : "accountNo"}, //银行账户
      	        {"data" : "remark"},//修改原因
      	        {"data" : "appTime"},//申请日期
      	        {"data" : "payTypeName"} //收付原因
      	        
      			];
	       	  
function rowCallback(row, data, displayIndex, displayIndexFull) {
	$('td:eq(0)', row).html("<a  onclick=intermMoreMessage('"+data.bussNo+"','"+data.accountId+"','"+data.backaccountId+"','"+data.intermCode+"','"+data.registNo+"');>"+data.bussNo+"</a>");
}

function search(){
	var handleStatus=$("input[name='handleStatus']:checked").val();
	if(isBlank(handleStatus)){
		layer.msg("请选择任务状态");
		return false;
	}
	var ajaxList = new AjaxList("#DataTable_"+handleStatus);
	ajaxList.targetUrl ='/claimcar/assessors/assessorFeeAccountSearch.do';
	ajaxList.postData = $("#form").serializeJson();
	ajaxList.rowCallback = rowCallback;
	if(handleStatus=='0'){
		ajaxList.columns = columns0;
	}else if(handleStatus=='3'){
		ajaxList.columns = columns3;
	}
	ajaxList.query();
}

//本案件下，公估费费用查看yzy
var assessor_index;
function intermMoreMessage(bussNo,accountId,backaccountId,intermCode,registNo) {
	var url = "/claimcar/assessors/assessorMoreMessageList.do?bussNo="+bussNo+"&accountId="+accountId +"&backaccountId="+backaccountId+"&intermCode="+intermCode+"&registNo="+registNo;
	openTaskEditWin("理赔账户信息修改", url);
}

</script>
</body>
</html>