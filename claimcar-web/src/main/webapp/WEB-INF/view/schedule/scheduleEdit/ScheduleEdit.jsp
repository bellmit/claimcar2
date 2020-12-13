<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>查勘登记</title>
</head>
<body>
	<%-- <%@include file="ScheduleEdit_RiskDialog.jspf"%> --%>
	<div class="top_btn">
		<a class="btn  btn-primary"  onclick="viewEndorseInfo('${prpLRegistVo.registNo}')">保单批改记录</a>
		<a class="btn  btn-primary" id="checkRegistMsg">案件备注</a>
		<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
		<a class="btn  btn-primary"  onclick="warnView('${prpLRegistVo.registNo}')">山东预警推送</a>
		<!-- <a class="btn  btn-primary" id="CertiSend">索赔单证发送</a> -->
		<!-- <a class="btn  btn-primary">注销/拒赔申请</a>
		<a class="btn  btn-primary">单证打印</a> -->
	</div>
	<div class="fixedmargin page_wrap">
		<!-- 保单信息开始 -->
		<div class="table_wrap">
			<div class="table_title f14">保单信息</div>
			<div class="table_cont table_list">
				<table class="table table-border table-hover">
					<thead class="text-c">
						<tr>
							<th>保单号</th>
							<th>车牌号</th>
							<th>车型名称</th>
							<th>被保险人</th>
							<th>客户等级</th>
							<th>业务板块</th>
							<th>业务分类</th>
							<th>起保日期</th>
							<th>终保日期</th>
							<th>承保机构</th>
							<th>保单类型</th>
							<th style="width: 10%">代抄单打印</th>
						</tr>
					</thead>
					<tbody class="text-c">
						<c:forEach var="prpLCMain" items="${prpLCMains}"
							varStatus="status">
							<input type="hidden" id="comCode" value="${prpLCMain.comCode}" />
							<input type="hidden" id="powerFlag" value="${powerFlag}" />
							<tr>
								<td><a id="policyNo_${status.index }" data-hasqtip="0"
									href="/claimcar/policyView/policyView.do?policyNo=${prpLCMain.policyNo }"
									target="_blank">${prpLCMain.policyNo}</a></td>
								<td>${prpLCMain.prpCItemCars[0].licenseNo}</td>
								<td>${prpLCMain.prpCItemCars[0].brandName}</td>
								<%-- <td>${prpLCMain.prpCInsureds[0].insuredName}</td> --%>
								<c:forEach var="prpCInsured" items="${prpLCMain.prpCInsureds}" varStatus="status">
					 					<c:if test="${prpCInsured.insuredFlag eq '1'}">
					 						<td>${prpCInsured.insuredName}</td>
					 					</c:if>
				 				</c:forEach>
								<td>vip</td>
								<td><app:codetrans codeType="BusinessPlate"
										codeCode="${prpLCMain.businessPlate}" /></td>
								<td><font
									<c:if test="${prpLCMain.memberFlag=='1'}">class="c-red"</c:if >>
										<app:codetrans codeType="BusinessClass"
											codeCode="${prpLCMain.businessClass}" />
								</font></td>
								<td><fmt:formatDate value="${prpLCMain.startDate}"
										pattern="yyyy-MM-dd" /></td>
								<td><fmt:formatDate value="${prpLCMain.endDate}"
										pattern="yyyy-MM-dd" /></td>
								<td><app:codetrans codeType="ComCode"
										codeCode="${prpLCMain.comCode}" /></td>
								<td><c:choose>
										<c:when test="${prpLCMain.riskCode eq '1101'}">
								    		交强
								       </c:when>
										<c:otherwise>
								       		商业
								       </c:otherwise>
									</c:choose></td>
								<td><input value="打印" style="width: 70%"
									class="btn btn-primary" type="button"
									onclick="reportPrint('${prpLRegistVo.registNo}')" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<!-- 保单信息结束 -->

		<!-- 报案信息开始 -->
		<div class="table_wrap">
			<div class="table_title f14">报案信息</div>
			<div class="table_cont ">
				<div class="formtable">
					<div class="row  cl">
						<label class="form_label col-1">报案号</label>
						<div class="form_input col-3">
							${prpLRegistVo.registNo} <input type="hidden" id="registNo"
								value="${prpLRegistVo.registNo}" />
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-1">报案人</label>
						<div class="form_input col-2">${prpLRegistVo.reportorName}</div>
						<label class="form_label col-1">报案人电话</label>
						<div class="form_input col-2">${prpLRegistVo.reportorPhone}
						</div>
						<label class="form_label col-2">报案人与被保险人关系</label>
						<div class="form_input col-2">
							<span class="form_input"> <app:codetrans
									codeType="InsuredIdentity"
									codeCode="${prpLRegistVo.reportorRelation}" />
							</span>
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-1">联系人</label>
						<div class="form_input col-2">${prpLRegistVo.linkerName}</div>
						<label class="form_label col-1">联系人电话1</label>
						<div class="form_input col-2">${prpLRegistVo.linkerMobile}</div>
						<label class="form_label col-2">联系人电话2</label>
						<div class="form_input col-2">${prpLRegistVo.linkerPhone}</div>
					</div>
					<div class="row cl">
						<label class="form_label col-1">报案时间</label>
						<div class="form_input col-2">
							<fmt:formatDate value="${prpLRegistVo.reportTime}"
								pattern="yyyy-MM-dd HH:mm:ss" />
						</div>
						<label class="form_label col-1">出险时间</label>
						<div class="form_input col-2">
							<fmt:formatDate value="${prpLRegistVo.damageTime}"
								pattern="yyyy-MM-dd HH:mm:ss" />
						</div>
						<label class="form_label col-2">出险原因</label>
						<div class="form_input col-2">
							<span class="form_input"> <app:codetrans
									codeType="DamageCode" codeCode="${prpLRegistVo.damageCode}" />
							</span>
						</div>
					</div>
					<div class="row cl">
						<label class="form_label col-1">互碰自赔</label>
						<div class="form_input col-2">
							<app:codetrans codeType="YN10"
								codeCode="${prpLRegistExtVo.isClaimSelf}" />
						</div>
						<label class="form_label col-1">案件紧急类型</label>
						<div class="form_input col-2">
							<span class="form_input"> <app:codetrans
									codeType="MercyFlag" codeCode="${prpLRegistVo.mercyFlag}" />
							</span>
						</div>
						<label class="form_label col-2">出险地点</label>
						<div class="form_input col-4">${prpLRegistVo.damageAddress}
						</div>
					</div>

					<!-- 报案页面加上是否自助报案和是否自助查勘 -->
					<div class="row cl">
						<label class="form_label col-1">是否自助查勘</label>
						<div class="form_input col-2">${prpLScheduleTaskVo.isAutoCheck == '0'?'否':'是'}</div>
						<input type="hidden" id="isAutoCheck" value="${prpLScheduleTaskVo.isAutoCheck}" />
						
						<label class="form_label col-1">是否自助报案</label>  
						<div class="form_input col-2">${prpLRegistVo.selfRegistFlag == '0'?'否':'是'}</div>
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
					<div class="row cl">
						<label class="form_label fl">出险经过说明</label>
						<div class="form_input col-9">
							<textarea class="textarea" readonly="readonly">${prpLRegistExtVo.dangerRemark}</textarea>
							<br />
						</div>
					</div>
					<div class="row cl">
						<div class="form_input col-5">
							<br />
						</div>
					</div>
					<div class="row cl">
						<div class="form_input col-5">
							<br />
						</div>
					</div>
				</div>
			</div>
		</div>
		<br />
		<!-- 报案信息结束 -->

		<!-- 工作流参数隐藏域开始 -->
		<input type="hidden" id="flowId" name="submitVo.flowId"
			value="${flowId}" /> <input type="hidden" id="flowTaskId"
			name="submitVo.flowTaskId" value="${flowTaskId}" /> <input
			type="hidden" id="nodeCode" name="nodeCode" value="${nodeCode}">
		<input type="hidden" id="registNo" value="${prpLRegistVo.registNo}">
		<input type="hidden" id="registTaskFlag"
			value="${prpLRegistVo.registTaskFlag}"> <input type="hidden"
			id="tempRegistFlag" value="${prpLRegistVo.tempRegistFlag}">
		<!-- 工作流参数隐藏域开始 -->
		<input type="hidden" id="checkScheduled" value="${checkScheduled}" />
		<input type="hidden" id="dLossScheduled" value="${dLossScheduled}" />
		<input type="hidden" id="endCase" value="${endCase}" /> <input
			type="hidden" id="checkPersonFinishes" value="${checkPersonFinishes}" />


		<!-- 查勘调度和定损调度信息 -->
		<div class="bxmessage">
			<div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabBar f_gray4 cl">
						<span>查勘调度信息</span><span>定损调度信息</span><span>公估师轨迹信息</span>
					</div>
					<div class="tabCon clearfix">
						<div class="table_title f14">查勘调度损失项状态</div>
						<div class="table_cont table_list">
							<table class="table table-border table-bordered table-hover"
								cellpadding="0" cellspacing="0">
								<thead>
									<tr class="text-c">
										<th>损失项</th>
										<th>损失项内容</th>
										<th>状态</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="prpLScheduleItem"
										items="${prpLScheduleItemses}" varStatus="status">
										<tr class="text-c">
											<td>${prpLScheduleItem.itemsName}</td>
											<td><c:choose>
													<c:when test="${prpLScheduleItem.itemType eq 4}">
					 								${prpLScheduleItem.itemsContent}
					 								<strong>&nbsp;伤&nbsp;</strong>
					 								${prpLScheduleItem.itemRemark}
					 								<strong>&nbsp;亡</strong>
													</c:when>
													<c:otherwise>
					 								${prpLScheduleItem.itemsContent}
					 							</c:otherwise>
												</c:choose></td>
											<td><app:codetrans codeType="ScheduleStatus"
													codeCode="${prpLScheduleItem.scheduleStatus}" /></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>

						<!--查勘调度损失项状态   结束-->

						<!-- 查勘调度轨迹 开始-->
						<div class="table_title f14">查勘调度轨迹</div>
						<div class="table_cont table_list">
							<table class="table table-border table-bordered table-hover"
								cellpadding="0" cellspacing="0">
								<thead>
									<tr class="text-c">
										<th>调度任务时间</th>
										<th>损失项内容</th>
										<th>调度状态</th>
										<th>调度人</th>
										<th>注销/改派原因</th>
										<th>注销/改派时间</th>
										<th>查勘机构/组别</th>
										<th>查勘人员</th>
										<th>联系人</th>
										<th>联系电话</th>
										<th>查勘地点</th>
										<th>注销/改派备注</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="prpLScheduleTasklog"
										items="${prpLScheduleTasklogs}" varStatus="status">
										<c:if test="${prpLScheduleTasklog.scheduleType eq 1}">
											<tr class="text-c">
												<td><fmt:formatDate
														value="${prpLScheduleTasklog.scheduledTime}" type="both" />
												</td>
												<td>${prpLScheduleTasklog.lossContent}</td>
												<td><app:codetrans codeType="ScheduleStatus"
														codeCode="${prpLScheduleTasklog.scheduleStatus}" /></td>
												<td><app:codetrans codeType="UserCode"
														codeCode="${prpLScheduleTasklog.operatorCode}" /></td>
												<td><c:if
														test="${prpLScheduleTasklog.scheduleStatus == '6'}">
														<app:codetrans codeType="ScheduleChange"
															codeCode="${prpLScheduleTasklog.cancelOrReassignCode}" />
													</c:if> <c:if test="${prpLScheduleTasklog.scheduleStatus == '9'}">
														<app:codetrans codeType="ScheduleCancel"
															codeCode="${prpLScheduleTasklog.cancelOrReassignCode}" />
													</c:if></td>
												<td><fmt:formatDate
														value="${prpLScheduleTasklog.cancelOrReassinModifyTime}"
														pattern="yyyy-MM-dd HH:mm:ss" /></td>
												<td>${prpLScheduleTasklog.scheduledComname}</td>
												<td>${prpLScheduleTasklog.scheduledUsername}</td>
												<td>${prpLScheduleTasklog.linkerMan}</td>
												<td>${prpLScheduleTasklog.linkerManPhone}</td>
												<td>${prpLScheduleTasklog.checkareaName}</td>
												<td>${prpLScheduleTasklog.cancelOrReassignContent}</td>
											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<!-- 查勘调度轨迹 结束-->

					</div>

					<!-- 定损调度 -->
					<div class="tabCon clearfix">
						<div class="table_title f14">定损调度损失项状态</div>
						<div class="table_cont table_list">
							<table class="table table-border table-bordered table-hover"
								cellpadding="0" cellspacing="0">
								<thead>
									<tr class="text-c">
										<th>损失项</th>
										<th>损失项内容</th>
										<th>状态</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="prpLScheduleDefLoss"
										items="${prpLScheduleDefLosses}" varStatus="status">
										<tr class="text-c">
											<td>${prpLScheduleDefLoss.itemsName}</td>
											<td>${prpLScheduleDefLoss.itemsContent}</td>
											<td><app:codetrans codeType="ScheduleStatus"
													codeCode="${prpLScheduleDefLoss.scheduleStatus}" /></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<!--定损调度损失项状态   结束-->

						<!-- 定损调度信息 开始-->
						<div class="table_title f14">定损调度信息</div>
						<div class="table_cont table_list">
							<table class="table table-border table-bordered table-hover"
								cellpadding="0" cellspacing="0">
								<thead>
									<tr class="text-c">
										<th>调度任务时间</th>
										<th>损失项内容</th>
										<th>调度状态</th>
										<th>调度人</th>
										<th>注销/改派原因</th>
										<th>注销/改派时间</th>
										<th>查勘机构/组别</th>
										<th>定损人员</th>
										<th>联系人</th>
										<th>联系电话</th>
										<th>定损地点</th>
										<th>注销/改派备注</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="prpLScheduleTasklog"
										items="${prpLScheduleTasklogs}" varStatus="status">
										<c:if test="${prpLScheduleTasklog.scheduleType eq '2'}">
											<tr class="text-c">
												<td><fmt:formatDate
														value="${prpLScheduleTasklog.scheduledTime}" type="both" />
												</td>
												<td>${prpLScheduleTasklog.lossContent}</td>
												<td><app:codetrans codeType="ScheduleStatus"
														codeCode="${prpLScheduleTasklog.scheduleStatus}" /></td>
												<td><app:codetrans codeType="UserCode"
														codeCode="${prpLScheduleTasklog.operatorCode}" /></td>
												<td><c:if
														test="${prpLScheduleTasklog.scheduleStatus == '6'}">
														<app:codetrans codeType="ScheduleChange"
															codeCode="${prpLScheduleTasklog.cancelOrReassignCode}" />
													</c:if> <c:if test="${prpLScheduleTasklog.scheduleStatus == '9'}">
														<app:codetrans codeType="ScheduleCancel"
															codeCode="${prpLScheduleTasklog.cancelOrReassignCode}" />
													</c:if></td>
												<td><fmt:formatDate
														value="${prpLScheduleTasklog.cancelOrReassinModifyTime}"
														pattern="yyyy-MM-dd HH:mm:ss" /></td>
												<td><app:codetrans codeType="ComCode"
														codeCode="${prpLScheduleTasklog.scheduledComcode}" /></td>
												<td>${prpLScheduleTasklog.scheduledUsername}</td>
												<td>${prpLScheduleTasklog.linkerMan}</td>
												<td>${prpLScheduleTasklog.linkerManPhone}</td>
												<td>${prpLScheduleTasklog.checkareaName}</td>
												<td>${prpLScheduleTasklog.cancelOrReassignContent}</td>
											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<!-- 定损调度信息 结束-->
					</div>
					<!-- 定损调度 结束 -->

					<!-- 公估师轨迹 -->
					<div class="tabCon clearfix">

						<!-- 查勘人员信息轨迹 开始-->
						<div class="table_title f14">查勘人员信息轨迹</div>
						<div class="table_cont table_list">
							<table class="table table-border table-bordered table-hover"
								cellpadding="0" cellspacing="0">
								<thead>
									<tr class="text-c">
										<th>损失项内容</th>
										<th>调度时间</th>
										<th>查勘人员</th>
										<th>查勘人员电话</th>
										<th>公估师姓名</th>
										<th>公估师电话</th>
										<th>公估师所在位置</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="prplCarchildScheduleVo"
										items="${prplCarchildScheduleVos}" varStatus="status">
										<c:if test="${prplCarchildScheduleVo.nodeType eq 'Check'}">
											<tr class="text-c">
												<td>${prplCarchildScheduleVo.lossCountent}</td>
												<td><fmt:formatDate
														value="${prplCarchildScheduleVo.timeStamp}"
														pattern="yyyy-MM-dd HH:mm:ss" /></td>
												<td>${prplCarchildScheduleVo.scheduleUserName}</td>
												<td>${prplCarchildScheduleVo.scheduleUserPhone}</td>
												<td>${prplCarchildScheduleVo.handlerName}</td>
												<td>${prplCarchildScheduleVo.handlerPhone}</td>
												<td>
													<%-- <a href="${prplCarchildScheduleVo.traceLink}">查看</a> --%>
												</td>
											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<!-- 查勘调度轨迹 结束-->

						<!-- 定损调度信息 开始-->
						<div class="table_title f14">定损人员信息信息</div>
						<div class="table_cont table_list">
							<table class="table table-border table-bordered table-hover"
								cellpadding="0" cellspacing="0">
								<thead>
									<tr class="text-c">
										<th>损失项内容</th>
										<th>调度时间</th>
										<th>查勘人员</th>
										<th>查勘人员电话</th>
										<th>公估师姓名</th>
										<th>公估师电话</th>
										<th>公估师所在位置</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="prplCarchildScheduleVo"
										items="${prplCarchildScheduleVos}" varStatus="status">
										<c:if
											test="${prplCarchildScheduleVo.nodeType eq 'DLCar' || prplCarchildScheduleVo.nodeType eq 'DLProp'}">
											<tr class="text-c">
												<td>${prplCarchildScheduleVo.lossCountent}</td>
												<td><fmt:formatDate
														value="${prplCarchildScheduleVo.timeStamp}"
														pattern="yyyy-MM-dd HH:mm:ss" /></td>
												<td>${prplCarchildScheduleVo.scheduleUserName}</td>
												<td>${prplCarchildScheduleVo.scheduleUserPhone}</td>
												<td>${prplCarchildScheduleVo.handlerName}</td>
												<td>${prplCarchildScheduleVo.handlerPhone}</td>
												<td>
													<%-- <a href="${prplCarchildScheduleVo.traceLink}">查看</a> --%>
												</td>
											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<!-- 定损调度信息 结束-->
					</div>
					<!-- 公估师轨迹 结束 -->

					<!-- 案件备注所需信息隐藏域  -->
					<input type="hidden" id="nodeCode" value="${nodeCode}"> <input
						type="hidden" id="registNo" value="${registNo}">
				</div>
			</div>
		</div>

		<!-- 底部按钮 -->
		<br /> <br /> <br />
		<!-- 底部按钮 -->
		<div class="btn-footer clearfix" id="endCase_id">
			<input type="button" class="btn btn-disabled fl" disabled="disabled"
				style="margin-left: 23%;" id="seassignment"
				onClick="addSchedule('/claimcar/schedule/schReassignment?registNo=')"
				value="调度改派" /> <input type="button" class="btn btn-disabled fl"
				id="logout" disabled="disabled"
				onClick="addSchedule('/claimcar/schedule/schLogout?nodeCode=Sched&registNo=')"
				value="调度注销" /> <input type="button" class="btn btn-primary fl"
				id="addCheck"
				onClick="addSchedule('/claimcar/schedule/addCheckTask.do?registNo=')"
				value="新增查勘" /> <input type="button" class="btn btn-primary fl"
				id="addDefloss" disabled="disabled"
				onClick="addSchedule('/claimcar/schedule/addDeflossTask.do?registNo=')"
				value="新增定损" /> <input type="button" class="btn btn-success  fl"
				onClick="msgRecord()" value="短信处理" />
		</div>
		<br /> <br /> <br /> <br />
	</div>

	<script type="text/javascript">
		//$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
		$(function() {
			$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon",
					"current", "click", "0");
			createRegistRisk();
			checkRegistTaskFlag();
			var checkValue=$("#isAutoCheck").val();
			if(checkValue=='0'){
				$("#LSTable").addClass("hide");
			}else{
				$("#LSTable").removeClass("hide");
			}
			if ($("#checkPersonFinishes").val() == "0") {
				$("#endCase_id").addClass("hidden");
			}
			if ($("#tempRegistFlag").val() == "1") {
				layer.alert("该案件为无保单报案，需转有保后方可提交调度!");
				$('#addCheck').attr('disabled', true);
				$('#addCheck').removeClass("btn-primary");
				$('#addCheck').addClass("btn-disabled");
			}
			if ($("#checkScheduled").val() == "0" || $("#endCase").val() == "0"
					|| $("#checkPersonFinishes").val() == "0") {
				$('#addDefloss').attr('disabled', true);
				$('#addDefloss').removeClass("btn-primary");
				$('#addDefloss').addClass("btn-disabled");
			} else {
				$('#addDefloss').attr("disabled", false);
				$('#addDefloss').removeClass("btn-disabled");
				$('#addDefloss').addClass("btn-primary");
				$('#seassignment').attr("disabled", false);
				$('#seassignment').removeClass("btn-disabled");
				$('#seassignment').addClass("btn-primary");
			}

			if ($("#dLossScheduled").val() == "1") {
				$('#logout').attr("disabled", false);
				$('#logout').removeClass("btn-disabled");
				$('#logout').addClass("btn-primary");
			}

			var powerFlag = $("#powerFlag").val();

			if (powerFlag == "0") {
				$("#endCase_id").addClass("hidden");
			}
			
		});
		function addSchedule(url) {
			var registNo = $("#registNo").val();
			location.href = url + registNo + "&flowId=" + $("#flowId").val()
					+ "&taskId=" + $("#flowTaskId").val() + "&comCode="
					+ $("#comCode").val();
		}

		function cancelSchedule(url) {
			location.href = url;
		}

		function caseDetails(title, url, id) {
			var index = layer.open({
				type : 2,
				title : title,
				content : url
			});
			layer.full(index);
		}

		function addScheduleTask(title, url, id) {
			var layIndex = layer.open({
				title : title,
				type : 2,
				fix : false, //不固定
				maxmin : true,
				content : url
			});
			layer.full(layIndex);
		}

		$("#checkRegistMsg").click(function checkRegistMsgInfo() {//打开案件备注之前检验报案号和节点信息是否为空
			var registNo = $("#registNo").val();
			var nodeCode = $("#nodeCode").val();
			createRegistMessage(registNo, nodeCode);
		});

		//校验报案状态 暂存 已提交 已注销
		function checkRegistTaskFlag() {
			if ($("#registTaskFlag").val() == 7) {
				$("body input").each(function() {
					$(this).prop("disabled", "disabled");
					if ($(this).attr("type") == "button") {
						$(this).removeClass("btn-primary");
						$(this).addClass("btn-disabled");
					}
				});
				$("body select").each(function() {
					$(this).prop("disabled", "disabled");
				});
				$("body textarea").each(function() {
					$(this).prop("disabled", "disabled");
				});
			}

		}
		//打印
		function certifyPrint(registNo) {
			var params = "?registNo=" + registNo;
			var url = "/claimcar/certifyPrint/prpLRegist.ajax";
			var index = layer.open({
				type : 2,
				title : "机动车辆保险报案记录",
				maxmin : true, // 开启最大化最小化按钮
				content : url + params
			});
			layer.full(index);
		}

		//短信记录
		var businessNo = $("#registNo").val();
		function msgRecord() {
			var params = "?businessNo=" + businessNo;
			var url = "/claimcar/schedule/initMsgRecord.do";
			var index = layer.open({
				type : 2,
				title : "短信记录",
				area : [ '850px', '500px' ],
				maxmin : true, // 开启最大化最小化按钮
				content : url + params
			});
		}
	</script>

</body>
</html>