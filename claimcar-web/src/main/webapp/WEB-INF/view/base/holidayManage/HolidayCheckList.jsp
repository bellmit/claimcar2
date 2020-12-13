<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>休假审核查询</title>
</head>
<body>
     <div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
					    <!-- 隐藏域 -->
					        <input type="hidden" name="flag_add" value="0"/>
		                    <input type="hidden" name="flag_dispose" value="1"/>
		                <!-- 隐藏域 -->
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">员工工号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="prpLUserHolidayVo.userCode" 
								datatype="n4-10" ignore="ignore" errormsg="请输入4到10位数" />
							</div>
							<label class="form-label col-1 text-c">员工姓名</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="prpLUserHolidayVo.userName" 
								value="${userName }" />
							</div>
							
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
									<app:codeSelect codeType="ComCode" name="prpLUserHolidayVo.comCode" 
											 type="select"  />
								</span>
							</div>
							
						</div>
						<div class="line"></div>
						<div class="row mb-3 cl">
						    <label class="form-label col-1 text-c">流入时间</label>
							    <div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin"
									name="timeStart" value="<fmt:formatDate value='${timeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*"/>
								<span class="datespt">-</span><input type="text" class="Wdate" id="tiDateMax"
									name="timeEnd" value="<fmt:formatDate value='${timeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*"/>
								<font color="red">*</font>
							</div>
						</div>
						<div class="line"></div>
						<div class="row mb-8 cl">
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-5">
								<div class="radio-box">
								 	<label><!-- recPayTaskFlag -->
								 		<input type="radio"  name="handleStatus" value="0" onchange="changeHandleStatus(this)" checked="checked">可处理
								 	<label>
								 </div>
								 <div class="radio-box">
									<label>
								 	 	<input type="radio"  name="handleStatus" onchange="changeHandleStatus(this)" value="3">已处理
								 	</label> 
								 </div>	
								 <div class="radio-box">
									<label>
								 	 	<input type="radio"  name="handleStatus" onchange="changeHandleStatus(this)" value="9">已撤销
								 	</label> 
								 </div>			
							</div>
						</div>
						<div class="line"></div>
						<div class="row cl text-c" >
							
								<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search" disabled type="submit">
								<i class="Hui-iconfont  Hui-iconfont-search2"></i>查询</button>
							</span>
						</div>
					</form>
				</div>
			</div>
		<!--标签页 开始-->
	    <div id="tab-system" class="HuiTab">
				<div class="tabBar cl"> 
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handing">&#xe619;</i>可处理</span> 
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
					<span onclick="changeHandleTab(9)"><i class="Hui-iconfont handout">&#xe619;</i>已撤销</span>
				</div>
				<!--可处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>员工工号</th>
								<th>员工姓名</th>
								<th>归属机构</th>
								<th>岗位</th>
								<th>手机号码</th>
								<th>审核状态</th>
								<th>处理人</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
				
				<!--已处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
						<thead>
							
								<tr class="text-c">
								<th>员工工号</th>
								<th>员工姓名</th>
								<th>归属机构</th>
								<th>岗位</th>
								<th>手机号码</th>
								<th>审核状态</th>
								<th>处理人</th>
								<th>操作</th>
							</tr>
							
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
				
				<!--已撤销-->
				<div class="tabCon clearfix">
					<table id="DataTable_9" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
						<thead>
							
								<tr class="text-c">
								<th>员工工号</th>
								<th>员工姓名</th>
								<th>归属机构</th>
								<th>岗位</th>
								<th>手机号码</th>
								<th>审核状态</th>
								<th>撤销时间</th>
								<th>操作</th>
							</tr>
							
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
			</div>
	<!--标签页 结束-->
    </div>
</div>
    <script type="text/javascript" src="/claimcar/js/common/AjaxList.js"></script> 
    <script type="text/javascript" src="/claimcar/js/flow/flowCommon.js"></script>
    <script type="text/javascript">
    var today=new Date();
   // var status;
    /*$("input[name='handleStatus']").each(function(){
		if($(this).prop("checked")==true){
			status=$("this").val();
		}
	});*/
    var columns0 = [
		       		{
		       			"data" : "userCode",
		       				"orderable" : true,
			       			"targets" : 0
		       		}, {
		       			"data" : "userName"
		       		}, {
		       			"data" : "comCode"
		       		}, {
		       			"data" : "prpLUserHolidayGrade.gradeName"
		       		},{
		       			"data" : "mobileNo"
		       		},{
		       			"data" : "checkStatusName"
		       		},{
		       			"data" : "checkerName"
		       		},{
		       			"data" : null
		       		}
		       	  ];
    
    var columns3 = [
		       		{
		       			"data" : "userCode",
		       				"orderable" : true,
			       			"targets" : 0
		       		}, {
		       			"data" : "userName"
		       		}, {
		       			"data" : "comCode"
		       		}, {
		       			"data" : "prpLUserHolidayGrade.gradeName"
		       		},{
		       			"data" : "mobileNo"
		       		},{
		       			"data" : "checkStatusName"
		       		},{
		       			"data" : "checkerName"
		       		},{
		       			"data" : null
		       		}
		       	  ];
    
    var columns9 = [
		       		{
		       			"data" : "userCode",
		       				"orderable" : true,
			       			"targets" : 0
		       		}, {
		       			"data" : "userName"
		       		}, {
		       			"data" : "comCode"
		       		}, {
		       			"data" : "prpLUserHolidayGrade.gradeName"
		       		},{
		       			"data" : "mobileNo"
		       		},{
		       			"data" : "checkStatusName"
		       		},{
		       			"data" : "cancelTime"
		       		},{
		       			"data" : null
		       		}
		       	  ];
    
    function rowCallback(row, data, displayIndex, displayIndexFull) {		
    	if(data.checkStatus == 2){
    		$('td:eq(7)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick='dispose("+data.id+");'>处理</button>");
    	}else{
    		$('td:eq(7)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick='dispose("+data.id+");'>查看</button>");
    	}
}	
    
    
    function dispose(hid){
    	index=layer.open({
		    type: 2,
		    title: "休假审核处理",
		    shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['1000px', '550px'],
		    content:"/claimcar/holidayManage/holidayManageEdit_Check.do?hid="+hid+"",
		    end:function(){
		    	$("#search").click();
		    }
		});
    }
    function search(){
		var handleStatus=$("input[name='handleStatus']:checked").val();
		if(isBlank(handleStatus)){
			layer.msg("请选择任务状态");
			return false;
		}
		var ajaxList = new AjaxList("#DataTable_"+handleStatus);
		ajaxList.targetUrl = "/claimcar/holidayManage/holidayManageFindCheck.do";
		ajaxList.postData = $("#form").serializeJson();
		ajaxList.rowCallback = rowCallback;
		
		if(handleStatus=='0'){
			ajaxList.columns = columns0;
		}else if(handleStatus=='3'){
			ajaxList.columns = columns3;
		}else{
			ajaxList.columns = columns9;
		}
		ajaxList.query();
	}
	
	</script>
</body>
</html>