<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>调度改派</title>
</head>
<body>
<form action="#" id="editform" >
	<div class="page_wrap">
		<!-- 查勘任务改派  开始 -->
		<div class="table_wrap">
			<div class="table_title f14">请选择需要改派的任务</div>
			<div class="table_cont ">
				<table class="table table-border table-bordered table-hover"
					cellpadding="0" cellspacing="0">
					<thead>
						<h5>查勘任务改派</h5>
						<tr class="text-c">
							<th style="width: 6%">选择</th>
							<th style="width: 16%">调度任务号</th>
							<th style="width: 26%">损失项</th>
							<th style="width: 16%">调度状态</th>
							<th style="width: 16%">机构/组别</th>
							<th style="width: 16%">查勘人员</th>
						</tr>
					</thead>
					<tbody>
						<!-- 循环查勘任务 开始 -->
						<c:forEach items="${scheduleTaskVos}" varStatus="index" var="scheduleTaskVo">
							<tr class="text-c">
								<td><input type="radio" onclick="changeCheckAddressLable('约定查勘地点')" name="reassignment" flowtaskid="${scheduleTaskVo.flowTaskId}" schType="Check" id="${scheduleTaskVo.id}" /></td>
								<td>${scheduleTaskVo.id}</td>
								<td>
									<app:codetrans codeType="IsPersonFlag" codeCode="${scheduleTaskVo.isPersonFlag}"/>
								</td>
								<td><app:codetrans codeType="ScheduleStatus" codeCode="${scheduleTaskVo.scheduleStatus}"/></td>
								<td><app:codetrans codeType="ComCode" codeCode="${scheduleTaskVo.scheduledComcode}"/></td>
								<td><app:codetrans codeType="UserCode" codeCode="${scheduleTaskVo.scheduledUsercode}"/></td>
							</tr>
						</c:forEach>
						<!-- 循环查勘任务 结束 -->
						<!-- 循环人伤任务 开始-->
						<c:forEach items="${scheduleItemVos}" varStatus="index" var="scheduleItemVo">
							<tr class="text-c">
								<td><input type="radio" name="reassignment" onclick="changeCheckAddressLable('约定查勘地点')" flowtaskid="${scheduleItemVo.flowTaskId}" schType="PLoss" id="${scheduleItemVo.id}" /></td>
								<td>${scheduleItemVo.id}</td>
								<td>
									<app:codetrans codeType="ItemType" codeCode="${scheduleItemVo.itemType}"/>
								</td>
								<td><app:codetrans codeType="ScheduleStatus" codeCode="${scheduleItemVo.scheduleStatus}"/></td>
								<td><app:codetrans codeType="ComCode" codeCode="${scheduleItemVo.scheduledComcode}"/></td>
								<td><app:codetrans codeType="UserCode" codeCode="${scheduleItemVo.scheduledUsercode}"/></td>
							</tr>
						</c:forEach>
						<!-- 循环人伤任务 结束-->
					</tbody>
				</table>
			</div>
			<br/>
			<div class="table_cont ">
				<!-- 定损任务改派 -->
				<table class="table table-border table-bordered table-hover"
					cellpadding="0" cellspacing="0">
					<thead>
						<h5>定损任务改派</h5>
						<tr class="text-c">
							<th style="width: 6%">选择</th>
							<th style="width: 16%">调度任务号</th>
							<th style="width: 26%">损失项</th>
							<th style="width: 16%">调度状态</th>
							<th style="width: 16%">机构/组别</th>
							<th style="width: 16%">定损人员</th>
						</tr>
					</thead>
					<tbody>
						<!-- 循环定损任务 开始-->
						<c:forEach items="${scheduleDefLossVos}" varStatus="index" var="scheduleDefLossVo">
							<tr class="text-c">
								<td><input type="radio" onclick="changeCheckAddressLable('约定定损地点')" name="reassignment"  flowtaskid="${scheduleDefLossVo.flowTaskId}"
								schType="<c:choose>
								    	<c:when test="${scheduleDefLossVo.deflossType eq '1'}">DLCar</c:when>
								    	<c:otherwise>DLProp</c:otherwise>
									</c:choose>" id="${scheduleDefLossVo.id}" /></td>
								<td>${scheduleDefLossVo.id}</td>
								<td>
									<c:choose>
								    	<c:when test="${scheduleDefLossVo.deflossType eq '1'}">
								    		车辆定损：${scheduleDefLossVo.itemsName}（${scheduleDefLossVo.itemsContent}）
								    	</c:when>
								    	<c:otherwise>
								    		财产定损：${scheduleDefLossVo.itemsContent}（${scheduleDefLossVo.licenseNo}）
								    	</c:otherwise>
									</c:choose>
								</td>
								<td><app:codetrans codeType="ScheduleStatus" codeCode="${scheduleDefLossVo.scheduleStatus}"/></td>
								<td><app:codetrans codeType="ComCode" codeCode="${scheduleDefLossVo.scheduledComcode}"/></td>
								<td>
								<app:codetrans codeType="UserCode" codeCode="${scheduleDefLossVo.scheduledUsercode}"/></td>
							</tr>
						</c:forEach>
						<!-- 循环定损任务 结束-->
					</tbody>
				</table>
				<!-- 定损任务改派 -->
			</div>
		</div>
		<br />
		
		<div class="table_wrap">
		
		</div>
		<input name="switchMap" value="${switchMap}" id="switchMap" type="hidden" />
		<input id="registNo" name="prpLScheduleTaskVo.registNo" value="${prpLScheduleTaskVo.registNo}" type="hidden" />
		<input id="selfDefinareaCode" name="prpLScheduleTaskVo.selfDefinareaCode" value="${prpLScheduleVo.selfDefinareaCode}" type="hidden" />
		<!-- 工作流参数隐藏域开始 -->
		<input id="flowId" type="hidden" name="submitVo.flowId" value="${flowId}" />
		<input id="flowTaskId" type="hidden" name="submitVo.flowTaskId" value="${flowTaskId}" />
		<input name="prpLScheduleTaskVo.isComuserCode" value="${prpLScheduleTaskVo.isComuserCode}" id="isComuserCode" type="hidden" />
		<!-- 工作流参数隐藏域开始 -->
		<!-- 通赔 -->
		<input  type="hidden" name="submitVo.comCode" value="${comCode}" />
		<input name="oldClaim" value="${oldClaim}" id="oldClaim" type="hidden" />
		<!--  -->
		<div class="table_wrap">
			<div class="table_cont">
				<div class="formtable">
					<div class="row  cl">
						<label class="form_label col-2">联系人姓名</label>
						<div class="form_input col-2">
							<input type="text" name="prpLScheduleTaskVo.linkerMan"  class="input-text" value="${prpLScheduleTaskVo.linkerMan}"/>
							<%-- ${prpLScheduleTaskVo.linkerMan} --%>
						</div>
						<label class="form_label col-2">联系人电话1</label>
						<div class="form_input col-2">
							<input type="text" class="input-text" name="prpLScheduleTaskVo.linkerManPhone" value="${prpLScheduleTaskVo.linkerManPhone}"/>
							<%-- ${prpLScheduleTaskVo.linkerManPhone} --%>
						</div>
						<label class="form_label col-1">联系人电话2</label>
						<div class="form_input col-2">
							<input type="text" class="input-text"  name="prpLScheduleTaskVo.linkerManPhone2" value="${prpLScheduleTaskVo.linkerManPhone2}"/>
							<%-- ${prpLScheduleTaskVo.linkerManPhone2} --%>
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-2">改派原因</label>
						<div class="form_input col-2">
							<app:codeSelect	codeType="ScheduleChange" type="select" id="CancelOrReassignCode"
										name="prpLScheduleTaskVo.CancelOrReassignCode" />
						</div>
						<label class="form_label col-2">改派原因备注</label>
						<div class="form_input col-2">
							<input type="text" class="input-text" maxlength="20" name="prpLScheduleTaskVo.cancelOrReassignContent" value="${prpLScheduleTaskVo.cancelOrReassignContent}"/>
						</div>
						<label class="form_label col-1"></label>
						<div class="form_input col-2">
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-2">出险地点</label>
						<div class="form_input col-6">
							<app:codetrans  codeType="AreaCode" codeCode="${prpLScheduleTaskVo.damageAreaCode}"/>
							<input type="hidden" id="damageAreaCode"  name="prpLScheduleTaskVo.damageAreaCode" value="${prpLScheduleTaskVo.damageAreaCode}" >
							&nbsp;${prpLScheduleTaskVo.damageAddress}
							<input type="hidden" name="prpLScheduleTaskVo.damageAddress" readonly="readonly" class="input-text" value="${prpLScheduleTaskVo.damageAddress}" placeholder="详细地址" />
						</div>
					</div>
					<div class="row cl">
						<%-- <label class="form_label col-2">约定查勘地点</label>
						<div class="form_input col-9">
							<app:codetrans  codeType="AreaCode" codeCode="${prpLScheduleTaskVo.checkorDeflossAreaCode}"/>
							<input type="hidden" id="checkorDeflossAreaCode"  name="prpLScheduleTaskVo.checkorDeflossAreaCode" value="${prpLScheduleTaskVo.checkorDeflossAreaCode}" >
							&nbsp;${prpLScheduleTaskVo.checkAddress}
							<app:areaSelect targetElmId="checkAddressCode" areaCode="${prpLScheduleTaskVo.checkorDeflossAreaCode}" showLevel="3" />
				 			<input type="hidden" id="checkAddressCode" name="prpLScheduleTaskVo.checkorDeflossAreaCode" value="${prpLScheduleTaskVo.checkorDeflossAreaCode}" >
				 			<input name="prpLRegistExtVo.checkAddress" value="${prpLScheduleTaskVo.checkAddress}" type="text" class="input-text w_25 mr-5" placeholder="详细地址" datatype="*" />
				 			<input type="button" class="btn btn-zd" value="电子地图" />
						</div> --%>
						<!--约定查勘地点注释开始-->
						<%-- <label class="form_label col-2" id="checkAddressLable">约定查勘地点</label>
				 		<div class="form_input col-10">
				 		    <div style="display:inline" id="appointedCheckAddressDiv">
				 			<app:areaSelect targetElmId="checkAddressCode" areaCode="${prpLScheduleTaskVo.checkorDeflossAreaCode}" showLevel="3" />
				 			</div>
				 			<input type="hidden" id="checkAddressCode" name="prpLScheduleTaskVo.checkorDeflossAreaCode" value="${prpLScheduleTaskVo.checkorDeflossAreaCode}">
							
							<input id="checkAddress" name="prpLScheduleTaskVo.checkAddress" maxlength="20" value="${prpLScheduleTaskVo.checkAddress}" type="text" class="input-text w_25 mr-5" placeholder="详细地址" datatype="*" />
							<input id="checkAddressMapCode" name="prpLRegistExtVo.checkAddressMapCode" value="${prpLRegistExtVo.checkAddressMapCode}" type="hidden" />
							<font class="form-text" color="red">*</font>
							<input type="button" class="btn btn-zd" onclick="openAddressMap()" value="电子地图"/>
				 	   </div> --%>
				 	   <!--约定查勘地点注释结束-->
					</div>
					<div class="row cl">
							<label class="form_label col-2">查勘片区所在省/市</label>
							<div class="form_input col-10">
								<c:if test="${switchMap == '1' }">
									<div id="checkAddressDiv">
										<app:areaSelect targetElmId="switchMapProvinceCityAreaCode" areaCode="${prpLScheduleTaskVo.regionCode}" showLevel="3" /> 
									</div>
									<input type="hidden" id="switchMapProvinceCityAreaCode"  name="prpLScheduleTaskVo.provinceCityAreaCode" value="${prpLScheduleTaskVo.provinceCityAreaCode}" >
								</c:if>
								<c:if test="${switchMap != '1' }">
								    <div id="checkAddressDiv">
									    <c:if test="${oldClaim == '1' }">
										   <app:areaSelect targetElmId="provinceCityAreaCode" areaCode="${prpLScheduleTaskVo.provinceCityAreaCode}" showLevel="2" />
									    </c:if>
										<c:if test="${oldClaim == '0' }">
										   <app:areaSelect targetElmId="provinceCityAreaCode" areaCode="${prpLScheduleTaskVo.provinceCityAreaCode}" showLevel="2"  disabled="true"/>
									    </c:if>
									
									</div>
									<input type="hidden" id="provinceCityAreaCode"  name="prpLScheduleTaskVo.provinceCityAreaCode" value="${prpLScheduleTaskVo.provinceCityAreaCode}" >
								</c:if>	
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-2">查勘片区</label>
									<div class="form_input col-3">
										<input type="text" class="input-text" id="checkareaName"  readOnly style="width:96%" name="prpLScheduleTaskVo.checkareaName" value="${prpLScheduleTaskVo.checkareaName}"/>
										<input type="hidden"  id="regionCode" style="width:96%" name= "prpLScheduleTaskVo.regionCode" value="${prpLScheduleTaskVo.regionCode}"/>
										<input type="hidden"  id="checkAddressMapCode" style="width:96%" name= "prpLScheduleTaskVo.checkAddressMapCode" value="${prpLScheduleTaskVo.checkAddressMapCode}"/>
										<font color="red">*</font>
									</div>
						<!-- 	<div class="form_input col-4 ml-20">
								<input type="button" class="btn btn-zd" onclick="openCheckMap()" value="电子地图" />
								<input type="button" value="自定义查勘员" onclick="openSelfDefineMap()" class="btn btn-zd">
								<div id="self" class="form_input col-3"></div>
							</div> -->
							<div class="form_input col-3 ml-15">
							<input type="button" id="switchMaps" class="btn btn-zd" onclick="openCheckMap()" value="电子地图" />
							<!-- <input type="button" value="自定义查勘员" onclick="openSelfDefineMap()" class="btn btn-zd"> -->
							
						</div>
						<div id="self" class="form_input col-3"></div>
						</div>
					<!-- <div class="row  cl">
						<label class="form_label col-2">查勘片区</label>
						<div class="form_input col-2">
							<span class="select-box" style="width: 95%">
								<select class="select">
									<option></option>
								</select>
							</span>
							<font color="red">*</font>
						</div>
						<div class="form_input col-4 ml-20">
							<a class="btn btn-zd">电子地图</a>
							<input type="button" value="自定义查勘员" id="define" onclick="selfDefineOrNot(1)" class="btn btn-zd">
							<input type="button" value="取消自定义" id="notDefine" onclick="selfDefineOrNot(0)" class="hide btn-zd">
						</div>
					</div> -->
					<%-- <div class="row cl hide" id="defineDiv">
						<label class="form_label col-2">查勘人员</label>
						<div class="form_input col-2">
							<!-- 
								<app:codeSelect	codeType="" dataSource="" id="lossParty" type="select"
									clazz="must" name="" value="" />
							-->
							<select class="input-text" >
								<option value="">${userName}</option>
							</select>
							<font color="red">*</font>
						</div>
						<label class="form_label col-1">人伤跟踪人员</label>
						<div class="form_input col-2">
							<!-- 
								<app:codeSelect	codeType="" dataSource="" id="lossParty" type="select"
									clazz="must" name="" value="" />
							-->
							<select class="input-text">
								<option value="">${userName}</option>
							</select>
							<font color="red">*</font>
						</div>
					</div> --%>
				</div>
				<div class="table_wrap">
		            	<div class="table_title f14">改派人员</div>
						<table class="table table-border table-bordered table-hover" cellpadding="0" cellspacing="0">
							<thead>
							 	<tr class="text-c">
							 	 	<th style="width:23%;">
							 	 	<div class="row  cl">
										 	 		<label class="form_label col-6">姓名	</label>
										 	 		<label class="form_label col-3 text-r">是否自定义</label>
										 	 		<div class="form_input col-1"><input type="checkbox" name="selfCheck"></div>
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
							<tbody id="checkBody">
							 	<tr class="text-c">
								 	<td width="15%" id="ploss1"><input  class="text-c input-text" value="" id="userName" readonly name="prpLScheduleTaskVo.scheduledUsername" type="text" datatype="*"/><input value="" id="userCode" name="prpLScheduleTaskVo.scheduledUsercode" type="hidden"/></td>
								 			<td width="15%">
											 	<input class="text-c input-text"  value="${prpLScheduleVo.relateHandlerName}" id="relateHandlerNames" type="text" readonly/>
											 	<input value="${prpLScheduleVo.relateHandlerName}" id="relateHandlerName" name="prpLScheduleTaskVo.relateHandlerName" type="hidden"/></td>
											 	<td width="15%">
											 	<input class="text-c input-text"  value="${prpLScheduleVo.relateHandlerMobile}" id="relateHandlerMobiles" type="text" onblur="setMobile()" />
											 	<input value="${prpLScheduleVo.relateHandlerMobile}" id="relateHandlerMobile" name="prpLScheduleTaskVo.relateHandlerMobile" type="hidden"/></td>
								 	<td width="15%"><input  class="text-c input-text" value="${mobile}" id="plossMoblie" readonly  type="text" />
								 	<td width="15%" id="ploss2"><input  class="text-c input-text" value="" id="comName" readonly name="prpLScheduleTaskVo.scheduledComname" type="text" datatype="*"/><input value="" id="comCode" name="prpLScheduleTaskVo.scheduledComcode" type="hidden"/></td>
								</tr>
						   </tbody>								
			   		   </table>
			   </div>
			</div>
		</div>
		<!-- 底部按钮 -->
		<br /> <br /> <br />
		<!-- 底部按钮 -->
		<div class="btn-footer clearfix text-c">
			<input class="btn btn-success" type="button" value="短信处理"></input>
			<input class="btn btn-primary btn-saveBtn" type="button" value="改派"></input>
			<input class="btn btn-success" onclick="goBack()" type="button" value="返回"></input>
		</div>
		<br /> <br /> <br /> <br />
	</div>
