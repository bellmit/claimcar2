<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="prpLRegistPropLoss" varStatus="status" items="${prpLRegistPropLosses}">
	<tr class="text-c wusun-detail-body">
	<td class="wusun-del-btn">
	<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao delwusunrowBtn" name="delProp_${status.index + propSize}" onclick="delProp(this)"></button>
	</td>
		<td>
			<input name="prpLRegistPropLosses[${status.index + propSize}].lossitemname" licenseNo="${prpLRegistPropLoss.licenseNo}" id="lossitemname"  value="${prpLRegistPropLoss.lossitemname}" maxlength="30" datatype="*1-30" />
		</td>
		<td>
			<app:codeSelect type="select" id="licenseNo" codeType="DefLossItemType" dataSource="${carNoMap}" onchange="writeLossParty(this, ${status.index + propSize})" name="prpLRegistPropLosses[${status.index + propSize}].licenseNo" value="${prpLRegistPropLoss.licenseNo}" />
			<input type="hidden" name="prpLRegistPropLosses[${status.index + propSize}].lossparty" value="${prpLRegistPropLoss.lossparty}" />
			<%-- <app:codeSelect type="select" codeType="DefLossItemType" id="lossparty" dataSource="${carNoMap }" name="prpLRegistPropLosses[${status.index + propSize}].lossparty" value="${prpLRegistPropLoss.lossparty}" /> --%>
		</td>
		<td>
		<app:codeSelect codeType="YN10" type="radio" name="prpLRegistPropLosses[${status.index + propSize}].damagelevel" value="${prpLRegistPropLoss.damagelevel }" nullToVal="0" onclick="writeAccidentTypes()" />
		<%-- 	<input name="prpLRegistPropLosses[${status.index + propSize}].damagelevel" id="damagelevel" value="${prpLRegistPropLoss.damagelevel}" maxlength="40" datatype="*0-40" />
		 --%>
		</td>
		<td>
			<input name="prpLRegistPropLosses[${status.index + propSize}].lossremark" value="${prpLRegistPropLoss.lossremark}" maxlength="100" datatype="*0-100" />
			<input type="hidden" name="prpLRegistPropLosses[${status.index + propSize}].id" value="${prpLRegistPropLoss.id}" />
			<input type="hidden" name="prpLRegistPropLosses[${status.index + propSize}].createUser" value="${prpLRegistPropLoss.createUser}" />
			<input type="hidden" name="prpLRegistPropLosses[${status.index + propSize}].createTime" value="<fmt:formatDate value='${prpLRegistPropLoss.createTime}' type='both'/>" />
			<input type="hidden" name="prpLRegistPropLosses[${status.index + propSize}].updateUser" value="${prpLRegistPropLoss.updateUser}" />
			<input type="hidden" name="prpLRegistPropLosses[${status.index + propSize}].updateTime" value="<fmt:formatDate value='${prpLRegistPropLoss.updateTime}' type='both'/>" />
		</td>
	   <%-- <td>
			<a class="btn btn-zd fl" id="delProp" onclick="delProp(this)">删除</a>
		</td> --%>
	</tr>
</c:forEach>