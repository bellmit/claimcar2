<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>立案-立案信息查看</title>

</head>
<body>
	<div class="top_btn">
		<a class="btn  btn-primary"  onclick="viewEndorseInfo('${prpLCMain.registNo}')">保单批改记录</a>
		<a class="btn btn-primary" onclick="viewPolicyInfo('${prpLCMain.registNo}')">出险保单</a> 
		<a class="btn btn-primary" onclick="createRegistMessage('${prpLCMain.registNo}', '${nodeCode }')">案件备注</a> 
		<a class="btn btn-primary"  onclick="caseDetails('${prpLCMain.registNo}')">历史出险信息</a>
		<a class="btn btn-primary ml-5" onclick="lawsuit('${prpLCMain.registNo}', '${nodeCode }')">诉讼</a> 
		<a class="btn btn-danger" href="javascript:;" onclick="createRegistRisk()">风险提示信息</a>
		<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
		 <a class="btn  btn-primary"  onclick="warnView('${prpLCMain.registNo}')">山东预警推送</a>
		<!-- 删除注销按钮  注销统一从菜单发起 -->
<%-- 	 	<a class="btn btn-primary" id="logOut" onclick="claimCancelRecoverByOne('${prpLRegist.registNo}','${taskVo.taskId}','${prpLClaim.claimNo}')">注销恢复</a>
		<a class="btn btn-primary" id="logOut" onclick="claimCancelByOne('${prpLRegist.registNo}','${taskVo.taskId}','${prpLClaim.claimNo}')">注销/拒赔</a> --%>
			
	
	</div>
	<form action="#" id="editClaimForm">
	  <input type="hidden" name="prpLCMain" value="${prpLCMain}" />
	  <input type="hidden" name="validFlag" value="${validFlag}" />
	  <input type="hidden" name="endCaseFlag" value="${endCaseFlag}" />
	  <input type="hidden" name="registNo" id="registNo" value="${prpLCMain.registNo}"/> 
		
		<div class="fixedmargin page_wrap">
			<!--标签页   开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl">
					<span>基本信息</span> <span>估损金额信息</span>
				</div>
				<!--基本信息页面 开始-->
				<div class="tabCon">
					<div class="table_wrap">
						<div class="table_cont mt-30 md-30">
							<div class="formtable ">
								<div class="row cl">
									<label class="form_label col-2">立案号：</label>
									<div class="form_input col-2">${prpLClaim.claimNo}</div>
									<label class="form_label col-2">结案号：</label>
									<div class="form_input col-2">${prpLClaim.caseNo}</div>
									<label class="form_label col-2">报案号：</label>
									<div class="form_input col-2">${prpLCMain.registNo}</div>
								</div>
							</div>
						</div>
						<p>
						<div class="table_cont mt-30 md-30">
							<div class="formtable">
								<div class="row cl">
									<label class="form_label col-2">保单号：</label>
									<div class="form_input col-2">${prpLCMain.policyNo}</div>
									<label class="form_label col-2">被保险人：</label>
									<div class="form_input col-2">${prpLCMain.insuredName}</div>
									<label class="form_label col-2">号牌底色：</label>
									<div class="form_input col-2">
									  <app:codetrans codeType="LicenseColorCode"
					                   codeCode="${prpLCMain.prpCItemCars[0].licenseColorCode}" />
									</div>
								</div>
								<div class="row cl">
									<label class="form_label col-2">保险期间：</label> 
									<div class="form_input col-2">
									  <fmt:formatDate value="${prpLCMain.startDate}" type="both" pattern='yyyy-MM-dd'/> 至
									  <fmt:formatDate value="${prpLCMain.endDate}" type="both" pattern='yyyy-MM-dd'/>止</div>
									<label class="form_label col-2">车牌号码：</label>
									<div class="form_input col-2">${prpLCMain.prpCItemCars[0].licenseNo}</div>
									<label class="form_label col-2">条款类别：</label>
									<div class="form_input col-2">
									  <app:codetrans codeType="ClauseType"
					                   codeCode="${prpLCMain.prpCItemCars[0].clauseType}" />
									</div>
								</div>
								<div class="row cl">
									<label class="form_label col-2">厂牌型号：</label>
									<div class="form_input col-2 text-overflow" title='${prpLCMain.prpCItemCars[0].brandName}'>${prpLCMain.prpCItemCars[0].brandName}</div>
									<label class="form_label col-2">车辆种类：</label>
									<div class="form_input col-2">
									   <app:codetrans codeType="CarTypeShow"
					                   codeCode="${prpLCMain.prpCItemCars[0].carType}" />
									</div>
									<label class="form_label col-2">保险金额：</label>
									<div class="form_input col-2">${prpLCMain.sumAmount}</div>
								</div>
								<div class="row cl md-30">
									<label class="form_label col-2">业务归属机构：</label>
									<div class="form_input col-2"><app:codetrans codeType="ComCode" codeCode="${prpLCMain.comCode}" /></div>
									<label class="form_label col-2">归属业务员：</label>
									<div class="form_input col-2">${prpLCMain.handler1Name}</div>
									<label class="form_label col-2"> </label>
									<div class="form_input col-2"></div>
								</div>
								<div class="row cl">
									<label class="form_label col-2">代理人：</label>
									<div class="form_input col-2">${prpLCMain.agentName}</div>
									<label class="form_label col-2">经办人：</label>
									<div class="form_input col-2"><app:codetrans codeType="UserCode" codeCode="${prpLCMain.handlerCode}"/></div>
									<label class="form_label col-2"> </label>
									<div class="form_input col-2"></div>
								</div>
							</div>
						</div>
						<p>
						<div class="table_cont">
							<div class="formtable">
								<div class="row cl">
									<label class="form_label col-2">出险时间：</label>
									<div class="form_input col-2"><fmt:formatDate value="${prpLRegist.damageTime}" type="both"/></div>
									<label class="form_label col-2">出险原因：</label>
									<div class="form_input col-2"><app:codetrans codeType="DamageCode" codeCode="${prpLCheck.damageCode}" /></div>
									<label class="form_label col-2">事故类型： </label>
									<div class="form_input col-2"><app:codetrans codeType="AccidentDutyType" codeCode="${prpLCheck.damageTypeCode}" /></div>
								</div>
								<div class="row cl">
									<label class="form_label col-2">立案日期：</label>
									<div class="form_input col-2"><fmt:formatDate value="${prpLClaim.claimTime}" type="both"/></div>
									<c:if test="${prpLClaim.riskCode eq '1101' }">
										<label class="form_label col-2">交强险责任类型：</label>
										<c:if test="${prpLCheckDuty.ciDutyFlag eq '1'}">
											<div class="form_input col-2">有责</div>
										</c:if>
										<c:if test="${prpLCheckDuty.ciDutyFlag ne '1'}">
											<div class="form_input col-2">无责</div>
										</c:if>
									</c:if>
									<c:if test="${prpLClaim.riskCode ne '1101' }">
									<label class="form_label col-2">事故责任：</label>
									<div class="form_input col-2">
										<app:codetrans codeType="IndemnityDuty" codeCode="${prpLCheckDuty.indemnityDuty}"/>
									</div>
									<label class="form_label col-2">责任比例：</label>
									<div class="form_input col-2">${prpLCheckDuty.indemnityDutyRate}</div>
									</c:if>
								</div>
								<div class="row cl">
									<label class="form_label col-2">出险地点：</label>
									<div class="form_input col-2 text-overflow" title='${prpLRegist.damageAddress}'>${prpLRegist.damageAddress}</div>
									<label class="form_label col-2">估损金额：</label>
									<div class="form_input col-2">${prpLClaim.sumDefLoss}</div>
									<label class="form_label col-2">估计赔款：</label>
									<div class="form_input col-2">${prpLClaim.sumClaim}</div>
								</div>
								<div class="row cl  md-30">
									<label class="form_label col-2">赔案类别：</label>
									<div class="form_input col-2">正常赔案</div>
									<label class="form_label col-2">是否全损：</label>
									<div class="form_input col-2">
									    <c:if test="${prpLCheck.lossType=='1' }">
											是
									     </c:if>
									     <c:if test="${prpLCheck.lossType=='0' }">
									                      否
									     </c:if>
									</div>
									
									<label class="form_label col-2">是否互碰自赔：</label>
									<div class="form_input col-2">
									   <c:if test="${prpLCheck.isClaimSelf=='1' }">
										   	是 
									   </c:if>	
									   <c:if test="${prpLCheck.isClaimSelf=='0' }">
									      	否 
									   </c:if>
									</div>
								</div>
							</div>
						</div>
						
						<p>
						<div class="line"></div>
						<div class="formtable mt-30">
							<div class="row cl">
								<lable class="form_label col-2"> 代位求偿类型：</lable>
								<div class="form_input col-3">
								  <c:if test="${prpLCheck.isSubRogation=='0' }">
									<input type="radio" id="radio-1" name="SubrogationFlag" checked disabled='disabled'>
									<label for="radio-1">非代位求偿案件</label>
									<input type="radio" id="radio-2" name="SubrogationFlag" disabled='disabled'> 
									<label for="radio-2">代位求偿</label>
								  </c:if>
								  
								  <c:if test="${prpLCheck.isSubRogation=='1' }">
									<input type="radio" id="radio-1" name="SubrogationFlag" checked disabled='disabled'>
									<label for="radio-1">非代位求偿案件</label>
									<input type="radio" id="radio-2" name="SubrogationFlag" checked disabled='disabled'> 
									<label for="radio-2">代位求偿</label>
								  </c:if>
								</div>
								
							</div>
						</div>
						<p>

						<div class="line"></div>
						<div class="formtable mt-30">
							<div class="table_wrap">
								<div class="row cl">
									<div class="col-1 text-c">
										<lable class="c-primary f-14">特别约定</lable>
									</div>
								</div>
								<div class="table_cont ">
									<table class="table table-border table-hover">
										<thead>
											<tr>
												<th>序号：</th>
												<th>特约代码：</th>
												<th>特约名称：</th>
											</tr>
										</thead>
										<tbody>
										 <input type="hidden" name="prpLCengages" value="${prpLCengages}" />
				 			               <c:forEach var="prpLCengage" items="${prpLCengages}" varStatus="status">
										   <tr>
												<td>${status.index+1}</td>
												<td>${prpLCengage.key}</td>
												<td>${prpLCengage.value}</td>
											</tr>
										</tbody>
									  </c:forEach>
									</table>
								</div>
							</div>
						</div>
						<p>
						<div class="line"></div>
						<div class="formtable mt-30">
							<div class="row cl">
								<div class="col-1 text-c">
									<lable class="c-primary f-14">出险经过</lable>
								</div>
							</div>
							<div class="row cl">
								<div class="col-11">
									<textarea class="textarea w90" maxlength="1000" datatype="*1-1000" readonly="readonly"/>${registExt.dangerRemark}</textarea>
								</div>
							</div>
						</div>
						<p>
						<div class="line"></div>
						<div class="table_wrap mt-30">
							<div class="row cl">
								<div class="col-1 text-c">
									<lable class="c-primary f-14">巨灾信息</lable>
								</div>
							</div>
							<div class="table_cont">								
								<table class="table table-border table-bordered table-bg">
									<thead>
										<tr class="text-c">
											<th style="width:25%">巨灾一级代码：</th>
											<td>${prpLDisaster.disasterCodeOne}</td>
											<th style="width:25%">巨灾名称：</th>
											<td>${prpLDisaster.disasterNameOne}</td>
										</tr>
										<tr class="text-c">
											<th style="width:25%">巨灾二级代码：</th>
											<td>${prpLDisaster.disasterCodeTwo}</td>
											<th style="width:25%">巨灾名称：</th>
											<td>${prpLDisaster.disasterNameTwo}</td>
										</tr>
									</thead>
									<tbody>

									</tbody>
								</table>
							</div>
						</div>

						<div class="formtable mt-30">
							<div class="row cl">
								<div class="col-1 text-c">
									<lable class="c-primary f-14">立案修改意见</lable>
								</div>
							</div>
							<div class="row cl">
								<div class="col-11">
									<input class="hidden" name="prplClaimText.bussNo" value="${prpLClaim.claimNo}"/>
									<textarea class="textarea w90" maxlength="1000" datatype="*1-500"  name="prplClaimText.description"/></textarea>
								</div>
								<div class="col-1 c-red">*</div>
							</div>
						</div>

						<div class="table_wrap mt-30 md-30">
							<div class="row cl">
								<div class="col-1 text-c">
									<lable class="c-primary f-14">意见列表</lable>
								</div>
							</div>
							<div class="table_cont ">
								<table class="table table-border table-hover table-bg">
									<thead>
										<tr class="text-c">
											<th>序号</th>
											<th>操作人员</th>
											<th>机构</th>
											<th>时间</th>
											<th>意见说明</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="prpLClaimText" varStatus="status" items="${prpLClaimTextList}">
											<tr class="text-c">
												<td>${status.index+1}</td>
												<td>${prpLClaimText.operatorName}</td>
												<td><app:codetrans codeType="ComCode" codeCode="${prpLClaimText.comCode}"/></td>
												<td><fmt:formatDate value="${prpLClaimText.inputTime}" pattern="yyyy年MM月dd日   HH:mm:ss"/></td>
												<td><span name="claimText">${prpLClaimText.description}</span></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
						
					</div>
				</div>
				<!--基本信息页面 结束-->


				<!--估损金额信息页面 开始-->
				<div class="tabCon">
					<div class="table_wrap">
						<div class="table_cont">
							<div class="formtable">
								<span class="c-primary f-14">险别估损信息</span>
								<div class="table_con">
									<table class="table table-bordered table-bg">
										<thead class="text-c">
											<tr>
												<th>险别</th>
												<th>损失类别</th>
												<th>保额/责任限额</th>
												<th>估损金额</th>
												<th>施救费</th>
												<th>估计赔款</th>
												<th>免赔率（%）</th>
												<th>调整原因</th>
											</tr>
										</thead>
										<tbody class="text-c" >
										  <input type="hidden" name="prpLClaimKinds" value="${prpLClaimKinds}" />
										   		<c:if test="${claimKindFlag eq '2'}">
													 <tr>
													 	<td rowspan="2">第三者责任保险 (B)</td>
													 	<td>${claimKindBLoss.lossItemName}</td>
													 	<td rowspan="2">${claimKindBLoss.amount}</td>
													 	<td>${claimKindBLoss.defLoss}</td>
														<td>${claimKindBLoss.rescueFee}</td>
														<td>
															<input type="text" class="input-text text-c" name="prpLClaimKind[${fn:length(prpLClaimKinds) }].claimLoss" 
																onchange="getChangeElement(this)" datatype="amount"  value="${claimKindBLoss.claimLoss}" />
														</td>
														<td><span>${claimKindBLoss.deductibleRate}</span></td>
														<td>
															<input type="button" class="btn btn-default radius" onclick="clickReasonTextShow(this)" value="···">
															<div class="hidden">
																<textarea rows="10" cols="100" name="prpLClaimKind[${fn:length(prpLClaimKinds) }].adjustReason" placeholder="请填写调整原因"></textarea>
															</div>
														</td>
														<!-- 隐藏域 -->
														<input type="hidden" id="kindCode" name="prpLClaimKind[${fn:length(prpLClaimKinds) }].kindCode" value="${claimKindBLoss.kindCode}"/>
														<input type="hidden" name="prpLClaimKind[${fn:length(prpLClaimKinds) }].id" value="${claimKindBLoss.id}"/>
														<input type="hidden" name="prpLClaimKind[${fn:length(prpLClaimKinds) }].claimLoss" value="${claimKindBLoss.claimLoss}"/>
														<input type="hidden" name="prpLClaimKind[${fn:length(prpLClaimKinds) }].deductibleRate" value="${claimKindBLoss.deductibleRate}"/>
														<!-- 隐藏域 -->
													 </tr>
													 <tr>
													 	<td style="border-left:1px solid #ddd">${claimKindBPers.lossItemName}</td>
													 	<td>${claimKindBPers.defLoss}</td>
														<td>${claimKindBPers.rescueFee}</td>
														<td>
															<input type="text" class="input-text text-c" name="prpLClaimKind[${fn:length(prpLClaimKinds)+1 }].claimLoss" 
																onchange="getChangeElement(this)" datatype="amount"  value="${claimKindBPers.claimLoss}" />
														</td>
														<td><span>${claimKindBPers.deductibleRate}</span></td>
														<td>
															<input type="button" class="btn btn-default radius" onclick="clickReasonTextShow(this)" value="···">
															<div class="hidden">
																<textarea rows="5" cols="15" name="prpLClaimKind[${fn:length(prpLClaimKinds)+1 }].adjustReason"></textarea>
															</div>
														</td>
														<!-- 隐藏域 -->
														<input type="hidden" id="kindCode" name="prpLClaimKind[${fn:length(prpLClaimKinds)+1 }].kindCode" value="${claimKindBPers.kindCode}"/>
														<input type="hidden" name="prpLClaimKind[${fn:length(prpLClaimKinds)+1 }].id" value="${claimKindBPers.id}"/>
														<input type="hidden" name="prpLClaimKind[${fn:length(prpLClaimKinds)+1 }].claimLoss" value="${claimKindBPers.claimLoss}"/>
														<input type="hidden" name="prpLClaimKind[${fn:length(prpLClaimKinds)+1 }].deductibleRate" value="${claimKindBPers.deductibleRate}"/>
														<!-- 隐藏域 -->
													 </tr>
												 </c:if>
				 			               		<c:forEach var="prpLClaimKind" items="${prpLClaimKinds}" varStatus="status">
													<tr>
														<td>
															<app:codetrans codeType="KindCode" codeCode="${prpLClaimKind.kindCode}" riskCode="${riskCode }" />
														  	(${prpLClaimKind.kindCode})
														  	<input type="hidden" id="kindCode" name="prpLClaimKind[${status.index }].kindCode" value="${prpLClaimKind.kindCode}"/>
														  	<input type="hidden" name="prpLClaimKind[${status.index }].id" value="${prpLClaimKind.id}"/>
														</td>
														<td>
															${prpLClaimKind.lossItemName}
															<input type="hidden" name="prpLClaimKind[${status.index }].lossItemName" value="${prpLClaimKind.lossItemName}"/>
														</td>
														<td>
															<span>${prpLClaimKind.amount}</span>
															<input type="hidden" name="prpLClaimKind[${status.index }].amount" value="${prpLClaimKind.amount}"/>
														</td>
														<td>${prpLClaimKind.defLoss}</td>
														<td>${prpLClaimKind.rescueFee}</td>
														<td>
															<input type="text" class="input-text text-c" name="prpLClaimKind[${status.index }].claimLoss" 
																onchange="getChangeElement(this)" datatype="amount"  value="${prpLClaimKind.claimLoss}" />
														</td>
														<td>
															<span>${prpLClaimKind.deductibleRate}</span>
															<input type="hidden" name="prpLClaimKind[${status.index }].deductibleRate" value="${prpLClaimKind.deductibleRate}"/>
														</td>
														<td>
														<input type="button" class="btn btn-default radius" onclick="clickReasonTextShow(this)" value="···">
														<div class="hidden">
															<textarea rows="10" cols="50" name="prpLClaimKind[${status.index }].adjustReason" placeholder="请填写调整原因"></textarea>
														</div>
														</td>
													</tr>
												</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
							<p>

							<div class="table_cont">
								<div class="formtable">
									<div>【系统提示】</div>
									<br>
									<div>1、自动生成的险别估损金额和估计赔款仅含 A、 B 、BZ 、D11、 D12 、 G、  F、  L
										、Z 险别的估损金额，其他险别请手工录入估损金额或施救费。</div>
								</div>
							</div>

							<div class="formtable mt-30">
								<span class="c-primary f-14">估计直接理赔费用</span>
								<div class="table_con">
									<table class="table table-bordered table-bg">
										<thead>
											<tr>
												<th>险别</th>
												<th>费用名称</th>
												<th>费用金额</th>
												<th>调整原因</th>
											</tr>
										</thead>
				 			               <c:forEach var="prpLClaimKindFee" items="${prpLClaimKindFees}" varStatus="status">
											<tr>	
												<td>
													<app:codetrans codeType="KindCode" codeCode="${prpLClaimKindFee.kindCode}" riskCode="${riskCode }"/>
													<input type="hidden" name="prpLClaimKindFee[${status.index }].kindCode" value="${prpLClaimKindFee.kindCode}"/> 
													<input type="hidden" name="prpLClaimKindFee[${status.index }].id" value="${prpLClaimKindFee.id}"/>										
												</td>
												<td>
													<app:codetrans codeType="ChargeCode" codeCode="${prpLClaimKindFee.feeCode}" /> 
													<input type="hidden" name="prpLClaimKindFee[${status.index }].feeName" value="${prpLClaimKindFee.feeCode}"/>
												</td>
												<td>
													<input class="input-text" type="text" id="payFees" name="prpLClaimKindFee[${status.index }].payFee" onchange="getChangeElement(this)" value="${prpLClaimKindFee.payFee}" />
												</td>
												<td>
													<textarea rows="1" cols="40" name="prpLClaimKindFee[${status.index }].adjustReason"></textarea>
												</td>	
											</tr>
										    </c:forEach>							
										<tr>
											<td>费用合计</td>
											<td colspan="4"><c:set var="Sum1" value="0" /> 
											<c:forEach items="${prpLClaimKindFees}" var="vo1">
												<c:set var="Sum1" value="${Sum1 + vo1.payFee}" />
											</c:forEach>
											<u>${Sum1}</u>
										</td>
										</tr>
									</table>
								</div>
							</div>
							<p>
							<div class="formtable">
								<span class="c-primary f-14">历史险别估损金额信息 </span>
								<div class="table_con">
								 <c:forEach var="prpLClaimKindHisMap" items="${prpLClaimKindHisMap}" varStatus="status_1">
									 <div class="table_title f14 table_close">第${status_1.index+1}次调整</div>
									 <div class="table_cont" style="display: none;">
										<table class="table table-bordered table-bg">
											<thead class="text-c">
												<tr>
													<th>历史调整次数</th>
													<th>赔款类别</th>
													<th>险别</th>
													<th>损失类别</th>
													<th>估损金额</th>
													<th>施救费</th>
													<th>估计赔款</th>
													<th>估损金额变化量</th>
													<th>施救费变化量</th>
													<th>估计赔款变化量</th>
													<th>输入时间</th>
													<th>调整原因</th>
												</tr>
											</thead>
											<tbody class="text-c">
											 <!-- <input type="hidden" name="prpLClaimKindHiss" value="${prpLClaimKindHisMap.value}" /> --> 
					 			               <c:forEach var="prpLClaimKindHis" items="${prpLClaimKindHisMap.value}" varStatus="status">
												<tr>
													<%-- <td>第${prpLClaim.estiTimes}次调整</td>
													<td> <app:codetrans codeType="PFeeType" codeCode="${prpLClaimKindHis.feeType}"/> </td>
													<td> <app:codetrans codeType="KindCode" codeCode="${prpLClaimKindHis.kindCode}" /></td>
													<td>${prpLClaimKindHis.lossItemName}</td>
													<td>${prpLClaimKindHis.defLoss}</td>
													<td>${prpLClaimKindHis.rescueFee}</td>
													<td>${prpLClaimKindHis.claimLoss}</td>
													<td>${prpLClaimKindHis.defLossChg}</td>
													<td>${prpLClaimKindHis.rescueFeeChg}</td>
													<td>${prpLClaimKindHis.claimLossChg}</td>
													<td><fmt:formatDate value="${prpLClaimKindHis.createTime}" type="both"/></td>
													<td>${prpLClaimKindHis.adjustReason}</td>  --%>
													<td>第${status_1.index+1}次调整</td>
									                <td>
									                	<c:if test="${prpLClaimKindHis.feeType == 'P'}">赔款</c:if> 
									                	<c:if test="${prpLClaimKindHis.feeType == 'F'}">费用</c:if>
									                </td>
									                 <td><app:codetrans codeCode="${prpLClaimKindHis.kindCode}" codeType="KindCode" riskCode="${riskCode }"/></td>
									                 <td><app:codetrans codeCode="${prpLClaimKindHis.lossItemName}" codeType="ChargeCode" /></td>
									                 <td>${prpLClaimKindHis.defLoss}</td>
									                 <td>${prpLClaimKindHis.rescueFee}</td>
									                 <td>${prpLClaimKindHis.claimLoss}</td>
									                 <td>${prpLClaimKindHis.defLossChg}</td>
									                 <td>${prpLClaimKindHis.rescueFeeChg}</td>
									                 <td>${prpLClaimKindHis.claimLossChg}</td>
									                 <td><fmt:formatDate value='${prpLClaimKindHis.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
									                 <td>${prpLClaimKindHis.adjustReason}</td>
												</tr> 
											   </c:forEach>
											</tbody>
										</table>
									</div>
								</c:forEach>
								</div>
							</div>

						</div>




					</div>
				</div>
				<!--估损金额信息页面 结束-->
				

			</div>
			<!--标签页   结束-->
			
			<!--立案修改功能暂不开放 开放取消此处注释展示提交按钮即可-->
				<%-- <shiro:hasRole name="claim.claim">
				<div class="formtable mt-30 text-c" id="submitDiv">
					<a class="btn btn-primary  radius" id="submit" >提交</a>
				</div>
				</shiro:hasRole> --%>
			<!--立案修改功能暂不开放 开放取消此处注释展示提交按钮即可-->
		</div>
	    
	</form>
	<script type="text/javascript" src="/claimcar/js/claim/claimEdit.js"></script>
	<script type="text/javascript">
	</script>
	
</body>

</html>