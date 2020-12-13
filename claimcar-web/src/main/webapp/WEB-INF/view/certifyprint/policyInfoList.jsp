<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>原始保单及出险前批单打印</title>
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
								<input type="hidden" id="prpUrl" value="${prpUrl }" />
							</div>
							
							<label for=" " class="form_label col-2">保单号</label>
							<div class="form_input col-3">
								<input id=" " type="text" class="input-text" name="policyNo"/>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label for=" " class="form_label col-2">截止日期</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="taskInTimeStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*" />
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="taskInTimeEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*" />
								<font color="red">*</font>
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
									<th>保单号</th>
									<th>批单号</th>
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
		       			"data" : "registNo"
		       		 }, {
		       			"data" : "policyNo"
		       		 }, {
		       			"data" : "endorseNo"
		       		 },{
		       			"data" : null
		       		 }
		       	  ];
   
	function rowCallback(row, data, displayIndex, displayIndexFull) {	
		if(data.endorseNo !=null && data.endorseNo !=""){                                                                                                                                                                                
			$('td:eq(3)', row).html("<input type='button' class='btn btn-primary' value='保单打印'   onclick='pcertifyPrint(\""+data.policyNo+"\");'>&nbsp<input type='button' id='endorseNo' class='btn btn-primary' value='批单打印'   onclick='ecertifyPrint(\""+data.pingendorseNo+"\",\""+data.riskCode+"\");'>");
		}else{ 
			$('td:eq(3)', row).html("<input type='button' class='btn btn-primary' value='保单打印'   onclick='pcertifyPrint(\""+data.policyNo+"\");'>&nbsp<input type='button' id='endorseNo' class='btn btn-disabled' value='批单打印'>");	
		}
	}
	//保单打印
	function pcertifyPrint(policyNo){
		var prpUrl =$("#prpUrl").val();
		var url = prpUrl + "/prpc/copyPrintFormat?policyNo="+policyNo;
        openTaskEditWin("保单打印", url);
        
        
		
	}
	//批单打印
	function ecertifyPrint(pingendorseNo,riskCode){
		var urls = "?pingendorseNo=" + pingendorseNo+"&riskCode="+riskCode;

        index = layer.open({
	     type : 2,
	     title : "批单打印",
	     closeBtn : 1,
	     shadeClose : true,
	     scrollbar : false,
	     skin : 'yourclass',
	     area : [ '500px', '300px' ],
	     content : [ "/claimcar/certifyPrintSearch/pcertifyprint.do" + urls ]
});
	}
	
	
	 
	$(function(){
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");

		$("#search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = "/claimcar/certifyPrintSearch/originalPolicyListSearch.do";
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
