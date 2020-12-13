<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
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
								<input type="text" class="input-text"  name="prpLWfTaskQueryVo.registNo" 
								datatype="*20-24" errormsg="请输入准确报案号" nullmsg="请输入准确报案号" />
								<font class="c-red">*</font>
							</div>
							<label class="form-label col-1 text-c">保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" 
								ignore="ignore" errormsg="请输入4到22位数" name="prpLWfTaskQueryVo.policyNo" />
							</div>
							<label class="form-label col-1 text-c">车牌号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"   name="prpLWfTaskQueryVo.licenseNo" />
							</div>
						</div>
						<!-- <div class="line"></div> -->
						<div class="row mb-8 cl">
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
							<label class="form-label col-1 text-c">被保险人
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="prpLWfTaskQueryVo.insuredName" />
							</div>
						</div>
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
								<th>报案号</th>
								<th>保单号</th>
								<th>被保险人</th>
								<th>标的车牌号</th>
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
								<th>标的车牌号</th>
								<th>处理时间</th>
								<th>处理人</th>
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
<script type="text/javascript">
var columns0 = [
	       		{
	       			"data":"registNo"     //报案号
	       		},{
	       			"data" : "policyNo"     //保单号
	       		}, {
	       			"data" : "insuredName"  //被保险人
	       		},{
	       			"data" : "licenseNo"   //标的车牌号
	       		}
	       	  ];

var columns3 = [
	       		{
	       			"data":"registNo"     //报案号
	       		},{
	       			"data" : "policyNo"     //保单号
	       		}, {
	       			"data" : "insuredName"  //被保险人
	       		},{
	       			"data" : "licenseNo"   //标的车牌号
	       		}, {
	       			"data" : "operateTime"  //处理时间
	       		},{
	       			"data" : "operateUser"   //处理人
	       		}
	       	  ];

function rowCallback(row, data, displayIndex, displayIndexFull) {
	var handleStatus=$("input[name='handleStatus']:checked").val();
	$('td:eq(0)', row).html("<a onclick=updateVIN('"+data.registNo+"');>"+data.registNo+"</a>");
}

function search(){
	var handleStatus=$("input[name='handleStatus']:checked").val();
	if(isBlank(handleStatus)){
		layer.msg("请选择任务状态");
		return false;
	}
	var ajaxList = new AjaxList("#DataTable_"+handleStatus);
	ajaxList.targetUrl = '/claimcar/updateVIN/VINSearch.do';
	ajaxList.postData = $("#form").serializeJson();
	ajaxList.rowCallback = rowCallback;
	if(handleStatus=='0'){
		ajaxList.columns = columns0;
	}else if(handleStatus=='3'){
		ajaxList.columns = columns3;
	}
	ajaxList.query();
}

function updateVIN(registNo){
	var handleStatus=$("input[name='handleStatus']:checked").val();
	var url = "/claimcar/updateVIN/VINEdit.do?registNo="+registNo+"&handleStatus="+handleStatus;
	openTaskEditWin("VIN码信息修改",url);
}
</script>
</body>
</html>