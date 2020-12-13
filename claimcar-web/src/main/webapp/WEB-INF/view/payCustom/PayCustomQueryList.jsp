<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>收款人查询</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont mb-10">
			<input id="lawsuitFlag" type="hidden" class="input-text" name="lawsuitFlag" value="${lawsuitFlag }"/>
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label for=" " class="form_label col-3">账户名</label>
							<div class="form_input col-2">
								<input type="text" class="input-text" name="prpLPayCustomVo.payeeName"/>
								<input id="registNo" type="hidden" class="input-text" name="prpLPayCustomVo.registNo" value="${registNo }"/>
							</div>
							
							<label for=" " class="form_label col-2">账号</label>
							<div class="form_input col-2">
								<input id=" " type="text" class="input-text" name="prpLPayCustomVo.accountNo"/>
							</div>
						</div>
						<p>
						<div class="line"></div>
						<div class="row cl text-c">
							<button class="btn btn-primary btn-outline btn-search" id="search" type="button" disabled>查询</button>
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
									<th>账户名</th>
									<th>账号</th>
									<th>开户行</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody class="text-c">
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

	var columns = [
		       		{
		       			"data" : "payeeName"
		       		}, {
		       			"data" : "accountNo"
		       		}, {
		       			"data" : "bankOutlets"
		       		},{
		       			"data" : null
		       		}
		       	  ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {	
		var lawsuitFlag = $("#lawsuitFlag").val();
		var registNo = $("[name='prpLPayCustomVo.registNo']").val();
		if(registNo == data.registNo){
			$('td:eq(3)', row).html("<a  onclick=\"payCustomOpenFind('N','"+data.id+"','"+lawsuitFlag+"')\">修改</a>");
		}else{
			$('td:eq(3)', row).html("<a  onclick=\"payCustomOpenFind('V','"+data.id+"','"+lawsuitFlag+"')\">查看</a>");
		}


	}		

	
	$(function(){
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");

		$("#search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = "/claimcar/payCustom/payCustomQuery.do";
				ajaxList.postData=$("#form").serializeJson();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			}
		);	
	});
	
	
	
	
	</script>
</body>
</html>
