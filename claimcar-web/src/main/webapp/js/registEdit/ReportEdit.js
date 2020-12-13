/**
 * 
 */
$('.Hui-article-box .floatLayBox').show(); //备注风险显示
		
var submitFlag = false;
var damageAddressFlags=0;
var ajaxEdit;
function caseDetailss(title,url,id){
	layer.open({
     	type: 2,
     	title:title,
     	area: ['90%', '25%'],
    	fix: false, //不固定
     	maxmin: false,
    	content: url
 	});
	//layer.full(index);
};

function initLossPart(){
	$("input[name$='losspart']").each(function(){
		var val = $(this).val();
		if(val=="5"||val=='8'||val=='11'||val=='6'||val=='9'){
			$(this).parents("label").hide();
		}
		if(val == "7"){//左中
			$(this).next().text("左");
		}
		if(val == "10"){//右中
			$(this).next().text("右");
		}
	});
}

$(function(){
	initLossPart();
	displayDisasterTwo();
	var rules = {
	};
	ajaxEdit = new AjaxEdit($('#editform'));
	ajaxEdit.beforeCheck=function(){//校验之前
		
	};
	ajaxEdit.beforeSubmit=function(){//提交前补充操作
		CheckAllLossHis();
		var repairId= $("#repairId").val();
		if(!isBlank(repairId)){
			layer.msg("此保单为推修业务，请知悉！",{icon: 6,offset:'lt'});
		}
	};
	ajaxEdit.rules =rules;
	ajaxEdit.afterSuccess=function(data){//操作成功后操作
		
		var result = eval(data);
		$("#registNo").val(result.data);
		var messageText = "";
		if (submitFlag) {
			if (result.status == "200") {
				var url = "/claimcar/regist/submitSchedule?registNo=" + result.data;
				layer.open({
			     	type: 2,
			     	closeBtn: 0,
			     	title: "报案任务提交",
			     	area: ['75%', '50%'],
			    	fix: true, //不固定
			     	maxmin: false,
			    	content: url,
			    	btn: ['返回'],
				    yes: function(index, layero){ //或者使用btn1
				    	layer.load(0, {shade : [ 0.8, '#393D49' ]});
				    	location.href = "/claimcar/regist/edit.do?registNo=" + result.data;
				    }
			    	
			 	});
			} else {
				messageText = '操作失败：' + result.statusText;
				layer.alert(messageText);
			}
		} else {
			layer.confirm('暂存成功,报案号：'+result.data, {
			    btn: ['确定'],cancel: function(){
			    	layer.load(0, {shade : [ 0.8, '#393D49' ]});
			    	location.href = "/claimcar/regist/edit.do?registNo=" + result.data;
			    }
			}, function(index){
				layer.load(0, {shade : [ 0.8, '#393D49' ]});
				location.href = "/claimcar/regist/edit.do?registNo=" + result.data;
				layer.close(index);
			});
		}
		layer.closeAll('loading');
	}; 
	/**
	 * 报案提交时，历次出险出现过全车盗抢、推定全损，报案提交需给出软提示
	 */
	function CheckAllLossHis(){
		
		var BIPolicyNo = $("#BIPolicyNo").val();
		var CIPolicyNo = $("#CIPolicyNo").val();
		var params = {"BIPolicyNo" : BIPolicyNo,"CIPolicyNo" : CIPolicyNo};
	    $.ajax({
			url : "/claimcar/regist/checkAllLossHis.ajax", // 后台校验
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async : false,
			success : function(jsonData) {// 回调方法，可单独定义
			   if(jsonData.data!='NoAllLossHis'){
				   layer.alert(jsonData.data);
			   }
			}
		});
	}
	//绑定表单
	/*
	ajaxEdit.targetUrl = "/claimcar/regist/save.do?BIPolicyNo=";
	if ($("input[id='BIPolicyNo']:checked").val() != undefined) {
		ajaxEdit.targetUrl = ajaxEdit.targetUrl + $("#BIPolicyNo").val();
	}
	ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&CIPolicyNo=";
	if ($("input[id='CIPolicyNo']:checked").val() != undefined) {
		alert($("#CIPolicyNo").val()+"较强");
		ajaxEdit.targetUrl = ajaxEdit.targetUrl + $("#CIPolicyNo").val();
	}
	
	ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&CIComCode=";
	$("input[name='checkCode']:checked").each(function(i) {
		if($(this).attr("id")=="CIPolicyNo"){
		ajaxEdit.targetUrl = ajaxEdit.targetUrl + $(this).attr("CIComCode");
		}
	});
	
	ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&BIComCode=";
	$("input[name='checkCode']:checked").each(function() {
		if($(this).attr("id")=="BIPolicyNo"){
		ajaxEdit.targetUrl = ajaxEdit.targetUrl + $(this).attr("BIComCode");
		}
	});
	ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&CIRiskCode=";
	$("input[name='checkCode']:checked").each(function() {
		if($(this).attr("id")=="CIPolicyNo"){
		ajaxEdit.targetUrl = ajaxEdit.targetUrl + $(this).attr("CIRiskCode");
		}
	});
	
	ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&BIRiskCode=";
	$("input[name='checkCode']:checked").each(function() {
		if($(this).attr("id")=="BIPolicyNo"){
		ajaxEdit.targetUrl = ajaxEdit.targetUrl + $(this).attr("BIRiskCode");
		}
	});*/
	ajaxEdit.bindForm();
	
	countSum();
	//$.Datatype.licenseNo = "/(^[\u4e00-\u9fa5]{1}(([A-Z]{1})|([0-9]{2}))[A-Z_0-9]{5}$)|(^新[0]{6}$)/";
	/*$.Datatype.licenseNo =  function(gets,obj,curform,regxp){
		var licenseNo = $(obj).val();
			var Expression = /^(WJ|[\u0391-\uFFE5]{1})[A-Z0-9]{5,}[\u0391-\uFFE5]{0,1}$/;
			if(licenseNo.length>11 || licenseNo.length < 6){
				return false;
				
			}else{
				if(!Expression.test(licenseNo)){
					return false;
				}
			}
    };*/
	
	$.Datatype.selectMust = function(gets,obj,curform,regxp){
		var code = $(obj).val();
		if(isBlank(code)){
			return false;
		}
    };
	$.Datatype.idNo = function(gets,obj,curform,regxp){
        //参数gets是获取到的表单元素值，
        //obj为当前表单元素，
        //curform为当前验证的表单，
        //regxp为内置的一些正则表达式的引用。
        //return false表示验证出错，没有return或者return true表示验证通过。
		var code = $(obj).val();
		if(!validIdentityCode(code)){
			return false;
		}else{
			if ( !($("#driverIdfNo").val().length>0) ) {
				$("#driverIdfNo").val(code);
			}
			
		}
    };
    
 // $("#reportorRelation").attr("datatype","selectMust");
	
	if($("#CheSun").val() == 1) {
		lossOrNot(1, "CheSun");
		$("#isCarLossY").prop("checked", true);
	} else {
		lossOrNot(0, "CheSun");
	}
	if($("#RenShang").val() == 1) {
		lossOrNot(1, "RenShang");
		$("#isPersonLossY").prop("checked", true);
	} else {
		lossOrNot(0, "RenShang");
	}
	if($("#WuSun").val() == 1) {
		lossOrNot(1, "WuSun");
		$("#isPropLossY").prop("checked", true);
	} else {
		lossOrNot(0, "WuSun");
	}
	$("#ShDamageAreaCode_lv1").find("option[value='310000']").attr("selected","selected");
	$("#ShDamageAreaCode_lv1").attr("disabled",true);
	
	$(".close").click(function(){
		$(this).parent().parent(".flaotWindow").css("display","none");
		});
	
	
	//报案注销
	/*$("input.btn-cancelBtn").click(function() {
		var cancelReason = "";
		$("#cancelOut select").each(function() {
			cancelReason = $(this).val();
		});
		if (cancelReason == "") {
			layer.msg("请选择注销原因。");
			return false;
		}
		
		var registNos = $("#registNo").val();
		$.ajax({
			url : "/claimcar/regist/reportCancels.do?registNo="+registNos, // 后台处理程序
			type : 'post', // 数据发送方式
			success : function(jsonData) {// 回调方法，可单独定义
				if(jsonData.status==200){
					if(jsonData.data==1){
						layer.confirm('是否注销', {
							btn : [ '是','否']
							}, function(index) {
						
						layer.close(index);
						layer.load(0, {shade : [0.8, '#393D49']});
						$("#cancelOut select").each(function() {
							$(this).attr("disabled",true);
						});
						var $divIn = $("#cancelIn");
						$(this).removeClass("btn-primary");
						$(this).removeClass("btn");
						$(this).addClass("hide");
						var registNo = $("#registNo").val();
						
						
						var params = {
							"cancelReason" : cancelReason,
							"registNo" : registNo,
						};
						var url = "/claimcar/regist/reportCancel.ajax";
						$.post(url, params, function(result) {
							$divIn.append(result);
							layer.alert("注销成功!");
							layer.confirm('注销成功!', {
								btn : [ '确定']
								}, function(index) {
									layer.closeAll('loading');
									layer.close(index);
									location.href = "/claimcar/regist/edit.do?registNo="+registNos;
									//window.location.reload();//刷新当前页面.
								});
						});
						
					}, function(index){
						layer.close(index);
					});
					
					}else{
						layer.alert("已立案不能报案注销");
					}
				}
				
			}
		});
	});*/
	$("input.btn-saveBtn").click(function() {
		/*var renShangTable = document.getElementById("RenShangTable").className;
		var wuSunTable = document.getElementById("WuSunTable").className;*/
		var renShangTable = $("#RenShangTable").attr("class");
		var wuSunTable = $("#WuSunTable").attr("class");
		if(renShangTable=="table_wrap hide"){
		    $("#injCountTag").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		    $("#deaCountTag").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		    $("#injCountThird").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		    $("#deaCountThird").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		    $("#injuredSum").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		    $("#deathSum").removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
		}else{
			//加上
			$("#injCountTag").removeAttr("datatype").attr("datatype","n");
			$("#deaCountTag").removeAttr("datatype").attr("datatype","n");
			$("#injCountThird").removeAttr("datatype").attr("datatype","n");
			$("#deaCountThird").removeAttr("datatype").attr("datatype","n");
			$("#injuredSum").removeAttr("datatype").attr("datatype","n");
			$("#deathSum").removeAttr("datatype").attr("datatype","n");
		}
		if(wuSunTable=="table_wrap hide"){
			$("#thridCarList [name$='licenseNo']").each(function(){
				$(this).removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
			});
			$("#thridCarList input[name$='lossitemname']").each(function(){
				$(this).removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
			});
			$("#thridCarList input[name$='damagelevel']").each(function(){
				$(this).removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
			});
			$("#thridCarList input[name$='lossremark']").each(function(){
				$(this).removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
			});
			 
		}else{
			//加上
			$("#thridCarList [name$='licenseNo']").each(function(){
				$(this).removeAttr("datatype").attr("datatype","*1-40");
			});
			$("#thridCarList input[name$='lossitemname']").each(function(){
				$(this).removeAttr("datatype").attr("datatype","*1-40");			
			});
			$("#thridCarList input[name$='damagelevel']").each(function(){
				$(this).removeAttr("datatype").attr("datatype","*0-40");
				});
			$("#thridCarList input[name$='lossremark']").each(function(){
				$(this).removeAttr("datatype").attr("datatype","*0-100");
				});
		}
		
		layer.load(0, {shade : [0.8, '#393D49']});
		disabledSec($("input[name='registSubmit']"), 5);
		//关地图
		var switchMap = $("#switchMap").val();
		if(switchMap=="1"){
			var switchMapAreaCode = $("#damageAreaCode_lv3").val();
			var switchMapCheckAreaCode = $("#checkAddressCode_lv3").val();
			
			var selfAreaCode = switchMapAreaCode.substring(0,1);
			
			if(selfAreaCode=="C"){
				$("#selfDefinareaCode").val(switchMapAreaCode);
			}
			$.ajax({
				url : '/claimcar/schedule/switchMapGpsCode.do?areaCode='
						+ switchMapAreaCode,
				dataType : "json",// 返回json格式的数据
				type : 'get',
				success : function(json) {// json是后端传过来的值
					 $("#damageMapCode").val(json.statusText);
				},
				error : function() {
					layer.msg("获取地址数据异常");
				}
			});
			$.ajax({
				url : '/claimcar/schedule/switchMapGpsCode.do?areaCode='
						+ switchMapCheckAreaCode,
				dataType : "json",// 返回json格式的数据
				type : 'get',
				success : function(json) {// json是后端传过来的值
					 $("#checkAddressMapCode").val(json.statusText);
				},
				error : function() {
					layer.msg("获取地址数据异常");
				}
			});
		}
		//查勘类型选择快处快赔，包含了人伤，提交时提示：包含人伤不许选择快处快赔。checkType
		if($("#checkType").val()=="2"){
			var isPersonLossY = $("input[id='isPersonLossY']:checked").val();
			if(isPersonLossY=="1"){//有人伤
				layer.closeAll('loading');
				layer.alert("案件包含人伤不许选择快处快赔");
				return false;
			}
			if($("#damage_Code").val()=="DM04"){
				layer.closeAll('loading');
				layer.alert("出险原因为全车盗抢不许选择快处快赔");
				return false;
			}
			//玻璃案件不许选择快处快
			if($("#damage_Code").val()=="DM02"){
				layer.closeAll('loading');
				layer.alert("玻璃案件不许选择快处快赔");
				return false;
			}
		}
		//如果出险原因选择了全车盗抢，然后录入了人伤，提交时提示：全车盗抢案件不许包含人伤
		//如果出险原因选择了玻璃单独破碎，然后录入了人伤，提交时提示：玻璃案件件不许包含人伤
		if($("#damage_Code").val()=="DM02"){
			var isPersonLossY = $("input[id='isPersonLossY']:checked").val();
			if(isPersonLossY=="1"){//有人伤
				layer.closeAll('loading');
				layer.alert("玻璃案件不许包含人伤");
				return false;
			}
		}
		if($("#damage_Code").val()=="DM04"){
			var isPersonLossY = $("input[id='isPersonLossY']:checked").val();
			if(isPersonLossY=="1"){//有人伤
				layer.closeAll('loading');
				layer.alert("全车盗抢案件不许包含人伤");
				return false;
			}
		}
		///当选择双方、多方时，提交时要管控必须录入三者车的车牌号。
		var accidentTypes  = $("#AccidentTypes").val();
		//与被保人关系必须录入
		var reportorRelation  = $("#reportorRelation").val();
		if(reportorRelation == ""){
			layer.closeAll('loading');
		layer.alert("与被保人关系不能为空！");
			return false;
		}
		/*if(accidentTypes=="03" || accidentTypes=="02"){
			var carSize = parseInt($("#carSize").val());
			if(Number(carSize) < 2){
				layer.closeAll('loading');
				layer.alert("必须录入三者车的车牌号");
				return false;
			}
			
		}*/
		var CIPolicyNo = $("input[id='CIPolicyNo']:checked").val(); 
		var BIPolicyNo = $("input[id='BIPolicyNo']:checked").val(); 
		var duoBao = 0;
		$("#thirdCars").find("[name$='licenseNo']").each(function(){
			duoBao+=1;
		});
		var isPersonLossY = $("input[id='isPersonLossY']:checked").val();
		var isPropLossY = $("input[id='isPropLossY']:checked").val();
		
		//if(parseInt(duoBao)<1 && isPersonLossY!="1" && isPropLossY!="1"){
			/*var CIPolicyNo = $("input[id='CIPolicyNo']:checked").val(); 
			var BIPolicyNo = $("input[id='BIPolicyNo']:checked").val(); */
			if(CIPolicyNo!=null&&CIPolicyNo!= "undefined"&&BIPolicyNo!=null&&BIPolicyNo!= "undefined"){
				if(parseInt(duoBao)<1){
					if(isPersonLossY!="1"){
						if(isPropLossY!="1"){
							layer.closeAll('loading');
							layer.alert("请取消关联交强险");
							return false;
						}
					}
				}
				
				
			}
			
		//}
		//需求==========
		//有人伤人员必须大于0；
		if(isPersonLossY=="1"){
			var injCountTag = $("#injCountTag").val();
			var deaCountTag = $("#deaCountTag").val();
			var injCountThird = $("#injCountThird").val(); 
			var deaCountThird = $("#deaCountThird").val();
			if(injCountTag == 0 && deaCountTag == 0 && injCountThird == 0 && deaCountThird == 0){
				layer.closeAll('loading');
				layer.alert("有人伤，伤亡人数不能全为0!");
				return false;
			}
		}
		if(isPropLossY=="1"){
			isPropLossY = 0;
			$("input[name$='damagelevel']").each(function(){
				
				if($(this).prop("checked")==true){
					if($(this).val()==1){
						isPropLossY = 1;
					}
					
				}
			});
		}else{
			isPropLossY = 0;
		}
		if ($(this).attr("id") == "submit") {
			submitFlag = true;
		}else{
			submitFlag = false;
		}
		if(submitFlag){
			//无保单报案，有标的有三者车、财产、人伤时，只关联商业险，交强险在我司承保时，未提示关联交强险START
			
			/*var damageTime = $("#damageTime").val();
			
			if(BIPolicyNo!=null && typeof(CIPolicyNo) == "undefined"){
				var  t = BIPolicyNo.substring(0,1);
				
				if(t!="T"){
					//查询交强险在我司承保
					$.ajax({
						url : "/claimcar/policyQuery/relatedSearch.do?BIPolicyNo="+BIPolicyNo+"&damageTime="+damageTime, // 后台处理程序
						type : 'post', // 数据发送方式
						success : function(jsonData) {// 回调方法，可单独定义
							if(jsonData.StatusText=="0"){
								if(parseInt(duoBao) > 0 && isPersonLossY=="1" && isPropLossY=="1"){
									layer.alert("请关联交强险");
									return false;
								}
							}
						}
					});
				}
			}*/
			
			//END
			
			var lossparty = "0";
			var indexNum= 0;
			$("#propTbody input[name$='lossparty']").each(function(){
				if($(this).val()!="1" && $(this).val()!=""){
					//加是否物损
					var damagelevel = $("#propTbody input[name='prpLRegistPropLosses["+indexNum+"].damagelevel']:checked").val();
					if(damagelevel == "1"){
						lossparty = "1";//三者
					}
				}
				indexNum = indexNum + 1;
			});
			
			if(parseInt(duoBao)<1 ){
				if(CIPolicyNo!=null && CIPolicyNo!= "undefined" && BIPolicyNo!=null && BIPolicyNo!= "undefined"){
					if(isPersonLossY!="1"){
						if(isPropLossY!="1" ){
							layer.closeAll('loading');
							layer.alert("请取消关联交强险");
							return false;
						}else{
							if(isPropLossY=="1"&& lossparty!="1"){
								layer.closeAll('loading');
								layer.alert("请取消关联交强险");
								return false;
							}
						}
					}else{
						if(($("#injCountThird").val()=='0' && $("#deaCountThird").val()=='0' )){
							if(isPropLossY!="1" ){
								layer.closeAll('loading');
								layer.alert("请取消关联交强险");
								return false;
							}else{
								if(isPropLossY=="1"&& lossparty!="1"){
									layer.closeAll('loading');
									layer.alert("请取消关联交强险");
									return false;
								}
							}
						}
					}
					
				}
			}
			var tiJiao = $("#tiJiao").val();
			if(parseInt(tiJiao)=="1"){
				layer.closeAll('loading');
				layer.alert("请重新选择出险原因！");
				return false;
			}
		 }
		
		//单交强险报案，只录标的车损，未录三者车损、无物和人伤，点击报案提交时，需管控不允许报案，硬控制
		if(submitFlag){
			var CIPolicyNo = $("input[id='CIPolicyNo']:checked").val(); 
			var BIPolicyNo = $("input[id='BIPolicyNo']:checked").val(); 
		/*	if(parseInt(duoBao)<1 && isPersonLossY!="1" && isPropLossY!="1"){
				var CIPolicyNo = $("input[id='CIPolicyNo']:checked").val(); 
				var BIPolicyNo = $("input[id='BIPolicyNo']:checked").val(); 
				if(CIPolicyNo!=null&&BIPolicyNo==null){
					layer.alert("单交强险未录三者车损、无财和人伤不允许报案");
					return false;
				}
				
			}*/
			
			if(parseInt(duoBao)<1 ){
				if(CIPolicyNo!=null&&BIPolicyNo==null){
					if(isPersonLossY!="1"){
						if(isPropLossY!="1" ){
							layer.closeAll('loading');
							layer.alert("单交强险未录三者车损、无财和人伤不允许报案");
							return false;
						}else{
							if(isPropLossY=="1"&& lossparty!="1"){
								layer.closeAll('loading');
								layer.alert("单交强险未录三者车损、无财和人伤不允许报案");
								return false;
							}
						}
					}else{
						if(($("#injCountThird").val()=='0' && $("#deaCountThird").val()=='0' )){
							
							if(isPropLossY!="1" ){
								layer.closeAll('loading');
								layer.alert("单交强险未录三者车损、无财和人伤不允许报案");
								return false;
							}else{
								if(isPropLossY=="1"&& lossparty!="1"){
									layer.closeAll('loading');
									layer.alert("单交强险未录三者车损、无财和人伤不允许报案");
									return false;
								}
							}
						}
					}
					
				}
			}
		 }
		
		
		ajaxEdit.targetUrl = "/claimcar/regist/save.do?BIPolicyNo=";
		if ($("input[id='BIPolicyNo']:checked").val() != undefined) {
			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $("#BIPolicyNo").val();
		}
		ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&CIPolicyNo=";
		if ($("input[id='CIPolicyNo']:checked").val() != undefined) {
			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $("#CIPolicyNo").val();
		}
		
		ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&CIComCode=";
		$("input[name='checkCode']:checked").each(function(i) {
			if($(this).attr("id")=="CIPolicyNo"){
			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $(this).attr("CIComCode");
			}
		});
		
		ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&BIComCode=";
		$("input[name='checkCode']:checked").each(function() {
			if($(this).attr("id")=="BIPolicyNo"){
			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $(this).attr("BIComCode");
			}
		});
		ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&CIRiskCode=";
		$("input[name='checkCode']:checked").each(function() {
			if($(this).attr("id")=="CIPolicyNo"){
			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $(this).attr("CIRiskCode");
			}
		});
		
		ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&BIRiskCode=";
		$("input[name='checkCode']:checked").each(function() {
			if($(this).attr("id")=="BIPolicyNo"){
			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $(this).attr("BIRiskCode");
			}
		});
		//----------------------------------
//		var rules = {
//		};
//		var ajaxEdit = new AjaxEdit($('#editform'));
//		ajaxEdit.beforeCheck=function(){//校验之前
//			
//		};
//		ajaxEdit.beforeSubmit=function(){//提交前补充操作
//			
//		};
//		ajaxEdit.rules =rules;
//		ajaxEdit.afterSuccess=function(data){//操作成功后操作
//			var result = eval(data);
//			$("#registNo").val(result.data);
//			var messageText = "";
//			if (submitFlag) {
//				if (result.status == "200") {
//					var url = "/claimcar/regist/submitSchedule?registNo=" + result.data;
//					layer.open({
//				     	type: 2,
//				     	closeBtn: 0,
//				     	title: "报案任务提交",
//				     	area: ['75%', '50%'],
//				    	fix: true, //不固定
//				     	maxmin: false,
//				    	content: url,
//				    	btn: ['返回'],
//					    yes: function(index, layero){ //或者使用btn1
//					    	location.href = "/claimcar/regist/edit.do?registNo=" + result.data;
//					    }
//				    	
//				 	});
//				} else {
//					messageText = '操作失败：' + result.statusText;
//					layer.alert(messageText);
//				}
//			} else {
//				layer.confirm('暂存成功,报案号：'+result.data, {
//				    btn: ['确定'] //按钮
//				}, function(){
//					location.href = "/claimcar/regist/edit.do?registNo=" + result.data;
//				});
//			}
//			
//		}; 
//		
//		//绑定表单
//		
//		ajaxEdit.targetUrl = "/claimcar/regist/save.do?BIPolicyNo=";
//		/*if ($("#BIPolicyNo").val() != undefined) {
//			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $("#BIPolicyNo").val();
//		}
//		ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&CIPolicyNo=";
//		if ($("#CIPolicyNo").val() != undefined) {
//			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $("#CIPolicyNo").val();
//		}*/
//		if ($("input[id='BIPolicyNo']:checked").val() != undefined) {
//			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $("#BIPolicyNo").val();
//		}
//		ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&CIPolicyNo=";
//		if ($("input[id='CIPolicyNo']:checked").val() != undefined) {
//			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $("#CIPolicyNo").val();
//		}
//		//
//		
//		/*$("input[name='checkCode']:checked").each(function() {
//			plyNoArr.push($(this).attr("id"));
//			relaPlyNoArr.push($(this).val());
//			comCodeArr.push($(this).attr("comCode"));
//		});*/
//		/*$("input[name='BIPolicyNoS']:checked").each(function() {
//			alert($(this).attr("BIComCode"));
//			return;
//		});*/
//		ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&CIComCode=";
//		$("input[name='checkCode']:checked").each(function(i) {
//			if($(this).attr("id")=="CIPolicyNo"){
//			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $(this).attr("CIComCode");
//			}
//		});
//		
//		ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&BIComCode=";
//		$("input[name='checkCode']:checked").each(function() {
//			if($(this).attr("id")=="BIPolicyNo"){
//			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $(this).attr("BIComCode");
//			}
//		});
//		ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&CIRiskCode=";
//		$("input[name='checkCode']:checked").each(function() {
//			if($(this).attr("id")=="CIPolicyNo"){
//			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $(this).attr("CIRiskCode");
//			}
//		});
//		
//		ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&BIRiskCode=";
//		$("input[name='checkCode']:checked").each(function() {
//			if($(this).attr("id")=="BIPolicyNo"){
//			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $(this).attr("BIRiskCode");
//			}
//		});
//		ajaxEdit.bindForm();
		var damageReason = $("#damage_Code").val();
		var sumDe=$("#deathSum").val();
		if(submitFlag && (damageReason == "DM04" || parseInt(sumDe) > 0)){//提交时,如是盗抢或者死亡案件
			layer.confirm('此案件涉及盗抢或死亡案件，为重大案件，是否确认提交', {
			    btn: ['确定','取消'] //按钮
			}, function(index){
				save();
				layer.close(index);
			}, function(index){
				layer.close(index);
				layer.closeAll('loading');
				return false;
			});
		}else{
			save();
		}
		//$('#editform').submit();
		
		
	});
	$("input.btn-pendBtn").click(function() {
		ajaxEdit.targetUrl = "/claimcar/regist/save.do?BIPolicyNo=";
		/*if ($("#BIPolicyNo").val() != undefined) {
			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $("#BIPolicyNo").val();
		}
		ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&CIPolicyNo=";
		if ($("#CIPolicyNo").val() != undefined) {
			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $("#CIPolicyNo").val();
		}*/
		if ($("input[id='BIPolicyNo']:checked").val() != undefined) {
			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $("#BIPolicyNo").val();
		}
		ajaxEdit.targetUrl = ajaxEdit.targetUrl +"&CIPolicyNo=";
		if ($("input[id='CIPolicyNo']:checked").val() != undefined) {
			ajaxEdit.targetUrl = ajaxEdit.targetUrl + $("#CIPolicyNo").val();
		}
		
		save();
	});
	
	$("#checkRegistMsg").click(
			function checkRegistMsgInfo(){//打开案件备注之前检验报案号和节点信息是否为空
				var registNo=$("#registNo").val();
				var nodeCode=$("#nodeCode").val();
				if(isBlank(registNo)||isBlank(nodeCode)){
					layer.msg("未生成报案号,请先暂存后再添加案件备注");
				}else{
					createRegistMessage(registNo,nodeCode);
				}
			});
	

	var dam_other=$("#dam_other");
	var damage_Code = $("#damage_Code");
	//报案登记新增页面，出险原因下拉框选择“其他”，显示附加选项框，将“倾覆”移动至前一下拉框“全车盗抢”下，将前一下拉框“车上货物人员意外撞击”移动至附加选项框。
	/*dam_other.find("option").each(function(){
		if($(this).val()=="DM51"){
			//将“倾覆”移动至前一下拉框“全车盗抢”下
			$(this).remove();
		}
	});
	
	damage_Code.find("option").each(function(){
		if($(this).val()=="DM11"){
			//删除车上货物人员意外撞击
			$(this).remove();
		}
		if($(this).val()=="DM04"){
			//将“倾覆”移动至前一下拉框“全车盗抢”下
			  $("<option value='DM51'>倾覆</option>").insertAfter($(this));
		}
	});
	dam_other.append("<option value='DM11'>车上货物人员意外撞击</option>");*/
	
	if($("#damage_Code").val()=="DM99"){
		dam_other.find("option").each(function(){
			$(this).removeAttr("disabled");
		});
		dam_other.parent().removeClass("hidden");
	}
	checkRegistTaskFlag();//校验报案状态 暂存 已提交 已注销
});

