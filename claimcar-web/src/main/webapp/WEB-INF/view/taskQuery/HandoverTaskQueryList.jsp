<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>平级移交任务查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet"  />
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
					   <input type="hidden"  name="nodeCode"  value="${nodeCode}" >
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">
								报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" width="95%" datatype="n21-22"  errormsg="请输入完整报案号信息"  name="registNo" value="" />
							    <font class="must">*</font>
							</div>
							 <label class="form-label col-1">任务类型</label>
							<div class="formControls col-3">
								<span class="select-box"> 
								 <app:codeSelect codeType="HandoverTask" type="select" id="subNodeCode" 
										name="subNodeCode" lableType="name" />
								</span>
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
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>任务类型</th>
								<th>移交次数</th>
								<th>报案号</th>
								<th>损失方</th>
								<th>案件状态</th>
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
	var columns1 = [
			{"data" : "subNodeCodeName"},
			{"data" : "handoverTimes"},
			{"data" : "registNoHtml"},
			{"data" : "itemName"},
			{"data" : "handlerStatusName"},
			{"data" : "taskInTime"}, //流入时间
			{"data" : "assignUserName"} //处理人
			];
		
		function rowCallback(row, data, displayIndex, displayIndexFull) {
			var handlerStatusName = data.handlerStatusName;
			if(handlerStatusName == '正在处理'){
				$('td:eq(6)', row).html(data.handlerUserName);
			}
			
		}
		
		function search(){
			var ajaxList = new AjaxList("#DataTable_0");
			ajaxList.targetUrl = 'searchHandoverTask.do';// + $("#form").serialize();
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.rowCallback = rowCallback;
			ajaxList.columns = columns1;
			ajaxList.query();
		}
		
		function clearAssignUser(){
			$("#userCodeAjax").val(null);
			$(".select2-chosen").html(null);
		}
	</script>
</body>
</html>
