<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>


<c:if test="${flag eq '2' }">
	<div class="table_wrap">
		<div class="table_title f14">不计免赔赔款</div>
		<div class="table_cont">
			<div class="formtable" id="buji">
				<table class="table table-bordered table-bg">
					<%-- <tr>
						<th>不计免赔险别</th>  
						<c:forEach var="claimKindM" items="${claimKindMList }" varStatus="status">
							<td><app:codetrans codeType="KindCode" codeCode="${claimKindM.kindCode }"/>
							<input type="hidden" class="input-text" name="deductOffAmt[${status.index }].kindCodes" value="${claimKindM.kindCode }" />
							</td>
						</c:forEach>
					</tr>
					<tr>
						<th>不计免赔赔款</th>
						<c:forEach var="claimKindM" items="${claimKindMList }" varStatus="status">
							<td><input type="text" class="input-text" name="deductOffAmt[${status.index }]" readonly /></td>
						</c:forEach>
					</tr> --%>
					<tr>
						<th>不计免赔险别</th>
						<c:forEach var="claimKindM" items="${claimKindMList }"
							varStatus="status">
							<td><app:codetrans codeType="KindCode"
									codeCode="${claimKindM.kindCode }" riskCode="${riskCode }"/></td>
						</c:forEach>
					</tr>
					<tr>
						<th>不计免赔赔款</th>
						<c:forEach var="claimKindM" items="${claimKindMList }"
							varStatus="status">
							<input type="hidden" class="input-text"
								name="deductOffAmt[${status.index }].kindCodes"
								value="${claimKindM.kindCode }" />
							<td><input type="text" class="input-text"
								name="deductOffAmt[${status.index }].kindCodees" readonly /></td>
						</c:forEach>
					</tr>
				</table>
			</div>
		</div>
	</div>
</c:if>

<a class="btn  btn-primary mt-5" onclick="payCustomSearch('N')">收款人信息维护</a>
<div class="table_wrap">
<div class="table_title f14">收款人信息</div>
<div class="table_cont">
	<div class="formtable" id="paymentVo">
		<div class="table_con">
			<table class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th width="6%">
							<button type="button"  class="btn btn-plus Hui-iconfont Hui-iconfont-add" onclick="addPayInfo('PayCustomTbody','custSize','${ prpLWfTaskVo.handlerStatus }')"></button>
						</th>
						<th>收款人<font class="must">*</font></th>
						<c:if test="${dwFlag ne '0' && not empty dwFlag }">
							<th>赔付类别</th>
							<th width="10%">清付/代付车辆</th>
						</c:if>
						<th>例外标志</th>
						<th>例外原因</th>
						<th>收款人帐号<font class="must">*</font></th>
						<th>开户银行<font class="must">*</font></th>
						<th>赔款金额<font class="must">*</font></th>
						<th>税收信息</th>
						<th>摘要<font class="must">*</font></th>
						<th>是否增值税专票</th>
						<th>税率</th>
						<c:if test="${prpLWfTaskVo.handlerStatus eq '3' && isGbFlag != '2' && showFlag eq '1'}">
						<th>发票操作</th>
						</c:if>
						<!-- <th width="10%">操作</th> -->
					</tr>
				</thead>
				<tbody class="text-c" id="PayCustomTbody">
					<input type="hidden" id="custSize" value="${fn:length(prpLPaymentVos)}">
				<%@include file="CompensateEdit_PayInfos_Pers.jsp" %>
				</tbody>
			</table>
			</div>
		</div>
	</div>
</div>