</form>
<script type="text/javascript">
$(function(){
	//判断地图有没有关，1为关闭,出险所在地可以手动录入
	if($("#switchMap").val()=="1"){
		$("#checkareaName").removeAttr("readonly");
		$('#switchMaps').attr("disabled",true); 
		$('#switchMaps').addClass("btn-disabled");
	}
	$("input.btn-saveBtn").click(function() {
		var rules = {
		};

		var ajaxEdit = new AjaxEdit($('#editform'));
		ajaxEdit.beforeCheck=function(){//校验之前
			
		};
		ajaxEdit.beforeSubmit=function(){//提交前补充操作

		};
		
		if($("input[name='reassignment']:checked").length == 0) {
			layer.alert("请选择需改派的任务。");
			return false;
		}
		//判断改派原因不为空CancelOrReassignCode
		if($("#CancelOrReassignCode").val()==""){
			layer.alert("请选择改派原因!");
			return false;
		}
		var id = $("input[name='reassignment']:checked").attr("id");
		var schType = $("input[name='reassignment']:checked").attr("schType");
		if(!isBlank(schType)){
			schType  = schType.replace(/\s+/g,"");
		}
		var taskId = $("input[name='reassignment']:checked").attr("flowtaskid");
		var checkAreaCode = $("#regionCode").val();
	    var lngXlatY = $("#checkAddressMapCode").val();
	    
	    //判断查勘自助案件没有接受前不能改派
	    var registNo = $("#registNo").val();
	    if(schType == "Check"  && (!checkAcceptSelfCase(registNo))){
	    	return false;
	    }
	    //end
		ajaxEdit.targetUrl = "/claimcar/schedule/reassignmentSubmit.do?id="+id+"&schType="+schType+"&flowTaskId="+taskId
				+"&checkAreaCode="+checkAreaCode+"&lngXlatY="+lngXlatY;
		ajaxEdit.rules =rules;
		ajaxEdit.afterSuccess=function(data){//操作成功后操作
			if(data.data != ""){
				layer.alert(data.data);
			}else{
				layer.confirm('调度改派成功', {
				    btn: ['确定'] //按钮
				}, function(){
					location.href = "/claimcar/schedule/preScheduleEdit.do?flowTaskId="+$("#flowTaskId").val() +"&registNo=" + $("#registNo").val();
				});
			}
		}; 
		//绑定表单
		ajaxEdit.bindForm();
		
		$("#editform").submit();
	});
});

