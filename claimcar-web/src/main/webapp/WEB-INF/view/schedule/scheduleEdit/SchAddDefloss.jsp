<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>新增定损任务</title>
</head>
<body>
	<form action="#" id="editform" >
	<div class="fixedmargin page_wrap">
		<!-- 查勘调度任务  新增 开始 -->
		<div class="table_wrap">
			<div class="table_title f14">定损调度任务 新增</div>
			<div class="table_cont ">
				<table class="table table-border table-bordered table-hover">
					<thead>
						<tr class="text-c">
							<th style="width: 16%">选择${checkSubmited}</th>
							<th style="width: 16%">定损类型</th>
							<th style="width: 16%">损失方</th>
							<th style="width: 16%">车牌号/物体名称</th>
							<th style="width: 16%">损失项备注说明</th>
							<th style="width: 16%">操作</th>
						</tr>
					</thead>
					<tbody id="defLossTbody">
						<input type="hidden" id="defLossSize" value="${defLossSize}">
						<input type="hidden" id="sanZhe" value="${sanZhe}">
						
					 	<%@include file="AddDefLoss.jsp" %>
					</tbody>
				</table>
			</div>
		</div>
		<br />
		<!--增加按钮 -->
		<div class="text-c">
			<button class="btn btn-primary btn-addBtn" onclick="addDefLoss()" type="button">增&nbsp;&nbsp;加</button>
		</div>
		<br />
		<!-- prpLScheduleTaskVo 隐藏域 开始 -->
		<input id="registNo" name="prpLScheduleTaskVo.registNo" value="${prpLScheduleTaskVo.registNo}" type="hidden" />
		<input name="prpLScheduleTaskVo.position" value="${prpLScheduleTaskVo.position}" type="hidden" />
		<input name="prpLScheduleTaskVo.mercyFlag" value="${prpLScheduleTaskVo.mercyFlag}" type="hidden" />
		<input name="prpLScheduleTaskVo.id" value="${prpLScheduleTaskVo.id}" type="hidden" />
		<input name="prpLScheduleTaskVo.selfDefinareaCode" value="${prpLScheduleVo.selfDefinareaCode}" id="selfDefinareaCode" type="hidden" />
		<input name="prpLScheduleTaskVo.isComuserCode" value="${prpLScheduleTaskVo.isComuserCode}" id="isComuserCode" type="hidden" />
		<!-- prpLScheduleTaskVo 隐藏域 结束 -->
		
		<!-- 工作流参数隐藏域开始 -->
		<input id="flowId" type="hidden" name="submitVo.flowId" value="${flowId}" />
		<input id="flowTaskId" type="hidden" name="submitVo.flowTaskId" value="${flowTaskId}" />
		<!-- 工作流参数隐藏域开始 -->
		
		<!-- 其他隐藏域 开始 -->
		<input type="hidden" id="checkSubmited" value="${checkSubmited}" />
		<input type="hidden" id="thisCarNo" value="${thisCarNo}" />
		<c:forEach var="thirtCarNo" items="${carNoList}" varStatus="index">
			<input type="hidden" name="thirtCarNo"  value="${thirtCarNo}" />
		</c:forEach>
		<input type="hidden" id="maxSerialNo" value="${maxSerialNo}" />
		<input name="switchMap" value="${switchMap}" id="switchMap" type="hidden" />
		<input name="oldClaim" value="${oldClaim}" id="oldClaim" type="hidden" />
		
		<!-- 其他隐藏域 结束 -->

		<!--  -->
		<div class="line"></div>
		<div class="table_wrap">
			<div class="table_cont ">
				<div class="formtable">
					<div class="row  cl">
						<label class="form_label col-2">联系人姓名</label>
						<div class="form_input col-2">
							<%-- ${prpLScheduleTaskVo.linkerMan} --%>
							<input type="text" style="width: 80%" name="prpLScheduleTaskVo.linkerMan" class="input-text" value="${prpLScheduleTaskVo.linkerMan}"/>
							<font class="form-text" color="red">*</font>
						</div>
						<label class="form_label col-1">联系人电话1</label>
						<div class="form_input col-2">
							<%-- ${prpLScheduleTaskVo.linkerManPhone} --%>
							<input type="text" style="width: 80%"  class="input-text" name="prpLScheduleTaskVo.linkerManPhone" value="${prpLScheduleTaskVo.linkerManPhone}"/>
							<font class="form-text" color="red">*</font>
						</div>
						<label class="form_label col-1">联系人电话2</label>
						<div class="form_input col-2">
							<%-- ${prpLScheduleTaskVo.linkerManPhone2} --%>
							<input type="text" style="width: 80%"  class="input-text" name="prpLScheduleTaskVo.linkerManPhone2" value="${prpLScheduleTaskVo.linkerManPhone2}"/>
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-2">出险地点</label>
						<div class="form_input col-5">
							<app:codetrans  codeType="AreaCode" codeCode="${prpLScheduleTaskVo.damageAreaCode}" />
							<input type="hidden" id="damageAreaCode"  name="prpLScheduleTaskVo.damageAreaCode" value="${prpLScheduleTaskVo.damageAreaCode}" >
							&nbsp;${prpLScheduleTaskVo.damageAddress}
						</div>
						<div class="form_input col-3">
							<input type="hidden" name="prpLScheduleTaskVo.damageAddress" readonly="readonly" class="input-text" value="${prpLScheduleTaskVo.damageAddress}" />
						</div>
					</div>
					<div class="row cl">
						<label class="form_label col-2">约定查勘地点</label>
						<div class="form_input col-5">
							<app:codetrans  codeType="AreaCode" codeCode="${prpLScheduleTaskVo.checkorDeflossAreaCode}" />
							<input type="hidden" id="checkorDeflossAreaCode"  name="prpLScheduleTaskVo.checkorDeflossAreaCode" value="${prpLScheduleTaskVo.checkorDeflossAreaCode}" >
							<input type="hidden" id="checkAddress" value="${prpLScheduleTaskVo.checkAddress}" />
							&nbsp;${prpLScheduleTaskVo.checkAddress}
						</div>
						<div class="form_input col-3">
							<input type="hidden" class="input-text" name="prpLScheduleTaskVo.checkAddress" value="${prpLScheduleTaskVo.checkAddress}" placeholder="详细地址" />
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-2">查勘/定损片区所在省/市</label>
						<div class="form_input col-10">
						<c:if test="${switchMap == '1' }">
						<div id="checkAddressDiv">
								<app:areaSelect targetElmId="switchMapProvinceCityAreaCode" areaCode="${prpLScheduleTaskVo.regionCode}" showLevel="3" handlerStatus="1"/> 
						</div>
						<input type="hidden" id="switchMapProvinceCityAreaCode"  name="prpLScheduleTaskVo.provinceCityAreaCode" value="${prpLScheduleTaskVo.provinceCityAreaCode}" >
						
						</c:if>
						<c:if test="${switchMap != '1' }">
							<div id="checkAddressDiv">
								<c:if test="${oldClaim == '1' }">
								   <app:areaSelect targetElmId="provinceCityAreaCode" areaCode="${prpLScheduleTaskVo.provinceCityAreaCode}" showLevel="2" handlerStatus="1"/>
							    </c:if>
								<c:if test="${oldClaim == '0' }">
								   <app:areaSelect targetElmId="provinceCityAreaCode" areaCode="${prpLScheduleTaskVo.provinceCityAreaCode}" showLevel="2"  disabled="true"/>
							    </c:if>
							</div>
							<input type="hidden" id="provinceCityAreaCode"  name="prpLScheduleTaskVo.provinceCityAreaCode" value="${prpLScheduleTaskVo.provinceCityAreaCode}" >
						</c:if>
						</div>
					<!-- 	<label class="form_label col-2">查勘片区归属机构</label>
						<div class="form_input col-3">
							<span class="select-box">
								<select class="select">
									<option></option>
								</select>
							</span>
						</div> -->
					</div>
					<div class="row  cl">
						<label class="form_label col-2">查勘/定损片区</label>
						<div class="form_input col-3">
							<input type="text" class="input-text" id="checkareaName"  readOnly style="width:96%" name="prpLScheduleTaskVo.checkareaName" value="${prpLScheduleTaskVo.checkareaName}"/>
							<input type="hidden"  id="regionCode" style="width:96%" name= "prpLScheduleTaskVo.regionCode" value="${prpLScheduleTaskVo.regionCode}"/>
							<input type="hidden"  id="checkAddressMapCode" style="width:96%" name= "prpLScheduleTaskVo.checkAddressMapCode" value="${prpLScheduleTaskVo.checkAddressMapCode}"/>
							<font color="red">*</font>
						</div>
						<div class="form_input col-3 ml-15">
							<input type="button" id="switchMaps" class="btn btn-zd" onclick="openDlossMap()" value="电子地图" />
							<!-- <input type="button" value="自定义查勘员" onclick="openSelfDefineMap()" class="btn btn-zd"> -->
							<!-- <input type="button" value="自定义查勘员" id="define" onclick="selfDefineOrNot(1)" class="btn btn-zd fl">
							<input type="button" value="取消自定义" id="notDefine" onclick="selfDefineOrNot(0)" class="hide btn-zd fl"> -->
						</div>
						<div id="self" class="form_input col-3"></div>
					</div>
					<div class="row  cl">
						<label class="form_label col-2">出险经过</label>
						<div class="form_input col-5">
						<textarea class="textarea w90" disabled  maxlength="1000" datatype="*1-1000" placeholder="请输入...">${registExt.dangerRemark}</textarea>
							
						</div>
						<br>
						<br>
						<br>
						<br>
						<br>
						<br>
					</div>
					</div>
					</div>
					<div class="table_wrap">
		            	<div class="table_title f14">定损人员</div>
							<table class="table table-border table-bordered table-hover" cellpadding="0" cellspacing="0">
								<thead>
									 	<tr class="text-c">
									 	 	<th style="width:23%;">
												<div class="row  cl">
											 	 		<label class="form_label col-6">姓名	</label>
											 	 		<label class="form_label col-3 text-r">是否自定义</label>
											 	 		<div class="form_input col-1"><input type="checkbox"  name="selfCheck" ></div>
											 	 </div>	
											</th>
									 	 	<th style="width:20%;">关联查勘员</th>
										 <!-- 	<th style="width:20%;">关联查勘员电话</th>
										 	<th style="width:20%;">查勘员电话2</th> -->
										 	<th style="width:20%;">短信通知号码</th>
										 	<th style="width:20%;">电话通知号码</th>
										 	<th style="width:20%;">所属机构</th>
									 	</tr>
										</thead>
										<tbody id="plossBody">
										 	<tr class="text-c">
											 	<td width="15%" id="check1"><!-- <span id="showUserName"></span> --><input  class="text-c input-text" value="" id="handleUserName" readonly name="prpLScheduleTaskVo.scheduledUsername" type="text" datatype="*"/><input value="" id="handleUserCode" name="prpLScheduleTaskVo.scheduledUsercode" type="hidden"/></td>
											 	<td width="15%">
											 	<input class="text-c input-text"  value="${prpLScheduleVo.relateHandlerName}" id="relateHandlerNames" type="text" readonly/>
											 	<input value="${prpLScheduleVo.relateHandlerName}" id="relateHandlerName" name="prpLScheduleTaskVo.relateHandlerName" type="hidden"/></td>
											 	<td width="15%">
											 	<input class="text-c input-text"  value="${prpLScheduleVo.relateHandlerMobile}" id="relateHandlerMobiles" type="text" onblur="setMobile()" />
											 	<input value="${prpLScheduleVo.relateHandlerMobile}" id="relateHandlerMobile" name="prpLScheduleTaskVo.relateHandlerMobile" type="hidden"/></td>
											 	<td width="15%"><input  class="text-c input-text" value="${moblie}" id="checkMoblie" readonly  type="text" />
											 	<td width="15%" id="check2"><!-- <span id="showComName"></span> --><input  class="text-c input-text" value="" id="handleComName" readonly name="prpLScheduleTaskVo.scheduledComname"  type="text" datatype="*"/><input value="" id="handleComCode" name="prpLScheduleTaskVo.scheduledComcode" type="hidden"/></td>
											</tr>
									   </tbody>							
				   		</table>
				 	</div>
				</div>
				
		
		
		<br />
		<!-- 底部按钮 -->
		<br /> <br /> <br />
		<!-- 底部按钮 -->
		<div class="btn-footer clearfix text-c">
			<input class="btn btn-primary ml-10" type="button" value="短信处理"></input> 
			<input class="btn btn-primary btn-saveBtn" type="button" value="提交"></input>
			<input class="btn btn-primary ml-10" onclick="goBack()" type="button" value="返回"></input>
		</div>
	</div>
	</form>
	<script type="text/javascript">
	
		//$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
		$(function(){
			//判断地图有没有关，1为关闭,出险所在地可以手动录入
			if($("#switchMap").val()=="1"){
				$("#checkareaName").removeAttr("readonly");
				$('#switchMaps').attr("disabled",true); 
				$('#switchMaps').addClass("btn-disabled");
			}
			//控制增加按钮的是否可以点击
			if ($("#checkSubmited").val() == "0") {
				/* $("button.btn-addBtn").removeClass('btn-primary');
				$("button.btn-addBtn").addClass('btn-disabled');
				$("button.btn-addBtn").prop("disabled",true);
				$("#defLossTbody select").prop("disabled", true); */
				$("#defLossTbody select").prop("disabled", true);
			} 
			
			//查勘调度提交
			$("input.btn-saveBtn").click(function() {
				//判断联系人姓名联系人电话1是否为空
				var linkerMan = $("input[name$='linkerMan']").val();
				var linkerManPhone = $("input[name$='linkerManPhone']").val();
				if(linkerManPhone==""||linkerManPhone==null){
					layer.alert("联系人电话1不能为空");
					
				}
				if(linkerMan==""||linkerMan==null){
					layer.alert("联系人姓名不能为空");
					return false;
				}
				var rules = {
				};

				var checkAreaCode = $("#regionCode").val();
			    var lngXlatY = $("#checkAddressMapCode").val();
				var ajaxEdit = new AjaxEdit($('#editform'));
				ajaxEdit.beforeCheck=function(){//校验之前
					
				};
				ajaxEdit.beforeSubmit=function(){//提交前补充操作
				};
		
				ajaxEdit.targetUrl = "/claimcar/schedule/defLossSave.do?checkAreaCode="+checkAreaCode+"&lngXlatY="+lngXlatY;;
				ajaxEdit.rules =rules;
				ajaxEdit.afterSuccess=function(data){//操作成功后操作
					//layer.alert("请选择需要调度的损失项");
					if(data.data != ""){
						layer.alert(data.data+"已调度");
					}else{
						layer.confirm('定损调度成功', {
						    btn: ['确定'] //按钮
						}, function(){
							location.href = "/claimcar/schedule/preScheduleEdit.do?flowTaskId="+$("#flowTaskId").val() +"&registNo=" + $("#registNo").val();
						});
					 }
				}; 
				//绑定表单
				ajaxEdit.bindForm();
				 if(!validate()){
               	  return false;
                 }
				/* var policyNo = "";
				
				var idArray = selectedIds.split(",");
				for (var i = 0; i < idArray.length; i++) {
					var id = idArray[i];
					var idA=id.split("&");
					policyNo = policyNo + idA[0] + ",";
				} */
				$("#editform").submit();
			});
			
			
		});
		
		
		function validate(){
			$()
			var selectedIds = getSelectedIds();
			var strs=new Array();
			strs = selectedIds.split(",");
			if(selectedIds=="") {
				layer.alert("请选择需要调度的损失项");
				return false;
			}
			//若已生成某三者车定损任务节点，还调度新增相同车牌的三者车定损任务，则提交时提示“该三者车定损任务已调度，不能重复调度
			var defLossSize = $("#defLossSize").val();
			//校验车牌号重复
			for (var i = 0; i < defLossSize; i ++) {
				for (var k=0;k<strs.length ;k++ ) {
					if(strs[k]==i){
						var deflossType = $("[name='prpLScheduleDefLosses[" + i + "].deflossType']").val();//定损类型
						var lossitemType = $("[name='prpLScheduleDefLosses[" + i + "].lossitemType']").val();//损失方
						var itemsContent = $("input[name='prpLScheduleDefLosses[" + i + "].itemsContent']").val();//车牌号/物体名称
						if(typeof(itemsContent) == "undefined"){
							itemsContent = $("select[name='prpLScheduleDefLosses[" + i + "].itemsContent']").val();
						}
						if(itemsContent == ""){
							layer.alert("请输入全部“车牌号/物体名称”");
							return false;
						}
						var thisCarNo = $("#thisCarNo").val();
						if(lossitemType =="3" && $.trim(itemsContent)==thisCarNo){
							layer.alert("三者车牌不能和标的车牌一致");
							return false;
						}
						/* if(deflossType == "1" && lossitemType == "1"){//车损
							layer.alert("标的车定损任务已调度，不能重复调度");
							return false;
						} */
						for (var j = i+1; j < defLossSize; j ++) {
							for (var a=0;a<strs.length ;a++ ) {
								if(strs[a]==j){
									var deflossTypeNext = $("[name='prpLScheduleDefLosses[" + j + "].deflossType']").val();
									var lossitemTypeNext = $("[name='prpLScheduleDefLosses[" + j + "].lossitemType']").val();
									var itemsContentNext = $("input[name='prpLScheduleDefLosses[" + j + "].itemsContent']").val();
									if(deflossType != deflossTypeNext){
										layer.alert("车辆定损和财产定损不能同时发定损 ");
										return false;
									}
									if(lossitemType == lossitemTypeNext && itemsContent == itemsContentNext){
										layer.alert("不能重复调度 ");
										return false;
									}
									if(deflossType == deflossTypeNext && deflossTypeNext=="2" && lossitemType == lossitemTypeNext && lossitemTypeNext=="3"){
										layer.alert("地面财不能重复调度 ");
										return false;
									}
								}
							}
						}
					}
				}
				
			}
			return true;
		}
		
		
		function goBack(){
			location.href = "/claimcar/schedule/preScheduleEdit.do?flowTaskId="+$("#flowTaskId").val() +"&registNo=" + $("#registNo").val();
			location.href = "/claimcar/schedule/preScheduleEdit.do?flowTaskId="+$("#flowTaskId").val() +"&registNo=" + $("#registNo").val();
		}
		
		function getSelectedIds() {
			var selectIds = "";
			$("input[name='checkCode']:checked").each(function() {
				selectIds = selectIds + $(this).val() + ",";
			});
			if (selectIds != "") {
				selectIds = selectIds.substr(0, selectIds.length - 1);
			}
			return selectIds;
		}
		
		/* function getDeflossTypes() {
			var selectIds = "";
			$("input[name='checkCode']:checked").each(function() {
				selectIds = selectIds + $(this).val() + ",";
			});
			if (selectIds != "") {
				selectIds = selectIds.substr(0, selectIds.length - 1);
			}
			return selectIds;
		} */

		//新增定损项
		function addDefLoss() {
			var $tbody = $("#defLossTbody");
			var defLossSize = $("#defLossSize").val();// 损失项条数
			var params = {
				"defLossSize" : defLossSize,
			};
			var url = "/claimcar/schedule/addDefLoss.ajax";
			$.post(url, params, function(result) {
				$("#defLossSize").val(parseInt(defLossSize) + 1);
				$tbody.append(result);
				
				//alert($("#defLossSize").val());
				/* changeDefLossType(1, defLossSize);
				$("select[name='prpLScheduleDefLosses["+defLossSize+"].licenseNo']").attr("datatype","*"); */
			});
		}
		
		//删除定损项
		function delDefLoss(element) {
			var $parentTr = $(element).parent().parent();
			var defLossSize = $("#defLossSize").val();// 损失项条数
			$("#defLossSize").val(defLossSize - 1);// 删除一条
			$parentTr.remove();
		}
		
		//自定义人员与取消
		function selfDefineOrNot(flag) {
			if (flag == 1) {
				$("#define").addClass('hide');
				$("#notDefine").removeClass('hide');
				$("#define").removeClass('btn');
				$("#notDefine").addClass('btn');
				$("#defineDiv").removeClass('hide');
			} else {
				$("#notDefine").addClass('hide');
				$("#define").removeClass('hide');
				$("#notDefine").removeClass('btn');
				$("#define").addClass('btn');
				$("#defineDiv").addClass('hide');
			}
		}
		
		//scheduleFlag
		function changeScheduleFlag(element) {
			var flag = $(element).prop("checked");
			if (flag) {
				$("#scheduleFlag"+$(element).val()).val(1);
			} else {
				$("#scheduleFlag"+$(element).val()).val(0);
			}
		}
		
		function changeDefLossType(value, index) {
			var thisCarNo = $("#thisCarNo").val();
			if (value == 1) {
				var options ="<option value=''></option><option value='"+thisCarNo+"'>标的车("+thisCarNo+")</option>";	
			} else {
				var options ="<option value=''></option><option value='0'>地面</option><option value='"+thisCarNo+"'>标的车("+thisCarNo+")</option>";
			}
			
			var items =$("input[name='thirtCarNo']");
			$(items).each(function(){
				var thirdNo = $(this).val();
				alert(thirdNo);
				if(!isBlank(thirdNo)){
					options = options+"<option value='"+thirdNo+"'>三者车("+thirdNo+")</option>";
				};
			});
			$("select[name='prpLScheduleDefLosses["+index+"].licenseNo']").find("option").remove();
			$("select[name='prpLScheduleDefLosses["+index+"].licenseNo']").append(options);
		}
		
		function openDlossMap() {
			
            var item = 'DLCar';
			
			var url = "/claimcar/manualSchedule/defLossScheduleOpenMap.ajax";
			
			var ids = "";
			
			/* $("input[name='checkCode']:checked").each(function() {
				//alert($(this).attr("dLossId") );
				ids = ids + $(this).attr("dLossId") + ",";
			});
			if (ids != "") {
				ids = ids.substr(0, ids.length - 1);
			}else{
				layer.alert("请选择调度任务");
				return false;
			} */
			var registNo = $("#registNo").val();
			var oldClaim = $("#oldClaim").val();
			var checkAreaCode = "";
			if(oldClaim =="1"){
				checkAreaCode = $("#provinceCityAreaCode").val();
			}else{
				checkAreaCode = $("#regionCode").val();
			}
			var checkAddress = $("#checkareaName").val();
			var lngXlatY = $("#checkAddressMapCode").val();
			var selfDefinareaCode = $("#selfDefinareaCode").val();
			var params = {
					"registNo" 		 	: registNo,
					"checkAreaCode"	 	: checkAreaCode,
					"checkAddress"	    : checkAddress,
					"typeFlag"			: item,
					"lngXlatY"          : lngXlatY,
					"selfDefinareaCode" :selfDefinareaCode
				};
			
			$.ajax({
				url : url,
				type : "post",
				data : params,
				async : false,
				success : function(htmlData) {
					var url = htmlData.data;
					// 调度定损任务电子地图请求中含有换行符无法正常打开电子地图，过滤掉
					if (url != undefined && url != null) {
						url = url.replace(/[\r\n]/g, "");
					}
					layer.open({
					    type: 2,
					    title:'电子地图',
					    area: ['98%', '98%'],
					    fix: true, //固定
					    maxmin: true,
					    content: url
					});
				}
			});
		}
		
		function getMapInfo(returnData) {
			/*  $("#nextHandlerName option").each(function(){
					var userCodeNow = $(this).val();
					var userCodeNows = userCodeNow.split(",");
					if(userCodeNow!=""){
						if(userCodeNows[1]==returnData.lossUserCode){
							$(this).prop("selected",true);
						}
					}
				}); */
			 if(returnData.lossUserName!=null && returnData.lossUserName!=""){
				 	$("input[name='selfCheck']").prop("disabled",false);
				 	$("input[name='selfCheck']").attr("checked",false);
				 	$("#check1").empty();
					var html = "<input class='text-c input-text' type='text'  value='' id='handleUserName'  name='prpLScheduleTaskVo.scheduledUsername'/>"+
					 "<input value='' id='handleUserCode' name='prpLScheduleTaskVo.scheduledUsercode' type='hidden'/>";
				 	$("#check1").append(html);
			}
			
			
			$("#showUserName").html(returnData.lossUserName);
			$("#showComName").html(returnData.lossComName);
			$("#checkareaName").val(returnData.areaAddress);
			$("#regionCode").val(returnData.regionCode);
			$("#handleComCode").val(returnData.lossComCode);
			$("#handleComName").val(returnData.lossComName);
			$("#handleUserCode").val(returnData.lossUserCode);
			$("#handleUserName").val(returnData.lossUserName);
			
			
		
			$("#selfDefinareaCode").val(returnData.selfDefinareaCode);
			$("#relateHandlerName").val(returnData.lrelateHandlerName);
			$("#relateHandlerMobile").val(returnData.lrelateHandlerMobile);
			$("#relateHandlerNames").val(returnData.lrelateHandlerName);
			$("#relateHandlerMobiles").val(returnData.lrelateHandlerMobile);
			$("#isComuserCode").val(returnData.lisComuserCode);
			getAllAreaInfo(returnData.cityCode,"checkAddressDiv","provinceCityAreaCode",2,null,'disabled',null);
		}
		
		function defLossTypeChange(e){
			var value = $(e).val();
			var name = $(e).attr("name");
			var index = name.replace(/[^0-9]/ig,""); //得到所在序列
			var itemTypeDiv = $("#itemTypeDiv"+index);
			var sanZhe = $("#sanZhe").val();
			
			
			var itemsContentDiv =  $("#itemsContentDiv"+index);
			var itemsContentObj = $("input[name='prpLScheduleDefLosses[" + index + "].itemsContent']");
			//var itemTypeName = "select[name='prpLScheduleDefLosses[" + index + "].lossitemType']";
			if(value == "2"){//物损
				itemsContentDiv.empty();
			
			//判断是否显示三者车
				if(sanZhe=="1"){
					itemsContentDiv.append("<input type='text' datatype='*' class='input-text' maxlength='50'   name='prpLScheduleDefLosses["+index+"].itemsContent' />");
					var html = "<select  name='prpLScheduleDefLosses["+index+"].lossitemType' onchange='lossitemTypeChange(this)' class='  must select'>"+
					"<option value='3'>地面</option><option value='2'>三者车</option><option value='1'>标的车</option></select>"
				}else{
					itemsContentDiv.append("<input type='text' datatype='*' class='input-text' maxlength='50'   name='prpLScheduleDefLosses["+index+"].itemsContent' />");
					var html = "<select  name='prpLScheduleDefLosses["+index+"].lossitemType' onchange='lossitemTypeChange(this)' class='  must select'>"+
					"<option value='3'>地面</option><option value='1'>标的车</option></select>"
				}
				
				itemTypeDiv.empty();
				itemTypeDiv.append(html);
			}else if(value =="1"){//车损
				itemsContentDiv.empty();
				itemsContentDiv.append("<input type='text' errormsg='请输入正确车牌号' datatype='/^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$/' class='input-text' maxlength='50'   name='prpLScheduleDefLosses["+index+"].itemsContent' />");
				//itemsContentObj.attr("datatype","/^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$/");
				var html = "<select  name='prpLScheduleDefLosses["+index+"].lossitemType' onchange='lossitemTypeChange(this)' class='  must select'>"+
				"<option value='2'>三者车</option></select>";
				itemTypeDiv.empty();
				itemTypeDiv.append(html);
			}
		}
		
		function lossitemTypeChange(e){
			var value = $(e).val();
			var name = $(e).attr("name");
			var index = name.replace(/[^0-9]/ig,""); //得到所在序列
			var itemsContentDiv =  $("#itemsContentDiv"+index);
			var itemsContentObj = $("input[name='prpLScheduleDefLosses[" + index + "].itemsContent']");
			var html ="";
			if(value == "3"){//物损
				itemsContentDiv.empty();
				html="<input type='text' datatype='*' class='input-text' maxlength='50'   name='prpLScheduleDefLosses["+index+"].itemsContent' />";
			}else if(value == "1"){//标的车
				itemsContentDiv.empty();
				var thisCarNo = $("#thisCarNo").val();
				html="<input type='text' readonly class='input-text' maxlength='50'  value='"+thisCarNo+"'  name='prpLScheduleDefLosses["+index+"].itemsContent' />";
				/* itemsContentObj.attr("datatype","/^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$/");
				itemsContentObj.attr("readonly","readonly");
				itemsContentObj.val($("#thisCar").val()); */
			}else{
				itemsContentDiv.empty();
				var items =$("input[name='thirtCarNo']");
				if(items.length > 0){
					html = "<select  name='prpLScheduleDefLosses["+index+"].itemsContent'  class='  must select'>";
					$(items).each(function(){
						var thirdNo = $(this).val();
						if(!isBlank(thirdNo)){
							html = html+"<option value='"+thirdNo+"'>"+thirdNo+"</option>";
						};
					});
					html = html+"</select>";
				}else{
					html="<input type='text' readonly datatype='*' class='input-text' maxlength='50'  value=''  name='prpLScheduleDefLosses["+index+"].itemsContent' />";
				}
			}
			itemsContentDiv.append(html);
		}
		
		function selfschedule(){
			var nextHandlerName = $("#nextHandlerName").val();
			var nextName = nextHandlerName.split(",");
			$("#handleUserName").val(nextName[0]);
			$("#handleComName").val(nextName[2]);
			$("#handleUserCode").val(nextName[1]);
			$("#handleComCode").val(nextName[3]);
			//查勘员电话2取值员工维护表的电话（prpduser）
			$.ajax({
				url : '/claimcar/schedule/findSysUserVo.do?userCode='
						+ nextName[1],
				dataType : "json",// 返回json格式的数据
				type : 'get',
				success : function(json) {// json是后端传过来的值
					$("#checkMoblie").val(json.statusText);
				},
				error : function() {
					layer.msg("查勘获取查勘员电话2异常");
				}
			});
			if(nextName[4]!='null'){
				$("#relateHandlerName").val(nextName[4]);
				$("#relateHandlerNames").val(nextName[4]);
			}
			if(nextName[5]!='null'){
				$("#relateHandlerMobile").val(nextName[5]);
				$("#relateHandlerMobiles").val(nextName[5]);
			}
			$("#isComuserCode").val(nextName[6]);
		}
		
		
		$("input[name='selfCheck']").change(function() {
			//关地图
			var switchMap = $("#switchMap").val();
			if(switchMap=="1"){
			$("#switchMapProvinceCityAreaCode").val($("#switchMapProvinceCityAreaCode_lv2").val());
			var switchMapAreaCode = $("#switchMapProvinceCityAreaCode_lv3").val();
			$("#regionCode").val(switchMapAreaCode);
			$.ajax({
				url : '/claimcar/schedule/switchMapGpsCode.do?areaCode='
						+ switchMapAreaCode,
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
		
			
			var selectedIds = getSelectedIds();
			if(selectedIds=="") {
				layer.alert("请选择需要调度的损失项");
				 $("input[name='selfCheck']").each(function(){
					   $(this).attr("checked",false);
					  });  
				return false;
			}
					var editform = $('#editform').serialize();
					var url = "/claimcar/manualSchedule/selfDefineOpenMaps.ajax";
					$.ajax({
						url : url,
						type : "post",
						data : editform,
						async : false,
						success : function(htmlData) {
							$("input[name='selfCheck']").prop("disabled",true);
								$("#check1").empty();
								var scheduleItemList = htmlData.data;
								html = "<select id='nextHandlerName' name='nextHandlerName' onchange='selfschedule()' class='  must select'>";
								html = html+"<option value=''></option>";
								for( var i = 0; i < scheduleItemList.length; i++){
									var name = scheduleItemList[i].nextHandlerName;
									var nextHandlerCode = scheduleItemList[i].nextHandlerCode;
									var scheduleObjectId = scheduleItemList[i].scheduleObjectId;
									var scheduleObjectName = scheduleItemList[i].scheduleObjectName;
									
									var isComuserCode = scheduleItemList[i].isComuserCode;
									var relateHandlerName = scheduleItemList[i].relateHandlerName;
									var relateHandlerMobile = scheduleItemList[i].relateHandlerMobile;
									html = html+"<option value='"+name+","+nextHandlerCode+","+scheduleObjectName+","+scheduleObjectId
									+","+relateHandlerName+","+relateHandlerMobile+","+isComuserCode+"'>"+name+"</option>";
								}
								html = html+"</select>";
								html = html+"<input type='hidden'  value='' id='handleUserName'  name='prpLScheduleTaskVo.scheduledUsername'/>"+
							 	"<input value='' id='handleUserCode' name='prpLScheduleTaskVo.scheduledUsercode' type='hidden'/>";
								$("#check1").append(html);

								}});
		});
		//var lossitemTypeObj = $("input[name='prpLScheduleDefLosses[" + index + "].lossitemType']");
		function setMobile(){
			var relateHandlerMobiles = $("#relateHandlerMobiles").val();
			$("#relateHandlerMobile").val(relateHandlerMobiles);
		}
		</script>
</body>
</html>