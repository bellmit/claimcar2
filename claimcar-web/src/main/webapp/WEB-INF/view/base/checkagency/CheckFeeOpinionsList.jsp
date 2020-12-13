<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!--  意见列表开始-->
<div class="table_wrap">
	<div class="table_title f14">意见列表</div>
	<div class="table_cont table_list">
		<table class="table table-border">
			<thead class="text-c">
				<tr>
					<th>序号</th>
					<th>角色</th>
					<th>操作人员</th>
					<th>发表意见时间</th>
					<th>意见</th>
					<th>意见说明</th>
				</tr>
			</thead>
			<!-- 此处放意见列表数据 -->
			<tbody class="text-c" style="font-size: 5px">
				<c:forEach var="claimText" items="${prpLClaimTextVos }" varStatus="chargeStatus">
					<tr>
						<td>${chargeStatus.index + 1 }</td>
						<td>
							<c:if test="${claimText.nodeCode=='CheckFeeQuery' }">查勘费录入</c:if>
							<c:if test="${claimText.nodeCode=='CheckFeeCheckQuery' }">查勘费审核</c:if>
						</td>
						<td>${claimText.operatorName }</td>
						<td>
							<fmt:formatDate value='${empty claimText.updateTime?claimText.createTime:claimText.updateTime}' pattern='yyyy-MM-dd HH:mm:ss' />
						</td>
						<td>
							<!--<app:codetrans codeType="AuditStatus" codeCode="${claimText.status }" />-->
							<c:if test="${claimText.status == 'submit' }">提交</c:if>
							<c:if test="${claimText.status == 'submitAudit' }">审核通过</c:if>
							<c:if test="${claimText.status == 'backIntermFee' }">退回申请</c:if>
						</td>
						<td>
							<div title="${claimText.remark }">
								<c:choose>
									<c:when test="${fn:length(claimText.remark ) > 4}"> 
									${fn:substring(claimText.remark, 0, 4)}......
									</c:when>
									<c:otherwise>  
									${claimText.remark }
									</c:otherwise>
								</c:choose>
							</div>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<!--  意见列表结束-->
