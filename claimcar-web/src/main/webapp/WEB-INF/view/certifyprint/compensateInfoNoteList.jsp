<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>机动车辆保险赔款通知书\收据打印</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont mb-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row cl">
							<label for=" " class="form_label col-2 col-offset-3">赔款计算书号：</label>
							<div class="form_input col-3">
								<input id="compensateNo" type="text" class="input-text" name="compensateNo"/>
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
									<th>业务号</th>
									<th>赔案号</th>
									<th>保单号</th>
									<th>赔款金额</th>
									<th>直付例外</th>
									<th>收款人</th>
									<th>账户号</th>
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
	<!-- 	<div class="table_wrap text-c">			
			<button class="btn btn-primary btn-outline btn-search mt-20" onclick="modifiInterm()" type="button">修改</button>
		</div> -->
		
</div>
	<!--
	<script src="/js/policyQuery/CheckList.js"></script>
	-->
	<script type="text/javascript">
	var columns = [
		       		 {
		       			"data" : "compensateNo"
		       		 }, {
		       			"data" : "claimNo"
		       		 }, {
		       			"data" : "policyNo"
		       		 },{
		       			"data" : "sumRealPay"
		       		 },{
		       			"data" : "otherFlag"
		       		 },{
		       			"data" : "payeeName"
		       		 },{
		       			"data" : "accountNo"
		       		 },{
		       			"data" : "bankName"
		       		 },{
		       			"data" : null
		       		 }
		       	  ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {	
		$('td:eq(8)', row).html("<input type='button' class='btn btn-primary' value='打印'   onclick='certifyPrintp(\""+data.registNo+"\",\""+data.compensateNo+"\",\""+data.payeeId+"\");'>");
		                                                                                     
	}
	
function certifyPrintp(registNo1,compensateNo1,mainId){
		
		var registNo=registNo1;
		
		var compensateNo=compensateNo1;
		var index="G";
	    certifyPrintType(mainId,registNo,compensateNo,index);
  }
	
	/* function certifyPrint(registNo,compensateNo){
		var params = "?registNo=" + registNo+"&compensateNo="+compensateNo;
		var url = "/claimcar/certifyPrint/compensateInfoNote.ajax";
		var index = layer.open({
			type : 2,
			title : "赔款通知书/收据",
			maxmin : true, // 开启最大化最小化按钮
			content : url + params
		});
		layer.full(index);
	} */
	
	$(function(){
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");

		$("#search").click(function() {
			
			var compensateNo=$("#compensateNo").val();
			if(isBlank(compensateNo)){
				layer.msg("请输入赔款计算书号");
				return false;
			}
			     
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = "/claimcar/certifyPrintSearch/compensateInfoNoteListSearch.do";
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
