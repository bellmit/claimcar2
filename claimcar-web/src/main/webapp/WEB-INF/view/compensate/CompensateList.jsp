<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>理算任务发起查询</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont pd-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
					<div class="row mb-7 cl">
			     		<label class="form_label col-2">报案号</label>
						<div class="form_input col-2">
					        <input type="text" class="input-text" name="prpLClaimVo.registNo" />
						</div>
						<label class="form_label col-2">保单号</label>
						<div class="form_input col-2">
							<input type="text" class="input-text" name="prpLClaimVo.policyNo" />
						</div>
						<label class="form_label col-2">立案号</label>
						<div class="form_input col-2">
							<input type="text" class="input-text" name="prpLClaimVo.claimNo" />
						</div>									     		
						<label class="form_label col-2">险种代码</label>
						<div class="form_input col-2">
							<%-- <app:codeSelect codeType="KindCode" upperCode="Z" name="prpLClaimVo.riskCode"
											 type="select" /> --%>
							<app:codeSelect codeType="RiskCode" upperCode="Z" name="prpLClaimVo.riskCode"
											 type="select" lableType="code-name"/> 
						</div>
						<label class="form_label col-2">被保险人名称</label>
						<div class="form_input col-2">
							<input type="text" class="input-text" name="prpLClaimVo.insuredName" />
						</div>		     			     		
						<label class="form_label col-2">归属机构</label>
						<div class="form_input col-2">
							<app:codeSelect codeType="ComCode" name="prpLClaimVo.comcode"
											 type="select" />
						</div>
						<label class="form_label col-2">案件紧急程度</label>
						<div class="form_input col-2">
							<app:codeSelect codeType="MercyFlag" name="prpLClaimVo.mercyFlag"
											 type="select" />
						</div>
		     		</div>
		     		</div>
		     		
					<div class="line mt-10 mb-10"></div>
						<div class="row cl">
							<span class="col-12 text-c">
							   <button class="btn btn-primary btn-outline btn-search" id="search" type="button" onclick="rowCallback()" disabled>查询</button>
							</span>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!--案查询条件 结束-->

		<!--标签页 开始-->
			<div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabCon clearfix">
							<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
							<thead>
								<tr class="text-c">
									<th>业务标识</th>
									<th>报案号</th>
									<th>立案号</th>
									<th>保单号</th>
									<th>被保险人</th>
									<th>承保机构</th>
									<th>提交人</th>
								</tr>
							</thead>
							<class="text-c">
								<!-- 动态生成表格 -->
							</tbody>
						</table>
				</div>
			</div>
		</div>
		<!--标签页 结束-->
		
		
</div>
	<!--
	<script src="/js/policyQuery/CheckList.js"></script>
	-->
	<script type="text/javascript">
	var today=new Date();
    var columns = [
		       		{
		       			"data" : "bussTagHtml"
		       		}, {
		       			"data" : "registNo"
		       		}, {
		       			"data" : "claimNo"
		       		}, {
		       			"data" : "policyNo"
		       		},{
		       			"data" : "insuredName"
		       		},{
		       			"data" : "comCodeName"
		       		}
		       		,{
		       			"data" : "createUserName"
		       		}
		       	  ];
    function rowCallback(row, data, displayIndex, displayIndexFull) {		
    	var url = "/claimcar/compensate/compensateLaunchEdit.do?registNo=" + data.registNo+"";
		$('td:eq(1)', row).html("<a  onclick='openTaskEditWin(\"理算任务发起\",\""+url+"\");'>"+data.registNo+"</a>");
	}	

	
	
		
	$(function(){
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = "/claimcar/compensate/compensatefindList.do";
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		$("#search").click(function(){
			ajaxList.postData=$("#form").serializeJson();
			ajaxList.query();
		});
	});
	
	
	
	
	</script>
</body>
</html>
