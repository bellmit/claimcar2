<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<c:forEach var="agentList" items="${courtMessageVo.prpLCourtAgents}" varStatus="status">
<tr class="text-c">
	<td>${agentList.dlrName}</td>
	<td>${agentList.dlrzw}</td>
	<td><app:codetrans codeType="AgentPosition" codeCode="${agentList.dlrzw}" /></td>
	<td><app:codetrans codeType="AgentIDType" codeCode="${agentList.dlrIDtype}" /></td>
	<td>${agentList.dlrID}</td>
	<td>${agentList.dlrPhone}</td>
	<td>${agentList.personID}</td>
</tr>
</c:forEach>