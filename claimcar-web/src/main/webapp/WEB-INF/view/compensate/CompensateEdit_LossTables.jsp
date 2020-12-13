<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<div id="lossTables">
<div class="table_wrap" id="carLossDiv">
<c:if test="${!empty prpLLossItems}">
<div class="table_title f14">车辆损失信息</div>
<div class="table_cont">
	<div class="formtable">
	<c:set var="lockIndex" value="-1" />
	<c:if test="${flag eq '1' }">
		<div class="table_cont">
			<table name="cpo_0" class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th>赔付类型</th>
						<th>险别</th>
						<th>损失类型</th>
						<th>车牌号</th>
						<th>损失金额</th>
						<th>施救费</th>
						<th>核定金额</th>
						<c:if test="${prpLCompensate.deductType eq '1' }">
							<th>应扣预付金额<font class="must">*</font></th>
						</c:if>
						<c:if test="${prpLCompensate.deductType eq '2' }">
							<th>应扣垫付金额<font class="must">*</font></th>
						</c:if>
						<c:if test="${dwFlag ne '0' }">
							<th>清付金额明细</th>
						</c:if>
					</tr>
				</thead>
				<tbody class="text-c">
					<c:forEach var="prpLLossItem" items="${prpLLossItems }"  varStatus="status">
					<c:if test="${prpLLossItem.payFlag ne '4' }">
					<tr>
						<!-- 隐藏域 -->
						<input type="hidden" name="prpLLossItem[${status.index }].dlossId" value="${prpLLossItem.dlossId }" />
						<input type="hidden" name="prpLLossItem[${status.index }].riskCode" value="${prpLLossItem.riskCode }" />
						<input type="hidden" name="prpLLossItem[${status.index }].itemId" value="${prpLLossItem.itemId }" />
						<input type="hidden" name="prpLLossItem[${status.index }].standardPrice" value="${prpLLossItem.standardPrice }" />
						<input type="hidden" name="prpLLossItem[${status.index }].depreMonthRate" value="${prpLLossItem.depreMonthRate }" />
						<input type="hidden" name="prpLLossItem[${status.index }].depreMonths" value="${prpLLossItem.depreMonths }" />
						<input type="hidden" name="prpLLossItem[${status.index }].itemAmount" value="${prpLLossItem.itemAmount }" />
						<input type="hidden" name="prpLLossItem[${status.index }].id" value="${prpLLossItem.id }" />
						<input type="hidden" name="prpLLossItem[${status.index }].dlossIdExt" value="${prpLLossItem.dlossIdExt }" />
						<!-- 隐藏域 -->
						<td> <app:codetrans codeType="PayFlag" codeCode="${prpLLossItem.payFlag }"/>
						<input type="hidden" name="prpLLossItem[${status.index }].payFlag" value="${prpLLossItem.payFlag }" /></td>
						<td><app:codetrans codeType="KindCode" codeCode="${prpLLossItem.kindCode }" riskCode="${riskCode }" />
						<input type="hidden" name="prpLLossItem[${status.index }].kindCode" value="${prpLLossItem.kindCode }" /></td>
						<td>
						<app:codetrans codeType="LossTypeCar" codeCode="${prpLLossItem.lossType }"/>
						<input type="hidden" name="prpLLossItem[${status.index }].lossType" value="${prpLLossItem.lossType }" />
						</td>
						<td>${prpLLossItem.itemName}
						<input type="hidden" name="prpLLossItem[${status.index }].itemName" value="${prpLLossItem.itemName}" readonly /></td>
						<td>
						<input type="text"  class="input-text" name="prpLLossItem[${status.index }].sumLoss" value="${prpLLossItem.sumLoss}" onchange="isSumFeeLower(this,'prpLLossItem','${status.index }',1)"  datatype="amount"/>
						<input type="hidden"  class="input-text" name="prpLLossItem[${status.index }].originLossFee" value="${prpLLossItem.originLossFee }" /></td>
						<td>
						<input type="text" class="input-text" name="prpLLossItem[${status.index }].rescueFee" value="${empty prpLLossItem.rescueFee?0.00:prpLLossItem.rescueFee }" datatype="amount"/></td>
						<td>
						<input type="text" class="input-text" name="prpLLossItem[${status.index }].sumRealPay" readonly datatype="amount" value="${prpLLossItem.sumRealPay }" onchange="changeRealPay()"/></td>
						<c:if test="${prpLCompensate.deductType ne '3' }">
						<td>
						<input type="text" class="input-text" name="prpLLossItem[${status.index }].offPreAmt" 
									value="${empty prpLLossItem.offPreAmt?0.00:prpLLossItem.offPreAmt }" datatype="amount" nullmsg="应扣预付金额不能为空"/>
						</td>
						</c:if>
						<c:if test="${dwFlag ne '0' }">
							<td>
								<c:if test="${prpLLossItem.payFlag eq '2' }">
								<input type="button" class="btn rateBtn" name="dwInfoBtn" onclick="showDw(this)" value="..." />
								<div id="dWDiv" class="hide">
								<!-- 结算码、车牌号码、责任对方保险公司、代交强险/商业三者险、代付金额。代付金额需手动录入，其他为只读，且所有代付金额的总和必须等于理算页面的核定金额 -->
								<table name="cpo_0" class="table table-bordered table-bg">
									<thead class="text-c">
										<tr>
											<th>结算码</th>
											<th>车牌号码</th>
											<th>责任对方保险公司</th>
											<th>代交强险/商业三者险</th>
											<th>清付金额</th>
										</tr>
									</thead>
									<tbody class="text-c">
										<c:forEach items="${prpLLossItem.platLockList }" var="platLockVo">
											<tr>
												<c:set var="lockIndex" value="${lockIndex+1 }" />
												<td>${platLockVo.recoveryCode }
													<input type="hidden" name="prpLPlatLockVo[${lockIndex }].id" value="${platLockVo.id }">
													<input type="hidden" name="prpLPlatLockVo[${lockIndex }].recoveryCode" value="${platLockVo.recoveryCode }">	
													<input type="hidden" name="prpLPlatLockVo[${lockIndex }].recoveryOrPayFlag" value="${platLockVo.recoveryOrPayFlag }">
												 </td>
												<td>${platLockVo.oppoentLicensePlateNo } </td>
												<td><app:codetrans codeType="DWInsurerCode" codeCode="${platLockVo.oppoentInsurerCode }"/> </td>
												<td><app:codetrans codeType="DWCoverageType" codeCode="${platLockVo.recoveryOrPayType }"/></td>
												<td><input type="text" class="input-text" name="prpLPlatLockVo[${lockIndex }].thisPaid"  
													 datatype="amount"	value="${platLockVo.thisPaid}">
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<br/>
								<input type="button" class="btn btn-primary rateBtn" name="closeBtn" onclick="closeDw(this)" value="关闭" />			
							</div>
							</c:if>
						 	</td>
						</c:if>
					</tr>
					</c:if>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<p>
			<!-- 无责代赔 -->
			<div class="table_cont" id="nodutyLossDiv">
			<table name="cpo_0" class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th width="5%">赔付类型</th>
						<th width="10%">险别</th>
						<th width="9%">代赔车牌号</th>
						<th width="8%">标的车辆损失小计</th>
						<th width="8%">标的车辆施救费</th>
						<th width="10%">核定代赔金额</th>
						<c:if test="${prpLCompensate.deductType eq '1' }">
							<th width="10%">应扣预付金额</th>
						</c:if>
						<c:if test="${prpLCompensate.deductType eq '2' }">
							<th width="10%">应扣垫付金额</th>
						</c:if>
						<th width="16%">代赔保险公司</th>
						<th width="16%">代赔保单号</th>
						<th width="8%">是否赔付</th>
					</tr>
				</thead>
				<tbody class="text-c">
				<c:forEach var="prpLLossItem" items="${prpLLossItems }" varStatus="status">
					<c:if test="${prpLLossItem.payFlag eq '4' }">
						<tr>
							<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].dlossId" value="${prpLLossItem.dlossId }" />
							<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].riskCode" value="${prpLLossItem.riskCode }" />
							<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].itemId" value="${prpLLossItem.itemId }" />
							<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].standardPrice" value="${prpLLossItem.standardPrice }" />
							<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].depreMonthRate" value="${prpLLossItem.depreMonthRate }" />
							<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].depreMonths" value="${prpLLossItem.depreMonths }" />
							<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].id" value="${prpLLossItem.id }" />
							<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].dlossIdExt" value="${prpLLossItem.dlossIdExt }" />
							<td>无责代赔
							<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].payFlag" value="4" /></td>
							<td><app:codetrans codeType="KindCode" codeCode="${prpLLossItem.kindCode }" riskCode="${riskCode }"/>
							<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].kindCode" value="${prpLLossItem.kindCode }" /></td>
							<td>${prpLLossItem.itemName}
							<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].itemName" value="${prpLLossItem.itemName}"/></td>
							<td>${prpLLossItem.sumLoss}
							<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].sumLoss" value="${prpLLossItem.sumLoss}" />
							<%-- <input type="text" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].sumLoss" value="${prpLLossItem.sumLoss}" onchange="isSumFeeLower(this,'prpLLossItem','${status.index + fn:length(prpLLossItems)}',1)"/>
							<input type="hidden"  class="input-text" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].originLossFee" value="${prpLLossItem.originLossFee }" /> --%></td>
							<td>${prpLLossItem.rescueFee }
							<input type="hidden" class="input-text" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].rescueFee" 
										value="${empty prpLLossItem.rescueFee?0.00:prpLLossItem.rescueFee }"/></td>
							<td><input type="text" class="input-text" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].sumRealPay" 
										datatype="amount" value="${prpLLossItem.sumRealPay }"  onchange="changeRealPay()"/></td>
							<c:if test="${(prpLCompensate.deductType eq '1') || (prpLCompensate.deductType eq '2')}">
							<td><input type="text" class="input-text" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].offPreAmt" 
										datatype="amount" value="${prpLLossItem.offPreAmt }"/></td>
							</c:if>
							<td>
							<c:choose>
								<c:when test="${ (compWfFlag eq '1') || (compWfFlag eq '2') }">
									<app:codetrans codeType="CIInsurerCompany" codeCode="${prpLLossItem.insuredComCode }"/>
									<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].insuredComCode" value="${prpLLossItem.insuredComCode}"/>
								</c:when>
								<c:otherwise >
									<app:codeSelect codeType="CIInsurerCompany" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].insuredComCode" 
										value="${prpLLossItem.insuredComCode }" type="select"/>
								</c:otherwise>
							</c:choose>
							</td>
							<td> <input type="text" class="input-text" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].ciPolicyNo" 
										value="${prpLLossItem.ciPolicyNo}"/> </td>
							<td> 
							<c:choose>
								<c:when test="${(compWfFlag eq '1') || (compWfFlag eq '2') }">
									<app:codetrans codeType="YN10" codeCode="${prpLLossItem.noDutyPayFlag }"/>
									<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].noDutyPayFlag" value="${prpLLossItem.noDutyPayFlag}"/>
								</c:when>
								<c:otherwise >
									<app:codeSelect codeType="YN10" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].noDutyPayFlag" 
										value="${prpLLossItem.noDutyPayFlag }" clazz="must"  type="select" />
								</c:otherwise>
							</c:choose>
							</td>
						</tr>
						</c:if>
					</c:forEach>
					</tbody>
				</table>
			</div>
			</c:if>
			<c:if test="${flag eq '2' }">
			<table name="cpo_0" class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th>险别</th>
						<th>赔付类别</th>
						<th>损失类型</th>
						<th>车牌号</th>
						<th>损失金额</th>
						<th>施救费</th>
						<!-- <th>其它扣除</th> -->
						<th>责任比例%</th>
						<th>定损扣交强</th>
						<th>施救费扣交强</th>
						<th>无责代赔金额</th>
						<th>核定金额</th>
						<c:if test="${prpLCompensate.deductType eq '1' }">
							<th>应扣预付金额<font class="must">*</font></th>
						</c:if>
						<c:if test="${prpLCompensate.deductType eq '2' }">
							<th>应扣垫付金额<font class="must">*</font></th>
						</c:if>
						<th>免赔率信息</th>
						<c:if test="${dwFlag ne '0' }">
							<th>代付金额明细</th>
							<th>第三方已赔付金额</th>
						</c:if>
					</tr>
				</thead>
				<tbody class="text-c">
				<c:forEach var="prpLLossItemVo" items="${prpLLossItems}" varStatus="status">
				<c:if test="${prpLLossItem.payFlag ne '4' }">
					<tr>
						<input type="hidden" name="prpLLossItem[${status.index }].dlossId" value="${prpLLossItemVo.dlossId }" />
						<input type="hidden" name="prpLLossItem[${status.index }].riskCode" value="${prpLLossItemVo.riskCode }"/>
						<input type="hidden" name="prpLLossItem[${status.index }].itemId" value="${prpLLossItemVo.itemId }" />
						<input type="hidden" name="prpLLossItem[${status.index }].standardPrice" value="${prpLLossItemVo.standardPrice }" />
						<input type="hidden" name="prpLLossItem[${status.index }].depreMonthRate" value="${prpLLossItemVo.depreMonthRate }" />
						<input type="hidden" name="prpLLossItem[${status.index }].depreMonths" value="${prpLLossItemVo.depreMonths }" />
						<input type="hidden" name="prpLLossItem[${status.index }].itemAmount" value="${prpLLossItemVo.itemAmount }" />
						<input type="hidden" name="prpLLossItem[${status.index }].id" value="${prpLLossItemVo.id }" />
						<input type="hidden" name="prpLLossItem[${status.index }].dlossIdExt" value="${prpLLossItemVo.dlossIdExt }" />
						
						<td>
							<app:codetrans codeType="KindCode" codeCode="${prpLLossItemVo.kindCode }" riskCode="${riskCode }"/>
							<input type="hidden" name="prpLLossItem[${status.index }].kindCode"  value="${prpLLossItemVo.kindCode }" />
						<td>
							<app:codetrans codeType="PayFlag" codeCode="${prpLLossItemVo.payFlag }"/>
							<input type="hidden" name="prpLLossItem[${status.index }].payFlag" value="${prpLLossItemVo.payFlag }" />
						</td>
						<td>
							<app:codetrans codeType="LossTypeCar" codeCode="${prpLLossItemVo.lossType }"/>
							<input type="hidden" name="prpLLossItem[${status.index }].lossType" value="${prpLLossItemVo.lossType }" />
						</td>
						<td>${prpLLossItemVo.itemName }
						<input type="hidden" name="prpLLossItem[${status.index }].itemName" value="${prpLLossItemVo.itemName }"/></td>
						<td>
						<input type="text" class="input-text" name="prpLLossItem[${status.index }].sumLoss" value="${prpLLossItemVo.sumLoss}" onchange="isSumFeeLower(this,'prpLLossItem','${status.index }',1)" datatype="amount"/>
						<input type="hidden"  class="input-text" name="prpLLossItem[${status.index }].originLossFee" value="${prpLLossItemVo.originLossFee}"  /></td>
						<td>
						<input type="text" class="input-text" name="prpLLossItem[${status.index }].rescueFee" value="${empty prpLLossItemVo.rescueFee?0.00:prpLLossItemVo.rescueFee }" datatype="amount"/></td>
						<input type="hidden" class="input-text" name="prpLLossItem[${status.index }].otherDeductAmt" value="${empty prpLLossItemVo.otherDeductAmt?0.00:prpLLossItemVo.otherDeductAmt }" datatype="amount" nullmsg="请填写其他扣除！"/>
						<td><input type="text" class="input-text" name="prpLLossItem[${status.index }].dutyRate" datatype="*" maxlength="3"
								nullmsg="请填写责任比例！" value="${prpLLossItemVo.dutyRate }"/></td>
						<td><input type="text" class="input-text" name="prpLLossItem[${status.index }].bzPaidLoss" readonly value="${prpLLossItemVo.bzPaidLoss }"/></td>
						<td><input type="text" class="input-text" name="prpLLossItem[${status.index }].bzPaidRescueFee" value="${prpLLossItemVo.bzPaidRescueFee }"/></td>
						<td>
						<input type="text" class="input-text" name="prpLLossItem[${status.index }].absolvePayAmt" value="${prpLLossItemVo.absolvePayAmt}"/>
						<input type="hidden" class="input-text" name="prpLLossItem[${status.index }].absolvePayAmts" value="${prpLLossItemVo.absolvePayAmts}"/>
						</td>
						<td><input type="text" class="input-text" name="prpLLossItem[${status.index }].sumRealPay" readonly datatype="amount" nullmsg="核定金额不能为空！" value="${prpLLossItemVo.sumRealPay }"  onchange="changeRealPay()"/>
						<input type="hidden" name="prpLLossItem[${status.index }].deductOffAmt" value="${prpLLossItemVo.deductOffAmt }" /><!-- 无责代培 -->
						</td>
						<!-- 存在商业预付时显示应扣预付 -->
						<c:if test="${prpLCompensate.deductType ne '3' }">
						<td><input type="text" class="input-text" name="prpLLossItem[${status.index }].offPreAmt" 
										value="${empty prpLLossItemVo.offPreAmt?0.00:prpLLossItemVo.offPreAmt }" datatype="amount" nullmsg="应扣预付金额不能为空"/></td>
						</c:if>
						<td>
						<input type="button" class="btn rateBtn" name="RateInfoBtn" onclick="showRate(this)" value="..." />
							<div id="rateDiv" class="hide">
								<div class="row cl">
						     		<label class="form_label col-6">事故免赔率</label>
									<div class="form_input col-6">
								        <input type="text" class="input-text" name="prpLLossItem[${status.index }].deductDutyRate" readonly value="${prpLLossItemVo.deductDutyRate }"/>
								        <input type="hidden"  name="prpLLossItem[${status.index }].deductDutyAmt" value="${prpLLossItemVo.deductDutyAmt }"/>
									</div>
								</div>
								<div class="row cl">
						     		<label class="form_label col-6">绝对免赔率</label>
									<div class="form_input col-6">
								        <input type="text" class="input-text" name="prpLLossItem[${status.index }].deductAbsRate" readonly value="${prpLLossItemVo.deductAbsRate }"/>
								        <input type="hidden"  name="prpLLossItem[${status.index }].deductAbsAmt" value="${prpLLossItemVo.deductAbsAmt }"/>
									</div>
								</div>
								<div class="row cl">
						     		<label class="form_label col-6">加扣免赔率</label>
									<div class="form_input col-6">
								        <input type="text" class="input-text" name="prpLLossItem[${status.index }].deductAddRate" readonly value="${prpLLossItemVo.deductAddRate }"/>
								        <input type="hidden"  name="prpLLossItem[${status.index }].deductAddAmt" value="${prpLLossItemVo.deductAddAmt }"/>
									</div>
								</div>
							</div>
						</td>
						
						<c:if test="${dwFlag ne '0' }">
							<td>
								<c:if test="${prpLLossItemVo.payFlag eq '1' || prpLLossItemVo.payFlag eq '2' }">
								<input type="button" class="btn rateBtn" name="dwInfoBtn" onclick="showDw(this)" value="..." />
								<div id="dWDiv" class="hide">
								<c:if test="${dwPersFlag ne '1' }">
								<!-- 结算码、车牌号码、责任对方保险公司、代交强险/商业三者险、代付金额。代付金额需手动录入，其他为只读，且所有代付金额的总和必须等于理算页面的核定金额 -->
								<table name="cpo_0" class="table table-bordered table-bg">
									<thead class="text-c">
										<tr>
											<th>结算码</th>
											<th>车牌号码</th>
											<th>责任对方保险公司</th>
											<th>代交强险/商业三者险</th>
											<c:if test="${prpLLossItemVo.payFlag eq '1'}">
												<th>代付金额</th>
											</c:if>
											<c:if test="${prpLLossItemVo.payFlag eq '2'}">
												<th>清付金额</th>
											</c:if>
										</tr>
									</thead>
									<tbody class="text-c">
										<c:forEach items="${prpLLossItemVo.platLockList }" var="platLockVo">
											<c:set var="lockIndex" value="${lockIndex+1 }" />
											<tr>
												<td>${platLockVo.recoveryCode }
													<input type="hidden" name="prpLPlatLockVo[${lockIndex }].id" value="${platLockVo.id }">
													<input type="hidden" name="prpLPlatLockVo[${lockIndex }].recoveryCode" value="${platLockVo.recoveryCode }">	
													<input type="hidden" name="prpLPlatLockVo[${lockIndex }].recoveryOrPayFlag" value="${platLockVo.recoveryOrPayFlag }">
												 </td>
												<td>${platLockVo.oppoentLicensePlateNo } </td>
												<td><app:codetrans codeType="DWInsurerCode" codeCode="${platLockVo.oppoentInsurerCode }"/> </td>
												<td><app:codetrans codeType="DWCoverageType" codeCode="${platLockVo.recoveryOrPayType }"/></td>
												<td><input type="text" class="input-text" name="prpLPlatLockVo[${lockIndex }].thisPaid"  
													 datatype="amount"	value="${platLockVo.thisPaid}">
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>	
								</c:if>
								<c:if test="${dwPersFlag eq '1' }">
									<table name="cpo_0" class="table table-bordered table-bg">
									<thead class="text-c">
										<tr>
											<th>姓名</th>
											<th>身份证号码</th>
											<th>代付金额</th>
										</tr>
									</thead>
									<tbody class="text-c">
										<c:forEach items="${prpLLossItemVo.subrogationPersonVoList }" var="subrogationPersonVo" varStatus="substatus">
											<input type="hidden" name="subrogationPersonVo[${substatus.index }].id"  
													 value="${subrogationPersonVo.id}"> 
											<td>${subrogationPersonVo.name } 
												<input type="hidden" name="subrogationPersonVo[${substatus.index }].name"  
													 value="${subrogationPersonVo.name}"> 
											</td>
											<td>${subrogationPersonVo.identifyNumber } 
												<input type="hidden" name="subrogationPersonVo[${substatus.index }].identifyNumber"  
													 value="${subrogationPersonVo.identifyNumber}"> 
											</td>
											<td>
											<input type="text" class="input-text" name="subrogationPersonVo[${substatus.index }].thisPaid"  
													 datatype="amount"	value="${subrogationPersonVo.thisPaid}"> 
											</td>
										</c:forEach>
									</tbody>
									</table>
								</c:if>
								<br/>
								<input type="button" class="btn btn-primary rateBtn" name="closeBtn" onclick="closeDw(this)" value="关闭" />
							</div>
							</c:if>
						 	</td>
							<c:if test="${prpLLossItemVo.payFlag eq '1' }">
							<td>
							<input type="text" class="input-text" name="prpLLossItem[${status.index }].thirdPaidAmt" 
								datatype="amount" value="${prpLLossItem.thirdPaidAmt }" nullmsg = "请填写第三方已赔付金额"/>
							</td>
							</c:if>
						</c:if>
					</tr>
					</c:if>
				</c:forEach>
					
				</tbody>
			</table>
		</c:if>
		</div>
	</div>
	</c:if>
