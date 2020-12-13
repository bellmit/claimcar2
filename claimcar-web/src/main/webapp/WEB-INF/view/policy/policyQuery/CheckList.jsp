<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>保单验证</title>
</head>
<body>
<div class="page_wrap">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont pd-10">
				<form id="form" class="form-horizontal" role="form" method="post" action="#">
					<div class="formtable f_gray4">
						<input type="hidden" name="policyInfoVo.CallId" value="${CallId}" />
						<div class="row mb-3 cl">
							
							<label for="policyNo" class="form_label col-1"><input type="radio" value="1" checked="checked"   name="policyInfoVo.checkFlag" id="checkFlag" />保单号</label>
							<div class="form_input col-3"> 
								<input id="policyNo" type="text" class="input-text" name="policyInfoVo.policyNo" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数"  clazz="must" onfocus="radioChecked(1)"/>
							</div>
							
							
							<label for="licenseNo" class="form_label col-1"><input type="radio" value="2" name="policyInfoVo.checkFlag" id="checkFlag" />车牌号
							
							
							</label>
							<div class="form_input col-3">
								<input id="licenseNo" type="text" class="input-text" datatype="carStrLicenseNo"  ignore="ignore" name="policyInfoVo.licenseNo" onfocus="radioChecked(2)"/>
							</div>
							
						
							
							<label for="engineNo" class="form_label col-1"><input type="radio" value="4" name="policyInfoVo.checkFlag" id="checkFlag" />发动机号</label>
							<div class="form_input col-3">
								<input id="engineNo" type="text" class="input-text" name="policyInfoVo.engineNo" onfocus="radioChecked(4)"/>
							</div>
							
						</div>
						<div class="row mb-3 cl">
							
						<!-- 	<label for="vinNo" class="form_label col-1"><input type="radio" value="6" name="policyInfoVo.checkFlag" id="checkFlag" />VIN 码</label>
							<div class="form_input col-3">
								<input id="vinNo" type="text" class="input-text" name="policyInfoVo.vinNo" onfocus="radioChecked(6)"/>
							</div> -->
							
							<label for="frameNo" class="form_label col-1"><input type="radio" value="5" name="policyInfoVo.checkFlag" id="checkFlag" />VIN码/车架</label>
							<div class="form_input col-3">
								<input id="frameNo" type="text" class="input-text" datatype="*4-17" ignore="ignore" name="policyInfoVo.frameNo" onfocus="radioChecked(5)"/>
							</div>
							
						<label for="insuredName" class="form_label col-1"><input type="radio" value="3" name="policyInfoVo.checkFlag" id="checkFlag" />被保险人</label>
							<div class="form_input col-3">
								<input id="insuredName" type="text" class="input-text" name="policyInfoVo.insuredName" onfocus="radioChecked(3)"/>
							</div>
							<label for="insuredIdNo" class="form_label col-1"><input type="radio" value="7" name="policyInfoVo.checkFlag" id="checkFlag" />身份证号</label>
							<div class="form_input col-3">
								<input id="insuredIdNo" type="text" class="input-text" name="policyInfoVo.insuredIdNo" placeholder="被保险人身份证" onfocus="radioChecked(7)" datatype="idcard" ignore="ignore"/>
							</div>
						</div>
						<div class="row mb-3 cl">
							<div>
							<label for="dgDateMin" class="form_label col-1">出险时间</label>
							<div class="form_input col-3">
								<input name="policyInfoVo.damageTime" id="damageTime"  value="<fmt:formatDate value='${policyInfoVo.damageTime}'  pattern='yyyy-MM-dd HH:mm' />" 
								datatype="*" type="text" class="Wdate wd96" 
								onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'nowTime\')||\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd HH:mm'})" 
								/><font class="must">*</font> 
								<!-- onfocus="WdatePicker({maxDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd HH:mm'})" -->
								<input id="nowTime" type="hidden" value="<fmt:formatDate value='${policyInfoVo.damageTime}'  pattern='yyyy-MM-dd HH:mm' />" />
							</div>
							</div>
							<label for="licenseColor" class="form_label col-1">号牌底色</label>
							<div class="form_input col-3">
								<span class="select-box" > 
									<app:codeSelect id="licenseColor" codeType="LicenseColor" name="policyInfoVo.licenseColor"  type="select" />
								</span>
							</div>
							
						</div>
						
						<div class="row mb-3 cl">
							<label class="form_label col-2"></label>
							<div class="form_input col-7">
							</div>
							<div class="form_input col-2 text-l">
								<label><input id="onlyValid" value="1" name="policyInfoVo.onlyValid" type="checkbox" />仅查询有效保单</label>
							</div>
						</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
							   <button class="btn btn-primary btn-outline btn-search" id="search" type="submit" disabled>查询</button>
							   <button class="btn btn-primary btn-outline btn-noPolicy" id="noPolicy" onclick="reportTmp()" type="button">无保单报案</button>
							</span>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!--案查询条件 结束-->


		<!--标签页 开始-->
			<div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabCon clearfix">
							<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
							<thead>
								<tr class="text-c">
									<th style="width: 4%" >选择</th>
									<th>客户等级</th>
									<th style="width: 17%">保单号</th>
									<th>车牌号</th>
									<th>承保机构</th>
									<th>被保险人</th>
									<th width="160">车型</th>
									<th>车架号/发动机号</th>
									<th>起保日期</th>
									<th>终保日期</th>
									<th>状态</th>
								</tr>
							</thead>
							<tbody class="text-c">
							</tbody>
						</table>
						<!--table   结束-->
						<!--无保单报案保存成功后生成的临时报案号获取 隐藏域-->
						<input type="hidden" id="registTmpNo"/>
						<div class="row text-c">
							<br/>
						</div>
						<div class="row text-c">
							<button class="btn btn-primary btn-outline btn-report" id="report" type="button">报 案 登 记</button>
							<input type="hidden" id="noCheckExistRegis" value="0"  />
						</div>
					</div>
					<div class="tabCon">tabCon22</div>
				</div>
			</div>
		<!--标签页 结束-->