function save() {
	var selectedIds = getSelectedIds();
	if(selectedIds=="") {
		layer.closeAll('loading');
		layer.alert("请选择保单信息");
		return false;
	}
	var idArray = selectedIds.split(",");
	if (idArray.length > 1) {
		$("#reportType").val(3);
	} else if (idArray[0] == "BI") {
		$("#reportType").val(1);
	} else {
		$("#reportType").val(2);
	}
	if (!toValidForm()) {
		return false;
	}
	
	//最后校验
	var duoBao = 0;
	var deaAndInj = "0";
	$("#thirdCars").find("[name$='licenseNo']").each(function(){
		duoBao+=1;
	});
	//var isPersonLossY = $("input[id='isPersonLossY']:checked").val();
	var isPersonLossY = $("input[id='isPersonLossY']:checked").val();
	var injCountThird = $("input[id='injCountThird']").val();
	var deaCountThird = $("input[id='deaCountThird']").val();
	
	if(isPersonLossY=="1" && (injCountThird != "0" || deaCountThird != "0")){
		deaAndInj = "1";
		
	}
	
	
	//var isPropLossY = $("input[id='isPropLossY']:checked").val();
	//需求==========
//	var isPropLossY = 0;
//	$("input[name$='damagelevel']").each(function(){
//		
//		if($(this).prop("checked")==true){
//			if($(this).val()==1){
//				isPropLossY = 1;
//			}
//		}
//	});

	var isPropLossY = "0";
	var indexNum= 0;
	var isPropLossYFalgs = $("input[id='isPropLossY']:checked").val();
	if(isPropLossYFalgs=="1"){
		$("#propTbody input[name$='lossparty']").each(function(){
			if($(this).val()!="1" && $(this).val()!=""){
				//加是否物损
				var damagelevel = $("#propTbody input[name='prpLRegistPropLosses["+indexNum+"].damagelevel']:checked").val();
				if(damagelevel == "1"){
					isPropLossY = "1";//三者
				}
			}
			indexNum = indexNum + 1;
		});
	}

	//无保单报案，有标的有三者车、财产、人伤时，只关联商业险，交强险在我司承保时，未提示关联交强险START
	var CIPolicyNo = $("input[id='CIPolicyNo']:checked").val(); 
	var BIPolicyNo = $("input[id='BIPolicyNo']:checked").val(); 
	var damageTime = $("#damageTime").val();
	
	if(BIPolicyNo!=null && typeof(CIPolicyNo) == "undefined"){
		var  t = BIPolicyNo.substring(0,1);
		if(t!="T"){
			//查询交强险在我司承保
			$.ajax({
				url : "/claimcar/policyQuery/relatedSearch.do?BIPolicyNo="+BIPolicyNo+"&damageTime="+damageTime, // 后台处理程序
				type : 'post', // 数据发送方式
				success : function(jsonData) {// 回调方法，可单独定义
					if(jsonData.statusText=="0"){
						if (submitFlag) {
							if(parseInt(duoBao) > 0 || deaAndInj=="1" || isPropLossY=="1"){
								layer.closeAll('loading');
								layer.alert("请关联交强险");
								return false;
							}else{
								layer.confirm('是否报案提交', {
								    btn: ['确定','取消'] //按钮
								}, function(index){
									$("#editform").submit();
									layer.close(index);
								}, function(){
									submitFlag = false;
								});
							}
						}else{
							layer.confirm('是否报案暂存', {
							    btn: ['确定','取消'] //按钮
							}, function(index){
								$("#editform").submit();
								layer.close(index);
							}, function(){
								
							});
						}
					}else{
						if (submitFlag) {
							layer.confirm('是否报案提交', {
							    btn: ['确定','取消'] //按钮
							}, function(index){
								$("#editform").submit();
								layer.close(index);
							}, function(){
								submitFlag = false;
							});
						} else {
							layer.confirm('是否报案暂存', {
							    btn: ['确定','取消'] //按钮
							}, function(index){
								$("#editform").submit();
								layer.close(index);
							}, function(){
								
							});
						}
					}
				}
			});
		}else{
			if (submitFlag) {
				layer.confirm('是否报案提交', {
				    btn: ['确定','取消'] //按钮
				}, function(index){
					$("#editform").submit();
					layer.close(index);
				}, function(){
					submitFlag = false;
				});
			} else {
				layer.confirm('是否报案暂存', {
				    btn: ['确定','取消'] //按钮
				}, function(index){
					$("#editform").submit();
					layer.close(index);
				}, function(){
					
				});
			}
		}
	}else{
		if (submitFlag) {
			layer.confirm('是否报案提交', {
			    btn: ['确定','取消'] //按钮
			}, function(index){
				$("#editform").submit();
				layer.close(index);
			}, function(){
				submitFlag = false;
			});
		} else {
			layer.confirm('是否报案暂存', {
			    btn: ['确定','取消'] //按钮
			}, function(index){
				$("#editform").submit();
				layer.close(index);
			}, function(){
				
			});
		}
	}
	
	//END
	/*if (submitFlag) {
		layer.confirm('是否报案提交', {
		    btn: ['确定','取消'] //按钮
		}, function(index){
			$("#editform").submit();
			layer.close(index);
		}, function(){
			submitFlag = false;
		});
	} else {
		layer.confirm('是否报案暂存', {
		    btn: ['确定','取消'] //按钮
		}, function(index){
			$("#editform").submit();
			layer.close(index);
		}, function(){
			
		});
	}*/
	
}
function toValidForm() {
	layer.closeAll('loading');
	//校验车牌号重复
	for (var i = 0; i < $("#carSize").val(); i ++) {
		if ($("input[name='prpLRegistCarLosses[" + i + "].licenseNo']").length>0) {
			var licenseNo1 = $("input[name='prpLRegistCarLosses[" + i + "].licenseNo']").val();
			for (var j = i + 1; j <= $("#carSize").val(); j ++) {
				if ($("input[name='prpLRegistCarLosses[" + j + "].licenseNo']").length>0) {
					var licenseNo2 = $("input[name='prpLRegistCarLosses[" + j + "].licenseNo']").val();
					if (licenseNo1 == licenseNo2) {
						if (i == 0) {
							layer.msg("三者车车牌号不能与标的车重复");
							return false;
						} else {
							layer.msg("三者车车牌号不能重复");
							return false;
						}
					}
				}
			}
		}
	}
	
	//按损失方归类，校验财产名称是否重复
	for (var i = 0; i < $("#propSize").val(); i ++) {
		var lossitemname1 = $("input[name='prpLRegistPropLosses[" + i + "].lossitemname']");
		if (lossitemname1.val() != undefined) {
			var lossitemname1 = $("input[name='prpLRegistPropLosses[" + i + "].lossitemname']");
			for (var j = i + 1; j <= $("#propSize").val(); j ++) {
				var lossitemname2 = $("input[name='prpLRegistPropLosses[" + j + "].lossitemname']");
				if (lossitemname2 != undefined) {
					if (lossitemname1.val() == lossitemname2.val() && lossitemname1.attr("licenseNo") == lossitemname2.attr("licenseNo")) {
						layer.msg("同一损失方的损失名称不能重复！");
						return false;
					}
				}
			}
		}
	}
	
	for (var i = 1; i < 4; i ++) {
		$("#damageAreaCode_lv"+i).attr("datatype","*");
		$("#checkAddressCode_lv"+i).attr("datatype","*");
		$("#damageAreaCode_lv"+i).attr("nullmsg","请选择");
		$("#checkAddressCode_lv"+i).attr("nullmsg","请选择");
	}
	/*if ($("#damageAreaCode").length>0) {errormsg="身份证格式不正确！"
		alert("请选择出险所在地");checkAddressCode_lv
		return false;damageAreaCode_lv
	}*/
	
	//报案环节 互碰自赔管控
	var isSubRogation = $("input[name='prpLRegistExtVo.isSubRogation']:checked").val(); 
	var isClaimSelf = $("input[name='prpLRegistExtVo.isClaimSelf']:checked").val();
	var obliGation = $("#obliGation").val();
	if(isClaimSelf == '1'){
		if(obliGation == '0'){
			layer.msg("互碰自赔案件事故责任比例不能为全责！");
			return false;
		}
	}
	if(isSubRogation == '1'){
		if($("#reportType").val() == "2"){
			layer.msg("本车无车损险，不能选择代位求偿！");
			return false;
		}
	}
	
	return true;
}