function goBack(){
	location.href = "/claimcar/schedule/preScheduleEdit.do?flowTaskId="+$("#flowTaskId").val() +"&registNo=" + $("#registNo").val();
}

function openAddressMap(){
	var checkAddressCode = $("#checkAddressCode").val();
	var checkAddress = $("#checkAddress").val();
	var provinceCode = $("#checkAddressCode_lv1").val();
	var cityCode = $("#checkAddressCode_lv2").val();
	openMap('check',checkAddressCode,checkAddress,"Sched",provinceCode,cityCode);
}

//处理电子地图返回信息
function getPositionMapInfo(item,regionCode,damageAddress,lngXlatY) {
	$("#checkAddressCode").val(regionCode);
	$("#checkAddress").val(damageAddress);
	//$("#checkAddressMapCode").val(lngXlatY);
	getAllAreaInfos(regionCode,"appointedCheckAddressDiv","checkAddressCode",3,null,null);
}

function trim(str){
	return str.replace(/\s|\xA0/g,"");	
}
function openCheckMap() {
	
	var url = "/claimcar/manualSchedule/reassignmentScheduleOpenMap.ajax";
	
	var id = $("input[name='reassignment']:checked").attr("id");
	var schType = $("input[name='reassignment']:checked").attr("schType");
	if(typeof(schType) == "undefined"){
		layer.alert("请选择改派的定损任务");
		return;
	}
	schType = trim(schType);
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
			"ids"				: id,
			"checkAreaCode"	 	: checkAreaCode,
			"checkAddress"	    : checkAddress,
			"typeFlag"			: schType,
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
			// 调度改派电子地图请求中含有换行符无法正常打开电子地图，过滤掉
			if (url != undefined && url != null) {
				url = url.replace(/[\r\n]/g, "");
			}
			layer.open({
			    type: 2,
			    title:"电子地图",
			    area: ['98%', '98%'],
			    fix: true, //固定
			    maxmin: true,
			    content: url
			});
		}
	});
}


