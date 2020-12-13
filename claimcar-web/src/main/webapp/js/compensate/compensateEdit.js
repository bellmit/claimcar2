	var saveType = "";
	var compensateNo = "";
	var bZCount = 0;
	var FaIndex = null;
	var TaxInfo;
	var coinsFlag = "";
	var sumPaidAmtBefore = $("input[name='prpLCompensate.sumPaidAmt']").val();
	var sumPaidFeeBefore = $("input[name='prpLCompensate.sumPaidFee']").val();
	var compWfFlag = $("#compWfFlag").val();
	var endCaseFlag = $("#endCaseFlag").val();
	// 用于管控如果界面输入内容改变，需要重新点击 获取免赔率  扣交强 生成计算书 三个按钮 的标志位
	var rateClick = true, BZAmtClick = true,compClick = true;
	//无责代赔金额要管控不允许往上改，只能往小改,false不校验，true开始校验
	var BZAmtByAbsolvePayAmt = false;
	$(function(){
		
		
		//理算的减损类型和欺诈类型赋值给核赔
		var impairmentType=$("#impairmentType").val();
		if(!isBlank(impairmentType) && impairmentType!=undefined){
			$("#impairmenttypesid option[value='"+impairmentType+"']").attr("selected","selected");
		}
		var fraudType=$("#fraudType").val();
		if(!isBlank(fraudType) && fraudType!=undefined){
			$("#fraudtypeid option[value='"+fraudType+"']").attr("selected","selected");
		}
		
		var subrogationLock = $("#subrogationLock").val();
		if(subrogationLock == "0"){
			layer.alert("代位求偿案件请先完成锁定确认！");
			$("form").find("input").attr("disabled","disabled");
			$("form").find("button").attr("disabled","disabled");
			$("form").find("select").attr("disabled","disabled");
			$("form").find("textarea").attr("disabled","disabled");
			$("#pend").removeAttr("onclick");
			$("#save").removeAttr("onclick");
		}
		//理算的人伤减损金额和车物减损金额的控制
		var reOpenFlag=$("#reOpenFlag").val();
		if(reOpenFlag=='0'){
			var pisderoAmout=$("#pisderoAmout").val();
			var cisderoAmout=$("#cisderoAmout").val();
			var claimCompleteFlag=$("#claimCompleteFlag").val();
			if(isBlank(pisderoAmout)){
				$("#pisderoAmout").val(0);
			}
			if(isBlank(cisderoAmout)){
				$("#cisderoAmout").val(0);
			}
			if(claimCompleteFlag=='1'){
				$("#cisderoAmout").attr("readonly","readonly");
				$("input[name='prpLCompensate.inSideDeroFlag']").attr("disabled","disabled");
				
				
			}
			
			var checkvalue=$("input[name='prpLCompensate.inSideDeroFlag']:checked").val();
			if(checkvalue=='1'){
				$("#cisderoAmout").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
				$("#cisderoAmout").attr("datatype","isDeroAmout");
				$("#cisderoAmout").attr("errormsg","请输入正整数或者正数保留两位小数");
				
			}else{
				$("#cisderoAmout").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
				$("#cisderoAmout").attr("datatype","isDeroAmoutCom");
				$("#cisderoAmout").attr("errormsg","请输入非负整数或者保留两位小数");
			}
			
		}
		
		
		//共保协议金额和备注的管控
		var coinsFlag  = $("input[name$='prpLCompensate.coinsFlag']:checked").val();
		if(coinsFlag == 1){
			$("#coinsDiv").show();
		}else{
 			$("input[name='prpLCompensate.coinsAmt']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		}
		
		$("#appli").addClass("btn  btn-primary"); 
		var prplcomContextVoflag=$("#prplcomContextVoflag").val();
		if(prplcomContextVoflag=='1'){
			$("#LSTable").removeClass('hide');
		}else{
			$("#causes").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
			$("[name='prplcomContextVo.flagContext']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		}
		refuseTip();
		//校验，有过结案记录，那么这个案子下的理算都不允许注销。
		if(endCaseFlag =="1"){
			$("#CompCancel").prop("disabled",true);
			$("#CompCancel").addClass("btn-disabled"); 
			$("#CompCancel").removeClass("btn-primary");
			
		}
		//生成计算书按钮是否置灰
		checkManageCompBI();
		//判断是否调用不计免赔赔款汇总方法
		var buJiState = $("#buJiState").val();
		if (buJiState == "1") {
			Buji();
		}
		if(compWfFlag=="1"||compWfFlag=="2"){
			compWfInit();
		}
		feeProcess();
		initPayCustomOtherFlag();//初始化理算例外原因下拉框状态
		var status=$("#status").val();
		var node=$("#nodeCode").val();
		var flowTaskId=$("#taskId").val();
		accept(status,node,flowTaskId);
/*		//校验是否通融prpLCompensate.allowFlag.val();
		var allowFlag  = $("input[name$='allowFlag']:checked").val();
		if(allowFlag==1){
			alert("1");
		}else{
			alert("0");
		}*/
		// 初始化时 处理交强有责无责下拉框
		initciIndemDuty();
		//财产损失名称处理
		propNameSet();
		//调用人伤损失金额计算
		$("input[name$='feeLoss']").blur();
		$("input[name$='feeOffLoss']").blur();
		//获取初始化时的责任比例 控制责任比例改变时不能低于该比例
		agreeControl();
		checkAbsInit();
		//初始化收款人信息
		if($("#dwFlag").val()!="0"){
			$("#PayCustomTbody select[name$='payFlag']").each(function(){
				changeDwPayFlag(this);
			});
		}
		fraudPayZero();	
		var ajaxEdit = new AjaxEdit($('#compEditForm'));
		ajaxEdit.targetUrl = "/claimcar/compensate/saveCompensateEdit.do";
		ajaxEdit.beforeSubmit = saveBeforeCheck;
		ajaxEdit.afterSuccess=function(result){
			after(result);
			compensateNo = result.data;
			//暂存成功后理算注销按钮亮显

			
			$("#CompCancel").prop("disabled",false);
			
		}; 
		//绑定表单
		ajaxEdit.bindForm();
		
		$.Datatype.selectMust = function(gets,obj,curform,regxp){
			var code = $(obj).val();
			if(isBlank(code)){
				return false;
			}
	    };
	    initMust();
	    
	    
	    if(status==0 || status==1 || status==2){
	    	indemnityDutyCheck();
	    }
	    $("input[name='file']").removeAttr("disabled");
	});
	
  /*
   * 理算
   */
	//车物减损金额
	var prevValue = "";//之前值
	var nowValue="";//目前输入值
	$("#cisderoAmout").change(function(){
		var pisderoAmout=$("#pisderoAmout").val();
		nowValue=$(this).val();
		prevValue=$(this).attr("oldValue");
		 if(pisderoAmout==0 && prevValue==0 && nowValue>0){
				var htmlstr="<option value=''></option>"+
					"<option value='2'>协谈减损</option>"+
				    "<option value='1'>拒赔减损</option>";
				$("#impairmenttypesid ").html(htmlstr);
			}else if (prevValue>0 && nowValue==0 && pisderoAmout==0){
				var htmls ="<option value=''></option>";
				$("#impairmenttypesid").html(htmls);
				$("#fraudtypeid").html(htmls);
			}
		 $(this).attr("oldValue",nowValue);
	});


	//欺诈类型
	$("#impairmenttypesid").click(function(){
		
		var fraudtype=$("#impairmenttypesid option:selected").val();
		
		var htmlstr="<option value=''></option>"+
			"<option value='1'>故意虚构保险标的</option>"+
		"<option value='2'>编造未曾发生的保险事故</option>"+
		"<option value='3'>编造虚假的事故原因</option>"+
		"<option value='4'>夸大损失程度</option>"+
		"<option value='5'>故意造成保险事故</option>";
		
		if(fraudtype=='1'){
			$("#fraudtypeid").html(htmlstr);
			
		}else{
			$("#fraudtypeid").html("");
		}
	});
	
	
	
	function initMust(){
		//$("select[name$='invoiceType']").attr("datatypeR","selectMust");
		$("select[name$='invoiceType']").each(function(){
			$(this).attr("datatype","selectMust");
		});
		
	}
	
	/**
	 * 如果界面输入内容改变，需要重新点击 获取免赔率  扣交强 生成计算书 三个按钮
	 */ 
	$("input").change(function(){
		var inputName = $(this).attr("name");
		var payMentName = inputName.substring(0,11);
		if(payMentName != 'prpLPayment'){
			var rateName = inputName.substr(inputName.length-8);
			if(rateName == 'dutyRate'){
				layer.msg("损失项责任比例改变，请重新进行定损扣交强和商业理算计算");
				BZAmtClick = false;
				compClick = false;
			}else if(payMentName=='prpLLossIte'){
				var name = inputName.split(".");
				if(name.length > 1){
					if(name[1]=='absolvePayAmt'){
						compClick = false;
						//无责代赔金额要管控不允许往上改，只能往小改,false不校验，true开始校验
						if(BZAmtByAbsolvePayAmt){
							var absolvePayAmts = $("[name='"+inputName+"s']").val();//当前值
							var absolvePayAmt = $(this).val();
							if(absolvePayAmt < 0){
								layer.alert("无责代赔金额不能小于0!");
								$(this).val(absolvePayAmts);
							}else{
								if(absolvePayAmt > absolvePayAmts){
									layer.alert("无责代赔金额不能大于"+absolvePayAmts);
									$(this).val(absolvePayAmts);
								}
							}
							
						}
					}else{
						rateClick = false; 
						BZAmtClick = false;
						compClick = false;
					}
				}else{
					rateClick = false; 
					BZAmtClick = false;
					compClick = false;
				}
			}else{
				rateClick = false; 
				BZAmtClick = false;
				compClick = false;
			}
		}
	});

	// 检查数据改变后是否重新点击了按钮处理数据
	function checkContentChangeHasReProcess(){
		// 如果为诉讼或通融  都不校验计算书改变
		var lawsuitFlag = 0;
		var allowFlag = 0;
		$("[name='prpLCompensate.lawsuitFlag']").each(function(){
			if($(this).prop("checked")==true){
				lawsuitFlag = $(this).val();
			}
		});
		$("[name='prpLCompensate.allowFlag']").each(function(){
			if($(this).prop("checked")==true){
				allowFlag = $(this).val();
			}
		});
		if(lawsuitFlag=='1' || allowFlag=='1'){
			return true;
		}
		
		var compKind = 1;//判断理算是 1交强理算 还是 2商业理算 还是 3理算冲销
		if($("#flag").val()=='2'){
			compKind = 2;
		}else if(!isBlank($("#compWfFlag").val())){
			compKind = 3;
		}
		
		if(compKind == 1){//交强理算只校验一个按钮
			return compClick;
		}
		if(compKind == 2){//商业理算校验三个按钮
			if(rateClick&&BZAmtClick&&compClick){
				return true;
			}else{
				return false;
			}
		}
		if(compKind == 3){//理算冲销不校验
			return true;
		}
	}
	
	/**
	 * 校验减损金额与欺诈类型是否匹配
	 */
	function checkIsderoAmout(){
		var pisderoAmoutid =$("#pisderoAmout").val();
		var cisderoAmoutid =$("#cisderoAmout").val();
		var fraudtypeid =$("#fraudtypeid").val();
		var impairmenttypesid = $("#impairmenttypesid").val();
		
		if(pisderoAmoutid > 0 || cisderoAmoutid > 0){
			if (impairmenttypesid == 1 && (fraudtypeid == null || fraudtypeid == '')) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	/**
	 * 校验减损金额与减损类型是否匹配
	 */
	function checkimpairmenttype(){
		var pisderoAmoutid =$("#pisderoAmout").val();
		var cisderoAmoutid =$("#cisderoAmout").val();
		var impairmenttypesid = $("#impairmenttypesid").val();
		if(pisderoAmoutid > 0 || cisderoAmoutid > 0){
			if (impairmenttypesid == null || impairmenttypesid== '' || impairmenttypesid=="") {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	
	function saveComp(type) {
		// 点击提交后禁用暂存提交按钮几秒 防止重复提交
		/*disabledSec($("#pend"),5);
		disabledSec($("#save"),5);*/
		$("#pend").prop("diabled",true);
		$("#save").prop("diabled",true);
		//人伤减损金额或车物减损金额不全为0时，减损类型不允许为空！
		if(!checkimpairmenttype()){
			layer.msg("人伤减损金额或车物减损金额不全为0时，减损类型不允许为空！");
			return false;
		}
		
		//车物减损金额的控制---防止案件在处理过程中，理算交强或商业之一回退
		$("#inSideDeroFlagValue").val($("input[name='prpLCompensate.inSideDeroFlag']:checked").val());
		if(!checkIsderoAmout()){
			layer.msg("减损类型为拒赔减损，欺诈类型不能为空！");
			return false;
		}
		var readonlySign=$("#cisderoAmout").attr("readonly");
		if(readonlySign != 'readonly'){
		var cisderoAmout=$("#cisderoAmout").val();//车物减损金额
		if(!isBlank(cisderoAmout)){
			
			var compensateNo=$("input[name='prpLCompensate.compensateNo']").val();
			var registNo=$("#registNo").val();
			var params = {"registNo":registNo,
					      "compensateNo":compensateNo
			                };
		    $.ajax({
				url : "/claimcar/compensate/isControlIsderoAmout.ajax", // 后台校验
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				data : params, // 要传递的数据
				async : false,
				success : function(jsonData) {// 回调方法，可单独定义
				    var result = eval(jsonData);
					if (result.data=='1') {
						var amout=result.statusText;
						
						if(!isBlank(amout)){
							$("#cisderoAmout").val(amout);
						}
						
						$("input[name='prpLCompensate.inSideDeroFlag']").each(function(){
							if(result.status=='0' && $(this).val()=='0'){
								$(this).prop("checked",true);
							}else if(result.status=='0' && $(this).val()=='1'){
								$(this).prop("checked",false);
							}else if(result.status=='1' && $(this).val()=='0'){
								$(this).prop("checked",false);
							}else if(result.status=='1' && $(this).val()=='1'){
								$(this).prop("checked",true);
							}
							
					   });
						
						
					}
				}
			});
		}
		}
		
		
		if(type=='submitLoss'){//yzy
			var isMinorInjuryCases=$("#isMinorInjuryCases").val();
			if(isMinorInjuryCases=='1'){
				alert('该案件为小额人伤案件');
				}
		}
		
		
		//判断是否有收款人信息
		/*	$("#input[name$='payeeName']").each(function(){
				if($(this).val()==null){
					alert("请选择收款人");
					return ;
				}
			});*/
		//理算冲销时不校验是否有收款人信息

		var lawsuitFlag  = $("input[name$='lawsuitFlag']:checked").val();
		
		var flagSummary = "0";
		/*if(lawsuitFlag=="1"){
			//收款人维护管控摘要
			var payeeId = new Array();
			$("#PayCustomTbody input[name$='payeeId']").each(function(){
				payeeId.push($(this).val());
				});
		    $.ajax({
				url : "/claimcar/compensate/isSummary.do?payeeId="+payeeId, // 后台校验
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				async : false,
				success : function(jsonData) {// 回调方法，可单独定义
				    //var result = eval(jsonData);
					if (jsonData.status == "200" ) {
						if(jsonData.data=="0"){
							layer.msg("请录入收款人摘要信息");
							flagSummary = "1";
						}
					}
				}
			});
		}*/
	    if(flagSummary == "0"){
			var sumLossALL = $("span#sumLossALL").next().val();
			var sumThisALL = $("span#sumThisALL").next().val();
			var isFraud = $("input[name=isFraud]:checked").val();  //是否欺诈
			var fraudLogo = $("#fraudLogo").val();
			if(isFraud!="1"&&$("#custSize").val()==0&&compWfFlag!='1'&&compWfFlag!='2'&& parseFloat(sumLossALL) != 0.00&&parseFloat(sumThisALL) != 0.00&&sumThisALL!=""){
					//layer.msg("请添加收款人信息");
				var a = 0;
					$("#PayCustomTbody input[name$='payeeName']").each(function(){
					/*	if($(this).val()==null){
							alert("请选择收款人");
						}*/
					a++;
					});
					if(a==0){
						layer.msg("请添加收款人信息");
					}
			}else{ 
				var sumThisALL = $("span#sumThisALL").next().val();
				if( isFraud!="1"&&parseFloat(sumThisALL)==0.00){
					layer.confirm('该计算书的本次赔付金额为0，是否继续?', {
						btn : [ '是', '否' ]
						}, function(index) {
							var checkRes = submitCheck();
							if(checkRes=="OK"){
								saveType = type;
								$("#compEditForm").submit();
							}else{
								layer.msg(checkRes);
							}
						}, function(index) {
							layer.close(index);
						});
				}else{
					var sum=0;
					$("#PayCustomTbody [name$='sumRealPay']").each(function(){
						//var payFlag = $(this).parents("tr").find("select[name$='payFlag']").val();
						var thisPay = parseFloat($(this).val());
//						if(payFlag=="1"){//代付
//							dfSum=parseFloat(dfSum)+thisPay; 
//						}else if(payFlag=="2"){//清付
//							//qfSum=parseFloat(qfSum)+thisPay; 
//						}
						sum=parseFloat(sum)+thisPay;
					});
					if(isFraud=="1"&&(fraudLogo=="1"||fraudLogo=="2")&&parseFloat(sum)!=0.00){
						layer.msg("欺诈案件欺诈标志为欺诈放弃索赔、欺诈拒绝赔付时，收款人金额需为0");   //欺诈案件 提示收款人金额 为0
					}else if(isFraud=="1"&&(fraudLogo=="1"||fraudLogo=="2")&&parseFloat(sum)==0.00){
						$("#compEditForm").submit();  // 欺诈案件绕过submitCheck校验
					}else{ // 正常案件
						var checkRes = submitCheck();
						if(checkRes=="OK"){
							saveType = type;
							$("#compEditForm").submit();
						}else{
							layer.msg(checkRes);
						}
					}
				}
			
			}
	    }
	}
	
	//检验涉恐人员
	function vaxInfor(){
		
		//费用收款人Id
		var feeList="";
	    var arrf = new Array();
		var i = 0 ;
		$("#PayInfoTbody input[name$='payeeId']").each(function(){
			arrf[i] = $(this).val();
			feeList+=arrf[i]+",";
			i = i +1;
			
		});
		
		//赔款收款人Id
		var payList="";
		var arrp = new Array();
		var j = 0 ;
		$("#PayCustomTbody input[name$='payeeId']").each(function(){
			arrp[j] = $(this).val();
			payList+=arrp[j]+",";
			j = j +1;
			
		});
		
		var claimNo=$("#claimNo").val();
		var registNo=$("#registNo").val();
		var policyNo=$("#policyNo").val();
		var nameList='';
		var params = {"claimNo" :claimNo,
				      "registNo":registNo,
				      "policyNo":policyNo,
				      "feeList":feeList,
				      "payList":payList
		                };
	    $.ajax({
			url : "/claimcar/compensate/vaxInfor.ajax", // 后台校验
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async : false,
			success : function(jsonData) {// 回调方法，可单独定义
			    var result = eval(jsonData);
				if (result.status == "200" ) {
				 nameList=result.data;
				}
			}
		});
		
		
		if(!isBlank(nameList)){
			alert(nameList+"已被冻结,案件不能提交");
			
			return false;
		}
		return true;
	}
	
	function coinsInfo(){
		var handlerStatus = $("#status").val();
		var compensateNo = $("input[name='prpLCompensate.compensateNo']").val();
		var sumPaidAmt = $("input[name='prpLCompensate.sumPaidAmt']").val();
		
		if(sumPaidAmt==null || sumPaidAmt==""){
			alert("请先计算本次赔款金额！");
			return false;
		}
		
		/*var params = {
				"compensateNo":compensateNo,
				"handlerStatus":handlerStatus,
				"sumPaidAmt":sumPaidAmt,
		};*/
		var params = $("#compEditForm").serialize();
		$.ajax({
			url : "/claimcar/compensate/coinsInfoEdit.ajax",
			type : "post",
			data : params,
			async: false,
			success : function(htmlData){
				layIndex=layer.open({
				    type: 1,
				    title:"联共保分摊信息",
				    skin: 'layui-layer-rim', //加上边框
				    area: ['80%', '65%'], //宽高
				    content: htmlData
				});
			}
		});
	}
	
	//赔款金额大于10000时，提醒赔款信息的反洗钱补录
	function payInfor(){
		var claimNo=$("#claimNo").val();
		var registNo=$("#registNo").val();
		var info='0';
		var params = {"claimNo" : claimNo,
				      "registNo":registNo
		                };
	    $.ajax({
			url : "/claimcar/compensate/payInfor.ajax", // 后台校验
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async : false,
			success : function(jsonData) {// 回调方法，可单独定义
			    var result = eval(jsonData);
				if (result.status == "200" ) {
					info=result.data;
					
				}
			}
		});
		
		
		if(info=='1'){
			
			alert("请补录案件的反洗钱信息");
			
			return false;
		}
		return true;
	}
	
	
	
	function saveBeforeCheck(){
		var agreeDiv = $("#agreeDiv").attr("class");
		var compensateNo = $("input[name='prpLCompensate.compensateNo']").val();
		if(agreeDiv=="hide"){
			$("#agreeDiv input[name$='agreeDesc']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
			$("#agreeDiv input[name$='agreeAmt']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		}else{
			$("#agreeDiv input[name$='agreeDesc']").removeAttr("datatype").attr("datatype","*0-20");
			$("#agreeDiv input[name$='agreeAmt']").removeAttr("datatype").attr("datatype","amount");
		}

		//收款人为非被保险人，请填写例外原因
		var othFlag = true;
		var vatFlag = true;
		$("input[name^='prpLPayment'][name$='payObjectKind']").each(function(){
			if($(this).val()!="2"){
				var idx = $(this).attr("name").substring(12,13);
				var otherFlag = $("input[name='prpLPayment["+idx+"].otherFlag']:checked").val();
				var otherCause = $("select[name='prpLPayment["+idx+"].otherCause']  option:selected").text();
				if(otherFlag =="1"&& isBlank(otherCause)){
					layer.msg("收款人为非被保险人，请填写例外原因");
					othFlag = false;
				}
			}
			//收款人为修理厂，需要校验发票税率
			if($(this).val()=="6"){
				var vatInvoiceFlag = $("input[name='prpLPayment["+idx+"].vatInvoiceFlag']:checked").val();
				var vatTaxRate = $("select[name='prpLPayment["+idx+"].vatTaxRate']").val();
				if(vatInvoiceFlag =="1"&& isBlank(vatTaxRate)){
					layer.msg("收款人为修理厂，请录入税率");
					vatFlag = false;
				}
			}
		});
		
		$("input[name^='prpLCharge'][name$='payObjectKind']").each(function(){
			var idx = $(this).attr("name").substring(11,12);
			//收款人为修理厂，需要校验发票税率
			if($(this).val()=="6"){
				var vatInvoiceFlag = $("input[name='prpLCharge["+idx+"].vatInvoiceFlag']:checked").val();
				var vatTaxRate = $("select[name='prpLCharge["+idx+"].vatTaxRate']").val();
				if(vatInvoiceFlag =="1"&& isBlank(vatTaxRate)){
					layer.msg("收款人为修理厂，请录入税率");
					vatFlag = false;
				}
			}
		});
		
		if(!othFlag){
			return false;
		}
		if(!vatFlag){
			return false;
		}
		if(!checkContentChangeHasReProcess()){
			layer.msg("计算书数据已改变，请重新点击按钮计算");
			return false;
		}
		//如果没有勾选无法找到第三方  不能有核对金额
		var noFindThird = false;
		$("#claimDeductDiv input[name$='deductCondCode']").each(function(){
			if($(this).val()=="320" || $(this).val()=="120"){
				var isCheck = $(this).parent().prev().find("input[name$='isCheck']").val();
				if(isCheck=="1"){
					noFindThird = true;
				}
				return false;
			}
		});
		
		var realLossNT= parseFloat(0);
		$("#otherLossDiv input[name$='kindCode']").each(function(){
			if($(this).val()=="NT"){
				realLossNTObj = $(this).parents("tr").find("input[name$='sumRealPay']");
				realLossNT = parseFloat(realLossNTObj.val());
				return false;
			}
		});
		if(!noFindThird && realLossNT>0){
			realLossNTObj.focus();
			layer.msg("未勾选无法找到第三方，其他损失中无法找到第三方特约险的定损金额请清0，重新商业试算！");
			return false;
		}
		
		//如果赔款或费用改变，需要重新计算联共保分摊金额
		var coinsFlag = $("#coinsFlag").val();
		var coinsSize = $("#coinsSize").val();
		if(compensateNo != null && compensateNo != "" && coinsFlag != null && "1234".indexOf(coinsFlag) != -1 
				&& (parseFloat(sumPaidAmtBefore) != parseFloat($("input[name='prpLCompensate.sumPaidAmt']").val()) 
				|| parseFloat(sumPaidFeeBefore) != parseFloat($("input[name='prpLCompensate.sumPaidFee']").val())
				||  coinsSize == 0)){
			layer.msg("请进入联共保分摊信息页面计算分摊金额！");
			return false;
		}
		
		//代位情况 提交是录入追偿或清付金额
		var returnFlag =false;
		$("#carLossDiv input[name$='itemId']").each(function(){
			var tr = $(this).parents("tr");
			var payFlag = tr.find("input[name$='payFlag']").val();
			
			if(payFlag =="1" || payFlag =="2"){//清付和追偿
				$(this).parents("tr").find("input[name$='thisPaid']").each(function(){
					if(isBlank($(this).val())){
						$(this).blur();
						var dwButton = tr.find("input[name$='dwInfoBtn']");
						showDw(dwButton);
						returnFlag = true;
						return false;//跳出for each 
					}
				});
			}
			if(returnFlag){
				return false;//跳出for each 
			}
		});
		if(returnFlag){
			return false;//不提交
		}
		
		//校验追偿金额等于拆分金额
		var sumRealPay = parseFloat(0.00);
		var thisPaid = parseFloat(0.00);
		$("#carLossDiv input[name$='itemId']").each(function(){
			var payFlag = $(this).parents("tr").find("input[name$='payFlag']").val();
			if(payFlag =="1"){//追偿
				sumRealPay = parseFloat($(this).parents("tr").find("input[name$='sumRealPay']").val());
				$(this).parents("tr").find("input[name$='thisPaid']").each(function(){
					thisPaid = thisPaid + parseFloat($(this).val());
				});
			}	
		});
		if(sumRealPay!=thisPaid){
			layer.msg("代付金额明细汇总应等于追偿核对金额！");
			return false;
		}	
//		var flages = "1";
		var prepadReturnFlag = false;
		//交强险理算，应扣垫付金额大于核定金额不可提交
		var flag = $("#flag").val();
			$("#propLossDiv [name$='offPreAmt']").each(function(){
				var sumRealPay = $(this).parents("tr").find("input[name$='sumRealPay']").val();
				if(parseFloat(sumRealPay) < parseFloat($(this).val())){
					if(flag =="1"){
						layer.alert("交强险理算，应扣预付/垫付金额大于核定金额!");
//						flages = "2";
						prepadReturnFlag = true;
					}else{
						layer.alert("商业险理算，应扣预付金额大于核定金额!");
						prepadReturnFlag = true;
					}
				}
			});
			if(prepadReturnFlag){
				return false;//不提交
			}
			$("#carLossDiv [name$='offPreAmt']").each(function(){
				var sumRealPay = $(this).parents("tr").find("input[name$='sumRealPay']").val();
				if(parseFloat(sumRealPay) < parseFloat($(this).val())){
					if(flag =="1"){
						layer.alert("交强险理算，应扣预付/垫付金额大于核定金额!");
//						flages = "2";
						prepadReturnFlag = true;
					}else{
						layer.alert("商业险理算，应扣预付金额大于核定金额!");
						prepadReturnFlag = true;
					}
				}
			});
			if(prepadReturnFlag){
				return false;//不提交
			}
			$("#persLossDiv [name$='offPreAmt']").each(function(){
				var sumRealPay = $(this).parents("tr").find("input[name$='feeRealPay']").val();
				if(parseFloat(sumRealPay) < parseFloat($(this).val())){
					if(flag =="1"){
						layer.alert("交强险理算，应扣预付/垫付金额大于核定金额!");
//						flages = "2";
						prepadReturnFlag = true;
					}else{
						layer.alert("商业险理算，应扣预付金额大于核定金额!");
						prepadReturnFlag = true;
					}
				}
			});
			if(prepadReturnFlag){
				return false;//不提交
			}

		
		//交强险计算书，若为诉讼或通融时，核定金额可修改，可修改的总额只需要控制如下
		var allowFlag  = $("input[name$='allowFlag']:checked").val();
		var lawsuitFlag  = $("input[name$='lawsuitFlag']:checked").val();
		
		if(flag=="1"){
			if(allowFlag == 1 || lawsuitFlag == 1){
				//var indemnityDuty = $("[name='prpLCompensate.indemnityDuty']").val();
				var sumRealPay=0;
				$("#PayCustomTbody [name$='sumRealPay']").each(function(){
					sumRealPay = parseFloat(sumRealPay+parseFloat($(this).val()));
					
				});

				//获取交强险限额
				var limit = getBzLimitAgreeAmt();
				if(parseFloat(sumRealPay) > limit){
					layer.alert("赔款金额不能超过" + limit + ",不能提交!");
					return false;
				}

			}
			
			
		}
		
		$("#nodutyLossDiv select[name$='insuredComCode']").each(function(){
			var insuredComCode = $(this).val();
			if(!isBlank(insuredComCode)){//代赔保险公司
				$(this).parents("tr").find("select[name$='noDutyPayFlag']").val("1");
			}
		});
		
		//判断收款人是否冻结，冻结不能提交
		/*if(!isFrostFlag()){
			return false;
		}*/
		//客户识别
		/*var clickFlag = $("#appli").attr("clickFlag");
		var sumPaidAmt = $("input[name$='prpLCompensate.sumPaidAmt']").val();
		var sumPaidFee = $("input[name$='prpLCompensate.sumPaidFee']").val();
		if((parseFloat(sumPaidAmt)+parseFloat(sumPaidFee))>=10000){
			if(clickFlag==null||clickFlag==""){
				$("#appli").focus();
				layer.msg("因合规要求，请先在理算页面进行客户识别操作！");
				return false;
			}
		}*/
		
		//存在已提交理算任务，责任比例需保持一致
		var checkDutyReturnFlag = true;
		checkDutyReturnFlag = indemnityDutyCheck("submit");
		if(!checkDutyReturnFlag){
			layer.alert("事故责任比例与已提交理算不一致，请刷新页面！");
			return false;
		}
		//共保协议，所有分项的核定金额等于协议金额，收款人赔款金额等于协议金额
		var compCoinsFlag  = $("input[name$='prpLCompensate.coinsFlag']:checked").val();
		if(compCoinsFlag == 1){
			var sumPaidAmt = $("input[name='prpLCompensate.sumPaidAmt']").val();
			var coinsAmt = $("input[name='prpLCompensate.coinsAmt']").val();
			if(sumPaidAmt != coinsAmt){
				layer.alert("所有分项的核定金额要等于协议金额！");
				return false;
			}
			var sumRealPay = 0;
			$("#PayCustomTbody input[name$='sumRealPay']").each(function(){
				sumRealPay = sumRealPay + $(this).val();
			});
			if(sumPaidAmt != sumRealPay){
				layer.alert("所有收款人赔款金额之和要等于协议金额！");
				return false;
			}
		}

		var flag = checkLawSuit ();
		console.log("flag", flag);
		if ( $("input[name = 'prpLCompensate.lawsuitFlag']:checked").val() == '1' && !flag) {
			layer.msg("请点击上方诉讼按钮，并录入诉讼信息");
			return false;
		}
	}
	
	function accept(status,node,flowTaskId){
		// 理算退回不需要重新接收任务
		var compWorkStatus = $("#compWorkStatus").val();
		if(status=='2'){
			BZAmtByAbsolvePayAmt = true;
		}
		if(status=="0"){
			if(compWorkStatus != "6"){
				acceptCompeTask();
			}else{
				BZAmtByAbsolvePayAmt = true;//无责代赔金额不能小于0
			}
		}else if(status=="3"||status=="9"){
			$("input").attr("disabled","disabled");
			$("select").attr("disabled","disabled");
			$("#pend").hide();
			$("#save").hide();
			$("button").attr("disabled","disabled");
			$("button").addClass("btn-disabled");
			
			//$("body textarea").attr("disabled", "disabled");
			$("body textarea").attr("readonly", "readonly");
//			$("body textarea").addClass("disabled");
			
			$(".rateBtn").removeAttr("disabled");
			$("#certi").removeAttr("onclick");
			$("#certi").attr("disabled","disabled");
			$("#certi").addClass("btn-disabled");
			$("#taskId").removeAttr("disabled");
			$("input[name='prpLCompensate.registNo']").removeAttr("disabled");
			$("input[name='prpLCompensate.policyNo']").removeAttr("disabled");
		}
	}
	
	/** 接收理算任务 **/
	function acceptCompeTask() {
		if(!isEmpty($("#oldClaim"))){
			layer.confirm('该案件为旧理赔迁移案件，是否继续处理?', {
				btn : [ '确定', '取消' ]
			},function(){
				acceptTask();
			},function(){
				$("body textarea").attr("disabled", "disabled");
				$("body input").each(function() {
					$(this).attr("disabled", "disabled");
				});
			});
		}else{
			acceptTask();
		}
	}
	
	function acceptTask(){
		var flowTaskId=$("#taskId").val();
		var registNo = $("#registNo").val();
		var url_ac="/claimcar/compensate/acceptCompeTask.do?flowTaskId=";
		var url_in="/claimcar/compensate/compensateEdit.do?registNo="+registNo+"&flowTaskId=";
		layer.confirm('是否确定接收此任务?', {
		btn : [ '确定', '取消' ]
		}, function(index) {
			layer.load(0, {shade : [0.8, '#393D49']});
			$.post(url_ac+Number(flowTaskId), function(jsonData) {
				if (jsonData.status != "200") {
					layer.alert("接收任务失败！");
					$("body textarea").eq(1).attr("disabled", "disabled");
					$("body input").each(function() {
						$(this).attr("disabled", "disabled");
					});
				} else {
					var url=url_in+ eval(jsonData).data;
					location.href = url;
					layer.close(index);
				}
			});
		}, function() {
			$("body textarea").attr("disabled", "disabled");
			$("body input").each(function() {
				$(this).attr("disabled", "disabled");
			});
		});
	}
	//计算人伤各项总金额
	function sumFee(element){
		var inputName = $(element).attr("name");
		var strL= new Array();
		strL = inputName.split(".");
		inputName = strL[2];//把字符按.分割，取后面的字段名称 
		var $parentDiv = $(element).parents("div[name^='personLossDiv']");//获取当前div
		var sumNum = 0.00;
		$parentDiv.find("[name$='"+inputName+"']").each(function(){
			if(!isBlank($(this).val())){
				sumNum+=parseFloat($(this).val());
			}
		});
		if(inputName=="bzPaidLoss"){
			var minusNum = $parentDiv.find("[name$='"+inputName+"']:last").val();
			sumNum = sumNum - minusNum;
		}
		$parentDiv.find("#"+inputName+"").val(sumNum.toFixed(2));

	}
	//总赔款金额
	function sumFeees(element){
		var inputName = $(element).attr("name");
		var strL= new Array();
		strL = inputName.split(".");
		inputName = strL[2];//把字符按.分割，取后面的字段名称 
		var $parentDiv = $(element).parents("div[name^='personLossDiv']");//获取当前div
		var sumNum = 0.00;
		$parentDiv.find("[name$='"+inputName+"']").each(function(){
			if(!isBlank($(this).val())){
				sumNum+=parseFloat($(this).val());
			}
		});
		if(inputName=="bzPaidLoss"){
			var minusNum = $parentDiv.find("[name$='"+inputName+"']:last").val();
			sumNum = sumNum - minusNum;
		}
		$parentDiv.find("#"+inputName+"").val(sumNum.toFixed(2));
		
		
		//总赔款金额
		var sumLossALL = 0;
		var payment = 0;
		$("[name$='sumRealPay']").each(function(){
			var payFlag = $(this).parents("tr").find("input[name$='payFlag']").val();
			if($(this).val()!=null&&$(this).val()!=""&&$(this).attr("id")!="compSum" 
				&&  payFlag!="1"){
				sumLossALL+=parseFloat($(this).val());
			}
		});
		$("#paymentVo input[name$='sumRealPay']").each(function(){
			if($(this).val()!=null&&$(this).val()!=""&&$(this).attr("id")!="compSum"){
				payment+=parseFloat($(this).val());
			}
		});
		//诉讼协议金额
		//总赔款金额=核定金额之和
		//总赔付金额
		var sumRealPayALL = sumLossALL-payment;
		$("span#sumLossALL").text(sumRealPayALL.toFixed(2));
		$("span#sumLossALL").next().val(sumRealPayALL.toFixed(2));
	}
	
	//财产名称截取显示并赋值
	function propNameSet(){
		$("#lossNamesSpan").each(function(){
			var propName = $(this).text();
			//$(this).next('input').val(propName);			
			if(propName.length>5){
				var shortName = propName.substring(0,5)+"...";
				$(this).attr("title",propName);
				$(this).text(shortName);
			}
		});
	}
	//案件备注功能实现
	$("#compeRegistMsg").click(
		function checkRegistMsgInfo(){//打开案件备注之前检验报案号和节点信息是否为空
			var registNo=$("#registNo").val();
			var nodeCode=$("#nodeCode").val();
			createRegistMessage(registNo,nodeCode,'');
			
	});
	//新增费用赔款信息或收款人信息行
	
	function addPayInfo(tbodyName,sizeName,handlerStatus) {
		var load = layer.load(0, {shade : [0.8, '#393D49']});
		var $tbody = $("#"+tbodyName+"");
		var size = parseInt($("#"+sizeName+"").val());
		var flag = $("#flag").val();
		var dwFlag = $("#dwFlag").val();
		var qfLicenseMap = $("#qfLicenseMap").val();
		var kindStr = "";
		var deductType = "";
		if(tbodyName=="PayInfoTbody"||tbodyName=="OtherLossTbody"){
			var kindStr = $("[name='"+tbodyName+"KindList']").val();
			var deductType = $("[name='prpLCompensate.deductType']").val();
		}
		
		if(tbodyName=="OtherLossTbody" && kindStr=="{}"){
			layer.msg("无附加险，不能增加！");
			return;
		}
		
		var registNo = $("#registNo").val();
		
		var params = {
			"bodyName" : tbodyName,
			"flag" : flag,
			"size" : size,
			"kindStr" : kindStr,
			"deductType" : deductType,
			"handlerStatus" :handlerStatus,
			"dwFlag" :dwFlag,
			"qfLicenseStr" : qfLicenseMap,
			"registNo" : registNo
		};
		var url = "/claimcar/compensate/addRowInfo.ajax";
		$.post(url, params, function(result) {
			$tbody.append(result);
			$("#"+sizeName+"").val(size + 1);
			$("select[name$='invoiceType']").attr("datatype","selectMust");
			layer.close(load);
		});
	}
	
	//删除费用赔款信息或收款人信息行
	function delPayInfo(element,sizeName) {
		var index = $(element).attr("name").split("_")[1];//下标
		
		var $parentTr = $(element).parent().parent();
		//自动带出的医保外用附加险，不允许删除。
		var nowKindOCode = $parentTr.children().find("input[name$='.kindCode']").val();
		var allKindCode = "DP,BP,D11P,D12P";
		if(allKindCode.indexOf(nowKindOCode) != -1){
			layer.msg("自动带出的医保外用药信息，不能删除！");
			return;
		}
		var size = parseInt($("#"+sizeName+"").val());
		$("#"+sizeName+"").val(size - 1);// 删除一条
		
		var proposalPrefix= "";
		var indexName= "";
		if(sizeName=='othSize'){
			proposalPrefix="otherLoss"; 
			indexName ="otherLossVo_";
		}else if(sizeName=='infoSize'){
			proposalPrefix="prpLCharge"; 
			indexName ="PayInfo_";
		}else if(sizeName=='custSize'){
			proposalPrefix="prpLPayment"; 
			indexName ="PayCustom_";
		}
		delTr(size, index, indexName, proposalPrefix);
		
		$parentTr.remove();
	}
	
	function initOtherProp(element){
		var kindCodes = "";
		var registNo = $("#registNo").val();
		var checkFlagEle =$("#OtherLossTbody input[name$='kindCode']");
		var kindArray = $("#otherLossDiv input[name='OtherLossTbodyKindList']").val();
		var cprcCase = $("#cprcCase").val();
		$(checkFlagEle).each(function(){
			if(kindCodes ==""){
				kindCodes =$(this).val();
			}else{
				kindCodes = kindCodes + "," + $(this).val();
			}
			
		});
		
		if(kindArray=="{}"){
			layer.msg("无附加险，不能增加！");
			return;
		}
		var params = {
				"registNo":registNo,"kindCodes":kindCodes,"kindArray":kindArray,"cprcCase":cprcCase
		};
		$.ajax({
			url : "/claimcar/compensate/initOtherProp.ajax",
			type : "post",
			data : params,
			async: false,
			success : function(htmlData){
				//initPopover(element, htmlData);
				layIndex=layer.open({
				    type: 1,
				    skin: 'layui-layer-rim', //加上边框
				    area: ['230px', '270px'], //宽高
				    content: htmlData
				});
			}
		});
	}
	
	function loadOtherPropTr(kindCode){
		var $tbody = $("#OtherLossTbody");
		var param = "";
		var count =1;
		if(kindCode != undefined){
			param = kindCode;
			
		}else{
			var indexSeriNo = $("#subRiskTab [name='subRiskCheckFlag']:checked");
			count = indexSeriNo.length;
			for(var i = 0 ; i < indexSeriNo.length; i++){
				if(i == 0){
					param = indexSeriNo.val();
				} else {
					param = $(indexSeriNo[i]).val() + "," + param; 
				}
			}
		}
		
		var registNo = $("#registNo").val();
		var policyNo = $("#policyNo").val();
		var cprcCase = $("#cprcCase").val();
		var riskCode = $("#riskCode").val();
		var $subRiskSize =$("#othSize") ;//附加险条数
		var size = parseInt($subRiskSize.val(),10);
		var params = {"size":size,"registNo":registNo,"policyNo" :policyNo,"kindCodes":param,"cprcCase" :cprcCase,"riskCode":riskCode};
		var url = "/claimcar/compensate/loadOtherPropTr.ajax";
		$.post(url,params, function(result){
			$tbody.append(result);
			$subRiskSize.val(size + count );//重新附加险条数
		});
		
		if(kindCode == undefined){
			closePop();
		}
		
	}
	
	function closePop(){
	    layer.close(layIndex);
	}
	
	//诉讼
	function lawsuits(registNo, nodeCode){
		var urls = "?registNo=" + registNo + "&nodeCode="+ nodeCode ;
		layer.open({
		    type: 2,
		    title: "诉讼",
		    shade: false,
			shadeClose : true,
			scrollbar : false,
		    skin: 'yourclass',
		    area: ['1100px', '600px'],
		     content:["/claimcar/lawSuit/lawSuitByLEdit.do"+urls]
		});
	}
	//输入框显示和隐藏
	//通融
	function agreeControl(){
		var checkFlag = "N";
		$(".agreeControlDiv").find("input[type='radio']").each(function(){
			if($(this).prop("checked")==true&&$(this).val()=="1"){
				checkFlag = "Y";
			}
		});
		if(checkFlag == "Y"){
			$("#agreeDiv").removeClass("hide");
			$("#agreeDiv input[name$='agreeDesc']").removeAttr("datatype").attr("datatype","*0-20");
			$("#agreeDiv input[name$='agreeAmt']").removeAttr("datatype").attr("datatype","amount");
			
			$("[name$='sumRealPay']").removeAttr("readonly");
			$("[name$='deductDutyRate']").removeAttr("readonly");
			$("[name$='deductAbsRate']").removeAttr("readonly");
			$("[name$='deductAddRate']").removeAttr("readonly");
			
			//实时汇总赔款金额
			
			$("[name$='sumRealPay']").change(function(){
				var sumLossALL = 0;
				var payment = 0;
				$("[name$='sumRealPay']").each(function(){
					var payFlag = $(this).parents("tr").find("input[name$='payFlag']").val();
					if($(this).val()!=null&&$(this).val()!=""&&$(this).attr("id")!="compSum" 
						&&  payFlag!="1"){
						sumLossALL+=parseFloat($(this).val());
					}
				});
				$("#paymentVo input[name$='sumRealPay']").each(function(){
					if($(this).val()!=null&&$(this).val()!=""&&$(this).attr("id")!="compSum"){
						payment+=parseFloat($(this).val());
					}
				});
				//诉讼协议金额
				//总赔款金额=核定金额之和
				//总赔付金额
				var sumRealPayALL = sumLossALL-payment;
				$("span#sumLossALL").text(sumRealPayALL.toFixed(2));
				$("span#sumLossALL").next().val(sumRealPayALL.toFixed(2));
			});
			$("[name$='sumRealPay']").change(function(){
				var sumLossALL = 0;
				var payment = 0;
				$("[name$='sumRealPay']").each(function(){
					var payFlag = $(this).parents("tr").find("input[name$='payFlag']").val();
					if($(this).val()!=null&&$(this).val()!=""&&$(this).attr("id")!="compSum" 
						&&  payFlag!="1"){
						sumLossALL+=parseFloat($(this).val());
					}
				});
				$("#paymentVo input[name$='sumRealPay']").each(function(){
					if($(this).val()!=null&&$(this).val()!=""&&$(this).attr("id")!="compSum"){
						payment+=parseFloat($(this).val());
					}
				});
				//诉讼协议金额
				//总赔款金额=核定金额之和
				//总赔付金额
				var sumRealPayALL = sumLossALL-payment;
				$("span#sumLossALL").text(sumRealPayALL.toFixed(2));
				$("span#sumLossALL").next().val(sumRealPayALL.toFixed(2));
			});
		/*	$("#persLossDiv input[name$='personId']").each(function(){
				$childDiv = $(this).parents("div[name^='personLossDiv']");
				$childDiv.find("input[name$='feeRealPay']").each(function(){
					$(this).prop("readonly",false);
				
				});
			});	*/
			
		}else{
			//协议金额不显示，并清空控件内的值
			$("#agreeDiv").addClass("hide");
			$("#agreeDiv input[name$='agreeDesc']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
			$("#agreeDiv input[name$='agreeAmt']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
			var txt = "";
			$("[name='prpLCompensate.agreeAmt']").val(txt);
			$("[name='prpLCompensate.agreeDesc']").val(txt);
			$("[name$='sumRealPay']").each(function(){
				if($(this).attr("id")!="payMentPay"){
					$(this).attr("readonly","readonly");
				}
			});
			setReadonly("deductDutyRate");
			setReadonly("deductAbsRate");
			setReadonly("deductAddRate");
		}
	}
	//将元素设为只读
	function setReadonly(Name){
		$("[name$='"+Name+"']").each(function(){
			$(this).attr("readonly","readonly");
		});
	}
	//协议金额输入框显示和隐藏
/*	$("[name='prpLCompensate.lawsuitFlag']").click(function(){
		var lawsuitInitVal = $("[name='lawsuitFlagInitVal']").val();
		if(lawsuitInitVal!="1"){
			agreeControl();
		}else{
			//理算初始化的时候如果案件诉讼为是，则不允许修改
			$("[name='prpLCompensate.lawsuitFlag']").each(function(){
				if($(this).val()=="1"){
					$(this).prop("checked",true);
				}
			});
			layer.msg("诉讼案件不允许修改");
		}
		
	});*/
	$("[name='prpLCompensate.lawsuitFlag']").click(function(){
		agreeControl();
	});
	$("[name='prpLCompensate.allowFlag']").click(function(){
		agreeControl();
	});
	function after(result) {
		
		var flowTaskId = $("input[name='flowTaskId']").val();
		var coinsFlag = $("#coinsFlag").val();
		var coinsSize = $("#coinsSize").val();
		
		//校验之后，保存或者提交
		if(saveType == "save") {
			if(result.status=="200"){
				layer.confirm('暂存成功', {
					btn : [ '确定']
					}, function(index) {
						window.location.reload();
						$("#compensateNo").text(result.data);
						$("input[name='prpLCompensate.compensateNo']").val(result.data);
						$("#pend").prop("disabled",true);	//保存成功防止重复提交
					}
			);
				/*window.location.reload();
				$("#compensateNo").text(result.data);
				$("input[name='prpLCompensate.compensateNo']").val(result.data);
				$("#pend").prop("disabled",true);	//保存成功防止重复提交
*/			}	
		}else {
			
			
			//检验被保险人，受益人，授办人是否是黑名单之人
			if(!vaxInfor()){
				window.location.reload();
				return false;
			}
			//判断是否提示补录反洗钱信息
			if(!payInfor()){
				window.location.reload();	
				return false;
			}
			
			//联共保单如果未实收不允许提交
			var payrefFlag = $("#payrefFlag").val();
			if(coinsFlag != null && "1234".indexOf(coinsFlag) != -1 && payrefFlag=="0"){
				layer.alert("共保单尚未实收，理算不能提交！");
				return false;
			}
			
			//如果赔款或费用改变，需要重新计算联共保分摊金额
			if(coinsFlag != null && "1234".indexOf(coinsFlag) != -1 
					&& (parseFloat(sumPaidAmtBefore) != parseFloat($("input[name='prpLCompensate.sumPaidAmt']").val()) 
					|| parseFloat(sumPaidFeeBefore) != parseFloat($("input[name='prpLCompensate.sumPaidFee']").val())
					||  coinsSize == 0)){
				layer.msg("请进入联共保分摊信息页面计算分摊金额！");
				window.location.reload();
				return false;
			}
			
			
			//如果是上海的保单，理算提交校验（需要上传平台理算以前的节点都成功才能提交）
			var comCode = $("#comCode").val();
			if(!isBlank(comCode) && comCode.substr(0,2)=="22"){
				if(!isPlatformSH()){
					layer.alert("该保单的案件在上海平台（报案登记、立案登记、查勘定核损登记、单证登记）节点未全部上传成功，请联系管理员！");
					return false;
				}
			}
			
			//提交
			var claimNo = $("[name='prpLCompensate.claimNo']").val();
			var registNo = $("[name='prpLCompensate.registNo']").val();
			var sumAmtNow = $("[name='prpLCompensate.sumPaidAmt']").val();
			var url="/claimcar/compensate/initNextTaskView.do?compensateId=" + eval(result).data+"&flowTaskId="+flowTaskId+"&claimNo="+claimNo+"&registNo="+registNo+"&sumAmtNow="+sumAmtNow;
			layer.open({
				type : 2,
				title : "理算提交",
				area : [ '75%', '70%' ],
				fix : true, // 不固定
				maxmin : false,
				content : url,
				cancel: function(index){ 
					location.reload(); 
					}, 
			});
		}
	}
	
	//上海保单平台是否通过
	function isPlatformSH(){
		var after = true;
		var params = {"registNo" : $("#registNo").val(),
			      	  "policyNo" : $("#policyNo").val()};
		$.ajax({
			url : "/claimcar/platformAlternately/isPlatformSH.ajax", // 后台校验
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async : false,
			success : function(jsonData) {// 回调方法，可单独定义
				after = eval(jsonData).data;//不成功
			}
		});
		return after;
	}
	
	//管控无法找到第三方（120）和自行协商（121）不能同时勾选
	function checkControl(checkName){
		var resFlag = false;
		var checkVal = $("#claimDeductDiv input[name='"+checkName+"']").parent().next().find("input[name$='deductCondCode']").val();
		if(checkVal=="120"||checkVal=="121" || checkVal=="320"){//120 --无法找到第三方  320--绝对免赔--无法找到第三方
			$("#claimDeductDiv input[name$='isCheck']").each(function(){
				if($(this).val()=="1"){
					var chkVal = $(this).parent().next().find("input[name$='deductCondCode']").val();
					if((checkVal=="120" || checkVal=="320") && chkVal =="121"){
						layer.msg("无法找到第三方和自行协商不能同时勾选！");
						resFlag = true;
						return false;
					}
					
					if(checkVal=="121"&& (chkVal=="120" || chkVal=="320")){
						layer.msg("无法找到第三方和自行协商不能同时勾选！");
						resFlag = true;
						return false;
					}
				}
			});
		}
		
		return resFlag;
	}
	
	//勾选免赔率
	function isCheck(element){
		var checkName = $(element).attr("name");
		var checkFlag = false;
		if(!checkControl(checkName)){
			var value = $(element).attr("value");
			if(value=="0"||value==""){
				$(element).attr("value","1");
				checkFlag = true;
			}else{
				$(element).attr("value","0");
			}
		}else{
			$(element).attr("value","0");
			$(element).prop("checked",false);
			//layer.msg("无法找到第三方和自行协商不能同时勾选");
		}
		var checkVal = $("#claimDeductDiv input[name='"+checkName+"']").parent().next().find("input[name$='deductCondCode']").val();
		var haveKindNT = $("#haveKindNT").val();
		//承保了无法找到第三方 勾选无法找到第三方条件 自动带出附加险
		var dutyRate = $("[name='prpLCompensate.indemnityDuty']").val();
		if(dutyRate!="4" && haveKindNT=="true" && (checkVal=="120" || checkVal=="320") && checkFlag){//120 --无法找到第三方  320--绝对免赔--无法找到第三方
			var otherKindNT = false;
			$("#OtherLossTbody input[name$='kindCode']").each(function(){
				if($(this).val()=="NT"){
					otherKindNT = true;
					return false;
				}
			});
			
			if(!otherKindNT){
				loadOtherPropTr("NT");
			}
		}
		
		
	}
	//点击获取免赔率-责任比例，事故免赔，绝对免赔，加扣免赔赋值
	$("#getAllRate").click(function(){
		rateClick = true;
		//责任比例赋值
		var dutyRate = $("[name='prpLCompensate.indemnityDutyRate']").val();
		$("div#lossTables [name$='dutyRate']").val(dutyRate);
		$("[name$='dutyRate']").blur();
		//发送请求计算免赔率
		var params = $("#compEditForm").serialize();
		$.ajax({
			url : "/claimcar/compensate/getAllRate.do", // 后台处理程序
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async:false, 
			success : function(jsonData) {// 回调方法，可单独定义
				var result = eval(jsonData.data);
				processRateData(result);
			}
		});
		checkNoFindThird();
		//生成计算书按钮是否置灰
		checkManageCompBI();
	});
	
	//未勾选无法找到第三方 并且带出了附加险无法找到第三方 需要删除
	function checkNoFindThird(){
		var noFindThird = false;
		$("#claimDeductDiv input[name$='deductCondCode']").each(function(){
			if($(this).val()=="320" || $(this).val()=="120"){
				var isCheck = $(this).parent().prev().find("input[name$='isCheck']").val();
				if(isCheck=="0"){
					noFindThird = true;
				}
				return false;
			}
		});
		
		if(noFindThird){
			$("#otherLossDiv [name$='kindCode']").each(function(){
				if($(this).val()=='NT'){
					var delButton = $(this).parents("tr").find("button[name^='otherLossVo']");
					$(delButton).click();
					return false;
				}
			});
		}
	}
	
	//对计算出来的免赔率Map进行处理并给相应的输入框赋值
	function processRateData(result){
		//责任免赔
		var dutyRate = result.dutyKindRateMap;//事故免赔
//		$("[name$='deductDutyRate']").val(dutyRate);
		var addMap = result.addKindRateMap;//加扣免赔Map
		var absMap = result.absKindRateMap;//绝对免赔Map
		
		$("[name$='deductDutyRate']").each(function(){
			$kindCodeInp = $(this).parents("td").siblings().find("[name$='kindCode']");
			var kindCode = "";
			if($kindCodeInp.has("option").length!=0){
				kindCode = $kindCodeInp.find("option:selected").val();
				kindCode = $.trim(kindCode);
			}else{
				kindCode = $kindCodeInp.val();
			}
			for(var i in dutyRate){
				if(kindCode==i){
					$(this).val(dutyRate[i]);
				}
			}
			if(isEmpty($(this))){
				$(this).val(0);
			}
		});
		
		$("[name$='deductAddRate']").each(function(){
			$kindCodeInp = $(this).parents("td").siblings().find("[name$='kindCode']");
			var kindCode = "";
			if($kindCodeInp.has("option").length!=0){
				kindCode = $kindCodeInp.find("option:selected").val();
				kindCode = $.trim(kindCode);
			}else{
				kindCode = $kindCodeInp.val();
			}
			for(var i in addMap){
				if(kindCode==i){
					$(this).val(addMap[i]);
				}
			}
			if(isEmpty($(this))){
				$(this).val(0);
			}
		});
		
		$("[name$='deductAbsRate']").each(function(){
			$kindCodeInp = $(this).parents("td").siblings().find("[name$='kindCode']");
			var kindCode = "";
			if($kindCodeInp.has("option").length!=0){
				kindCode = $kindCodeInp.find("option:selected").val();
				kindCode = $.trim(kindCode);
			}else{
				kindCode = $kindCodeInp.val();
			}
			for(var i in absMap){
			if(kindCode==i){
				$(this).val(absMap[i]);
			}
			}
			if(isEmpty($(this))){
				$(this).val(0);
			}
		});
	}
	// 责任类型全责 和 无责 的时候 比例值一定是100 和 0 其他责任类型时候  比例随便输入值，不管控
	$("#indemnityDuty").change(function(){
		$("[name='prpLCompensate.indemnityDuty']").val($("#indemnityDuty").val())
		rateClick = false; 
		BZAmtClick = false;
		compClick = false;
	
		var dutyType = $("[name='prpLCompensate.indemnityDuty']").val();
		var dutyRate = parseFloat($("[name='prpLCompensate.indemnityDutyRate']").val());
		var dutyVa = $("[name='prpLCompensate.indemnityDutyRate']");
		if(dutyType == "0"&&dutyRate != 100.0){
			layer.msg("全责时责任比例为100%");
			$("[name='prpLCompensate.indemnityDutyRate']").val("100.0");
		}
		if(dutyType == "4"&&dutyRate != 0.0){
			layer.msg("无责时责任比例为0%");
			$("[name='prpLCompensate.indemnityDutyRate']").val("0.0");
		}
		if(dutyType != "0"&&dutyRate == 100.0){
			layer.msg("非全责时责任比例不为100%");
			if(dutyType == null || dutyType == ""){
				dutyVa.val("");
			}else if(dutyType == "1"){
				dutyVa.val("70.0");
			}else if(dutyType == "2"){
				dutyVa.val("50.0");
			}else if(dutyType == "3"){
				dutyVa.val("30.0");
			}else if(dutyType == "4"){
				dutyVa.val("0.0");
			}
		}
		if(dutyType != "4"&&dutyRate == 0.0){
			layer.msg("非无责时责任比例不为0%");
			if(dutyType == null || dutyType == ""){
				dutyVa.val("");
			}else if(dutyType == "1"){
				dutyVa.val("70.0");
			}else if(dutyType == "2"){
				dutyVa.val("50.0");
			}else if(dutyType == "3"){
				dutyVa.val("30.0");
			}else if(dutyType == "0"){
				dutyVa.val("100.0");
			}
		}
		
		//责任比例改变需要联动标的车辆责任比例的改变
		var selfIndex = "";
		$("#checkDutySeri").each(function(){
			if($(this).val()=='1'){
				selfIndex = $(this).attr("name").substring(20,21);
			}
		});
		$("[name='prpLCheckDutyVoList["+selfIndex+"].indemnityDuty']").find("option[value='"+dutyType+"']").prop("selected","selected");
		var newRate = dutyVa.val();
		$("[name='prpLCheckDutyVoList["+selfIndex+"].indemnityDutyRate']").val(newRate);
		// 同时联动交强有责无责
		ciIndemDuty(dutyType,selfIndex);
	});
	
	// 责任比例选择和比较
	$("[name='prpLCompensate.indemnityDutyRate']").change(function(){
		$("#indemnityDuty").trigger("change");
	});
	
	
	//绝对免赔率界面初始化的选中
	function checkAbsInit(){
		$("[name$='isCheck']").each(function(){
			if($(this).val()=="1"){
				$(this).prop("checked",true);
			}
		});
	}
	
	//点击展示免赔率信息
	function showRate(element){
		$rateDiv = $(element).next();
		var rIndex = layer.open({
			type: 1,
	     	area: ['300px', '180px'],
	     	title:'免赔率信息',
	    	fix: false, //不固定
	    	move: false,
	    	scrollbar: false,
	     	maxmin: false,
	    	content: $rateDiv
		});
	}
	
	function showFee(element,lossItemNo){
		$rateDiv = $(element).next();
		var titleStr = "";
		if(lossItemNo == "02"){
			titleStr = "死亡伤残赔付详情";
		}else{
			titleStr = "医疗费用赔付详情";
		}
		var rIndex = layer.open({
			type: 1,
	     	area: ['400px', '200px'],
	     	title:titleStr,
	    	fix: false, //不固定
	    	move: false,
	    	scrollbar: false,
	     	maxmin: false,
	    	content: $rateDiv
		});
	}
	
	//点击展示免赔率信息
	var rIndex ;
	function showDw(element){
		$rateDiv = $(element).next();
		rIndex = layer.open({
			type: 1,
	     	area: ['1000px', '450px'],
	     	title:'代位金额明细',
	    	fix: false, //不固定
	    	move: false,
	    	scrollbar: false,
	     	maxmin: false,
	    	content: $rateDiv
		});
	}
	
	function closeDw(element){
		layer.close(rIndex); 
	}
	//扣交强金额赋值
	$("#getBZAmt").click(function(){
		BZAmtByAbsolvePayAmt = true;
		BZAmtClick = true;
		bIGetBZAmt();
	/*	$("input[name$='feeOffLoss']").blur();
		$("input[name$='feeRealPay']").blur();*/
		//扣交强总金额
		var bzPaidALL = 0;
		$("[name$='bzPaidLoss']").each(function(){
			if($(this).val()!=null&&$(this).val()!=""){
				bzPaidALL+=parseFloat($(this).val());
			}
			
		});
		
		$("[name$='bzPaidRescueFee']").each(function(){
			if($(this).val()!=null&&$(this).val()!=""){
				bzPaidALL += parseFloat($(this).val());
			}
		});
		//扣交强总金额  prpLLossPerson 人伤有子项需要算重复了
		var perBzPaidALL = 0;
		$("[id ^='lossPersDiv_']").each(function(){
			perBzPaidALL +=parseFloat($(this).find("input[name$='bzPaidLoss']").val());
		});
		bzPaidALL=bzPaidALL-perBzPaidALL;
		$("span#bzPaidALL").text(bzPaidALL.toFixed(2));
		$("span#bzPaidALL").next().val(bzPaidALL.toFixed(2));
		//生成计算书按钮是否置灰
		checkManageCompBI();
	});
	//理算注销
	
	function compCancel(){
		
		
		var flowTaskId = $("[name='flowTaskId']").val();
		var compensateNo = $("#compensateNo").text();
		var claimNo = $("[name='prpLCompensate.claimNo']").val();

		$.ajax({
			url : "/claimcar/compensate/compCancel.do?claimNo="+claimNo+"", // 后台处理程序
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			//data : params, // 要传递的数据
			async:false, 
			success : function(jsonData) {// 回调方法，可单独定义
				if(jsonData.statusText=="12"){
					layer.confirm('商业理算未注销，不能发起定损修改任务，是否注销交强计算书？', {
						btn: ['是','否'] //按钮
					}, function(index){
						$.ajax({
							url : "/claimcar/compensate/compCancels.do?compensateNo="+compensateNo+"&flowTaskId="+flowTaskId+"&claimNo="+claimNo+"", // 后台处理程序
							type : 'post', // 数据发送方式
							dataType : 'json', // 接受数据格式
							//data : params, // 要传递的数据
							async:false, 
							success : function(jsonData) {// 回调方法，可单独定义
								if(jsonData.status=="200"){
									layer.alert("理算注销成功！");
										$("#save").removeAttr("onclick");
										$("#save").addClass("btn-disabled");
										$("#save").removeClass("btn-primary");
										$("#pend").removeAttr("onclick");
										$("#pend").addClass("btn-disabled");
										$("#pend").removeClass("btn-primary");
										$(":input").attr("disabled","disabled");
										$(":input[type='button']").addClass("btn-disabled");
										$(".nodisabled").removeAttr("disabled");
										$(".nodisabled").removeClass("btn-disabled");
										$("#CompCancel").prop("disabled",true);
										parent.layer.closeAll();
									
								}
							}});
					});
					
				}
				if(jsonData.statusText=="11"){
					layer.confirm('交强理算未注销，不能发起定损修改任务，是否注销商业计算书？', {
						btn: ['是','否'] //按钮
					}, function(index){
						$.ajax({
							url : "/claimcar/compensate/compCancels.do?compensateNo="+compensateNo+"&flowTaskId="+flowTaskId+"&claimNo="+claimNo+"", // 后台处理程序
							type : 'post', // 数据发送方式
							dataType : 'json', // 接受数据格式
							//data : params, // 要传递的数据
							async:false, 
							success : function(jsonData) {// 回调方法，可单独定义
								layer.alert("理算注销成功！");
								$("#save").removeAttr("onclick");
								$("#save").addClass("btn-disabled");
								$("#save").removeClass("btn-primary");
								$("#pend").removeAttr("onclick");
								$("#pend").addClass("btn-disabled");
								$("#pend").removeClass("btn-primary");
								$(":input").attr("disabled","disabled");
								$(":input[type='button']").addClass("btn-disabled");
								$(".nodisabled").removeAttr("disabled");
								$(".nodisabled").removeClass("btn-disabled");
								$("#CompCancel").prop("disabled",true);
								parent.layer.closeAll();
								
							}});
					});
				}
				//结案前可发起定损修改
				//发起任务
				//alert(jsonData.statusText);
				if(jsonData.statusText=="0"){
					var registNo = $("#registNo").val();
					var url="/claimcar/compensate/compCancelInit.do?registNo="+registNo;
					FaIndex=layer.open({
					    type: 2,
					    title: '发起',
					    shade: true,
					    area: ['1000px', '500px'],
					    content:[url],
					    end:function(){
					    }
					});
				}
				if(jsonData.statusText=="1"){
					$.ajax({
						url : "/claimcar/compensate/compCancels.do?compensateNo="+compensateNo+"&flowTaskId="+flowTaskId+"&claimNo="+claimNo+"", // 后台处理程序
						type : 'post', // 数据发送方式
						dataType : 'json', // 接受数据格式
						//data : params, // 要传递的数据
						async:false, 
						success : function(jsonData) {// 回调方法，可单独定义
							layer.alert("理算注销成功！");
							$("#save").removeAttr("onclick");
							$("#save").addClass("btn-disabled");
							$("#save").removeClass("btn-primary");
							$("#pend").removeAttr("onclick");
							$("#pend").addClass("btn-disabled");
							$("#pend").removeClass("btn-primary");
							$(":input").attr("disabled","disabled");
							$(":input[type='button']").addClass("btn-disabled");
							$(".nodisabled").removeAttr("disabled");
							$(".nodisabled").removeClass("btn-disabled");
							$("#CompCancel").prop("disabled",true);
							parent.layer.closeAll();
							
						}});
				}
				
			}});
		
		/*layer.confirm('是否注销此任务?', {
			btn: ['确定','取消'] //按钮
		}, function(index){
			$.ajax({
				url : "/claimcar/compensate/compCancel.do?compensateNo="+compensateNo+"&flowTaskId="+flowTaskId+"&claimNo="+claimNo+"", // 后台处理程序
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				//data : params, // 要传递的数据
				async:false, 
				success : function(jsonData) {// 回调方法，可单独定义
					if(jsonData.status=="200"){
						layer.confirm('理算注销成功！', {
							btn: ['确定','取消'] //按钮
						}, function(index){
						$(":input").attr("disabled","disabled");
						$(":input[type='button']").addClass("btn-disabled");
						$(".nodisabled").removeAttr("disabled");
						$(".nodisabled").removeClass("btn-disabled");
						$("#CompCancel").prop("disabled",true);
					
					//结案前可发起定损修改
					//发起任务
					//alert(jsonData.statusText);
					if(jsonData.statusText=="0"){
						var registNo = $("#registNo").val();
						var url="/claimcar/compensate/compCancelInit.do?registNo="+registNo;
						FaIndex=layer.open({
						    type: 2,
						    title: '发起',
						    shade: true,
						    area: ['1000px', '500px'],
						    content:[url],
						    end:function(){
						    }
						});
					}
					if(jsonData.statusText=="12"){
						layer.alert("商业未注销，不能发起定损修改");
					}
					if(jsonData.statusText=="11"){
						layer.alert("交强未注销，不能发起定损修改");
					}
					//end
						});
						
					}
				}
			});
		});*/
	
	}
	//发起定损
	 
	function submitDloss(){
		var carVosList = $("input[name='carVos']:checked");
		var persInjuredVoList =$("input[name='persInjuredVo']:checked");
		var propVosList =$("input[name='propVos']:checked");
		if(carVosList.length==0 && persInjuredVoList.length==0 && propVosList.length==0){
			layer.msg("请勾选需要退回的定损节点！");
			return;
		}
		var flowTaskId = parent.$("[name='flowTaskId']").val();
		var compensateNo = parent.$("#compensateNo").text();
		var persInjuredVo = new Array();
		$("input[name='persInjuredVo']:checked").each(function() {
			persInjuredVo.push($(this).val());
		});
		var carVos = new Array();
		$("input[name='carVos']:checked").each(function() {
			carVos.push($(this).val());
		});
		var propVos = new Array();
		$("input[name='propVos']:checked").each(function() {
			propVos.push($(this).val());
		});
		var remarks = $("[name='remarks']").val();
		
		//
		if(persInjuredVo.length>0){
			var url = "/claimcar/compensate/loadSubmitChargeAdjust.ajax";
			$.post(url, function(result) {
				// 页面层
				layer.open({
					title : "费用审核修改提交",
					type : 1,
					skin : 'layui-layer-rim', // 加上边框
					area : ['520px', '320px'], // 宽高
					async : false,
					content : result,
					yes : function(index, layero) {
						var html = layero.html();
						layer.closeAll();
						$.ajax({
							url  : "/claimcar/compensate/compCancelSubmit.do?persInjuredVo="+persInjuredVo+"&propVos="+propVos+"&carVos="+carVos,
							type : 'post', // 数据发送方式
							dataType : 'json', // 接受数据格式
							data : {
								"remarks":remarks,
								"compensateNo":compensateNo,
								"flowTaskId":flowTaskId,
							}, // 要传递的数据
							async:false, 
							traditional: true, 
							success : function(jsonData) {// 回调方法，可单独定义
								 if(jsonData.status=="200"&&jsonData.statusText==""){
									 layer.confirm('发起成功!', {
											btn: ['确定'] //按钮
										}, function(index){
											parent.$("#save").removeAttr("onclick");
											parent.$("#save").addClass("btn-disabled");
											parent.$("#pend").removeAttr("onclick");
											parent.$("#pend").addClass("btn-disabled");
											parent.$("#save").removeClass("btn-primary");
											parent.$("#pend").removeClass("btn-primary");
											parent.$(":input").attr("disabled","disabled");
											parent.$(":input[type='button']").addClass("btn-disabled");
											parent.$(".nodisabled").removeAttr("disabled");
											parent.$(".nodisabled").removeClass("btn-disabled");
											parent.$("#CompCancel").prop("disabled",true);
											parent.layer.closeAll();
										});
								 }else{
									 layer.confirm(jsonData.statusText, {
											btn: ['确定'] //按钮
										}, function(index){
											parent.$("#save").removeAttr("onclick");
											parent.$("#save").addClass("btn-disabled");
											parent.$("#pend").removeAttr("onclick");
											parent.$("#pend").addClass("btn-disabled");
											parent.$("#save").removeClass("btn-primary");
											parent.$("#pend").removeClass("btn-primary");
											parent.$(":input").attr("disabled","disabled");
											parent.$(":input[type='button']").addClass("btn-disabled");
											parent.$(".nodisabled").removeAttr("disabled");
											parent.$(".nodisabled").removeClass("btn-disabled");
											parent.$("#CompCancel").prop("disabled",true);
											parent.layer.closeAll();
										});
								 }
								
								
								}
						});
						//var url = "/claimcar/compensate/compCancelSubmit.do?persInjuredVo="+persInjuredVo+"&propVos="+propVos+"&carVos="+carVos+"&remarks="+remarks;
						/*$.post(url, function(jsonData) {
							//search();
							layer.close(index); //关闭页面层
							layer.close(FaIndex); //关闭页面层
							alert(FaIndex);
							layer.closeAll();
							layer.alert("发起成功!");
						});*/
						
					}
				});
			});
		} else {
			
			$.ajax({
				url  : "/claimcar/compensate/compCancelSubmit.do?persInjuredVo="+persInjuredVo+"&propVos="+propVos+"&carVos="+carVos,
				type : 'post', // 数据发送方式
				 dataType : 'json', // 接受数据格式
				data : {
					"remarks":remarks,
					"compensateNo":compensateNo,
					"flowTaskId":flowTaskId,
				}, // 要传递的数据
				async:false, 
				traditional: true, 
				success : function(jsonData) {// 回调方法，可单独定义
				if(jsonData.status=="200"&&jsonData.statusText==""){
					layer.confirm('发起成功!', {
						btn: ['确定'] //按钮
					}, function(index){
						parent.$("#save").removeAttr("onclick");
						parent.$("#save").addClass("btn-disabled");
						parent.$("#pend").removeAttr("onclick");
						parent.$("#pend").addClass("btn-disabled");
						parent.$("#save").removeClass("btn-primary");
						parent.$("#pend").removeClass("btn-primary");
						parent.$(":input").attr("disabled","disabled");
						parent.$(":input[type='button']").addClass("btn-disabled");
						parent.$(".nodisabled").removeAttr("disabled");
						parent.$(".nodisabled").removeClass("btn-disabled");
						parent.$("#CompCancel").prop("disabled",true);
						parent.layer.closeAll();
					});
					}else{
						layer.confirm(jsonData.statusText, {
							btn: ['确定'] //按钮
						}, function(index){
							parent.$("#save").removeAttr("onclick");
							parent.$("#save").addClass("btn-disabled");
							parent.$("#pend").removeAttr("onclick");
							parent.$("#pend").addClass("btn-disabled");
							parent.$("#save").removeClass("btn-primary");
							parent.$("#pend").removeClass("btn-primary");
							parent.$(":input").attr("disabled","disabled");
							parent.$(":input[type='button']").addClass("btn-disabled");
							parent.$(".nodisabled").removeAttr("disabled");
							parent.$(".nodisabled").removeClass("btn-disabled");
							parent.$("#CompCancel").prop("disabled",true);
							parent.layer.closeAll();
						});
					}
				}
			});
		}
		
	}
	//收款人选择界面打開
	var payIndex=null;
	function showPayCust(element){
		var registNo = $("#registNo").val();
		var tdName = $(element).attr("name");
		var trIndex = tdName.substring(12,13);
		var dwPayFlag = $("[name='prpLPayment["+trIndex+"].payFlag']").val();
		var compFlag = "comp";
		var url="/claimcar/payCustom/payCustomList.do?registNo="+registNo+"&tdName="+tdName+"&compFlag="+compFlag+"&flag=1"+"&dwPayFlag="+dwPayFlag;
		if(payIndex==null){
			payIndex=layer.open({
			    type: 2,
			    title: '收款人选择',
			    shade: false,
			    area: ['1000px', '500px'],
			    content:url,
			    end:function(){
			    	payIndex=null;
			    	$(element).blur();
			    	otherFlagCon();
			    	paymentVatInvoiceFlagCon(trIndex);
			    }
			});
		}
	}
	//费用
	function showPayCusts(element){
		var registNo = $("#registNo").val();
		var tdName = $(element).attr("name");
		var trIndex = tdName.substring(11,12);
		var compFlag = "comp";
		var url="/claimcar/payCustom/payCustomList.do?registNo="+registNo+"&tdName="+tdName+"&compFlag="+compFlag+"&flag=2"+"";
		if(payIndex==null){
			payIndex=layer.open({
			    type: 2,
			    title: '收款人选择',
			    shade: false,
			    area: ['1000px', '500px'],
			    content:url,
			    end:function(){
			    	payIndex=null;
			    	$(element).blur();
			    	otherFlagCon();
			    	chargeVatInvoiceFlagCon(trIndex);
			    }
			});
		}
	}
	//各项费用合计方法
	function amtSum(){
		//责任赔款合计
		var sumLossALL = 0;
		var payment = 0;
		
		$("[name$='sumRealPay']").each(function(){
//			var payFlag = $(this).parents("tr").find("input[name$='payFlag']").val();
			if($(this).val()!=null&&$(this).val()!=""&&$(this).attr("id")!="compSum"){
				sumLossALL+=parseFloat($(this).val());
			}
		});
		$("#paymentVo input[name$='sumRealPay']").each(function(){
			if($(this).val()!=null&&$(this).val()!=""&&$(this).attr("id")!="compSum"){
				payment+=parseFloat($(this).val());
			}
		});
		//诉讼协议金额
		//总赔款金额=核定金额之和
		//总赔付金额
/*		var agreeAmt = 0;
		agreeAmt = $("[name='prpLCompensate.agreeAmt']").val();//协议金额
		if(isBlank(agreeAmt)){//TODO 这块逻辑是否有问题
			agreeAmt = 0;
		}*/
		var sumRealPayALL = sumLossALL-payment;
		//sumRealPayALL+=parseFloat(agreeAmt);
		$("span#sumLossALL").text(sumRealPayALL.toFixed(2));
		$("span#sumLossALL").next().val(sumRealPayALL.toFixed(2));
		var allPreAmtPers = 0;
		var allPreAmtP=0;
		var allPreAmtF=0;
		//垫付总金额
		$("#PaidPersTables [id^='padAmt']").each(function(){
			var padAmt = $(this).text();
			if(!isBlank(padAmt)){
				allPreAmtPers+=parseFloat(padAmt);
			}
		});
		//预赔赔款
		$("#prePaidPTables [id^='preAmt']").each(function(){
			var preAmt = $(this).text();
			if(!isBlank(preAmt)){
				allPreAmtP+=parseFloat(preAmt);
			}
		});
		//预赔费用
		$("#prePaidFTables [id^='preAmt']").each(function(){
			var preAmt = $(this).text();
			if(!isBlank(preAmt)){
				allPreAmtF+=parseFloat(preAmt);
			}
		});
		
	/*	alert(allPreAmtP);
		alert(allPreAmtF);
		alert(allPreAmtPers);*/
		allPreAmtPers=allPreAmtPers+allPreAmtP;
		$("span#preSumALL").text(allPreAmtPers.toFixed(2));
		$("span#preSumALL").next().val(allPreAmtPers.toFixed(2));
		//本次赔款
		//本次赔款金额=总赔款金额-预付垫付赔款
		var sumThisALL = sumRealPayALL-allPreAmtPers;
		$("span#sumThisALL").text(sumThisALL.toFixed(2));
		$("span#sumThisALL").next().val(sumThisALL.toFixed(2));
		//总费用金额=费用金额之和
		var feeALL = 0;
		var offAmt = 0;
		$("#feeTables [name$='feeAmt']").each(function(){
			if($(this).val()!=null&&$(this).val()!=""){
				feeALL+=parseFloat($(this).val());
			}
		});
		$("#feeTables [name$='offAmt']").each(function(){
			if($(this).val()!=null&&$(this).val()!=""){
				offAmt+=parseFloat($(this).val());
			}
		});
		feeALL=feeALL-offAmt;
		$("span#feeALL").text(feeALL.toFixed(2));
		$("span#feeALL").next().val(feeALL.toFixed(2));
		//预付费用=预付费用之和
//		$("#prePaidFTables [id^='padAmt']").each(function(){
//			var padAmt = $(this).val();
//			if(!isBlank(padAmt)){
//				allPreAmtPerss+=parseFloat(padAmt);
//			}
//		});
		$("span#allPreAmtPerss").text(allPreAmtF.toFixed(2));
		$("span#allPreAmtPerss").next().val(allPreAmtF.toFixed(2));
		
		//本次赔付费用=总费用金额-预付费用
		var feeALLtoallPreAmt = feeALL - allPreAmtF;
		$("span#feeALLtoallPreAmt").text(feeALLtoallPreAmt.toFixed(2));
		$("span#feeALLtoallPreAmt").next().val(feeALLtoallPreAmt.toFixed(2));
		/*sumLossALL=sumLossALL-payment;
		$("span#sumLossALL").text(sumLossALL.toFixed(2));
		$("span#sumLossALL").next().val(sumLossALL.toFixed(2));*/
		//不计免赔合计
		var deductOffALL = 0;
		/*$("[name^='deductOffAmt']").each(function(){
			if($(this).val()!=null&&$(this).val()!=""){
				deductOffALL+=parseFloat($(this).val());
			}
		});*/
		$("[name$='deductOffAmt']").each(function(){
			if($(this).val()!=null&&$(this).val()!=""){
				if($("#dwFlag").val() == '1'||$("#dwFlag").val() == '3'){
					var kindCode = $(this).parents("tr").find("input[name$='kindCode']").val();
					var payFlag = $(this).parents("tr").find("input[name$='payFlag']").val();
					if(kindCode == 'A' && payFlag == '3' ){
						deductOffALL = deductOffALL;// 代位时，不计免赔累计不累加A险正常自付的那一条
					}else{
						deductOffALL+=parseFloat($(this).val());
					}
				}else{
					deductOffALL+=parseFloat($(this).val());
				}
			}
		});
		$("span#deductOffALL").text(deductOffALL.toFixed(2));
		$("span#deductOffALL").next().val(deductOffALL.toFixed(2));
		//赋值不计免赔赔款
		$("#buji input[name$='kindCodes']").each(function(){
				var deductALL = 0;
				var a = 0;
				var isNew = $(this).val();
				var temp = isNew.charAt(isNew.length - 1);
				if(temp!="M"){//旧条款
					$("#bujimian [name$='kindCode']").each(function(){	
						var s = $(this).val();
						if(isNew == s){
							//判断是否为人
							var name1 = $(this).parent().parent().parent().parent().attr("name");
							//alert("211"+name1);
							var name = name1.split("_")[0];
							if(name=="personkindcode"){//人
								var index = $(this).parent().parent().parent().parent().attr("name").split("_")[1];
								var b=0;
								$(this).parent().parent().parent().parent().siblings().find("input[name$='deductOffAmt']").each(function(){
									b = b+parseFloat($(this).val());
								});
								a=b;
							}else if(name=='cpo'){//财跟人
								a = parseFloat($(this).parent().parent().find("input[name$='deductOffAmt']").val());
							}
							if(!(isNaN(a))&&typeof(a) != "undefined"){
							deductALL=deductALL+a;
							}
						}
					});
					$(this).next().find("input[name$='kindCodees']").val(deductALL.toFixed(2));
					//$(this).parents("tr").find("input[name$='kindCodees']").val(deductOffALL.toFixed(2));
				} else if(isNew=="M"){
					$(this).next().find("input[name$='kindCodees']").val(deductOffALL.toFixed(2));
				} else {//新条款
					$("#bujimian [name$='kindCode']").each(function(){
						// 遍历所有损失项信息
						var s = $(this).val()+"M";
						//alert(isNew+"=="+s);
						if(isNew == s){
							//alert($(this).parents("tr").find("input[name$='sumRealPay']").siblings().val());deductOffAmt
							//判断是否为人
							var name1 = $(this).parent().parent().parent().parent().attr("name");
							//alert("211"+name1+isNew+s);
							var name = name1.split("_")[0];
							if(name=="personkindcode"){//人
								var index = $(this).parent().parent().parent().parent().attr("name").split("_")[1];
								
								//a = parseFloat($("table name[personfee_"+index+"]").find("input[name$='deductOffAmt']").val());
								var b=0;
								$(this).parent().parent().parent().parent().siblings().find("input[name$='deductOffAmt']").each(function(){
									b = b+parseFloat($(this).val());
								});
								//a = a+ parseFloat($(this).parent().parent().parent().parent().siblings().find("input[name$='deductOffAmt']").val());
								//alert(a+"人"+index+b);
								a=b;
							}else if(name=='cpo'){//财跟人
								if($("#dwFlag").val() == '1'||$("#dwFlag").val() == '3'){
	//								alert($(this).parent().parent().find("input[name$='deductOffAmt']").attr("name"));
									var kindCode = $(this).val();
									var payFlag = $(this).parents("tr").find("input[name$='payFlag']").val();
									if(kindCode == 'A' && payFlag == '3' ){
										a = a;// 代位时，不计免赔累计不累加A险正常自付的那一条
									}else{
										a = parseFloat($(this).parent().parent().find("input[name$='deductOffAmt']").val());
									}
								}else{
									a = parseFloat($(this).parent().parent().find("input[name$='deductOffAmt']").val());
								}
								//a = parseFloat($(this).parent().parent().find("input[name$='deductOffAmt']").val());
								//alert(a+"财车");
							}
							//zhu
							//var a = parseFloat($(this).parents("tr").find("input[name$='deductOffAmt']").val());
							
							if(!(isNaN(a))&&typeof(a) != "undefined"){
							deductALL=deductALL+a;
							}
						}
					});
					$(this).next().find("input[name$='kindCodees']").val(deductALL.toFixed(2));
				}
		});
		
	
		
	/*	var preSumALL = 0;
		$("[name$='offPreAmt']").each(function(){
			if($(this).val()!=null&&$(this).val()!=""){
				preSumALL+=parseFloat($(this).val());
			}
		});*/
	/*	$("span#preSumALL").text(allPreAmtPers.toFixed(2));
		$("span#preSumALL").next().val(allPreAmtPers.toFixed(2));*/
		
		
		//扣交强总金额
		var bzPaidALL = 0;
		$("[name$='bzPaidLoss']").each(function(){
			if($(this).val()!=null&&$(this).val()!=""){
				if($("#dwFlag").val() == '1'||$("#dwFlag").val() == '3'){
					var kindCode = $(this).parents("tr").find("input[name$='kindCode']").val();
					var payFlag = $(this).parents("tr").find("input[name$='payFlag']").val();
//					alert("bzPaidLoss"+kindCode+payFlag);
					if(kindCode == 'A' && payFlag == '3' ){
						bzPaidALL = bzPaidALL;// 代位时，定损扣交强累计不累加A险正常自付的那一条
					}else{
						bzPaidALL+=parseFloat($(this).val());
					}
				}else{
					bzPaidALL+=parseFloat($(this).val());
				}
			}
		});
		
		$("[name$='bzPaidRescueFee']").each(function(){
			if($(this).val()!=null&&$(this).val()!=""){
				bzPaidALL += parseFloat($(this).val());
			}
		});
		//扣交强总金额prpLLossPerson
		var perBzPaidALL = 0;
		$("[id ^='lossPersDiv_']").each(function(){
			perBzPaidALL +=parseFloat($(this).find("input[name$='bzPaidLoss']").val());
		});
		
		//汇总无责代赔金额
		var absolvePayAmt = 0;
		$("[name$='absolvePayAmt']").each(function(){
			if($(this).val()!=null&&$(this).val()!=""){
				absolvePayAmt += parseFloat($(this).val());
			}
		});
		//再加上无责代赔金额
		bzPaidALL=bzPaidALL-perBzPaidALL+absolvePayAmt;
		$("span#bzPaidALL").text(bzPaidALL.toFixed(2));
		$("span#bzPaidALL").next().val(bzPaidALL.toFixed(2));
		//扣交强总金额
/*		var bzPaidALLs = 1;
		$("[name$='bzPaidLoss']").each(function(){
			if($(this).val()!=null&&$(this).val()!=""){
				bzPaidALLs+=parseFloat($(this).val());
			}
		});
		$("span#bzPaidALL").text(bzPaidALLs.toFixed(2));
		$("span#bzPaidALL").next().val(bzPaidALLs.toFixed(2));	*/
	
/*		//预付费用=预付费用之和
		$("#prePaidFTables [id^='padAmt']").each(function(){
			var padAmt = $(this).text();
			if(!isBlank(padAmt)){
				allPreAmtPerss+=parseFloat(padAmt);
			}
		});*/
		feeProcess();
		fraudPayZero();
	}
	//页面提交校验
	function submitCheck(){
		amtSum();
		var resMsg = "OK";
		var dutySel = $("[name='prpLCompensate.indemnityDuty']").val();
		if(isBlank(dutySel)){
			return "责任类型不能为空";
		}
		$("#PayCustomTbody [name$='sumRealPay']").each(function(){
			if(isBlank($(this).val())){
				resMsg="1";
				return "请录入收款人赔款金额！";
			}
		});
		var sum=0;
		var dfSum = parseFloat(0.00);//代付收款人金额
		var qfSum = parseFloat(0.00);//清付收款人金额
		var zfSum = parseFloat(0.00);//自付收款人金额
		
		var carLossPreSum = parseFloat(0.00);//代付预付金额
		var propLossPreSum = parseFloat(0.00);//清付预付金额
		var persLossPreSum = parseFloat(0.00);//自付预付金额
		
		var sumThisALL = parseFloat($("#sumThisALL").text()).toFixed(2);
		$("#PayCustomTbody [name$='sumRealPay']").each(function(){
			var payFlag = $(this).parents("tr").find("select[name$='payFlag']").val();
			var thisPay = parseFloat($(this).val());
			if(payFlag=="1"){//代付
				dfSum=parseFloat(dfSum)+thisPay; 
			}else if(payFlag=="2"){//清付
				//qfSum=parseFloat(qfSum)+thisPay; 
			}else if(payFlag=="3"){//自付
				zfSum=parseFloat(zfSum)+thisPay; 
			}
			sum=parseFloat(sum)+thisPay;
		});
		
		//代付,清付,自付再减去预付金额
		sum = sum.toFixed(2);
		var dfLossSum = parseFloat(0.00);//代付核对金额
//		var qfLossSum = parseFloat(0.00);//清付核对金额
		var zfLossSum = parseFloat(0.00);//自付核对金额
		var qfMessage= "";
		$("#carLossDiv [name$='sumRealPay']").each(function(){
			var payFlag = $(this).parents("tr").find("input[name$='payFlag']").val();
			var thisPay = parseFloat($(this).val());
			var itemId =  $(this).parents("tr").find("input[name$='itemId']").val();
			if(payFlag=="1"){//代付
				if(!isBlank($(this).parents("tr").find("input[name$='offPreAmt']").val())){
					carLossPreSum = $(this).parents("tr").find("input[name$='offPreAmt']").val();
				}
				dfLossSum = parseFloat(dfLossSum)+thisPay-parseFloat(carLossPreSum);
			}else if(payFlag=="2"){//清付
				//qfLossSum = parseFloat(qfLossSum)+thisPay; 
				var qfLossSum = parseFloat(0.00);
				var itemName = $(this).parents("tr").find("input[name$='itemName']").val();
				$("#PayCustomTbody [name$='sumRealPay']").each(function(){
					var payFlag2 = $(this).parents("tr").find("select[name$='payFlag']").val();
					var payItemId =  $(this).parents("tr").find("select[name$='itemId']").val();
					var thisPay2 = parseFloat($(this).val());
					if(payItemId==itemId && payFlag2 =="2"){
						qfLossSum = parseFloat(qfLossSum)+thisPay2; 
					}
				});
				var qfOffPreAmt2 = 0;
				if(!isBlank($(this).parents("tr").find("input[name$='offPreAmt']").val())){
					qfOffPreAmt2 = $(this).parents("tr").find("input[name$='offPreAmt']").val();
				}
				qfOffPreAmt2 = thisPay - parseFloat(qfOffPreAmt2);
				if(qfOffPreAmt2.toFixed(2) != qfLossSum.toFixed(2)){
					qfMessage = "收款人中清付"+itemName+" 的赔款金额不等于核定金额，不能提交！";
					return false;
				}
			}else if(payFlag=="3"){
				if(!isBlank($(this).parents("tr").find("input[name$='offPreAmt']").val())){
					carLossPreSum = $(this).parents("tr").find("input[name$='offPreAmt']").val();
				}
				zfLossSum = parseFloat(zfLossSum)+thisPay-parseFloat(carLossPreSum);
			}
		});
		
		$("#propLossDiv [name$='sumRealPay']").each(function(){
			var thisPay = parseFloat($(this).val());
			if(!isBlank($(this).parents("tr").find("input[name$='offPreAmt']").val())){
				propLossPreSum = $(this).parents("tr").find("input[name$='offPreAmt']").val();
			}
			zfLossSum = parseFloat(zfLossSum)+thisPay-parseFloat(propLossPreSum); 
		});
		
		$("#persLossDiv [name$='sumRealPay']").each(function(){
			var thisPay = parseFloat($(this).val());
			zfLossSum = parseFloat(zfLossSum)+thisPay; 
			
		});
		
		$("#persLossDiv [name$='offPreAmt']").each(function(){
			if(!isBlank($(this).val())){
				var thisPay = parseFloat($(this).val());
				persLossPreSum = parseFloat(persLossPreSum) + thisPay;
			}
		});
		
		var sumPersonP = 0;
		$("[id ^='lossPersDiv_']").each(function(){
			if(!isBlank($(this).find("input[name$='offPreAmt']").val())){
				sumPersonP +=parseFloat($(this).find("input[name$='offPreAmt']").val());
			}
		});
		zfLossSum = parseFloat(zfLossSum) - parseFloat(persLossPreSum) + parseFloat(sumPersonP);
		if(!isBlank(qfMessage)){
			return qfMessage;
		}
		if($("#dwFlag").val()!="0"){
			if(dfSum.toFixed(2) != dfLossSum.toFixed(2)){
				return "收款人中代付的赔款金额不等于核定金额，不能提交！";
			}

			if(zfSum.toFixed(2) != zfLossSum.toFixed(2)){
				return "收款人中自付的赔款金额不等于核定金额，不能提交！";
			}
		}
		
		if(sumThisALL!=sum&&compWfFlag!='1'&&compWfFlag!='2'){
			resMsg="1";
			return "收款人赔款金额不等于本次赔款金额不能提交！";
		}
		var lawsuitFlag  = $("input[name$='lawsuitFlag']:checked").val();
		var allowFlag  = $("input[name$='allowFlag']:checked").val();
		var agreeAmt = 0;
		agreeAmt = $("[name='prpLCompensate.agreeAmt']").val();//协议金额
		if(isBlank(agreeAmt)){
			agreeAmt = 0;
		}
		if(lawsuitFlag==1||allowFlag==1){
			if(sumThisALL!=parseFloat(agreeAmt)){
				resMsg="1";
				return "收款人赔款金额不等于协议金额不能提交！";
			}
		}
		
		if(resMsg!="1"){
			$("[name$='sumRealPay']").each(function(){
				if(isBlank($(this).val())){
					resMsg = "核定金额不能为空";
				}
			});
		}
		//判断小于损失金额+施救费-定损扣交强施救费扣交强
		$("#otherLossDiv [name$='kindCode']").each(function(){
			if($(this).val()=='F'){
				var bijiao = 0;
				$("#carLossDiv [name$='kindCode']").each(function(){
					if($(this).val()=='A'){
						var sumLoss=$(this).parents("tr").find("input[name$='sumLoss']").val();
						var rescueFee=$(this).parents("tr").find("input[name$='rescueFee']").val();
						var bzPaidLoss=$(this).parents("tr").find("input[name$='bzPaidLoss']").val();
						var bzPaidRescueFee=$(this).parents("tr").find("input[name$='bzPaidRescueFee']").val();
						bijiao=Number(sumLoss)+Number(rescueFee)-Number(bzPaidLoss)-Number(bzPaidRescueFee);
					}
				});
			}
		});
		if(resMsg!="OK"){
			if(resMsg=="1"){
				resMsg="请录入收款人赔款金额！";
			}
			return resMsg;
		}
		//人伤赋值预付金额赋值：
		var deductType = $("[name='prpLCompensate.deductType']").val();
		if(deductType!="3"){
			$("table[name^='personfee_']").each(function(){
				var sumAmt = 0.00;
				var tableName = $(this).attr("name");
				var index = tableName.split("_")[1];
				$(this).find("input[name$='offPreAmt']").each(function(){
					var offPreAmt = $(this).val();
					if(!isBlank(offPreAmt)){
						sumAmt += parseFloat(offPreAmt);
					}
				});
				
				var personSumAmtDiv = "input[name='prpLLossPerson["+index + "].offPreAmt']";
				$(personSumAmtDiv).val(sumAmt.toFixed(2));
			});
		}
			
		
		
		resMsg = prePaidCheck();
		if(resMsg!="OK"){
			return resMsg;
		}
		//获取是否反洗钱成功的标志位
		function getIndex(){
			var registNo = $("#registNo").val();
			var index = 0;
			$.ajax({
				url:"/claimcar/compensate/getHisSumLoss.ajax",
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				data : {"registNo":registNo}, // 要传递的数据
				async:false, 
				success : function(jsonData) {// 回调方法，可单独定义
					var result = eval(jsonData);
					if(result.status == "200"){
					index = result.data;
					}
					}
			});
			return index;
		}
		//该险别的实赔金额不能超过该险别的保额和该险别对应的总定损金额
		var params = $("#compEditForm").serialize();
			$.ajax({
				url : "/claimcar/compensate/checkKindAmt.ajax", // 后台处理程序
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				data : params, // 要传递的数据
				async:false, 
				success : function(jsonData) {// 回调方法，可单独定义
					var result = eval(jsonData);
					resMsg = result.data;
					}
				});
			return resMsg;
	}
	
	/**
	 * 预付校验
	 * @returns {String}
	 */
	function prePaidCheck(){
		//应扣预付金额控制
		var sumPreAmtP = 0;
		var sumPreAmtF =0;
		//分扣预赔金额  实赔
		$("[name$='offPreAmt']").each(function(){
			if($(this).val()!=null&&$(this).val()!=""){
				sumPreAmtP+=parseFloat($(this).val());
			}
		});
		
		var sumPersonP = 0;
		$("[id ^='lossPersDiv_']").each(function(){
			if(!isBlank($(this).find("input[name$='offPreAmt']").val())){
				sumPersonP +=parseFloat($(this).find("input[name$='offPreAmt']").val());
			}
		});
		sumPreAmtP = sumPreAmtP - sumPersonP;
		
		//旧理赔赔款费用汇总在一起，所以旧理赔案子不用减费用
		if(isEmpty($("#oldClaim"))){
			//分扣预赔金额  费用
			$("#feeTables [name$='offPreAmt']").each(function(){
				if($(this).val()!=null&&$(this).val()!=""){
					sumPreAmtF+=parseFloat($(this).val());
				}
			});
			sumPreAmtP = sumPreAmtP -sumPreAmtF;
		}
		var allPreAmtPers = 0;
		var allPreAmtP=0;
		var allPreAmtF=0;
		//垫付总金额
		$("#PaidPersTables [id^='padAmt']").each(function(){
			var padAmt = $(this).text();
			if(!isBlank(padAmt)){
				allPreAmtPers+=parseFloat(padAmt);
			}
		});
		//预赔赔款
		$("#prePaidPTables [id^='preAmt']").each(function(){
			var preAmt = $(this).text();
			if(!isBlank(preAmt)){
				allPreAmtP+=parseFloat(preAmt);
			}
		});

		//预赔费用（旧理赔要加入到赔款一起比较）
		if(!isEmpty($("#oldClaim"))){
			$("#prePaidFTables [id^='preAmt']").each(function(){
				var preAmt = $(this).text();
				if(!isBlank(preAmt)){
					allPreAmtP+=parseFloat(preAmt);
				}
			});
		}

		
		if(allPreAmtPers>0){
			if(sumPreAmtP.toFixed(2) != allPreAmtPers.toFixed(2)){
				return "垫付赔款金额拆分不相等！";
			}
		}
		
		if(allPreAmtP>0){
			if(sumPreAmtP.toFixed(2) != allPreAmtP.toFixed(2)){//保留两位小数
				return "预付赔款金额拆分不相等！";
			}
		}
		
		//预赔费用
		$("#prePaidFTables [id^='preAmt']").each(function(){
			var preAmt = $(this).text();
			if(!isBlank(preAmt)){
				allPreAmtF+=parseFloat(preAmt);
			}
		});
		
		//旧理赔赔款费用汇总在一起，所以旧理赔案子不用比较费用
		if(isEmpty($("#oldClaim"))){
			if(sumPreAmtF.toFixed(2)!=allPreAmtF.toFixed(2)){
				return "预付费用总额拆分不相等！";
			}
		}
		
		
		return "OK";
	}
	
	//界面初始化时例外原因管控
	function initPayCustomOtherFlag(){
		//遍历所有例外原因
		$("[name$='otherFlag']").each(function(){
			if($(this).prop("checked")==true&&$(this).val()=="0"){
				$otherSel = $(this).parents("td").next().find("select");
				$otherSel.attr("disabled","disabled");
			}
		});
	}
	
	//例外原因管控
	function otherFlagCon(){
		$("[name$='otherFlag']").each(function(){
			if($(this).prop("checked")==true&&$(this).val()=="1"){
				var payKind = $(this).parents("td").siblings().find("[name$='payObjectKind']").val();
				if(payKind!="2"){
					$otherSel = $(this).parents("td").next().find("select");
					$otherSel.removeAttr("disabled");
				}else{
					layer.msg("被保险人无例外原因");
					$(this).prop("checked",false);
					$(this).parent().siblings().find("[name$='otherFlag']").prop("checked",true);
				}
			}
			if($(this).prop("checked")==true&&$(this).val()=="0"){
				$otherSel = $(this).parents("td").next().find("select");
				$otherSel.attr("disabled","disabled");
				$otherSel.val("");
			}
		});
	}
	
	//是否增值专票管控
	function paymentVatInvoiceFlagCon(num){
		var payKind = $("input[name$='prpLPayment["+ num +"].payObjectKind']").val();
		
		if(payKind == "6"){
			
			$("input[name$='prpLPayment["+ num +"].vatInvoiceFlag']").removeAttr("disabled","disabled");
			var vatInvoiceFlagVal = $("input[name='prpLPayment["+ num +"].vatInvoiceFlag']:checked").val(); 
			if(vatInvoiceFlagVal == "1"){
				$("#prpLPaymentVatTaxRate"+ num).attr("style","");
				$("input[name$='prpLPayment["+ num +"].vatTaxRate']").removeAttr("disabled");
			}else{
				$("#prpLPaymentVatTaxRate"+ num).attr("style","display:none");
				$("input[name$='prpLPayment["+ num +"].vatTaxRate']").attr("disabled","disabled");
			}
		}else{
			$("#prpLPaymentVatTaxRate"+ num).attr("style","display:none");
			$("input[name$='prpLPayment["+ num +"].vatInvoiceFlag']").attr("disabled","disabled");
			$("input[name$='prpLPayment["+ num +"].vatTaxRate']").attr("disabled","disabled");
		}
		
	}
	
	function chargeVatInvoiceFlagCon(num){
		$("input[name$='prpLCharge["+ num +"].vatInvoiceFlag']").removeAttr("disabled","disabled");
		var vatInvoiceFlagVal = $("input[name='prpLCharge["+ num +"].vatInvoiceFlag']:checked").val();
		if(vatInvoiceFlagVal == "1"){
			$("#prpLChargeVatTaxRate"+ num).attr("style","");
			$("input[name$='prpLCharge["+ num +"].vatTaxRate']").removeAttr("disabled");
		}else{
			$("#prpLChargeVatTaxRate"+ num).attr("style","display:none");
			$("input[name$='prpLCharge["+ num +"].vatTaxRate']").attr("disabled","disabled");
		}
	}
	
	
	$("[name='prpLCompensate.agreeAmt']"). blur(function(){
		var bcFlag = $("#flag").val();
		var agreeAmt = $("[name='prpLCompensate.agreeAmt']").val();
		var limitAmt = 0;

		if(bcFlag=="1"){
			//取交强责任限额
			limitAmt = getBzLimitAgreeAmt();
		}else{
			// 获取商业责任限额
			limitAmt = getLimitAgreeAmt();
		}
		if(agreeAmt>limitAmt){
			layer.msg("协议金额超过责任限额");
			$("[name='prpLCompensate.agreeAmt']").val("");
		}
		 
	});
	
	function getLimitAgreeAmt(){
		var limitAmt = 0;
		var registNo = $("#registNo").val();
		$.ajax({
			url : "/claimcar/compensate/getLimitAgreeAmt.ajax", // 后台处理程序
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : {"registNo":registNo}, // 要传递的数据
			async:false, 
			success : function(jsonData) {// 回调方法，可单独定义
				var result = eval(jsonData);
				limitAmt = result.data;
				}
			});
		return limitAmt;
	}

	/**
	 * 获取交强险总限额
	 * @returns {number}
	 */
	function getBzLimitAgreeAmt(){
		var limitAmt = 0;
		var registNo = $("#registNo").val();
		var indemnityDuty = $("[name='prpLCompensate.indemnityDuty']").val();

		var isCiIndemDuty;//是否有责
		if(indemnityDuty=="4"){//无责
			isCiIndemDuty = false;
		}else{//有责
			isCiIndemDuty = true;
		}
		$.ajax({
			url : "/claimcar/compensate/getBzLimitAgreeAmt.ajax", // 后台处理程序
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : {"registNo":registNo,"isCiIndemDuty":isCiIndemDuty}, // 要传递的数据
			async:false,
			success : function(jsonData) {// 回调方法，可单独定义
				var result = eval(jsonData);
				limitAmt = result.data;
			}
		});
		return limitAmt;
	}

	//控制扣减金额不能大于费用金额
	function compareAmt(element){
		var deduAmt = parseFloat($(element).val());
		var feeAmt = parseFloat($(element).parent().siblings().find("[name$='feeAmt']").val());
		if(deduAmt>feeAmt){
			layer.msg("扣减金额大于费用金额");
			$(element).val("");
		}
		amtSum(element);
	}
	
	/**
	 * 理算冲销JS
	 */
	function compWfInit(){
		
		var status=$("#status").val();
		var workStatus = $("#workStatus").val();
		if(status=="2"){
			$("#CompCancel").prop("disabled",false);
		}
		
		if(compWfFlag=="1"){
			$("input").attr("readonly","readonly");
			$("button").attr("disabled","disabled");
			$("select").prop("disabled",true);
			$("input").addClass("disabled");
			$("button").addClass("disabled");
			$("#pend").hide();
			$("#save").hide();
			$("body textarea").attr("readonly", "readonly");
			$("#compWfBtnDiv").show();
			$("#nodeCode").val("CompeWf");
			$("input").each(function(){
				if($(this).attr("type")=="radio"||$(this).attr("type")=="checkbox"){
					$(this).prop("disabled",true);
				}
				if($(this).val()=="注销"){
					$(this).removeAttr("onclick");
					$(this).removeClass("btn-primary");
					$(this).addClass("btn-disabled");
				}
			});
			$("a").each(function(){
				var a = $(this).attr("id");
				var parentClass = $(this).parent("div").attr("class");
				if (a != "writeOff"&&parentClass != "top_btn" ) {
					$(this).removeAttr("onclick");
					$(this).addClass("disabled");
				}
				if(parentClass == "top_btn"){
					// 诉讼、单证打印、注销、反洗钱补录不能用
					var aText = $(this).text();
					if(aText=="诉讼"||aText=="单证打印"||aText=="注销"||aText=="反洗钱信息补录"){
						$(this).removeAttr("onclick");
						$(this).removeClass("btn-primary");
						$(this).addClass("btn-disabled");
					}
				}
			});
			
		}
		if(compWfFlag=="2"){
			$("input").attr("readonly","readonly");
			$("button").attr("disabled","disabled");
			$("select").prop("disabled",true);
			$("input").addClass("disabled");
			$("button").addClass("disabled");
			$("body textarea").attr("readonly", "readonly");
			$("#nodeCode").val("CompeWf");
			$("input").each(function(){
				if($(this).attr("type")=="radio"||$(this).attr("type")=="checkbox"){
					$(this).prop("disabled",true);
				}
				if($(this).val()=="注销"){
					$(this).removeAttr("onclick");
					$(this).removeClass("btn-primary");
					$(this).addClass("btn-disabled");
				}
			});
			$("a").each(function(){
				var a = $(this).attr("id");
				var parentClass = $(this).parent("div").attr("class");
				if (a != "save" &&parentClass != "top_btn") {
					$(this).removeAttr("onclick");
					$(this).addClass("disabled");
				}	
				if(parentClass == "top_btn"){
					// 诉讼、单证打印、注销、反洗钱补录不能用
					var aText = $(this).text();
					if(aText=="诉讼"||aText=="单证打印"||aText=="注销"||aText=="反洗钱信息补录"){
						$(this).removeAttr("onclick");
						$(this).removeClass("btn-primary");
						$(this).addClass("btn-disabled");
					}
				}
			});
			
		}
		if(workStatus=="3"){
			$("#compWfBtnDiv").hide();
		}
		if(workStatus!="3"){
			$("#compWfBtnDiv").show();
			$("#writeOff").attr("onclick","submitCompWf()");
			$("#compWfBtnDiv").find("a").removeClass("disabled");
			$("#compWfBtnDiv").find("a").addClass("btn-primary");
		}
	}
	function submitCompWf(){
		var lIndex = layer.load(0, {shade : [0.8, '#393D49']});
		var currentNode = $("#nodeCode").val();
		var compNo = $("[name='prpLCompensate.compensateNo']").val();
		var riskCode = $("[name='prpLCompensate.riskCode']").val();
		var params = {
				"currentNode" : currentNode,
				"compNo" : compNo,
				"riskCode" : riskCode,
			};
		$.ajax({
			url : "/claimcar/compensate/loadSubmitVClaimNext.ajax",
			type : "post",
			data : params,
			async : false, 
			success : function(htmlData){
				layer.close(lIndex);
				layer.open({
					title : "提示信息",
					type : 1,
					skin : 'layui-layer-rim', // 加上边框
					area : ['420px', '220px'], // 宽高
					content : htmlData,
					yes : function(index, layero) {
						var html = layero.html();
						$("#hideDiv").empty();
						$("#hideDiv").append(html);
						$("#hideDiv").hide();
						layer.close(index);
						submCompWfForm();
					}
				});
			}
		});
		
	} 
	
	function submCompWfForm(){
		var lIndex = layer.load(0, {shade : [0.8, '#393D49']});
		var params = $("#compEditForm").serialize();
		$.ajax({
			url : "/claimcar/compensate/submitCompeWriteOff.do", // 后台处理程序
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async:false, 
			success : function(result) {// 回调方法，可单独定义
				layer.close(lIndex);
				if(result.status=="200"){
					layer.msg("发起成功!");
					$("#compensateNo").text(result.data);
					$("#compWfBtnDiv").hide();
				}
			}
		});
	}
	
	/**
	 * 理算交强试算
	 */
	
	
	//核定金额赋值-先写死=核损金额
	$("#manageComp").click(function(){
		compBIProcess();
		$("[name$='sumRealPay']").each(function(){
			var amt = $(this).parents("td").siblings().find("input[name$='sumLoss']").val();
			$(this).val(amt);
		});
		$("[name$='feeRealPay']").each(function(){
			var amt = $(this).parents("td").siblings().find("input[name$='feeLoss']").val();
			$(this).val(amt);
			sumFee(this);
		});
		$("input[name$='feeOffLoss']").blur();
		$("input[name$='feeRealPay']").blur();
		amtSum();
	});
	//交强理算方法
	$("#manageCompBZ").click(function(){
		compClick = true;
		//itemName
		var nodutyFlag =false;
		$("#nodutyLossDiv select[name$='insuredComCode']").each(function(){
			var insuredComCode = $(this).val();
			var itemName = $(this).parents("tr").find("input[name$='itemName']").val();
			var ciPolicyNo = $(this).parents("tr").find("input[name$='ciPolicyNo']"); 
			if(!isBlank(insuredComCode)){//代赔保险公司名称有值，"代赔保单号" 必录，否则"是否赔付"必录 
				if(isBlank(ciPolicyNo.val())){
					layer.msg("无责代赔车辆"+itemName+"'代赔保单号'必录!");
					ciPolicyNo.focus();
					nodutyFlag = true;
					return false;
				}
				$(this).parents("tr").find("select[name$='noDutyPayFlag']").val("1");
			}
		});
		
		if(nodutyFlag){
			return;
		}
		
		compCIProcess();
		
		$("input[name$='sumRealPay']").blur();
		$("input[name$='feeOffLoss']").blur();
		$("input[name$='feeRealPay']").blur();
		amtSum();
		fuzhi();
	});
	
	function validValue(obj){
		if(parseFloat($(obj).val()) > parseFloat($(obj).next().val())){
			$(obj).val($(obj).next().val());
			alert("不能超过最大值："+ $(obj).next().val());
			return false;
		}
		if(parseFloat($(obj).val()) < 0){
			$(obj).val(0);
			alert("不能小于0");
		}
	}
	//商业理算方法
	$("#manageCompBI").click(function(){
		compClick = true;
		var bzPaidFlag = false;
		$("#lossTables input[name$='bzPaidLoss']").each(function(){
			bzPaidFlag = true ;
			return false;
		});
		
		var sumBzPaid = $("#allFee_table input[name='prpLCompensate.sumBzPaid']").val();
		if(bzPaidFlag && isBlank(sumBzPaid)){
			layer.msg("请获取交强赔付金额！");
			return ;
		}
		//校验无法找到第三方的数据
		checkNoFindThird();
		
		var numes=0;
		//理算中如果有车上乘客险的 人员并且责任比例大于0，则计数
		$("#persLossDiv input[name$='kindCode']").each(function(){
			if($(this).val()=='D12' && parseFloat($(this).parents("tr").find("input[name$='dutyRate']").val())>0 ){
				numes=numes+1;
			}
		});
		var quantity = $("#quantity").val();//承保人数
		if(quantity<numes){
			layer.alert("车上人员数据大于承保的人数，请调整部分车上乘客的责任比例为0");
			return;
		}

		compBIProcess();
		feeProcess();
	});

	/**
	 * 计算费用实赔金额并赋值给隐藏域feeRealAmt
	 */
	function feeProcess(){
		var feeAmt = 0;
		var offAmt = 0;
		var feeRealAmt = 0;
		$("#PayInfoTbody").find("tr").each(function(){
			feeAmt = Number($(this).find("input[name$='feeAmt']").val());
			offAmt = Number($(this).find("input[name$='offAmt']").val());
			offPreAmt = 0;
			offPreVal = $(this).find("input[name$='offPreAmt']").val();
			if(offPreVal!=null){
				offPreAmt = Number(offPreVal);
			}
			feeRealAmt = feeAmt - offAmt - offPreAmt;
			$(this).find("input[name$='feeRealAmt']").val(feeRealAmt);
		});
		
	}
	
	function compCIProcess(){
		var params = $("#compEditForm").serialize();
		$.ajax({
			url : "/claimcar/compensate/calculateCI.do", // 后台处理程序
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async:false, 
			success : function(jsonData) {// 回调方法，可单独定义
				if(jsonData.status=="200"){
					var result = eval(jsonData.data);
					processCalculateCIData(result);
					$("input[name$='feeOffLoss']").blur();
					$("input[name$='feeRealPay']").blur();
					
				}else{
					var messageText = '操作失败：' + jsonData.statusText;
					layer.alert(messageText);
				}
			}
		});
	}
	
	/**
	 * 商业扣交强
	 */
	function bIGetBZAmt(){
		var params = $("#compEditForm").serialize();
		$.ajax({
			url : "/claimcar/compensate/bIGetBZAmt.do", // 后台处理程序
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async:false, 
			success : function(jsonData) {// 回调方法，可单独定义
				if(jsonData.status=="200"){
					var result = eval(jsonData.data);
					processBIGetBZAmtData(result);
					$("input[name$='bzPaidLoss']").blur();
					/*$("input[name$='feeOffLoss']").blur();
					$("input[name$='feeRealPay']").blur();*/
					bZCount++;
				}else{
					var messageText = '操作失败：' + jsonData.statusText;
					layer.alert(messageText);
				}
			}
		});
	}

	/**
	 * 商业试算
	 */
	function compBIProcess(){
	
		var returnFlag = false;
		$("input[name$='dutyRate']").each(function(){
			if(isBlank($(this).val())){
				returnFlag = true;
				$(this).focus();
				return false;
			}
		});
		
		if(returnFlag){
			layer.msg("请获取免赔率！");
			$("input[name$='dutyRate']").blur();
			return;
		}
		
		$("#OtherLossTbody input[name$='sumLoss']").each(function(){
			var kindCode = $(this).parents("tr").find("input[name$='kindCode']").val();
			if(kindCode != "T" && kindCode != "C" && kindCode != "RF"){
				if(isBlank($(this).val())){
					returnFlag = true;
					$(this).focus();
					return false;
				}
			}
			
			if(kindCode == "T" || kindCode == "C" || kindCode == "RF"){
				var lossQuantity = $(this).parents("tr").find("input[name$='lossQuantity']").val();
				if(isBlank(lossQuantity)){
					returnFlag = true;
					$(this).focus();
					layer.msg("请录入天数!");
					return false;
				}
			}
		});
		
		if(returnFlag){
			$("#OtherLossTbody input[name$='sumLoss']").blur();
			$("#OtherLossTbody input[name$='lossQuantity']").blur();
			return;
		}
		
		var params = $("#compEditForm").serialize();
		$.ajax({
			url : "/claimcar/compensate/calculateBI.do", // 后台处理程序
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async:false, 
			success : function(jsonData) {// 回调方法，可单独定义
				if(jsonData.status=="200"){
					var result = eval(jsonData.data);
					processCalculateBIData(result);
					$("input[name$='feeOffLoss']").blur();
					$("input[name$='feeRealPay']").blur();
					amtSum();
					fuzhi();
				}else{
					var messageText = '操作失败：' + jsonData.statusText;
					layer.alert(messageText);
				}
			}
		});
	}
    //赋值
	function fuzhi(){
	
		var sumPaidAmt = $("input[name$='prpLCompensate.sumPaidAmt']").val();
		var handlstatus =$("#handlerStatus").val();
		
		if(handlstatus!="3"){
			var i=0;
			$("#paymentVo input[name$='payeeId']").each(function(){
				i=i+1;
				});
			
			$("#paymentVo input[name$='payeeId']").each(function(){
				
				if(i==1){
				    var pre = $(this).attr("name").split("]")[0];
					var index = pre.split("[")[1];//下标
					$("input[name='prpLPayment["+index+"].sumRealPay']").val(sumPaidAmt);
				}
			});
		}
	}
	
	//处理并赋值核定金额
	function processCalculateCIData(result){
		var compText = result.compText;
		$("[name='prpLCompensate.lcText']").text(compText);
		var itemLossIdAmtMap = result.itemLossIdAndAmtMap;
		for(var key in itemLossIdAmtMap){
			var strKey = new Array();
			strKey = key.split("=");
			if("noduty"==strKey[0]){
				$("#carLossDiv input[name$='itemId']").each(function(){
					if($(this).val()==strKey[1] && $(this).parents("tr").find("input[name$='payFlag']").val()=="4" ){
						$(this).parents("tr").find("input[name$='sumRealPay']").val(itemLossIdAmtMap[key].toFixed(2));
					}
				});
			}else if("car"==strKey[0]||"prop"==strKey[0]){
				$("#"+strKey[0]+"LossDiv input[name$='itemId']").each(function(){
					var payFlag = $(this).parents("tr").find("input[name$='payFlag']").val();
					if($(this).val()==strKey[1]){//车辆考虑有责和无责方
						if("prop"==strKey[0] || ("car"==strKey[0] && payFlag!="4")){
							$(this).parents("tr").find("input[name$='sumRealPay']").val(itemLossIdAmtMap[key].toFixed(2));
							
							if(payFlag=="2"){//清付
								$(this).parents("tr").find("input[name$='thisPaid']").val(itemLossIdAmtMap[key].toFixed(2));
							}
						}
					}
				});
			}else{
				var typeVal = "00";
				if("med"==strKey[0]){
					typeVal = "03";
				}
				if("pers"==strKey[0]){
					typeVal = "02";
				}
				$("#persLossDiv input[name$='personId']").each(function(){
					if($(this).val()==strKey[1]){
						$childDiv = $(this).parents("div[name^='personLossDiv']");
						$childDiv.find("input[name$='feeRealPay']").each(function(){
							var feeType = $(this).parents("td").siblings().find("input[name$='lossItemNo']").val();
							if(feeType==typeVal){
								$(this).val(itemLossIdAmtMap[key].toFixed(2));
							}
							//费用详情赋值
							//定损金额
							var feeLoss = $(this).parents("td").siblings().find("input[name$='feeLoss']").val();
							//核定金额
							var feeRealPay = $(this).val();
							$showFeeDiv = $(this).parents("td").siblings().find("div[name^='showFeeDiv']");
							var detailSize = parseInt($showFeeDiv.find("input[name$='realPay']").size());
							var subtrahend = parseInt(0);
							$showFeeDiv.find("input[name$='realPay']").each(function(){
								var name = $(this).attr("name");
								var index = parseInt(name.split("].realPay")[0].split("Details[")[1]);
								var lossFee = $(this).parents("td").siblings().find("input[name$='lossFee']").val();
								if(feeRealPay == 0){
									$(this).val(0);
								}else if((index + 1) == detailSize){
									$(this).val((feeRealPay - subtrahend).toFixed(2));
								}else{
									var proportion = lossFee / feeLoss;
									var fee = feeRealPay * proportion;
									subtrahend = subtrahend + fee;
									$(this).val(fee.toFixed(2));
								}
							});
						});
					}
				});
			}
		}
		
		$("#carLossDiv input[name$='itemId']").each(function(){
			if($(this).parents("tr").find("input[name$='payFlag']").val()=="4" ){
				var pay = $(this).parents("tr").find("input[name$='sumRealPay']");
				if(isBlank(pay.val())){
					pay.val("0.00");
				}
			}
		});
	}
	
	
	
	//处理并赋值定损跟施救费金额商业
	function processBIGetBZAmtData(result){
		var itemLossIdMap = result.itemLossIdMap;
		var itemAmtMap = result.itemAmtMap;
		var noDutyCarPay = result.NoDutyCarPay;
		$("#carLossDiv input[name$='dlossId']").each(function(){
			$(this).parents("tr").find("input[name$='bzPaidLoss']").val("0.00");
			$(this).parents("tr").find("input[name$='bzPaidRescueFee']").val("0.00");
			$(this).parents("tr").find("input[name$='absolvePayAmt']").val("0.00");
			$(this).parents("tr").find("input[name$='absolvePayAmts']").val("0.00");
			var kindcode = $(this).parents("tr").find("input[name$='kindCode']").val();
			var itemLossKey = "car="+$(this).val();
			if(itemLossKey in itemLossIdMap){
				if(kindcode=="A" || kindcode=="B" || kindcode=="A1"){
					$(this).parents("tr").find("input[name$='bzPaidLoss']").val(itemLossIdMap[itemLossKey].toFixed(2));
				}
			}
			
			if(itemLossKey in itemAmtMap){
				if(kindcode=="A" || kindcode=="B" || kindcode=="A1"){
					$(this).parents("tr").find("input[name$='bzPaidRescueFee']").val(itemAmtMap[itemLossKey].toFixed(2));
				}
			}
			
			var nodutyKey = "noduty="+$(this).parents("tr").find("input[name$='itemId']").val();;
			if(nodutyKey in noDutyCarPay){
				if(kindcode=="A" || kindcode=="A1"){
					$(this).parents("tr").find("input[name$='absolvePayAmt']").val(noDutyCarPay[nodutyKey].toFixed(2));
					$(this).parents("tr").find("input[name$='absolvePayAmts']").val(noDutyCarPay[nodutyKey].toFixed(2));
				}
			}
			
		});
		
		$("#propLossDiv input[name$='dlossId']").each(function(){
			$(this).parents("tr").find("input[name$='bzPaidLoss']").val("0.00");
			$(this).parents("tr").find("input[name$='bzPaidRescueFee']").val("0.00");
			
			var itemLossKey = "prop="+$(this).val(); 
			if(itemLossKey in itemLossIdMap){
				$(this).parents("tr").find("input[name$='bzPaidLoss']").val(itemLossIdMap[itemLossKey].toFixed(2));
			}
			
			if(itemLossKey in itemAmtMap){
				$(this).parents("tr").find("input[name$='bzPaidRescueFee']").val(itemAmtMap[itemLossKey].toFixed(2));
			}
		});
		
		
		$("#persLossDiv input[name$='personId']").each(function(){
			var personId = $(this).val(); 
			$childDiv = $(this).parents("div[name^='personLossDiv']");
			$childDiv.find("input[name$='bzPaidLoss']").each(function(){
				var feeType = $(this).parents("td").siblings().find("input[name$='lossItemNo']").val();
				var itemLossKey ="";
				if(feeType =="02"){//死亡伤残
					itemLossKey = "pers="+personId; 
					
				}else if(feeType =="03"){//医疗费用
					itemLossKey = "med="+personId;  
				}
				
				if(itemLossKey in itemLossIdMap){
					$(this).val(itemLossIdMap[itemLossKey].toFixed(2));
				}
				
				if(itemLossKey in itemAmtMap){
					$(this).val(itemAmtMap[itemLossKey].toFixed(2));
				}
				
			});
		});
	}
	
	//商业处理并赋值核定金额
	function processCalculateBIData(result){
		var compText = result.compText;
		$("[name='prpLCompensate.lcText']").text(compText);
		var itemLossIdAmtMap = result.itemLossIdAndAmtMap;
		var deductOffMap = result.deductOffMap;//不计免赔
		$("#carLossDiv input[name$='itemId']").each(function(){
			var payFlag = $(this).parents("tr").find("input[name$='payFlag']").val();
			if(payFlag=="4" ){//无责代赔
				var key = "noduty="+$(this).val(); 
				if(key in itemLossIdAmtMap){
					var sumRealPay = itemLossIdAmtMap[key]+deductOffMap[key];//实赔加上不计免赔
					$(this).parents("tr").find("input[name$='sumRealPay']").val(sumRealPay.toFixed(2));
					$(this).parents("tr").find("input[name$='deductOffAmt']").val(deductOffMap[key].toFixed(2));
				}
			}else if(payFlag=="1"){//追偿
				var key = "recovery="+$(this).val(); 
				if(key in itemLossIdAmtMap){
					var sumRealPay = itemLossIdAmtMap[key];//实赔加上不计免赔
					$(this).parents("tr").find("input[name$='sumRealPay']").val(sumRealPay.toFixed(2));
					$(this).parents("tr").find("input[name$='deductOffAmt']").val(deductOffMap[key].toFixed(2));
					var dwPersFlag = $("input[name='dwPersFlag']").val();
					if(dwPersFlag == '1'){
						//非机动车代位明细赋值
						$(this).parents("tr").find("input[name$='thisPaid']").first().val(sumRealPay.toFixed(2));
					}
				}
			}else{
				var kindCode = $(this).parents("tr").find("input[name$='kindCode']").val();
				if(kindCode !="A" && kindCode !="B" && kindCode !="A1"){//非主险
					var key = "car="+kindCode; 
					if(key in itemLossIdAmtMap){
						var sumRealPay = itemLossIdAmtMap[key]+deductOffMap[key];//实赔加上不计免赔
						$(this).parents("tr").find("input[name$='sumRealPay']").val(sumRealPay.toFixed(2));
						$(this).parents("tr").find("input[name$='deductOffAmt']").val(deductOffMap[key].toFixed(2));
					}
				}else{
					var key = "car="+$(this).val(); //车辆考虑有责和无责方
					if(payFlag =="3" || payFlag =="2"){//自付和清付
						if(key in itemLossIdAmtMap){
							var sumRealPay = itemLossIdAmtMap[key]+deductOffMap[key];//实赔加上不计免赔
							$(this).parents("tr").find("input[name$='sumRealPay']").val(sumRealPay.toFixed(2));
							$(this).parents("tr").find("input[name$='deductOffAmt']").val(deductOffMap[key].toFixed(2));
							if(payFlag=="2"){//清付
								$(this).parents("tr").find("input[name$='thisPaid']").val(sumRealPay.toFixed(2));
							}
						}
					}
				}
			}
		});
		
		$("#propLossDiv input[name$='itemId']").each(function(){
			var key = "prop="+$(this).val(); 
			if(key in itemLossIdAmtMap){
				var sumRealPay = itemLossIdAmtMap[key]+deductOffMap[key];//实赔加上不计免赔
				$(this).parents("tr").find("input[name$='sumRealPay']").val(sumRealPay.toFixed(2));
				$(this).parents("tr").find("input[name$='deductOffAmt']").val(deductOffMap[key].toFixed(2));
			}
			
		});
		
		$("#otherLossDiv input[name$='kindCode']").each(function(){
			var key = "other="+$(this).val(); 
			if(key in itemLossIdAmtMap){
				var sumRealPay = itemLossIdAmtMap[key]+deductOffMap[key];//实赔加上不计免赔
				$(this).parents("tr").find("input[name$='sumRealPay']").val(sumRealPay.toFixed(2));
				$(this).parents("tr").find("input[name$='deductOffAmt']").val(deductOffMap[key].toFixed(2));
			}
		});
		
		$("#persLossDiv input[name$='personId']").each(function(){
			var personId = $(this).val(); 
			$childDiv = $(this).parents("div[name^='personLossDiv']");
			$childDiv.find("input[name$='feeRealPay']").each(function(){
				var feeType = $(this).parents("td").siblings().find("input[name$='lossItemNo']").val();
				var key ="";
				if(feeType =="02"){//死亡伤残
					key = "pers="+personId; 
					
				}else if(feeType =="03"){//医疗费用
					key = "med="+personId;  
				}
				if(key in itemLossIdAmtMap){
					var feeRealPay = itemLossIdAmtMap[key]+deductOffMap[key];//实赔加上不计免赔
					$(this).val(feeRealPay.toFixed(2));
					$(this).parents("tr").find("input[name$='deductOffAmt']").val(deductOffMap[key].toFixed(2));
					//费用详情赋值
					//定损金额
					var feeLoss = $(this).parents("td").siblings().find("input[name$='feeLoss']").val();
					//核定金额
					var feeRealPay = $(this).val();
					$showFeeDiv = $(this).parents("td").siblings().find("div[name^='showFeeDiv']");
					var detailSize = parseInt($showFeeDiv.find("input[name$='realPay']").size());
					var subtrahend = parseInt(0);
					$showFeeDiv.find("input[name$='realPay']").each(function(){
						var name = $(this).attr("name");
						var index = parseInt(name.split("].realPay")[0].split("Details[")[1]);
						var lossFee = $(this).parents("td").siblings().find("input[name$='lossFee']").val();
						if(feeRealPay == 0){
							$(this).val(0);
						}else if((index + 1) == detailSize){
							$(this).val((feeRealPay - subtrahend).toFixed(2));
						}else{
							var proportion = lossFee / feeLoss;
							var fee = feeRealPay * proportion;
							subtrahend = subtrahend + fee;
							$(this).val(fee.toFixed(2));
						}
					});
				}
			});
		});
		
	}
	function checkAllowFlag(){
		//校验是否通融prpLCompensate.allowFlag.val();
		var allowFlag  = $("input[name$='allowFlag']:checked").val();
		var lawsuitFlag  = $("input[name$='lawsuitFlag']:checked").val();
		if(allowFlag==1 ||lawsuitFlag==1){
			$("#persLossDiv input[name$='personId']").each(function(){
				$childDiv = $(this).parents("div[name^='personLossDiv']");
				$childDiv.find("input[name$='feeRealPay']").each(function(){
					$(this).prop("readonly",false);
				
				});
			});
		}else{
			$("#persLossDiv input[name$='personId']").each(function(){
				$childDiv = $(this).parents("div[name^='personLossDiv']");
				$childDiv.find("input[name$='feeRealPay']").each(function(){
					$(this).val("");
					$(this).prop("readonly",true);
				});
				$childDiv.find("input[name$='sumRealPay']").each(function(){
					$(this).val("");
				});
		});
		}
	}
	
	//显示险别金额汇总
	$("#compeRegistMsg").click(
		function checkRegistMsgInfo(){//打开案件备注之前检验报案号和节点信息是否为空
			var registNo=$("#registNo").val();
			var nodeCode=$("#nodeCode").val();
			createRegistMessage(registNo,nodeCode,'');

	});
	$("#KindAmtSum").click(
		function(){
			var sumFlag = 0;
			$("[name$='lcText']").each(function(){
				if(isBlank($(this).val())){
					sumFlag = 1;
				}
			});
			if(sumFlag==1){
				layer.msg("请先点击生成计算书");
			}else{
				var params = $("#compEditForm").serialize();
				$.post("/claimcar/compensate/showKindAmtSum.ajax", params, function(result) {
					$("#CompensateEdit_KindAmt").html(result);
				});
				
				}
			}
	);
	//
	function checkKindMap(element,a){
		//获取被选中的option标签
		// var vs = $('select  option:selected').val();
		
		var policyNo = $("#policyNo").val();
		var registNo  =  $("#registNo").val();
		var vs = $(element).val();
		//alert($(element).attr("id"));
		var id = $(element).attr("id");
		var  riskCode = $("#OtherLossTbody input[name$='riskCode']").val();
		var sizes = $("#otherLossDiv select[name$='kindCode']").length;
		if(sizes==1){
			if(vs=='RF'||vs=='T'||vs=='C'){
				//选择('RF','T','C'),定损金额 默认为单位限额，prplcitemkind 的UNITAMOUNT字段，不能修改
				var unitamount = $("#unitamount").val();
				$(element).parent().parent().find("input[name$='sumLoss']").val(unitamount);
				$(element).parent().parent().find("input[name$='sumLoss']").attr("readonly",true);
			}
			
			if(vs=="X"){
				$(element).parent().next().empty();
				//添加下拉框
				var params = {
						"riskCode" : riskCode,
						"policyNo": policyNo,
						"registNo":registNo,
						"size" : a,
					};
					var url = "/claimcar/compensate/addSelect.ajax";
					$.post(url, params, function(result) {
						//$tbody.append(result);
						$(element).parent().next().append(result);
					//	$("#"+sizeName+"").val(size + 1);
					});
				 //
			}else{
				//添加文本框
				$(element).parent().next().empty();
				var params = {
						"riskCode" : riskCode,
						"policyNo": policyNo,
						"registNo":registNo,
						"size" : a,
					};
					var url = "/claimcar/compensate/addText.ajax";
					$.post(url, params, function(result) {
						//$tbody.append(result);
						$(element).parent().next().append(result);
					//	$("#"+sizeName+"").val(size + 1);
					});
				
			}
		} else {
		$("#otherLossDiv select[name$='kindCode']").each(function(){
			if(id!=$(this).attr("id")){
				if($(this).val()==vs){
				$(element).val("");
				if(vs=="X"){
					$(element).parent().next().empty();
					//添加下拉框
					var params = {
							"riskCode" : riskCode,
							"size" : a,
						};
						var url = "/claimcar/compensate/addSelect.ajax";
						$.post(url, params, function(result) {
							//$tbody.append(result);
							$(element).parent().next().append(result);
						//	$("#"+sizeName+"").val(size + 1);
						});
					 //
				}else{
					//添加文本框
					$(element).parent().next().empty();
					var params = {
							"riskCode" : riskCode,
							"policyNo": policyNo,
							"registNo":registNo,
							"size" : a,
						};
						var url = "/claimcar/compensate/addText.ajax";
						$.post(url, params, function(result) {
							//$tbody.append(result);
							$(element).parent().next().append(result);
						//	$("#"+sizeName+"").val(size + 1);
						});
				}
				}else{
					if(vs=="X"){
						$(element).parent().next().empty();
						//添加下拉框
						var params = {
								"riskCode" : riskCode,
								"policyNo": policyNo,
								"registNo":registNo,
								"size" : a,
							};
							var url = "/claimcar/compensate/addSelect.ajax";
							$.post(url, params, function(result) {
								//$tbody.append(result);
								$(element).parent().next().append(result);
							//	$("#"+sizeName+"").val(size + 1);
							});
						 //
					}else{
						//添加文本框
						$(element).parent().next().empty();
						var params = {
								"riskCode" : riskCode,
								"policyNo": policyNo,
								"registNo":registNo,
								"size" : a,
							};
							var url = "/claimcar/compensate/addText.ajax";
							$.post(url, params, function(result) {
								//$tbody.append(result);
								$(element).parent().next().append(result);
							//	$("#"+sizeName+"").val(size + 1);
							});
					}
				}
				//alert($(this).val()+$(this).attr("id"));
			}
			
		});
		}
	}
function Buji(){
	//赋值不计免赔赔款
	var deductOffALL = 0;
	/*$("[name^='deductOffAmt']").each(function(){
		if($(this).val()!=null&&$(this).val()!=""){
			deductOffALL+=parseFloat($(this).val());
		}
	});*/
	$("[name$='deductOffAmt']").each(function(){
		if($(this).val()!=null&&$(this).val()!=""){
			deductOffALL+=parseFloat($(this).val());
		}
	});
	$("span#deductOffALL").text(deductOffALL.toFixed(2));
	$("span#deductOffALL").next().val(deductOffALL.toFixed(2));
	//赋值不计免赔赔款
	$("#buji input[name$='kindCodes']").each(function(){
			var deductALL = 0;
			var a = 0;
			var isNew = $(this).val();
			var temp = isNew.charAt(isNew.length - 1);
		if(temp!="M"){//旧条款
			$("#bujimian [name$='kindCode']").each(function(){	
				var s = $(this).val();
				if(isNew == s){
					//判断是否为人
					var name1 = $(this).parent().parent().parent().parent().attr("name");
					//alert("211"+name1);
					var name = name1.split("_")[0];
					if(name=="personkindcode"){//人
						var index = $(this).parent().parent().parent().parent().attr("name").split("_")[1];
						var b=0;
						$(this).parent().parent().parent().parent().siblings().find("input[name$='deductOffAmt']").each(function(){
							b = b+parseFloat($(this).val());
						});
						a=b;
					}else if(name=='cpo'){//财跟人
						a = parseFloat($(this).parent().parent().find("input[name$='deductOffAmt']").val());
					}
					if(!(isNaN(a))&&typeof(a) != "undefined"){
					deductALL=deductALL+a;
					}
				}
			});
			$(this).next().find("input[name$='kindCodees']").val(deductALL.toFixed(2));
			//$(this).parents("tr").find("input[name$='kindCodees']").val(deductOffALL.toFixed(2));
		} else if(isNew=="M"){
			$(this).next().find("input[name$='kindCodees']").val(deductOffALL.toFixed(2));
		} else {//新条款
			//$("[name$='kindCode']").each(function(){
			$("#bujimian [name$='kindCode']").each(function(){	
				var s = $(this).val()+"M";
				if(isNew == s){
					//判断是否为人
					var name1 = $(this).parent().parent().parent().parent().attr("name");
					//alert("211"+name1);
					var name = name1.split("_")[0];
					if(name=="personkindcode"){//人
						var index = $(this).parent().parent().parent().parent().attr("name").split("_")[1];
						var b=0;
						$(this).parent().parent().parent().parent().siblings().find("input[name$='deductOffAmt']").each(function(){
							b = b+parseFloat($(this).val());
						});
						a=b;
					}else if(name=='cpo'){//财跟人
						a = parseFloat($(this).parent().parent().find("input[name$='deductOffAmt']").val());
					}
					if(!(isNaN(a))&&typeof(a) != "undefined"){
					deductALL=deductALL+a;
					}
				}
			});
			$(this).next().find("input[name$='kindCodees']").val(deductALL.toFixed(2));
		}
	});
}

function calTSFee(filed){
	var count = $(filed).val();
	if(count =="" || isNaN(count)){
		return false;
	}
	
	var unitAmount = $(filed).parents("tr").find("input[name$='unitPrice']").val();
	var kindCode = $(filed).parents("tr").find("input[name$='kindCode']").val();
	var riskcode=$("#riskcode1").val();
	var sumFee = parseFloat(0);
	if(kindCode=='C' || kindCode=='RF'){//代步车费用特约条款
		if(!isBlank(riskcode) && (riskcode=='1230' || riskcode=='1231' || riskcode=='1232' || riskcode=='1233') && kindCode=='RF'){
			sumFee = parseFloat(count)*parseFloat(unitAmount);
		}else{
			sumFee = parseFloat(count-1)*parseFloat(unitAmount);
		}
		
		if(sumFee<0){
			sumFee = 0.00;
		}
	}else{//机动车停驶损失险
		sumFee = parseFloat(count)*parseFloat(unitAmount);
	}
	
	$(filed).parents("tr").find("input[name$='sumLoss']").val(sumFee);
}

function checkManageCompBI(){
	var bzPaidFlag = false;
	$("#lossTables input[name$='bzPaidLoss']").each(function(){
		bzPaidFlag = true ;
		return false;
	});
	
	var sumBzPaid = $("#allFee_table input[name='prpLCompensate.sumBzPaid']").val();
	if(bzPaidFlag && isBlank(sumBzPaid)){
		$("#manageCompBI").attr("disabled","disabled");
	}else{
		$("#manageCompBI").removeClass("btn-disabled").addClass("btn-primary");
		$("#manageCompBI").attr("disabled",false);
	}
}
function showTaxInfos(index,taxRate,taxVlaue,noTaxVlaue){
	var url = "/claimcar/compensate/showTaxInfos.ajax?index="+index+"&taxRate="+taxRate+"&taxVlaue="+taxVlaue+"&noTaxVlaue="+noTaxVlaue;
	 layer.open({
		type : 2,
		area : [ '400px', '200px' ],
		fix : false, // 不固定
		maxmin : false,
		shadeClose : true,
		scrollbar : true,
		skin : 'yourclass',
		title : "税收信息",
		content : [ url, "no" ],
	});
}

//代位收款人清付时 需要录入清付对象
function changeDwPayFlag(obj){
	if($(obj).val()=="2" || $(obj).val()=="1"){//清付 或代付
		$(obj).parents("tr").find("div[name^='itemId_']").show();
	}else{
		$(obj).parents("tr").find("div[name^='itemId_']").hide();
		$(obj).parents("tr").find("select[name$='itemId']").val("");
	}
	
}

//清付代付下拉框改变时，清空已选择的收款人,重新选择
$("#PayCustomTbody [name$='payFlag']").change(function(){
	var paySelIndex = $(this).attr("name").substring(12,13);
	$("[name='prpLPayment["+paySelIndex+"].payeeName']").val(null);
	$("[name='prpLPayment["+paySelIndex+"].payeeId']").val(null);
});

/** 责任比例比较
 */
function compareDutyRate(field,index){
	var rate = parseFloat($(field).val());
	var indemnityDuty = "";
	indemnityDuty = $(".Iindemnitydutys:eq("+index+")");

	var minRate = 0;
	var maxRate = 0;
	var rateName ="";
	if(indemnityDuty.val() == "0"){
		if(rate != 100){
			layer.alert("全责的责任比例必须为100%");
			changeDuty(indemnityDuty,index);
			return false;
		}
	  }else if(indemnityDuty.val() == "1"){
		minRate = 0;
	    maxRate = 100;
		rateName ="主责";
	  }else if(indemnityDuty.val() == "2"){
		minRate = 0;
		maxRate = 100;
		rateName ="同责";
	  }else if(indemnityDuty.val() == "3"){
		minRate = 0;
		maxRate = 100;
		rateName ="次责";
	  }else if(indemnityDuty.val() == "4"){
		  if(rate != 0){
			  layer.alert("无责的责任比例必须为0%");
			  changeDuty(indemnityDuty,index);
			  return false;
		  }
	  }
	  if(rate >= maxRate || rate <= minRate) {
		layer.alert(rateName+"的责任比例必须在"+minRate+"%~"+maxRate+"%之间");
		changeDuty(indemnityDuty,index);
	  }
}

function changeDuty(field,index) {
	var duty = $(field).val();
	var dutyVa = "";
	var serialNo = $("[name='prpLCheckDutyVoList["+index+"].serialNo']").val();

	dutyVa=$(".IindemnityDutyRates:eq("+index+")");
	dutyDuty=$(".Iindemnitydutys:eq("+index+")").val();
	ciIndemDuty(dutyDuty,index);
	
	if (isBlank(duty)) {
		dutyVa.val("");
	} else if (duty == "0") {
        dutyVa.val("100");
	} else if (duty == "1") {
		dutyVa.val("70");
	} else if (duty == "2") {
		dutyVa.val("50");
	} else if (duty == "3") {
		dutyVa.val("30");
	} else if (duty == "4") {
		dutyVa.val("0");
	}
	if(serialNo == "1"){
		// 标的车比例改变联动计算书责任比例改变
		$("[name='prpLCompensate.indemnityDuty']").find("option[value='"+duty+"']").prop("selected","selected");
		var newRate = dutyVa.val();
		$("[name='prpLCompensate.indemnityDutyRate']").val(newRate);
		// 计算书内容改变，需要重新点击按钮计算
		rateClick = false; 
		BZAmtClick = false;
		compClick = false;
	}
	var oriIndemnityDuty = $("[name='indemnityDutyOri_"+index+"']").val();
	if((oriIndemnityDuty = '4' && duty != '4' )||(oriIndemnityDuty != '4' && duty == '4' ) ){
		// 车辆责任从有责变成无责时，界面重新加载车损信息
		initCompLossCar();
		$("[name='indemnityDutyOri_"+index+"']").val(duty);
	}
}

//事故责任比例选择有责时，交强险必选选择有责
function ciIndemDuty(dutyDuty,index) {
	var ciDuty=$(".ciIndemDuty:eq("+index+")");
	var duty = dutyDuty;
	if (duty!= "4") {
		ciDuty.val("1");
		ciDuty.find("option[value='1']").removeAttr("disabled");
		ciDuty.find("option[value='0']").attr("disabled", "disabled");
	} else {
		ciDuty.val("0");
		ciDuty.find("option[value='0']").removeAttr("disabled");
		ciDuty.find("option[value='1']").attr("disabled", "disabled");
	}
}
/*
 * 初始化时 处理交强有责无责下拉框
 */
function initciIndemDuty(){
	var dutyType = '';
	var ciIndex = '';
	$(".Iindemnitydutys").each(function(){
		dutyType = $(this).val();
		ciIndex = $(this).attr("name").substring(20,21);
		ciIndemDuty(dutyType,ciIndex);
	});
}

function initCompLossCar(){
	$("#manageCompBZ").removeClass("btn-primary").addClass("btn-disabled").attr("disabled",true);
	// 获取carinfoDiv
	$carInfoDiv = $("#carLossDiv");
	var params = $("#compEditForm").serialize();
	
	var url = "/claimcar/compensate/initCompensateLossCarInfo.ajax";
	$.post(url, params, function(result) {
		$carInfoDiv.empty();
		$carInfoDiv.append(result);
		$("#manageCompBZ").removeClass("btn-disabled").addClass("btn-primary").attr("disabled",false);
	});
}



function isSumFeeLower(t_t,sumFeeChg,sumFeeOri,type){
	var chgFee = $("[name='"+sumFeeChg+"["+sumFeeOri+"].sumLoss']").val();
	var oriFee = $("[name='"+sumFeeChg+"["+sumFeeOri+"].originLossFee']").val();
	if (1 == type) {
		if (parseFloat(chgFee) > parseFloat(oriFee)) {
			layer.alert("车辆损失金额不能大于原损失金额" + oriFee);
			$(t_t).val(oriFee);
			return false;
		}
	}
	if (2 == type) {
		if (Number(chgFee) > Number(oriFee)) {
			layer.alert("财产损失金额不能大于原损失金额" + oriFee);
			$(t_t).val(oriFee);
			return false;
		}
	}
	if(3 == type){
		var ylFeeOri = $("[name='"+sumFeeChg+"["+sumFeeOri+"].prpLLossPersonFees[0].feeLoss']").val(); //医疗费 
		var swFeeOri = $("[name='"+sumFeeChg+"["+sumFeeOri+"].prpLLossPersonFees[1].feeLoss']").val();  //死亡伤残费
		var ylFeechg = (parseFloat(ylFeeOri)/(parseFloat(ylFeeOri)+parseFloat(swFeeOri))*chgFee).toFixed(2);
		var swFeechg = (parseFloat(chgFee) -parseFloat(parseFloat(ylFeeOri)/(parseFloat(ylFeeOri)+parseFloat(swFeeOri))*chgFee)).toFixed(2);
		$("[name='"+sumFeeChg+"["+sumFeeOri+"].prpLLossPersonFees[0].feeLoss']").val(ylFeechg);
		$("[name='"+sumFeeChg+"["+sumFeeOri+"].prpLLossPersonFees[1].feeLoss']").val(swFeechg);
		if (Number(chgFee) > Number(oriFee)) {
			layer.alert("人伤总定损金额不能大于原定损金额" + oriFee);
			$(t_t).val(oriFee);
			//人伤金额大于原定损金额 则按照比例 恢复医疗费和死亡伤残费
			ylFeechg = (parseFloat(ylFeeOri)/(parseFloat(ylFeeOri)+parseFloat(swFeeOri))*oriFee).toFixed(2);
		    swFeechg = (parseFloat(oriFee) -parseFloat(parseFloat(ylFeeOri)/(parseFloat(ylFeeOri)+parseFloat(swFeeOri))*oriFee)).toFixed(2);
			$("[name='"+sumFeeChg+"["+sumFeeOri+"].prpLLossPersonFees[0].feeLoss']").val(ylFeechg);
			$("[name='"+sumFeeChg+"["+sumFeeOri+"].prpLLossPersonFees[1].feeLoss']").val(swFeechg);
			return false;
		}
		return true;
	}
	
}
//退回单证
function backCerti(registNo){
	var flowTaskId = $("[name='flowTaskId']").val();
	layer.confirm('是否需要退回单证?', {
		btn: ['确定','取消'] //按钮
	}, function(index){
		$.ajax({
			url : "/claimcar/compensate/backCerti.do?registNo="+registNo+"&flowTaskId="+flowTaskId+"", // 后台处理程序
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			//data : params, // 要传递的数据
			async:false, 
			success : function(jsonData) {// 回调方法，可单独定义
				layer.alert(jsonData.statusText);
				if(jsonData.data=="1"){
					
					$("input").attr("disabled","disabled");
					$("select").attr("disabled","disabled");
					$("#pend").hide();
					$("#save").hide();
					$("button").attr("disabled","disabled");
					$("button").addClass("btn-disabled");
					$("body textarea").attr("readonly", "readonly");
					
					$(".rateBtn").removeAttr("disabled");
					
					$("#CompCancel").removeAttr("onclick");
					$("#CompCancel").attr("disabled","disabled");
					$("#CompCancel").addClass("btn-disabled");
					$("#certi").removeAttr("onclick");
					$("#certi").attr("disabled","disabled");
					$("#certi").addClass("btn-disabled");
					$("#taskId").removeAttr("disabled");
					$("input[name='prpLCompensate.registNo']").removeAttr("disabled");
					$("input[name='prpLCompensate.policyNo']").removeAttr("disabled");
					layer.close(index);
				}
			/*	if(jsonData.statusText=="1"){
					layer.alert("该案件下已存在核赔通过记录不允许理算退回!");
				}else if(jsonData.statusText=="2"){
					layer.alert("退回成功!");
				}else if(jsonData.statusText=="3"){
					layer.alert("退回失败!");
				}else{
					layer.alert("异常!");
				}*/
			}
		});
	});
}
// 反欺诈拒赔
function fraudPayZero(){
	var isFraud = $("input[name=isFraud]:checked").val();  //是否欺诈
	var fraudLogo = $("#fraudLogo").val(); //欺诈标志
	if("1"==isFraud&&(fraudLogo=="01"||fraudLogo=="02")){
		$("input[name$='sumRealPay']").each(function(){
//			var payFlag = $(this).parents("tr").find("input[name$='payFlag']").val();
			if($(this).val()!=null&&$(this).val()!=""){
				$(this).val("0");
			}
		});
		//本次赔款  置为0
		$("input[name='prpLCompensate.sumPaidAmt']").val("0"); //隐藏域置为0
		$("span#sumThisALL").text("0");
        //赔款总金额 = 预付+垫付 金额
		$("input[name='prpLCompensate.sumAmt']").val($("input[name='prpLCompensate.sumPreAmt").val()); //隐藏域置为0
		$("span#sumLossALL").text($("input[name='prpLCompensate.sumPreAmt").val());
	}
}



//费用赔款信息摘要
function paySummary(element,chargeCode){
	//alert($(element option:selected).text());
	var licenseNoSummary = $("#licenseNoSummary").val();
	$("input[name='"+chargeCode+"']").val(licenseNoSummary+$(element).find("option:selected").text());
}

function refuseTip(){
	var compType = $("#compTypeX").val();
	var isRefuse = $("#isRefuse").val();
	if("1" == compType && "1"== isRefuse){
		layer.alert("该案件为结案拒赔案件!");
	}
	if("2" == compType && "1"== isRefuse){
		layer.alert("该案件为结案拒赔案件!");
	}
}

//摘要特殊字符校验
function checkSpecialCharactor(element,nowStr){
	var str = $(element).val();
	//var pattern = "^‘'！!#$%*+，,-./:：=？?@【】[\]_`·{|}~ ";//特殊字符
	var pattern = "^'!#$%*+,-./:=?@[\]_`{|}~ ……‘！#￥%*+，-。/：；=？@【、】——·{|}~ ";//特殊字符
	if(typeof(str) === "undefined" || str === null || str===""){
		$(element).val(nowStr);
		return false;
	}
	for(var i = 0,size = pattern.length;i<size;i++){
		var reg = pattern.substring(i, i+1);
		if(str.indexOf(reg)>-1){
			layer.alert("包含特殊字符“"+reg+"”,请核实！");
			$(element).val(nowStr);
			return false;
		}
	}
	//对于&quot和&lt和&gt不能同时出现
	var regArr = new Array();
	regArr.push("&quot");
	regArr.push("&lt");
	regArr.push("&gt");
	for(var i = 0,size = regArr.length;i<size;i++){
		var reg = regArr[i];
		if(str.toLowerCase().indexOf(reg)>-1){
			//进行转义提示
			//reg = "\\"+reg;
			alert("包含特殊字符“"+reg+"”,请核实！");
			$(element).val(nowStr);
			return false;
		}
	}
	return true;
}

//客户识别
/**function customerIden(){
	layer.open({
	    type: 2,
	    title: "客户身份重现识别【客户号：蕾凤蕾逃犯】",
		scrollbar : true,
	    skin: 'yourclass',
	    area: ['990px', '400px'],
	    content:["/claimcar/compensate/customerIden.do"]
	});
}
 */
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

/**
 * 获取地址链接
 * @returns {Boolean}
 */
function getCustomerFxqUrl(){
	$.ajax({
		url : "/claimcar/compensate/getFxqUrl.ajax", // 后台校验
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

/**
 * 判断收款人是否冻结，冻结不能提交
 * @returns {Boolean}
 */
function isFrostFlag(){
	var payList="";
	var registNo=$("#registNo").val();
    var arr = new Array();
	var i = 0 ;
	var nameList = new Array();
	$("#PayCustomTbody input[name$='payeeId']").each(function(){
		arr[i] = $(this).val();
		payList+=arr[i]+",";
		i = i +1;
		
	});
	
	if(isBlank(payList)){
		return true;
	}
	
	var params = {"payeeList" : payList,
			      "registNo":registNo
	                };
    $.ajax({
		url : "/claimcar/compensate/isFrostFlag.ajax", // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(jsonData) {// 回调方法，可单独定义
		    var result = eval(jsonData);
			if (result.status == "200" ) {
				nameList=result.data;
				
			}
		}
	});
	
	var str="";
	if(!isBlank(nameList)){
		for(var i=0;i<nameList.length;i++){
			str+=nameList[i]+",";
			}
		str=str.substring(0,str.length-1);//将最后一个逗号去掉
		layer.alert("收款人[ "+str+" ]被冻结");
		
		return false;
	}
	return true;
}

//反洗钱补录
function CustomInfoOpen(claimNo,registNo){
	var pid='';
	var params ="?registNo=" + registNo+"&pid="+pid+"&claimNo="+claimNo;
	var url = "/claimcar/payCustom/addRowInfo.do";
	 layer.open({
		type : 2,
		title : "反洗钱信息补录",
		shadeClose : true,
		area: ['90%', '80%'],
		content : url + params
	});
	
}

//反洗钱冻结
function freezeUp(claimNo,registNo,riskCode){
	
      params="?claimNo="+claimNo+"&registNo="+registNo+"&riskCode="+riskCode;
	
	var url = "/claimcar/payCustom/freezeUp.do";
	layer.open({
     	type: 2,
     	title:"反洗钱冻结",
     	shadeClose : true,
		scrollbar : false,
     	area: ['95%', '70%'],
        content: url + params
 	});
}

//控制是否有反洗钱特征
function lossOrNot(value, id){
	if(value==1){
		$("#"+id+"Table").removeClass('hide');
		$("#"+id+"Table").find("input").each(function(){
		$(this).prop("disabled",false);
		});
		$("#causes").attr("datatype","*1-200").addClass("Validform_error");
		$("[name='prplcomContextVo.flagContext']").attr("datatype","*").addClass("Validform_error");
		$("#ContextVoFlag").val("1");
		
		
	}else{
		$("#"+id+"Table").addClass('hide');
		$("#"+id+"Table").find("input").each(function(){
		$(this).prop("disabled",true);
			});
		$("#causes").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		$("[name='prplcomContextVo.flagContext']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		$("#ContextVoFlag").val("0");
		$("#causes").val("");
		$("input[name='prplcomContextVo.flagContext']").each(function(){
			$(this).prop("checked",false);
		});
		
	}
	/*$("#"+id).val(value);
	//财产损失增加损失方车牌号
	linceseNoChg("car");
	writeAccidentTypes();*/
}


function indemnityDutyCheck(operation){
	var returnFlag = true;
	
	var registNo = $("[name='prpLCompensate.registNo']").val();
	
	var dutyType_BI = $("#indemnityDuty");
	var dutyRate_BI = $("[name='prpLCompensate.indemnityDutyRate']");	
	var dutyType_CI = $("[name='prpLCheckDutyVoList[0].indemnityDuty']");
	var dutyRate_CI = $("[name='prpLCheckDutyVoList[0].indemnityDutyRate']");
	
	var params = {
		      "registNo":registNo,
              };
    $.ajax({
		url : "/claimcar/compensate/isControlDuty.ajax", // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(jsonData) {// 回调方法，可单独定义
		    var result = eval(jsonData);
		    var checkDuty = result.data;
			if (result.status == "200" ) {
				if("1101"==$("#riskCode").val()){
					$("[name$='].indemnityDuty']").each(function(){
						$(this).attr("disabled","disabled");
					});
					$("[name$='].indemnityDutyRate']").each(function(){
						$(this).attr("readonly","readonly");
					});
				}else{
					dutyType_BI.attr("disabled","disabled");
					dutyRate_BI.attr("readonly","readonly");
				}	
				
				if("submit"==operation){
					if("1101"==$("#riskCode").val()){
						if(checkDuty.indemnityDuty!=dutyType_CI.val()||checkDuty.indemnityDutyRate!=dutyRate_CI.val()){
							returnFlag = false;
						}
					}else{
						if(checkDuty.indemnityDuty!=dutyType_BI.val()||checkDuty.indemnityDutyRate!=dutyRate_BI.val()){
							returnFlag = false;
						}
					}
					
				}
			}
		}
	});
    return returnFlag;
}

$("input[name='prpLCompensate.inSideDeroFlag']").click(function(){
	var checkvalue=$("input[name='prpLCompensate.inSideDeroFlag']:checked").val();
	if(checkvalue=='1'){
		$("#cisderoAmout").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		$("#cisderoAmout").attr("datatype","isDeroAmout");
		$("#cisderoAmout").attr("errormsg","请输入正整数或者正数保留两位小数");
		
	}else{
		$("#cisderoAmout").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		$("#cisderoAmout").attr("datatype","isDeroAmoutCom");
		$("#cisderoAmout").attr("errormsg","请输入非负整数或者保留两位小数");
	}
});

$("[name='prpLCompensate.coinsFlag']").click(function(){
	//如果是否协议选是，则车辆、财产、人伤损失项的核定金额字段放开只读控制，允许手录
	var coinsFlag  = $("input[name$='prpLCompensate.coinsFlag']:checked").val();
	if(coinsFlag == 1){
		$("#coinsDiv").show();
		$("input[name='prpLCompensate.coinsAmt']").removeAttr("datatype").attr("datatype","amount");
		$("#carLossDiv input[name$='sumRealPay']").each(function(){
			$(this).removeAttr("readonly");
		});
		$("#propLossDiv input[name$='sumRealPay']").each(function(){
			$(this).removeAttr("readonly");
		});
		$("#persLossDiv input[name$='sumRealPay']").each(function(){
			$(this).removeAttr("readonly");
		});
	}else{
		$("#coinsDiv").hide();
		$("input[name='prpLCompensate.coinsAmt']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("input[name='prpLCompensate.coinsAmt']").val(null);
		$("input[name='prpLCompensate.coinsDesc']").val(null);
		$("#carLossDiv input[name$='sumRealPay']").each(function(){
			$(this).attr("readonly","readonly");
		});
		$("#propLossDiv input[name$='sumRealPay']").each(function(){
			$(this).attr("readonly","readonly");
		});
		$("#persLossDiv input[name$='sumRealPay']").each(function(){
			$(this).attr("readonly","readonly");
		});
	}
});

function changeRealPay(){
	var coinsFlag  = $("input[name$='prpLCompensate.coinsFlag']:checked").val();
	if(coinsFlag == 1){
		amtSum();
	}
}

function importExcel(compensateNo,bussType,feeCode,payId,payName,indexNo,bussId) {
	
	//添加附件start
	var formData = new FormData();
    for(var i=0;i<$("#Upfile"+indexNo)[0].files.length;i++) {  //循环获取上传个文件
        formData.append("file", $("#Upfile"+indexNo)[0].files[i]);
    }
    formData.append("compensateNo",compensateNo);
    formData.append("bussType",bussType);
    formData.append("feeCode",feeCode);
    formData.append("payId",payId);
    formData.append("payName",payName);
    formData.append("bussId",bussId); 
    $.ajax({
        "url": "/claimcar/bill/importExcel.ajax",
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

