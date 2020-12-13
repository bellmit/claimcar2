<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>大案预报</title>
</head>
<body>
	<!-- 意见列表 开始 -->
	<div class="table_wrap">
		<div class="table_title f14">意见列表(查勘)</div>
		<div class="table_cont table_list ">
			<table class="table table-border table-hover">
				<thead>
					<tr class="text-c">
						<th style="width: 15%">角色</th>
						<th style="width: 15%">操作人员</th>
						<th style="width: 15%">机构</th>
						<th style="width: 15%">发表意见时间</th>
						<th style="width: 18%">意见说明</th>
						<th style="width: 15%">审核状态</th>
					</tr>
				</thead>
				<tbody class="" id="claimTextItem">
					<c:forEach var="claimTextVo" items="${claimTextVos}" varStatus="status">
						<input type="hidden" name="claimTextVo[${status.index}].id" value="${claimTextVo.id}">
						<tr class="text-c" id="claimTextTr">
							<td><c:choose>
									<c:when test="${claimTextVo.nodeCode eq 'Chk'}">
									查勘员
								</c:when>
									<c:when test="${claimTextVo.nodeCode eq 'ChkRe'}">
									复勘员
								</c:when>
									<c:when test="${claimTextVo.nodeCode eq 'ChkBig_LV1'}">
									大案一级审核人员
								</c:when>
									<c:when test="${claimTextVo.nodeCode eq 'ChkBig_LV2'}">
									大案二级审核人员
								</c:when>
									<c:when test="${claimTextVo.nodeCode eq 'ChkBig_LV3'}">
									大案三级审核人员
								</c:when>
									<c:when test="${claimTextVo.nodeCode eq 'ChkBig_LV4'}">
									大案四级审核人员
								</c:when>
									<c:when test="${claimTextVo.nodeCode eq 'ChkBig_LV5'}">
									大案五级审核人员
								</c:when>
									<c:otherwise>
									大案审核人员
								</c:otherwise>
								</c:choose></td>
							<td>${claimTextVo.operatorName}</td>
							<td><app:codetrans codeType="ComCode"
									codeCode="${claimTextVo.comCode}" /></td>
							<td><app:date date='${claimTextVo.inputTime}' /></td>
							<td>${claimTextVo.description}</td>
							<td><app:codetrans codeType="AuditStatus"
									codeCode="${claimTextVo.status}" /></td>
						</tr>
					</c:forEach>
					
					<c:forEach var="checkTaskVo" items="${checkTaskVos}" varStatus="status">
					
					<c:if test="${!empty checkTaskVo.claimText }">
					<tr class="text-c">
					<td>查勘员</td>
					<td><app:codetrans codeType="UserCode" codeCode="${checkTaskVo.createUser}" /></td>
					<td><app:codetrans codeType="ComCode" codeCode="${checkTaskVo.comCode}" /></td>
					<td><app:date date='${checkTaskVo.createTime}'/></td>
					<td>${checkTaskVo.claimText}</td>
					<td>大案预报</td>
					</tr>
					</c:if>
					
					</c:forEach>
					<c:forEach var="lossCarMainVo" items="${lossCarMainVos}" varStatus="status">
					
					<c:if test="${!empty lossCarMainVo.contexts }">
					<tr class="text-c">
					<td>定损员</td>
					<td><app:codetrans codeType="UserCode" codeCode="${lossCarMainVo.createUser}" /></td>
					<td><app:codetrans codeType="ComCode" codeCode="${lossCarMainVo.comCode}"/></td>
					<td><app:date date='${lossCarMainVo.createTime}'/></td>
					<td>${lossCarMainVo.contexts}</td>
					<td>大案预报</td>
					</tr>
					</c:if>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<!-- 意见列表 结束 -->
	
	
	<!-- 意见列表 开始 -->
	<div class="table_wrap">
		<div class="table_title f14">意见列表(人伤)</div>
		<div class="table_cont table_list ">
			<table class="table table-border table-hover">
				<thead>
					<tr class="text-c">
						<th style="width: 15%">角色</th>
						<th style="width: 15%">操作人员</th>
						<th style="width: 15%">机构</th>
						<th style="width: 15%">发表意见时间</th>
						<th style="width: 18%">意见说明</th>
						<th style="width: 15%">审核状态</th>
					</tr>
				</thead>
				<tbody class="" id="claimTextItem">
					<c:forEach var="claimTextVo" items="${claimTextVoPLBigs}"
						varStatus="status">
						<input type="hidden" name="claimTextVo[${status.index}].id"
							value="${claimTextVo.id}">
						<tr class="text-c" id="claimTextTr">
							<td><c:choose>
									<c:when test="${claimTextVo.nodeCode eq 'Chk'}">
									查勘员
								</c:when>
									<c:when test="${claimTextVo.nodeCode eq 'ChkRe'}">
									复勘员
								</c:when>
									<c:when test="${claimTextVo.nodeCode eq 'ChkBig_LV1'}">
									大案一级审核人员
								</c:when>
									<c:when test="${claimTextVo.nodeCode eq 'ChkBig_LV2'}">
									大案二级审核人员
								</c:when>
									<c:when test="${claimTextVo.nodeCode eq 'ChkBig_LV3'}">
									大案三级审核人员
								</c:when>
									<c:when test="${claimTextVo.nodeCode eq 'ChkBig_LV4'}">
									大案四级审核人员
								</c:when>
									<c:when test="${claimTextVo.nodeCode eq 'ChkBig_LV5'}">
									大案五级审核人员
								</c:when>
									<c:otherwise>
									大案审核人员
								</c:otherwise>
								</c:choose></td>
							<td>${claimTextVo.operatorName}</td>
							<td><app:codetrans codeType="ComCode"
									codeCode="${claimTextVo.comCode}" /></td>
							<td><app:date date='${claimTextVo.inputTime}' /></td>
							<td>${claimTextVo.description}</td>
							<td><app:codetrans codeType="AuditStatus"
									codeCode="${claimTextVo.status}" /></td>
						</tr>
					</c:forEach>
					<c:forEach var="persTraceMainVo" items="${persTraceMainVos}" varStatus="status">
					<c:if test="${!empty persTraceMainVo.payOpinions}">
					<tr class="text-c">
				    <td>人伤跟踪员</td>
				    <td><app:codetrans codeType="UserCode" codeCode="${persTraceMainVo.createUser}" /></td>
				    <td><app:codetrans codeType="ComCode" codeCode="${persTraceMainVo.comCode}" /></td>
				    <td><app:date date='${persTraceMainVo.createTime}'/></td>
				    <td>${persTraceMainVo.payOpinions}</td>
				    <td>大案预报</td>
				    </tr>
				    </c:if>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	<!-- 意见列表 结束 -->
	
</body>
</html>