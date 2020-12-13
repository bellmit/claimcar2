<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!--第一次初始化为空  -->
<c:forEach var="prpLdlossPropFee" items="${prpLdlossPropMainVo.prpLdlossPropFees }" varStatus="status">
	<c:set var="prIdx" value="${status.index + size}"></c:set>
	<c:set var="pIdx" value="${status.index}"></c:set>
	<tr class="text-c">
		<td width="6%">
			<button type="button" class="fl btn btn-minus Hui-iconfont Hui-iconfont-jianhao"
				onclick="delSubRisk(this)" name="prpLdlossPropFeeVo_${prIdx}"></button>
		</td>
		<td style="width: 5.111%;">
			<input type="text" class="input-text" datatype="*" name="prpLdlossPropMainVo.prpLdlossPropFees[${prIdx}].owner"
				value="${prpLdlossPropFee.owner }" />
		</td>
		<td style="width: 11.111%;">
			<!-- 该字段用于判断该选项能否点击删除事件 --> 
			<input type="hidden" name="prpLdlossPropMainVo.prpLdlossPropFees[${prIdx}].validFlag " value="${prpLdlossPropFee.validFlag }" /> 
			<input type="hidden" name="prpLdlossPropMainVo.prpLdlossPropFees[${prIdx}].id" value="${prpLdlossPropFee.id }" />
			<input type="hidden" id=feeRegistNo	name="prpLdlossPropMainVo.prpLdlossPropFees[${prIdx}].registNo" 
				value="${prpLdlossPropFee.registNo }" /> 
			<input type="hidden" name="prpLdlossPropMainVo.prpLdlossPropFees[${prIdx}].riskCode " value="${prpLdlossPropFee.riskCode }" />
			<input type="text" class="input-text" datatype="*" name="prpLdlossPropMainVo.prpLdlossPropFees[${prIdx}].lossItemName"
				value="${prpLdlossPropFee.lossItemName }" />
		</td>
		<td style="width: 8.111%;">
			<span class="select-box"> 
				<app:codeSelect codeType="LossSpeciesCode" type="select"
					name="prpLdlossPropMainVo.prpLdlossPropFees[${prIdx}].lossSpeciesCode"
					value="${prpLdlossPropFee.lossSpeciesCode }" />
			</span>
		</td>
		<td style="width: 13.111%;">
			<span class="select-box">
				<app:codeSelect codeType="PropertyFeeType"  type="select"
					name="prpLdlossPropMainVo.prpLdlossPropFees[${prIdx}].feeTypeCode"
					value="${prpLdlossPropFee.feeTypeCode }"/>
			</span>
		</td>
		<td style="width: 11.111%;">
			<input type="text" class="input-text" datatype="n"
				name="prpLdlossPropMainVo.prpLdlossPropFees[${prIdx}].lossQuantity" style="vertical-align:middle;width:60%;" onblur="calSumLossFee()"
				value="${prpLdlossPropFee.lossQuantity }" />${prpLdlossPropFee.unit}
		</td>
		<td style="width: 11.111%;">
			<input type="text" class="input-text" datatype="amount" 
				name="prpLdlossPropMainVo.prpLdlossPropFees[${prIdx}].unitPrice" value="${prpLdlossPropFee.unitPrice }" onblur="calSumLossFee()" />
		</td>
		<td style="width: 11.111%;">
		    <c:if test="${flag == 't' }" >
			<div class="radio-box">
				<input type="checkbox" name="prpLdlossPropMainVo.prpLdlossPropFees[${prIdx}].recycleFlag"
					value="${prpLdlossPropFee.recycleFlag}" onclick="recycleFlag(this)" />
			</div>
			</c:if>
			<c:if test="${flag == 'f' }">
			    <div class="radio-box">
			       <input type="checkbox" name="prpLdlossPropMainVo.prpLdlossPropFees[${prIdx}].recycleFlag"
					value="${prpLdlossPropFee.recycleFlag}" disabled="disabled"/>
				</div>
			</c:if>
		</td>
		<td style="width: 11.111%;">
			<input type="text" class="input-text" datatype="amount" onblur="calSumLossFee()"
			name="prpLdlossPropMainVo.prpLdlossPropFees[${prIdx}].recyclePrice" value="${prpLdlossPropFee.recyclePrice }"  onchange="defaultRecyclePrice()"/>
		</td>
		<td style="width: 11.111%;">
			<input type="text" class="input-text" name="prpLdlossPropMainVo.prpLdlossPropFees[${prIdx}].sumDefloss"
				value="${prpLdlossPropFee.sumDefloss }" readonly="readonly" />
		</td>
	</tr>
	<c:if test="${!empty prpLdlossPropFee.sumVeriLoss  }">
		<tr class="text-c">
			<td colspan="4" align="center">
				<strong>核损意见</strong>
			</td>
			<td style="width: 11.111%;">
				<input type="text" class="input-text" value="${prpLdlossPropFee.veriLossQuantity }" disabled="disabled" 
				style="vertical-align:middle;width:60%;" />${prpLdlossPropFee.unit }
			</td>

			<td style="width: 11.111%;">
				<input type="text" class="input-text" value="${prpLdlossPropFee.veriUnitPrice }" disabled="disabled"  />
			</td>
			<td style="width: 11.111%;">
				<div class="radio-box"></div>
			</td>
			<td style="width: 11.111%;">
				<input type="text" class="input-text"	value="${prpLdlossPropFee.veriRecylePrice }" disabled="disabled" />
			</td>
			<td style="width: 11.111%;">
				<input type="text" class="input-text" id="prpLdlossPropFees_${prIdx}_sumVeriLoss" 
					value="${prpLdlossPropFee.sumVeriLoss }" disabled="disabled"/>
			</td>
		</tr>
	</c:if>
</c:forEach>


