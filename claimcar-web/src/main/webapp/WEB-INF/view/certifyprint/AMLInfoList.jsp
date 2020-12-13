<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>反洗钱信息打印</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont mb-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label for=" " class="form_label col-2">报案号</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="registNo" value="" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" />
							</div>
							
							<label for=" " class="form_label col-2">收款人姓名</label>
							<div class="form_input col-3">
								<input id=" " type="text" class="input-text" name="payeeName"/>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label for=" " class="form_label col-2">收款人账号</label>
							<div class="form_input col-3">
								<input id=" " type="text" class="input-text" name="accountNo"/>
							</div>
						</div>
						<p>
						<div class="line"></div>
						<div class="row cl text-c">
							   <button class="btn btn-primary btn-outline btn-search" id="search" type="submit" disabled>
							   <i class="Hui-iconfont  Hui-iconfont-search2"></i>查询</button>
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
									<th>报案号</th>
									<th>收款人姓名</th>
									<th>收款人帐号</th>
									<th>立案号</th>
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
</div>
	<!--
	<script src="/js/policyQuery/CheckList.js"></script>
	-->
	<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
	<script type="text/javascript">
	
	var columns = [
		       		 {
		       			"data" : "registNo"
		       		 }, {
		       			"data" : "payeeName"
		       		 }, {
		       			"data" : "accountNo"
		       		 },{
		       			"data" : "claimNo"
		       		 },{
		       			"data" : null
		       		 }
		       	  ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {	
		$('td:eq(4)', row).html("<input type='button' class='btn btn-primary' value='打印'   onclick='certifyPrintp(\""+data.id+"\",\""+data.registNo+"\",\""+data.claimNo+"\");'>");
	}
	function certifyPrintp(mainId1,registNo1,claimNo){
		var mainId=mainId1;
		var registNo=registNo1;
		var compensateNo=claimNo;
		var index="H";
		certifyPrintType(mainId,registNo,compensateNo,index);
	}
	
	function search(){
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = "/claimcar/certifyPrintSearch/AMLInfoListSearch.do";
		ajaxList.postData=$("#form").serializeJson();
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.query();
	}
		
		
	
	
	
	</script>
</body>
</html>
