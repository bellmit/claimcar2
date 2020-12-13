<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>查勘任务查询</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" class="form-horizontal" role="form" method="post">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">报案号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" id="registNo"
									name="wfTaskCheckQueryVo.registNo" value="" />
							</div>
							<label class="form-label col-1 text-c">保单号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" id="policyNo"
									name="wfTaskCheckQueryVo.policyNo"  value="" />
							</div>
							<label class="form-label col-1 text-c">车牌号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" id="licenNo"
									name="wfTaskCheckQueryVo.licenseNo" value="" datatype="carStrLicenseNo" ignore="ignore"/>
							</div>
						</div>
						<div class="row mb-3 cl">
						    <label class="form-label col-1 text-c">出险时间</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="dmgDateMin"
									name="wfTaskCheckQueryVo.damageStartTime" value="<fmt:formatDate value='${damageStartTime}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'dmgDateMax\')||\'%y-%M-%d\'}'})" />
								- <input type="text" class="Wdate" id="dmgDateMax"
									name="wfTaskCheckQueryVo.damageEndTime"  value="<fmt:formatDate value='${damageEndTime}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'dmgDateMin\')}',maxDate:'%y-%M-%d'})" />
							</div>
							<label class="form-label col-1 text-c">案件紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box"><app:codeSelect
										codeType="MercyFlag" type="select" id="mercyFlag"
										name="wfTaskCheckQueryVo.mercyFlag" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="ComCode" type="select" id="comCode" clazz="must"
										name="wfTaskCheckQueryVo.comCode" lableType="code-name"
										value="" />
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">流入时间</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin"
									name="wfTaskCheckQueryVo.taskInStartTime" value="<fmt:formatDate value='${taskInStartTime}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" />
								- <input type="text" class="Wdate" id="tiDateMax"
									name="wfTaskCheckQueryVo.taskInEndTime" value="<fmt:formatDate value='${taskInEndTime}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" />
							</div>
							<label class="form-label col-1 text-c">任务类型</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="CheckHandleType" type="select" id="subNodeCode"
										name="wfTaskCheckQueryVo.subNodeCode" lableType="code-name"
										value="" />
								</span>
							</div>
						</div>
						<div class="row mb-6 cl">
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-6">
								<div class="radio-box">
								    <input type="hidden" id="handleStatus" name="wfTaskCheckQueryVo.handleStatus">
									<input type="checkbox" id="notHandleFlagChk"/> <label for="zt1">未接收</label>
								</div>
								<div class="radio-box">
									<input type="checkbox" id="handlingFlagChk"/> <label for="zt1">正在处理</label>
								</div>
								<div class="radio-box">
									<input type="checkbox" id="handledFlagChk" > <label for="zt1">已处理</label>
								</div>
							</div>
						</div>
						<br />
						<div class="line"></div>
						<br />
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search"
									id="search" type="button" disabled>查询</button>
							</span><br />
						</div>
					</form>
				</div>
			</div>
			<!--案查询条件 结束-->


			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl">
					<span id="handun_num"><i class="Hui-iconfont handun">&#xe619;</i>未处理</span> <span id="handing_num"><i
						class="Hui-iconfont handing">&#xe619;</i>正在处理</span> <span id="handout_num"><i
						class="Hui-iconfont handout">&#xe619;</i>已处理</span><span id="handback_num"><i
						class="Hui-iconfont handback">&#xe619;</i>已退回</span>	
				</div>
				<!--未处理-->
				<div class="tabCon clearfix">
					<table id="notHandleDataTable"
						class="table table-border table-hover data-table" cellpadding="0"
						cellspacing="0" style="width:1108px;">
						<thead>
							<tr>
								<th>案件紧急程度</th>
								<th>客户等级</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>车牌号码</th>
								<th>被保险人</th>
								<th>承保机构</th>
								<th>出险时间</th>
								<th>流入时间</th>
								<th>提交人</th>
								<th>任务类型</th>
								<th>照片上传</th>
								<th>未处理原因</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="handlingDataTable"
						class="table table-border table-hover data-table" cellpadding="0"
						cellspacing="0" style="width:1108px;">
						<thead>
							<tr>
								<th>案件紧急程度</th>
								<th>客户等级</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>车牌号码</th>
								<th>被保险人</th>
								<th>承保机构</th>
								<th>出险时间</th>
								<th>流入时间</th>
								<th>提交人</th>
								<th>任务类型</th>
								<th>照片上传</th>
								<th>未处理原因</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>

				<!-- 已处理 -->
				<div class="tabCon clearfix">
					<table id="handledDataTable"
					class="table table-border table-hover data-table"
						cellpadding="0" cellspacing="0" style="width:1108px;">
						<thead>
							<tr>
								<th>案件紧急程度</th>
								<th>客户等级</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>车牌号码</th>
								<th>被保险人</th>
								<th>承保机构</th>
								<th>出险时间</th>
								<th>流入时间</th>
								<th>提交人</th>
								<th>任务类型</th>
								<th>照片上传</th>
								<th>未处理原因</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				
				<!-- 已退回 -->
				<div class="tabCon clearfix">
					<table id="backHandleDataTable"
					class="table table-border table-hover data-table"
						cellpadding="0" cellspacing="0" style="width:1108px;">
						<thead>
							<tr>
								<th>案件紧急程度</th>
								<th>客户等级</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>车牌号码</th>
								<th>被保险人</th>
								<th>承保机构</th>
								<th>出险时间</th>
								<th>流入时间</th>
								<th>提交人</th>
								<th>任务类型</th>
								<th>照片上传</th>
								<th>未处理原因</th>
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
		var columns = [ {
			"data" : "mercyFlag"
		}, {
			"data" : "customerLevel"
		}, {
			"data" : "registNo"
		}, {
			"data" : "policyNo"
		}, {
			"data" : "licenseNo"
		}, {
			"data" : "insuredName"
		}, {
			"data" : "comCode"
		}, {
			"data" : "damageTime"
		}, {
			"data" : "taskInTime"
		}, {
			"data" : "taskInUser"
		}, {
			"data" : "handleType"
		},{
			"data" : null
		},{
			"data" : null
		}];

		function rowCallback(row, data, displayIndex, displayIndexFull) {
			$('td:eq(2)', row).html(
					"<a target='_blank' href='javaScript:void(0)'>" + data.registNo + "</a>");
			$('td:eq(11)', row).html(
					"<a target='_blank' href='javaScript:void(0)'></a>");
			$('td:eq(12)', row).html(
					"<a target='_blank' href='javaScript:void(0)'></a>");
		}
		
		function ajaxSubmit(tableId){
			var ajaxList = new AjaxList("#"+tableId+"");
			ajaxList.targetUrl = '/claimcar/checkQuery/wfTaskCheckQuery.do';// + $("#form").serialize();
			ajaxList.columns = columns;
			ajaxList.rowCallback = rowCallback;
			ajaxList.query();
		}
		$(function() {
			$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon",
					"current", "click", "0");
			$("#search")
					.click(
							function() {
								if(!$("#notHandleFlagChk").is(':checked') && !$("#handlingFlagChk").is(':checked') && !$("#handledFlagChk").is(':checked')&& !$("#backHandleFlagChk").is(':checked')){
									layer.msg("请选择任务状态");
								}
								if ($("#notHandleFlagChk").is(':checked')) {
									 $("#handleStatus").val("notHandle");
                                     ajaxSubmit("notHandleDataTable");
				                }
								if ($("#handlingFlagChk").is(':checked')) {
									 $("#handleStatus").val("handling");
									 ajaxSubmit("handlingDataTable");
								}
								if ($("#handledFlagChk").is(':checked')) {
									 $("#handleStatus").val("handled");
									 ajaxSubmit("handledDataTable");
								}
								if ($("#backHandleFlagChk").is(':checked')) {
									 $("#handleStatus").val("backHandle");
									 ajaxSubmit("backHandleDataTable");
								}
			});
			
			var old = null; //用来保存原来的对象  
		    $(".mr-1").each(function(){//循环绑定事件  
		        if(this.checked){  
		            old = this; //如果当前对象选中，保存该对象  
		        }  
		        this.onclick = function(){  
		            if(this == old){//如果点击的对象原来是选中的，取消选中  
		                this.checked = false;  
		                old = null;  
		            } else{  
		                old = this;  
		            }  
		        }  
		    });  
		});
	</script>
</body>
</html>
