<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>平台交互查询</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<%-- <input type="hidden"  name="qryVos.nodeCode[0]"  value="${nodeCode }" >
					<input type="hidden"  name="subNodeCode"  value="${subNodeCode }" > --%>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">报案号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="interfaceLogQueryVo.registNo" value="" />
							</div>
							<label class="form-label col-1 text-c">立案号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="interfaceLogQueryVo.claimNo" value="" />
							</div>
							<label class="form-label col-1 text-c">业务号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="interfaceLogQueryVo.compensateNo" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="ComCodeSelect" type="select"
										name="interfaceLogQueryVo.comCode" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">业务名称</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="interfaceLogQueryVo.businessName" value="" />
							</div>
							<label class="form-label col-1 text-c">上传状态</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="IsValid" type="select" name="interfaceLogQueryVo.status"
										lableType="code-name" />
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">操作日期</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin"
									name="interfaceLogQueryVo.requestTimeStart" value="<fmt:formatDate value='${requestTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax"
									name="interfaceLogQueryVo.requestTimeEnd" value="<fmt:formatDate value='${requestTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*"/>
								<font color="red">*</font>
							</div>
						</div>

						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<input class="btn btn-primary btn-outline btn-search" type="button" onclick="search()" disabled value="查询"/>
									<!-- <i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询 -->
								
							</span><br />
						</div>
					</form>
				</div>
			</div>
			<!--案查询条件 结束-->


			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabCon clearfix table_cont table_list">
					<table id="resultDataTable"
						class="table table-border table-hover data-table" cellpadding="0"
						cellspacing="0">
						<thead>
							<tr class="text-c">
								<th>报案号</th>
								<th>业务名称</th>
								<th>服务名称</th>
								<th>操作节点</th>
								<th>归属机构</th>
								<th>立案号</th>
								<th>业务号</th>
								<th>状态</th>
								<th>错误代码</th>
								<th>错误信息</th>
								<th>操作日期</th>
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
	<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
	<script type="text/javascript" src="${ctx }/js/common/AjaxList.js"></script>
	<script type="text/javascript">
	$(function() {
		$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon",
				"current", "click", "0");
	});
	
		var columns = [ {
			"data" : "registNo"
		}, {
			"data" : "businessName"
		}, {
			"data" : "serviceType"
		}, {
			"data" : "operateNodeName"
		}, {
			"data" : "comCodeName"
		},{
			"data" : "claimNo"
		},{
			"data" : "compensateNo"
		},{
			"data" : "status"
		}, {
			"data" : "errorCode"
		}, {
			"data" : "errorMessage"
		}, {
			"data" : "requestTime"
		}, {
			"data" : null
		}];

		function rowCallback(row, data, displayIndex, displayIndexFull) {
			var status = "上传失败";
			if (data.status == "1") {
				status = "上传成功";
			}
			$('td:eq(7)', row).html(status);
			//
			//var btn = "<button class='btn btn-primary btn-outline btn-search' onclick=''>补送</button>";
			var btn = "<button class='btn btn-primary btn-outline btn-search' onclick=Reupload("+ "'" + data.id + "'" + ")>补送</button>";
			if(data.status == "1"){
				btn = "<button class='btn btn-disabled btn-outline btn-search' oncl()>补送</button>";
			}else if(data.status == "2"){
				btn = "<button class='btn btn-disabled btn-outline btn-search' oncl()>已补送</button>";
			}
			$('td:eq(11)', row).html(btn);
		}
		
		function search() {
			var registNo = $("*[name='interfaceLogQueryVo.registNo']").val().trim();
			var claimNo = $("*[name='interfaceLogQueryVo.claimNo']").val().trim();
		 	if(isBlank(registNo) && isBlank(claimNo)){
				layer.alert("请录入报案号或立案号");
				return false;
			} 
			var ajaxList = new AjaxList("#resultDataTable");
			ajaxList.targetUrl = "/claimcar/interfaceLog/search.do";//
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.columns = columns;
			ajaxList.rowCallback = rowCallback;
			ajaxList.query();
		}
		
		function Reupload(logId) {
			var goUrl = "/claimcar/interfaceLog/interfaceReloadInit.do?logId="
					+ logId;
			//layer.alert(logId+goUrl);
			/** 页面跳转 */
			layer.open({
				title : "补送界面",
				type : 2,
				area : [ '80%', '70%' ],
				fix : false, // 固定
				maxmin : true,
				content : goUrl
			});
		}
	</script>
</body>
</html>
