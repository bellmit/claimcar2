<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="prePayPVo" items="${prePayPVos }" varStatus="status">
<c:set var="index" value="${index+status.index}" />
<tr class="detail-body">
	<td>
		<button type="button" name="indemntifyDelBtn_${index }" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao " onclick="delPayInfo(this,'indeSize','prePayPVos')"></button>
		
	</td>
	<input type="hidden" name="prePayPVos[${index }].feeType" value="P">
	<c:if test="${handlerStatus == '3' }">
		<td>
			<span class="select-box"> <app:codeSelect codeType="KindCode" dataSource="${kindMap }" type="select" name="prePayPVos[${index }].kindCode" value="${prePayPVo.kindCode }" />
			</span>
		</td>
	</c:if> 
	<td>
		<input type="hidden" name="prePayPVos[${index }].lossName" value="${prePayPVo.lossName }">
		<span class="select-box"> 
			<app:codeSelect title="true" codeType="lossType" dataSource="${lossInfoList }" clazz="must" type="select" 
					name="prePayPVos[${index }].lossType" onchange="changeLossType(this)" value="${prePayPVo.lossType }" />
		</span>
	</td>
	<td>
		<input type="hidden" name="prePayPVos[${index }].chargeName" value="${prePayPVo.chargeName }">
		<app:codeSelect title="true" codeType="PrePayFeeType" type="select" name="prePayPVos[${index }].chargeCode" value="${prePayPVo.chargeCode }" />
	</td>
	<td>
		<input type="text" class="input-text payAmt" datatype="amount" name="prePayPVos[${index }].payAmt" value="${prePayPVo.payAmt }" onchange="sumPayAmt(this)" />
	</td>
	<td>
		<input type="text" class="input-text" datatype="*" name="prePayPVos[${index }].payeeName" onclick="showPayCust(this)" value="${prePayPVo.payeeName }" />
		<input type="hidden" name="prePayPVos[${index }].payeeId" value="${prePayPVo.payeeId }" />
		<input type="hidden" name="prePayPVos[${index }].payObjectKind" value="${prePayPVo.payObjectKind }" />
	</td>
	<td>
		<app:codeSelect codeType="YN10" type="radio" style="min-width:50px" name="prePayPVos[${index }].otherFlag" value="${prePayPVo.otherFlag }" nullToVal="1"  onclick="otherFlagCon()" /> 
		<%-- <label class='radio-box'   style='min-width:50px;' >
			<input type='radio'  onchange="otherFlagCon()" name='prePayPVos[${index }].otherFlag' value='1' 
			 <c:if test="${prePayPVo.otherFlag =='1'}" >checked='checked'</c:if> datatype="*" />是</label>
		<label class='radio-box'   style='min-width:50px;' >
			<input type='radio'  onchange="otherFlagCon()" name='prePayPVos[${index }].otherFlag' value='0'  
			<c:if test="${prePayPVo.otherFlag =='0'}" >checked='checked'</c:if> />否</label> --%>

	</td>
	<td>
		<app:codeSelect title="true" codeType="OtherCase" type="select" name="prePayPVos[${index }].otherCause" value="${prePayPVo.otherCause }" />
	</td>
	<td>
		<input type="text" class="input-text" name="prePayPVos[${index }].accountNo" value="${prePayPVo.accountNo }" readonly="readonly" />
	</td>
	<td>
		<input type="text" class="input-text" name="prePayPVos[${index }].bankName" value="<app:codetrans codeType="BankCode" codeCode="${prePayPVo.bankName }"/>" readonly="readonly" />
	</td>
	<td>
	    <input type="hidden"  name="prePayPVos[${index}].addTaxRate" value="${prePayPVo.addTaxRate }" />
		<input type="hidden"  name="prePayPVos[${index }].addTaxValue" value="${prePayPVo.addTaxValue }" />
		<input type="hidden"  name="prePayPVos[${index }].noTaxValue" value="${prePayPVo.noTaxValue}" />
		<input type="button" class="btn rateBtn" name="[${index}]_showButton" onclick="showTaxInfos('${index}','${prePayPVo.addTaxRate }','${prePayPVo.addTaxValue }','${prePayPVo.noTaxValue}')" value="..." />
	</td>
	<td>
			<input type="text" class="input-text" name="prePayPVos[${index }].summary" value="${prePayPVo.summary}" onchange="checkSpecialCharactor(this,'${prePayPVo.summary}')" datatype="*1-1000"/>
		</td>
		<c:choose>
		<c:when test="${prePayPVo.payObjectKind eq '6'}">
		  <td>
		     <div style="" id="prePayPVosVatInvoiceFlag${index}">
				<app:codeSelect codeType="YN10" type="radio" style="min-width:50px" name="prePayPVos[${index}].vatInvoiceFlag" value="${prePayPVo.vatInvoiceFlag }" nullToVal="0" onclick="paymentVatInvoiceFlagCon('${index}')"/>
		     </div>
		  </td>
		  
		  <td>
		  <c:choose>
			
			<c:when test="${prePayPVo.vatInvoiceFlag eq '1'}">
				<div style="" id="prePayPVosVatTaxRate${index}">
			       <app:codeSelect codeType="AddTaxRate" type="select" name="prePayPVos[${index}].vatTaxRate" value="${prePayPVo.vatTaxRate }"/>%
			    </div>
		    </c:when>
		    <c:otherwise>  
			    <div style="display: none" id="prePayPVosVatTaxRate${index}">
			       <app:codeSelect codeType="AddTaxRate" type="select" name="prePayPVos[${index}].vatTaxRate" value="${prePayPVo.vatTaxRate }"/>%
			    </div>
            </c:otherwise>
		    
			</c:choose>
		  </td>
		  
		</c:when>
		<c:otherwise>
		<td>
				<div style="display: none"
					id="prePayPVosVatInvoiceFlag${index}">
					<app:codeSelect codeType="YN10" type="radio" style="min-width:50px" name="prePayPVos[${index}].vatInvoiceFlag"
						value="" nullToVal="0" onclick="paymentVatInvoiceFlagCon('${index}')" />
				</div>
			</td>
			<td>
			<div style="display: none" id="prePayPVosVatTaxRate${index}">
		       <app:codeSelect codeType="AddTaxRate" type="select" name="prePayPVos[${index}].vatTaxRate" value="${prePayPVo.vatTaxRate }"/>%
		    </div>
			</td>
		</c:otherwise>
		 </c:choose>
		 <c:if test="${handlerStatus eq '3' && prpLregistVo.isGBFlag != '2' && showFlag eq '1'}">
		   <c:choose>
		   <c:when test="${prePayPVo.payObjectKind eq '6'}">
		   <td>
		   <span class="btn-upload"><a href="javascript:void();" class="btn">上传</a><input name="file" id="Upfile${index}" type="file" value="点我上传"  multiple="multiple" onchange="importExcel('${compensateVo.compensateNo}','0','38','${prePayPVo.payeeId}','${prePayPVo.payeeName }','${index}','${prePayPVo.id }')" class="input-file"/></span><br>&nbsp;<a href="/claimcar/bill/urlReqQueryParam.do?bussNo=${compensateVo.compensateNo}" target="_blank" class="btn" >查看</a>
		   
		   </td>
		   </c:when>
		   <c:otherwise>
		    <td></td>
		   </c:otherwise>
		</c:choose> 
		   
		  </c:if>
		<%-- <c:if test="${empty prePayPVo.payObjectKind}">
			<td>
				<div style="display: none"
					id="prePayPVosVatInvoiceFlag${index}">
					<app:codeSelect codeType="YN10" type="radio" style="min-width:50px" name="prePayPVos[${index}].vatInvoiceFlag"
						value="" nullToVal="0" onclick="paymentVatInvoiceFlagCon('${index}')" />
				</div>
			</td>
			<td>
			<div style="display: none" id="prePayPVosVatTaxRate${index}">
		       <app:codeSelect codeType="AddTaxRate" type="select" name="prePayPVos[${index}].vatTaxRate" value="${prePayPVo.vatTaxRate }"/>%
		    </div>
			</td>
		</c:if> --%>
		
		
	<%-- <td>
						<c:choose> 
		                 <c:when test="${handlerStatus !='3'}">
		                <button type="button" class="btn  btn-primary mt-5" " onclick="CustomOpen('Y',this)">反洗钱信息补录</button>
		                  </c:when>
		              <c:otherwise>
			            <button  type="button" class="btn  btn-disabled">反洗钱信息补录</button>
		              </c:otherwise>
	                </c:choose>
					</td> --%>
</tr>
</c:forEach>