<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<c:if test="${checkCarVo.serialNo ne 1}">
	<div class="formtable tableoverlable">
		<div class="row cl">
			<label class="form_label col-1">商业险报案号</label>
			<div class="form_input col-2">
				<input type="text" class="input-text"
					name="checkCarVo.prpLCheckCarInfo.biRegistNo"
					value="${checkCarVo.prpLCheckCarInfo.biRegistNo}" />
			</div>
			<label class="form_label col-1">商业险保单号</label>
			<div class="form_input col-2">
				<input type="text" class="input-text"
					name="checkCarVo.prpLCheckCarInfo.biPolicyNo"
					value="${checkCarVo.prpLCheckCarInfo.biPolicyNo}" />
			</div>
			<label class="form_label col-1">商业承保机构</label>
			<div class="form_input col-2">
				<app:codeSelect codeType="CIInsurerCompany" type="select"
					lableType="name" name="checkCarVo.prpLCheckCarInfo.biInsureComCode"
					value="${checkCarVo.prpLCheckCarInfo.biInsureComCode}" value="016"
					style="width: 95%" />
			</div>
			<label class="form_label col-1">商业承保地区</label>
			<div class="form_input col-2">
				<input type="text" class="input-text"
					name="checkCarVo.prpLCheckCarInfo.biInsurerArea"
					value="${checkCarVo.prpLCheckCarInfo.biInsurerArea}" />
			</div>
		</div>
	</div>
	<div class="formtable tableoverlable">
		<div class="row cl">
			<label class="form_label col-1">交强险报案号</label>
			<div class="form_input col-2">
				<input type="text" class="input-text"
					name="checkCarVo.prpLCheckCarInfo.ciRegistNo"
					value="${checkCarVo.prpLCheckCarInfo.ciRegistNo}" />
			</div>
			<label class="form_label col-1">交强险保单号</label>
			<div class="form_input col-2">
				<input type="text" class="input-text"
					name="checkCarVo.prpLCheckCarInfo.ciPolicyNo"
					value="${checkCarVo.prpLCheckCarInfo.ciPolicyNo}" />
			</div>
			<label class="form_label col-1">交强承保机构</label>
			<div class="form_input col-2">
				<app:codeSelect codeType="CIInsurerCompany" type="select"
					style="width: 95%" lableType="name"
					name="checkCarVo.prpLCheckCarInfo.ciInsureComCode"
					value="${checkCarVo.prpLCheckCarInfo.ciInsureComCode}" />
			</div>
			<label class="form_label col-1">交强承保地区</label>
			<div class="form_input col-2">
				<input type="text" class="input-text"
					name="checkCarVo.prpLCheckCarInfo.ciInsurerArea"
					value="${checkCarVo.prpLCheckCarInfo.ciInsurerArea}" />
			</div>
		</div>
	</div>
</c:if>