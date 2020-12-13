<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>双代岗查询</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
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
								<input type="text" class="input-text"  datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数" name="policyNo"  value="" />
							</div>
							<label class="form-label col-1 text-c">
							标的车牌号码
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="carStrLicenseNo" ignore="ignore" name="licenseNo" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">立案号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数"  name="claimNo" value="" />
							</div>
							<label class="form-label col-1 text-c">被保险人</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="insuredName"/>
							</div>
							<label class="form-label col-1 text-c">标的车架号</label>
							<div class="formControls col-3">
								 <input type="text" class="input-text"  name="frameNo"/>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">查勘类别</label>
							<div class="formControls col-3">
								<span class="select-box"> 
										<select name="checkType" class="select">
										    <option value=""></option>
											<option value="0">公估代查勘</option>
											<option value="1">司内代查勘</option>
										</select>
									</span>
							</div>
							<label class="form-label col-1 text-c">承保机构</label>
							<div class="formControls col-3">
								<span class="select-box"> 
									<%-- <app:codeSelect codeType="ComCode" type="select" id="comCode" 
											name="comCode" lableType="code-name"   dataSource="${carNoMap }"/> --%>
									<app:codeSelect codeType="ComCodeLv2" name="comCode" id="comCode" 
											 type="select"  value=""/>
								</span>
							</div>
							<label class="form-label col-1 text-c">标的车发动机号</label>
							<div class="formControls col-3">
								 <input type="text" class="input-text"  name="engineNo"/>
							</div>
						</div>
						<div class="row mb-3 cl">
						<label class="form-label col-1 text-c">出险日期</label>
							<div class="formControls col-3">
							<input type="text" class="Wdate" id="tiDateMin"
									name="damageTimeStart" value="<fmt:formatDate value='${damageTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" />
								- <input type="text" class="Wdate" id="tiDateMax"
									name="damageTimeEnd" value="<fmt:formatDate value='${damageTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" />
								<%-- <span class="select-box"><app:codeSelect
										codeType="DataType" type="select"  clazz="must"
										 lableType="name" onchange="chooseDate(this);" /> </span> --%>
							</div>
							<label class="form-label col-1 text-c">报案日期</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="rpDateMin"
									name="reportTimeStart" value="<fmt:formatDate value='${reportTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'rpDateMax\')||\'%y-%M-%d\'}'})" />
								- <input type="text" class="Wdate" id="rpDateMax"
									name="reportTimeEnd" value="<fmt:formatDate value='${reportTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'rpDateMin\')}',maxDate:'%y-%M-%d'})" />
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
				<div class="tabCon clearfix table_cont table_list">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>报案号</th>
								<th>保单号</th>
								<th>被保险人</th>
								<th>标的车牌号码</th>
								<th>出险时间</th>
								<th>报案时间</th>
								<th>出险地点</th>
								<th>承保机构</th>
								<th>处理机构</th>
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
						<div class="row text-c">
						</div>
				</div>
			</div>
			<!--标签页 结束-->
		</div>
	</div>
	<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
	<script type="text/javascript">
		var columns = [ {
			"data" : "registNo"
		}, {
			"data" : "policyNoHtml"
		}, {
			"data" : "insuredName"
		}, {
			"data" : "licenseNo"
		},{
			"data" : "damageTime"
		}, {
			"data" : "reportTime"
		}, {
			"data" : "damageAddress"
		}, {
			"data" : "comCodePlyName"//承保机构
		},{
			"data" : null//处理机构
		}];
		

		function rowCallback(row, data, displayIndex, displayIndexFull) {
			 var goUrl = "/claimcar/flowQuery/showFlow.do?flowId="+data.flowId;
			 
			 var registNo = data.registNo;
			 $('td:eq(0)', row).html("<a target='_blank'  onClick=openTaskEditWin('流程图','"+goUrl+"')>"+registNo+"</a>");
			 $('td:eq(8)', row).html("<a target='_blank' onclick=comCodeShow('"+data.flowId+"');>查看</a>");	
		}
		
		
		
	    function search(){
	    	var ajaxList = new AjaxList("#DataTable_0");
			ajaxList.targetUrl = '/claimcar/dcheck/dcheckSearch.do';// + $("#form").serialize();
			ajaxList.postData=$("#form").serializeJson();
			ajaxList.rowCallback = rowCallback;
		    ajaxList.columns = columns;
			ajaxList.query();
		}
		
	    function comCodeShow(flowId){
	    	var params = "?flowId=" + flowId;
	    	layer.open({
	    		type: 2,
	    		title: '处理机构(代查勘)查看',
	    		shadeClose : true,
	    		scrollbar : false,
	    		area : [ '85%', '70%' ],
	    		content : [ "/claimcar/dcheck/dcheckHandlerList.do" + params ]
	    	});
	    }
	    
	    function exportExcel() {
	    	var sign=$("tbody").find("tr td").first().text();
	    	if(!isBlank(sign) && sign.indexOf("4")==0){
	    		window.open("/claimcar/dcheck/exportExcel.do?" + $('#form').serialize());
	    	}else{
	    		alert("请先查询，再导出!");
	    		return false;
	    	}
	    	
	    }
	</script>
</body>
</html>
