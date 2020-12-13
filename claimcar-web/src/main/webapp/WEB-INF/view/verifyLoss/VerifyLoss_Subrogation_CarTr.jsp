<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<c:forEach items="${subrogationMain.prpLSubrogationCars }" var="subrogationCar" varStatus="status">
	<c:set var="carSize" value="${status.index + size  }"/>
	<tbody class="text-c" id="subrogationCarTbody_${carSize }">
	<tr>
		<td rowspan="3" style="width: 5	%;border-left: 1px solid #ddd;border-right: 1px solid #ddd;">
			${carSize+1 }
		</td>
		<td>
			<div class="formtable">	
			<div class="row cl">
				<label class="form_label col-2">姓名/名称</label>
				<div class="form_input col-2">${subrogationCar.linkerName }
				</div>
				
				<label class="form_label col-2">车牌号码</label>
				<div class="form_input col-2">${subrogationCar.licenseNo }</div>
				
				<label class="form_label col-2">车牌种类</label>
				<div class="form_input col-2">
					<app:codetrans codeType="LicenseKindCode" codeCode="${subrogationCar.licenseType }"/>
				</div>
			</div>	
			</div>
		</td>
	</tr>
	<tr>
		<td>
		<div class="formtable">	
		<div class="row cl">
			<div>
				<label class="form_label col-2">车辆VIN码</label>
				<div class="form_input col-2">${subrogationCar.vinNo } </div>
			</div>
			<div>
				<label class="form_label col-2">发动机号</label>
				<div class="form_input col-2"> ${subrogationCar.engineNo }</div>
			</div>
			<div>
				<label class="form_label col-2">商业承保公司</label>
				<div class="form_input col-2">
					<app:codetrans codeType="CIInsurerCompany" codeCode="${subrogationCar.biInsurerCode }"/>
				</div>
			</div>	
			</div>	
			</div>
		</td>
	</tr>
	<tr>
		<td>
		<div class="formtable">	
		<div class="row cl">
				<label class="form_label col-2">商业承保地区</label>
				<div class="form_input col-2">
					<app:codetrans codeType="AreaCode" codeCode="${subrogationCar.biInsurerArea }"/>
				</div>
				
				<label class="form_label col-2">交强承保公司</label>
				<div class="form_input col-2">
					<app:codetrans codeType="CIInsurerCompany" codeCode="${subrogationCar.ciInsurerCode }"/>
				</div>
				
				<label class="form_label col-2">交强承保地区</label>
				<div class="form_input col-2">
					<app:codetrans codeType="AreaCode" codeCode="${subrogationCar.ciInsurerArea }"/>
				</div>
			</div>	
			</div>
		</td>
	</tr>
</tbody>
</c:forEach>	

			