<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="prePayFVo" items="${prePayFVos }" varStatus="status">
	<c:set var="index" value="${size+status.index}" />
	<tr class="detail-body">
		<td>
			<button type="button" name="FeeDelBtn_${index }" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao " onclick="delPayInfo(this,'feeSize','prePayFVos')"></button>

		</td>
		<td>
			<input type="hidden" name="prePayFVos[${index }].feeType" value="F">
			<span class="select-box"> <app:codeSelect codeType="KindCode" dataSource="${kindMap }" type="select" name="prePayFVos[${index }].kindCode" value="${prePayFVo.kindCode }" />
			</span>
		</td> 
		<td>
			<input type="hidden" name="prePayFVos[${index }].chargeCode" value="${prePayFVo.chargeCode }">
			<app:codetrans codeType="ChargeCode" codeCode="${prePayFVo.chargeCode }" />
		</td>
		<td>
			<input type="text" class="input-text payAmt" datatype="amount" name="prePayFVos[${index }].payAmt" value="${prePayFVo.payAmt }" />
		</td>
		<td>
			<input type="text" class="input-text" datatype="*" name="prePayFVos[${index }].payeeName" onclick="showPayCustForFee(this)" value="${prePayFVo.payeeName }" />
			<input type="hidden" name="prePayFVos[${index }].payeeId" value="${prePayFVo.payeeId }" />
			<input type="hidden" name="prePayFVos[${index }].payObjectKind" value="${prePayFVo.payObjectKind }" />
		</td>
		<td>
			<input type="text" class="input-text" name="prePayFVos[${index }].accountNo" value="${prePayFVo.accountNo }" readonly="readonly" />
		</td>
		<td>
			<input type="text" class="input-text" name="prePayFVos[${index }].bankName" value="<app:codetrans codeType="BankCode" codeCode="${prePayFVo.bankName }"/>" readonly="readonly" />

		</td>
		<%--<td>--%>
			<%--<app:codeSelect codeType="InvoiceTypeY" type="select"  name="prePayFVos[${index}].invoiceType" title="true" value="000" nullToVal="000"/>--%>
		<%--</td>--%>
		<input type="hidden" name="prePayFVos[${index}].invoiceType" value="000" />
		<td>
		    <input type="hidden"  name="prePayFVos[${index}].addTaxRate" value="${prePayFVo.addTaxRate }" />
			<input type="hidden"  name="prePayFVos[${index }].addTaxValue" value="${prePayFVo.addTaxValue }" />
			<input type="hidden"  name="prePayFVos[${index }].noTaxValue" value="${prePayFVo.noTaxValue}" />
			<input type="button" class="btn rateBtn" name="[${index}]_showButton" onclick="showTaxInfos('${index}','${prePayFVo.addTaxRate }','${prePayFVo.addTaxValue }','${prePayFVo.noTaxValue}')" value="..." />
		</td>
		<td>
			<input type="text" class="input-text" name="prePayFVos[${index }].summary" value="${prePayFVo.summary}" onchange="checkSpecialCharactor(this,'${prePayFVo.summary}')" datatype="*1-1000"/>
		</td>
		
	
		  <td>
		     <div style="" id="prePayFVosVatInvoiceFlag${index}">
				<app:codeSelect codeType="YN10" type="radio" style="min-width:50px" name="prePayFVos[${index}].vatInvoiceFlag" value="${prePayFVo.vatInvoiceFlag }" nullToVal="0" onclick="chargeVatInvoiceFlagCon('${index}')"/>
		     </div>
		  </td>
		  
		  <td>
		  <c:choose>
			
			<c:when test="${prePayFVo.vatInvoiceFlag eq '1'}">
				<div style="" id="prePayFVosVatTaxRate${index}">
			       <app:codeSelect codeType="AddTaxRate" type="select" name="prePayFVos[${index}].vatTaxRate" value="${prePayFVo.vatTaxRate }"/>%
			    </div>
		    </c:when>
		    <c:otherwise>  
			    <div style="display: none" id="prePayFVosVatTaxRate${index}">
			       <app:codeSelect codeType="AddTaxRate" type="select" name="prePayFVos[${index}].vatTaxRate" value="${prePayFVo.vatTaxRate }"/>%
			    </div>
            </c:otherwise>
		    
			</c:choose>
		  </td>
		<c:if test="${handlerStatus eq '3' && prpLregistVo.isGBFlag != '2' && showFlag eq '1'}">
		   <td>
		   <span class="btn-upload"><a href="javascript:void();" class="btn">上传</a><input name="file" id="Upfile${index}" type="file" value="点我上传"  multiple="multiple" onchange="importExcel('${compensateVo.compensateNo}','1','${prePayFVo.chargeCode}','${prePayFVo.payeeId}','${prePayFVo.payeeName }','${index}','${prePayFVo.id }')" class="input-file"/></span><br>&nbsp;<a href="/claimcar/bill/urlReqQueryParam.do?bussNo=${compensateVo.compensateNo}" target="_blank" class="btn">查看</a>
		   
		   </td> 
		    
		</c:if>
		<%-- <c:if test="${empty prePayFVo.payObjectKind}">
			<td>
				<div style="display: none"
					id="prePayFVosVatInvoiceFlag${index}">
					<app:codeSelect codeType="YN10" type="radio" style="min-width:50px" name="prePayFVos[${index}].vatInvoiceFlag"
						value="" nullToVal="0" onclick="chargeVatInvoiceFlagCon('${index}')" />
				</div>
			</td>
			<td>
			<div style="display: none" id="prePayFVosVatTaxRate${index}">
		       <app:codeSelect codeType="AddTaxRate" type="select" name="prePayFVos[${index}].vatTaxRate" value="${prePayFVo.vatTaxRate }"/>%
		    </div>
			</td>
		</c:if> --%>
		
		<%-- <td>
			<c:choose>
				<c:when test="${handlerStatus !='3'}">
					<button type="button" class="btn  btn-primary mt-5" onclick="CustomOpen('Y',this)" name="${index }">反洗钱信息补录</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn  btn-disabled">反洗钱信息补录</button>
				</c:otherwise>
			</c:choose>
		</td> --%>
	</tr>
</c:forEach>