</div>


<div class="table_wrap" id="propLossDiv">
<div class="table_title f14">财产损失信息</div>
<div class="table_cont">
	<div class="formtable">
			<table name="cpo_0" class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th>险别</th>
						<th>损失类型</th>
						<th>财物名称</th>
						<th>车牌号</th>
						<th>损失金额</th>
						<th>施救费</th>
						<c:if test="${flag eq '2' }">
							<th class="c-red">责任比例%</th>
							<th>定损扣交强</th>
							<th>施救费扣交强</th>
						</c:if>
							<th>核定金额</th>
						<c:if test="${prpLCompensate.deductType eq '1' }">
							<th>应扣预付金额<font class="must">*</font></th>
						</c:if>
						<c:if test="${prpLCompensate.deductType eq '2' }">
							<th>应扣垫付金额<font class="must">*</font></th>
						</c:if>
						<c:if test="${flag eq '2' }">
							<th>免赔率信息</th>
						</c:if>
					</tr>
				</thead>
				<tbody class="text-c">
				<c:forEach var="prpLLossProp" items="${prpLLossProps }" varStatus="status">
					<c:if test="${prpLLossProp.propType eq '1' }">
					<tr>
					
						<input type="hidden" name="prpLLossProp[${status.index }].propType" value="${prpLLossProp.propType }" />
						<input type="hidden" name="prpLLossProp[${status.index }].dlossId" value="${prpLLossProp.dlossId }" />
						<input type="hidden" name="prpLLossProp[${status.index }].riskCode" value="${prpLLossProp.riskCode }"/>
						<input type="hidden" name="prpLLossProp[${status.index }].itemId" value="${prpLLossProp.itemId }"/>
						<input type="hidden" name="prpLLossProp[${status.index }].itemAmount" value="${prpLLossProp.itemAmount }" />
						<input type="hidden" name="prpLLossProp[${status.index }].id" value="${prpLLossProp.id }" />
						<input type="hidden" name="prpLLossProp[${status.index }].dlossIdExt" value="${prpLLossProp.dlossIdExt }" />
						<td>
							<app:codetrans codeType="KindCode" codeCode="${prpLLossProp.kindCode }" riskCode="${riskCode }"/>
							<input type="hidden" name="prpLLossProp[${status.index }].kindCode" value="${prpLLossProp.kindCode }" />
						</td>
						<td>
							<app:codetrans codeType="LossTypeProp" codeCode="${prpLLossProp.lossType }"/>
							<input type="hidden" name="prpLLossProp[${status.index }].lossType" value="${prpLLossProp.lossType }" />
						</td>
						<td>
							<!-- 遍历所有的lossNamesSpan 字符长度超过5的 截取字符串并添加title属性悬浮显示 同时给隐藏域赋值-->
							<span id="lossNamesSpan">${prpLLossProp.lossName }</span>
							<input type="hidden" name="prpLLossProp[${status.index }].lossName" value="${prpLLossProp.lossName }"/>
						</td>
						<td>
							<c:if test="${prpLLossProp.lossType ne '0' }">
								${prpLLossProp.itemName}
								<input type="hidden" name="prpLLossProp[${status.index }].itemName" value="${prpLLossProp.itemName}" /></td>
							</c:if>
							<c:if test="${prpLLossProp.lossType eq '0' }">
								地面
								<input type="hidden" name="prpLLossProp[${status.index }].itemName" value="地面" /></td>
							</c:if>
						<td>
						<input type="text"  class="input-text" name="prpLLossProp[${status.index }].sumLoss" value="${prpLLossProp.sumLoss }" onchange="isSumFeeLower(this,'prpLLossProp','${status.index }',2)" datatype="amount"/>
							<input type="hidden"  class="input-text" name="prpLLossProp[${status.index }].originLossFee" value="${prpLLossProp.originLossFee}"  />
						</td>
						<td>
							<input type="text" class="input-text" name="prpLLossProp[${status.index }].rescueFee" value="${empty prpLLossProp.rescueFee?0.00:prpLLossProp.rescueFee }" datatype="amount"/>
						</td>
						<c:if test="${flag eq '2' }">
						<input type="hidden" class="input-text" name="prpLLossProp[${status.index }].otherDeductAmt" 
										value="${empty prpLLossProp.otherDeductAmt?0.00:prpLLossProp.otherDeductAmt }" datatype="amount" nullmsg="请填写其它扣除！"/>
							<td><input type="text" class="input-text" name="prpLLossProp[${status.index }].dutyRate" datatype="*" nullmsg="请填写责任比例！" value="${prpLLossProp.dutyRate }" maxlength="3"/></td>
							<td><input type="text" class="input-text" name="prpLLossProp[${status.index }].bzPaidLoss" readonly value="${prpLLossProp.bzPaidLoss }"/></td>
							<td><input type="text" class="input-text" name="prpLLossProp[${status.index }].bzPaidRescueFee"  value="${prpLLossProp.bzPaidRescueFee }"/></td>
						</c:if>
						<td><input type="text" class="input-text" name="prpLLossProp[${status.index }].sumRealPay" datatype="amount" value="${prpLLossProp.sumRealPay }" nullmsg="核定金额不能为空！"  onchange="changeRealPay()"/>
						<input type="hidden" name="prpLLossProp[${status.index }].deductOffAmt" value="${prpLLossProp.deductOffAmt }" /><!-- 无责代培 -->
						</td>
						<c:if test="${prpLCompensate.deductType ne '3' }">
						<td>
							<input type="text" class="input-text" name="prpLLossProp[${status.index }].offPreAmt" 
										value="${empty prpLLossProp.offPreAmt?0.00:prpLLossProp.offPreAmt }" datatype="amount" nullmsg="应扣预付金额不能为空"/>
						</td>
						</c:if>
						<c:if test="${flag eq '2' }">
							<td>
								<input type="button" class="btn rateBtn" name="" onclick="showRate(this)" value="..." />
								<div id="rateDiv" class="hide">
									<div class="row cl">
							     		<label class="form_label col-6">事故免赔率</label>
										<div class="form_input col-6">
									        <input type="text" class="input-text" name="prpLLossProp[${status.index }].deductDutyRate" readonly value="${prpLLossProp.deductDutyRate }"/>
									        <input type="hidden"  name="prpLLossProp[${status.index }].deductDutyAmt" value="${prpLLossProp.deductDutyAmt }"/>
										</div>
									</div>
									<div class="row cl">
							     		<label class="form_label col-6">绝对免赔率</label>
										<div class="form_input col-6">
									        <input type="text" class="input-text" name="prpLLossProp[${status.index }].deductAbsRate" readonly value="${prpLLossProp.deductAbsRate }"/>
									        <input type="hidden"  name="prpLLossProp[${status.index }].deductAbsAmt" value="${prpLLossProp.deductAbsAmt }"/>
										</div>
									</div>
									<div class="row cl">
							     		<label class="form_label col-6">加扣免赔率</label>
										<div class="form_input col-6">
									        <input type="text" class="input-text" name="prpLLossProp[${status.index }].deductAddRate" readonly value="${prpLLossProp.deductAddRate }"/>
									        <input type="hidden"  name="prpLLossProp[${status.index }].deductAddAmt" value="${prpLLossProp.deductAddAmt }"/>
										</div>
									</div>
								</div>
							</td>
						</c:if>
					</tr>
					</c:if>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>