function getMapInfo(returnData){
	var nodeType = returnData.nodeType;
	
	html = html+"<input type='hidden'  value='' id='userName'  name='prpLScheduleTaskVo.scheduledUsername'/>"+
 	"<input value='' id='userCode' name='prpLScheduleTaskVo.scheduledUsercode' type='hidden'/>";
	$("#ploss1").append(html);
	if(nodeType == "Check"){
		
		 if(returnData.checkUserCode!=null && returnData.checkUserCode!=""){
			 	$("input[name='selfCheck']").prop("disabled",false);
			 	$("input[name='selfCheck']").attr("checked",false);
			 	$("#ploss1").empty();
				var html = "<input class='text-c input-text' type='text'  id='userName'  name='prpLScheduleTaskVo.scheduledUsername'/>"+
				 "<input value='' id='userCode' name='prpLScheduleTaskVo.scheduledUsercode' type='hidden'/>";
				 $("#ploss1").append(html);
		}
		 
		$("#comCode").val(returnData.checkComCode);
		$("#userCode").val(returnData.checkUserCode);
		$("#userName").val(returnData.checkUserName);
		$("#comName").val(returnData.checkComName);
	/* 	 $("#nextHandlerName option").each(function(){
				var userCodeNow = $(this).val();
				var userCodeNows = userCodeNow.split(",");
				if(userCodeNow!=""){
					if(userCodeNows[1]==returnData.checkUserCode){
						$(this).prop("selected",true);
					}
				}
			}); */
		 
		 	$("#selfDefinareaCode").val(returnData.selfDefinareaCode);
			$("#relateHandlerName").val(returnData.relateHandlerName);
			$("#relateHandlerMobile").val(returnData.relateHandlerMobile);
			$("#relateHandlerNames").val(returnData.relateHandlerName);
			$("#relateHandlerMobiles").val(returnData.relateHandlerMobile);
			$("#isComuserCode").val(returnData.isComuserCode);
	}else if (nodeType =="PLoss"){
		 if(returnData.pLossUserName!=null && returnData.pLossUserName!=""){
			 	$("input[name='selfCheck']").prop("disabled",false);
			 	$("input[name='selfCheck']").attr("checked",false);
			 	$("#ploss1").empty();
				var html = "<input class='text-c input-text' type='text'  id='userName'  name='prpLScheduleTaskVo.scheduledUsername'/>"+
				 "<input value='' id='userCode' name='prpLScheduleTaskVo.scheduledUsercode' type='hidden'/>";
				 $("#ploss1").append(html);
		}
		 
		$("#comCode").val(returnData.pLossComCode);
		$("#userCode").val(returnData.pLossUserCode);
		$("#userName").val(returnData.pLossUserName);
		$("#comName").val(returnData.pLossComName);
		/*  $("#nextHandlerName option").each(function(){
				var userCodeNow = $(this).val();
				var userCodeNows = userCodeNow.split(",");
				if(userCodeNow!=""){
					if(userCodeNows[1]==returnData.pLossUserCode){
						$(this).prop("selected",true);
					}
				}
			}); */
		 
		 $("#selfDefinareaCode").val(returnData.selfDefinareaCode);
			$("#relateHandlerName").val(returnData.prelateHandlerName);
			$("#relateHandlerMobile").val(returnData.prelateHandlerMobile);
			$("#relateHandlerNames").val(returnData.prelateHandlerName);
			$("#relateHandlerMobiles").val(returnData.prelateHandlerMobile);
			$("#isComuserCode").val(returnData.pisComuserCode);
	}else {
		 if(returnData.lossUserName!=null && returnData.lossUserName!=""){
			 	$("input[name='selfCheck']").prop("disabled",false);
			 	$("input[name='selfCheck']").attr("checked",false);
			 	$("#ploss1").empty();
				var html = "<input class='text-c input-text' type='text'  id='userName'  name='prpLScheduleTaskVo.scheduledUsername'/>"+
				 "<input value='' id='userCode' name='prpLScheduleTaskVo.scheduledUsercode' type='hidden'/>";
				 $("#ploss1").append(html);
		}
		 
		$("#comCode").val(returnData.lossComCode);
		$("#userCode").val(returnData.lossUserCode);
		$("#userName").val(returnData.lossUserName);
		$("#comName").val(returnData.lossComName);
		/*  $("#nextHandlerName option").each(function(){
				var userCodeNow = $(this).val();
				var userCodeNows = userCodeNow.split(",");
				if(userCodeNow!=""){
					if(userCodeNows[1]==returnData.lossUserCode){
						$(this).prop("selected",true);
					}
				}
			}); */
		
			
			$("#selfDefinareaCode").val(returnData.selfDefinareaCode);
			$("#relateHandlerName").val(returnData.lrelateHandlerName);
			$("#relateHandlerMobile").val(returnData.lrelateHandlerMobile);
			$("#relateHandlerNames").val(returnData.lrelateHandlerName);
			$("#relateHandlerMobiles").val(returnData.lrelateHandlerMobile);
			$("#isComuserCode").val(returnData.lisComuserCode);
	}
	$("#checkareaName").val(returnData.areaAddress);
	$("#regionCode").val(returnData.regionCode);
	
	getAllAreaInfo(returnData.cityCode,"checkAddressDiv","provinceCityAreaCode",2,null,'disabled',null);
}