</div>
	<!--
	<script src="/js/policyQuery/CheckList.js"></script>
	-->
	<script type="text/javascript">
	var columns = [
		       		{
		       			"data" : null,
		       			"orderable" : false,
		       			"targets" : 0
		       		}, {
		       			"data" : null,
		       			"orderable" : false,
		       			"targets" : 0
		       		}, {
		       			"data" : "policyNo"
		       		}, {
		       			"data" : "licenseNo"
		       		}, {
		       			"data" : "comCodeName"
		       		}, {
		       			"data" : "insuredName"
		       		}, {
		       			"data" : "brandName",
		       			render : function(data, type, row) {
		    				return shortTxt(data,22);
		    			}
		       		}, {
		       			"data" : "frameNo"
		       		}, {
		       			"data" : "startDate",
		       			render : function(data, type, row) {
							return DateUtils.cutToDate(data);
						}
		       		}, {
		       			"data" : "endDate",
		       			render : function(data, type, row) {
							return DateUtils.cutToDate(data);
						}
		       		}, {
		       			"data" : "validFlag",
		       			render : function(data, type, row) {
		       				var  name="有效";
		       				if(data=='0')name="<span style='color:red'>无效</span>";
		       				if(data=='2')name="<span style='color:red'>未起保</span>";
		       				if(data=='3')name="<span style='color:red'>已退保</span>";
		       				if(data=='4')name="<span style='color:red'>已终保</span>";
		       				if(data=='5')name="<span style='color:red'>已停驶</span>";
		    				return name;
		    			}
		       		} ];
	var sizes=0;
	function rowCallback(row, data, displayIndex, displayIndexFull) {
		//layer.closeAll('loading');
		var goUrl = "/claimcar/policyView/policyView.do?policyNo="+ data.policyNo;
		if(data.counts <= 2){
			if (data.validFlag == '0' || data.validFlag == '2' || data.validFlag == '3'|| data.validFlag == '4' || data.validFlag == '5') {
				$('td:eq(0)', row).html("<input disabled='disabled' name='checkCode' group='ids' risktype='" + data.riskType + "' onclick='relatePly(this)' type='checkbox' id='" + data.policyNo + "' value='" + data.relatedPolicyNo + "' comCode='"+data.comCode + "' licenseno='"+data.licenseNo+"' insuredname='"+data.insuredName+"' />");
			}else{
				$('td:eq(0)', row).html("<input name='checkCode' checked='checked' group='ids' risktype='" + data.riskType + "' onclick='relatePly(this)' type='checkbox' id='" + data.policyNo + "' value='" + data.relatedPolicyNo + "' comCode='"+data.comCode + "' licenseno='"+data.licenseNo+"' insuredname='"+data.insuredName+"' endDate='"+data.endDateHour+"' startDate='"+data.startDateHour+"' frameNo='"+data.frameNo+"' />");	
			}
		}else{		
			if (data.validFlag == '0' || data.validFlag == '2' || data.validFlag == '3'|| data.validFlag == '4' || data.validFlag == '5') {
				$('td:eq(0)', row).html("<input disabled='disabled' name='checkCode' group='ids' risktype='" + data.riskType + "' onclick='relatePly(this)' type='checkbox' id='" + data.policyNo + "' value='" + data.relatedPolicyNo + "' comCode='"+data.comCode + "' licenseno='"+data.licenseNo+"' insuredname='"+data.insuredName+"' />");
			}else{
				$('td:eq(0)', row).html("<input name='checkCode' group='ids' risktype='" + data.riskType + "' onclick='relatePly(this)' type='checkbox' id='" + data.policyNo + "' value='" + data.relatedPolicyNo + "' comCode='"+data.comCode + "' licenseno='"+data.licenseNo+"' insuredname='"+data.insuredName+"' endDate='"+data.endDateHour+"' startDate='"+data.startDateHour+"' frameNo='"+data.frameNo+"' />");	
			}
		}
		
		
		$('td:eq(1)', row).html("");
		$('td:eq(2)', row).html("<a href="+goUrl+"  target='_blank'>"+data.riskType+data.policyNo+"</a>");
		$('td:eq(7)', row).html(data.frameNo+"<br>"+data.engineNo);
  	}
	
	
	$(function(){
		
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
		
		bindValidForm($('#form'),searchTable);
		
		function searchTable () {
			if (checkBeforeQuery()) {
				layer.msg("需输入出险日期、号牌底色之外的其他查询条件进行查询。");
				return;
			}
			//出险时间
		/* 	now=new Date(); //读取当前日期
			var b = 2; //分钟数
			now.setMinutes(now.getMinutes() + b);
			var nowTime = Date.parse(now);
			var damageTime = Date.parse($("#damageTime").val().replace('-','/'));
			if (damageTime > nowTime) {
				layer.msg("出险时间不能大于当前时间");
				return false;
			} */
			var damageTime = Date.parse($("#damageTime").val().replace('-','/'));
			var nowTime = Date.parse($("#nowTime").val().replace('-','/'));
			if (damageTime > nowTime) {
				layer.msg("出险时间不能大于系统当前时间（"+$("#nowTime").val()+"）");
				return false;
			}
			var ajaxList = new AjaxList("#resultDataTable");
			ajaxList.targetUrl = '/claimcar/policyQuery/search.do/';
			ajaxList.postData=$("#form").serializeJson();
			ajaxList.columns = columns;
			ajaxList.rowCallback = rowCallback;
			ajaxList.query();
			//layer.load(0, {shade : [0.8, '#393D49']});
		}
 
		
		$("button.btn-report").click(
				function() {
					//var selectedIds = getSelectedIds();
					if (!relateCheck()) {
						return false;
					}
					
					//需求-增加“正在报案”提示功能，即某车辆已开始记录报案（未提交），弹出提示和链接，下一位坐席就该车辆再点击开始报案
					var noCheckExistRegisFlag = $("#noCheckExistRegis").val();
					//alert("noCheckExistRegisFlag=="+noCheckExistRegisFlag);
					if(noCheckExistRegisFlag == '0'){
						//判断该保单是否正在报案
						var existRegFlag = checkHandlerRegist();
						//alert("existRegFlag=="+existRegFlag);
						if(existRegFlag=='Y'){
							//正在报案提供案件进入链接
							showHandlerRegist();				
							return false;						
						}
					}
					
					//出险时间
					/* now=new Date(); //读取当前日期
					var b = 2; //分钟数
					now.setMinutes(now.getMinutes() + b);
					var nowTime = Date.parse(now); */
					var damageTime = Date.parse($("#damageTime").val().replace('-','/'));
					var nowTime = Date.parse($("#nowTime").val().replace('-','/'));
					if (damageTime > nowTime) {
						layer.msg("出险时间不能大于系统当前时间（"+$("#nowTime").val()+"）");
						return false;
					}
					var policyNo = "";
					var relatedPlyNo = "";
					
					var callId = $("input[name='policyInfoVo.CallId']").val();
					
					var plyNoArr = new Array();
					var relaPlyNoArr = new Array();
					var comCodeArr = new Array();
					var startDate = new Array();
					var endDate = new Array();
					var frameNo = new Array();
					$("input[name='checkCode']:checked").each(function() {
						plyNoArr.push($(this).attr("id"));
						relaPlyNoArr.push($(this).val());
						comCodeArr.push($(this).attr("comCode"));
					});
					$("input[name='checkCode']:checked").each(function() {
						startDate.push($(this).attr("startDate"));
						endDate.push($(this).attr("endDate"));
						frameNo.push($(this).attr("frameNo"));
					});
					//交强险商业险保单不是同一家分公司
					if(comCodeArr.length==2){
						if(comCodeArr[0].substring(0,4)=="0002"||comCodeArr[1].substring(0,4)=="0002"){//深圳
							if(comCodeArr[0].substring(0,4)!=comCodeArr[1].substring(0,4)){
								
								layer.alert("交强险商业险保单不是同一家分公司");
								return false;
							}
						}else{
							if(comCodeArr[0].substring(0,2)!=comCodeArr[1].substring(0,2)){
								//交强险商业险保单不是同一家分公司
								layer.alert("交强险商业险保单不是同一家分公司");
								return false;
							}
						}
					}
					if(frameNo.length==2){
						if(frameNo[0] !=frameNo[1] ){
							layer.alert("保单车架号不一致不能报案");
							return false;
						}
					}
					
					//判断出险时间
					for(var i=0;i < startDate.length;i++){
						var startDatei = Date.parse(startDate[i].replace('-','/'));
						if(damageTime < startDatei){
							layer.alert("出险时间不在保单有效期内!");
							return false;
						}
					}
					for(var i=0;i < endDate.length;i++){
						var endDatei = Date.parse(endDate[i].replace('-','/'));
						if(damageTime > endDatei){
							layer.alert("出险时间不在保单有效期内!");
							return false;
						}
					}
					
					
					var damageTime=$("#damageTime").val();
					var editUrl = "/claimcar/regist/addRegist.do?policyNo=" + plyNoArr[0] + "&damageTime=" + damageTime + "&CallId=" + callId+ "&comCode=" + comCodeArr[0];
					
					if ($("input[name='checkCode']:checked").length == 1 && relaPlyNoArr[0] != ""
							 && relaPlyNoArr[0]  != 'null' && relaPlyNoArr[0]  != null ) {
						//检查保单报案关联关系，
						var relatedUrl = "/claimcar/policyQuery/relatedList.do?policyNo=" + plyNoArr[0] + "&relatedPolicyNo=" + relaPlyNoArr[0] + "&damageTime=" + damageTime+ "&comCode=" + comCodeArr[0]+"&frameNo="+frameNo[0];
						var flags = 1;
						layer.open({
						    type: 2,
						    title: '关联报案',
						    shadeClose: true,
						    shade: 0.8,
						    area: ['95%', '50%'],
						    content: relatedUrl, //iframe的url
						    btn: ['确定', '返回'],
					    	yes: 	function(index, layero){
					    			//alert(window.frames[0].document.URL); //地址有问题
					    			//alert(iframeWin.document.URL); 
					    			 var iframeWin = window[layero.find('iframe')[0]['name']];
					    			var checkObj = iframeWin.document.getElementById("checkCode0");
		 				    			if (checkObj && checkObj.checked) {
						    				relatedPlyNo = checkObj.value;
						    				editUrl+="&relatedPolicyNo="+relatedPlyNo;
						    			}else{
						    				relatedPlyNo=null;
						    			}
		 				    			if(flags==1){
		 				    				var res=checkPolicyIsGB(relatedPlyNo);
								    		if(res.statusText == "Y"){
								    			layer.confirm(res.data, {
								    				btn : [ '确定', '取消' ]
								    				}, function(index) {
								    					layer.close(layer.index);
								    					$("#report").attr("disabled","disabled");
								    					$("#report").addClass("btn-disabled");
								    					location.href = editUrl ;
								    					layer.closeAll();
										    			layer.load(0, {shade : [0.8, '#393D49']});
								    					});
								    		}else if(res.statusText == 'N'){
								    			$("#report").attr("disabled","disabled");
								    			$("#report").addClass("btn-disabled");
								    			location.href = editUrl ;
								    			layer.closeAll();
								    			layer.load(0, {shade : [0.8, '#393D49']});
								    		}
		 				    				
		 				    			}
						    			//flags = flags + 1;
						    		},
					    	cancel: function(index){ //或者使用btn2
					        			//按钮【按钮二】的回调
					    			}
						});
						
					} else {
						var res=checkPolicyIsGB();
			    		if(res.statusText == "Y"){
			    			layer.confirm(res.data, {
			    				btn : [ '确定', '取消' ]
			    				}, function(index) {
			    					layer.close(layer.index);
			    					$("#report").attr("disabled","disabled");
			    					$("#report").addClass("btn-disabled");
			    					location.href = editUrl + "&relatedPolicyNo=" + plyNoArr[1];
			    					layer.closeAll();
					    			layer.load(0, {shade : [0.8, '#393D49']});
			    					});
			    		}else if(res.statusText == 'N'){
			    			$("#report").attr("disabled","disabled");
			    			$("#report").addClass("btn-disabled");
			    			location.href = editUrl + "&relatedPolicyNo=" + plyNoArr[1];
			    			layer.closeAll();
			    			layer.load(0, {shade : [0.8, '#393D49']});
			    		}
						
					}
					
					
				}
			);
		
		
		
		});
			/**检查该保单是否正在报案**/
			function checkHandlerRegist(){
				var returnFlag = 'N';
				var plyNoArr = new Array();
				$("input[name='checkCode']:checked").each(function() {
					plyNoArr.push($(this).attr("id"));
				});
				//alert(plyNoArr);
				var plyNo = null;
				for(var i = 0;i<plyNoArr.length;i++){
					plyNo = plyNoArr[i];
					$.ajax({
						url : "/claimcar/policyQuery/checkHandlerRegist.ajax?plyNo="+plyNo, // 后台校验
						type : 'post', // 数据发送方式
						dataType : 'json', // 接受数据格式
						async : false,
						success : function(res) {// 回调方法，可单独定义
							if(res.statusText == 'Y'&&regIndex==null){
								//alert("存在正在处理的报案");
								returnFlag = 'Y';
							}else{
								//alert("不存在正在处理的报案");
								returnFlag = 'N';
							}
						}
					});
				}	
				return returnFlag;
				
			}
			/**展示正在处理的案件列表**/
			var regIndex = null;
			function showHandlerRegist(){
				var plyNoArr = new Array();
				$("input[name='checkCode']:checked").each(function() {
					plyNoArr.push($(this).attr("id"));
				});
				regIndex = layer.open({
				      title:"报案处理",
					  type: 2,
					  async : false,
					  skin: 'layui-layer-rim', //加上边框
					  area: ['80%', '60%'], //宽高
					  content: "/claimcar/policyQuery/showHandlerRegist.do?plyNo="+plyNoArr[0], 
					  btn: ['新增报案', '返回'],
				    	yes: 	function(index, layero){
						    		//alert("仍然继续新增报案");
						    		
						    		layer.close(regIndex);
						    	    regIndex = null;
						    		$("#noCheckExistRegis").val("1");//将不检查已存在报案标识设置为1-是
						    		$('#report').trigger("click");;//再次激活报案按钮
					    		},
				    	cancel: function(index){
				    		window.location.reload();
				    		}
					});
			}
			function radioChecked(val){
				$("input[name='policyInfoVo.checkFlag']").each(
					function(){
						if( $(this).val()==val){
							$(this).prop("checked",true);
						}
					});
			}
			
			//校验查询条件
			function checkBeforeQuery(){
				var checkedIndex = $("input[name='policyInfoVo.checkFlag']").filter(":checked").val();
				var flag = false;
				if (checkedIndex == 1) {
					var policyNo = $.trim($("#policyNo").val());
					if (policyNo == "") {
						flag = true;
					}
				}
				if (checkedIndex == 2) {
					var licenseNo = $.trim($("#licenseNo").val());
					if (licenseNo == "") {
						flag = true;
					}
				}
				if (checkedIndex == 3) {
					var insuredName = $.trim($("#insuredName").val());
					if (insuredName == "") {
						flag = true;
					}
				}
				if (checkedIndex == 4) {
					var engineNo = $.trim($("#engineNo").val());
					if (engineNo == "") {
						flag = true;
					}
				}
				if (checkedIndex == 5) {
					var frameNo = $.trim($("#frameNo").val());
					if (frameNo == "") {
						flag = true;
					}
				}
				if (checkedIndex == 6) {
					var vinNo = $.trim($("#vinNo").val());
					if (vinNo == "") {
						flag = true;
					}
				}
				if (checkedIndex == 7) {
					var vinNo = $.trim($("#insuredIdNo").val());
					if (vinNo == "") {
						flag = true;
					}
				}
				return flag;
			}

			//关联保单自动勾选及关联校验
			function relatePly(element) {
				var relatePly = $(element).val();
				if ($(element).prop("checked")) {
					var a = $("#" + relatePly).attr("disabled");
					if(!a){
						$("#" + relatePly).prop("checked", true);
					}
				}
			}
			
			//勾选数量及关联勾选校验
			function relateCheck() {
				//勾选保单不能超过2张
				if ($("input[name='checkCode']:checked").length == 0) {
					layer.alert("请选择需要报案的保单");
					return false;
				}
				//勾选保单不能超过2张
				if ($("input[name='checkCode']:checked").length > 2) {
					layer.alert("最多只能选择2张保单进行报案。");
					return false;
				}
				
				if ($("input[name='checkCode']:checked").length == 2) {
					var licenseNoArr = new Array();
					var insuredNameArr = new Array();
					var risktypeArr = new Array();
					$("input[name='checkCode']:checked").each(function(){
						licenseNoArr.push($(this).attr("licenseno"));
						insuredNameArr.push($(this).attr("insuredname"));
						risktypeArr.push($(this).attr("risktype"));
					});
					//交强商业险保单车牌不一时，应该可以做关联报案。多保单关联时，也要做这个需求，车牌不一致时也可以关联保单
					//勾选的保单标的车牌号需要一致
					/* if (licenseNoArr[0] != licenseNoArr[1]) {
						layer.alert("被保险人相同，保单号关联，只能选择同一辆标的车进行报案");
						return false;
					} */
					//勾选的保单被保险人需要一致
					/* if (insuredNameArr[0] != insuredNameArr[1]) {
						layer.alert("只能选择同一个被保险人的两张保单进行报案。");
						return false;
					} */
					//勾选的保单类型不能相同risktype
					if (risktypeArr[0] == risktypeArr[1]) {
						layer.alert("只能选择同一被保险人的关联商业险和交强险进行报案。");
						return false;
					}
				}
				
				
				
				
				
				return true;
			}
			var rtIndex=null;
			function reportTmp(){
				var iName = $("input[name='policyInfoVo.insuredName']").val();
				var liNo = $("input[name='policyInfoVo.licenseNo']").val();
				var enNo = $("input[name='policyInfoVo.engineNo']").val();
				var frNo = $("input[name='policyInfoVo.frameNo']").val();
				var damageTime = $("#damageTime").val();
				var url="/claimcar/regist/ReportTmpEdit.do?iName="+encodeURI(encodeURI(iName))+"&liNo="+encodeURI(encodeURI(liNo))
						+"&enNo="+encodeURI(encodeURI(enNo))+"&frNo="+encodeURI(encodeURI(frNo))+"&damageTime="+damageTime+"";
				if(rtIndex==null){
					rtIndex=layer.open({
					    type: 2,
					    title: '无保单报案',
					    shade: false,
					    area: ['1100px', '500px'],
					    content:url,
					    end:function(){
					    	rtIndex=null;
					    	var registTmpNo = $("#registTmpNo").val();
					    	//alert(registTmpNo);
					    	//保存成功后跳转到报案登记界面
					    	if(registTmpNo!=null&&registTmpNo!=""){
					    		window.location.href="/claimcar/regist/addRegist.do?registNo="+registTmpNo
					    				+"&tempRegistFlag=1&damageTime="+$("#damageTime").val()
					    				+"&CallId="+$("input[name='policyInfoVo.CallId']").val(); 
					    	}
					    	
					    }
					});
				}
			}
		//检验是否为联共保保单
		function checkPolicyIsGB(relatedPlyNo){
			
			var plyNoArr = new Array();
			var result ;
			//拿到需要查询的保单 保单+关联保单(不一定存在)
			$("input[name='checkCode']:checked").each(function() {
				plyNoArr.push($(this).attr("id"));
			});
			if(relatedPlyNo!=null){
				plyNoArr.push(relatedPlyNo);
			}
//			$.each(plyNoArr,function(i,n){
//				alert("第"+i+"个保单号为:"+n);
//			});
			$.ajax({
				url : "/claimcar/policyQuery/queryPolicyIsGB.ajax", // 后台校验
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				data:{
					'plyNoArr':plyNoArr
				},
				async : false,
				traditional: true,
				success : function(res) {// 回调方法，可单独定义
					if(res.statusText == 'Y'){
						//alert("存在未实收保费的保单");
						returnFlag = 'Y';
						result=res;
					}else{
						//alert("不存在未实收保费的保单");
						returnFlag = 'N';
						result=res;
					}
				}
			});
			return result;
		}
	</script>
	<!--workbench_div     结束-->
</body>
</html>
