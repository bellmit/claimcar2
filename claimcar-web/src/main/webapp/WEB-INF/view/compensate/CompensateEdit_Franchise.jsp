<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<div class="table_wrap">
	<div class="table_title f14">设置免赔率</div>
	<div class="table_cont" id="claimDeductDiv">
		<div class="formtable">
			<div class="row cl mb-10">
				<label class="form_label col-1">责任类型</label>
				<div class="form_input col-3">
				<c:choose>
					<c:when test="${prpLWfTaskVo.handlerStatus eq '3' || compWfFlag eq '1' ||compWfFlag eq '2'}">
						<app:codetrans codeType="IndemnityDuty" codeCode="${prpLCompensate.indemnityDuty }"/>
						<input type="hidden" name="prpLCompensate.indemnityDuty" value="${prpLCompensate.indemnityDuty }" />
					</c:when>
					<c:otherwise>
						<app:codeSelect id="indemnityDuty" codeType="IndemnityDuty" type="select" value="${prpLCompensate.indemnityDuty }"/>
						<input type="hidden" name="prpLCompensate.indemnityDuty" value="${prpLCompensate.indemnityDuty }" />
						<font class="must">*</font>
					</c:otherwise>
				</c:choose>

				</div>
				<label class="form_label col-1">责任比例</label>
				<div class="form_input col-3">
					<input type="text" class="input-text" name="prpLCompensate.indemnityDutyRate" value="${prpLCompensate.indemnityDutyRate }" 
						datatype="/^(\d{1,2}(\.\d{1,2})?|100(\.0{1,2})?)$/" />
					<font class="must">*</font>
				</div>
			</div>
			<div class="row cl">
				<div class="table_cont">
					<table class="table table-bordered table-bg">
						<thead class="text-c">
							<tr>
								<th>选择</th>
								<th>免赔条件</th>
								<th>选择</th>
								<th>免赔条件</th>
							</tr>
						</thead>
						<tbody class="text-c">
							<input type="hidden" id="deductSize" value="${fn:length(claimDeductVos)}">
							<c:forEach var="claimDeductVo" items="${claimDeductVos}" varStatus="status">
								<c:set var="idx" value="${status.index+deductSize}" />
								<c:if test="${status.index%2==0}">
									<tr>
								</c:if>
								<td class="text-c">
									<input type="checkbox" name="claimDeductVo[${idx}].isCheck"
									value="${claimDeductVo.isCheck}" onclick="isCheck(this)" />
									<input type="hidden" name="claimDeductVo[${idx}].id" value="${claimDeductVo.id}"/>
									<input type="hidden" name="claimDeductVo[${idx}].registNo" value="${claimDeductVo.registNo}"/>
									<input type="hidden" name="claimDeductVo[${idx}].policyNo" value="${claimDeductVo.policyNo}"/>
									<input type="hidden" name="claimDeductVo[${idx}].serialNo" value="${claimDeductVo.serialNo}"/>
									<input type="hidden" name="claimDeductVo[${idx}].riskCode" value="${claimDeductVo.riskCode}"/>
								</td>
								<td class="text-c">
									<input type="hidden" name="claimDeductVo[${idx}].deductCondCode"
										value="${claimDeductVo.deductCondCode}" />
									<input type="hidden" name="claimDeductVo[${idx}].deductCondName"
										value="${claimDeductVo.deductCondName}"/>
										${claimDeductVo.deductCondName}
								</td>
								<c:if test="${status.index%2!=0}">
									</tr>
								</c:if>
							</c:forEach>
						</tbody>
					</table>	
				</div>
			</div>
		</div>
		<input type="button" class="btn btn-primary mt-10" id="getAllRate" value="获取免赔率">
	</div>
</div>