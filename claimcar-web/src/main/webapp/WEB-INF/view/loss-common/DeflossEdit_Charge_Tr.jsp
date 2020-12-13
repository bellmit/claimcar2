<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
			
<c:forEach var="chargeFeeVo" items="${lossChargeVos}" varStatus="status">
	<tr>
		<td>
			<c:choose>
				<c:when test="${!empty chargeFeeVo.veriChargeFee }">
					<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao delwusunrowBtn btn-disabled" 
						 disabled="disabled" onclick="delCharge(this)" name="lossChargeVo_${status.index + size }" ></button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao delwusunrowBtn" 
						 onclick="delCharge(this)" name="lossChargeVo_${status.index + size }" ></button>
				</c:otherwise>
			</c:choose>	
		</td>
		<td>
		<span class="select-box">
			<app:codeSelect codeType="kindCode " type="select" name="lossChargeVos[${status.index + size  }].kindCode" value="${chargeFeeVo.kindCode }" clazz="must"  dataSource="${kindMap }"/>
		</span>	
			<input type="hidden" class="input-text" name="lossChargeVos[${status.index + size  }].id" value="${chargeFeeVo.id }"/>
			<input type="hidden" class="input-text" name="lossChargeVos[${status.index + size  }].businessId" value="${chargeFeeVo.businessId }"/>
			<input type="hidden" class="input-text" name="lossChargeVos[${status.index + size  }].chargeCode" value="${chargeFeeVo.chargeCode }"/>
			<input type="hidden" class="input-text" name="lossChargeVos[${status.index + size  }].registNo" value="${chargeFeeVo.registNo }"/>
			<input type="hidden" class="input-text " name="lossChargeVos[${status.index + size  }].businessType" value="${chargeFeeVo.businessType }"/>
			<input type="hidden" class="input-text "  name="lossChargeVos[${status.index + size  }].chargeName" value="${chargeFeeVo.chargeName }"/>
		</td>
		<td>
			${chargeFeeVo.chargeName }
		</td>
			<td>
				<input type="text" class="input-text "  name="lossChargeVos[${status.index + size  }].chargeFee" datatype="amount" onblur="calSumChargeFee(this,true)" value="${chargeFeeVo.chargeFee }"/>
			</td>
			<c:if test="${!empty lossCarMainVo.sumVeriChargeFee || veriChargeFlag eq '1' }" >
				<td>${chargeFeeVo.veriChargeFee }</td>
			</c:if>
		<td>
			<input type="text" class="input-text "  name="lossChargeVos[${status.index + size  }].receiver" 
					onclick="showPayCust(this)" value="${chargeFeeVo.receiver }"/>
			<input type="hidden" class="input-text "  name="lossChargeVos[${status.index + size  }].receiverId" value="${chargeFeeVo.receiverId }"/>
		</td>
	</tr>
</c:forEach>

			
