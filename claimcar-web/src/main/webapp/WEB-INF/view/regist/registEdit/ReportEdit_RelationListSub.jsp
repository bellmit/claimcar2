<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="prpLCMain" items="${prpLCMains}" varStatus="status">
	<tr class="text-c">
		<td>
			<c:choose>
		       <c:when test="${prpLCMain.riskCode eq '1101'}">
						<input name="hadCode" policyNo="${prpLCMain.policyNo}" comCode="${prpLCMain.comCode}" frameNo="${prpLCMain.prpCItemCars[0].frameNo}" licenseNo="${prpLCMain.prpCItemCars[0].licenseNo}" group="ids" type="checkbox" value="交强" />
		       </c:when>
		       <c:otherwise>
		       		<input name="hadCode" policyNo="${prpLCMain.policyNo}" comCode="${prpLCMain.comCode}" frameNo="${prpLCMain.prpCItemCars[0].frameNo}" licenseNo="${prpLCMain.prpCItemCars[0].licenseNo}" group="ids" type="checkbox" value="商业" />
		       </c:otherwise>
			</c:choose>
		</td>

		<td>
			<a id="policyNo_${status.index }" data-hasqtip="0" href="/claimcar/policyView/policyView.do?policyNo=${prpLCMain.policyNo }" target="_blank">${prpLCMain.policyNo}</a>
		</td>										
		<td>${prpLCMain.prpCItemCars[0].licenseNo}</td>
		<td>${prpLCMain.prpCInsureds[0].insuredName}</td>
		<td>${prpLCMain.prpCItemCars[0].brandName}</td>
		<td></td>
		<td>
			<app:codetrans codeType="ComCodeFull" codeCode="${prpLCMain.comCode}"/>
		</td>
		<td>${prpLCMain.prpCItemCars[0].frameNo}</td>
		<td>${prpLCMain.prpCItemCars[0].engineNo}</td>
		<td><fmt:formatDate value="${prpLCMain.startDate}" pattern="yyyy-MM-dd"/></td>
		<td><fmt:formatDate value="${prpLCMain.endDate}" pattern="yyyy-MM-dd"/></td>
		<td>
			<c:choose>
				<c:when test="${prpLCMain.riskCode eq '1101'}">
		    		交强
		    		<input type="hidden" id="CIPolicyNo" value="${prpLCMain.policyNo}" />
				</c:when>
				<c:otherwise>
		       		商业
		       		<input type="hidden" id="BIPolicyNo" value="${prpLCMain.policyNo}" />
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</c:forEach>