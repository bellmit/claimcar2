<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!--第一次初始化为空  -->
<c:forEach var="prpLdlossPropFee"
	items="${prpLdlossPropMainVo.prpLdlossPropFees }" varStatus="status">
	<c:set var="propIdx" value="${status.index + size}"></c:set>
	<tr class="text-c">
		<td width="6%"><input class="btn btn-zd fl"
			onclick="delSubRisk(this)" name="prpLdlossPropFeeVo_${propIdx}"
			type="button" value="-" /></td>
		<td style="width: 11.111%;"><input type="hidden"
			name="prpLdlossPropMainVo.prpLdlossPropFees[${propIdx}].id"
			value="${prpLdlossPropFee.id }" /> <input type="hidden"
			id=feeRegistNo
			name="prpLdlossPropMainVo.prpLdlossPropFees[${propIdx}].registNo"
			value="${prpLdlossPropFee.registNo }" /> <input type="hidden"
			name="prpLdlossPropMainVo.prpLdlossPropFees[${propIdx}].riskCode "
			value="${prpLdlossPropFee.riskCode }" /> <input type="hidden"
			name="prpLdlossPropMainVo.prpLdlossPropFees[${propIdx}].validFlag "
			value="${prpLdlossPropFee.validFlag }" /> <input type="text"
			class="input-text"
			name="prpLdlossPropMainVo.prpLdlossPropFees[${propIdx}].lossItemName"
			value="${prpLdlossPropFee.lossItemName }" /></td>
		<td style="width: 13.111%;"><span class="select-box"> <app:codeSelect
					codeType="LossSpeciesCode" type="select"
					name="prpLdlossPropMainVo.prpLdlossPropFees[${propIdx}].lossSpeciesCode"
					value="${prpLdlossPropFee.lossSpeciesCode }" clazz="must" />
		</span></td>
		<td style="width: 13.111%;"><span class="select-box"> <app:codeSelect
					codeType="FeeTypeCode" type="select"
					name="prpLdlossPropMainVo.prpLdlossPropFees[${propIdx}].feeTypeCode"
					value="${prpLdlossPropFee.feeTypeCode }" clazz="must" />
		</span></td>
		<td style="width: 11.111%;"><input type="text" class="input-text"
			name="prpLdlossPropMainVo.prpLdlossPropFees[${propIdx}].lossQuantity"
			value="${prpLdlossPropFee.lossQuantity }" /></td>
		<td style="width: 11.111%;"><input type="text" class="input-text"
			name="prpLdlossPropMainVo.prpLdlossPropFees[${propIdx}].unitPrice"
			value="${prpLdlossPropFee.unitPrice }" onblur="calSumLossFee()" /></td>
		<td style="width: 11.111%;">
			<div class="radio-box">
				<input type="checkbox"
					name="prpLdlossPropMainVo.prpLdlossPropFees[${propIdx}].recycleFlag"
					value="${prpLdlossPropFee.recycleFlag}"
					onchange="recycleFlag(this)" />
			</div>
		</td>
		<td style="width: 11.111%;"><input type="text" class="input-text"
			name="prpLdlossPropMainVo.prpLdlossPropFees[${propIdx}].recyclePrice"
			value="${prpLdlossPropFee.recyclePrice }" onblur="calSumLossFee()" />
		</td>
		<td style="width: 11.111%;"><input type="text" class="input-text"
			name="prpLdlossPropMainVo.prpLdlossPropFees[${propIdx}].sumDefloss"
			value="${prpLdlossPropFee.sumDefloss }" /></td>
	</tr>
</c:forEach>


