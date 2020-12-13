<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>案件查询</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 报案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="prpLWfTaskQueryVo.registNo" value="" />
							</div>
							<label class="form-label col-2 text-c">报案日期</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="rpDateMin"
									name="prpLWfTaskQueryVo.reportTimeStart" value="<fmt:formatDate value='${reportTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'rpDateMax\')||\'%y-%M-%d\'}'})" datatype="*"/>
								- <input type="text" class="Wdate" id="rpDateMax"
									name="prpLWfTaskQueryVo.reportTimeEnd" value="<fmt:formatDate value='${reportTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'rpDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*"/>
									</div>
									<font color="red">*</font>
						</div>
						

						
						<div class="row cl text-c">
							<span class="col-offset-8 col-4">
								<button class="btn btn-primary btn-outline btn-search" disabled type="submit">
									<i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询
								</button>
							</span>
							</span>
						</div>
					</form>
				</div>
			</div>
			<br />
			<!-- 查询条件结束 -->

			<!--标签页 开始-->
			<div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabCon clearfix">
						<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
							<thead>
								<tr class="text-c">
									<th>报案号</th>
									<th>机构名称</th>
									<th>报案时间</th>
									<th>案件类型</th>
									<th>案件解锁</th>
								</tr>
							</thead>
							<tbody class="text-c">
							</tbody>
						</table>
						<!--table   结束-->
						<div class="row text-c">
							
						</div>
					</div>
				</div>
			</div>
			<!--标签页 结束-->
		</div>
    </div>
		<!-- 此处放页面数据 -->
		<script type="text/javascript" src="/claimcar/plugins/ajaxfileupload/ajaxfileupload.js"></script>
		<script type="text/javascript">
			$(function() {
				$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
				bindValidForm($('#form'), search);
				
				
			});

			var columns = [{
				"data" : "registNo"
			}, //报案号
			{
				"data" : "comCode"
			}, //机构
			{
				"data" : "reportTime"
			}, //报案时间
			{
				"data" : "remark"
			}, //案件类型
			{
				"data" : null
			}//案件解锁
			];

			function rowCallback(row, data, displayIndex, displayIndexFull) {
				var goUrl="/claimcar/regist/edit.do?registNo="+data.registNo;
				$('td:eq(0)', row).html("<a target='_blank'  onClick=openTaskEditWin('报案处理','"+goUrl+"')>" + data.registNo + "</a>");
				$('td:eq(4)',row).html("<a class='btn  btn-primary' onclick=caseUnlock('"+data.registNo+"');><font size='2'>案件解锁</font></a>");
				
				
			}

			function search() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = '/claimcar/systemFunction/search.do';
				ajaxList.postData = $("#form").serializeJson();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			}
			function caseUnlock(registNo){
				var params = {
						"registNo":registNo
				};
				$.ajax({
					url : "/claimcar/systemFunction/updateStatus.ajax",
					type : "post",
					data : params,
					async: false,
					success : function(jsonData){
						var result = eval(jsonData);
						if(result.data=="1"){
							layer.msg("解锁成功!");
						}else{
							layer.msg("解锁失败:"+result.statusText);
						}
						
					}
				});
		    }
		
			
		</script>
</body>
</html>