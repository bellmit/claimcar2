<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<c:forEach items="${subrogationMain.prpLSubrogationCars }" var="subrogationCar" varStatus="status">
	<c:set var="carSize" value="${status.index + size  }"/>
	<tbody class="text-c" id="subrogationCarTbody_${carSize }">
	<tr>
		<td rowspan="3" style="width: 5%;">
			<button type="button"  class="btn fl btn-minus Hui-iconfont Hui-iconfont-jianhao" 
				name="subrogationCar_${carSize }" onclick="delSubrogationCar(this)" style="margin-left: 15%;" />
		</td>	
		<td rowspan="3" style="width: 5	%;border-left: 1px solid #ddd;border-right: 1px solid #ddd;">
			${carSize+1 }
		</td>
		<td>
			<div class="row cl">
				<label class="form_label col-1">姓名/名称</label>
				<div class="form_input col-3">
					<input type="text" class="input-text" name="subrogationMain.prpLSubrogationCars[${carSize}].linkerName" value="${subrogationCar.linkerName }" />
					 <input type="hidden" class="input-text" name="subrogationMain.prpLSubrogationCars[${carSize}].registNo" value="${subrogationCar.registNo }" /> 
				<input type="hidden" name="subrogationMain.prpLSubrogationCars[${carSize}].id" value="${subrogationCar.id }"/>
				</div>
				
				<label class="form_label col-1">车牌号码</label>
				<div class="form_input col-3">
					<span class="select-box">
						<app:codeSelect codeType="licenseNo" type="select" dataSource="${commonVo.thirdCarMap }" clazz="must" 
								name="subrogationMain.prpLSubrogationCars[${carSize}].serialNo" value="${subrogationCar.serialNo }"/>
					</span>		
					<input type="hidden" name="subrogationMain.prpLSubrogationCars[${carSize}].licenseNo" value="${subrogationCar.licenseNo }"/>
				</div>
				
				<label class="form_label col-1">车牌种类</label>
				<div class="form_input col-3">
					<span class="select-box">
						<app:codeSelect codeType="LicenseKindCode" type="select" clazz="must" name="subrogationMain.prpLSubrogationCars[${carSize}].licenseType" value="${subrogationCar.licenseType }"/>
					</span>
				</div>
			</div>	
		</td>
	</tr>
	<tr>
		<td>
		<div class="row cl">
			<div>
				<label class="form_label col-1">车辆VIN码</label>
				<div class="form_input col-3">
					<input type="text" class="input-text" name="subrogationMain.prpLSubrogationCars[${carSize}].vinNo" datatype="vin" value="${subrogationCar.vinNo }" onkeyup="toUpperCase(this)"/> <!-- datatype="vin" -->
				</div>
			</div>
			<div>
				<label class="form_label col-1">发动机号</label>
				<div class="form_input col-3">
					<input type="text" class="input-text" name="subrogationMain.prpLSubrogationCars[${carSize}].engineNo" datatype="*" value="${subrogationCar.engineNo }"/>
				</div>
			</div>
			<div>
				<label class="form_label col-1">商业承保公司</label>
				<div class="form_input col-3">
					<span class="select-box">
						<app:codeSelect codeType="CIInsurerCompany" type="select" name="subrogationMain.prpLSubrogationCars[${carSize}].biInsurerCode" value="${subrogationCar.biInsurerCode }"/>
					</span>
				</div>
			</div>	
			</div>	
		</td>
	</tr>
	<tr>
		<td>
		<div class="row cl">
				<label class="form_label col-1">商业承保地区</label>
				<div class="form_input col-3">
					<span class="select-box"> 
						<app:areaSelect targetElmId="biInsurerArea_${carSize}" style="width:98%" areaCode="${subrogationCar.biInsurerArea}"  showLevel="1" />
						<input type="hidden" id="biInsurerArea_${carSize}" name="subrogationMain.prpLSubrogationCars[${carSize}].biInsurerArea" value="${subrogationCar.biInsurerArea }" />
					</span>
				</div>
				
				<label class="form_label col-1">交强承保公司</label>
				<div class="form_input col-3">
					<span class="select-box">
						<app:codeSelect codeType="CIInsurerCompany" type="select" name="subrogationMain.prpLSubrogationCars[${carSize}].ciInsurerCode" value="${subrogationCar.ciInsurerCode }"/>
					</span>
				</div>
				
				<label class="form_label col-1">交强承保地区</label>
				<div class="form_input col-3">
					<span class="select-box">
						<app:areaSelect targetElmId="ciInsurerArea_${carSize}" style="width:98%"  areaCode="${subrogationCar.ciInsurerArea}"  showLevel="1" />
						<input type="hidden" id="ciInsurerArea_${carSize}" name="subrogationMain.prpLSubrogationCars[${carSize}].ciInsurerArea" value="${subrogationCar.ciInsurerArea }" />
					</span>
				</div>
			</div>	
		</td>
	</tr>
</tbody>
</c:forEach>	

			