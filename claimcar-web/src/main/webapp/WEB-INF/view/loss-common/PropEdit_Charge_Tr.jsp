<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
			
<c:forEach var="chargeFeeVo" items="${lossChargeVos}" varStatus="status">
	<c:set var="cIdx" value="${status.index + size}"></c:set>
	<tr>
		<td>
			<button type="button"   class="fl btn btn-minus Hui-iconfont Hui-iconfont-jianhao" 
				onclick="delCharge(this)" name="lossChargeVo_${cIdx}" > </button>
		</td>
		<td>
			<app:codeSelect codeType="" type="select" name="lossChargeVos[${cIdx}].kindCode" 
					value="${chargeFeeVo.kindCode }" clazz="must"  dataSource="${kindMap}"/>

			<input type="hidden" class="input-text" name="lossChargeVos[${cIdx}].id" value="${chargeFeeVo.id }"/>
			<input type="hidden" class="input-text" name="lossChargeVos[${cIdx}].businessId" value="${chargeFeeVo.businessId }"/>
			<input type="hidden" class="input-text" name="lossChargeVos[${cIdx}].chargeCode" value="${chargeFeeVo.chargeCode }"/>
			<input type="hidden" class="input-text" name="lossChargeVos[${cIdx}].registNo" value="${chargeFeeVo.registNo }"/>
			<input type="hidden" class="input-text " name="lossChargeVos[${cIdx}].businessType" value="${chargeFeeVo.businessType }"/>
			<input type="hidden" class="input-text "  name="lossChargeVos[${cIdx}].chargeName" value="${chargeFeeVo.chargeName }"/>
		</td>
		<td>
			${chargeFeeVo.chargeName}
		</td>
			<td>
				<input type="text" class="input-text "  name="lossChargeVos[${cIdx}].chargeFee"  
					datatype="amount" onchange="calSumChargeFee(this,true)" value="${chargeFeeVo.chargeFee }"/>
				<input type="hidden" class="input-text" name="lossChargeVos[${cIdx}].veriChargeFee" 
				value="${chargeFeeVo.veriChargeFee } "/>
			</td>
		<td>
			<input type="text" class="input-text "  name="lossChargeVos[${status.index + size  }].receiver" 
					onclick="showPayCust(this)" value="${chargeFeeVo.receiver }"/>
			<input type="hidden" class="input-text "  name="lossChargeVos[${status.index + size  }].receiverId" value="${chargeFeeVo.receiverId }"/>
		</td>
	</tr>
</c:forEach>

			
