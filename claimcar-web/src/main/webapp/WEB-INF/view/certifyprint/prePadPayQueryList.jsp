<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>预付垫付计算书打印</title>
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
								<input id=" " type="text" class="input-text" name="registNo"/>
							</div>
							
							<label for=" " class="form_label col-2">赔款计算书号</label>
							<div class="form_input col-3">
								<input id=" " type="text" class="input-text" name="compensateNo"/>
							</div>
						</div>
						<div class="row mb-3 cl"> 
							<label for=" " class="form_label col-2">立案号</label>
							<div class="form_input col-3">
								<input id=" " type="text" class="input-text" name="claimNo"/>
							</div>
							<label for=" " class="form_label col-2">计算书类型</label>
							<div class="form_input col-3">
								<span class="select-box">
								<select id="" name="compeType" class="select" >
									<option value="0">预付计算书</option>
									<option value="1">垫付计算书</option>
								</select>
								</span>
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
									<th>报案号</th>
									<th>立案号</th>
									<th>计算书号</th>
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

	<script type="text/javascript">
	var columns = [
		       		 {
		       			"data" : "registNo"
		       		 }, {
		       			"data" : "claimNo"
		       		 }, {
		       			"data" : "compensateNo"
		       		 },{
		       			"data" : null
		       		 }
		       	  ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {	
		$('td:eq(3)', row).html("<input type='button' class='btn btn-primary' value='打印'   onclick='certifyPrintp(\""+data.registNo+"\",\""+data.compensateNo+"\");'>");
	}
  function certifyPrintp(registNo1,compensateNo1){
		
		var registNo=registNo1;
		var mainId="";
		var compensateNo=compensateNo1;
		var index="P";
	    certifyPrintType(mainId,registNo,compensateNo,index);
  }

	
	$(function(){
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");

		$("#search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = "/claimcar/certifyPrintSearch/prePadPayQueryListSearch.do";
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
