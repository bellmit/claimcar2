<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>强制立案失败查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<input type="hidden" name="nodeCode" value="${nodeCode }">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 报案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="registNo" value="" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" />
							</div>
							<label class="form-label col-1 text-c"> 报案时间 </label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="reportTimeMin" name="reportTimeStart"
									value="<fmt:formatDate value='${reportTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'reportTimeMax\')||\'%y-%M-%d\'}'})"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="reportTimeMax" name="reportTimeEnd"
									value="<fmt:formatDate value='${reportTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'reportTimeMin\')}',maxDate:'%y-%M-%d'})"/>
							</div>
							<label class="form-label col-1 text-c"> 创建时间 </label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="createTimeMin" name="createTimeStart"
									value="<fmt:formatDate value='${createTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'createTimeMax\')||\'%y-%M-%d\'}'})" datatype="*" />
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="createTimeMax" name="createTimeEnd"
									value="<fmt:formatDate value='${createTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'createTimeMin\')}',maxDate:'%y-%M-%d'})" datatype="*" />
								<font color="red">*</font>
							</div>
							<label class="form-label col-1 text-c"> 状态</label>
							<div class="formControls col-3">
								<select name="status" class="select">
									<option value=""></option>
									<option value="1">成功</option>
									<option value="0">失败</option>
								</select>
							</div>
						</div>
						<div class="line"></div>
						<div class="row cl text-c">
							<button class="btn btn-primary btn-outline btn-search" disabled type="submit">查询</button> 
						</div>
						<div class="row">
							<span class="col-1 text-c">
								<button class="btn btn-primary btn-outline"  type="button" onclick="cliamForce();">强制立案</button>
							</span>
							<br/>
						</div>
					</form>
				</div>
			</div>
			<!--案查询条件 结束-->
			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabCon clearfix">
					<table id="DataTable" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
						<thead>
							<tr class="text-c">
								<th><input type="checkbox" id="checkAll">选择</input></th>
								<th>报案号</th>
								<th>报案时间</th>
								<th>错误原因</th>
								<th>状态</th>
								<th>创建人</th>
								<th>创建时间</th>
								<th>修改人</th>
								<th>修改时间</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var columns = [ {
			"data" : null,
		},{
			"data" : "registNo"
		}, //报案号
		{
			"data" : "reportTime"
		}, //报案时间
		{
			"data" : null,
		}, //错误原因
		{
			"data" : null,
		}, //状态
		{
			"data" : "createUser"
		}, //创建人
		{
			"data" : "createTime"
		}, //创建时间
		{
			"data" : "updateUser"
		}, //更新人
		{
			"data" : "updateTime"
		}, //更新时间
		{
			"data" : null
		} 
		];
		$(function() {
			$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
			bindValidForm($('#form'),search);
			function search() {		
				var ajaxList = new AjaxList("#DataTable");
				ajaxList.targetUrl = '/claimcar/quartz/claimForceFailSearch.do';
				ajaxList.postData = $("#form").serializeJson();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			}
		});
		function rowCallback(row, data, displayIndex, displayIndexFull) {
			$('td:eq(0)',row).html("<input type='checkbox' value='"+data.id+"' name='checkCode'/> ");
			$('td:eq(3)',row).html("<span title='"+data.failReason+"'>"+data.exception+"</span> ");
			if(data.status!='1'){
				$('td:eq(4)',row).html("<span>失败</span>");	
				$('td:eq(9)',row).html("<button class='btn btn-primary btn-outline size-S'  type='button' onclick='cliamForce("+data.id+")'>强制立案</button> ");
			}else{
				$('td:eq(4)',row).html("<span>成功</span>");
				$('td:eq(9)',row).html("<button class='btn btn-default btn-outline size-S'  type='button' disabled='disabled'>强制立案</button> ");
			}
		}
		
		//强制立案
		function cliamForce(id){	
			var ids = null;
			if(isBlank(id)){
				ids = getSelectedIds();
				;
			}else{
				ids = id;
			}
			var params ={
					"ids" : ids
				}
			if(isBlank(ids)){
				layer.alert("请选择一个失败案件进行强制立案！");
				return false;
			}else{
				$.ajax({
					url : "/claimcar/quartz/claimForce.ajax",
					type : 'post', // 数据发送方式
					dataType : 'json', // 接受数据格式
					data : params, // 要传递的数据
					async : false,
					success : function(jsonData) {// 回调方法，可单独定义
					    var result = eval(jsonData);
						if (result.status == "200" ) {
							layer.alert("立案成功！");
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						layer.alert(XMLHttpRequest.responseText);
					}
				});
			}	
		}
	</script>
</body>
</html>