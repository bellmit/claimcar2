<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>补登报案查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet"  />
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
					<input type="hidden"  name="nodeCode"  value="Regis" >
					<input type="hidden"  name="subNodeCode"  value="Regis" >
					<!-- 查询所有  -->
					<input type="hidden"  name="queryRange"  value="1" >
								<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">报案号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="registNo" value="" />
							</div>
							<label class="form-label col-1 text-c">保单号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数" name="policyNo"  value="" />
							</div>
							<label class="form-label col-1 text-c">被保险人</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="insuredName" value=""/>
							</div>
						</div>

						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">案件状态</label>
							<div class="formControls col-3">
								<label><input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="3" checked="checked">已处理</label>
								<font color="red">*</font>
							</div>
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
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
				</div>

				<!-- 已处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>车牌号</th>
								<th>出险地点</th>
								<th>出险日期</th>
								<th>报案时间</th>
								<th>被保险人</th>
								<th>保单类型</th>
								<th>处理人员</th>
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
			$('td:eq(1)', row).html("<a  onclick=registAdd('"+data.registNo+"','registAdd');>"+data.registNo+"</a>");
		}
		var columns = [
		   			{"data" : "bussTagHtml"}, //业务标识
		   			{"data" : "registNoHtml"}, //报案号
		   	        {"data" : "policyNoHtml"}, //保单号
		   	       // {"data" : "license"}, //车牌号
		   	        {"data" : "license"}, //车牌号
		   			{"data" : "damageAddress"}, //出险地点
		   			{"data" : "damageTime"}, //出险日期
		   			{"data" : "reportTime"}, //报案时间
		   			{"data" : "insuredName"}, //被保险人
		   			{"data" : "policyType"}, //保单类型
		   			{"data" : "handlerUserName"} //处理人员
		   			];
		
		function search(){
					var ajaxList = new AjaxList("#DataTable_3");
					ajaxList.targetUrl = '/claimcar/taskQuery/search.do';
					ajaxList.postData = $("#form").serializeJson();
					ajaxList.rowCallback = rowCallback;

					ajaxList.columns = columns;
					
					ajaxList.query();
			}
		
		function registAdd(registNo,relationFlag){
			// 查询保单和立案信息是否满足条件
			$.ajax({
				url : "/claimcar/registAdd/isRegistAdd.do?registNo="+registNo, // 后台校验
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				async : false,
				success : function(jsonData) {// 回调方法，可单独定义
				    //var result = eval(jsonData);
					if (jsonData.status == "200" ) {
						if(jsonData.data=='true'){
							// 打开多保单关联与取消界面  不展示取消关联的按钮
							relationshipList(registNo,relationFlag);
						}else{
							layer.alert(jsonData.data);
						}
					}
				}
			});
			
		}

	</script>
</body>
</html>