function getSelectedIds() {
	var selectIds = "";
	$("input[name='checkCode']:checked").each(function() {
		selectIds = selectIds + $(this).attr("policyType") + ",";
	});
	if (selectIds != "") {
		selectIds = selectIds.substr(0, selectIds.length - 1);
	}
	return selectIds;
}


//新增三者车损失项
function addThirdCar() {
	var $div = $("#thirdCars");
	var carSize = $("#carSize").val();// 损失项条数
	var params = {
		"carSize" : carSize,
	};
	var url = "/claimcar/regist/addThirdCar.ajax";
	$.ajax({ 
        type : "post", 
        url : url, 
        data : params, 
        async : false, 
        success : function(result){ 
        	$div.append(result);
    		$("#carSize").val(parseInt(carSize) + 1);
    	//	accidentTypeOtherCarAdd($("#lossesA").val());
    		initLossPart();
        } 
     });
	
	/*$.post(url, params, function(result) {
		$div.append(result);
		$("#carSize").val(parseInt(carSize) + 1);
	});*/
	writeAccidentTypes();
}
//删除三者车损失项
function delThirdCar(element) {
	var delIndex = $(element).parent().parent().index();
	//获取删除的车牌号
	var index = $(element).attr("name").split("_")[1];
	var linseEle = "prpLRegistCarLosses["+index+"].licenseNo";
	
	var $parentTr = $(element).parents('.table_cont ');
	var carSize = $("#carSize").val();// 损失项条数
	$("#carSize").val(carSize - 1);// 删除一条
//	accidentTypeOtherCarDel($("#lossesA").val());
	//alert(carSize);
	delTr(carSize, delIndex, "thirdCar_", "prpLRegistCarLosses");
	$parentTr.remove();
	//调整财产损失车牌号
	linceseNoChg($("#thirdCars input[name='"+linseEle+"'"));
	
	writeAccidentTypes();
}

