<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>单证收集</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<!-- 按钮组 -->
			<div class="top_btn">
				<a class="btn  btn-primary mb-10"  onclick="viewEndorseInfo('${prpLCertifyMainVo.registNo}')">保单批改记录</a>
				<input type="button" class="btn  btn-primary mb-10" id="messageInfo" value="案件备注">
				<input type="button" class="btn  btn-primary mb-10" id="certifyList" value="索赔清单">
		
 				<input type="button" class="btn  btn-primary mb-10" onclick="policyImage('${prpLCertifyMainVo.policyNo}')" value="承保影像资料">
				<input type="button" class="btn  btn-primary mb-10" id="payCustom"  value="收款人账户信息维护"> 
				<input type="button" class="btn  btn-primary mb-10" id="lawsuit" value="诉讼">
	            <input type="button" class="btn btn-danger mb-10" href="javascript:;" onclick="createRegistRisk()" value="风险提示信息">
				<input type="button" class="btn  btn-primary mb-10 addLoss" style="display:none" id="lossAdd" value="定损追加">
				<input type="button" class="btn  btn-primary mb-10 addLoss" style="display:none" onclick="openTaskEditWin('新增定损任务','/claimcar/schedule/addDeflossTask.do?registNo=${prpLCertifyMainVo.registNo}&flowId=${scheduleTaskVo.flowId}&taskId=${scheduleTaskVo.taskId}')" value="新增定损">
				<input type="button" class="btn  btn-primary mb-10" id="refresh" value="页面刷新">
				<a class="btn  btn-primary mb-10" onclick="reportPrint('${prpLCertifyMainVo.registNo}')">代抄单打印</a>
				<c:if test="${roleFlag eq '1' }">		
					<a class="btn  btn-primary mb-10" onclick="ruleView('${prpLCertifyMainVo.registNo}','${prpLCertifyMainVo.riskCode}','${prpLWfTaskVo.nodeCode}','','${prpLWfTaskVo.upperTaskId}')">ILOG规则信息查看</a>
				</c:if>			
				<a class="btn btn-primary mb-10" id="surveyButton" onclick="createSurvey('${prpLCertifyMainVo.registNo}','${prpLWfTaskVo.flowId}','${prpLWfTaskVo.taskId}','${prpLWfTaskVo.nodeCode}','${handlerUser}');">调查</a>
				<a class="btn  btn-primary mb-10" onclick="imageMovieScan('${prpLCertifyMainVo.registNo}')">信雅达影像查看</a>
				<a class="btn  btn-primary mb-10" onclick="imageMovieUpload('${prpLWfTaskVo.taskId}')">信雅达影像上传</a>
				<input type="button" class="btn  btn-primary mb-10" onclick="policyImage('${prpLCertifyMainVo.policyNoLink}')" value="关联保单承保影像资料">
				<a class="btn  btn-primary mb-10"  onclick="feeImageView()">人伤赔偿标准</a>
				<a class="btn  btn-primary mb-10"  onclick="warnView('${prpLCertifyMainVo.registNo}')">山东预警推送</a>
			</div>
		<p>
		<form action="#" id="editform">
		    <input type="hidden" id="saveType" name="saveType"/>
		    <input type="hidden" id="certifyMakeup" value="${certifyMakeup}" />
		    <input type="hidden" id="isLock" value="${isLock}" />
		    <input type='hidden' value='${prpLWfTaskVo.handlerStatus}' id="handlerStatus">
		    <input type='hidden' value='${prpLWfTaskVo.workStatus}' id="workStatus">
		    <input type="hidden" id="flowId" name="submitVo.flowId" value="${prpLWfTaskVo.flowId}"/>
		    <input type="hidden" id="taskId" name="submitVo.flowTaskId" value="${prpLWfTaskVo.taskId}"/>
		    <input type="hidden" name="submitVo.comCode" value="${comCode}"/>
		    <input type="hidden" id="nodeCode" value="${prpLWfTaskVo.nodeCode}"/>
		    <input type="hidden" id="subNodeCode" name="submitVo.currentNode" value="${prpLWfTaskVo.subNodeCode}"/>
		    <input type="hidden" id="registNo" value="${prpLCertifyMainVo.registNo}" name="prpLCertifyMainVo.registNo">
		    <input type="hidden" id="policyNo" value="${prpLCertifyMainVo.policyNo}" name="prpLCertifyMainVo.policyNo">
		    <input type="hidden" id="policyNoLink" value="${prpLCertifyMainVo.policyNoLink}" name="prpLCertifyMainVo.policyNoLink">
		    <input type="hidden" value="${prpLCertifyMainVo.id}" name="prpLCertifyMainVo.id">
		    <input type="hidden" id="reCaseFlag" value="${reCaseFlag}" name="reCaseFlag">
		    <input type="hidden" id="subrogationFlag" value="${subrogationMain.subrogationFlag}" name='isSubRogation'>
		    <input type="hidden" id="sourceFlag" value="${subrogationMain.sourceFlag}">
		    <input type="hidden" value="${subrogationMain.id}" name='subrogationMain.id'>
		    <input type="hidden" value="${prpLCertifyMainVo.registNo}" name='subrogationMain.registNo'>
		    <input type="hidden" value="<fmt:formatDate value='${reportTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" id='reportTime'>
			<input type="hidden" id="reportType" value="${reportType}">
			<input type="hidden" id="prpUrl"  value="${prpUrl}"/>
			<div class="page_wrap">
				<!--标签页   开始-->
				<div id="tab-system" class="HuiTab">
					<div class="tabBar cl">
						<span>单证收集信息</span> 
						<span id="lossPhoto">损失照片</span>
						<span>收款人账户信息</span>
					</div>
					<!--单证收集信息页面 开始-->
					<div class="tabCon">
						<div class="table_wrap">
						<div class="table_title f14">单证收集标志</div>
							<div class="table_cont mt-30 md-30" id="certifyItems">
								<div class="formtable ">
									<div class="row cl">
										<label class="form_label col-4">保单号码：</label>
										<div class="form_input col-4">
											<span>${prpLCertifyMainVo.policyNo}</span>
											<span class="ml-15">${prpLCertifyMainVo.policyNoLink}</span>
										</div>
										<c:if test="${not empty prpLCertifyMainVo.policyNoLink}">
