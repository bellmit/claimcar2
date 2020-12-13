<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
			
<c:forEach var="chargeFeeVo" items="${lossChargeVos}" varStatus="status">
	<tr>
		<td>
			<input type="button" title="删除" onclick="delCharge(this)" name="lossChargeVo_${status.index + size }" class="btn btn-zd fl" value="-" />
		</td>
		<td>
			<app:codeSelect codeType="kindCode " type="select" name="lossChargeVos[${status.index + size  }].kindCode" value="${chargeFeeVo.kindCode }" clazz="must"  dataSource="${kindMap }"/>
			<%-- <input type="text" class="input-text"  name="lossChargeVos[${status.index + size  }].kindCode" value="${chargeFeeVo.kindCode }"/> --%>
			<input type="hidden" class="input-text" name="lossChargeVos[${status.index + size  }].id" value="${chargeFeeVo.id }"/>
			<input type="hidden" class="input-text" name="lossChargeVos[${status.index + size  }].businessId" value="${chargeFeeVo.businessId }"/>
			<input type="hidden" class="input-text" name="lossChargeVos[${status.index + size  }].chargeCode" value="${chargeFeeVo.chargeCode }"/>
			<input type="hidden" class="input-text" name="lossChargeVos[${status.index + size  }].registNo" value="${chargeFeeVo.registNo }"/>
			<input type="hidden" class="input-text " name="lossChargeVos[${status.index + size  }].businessType" value="${chargeFeeVo.businessType }"/>
			<input type="hidden" class="input-text "  name="lossChargeVos[${status.index + size  }].chargeStandard" value="${chargeFeeVo.chargeStandard }"/>
			<input type="hidden" class="input-text "  name="lossChargeVos[${status.index + size  }].chargeName" value="${chargeFeeVo.chargeName }"/>
		</td>
		<td>
			${chargeFeeVo.chargeName }
		</td>
			<c:choose>
				<c:when test="${chargeFeeVo.chargeCode == '13'}">
					<td>
						<app:codeSelect codeType="GServiceType" type="select" clazz="must" name="lossChargeVos[${status.index + size  }].serviceType" onchange="changeServerType(this)" value="${chargeFeeVo.serviceType }"/>
					</td>	
					<td>
						<input type="hidden" name="lossChargeVos[${status.index + size  }].chargeStandard" value="chargeFeeVo.chargeStandard"/>
						<div id="chargeStandard_${status.index + size  }">${chargeFeeVo.chargeStandard }</div>
					</td>
					<td>
						<input type="text" class="input-text "  name="lossChargeVos[${status.index + size  }].chargeFee" datatype="n" onchange="calSumChargeFee(this,true)" value="${chargeFeeVo.chargeFee }"/>
					</td>
					<td>
						<input type="text" class="input-text "  name="lossChargeVos[${status.index + size  }].floatReason" value="${chargeFeeVo.floatReason }"/>
					</td>
				</c:when>
				<c:otherwise>
					<td>
						<input type="text" class="input-text " placeholder="不可录入" readonly="readonly""/>
					</td>
					<td>
						<div id="chargeStandard_${status.index + size  }">${chargeFeeVo.chargeStandard }</div>
					</td>
					<td>
						<input type="text" class="input-text "  name="lossChargeVos[${status.index + size  }].chargeFee" datatype="n" onchange="calSumChargeFee(this,true)" value="${chargeFeeVo.chargeFee }"/>
					</td>
					<td>
						<input type="text" class="input-text " placeholder="不可录入" readonly="readonly""/>
					</td>
				</c:otherwise>
			</c:choose>
		<td>
			<input type="text" class="input-text "  name="lossChargeVos[${status.index + size  }].receiver" value="${chargeFeeVo.receiver }"/>
		</td>
	</tr>
</c:forEach>
			