//选择定损查勘改派  预定查勘地点约定定损地点联动
function changeCheckAddressLable(text){
	$("#checkAddressLable").empty();
	$("#checkAddressLable").append(text);
}
function addPayInfos(){
	alert("231");
	var str = "Hello World";
	// H
	alert(str.substr(0, 4)+"00");
}
/* $("#checkAddressCode_lv1").change(function(){
	alert("231");
}); */
/* $('#checkAddressCode').bind('input propertychange', function() {  
    alert("21312");
});  */
function getAllAreaInfos(lowerCode, areaId, targetElmId, showLevel, clazz,
		disabled) {
	var areaDiv = $("#" + areaId);
	$("#" + targetElmId).val(lowerCode);
	$.ajax({
		url : '/claimcar/areaSelect/getAllArea.do?areaCode=' + lowerCode
				+ '&showLevel=' + showLevel + '&targetElmId=' + targetElmId
				+ '&clazz=' + clazz + '&disabled=' + disabled,
		dataType : "json",// 返回json格式的数据
		type : 'get',
		success : function(json) {// json是后端传过来的值
			areaDiv.empty();
			$.each(json, function(index, item) {
				areaDiv.append(item.html);
				setCheckArea(lowerCode, areaId, targetElmId, showLevel, clazz,
						disabled);
			});
			
		},
		error : function() {
			layer.msg("获取地址数据异常");
		}
	});
}
function setCheckArea(lowerCode, areaId, targetElmId, showLevel, clazz,
		disabled){
	
	var checkAddressCode = $("#checkAddressCode").val();
	var provinceCityAreaCode = checkAddressCode.substr(0, 4)+"00";
	$("#provinceCityAreaCode").val(provinceCityAreaCode);
	var areaDiv = $("#checkAddressDiv");
	$.ajax({
		url : '/claimcar/areaSelect/getAllArea.do?areaCode=' + provinceCityAreaCode
				+ '&showLevel=2' + '&targetElmId=provinceCityAreaCode'
				+ '&clazz=' + clazz + '&disabled=' + disabled,
		dataType : "json",// 返回json格式的数据
		type : 'get',
		success : function(json) {// json是后端传过来的值
			areaDiv.empty();
			$.each(json, function(index, item) {
				areaDiv.append(item.html);
			});
			var checkAddress = $("#checkAddress").val();
			$("#checkareaName").val(checkAddress);
		},
		error : function() {
			layer.msg("获取地址数据异常");
		}
	});
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
	
	var schType = $("input[name='reassignment']:checked").attr("schType");
	if(schType=="PLoss"){
		schType = "PLoss";
	}else if(schType=="Check"){
		schType = "Check";
	}else{
		schType = "DLoss";
	}
		 var item = schType;
	
	var url = "/claimcar/manualSchedule/selfReassignMentOpenMap.ajax";
	var registNo = $("#registNo").val();
	var checkAreaCode = $("#regionCode").val();
	var checkAddress = $("#checkareaName").val();
	var lngXlatY = $("#checkAddressMapCode").val();
	var id = $("input[name='reassignment']:checked").attr("id");
	var selfDefinareaCode =$("#selfDefinareaCode").val();
	var params = {
			"registNo" 		 	: registNo,
			"checkAreaCode"	 	: checkAreaCode,
			"checkAddress"	    : checkAddress,
			"typeFlag"			: item,
			"lngXlatY"          : lngXlatY,
			"id"				:id,
			"selfDefinareaCode" :selfDefinareaCode
		};
	if($("input[name='reassignment']:checked").length == 0) {
		layer.alert("请选择需改派的任务。");
		$("input[name='selfCheck']").each(function(){
			   $(this).attr("checked",false);
			  }); 
		return false;
	}
	$.ajax({
		url : url,
		type : "post",
		data : params,
		async : false,
		success : function(htmlData) {
			$("input[name='selfCheck']").prop("disabled",true);
			$("#ploss1").empty();
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
			html = html+"<input type='hidden'  value='' id='userName'  name='prpLScheduleTaskVo.scheduledUsername'/>"+
		 	"<input value='' id='userCode' name='prpLScheduleTaskVo.scheduledUsercode' type='hidden'/>";
			$("#ploss1").append(html);
			
		}
	});
});
function selfschedule(){
	var nextHandlerName = $("#nextHandlerName").val();
	var nextName = nextHandlerName.split(",");
	//查勘员电话2取值员工维护表的电话（prpduser）
	$.ajax({
		url : '/claimcar/schedule/findSysUserVo.do?userCode='
				+ nextName[1],
		dataType : "json",// 返回json格式的数据
		type : 'get',
		success : function(json) {// json是后端传过来的值
			$("#plossMoblie").val(json.statusText);
		},
		error : function() {
			layer.msg("查勘获取查勘员电话2异常");
		}
	});
	$("#userName").val(nextName[0]);
	$("#comName").val(nextName[2]);
	$("#userCode").val(nextName[1]);
	$("#comCode").val(nextName[3]);
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
function setMobile(){
	var relateHandlerMobiles = $("#relateHandlerMobiles").val();
	$("#relateHandlerMobile").val(relateHandlerMobiles);
}
</script>
</body>
</html>