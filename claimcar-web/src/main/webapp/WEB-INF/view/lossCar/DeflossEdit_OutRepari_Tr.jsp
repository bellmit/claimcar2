<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<c:forEach var="carRepair" items="${lossCarMainVo.outRepairList }" varStatus="status">	
	<c:set var="repariIndex" value="${status.index + size }"/>
	<tr>
		<td>
		<input type="hidden" class="input-text" name="prpLDlossCarRepairs[${repariIndex }].id" value="${carRepair.id }"/>
			<input type="text" class="input-text" name="prpLDlossCarRepairs[${repariIndex }].compName" value="${carRepair.compName }"/>
		</td>
		<td style="border-left:2px solid #91c9f9;">
			<input type="text" class="input-text" name="prpLDlossCarRepairs[${repariIndex }].sumDefLoss" datatype="amount" value="${carRepair.sumDefLoss }" onblur="calSumOutRepiar(this)"/>
		</td>
		<td>
			<input type="text" class="input-text" name="prpLDlossCarRepairs[${repariIndex }].remark" value="${carRepair.remark }"/>
		</td>
			<c:if test="${!empty lossCarMainVo.sumVeriLossFee || veriFeeFlag eq '1' }" >
				<td style="border-left:2px solid #91c9f9;">
					${carRepair.sumVeriLoss }
				</td>
			</c:if>
	</tr>
</c:forEach>