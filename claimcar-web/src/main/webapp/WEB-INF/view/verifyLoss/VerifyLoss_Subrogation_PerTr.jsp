<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach items="${subrogationMain.prpLSubrogationPersons }" var="personVo" varStatus="status">
	<c:set var="personSize" value="${status.index + size  }"/>
	<tr class="text-c">
		<td rowspan="4" style="width: 5%;border-left: 1px solid #ddd;border-right: 1px solid #ddd;">
			${personSize+1 }
		</td>
		<td>
			<div class="formtable">	
			<div class="row cl">
				<label class="form_label col-2">姓名/名称</label>
				<div class="form_input col-2"> ${personVo.name } </div>
				
				<label class="form_label col-2">单位名称</label>
				<div class="form_input col-2">${personVo.unitName }</div>
				
				<label class="form_label col-2">联系人</label>
				<div class="form_input col-2">${personVo.linkerName }</div>
			</div>	
			</div>
		</td>
		</tr>
		<tr>
			<td>
			<div class="formtable">	
			<div class="row cl">
				<label class="form_label col-2">联系电话</label>
				<div class="form_input col-2">${personVo.phone }</div>
				
				<label class="form_label col-2">家庭（或单位）地址</label>
				<div class="form_input col-2">${personVo.address }</div>
				
				<label class="form_label col-2">邮政编码</label>
				<div class="form_input col-2">${personVo.zipno }</div>
			</div>
			</div>	
		</td>
		</tr>
		<tr>
			<td>
			<div class="formtable">	
				<div class="row cl">
					<label class="form_label col-2">身份证号码</label>
					<div class="form_input col-2">${personVo.identifyNumber }</div>
					
					<label class="form_label col-2">投保保险情况</label>
					<div class="form_input col-2">${personVo.insuredInfo }</div>
					
					<label class="form_label col-2">法定代表人姓名</label>
					<div class="form_input col-2">${personVo.lawLinkerName }</div>
				</div>	
				</div>
			</td>
		</tr>
		<tr>
			<td>
			<div class="formtable">	
				<div class="row cl">
					<label class="form_label col-2">单位联系号码</label>
					<div class="form_input col-2">${personVo.unitPhone } </div>
					
					<label class="form_label col-2">其他信息</label>
					<div class="form_input col-2">${personVo.otherInfo }</div>
					
					<label class="form_label col-2"></label>
					<div class="form_input col-2">
						
					</div>
				</div>	
				</div>
			</td>
		</tr>
</c:forEach>	
		
