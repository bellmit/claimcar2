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
				<form id="form" class="form-horizontal" role="form" method="post">
					<!-- 隐藏域 -->
					<input type="hidden" class="input-text" name="prpLPayCustomVo.registNo"  value="${registNo }"/>
					<input id="lawsuitFlag" type="hidden" class="input-text" name="lawsuitFlag" value="${lawsuitFlag }"/>
					<input type="hidden" class="input-text" name="flag" value="${flag }"/>
					<input type="hidden" class="input-text" name="nodeCode" value="${nodeCode }"/>
					<!-- 隐藏域 -->
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label for=" " class="form_label col-2">账户名</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpLPayCustomVo.payeeName"/>
							</div>
							
							<label for=" " class="form_label col-3">账号</label>
							<div class="form_input col-3">
								<input id=" " type="text" class="input-text" name="prpLPayCustomVo.accountNo"/>
							</div>
							
						</div>
						<p>
						<div class="line"></div>
						<div class="row cl text-c">
							<button class="btn btn-primary btn-outline btn-search" id="search" type="button" disabled>查询</button>
							<button class="btn btn-primary btn-outline btn-search" id="searchUpdate" type="button" disabled>冻结查询</button>
<!-- 							<button class="btn btn-primary btn-outline btn-search" type="button" onclick="payCustomOpenEdit('')" disabled>增加</button>
 -->					</div>
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
		$('td:eq(3)', row).html("<a  onclick=\"payCustomOpenEdit('"+data.id+"')\">维护</a>");
	}	
	
	function rowCallbacks(row, data, displayIndex, displayIndexFull) {	
		$('td:eq(3)', row).html("<a  onclick=\"payCustomOpenEditFrostFlags('"+data.id+"')\">冻结</a>");
	}

	
	$(function(){
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");

		$("#search").click(
			function() {
			 	/* var payObjectKind = $("[name='prpLPayCustomVo.payObjectKind']").val();
				var payeeName = $("[name='prpLPayCustomVo.payeeName']").val();
				var accountNo = $("[name='prpLPayCustomVo.accountNo']").val();
				if(payObjectKind=='2' && payeeName=="" && accountNo==""){
					layer.msg("账户名和账号必须填写一项！");
					return false;
				} */
				
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = "/claimcar/payCustom/payCustomSearch.do";
				ajaxList.postData=$("#form").serializeJson();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList
				ajaxList.query();
			}
		);
		
		$("#searchUpdate").click(
				function() {
					var ajaxListUpdate = new AjaxList("#resultDataTable");
					ajaxListUpdate.targetUrl = "/claimcar/payCustom/payCustomSearchByRegistNo.do";
					ajaxListUpdate.postData=$("#form").serializeJson();
					ajaxListUpdate.columns = columns;
					ajaxListUpdate.rowCallback = rowCallbacks;
					ajaxListUpdate.query();
				}
			);
	});
	
	function payCustomOpenEdit(payId){
		var url = "/claimcar/lawFirm/payCustomEdit.do?payId=" + payId;
		var pIndex = null;
		if (pIndex == null) {
			pIndex = layer.open({
				type : 2,
				title : '收款人账户信息维护',
				shade : false,
				area : [ '100%', '100%' ],
				content : url,
				resize : true,
				end : function() {
					pIndex = null;
				}
			});
		}
	}
	
	function payCustomOpenEditFrostFlags(payId){
		var registNo = $("input[name='prpLPayCustomVo.registNo']").val();
		var flag = $("input[name='flag']").val();
		var nodeCode = $("input[name='nodeCode']").val();
		var lawsuitFlag = $("input[name='lawsuitFlag']").val();
		var url = "/claimcar/payCustom/payCustomEdit.do?registNo=" + registNo
		+ "&flag=" + flag + "&nodeCode=" + nodeCode + "" + "&payId="
		+ payId +"&lawsuitFlag="+lawsuitFlag + "&frostFlags=1" + "";
		var pIndex = null;
		if (pIndex == null) {
			pIndex = layer.open({
				type : 2,
				title : '收款人账户信息冻结',
				shade : false,
				area : [ '100%', '100%' ],
				content : url,
				resize : true,
				end : function() {
					pIndex = null;
				}
			});
		}
	}
	
	</script>
</body>
</html>