<div class="table_wrap" id="persLossDiv">
<c:if test="${!empty prpLLossPersons }">
<div class="table_title f14">人员损失信息</div>
</c:if>
<div class="table_cont">
	<c:forEach var="prpLLossPerson" items="${prpLLossPersons }" varStatus="statusOut">
	<div class="formtable" name="personLossDiv_${statusOut.index }">
			<table name="personkindcode_${statusOut.index}" class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th>险别</th>
						<th>损失类型</th>
						<th>伤情类型</th>
						<th>姓名</th>
						<th>年龄</th>
						<th>车牌号</th>
						<c:if test="${flag eq '2' }">
							<th class="c-red">责任比例%</th>
							<th>事故免赔</th>
							<th>绝对免赔</th>
							<th>加扣免赔</th>
						</c:if>
					</tr>
				</thead>
				<tbody class="text-c">
					<tr>
						<input type="hidden" name="prpLLossPerson[${statusOut.index }].dlossId" value="${prpLLossPerson.dlossId }" />
						<input type="hidden" name="prpLLossPerson[${statusOut.index }].personId" value="${prpLLossPerson.personId }" />
						<input type="hidden" name="prpLLossPerson[${statusOut.index }].itemId" value="${prpLLossPerson.itemId }" />
						<input type="hidden" name="prpLLossPerson[${statusOut.index }].id" value="${prpLLossPerson.id }" />
						<input type="hidden" name="prpLLossPerson[${statusOut.index }].dlossIdExt" value="${prpLLossPerson.dlossIdExt }" />
						<td>
							<app:codetrans codeType="KindCode" codeCode="${prpLLossPerson.kindCode }" riskCode="${riskCode }"/>
							<input type="hidden" name="prpLLossPerson[${statusOut.index }].kindCode" value="${prpLLossPerson.kindCode }" />
						</td>
						<td>
							<app:codetrans codeType="LossType" codeCode="${prpLLossPerson.lossType}"/>
							<input type="hidden" name="prpLLossPerson[${statusOut.index }].lossType" value="${prpLLossPerson.lossType}" />
						</td>
						<td>
						<app:codetrans codeType="WoundCode" codeCode="${prpLLossPerson.injuryType}"/>
						<input type="hidden" name="prpLLossPerson[${statusOut.index }].injuryType" value="${prpLLossPerson.injuryType}" />
						</td>
						<td><input type="text" class="input-text" name="prpLLossPerson[${statusOut.index }].personName" value="${prpLLossPerson.personName }" readonly="readonly"/></td>
						<td><input type="text" class="input-text" name="prpLLossPerson[${statusOut.index }].personAge" value="${prpLLossPerson.personAge }" readonly="readonly"/></td>
						<td><input type="text" class="input-text" name="prpLLossPerson[${statusOut.index }].itemName" value="${prpLLossPerson.itemName }" readonly="readonly"/></td>
						<c:if test="${flag eq '2' }">
							<td><input type="text" class="input-text" name="prpLLossPerson[${statusOut.index }].dutyRate" datatype="*"  maxlength="3"
										nullmsg="请填写责任比例！" value="${prpLLossPerson.dutyRate }"/></td>
							<td><input type="text" class="input-text" name="prpLLossPerson[${statusOut.index }].deductDutyRate" readonly value="${prpLLossPerson.deductDutyRate }"/></td>
							<td><input type="text" class="input-text" name="prpLLossPerson[${statusOut.index }].deductAbsRate" readonly value="${prpLLossPerson.deductAbsRate }"/></td>
							<td><input type="text" class="input-text" name="prpLLossPerson[${statusOut.index }].deductAddRate" readonly value="${prpLLossPerson.deductAddRate }"/></td>
						</c:if>
					</tr>
					</tbody>
				</table>
			<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			<table name="personfee_${statusOut.index}" class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th>费用名称</th>
						<th>定损金额</th>
						<c:if test="${flag eq '2' }">
							<th>核减金额</th>
							<th>定损扣交强</th>
						</c:if>
						<th>核定金额</th>
						<c:if test="${prpLCompensate.deductType eq '1' }">
							<th>应扣预付金额<font class="must">*</font></th>
						</c:if>
						<c:if test="${prpLCompensate.deductType eq '2' }">
							<th>应扣垫付金额<font class="must">*</font></th>
						</c:if>
						<th>详情</th>
					</tr>
				</thead>
				<tbody class="text-c">
					<c:set var="personFee">prpLLossPerson[${statusOut.index }].prpLLossPersonFees</c:set>
					<c:forEach var="prpLLossPersonFee" items="${prpLLossPerson.prpLLossPersonFees }" varStatus="status">
					<tr>
						<td><app:codetrans codeType="DAALossItemType" codeCode="${prpLLossPersonFee.lossItemNo }"/>
						<input type="hidden" name="${personFee }[${status.index }].lossItemNo" value="${prpLLossPersonFee.lossItemNo }" readonly="readonly"/></td>
						<input type="hidden" name="${personFee }[${status.index }].id" value="${prpLLossPersonFee.id }" /></td>
						<td><input type="text" class="input-text" name="${personFee }[${status.index }].feeLoss" onblur="sumFee(this)" value="${prpLLossPersonFee.feeLoss }" readonly="readonly"/></td>
						<c:if test="${flag eq '2' }">
						<td><input type="text" class="input-text" name="${personFee }[${status.index }].feeOffLoss" onblur="sumFee(this)" 
									value="${empty prpLLossPersonFee.feeOffLoss?0.00:prpLLossPersonFee.feeOffLoss }" datatype="amount"/></td>
							<td><input type="text" class="input-text" name="${personFee }[${status.index }].bzPaidLoss" readonly onblur="sumFee(this)" datatype="amount" value="${prpLLossPersonFee.bzPaidLoss }"/></td>
						</c:if>
						<td><input type="text" class="input-text" name="${personFee }[${status.index }].feeRealPay" readonly onblur="sumFeees(this)" datatype="amount" value="${prpLLossPersonFee.feeRealPay }" nullmsg="核定金额不能为空"/>
						<input type="hidden" name="personFee[${status.index }].deductOffAmt" value="${prpLLossPersonFee.deductOffAmt }" /><!-- 无责代培 -->

						</td>
						<c:if test="${prpLCompensate.deductType ne '3' }">
						<td>
							<input type="text" class="input-text" name="${personFee }[${status.index }].offPreAmt" 
										value="${empty prpLLossPersonFee.offPreAmt?0.00:prpLLossPersonFee.offPreAmt }" datatype="amount" nullmsg="应扣预付金额不能为空"/>
						</td>
						</c:if>
						<td>
						<input type="button" class="btn rateBtn" name="showFeeBtn" onclick="showFee(this,'${prpLLossPersonFee.lossItemNo}')" value="..." />
							<div name="showFeeDiv_${status.index }" class="hide">
								<table name="personfeeDetail_${status.index}" class="table table-bordered table-bg">
									<thead class="text-c">
										<tr>
											<th>费用名称</th>
											<th>定损金额</th>
											<th>核定金额</th>
										</tr>
									</thead>
									<tbody class="text-c">
										<c:forEach var="prpLLossPersonFeeDetail" items="${prpLLossPersonFee.prpLLossPersonFeeDetails }" varStatus="detailStatus">
										<tr>
											<td><app:codetrans codeType="FeeType" codeCode="${prpLLossPersonFeeDetail.feeTypeCode }" />
											<input type="hidden" name="${personFee }[${status.index }].prpLLossPersonFeeDetails[${detailStatus.index }].feeTypeCode" value="${prpLLossPersonFeeDetail.feeTypeCode }" /></td>
											<input type="hidden" name="${personFee }[${status.index }].prpLLossPersonFeeDetails[${detailStatus.index }].lossItemNo" value="${prpLLossPersonFeeDetail.lossItemNo }" /></td>
											<input type="hidden" name="${personFee }[${status.index }].prpLLossPersonFeeDetails[${detailStatus.index }].id" value="${prpLLossPersonFeeDetail.id }" /></td>
											<td><input type="text" class="input-text"  readonly  value="${prpLLossPersonFeeDetail.lossFee }" name="${personFee }[${status.index }].prpLLossPersonFeeDetails[${detailStatus.index }].lossFee"/></td>
											<td><input type="text" class="input-text"  readonly  value="${prpLLossPersonFeeDetail.realPay }" name="${personFee }[${status.index }].prpLLossPersonFeeDetails[${detailStatus.index }].realPay"/></td>
										</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</td>
					</tr>
					</c:forEach>
					</tbody>
				</table>
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
				<div class="row cl mt-10" id="lossPersDiv_${statusOut.index }">
			     	<label class="form_label col-1">总定损金额</label>
					<div class="form_input col-2">
					     <input type="text" class="input-text" name="prpLLossPerson[${statusOut.index }].sumLoss" value="${prpLLossPerson.sumLoss }" id="feeLoss"  datatype="amount" onchange="isSumFeeLower(this,'prpLLossPerson','${statusOut.index }',3)" datatype="amount"/>  
					      <input type="hidden" class="input-text" name="prpLLossPerson[${statusOut.index }].originLossFee" value="${prpLLossPerson.originLossFee }"   />
					</div>
					<c:if test="${flag eq '2' }">
					<label class="form_label col-1">总核减金额</label>
					<div class="form_input col-2">
						<input type="text" class="input-text" name="prpLLossPerson[${statusOut.index }].sumOffLoss" value="${prpLLossPerson.sumOffLoss }" id="feeOffLoss" readonly datatype="amount"/>
					</div>
					<label class="form_label col-1">总交强赔付</label>
					<div class="form_input col-2">
						<input type="text" class="input-text" name="prpLLossPerson[${statusOut.index }].bzPaidLoss" value="${prpLLossPerson.bzPaidLoss }" id="bzPaidLoss" readonly datatype="amount"/>
					</div>
					</c:if>
					<label class="form_label col-1">实赔金额</label>
					<div class="form_input col-2">
						<input type="text" class="input-text" name="prpLLossPerson[${statusOut.index }].sumRealPay" value="${prpLLossPerson.sumRealPay }" id="feeRealPay" readonly datatype="amount"  onchange="changeRealPay()"/>
						<input type="hidden" class="input-text" name="prpLLossPerson[${statusOut.index }].offPreAmt" value="${prpLLossPerson.offPreAmt }" id="sumOffPreAmt" />
					</div>
		     	</div>
		      </div>
		     <p><hr style="height:1px;border:none;border-top:1px solid #0080FF;" /><p>
		     </c:forEach>
		     </div>
	</div>
		     
		 <c:if test="${isFraud=='0'||(isFraud=='1'&&fraudLogo=='03	') }">
		    <div class="table_wrap"  id="otherLossDiv">
		     <c:if test="${flag eq '2' }">
		     	<input type="hidden" name="OtherLossTbodyKindList" value="${kindForOthMap }">
				<div class="table_title f14">其它损失信息</div>

				<div class="table_cont">
					<div class="formtable">
							<table name="cpo_0" class="table table-bordered table-bg">
								<thead class="text-c">
									<tr>
										<th width="10%">
											<button type="button"  class="btn btn-plus Hui-iconfont Hui-iconfont-add" onclick="initOtherProp(this)"></button>
										</th>
										<th>附加险险别<font class="must">*</font></th>
										<th>车/财名称 <font class="must">*</font></th>
										<th>数量/天数</th>
										<th>定损金额</th>
										<!-- <th>残值/其他扣除</th> -->
										<th>核定金额</th>
										<th>责任比例%</th>
										<th>事故免赔</th>
										<th>绝对免赔</th>
										<th>加扣免赔</th>
									</tr>
								</thead>
								<tbody class="text-c" id="OtherLossTbody">
									<!-- 其他财产损失信息也保存在财产损失表中，因此序列号为财产损失记录条数++ -->
									<input type="hidden" id="othSize" value="${fn:length(otherLossProps)}">
									
									<%@include file="CompensateEdit_LossTables_OtheLo.jsp" %>
								</tbody>
							</table>
						</div>
					</div>
			</c:if>
</div>
</c:if>
</div>