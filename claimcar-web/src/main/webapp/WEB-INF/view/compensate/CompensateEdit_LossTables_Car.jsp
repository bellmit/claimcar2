<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

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
					<c:forEach var="prpLLossItem" items="${prpLLossItems }" varStatus="status">
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
						<td>${prpLLossItem.sumLoss}
						<input type="hidden" name="prpLLossItem[${status.index }].sumLoss" value="${prpLLossItem.sumLoss}" readonly /></td>
						<td>
						<input type="text" class="input-text" name="prpLLossItem[${status.index }].rescueFee" value="${empty prpLLossItem.rescueFee?0.00:prpLLossItem.rescueFee }" datatype="amount"/></td>
						<td>
						<input type="text" class="input-text" name="prpLLossItem[${status.index }].sumRealPay" readonly datatype="amount" value="${prpLLossItem.sumRealPay }"/></td>
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
						<th width="10%">代赔车牌号</th>
						<th width="8%">标的车辆损失小计</th>
						<th width="8%">标的车辆施救费</th>
						<th width="10%">核定代赔金额</th>
						<th width="20%">代赔保险公司</th>
						<th width="20%">代赔保单号</th>
						<th width="9%">是否赔付</th>
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
							<input type="hidden" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].sumLoss" value="${prpLLossItem.sumLoss}"/></td>
							<td>${prpLLossItem.rescueFee }
							<input type="hidden" class="input-text" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].rescueFee" 
										value="${empty prpLLossItem.rescueFee?0.00:prpLLossItem.rescueFee }"/></td>
							<td><input type="text" class="input-text" name="prpLLossItem[${status.index + fn:length(prpLLossItems)}].sumRealPay" 
										datatype="amount" value="${prpLLossItem.sumRealPay }"/></td>
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
						<th>其它扣除</th>
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
							<input type="hidden" name="prpLLossItem[${status.index }].kindCode"  value="${prpLLossItemVo.kindCode }" riskCode="${prpLLossItemVo.riskCode }" />
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
						<td>${prpLLossItemVo.sumLoss}
						<input type="hidden" name="prpLLossItem[${status.index }].sumLoss" value="${prpLLossItemVo.sumLoss}" /></td>
						<td>
						<input type="text" class="input-text" name="prpLLossItem[${status.index }].rescueFee" value="${empty prpLLossItemVo.rescueFee?0.00:prpLLossItemVo.rescueFee }" datatype="amount"/></td>
						<td><input type="text" class="input-text" name="prpLLossItem[${status.index }].otherDeductAmt" value="${empty prpLLossItemVo.otherDeductAmt?0.00:prpLLossItemVo.otherDeductAmt }" datatype="amount" nullmsg="请填写其他扣除！"/></td>
						<td><input type="text" class="input-text" name="prpLLossItem[${status.index }].dutyRate" datatype="*" 
								nullmsg="请填写责任比例！" value="${prpLLossItemVo.dutyRate }"/></td>
						<td><input type="text" class="input-text" name="prpLLossItem[${status.index }].bzPaidLoss" readonly value="${prpLLossItemVo.bzPaidLoss }"/></td>
						<td><input type="text" class="input-text" name="prpLLossItem[${status.index }].bzPaidRescueFee" value="${prpLLossItemVo.bzPaidRescueFee }"/></td>
						<td><input type="text" class="input-text" name="prpLLossItem[${status.index }].absolvePayAmt" value="${prpLLossItemVo.absolvePayAmt}"/></td>
						<td><input type="text" class="input-text" name="prpLLossItem[${status.index }].sumRealPay" readonly datatype="amount" nullmsg="核定金额不能为空！" value="${prpLLossItemVo.sumRealPay }"/>
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
		</c:if>
		</div>
	</div>
	</c:if>
