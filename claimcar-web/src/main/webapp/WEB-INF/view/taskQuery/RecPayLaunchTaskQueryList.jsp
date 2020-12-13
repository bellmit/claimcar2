<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>追偿任务发起</title>
</head>
<body>
    <div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">立案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="recPayLaunchVo.claimNo" 
								datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数"/>
							</div>
							<label class="form-label col-1 text-c">保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-21" 
								ignore="ignore" errormsg="请输入4到21位数" name="recPayLaunchVo.policyNo" />
							</div>
							<label class="form-label col-1 text-c">报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" 
								errormsg="请输入4到22位数" name="recPayLaunchVo.registNo" />
							</div>
						</div>
						<div class="line"></div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">流入时间</label>
							    <div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin"
									name="recPayLaunchVo.endCaseDateStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*"/>
								<span class="datespt">-</span><input type="text" class="Wdate" id="tiDateMax"
									name="recPayLaunchVo.endCaseDateEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*"/>
								<font color="red">*</font>
							</div>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> 
									<app:codeSelect codeType="ComCode" name="recPayLaunchVo.comCode" 
											 type="select"  />
								</span>
							</div>
							<label class="form-label col-1 text-c">案件紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box">
								<app:codeSelect 	codeType="MercyFlag" type="select" 
										name="recPayLaunchVo.mercyFlag" lableType="code-name" />
								</span>
							</div>
						</div>
						
						<div class="line"></div>
						<div class="row">
								<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search"  type="button" id="findInfo" disabled>
								<i class="Hui-iconfont  Hui-iconfont-search2"></i>查询</button>
							</span><br />
						</div>
						
					</form>
				</div>
			</div>
			<!--案查询条件 结束-->


			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabCon clearfix">
					<table id="resultDataTable" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>案件紧急程度</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>立案号</th>
								<th>交强/商业</th>
								<th>结束时间</th>
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
    <%-- <script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script> --%>
	<script type="text/javascript">
	//var handleStatus=$("input[name='handleStatus']:checked").val();
	var columns = [
		       		{
		       			"data":"mercyFlagName"     //案件紧急程度
		       		},{
		       			"data" : "registNoHtml"        //报案号
		       		},{
		       			"data" : "policyNoHtml"        //保单号
		       		}, {
		       			"data" : "claimNo"        //立案号
		       		},{
		       			"data" : "policyType"   //交强/商业
		       		},{
		       			"data" : "endTime",    //结束时间
		       			"orderable" : true,
		       			render : function(data, type, row) {
							return DateUtils.cutToMinute(data);
						}
		       		},{
		       			"data" : null    
		       		}
		       	  ];
	
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {                                
		$('td:eq(6)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick='validateExist(\""+data.claimNo+"\",\""+data.registNoHtml+"\");'>发起追偿</button>");
	}
	   
	
	function validateExist(claimNo,registNo){
		layer.confirm('是否发起追偿?', {
			btn : [ '确认', '取消' ]
			}, function(index) {
				$.ajax({
					url : "/claimcar/recPay/validateExist.do", // 后台校验
					type : 'post', // 数据发送方式
					dataType : 'json', // 接受数据格式
					data : {"claimNo":claimNo},
					async : false,
					success : function(jsonData) {// 回调方法，可单独定义
						var result = eval(jsonData);
						var resMsg = result.data;
						if(resMsg == "yes"){
							layer.msg("该案件存在未完成的追偿任务");
						}else{
							launchRecPay(claimNo,registNo);
						}
					},
					error : function() {
						layer.msg("系统错误");
				    }
				});
			}, function(index) {
				layer.close(index);
			});
	}
	
	function launchRecPay(claimNo,registNo){	
		$.ajax({
			url : "/claimcar/recPay/recPayLaunch.do", // 后台校验
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : {"claimNo":claimNo,"registNo":registNo},
			async : false,
			success : function(jsonData) {// 回调方法，可单独定义
				var result = eval(jsonData);
				var flowTaskId = result.data;
				if(flowTaskId == "fail"){
					layer.msg("系统错误");
				}else{
					var goUrl="/claimcar/recPay/recPayEdit.do?flowTaskId=" + flowTaskId;
					openTaskEditWin("追偿处理",goUrl);
				}
			},
		});
	}
	
	
	
	$(function(){
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");

		$("#findInfo").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = "/claimcar/recPay/recPayLaunchFind.do";
				ajaxList.postData=$("#form").serializeJson();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			}
		);	
	});
	
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