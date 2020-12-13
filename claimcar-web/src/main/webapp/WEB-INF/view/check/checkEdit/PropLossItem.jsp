<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%--tbody内容,财产损失项 --%>
<c:forEach var="checkPropVo" items="${checkPropVos}" varStatus="status">
	<c:set var="inputIdx" value="${status.index + propSize}" />
	<tr class="text-c" id="propTr" name="propTr">
		<input type="hidden" id="propId${inputIdx}" name="checkPropVos[${inputIdx}].id" value="${checkPropVo.id}">
		<input type="hidden" id="propRegistNo"   name="checkPropVos[${inputIdx}].registNo" value="${checkPropVo.registNo}">
		<input type="hidden" id="propItemId${inputIdx}" name="checkPropVos[${inputIdx}].scheduleitem" value="${checkPropVo.scheduleitem}">
		<td>
				<app:codeSelect codeType="" type="select" dataSource="${loss}" lableType="name"
					onchange="setSelectName(this,'lossPropName_${inputIdx}')" clazz="must"
					name="checkPropVos[${inputIdx}].lossPartyId" value="${checkPropVo.lossPartyId}" nullToVal="0"/>
			<input type="hidden" id="lossPropName_${inputIdx}" name="checkPropVos[${inputIdx}].lossPartyName" value="${checkPropVo.lossPartyName}">
		</td>
		<td><app:codeSelect codeType="IsPay" type="select" clazz="must" 
				name="checkPropVos[${inputIdx}].isNoClaim" onchange="" nullToVal="0" value="${checkPropVo.isNoClaim}"/></td>
		<td>
			<input type="text" class="input-text" id="lossItemName${inputIdx}" datatype="*0-100"
					name="checkPropVos[${inputIdx}].lossItemName"
					value="${checkPropVo.lossItemName}">
		</td>
		<td>
			<input type="text" style="vertical-align:middle;width:40%;" class="input-text" datatype="number"
					name="checkPropVos[${inputIdx}].lossNum" errormsg="请输入正确的数字！" value="${checkPropVo.lossNum}" />
			<input type="text" style="vertical-align:middle;width:15%;" class="input-text" datatype="*"
			name="checkPropVos[${inputIdx}].lossFeeType" errormsg="请输入损失单位！" maxlength="10" value="${checkPropVo.lossFeeType}" />
		</td>
		<td>
			<app:codeSelect codeType="LossLevel" type="select"
					name="checkPropVos[${inputIdx}].lossDegreeCode" clazz="must"
					value="${checkPropVo.lossDegreeCode}" />
		</td>
		<td>
			<input type="text" class="input-text" name="checkPropVos[${inputIdx}].lossFee" 
			errormsg="请输入正确的金额，只能精确到两位小数！" value="${checkPropVo.lossFee}" datatype="amount" />
		</td>
		<td><c:if test="${nodeCode eq 'Chk'}">
				<button type="button" class="btn btn-plus Hui-iconfont Hui-iconfont-jianhao"
					onclick="delPropItem(this)" name="delPropItem_${inputIdx}"></button>
			</c:if>
		</td>
	</tr>
</c:forEach>
<!--  -->
