<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>结案查询</title>
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
						<input hidden="hidden" type="text" name="handleStatus" value="3">
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
							<label class="form-label col-1 text-c">  立案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="claimNo" value=""
									datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数"/>
							</div>
						</div>
						
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 被保险人名称
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="insuredName" value=""
									  />
							</div>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="ComCodeSelect" type="select" id="comCode" 
										name="comCode" lableType="code-name" />
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
				
				<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_2" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
							   <th>立案号</th>
							   <th>结案号</th>
								<th>保单号</th>
								<th>险种代码</th>
								<th>被保险人</th>
								<th>承保机构</th>
								<th>结案时间</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
				
				
			<!--标签页 结束-->
			
		</div>
	</div>
<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>

	<script type="text/javascript">


		function rowCallback(row, data, displayIndex, displayIndexFull) {
			if(data.policyNo!=null){
				$('td:eq(2)',row).html("<a href=/claimcar/policyView/policyView.do?registNo="+data.registNo+" target='_blank'>"+data.policyNo+"</a>");
			}
			
			
		}
		var columns = [
		   			{"data" : "claimNoHtml"}, //立案号
		   			{"data" : "taskOutKey"}, //结案号
		   	        {"data" : "policyNo"}, //保单号
		   	     	{"data" : "riskCodeName"}, //险种代码
		   	     	{"data" : "insuredName"}, //被保险人
		   			{"data" : "comCodeName"}, //承保机构
		   			{"data" : "taskInTime"} //结案时间11
		   			];
		function search(){
					var ajaxList = new AjaxList("#DataTable_2");
					ajaxList.targetUrl = 'search.do';// + $("#form").serialize();
					ajaxList.postData = $("#form").serializeJson();
					ajaxList.rowCallback = rowCallback;

					ajaxList.columns = columns;
					
					ajaxList.query();
			}
		
	</script>
</body>
</html>
