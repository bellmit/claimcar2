<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!-- 设置免赔率 开始 -->
<div class="table_wrap">
	<div id="claimDeduct_title" class="table_title f14">设置免赔率</div>
	<div class="table_cont table_list ">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th style="width: 10%">选择</th>
					<th style="width: 30%">免赔条件</th>
					<th style="width: 10%">选择</th>
					<th style="width: 30%">免赔条件</th>
				</tr>
			</thead>
			<tbody>
				<input type="hidden" id="deductSize" value="${fn:length(claimDeductVos)}">
				<c:forEach var="claimDeductVo" items="${claimDeductVos}" varStatus="status">
					<c:set var="idx" value="${status.index+deductSize}" />
					<c:if test="${status.index%2==0}">
						<tr>
					</c:if>
					<td class="text-c">
						<input type="checkbox" name="claimDeductVo[${idx}].isCheck"
						value="${claimDeductVo.isCheck}" onclick="isCheck(this)"/>
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
<!-- 设置免赔率 结束 -->