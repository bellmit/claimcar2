<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<div class="table_title f14">责任比例信息</div>
<div class="table_cont mt-30 md-30">
	<div class="formtable">
		<table class="table table-border table-bordered text-c">
			<thead class="text-c">
				<tr>
					<th>车牌号</th>
					<th>事故责任比例</th>
					<th>交强险是否有责</th>
					<th>交强险保险公司</th>
					<th>交强险保单号</th>
					<th>是否无责代赔</th>
				</tr>
			</thead>
			<tbody class="text-c">
				<tr>
					<td width="100px"><c:if test="${prpLCheckDutyVo.serialNo eq 1}">标的车</c:if>
						<c:if test="${prpLCheckDutyVo.serialNo > 1}">三者车</c:if>
						(${prpLCheckDutyVo.licenseNo})</td>
					<td width="200px">
					<div class="form_input col-5">
					<span class="select-box"> <app:codeSelect
								codeType="IndemnityDuty" type="select" id="Iindemnityduty"
								name="prpLCheckDutyVo.indemnityDuty" clazz="must" nullToVal="4"
								onchange="changeDuty(this,'-1')"
								value="${prpLCheckDutyVo.indemnityDuty}" />
					</span>
					</div>
					<div class="form_input col-5">
					<input type="text" class="input-text" style="width:70%"
						id="IindemnityDutyRate" name="prpLCheckDutyVo.indemnityDutyRate"
						onchange="compareDutyRate(this,'-1')"
						datatype="/^(\d{1,2}(\.\d{1,2})?|100(\.0{1,2})?)$/"
						value="${prpLCheckDutyVo.indemnityDutyRate}" nullToVal="0.0"
						nullmsg="请输入信息！" maxlength="3"/>%</div></td>
					<td width="100px"><span class="select-box"> <app:codeSelect
								codeType="CiIndemDuty" type="select" id="ciIndemDuty"
								name="prpLCheckDutyVo.ciDutyFlag" clazz="must" nullToVal="1"
								value="${prpLCheckDutyVo.ciDutyFlag}" />
					</span>
					</td>
					<td width="150px"></td>
					<td width="100px"></td>
					<td width="50px"></td>
				</tr>
				<c:forEach var="checkDutyVo" items="${prpLCheckDutyVoList}"
					varStatus="status">
					<input type="hidden" name="prpLCheckDutyVoList[${status.index}].id" value="${checkDutyVo.id}">
						<tr>
							<td width="100px"><c:if test="${checkDutyVo.serialNo eq 1}">标的车</c:if>
								<c:if test="${checkDutyVo.serialNo > 1}">三者车</c:if>
								(${checkDutyVo.licenseNo})</td>
							<td width="200px">
							<div class="form_input col-5">
							<span class="select-box"> <app:codeSelect
										codeType="IndemnityDuty" type="select"
										name="prpLCheckDutyVoList[${status.index}].indemnityDuty"
										clazz="must Iindemnitydutys" nullToVal="4"
										onchange="changeDuty(this,'${status.index}')"
										value="${checkDutyVo.indemnityDuty}" />
							</span> 
							</div>
							<div class="form_input col-5">
							<input type="text" class="input-text IindemnityDutyRates" style="width:70%"
								name="prpLCheckDutyVoList[${status.index}].indemnityDutyRate"
								onchange="compareDutyRate(this,'${status.index}')"
								datatype="/^(\d{1,2}(\.\d{1,2})?|100(\.0{1,2})?)$/"
								value="${checkDutyVo.indemnityDutyRate}" nullToVal="0.0"
								nullmsg="请输入信息！"  maxlength="3"/>%
							</div>
							</td>
							<td width="100px"></td>
							<td width="150px">
							<app:codeSelect codeType="CIInsurerCompany" type="select"
										style="width: 95%" lableType="name"
										name="prpLCheckDutyVoList[${status.index}].ciInsureComCode"
										value="${checkDutyVo.ciInsureComCode}"/>
	                        </td>
							<td width="100px"><input type="text" class="input-text"
										maxlength="30"
										name="prpLCheckDutyVoList[${status.index}].ciPolicyNo"
										value="${checkDutyVo.ciPolicyNo}" /></td>
							<td width="50px"><input type = 'checkbox' value="1" name="prpLCheckDutyVoList[${status.index}].noDutyPayFlag" <c:if test ="${checkDutyVo.noDutyPayFlag eq '1'}">checked</c:if>></td>
						</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
