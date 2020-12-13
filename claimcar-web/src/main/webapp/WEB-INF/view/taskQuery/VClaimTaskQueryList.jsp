<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>核赔任务处理查询</title>
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
						<input type="hidden" name="userCode" value="${user.userCode}" id="userCode"/>
						<input type="hidden" name="nodeCode" value="${nodeCode }">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="registNo" value=""
									 datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数"/>
							</div>
							<label class="form-label col-1 text-c"> 保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="policyNo" value=""
									 datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数"/>
							</div>
							<label class="form-label col-1 text-c">险种代码</label>
							<div class="formControls col-3">
								<span class="select-box"> 
								 <app:codeSelect codeType="RiskCode" type="select" id="riskCode" 
										name="riskCode" lableType="code-name" />
								</span>
							</div>
						</div>
						
						<div class="row mb-3 cl">
						<label class="form-label col-1 text-c"> 客户等级
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="" value=""
									 datatype="*4-7" ignore="ignore" />
							</div>
							<label class="form-label col-1 text-c">  立案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="claimNo" value=""
									 datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数"/>
							</div>
							<label class="form-label col-1 text-c"> 任务类型
							</label>
							<div class="formControls col-3">
									<%-- <app:codeSelect codeType="FlowNode" type="select" id="taskInNode" 
										name="taskInNode" lableType="name"/> --%>
								<span class="select-box"> <select name="ywTaskType"
									class=" select ">
										<option value=''>全部</option>
										<option value='CompeBI'>理算(商业)</option>
										<option value='CompeCI'>理算(交强)</option>
										<option value='PrePayBI'>预付(商业)</option>
										<option value='PrePayCI'>预付(交强)</option>
										<option value='PadPay'>垫付</option>
										<option value='CancelAppJuPei'>拒赔</option>
										<option value='PrePayWfCI'>预付冲销(交强)</option>
										<option value='PrePayWfBI'>预付冲销(商业)</option>
										<option value='CompeWfCI'>理算冲销(交强)</option>
										<option value='CompeWfBI'>理算冲销(商业)</option>
								</select>
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="ComCodeSelect" type="select" id="comCode" 
										name="comCode" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">计算书号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="compensateNo" value=""/>
							</div>
							<label class="form-label col-1 text-c">流入时间</label>
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
							<%-- <label class="form-label col-1 text-c">案件紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="MercyFlag" type="select" name="mercyFlag"
										lableType="code-name" />
								</span>
							</div> --%>
						</div>
						<div class="row mb-3 cl">
						<label class="form-label col-1 text-c">车牌号码</label>
								<div class="formControls col-3">
									<input type="text" class="input-text" datatype="carStrLicenseNo" ignore="ignore" name="licenseNo" value="" />
								</div>
							
							<label class="form-label col-1 text-c">
								被保险人
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="insuredName" value="" />
							</div>
							<label class="form-label col-1 text-c">排序</label>
								<div class="formControls col-3">
									<span class="select-box"> 
										<select name="sorting" class="select" style="width:46%">
											<option value="0">流入时间</option>
											<option value="1">金额</option>
										</select>
										<select name="sortType"  class="select" style="width:46%">
											<option value="0">降序</option>
											<option value="1">升序</option>
										</select>
									</span>
								</div>
						</div>
							<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">理算金额</label>
							<div class="formControls col-3">
							<span class="select-box"> 
							<select name="compensateAmount" class="select" >
								<option value=''></option>
								<option value='1'>0-5000</option>
								<option value='2'>5000-10000</option>
								<option value='3'>10000以上</option>
							</select>
							</span>
							</div>
							
							<label class="form-label col-1 text-c">是否自动核赔</label>
							<div class="formControls col-3">
								<span class="select-box"> 
										<select name="autoType" class="select" >
										    <option value=''></option>
											<option value="0">否</option>
											<option value="1">是</option>
										</select>
								</span>
								</div>
							
							<label class="form-label col-1 text-c">案件状态</label>
							<div class="formControls col-3">
								<label><input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" checked="checked" value="0">未接收</label> 	
								<label><input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="2">正在处理</label>
								<label><input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="3">已处理</label>
								<font color="red">*</font>
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
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handback">&#xe619;</i>未接收</span>
					<span onclick="changeHandleTab(2)"><i class="Hui-iconfont handing">&#xe619;</i>正在处理</span> 
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
				</div>
				<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
							    <th>业务标识</th>
								<th>报案号</th>
								<th>业务号</th>
								<th>核赔等级</th>
								<th>被保险人</th>
								<th>车牌号码</th>
								<th>任务类型</th>
								<th>承保机构</th>
								<th>流入时间</th>
								<th>提交人</th>
								<th>理算金额</th>
								<th>未处理原因</th>
								
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
								<th>业务标识</th>
								<th>报案号</th>
								<th>业务号</th>
								<th>核赔等级</th>
								<th>被保险人</th>
								<th>车牌号码</th>
								<th>任务类型</th>
								<th>承保机构</th>
								<th>流入时间</th>
								<th>理算金额</th>
								<th>提交人</th>
								
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
				<!--注销-->
				<div class="tabCon clearfix">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" >
						<thead>
							<tr class="text-c">
								<th>业务标识</th>
								<th>报案号</th>
								<th>业务号</th>
								<th>核赔等级</th>
								<th>被保险人</th>
								<th>车牌号码</th>
								<th>任务类型</th>
								<th>承保机构</th>
								<th>流入时间</th>
								<th>理算金额</th>
								<th>提交人</th>
								<th>是否自动核赔</th>
								
							</tr>
						</thead>
						<tbody class="text-c">
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
			var handleStatus=$("input[name='handleStatus']:checked").val();
			if(handleStatus=='3'){
				if(data.assignUser=='AutoVClaim'){
					$('td:eq(11)',row).html("是");
				}else{
					$('td:eq(11)',row).html("否");
				}
				
			}
			if(data.taskInNode=='CancelAppJuPei'){
				//$('td:eq(2)', row).html(data.claimNo);
				if(data.claimNo!=null){
					  $('td:eq(2)',row).html("<a onclick=prePayToclaimView('"+data.claimNo+"','"+data.registNo+"');>"+data.claimNo+"</a>");
				}
			}else{
				//$('td:eq(2)', row).html(data.handlerIdKey);
				if(data.handlerIdKey!=null){
					$('td:eq(2)', row).html("<a onclick=thisToCompenView('"+data.registNo+"','"+data.handlerIdKey+"');>"+data.handlerIdKey+"</a>");
				}
			}
			
			
		}
		
		var columns = [
		   			{"data" : "bussTagHtml"}, //业务标识
		   	        {"data" : "registNoHtml"}, //报案号
		   	     	{"data" : null}, //业务号
		   	     	{"data" : "taskName"},//核赔级别
		   			{"data" : "insuredName"}, //被保险人
		   			{"data" : "licenseNo"}, //车牌号码
		   			{"data" : "ywTaskTypeName"}, //任务类型
		   			{"data" : "comCodePlyName"}, //承保机构
		   			{"data" : "taskInTime"}, //流入时间
		   			{"data" : "taskInUserName"}, //提交人
		   			{"data" : "compensateAmount"},//理算金额
		   			{"data" : "unHandleBtn"} //未处理原因
		   			
		   			];
		
		
		var columns1 = [
			   		{"data" : "bussTagHtml"}, //业务标识
		   	        {"data" : "registNoHtml"}, //报案号
		   	     	{"data" : null}, //业务号
		   	     	{"data" : "taskName"},//核赔级别
		   			{"data" : "insuredName"}, //被保险人
		   			{"data" : "licenseNo"}, //车牌号码
		   			{"data" : "ywTaskTypeName"}, //任务类型
		   			{"data" : "comCodePlyName"}, //承保机构
		   			{"data" : "taskInTime"}, //流入时间
		   			{"data" : "compensateAmount"}, //理算金额
		   			{"data" : "taskInUserName"} //提交人
		   		
			   			];
		
		var columns2 = [
				   		{"data" : "bussTagHtml"}, //业务标识
			   	        {"data" : "registNoHtml"}, //报案号
			   	     	{"data" : null}, //业务号
			   	     	{"data" : "taskName"},//核赔级别
			   			{"data" : "insuredName"}, //被保险人
			   			{"data" : "licenseNo"}, //车牌号码
			   			{"data" : "ywTaskTypeName"}, //任务类型
			   			{"data" : "comCodePlyName"}, //承保机构
			   			{"data" : "taskInTime"}, //流入时间
			   			{"data" : "compensateAmount"}, //理算金额
			   			{"data" : "taskInUserName"}, //提交人
			   			{"data" : null} //是否自动核赔
				   			];
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

			if (handleStatus=='0') {
				ajaxList.columns = columns;
			} else if(handleStatus=='2'){
				ajaxList.columns = columns1;
			}else{
				ajaxList.columns = columns2;
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
