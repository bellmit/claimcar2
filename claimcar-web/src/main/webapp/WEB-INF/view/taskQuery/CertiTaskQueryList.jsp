<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>单证任务处理查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet"  />

</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
					<input type="hidden"  name="queryRange"  value="1" >
					<input type="hidden"  name="nodeCode"  value="${nodeCode }" >
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">
							 	报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="registNo" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" value="" />
							</div>
							<label class="form-label col-1 text-c">
								保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="policyNo"  datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数" value="" />
							</div>
							<label class="form-label col-1 text-c">
								立案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="claimNo" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
						<label class="form-label col-1 text-c">车牌号码</label>
								<div class="formControls col-3">
									<input type="text" class="input-text" datatype="carStrLicenseNo" ignore="ignore" name="licenseNo" value="" />
								</div>
							
							<label class="form-label col-1 text-c">
								被保险人
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="insuredName" value="" />
							</div>
							<label class="form-label col-1 text-c">紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box">
								<app:codeSelect 	codeType="MercyFlag" type="select" 
										name="mercyFlag" lableType="code-name" />
								</span>
							</div>
						</div>
							
						<div class="row mb-6 cl">
							<label class="form-label col-1 text-c">是否核损完成</label>
								<div class="formControls col-3">
									<span class="select-box">
										<app:codeSelect 	codeType="YN01" type="select" 
												name="isVLoss" lableType="code-name"/>
									</span>
								</div>
							<label class="form-label col-1 text-c">流入时间</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin"
									name="taskInTimeStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax"
									name="taskInTimeEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*"/>
								<font color="red">*</font>
							</div>
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-3">
							 	<label>
							 		<input type="radio" name="handleStatus" value="0" onchange="changeHandleStatus(this)" checked="checked">可处理
							 	</label>
							 	<label>
							 	<input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="2">正在处理
							 	</label>
							    <label>
							    	<input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="3">已处理
							    </label>
								<font color="red">*</font>			
							</div>
						</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-disabled btn-outline btn-search" disabled type="submit">
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
					<span onclick="changeHandleTab(2)"><i class="Hui-iconfont handing">&#xe619;</i>正在处理</span> 
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
				</div>
				<!--未接收-->
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>车牌号</th>
								<th>被保险人</th>
								<th>提交人</th>
								<th>流入时间</th>
								<th>是否核损完成</th>
								<th>核损总金额</th>
								<th>未处理原因</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
				
				<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_2" class="table table-border table-hover data-table" cellpadding="0" >
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>车牌号</th>
								<th>被保险人</th>
								<th>提交人</th>
								<th>流入时间</th>
								<th>是否核损完成</th>
								<th>核损总金额</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>

				<!-- 已处理 -->
				<div class="tabCon clearfix">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>车牌号</th>
								<th>被保险人</th>
								<th>提交人</th>
								<th>处理人</th>
								<th>流入时间</th>
								<th>是否核损完成</th>
								<th>核损总金额</th>
								<th>单证补录</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
			<!--标签页 结束-->
		</div>
	</div>
<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>

	<script type="text/javascript">


		function rowCallback(row, data, displayIndex, displayIndexFull) {
			
		}
		var columns1 = [ 
		   {"data" : "bussTagHtml"}, //
		   {"data" : "registNoHtml"}, //报案号
		   {"data" : "policyNoHtml"}, //保单号
		   {"data" : "licenseNo"}, //车牌号
		   {"data" : "insuredName"}, //被保险人	   
		   {"data" : "taskInUserName"},//提交人
		   {"data" : "taskInTime"}, //流入时间
		   {"data" : "isVLossName"}, //是否核损完成
		   {"data" : "sumVeriLossFee"},//核损总金额
		   {"data" : "unHandleBtn"} //未处理原因
		   
		 ];
		var columns2 = [ 
   		   {"data" : "bussTagHtml"}, //
   		   {"data" : "registNoHtml"}, //报案号
   		   {"data" : "policyNoHtml"}, //保单号
   		   {"data" : "license"}, //车牌号
   		   {"data" : "insuredName"}, //被保险人
   		   {"data" : "taskInUserName"},//提交人
   		   {"data" : "taskInTime"},//流入时间
   		   {"data" : "isVLossName"},//是否核损完成
   		   {"data" : "sumVeriLossFee"}//核损总金额
   		 ];
		var columns3 = [ 
   		   {"data" : "bussTagHtml"}, //
   		   {"data" : "registNoHtml"}, //报案号
   		   {"data" : "policyNoHtml"}, //保单号
   		   {"data" : "license"}, //车牌号
   		   {"data" : "insuredName"}, //被保险人
   		   {"data" : "taskInUserName"},//提交人
   		   {"data" : "taskOutUserName"},//处理人
   		   {"data" : "taskInTime"}, //流入时间
   		   {"data" : "isVLossName"}, //是否核损完成
   		   {"data" : "sumVeriLossFee"},//核损总金额
   		   {"data" : "certifyBtn"} //单证补录
   		   
   		 ];


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

					if (handleStatus=='0') {
						ajaxList.columns = columns1;
					} else if(handleStatus=='2'){
						ajaxList.columns = columns2;
					} else if(handleStatus=='3'){
						ajaxList.columns = columns3;
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