//财产损失增加损失方车牌号
function linceseNoChg(item){
	var licenseNo = $(item).val();
	var thisCarNo = $("#itemLicenseNo").val();//标的车牌
	var options ="<option value=''></option><option value='0'>地面</option><option value='"+thisCarNo+"'>标的车("+thisCarNo+")</option>";
	var items =$("#thirdCars input[name$='licenseNo']");
	var carNo ="0,"+thisCarNo;
	$(items).each(function(){
		var thirdNo = $(this).val();
		if(!isBlank(thirdNo)){
			options =options+"<option value='"+thirdNo+"'>三者车("+thirdNo+")</option>";
			carNo = carNo+","+thirdNo;
		}
		
	});
	var propItems =$("#propTbody select[name$='licenseNo']");
	$(propItems).each(function(){
		var selected = $(this).val();
		$(this).find("option").remove();//先删除
		$(this).append(options);//在重新增加
		if(carNo.indexOf(selected)>=0){
			$(this).find("option[value="+selected+"]").attr("selected","selected");
		}
	});
	
}
	
//新增财产损失项
function addProp() {
	var thisCarNo = $("input[name='prpLRegistCarLosses[0].licenseNo']").val();
	var thirdCarNos ="" ;
	var items =$("#thirdCars input[name$='licenseNo']");
	$(items).each(function(){
		var thirdNo = $(this).val();
		if(!isBlank(thirdNo)){
			if(thirdCarNos == ""){
				 thirdCarNos = thirdNo;
			}
			thirdCarNos =thirdCarNos+"," + thirdNo;
		}
	});
	
	var $tbody = $("#propTbody");
	var propSize = $("#propSize").val();
	var params = {
		"propSize" : propSize,
		"licenseNo":thisCarNo,
		"thirdCarNos":thirdCarNos,
	};
	var url = "/claimcar/regist/addProp.ajax";
	$.ajax({ 
        type : "post", 
        url : url, 
        data : params, 
        async : false, 
        success : function(result){ 
        	$tbody.append(result);
    		$("#propSize").val(parseInt(propSize) + 1);
    		$("select[name='prpLRegistPropLosses["+propSize+"].licenseNo']").attr("datatype","*"); 
    		if(propSize=="0"){
    			$("#isPropLossY").prop("checked",true);
    		}
        } 
     }); 
	/*$.post(url, params, function(result) {
		$tbody.append(result);
		$("#propSize").val(parseInt(propSize) + 1);
		$("select[name='prpLRegistPropLosses["+propSize+"].licenseNo']").attr("datatype","*");
	});*/
	writeAccidentTypes();
}
//删除财产损失项
function delProp(element) {
	var index = $(element).parent().parent().index()-1;
	var $parentTr = $(element).parent().parent();
	var propSize = $("#propSize").val();
	$("#propSize").val(propSize - 1);// 删除一条
	delTr(propSize, index, "delProp_", "prpLRegistPropLosses");
	$parentTr.remove();
	//设置
	if(propSize=="1"){
		$("#isPropLossN").prop("checked",true);
	}
	
	writeAccidentTypes();
}
//qtip显示险别信息
$(function(){
	$("#policyNo_1").qtip({
		content : {
			title : "险别信息",
			text : $("#RiskKindLayer_1"),
			button : true
		},
		hide: {
			fixed: true,
			delay: 300
		},
		style: {
		      classes: 'qtip-blue'
		    }
	});
});
$(function(){
	$("#policyNo_0").qtip({
		content : {
			title : "险别信息",
			text : $("#RiskKindLayer_0"),
			button : true
		},
		hide: {
			fixed: true,
			delay: 300
		},
		style: {
		      classes: 'qtip-blue'
		    }
	});
});


