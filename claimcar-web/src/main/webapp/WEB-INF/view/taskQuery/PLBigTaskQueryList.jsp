<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>人伤大案审核查询</title>
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
					<input type="hidden"  name="subNodeCode"  value="${subNodeCode}" >
						<input type="hidden"  name="queryRange"  value="1" >
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">
								报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="registNo" value="" />
							</div>
							<label class="form-label col-1 text-c">
								保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数"  name="policyNo"  value="" />
							</div>
							<label class="form-label col-1 text-c">
								标的车牌号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="carStrLicenseNo" ignore="ignore" name="licenseNo" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
						    <label class="form-label col-1 text-c">
								人员名称
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="personName" value="" />
							</div>
							<label class="form-label col-1 text-c">
								医院名称
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="hospitalName" value="" />
							</div>
							<label class="form-label col-1 text-c">
								被保险人名称
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="insuredName" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
						 <label class="form-label col-1 text-c">住院时间</label>
							<div class="formControls col-3">
							<!--  damageTimeStart-->
								<input type="text" class="Wdate" id="rgtDateMin"
									name="inHospitalDateStart" value="<fmt:formatDate value='${inHospitalDateStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'rgtDateMax\')||\'%y-%M-%d\'}'})" />
								<span class="datespt">-</span> <input type="text" class="Wdate" id="rgtDateMax"
									name="inHospitalDateEnd"  value="<fmt:formatDate value='${inHospitalDateEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'rgtDateMin\')}',maxDate:'%y-%M-%d'})" />
							</div> 
							<label class="form-label col-1 text-c">险种代码</label>
							<div class="formControls col-3">
								<span class="select-box"> 
								 <app:codeSelect codeType="RiskCode" type="select" id="riskCode" 
										name="riskCode" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box">
								<app:codeSelect 	codeType="MercyFlag" type="select" 
										name="mercyFlag" lableType="code-name" />
								</span>
							</div>
							
						</div>
						
						<div class="row mb-3 cl">
						 	<label class="form-label col-1 text-c">流入时间</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin"
									name="taskInTimeStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax"
									name="taskInTimeEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*"/>
						<font color="red">*</font>
							</div>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> 
								 <app:codeSelect codeType="ComCodeSelect" type="select" id="comCode" 
										name="comCodePly" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">是否调解</label>
							<div class="formControls col-3">
								<span class="select-box">
								<app:codeSelect 	codeType="YN01" type="select" 
										name="reconcileFlag" lableType="code-name" />
								</span>
							</div>
						</div>
						<!-- <div class="row mb-3 cl">
						<label class="form-label col-1 text-c">大案审核等级</label>
							<div class="formControls col-6">
								<label><input name="subNodeCode" type="radio" value="PLBig_LV1" />初一级大案审核 </label> 
								<label><input name="subNodeCode" type="radio" value="PLBig_LV2" />初二级大案审核 </label> 
								<label><input name="subNodeCode" type="radio" value="PLBig_LV3" />初三级大案审核 </label>
								<label><input name="subNodeCode" type="radio" value="PLBig_LV4" />初四级大案审核 </label>
								<label><input name="subNodeCode" type="radio" value="PLBig_LV5" />初五级大案审核 </label>
							</div>
						</div> -->
						<div class="row mb-8 cl">
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-4">
							 	<label>
							 		<input type="radio" name="handleStatus" value="0" onchange="changeHandleStatus(this)" checked="checked">未接收
							 	</label>
							 	<label>
							 	<input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="2">正在处理
							 	</label>
							    <label>
							    	<input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="3">已处理
							    </label>
							 	<font color="red">*</font>					
							</div>
							<div class="formControls  col-4">
							</div>
							<div class="formControls  col-2">
								<label ><input type="checkbox" name="includeLower" value="1">是否包含下级</label>
							</div>
						</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search"  type="submit">
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
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handun" >&#xe619;</i>未接收</span> 
					<span onclick="changeHandleTab(2)"><i class="Hui-iconfont handing">&#xe619;</i>正在处理</span> 
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
				</div>
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>业务标识</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>标的车车牌号码</th>
								<th>被保险人</th>
								<th>承保机构</th>
								<th>流入时间</th>
								<th>已报案天数</th>
								<th>是否现场调解</th>
								<th>提交人</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
				<div class="tabCon clearfix">
					<table id="DataTable_2" class="table table-border table-hover data-table" cellpadding="0" >
						<thead>
							<tr class="text-c">
							   <th>业务标识</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>标的车车牌号码</th>
								<th>被保险人</th>
								<th>承保机构</th>
								<th>流入时间</th>
								<th>已报案天数</th>
								<th>是否现场调解</th>
								<th>提交人</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>

				<div class="tabCon clearfix">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>业务标识</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>标的车车牌号码</th>
								<th>被保险人</th>
								<th>承保机构</th>
								<th>流入时间</th>
								<th>已报案天数</th>
								<th>是否现场调解</th>
								<th>提交人</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			<!--标签页 结束-->
		</div>
	</div>

	<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
	
	<script type="text/javascript">
	
		//----------未接收   已接收待处理--- start
		var columns1 = [
			{"data" : "bussTagHtml"}, //
			{"data" : "registNoHtml"}, //报案号
			{"data" : "policyNoHtml"}, //保单号
			{"data" : "licenseNo"}, //车牌号码
			{"data" : "insuredName"}, //被保险人
			{"data" : "comCodePlyName"}, //承保机构
			{"data" : "taskInTime"}, //流入时间
			{"data" : "reportDay"}, //已报案天数
			{"data" : "reconcileFlagName"}, //是否现场调解
			{"data" : "taskInUserName"}
			];
		
		function rowCallback(row, data, displayIndex, displayIndexFull) {
		}
		//----------未接收   已接收待处理--- end

		
		//----------已处理--- end
		
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
			if(handleStatus!='5'){
				ajaxList.columns = columns1;
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
