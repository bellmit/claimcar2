<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<c:if test="${!empty checkChargeVo }">
	<tr>
		<c:if test="${fn:contains(taskVo.subNodeCode,'VLCar') ==false}">
			<td>
				<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao delwusunrowBtn btn-disabled" disabled="disabled" ></button>
			</td>
		</c:if>
		<td><app:codetrans codeType="KindCode" codeCode="${checkChargeVo.kindCode}"/> </td>
		<td>
			${checkChargeVo.chargeName }
		</td>
		<td>
		<span class="select-box">
			<app:codetrans codeType="GServiceType" codeCode="${checkChargeVo.serviceType }"/>
		</span>
		</td>	
		<td>
			<div >${checkChargeVo.chargeStandard }</div>
		</td>
		<td>
			${checkChargeVo.chargeFee }
		</td>
		<c:if test="${!empty lossCarMainVo.sumVeriChargeFee || fn:contains(taskVo.subNodeCode,'VLCar') ==true }" >
			<td></td>
		</c:if>
		<td>
			${checkChargeVo.floatReason }
		</td>
		<td>
		${checkChargeVo.receiver }
		</td>
	</tr>
</c:if>

			