//自动生成出险经过
function createDangerRemark(){
	/*
	驾驶人（驾驶人姓名）于（出险日期）（出险时间）在（出险地点）使用被保险机动车过程中，发生（出险原因）；
	造成标的：车牌+车型+行驶状态+损失部位；
	三者车:车牌+车型+行驶状态+损失部位；三者车牌+车型+行驶状态+损失部位；
	人员*伤*亡，其中本车人员几伤几亡；三者人员几伤几亡；
	财产损失：损失方+损失名称+损失程度，
	当前损失标的位于（查勘地点市+区+详细地点）；
	要求有（事故处理类型）；需要查勘类型。 
	*/
	var dangerRmk="";
	dangerRmk+=bulidDanger("驾驶人","prpLRegistVo.driverName","");
	dangerRmk+=bulidDanger("于","prpLRegistVo.damageTime","");
	dangerRmk+=bulidDanger("在","prpLRegistVo.damageAddress","使用被保险机动车过程中，");
	dangerRmk+=bulidDangerSe("发生","prpLRegistVo.damageCode","；");
	
	dangerRmk+=bulidDanger("造成标的：","prpLRegistCarLosses[0].licenseNo","");
	dangerRmk+=bulidDanger("，","prpLRegistCarLosses[0].brandName","");
	var Lloss = $("#loss").val();
	
	if(Lloss!='其他'){
		//dangerRmk+=	","+Lloss;
	}else{
		dangerRmk+=bulidDanger("，","prpLRegistCarLosses[0].lossremark","");								
	}
	dangerRmk+=bulidDangerCh();
	
	dangerRmk+=bulidDangerThird(); 
	var isPersonLossY = $("input[id='isPersonLossY']:checked").val();
	var isPropLossY = $("input[id='isPropLossY']:checked").val();
	if(isPersonLossY=="1"){
		dangerRmk+=bulidDangerRen();
	}
	if(isPropLossY=="1"){
		dangerRmk+=bulidDangerProp();
	}
	
	dangerRmk+=bulidDangerAddr();
	
	dangerRmk+=bulidDangerSe("要求有","prpLRegistExtVo.manageType","，");
	dangerRmk+=bulidDangerSe("需要","prpLRegistExtVo.checkType","。");

	$("textarea[name='prpLRegistExtVo.dangerRemark']").val(dangerRmk);
}

function bulidDanger(text1,inpName,text2){
	var dangerString="";
	var inpText = $("input[name='"+inpName+"']").val();			
	if(isNull(inpText)){
		dangerString = "";
	}else{
		dangerString = text1+inpText+text2;				
	}
	return dangerString;
}

function bulidDangerCh(){//标的车受损部位多选取值
	var dangerString="";
	$("div#LossPart_0 input[name='prpLRegistCarLosses[0].losspart']").each(function(){
		if(this.checked&&$(this).parent().text()!="全部"){
				dangerString+="，"+$(this).parent().text();
		}
		/*if($(this).hasClass("allLossCbx")){
			dangerString +="；";
		 }*/
	} );
	dangerString +="；";
	return dangerString;
}

