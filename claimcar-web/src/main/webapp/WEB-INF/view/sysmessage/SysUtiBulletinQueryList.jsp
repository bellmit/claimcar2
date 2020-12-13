<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>系统公告查询</title>
</head>
<body>
<div class="page_wrap" id="fu">

		<!--标签页 开始-->
			<div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabCon clearfix">
							<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
							<thead>
								<tr class="text-c">
									<th>序号</th>
									<th>公告内容</th>
									<th>发布时间</th>
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

	<script type="text/javascript">
	var index;
	var id;	
	var modifiId=0;
	var columns = [
		       		{
		       			"data" :"bulletinId",
		       			"orderable" : true,
		       			"targets" : 0
		       		}, {
		       			"data" : "title"
		       		}, {
		       			"data" : "inputTime"
		       		}
		       	  ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {	
		
	}		
	
	$(function(){
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
		search();
	});
	
	function search() {
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = "/claimcar/sysMsg/findMoreSysUtiBulletin.do";
		ajaxList.postData=$("#form").serializeJson();
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.query();
	}
	
	
	</script>
</body>
</html>
