<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
			
<c:forEach var="chargeFeeVo" items="${lossChargeVos}" varStatus="status">
	<tr>
		<td>
			<input type="button" title="删除" onclick="delCharge(this)" name="lossChargeVo_${status.index + size }" class="btn btn-zd fl" value="-" />
		</td>
		<td>
			<%-- <app:codeSelect codeType="kindCode " type="select" name="lossChargeVos[${status.index + size  }].kindCode" clazz="must"  dataSource="${map }"/> --%>
			<%-- <input type="text" class="input-text"  name="lossChargeVos[${status.index + size  }].kindCode" value="${chargeFeeVo.kindCode }"/> --%>
			<input type="hidden" class="input-text" name="lossChargeVos[${status.index + size  }].id" value="${chargeFeeVo.id }"/>
			<input type="hidden" class="input-text" name="lossChargeVos[${status.index + size  }].chargeCode" value="${chargeFeeVo.chargeCode }"/>
			<input type="hidden" class="input-text" name="lossChargeVos[${status.index + size  }].registNo" value="${chargeFeeVo.registNo }"/>
			<input type="hidden" class="input-text " name="lossChargeVos[${status.index + size  }].businessType" value="${chargeFeeVo.businessType }"/>
			<input type="hidden" class="input-text "  name="lossChargeVos[${status.index + size  }].chargeStandard" value="${chargeFeeVo.chargeStandard }"/>
		</td>
		<td>
			<input type="text" class="input-text "  name="lossChargeVos[${status.index + size  }].chargeName" value="${chargeFeeVo.chargeName }"/>
		</td>
		<td>
			<app:codeSelect codeType="GServiceType" type="select" name="lossChargeVos[${status.index + size  }].serviceType" value="${chargeFeeVo.serviceType }"/>
		</td>
		<td>
			${chargeFeeVo.chargeStandard }
		</td>
		<td>
			<input type="text" class="input-text "  name="lossChargeVos[${status.index + size  }].chargeFee"  onchange="calSumChargeFee(this,true)" value="${chargeFeeVo.chargeFee }"/>
		</td>
		<td>
			<input type="text" class="input-text "  name="lossChargeVos[${status.index + size  }].floatReason" value="${chargeFeeVo.floatReason }"/>
		</td>
		<td>
			<input type="text" class="input-text "  name="lossChargeVos[${status.index + size  }].receiver" value="${chargeFeeVo.receiver }"/>
		</td>
	</tr>
	
</c:forEach>
			