function bulidDangerSe(text1,selectName,text2){//对下拉框取值
	var dangerString="";
	var inpText = $("select[name='"+selectName+"'] option:selected").text();
	if(isNull(inpText)){
		dangerString = "";
	}else{
		dangerString = text1+inpText+text2;
	}
	return dangerString;
}

function bulidDangerAddr(){//地址
	var dangerString="";
	var city = $("select[id='checkAddressCode_lv2'] option:selected").text();
	var area = $("select[id='checkAddressCode_lv3'] option:selected").text();
	var addr = $("input[name='prpLRegistExtVo.checkAddress']").val();
	if(!isNull(addr)){
		addr = "，"+$("input[name='prpLRegistExtVo.checkAddress']").val();
	}
	dangerString = "当前损失标的位于"+city+area+addr+"；";
	return dangerString;
}

function bulidDangerProp(){//循环取財產損失  损失方+损失名称+损失程度
	var dangerString="";
	var dangerStrings="";
	$("tbody#propTbody [name^='prpLRegistPropLosses']").each(function(i){  
		 
		 if($(this).attr("id")=="lossparty"){
			 if(!isNull($(this).val())){
			var name = $(this).attr("name");
			var lossparty = $("select[name='"+name+"'] option:selected").text();
			dangerString+=lossparty+"，";
		 }
		 }
		 if($(this).attr("id")=="lossitemname"){
			 if(!isNull($(this).val())){
			 dangerString+=$(this).val()+"，";
			 }
		 }
		 if($(this).attr("id")=="damagelevel"){
			 if(!isNull($(this).val())){
			 dangerString+=$(this).val()+"；";
			 }
		 }
		 
	});
	if(dangerString!=""){
		dangerStrings="财产损失："+dangerString;
	}
	return dangerStrings;
}

/*function trim(str){ //删除左右两端的空格
	 
    return str.replace(/(^\s*)|(\s*$)/g, "");

}*/
function bulidDangerThird(){//循环取三者车
	var dangerString="";
	var licenseNo="",brandName="",lossremark="",losspart="";
	var strL= new Array();
	var strB= new Array();
	var strLr= new Array();
	var strLp= new Array();
	var strName="";
	
	$("div#thirdCars input[name^='prpLRegistCarLosses']").each(function(i){  
		 if(!isNull($(this).val())&&$(this).attr("id")=="licenseNo"){
			 licenseNo+="三者车："+$(this).val()+"￥";
			 strName = $(this).attr("name").split("[");
			 strName  = strName[1].split("]");
			 strName = strName[0];
		 }
		// if(!isNull($(this).val())&&typeof($(this).val())!="undefined"&&$(this).attr("id")=="brandName"){
		 if(!isNull($(this).val())&&$(this).attr("id")=="brandName"){
			 brandName+="，"+$(this).val()+"￥";
		 }else if(isNull($(this).val())&&$(this).attr("id")=="brandName"){
			 brandName+=$(this).val()+"￥";
		 }
		 //if(!isNull($(this).val())&&typeof($(this).val())!="undefined"&&$(this).attr("id")=="lossremark"){
		//var Lloss = $("#Loss"+strName).val();
/*		 if(!isNull(strName)){
		 var a = "prpLRegistCarLosses["+strName+"].loss";
		var Lloss = "";
//		alert(strName);
//		if(!isNull($(this).val())&&$(this).attr("id")=="Lloss"){
//			 lossremark1="，"+$(this).val()+"￥";
//			 alert(Lloss+lossremark1+lossremark2);
//		}
//		 if(!isNull($(this).val())&&$(this).attr("id")=="lossremark"){
//			 lossremark2="，"+$(this).val()+"￥";
//		 }else if(isNull($(this).val())&&$(this).attr("id")=="lossremark"){
//			 lossremark2=$(this).val()+"￥";
//		 }
		if(Lloss!='其他'){
			if(!isNull($(this).val())&&$(this).attr("id")=="Lloss"){
				 lossremark+="，"+$(this).val()+"￥";
			}
			lossremark+="，"+Lloss+"￥";
			
		}else{
			 if(!isNull($(this).val())&&$(this).attr("id")=="lossremark"){
				 lossremark+="，"+$(this).val()+"￥";
			 }else if(isNull($(this).val())&&$(this).attr("id")=="lossremark"){
				 lossremark+=$(this).val()+"￥";
			 }
		}
		 }*/
		// if($(this).attr("type")=="checkbox"&&$(this).attr("name")=="prpLRegistCarLosses["+strName+"].losspart"){	
		 if($(this).attr("type")=="checkbox"){					 					 					 
			 //alert($(this).parent().text());
			// alert(strName);
			 var xuan = $(this).parent().text();
			 xuan = $.trim(xuan);
			// if(this.checked&&(xuan!="不详")){
			 if(this.checked){
				/* var a = $(this).parent().val();
				 $.trim(a);
				 alert("hh"+a);*/
				 losspart += "，"+xuan;
			 }
			 if((xuan=="不详")){
				 losspart +="；";
			 }
			/* if($(this).attr("class")=="allLossCbx_3"){
				 losspart +="；";
			 }*/
			 //alert(strName);
			 if($(this).attr("id")=="lossPart_"+strName){
				 //alert(strName);
				 losspart +="；";
				 //alert(losspart);
			 }
		 }
		 
	});
	//调整组合语序
	strL = licenseNo.split("￥");  
	strB = brandName.split("￥");
	strLr = lossremark.split("￥");
	strLp = losspart.split("；");
	for (var i=0;i<strL.length-1 ;i++ )   
    {   
		if(!isNull(strL[i])&&typeof(strL[i])!="undefined"){
			dangerString+=strL[i];
		}
		if(!isNull(strB[i])&&typeof(strB[i])!="undefined"){
			dangerString+=strB[i];
		}
		if(!isNull(strLr[i])&&typeof(strLr[i])!="undefined"){
			//dangerString+=strLr[i];
		}
		if(!isNull(strLp[i])&&typeof(strLp[i])!="undefined"){
			dangerString+=strLp[i]+"；";
		}
		//dangerString+=strL[i]+strB[i]+strLr[i]+strLp[i]+"；";   
    }  
	
	return dangerString;
}



function bulidDangerRen(){//人员*伤*亡，其中本车人员几伤几亡；三者人员几伤几亡；
	var dangerString="";
	var sumIn=$("#injuredSum").val();
	var sumDe=$("#deathSum").val();
	var selfIn=$("[name='prpLRegistPersonLosses[0].injuredcount']").val();
	var selfDe=$("[name='prpLRegistPersonLosses[0].deathcount']").val();
	var thirdIn=$("[name='prpLRegistPersonLosses[1].injuredcount']").val();
	var thirdDe=$("[name='prpLRegistPersonLosses[1].deathcount']").val();
	if(sumIn!=0||sumDe!=0){
		dangerString="人员"+sumIn+"伤"+sumDe+"亡"+"，其中";
		if(selfIn!=0||selfDe!=0){
			dangerString+="本车人员"+selfIn+"伤"+selfDe+"亡；";
		}
		if(thirdIn!=0||thirdDe!=0){
			dangerString+="三者人员"+thirdIn+"伤"+thirdDe+"亡；";	
		}
						
	}else{
		dangerString="";
	}
	return dangerString;			
}

 		
function isNull(val){
	if(val==null||val==""){
		return true;
	}else{
		return false;
	}
};

//控制是否有损失（车损，人伤，物损）
function lossOrNot(value, id){
	if(value==1){
		$("#"+id+"Table").removeClass('hide');
		$("#"+id+"Table").find("input").each(function(){
		$(this).prop("disabled",false);
		});
		
	}else{
		$("#"+id+"Table").addClass('hide');
		$("#"+id+"Table").find("input").each(function(){
			$(this).prop("disabled",true);
			});
	}
	$("#"+id).val(value);
	//财产损失增加损失方车牌号
	linceseNoChg("car");
	writeAccidentTypes();
}


//合计人伤数量
function countSum() {
	var injCountTag = parseInt($("#injCountTag").val());
	var deaCountTag = parseInt($("#deaCountTag").val());
	var injCountThird = parseInt($("#injCountThird").val());
	var deaCountThird = parseInt($("#deaCountThird").val());
	
	$("#injuredSum").val(injCountTag + injCountThird);
	$("#deathSum").val(deaCountTag + deaCountThird);
	
	writeAccidentTypes();
}

//校验报案状态 暂存 已提交 已注销
function checkRegistTaskFlag() {
	
	if ($("#registTaskFlag").val() == "1" || $("#registTaskFlag").val() == "7") {
		$("body input").each(function(){
			 $(this).prop("disabled","disabled");
		}); 
		$("body select").each(function(){
			 $(this).prop("disabled","disabled");
		}); 
		$("body textarea").each(function(){
			 $(this).prop("disabled","disabled");
		});
		/*$("body button").each(function(){
			 $(this).prop("disabled","disabled");
		});*/
		$("#thirdCars button").each(function(){
			 $(this).prop("disabled","disabled");
		});
		$("#cancelOut input").each(function() {
			if ($("#registTaskFlag").val() == "1") {
				$(this).prop("disabled", false);
			}
		});
		
		$("#cancelOut select").each(function() {
			if ($("#registTaskFlag").val() == "1") {
				$(this).prop("disabled", false);
			}
		});
		
		$("#baoprint").attr("disabled", false);
		
		//报案已处理界面，财产损失项可以点击删除和增加，已处理界面按钮应处于置灰
		$("#WuSunTable button").each(function(){
			$(this).prop("disabled","disabled");
			$(this).addClass("btn-disabled");
		});
	}
	
	//临时报案不调用风险提示
	if ($("#tempRegistFlag").val() != 1) {
		createRegistRisk();//调用显示风险提示信息
	}
	
	//为财产损失房下拉选增加非空校验
	var propSize = $("#propSize").val();
	for (var i = 0; i < propSize; i++) {
		$("select[name='prpLRegistPropLosses["+i+"].licenseNo']").attr("datatype", "*");
	}
}