<!-- 										<div class="form_input col-4">
											<input class="btn" id="policyNoLinkCertify" type="button" value="关联保单承保影像资料">
										</div>       -->
										</c:if>
									</div>
									<div id="prpLCertifyItems">
										<%@include file="certifyItems.jsp"%>
									</div>
								</div>
							</div>
							
					    	<div class="table_title f14">代位求偿类型</div>
					    	    <div class="table_cont mt-30 md-30">
								<div class="formtable ">
									<div class="row cl">
										<div class="form_input col-6">
										<c:if test="${subrogationMain.subrogationFlag eq '1' && subrogationMain.sourceFlag != '1'}">
										  <input type="hidden" value="${subrogationMain.subrogationFlag}" name='subrogationMain.subrogationFlag'>
										</c:if>
										    <label class="radio-box">
												<input type="radio" name="subrogationMain.subrogationFlag" value="0" class=""  <c:if test="${subrogationMain.subrogationFlag !='1'}">checked</c:if>>
												非代位求偿案件
											</label>
											<label class="radio-box">
												<input type="radio" name="subrogationMain.subrogationFlag" value="1" class=""  <c:if test="${subrogationMain.subrogationFlag eq '1'}">checked</c:if>>
												代位求偿
											</label>
										</div>
									</div>
								</div>	
								<div class="table_list " id="subRogationDiV" <c:if test="${empty subrogationMain.subrogationFlag || subrogationMain.subrogationFlag != '1'}">style='display:none'</c:if>>
									<input type="hidden" id="subrogationCarSize"
										value="${fn:length(subrogationMain.prpLSubrogationCars) }">
									<table class="table table-bordered table-bg" id="subrogationCarTable">
										<thead class="text-c" id="subrogationCarThead">
											<tr>
												<th style="width: 5%;">
													<button type="button"
														class="btn btn-plus Hui-iconfont Hui-iconfont-add"
														onclick="initSubrogationCar(this)"></button>
												</th>
												<th style="width: 5%; text-align: center; font-weight: bold;">序号</th>
												<th style="text-align: center; font-weight: bold;">责任对方内容（责任方为机动车）</th>
											</tr>
										</thead>
										<%@include file="certifyEdit_Subrogation_CarTr.jsp"%>
									</table>
									<br/>
                                    <input type="hidden" id="subrogationPerSize" value="${fn:length(subrogationMain.prpLSubrogationPersons) }">
									<table class="table table-bordered table-bg">
										<thead class="text-c">
											<tr>
											    <th style="width: 5%;">
													<button type="button"
														class="btn btn-plus Hui-iconfont Hui-iconfont-add"
														onclick="initSubrogationPers(this)"></button>
												</th>
												<th
													style="width: 5%; text-align: center; font-weight: bold;">序号
												</th>
												<th style="text-align: center; font-weight: bold;">责任对方内容（责任方为非机动车）
												</th>
											</tr>
										</thead>
										<tbody id="subrogationPerTbody">
											<%@include file="certifyEdit_Subrogation_PerTr.jsp"%>
										</tbody>
									</table>
								</div>
								</div>
								<%-- <%@include file="certifySubrogation.jspf"%> --%>
								
							<div class="table_title f14">案件信息</div>
							   <div class="table_cont mt-30 md-30">
								 <div class="formtable ">
									<div class="row cl">
									   <label class="form_label col-2">互碰自赔</label>  
										<div class="form_input col-4">
										   <input type="hidden" name="prpLCheckDutyVo.id" value="${prpLCheckDutyVo.id}">
										   <input type="hidden" name="prpLCheckDutyVo.registNo" value="${prpLCheckDutyVo.registNo}">
										   <input type="hidden" name="prpLCheckDutyVo.serialNo" value="${prpLCheckDutyVo.serialNo}">
										   <!-- isClaimSelf用于判断单证是否更改了互陪自赔标示 -->
										   <input type="hidden" name="isClaimSelf" value="${prpLCheckDutyVo.isClaimSelf}">
										   <span class="radio-box"> <app:codeSelect codeType="IsValid"
							               type="radio" value="${prpLCheckDutyVo.isClaimSelf}" id="isClaimSelf"
							               name="prpLCheckDutyVo.isClaimSelf" nullToVal="0" />
										  </span> 
									   </div>
									   
									  <label class="form_label col-2">是否诉讼</label>
										<div class="form_input col-4">
										   <span class="radio-box">
												<app:codeSelect codeType="IsValid" type="radio" value="${prpLCertifyMainVo.lawsuitFlag}"  name="prpLCertifyMainVo.lawsuitFlag" nullToVal="0"  />
													
											</span>
										</div>
									</div>
									
									<div class="row cl">
									<label class="form_label col-2">是否欺诈</label>
										<div class="form_input col-4">
											<span class="radio-box">
												<app:codeSelect codeType="IsValid" type="radio" value="${prpLCertifyMainVo.isFraud}" id="isFraud" name="prpLCertifyMainVo.isFraud" nullToVal="0" onchange="validFraud()" />
													<input type="hidden" id="isFraudOrigin"  value="${prpLCertifyMainVo.isFraud}" />
											</span>
										</div>
										
										<label class="form_label col-2">是否调查</label>
										<div class="form_input col-4">
											<span class="radio-box">
												<app:codeSelect codeType="IsValid" type="radio" value="${prpLCertifyMainVo.surveyFlag}"  name="prpLCertifyMainVo.surveyFlag" nullToVal="0" />
											</span>
										</div>
										
									</div>
									
										<div id="fraudInfo" class="hide">
										   <div class="row cl mb-5">
											<label class="form_label col-2">欺诈标志</label>
											<div class="form_input col-2">
												<app:codeSelect codeType="FraudLogo" type="select" name="prpLCertifyMainVo.fraudLogo" value="${prpLCertifyMainVo.fraudLogo }" clazz="must"
													onchange="validFraud()" />
											</div>
											<label class="form_label col-2">欺诈类型</label>
											<div class="form_input col-2">
												<app:codeSelect codeType="SwindleType" type="select" name="prpLCertifyMainVo.fraudType" value="${prpLCertifyMainVo.fraudType }" clazz="must" />
											</div>
											<label class="form_label col-2">欺诈挽回损失金额</label>
											<div class="form_input col-2">
												<input type="text" class="input-text" name="prpLCertifyMainVo.fraudRecoverAmount" value="${prpLCertifyMainVo.fraudRecoverAmount }"
													errormsg="欺诈挽回损失金额" nullmsg="请填写欺诈挽回损失金额" />
												<font color="red">*</font>
											</div>
										</div>
									</div>
								    
								
							  
							  <div class="row cl">
			                     <c:if test="${caseFlag eq '1' || caseFlag eq '3' }">
			                         <label class="form_label col-2">商业拒赔</label>
			                              <div class="form_input col-4">
						                     <span class="radio-box"> <app:codeSelect codeType="IsValid"
								                 type="radio" nullToVal="0" value="${prpLCertifyMainVo.isSYFraud}"
								                         name="prpLCertifyMainVo.isSYFraud"  />
						                                   </span>
			                               </div>
			                     </c:if>
			                 <c:if test="${caseFlag eq '2' || caseFlag eq '3' }">
			                      <label class="form_label col-2">交强拒赔</label>
			                           <div class="form_input col-4">
						                     <span class="radio-box"> <app:codeSelect codeType="IsValid"
								                  type="radio" nullToVal="0" value="${prpLCertifyMainVo.isJQFraud}"
								             name="prpLCertifyMainVo.isJQFraud"  />
						                     </span>
			                           </div>
			                </c:if>
			                
							</div>
				<div class="row cl">		
					<div id="payCause" class="hide">
			                 <label class="form_label col-2">拒赔原因</label>
				                <div class="form_input col-3">
				                  <span class="select-box">
				                    <app:codeSelect codeType="DZNOTPAY" type="select" value="${prpLCertifyMainVo.newNotpaycause}" name="prpLCertifyMainVo.newNotpaycause"/>
				                     <font class="c-red">*</font>
				                  </span>
				               </div>
			         </div>
			
		             <div id="otherPayCause" class="hide">
		                  <label class="form_label col-1"></label>
			              <label class="form_label col-2">其它原因</label>
			              <div class="form_input col-3">
			              <input type="text" class="input-text" name="prpLCertifyMainVo.othernotPaycause" id="otherNotpayCause" value="${prpLCertifyMainVo.othernotPaycause}" /><font class="c-red">*</font>
			             </div>
			          </div>
				</div>  
						
					</div>
				 </div>		
							
							
							<!-- 责任比例信息-->
							<%@include file="certifyLossCarInfo.jsp"%>
							<!-- 免赔率信息 -->
							<c:if test="${policyType ne '1'}">
							<%@include file="certifyEdit_Dutynopay.jsp"%>
							</c:if>
							<div class="table_title f14">发票信息</div>
							<div class="table_cont mt-30 md-30">
								<table class="table table-border table-bordered text-c">
									<thead class="text-c">
										<tr>
											<th width='18%'>损失类型</th>
											<th>损失方</th>
											<th>人员属性</th>
											<th>定损金额</th>
											<th>发票金额</th>
										</tr>
										<c:forEach var="lossCarMain" items="${lossCarMainList}" varStatus="status">
											<tr>
												<th>车辆损失</th>
												<input type="hidden" class="input-text" name="lossCarMainList[${status.index }].id" value="${lossCarMain.id }" />
												<td>
													<input type="text" class="input-text" name="lossCarMainList[${status.index }].licenseNo" value="${lossCarMain.licenseNo }" readonly="readonly" />
												</td>
												<td>
													
												</td>
												<td>
													<input type="text" class="input-text" name="lossCarMainList[${status.index }].sumVeriLossFee" value="${lossCarMain.sumVeriLossFee }"
														readonly="readonly" />
												</td>
												<td>
													<input type="text" class="input-text" name="lossCarMainList[${status.index }].invoiceFee" value="${lossCarMain.invoiceFee }" />
												</td>
											</tr>
										</c:forEach>
										<c:forEach var="lossPropMain" items="${lossPropMainList}" varStatus="status">

											<input type="hidden" class="input-text" name="lossPropMainList[${status.index }].id" value="${lossPropMain.id }" />
											<tr>
												<th>财产损失</th>
												<td>
													<input type="text" class="input-text"
														value=" <c:forEach var="prpLdlossPropFee" items="${lossPropMain.prpLdlossPropFees}" varStatus="statu">${prpLdlossPropFee.lossItemName }  </c:forEach>"
														readonly="readonly" />
												</td> 
												<td>
													
												</td>
												<td>
													<input type="text" class="input-text" name="lossPropMainList[${status.index }].sumVeriLoss" value="${lossPropMain.sumVeriLoss }"
														readonly="readonly" />
												</td>
												
												<td>
													<input type="text" class="input-text" name="lossPropMainList[${status.index }].invoiceFee" value="${lossPropMain.invoiceFee }" />
												</td>
											</tr>
										</c:forEach>
										<c:forEach var="lossPersTrace" items="${lossPersTraceList}" varStatus="status">
											<tr>
												<th>人伤损失</th>
												<input type="hidden" class="input-text" name="lossPersTraceList[${status.index }].id" value="${lossPersTrace.id }" />
												<td>
													<input type="text" class="input-text" name="lossPersTraceList[${status.index }].personName" value="${lossPersTrace.personName }"
														readonly="readonly" />
												</td>
												<td>
													<input type="text" class="input-text" name="lossPersTraceList[${status.index }].remark" value="${lossPersTrace.remark }"
														readonly="readonly" />
												</td>
												<td>
													<input type="text" class="input-text" name="lossPersTraceList[${status.index }].sumVeriDefloss" value="${lossPersTrace.sumVeriDefloss }"
														readonly="readonly" />
												</td>
												<td>
													<input type="text" class="input-text" name="lossPersTraceList[${status.index }].invoiceFee" value="${lossPersTrace.invoiceFee }" />
												</td>
											</tr>
										</c:forEach>
										<%-- <tbody class="text-c">
				                         <tr>
				                         <th>定损金额</th>
				                         <td> <input type="text" class="input-text" name="defLossCar" value="${defLossCar }" readonly="readonly" /></td>
				                         <td> <input type="text" class="input-text" name="defLossProp" value="${defLossProp }" readonly="readonly"/></td>
				                         <td> <input type="text" class="input-text" name="defLossPersion" value="${defLossPersion }" readonly="readonly"/></td>
				                         </tr>
				                          <tr>
				                         <th>发票金额</th>
				                        <td> <input type="text" class="input-text" name="prpLCertifyMainVo.defLossCar" value="${prpLCertifyMainVo.defLossCar }" /></td>
				                         <td> <input type="text" class="input-text" name="prpLCertifyMainVo.defLossProp" value="${prpLCertifyMainVo.defLossProp }" /></td>
				                         <td> <input type="text" class="input-text" name="prpLCertifyMainVo.defLossPersion" value="${prpLCertifyMainVo.defLossPersion }" /></td>
				                         </tr>
				                         </tbody> --%>
									</thead>
								</table>
							</div>
							<div class="table_title f14">确认信息</div>
								<div class="table_cont mt-30 md-30">
								<div class="formtable ">
									<div class="row cl">
										<label class="form_label col-2">客户索赔时间：</label>
											<div class="form_input col-4">
												<input type="text" class="Wdate" id="customClaimTime" name="prpLCertifyMainVo.customClaimTime"
												value="<fmt:formatDate value='${prpLCertifyMainVo.customClaimTime}' pattern='yyyy-MM-dd HH:mm:ss'/>"
													onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'claimEndTime\')||\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
													style="width: 97%" /> 
											</div>
										<label class="form_label col-2">补充材料一次性告知时间：</label>
											<div class="form_input col-4">
											<input type="text" class="Wdate" id="addNotifyTime"
										           name="prpLCertifyMainVo.addNotifyTime"
										           value="<fmt:formatDate value='${prpLCertifyMainVo.addNotifyTime}' pattern='yyyy-MM-dd HH:mm:ss'/>"
										           onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'time\')||\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
									           	style="width: 97%">
									         <input type="hidden" value="" id="time"/>
											</div>
											
									</div>
								</div>
								
								<div class="formtable ">
									<div class="row cl">
										<label class="form_label col-2">索赔材料收集齐全时间：</label>
										<div class="form_input col-4">
										<input type="text" class="input-text" id="claimEndTime"
										           readOnly
										           value="<fmt:formatDate value='${prpLCertifyMainVo.claimEndTime}' pattern='yyyy-MM-dd HH:mm:ss'/>"
									           	style="width: 97%">
										</div>
										<label class="form_label col-2">核损总金额：</label>
										<div class="form_input col-4">
										    <td>
												<input type="text" class="input-text" name="sumVeriLossFee" value="${sumVeriLossFee }"
													readonly="readonly" />
											</td>
										</div>
									</div>
								
									
									<div class="row cl">
										<label class="form_label col-2">行驶证是否有效：</label>
										<div class="form_input col-4">
											<span class="radio-box">
												<app:codeSelect codeType="IsValid" type="radio" value="${prpLCertifyMainVo.driveLicenceFlag}"  name="prpLCertifyMainVo.driveLicenceFlag" nullToVal="0" />
											</span>
										</div>
										
										<label class="form_label col-2">驾驶证是否有效：</label>
										<div class="form_input col-4">
											<span class="radio-box">
												<app:codeSelect codeType="IsValid" type="radio" value="${prpLCertifyMainVo.carLicenceFlag}"  name="prpLCertifyMainVo.carLicenceFlag" nullToVal="0" />
											</span>
										</div>
									</div>
									
								<div class="row cl">
									<label class="form_label col-2">转人工理算原因：</label>
							           <div class="form_input col-6">
								             <app:codeSelect codeType="DZCOMPENCAUSE" name="prpLCertifyMainVo.manCompenCause" 
								                  value="${prpLCertifyMainVo.manCompenCause}" type="checkbox"/>
								                   <input type="hidden" id="manCompenCause" value="${prpLCertifyMainVo.otherManCompenCause}"/>
								                 
							         </div>
									  <div class="hide" id="otherManCompenCause">
									  <label class="form_label col-1" style="text-align: left;">其它原因:</label>
								                  <div class="form_input col-3" style="text-align: left;">
								                  <input type="text" name="prpLCertifyMainVo.otherManCompenCause" value="${prpLCertifyMainVo.otherManCompenCause}" id="compensateCause" />
								                  <font class="c-red">*</font>
								                  </div>
								       </div>
								</div>
							</div>
						</div>
					</div>
					</div>
					<!--损失照片页面 开始-->
					<div class="tabCon">
						<div class="table_wrap">
						<div class="table_title f14" >损失照片</div>
						<div class="row cl">
						<div class="table_cont col-4">
							<table class="table table-bordered table-bg" style="width：50%">
									<thead class="text-c">
										<tr>
											<th>照片类型</th>
											<th>已上传数量</th>
										</tr>
									</thead>
									<tbody class="text-c" id="imagesInfo">
									 <%-- <c:forEach var="imagesMap" items="${imagesMap}" varStatus="status">
										<tr>
											<td>${imagesMap.key}</td>
											<td>${imagesMap.value}</td>
										</tr>
									</c:forEach>	 --%>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
					<!--收款人信息页面 开始-->
					<div class="tabCon">
						<div class="table_wrap" id="payCustomDiv">
							<%@include file="certifyPayCustom.jsp"%>
						</div>
					</div>
					
				</div>
			</div>
		</form>
	
	<div class="text-c mt-10">
		<input class="btn btn-primary ml-5" id="save" onclick="saveCertify('submit')" type="button" value="提交" /> 
		<input class="btn btn-primary ml-5" id="tempSave" onclick="saveCertify('save')" type="button" value="暂存" /> 
	</div>
	</div>
    <script type="text/javascript" src="${ctx }/js/certify/certify.js"></script>
    <script type="text/javascript" src="${ctx }/js/certify/subrogation.js"></script>
    <script type="text/javascript" src="/claimcar/js/common/application.js"></script>
</body>

</html>