<div class="table_wrap">
<input type="hidden" name="PayInfoTbodyKindList" value="${kindForChaMap }">
<div class="table_title f14">费用赔款信息</div>
<div class="table_cont" id="feeTables">
	<div class="formtable">
		<input type="hidden" name="PayInfoTbodyKindList" value="${kindForChaMap }">
		<div class="table_con">
			<table class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th width="6%">
							<button type="button"  class="btn btn-plus Hui-iconfont Hui-iconfont-add"   onclick="addPayInfo('PayInfoTbody','infoSize','${ prpLWfTaskVo.handlerStatus }')"></button>
						</th>
						<th>险别</th>
						<th>来源环节</th>
						<th>费用名称</th>
						<th>费用金额<font class="must">*</font></th>
						<th>扣减金额</th>
						<c:if test="${prpLCompensate.deductType eq '1' }">
							<th>应扣预付金额<font class="must">*</font></th>
						</c:if>
						<c:if test="${prpLCompensate.deductType eq '2' }">
							<th>应扣垫付金额<font class="must">*</font></th>
						</c:if>
						<th>收款人<font class="must">*</font></th>
						<th>收款方账号<font class="must">*</font></th>
						<th>开户银行<font class="must">*</font></th>
						<th>证件号码</th>
						<%--<th>发票类型</th>--%>
						<th>税收信息</th>
						<th>摘要<font class="must">*</font></th>
						<th>是否增值税专票</th>
						<th>税率</th>
						<c:if test="${prpLWfTaskVo.handlerStatus eq '3' && isGbFlag != '2' && showFlag eq '1'}">
						<th>发票操作</th>
						</c:if>
						<!-- <th width="5%">操作</th> -->

					</tr>
				</thead>
				<tbody class="text-c" id="PayInfoTbody">
					<input type="hidden" id="infoSize" value="${fn:length(prpLChargeVos)}">
					<c:forEach var="prpLCharge" items="${prpLChargeVos }" varStatus="status">
					<tr class="detail-body">
						<input type="hidden" name="prpLCharge[${status.index}].currency" value="CNY"/>
						<input type="hidden" name="prpLCharge[${status.index}].validFlag" value="1"/>
						<input type="hidden" name="prpLCharge[${status.index}].businessId" value="${prpLCharge.businessId }"/>
						<input type="hidden" name="prpLCharge[${status.index}].id" value="${prpLCharge.id }"/>
						<input type="hidden" name="prpLCharge[${status.index}].serialNo" value="${prpLCharge.serialNo }"/>
						<c:if test="${empty prpLCharge.businessId && empty compWfFlag}">
							<!-- 非定损环节录入的费用可删改 -->
							<td width="6%">
								<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao " name="PayInfo_${status.index }" onclick="delPayInfo(this,'infoSize')" />
							</td>
							<c:if test="${flag eq '1'}">
								<td width="8%">
									机动车交通事故责任强制险<input type="hidden" name="prpLCharge[${status.index }].kindCode" value="BZ"/>
								</td>
							</c:if>
							<c:if test="${flag ne '1'}">
								<td width="8%">
									<app:codeSelect type="select" codeType="KindCode" dataSource="${kindForChaMap }" value="${prpLCharge.kindCode }" name="prpLCharge[${status.index }].kindCode"/>
								</td>
							</c:if>
							<td width="6%">
								<app:codeSelect type="select" codeType="ChargeNodeCode" name="prpLCharge[${status.index }].businessType" value="${prpLCharge.businessType }"/>
							</td>
							<c:if test="${prpLCharge.chargeCode eq '13'}">
							<td width="8%">
								<input type="text" class="input-text"  value="公估费"  readonly/>
								<input type="hidden" name="prpLCharge[${status.index }].chargeCode"  value="${prpLCharge.chargeCode }"/>
							</td>
							</c:if>
							<c:if test="${prpLCharge.chargeCode ne '13'}">
							<td width="8%">
								<app:codeSelect type="select" codeType="ChargeCode" clazz="must" name="prpLCharge[${status.index }].chargeCode" value="${prpLCharge.chargeCode }" onclick="paySummary(this,'prpLCharge[${status.index }].summary')"/>
							</td>
							</c:if>
						</c:if>
						<c:if test="${not empty prpLCharge.businessId || not empty compWfFlag }">
							<!-- 定损环节录入的费用不可删改 -->
							<td width="6%">
								<button type="button" class="btn disabled btn-minus Hui-iconfont Hui-iconfont-jianhao " name="PayInfo_${status.index }" />
							</td>
							<td width="8%">
								<app:codetrans codeType="KindCode" codeCode="${prpLCharge.kindCode }" riskCode="${riskCode }"/>
								<input type="hidden" name="prpLCharge[${status.index}].kindCode" value="${prpLCharge.kindCode }"/>
							</td>
							<td width="5%">
								<app:codetrans codeType="ChargeNodeCode" codeCode="${prpLCharge.businessType }"/>
								<input type="hidden" name="prpLCharge[${status.index}].businessType" value="${prpLCharge.businessType }"/>
							</td>
							<c:if test="${prpLCharge.chargeCode eq '13'}">
							<td width="8%">
								<input type="text" class="input-text"  value="公估费"  readonly/>
								<input type="hidden" name="prpLCharge[${status.index }].chargeCode"  value="${prpLCharge.chargeCode }"/>
							</td>
							</c:if>
							<c:if test="${prpLCharge.chargeCode ne '13'}">
							<td width="8%">
								<app:codetrans codeType="ChargeCode" codeCode="${prpLCharge.chargeCode }"/>
								<input type="hidden" name="prpLCharge[${status.index}].chargeCode" value="${prpLCharge.chargeCode }"/>
							</td>
							</c:if>
						</c:if>
						<td>
							<input type="text" class="input-text" name="prpLCharge[${status.index}].feeAmt" value="${prpLCharge.feeAmt }" datatype="amount"/>
						</td>
						<td>
							<input type="text" class="input-text" name="prpLCharge[${status.index}].offAmt" 
										value="${empty prpLCharge.offAmt?0.00:prpLCharge.offAmt }" datatype="amount" onblur="compareAmt(this)"/>
							<input type="hidden" name="prpLCharge[${status.index}].feeRealAmt" value="${prpLCharge.feeRealAmt }" />
						</td>
						<c:if test="${prpLCompensate.deductType ne '3' }">
						<td>
							<input type="text" class="input-text" name="prpLCharge[${status.index}].offPreAmt" 
										value="${empty prpLCharge.offPreAmt?0.00:prpLCharge.offPreAmt }" datatype="amount"/>
						</td>
						</c:if>
						<td>
							<input type="text" class="input-text" onclick="showPayCusts(this)" value="${prpLCharge.payeeName }" name="prpLCharge[${status.index}].payeeName" datatype="*" readonly placeholder="点击选择收款人" />
							<input type="hidden" name="prpLCharge[${status.index}].payeeId" value="${prpLCharge.payeeId }" />
						</td>
						<td>
							<input type="text" class="input-text" name="prpLCharge[${status.index}].accountNo" value="${prpLCharge.accountNo }"  readonly/>
						</td>
						<td>
							<c:set var="bankName">
								<app:codetrans codeType="BankCode" codeCode="${prpLCharge.bankName }"/>
							</c:set>
							<input type="text" class="input-text" name="prpLCharge[${status.index}].bankName" value="${bankName }" readonly />
						</td>
						<td>
							<input type="text" class="input-text" name="prpLCharge[${status.index}].payeeIdfNo" value="${prpLCharge.payeeIdfNo }" readonly/>
						</td>
					<%--<td>--%>
					<%--<app:codeSelect codeType="InvoiceTypeY" type="select" name="prpLCharge[${status.index}].invoiceType" title="true" value="000" nullToVal="000"/>--%>
						<%--1001 隐藏理算界面上发票类型，默认待补票 --%>
					<input type="hidden" name="prpLCharge[${status.index}].invoiceType" value="000" />
		<%-- <input type="hidden" name="prpLCharge[${status.index}].feeRealAmt" />
          <input  type="text" class="input-text"  name="prpLCharge[${status.index}].invoiceType" value="${prpLCharge.invoiceType}"/>
          	<c:choose>
			<c:when test="${prplCharge.feeRealAmt < 0}">
				<app:codeSelect codeType="InvoiceTypeN" type="select" name="prpLCharge[${status.index}].invoiceType" value="${prpLCharge.invoiceType }" />
			</c:when>
			<c:otherwise>
			<app:codeSelect codeType="InvoiceTypeY" type="select" name="prpLCharge[${status.index}].invoiceType" title="true" value="${prpLCharge.invoiceType }" />
			</c:otherwise>
		</c:choose> --%>
        <%--</td>--%>
				<td>
						           <input type="hidden"  name="prpLCharge[${status.index}].addTaxRate" value="${prpLCharge.addTaxRate }" />
						           <input type="hidden"  name="prpLCharge[${status.index}].addTaxValue" value="${prpLCharge.addTaxValue }" />
						           <input type="hidden"  name="prpLCharge[${status.index}].noTaxValue" value="${prpLCharge.noTaxValue}" />
									<%-- <button type="button" class="btn btn-primary" 
									onclick="showTaxInfos('${status.index}','${prpLCharge.addTaxRate }','${prpLCharge.addTaxValue }','${prpLCharge.noTaxValue}')">税收信息</button> --%>
									<input type="button" class="btn rateBtn" name="" onclick="showTaxInfos('${status.index}','${prpLCharge.addTaxRate }','${prpLCharge.addTaxValue }','${prpLCharge.noTaxValue}')" value="..." />
				</td>
				<td>
					<c:if test="${empty prpLCharge.summary }">
						<c:set var="prplChargeCode">
							<c:if test="${prpLCharge.chargeCode eq '13'}">
								<input type="text" class="input-text"  value="公估费"  readonly/>
							</c:if>
							<c:if test="${prpLCharge.chargeCode ne '13'}">
							<app:codetrans codeType="ChargeCode" codeCode="${prpLCharge.chargeCode }"  />
							</c:if>
						</c:set>
						<input type="text" class="input-text" name="prpLCharge[${status.index}].summary" value="${licenseNoSummary}${prplChargeCode}" onchange="checkSpecialCharactor(this,'${licenseNoSummary}${prplChargeCode}')" datatype="*1-1000" />
					</c:if>
					<c:if test="${!empty prpLCharge.summary }">
						<input type="text" class="input-text" name="prpLCharge[${status.index}].summary" value="${prpLCharge.summary}" onchange="checkSpecialCharactor(this,'${prpLCharge.summary}')" datatype="*1-1000" />
					</c:if> 
				</td>
				
		  <td>
		     <div style="" id="prpLChargeVatInvoiceFlag${status.index}">
				<app:codeSelect codeType="YN10" type="radio" style="min-width:50px" name="prpLCharge[${status.index}].vatInvoiceFlag" value="${prpLCharge.vatInvoiceFlag }" nullToVal="0" onclick="chargeVatInvoiceFlagCon('${status.index}')"/>
		     </div>
		  </td>
		  
		  <td  width="5%">
		  <c:choose>
			
			<c:when test="${prpLCharge.vatInvoiceFlag eq '1'}">
				<div style="" id="prpLChargeVatTaxRate${status.index}">
			       <app:codeSelect codeType="AddTaxRate" type="select" name="prpLCharge[${status.index}].vatTaxRate" value="${prpLCharge.vatTaxRate }"/>%
			    </div>
		    </c:when>
		    <c:otherwise>  
			    <div style="display: none" id="prpLChargeVatTaxRate${status.index}">
			       <app:codeSelect codeType="AddTaxRate" type="select" name="prpLCharge[${status.index}].vatTaxRate" value="${prpLCharge.vatTaxRate }"/>%
			    </div>
            </c:otherwise>
		    
			</c:choose>
		  </td>
		<c:if test="${prpLWfTaskVo.handlerStatus eq '3' && isGbFlag != '2' && showFlag eq '1'}">
		   <td>
		   <span class="btn-upload"><a href="javascript:void();" class="btn">上传</a><input name="file" id="Upfile${status.index}" type="file" value="点我上传"  multiple="multiple" onchange="importExcel('${prpLCompensate.compensateNo}','1','${prpLCharge.chargeCode}','${prpLCharge.payeeId}','${prpLCharge.payeeName }','${status.index}','${prpLCharge.id }')" class="input-file"/></span><br>&nbsp;<a href="/claimcar/bill/urlReqQueryParam.do?bussNo=${prpLCompensate.compensateNo}" target="_blank" class="btn">查看</a>
		   
		   </td>
		   
		</c:if>
				 
				
                       <%-- <td>
						<c:choose>
		                 <c:when test="${handlerStatus !='3'}">
		                <button type="button" class="btn  btn-primary mt-5"  onclick="CustomOpen('Y',this)">反洗钱信息补录</button>
		                  </c:when>
		              <c:otherwise>
			            <button  type="button" class="btn  btn-disabled">反洗钱信息补录</button>
		              </c:otherwise>
	                </c:choose>
					</td> --%>
					</tr>
				</c:forEach>
				</tbody>
				</table>
			</div>
		</div>
	</div>
</div>