<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>收款人信息查询</title>
</head>
<body>

	<div class="table_wrap">
		<div class="formtable">
			<form id="form" class="form-horizontal" role="form" method="post">
				<input id="registNo" type="hidden" class="input-text" name="prpLPayCustomVo.registNo" value="${registNo }"/>
			</form>
			<!--标签页 开始-->
			<div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabCon clearfix">
							<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
							<thead>
								<tr class="text-c">
									<th>账户名</th>
									<th>收款人类型</th>
									<th>开户行</th>
									<th>账号</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody class="text-c">
								<!-- 动态生成表格 -->
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!--标签页 结束-->
		
			
		</div>
		<p>
		<div class="table_cont mt-15">
			<div>如没有收款人信息，请点击收款人账户信息维护按钮添加</div>
		</div>
		<!-- 结束-->
		<input type="hidden" name="tdName" value="${tdName }">
		<input type="hidden" name="compFlag" value="${compFlag }">
		<input type="hidden" id="registNo" value="${param.registNo}">
		<input type="hidden" id="flag" value="${flag}">
		<input type="hidden" id="dwPayFlag" value="${dwPayFlag}">
		
		
	</div>
	<!--
	<script src="/js/policyQuery/CheckList.js"></script>
	-->
	<script type="text/javascript">
		var columns = [
			       		{
			       			"data" : "payeeName"
			       		}, {
			       			"data" : "payObjectKindName"
			       		}, {
			       			"data" : "bankOutlets"
			       		}, {
			       			"data" : "accountNo"
			       		},{
			       			"data" : null
			       		}
			       	  ];
		
		function rowCallback(row, data, displayIndex, displayIndexFull) {	
				$('td:eq(4)', row).html("<a class='btn-primary' onclick=\"getCustRiskInfo('"+data.payeeName+"','"+data.accountNo+"','"+data.id+"','"+data.bankOutlets+"','"+data.certifyNo+"','"+data.payObjectKind+"')\">选择</a>");
		}		
	
		
		$(function(){
			$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");

			var ajaxList = new AjaxList("#resultDataTable");
			ajaxList.targetUrl = "/claimcar/payCustom/payCustomQuery.do";
			ajaxList.postData=$("#form").serializeJson();
			ajaxList.columns = columns;
			ajaxList.rowCallback = rowCallback;
			ajaxList.query();
	
		});
	
		function choosePay(payName,accNo,reId,bankName,certifyNo,payObjectKind){
			/* if(!getCustRiskInfo(payName,certifyNo,reId)){
				return;
			}   */
		    
			var flag = $("#flag").val();
			var dwPayFlag = $("#dwPayFlag").val();
			var nodeCode = parent.$("#nodeCode").val();
			if(flag=="1"){
				//判断是否重复
				var flags="0";
				var flages="0";
				parent.$("#paymentVo input[name$='payeeName']").each(function() {
					if($(this).val()== payName ){
						flags="1";
					}
				});
				parent.$("#paymentVo input[name$='accountNo']").each(function() {
					if($(this).val()== accNo ){
						flages="1";
					}
				});
				if(nodeCode=="PadPay" || nodeCode=="PrePay"){
					parent.$("#paymentVo [name$='accountNo']").each(function() {
						var acc = $(this).val();
						//账号不能相同
						if(acc == accNo){
							layer.confirm('相同赔付类型不能选择相同的收款人', {
								btn : [ '确定']
								}, function(index) {
									var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
									parent.layer.close(index); 
								});
						}
					});
				}else{
					// 代位求偿的时候可以录入相同收款人但是赔付类型不同
					var addDwPay = false;//是否有相同赔付类型的相同收款人
					var addDwPayPass = false;//是否已经校验赔付类型并通过
					if(!isBlank(dwPayFlag)){
						parent.$("#paymentVo [name$='payFlag']").each(function() {
							var trIndex = $(this).attr("name").substring(12,13);
							var acc = parent.$("[name='prpLPayment["+trIndex+"].accountNo']").val();
							var dwPayFlagChoosed = parent.$("[name='prpLPayment["+trIndex+"].payFlag']").val();

							//赔付类型 账号  如果相同不能添加
							if(acc == accNo&&dwPayFlag == dwPayFlagChoosed){
								addDwPay = true;
								layer.confirm('相同赔付类型不能选择相同的收款人', {
									btn : [ '确定']
									}, function(index) {
										var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
										parent.layer.close(index); 
									});
							}
							
						});
					}
					if(addDwPay){
						return;
					}else{
						if(!isBlank(dwPayFlag)){
							addDwPayPass = true;
						}
					}
				}
				
				if((flags!="1"&&flages!="1")||addDwPayPass){
						var tdName = $("[name='tdName']").val();
						var subAccNo = accNo.substr(accNo.length-4);
						var str = payName +"-"+subAccNo;
						parent.$("[name='"+tdName+"']").val(str);
						parent.$("[name='"+tdName+"']").removeClass("Validform_error").qtip('destroy', true);
						parent.$("[name='"+tdName+"']").next('input').val(reId);
						parent.$("[name='"+tdName+"']").parent().find("[name$='payObjectKind']").val(payObjectKind);
						//理算费用赔款和收款人表的赋值
						var compFlag = $("[name='compFlag']").val(); 
						if(compFlag=='comp'){
							$payInput = parent.$("[name='"+tdName+"']");
							$payInput.parents("td").siblings().find("input[name$='accountNo']").val(accNo);
							$payInput.parents("td").siblings().find("input[name$='bankName']").val(bankName);
							$payInput.parents("td").siblings().find("input[name$='payeeIdfNo']").val(certifyNo);

							// 收款人为修理厂需要展示是否增值税专票
							if(payObjectKind == '6'){
								$payInput.parents("td").siblings().find("div").attr("style","");
							}else{
								$payInput.parents("td").siblings().find("div").attr("style","display:none");
							}
						}
			
						var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
						parent.layer.close(index);
				}else{
					layer.confirm('不能选择相同的收款人', {
						btn : [ '确定']
						}, function(index) {
							var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
							parent.layer.close(index); 
						});
				}
			}else{
				var tdName = $("[name='tdName']").val();
				var subAccNo = accNo.substr(accNo.length-4);
				var str = payName +"-"+subAccNo;
				parent.$("[name='"+tdName+"']").val(str);
				parent.$("[name='"+tdName+"']").removeClass("Validform_error").qtip('destroy', true);
				parent.$("[name='"+tdName+"']").next('input').val(reId);
				parent.$("[name='"+tdName+"']").parent().find("[name$='payObjectKind']").val(payObjectKind);
				//理算费用赔款和收款人表的赋值
				var compFlag = $("[name='compFlag']").val(); 
				if(compFlag=='comp'){
					$payInput = parent.$("[name='"+tdName+"']");
					$payInput.parents("td").siblings().find("input[name$='accountNo']").val(accNo);
					$payInput.parents("td").siblings().find("input[name$='bankName']").val(bankName);
					$payInput.parents("td").siblings().find("input[name$='payeeIdfNo']").val(certifyNo);
				
					$payInput.parents("td").siblings().find("div").attr("style","");
					
				}
	
				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
				parent.layer.close(index);
			}
		}
		
		/**
		 * 判断收款人是否冻结，冻结不能提交
		 * @returns {Boolean}
		 */
		function getCustRiskInfo(payName,accNo,reId,bankName,certifyNo,payObjectKind){
			/*选择收款人时，如系统已支付信息中有最近一条数据账号一致，
			     用户名不一致，弹出确认框“系统中{报案号}账号{账号}户名{户名}已有支付成功信息，
			     与当前户名{当前户名}不一致，请注意核实！”，软控制。*/
			  if(!isBlank(accNo)){
			     var paysign='0';
			     var fxqIndex="1";//反洗钱标志位
				 params1={"payeeName":payName,
							"accountNo":accNo,
							"fxqIndex":fxqIndex
							};
					$.ajax({
						url : "/claimcar/payCustom/vaildAccoundNo.ajax", // 后台校验
					    type : 'post', // 数据发送方式
					    dataType : 'json', // 接受数据格式
					    data : params1, // 要传递的数据
					    async : false,
					   success : function(jsonData) {// 回调方法，可单独定义
						   var sign="";
						   var array=new Array();
					       var result = eval(jsonData);
					       var statusText1=result.statusText;
					        array=statusText1.split(",");
						  if (result.status == "200" ) {
							  sign=result.data;
							if(sign=='1'){
							   var index=layer.confirm("系统中报案号{"+array[0]+"}账号{"+accNo+"}户名{"+array[1]+"}已有支付成功信息,与当前户名{"+payName+"}不一致,请确认是否继续选择该账号?",{
									btn:['是','否']
								},function(index){
									//检验收款人是否属于黑名单中的名单
									getCustRiskInfos(payName,accNo,reId,bankName,certifyNo,payObjectKind);
												
									layer.close(index);
								},function(index){
										 layer.close(index);
								});
									
							   
							   
							}else{
							//检验收款人是否属于黑名单中的名单
							 getCustRiskInfos(payName,accNo,reId,bankName,certifyNo,payObjectKind);
											
							}
							
						}
					}
					});
			}else{
			//检验收款人是否属于黑名单中的名单	
			getCustRiskInfos(payName,accNo,reId,bankName,certifyNo,payObjectKind);
							
			}
			 
		 }
		 
		 /**
		  * 获取地址链接
		  * @returns {Boolean}
		  */
		 function getCustomerFxUrl(){
		 	$.ajax({
		 		url : "/claimcar/compensate/getFxUrl.ajax", // 后台校验
		 		type : 'post', // 数据发送方式
		 		dataType : 'json', // 接受数据格式
		 		async : false,
		 		success : function(jsonData) {// 回调方法，可单独定义
		 			window.open(jsonData.data);
		 		    var result = eval(jsonData);
		 			if (result.status == "200" ) {
		 				nameList=result.data;
		 				
		 			}
		 		}
		 	});
		 }
		  function getCustRiskInfos(payName,accNo,reId,bankName,certifyNo,payObjectKind){
				
				var msg = "";
				var index = 0;
				var params = {"payeeName" : payName,
						      "certifyNo":certifyNo,
						      "id":reId
				                };
			    $.ajax({
					url : "/claimcar/payCustom/getCustRiskInfo.ajax", // 后台校验
					type : 'post', // 数据发送方式
					dataType : 'json', // 接受数据格式
					data : params, // 要传递的数据
					async : false,
					success : function(jsonData) {// 回调方法，可单独定义
					    var result = eval(jsonData);
						if (result.status == "200" ) {
							msg =result.data;
							
						}
					}
				});
			    if("0"==msg.msgType){
			    	choosePay(payName,accNo,reId,bankName,certifyNo,payObjectKind);
				}else if("1"==msg.msgType){//黑名单
				index = layer.confirm(msg.msgInfo, {
				    btn: ['是','否'] //按钮
				}, function(){
					//跳转新增风险交易界面
					getCustomerFxUrl();
					layer.close(index);
					
				}, function(){
					layer.alert("重新输入客户名称!");
					layer.close(index);
				});
				
				}else if("2"==msg.msgType){
					layer.confirm(msg.msgInfo, {
					    btn: ['确定'] //按钮
					}, function(index){
						layer.close(index);
						choosePay(payName,accNo,reId,bankName,certifyNo,payObjectKind);
					});
				}else if("-1"==msg.msgType){
					layer.confirm(msg.msgInfo, {
					    btn: ['确定'] //按钮
					}, function(index){
						layer.close(index);
						choosePay(payName,accNo,reId,bankName,certifyNo,payObjectKind);
					});
				}else{
					choosePay(payName,accNo,reId,bankName,certifyNo,payObjectKind);
				}
			 }
	</script>
</body>
</html>
