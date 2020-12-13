<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<c:forEach items="${subrogationMain.prpLSubrogationCars }"
	var="subrogationCar" varStatus="status">
	<c:set var="carSize" value="${status.index + size  }" />
	<tbody class="text-c" id="subrogationCarTbody_${carSize }">
		<tr>
			<td rowspan="3" style="width: 5%;">
				<button type="button" style="margin-left: 15%;"
					class="btn fl btn-minus Hui-iconfont Hui-iconfont-jianhao"
					name="subrogationCar_${carSize }" onclick="delSubrogationCar(this)"/>
			</td>
			<td rowspan="3" style="width: 5%; border-left: 1px solid #ddd; border-right: 1px solid #ddd;">
			${carSize+1 }
			</td>
			<td>
				<div class="row cl">
					<label class="form_label col-1">姓名/名称</label>
					<div class="form_input col-3">
						<input type="text" style="width: 80%" class="input-text" datatype="*"
							name="subrogationMain.prpLSubrogationCars[${carSize}].linkerName"
							value="${subrogationCar.linkerName }" /><font class="must">*</font>
							
						<input type="hidden" class="input-text" name="subrogationMain.prpLSubrogationCars[${carSize}].registNo"
							value="${subrogationCar.registNo }" />
						<input type="hidden" name="subrogationMain.prpLSubrogationCars[${carSize}].id"
							value="${subrogationCar.id }" />

					</div>

					<label class="form_label col-1">车牌号码</label>
					<div class="form_input col-3">
						<span class="select-box" style="width: 83%">
						<app:codeSelect codeType="" type="select" dataSource="${subLiNoMap}" lableType="name"
								name="subrogationMain.prpLSubrogationCars[${carSize}].serialNo" clazz="must"
								value="${subrogationCar.serialNo}"/>
						</span>
						<input type="hidden" name="subrogationMain.prpLSubrogationCars[${carSize}].licenseNo" value="${subrogationCar.licenseNo}">
						<font class="must">*</font>
					</div>

					<label class="form_label col-1">车牌种类</label>
					<div class="form_input col-3">
						<span class="select-box" style="width: 81%">
							<app:codeSelect codeType="LicenseKindCode" type="select" clazz="must"
								name="subrogationMain.prpLSubrogationCars[${carSize}].licenseType" value="${subrogationCar.licenseType }" />
						</span>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="row cl">
					<label class="form_label col-1">车辆VIN码</label>
					<div class="form_input col-3">
						<input type="text" style="width: 80%" class="input-text" datatype="vinNo"
							name="subrogationMain.prpLSubrogationCars[${carSize}].vinNo"
                            value="${subrogationCar.vinNo }" onkeyup="toUpperCase(this)"/><font class="must">*</font><!-- datatype="vinNo" -->
					</div>

					<label class="form_label col-1">发动机号</label>
					<div class="form_input col-3">
						<input type="text" style="width: 80%" class="input-text" datatype="letterAndNumber"
							name="subrogationMain.prpLSubrogationCars[${carSize}].engineNo" nullmsg="请填写发动机号！"
							value="${subrogationCar.engineNo }" /> <font class="must">*</font>
					</div>

					<label class="form_label col-1">商业险承保公司</label>
					<div class="form_input col-3">
						<span class="select-box" style="width: 81%"> <app:codeSelect
								codeType="CIInsurerCompany" type="select"
								name="subrogationMain.prpLSubrogationCars[${carSize}].biInsurerCode"
								value="${subrogationCar.biInsurerCode }" />
						</span>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="row cl">
					<label class="form_label col-1">商业险承保地区</label>
					<div class="form_input col-3">
						<span class="select-box" style="width: 80%"> <app:areaSelect
								targetElmId="biInsurerArea_${carSize}" style="width:98%"
								areaCode="${subrogationCar.biInsurerArea}" showLevel="1" /> <input
							type="hidden" id="biInsurerArea_${carSize}"
							name="subrogationMain.prpLSubrogationCars[${carSize}].biInsurerArea"
							value="${subrogationCar.biInsurerArea }" /> <font class="must">&nbsp;</font>
						</span>
					</div>

					<label class="form_label col-1">交强险承保公司</label>
					<div class="form_input col-3">
						<span class="select-box" style="width: 84%"> <app:codeSelect
								codeType="CIInsurerCompany" type="select"
								name="subrogationMain.prpLSubrogationCars[${carSize}].ciInsurerCode"
								value="${subrogationCar.ciInsurerCode }" /> <font class="must">&nbsp;</font>
						</span>
					</div>

					<label class="form_label col-1">交强险承保地区</label>
					<div class="form_input col-3">
						<span class="select-box" style="width: 80%"> <app:areaSelect
								targetElmId="ciInsurerArea_${carSize}" style="width:98%"
								areaCode="${subrogationCar.ciInsurerArea}" showLevel="1" /> <input
							type="hidden" id="ciInsurerArea_${carSize}"
							name="subrogationMain.prpLSubrogationCars[${carSize}].ciInsurerArea"
							value="${subrogationCar.ciInsurerArea }" />
						</span>
					</div>
				</div>
			</td>
		</tr>
	</tbody>
</c:forEach>

