<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>查勘费查询</title>
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
							<label class="form-label col-1 text-c"> 报案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="registNo" value="" />
							</div>
							<label class="form-label col-1 text-c"> 保单号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数" name="policyNo" value="" />
							</div>
							<label class="form-label col-1 text-c"> 车牌号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="carStrLicenseNo" ignore="ignore" name="licenseNo"  value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 被保险人 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="insuredName" value="" />
							</div>
							<label class="form-label col-1 text-c">任务类型</label>
							<div class="formControls col-3">
								<span class="select-box"> <select name="taskType" class=" select ">
										<option value="0,1,2">车财任务</option>
										<option value="3">人伤任务</option>
								</select>
								</span>
							</div>
							<label class="form-label col-1 text-c">结案时间</label>
							<!-- 时间间隔要求为一个月 -->
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="diDateMin" name="caseTimeStart" value="" onchange="validate()"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'diDateMax\')||\'%y-%M-%d\'}'})"  />
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="diDateMax" name="caseTimeEnd" value="" onchange="validate()"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'diDateMin\')}',maxDate:'%y-%M-%d'})" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">查勘机构</label>
							<div class="formControls col-3">
								<app:codeSelect codeType="" type="select" id="checkCode" name="checkCode" lableType="name" value="" dataSource="${dictVos}"/>
								<c:if test="${empty dictVos}"> <select  class="select "   value=""  dataType="selectMust"> </select> </c:if>
                         		<font color="red">*</font>
								</span>
							</div>
							<label class="form-label col-1 text-c">核损通过时间</label>
							<!-- 时间间隔要求为一个月 -->
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="lossDateStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*" />
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="lossDateEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*" />
								<font color="red">*</font>
							</div>
								
						</div>


						<div class="line"></div>
						<div class="row cl text-c">
							<span class="col-offset-8 col-4">
								<button class="btn btn-primary btn-outline btn-search" disabled type="submit">
									<i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询
								</button> <span class="btn-upload"> <a href="javascript:void();" class="btn btn-primary radius"> 导入 </a> <input type="file" name="file" id="file" accept="xls" class="input-file"
										onchange="importExcel()">
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
									<th>查勘机构</th>
									<th>被保险人</th>
									<th style="width: 14%">保单号</th>
									<th style="width: 11%">核损通过时间</th>
									<th>任务详情</th>
									<th>处理人员</th>
									<th>照片数量</th>
									<th>查勘费用金额</th>
									<th>核损金额</th>
									<th>结案时间</th>
									
								</tr>
							</thead>
							<tbody class="text-c">
							</tbody>
						</table>
						<!--table   结束-->
						<div class="row text-c">
							<br />
						</div>
						<div class="row text-c">
							<!-- 导出成一个表格 -->
							<button class="btn btn-primary btn-outline btn-report" id="export" onclick="exportExcel()" type="button">导出</button>
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
				$("#checkCode").attr("datatype","selectMust");
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
			}, //
			{
				"data" : "checkName"
			}, //报案号
			{
				"data" : "insureName"
			}, //保单号
			{
				"data" : "policyNo"
			}, //车牌号码
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
				"data" :"photoCount"
			},
			{
				"data" : "checkFee"
			} ,//公估费用
			{
				"data" :"veriLoss"
			},
			{
				"data" : "endCaseTime"
			}
			];

			function rowCallback(row, data, displayIndex, displayIndexFull) {
				$('td:eq(0)',row).html("<a onclick=claimToRegistView('"+data.registNo+"');>"+data.registNo+"</a>");
				$('td:eq(3)',row).html("<a href=/claimcar/policyView/policyView.do?registNo="+data.registNo+" target='_blank'>"+data.policyNo+"</a>");
				
			}

			function search() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = '/claimcar/checkfee/checkFeeQuery.do';
				ajaxList.postData = $("#form").serializeJson();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			}

			function exportExcel() {
				window.open("/claimcar/checkfee/exportExcel.do?" + $('#form').serialize());
			}

			function importExcel() {
				layer.load(0, {
					shade : [0.8, '#393D49']
				});
				$.ajaxFileUpload({
					url : '/claimcar/checkfee/importExcel.ajax', //用于文件上传的服务器端请求地址
					secureuri : false, //是否需要安全协议，一般设置为false
					fileElementId : 'file', //文件上传域的ID
					dataType : 'text/html', //返回值类型 一般设置为json
					success : function(data, status) //服务器成功响应处理函数
					{
						layer.closeAll();
						var result = $.parseJSON(data);
						if(result.status=="500"){
							layer.msg(result.msg,{
							    icon: 2,
							    time: 4000
							});
						}else{
							layer.msg("导入成功");
							checkFee(result.data);
						}
					},
					error : function(data, status, e)//服务器响应失败处理函数
					{
						layer.msg(e);
					}
				});
				return false;
			}
			
			function claimToRegistView(registNo){
			     var title='报案处理';
			     var url='/claimcar/regist/edit.do?registNo='+registNo;
			      openTaskEditWin(title,url);
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
		</script>
</body>
</html>