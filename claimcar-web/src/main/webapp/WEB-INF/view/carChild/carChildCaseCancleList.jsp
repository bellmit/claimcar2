<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>案件注销审核查询</title>
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
							<label class="form-label col-1 text-c">报案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="*4-24" ignore="ignore" errormsg="请输入4到22位数" name="prpLWfTaskQueryVo.registNo" value="" />
							</div>
							<label class="form-label col-1 text-c">保单号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="prpLWfTaskQueryVo.policyNo" value="" />
							</div>
							<label class="form-label col-1 text-c">车牌号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="prpLWfTaskQueryVo.licenseNo" value=""
									 datatype="carStrLicenseNo" ignore="ignore" />
								
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
							<label class="form-label col-1 text-c">申请人 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="prpLWfTaskQueryVo.reporterName" value="" ignore="ignore" />
							</div>
							
							<label class="form-label col-1 text-c">被保险人</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="prpLWfTaskQueryVo.insuredName" value="" ignore="ignore" />
							</div>
							</div>
							<div class="row mb-3 cl">
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
								<th>报案号</th>
								<th>保单号</th>
								<th>被保险人</th>
								<th>车牌号</th>
								<th>申请时间</th>
								<th>申请人</th>
								<th>注销原因</th>
								<th>操作</th>
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
								<th>报案号</th>
								<th>保单号</th>
								<th>被保险人</th>
								<th>车牌号</th>
								<th>申请时间</th>
								<th>申请人</th>
								<th>注销原因</th>
								<th>处理时间</th>
								<th>处理人</th>
								<th>处理结果</th>
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
      			{"data" : "registNoHtml"},  //报案号
      			{"data" : "policyNoHtml"},//保单号
      		    {"data" : "flagLog"},//被保险人
      		    {"data" : "licenseNo"}, //车牌号
      	        {"data" : "cancleDate"},//申请时间
      	        {"data" : "userCode"},//申请人
      	        {"data" : "reason"},//注销原因
      	        {"data" : null}//操作
      	        
      			];

   var columns3 = [
      			{"data" : "registNoHtml"}, //报案号
      			{"data" : "policyNoHtml"},//保单号
      		    {"data" : "flagLog"},//被保险人
      		    {"data" : "licenseNo"}, //车牌号
      	        {"data" : "cancleDate"},//申请时间
      	        {"data" : "userCode"},//申请人
      	        {"data" : "reason"}, //注销原因
      	        {"data" : "handleDate"}, //处理时间
      	        {"data" : "handleUser"}, //处理人
      	        {"data" : "examineRusult"}//处理结果
      			];
	       	  
function rowCallback(row, data, displayIndex, displayIndexFull) {
	var handleStatus=$("input[name='handleStatus']:checked").val();
	if(handleStatus=='0'){
		$('td:eq(7)', row).html("<a  class='btn btn-primary' onclick=caseCancleNopass('"+data.id+"');>审核不通过</a>");	
	}
	
	
}                                                            

function search(){
	var handleStatus=$("input[name='handleStatus']:checked").val();
	if(isBlank(handleStatus)){
		layer.msg("请选择任务状态");
		return false;
	}
	var ajaxList = new AjaxList("#DataTable_"+handleStatus);
	ajaxList.targetUrl ='/claimcar/carchild/carchildCaseCancleSearch.do';
	ajaxList.postData = $("#form").serializeJson();
	ajaxList.rowCallback = rowCallback;
	if(handleStatus=='0'){
		ajaxList.columns = columns0;
	}else if(handleStatus=='3'){
		ajaxList.columns = columns3;
	}
	ajaxList.query();
}

function caseCancleNopass(id){
	var params = {
			"id":id
		};
	$.ajax({
		url : "/claimcar/carchild/caseCancleNopass.do",
		type : "post",
		data : params,
		async: false,
		success : function(jsonData){
			var instars=eval(jsonData).status;
			var sign=eval(jsonData).data;
			if(instars=='200'){
				if(sign=='1'){
					layer.confirm("操作成功！",{
						btn:['确认']
					},function(){
						window.location.reload();
					});
					
				}else{
					layer.confirm("操作失败，请到接口平台进行补传！",{
						btn:['确认']
					},function(){
						window.location.reload();
					});
					
				}
				
			}
		}
	});
}


</script>
</body>
</html>