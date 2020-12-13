<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<div class="table_title f14">车辆事故责任信息</div>
<div class="table_cont mt-30 md-30">
	<div class="formtable">
		<table class="table table-border table-bordered text-c">
			<thead class="text-c">
				<tr>
					<th>车牌号</th>
					<th>事故责任比例</th>
					<th>交强险是否有责</th>
				</tr>
			</thead>
			<tbody class="text-c">
				<c:forEach var="checkDutyVo" items="${prpLCheckDutyVoList}" varStatus="status">
						<tr>
							<input type="hidden" name="prpLCheckDutyVoList[${status.index}].id" value="${checkDutyVo.id}">
							<input type="hidden" id="checkDutySeri" name="prpLCheckDutyVoList[${status.index}].serialNo" value="${checkDutyVo.serialNo}">
							<td width="30%"><c:if test="${checkDutyVo.serialNo eq 1}">标的车</c:if>
								<c:if test="${checkDutyVo.serialNo > 1}">三者车</c:if>
								(${checkDutyVo.licenseNo})</td>
							<td width="200px">
							<div class="form_input col-5">
							<span class="select-box"> 
								<app:codeSelect codeType="IndemnityDuty" type="select" name="prpLCheckDutyVoList[${status.index}].indemnityDuty"
										clazz="must Iindemnitydutys" nullToVal="4"  onchange="changeDuty(this,'${status.index}')"
										value="${checkDutyVo.indemnityDuty}" />
								<input type="hidden" name="indemnityDutyOri_${status.index }" value="${checkDutyVo.indemnityDuty}">
							</span> 
							</div>
							<div class="form_input col-5">
							<input type="text" class="input-text IindemnityDutyRates" style="width:70%"
								name="prpLCheckDutyVoList[${status.index}].indemnityDutyRate"
								onchange="compareDutyRate(this,'${status.index}')"
								datatype="/^(\d{1,2}(\.\d{1,2})?|100(\.0{1,2})?)$/"
								value="${checkDutyVo.indemnityDutyRate}" nullToVal="0.0"
								nullmsg="请输入信息！" readonly/>%
							</div>
							</td>
							<td width="100px"><span class="select-box"> <app:codeSelect
								codeType="CiIndemDuty" type="select" id="ciIndemDuty"
								name="prpLCheckDutyVoList[${status.index}].ciDutyFlag" clazz="must ciIndemDuty"
								value="${checkDutyVo.ciDutyFlag}" />
							</span>
							</td>
						</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
