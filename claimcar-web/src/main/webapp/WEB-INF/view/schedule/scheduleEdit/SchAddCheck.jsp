<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<body>
	 	<!-- <div class="top_btn">
		 	 <a class="btn  btn-primary">参考信息</a>
		</div> -->
		<form action="#" id="editform" >
		<div class="fixedmargin page_wrap">
			<!-- 查勘调度任务  新增 开始 -->
			<div class="table_wrap">
            	<div class="table_title f14">查勘调度任务  新增</div>
				<div class="table_cont ">
					 <table class="table table-border table-bordered table-hover">
					 		<thead>
					 			<tr class="text-c">
					 				<th style="width:25%">选择</th>
					 				<th style="width:25%">损失项</th>
					 				<th style="width:25%">损失项内容</th>
					 				<th style="width:25%">操作</th>
					 			</tr>
					 		</thead>
					 		<tbody id="itemsTbody">
					 			<input type="hidden" id="itemSize" value="${fn:length(prpLScheduleItemses)}">
					 			<%@include file="AddItems.jsp" %>
					 		</tbody>
					 </table>
				</div>
			</div>
			<!--增加按钮 -->
		 	<div class="text-c">
		 	 	<button class="btn btn-primary btn-addBtn" style="display:none" onclick="addItem()" type="button">增&nbsp;&nbsp;加</button>
		 	</div><br />
		
			<!-- prpLScheduleTaskVo 隐藏域 开始 -->
			<input type="hidden" id="handlerstatus" value="${handlerstatus }" />
			<input name="prpLScheduleTaskVo.isAutoCheckFlag" type="hidden" id="isAutoCheckFlag" value="${isAutoCheckFlag }" />
			<input id="registNo" name="prpLScheduleTaskVo.registNo" value="${prpLScheduleTaskVo.registNo}" type="hidden" />
			<input name="prpLScheduleTaskVo.position" value="${prpLScheduleTaskVo.position}" type="hidden" />
			<input name="prpLScheduleTaskVo.mercyFlag" value="${prpLScheduleTaskVo.mercyFlag}" type="hidden" />
			<input name="prpLScheduleTaskVo.id" value="${prpLScheduleTaskVo.id}" type="hidden" />
			<input id="selfDefinareaCode" name="prpLScheduleTaskVo.selfDefinareaCode" value="${prpLScheduleVo.selfDefinareaCode}" type="hidden" />
			<input name="prpLScheduleTaskVo.isComuserCode" value="${prpLScheduleTaskVo.isComuserCode}" id="isComuserCode" type="hidden" />
			<input name="prpLScheduleTaskVo.personIsComuserCode" value="${prpLScheduleTaskVo.personIsComuserCode}" id="pisComuserCode" type="hidden" />
			<input name="switchMap" value="${switchMap}" id="switchMap" type="hidden" />
			<!-- prpLScheduleTaskVo 隐藏域 结束 -->
			
			<!-- 工作流参数隐藏域开始 -->
			<input id="flowId" type="hidden" name="submitVo.flowId" value="${flowId}" />
			<input id="flowTaskId" type="hidden" name="submitVo.flowTaskId" value="${flowTaskId}" />
			<input  type="hidden" name="submitVo.comCode" value="${comCode}" />
			<%-- <input   type="hidden" value="<app:codetrans  codeType="ComCodeLv2" codeCode="${comCode}"/>" /> --%>
			<!-- 工作流参数隐藏域开始 -->
			
			<!-- 其他隐藏域 开始 -->
			<input type="hidden" id="checkScheduled" value="${checkScheduled}" />
			<input type="hidden" id="checkPersonFinish" value="${checkPersonFinish}" />
			<input type="hidden" id="checkPersonFinishs" value="${checkPersonFinishs}" />
			<input name="oldClaim" value="${oldClaim}" id="oldClaim" type="hidden" />
			<!-- 其他隐藏域 结束 -->
			<div class="table_wrap">
				<div class="table_cont">
					<div class="formtable">
						<div class="row  cl">
							<label class="form_label col-2">联系人姓名</label>
							<div class="form_input col-2">
								<input type="hidden" name="prpLScheduleTaskVo.linkerMan" readonly="readonly" class="input-text" value="${prpLScheduleTaskVo.linkerMan}"/>
								${prpLScheduleTaskVo.linkerMan}
							</div>
							<label class="form_label col-1">联系人电话1</label>
							<div class="form_input col-2">
								<input type="hidden" class="input-text" readonly="readonly" name="prpLScheduleTaskVo.linkerManPhone" value="${prpLScheduleTaskVo.linkerManPhone}"/>
								${prpLScheduleTaskVo.linkerManPhone}
							</div>
							<label class="form_label col-1">联系人电话2</label>
							<div class="form_input col-2">
								<input type="hidden" class="input-text" readonly="readonly" name="prpLScheduleTaskVo.linkerManPhone2" value="${prpLScheduleTaskVo.linkerManPhone2}"/>
								${prpLScheduleTaskVo.linkerManPhone2}
							</div>
						</div>
						
						<!-- 添加是否自助查勘和是否自助报案单选按钮 -->
						<div class="row  cl">
							<label class="form_label col-2">是否自助查勘</label>
							<div class="form_input col-2">
								<span class="radio-box" style="padding-right:5px;"> <app:codeSelect
									codeType="IsAutoCheck" type="radio"
									value="${prpLScheduleTaskVo.isAutoCheck}"
									name="prpLScheduleTaskVo.isAutoCheck" />
								</span>
							</div>
						
							<label class="form_label col-1">是否自助报案</label>
							<div class="form_input col-2">
								<span class="radio-box" style="padding-right:5px;"> <app:codeSelect
									codeType="SelfRegistFlag" type="radio"
									value="${prpLRegistVo.selfRegistFlag}"
									name="prpLRegistVo.selfRegistFlag"/>
								</span>
							</div>
						</div>
						 <div class="row cl" id="LSTable">
						<div class="table_title f14">自助查勘人员信息</div>
						<div class="table_cont table_list" style="width:750px;">
							<table class="table table-border table-bordered table-hover"
								cellpadding="0" cellspacing="0">
								<thead>
									<tr class="text-c">
										<th>人员姓名</th>
										<th>人员电话</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="sysVo" items="${sysVos}" varStatus="status">
									    <tr class="text-c">
											<td>${sysVo.userName}</td>
											<td>${sysVo.linkPhone}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
                   </div>

                    </br>
						<div class="row  cl">
							<label class="form_label col-2">出险地点</label>
							<div class="form_input col-10">
								<app:codetrans  codeType="AreaCode" codeCode="${prpLScheduleTaskVo.damageAreaCode}"/>
								<input type="hidden" id="damageAreaCode"  name="prpLScheduleTaskVo.damageAreaCode" value="${prpLScheduleTaskVo.damageAreaCode}" >
								&nbsp;${prpLScheduleTaskVo.damageAddress}
							</div>
							<!-- <div class="form_input col-3"> -->
								<input type="hidden" name="prpLScheduleTaskVo.damageAddress" readonly="readonly" class="input-text" value="${prpLScheduleTaskVo.damageAddress}" placeholder="详细地址" />
							<!-- </div> -->
						</div>
						<div class="row cl">
							<label class="form_label col-2">约定查勘地点</label>
							<div class="form_input col-10">
								<app:codetrans  codeType="AreaCode" codeCode="${prpLScheduleTaskVo.checkorDeflossAreaCode}"/>
								<input type="hidden" id="checkorDeflossAreaCode"  name="prpLScheduleTaskVo.checkorDeflossAreaCode" value="${prpLScheduleTaskVo.checkorDeflossAreaCode}" >
								&nbsp;${prpLScheduleTaskVo.checkAddress}
								<input id="checkAddress" type="hidden" value="${prpLScheduleTaskVo.checkAddress}" />
							</div>
							<!-- <div class="form_input col-3"> -->
								<input type="hidden" class="input-text" name="prpLScheduleTaskVo.checkAddress" value="${prpLScheduleTaskVo.checkAddress}" placeholder="详细地址" />
							<!-- </div> -->
						</div>
						<div class="row cl">
							<label class="form_label col-2">查勘片区所在省/市</label>
							<div class="form_input col-5">
							   <div id="checkAddressDiv">
								<%-- <app:areaSelect targetElmId="provinceCityAreaCode" areaCode="${prpLScheduleTaskVo.provinceCityAreaCode}" showLevel="2"  disabled="true"/> --%>
								<c:if test="${switchMap == '1' }">
								<app:areaSelect targetElmId="switchMapProvinceCityAreaCode" areaCode="${prpLScheduleTaskVo.regionCode}" showLevel="3" handlerStatus="1" />
								</div>  
								<input type="hidden" id="switchMapProvinceCityAreaCode"  name="prpLScheduleTaskVo.provinceCityAreaCode" value="${prpLScheduleTaskVo.provinceCityAreaCode}" >
								</c:if>
								<c:if test="${switchMap != '1' }">
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
							<!-- <label class="form_label col-2">查勘片区归属机构</label>
							<div class="form_input col-3">
								<span class="select-box">
									<select class="select">
										<option></option>
									</select>
								</span>
							</div> -->
						</div>
						<div class="row  cl">
							<label class="form_label col-2">查勘片区</label>
									<div class="form_input col-3">
										<input type="text" class="input-text" id="checkareaName"  readOnly style="width:96%" name="prpLScheduleTaskVo.checkareaName" value="${prpLScheduleTaskVo.checkareaName}"/>
										<input type="hidden"  id="regionCode" style="width:96%" name= "prpLScheduleTaskVo.regionCode" value="${prpLScheduleTaskVo.regionCode}"/>
										<input type="hidden"  id="checkAddressMapCode" style="width:96%" name= "prpLScheduleTaskVo.checkAddressMapCode" value="${prpLScheduleTaskVo.checkAddressMapCode}"/>
										<font color="red">*</font>
									</div>
							<div class="form_input col-3 ml-15">
								<input type="button" id="switchMaps" class="btn btn-zd" onclick="openCheckMap('Check')" value="电子地图" />
								<!-- <input type="button" value="自定义查勘员" onclick="openSelfDefineMap()" class="btn btn-zd"> -->
								<!-- <input type="button" value="自定义查勘员" id="define" onclick="selfDefineOrNot(1)" class="btn btn-zd fl">
								<input type="button" value="取消自定义" id="notDefine" onclick="selfDefineOrNot(0)" class="hide btn-zd fl"> -->
							</div>
							<div id="self" class="form_input col-3"></div>
						</div>
						<%-- <div class="row cl" id="defineDiv">
						<label class="form_label col-2">查勘人员</label>
						<div class="form_input col-2">
							<!-- 
								<app:codeSelect	codeType="" dataSource="" id="lossParty" type="select"
									clazz="must" name="" value="" />
							-->
							<input value="${userName}" />
							<font color="red">*</font>
						</div>
						<label class="form_label col-1">人伤跟踪人员</label>
						<div class="form_input col-2">
							<!-- 
								<app:codeSelect	codeType="" dataSource="" id="lossParty" type="select"
									clazz="must" name="" value="" />
							-->
							<input value="${userName}" />
							<font color="red">*</font>
						</div>
					</div> --%>
					</div>
				</div>
			</div><br />
			<c:if test ="${checkScheduled eq '0'}">
			<c:forEach var="prpLScheduleItem" items="${prpLScheduleItemses}" varStatus="status">
			   <c:if test="${prpLScheduleItem.itemType eq '1'}"><!-- 默认以标的车的为准 -->
					<div class="table_wrap">
		            	<div class="table_title f14">查勘员</div>
		            	<input value="1" id="checkPerson" name="checkPerson" type="hidden"/>
									<table class="table table-border table-bordered table-hover" cellpadding="0" cellspacing="0">
										<thead>
										 	<tr class="text-c">
										 	 	<th style="width:23%;">
											 	 	<div class="row  cl">
											 	 		<label class="form_label col-6">姓名	</label>
											 	 		<label class="form_label col-3 text-r">是否自定义</label>
											 	 		<div class="form_input col-1"><input type="checkbox"  name="selfCheck" ></div>
											 	 	<!-- 	<label class="form_label col-3">关联查勘员：</label>
											 	 		
											 	 		<label class="form_label col-3 text-r">关联查勘员电话：</label> -->
											 	 		
											 	 	</div>	
										 	 	</th>
										 	 	<th style="width:20%;">关联查勘员</th>
										 	 	<th style="width:20%;">短信通知号码</th>
										 	 	<th style="width:20%;">电话通知号码</th>
										 	 	<th style="width:20%;">所属机构</th>
										 	</tr>
										</thead>
										<tbody id="checkBody">
										 	<tr class="text-c">
											 	<td width="15%" id="check1"><input  class="text-c input-text" value="<app:codetrans  codeType="UserCode" codeCode="${prpLScheduleItem.scheduledUsercode}"/>" id="userName" readonly name="prpLScheduleTaskVo.scheduledUsername" type="text" datatype="*"/>
											 	<input value="${prpLScheduleItem.scheduledUsercode}" id="userCode" name="prpLScheduleTaskVo.scheduledUsercode" type="hidden"/></td>
											 	<td width="15%">
											 	<input  class="text-c input-text"  value="${prpLScheduleVo.relateHandlerName}" id="relateHandlerNames" type="text" readonly/>
											 	<input value="${prpLScheduleVo.relateHandlerName}" id="relateHandlerName" name="prpLScheduleTaskVo.relateHandlerName" type="hidden"/></td>
											 	<td width="15%"><input class="text-c input-text"  value="${prpLScheduleVo.relateHandlerMobile}" id="relateHandlerMobiles" type="text" onblur="setMobile()" />
											 	<input value="${prpLScheduleVo.relateHandlerMobile}" id="relateHandlerMobile" name="prpLScheduleTaskVo.relateHandlerMobile" type="hidden"/></td>
											 	<td width="15%"><input  class="text-c input-text" value="${prpLScheduleItem.moblie}" id="checkMoblie" readonly  type="text" />
											 	<td width="15%" id="check2"><input  class="text-c input-text" value="<app:codetrans  codeType="ComCode" codeCode="${prpLScheduleItem.scheduledComcode}"/>" id="comName" readonly name="prpLScheduleTaskVo.scheduledComname" type="text" datatype="*"/>
											 	<input value="${prpLScheduleItem.scheduledComcode}" id="comCode" name="prpLScheduleTaskVo.scheduledComcode" type="hidden"/></td>
											</tr>   
									   </tbody>								
						   		</table>
					 	</div>
			 </c:if>
			  <c:if test="${prpLScheduleItem.itemType eq '4'}"><!-- 人伤 -->
					<div class="table_wrap">
		            	<div class="table_title f14">人伤跟踪员</div>
		            	<input value="1" id="personLoss" name="personLoss" type="hidden"/>
							<table class="table table-border table-bordered table-hover" cellpadding="0" cellspacing="0">
								<thead>
									 	<tr class="text-c">
									 	 	<th style="width:23%;">
										 	 	<div class="row  cl">
										 	 		<label class="form_label col-6">姓名	</label>
										 	 		<label class="form_label col-3 text-r">是否自定义</label>
										 	 		<div class="form_input col-1"><input type="checkbox" name="selfPloss"></div>
										 	 	</div>				
									 	 	</th>
									 	 	<th style="width:20%;">关联查勘员</th>
										 	<th style="width:20%;">短信通知号码</th>
										 	<th style="width:20%;">电话通知号码</th>
									 	 	<th style="width:20%;">所属机构</th>
									 	</tr>
										</thead>
										<tbody id="checkBody">
										 	<tr class="text-c">
											 	<td width="15%" id="ploss1"><input  class="text-c input-text" value="<app:codetrans  codeType="UserCode" codeCode="${prpLScheduleItem.scheduledUsercode}"/>" id="personUserName" readonly name="prpLScheduleTaskVo.personScheduledUsername" type="text" datatype="*"/>
											 	<input value="${prpLScheduleItem.scheduledUsercode}" id="personUserCode" name="prpLScheduleTaskVo.personScheduledUsercode" type="hidden"/></td>
											 	<td width="15%">
											 	<input class="text-c input-text"  value="${prpLScheduleVo.personRelateHandlerName}" id="personRelateHandlerNames" type="text" readonly/>
											 	<input value="${prpLScheduleVo.personRelateHandlerName}" id="personRelateHandlerName" name="prpLScheduleTaskVo.personRelateHandlerName" type="hidden"/></td>
											 	<td width="15%">
											 	<input class="text-c input-text"  value="${prpLScheduleVo.personRelateHandlerMobile}" id="personRelateHandlerMobiles" type="text" onblur="setPersonMobile()"/>
											 	<input value="${prpLScheduleVo.personRelateHandlerMobile}" id="personRelateHandlerMobile" name="prpLScheduleTaskVo.personRelateHandlerMobile" type="hidden"/></td>
											 	<td width="15%"><input  class="text-c input-text" value="${prpLScheduleItem.moblie}" id="plossMoblie" readonly  type="text" />
											 	<td width="15%" id="ploss2"><input  class="text-c input-text" value="<app:codetrans  codeType="ComCode" codeCode="${prpLScheduleItem.scheduledComcode}"/>" id="personComName" readonly name="prpLScheduleTaskVo.personScheduledComname" type="text" datatype="*"/>
											 	<input value="${prpLScheduleItem.scheduledComcode}" id="personComCode" name="prpLScheduleTaskVo.personScheduledComcode" type="hidden"/></td>
											</tr>
									   </tbody>							
				   		</table>
				 	</div>
				</c:if> 	
		 	</c:forEach>
		 	</c:if>
		 	<c:if test ="${checkScheduled eq '1'}">
		 	    <div class="table_wrap">
		            	<div class="table_title f14">人伤跟踪员</div>
		            	<input value="1" id="personLoss" name="personLoss" type="hidden"/>
									<table class="table table-border table-bordered table-hover" cellpadding="0" cellspacing="0">
										<thead>
										 	<tr class="text-c">
										 	 	<th style="width:23%;">
											 	 	<div class="row  cl">
											 	 		<label class="form_label col-6">姓名	</label>
											 	 		<label class="form_label col-3 text-r">是否自定义</label>
											 	 		<div class="form_input col-1"><input type="checkbox" name="selfPloss"></div>
											 	 	</div>	
										 	 	</th>
										 	 	<th style="width:20%;">关联查勘员</th>
										 		<th style="width:20%;">短信通知号码</th>
										 	 	<th style="width:20%;">电话通知号码</th>
										 	 	<th style="width:20%;">所属机构</th>
										 	</tr>
										</thead>
										<tbody id="checkBody">
										 	<tr class="text-c">
											 	<td width="15%" id="ploss1"><input  class="text-c input-text" value="" id="personUserName" readonly name="prpLScheduleTaskVo.personScheduledUsername" type="text" datatype="*"/>
											 	<input value="" id="personUserCode" name="prpLScheduleTaskVo.personScheduledUsercode" type="hidden"/></td>
											 	<td width="15%">
											 	<input  class="text-c input-text"  value="${prpLScheduleVo.personRelateHandlerName}" id="personRelateHandlerNames" type="text" readonly/>
											 	<input value="${prpLScheduleVo.personRelateHandlerName}" id="personRelateHandlerName" name="prpLScheduleTaskVo.personRelateHandlerName" type="hidden"/></td>
											 	<td width="15%">
											 	<input class="text-c input-text"  value="${prpLScheduleVo.personRelateHandlerMobile}" id="personRelateHandlerMobiles" type="text" onblur="setPersonMobile()"/>
											 	<input value="${prpLScheduleVo.personRelateHandlerMobile}" id="personRelateHandlerMobile" name="prpLScheduleTaskVo.personRelateHandlerMobile" type="hidden"/></td>
											 	<td width="15%"><input  class="text-c input-text" value="${prpLScheduleItem.moblie}" id="plossMoblie" readonly  type="text" />
											 	<td width="15%" id="ploss2"><input  class="text-c input-text" value="" id="personComName" readonly name="prpLScheduleTaskVo.personScheduledComname" type="text" datatype="*"/>
											 	<input value="" id="personComCode" name="prpLScheduleTaskVo.personScheduledComcode" type="hidden"/></td>
											</tr>
									   </tbody>								
						   		</table>
				</div>
		 	</c:if>
 			<!-- 底部按钮 -->
 			<br/><br/><br/>
			<!-- 底部按钮 -->
			<div class="btn-footer clearfix text-c">
				<input class="btn btn-primary ml-10" type="button" value="短信处理"></input> 
				<input class="btn btn-primary btn-saveBtn" type="button" value="提交"></input>
				<input class="btn btn-disabled" id="submits" type="button" value="提交"></input>
				<input class="btn btn-primary ml-10" onclick="goBack()" type="button" value="返回"></input>
			</div>
			<br/><br/><br/><br/>
		</div>
		</form>
		
		<script type="text/javascript">
	
		//$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
		$(function(){
			var checkValue=$("input[name='prpLScheduleTaskVo.isAutoCheck']:checked").val();
			if(checkValue=='0'){
				$("#LSTable").addClass("hide");
			}else{
				$("#LSTable").removeClass("hide");
			}
			
			//判断地图有没有关，1为关闭,出险所在地可以手动录入
			if($("#switchMap").val()=="1"){
				$("#checkareaName").removeAttr("readonly");
				$('#switchMaps').attr("disabled",true); 
				$('#switchMaps').addClass("btn-disabled");
			}
			//控制增加按钮的是否可以点击
			if ($("#checkScheduled").val() == "1") {
				$("button.btn-addBtn").show();
			}
			$("#submits").hide();
			//查勘调度提交
			$("input.btn-saveBtn").click(function() {
				var checkAreaCode = $("#regionCode").val();
			    var lngXlatY = $("#checkAddressMapCode").val();
				var rules = {
				};
				
				//判断是否为空人员
				var personLoss = $("#personLoss").val();
				var checkPerson = $("#checkPerson").val();
				if(checkPerson=="1"){
					if($("#userName").val()==""){
						layer.alert("请选择查勘员！");
						return;
					}
				}
			 	
				if(personLoss=="1"){
					if($("#personUserName").val()==""){
						layer.alert("请选择人伤跟踪员！");
						return;
					}
				}

				var ajaxEdit = new AjaxEdit($('#editform'));
				ajaxEdit.beforeCheck=function(){//校验之前
					
				};
				ajaxEdit.beforeSubmit=function(){//提交前校验
					
				}
		
				ajaxEdit.targetUrl = "/claimcar/schedule/checkSave.do?checkAreaCode="+checkAreaCode+"&lngXlatY="+lngXlatY;
				ajaxEdit.rules =rules;
				ajaxEdit.afterSuccess=function(data){//操作成功后操作
					layer.confirm('查勘调度成功', {
					    btn: ['确定'] //按钮
					}, function(index){
						layer.close(index);
						location.href = "/claimcar/schedule/preScheduleEdit.do?flowTaskId="+$("#flowTaskId").val() +"&registNo=" + $("#registNo").val();
					});
				}; 
				//绑定表单
				ajaxEdit.bindForm();
				
				var policyNo = "";
				var selectedIds = getSelectedIds();
				if(selectedIds=="") {
					layer.alert("请选择需要调度的损失项");
					return false;
				}
				var idArray = selectedIds.split(",");
				for (var i = 0; i < idArray.length; i++) {
					var id = idArray[i];
					var idA=id.split("&");
					policyNo = policyNo + idA[0] + ",";
				}
				var flag =saveBeforeCheck();
				if(flag){
					$("#editform").submit();
					layer.load(0, {shade : [0.8, '#393D49']});
					$("input.btn-saveBtn").hide();
					$("#submits").show();
				}else{
					layer.alert("此为快赔案件，请调度给快赔人员！");
				}
			});
			//自助理赔，报案单选按钮不可选和提交后不可选
			$("input[name='prpLRegistVo.selfRegistFlag']").attr("disabled",true);
			var isAutoCheckFlag=$("#isAutoCheckFlag").val();
			if(isAutoCheckFlag=='1'){
				$("input[name='prpLScheduleTaskVo.isAutoCheck']").prop("disabled",true);
			}
		});
		
		function saveBeforeCheck(){
			var flag = true;
			var registNo = $("#registNo").val();
			var userCode = $("#userCode").val();
			var params = {
					"registNo" : registNo,
					"userCode" : userCode
				};
			$.ajax({
				url : "/claimcar/schedule/isQuickUser.ajax",
				type : "post",
				data : params,
				async : false,
				success : function(result){
					if(result.data=="0"){
						flag=false;
					}
				}
			});
			return flag;		
		}
		
		
		function goBack(){
			location.href = "/claimcar/schedule/preScheduleEdit.do?flowTaskId="+$("#flowTaskId").val() +"&registNo=" + $("#registNo").val();
		}
		//checkbox勾选联动无需调度按钮
		function checkedOrNot(element, index) {
			var flag = 0;
			if ($(element).prop("checked")) {
				flag = 1;
			} else {
				flag = 0;
			}
			scheduleOrNot(flag, index);
		}
		//无需调度按钮
		function scheduleOrNot(flag,index){
			if (flag == 0) {
				$("#without"+index).addClass('hide');
				$("#recover"+index).removeClass('hide');
				$("#without"+index).removeClass('btn');
				$("#recover"+index).addClass('btn');
				$("[id='" + index + "']:checkbox").prop("checked", false);
				$("input[name='prpLScheduleItemses["+index+"].scheduleStatus']").val(8);
			} else {
				$("#without"+index).removeClass('hide');
				$("#recover"+index).addClass('hide');
				$("#without"+index).addClass('btn');
				$("#recover"+index).removeClass('btn');
				$("[id='" + index + "']:checkbox").prop("checked", true);
				$("input[name='prpLScheduleItemses["+index+"].scheduleStatus']").val(3);
			}
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
		
		//新增损失项
		function addItem() {
			//新增前先校验
			if ($("#checkPersonFinishs").val() == "0") {
				$("button.btn-addBtn").removeClass('btn-primary');
				$("button.btn-addBtn").addClass('btn-disabled');
				$("button.btn-addBtn").prop("disabled",true);
				layer.alert("查勘、定损、单证收集环节标为互陪自赔案件，不允许调度人伤任务");
			} else {
				if ($("#checkPersonFinish").val() == "1") {
					var $tbody = $("#itemsTbody");
					var itemSize = $("#itemSize").val();// 损失项条数
					var params = {
						"itemSize" : itemSize,
					};
					var url = "/claimcar/schedule/addItem.ajax";
					$.post(url, params, function(result) {
						$tbody.append(result);
						$("#itemSize").val(itemSize + 1);
						$("button.btn-addBtn").removeClass('btn-primary');
						$("button.btn-addBtn").addClass('btn-disabled');
						$("button.btn-addBtn").prop("disabled",true);
					});
				} else {
					$("button.btn-addBtn").removeClass('btn-primary');
					$("button.btn-addBtn").addClass('btn-disabled');
					$("button.btn-addBtn").prop("disabled",true);
					layer.alert("存在未核赔通过的人伤跟踪任务，不能再次发起人伤跟踪任务。");
				}
			}
		}
		
		//删除损失项
		function delItem(element) {
			var $parentTr = $(element).parent().parent();
			var itemSize = $("#itemSize").val();// 损失项条数
			$("#itemSize").val(itemSize - 1);// 删除一条
			$parentTr.remove();
			$("button.btn-addBtn").addClass('btn-primary');
			$("button.btn-addBtn").removeClass('btn-disabled');
			$("button.btn-addBtn").prop("disabled",false);
		}
		
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
		
		function openCheckMap(item) {
			
			var url = "/claimcar/manualSchedule/checkScheduleOpenMap.ajax";
			
			var ids = "";
			
			$("input[name='checkCode']:checked").each(function() {
				ids = ids + $(this).attr("itemId") + ",";
			});
			if (ids != "") {
				ids = ids.substr(0, ids.length - 1);
			}else{
				layer.alert("请选择调度任务");
				return false;
			}
			
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
					"ids"				: ids,
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
					// 调度查勘任务电子地图请求中含有换行符无法正常打开电子地图，过滤掉
					if (url != undefined && url != null) {
						url = url.replace(/[\r\n]/g, "");
					}
					//alert(url);
					//var newWindow = window.showModalDialog("http://IP:7001/mclaim/encodeForClaimServlet?xml=" + xml,"","dialogWidth=1024px;dialogHeight=768px;resizable=yes;");
					layer.open({
					    type: 2,
					    title:'电子地图',
					    area: ['98%', '98%'],
					    fix: true, //固定
					    maxmin: true,
					    content: url//"http://IP:7001/mclaim/encodeForClaimServlet?xml=" + xml
					});
				}
			});
		}
		
 
		function getMapInfo(returnData){
			//赋值
			/*  $("#checkHandlerName option").each(function(){
				var userCodeNow = $(this).val();
				var userCodeNows = userCodeNow.split(",");
				if(userCodeNow!=""){
					if(userCodeNows[1]==returnData.checkUserCode){
						$(this).prop("selected",true);
					}
				}
			});
			
			 $("#plossHandlerName option").each(function(){
					var pLossUserCode = $(this).val();
					var pLossUserCodes = pLossUserCode.split(",");
					if(pLossUserCode!=""){
						if(pLossUserCodes[1]==returnData.pLossUserCode){
							$(this).prop("selected",true);
						}
					}
				}); */
			 
			 if(returnData.checkUserCode!=null&&returnData.checkUserCode!=""){
				$("input[name='selfCheck']").prop("disabled",false);
				$("input[name='selfCheck']").attr("checked",false);
				 $("#check1").empty();
				 html = "<input class='text-c input-text' type='text'  value='' id='userName'  name='prpLScheduleTaskVo.scheduledUsername'/>"+
				 	"<input value='' id='userCode' name='prpLScheduleTaskVo.scheduledUsercode' type='hidden'/>"+
				 	"<input value='' id='callNumber' name='prpLScheduleTaskVo.callNumber' type='hidden'/>";
				 $("#check1").append(html);
				//呼叫电话
				$("#callNumber").val(returnData.callNumber);
			 }
			 
			 
			 if(returnData.pLossUserCode!=null&&returnData.pLossUserCode!=""){
				 	$("input[name='selfPloss']").prop("disabled",false);
				 	$("input[name='selfPloss']").attr("checked",false);
				 	$("#ploss1").empty();
					var html = "<input class='text-c input-text' type='text'  value='' id='personUserName'  name='prpLScheduleTaskVo.personScheduledUsername'/>"+
				 	"<input value='' id='personUserCode' name='prpLScheduleTaskVo.personScheduledUsercode' type='hidden'/>";
					$("#ploss1").append(html);
			}
			$("#checkareaName").val(returnData.areaAddress);
			$("#regionCode").val(returnData.regionCode);
			$("#comCode").val(returnData.checkComCode);
			$("#userCode").val(returnData.checkUserCode);
			$("#userName").val(returnData.checkUserName);
			$("#comName").val(returnData.checkComName);
			$("#personComCode").val(returnData.pLossComCode);
			$("#personUserCode").val(returnData.pLossUserCode);
			$("#personUserName").val(returnData.pLossUserName);
			$("#personComName").val(returnData.pLossComName);
			
			$("#selfDefinareaCode").val(returnData.selfDefinareaCode);
			$("#relateHandlerName").val(returnData.relateHandlerName);
			$("#relateHandlerMobile").val(returnData.relateHandlerMobile);
			$("#relateHandlerNames").val(returnData.relateHandlerName);
			$("#relateHandlerMobiles").val(returnData.relateHandlerMobile);
			$("#isComuserCode").val(returnData.isComuserCode);
			
			$("#personRelateHandlerName").val(returnData.prelateHandlerName);
			$("#personRelateHandlerMobile").val(returnData.prelateHandlerMobile);
			$("#personRelateHandlerNames").val(returnData.prelateHandlerName);
			$("#personRelateHandlerMobiles").val(returnData.prelateHandlerMobile);
			$("#pisComuserCode").val(returnData.pisComuserCode);
			
			//var lselfDefinAreaCode = $("#lselfDefinAreaCode").val();
			/* var lrelateHandlerName = $("#lrelateHandlerName").val();
			var lrelateHandlerMobile = $("#lrelateHandlerMobile").val();
			var lisComuserCode = $("#lisComuserCode").val(); */
			getAllAreaInfo(returnData.cityCode,"checkAddressDiv","provinceCityAreaCode",2,null,'disabled',null);
		}

		function selfschedule(a){
			var personLoss = $("#personLoss").val();
			var checkPerson = $("#checkPerson").val();
			var plossHandlerName = $("#plossHandlerName").val();
			var checkHandlerName = $("#checkHandlerName").val();
			
			
			if(a=="1"){
				if(checkPerson=="1"){
					var checkName = checkHandlerName.split(",");
					if(checkHandlerName!=""){
						if(checkName[4]=="Check"){
							//查勘员电话2取值员工维护表的电话（prpduser）
							$.ajax({
								url : '/claimcar/schedule/findSysUserVo.do?userCode='
										+ checkName[1],
								dataType : "json",// 返回json格式的数据
								type : 'get',
								success : function(json) {// json是后端传过来的值
									$("#checkMoblie").val(json.statusText);
								},
								error : function() {
									layer.msg("查勘获取查勘员电话2异常");
								}
							});
							$("#userName").val(checkName[0]);
							$("#comName").val(checkName[2]);
							$("#userCode").val(checkName[1]);
							$("#comCode").val(checkName[3]);
							if(checkName[5]!='null'){
								$("#relateHandlerName").val(checkName[5]);
								$("#relateHandlerNames").val(checkName[5]);
							}
							if(checkName[6]!='null'){
								$("#relateHandlerMobile").val(checkName[6]);
								$("#relateHandlerMobiles").val(checkName[6]);
							}
							$("#isComuserCode").val(checkName[7]);
							//呼出电话
							$("#callNumber").val(checkName[8]);
						}
					}
				}
			}
			
		 	if(a=="2"){
		 		if(personLoss=="1"){
		 			var plossName = plossHandlerName.split(",");
					if(plossHandlerName!=""){
						if(plossName[4]=="PLoss"){
							//查勘员电话2取值员工维护表的电话（prpduser）
							$.ajax({
								url : '/claimcar/schedule/findSysUserVo.do?userCode='
										+ plossName[1],
								dataType : "json",// 返回json格式的数据
								type : 'get',
								success : function(json) {// json是后端传过来的值
									$("#plossMoblie").val(json.statusText);
								},
								error : function() {
									layer.msg("人伤获取查勘员电话2异常");
								}
							});
							$("#personUserName").val(plossName[0]);
							$("#personComName").val(plossName[2]);
							$("#personUserCode").val(plossName[1]);
							$("#personComCode").val(plossName[3]);
							if(plossName[5]!='null'){
								$("#personRelateHandlerName").val(plossName[5]);
								$("#personRelateHandlerNames").val(plossName[5]);
							}
							if(plossName[6]!='null'){
							$("#personRelateHandlerMobile").val(plossName[6]);
							
							$("#personRelateHandlerMobiles").val(plossName[6]);
							}
							$("#pisComuserCode").val(plossName[7]);
							//呼出电话
							//$("#callNumber").val(checkName[8]);
						}
					}
				}
		 	}
		
			
		}
		
		$("input[name='selfPloss']").change(function() {
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
				$("input[name='selfPloss']").each(function(){
					   $(this).attr("checked",false);
					  }); 
				return false;
			}
		            var item = 'DLCar';
		        	var registNo = $("#registNo").val();
					var checkAreaCode = $("#regionCode").val();
					var checkAddress = $("#checkareaName").val();
					var lngXlatY = $("#checkAddressMapCode").val();
					var editform = $('#editform').serialize();
					var url = "/claimcar/manualSchedule/selfDefineOpenMaps.ajax";
					
					var ids = "";
				
					var params = {
							"registNo" 		 	: registNo,
							"checkAreaCode"	 	: checkAreaCode,
							"checkAddress"	    : checkAddress,
							"typeFlag"			: item,
							"lngXlatY"          : lngXlatY,
							"lngXlatY":editform
						};
					
					$.ajax({
						url : url,
						type : "post",
						data : editform,
						async : false,
						success : function(htmlData) {
							$("input[name='selfPloss']").prop("disabled",true);
							var ploss1=document.getElementById("ploss1");
							var check1=document.getElementById("check1");
							if(ploss1){
								$("#ploss1").empty();
								var scheduleItemList = htmlData.data;
								html = "<select id='plossHandlerName' name='nextHandlerName' onchange='selfschedule(2)' class='text-c  must select'>";
								html = html+"<option value=''></option>";
								for( var i = 0; i < scheduleItemList.length; i++){
									if(scheduleItemList[i].nodeType=="PLoss"){
									var nodeType = scheduleItemList[i].nodeType;
									var name = scheduleItemList[i].nextHandlerName;
									var nextHandlerCode = scheduleItemList[i].nextHandlerCode;
									var scheduleObjectId = scheduleItemList[i].scheduleObjectId;
									var scheduleObjectName = scheduleItemList[i].scheduleObjectName;
									
									var isComuserCode = scheduleItemList[i].isComuserCode;
									var relateHandlerName = scheduleItemList[i].relateHandlerName;
									var relateHandlerMobile = scheduleItemList[i].relateHandlerMobile;
									//呼出电话
									html = html+"<option class='text-c' value='"+name+","+nextHandlerCode+","+scheduleObjectName+","+scheduleObjectId+","+nodeType
									+","+relateHandlerName+","+relateHandlerMobile+","+isComuserCode+"'>"+name+"</option>";
									}
								}
								
								
								html = html+"</select>";
								html = html+"<input type='hidden'  value='' id='personUserName'  name='prpLScheduleTaskVo.personScheduledUsername'/>"+
							 	"<input value='' id='personUserCode' name='prpLScheduleTaskVo.personScheduledUsercode' type='hidden'/>";
								$("#ploss1").append(html);
							}
							/* if(check1){
								$("#check1").empty();
								var scheduleItemList = htmlData.data;
								html = "<select id='nextHandlerName' name='nextHandlerName' onchange='selfschedule()' class='  must select'>";
								html = html+"<option value=''></option>";
								for( var i = 0; i < scheduleItemList.length; i++){
									if(scheduleItemList[i].nodeType=="Check"){
									var nodeType = scheduleItemList[i].nodeType;
									var name = scheduleItemList[i].nextHandlerName;
									var nextHandlerCode = scheduleItemList[i].nextHandlerCode;
									var scheduleObjectId = scheduleItemList[i].scheduleObjectId;
									var scheduleObjectName = scheduleItemList[i].scheduleObjectName;
									
									html = html+"<option value='"+name+","+nextHandlerCode+","+scheduleObjectName+","+scheduleObjectId+","+nodeType+"'>"+name+"</option>";
									}
								}
								
								
								html = html+"</select>";
								$("#check1").append(html);
							} */
							
						}});
		});
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
					//alert(json.statusText);
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
							var check1=document.getElementById("check1");
							if(check1){
								$("#check1").empty();
								var scheduleItemList = htmlData.data;
								html = "<select id='checkHandlerName' name='nextHandlerName' onchange='selfschedule(1)' class='text-c  must select'>";
								html = html+"<option value=''></option>";
								for( var i = 0; i < scheduleItemList.length; i++){
									if(scheduleItemList[i].nodeType=="Check"){
									var nodeType = scheduleItemList[i].nodeType;
									var name = scheduleItemList[i].nextHandlerName;
									var nextHandlerCode = scheduleItemList[i].nextHandlerCode;
									var scheduleObjectId = scheduleItemList[i].scheduleObjectId;
									var scheduleObjectName = scheduleItemList[i].scheduleObjectName;
									
									var isComuserCode = scheduleItemList[i].isComuserCode;
									var relateHandlerName = scheduleItemList[i].relateHandlerName;
									var relateHandlerMobile = scheduleItemList[i].relateHandlerMobile;
									//呼出电话
									var callNumber = scheduleItemList[i].callNumber;
									html = html+"<option class='text-c' value='"+name+","+nextHandlerCode+","+scheduleObjectName+","
									+scheduleObjectId+","+nodeType+","+relateHandlerName+","+relateHandlerMobile+","+isComuserCode+","+callNumber+"'>"+name+"</option>";
									}
								}
								
								
								html = html+"</select>";
								html = html+"<input type='hidden'  value='' id='userName'  name='prpLScheduleTaskVo.scheduledUsername'/>"+
							 	"<input value='' id='userCode' name='prpLScheduleTaskVo.scheduledUsercode' type='hidden'/>"+
							 	"<input value='' id='callNumber' name='prpLScheduleTaskVo.callNumber' type='hidden'/>";
								$("#check1").append(html);
							}
							
							
						}});
		});
		
		function setMobile(){
			var relateHandlerMobiles = $("#relateHandlerMobiles").val();
			$("#relateHandlerMobile").val(relateHandlerMobiles);
		}
		function setPersonMobile(){
			var personRelateHandlerMobiles = $("#personRelateHandlerMobiles").val();
			$("#personRelateHandlerMobile").val(personRelateHandlerMobiles);
		}
		
		//合计人伤数量
		function countSum() {
			var injCountTag = parseInt($("#injCountTag").val());
			var deaCountTag = parseInt($("#deaCountTag").val());
			var injCountThird = parseInt($("#injCountThird").val());
			var deaCountThird = parseInt($("#deaCountThird").val());
			
			if(isNaN(injCountTag) || isNaN(deaCountTag)){//标的
				layer.alert("标的人亡不能为空！");
				return;
			}
			if(isNaN(injCountThird) || isNaN(deaCountThird)){//标的
				layer.alert("三者人亡不能为空！");
				return;
			}
			$("#injuredSum").val(injCountTag + injCountThird);
			$("#deathSum").val(deaCountTag + deaCountThird);
			
		}
		$("input[name='prpLScheduleTaskVo.isAutoCheck']").change(function(){
			var checkValue=$("input[name='prpLScheduleTaskVo.isAutoCheck']:checked").val();
			if(checkValue=='0'){
				$("#LSTable").addClass("hide");
			}else{
				$("#LSTable").removeClass("hide");
			}
		});
		</script>
	</body>
</html>