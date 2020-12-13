<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:forEach var="prpLPayment" items="${prpLPaymentVos }" varStatus="status">
	<tr class="detail-body">
		<td width="6%">
			<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao " name="PayCustom_${status.index + size }" onclick="delPayInfo(this,'custSize')"></button>
		</td>
		<td>
		<input type="text" class="input-text" onclick="showPayCust(this)" name="prpLPayment[${status.index + size }].payeeName" value="${prpLPayment.payeeName }" datatype="*" readonly placeholder="点击选择收款人" />
		<input type="hidden" name="prpLPayment[${status.index + size }].payeeId" value="${prpLPayment.payeeId }"/>
		<input type="hidden" name="prpLPayment[${status.index + size }].id" value="${prpLPayment.id }"/>
		<input type="hidden" name="prpLPayment[${status.index + size }].payObjectKind" value="${prpLPayment.payObjectKind }"/>
		<input type="hidden" name="prpLPayment[${status.index + size }].serialNo" value="${prpLPayment.serialNo }"/>
		<c:if test="${dwFlag eq '0' }"> 
			<input type="hidden" name="prpLPayment[${status.index + size }].payFlag" value="3"/>
		</c:if>
		<c:if test="${not empty compWfFlag }">  
			<!-- 理算冲销时 直接取原计算书的payFlag值 -->
			<input type="hidden" name="prpLPayment[${status.index + size }].payFlag" value="${prpLPayment.payFlag }"/>
		</c:if>
		</td>
		
		<c:if test="${dwFlag ne '0' && not empty dwFlag }">
			<td  width="8%">
			<app:codeSelect name="prpLPayment[${status.index + size }].payFlag" codeType="PayFlag"  exceptValue="4"
				type="select" value="${prpLPayment.payFlag }" onchange="changeDwPayFlag(this)"/>
			</td>
			<!-- 选择清付时才需要选择车辆 -->
			<td width="8%">
				<div name="itemId_${status.index + size }">
				<app:codeSelect name="prpLPayment[${status.index + size }].itemId" codeType="PayFlag" dataSource="${qfLicenseMap }"
				type="select" value="${prpLPayment.itemId }"/>
				</div>
			</td>
		</c:if>
		
		<td>
			<app:codeSelect codeType="YN10" type="radio" style="min-width:50px" name="prpLPayment[${status.index + size }].otherFlag" value="${prpLPayment.otherFlag }" nullToVal="1" onclick="otherFlagCon()"/>
			<c:if test="${(compWfFlag eq '1') || (compWfFlag eq '2') }">
				<input type="hidden" name="prpLPayment[${status.index + size }].otherFlag" value="${prpLPayment.otherFlag }"/>
			</c:if>
		</td>
		<td>
			<app:codeSelect codeType="OtherCase" type="select" name="prpLPayment[${status.index + size }].otherCause" value="${prpLPayment.otherCause }"/>
			<c:if test="${(compWfFlag eq '1') || (compWfFlag eq '2') }">
				<input type="hidden" name="prpLPayment[${status.index + size }].otherCause" value="${prpLPayment.otherCause }"/>
			</c:if>
		</td>
		<td>
			<input type="text" class="input-text" name="prpLPayment[${status.index + size }].accountNo" value="${prpLPayment.accountNo }" readonly/>
		</td>
		<td>
			<c:set var="bankName">
				<app:codetrans codeType="BankCode" codeCode="${prpLPayment.bankName}"/> 
			</c:set>
			<input type="text" class="input-text" name="prpLPayment[${status.index + size }].bankName" value="${bankName }" readonly/>
		</td>
		<td>
		<input type="text" class="input-text" name="prpLPayment[${status.index + size }].sumRealPay"  value='<fmt:formatNumber type="number" value="${prpLPayment.sumRealPay }" pattern="0.00" maxFractionDigits="2" />' id="payMentPay" datatype="amount"  errormsg="请录入收款人赔款金额!"  />
		</td> 
		
		<td>
		<input type="hidden"  name="prpLPayment[${status.index + size }].addTaxRate" value="${prpLPayment.addTaxRate }" />
		<input type="hidden"  name="prpLPayment[${status.index + size }].addTaxValue" value="${prpLPayment.addTaxValue }" />
		<input type="hidden"  name="prpLPayment[${status.index + size }].noTaxValue" value="${prpLPayment.noTaxValue}" />
		<input type="button" class="btn rateBtn" name="" onclick="showTaxInfos('${status.index + size }','${prpLPayment.addTaxRate }','${prpLPayment.addTaxValue }','${prpLPayment.noTaxValue}')" value="..." />
		</td>
		<td>
		<c:if test="${empty prpLPayment.summary }">
			<input type="text" class="input-text" name="prpLPayment[${status.index + size }].summary" value="${licenseNoSummary}赔款" onchange="checkSpecialCharactor(this,'${licenseNoSummary}赔款')" datatype="*1-1000" />
		</c:if>
		<c:if test="${!empty prpLPayment.summary }">
			<input type="text" class="input-text" name="prpLPayment[${status.index + size }].summary" value="${prpLPayment.summary}" onchange="checkSpecialCharactor(this,'${prpLPayment.summary}')" datatype="*1-1000" />
		</c:if> 
		</td>
		<c:choose>
		<c:when test="${prpLPayment.payObjectKind eq '6'}">
		  <td>
		    <div style="" id="prpLPaymentVatInvoiceFlag${status.index + size }">
				<app:codeSelect codeType="YN10" type="radio" style="min-width:50px" name="prpLPayment[${status.index + size }].vatInvoiceFlag" value="${prpLPayment.vatInvoiceFlag }" nullToVal="0" onclick="paymentVatInvoiceFlagCon('${status.index + size }')"/>
		    </div>
		  </td>
		  <td>
		  <c:choose>
			
			<c:when test="${prpLPayment.vatInvoiceFlag eq '1'}">
				<div style="" id="prpLPaymentVatTaxRate${status.index + size }">
			       <app:codeSelect codeType="AddTaxRate" type="select" name="prpLPayment[${status.index + size }].vatTaxRate" value="${prpLPayment.vatTaxRate }"/>%
			    </div>
		    </c:when>
		    <c:otherwise>  
			    <div style="display: none" id="prpLPaymentVatTaxRate${status.index + size }">
			       <app:codeSelect codeType="AddTaxRate" type="select" name="prpLPayment[${status.index + size }].vatTaxRate" value="${prpLPayment.vatTaxRate }"/>%
			    </div>
            </c:otherwise>
		    
			</c:choose>
		  </td>
		</c:when>
		 <c:otherwise>
		 <td>
				<div style="display: none"
					id="prpLPaymentVatInvoiceFlag${status.index + size }">
					<app:codeSelect codeType="YN10" type="radio" style="min-width:50px" name="prpLPayment[${status.index + size }].vatInvoiceFlag"
						value="" nullToVal="0" onclick="paymentVatInvoiceFlagCon('${status.index + size }')" />
				</div>
			</td>
			<td>
			<div style="display: none" id="prpLPaymentVatTaxRate${status.index + size }">
		       <app:codeSelect codeType="AddTaxRate" type="select" name="prpLPayment[${status.index + size }].vatTaxRate" value="${prpLPayment.vatTaxRate }"/>%
		    </div>
			</td>
		  </c:otherwise>
		</c:choose>
		<c:if test="${prpLWfTaskVo.handlerStatus eq '3' && isGbFlag != '2' && showFlag eq '1'}">
		<c:choose>
		   <c:when test="${prpLPayment.payObjectKind eq '6'}"> 
		   <td>
		   <span class="btn-upload"><a href="javascript:void();" class="btn">上传</a><input name="file" id="Upfile${status.index + size }" type="file" value="点我上传"  multiple="multiple" onchange="importExcel('${prpLCompensate.compensateNo}','0','37','${prpLPayment.payeeId}','${prpLPayment.payeeName }','${status.index + size }','${prpLPayment.id }')" class="input-file"/></span><br>&nbsp;<a href="/claimcar/bill/urlReqQueryParam.do?bussNo=${prpLCompensate.compensateNo}" target="_blank" class="btn" >查看</a>
		   
		   </td>
		   </c:when>
		   <c:otherwise>
		    <td></td>
		   </c:otherwise>
		</c:choose>
		</c:if>
		<%-- <c:if test="${empty prpLPayment.payObjectKind}">
			<td>
				<div style="display: none"
					id="prpLPaymentVatInvoiceFlag${status.index + size }">
					<app:codeSelect codeType="YN10" type="radio" style="min-width:50px" name="prpLPayment[${status.index + size }].vatInvoiceFlag"
						value="" nullToVal="0" onclick="paymentVatInvoiceFlagCon('${status.index + size }')" />
				</div>
			</td>
			<td>
			<div style="display: none" id="prpLPaymentVatTaxRate${status.index + size }">
		       <app:codeSelect codeType="AddTaxRate" type="select" name="prpLPayment[${status.index + size }].vatTaxRate" value="${prpLPayment.vatTaxRate }"/>%
		    </div>
			</td>
		</c:if> --%>
		
		<%-- <td>
		<c:choose>
		<c:when test="${handlerStatus !='3'}">
		   <button type="button" class="btn  btn-primary mt-5"  onclick="CustomOpen('Y',this)">反洗钱信息补录</button>
		 </c:when>
		<c:otherwise>
			<button  type="button" class="btn  btn-disabled">反洗钱信息补录</button>
		</c:otherwise>
	</c:choose>
		</td>                   --%>                                                                            
	</tr>
	
</c:forEach>


