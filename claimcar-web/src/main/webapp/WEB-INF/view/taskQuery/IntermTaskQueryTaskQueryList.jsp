<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>公估费任务查询</title>
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
						<input type="hidden" name="subNodeCode" value="${subNodeCode}">
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 任务号 </label>
							<div class="formControls col-4">
								<input type="text" class="input-text" name="taskNo" value="" />
							</div>

							<label class="form-label col-2 text-c"> 报案号 </label>
							<div class="formControls col-4">
								<input type="text" class="input-text" name="registNo" value="" />
							</div>

						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c">公估机构</label>
							<div class="formControls col-4">
								<app:codeSelect codeType="" type="select" name="intermCode" lableType="name" value="" dataSource="${dictVos}" />
							<c:if test="${empty dictVos}"> <select  class="select "   value="" dataType="selectMust"> </select> </c:if>
								
								<font color="red">*</font>

							</div>
							<label class="form-label col-2 text-c">流入时间</label>
							<div class="formControls col-4">
								<input type="text" class="Wdate" id="tiDateMin" name="taskInTimeStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*" />
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="taskInTimeEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*" />
								<font color="red">*</font>
							</div>


						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c">任务状态</label>
							<div class="formControls col-4">
								<label> <input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="2" checked="checked">正在处理
								</label> <label> <input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="3">已处理
								</label> <label> <input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="6">已退回
								</label> <label> <input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="9">已注销
								</label> </label> <font color="red">*</font>
							</div>
							<div class="formControls col-6"></div>
						</div>

						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search" type="submit" disabled>
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
					<span onclick="changeHandleTab(2)"><i class="Hui-iconfont handing">&#xe619;</i>正在处理</span> 
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
					<span onclick="changeHandleTab(6)"><i class="Hui-iconfont handout">&#xe619;</i>已退回</span>
					<span onclick="changeHandleTab(9)"><i class="Hui-iconfont handout">&#xe619;</i>已注销</span> 	
				</div>
				<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_2" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
						<thead>
							<tr class="text-c">
								<th>任务号</th>
								<th>公估机构</th>
								<th>流入时间</th>
								<th>提交人</th>
								<th>发票影像操作</th>
							</tr>
						</thead>
						<tbody class="text-c"></tbody>
					</table>
				</div>

				<!--已处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
						<thead>
							<tr class="text-c">
								<th>任务号</th>
								<th>公估机构</th>
								<th>流入时间</th>
								<th>提交人</th>
								<th>发票影像操作</th>
							</tr>
						</thead>
						<tbody class="text-c"></tbody>
					</table>
				</div>

				<!-- 已退回-->
				<div class="tabCon clearfix">
					<table id="DataTable_6" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
						<thead>
							<tr class="text-c">
								<th>任务号</th>
								<th>公估机构</th>
								<th>流入时间</th>
								<th>提交人</th>
								<th>发票影像操作</th>

							</tr>
						</thead>
						<tbody class="text-c">
							<!-- 代入数据放入此处 -->
						</tbody>
					</table>
				</div>
				
				<!-- 已注销-->
				<div class="tabCon clearfix">
					<table id="DataTable_9" class="table table-border table-hover data-table" cellpadding="" cellspacing="">
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
		/* 未接收 */
		var columns = [{
			"data" : "registNo",
		}, //任务号
		{
			"data" : "intermCodeName"
		}, //公估机构
		{
			"data" : "taskInTime"
		}, //流入时间
		{
			"data" : "taskInUserName"
		},//提交人
		{
			"data" : null
		}//发票影像操作
		];

		/* 正在处理 */
		var columns1 = [{
			"data" : "registNo"
		}, //任务号
		{
			"data" : "intermCodeName"
		}, //公估机构
		{
			"data" : "taskInTime"
		}, //流入时间
		{
			"data" : "taskInUserName"
		},//提交人
		{
			"data" : null
		}//发票影像操作
		];

		/*  已处理 */
		var columns2 = [{
			"data" : "registNo",
			"orderable" : true,
			"targets" : 0
		}, //任务号
		{
			"data" : "intermCodeName"
		}, //公估机构
		{
			"data" : "taskInTime"
		}, //流入时间
		{
			"data" : "taskInUserName"
		},//提交人
		{
			"data" : null
		}//发票影像操作
		];
        
		/*  已注销 */
		var columns3 = [{
			"data" : "registNo",
			"orderable" : true,
			"targets" : 0
		}, //任务号
		{
			"data" : "intermCodeName"
		}, //公估机构
		{
			"data" : "taskInTime"
		}, //流入时间
		{
			"data" : "taskInUserName"
		}//提交人
		];
		
		
		function rowCallback(row, data, displayIndex, displayIndexFull) {
			$('td:eq(0)', row).html("<a  onclick='IntermFee(" + data.taskId + ");'>" + data.registNo + "</a>");
			var handleStatus = $("input[name='handleStatus']:checked").val();
			if(handleStatus !='9'){
				$('td:eq(4)',row).html("<span class='btn-upload'><a href='javascript:void();' class='btn btn-primary radius'>上传</a><input name='file' id='Upfile"+displayIndex+"' type='file' value='点我上传'  multiple='multiple' onchange=importExcel('"+data.registNo+"','"+data.intermCode+"','"+data.intermCodeName+"','"+displayIndex+"') class='input-file'/></span>&nbsp;&nbsp;<a href=/claimcar/bill/urlReqQueryParam.do?bussNo="+data.registNo+" target='_blank' class='btn btn-primary radius'>查看</a>");
				
			}
		}

		function search() {
			var handleStatus = $("input[name='handleStatus']:checked").val();
			if (isBlank(handleStatus)) {
				layer.msg("请选择任务状态");
				return false;
			}
			var ajaxList = new AjaxList("#DataTable_" + handleStatus);

			ajaxList.targetUrl = 'AssessorFeeTaskQuery.do';// + $("#form").serialize();

			ajaxList.postData = $("#form").serializeJson();
			ajaxList.rowCallback = rowCallback;

			if (handleStatus == '2') {
				ajaxList.columns = columns1;
			} else if (handleStatus == '0') {
				ajaxList.columns = columns;
			} else if(handleStatus == '9'){
				ajaxList.columns = columns3;
			}else {
				ajaxList.columns = columns2;
			}

			ajaxList.query();
		}
		$("[name='registNo']").change(function() {
			if ($("input[name='registNo']").val().length >= 4) {
				$("[name='taskInTimeStart']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
				$("[name='taskInTimeEnd']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);

			} else if ($("input[name='registNo']").val().length == 0) {
				$("[name='taskInTimeStart']").attr("datatype", "*");
				$("[name='taskInTimeEnd']").attr("datatype", "*");
			}
		});
		
		function importExcel(registNo,intermCode,intermCodeName,displayIndex) {
			
			//添加附件start
			var formData = new FormData();
	        for(var i=0;i<$("#Upfile"+displayIndex)[0].files.length;i++) {  //循环获取上传个文件
	            formData.append("file", $("#Upfile"+displayIndex)[0].files[i]);
	        }
	        formData.append("taskNo",registNo);
	        formData.append("code",intermCode);
	        formData.append("codeName",intermCodeName);
	        formData.append("taskType","A");
	        $.ajax({
	            "url": "/claimcar/bill/asscheckimportExcel.ajax",
	            "data" : formData,
	            "dataType":"json",
	            "type": "post",
	            "contentType" : false, //上传文件一定要是false
	            "processData":false,
	            "success" : function(json) {
	            	
					if (json.status !='500') {
						layer.msg("上传发票成功！",{icon:6},3000);
					}else{
						layer.msg(json.statusText,{icon:5},3000);
					}
				}
	        });
           //添加附件end
		}
		
	</script>
</body>
</html>
