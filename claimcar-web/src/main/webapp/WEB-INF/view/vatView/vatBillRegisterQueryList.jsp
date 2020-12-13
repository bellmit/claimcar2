<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>发票登记(推送)</title>
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
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="vatQueryViewVo.registNo" value="" />
							</div>
							<label class="form-label col-1 text-c">计算书号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="vatQueryViewVo.compensateNo" value="" />
							</div>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> 
								 <app:codeSelect codeType="ComCodeSelect" type="select" id="comCode"
										name="vatQueryViewVo.comCode" lableType="code-name" />
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
						    <label class="form-label col-1 text-c">保单号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.policyNo" value="" />
							</div>
							<label class="form-label col-1 text-c">收款人 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.payName" value="" />
							</div>
							<label class="form-label col-1 text-c">收款人账号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.accountNo" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">验真状态</label>
							<div class="formControls col-3">
								<span class="select-box"> <select name="vatQueryViewVo.vidflag" class=" select ">
								        <option value=""></option>
										<option value="0">失败</option>
										<option value="1">成功</option>
								</select>
								</span>
							</div>
							<label class="form-label col-1 text-c">销方名称 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.saleName" value="" />
							</div>
							<label class="form-label col-1 text-c">销方纳税人识别号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.saleNo" value="" />
							</div>
						</div>
                        <div class="row mb-3 cl"> 
							<label class="form-label col-1 text-c">发票号码</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.billNo" value="" />
							</div>
							<label class="form-label col-1 text-c">发票代码</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.billCode" value="" />
							</div>
							<label class="form-label col-1 text-c">被保险人</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.policyName" value="" />
							</div>
						</div>
						 <div class="row mb-3 cl"> 
							
							<label class="form-label col-1 text-c">推送状态</label>
							<div class="formControls col-3">
								<span class="select-box"> <select name="vatQueryViewVo.sendStatus" class=" select ">
								        <option value=""></option>
										<option value="0">未推送</option>
										<option value="1">已推送</option>
								</select>
								</span>
							</div>
							<label class="form-label col-1 text-c">标的车牌号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.licenseNo" value="" />
								<input type="hidden"  name="vatQueryViewVo.registerStatus" id="registerStatus" value="0" />
							</div>
							 <label class="form-label col-1 text-c">报案日期</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="vatQueryViewVo.reportStartTime"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="vatQueryViewVo.reportEndTime"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})"/>
							</div>
							
						</div>
                        <div class="row mb-3 cl">
                        <label class="form-label col-1 text-c">开票日期</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="vatQueryViewVo.billStartDate" 
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="vatQueryViewVo.billEndDate" 
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})"/>
							</div>
                         <label class="form-label col-1 text-c">出险日期</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="vatQueryViewVo.damageStartTime" 
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="vatQueryViewVo.damageEndTime" 
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})"/>
							</div>
                           <label class="form-label col-1 text-c">核损通过时间</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="vatQueryViewVo.underwriteStartDate" 
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="vatQueryViewVo.underwriteEndDate" 
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" />
							</div>
							 
							
						</div>
						<div class="row mb-3 cl">
						<label class="form-label col-1 text-c">结案日期</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="vatQueryViewVo.endcaseStartTime" 
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="vatQueryViewVo.endcaseEndTime" 
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})"/>
                             </div>
						</div>
						<div class="line"></div>
						<div class="row cl text-c">
							<span class="col-offset-8 col-4">
								<button class="btn btn-primary btn-outline btn-search" disabled type="submit">
									<i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询</button>
									&nbsp;&nbsp;
								<button type="reset" class="btn btn-primary" value="reset">重置</button>
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
				<div class="tabBar cl">
				  <input type="hidden" value="0" id="tableStatus"/>
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handing">&#xe619;</i>发票登记</span>
					<span onclick="changeHandleTab(1)"><i class="Hui-iconfont handing">&#xe619;</i>发票推送</span>
				</div>
				  <!-- 发票登记 -->
					<div class="tabCon clearfix table_cont table_list">
						<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
							<thead>
							<tr>
							   <th colspan="22" style="height: 30px;">
							   <a class="btn btn-primary" onclick="exportExcel()">导出</a>&nbsp;&nbsp;&nbsp;
							   <span class="btn-upload"> <a href="javascript:void();" class="btn btn-primary radius"> 导入 </a> <input type="file" name="file" id="file" accept="xls" class="input-file"
										onchange="importExcel()"></span>&nbsp;&nbsp;&nbsp;
							   <a class="btn btn-primary" onclick="billdelete()">删除</a>
							   </th>
							</tr>
							    
							<tr class="text-c">
							    <th><input type='checkbox' name='moreChoose0' id='moreChoose0All' /></th>
								<th>发票号码</th>
								<th>发票代码</th>
								<th>开票日期</th>
								<th>销方名称</th>
								<th>销方纳税人识别号</th>
								<th>发票不含税金额</th>
								<th>发票税额</th>
								<th>发票税率</th>
								<th>发票价税合计</th>
								<th>验真状态</th>
								<th>报案号</th>
								<th>计算书号</th>
								<th>赔款类型</th>
								<th>费用类型</th>
								<th>赔付类型</th>
								<th>赔付金额</th>
								<th>已登记金额</th>
								<th>归属机构</th>
								<th>收款人</th>
								<th>收款人账号</th>
								<th>核赔通过时间</th>
							</tr>
							</thead>
							<tbody class="text-c">
							</tbody>
						</table>
						<!--table   结束-->
						<div class="row text-c">
							<br />
						</div>
					</div>
					
				<!-- 发票推送-->
				<div class="tabCon clearfix table_cont table_list">
					<table id="DataTable_1" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
						<thead>
						    <tr>
							   <th colspan="12" style="height: 30px;">
							   <a class="btn btn-primary" onclick="sendvat()">推送</a>&nbsp;&nbsp;&nbsp;
							   <a class="btn btn-primary" onclick="backbill()">撤回</a>&nbsp;&nbsp;&nbsp;
							   <a class="btn btn-primary" onclick="openBill()">解绑</a>
							   </th>
							</tr>
							<tr class="text-c">
							    <th><input type='checkbox' name='moreChoose1' id='moreChoose1All'/></th>
								<th>发票号码</th>
								<th>发票代码</th>
								<th>开票日期</th>
								<th>销方名称</th>
								<th>销方纳税人识别号</th>
								<th>发票不含税金额</th>
								<th>发票税额</th>
								<th>发票税率</th>
								<th>发票价税合计</th>
								<th>推送状态</th>
								<th>打回原因</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
					<div class="row text-c">
							<br />
					</div>
				</div>
				</div>
			</div>
			<!--标签页 结束-->
		</div>
    </div>
		<!-- 此处放页面数据 -->
		<script type="text/javascript" src="/claimcar/plugins/ajaxfileupload/ajaxfileupload.js"></script>
		<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
		<script type="text/javascript">
			$(function() {
				$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
				bindValidForm($('#form'), search);
				$("select[name='vatQueryViewVo.sendStatus']").attr("disabled",true);
				$("select[name='vatQueryViewVo.vidflag']").attr("disabled",false);
				$("input[name='vatQueryViewVo.reportStartTime']").attr("disabled",false);
				$("input[name='vatQueryViewVo.reportEndTime']").attr("disabled",false);
				$("input[name='vatQueryViewVo.damageStartTime']").attr("disabled",false);
				$("input[name='vatQueryViewVo.damageEndTime']").attr("disabled",false);
				$("input[name='vatQueryViewVo.underwriteStartDate']").attr("disabled",false);
				$("input[name='vatQueryViewVo.underwriteEndDate']").attr("disabled",false);
				$("input[name='vatQueryViewVo.endcaseStartTime']").attr("disabled",false);
				$("input[name='vatQueryViewVo.endcaseEndTime']").attr("disabled",false);
				
			});

			function changeHandleTab(val) {
				var statusValue=$("#tableStatus").val();
				
				if(statusValue!=val){
					$("select[name='vatQueryViewVo.sendStatus']").val(null);
					$("#moreChoose0All").prop("checked",false);
					$("#moreChoose1All").prop("checked",false);
				}
				$("#tableStatus").val(val);
				$("#registerStatus").val(val);
				if(val=='0'){
					$("select[name='vatQueryViewVo.sendStatus']").attr("disabled",true);
					$("select[name='vatQueryViewVo.vidflag']").attr("disabled",false);
					$("input[name='vatQueryViewVo.reportStartTime']").attr("disabled",false);
					$("input[name='vatQueryViewVo.reportEndTime']").attr("disabled",false);
					$("input[name='vatQueryViewVo.damageStartTime']").attr("disabled",false);
					$("input[name='vatQueryViewVo.damageEndTime']").attr("disabled",false);
					$("input[name='vatQueryViewVo.underwriteStartDate']").attr("disabled",false);
					$("input[name='vatQueryViewVo.underwriteEndDate']").attr("disabled",false);
					$("input[name='vatQueryViewVo.endcaseStartTime']").attr("disabled",false);
					$("input[name='vatQueryViewVo.endcaseEndTime']").attr("disabled",false);
				}else{
					$("input[name='vatQueryViewVo.reportStartTime']").val(null);
					$("input[name='vatQueryViewVo.reportEndTime']").val(null);
					$("input[name='vatQueryViewVo.damageStartTime']").val(null);
					$("input[name='vatQueryViewVo.damageEndTime']").val(null);
					$("input[name='vatQueryViewVo.underwriteStartDate']").val(null);
					$("input[name='vatQueryViewVo.underwriteEndDate']").val(null);
					$("input[name='vatQueryViewVo.endcaseStartTime']").val(null);
					$("input[name='vatQueryViewVo.endcaseEndTime']").val(null);
					$("select[name='vatQueryViewVo.vidflag']").val(null);
					$("select[name='vatQueryViewVo.sendStatus']").attr("disabled",false);
					$("select[name='vatQueryViewVo.vidflag']").attr("disabled",true);
					$("input[name='vatQueryViewVo.reportStartTime']").attr("disabled",true);
					$("input[name='vatQueryViewVo.reportEndTime']").attr("disabled",true);
					$("input[name='vatQueryViewVo.damageStartTime']").attr("disabled",true);
					$("input[name='vatQueryViewVo.damageEndTime']").attr("disabled",true);
					$("input[name='vatQueryViewVo.underwriteStartDate']").attr("disabled",true);
					$("input[name='vatQueryViewVo.underwriteEndDate']").attr("disabled",true);
					$("input[name='vatQueryViewVo.endcaseStartTime']").attr("disabled",true);
					$("input[name='vatQueryViewVo.endcaseEndTime']").attr("disabled",true);
				}
				search();
			};

			
			var columns0= [
				{
					"data" : null
				},{//多选框
					
					"data" : "billNo"
				}, //发票号码
				{
					"data" : "billCode"
				}, //发票代码
				{
					"data" : "billDate"
				}, //开票日期
				{
					"data" : "saleName"
				}, //销方名称
				{
					"data" : "saleNo"
				}, //纳税人识别号
				{
					"data" : "billNnum"
				} ,//发票不含税金额
				{
					"data" : "billSnum" 
				},//发票税额
				{
					"data" :"billSlName"
				},//发票税率
				{
					"data" : "billNum"
				} ,//发票价税合计
				{
					"data" :"vidflagName"
				},//验真状态
				{
					"data" : "registNo"
				},//报案号
				{
					"data" : "compensateNo"
				},//计算书号
				{
					"data" : "bussName"
				},//赔款类型
				{
					"data" : "feeName"
				},//费用类型
				{
					"data" : "payeeType"
				},//赔付类型
				{
					"data" : "sumAmt"
				},//赔付金额
				{
					"data" : "registerNum"
				},//已登记金额
				{
					"data" : "comName"
				},//归属机构
				{
					"data" : "payName"
				},//收款人
				{
					"data" : "accountNo"
				},//收款人
				{
					"data" : "underwriteDate"
				}//核赔通过时间
				
			];
            
			var columns1= [
	   			{
	   				"data" : null
	   			},//多选框
	   			{
                    "data" : "billNo"
				}, //发票号码
				{
					"data" : "billCode"
				}, //发票代码
				{
					"data" : "billDate"
				}, //开票日期
				{
					"data" : "saleName"
				}, //销方名称
				{
					"data" : "saleNo"
				}, //纳税人识别号
				{
					"data" : "billNnum"
				} ,//发票不含税金额
				{
					"data" : "billSnum" 
				},//发票税额
				{
					"data" :"billSlName"
				},//发票税率
				{
					"data" : "billNum"
				} ,//发票价税合计
	   			{
	   				"data" : "sendStatusName"
	   			},//推送状态
	   			{
	   				"data" : "backCauseInfo"
	   			}//打回原因
			    ];
			
			function rowCallback(row, data, displayIndex, displayIndexFull) {
				var tableStatus=$("#tableStatus").val();
				if(tableStatus=='0'){
					var rgisterFlag="1";//标记页面标识
					$('td:eq(0)',row).html("<input type='checkbox' name='moreChoose0' id='"+data.billContId+"_"+data.billId+"' value='"+data.vidflag+"'/>");
					if(data.vidflag == '1'){//验真成功的记录展示跳转链接
						$('td:eq(1)',row).html("<a   onclick=linkedCompInfoView('"+data.billNo+"','"+data.billCode+"','"+rgisterFlag+"')>"+data.billNo+"</a>");
						$('td:eq(12)',row).html("<a   onclick=linkedBillView('"+data.registNo+"','"+data.compensateNo+"','"+data.bussType+"','"+data.feeCode+"','"+data.payId+"','"+data.sumAmt+"','"+data.registerNum+"','"+data.bussId+"')>"+data.compensateNo+"</a>");
						
					}
					
				}else{
					$('td:eq(0)',row).html("<input type='checkbox' name='moreChoose1' id='"+data.billId+"'  value='"+data.sendStatus+"'/> ");
					$('td:eq(1)',row).html("<a   onclick=linkedCompInfoView1('"+data.billNo+"','"+data.billCode+"')>"+data.billNo+"</a>");
					if(tableStatus=='1'){
						$('td:eq(11)',row).html("<font color='red'>"+data.backCauseInfo+"</font>");
					}
					
				}
				
				
				
			}

			function search() {
				var tableStatus=$("#tableStatus").val();
				var ajaxList = new AjaxList("#DataTable_"+tableStatus);
				ajaxList.targetUrl = '/claimcar/bill/vatBillRegisterSerach.do';
				ajaxList.postData = $("#form").serializeJson();
				ajaxList.rowCallback = rowCallback;
				
				if(tableStatus=='0') {
					ajaxList.columns = columns0;
				} else if(tableStatus=='1'){
					ajaxList.columns = columns1;
				}
				ajaxList.query();
				
			}
			//全选或全不选
			$("#moreChoose0All").click(function(){
				var chooseFlag=$("#moreChoose0All").prop("checked");
				$("input[name='moreChoose0']").each(function(){
					if(chooseFlag){
						$(this).prop("checked",true);
					}else{
						$(this).prop("checked",false);
					}
				});
			});
            
			//全选或全不选
			$("#moreChoose1All").click(function(){
				var chooseFlag=$("#moreChoose1All").prop("checked");
				$("input[name='moreChoose1']").each(function(){
					if(chooseFlag){
						$(this).prop("checked",true);
					}else{
						$(this).prop("checked",false);
					}
				});
			});
			
			//数据删除
			function billdelete(){
				var params="";
				$("input[name='moreChoose0']").each(function(){
					var flag=$(this).prop("checked");
					if(flag){
						var id=$(this).prop("id");
						if(id !=null && id != undefined && id!='moreChoose0All'){
							params=params+id+",";
						}	
					}
				});
				if(isBlank(params)){
					layer.alert("请选择要删除的数据记录！");
					return false;
				}
				layer.confirm('确定要删除数据吗?', {
					btn : [ '确定', '取消' ]
				},function(){
					$.ajax({
						url : '/claimcar/bill/billdelete.ajax?params='+params,
						dataType : "json",// 返回json格式的数据
						type : 'post',
						success : function(json) {// json是后端传过来的值
							var result = eval(json);
							if (result.status == "200") {
								layer.msg("删除记录成功");
								search();
							}else{
								layer.msg("删除记录失败:"+result.statusText);
							}
						},
						error : function() {
							layer.msg("获取地址数据异常");
						}
					});

				},function(){
					
				});
			}
			
			//推送vat
			function sendvat(){
				var params="";
				var sendparams ="";
				$("input[name='moreChoose1']").each(function(){
					var flag=$(this).prop("checked");
					if(flag){
						var id=$(this).prop("id");
						if(id !=null && id != undefined && id!='moreChoose1All'){
							params=params+id+",";
						}	
						var sendStatus=$(this).val();
						if(sendStatus=='1'){
							sendparams=sendparams+sendStatus+",";
						}
					}
				});
				
				if(!isBlank(sendparams)){
					layer.alert("发票已推送vat,请先申请撤回！");
					return false;
				}
				if(isBlank(params)){
					layer.alert("请选择记录推送！");
					return false;
				}
				
				layer.confirm('确定要推送吗?', {
					btn : [ '确定', '取消' ]
				},function(){
					$.ajax({
						url : '/claimcar/bill/sendBillregiterToVat.ajax?params='+params,
						dataType : "json",// 返回json格式的数据
						type : 'post',
						success : function(json) {// json是后端传过来的值
							var result = eval(json);
							if (result.status == "200") {
								layer.msg("推送vat成功");
								search();
							}else{
								layer.msg("推送vat失败:"+result.statusText);
							}
						},
						error : function() {
							layer.msg("获取地址数据异常");
						}
					});

				},function(){
					
				});
			}
			
			//发票绑定撤回申请
			function backbill(){
				var params="";
				var sendparams="";
				$("input[name='moreChoose1']").each(function(){
					var flag=$(this).prop("checked");
					if(flag){
						var id=$(this).prop("id");
						var sendStatus=$(this).val();
						if(sendStatus!='1' && id!='moreChoose1All'){
							sendparams=sendparams+sendStatus+",";
						}
						if(id !=null && id != undefined && id!='moreChoose1All'){
							params=params+id+",";
						}	
					}
				});
				if(!isBlank(sendparams)){
					layer.alert("只能撤回已推送状态下的数据！");
					return false;
				}
				if(isBlank(params)){
					layer.alert("请选择已推送状态下需要撤回的数据！");
					return false;
				}
				layer.confirm('确定要撤回吗?', {
					btn : [ '确定', '取消' ]
				},function(){
					$.ajax({
						url : '/claimcar/bill/reqBackBillToVat.ajax?params='+params,
						dataType : "json",// 返回json格式的数据
						type : 'post',
						success : function(json) {// json是后端传过来的值
							var result = eval(json);
							if (result.status == "200") {
								layer.alert("操作成功:"+result.statusText);
								search();
							}else{
								layer.msg("操作失败:"+result.statusText);
							}
						},
						error : function() {
							layer.msg("获取地址数据异常");
						}
					});

				},function(){
					
				});
			}
			
			//解绑
			function openBill(){
				var params="";
				var sendparams="";
				$("input[name='moreChoose1']").each(function(){
					var flag=$(this).prop("checked");
					if(flag){
						var id=$(this).prop("id");
						var sendStatus=$(this).val();
						if(sendStatus=='1'){
							sendparams=sendparams+sendStatus+",";
						}
						if(id !=null && id != undefined && id!='moreChoose1All'){
							params=params+id+",";
						}	
					}
				});
				if(isBlank(params)){
					layer.alert("请选择需要解绑的数据！");
					return false;
				}
				if(!isBlank(sendparams)){
					layer.alert("不能选择已推送的发票解绑！");
					return false;
				}
				layer.confirm('确定要解绑吗?', {
					btn : [ '确定', '取消' ]
				},function(){
					$.ajax({
						url : '/claimcar/bill/openBillToVat.ajax?params='+params,
						dataType : "json",// 返回json格式的数据
						type : 'post',
						success : function(json) {// json是后端传过来的值
							var result = eval(json);
							if (result.status == "200") {
								layer.alert("操作成功");
								search();
							}else{
								layer.msg("操作失败:"+result.statusText);
							}
						},
						error : function() {
							layer.msg("获取地址数据异常");
						}
					});

				},function(){
					
				});
			}
			//数据导出
			function exportExcel() {
				var params="";
				var vidflags="";
				$("input[name='moreChoose0']").each(function(){
					var flag=$(this).prop("checked");
					if(flag){
						var id=$(this).prop("id");
						var vidflag=$(this).val();
						if(id !=null && id != undefined && id!='moreChoose0All'){
							params=params+id+",";
						}
						if(vidflag !='1' && id!='moreChoose0All'){
							vidflags=vidflags+vidflag+",";
							
						}
					}
				});
				if(isBlank(params)){
					layer.alert("请选择要导出的数据！");
					return false;
				}
				if(!isBlank(vidflags)){
					layer.alert("不允许选择导出验真失败的记录！");
					return false;
				}
				params=params.substring(0, params.length-1);
				window.open("/claimcar/bill/exportExcel.do?params="+params);
			}
			//数据导入
			function importExcel() {
				layer.load(0, {
					shade : [0.8, '#393D49']
				});
				$.ajaxFileUpload({
					url : '/claimcar/bill/importbillExcel.ajax', //用于文件上传的服务器端请求地址
					secureuri : false, //是否需要安全协议，一般设置为false
					fileElementId : 'file', //文件上传域的ID
					dataType : 'text/html', //返回值类型 一般设置为json
					success : function(data, status) //服务器成功响应处理函数
					{
						layer.closeAll();
						var result = $.parseJSON(data);
						if(result.status=="500"){
							layer.msg(result.msg);
							$("#file").val(null);
						}else{
							layer.msg("导入成功");
							search();
							$("#file").val(null);
						}
					},
					error : function(data, status, e)//服务器响应失败处理函数
					{
						layer.msg(e);
					}
				});
				return false;
			}
			
			
   			
			//展示对应的关联计算书信息
			function linkedCompInfoView(billNo,billCode,rgisterFlag){
				 var goUrl ="/claimcar/bill/billInfoList.ajax?billNo="+billNo+"&billCode="+billCode+"&rgisterFlag="+rgisterFlag;
				 openTaskEditWin("发票(计算书)关联信息",goUrl);
			}
			//展示对应的关联计算书信息
			function linkedCompInfoView1(billNo,billCode){
				 var goUrl ="/claimcar/bill/billInfoList.ajax?billNo="+billNo+"&billCode="+billCode;
				 openTaskEditWin("发票(计算书)关联信息",goUrl);
			}
			//展示对应的关联发票信息
			function linkedBillView(registNo,compensateNo,bussType,feeCode,payId,sumAmt,registerNum,bussId){
				 var goUrl ="/claimcar/bill/compenInfoList.do?registNo="+registNo+"&compensateNo="+compensateNo+"&bussType="+bussType+"&feeCode="+feeCode+"&payId="+payId+"&sumAmt="+sumAmt+"&registerNum="+registerNum+"&bussId="+bussId;
				 openTaskEditWin("计算书(发票)关联信息",goUrl);
			}
			
			
	</script>
</body>
</html>