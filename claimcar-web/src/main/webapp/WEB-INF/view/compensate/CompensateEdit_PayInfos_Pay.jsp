<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="prpLCharge" items="${prpLCharges }" varStatus="status">
	<tr class="detail-body">
		<input type="hidden" name="prpLCharge[${status.index + size}].currency" value="CNY"/>
		<input type="hidden" name="prpLCharge[${status.index + size}].validFlag" value="1"/>
		<td width="6%">
			<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao "  name="PayInfo_${status.index + size}"  onclick="delPayInfo(this,'infoSize')"></button>
		</td>
		<td width="8%">
		<c:if test="${flag eq '1' }">
			机动车交通事故责任强制险<input type="hidden" name="prpLCharge[${status.index + size }].kindCode" value="BZ"/>
		</c:if>
		<c:if test="${flag eq '2' }">
			<select class="select" name="prpLCharge[${status.index + size }].kindCode">
				<c:forEach var="kind" items="${kindMap }"  varStatus="sta">
					<option value="${kind.key}">${kind.value }</option>
				</c:forEach>
			</select>
		</c:if>
		</td>
		<td width="6%"><app:codeSelect type="select" codeType="ChargeNodeCode" name="prpLCharge[${status.index + size}].businessType"/></td>
		<td width="6%"><app:codeSelect type="select" codeType="ChargeCode" clazz="must" name="prpLCharge[${status.index + size}].chargeCode" onchange="paySummary(this,'prpLCharge[${status.index + size }].summary')"/></td>
		<td>
			<input type="text" class="input-text" name="prpLCharge[${status.index + size }].feeAmt" onblur="amtSum(this)" datatype="amount"/>
		</td>
		<td>
			<input type="text" class="input-text" name="prpLCharge[${status.index + size }].offAmt" datatype="amount" onblur="compareAmt(this)" value="0"/>
			<input type="hidden" name="prpLCharge[${status.index + size }].feeRealAmt" value="${prpLCharge.feeRealAmt }" />
		</td>
		<c:if test="${deductType ne '3' }">
		<td>
			<input type="text" class="input-text" name="prpLCharge[${status.index + size }].offPreAmt" datatype="amount"/>
		</td>
		</c:if>
		<td>
		<input type="text" class="input-text" onclick="showPayCusts(this)" name="prpLCharge[${status.index + size }].payeeName" datatype="*" readonly placeholder="点击选择收款人" />
		<input type="hidden" name="prpLCharge[${status.index + size }].payeeId" />
		<input type="hidden" name="prpLCharge[${status.index + size }].payObjectKind" />
		</td>
		<td>
			<input type="text" class="input-text" name="prpLCharge[${status.index + size }].accountNo"  readonly/>
		</td>
		<td>
			<input type="text" class="input-text" name="prpLCharge[${status.index + size }].bankName"  readonly/>
		</td>
		<td>
			<input type="text" class="input-text" name="prpLCharge[${status.index + size }].payeeIdfNo" readonly/>
		</td>
		<%--<td>--%>
		<%--<app:codeSelect codeType="InvoiceTypeY" type="select" name="prpLCharge[${status.index+ size}].invoiceType" title="true" value="000" nullToVal="000"/>--%>

	<%-- 	<input type="hidden" name="prpLCharge[${status.index}].feeRealAmt" value="prplCharge.feeRealAmt"/>
		<c:choose>
			<c:when test="${prplCharge.feeRealAmt < 0}">
				<app:codeSelect codeType="InvoiceTypeN" type="select" name="prpLCharge[${status.index}].invoiceType" value="${prpLCharge.invoiceType }" />
			</c:when>
			<c:otherwise>
			<app:codeSelect codeType="InvoiceTypeY" type="select" name="prpLCharge[${status.index}].invoiceType" title="true" value="${prpLCharge.invoiceType }" />
			</c:otherwise>
       </c:choose> --%>
		<%--</td>--%>
		<input type="hidden" name="prpLCharge[${status.index+ size}].invoiceType" value="000" />

		<td>
		                          <input type="hidden"  name="prpLCharge[${status.index + size  }].addTaxRate" value="${prpLCharge.addTaxRate }" />
						           <input type="hidden"  name="prpLCharge[${status.index + size }].addTaxValue" value="${prpLCharge.addTaxValue }" />
						           <input type="hidden"  name="prpLCharge[${status.index + size }].noTaxValue" value="${prpLCharge.noTaxValue}" />
									<%-- <button type="button" class="btn btn-primary" 
									onclick="showTaxInfos('${status.index}','${prpLCharge.addTaxRate }','${prpLCharge.addTaxValue }','${prpLCharge.noTaxValue}')">税收信息</button> --%>
									<input type="button" class="btn rateBtn" name="" onclick="showTaxInfos('${status.index}','${prpLCharge.addTaxRate }','${prpLCharge.addTaxValue }','${prpLCharge.noTaxValue}')" value="..." />
		</td>
		<td>
			<input type="text"  class="input-text" name="prpLCharge[${status.index + size }].summary"  value="${licenseNo}"  onchange="checkSpecialCharactor(this,'${licenseNo}')" datatype="*1-1000" />
		</td>
		
		  <td>
		     <div style="" id="prpLChargeVatInvoiceFlag${status.index + size }">
				<app:codeSelect codeType="YN10" type="radio" style="min-width:50px" name="prpLCharge[${status.index + size }].vatInvoiceFlag" value="${prpLCharge.vatInvoiceFlag }" nullToVal="0" onclick="chargeVatInvoiceFlagCon('${status.index + size }')"/>
		     </div>
		  </td>
		  
		  <td width="5%">
		  <c:choose>
			
			<c:when test="${prpLCharge.vatInvoiceFlag eq '1'}">
				<div style="" id="prpLChargeVatTaxRate${status.index + size }">
			       <app:codeSelect codeType="AddTaxRate" type="select" name="prpLCharge[${status.index + size }].vatTaxRate" value="${prpLCharge.vatTaxRate }"/>%
			    </div>
		    </c:when>
		    <c:otherwise>  
			    <div style="display: none" id="prpLChargeVatTaxRate${status.index + size }">
			       <app:codeSelect codeType="AddTaxRate" type="select" name="prpLCharge[${status.index + size }].vatTaxRate" value="${prpLCharge.vatTaxRate }"/>%
			    </div>
            </c:otherwise>
		    
			</c:choose>
		  </td>
		  
		<%-- <c:if test="${empty prpLCharge.payObjectKind}">
			<td>
				<div style=""
					id="prpLChargeVatInvoiceFlag${status.index + size }">
					<app:codeSelect codeType="YN10" type="radio" style="min-width:50px" name="prpLCharge[${status.index + size }].vatInvoiceFlag"
						value="" nullToVal="0" onclick="chargeVatInvoiceFlagCon('${status.index + size }')" />
				</div>
			</td>
			<td>
			<div style="display: none" id="prpLChargeVatTaxRate${status.index + size }">
		       <app:codeSelect codeType="AddTaxRate" type="select" name="prpLCharge[${status.index + size }].vatTaxRate" value="${prpLCharge.vatTaxRate }"/>%
		    </div>
			</td>
		</c:if> --%>
		
		<%--<td>
			<c:choose>
	               <c:when test="${handlerStatus !='3'}">
	              <button type="button" class="btn  btn-primary mt-5" onclick="CustomOpen('Y',this)">反洗钱信息补录</button>
	                </c:when>
	            <c:otherwise>
	           <button  type="button" class="btn  btn-disabled">反洗钱信息补录</button>
	            </c:otherwise>
	       </c:choose>
		</td> --%>
	</tr>
</c:forEach>