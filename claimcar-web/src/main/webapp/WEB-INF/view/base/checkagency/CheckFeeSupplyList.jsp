<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>查勘费补录</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<input type="hidden" name="nodeCode" value="">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 工号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-10" ignore="ignore" errormsg="请输入4到10位数" name="queryVo.handlerCode" value="" />
							</div>
								
							<label class="form-label col-1 text-c">核损通过时间</label>
							<!-- 时间间隔要求为一个月 -->
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="queryVo.lossDateStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*" />
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="queryVo.lossDateEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*" />
								<font color="red">*</font>
							</div>
								<label class="form-label col-1 text-c">结案时间</label>
							<!-- 时间间隔要求为一个月 -->
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="diDateMin" name="queryVo.caseTimeStart" value="" onchange="validate()"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'diDateMax\')||\'%y-%M-%d\'}'})"  />
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="diDateMax" name="queryVo.caseTimeEnd" value="" onchange="validate()"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'diDateMin\')}',maxDate:'%y-%M-%d'})" />
							</div>
						</div>
						<div class="line"></div>
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
									<th style="width: 14%">报案号</th>
									<th>被保险人</th>
									<th style="width: 14%">保单号</th>
									<th style="width: 11%">核损通过时间</th>
									<th>任务详情</th>
									<th>处理人员</th>
									<th>操作</th>
									
								</tr>
							</thead>
							<tbody class="text-c">
							</tbody>
						</table>
						<!--table   结束-->
						<div class="row text-c">
							<br />
						</div>
					</div>
				</div>
			</div>
			<!--标签页 结束-->
		</div>
    </div>
    
		<script type="text/javascript" src="/claimcar/plugins/ajaxfileupload/ajaxfileupload.js"></script>
		<!-- 此处放页面数据 -->
		<script type="text/javascript">
			$(function() {
				$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
				$("#intermCode").attr("datatype","selectMust");
				$.Datatype.selectMust = function(gets, obj, curform, regxp) {
					var code = $(obj).val();
					if (isBlank(code)) {
						return false;
					}
				};
				bindValidForm($('#form'), search);
				
				
			});

			var columns = [{
				"data" : "registNo"
			}, //报案号
			{
				"data" : "insureName"
			}, //保单号
			{
				"data" : "policyNo"
			}, //核损通过时间
			{
				"data" : "lossDate"
			}, //被保险人
			{
				"data" : "taskDetail"
			} ,//备注
			{
				"data" : "userName" //处理人
			},
			{
				"data": null
			}
			];

			function rowCallback(row, data, displayIndex, displayIndexFull) {
				$('td:eq(0)',row).html("<a onclick=claimToRegistView('"+data.registNo+"');>"+data.registNo+"</a>");
				$('td:eq(2)',row).html("<a href=/claimcar/policyView/policyView.do?registNo="+data.registNo+" target='_blank'>"+data.policyNo+"</a>");
				$('td:eq(6)',row).html("<button class ='btn btn-primary btn-outline' onclick=supplment('"+ data.registNo +"'," + data.taskType + ","+ data.mainId+",'" + data.userName + "')>补录</button>");
			}
			function search() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = '/claimcar/checkfee/chFSupplymentSerach';
				ajaxList.postData = $("#form").serializeJson();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			}
			
			function validate(){
				if($("#diDateMax").val()==null||$("#diDateMax").val()==""){
					$("#diDateMin"). removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
				}else{
					$("#diDateMin").attr("datatype","*");
				}
				if($("#diDateMin").val()==null||$("#diDateMin").val()==""){
					$("#diDateMax"). removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
				}else{
					$("#diDateMax").attr("datatype","*");			
				}
			}
			function supplment(registNo,taskType,mainId,userCode){
				var url = "/claimcar/checkfee/supplementCheckFee";
				var param = {
						"mainId": mainId,
						"registNo": registNo,
						"taskType": taskType,
						"userCode": userCode
				};
				$.ajax({
					url : url, // 后台处理程序
					type : 'post', // 数据发送方式
					dataType : 'json', // 接受数据格式
					data : param, // 要传递的数据
					async : true,
					success : function(jsonData) {// 回调方法，可单独定义
						var text = jsonData.statusText;
						layer.alert(text);
						$("#resultDataTable").dataTable().fnDraw(false);
					},
					error : function(jsonData){
						var text = jsonData.statusText;
						layer.alert(text);
					}
				});
				
			}
		</script>
</body>
</html>