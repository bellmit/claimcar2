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
	<div class="table_wrap">
		<div class="table_title f14">案件${prpLCMains[0].registNo}已关联保单</div>
		<div class="table_cont pd-10">
			<div class="formtable f_gray4">
				<table class="table table-border table-hover">
					<thead>
			 			<tr class="text-c">
			 				<th title="勾选本报案关联的保单">报案登记</th>
			 				<th>保单号</th>
			 				<th>车牌号码</th>
			 				<th>被保险人</th>
			 				<th>车型名称</th>
			 				<th>客户等级</th>
			 				<th>承保机构</th>
			 				<th>车架号</th>
			 				<th>发动机号</th>
			 				<th>起保日期</th>
			 				<th>终保日期</th>
			 				<th>保单类型</th>
			 			</tr>
			 		</thead>
			 		<tbody id="tbody">
			 			<%@include file="ReportEdit_RelationListSub.jsp" %>
			 		</tbody>
				 </table>
				 <div class="line"></div>
				 <div class="row">
				 <c:if test="${regAddFlag ne '1' }">
				 	<span class="col-offset-5 col-2">
					   <button class="btn btn-primary btn-cancel">取消关联</button>
					</span>
				</c:if>
				 </div>
			 </div>
		</div>
	</div>
	<!--查询条件 开始-->
	<div class="table_wrap">
		<div class="table_cont pd-10">
			<form id="form" class="form-horizontal" role="form" method="post" action="#">
				<div class="formtable f_gray4">
					<div class="row mb-3 cl">
						
						<label for="policyNo" class="form_label col-1"><input type="radio" value="1" checked="checked" name="policyInfoVo.checkFlag" id="checkFlag" />保单号</label>
						<div class="form_input col-3"> 
							<input id="policyNo" type="text" class="input-text" name="policyInfoVo.policyNo"  clazz="must" onfocus="radioChecked(1)"/>
						</div>
						
						
						<label for="licenseNo" class="form_label col-1"><input type="radio" value="2" name="policyInfoVo.checkFlag" id="checkFlag" />车牌号</label>
						<div class="form_input col-3">
							<input id="licenseNo" type="text" class="input-text" name="policyInfoVo.licenseNo" onfocus="radioChecked(2)"/>
						</div>
						
					
						
						<label for="engineNo" class="form_label col-1"><input type="radio" value="4" name="policyInfoVo.checkFlag" id="checkFlag" />发动机号</label>
						<div class="form_input col-3">
							<input id="engineNo" type="text" class="input-text" name="policyInfoVo.engineNo" onfocus="radioChecked(4)"/>
						</div>
						
					</div>
					<div class="row mb-3 cl">
						
						<label for="vinNo" class="form_label col-1"><input type="radio" value="6" name="policyInfoVo.checkFlag" id="checkFlag" />VIN 码</label>
						<div class="form_input col-3">
							<input id="vinNo" type="text" class="input-text" name="policyInfoVo.vinNo" onfocus="radioChecked(6)"/>
						</div>
						
						<label for="frameNo" class="form_label col-1"><input type="radio" value="5" name="policyInfoVo.checkFlag" id="checkFlag" />车架号</label>
						<div class="form_input col-3">
							<input id="frameNo" type="text" class="input-text" name="policyInfoVo.frameNo" onfocus="radioChecked(5)"/>
						</div>
						
					<label for="insuredName" class="form_label col-1"><input type="radio" value="3" name="policyInfoVo.checkFlag" id="checkFlag" />被保险人</label>
						<div class="form_input col-3">
							<input id="insuredName" type="text" class="input-text" name="policyInfoVo.insuredName" onfocus="radioChecked(3)"/>
						</div>
						
					</div>
					<div class="row mb-3 cl">
						<div>
						<label for="dgDateMin" class="form_label col-1">出险时间</label>
						<div class="form_input col-3">
							<input name="policyInfoVo.damageTime" readonly id="damageTime" value="<fmt:formatDate value='${policyInfoVo.damageTime}'  pattern='yyyy-MM-dd HH:mm' />" type="text" class="input-text"/>
							<input id="nowTime" type="hidden" value="<fmt:formatDate value='${policyInfoVo.damageTime}'  pattern='yyyy-MM-dd HH:mm' />" />
						</div>
						</div>
						<label for="licenseColor" class="form_label col-1">号牌底色</label>
						<div class="form_input col-3">
							<span class="select-box" > 
								<app:codeSelect id="licenseColor" codeType="LicenseColor" name="policyInfoVo.licenseColor"  type="select" />
							</span>
						</div>
						<div>
						<label for="insuredIdNo" class="form_label col-1"><input type="radio" value="7" name="policyInfoVo.checkFlag" id="checkFlag" />身份证号</label>
						<div class="form_input col-3">
							<input id="insuredIdNo" type="text" class="input-text" name="policyInfoVo.insuredIdNo" placeholder="被保险人身份证" onfocus="radioChecked(7)" datatype="idcard" ignore="ignore"/>
						</div>
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
						</span>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!--案查询条件 结束-->
	
	<!-- 隐藏域开始 -->
	<input type="hidden" id="registNo" value="${prpLCMains[0].registNo}" />
	<input type="hidden" id="relationFlag" value="${relationFlag}" />
	<!-- 隐藏域结束 -->

	<!--标签页 开始-->
	<div class="tabbox">
		<div id="tab-system" class="HuiTab">
			<div class="tabCon clearfix">
					<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
					<thead>
						<tr class="text-c">
							<th style="width: 4%">选择</th>
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
				<!--table 结束-->
				<div class="row text-c">
					<br/>
				</div>
				<div class="row text-c">
					<button class="btn btn-primary btn-relation">报案保单关联</button>
					<c:if test="${!(flag eq 'check')}">
						<button class="btn btn-primary" onclick="closeThis()" type="button">关闭</button>
					</c:if>
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
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {
		var goUrl = "/claimcar/policyView/policyView.do?policyNo="+ data.policyNo;
		if (data.validFlag == '0' || data.validFlag == '2' || data.validFlag == '3'|| data.validFlag == '4'|| data.validFlag == '5') {
			$('td:eq(0)', row).html("<input disabled='disabled' name='checkCode' group='ids' risktype='"
					+ data.riskType + "' onclick='relatePly(this)' type='checkbox' id='" + data.policyNo
					+ "' value='" + data.relatedPolicyNo + "' licenseno='"+data.licenseNo+"' insuredname='"
					+data.insuredName+"' frameNo='"+data.frameNo+"' />");
		} else {
			$('td:eq(0)', row).html("<input name='checkCode' group='ids' risktype='" + data.riskType 
					+ "' onclick='relatePly(this)' type='checkbox' id='" + data.policyNo + "' value='" 
					+ data.relatedPolicyNo + "' licenseno='"+data.licenseNo+"' insuredname='"
					+data.insuredName+"' endDate='"+data.endDateHour+"' startDate='"+data.startDateHour+"' frameNo='"+data.frameNo+"' policyNo='" + data.policyNo+"' comCode='" + data.comCode+"' />");
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
			var nowTime = Date.parse(new Date());
			var damageTime = Date.parse($("#damageTime").val().replace('-','/'));
			if (damageTime > nowTime) {
				layer.msg("出险时间不能大于当前时间");
				return false;
			}
			var ajaxList = new AjaxList("#resultDataTable");
			ajaxList.targetUrl = '/claimcar/regist/search.do/';
			ajaxList.postData=$("#form").serializeJson();
			ajaxList.columns = columns;
			ajaxList.rowCallback = rowCallback;
			ajaxList.query();
		}
 		//
		$("button.btn-cancel").click(
			function() {
				//校验勾选是否符合规则
				if (!cancelCheck()) {
					return false;
				}
				
				$(this).attr("disabled", true);
				$(this).removeClass("btn-primary");
				$(this).addClass("btn-disabled");
				
				//获取选中的保单号
				var policyNo = $("input[name='hadCode']:checked").attr("policyNo");
				var registNo = $("#registNo").val();
				var relationFlag = $("#relationFlag").val();
				
				var params = {
					"policyNo" : policyNo,
					"registNo" : registNo,
					"relationFlag":relationFlag,
				};
				
				var url = "/claimcar/regist/cancelPolicy.ajax";
				layer.load(0, {shade : [0.8, '#393D49']});
				$.post(url, params, function(result) {
					if (result == 1) {
						layer.msg("保单"+policyNo+" 已成功取消关联");
						var $parentTr = $("input[name='hadCode']:checked").parent().parent();
						$parentTr.remove();
						$("button.btn-cancel").attr("disabled", false);
						$("button.btn-cancel").removeClass("btn-disabled");
						$("button.btn-cancel").addClass("btn-primary");
						layer.closeAll('loading');
						if("regist"==relationFlag){
							parent.location.href="/claimcar/regist/edit.do?registNo="+registNo;
						}else{
							parent.location.reload();
						}
						parent.layer.closeAll();
						
					} else if (result == 2){
						layer.msg("该保单已立案，不能进行取消操作。");
						$("button.btn-cancel").attr("disabled", false);
						$("button.btn-cancel").removeClass("btn-disabled");
						$("button.btn-cancel").addClass("btn-primary");
						layer.closeAll('loading');
						
					
					}
					
				});
			}
		);
		
		
		$("button.btn-relation").click(
				function() {
					//var selectedIds = getSelectedIds();
					if (!relateCheck()) {
						return false;
					}
					$(this).attr("disabled", true);
					$(this).removeClass("btn-primary");
					$(this).addClass("btn-disabled");
					var registNo = $("#registNo").val();
					var policyNo = $("input[name='checkCode']:checked").attr("id");
					var relationFlag = $("#relationFlag").val();
					var $tbody = $("#tbody");
					var res =null;
					var params = {
						"registNo" : registNo,
						"policyNo" : policyNo,
						"relationFlag":relationFlag,
					};
					var checkUrl = "/claimcar/regist/checkRelationPolicy.ajax";
					var relationUrl = "/claimcar/regist/relationPolicy.ajax";
					layer.load(0, {shade : [0.8, '#393D49']});
					$.post(checkUrl, params, function(checkResult) {
						if (checkResult == 1) {
							res = checkPolicyIsGB(policyNo);
							if(res.statusText == "Y"){
				    			layer.confirm(res.data, {
				    				btn : [ '确定', '取消' ]
				    				}, function(index) {
				    					layer.close(layer.index);
				    				 	$.post(relationUrl, params, function(relationResult) {
											layer.msg("操作成功!");
											$tbody.append(relationResult);
											$("button.btn-relation").attr("disabled", false);
											$("button.btn-relation").removeClass("btn-disabled");
											$("button.btn-relation").addClass("btn-primary");
											layer.closeAll('loading');
											if("regist"==relationFlag){
												parent.location.href="/claimcar/regist/edit.do?registNo="+registNo;
											}else{
												parent.location.reload();
											}
										 	
											parent.layer.closeAll();
											
										}); 
				    				 	layer.closeAll();
						    			layer.load(0, {shade : [0.8, '#393D49']});
				    					},function(index){
				    						layer.closeAll();
											$("button.btn-relation").attr("disabled", false);
											$("button.btn-relation").removeClass("btn-disabled");
											$("button.btn-relation").addClass("btn-primary");
				    					});
				    		}else if(res.statusText == 'N'){
				    			$.post(relationUrl, params, function(relationResult) {
									layer.msg("操作成功!");
									$tbody.append(relationResult);
									$("button.btn-relation").attr("disabled", false);
									$("button.btn-relation").removeClass("btn-disabled");
									$("button.btn-relation").addClass("btn-primary");
									layer.closeAll('loading');
									if("regist"==relationFlag){
										parent.location.href="/claimcar/regist/edit.do?registNo="+registNo;
									}else{
										parent.location.reload();
									}
								 	
									parent.layer.closeAll();
									
								});
				    			layer.closeAll();
				    			layer.load(0, {shade : [0.8, '#393D49']});
				    		}
						} else if (checkResult == 2){
							layer.msg("该保单已进行过保单取消操作，不能再进行保单关联。");
							$("button.btn-relation").attr("disabled", false);
							$("button.btn-relation").removeClass("btn-disabled");
							$("button.btn-relation").addClass("btn-primary");
							
							layer.closeAll('loading');
							
						}
						
					});
				}
			);
		
		
		});
		
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
				layer.alert("请选择需要关联的保单信息。");
				return false;
			}
			//勾选保单不能超过2张
			if ( ($("input[name='checkCode']:checked").length+$("input[name='hadCode']").length) > 2) {
				layer.alert("同一个案件不能关联2张（不含）以上的保单");
				return false;
			}
			
			if (($("input[name='checkCode']:checked").length+$("input[name='hadCode']").length) == 2) {
				/* if ( ( $.trim($("input[name='checkCode']:checked").attr("licenseNo")) !=  $.trim($("input[name='hadCode']").attr("licenseNo"))) ||
						($("input[name='checkCode']:checked").attr("frameNo") != $("input[name='hadCode']").attr("frameNo")) ) {
					layer.alert("该保单与已关联保单不是关联保单。");
					return false;
				} */
				//交强商业险保单车牌不一时，应该可以做关联报案。多保单关联时，也要做这个需求，车牌不一致时也可以关联保单
				if ( ($("input[name='checkCode']:checked").attr("frameNo") != $("input[name='hadCode']").attr("frameNo")) ) {
					layer.alert("该保单与已关联保单不是关联保单。");
					return false;
				} 
				if ($("input[name='checkCode']:checked").attr("risktype") == $("input[name='hadCode']").val()) {
					layer.alert("该保单与已关联保单不是关联保单。");
					return false;
				}
				
				//comCode
				//交强险商业险保单不是同一家分公司
				var comCodeArr = new Array();
				comCodeArr.push($("input[name='checkCode']:checked").attr("comCode"));
				comCodeArr.push($("input[name='hadCode']").attr("comCode"));
				if(comCodeArr[0] != null&&comCodeArr[1] != null){
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
				var damageTime=$("#damageTime").val();
				var startDate = new Array();
				var endDate = new Array();
				$("input[name='checkCode']:checked").each(function() {
					startDate.push($(this).attr("startDate"));
					endDate.push($(this).attr("endDate"));
				});
				//判断出险时间
				for(var i=0;i < startDate.length;i++){
					if(damageTime < startDate[i]){
						layer.alert("出险时间不在保单有效期内!");
						return false;
					}
				}
				for(var i=0;i < endDate.length;i++){
					if(damageTime > endDate[i]){
						layer.alert("出险时间不在保单有效期内!");
						return false;
					}
				}
				/*//var licenseNoArr = new Array();
				var insuredNameArr = new Array();
				var risktypeArr = new Array();
				$("input[name='checkCode']:checked").each(function(){
					licenseNoArr.push($(this).attr("licenseno"));
					insuredNameArr.push($(this).attr("insuredname"));
					risktypeArr.push($(this).attr("risktype"));
				});
				//勾选的保单标的车牌号需要一致
				if (licenseNoArr[0] != licenseNoArr[1]) {
					layer.alert("被保险人相同，保单号关联，只能选择同一辆标的车进行报案");
					return false;
				}
				//勾选的保单被保险人需要一致
				if (insuredNameArr[0] != insuredNameArr[1]) {
					layer.alert("只能选择同一个被保险人的两张保单进行报案。");
					return false;
				}
				//勾选的保单类型不能相同risktype
				if (risktypeArr[0] == risktypeArr[1]) {
					layer.alert("只能选择同一被保险人的关联商业险和交强险进行报案。");
					return false;
				} */
			}
			return true;
		}
			
		function closeThis() {
			var url = "/claimcar/regist/edit.do?registNo=";
			location.href = url + $("#registNo").val();
		}
		
		//勾选数量及关联勾选校验
		function cancelCheck() {
			//勾选保单不能超过2张
			if ($("input[name='hadCode']:checked").length == 0) {
				layer.alert("请选择需要取消关联的保单信息。");
				return false;
			}
			//不能取消所有保单
			if ($("input[name='hadCode']:checked").length == $("input[name='hadCode']").length) {
				layer.alert("不能同时取消所有关联保单。");
				return false;
			}
			//只有一张保单时，不能将该保单并取消关联
			if ($("input[name='hadCode']:checked").length == 1 &&
					$("input[name='hadCode']").length == 1) {
				layer.alert("不能同时取消所有关联保单。");
				return false;
			}
			return true;
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
