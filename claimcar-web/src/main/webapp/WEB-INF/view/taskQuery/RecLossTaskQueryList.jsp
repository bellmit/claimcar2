<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>损余回收任务查询</title>
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
					<input type="hidden"  name="queryRange"  value="1" >
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">
								报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数"  name="registNo" value="" />
							</div>
							<label class="form-label col-1 text-c">
								保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数"  name="policyNo"  value="" />
							</div>
							<label class="form-label col-1 text-c">
								车牌号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="carStrLicenseNo" ignore="ignore"   name="licenseNo" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
							  	<label class="form-label col-1 text-c">
								 车架号
								</label>
								<div class="formControls col-3">
									<input type="text" class="input-text" datatype="*4-17" ignore="ignore" name="frameNo" value="" />
								</div>
								<label class="form-label col-1 text-c">
									被保险人
								</label>
								<div class="formControls col-3">
									<input type="text" class="input-text"  name="insuredName"  value="" />
								</div>
								<label class="form-label col-1 text-c">归属机构</label>
								<div class="formControls col-3">
									<span class="select-box"> 
									 <app:codeSelect codeType="ComCodeSelect" type="select" id="comCode" 
											name="comCode" lableType="code-name" />
									</span>
								</div>
							</div>
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
							 <label class="form-label col-1 text-c">紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box">
								<app:codeSelect 	codeType="MercyFlag" type="select" 
										name="mercyFlag" lableType="code-name" />
								</span>
							</div>
							 <label class="form-label col-1">任务类型</label>
							<div class="formControls col-3">
								<span class="select-box"> 
									 <select name="subNodeCode" class=" select ">
									 <option value=""></option>
									 <option value="RecLossCar">车辆定损</option>
									<option value="RecLossProp">财产定损</option>
									</select>
								</span>
							</div>
						</div>
						<div class="row mb-6 cl">
							 <label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-6">
							 	<label>
							 		<input type="radio" name="handleStatus" value="0" onchange="changeHandleStatus(this)" checked="checked">可处理
							 	</label>
							 	<label>
							 	<input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="2">正在处理
							 	</label>
							    <label>
							    	<input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="3">已处理
							    </label>
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
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handun" >&#xe619;</i>可处理</span> 
				    <span onclick="changeHandleTab(2)"><i class="Hui-iconfont handing">&#xe619;</i>正在处理</span> 
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
				</div>
				<!--可处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
							    <th><input type = 'checkbox' id="checkAll">全选</th>
								<th>业务标识</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>险种名称</th>
								<th>被保险人</th>
								<th>流入时间</th>
								<th>处理人</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					<div class="text-c mt-20 mb-20">
						<input type="button" class="btn btn-primary" id="saveBtn"
						onClick="addTask()"  value="加入任务包" />
						<input type="button" class="btn btn-primary" id="saveBtn"
						onClick="handleTask()"  value="处理任务" />
						<input type="button" class="btn btn-primary" id="saveBtn"
						onClick="clearTask()"  value="清空任务" />
					</div>
				</div>
		    	<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_2" class="table table-border table-hover data-table" cellpadding="0" >
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>险种名称</th>
								<th>被保险人</th>
								<th>流入时间</th>
								<th>处理人</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!-- 已处理 -->
				<div class="tabCon clearfix">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>险种名称</th>
								<th>被保险人</th>
								<th>流入时间</th>
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
		$(function(){
			$("#checkAll").click(function() {
				$("input[name='taskId']").prop("checked", this.checked);
			});
		});
		var tasks = new Array();
		
		//加入任务包
		function addTask(){
			var checkedTask = $("input[name='taskId']:checked");
			if(checkedTask.length == 0){
				layer.alert("请选择任务");
				return false;
			}
			checkedTask.each(function(){
				var isExist = true;
				for(var i = 0; i < tasks.length;i ++){//不能重复加入任务包
					if(tasks[i] == $(this).val()){
						isExist = false;
						break;
					}
				}
				if(isExist){
					tasks.push($(this).val());
				}
			});
			layer.alert("已加入"+tasks.length+"个任务");
		}
		
		//处理任务
		function handleTask(){
			if(tasks.length == 0){
				layer.alert("请选择任务并加入任务包");
				return false;
			}
			openTaskEditWin("损余回收处理","/claimcar/recLoss/initFirst.do?tasks="+tasks);
		}
		
		//清空任务
		function clearTask(){
			tasks.splice(0,tasks.length);
			layer.alert("任务包已清空");
		}
		
		function checkIt(){
			var handlerIdKey = $("input[name='taskId']");
			$("#checkAll").prop("checked",
					handlerIdKey.length == $("input[name='taskId']:checked").length ? true : false);
		}
		
		var columns1 = [
            {"data" : "taskId"},
			{"data" : "bussTagHtml"}, //
			{"data" : "registNo"}, //报案号
			{"data" : "policyNoHtml"}, //保单号
			{"data" : "riskCodeName"}, //险种名称
			{"data" : "insuredName"}, //被保险人
			{"data" : "taskInTime"}, //流入时间
			{"data" : "assignUserName"} //处理人
			];
		
		var columns2 = [
      		{"data" : "bussTagHtml"}, //
   			{"data" : "registNoHtml"}, //报案号
   			{"data" : "policyNoHtml"}, //保单号
   			{"data" : "riskCodeName"}, //险种名称
   			{"data" : "insuredName"}, //被保险人
   			{"data" : "taskInTime"}, //流入时间
   			{"data" : "handlerUserName"} //处理人
      		];
   		
   		var columns3 = [
      		{"data" : "bussTagHtml"}, //
   			{"data" : "registNoHtml"}, //报案号
   			{"data" : "policyNoHtml"}, //保单号
   			{"data" : "riskCodeName"}, //险种名称
   			{"data" : "insuredName"}, //被保险人
   			{"data" : "taskInTime"}, //流入时间
   			{"data" : "taskOutUserName"} //处理人
      		];
   		
   		function rowCallback(row, data, displayIndex, displayIndexFull) {
   			
   			
		}
		function rowCallback1(row, data, displayIndex, displayIndexFull) {
			var taskId = data.taskId;
			$('td:eq(0)', row).html("<input type='checkbox' name='taskId' value='"+taskId+"' onclick='checkIt()'>");
			
			var handleStatus1=$("input[name='handleStatus']:checked").val();
   		    if(handleStatus1=='0'){
   				$('td:eq(2)',row).html("<a onclick=claimToRegistView('"+data.registNo+"');>"+data.registNo+"</a>");
   			}
		}
		
		function search(){
			$("#checkAll").prop("checked", false);
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
				ajaxList.columns = columns1;
				ajaxList.rowCallback = rowCallback1;
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