//报案人，报案人电话，报案人身份证写入联动
function writeNull(index) {
	if (index == 1) {
		var reportorName = $("#reportorName").val();
		if ($("#driverName").val() == null || $("#driverName").val() == "") {
			$("#driverName").val(reportorName);
		}
		if ($("#linkerName").val() == null || $("#linkerName").val() == "") {
			$("#linkerName").val(reportorName);
		}
	}
	if (index == 2) {
		var reportorPhone = $("#reportorPhone").val();
		var reportorRelation = $("#reportorRelation").val();
		if(!(/^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|18[0-9]{9}|17[0-9]{9}$/.test(reportorPhone))){ 
	        return false; 
	    } 
		if ($("#driverPhone").val() == null || $("#driverPhone").val() == "") {
			$("#driverPhone").val(reportorPhone);
		}
		if ($("#linkerMobile").val() == null || $("#linkerMobile").val() == "") {
			$("#linkerMobile").val(reportorPhone);
		}
		if ($("#insuredPhone").val() == null || $("#insuredPhone").val() == "" && reportorRelation=="01") {
			$("#insuredPhone").val(reportorPhone);
		}
		if(reportorRelation=="01"){
		$("#insuredFlag").removeClass("hidden");
		}else{
			$("#insuredFlag").addClass("hidden");
			$("#insuredPhone").val("");
		}
	}
}

function allCheck(obj){//单击选中全部则选中所有受损部位
	$(obj).parent().prevAll().find("input").each(function(){
		console.log($(obj).html());
		if($(this).attr("type")=="checkbox"){
			$(this).prop("checked",obj.checked);
		}
	});
}

//联动写入损失方隐藏域
function writeLossParty(element, index) {
	var licenseNo = $(element).val();
	if (licenseNo == "0") {
		$("input[name='prpLRegistPropLosses["+index+"].lossparty']").val("0");
	} else if (licenseNo == $("#itemLicenseNo").val()) {
		$("input[name='prpLRegistPropLosses["+index+"].lossparty']").val("1");
	} else if (licenseNo.length != 0) {
		$("input[name='prpLRegistPropLosses["+index+"].lossparty']").val("3");
	}
	
	writeAccidentTypes();
}

//联动将 出险地省市区 写入 约定查勘地省市区
/*areaLevel = 1;
areaFlag = true;*/
function writeCheckArea(lowerCode,areaId,targetElmId,showLevel,clazz,handlerStatus) {
	/*alert(areaLevel);
	if ($("#checkAddressCode").val().length == 0 || areaLevel<4) {
		//checkAddressCode，damageAreaCode
		alert("1:"+$("#damageAreaCode_lv"+areaLevel).val());
		$("#checkAddressCode_lv"+areaLevel).val($("#damageAreaCode_lv"+areaLevel).val());
		changeArea($("#checkAddressCode_lv"+areaLevel),"checkAddressCode",areaLevel);
		areaLevel = areaLevel+1;
		alert(areaLevel);
		 var select = document.getElementById("checkAddressCode_lv1");
		//select.fireEvent("onchange");
		if(document.all){  
			select.fireEvent("onchange");  
		} else {  
        	var evt = document.createEvent('HTMLEvents');  
        	evt.initEvent('change',true,true);  
        	select.dispatchEvent(evt);  
		} 
	}*/
	getAllAreaInfo(lowerCode,areaId,targetElmId,showLevel,clazz,handlerStatus);
}

function gotoPage(url) {
	location.href = url;
}


function openDamageMap(){
	var damageAreaCode = $("#damageAreaCode").val();
	var damageAddress = $("#damageAddress").val();
	var damageAreaCode_lv3 = $("#damageAreaCode_lv3").val();
	if(damageAreaCode == ''||damageAreaCode_lv3==''){
		layer.alert("请选择出险地点省市区");
		return;
	}
	var provinceCode = $("#damageAreaCode_lv1").val();
	var cityCode = $("#damageAreaCode_lv2").val();
	openMap('damage',damageAreaCode,damageAddress,'Regis',provinceCode,cityCode);
}

function openCheckMap(){
	var checkAddressCode = $("#checkAddressCode").val();
	var checkAddress = $("#checkAddress").val();
	if(checkAddressCode == ''){
		layer.alert("请选择约定查勘地点省市区");
		return;
	}
	var provinceCode = $("#checkAddressCode_lv1").val();
	var cityCode = $("#checkAddressCode_lv2").val();
	openMap('check',checkAddressCode,checkAddress,"Regis",provinceCode,cityCode);
}

//处理电子地图返回信息
function getMapInfo(item,regionCode,damageAddress,lngXlatY,selfDefinareaCode) {
	if(item == "damage"){
		$("#damageAreaCode").val(regionCode);
		$("#damageAddress").val(damageAddress);
		$("#damageMapCode").val(lngXlatY);
		$("#checkAddress").val(damageAddress);
		$("#checkAddressMapCode").val(lngXlatY);
		var isOrMarkCode = $("#isOrMarkCode").val();//为1即用约定查勘的自定义，0说明没有进入约定查勘的地图即用出险地自定义
		if(isOrMarkCode!=1){
			$("#selfDefinareaCode").val(selfDefinareaCode);
		}
		
		getAllAreaInfo(regionCode,"damageAddressDiv","damageAreaCode",3,null,null,null);
		getAllAreaInfo(regionCode,"checkAddressDiv","checkAddressCode",3,null,null,null);
	}else if(item == "check"){
		//$("#checkAddressCode").val(regionCode);
		$("#checkAddress").val(damageAddress);
		$("#checkAddressMapCode").val(lngXlatY);
		//var isOnSitReport = $("input[name$='isOnSitReport']:checked").val();
		$("#isOrMarkCode").val("1");
		$("#selfDefinareaCode").val(selfDefinareaCode);
		
		getAllAreaInfo(regionCode,"checkAddressDiv","checkAddressCode",3,null,null,null);
	}
}

/*//出险地点输入开关控制
function changeDAStatus(e){
	var obj = $("#damageAddress");
	var status = obj.attr("readonly");
	if(status == 'readonly'){
		$(e).val("地点输入开关(开)");
		obj.removeAttr("readonly"); 
	}else{
		$(e).val("地点输入开关(关)");
		obj.attr("readonly","readonly");
	}
	
}
*/
/*$("#zhapian").removeClass("hidden");  
}else if(dealReasoon==1||dealReasoon==2){
	$("#zhapian").addClass("hidden"); */
