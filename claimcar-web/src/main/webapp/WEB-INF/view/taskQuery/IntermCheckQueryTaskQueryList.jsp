<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>公估费审核查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet"  />

</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
					<%-- <input type="hidden"  name="qryVos.nodeCode[0]"  value="${nodeCode }" > --%>
					<input type="hidden"  name="nodeCode"  value="${nodeCode }" >
					<input type="hidden"  name="subNodeCode"  value="${subNodeCode }" >
				<%-- 	<input type="hidden"  name="subNodeCode"  value="${subNodeCode }" >  --%>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c">
							 	任务号
							</label>
							<div class="formControls col-4">
								<input type="text" class="input-text"  name="taskNo" value="" />
							</div>
							
							<label class="form-label col-2 text-c">
								报案号
							</label>
							<div class="formControls col-4">
								<input type="text" class="input-text"  name="registNo"  value="" />
							</div>
							
						</div>
						<div class="row mb-3 cl">
						 <label class="form-label col-2 text-c">公估机构</label>
							<div class="formControls col-4">
								<app:codeSelect codeType="" type="select" name="intermCode" lableType="name" value="" dataSource="${dictVos}"/>
								<c:if test="${empty dictVos}"> <select  class="select "   value=""  dataType="selectMust"> </select> </c:if>
								
                         		
							</div>
							<label class="form-label col-2 text-c">流入时间</label>
							<div class="formControls col-4">
								<input type="text" class="Wdate" id="tiDateMin"
									name="taskInTimeStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax"
									name="taskInTimeEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*"/>
								<font color="red">*</font>
							</div>
								
							
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c">任务状态</label>
							<div class="formControls col-4">
							 	<label>
							 		<input type="radio" name="handleStatus" value="0" onchange="changeHandleStatus(this)" checked="checked">可处理
							 	</label>
							 	<label>
							    	<input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="3">已处理
							    </label>
							   <font color="red">*</font>	
						    </div>
							<div class="formControls col-6"></div>
						</div>
						
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search"  type="submit" disabled>
								<i class="Hui-iconfont  Hui-iconfont-search2"></i>  查询</button>
							</span><br />
						</div>
					</form>
				</div>
			</div>
			<!--案查询条件 结束-->


			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl">
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handun" >&#xe619;</i>可处理</span> 
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
				</div>
				<!--可处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>任务号</th>
								<th>公估机构</th>
								<th>流入时间</th>
								<th>提交人</th>
							</tr>
						</thead>
						<tbody class="text-c">
						
						</tbody>
					</table>
				</div>
				
				<!-- 已处理 -->
				<div class="tabCon clearfix">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>任务号</th>
								<th>公估机构</th>
								<th>流入时间</th>
								<th>提交人</th>
								
							</tr>
						</thead>
						<tbody class="text-c">
						<!-- 代入数据放入此处 -->
						</tbody>
					</table>
				</div>
			</div>
			<!--标签页 结束-->
		</div>
	</div>
	<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
<script type="text/javascript">
$(function() {
	$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
	bindValidForm($('#form'),search);
});

var columns = [
   			{"data" : "registNo",
	         "orderable" : true,
	          "targets" : 0  
             }, //任务号
   		    {"data" : "intermCodeName"},//公估机构
   		    {"data" : "taskInTime"}, //流入时间
   	        {"data" : "taskInUserName"}//提交人
   			];

var columns1 = [
   			{"data" : "registNo",
	         "orderable" : true,
	          "targets" : 0  
             }, //任务号
   			{"data" : "intermCodeName"},//公估机构
   		    {"data" : "taskInTime"}, //流入时间
   	        {"data" : "taskInUserName"}//提交人
   			];
   			
function rowCallback(row, data, displayIndex, displayIndexFull) {	
	$('td:eq(0)', row).html("<a  onclick='IntermVeri("+data.taskId+");'>"+data.registNo+"</a>");
	
}	

function search(){
			var handleStatus=$("input[name='handleStatus']:checked").val();
			if(isBlank(handleStatus)){
				layer.msg("请选择任务状态");
				return false;
			}
			var ajaxList = new AjaxList("#DataTable_"+handleStatus);
			ajaxList.targetUrl = 'assessorFeeVeriTaskQuery.do';// + $("#form").serialize();
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.rowCallback = rowCallback;
            
			if(handleStatus == '0') {
				ajaxList.columns = columns;
			}else if(handleStatus == '3'){
				ajaxList.columns = columns1;
			}
			
			ajaxList.query();
	}
$("[name='registNo']").change(function(){
	if($("input[name='registNo']").val().length >= 4){
		$("[name='taskInTimeStart']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("[name='taskInTimeEnd']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		
	}else if($("input[name='registNo']").val().length == 0){
		$("[name='taskInTimeStart']").attr("datatype","*");
		$("[name='taskInTimeEnd']").attr("datatype","*");
	}
});


</script>
</body>
</html>
