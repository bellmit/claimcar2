<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>赔案流程查询</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
					    <input style="display:none" type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="0" checked="checked">
					    <input style="display:none" type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="1">
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
								<input type="text" class="input-text"  datatype="/^([A-Z]|[1-9])(\d{4,21})$/" ignore="ignore" errormsg="请输入4到21位数" name="policyNo"  value="" />
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
							<label class="form-label col-1 text-c">
						          被保险人</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="insuredName"/>
							</div>
							<label class="form-label col-1 text-c">标的车架号</label>
							<div class="formControls col-3">
								 <input type="text" class="input-text"  name="frameNo"/>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">三者车车牌号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="carStrLicenseNo" ignore="ignore" errormsg="请输入正确车牌号码"  name="thirdLicenseNo" value="" />
							</div>
							<label class="form-label col-1 text-c">
						          三者车车架号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="thirdFrameNo"/>
							</div>
							<label class="form-label col-1 text-c">三者车发动机号</label>
							<div class="formControls col-3">
								 <input type="text" class="input-text"  name="thirdEngineNo"/>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">标的车发动机号</label>
							<div class="formControls col-3">
								 <input type="text" class="input-text"  name="engineNo"/>
							</div>
							
							<label class="form-label col-1 text-c">出险日期</label>
							<div class="formControls col-3">
							<input type="text" class="Wdate" id="tiDateMin"
									name="damageTimeStart" value="<fmt:formatDate value='${damageTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})"  datatype="*"/>
								- <input type="text" class="Wdate" id="tiDateMax"
									name="damageTimeEnd" value="<fmt:formatDate value='${damageTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*" /><font color="red">*</font>
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
						<div class="row mb-6 cl">
							<label class="form-label col-1 text-c">流转状态</label>
							<div class="formControls col-3">
								<span class="select-box"><app:codeSelect
										codeType="FlowStatus" type="select"
										name="flowStatus" lableType="name"/></span>
							</div>
							<label class="form-label col-1"><input type="checkbox" name="intermFlag" value="1"/></label>
						    <div class="formControls col-1">公估处理案件</div>
						    <label class="form-label col-1"><input type="checkbox" name="subCheckFlag" value="1"/></label>
						    <div class="formControls col-1">司内代查勘</div>
							<label class="form-label col-1"><input type="checkbox" name="tpFlag" value="1"/></label>
						    <div class="formControls col-3">异地案件</div>
						</div>
						<div class="row mb-6 cl">
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> 
									<app:codeSelect codeType="ComCode" type="select" id="comCode" 
											name="comCode" lableType="code-name"   dataSource="${carNoMap }"/>
								</span>
							</div>
							
							<label class="form-label col-1"><input type="checkbox" name="isPerson" value="1"/></label>
						    <div class="formControls col-1">涉及人伤案件</div>
						    <label class="form-label col-1"><input type="checkbox" name="assessSubCheckFlag" value="1"/></label>
						    <div class="formControls col-1">公估代查勘</div>
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
			    <div class="tabBar cl"> 
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handout">&#xe619;</i>新理赔流程</span>
					<span onclick="changeHandleTab(1)"><i class="Hui-iconfont handing">&#xe619;</i>旧理赔流程</span> 
				</div>
				<div class="tabCon clearfix table_cont table_list">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>报案号</th>
								<th>保单号</th>
								<th>标的车牌号码</th>
								<th>被保险人</th>
								<th>出险时间</th>
								<th>报案时间</th>
								<th>承保机构</th>
								<th>出险地点</th>
								<th>查看</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
				<div class="tabCon clearfix table_cont table_list">
					<table id="DataTable_1" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>报案号</th>
								<th>保单号</th>
								<th>标的车牌号码</th>
								<th>被保险人</th>
								<th>处理人</th>
								<th>报案时间</th>
								<th>险种</th>
								<th>出险地点</th>
								<th>案件任务列表</th>
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
	<script type="text/javascript">
		var columns = [ {
			"data" : "registNo"
		}, {
			"data" : "policyNoHtml"
		}, {
			"data" : "licenseNo"
		}, {
			"data" : "insuredName"
		}, {
			"data" : "damageTime"
		}, {
			"data" : "reportTime"
		}, {
			"data" : "comCodePlyName"
		}, {
			"data" : "damageAddress"
		}, {
			"data" : "flowId"
		} ];
		
		
		var columns1 = [ {
			"data" : "registNo"
		}, {
			"data" : "policyNo"
		}, {
			"data" : "licenseNo"
		}, {
			"data" : "insuredName"
		}, {
			"data" : "handlerUser"
		}, {
			"data" : "reportTime"
		}, {
			"data" : "riskCodeName"
		}, {
			"data" : "damageAddress"
		}, {
			"data" : "flowId"
		}];

		function rowCallback(row, data, displayIndex, displayIndexFull) {
			 var goUrl = "/claimcar/flowQuery/showFlow.do?flowId="+data.flowId;
			 var registNo = data.registNo;
			 $('td:eq(8)', row).html(
					"<a target='_blank'  onClick=openTaskEditWin('流程图','"+goUrl+"')>查看</a>");
			 $('td:eq(0)', row).html(
						"<a target='_blank'  onClick=openTaskEditWin('流程图','"+goUrl+"')>"+registNo+"</a>");
		}
		
		function rowCallback1(row, data, displayIndex, displayIndexFull) {
			//报案号连接到旧系统
			var policyNo = data.policyNo;
			var policyNoLink = data.policyNoLink;
			var registNo = data.registNo;
			var riskCode = data.serialNo;
			$('td:eq(0)', row).html("<a onClick=openOldClaimTaskFlow('"+registNo+"')>"+registNo+"</a>");
			if(isBlank(policyNoLink)){
				$('td:eq(1)', row).html("<span>"+policyNo+"</span>");
			}else{
				$('td:eq(1)', row).html("<span>"+policyNo+"<br>"+policyNoLink+"</span>");
			}
			
			if(riskCode=='1101' || riskCode=='1201' || riskCode=='1202' || riskCode=='1203' || riskCode=='1204' || riskCode=='1205' || riskCode=='1206' || riskCode=='1207' || riskCode=='1208' || riskCode=='1209'){
				$('td:eq(8)', row).html("<input type='button' class='btn  btn-primary mb-10' id='"+registNo+"'" 
						 +"onClick=openOldClaimTaskFlow('"+registNo+"') value='查看案件工作流' >");
			}
			 
			 
		}
		
	    function search(){
	    	var handleStatus=$("input[name='handleStatus']:checked").val();
	    	var ajaxList = new AjaxList("#DataTable_"+handleStatus);
			ajaxList.targetUrl = 'search.do';// + $("#form").serialize();
			ajaxList.postData=$("#form").serializeJson();
			var registNo = $("*[name='registNo']").val(); 
			var policyNo = $("*[name='policyNo']").val();
			var licenseNo= $("*[name='licenseNo']").val();
			var claimNo = $("*[name='claimNo']").val();
			var insuredName= $("*[name='insuredName']").val();
			var frameNo =  $("*[name='frameNo']").val();
			if(isIgnoreTime(registNo,policyNo,licenseNo,claimNo,insuredName,frameNo,handleStatus)){
				var damageTimeStart = $("*[name='damageTimeStart']").val();
				var damageTimeEnd = $("*[name='damageTimeEnd']").val();
				var day = getDays(damageTimeEnd,damageTimeStart);//计算整数天数
				if(day>30){
					layer.alert("出险时间查询范围最大不能超过30天");
					return false;
				}
			} 
			if(handleStatus == '0') {
				ajaxList.rowCallback = rowCallback;
				ajaxList.columns = columns;
			}else{
				ajaxList.rowCallback = rowCallback1;
				ajaxList.columns = columns1;
			}
			ajaxList.query();
		}
	    function isIgnoreTime(registNo,policyNo,licenseNo,claimNo,insuredName,frameNo,handleStatus){
	    	if(!isBlank(registNo)){
	    		registNo  = registNo.replace(/\s+/g,"");
			}
			if(!isBlank(policyNo)){
				policyNo  = policyNo.replace(/\s+/g,"");
			}
			if(!isBlank(frameNo)){
	    		frameNo  = frameNo.replace(/\s+/g,"");
			}
			if(!isBlank(claimNo)){
				claimNo  = claimNo.replace(/\s+/g,"");
			}
			if(!isBlank(licenseNo)){
				licenseNo  = licenseNo.replace(/\s+/g,"");
			}
			if(!isBlank(insuredName)){
	    		insuredName  = insuredName.replace(/\s+/g,"");
			}
			if(handleStatus == '1') {
		    	if(!isBlank(registNo) &&  registNo.length >= 21){
					return false;
				} else if(!isBlank(policyNo) &&  policyNo.length >= 21){
					return false;
				} else if(!isBlank(claimNo) &&  claimNo.length >= 21){
					return false;
				} else if(!isBlank(licenseNo) &&  licenseNo.length >= 6){
					return false;
				}else if(!isBlank(insuredName) &&  insuredName.length >= 2){
					return false;
				} else if(!isBlank(frameNo) &&  frameNo.length >= 10){
					return false;
				}
			} else{
		    	if(!isBlank(registNo) &&  registNo.length >= 4){
					return false;
				} else if(!isBlank(policyNo) &&  policyNo.length >= 4){
					return false;
				} else if(!isBlank(claimNo) &&  claimNo.length >= 21){
					return false;
				} else if(!isBlank(licenseNo) &&  licenseNo.length >= 4){
					return false;
				}else if(!isBlank(insuredName) &&  insuredName.length >= 2){
					return false;
				} else if(!isBlank(frameNo) &&  frameNo.length >= 4){
					return false;
				}
			}
	   	 	return true;
		}
	    //时间减法
	    function getDays(strDateStart,strDateEnd){ 
	    	var strSeparator = "-"; //日期分隔符 
	    	var oDate1; 
	    	var oDate2; 
	    	var iDays; 
	    	oDate1= strDateStart.split(strSeparator); 
	    	oDate2= strDateEnd.split(strSeparator); 
	    	var strDateS = new Date(oDate1[0], oDate1[1]-1, oDate1[2]); 
	    	var strDateE = new Date(oDate2[0], oDate2[1]-1, oDate2[2]); 
	    	iDays = parseInt(Math.abs(strDateS - strDateE ) / 1000 / 60 / 60 /24)//把相差的毫秒数转换为天数 
	    	return iDays ; 
	    } 
	    function openOldClaimTaskFlow(registNo){
	    	var params = {"registNo":registNo};
		    $.ajax({
				url : "/claimcar/flowQuery/showOldTaskFlow.ajax", // 后台校验
				type : 'get', // 数据发送方式
				dataType : 'json', // 接受数据格式
				data : params, // 要传递的数据
				async : false,
				success : function(jsonData) {// 回调方法，可单独定义
				    var result = eval(jsonData);
					if (!isBlank(result.data)) {
						var goUrl = "/claimcar/flowQuery/showFlow.do?flowId="+result.data;
						openTaskEditWin('流程图',goUrl);
					}else{
						layer.alert("流程图查看失败，请刷新重试！");
						
					}
				}
			});
	    }
		
	</script>
</body>
</html>