function damageSelect(obj){
	var damCo = $("option:selected", $(obj));
	var dam_other=$("#dam_other");
	var damage_Code = $("#damage_Code");
	if(damCo.val()!="DM99"){
		//dam_other.val("");
		dam_other.find("option").each(function(){
			$(this).attr("disabled",true);
		});
	//	dam_other.hide();
		dam_other.parent().addClass("hidden");
		//恢复原来样子，倾覆时不恢复
	/*	dam_other.find("option").each(function(){
			if($(this).val()==""){
				$("<option value='DM51'>倾覆</option>").insertAfter($(this));
			}
			if($(this).val()=="DM11"){
				$(this).remove();
			}
			
		});
		
		damage_Code.find("option").each(function(){
			if($(this).val()=="DM10"){
				  $("<option value='DM11'>车上货物人员意外撞击</option>").insertAfter($(this));
			}
		});
		if(damCo.val()!="DM51"){
			
			damage_Code.find("option").each(function(){
				if($(this).val()=="DM51"){
					$(this).remove();
				}
			});
		}*/
		
	}else{
		//报案登记新增页面，出险原因下拉框选择“其他”，显示附加选项框，将“倾覆”移动至前一下拉框“全车盗抢”下，将前一下拉框“车上货物人员意外撞击”移动至附加选项框。
		dam_other.find("option").each(function(){
			$(this).removeAttr("disabled");
		/*	if($(this).val()=="DM51"){
				//将“倾覆”移动至前一下拉框“全车盗抢”下
				$(this).remove();
			}*/
		});
		
		
	/*	
		damage_Code.find("option").each(function(){
			if($(this).val()=="DM51"){
				$(this).remove();
			}
		});*/
	/*	damage_Code.find("option").each(function(){
			if($(this).val()=="DM11"){
				//删除车上货物人员意外撞击
				$(this).remove();
			}
			if($(this).val()=="DM04"){
				//将“倾覆”移动至前一下拉框“全车盗抢”下
				  $("<option value='DM51'>倾覆</option>").insertAfter($(this));
			}
		});*/
		dam_other.parent().removeClass("hidden");
		//dam_other.append("<option value='DM11'>车上货物人员意外撞击</option>");
		//dam_other.show();
	}
	var BIPolicyNo = $("#BIPolicyNo").val();
	var biriskcode = '';
	$("input[name='checkCode']:checked").each(function() {
		if($(this).attr("id")=="BIPolicyNo"){
			biriskcode = $(this).attr("BIRiskCode");
		}
	});
	if(damCo.val()=="DM04"){
		$.ajax({
			url : "/claimcar/regist/checkCode.do?BIPolicyNo="+BIPolicyNo+"&BIRiskCode="+biriskcode, // 后台处理程序
			type : 'post', // 数据发送方式
			success : function(jsonData) {// 回调方法，可单独定义
				if(jsonData.status==200){
					if(jsonData.data==1){
						layer.alert("未投保盗抢险不能生成报案!");
						$("#tiJiao").val("1");
					}else{
						$("#tiJiao").val("0");
					}
					/*else if(jsonData.data==0){
						layer.alert("报案");
					}*/
				}
				
			}
		});
	}
	if(damCo.val()=="DM02"){
		$.ajax({
			url : "/claimcar/regist/checkCodes.do?BIPolicyNo="+BIPolicyNo+"&kindCode=F"+"&BIRiskCode="+biriskcode, // 后台处理程序
			type : 'post', // 数据发送方式
			success : function(jsonData) {// 回调方法，可单独定义
				if(jsonData.status==200){
					if(jsonData.data==1){
						layer.alert("未投保玻璃单独破碎不能生成报案!");
						$("#tiJiao").val("1");
					}else{
						$("#tiJiao").val("0");
					}/*else if(jsonData.data==0){
						layer.alert("报案");
					}*/
				}
				
			}
		});
	}
	if(damCo.val()=="DM03"){
		$.ajax({
			url : "/claimcar/regist/checkCodes.do?BIPolicyNo="+BIPolicyNo+"&kindCode=L"+"&BIRiskCode="+biriskcode, // 后台处理程序
			type : 'post', // 数据发送方式
			success : function(jsonData) {// 回调方法，可单独定义
				if(jsonData.status==200){
					if(jsonData.data==1){
						layer.alert("未投保车身划痕不能生成报案!");
						$("#tiJiao").val("1");
					}else{
						$("#tiJiao").val("0");
					}/*else if(jsonData.data==0){
						layer.alert("报案");
					}*/
				}
				
			}
		});
	}
	if(damCo.val()!="DM02"||damCo.val()!="DM03"||damCo.val()!="DM04"){
		$("#tiJiao").val("0");
	}
}

//巨灾
function disaster_ones(){
	var disaster=$("#disaster_one").val();
	var name =$("#disaster_one").find("option[value='"+disaster+"']").text();
	$("#disaster_oneName").val(name.split("-")[1]);
	var disasterOneCode = name.split("-")[0];
	clearDisasterTwo();
	initDisasterTwoInfo(disasterOneCode);
}
function disaster_twos(){
	var disaster=$("#disaster_two").find("option:selected").text();
	$("#disaster_twoName").val(disaster.split("-")[1]);
}
function disTwChange(){
	var dis_two=$("#disaster_two");
	dis_two.find("option[value='002']").attr("disabled","disabled");
	dis_two.find("option[value='004']").attr("disabled","disabled");
	dis_two.find("option[value='006']").attr("disabled","disabled");
	dis_two.find("option[value='007']").attr("disabled","disabled");
}

/**
 * 暂存之后显示已保存的值
 */
function displayDisasterTwo() {
	var code = $("input[name='lastSelectCode']").val();
	var name = $("#disaster_twoName").val();
	if (name != null && name.length > 0) {
		$("#disaster_two").append("<option value='"+code+"' selected>"+code+"-"+name+"</option>");
	}
}

/**
 * 当巨灾一级代码发生变化时，将已选择的巨灾二级代码信息清空
 */
function clearDisasterTwo() {
	$("#disaster_two").val("");
	$("#disaster_two").text("");
	$("#disaster_twoName").val("");
}

function initDisasterTwoInfo(disasterOneCode) {
	$("#disaster_two").empty();
	$("#disaster_two").append("<option value=''></option>");
	$.ajax({
		url : "/claimcar/regist/getDisasterTwoInfo.ajax?disasterOneCode=" + disasterOneCode, // 从再保获取巨灾二级代码
		type : 'post', // 数据发送方式
		success : function(ajaxResult) {// 回调方法，可单独定义
			if (ajaxResult.status == 200 && ajaxResult.data != null) {
				for (var i = 0; i < ajaxResult.data.length; i++) {
					var codeinfo = ajaxResult.data[i];
					var codeArr = codeinfo.split("-");
					$("#disaster_two").append("<option value='"+codeArr[0]+"'>"+codeinfo+"</option>");
				}
			}
		}
	});
}


function lossesChg(this1){
	var id = $(this1).val();
	if(id != ""){
		var losses = $("#losses").val();
		$("#losses").val(losses+1);
	}
}

function writeAccidentTypes(){
	var statistic = 0;
	if($("#isCarLossY").prop("checked")==true){
		var carSize = parseInt($("#carSize").val()); // 标的车和三者车总数
		statistic = statistic + carSize;
	}
	if($("#isPersonLossY").prop("checked")==true){
		var injCountThird = parseInt($("#injCountThird").val());
		var deaCountThird = parseInt($("#deaCountThird").val());
		if((injCountThird+deaCountThird)>0){
			statistic = statistic + 1;               //三者伤亡只算一方
		}
	}
	if($("#isPropLossY").prop("checked")==true){
		var propSize = parseInt($("#propSize").val());
		for(var i=0;i<propSize;i++){
			var licenseNo = $("select[name='prpLRegistPropLosses["+i+"].licenseNo']").val();
			var damagelevel = $("input[name='prpLRegistPropLosses["+i+"].damagelevel']:checked").val();
			if(licenseNo=='0' && damagelevel=='1'){ //物体方是地面且有损才算一方
				statistic = statistic + 1;
			}
		}
	}
	//判断单方，双方和多方
	var paicReportNo = $("#paicReportNo").val();
	if (paicReportNo == null || paicReportNo == '') {//平安联盟单不需要重新设置，直接显示数据库值
		if (statistic == 1) {
			$("#AccidentTypes").val("01");
		} else if (statistic == 2) {
			$("#AccidentTypes").val("02");
		} else if (statistic > 2) {
			$("#AccidentTypes").val("03");
		} else {
			$("#AccidentTypes").val("99");
		}
	}
}
$("#damageAddress").blur(function(){
	//出险地址在出险经过，调度页面，包括发送查勘员的短信的都只显示洛龙大道，没有省市区
	var isOnSitReport = $("input[name$='isOnSitReport']:checked").val();
	var switchMapValue = $("#switchMap").val();
	if(isOnSitReport==0 || switchMapValue==1){
		var damageAddress=$("#damageAddress").val();
		var damageAreaCode_lv1=$("#damageAreaCode_lv1 option:selected").text();
		var damageAreaCode_lv2=$("#damageAreaCode_lv2 option:selected").text();
		var damageAreaCode_lv3=$("#damageAreaCode_lv3 option:selected").text();
		if(damageAreaCode_lv3 != "" && damageAddressFlags == 0){
			damageAddressFlags=1;
			$("#damageAddress").val("");
			damageAddress = damageAreaCode_lv1+damageAreaCode_lv2+damageAreaCode_lv3+damageAddress;
			$("#damageAddress").val(damageAddress);
		}
		
	}
	});
$(function (){
	getDamageAddress();
	
	
});
function getDamageAddress(){
	$("[id^='damageAreaCode_lv']").change(function() {
		// 出险地址在出险经过，调度页面，包括发送查勘员的短信的都只显示洛龙大道，没有省市区
		var isOnSitReport = $("input[name$='isOnSitReport']:checked").val();
		var switchMapValue = $("#switchMap").val();
		if (isOnSitReport == 0 || switchMapValue == 1) {
			damageAddressFlags = 0;
			$("#damageAddress").val("");
		}
	});
}

Date.prototype.Format = function (fmt) { // author: meizz
	var o = {
	"M+": this.getMonth() + 1, // 月份
	"d+": this.getDate(), // 日
	"h+": this.getHours(), // 小时
	"m+": this.getMinutes(), // 分
	"s+": this.getSeconds(), // 秒
	"q+": Math.floor((this.getMonth() + 3) / 3), // 季度
	"S": this.getMilliseconds() // 毫秒
	};
	if (/(y+)/.test(fmt))
	fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
	if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
	};

function caseCancle(){
	var reportTime=$("input[name='prpLRegistVo.reportTime']").val();
	var completeClaimFlag=$("#completeClaimFlag").val();//是否已立案标志
	reportTime=reportTime.replace(new RegExp(/-/gm) ,"/");
	var reportT=new Date(reportTime);
	reportT.setHours(reportT.getHours()+24); //案件报案时间
	var nowTime=new Date();//当前时间
	if((reportT.getTime()-nowTime.getTime())>=0 && completeClaimFlag=='0'){
		layer.alert("案件报案后的24小时内不允许进行报案注销!");
		return false;
		
	}
	var registNo=$("#registNo").val();
	var url="/claimcar/regist/registCancle.do?registNo="+registNo;
	layer.open({
		type : 2,
		title : '案件注销',
		scrollbar: false,
		area: ['50%', '60%'],
		content :url,
		end : function() {
			
		}
	});
	
};
