<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>调度任务查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet"  />
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form"
						method="post">
						
						<input type="hidden" name="userCode" value="${user.userCode}" id="userCode"/>
						<input type="hidden" name="nodeCode" value="${nodeCode }">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> <input
								type="radio" name="keyProperty" value="registNo" /> 报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="registNo" value=""
									onfocus="radioChecked(this)" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数"/>
							</div>
							<label class="form-label col-1 text-c"> <input
								type="radio" name="keyProperty" value="policyNo" />保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="policyNo" value=""
									onfocus="radioChecked(this)" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数"/>
							</div>
							<label class="form-label col-1 text-c"> <input
								type="radio" name="keyProperty" value="licenseNo" />车牌号码
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="licenseNo" value=""
									onfocus="radioChecked(this)" datatype="carStrLicenseNo" ignore="ignore" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="MercyFlag" type="select" name="mercyFlag"
										lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">提交人</label>
							<div class="formControls col-3">
							<span class="select-box">
								<input type="select" class="select2-allowclear" name="taskInUser" id="taskInUser"/>
							</span>
							</div>
							<label class="form-label col-1 text-c"> <input
								type="radio" name="keyProperty" value="insuredName" />被保险人
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="insuredName"
									onfocus="radioChecked(this)" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<div>
								<label class="form-label col-1 text-c">报案时间</label>
								<div class="formControls col-3">
									<input type="text" class="Wdate" id="rgtDateMin"
										name="reportTimeStart"
										value="<fmt:formatDate value='${reportTimeStart}' pattern='yyyy-MM-dd'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'rgtDateMax\')||\'%y-%M-%d\'}'})"
										datatype="*" /> <span class="datespt">-</span> <input
										type="text" class="Wdate" id="rgtDateMax" name="reportTimeEnd"
										value="<fmt:formatDate value='${reportTimeEnd}' pattern='yyyy-MM-dd'/>"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'rgtDateMin\')}',maxDate:'%y-%M-%d'})"
										datatype="*" /> <font color="red">*</font>
								</div>
							</div>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="ComCodeSelect" type="select" id="comCode" 
										name="comCode" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">客户等级</label>
							<div class="formControls col-3">
								<span class="select-box"><%--  <app:codeSelect
										codeType="CustomerLevel" type="select" id="customerLevel"
										name="customerLevel" lableType="code-name" /> --%></span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<div>
								<label class="form-label col-1 text-c">流入时间</label>
								<div class="formControls col-3">
									<input type="text" class="Wdate" id="tiDateMin"
										name="taskInTimeStart"
										value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})"
										datatype="*" /> <span class="datespt">-</span> <input
										type="text" class="Wdate" id="tiDateMax" name="taskInTimeEnd"
										value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})"
										datatype="*" /> <font color="red">*</font>
								</div>
							</div>
							<label class="form-label col-1 text-c">查询范围</label>
							<div class="formControls col-3">
								<span class="select-box"><app:codeSelect
										codeType="QueryRange" type="select" name="queryRange"
										lableType="code-name" clazz="must" /> </span>
							</div>
							<div>
								<label class="form-label col-1 text-c">联系电话</label>
								<div class="formControls col-3">
									<input type="text" class="input-text" name="reporterPhone"
										datatype="m" ignore="ignore" />
								</div>
							</div>
						</div>
						<div class="row mb-6 cl">
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-6">
								<div class="radio-box">
									<label><input type="radio" name="handleStatus"
										value="0" onchange="changeHandleStatus(this)"
										checked="checked" /> 未接收</label>
								</div>
								<div class="radio-box">
									<label><input type="radio" name="handleStatus"
										value="2" onchange="changeHandleStatus(this)" />正在处理</label>
								</div>
								<div class="radio-box">
									<label><input type="radio" name="handleStatus"
										value="3" onchange="changeHandleStatus(this)" />已处理</label>
								</div>
							</div>
						</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search"
									type="submit" disabled>
									<i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询
								</button>
							</span><br />
						</div>
					</form>
				</div>
			</div>
			<!--案查询条件 结束-->


			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl">
					<span id="tabBar_0" onclick="changeHandleTab(0)"><i
						class="Hui-iconfont handun">&#xe619;</i>未处理</span> <span id="tabBar_2"
						onclick="changeHandleTab(2)"><i
						class="Hui-iconfont handing">&#xe619;</i>正在处理</span> <span id="tabBar_3"
						onclick="changeHandleTab(3)"><i
						class="Hui-iconfont handout">&#xe619;</i>已处理</span>
				</div>
				<!--未处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_0"
						class="table table-border table-hover data-table" cellpadding="0"
						cellspacing="0">
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>车牌号码</th>
								<th>被保险人</th>
								<th>案件属地</th>
								<th>是否现场</th>
								<th>报案时间</th>
								<th>流入时间</th>
								<th>提交人</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_2"
						class="table table-border table-hover data-table" cellpadding="0">
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>车牌号码</th>
								<th>被保险人</th>
								<th>案件属地</th>
								<th>是否现场</th>
								<th>报案时间</th>
								<th>流入时间</th>
								<th>提交人</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>

				<!-- 已处理 -->
				<div class="tabCon clearfix">
					<table id="DataTable_3"
						class="table table-border table-hover data-table" cellpadding="0"
						cellspacing="0">
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>车牌号码</th>
								<th>被保险人</th>
								<th>案件属地</th>
								<th>是否现场</th>
								<th>报案时间</th>
								<th>流入时间</th>
								<th>提交人</th>
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
		var columns = [ {
			"data" : "bussTagHtml"
		}, {
			"data" : "registNoHtml"
		}, {
			"data" : "license"//车牌号licenseNo
		}, {
			"data" : "insuredName"//被保险人
		},{
			"data" : "comCodeName"
		}, {
			"data" : "isOnSitReportName"
		}, {
			"data" : "reportTime"
		}, {
			"data" : "taskInTime"
		}, {
			"data" : "taskInUserName"
		}
		//{"data" : null}
		];

		var columns1 = [ {
			"data" : "bussTagHtml"
		}, {
			"data" : "registNoHtml"
		}, {
			"data" : "license"//车牌号
		}, {
			"data" : "insuredName"//被保险人
		},{
			"data" : "comCodeName"
		}, {
			"data" : "isOnSitReportName"
		}, {
			"data" : "reportTime"
		}, {
			"data" : "taskInTime"
		}, {
			"data" : "taskInUserName"
		} ];

		function rowCallback(row, data, displayIndex, displayIndexFull) {
			var user = $("#userCode").val();
			if(user!=data.assignUser){
				$('td:eq(1)', row).addClass("c-red");
				$('td:eq(1)', row).find("a").eq(0).removeAttr("onclick");
				$('td:eq(1)', row).unbind("click");
			/* 	$('td:eq(1)', row).click(function(){
					alert("12312");
					$(this).removeAttr("onclick");
					setTimeout(function(){
						alert("12fff12");
						},3000);
				}); */
				
			}
			//$('td:eq(1)', row).prop("readonly","readonly");
		}

		function search() {
			var handleStatus = $("input[name='handleStatus']:checked").val();

			if (isBlank(handleStatus)) {
				layer.msg("请选择任务状态");
				return false;
			}

			var ajaxList = new AjaxList("#DataTable_" + handleStatus);
			ajaxList.targetUrl = 'search.do';// + $("#form").serialize();
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.rowCallback = rowCallback;

			if (handleStatus == '0') {
				ajaxList.columns = columns;
			} else {
				ajaxList.columns = columns1;
			}

			ajaxList.query();
		}

		function modification(urls) {
			index = layer.open({
				type : 2,
				title : "未处理原因",
				closeBtn : 0,
				shadeClose : true,
				scrollbar : false,
				skin : 'yourclass',
				area : [ '1100px', '550px' ],
				content : [ "/claimcar/common/init.do" + urls, "no" ]
			});
		}
		
	</script>
	<script type="text/javascript" src="/claimcar/plugins/select2-3.4.4/select2.js"></script>
	
	<script type="text/javascript">
	$(document).ready(function(){ 
		$("#s2id_taskInUser").addClass("select2-allowclear");
		}); 
	$(".select2-allowclear").select2({
		
		ajax: {
			url: "/claimcar/common/findByMList.do",
		    dataType: 'json', 
		    delay: 250,
		    data: function (params, page) {
		      return {
		        q: params
		      };
		    },
	/* 	    processResults: function (data) {
		      return {
		        results: data
		      };
		    },
		    cache: true
		  }, */
		  results: function(data, page) { 
	            return data;  
	       }, // 构造返回结果  */
		  escapeMarkup: function (markup) { return markup; }, 
		  minimumInputLength: 1,
		  templateResult: formatRepo, 
		  templateSelection: formatRepoSelection 
	}});
	

	//格式化查询结果,将查询回来的id跟name放在两个div里并同行显示，后一个div靠右浮动     
function formatRepo(repo){return repo.text;}
	
function formatRepoSelection(repo){return repo.text;}

$("[name='registNo']").change(function(){
	if($("input[name='registNo']").val().length >= 4){
		$("[name='taskInTimeStart']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("[name='taskInTimeEnd']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("[name='reportTimeStart']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("[name='reportTimeEnd']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		
		
	}else if($("input[name='registNo']").val().length == 0){
		$("[name='taskInTimeStart']").attr("datatype","*");
		$("[name='taskInTimeEnd']").attr("datatype","*");
		$("[name='reportTimeStart']").attr("datatype","*");
		$("[name='reportTimeEnd']").attr("datatype","*");
	}
});
</script>
</body>
</html>
