<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<tr class="text-c">
	<input type="hidden" id="mainCarId" name="checkMainCarVo.carid" value="${checkMainCarVo.carid}">
	<input type="hidden" id="mainSerialNo" name="checkMainCarVo.serialNo" value="${checkMainCarVo.serialNo}">
	<input type="hidden" id="mainScheduleItem" name="checkMainCarVo.scheduleitem" value="${checkMainCarVo.scheduleitem}">
	<input type="hidden" id="licenseNo" name="checkMainCarVo.licenseNo" value="${checkMainCarVo.prpLCheckCarInfo.licenseNo}">
	<input type="hidden" id="brandName" name="checkMainCarVo.brandName" value="${checkMainCarVo.prpLCheckCarInfo.brandName}">
	<input type="hidden" id="carOwner" name="checkMainCarVo.carOwner" value="${checkMainCarVo.prpLCheckCarInfo.carOwner}">
	<input type="hidden" id="linkPhoneNumber" name="checkMainCarVo.linkPhoneNumber" value="${checkMainCarVo.prpLCheckDriver.linkPhoneNumber}">
	<input type="hidden" id="" name="checkMainCarVo.lossPart" value="${checkMainCarVo.lossPart}">
	<input type="hidden" id="sumLossFee" name="checkMainCarVo.sumLossFee" value="${checkMainCarVo.lossFee}">
	
	<td>${checkMainCarVo.prpLCheckCarInfo.licenseNo}</td>
	<td>${checkMainCarVo.prpLCheckCarInfo.brandName}</td>
	<td>${checkMainCarVo.prpLCheckCarInfo.carOwner}</td>
	<td>${checkMainCarVo.prpLCheckDriver.linkPhoneNumber}</td>
	<td><app:codetrans codeType="LossPart" codeCode="${checkMainCarVo.lossPart}" /></td>
	<td>${checkMainCarVo.lossFee}</td>
	<td>
		<!-- <input type="button" class="btn btn-secondary" " href="javascript:;"
			onclick="checkCarEdit(this)" inputIdx="-1" value="处理"> -->
	<c:choose>
		<c:when test="${(nodeCode eq 'Chk')&&(status eq '2')}">
			<input type="button" class="btn btn-secondary" href="javascript:;"
			onclick="checkCarEdit(this)" inputIdx="-1" value="处理">
		</c:when>
		<c:otherwise>
			<input type="button" class="btn btn-secondary" href="javascript:;" id="chkReOrChkBig"
			onclick="checkCarView('${checkMainCarVo.carid}')" inputIdx="-1" value="详情">
		</c:otherwise>
	</c:choose>
	</td>
</tr>