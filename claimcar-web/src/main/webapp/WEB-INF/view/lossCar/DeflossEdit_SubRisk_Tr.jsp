<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
			
<c:forEach var="carSubRiskVo" items="${lossCarMainVo.prpLDlossCarSubRisks }" varStatus="status">
	<c:set var="subRiskSize" value="${status.index + size  }" />
	<tr>
		<td>
			<c:choose>
				<c:when test="${!empty carSubRiskVo.veriSubRiskFee }">
					<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao delwusunrowBtn btn-disabled" 
						disabled="disabled"  onclick="delSubRisk(this)" name="subRiskVo_${subRiskSize }"></button>	 
				</c:when>
				<c:otherwise>
					<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao delwusunrowBtn" 
						onclick="delSubRisk(this)" name="subRiskVo_${subRiskSize }"></button>
				</c:otherwise>
			</c:choose>	
		</td>
		<td>${carSubRiskVo.kindName }
			<input type="hidden" class="input-text"  name="lossCarMainVo.prpLDlossCarSubRisks[${subRiskSize }].kindName" value="${carSubRiskVo.kindName }"/>
			<input type="hidden" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${subRiskSize }].id" value="${carSubRiskVo.id }"/>
			<input type="hidden" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${subRiskSize }].subRiskKindCode" value="${carSubRiskVo.kindCode }"/>
			<input type="hidden" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${subRiskSize }].kindCode" value="${carSubRiskVo.kindCode }"/>
			<input type="hidden" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${subRiskSize }].registNo" value="${carSubRiskVo.registNo }"/>
			<input type="hidden" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${subRiskSize }].riskCode" value="${carSubRiskVo.riskCode }"/>
			<input type="hidden" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${subRiskSize }].unitAmount" value="${carSubRiskVo.unitAmount }"/>
		</td>
		<td>
			<c:if test="${carSubRiskVo.kindCode =='X' && !empty deviceMap }">
			  <app:codeSelect codeType="name" name="lossCarMainVo.prpLDlossCarSubRisks[${subRiskSize }].itemName" type="select" value="${carSubRiskVo.itemName }" dataSource="${deviceMap }" clazz="must" />
			</c:if>
			<c:if test="${carSubRiskVo.kindCode !='X' || empty deviceMap }">
				<input type="text" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${subRiskSize }].itemName"  value="${carSubRiskVo.itemName }"  />
			</c:if>
		</td>
		<c:choose>
			<c:when test="${carSubRiskVo.kindCode ne 'C' && carSubRiskVo.kindCode ne 'T' && carSubRiskVo.kindCode ne 'RF'}">
				<td></td>
				<td>
					<input type="text" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${subRiskSize }].subRiskFee" datatype="amount" value="${carSubRiskVo.subRiskFee }" onblur="upperCase()" />
				</td>
			</c:when>
			<c:otherwise>
				<td>
					<input type="text" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${subRiskSize }].count" datatype="n" value="${carSubRiskVo.count }" onblur="calsuRiskFee(this)"/>
				</td>
				<td>
					<input type="text" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${subRiskSize }].subRiskFee" readonly="readonly"  value="${carSubRiskVo.subRiskFee }" onblur="upperCase()"/>
				</td>
			</c:otherwise>
		</c:choose>
			<c:if test="${!empty lossCarMainVo.sumVeriLossFee }" >
				<td>
					${carSubRiskVo.veriSubRiskFee }
				</td>
				</c:if>
	</tr>
</c:forEach>
	

			