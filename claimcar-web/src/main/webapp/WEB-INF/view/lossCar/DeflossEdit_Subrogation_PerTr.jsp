<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach items="${subrogationMain.prpLSubrogationPersons }" var="personVo" varStatus="status">
	<c:set var="personSize" value="${status.index + size  }"/>
	<tr class="text-c">
		<td class="" rowspan="4" style="width: 5%;">
			<button type="button" class="btn fl btn-minus Hui-iconfont Hui-iconfont-jianhao" 
				name="subrogationPer_${personSize }"  onclick="delSubrogationPers(this)" style="margin-left: 15%;" /> 
		</td>
		<td rowspan="4" style="width: 5%;border-left: 1px solid #ddd;border-right: 1px solid #ddd;">
			<input type="hidden" class="input-text" name="subrogationMain.prpLSubrogationPersons[${personSize}].serialNo" value="${personVo.serialNo }" />
			${personSize+1 }
		</td>
		<td>
			<div class="row cl">
				<label class="form_label col-1">姓名/名称</label>
				<div class="form_input col-3">
				<input type="hidden" class="input-text" name="subrogationMain.prpLSubrogationPersons[${personSize}].id" value="${personVo.id }" />
				 	<input type="hidden" class="input-text" name="subrogationMain.prpLSubrogationPersons[${personSize}].registNo"  value="${personVo.registNo }" /> 
					<input type="text" class="input-text" name="subrogationMain.prpLSubrogationPersons[${personSize}].name" datatype="*" value="${personVo.name }" />
				</div>
				
				<label class="form_label col-1">单位名称</label>
				<div class="form_input col-3">
					<input type="text" class="input-text" name="subrogationMain.prpLSubrogationPersons[${personSize}].unitName" value="${personVo.unitName }" />
				</div>
				
				<label class="form_label col-1">联系人</label>
				<div class="form_input col-3">
					<input type="text" class="input-text" name="subrogationMain.prpLSubrogationPersons[${personSize}].linkerName" value="${personVo.linkerName }" />
				</div>
			</div>	
		</td>
		</tr>
		<tr>
			<td>
			<div class="row cl">
				<label class="form_label col-1">联系电话</label>
				<div class="form_input col-3">
					<input type="text" class="input-text" name="subrogationMain.prpLSubrogationPersons[${personSize}].phone" value="${personVo.phone }" />
				</div>
				
				<label class="form_label col-1">家庭（或单位）地址</label>
				<div class="form_input col-3">
					<input type="text" class="input-text" name="subrogationMain.prpLSubrogationPersons[${personSize}].address" value="${personVo.address }" />
				</div>
				
				<label class="form_label col-1">邮政编码</label>
				<div class="form_input col-3">
					<input type="text" class="input-text" name="subrogationMain.prpLSubrogationPersons[${personSize}].zipno" value="${personVo.zipno }" />
				</div>
			</div>	
		</td>
		</tr>
		<tr>
			<td>
				<div class="row cl">
					<label class="form_label col-1">身份证号码</label>
					<div class="form_input col-3">
						<input type="text" class="input-text" datatype="idcard" ignore="ignore"
							name="subrogationMain.prpLSubrogationPersons[${personSize}].identifyNumber" value="${personVo.identifyNumber }" />
					</div>
					
					<label class="form_label col-1">投保保险情况</label>
					<div class="form_input col-3">
						<input type="text" class="input-text" name="subrogationMain.prpLSubrogationPersons[${personSize}].insuredInfo" value="${personVo.insuredInfo }" />
					</div>
					
					<label class="form_label col-1">法定代表人姓名</label>
					<div class="form_input col-3">
						<input type="text" class="input-text" name="subrogationMain.prpLSubrogationPersons[${personSize}].lawLinkerName" value="${personVo.lawLinkerName }" />
					</div>
				</div>	
			</td>
		</tr>
		<tr>
			<td>
				<div class="row cl">
					<label class="form_label col-1">单位联系号码</label>
					<div class="form_input col-3">
						<input type="text" class="input-text" name="subrogationMain.prpLSubrogationPersons[${personSize}].unitPhone" value="${personVo.unitPhone }" />
					</div>
					
					<label class="form_label col-1">其他信息</label>
					<div class="form_input col-3">
						<input type="text" class="input-text" name="subrogationMain.prpLSubrogationPersons[${personSize}].otherInfo" value="${personVo.otherInfo }" />
					</div>
					
					<label class="form_label col-1"></label>
					<div class="form_input col-3">
						
					</div>
				</div>	
			</td>
		</tr>
</c:forEach>	
		
