<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>理算冲销任务处理查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet"  />
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<input type="hidden" name="userCode" value="${user.userCode}" id="userCode"/>
						<input type="hidden" name="nodeCode" value="${nodeCode }">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="registNo" value="" datatype="n4-22" ignore="ignore" />
							</div>
							<label class="form-label col-1 text-c">  立案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="claimNo" value="" datatype="n4-22" ignore="ignore" />
							</div>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> 
								<app:codeSelect codeType="ComCode" type="select" name="comCode" lableType="code-name" />
								</span>
							</div>
						</div>
						
						<div class="row mb-3 cl">
						<label class="form-label col-1 text-c"> 车牌号码
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="licenseNo" datatype="carStrLicenseNo" errormsg="请输入正确的车牌号" ignore="ignore" />
							</div>
							
							<label class="form-label col-1 text-c">计算书号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="compensateNo" value=""  datatype="n4-24" ignore="ignore" />
							</div>
							<label class="form-label col-1 text-c">案件紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="MercyFlag" type="select" name="mercyFlag"
										lableType="code-name" />
								</span>
							</div>
							
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 被保险人名称
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="insuredName" value=""
									  ignore="ignore" />
							</div>
							<label class="form-label col-1 text-c">计算书类型
							</label>
							<div class="formControls col-3">
								<%-- <span class="select-box"> 
								<app:codeSelect codeType="CompKind" type="select" name="compensateKind"
										lableType="code-name" />
								</span> --%>
								 <span class="select-box"> 
								 <select  name='subNodeCode'  class='select'><option value='' ></option>
									 <option value='CompeWfCI'>理算(交强)</option>
									 <option value='CompeWfBI'>理算(商业)</option>
								 </select>
								</span>
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
							
							
						</div>
							<div class="row mb-6 cl">
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-6">
								<label><input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="2"  checked="checked">正在处理</label>
								<label><input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="3">已处理</label>
								<label><input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="6">已退回</label>
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
					<span onclick="changeHandleTab(2)"><i class="Hui-iconfont handing">&#xe619;</i>正在处理</span> 
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
					<span onclick="changeHandleTab(6)"><i class="Hui-iconfont handing">&#xe619;</i>已退回</span> 
				</div>
				<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_2" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>紧急程度</th>
								<th>计算书类型</th>
								<th>计算书号</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>立案号</th>
								<th>被保险人</th>
								<th>车牌号码</th>
								<th>承保机构</th>
								<th>流入时间</th>
								<th>处理人</th>
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
								<th>紧急程度</th>
								<th>计算书类型</th>
								<th>报案号</th>
								<th>计算书号</th>
								<th>保单号</th>
								<th>立案号</th>
								<th>被保险人</th>
								<th>车牌号码</th>
								<th>承保机构</th>
								<th>流入时间</th>
								<th>处理人</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!-- 已处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_6" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>紧急程度</th>
								<th>计算书类型</th>
								<th>报案号</th>
								<th>计算书号</th>
								<th>保单号</th>
								<th>立案号</th>
								<th>被保险人</th>
								<th>车牌号码</th>
								<th>承保机构</th>
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


		function rowCallback(row, data, displayIndex, displayIndexFull) {
			var handleStatus1=$("input[name='handleStatus']:checked").val();
			if(handleStatus1 !=2){
				if(data.compensateNo!=null){
					$('td:eq(3)',row).html("<a onclick=thisToCompenView('"+data.registNo+"','"+data.compensateNo+"');>"+data.compensateNo+"</a>");
				}
			}
			if(data.claimNo!=null){
				  $('td:eq(5)',row).html("<a onclick=prePayToclaimView('"+data.claimNo+"','"+data.registNo+"');>"+data.claimNo+"</a>");
			}
			
		}
	
		var columns = [
		   			{"data" : "bussTagHtml"}, //业务标识
		   			{"data" : "taskName"}, //计算书类型
		   			{"data" : "registNoHtml"}, //报案号
		   			{"data" : "compensateNo"}, //计算书号
		   	        {"data" : "policyNoHtml"}, //保单号
		   	    	{"data" : "claimNo"}, //立案号
		   	    	{"data" : "insuredName"}, //被保险人
		   	        {"data" : "licenseNo"}, //车牌号
		   	     	{"data" : "comCodePlyName"}, //承保机构
		   			{"data" : "taskInTime"}, //流入时间
		   			{"data" : "underwriteName"} //处理人员
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
					ajaxList.columns = columns;
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
