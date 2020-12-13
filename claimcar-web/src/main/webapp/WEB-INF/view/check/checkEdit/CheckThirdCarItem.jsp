<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

	<tr class="text-c" id="carLossTr">
		<%-- <input type="hidden" value="${inputIdx}"/> --%>
		<input type="hidden" name="checkThirdCarVos[${inputIdx}].carid" value="${checkThirdCarVo.carid}">
		<input type="hidden" name="checkThirdCarVos[${inputIdx}].serialNo" value="${checkThirdCarVo.serialNo}">
		<input type="hidden" name="checkThirdCarVos[${inputIdx}].scheduleitem" value="${checkThirdCarVo.scheduleitem}">
		<input type="hidden" name="checkThirdCarVos[${inputIdx}].prpLCheckCarInfo.licenseNo" value="${checkThirdCarVo.prpLCheckCarInfo.licenseNo}">
		<input type="hidden" name="checkThirdCarVos[${inputIdx}].prpLCheckCarInfo.brandName" value="${checkThirdCarVo.prpLCheckCarInfo.brandName}">
		<input type="hidden" name="checkThirdCarVos[${inputIdx}].prpLCheckDriver.driverName" value="${checkThirdCarVo.prpLCheckDriver.driverName}">
		<input type="hidden" name="checkThirdCarVos[${inputIdx}].prpLCheckDriver.linkPhoneNumber" value="${checkThirdCarVo.prpLCheckDriver.linkPhoneNumber}">
		<input type="hidden" name="checkThirdCarVos[${inputIdx}].lossPart" value="${checkThirdCarVo.lossPart}">
		<input type="hidden" name="checkThirdCarVos[${inputIdx}].lossFee" value="${checkThirdCarVo.lossFee}">
		
		<td>${checkThirdCarVo.prpLCheckCarInfo.licenseNo}</td>
		<td>${checkThirdCarVo.prpLCheckCarInfo.brandName}</td>
		<td>${checkThirdCarVo.prpLCheckDriver.driverName}</td>
		<td>${checkThirdCarVo.prpLCheckDriver.linkPhoneNumber}</td>
		<td><app:codetrans codeType="LossPart" codeCode="${checkThirdCarVo.lossPart}"/></td>
		<td id="thLossFee" value="${checkThirdCarVo.lossFee}">${checkThirdCarVo.lossFee}</td>
		<c:choose>
			<%-- <c:when test="${(nodeCode eq 'Chk')&&(status eq '2')}"> --%>
			<c:when test="${(nodeCode eq 'Chk')&&(statusFlag eq '2')}">
				<td><input type="button" class="btn btn-secondary" onclick="historyDanger('${checkThirdCarVo.carid}')" value="出险历史"></td>
				<td><input type="button" class="btn btn-secondary" href="javascript:;"
					value="处理" id="initThirdCar" onclick="checkCarEdit(this)" inputIdx="${inputIdx}" />
					<input type="button" class="btn btn-secondary" name="delThirdCarTr_${inputIdx}"
						onclick="dropThirdCar(this)" inputIdx="${inputIdx}" value="删除" />
				</td>
			</c:when>
			<c:otherwise>
				<td><input type="button" class="btn btn-secondary" onclick="historyDanger('${checkThirdCarVo.carid}')" value="出险历史"></td>
				<td>
					<%-- <button class="btn btn-secondary" onclick="checkCarView('${checkThirdCarVo.carid}')" 
					inputIdx="${inputIdx}">详情</button> --%>
				<input type="button" class="btn btn-secondary" href="javascript:;"
					value="详情" name="initThirdCarsss" onclick="checkCarView('${checkThirdCarVo.carid}')" inputIdx="${inputIdx}">
				<input type="button" class="btn btn-secondary" name="delThirdCarTr_${inputIdx}"
					onclick="" inputIdx="${inputIdx}" value="删除">
			</c:otherwise>
		</c:choose>
		<%-- <td><input type="button" class="btn btn-zd" onclick=""value="出险历史"></td>
		<td><input type="button" class="btn btn-zd" href="javascript:;" value="处理"
					id="initThirdCar" onclick="checkCarEdit(this)" inputIdx="${inputIdx}">
			<input type="button" class="btn btn-zd" 
			name="delThirdCarTr_${inputIdx}" onclick="dropThirdCar(this)" inputIdx="${inputIdx}" value="删除">
		</td> --%>
	</tr>
	<%-- <a class="btn btn-zd fl" href="javascript:;" id="initThirdCar" onclick="checkCarEdit(${inputIdx})">处理</a> --%>