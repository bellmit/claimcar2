<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>财产核损查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet"  />
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
					<input type="hidden"  name="nodeCode"  value="${nodeCode }" >
					<input type="hidden"  name="subNodeCode"  value="${subNodeCode }" >
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">
								报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="registNo" value="" />
							</div>
							<label class="form-label col-1 text-c">
								保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数" name="policyNo"  value="" />
							</div>
							<label class="form-label col-1 text-c">
								车牌号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="carStrLicenseNo" ignore="ignore" name="licenseNo" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
						 <label class="form-label col-1 text-c">
							车架号
						</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="*4-17" ignore="ignore" name="frameNo" value="" />
							</div>
							<label class="form-label col-1 text-c">
								被保险人
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="insuredName"  value="" />
							</div>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> 
								 <app:codeSelect codeType="ComCodeSelect" type="select" id="comCode" 
										name="comCode" lableType="code-name" />
								</span>
							</div>
							</div>
							<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">流入时间</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin"
									name="taskInTimeStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*"/>
								<span class="datespt">-</span><input type="text" class="Wdate" id="tiDateMax"
									name="taskInTimeEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*" />
								<font color="red">*</font>
							</div>
							<label class="form-label col-1 text-c">紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box">
								<app:codeSelect 	codeType="MercyFlag" type="select" 
										name="mercyFlag" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">定损类型</label>
							<div class="formControls col-3">
									<span class="select-box"> 
									 <select name="taskInNode" class=" select ">
										 <option value=""></option>
										 <option value="DLProp">财产定损</option>
										<option value="DLAdd">定损追加</option>
										<option value="DLMod">定损修改</option>
									</select>
									</span>
								</div>
						</div>
						<div class="row mb-3 cl">
							
						</div>
					<div class="row mb-12 cl">
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-3">
							 	<label>
							 		<input type="radio" name="handleStatus" value="0" onchange="changeHandleStatus(this)" checked="checked">未接收
							 	</label>
							 	<label>
							 	<input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="2">正在处理
							 	</label>
							    <label>
							    	<input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="3">已处理
							    </label>
							    <label>
							 	 	<input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="6">已退回
							 	</label> 	
							 	<font color="red">*</font>			
							</div>
							<label class="form-label col-1 text-c">排序</label>
								<div class="formControls col-3">
									<span class="select-box"> 
										<select name="sorting" class="select" style="width:46%" id="vsorting">
											<option value="0">流入时间</option>
											<option value="1">金额</option>
										</select>
										<select name="sortType"  class="select" style="width:46%">
											<option value="0">降序</option>
											<option value="1">升序</option>
										</select>
									</span>
								</div>
								<div class="formControls  col-1">
							</div>
							<div class="formControls  col-3">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label ><input type="checkbox" name="includeLower" value="1">是否包含下级</label>
							</div>
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
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handun" >&#xe619;</i>未接收</span>  
					<span onclick="changeHandleTab(2)"><i class="Hui-iconfont handing">&#xe619;</i>正在处理</span> 
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
					<span onclick="changeHandleTab(6)"><i class="Hui-iconfont handback">&#xe619;</i>已退回</span>
				</div>
				<!--未接收-->
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>车牌号码</th>
								<th>车型名称</th>
								<th>被保险人</th>
								<th>承保机构</th>
								<th>待核金额</th>
								<th>流入时间</th>
								<th>提交人</th>
								<th>指定处理人</th>
								<th>定损任务类型</th>
								<th>未处理原因</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_2" class="table table-border table-hover data-table" cellpadding="0" >
						<thead>
							<tr>
							   <tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>车牌号码</th>
								<th>车型名称</th>
								<th>被保险人</th>
								<th>承保机构</th>
								<th>待核金额</th>
								<th>流入时间</th>
								<th>提交人</th>
								<th>定损任务类型</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>

				<!-- 已处理 -->
				<div class="tabCon clearfix">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>车牌号码</th>
								<th>车型名称</th>
								<th>被保险人</th>
								<th>承保机构</th>
								<th>待核金额</th>
								<th>核损金额</th>
								<th>流入时间</th>
								<th>提交人</th>
								<th>处理人</th>
								<th>定损任务类型</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!-- 已退回的任务 -->
				<div class="tabCon clearfix">
					<table id="DataTable_6" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>车牌号码</th>
								<th>车型名称</th>
								<th>被保险人</th>
								<th>承保机构</th>
								<th>待核金额</th>
								<th>核损金额</th>
								<th>流入时间</th>
								<th>提交人</th>
								<th>定损任务类型</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
			<!--标签页 结束-->
		</div>
	</div>

	<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
	
	<script type="text/javascript">
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {
		if(data.license == '地面' || data.license == '地面损失'){
			$('td:eq(2)', row).html("无（地面）");
		}
	}
		//----------未接收   tart
		var columns0 = [
			{"data" : "bussTagHtml"}, 
			{"data" : "registNoHtml"}, //报案号
			{"data" : "license"}, //车牌号码
			{"data" : "modelName"}, //车型名称
			{"data" : "insuredName"}, //被保险人
			{"data" : "comCodeName"}, //承保机构
			{"data" : "sumVeripLoss"}, //待核金额
			{"data" : "taskInTime"}, //流入时间
			{"data" : "taskInUserName"}, //提交人
			{"data" : "assignUserName"}, //处理人
			{"data" : "subNodeCodeName"}, //任务类型
			{"data" : "unHandleBtn"} //未处理原因
			];
		
		/* //----------正在处理  --- start */
		var columns2 = [ 
		    {"data" : "bussTagHtml"}, 
		    {"data" : "registNoHtml"}, //报案号
		    {"data" : "license"}, //车牌号码
			{"data" : "modelName"}, //车型名称
			{"data" : "insuredName"}, //被保险人
			{"data" : "comCodeName"}, //承保机构
			{"data" : "sumVeripLoss"}, //待核金额
			{"data" : "taskInTime"}, //流入时间
			{"data" : "taskInUserName"}, //提交人
			{"data" : "subNodeCodeName"} //任务类型
		    ];
		//----------正在处理--- end
		
	  //----------已处理  --- start
		var columns3 = [ 
			{"data" : "bussTagHtml"}, 
			{"data" : "registNoHtml"}, //报案号
			{"data" : "license"}, //车牌号码
			{"data" : "modelName"}, //车型名称
			{"data" : "insuredName"}, //被保险人
			{"data" : "comCodeName"}, //承保机构
			{"data" : "sumVeripLoss"}, //待核金额
			{"data" : "sumVeriLoss"}, //核损金额
			{"data" : "taskInTime"}, //流入时间
			{"data" : "taskInUserName"}, //提交人
			{"data" : "handlerUserName"}, //处理人
			{"data" : "subNodeCodeName"}//任务类型
			];
		//----------已处理--- end
		
		
			  //----------已退回  --- start
		var columns6 = [ 
			{"data" : "bussTagHtml"}, 
			{"data" : "registNoHtml"}, //报案号
			{"data" : "license"}, //车牌号码
			{"data" : "modelName"}, //车型名称
			{"data" : "insuredName"}, //被保险人
			{"data" : "comCodeName"}, //承保机构
			{"data" : "sumVeripLoss"}, //待核金额
			{"data" : "sumVeriLoss"}, //核损金额
			{"data" : "taskInTime"}, //流入时间
			{"data" : "taskInUserName"}, //提交人
			{"data" : "subNodeCodeName"} //任务类型
			];
		//----------已退回--- end
		
		function search(){
			var handleStatus=$("input[name='handleStatus']:checked").val();
			
			if(isBlank(handleStatus)){
				layer.msg("请选择任务状态");
				return false;
			}
			var ajaxList = new AjaxList("#DataTable_"+handleStatus);
			ajaxList.targetUrl = 'search.do';// + $("#form").serialize();
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.rowCallback = rowCallback;
			if(handleStatus=='0'){
				ajaxList.columns = columns0;
			}else if(handleStatus=='2'){
				ajaxList.columns = columns2;
			}else if(handleStatus=='3'){
				ajaxList.columns = columns3;
			}else if(handleStatus=='6'){
				ajaxList.columns = columns6;
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
