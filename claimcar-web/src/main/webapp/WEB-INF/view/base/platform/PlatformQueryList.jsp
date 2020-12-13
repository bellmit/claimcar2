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
							<label class="form-label col-1 text-c">理赔编码</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="platformQueryVo.claimSeqNo" value="" />
							</div>
							<label class="form-label col-1 text-c">业务号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="platformQueryVo.bussNo" value="" />
							</div>
							<label class="form-label col-1 text-c">赔案结案校验码</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="platformQueryVo.validNo" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<%-- <label class="form-label col-1 text-c">报案日期</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin"
									name="platformQueryVo.reportTime" value="<fmt:formatDate value='${reportTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax"
									name="" value="<fmt:formatDate value='${reportTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*"/>
								<font color="red">*</font>
							</div> --%>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="ComCodeSelect" type="select"
										name="platformQueryVo.comCode" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">上传节点</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="" type="select" dataSource="${uploadNodeMap}" name="platformQueryVo.requestType"
										lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">上传状态</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="IsValid" type="select" name="platformQueryVo.status"
										lableType="code-name" />
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">操作日期</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin"
									name="platformQueryVo.requestTimeStart" value="<fmt:formatDate value='${requestTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax"
									name="platformQueryVo.requestTimeEnd" value="<fmt:formatDate value='${requestTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*"/>
								<font color="red">*</font>
							</div>
						</div>

						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<input class="btn btn-primary btn-outline btn-search" type="button" onclick="search()" value="查询" disabled/>
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
								<th>归属机构</th>
								<th>理赔编码</th>
								<th>业务号</th>
								<th>上传节点</th>
								<th>上传状态</th>
								<th>错误代码</th>
								<th>上传错误原因</th>
								<th>操作日期</th>
								<th>操作</th>
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
	<script type="text/javascript" src="${ctx }/js/common/AjaxList.js"></script>
	<script type="text/javascript">
	$(function() {
		$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon",
				"current", "click", "0");
	});
		/* function chooseDate(e){
			if(e.value == "1"){
				$("#tiDateMin").attr("name","damageTimeStart");
				$("#tiDateMax").attr("name","damageTimeEnd");
			}else{
				$("#tiDateMin").attr("name","reportTimeStart");
				$("#tiDateMax").attr("name","reportTimeEnd");
			}
		} */

		
		var columns = [ {
			"data" : "comCodeName"
		}, {
			"data" : "claimSeqNo"
		}, {
			"data" : "bussNo"
		}, {
			"data" : "requestName"
		}, {
			"data" : "status"
		}, {
			"data" : "errorCode"
		}, {
			"data" : "errorMessage"
		}, {
			"data" : "createTime"
		}, {
			"data" : null
		}];

		function rowCallback(row, data, displayIndex, displayIndexFull) {
			if (data.status == "0") {
				$('td:eq(4)', row).html("上传失败");
			} else {
				$('td:eq(4)', row).html("上传成功");
			}
			//
			if (data.status == "1") {
				$('td:eq(8)', row)
						.html(
								"<button class='btn btn-disabled btn-outline btn-search' oncl()>补送</button>");
			} else if (data.status == "2") {
				$('td:eq(8)', row).html(
						"<button class='btn btn-disabled btn-outline btn-search' >已补送</button>");
			} else {
				$('td:eq(8)', row).html(
						"<button class='btn btn-primary btn-outline btn-search' onclick=reUpload("
								+ "'" + data.id + "'" + ")>补送</button>");
			}
			$('td:eq(0)', row).addClass("text-c");
			$('td:eq(2)', row).addClass("text-c");
			$('td:eq(3)', row).addClass("text-c");
			$('td:eq(4)', row).addClass("text-c");
			$('td:eq(5)', row).addClass("text-c");
			$('td:eq(6)', row).addClass("text-c");
			$('td:eq(7)', row).addClass("text-c");
			$('td:eq(8)', row).addClass("text-c");
		}

		function reUpload(logId) {
			var goUrl = "/claimcar/platformAlternately/platformReloadInit.do?logId="
					+ logId;
			//layer.alert(logId+goUrl);
			/** 页面跳转 */
			var layIndex = layer.open({
				title : "平台补送",
				type : 2,
				area : [ '80%', '75%' ],
				fix : false, // 固定
				maxmin : true,
				content : goUrl
			});
		}

		function search() {
			var claimSeqNo = $("*[name='platformQueryVo.claimSeqNo']").val().trim();
			var bussNo = $("*[name='platformQueryVo.bussNo']").val().trim();
			if((isBlank(claimSeqNo))&& (isBlank(bussNo))){
				layer.alert("请录入理赔编码或者业务号");
				return false;
			}
			var ajaxList = new AjaxList("#resultDataTable");
			ajaxList.targetUrl = "/claimcar/platformAlternately/search.do";// + $("#form").serialize();
			ajaxList.postData = $("#form").serializeJson();
			ajaxList.columns = columns;
			ajaxList.rowCallback = rowCallback;
			ajaxList.query();
		}
	</script>
</body>
</html>
