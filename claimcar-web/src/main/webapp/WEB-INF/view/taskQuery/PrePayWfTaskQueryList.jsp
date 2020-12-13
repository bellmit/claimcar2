<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>预付冲销查询</title>
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
						<input type="hidden" name="subNodeCode" value="${subNodeCode }">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">报案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" onchange="change()"  name="registNo" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" />
							</div>
							<label class="form-label col-1 text-c">保单号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="policyNo" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数" />
							</div>
							<label class="form-label col-1 text-c">车牌号码 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="licenseNo" datatype="carStrLicenseNo" ignore="ignore" />
							</div>
						</div>

						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">立案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="claimNo" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" />
							</div>
							<label class="form-label col-1 text-c">预付号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="compensateNo" datatype="/^Y{0,1}[0-9]{4,21}$/" ignore="ignore" errormsg="请输入4到21位数" />
							</div>
							<label class="form-label col-1 text-c">被保险人名称 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="insuredName" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">预付类型</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect codeType="PrePayType" type="select" name="prePayType" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect codeType="ComCodeSelect" type="select" id="comCode" name="comCode" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">案件紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect codeType="MercyFlag" type="select" name="mercyFlag" lableType="code-name" />
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<div>
								<label class="form-label col-1 text-c">流入时间</label>
								<div class="formControls col-3">
									<input type="text" class="Wdate" id="rgtDateMin" name="taskInTimeStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'rgtDateMax\')||\'%y-%M-%d\'}'})" datatype="*" />
									<span class="datespt">-</span>
									<input type="text" class="Wdate" id="rgtDateMax" name="taskInTimeEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'rgtDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*" />
									<font color="red">*</font>
								</div>
							</div>
							<label class="form-label col-1 text-c">险种代码</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect codeType="RiskCode" type="select" id="riskCode" name="riskCode" lableType="code-name" />
								</span>
							</div>

						</div>
						<div class="row mb-8 cl">
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-3">
								</label> <label> <input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="3" checked="checked">已处理
								</label> <label> <input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="6">已退回
								</label>
							</div>
						</div>

						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search" type="submit" disabled>
									<i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询
								</button>
							</span> <br />
						</div>
					</form>
				</div>
			</div>
			<!--案查询条件 结束-->


			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl">
					<span id="tabBar_3" onclick="changeHandleTab(3)"> <i class="Hui-iconfont handout">&#xe619;</i>已处理
					</span> <span id="tabBar_6" onclick="changeHandleTab(6)"> <i class="Hui-iconfont handback">&#xe619;</i>已退回
					</span>
				</div>
				<!-- 已处理 -->
				<div class="tabCon clearfix">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0">
						<thead>
							<tr>
								<th>任务类型</th>
								<th>报案号</th>
								<th>预付号</th>
								<th>立案号</th>
								<th>流入时间</th>
								<th>处理人</th>
								<th>承保机构</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>

				<!-- 已退回 -->
				<div class="tabCon clearfix">
					<table id="DataTable_6" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
						<thead>
							<tr>
								<th>任务类型</th>
								<th>报案号</th>
								<th>预付号</th>
								<th>立案号</th>
								<th>流入时间</th>
								<th>处理人</th>
								<th>承保机构</th>
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
		var columns = [{
			"data" : null
		}, {
			"data" : "registNoHtml" //报案号
		}, {
			"data" : "compensateNo"//预付号
		}, 
		{
			"data" : "claimNo" //立案号
		},
		{
			"data" : "taskInTime" //流入时间
		}, 
		{
			"data" : "assignUserName" //处理人
		}, 
		{
			"data" : "comCodePlyName"//承保机构
		} 

		];

		function rowCallback(row, data, displayIndex, displayIndexFull) {
			if (data.riskCode == "1101") {
				$('td:eq(0)', row).html("交强预付");
			} else {
				$('td:eq(0)', row).html("商业预付");
			}
			
			if(data.claimNo!=null){
				  $('td:eq(3)',row).html("<a onclick=prePayToclaimView('"+data.claimNo+"','"+data.registNo+"');>"+data.claimNo+"</a>");
				}
		}

		function search() {
			var handleStatus = $("input[name='handleStatus']:checked").val();
			/* 
			 if (isBlank(handleStatus)) {
			 layer.msg("请选择任务状态");
			 return false;
			 } */

			var ajaxList = new AjaxList("#DataTable_" + handleStatus);
			ajaxList.targetUrl = 'search.do';// + $("#form").serialize();
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.rowCallback = rowCallback;
			ajaxList.columns = columns;

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
				area : ['1100px', '550px'],
				content : ["/claimcar/common/init.do" + urls, "no"]
			});
		}
		function change(){
			if($("input[name='registNo']").val().length >= 4){
				$("[name='taskInTimeStart']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
				$("[name='taskInTimeEnd']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
				
			}else if($("input[name='registNo']").val().length == 0){
				$("[name='taskInTimeStart']").attr("datatype","*");
				$("[name='taskInTimeEnd']").attr("datatype","*");
			}
		}
		
		
	</script>
</body>
</html>
