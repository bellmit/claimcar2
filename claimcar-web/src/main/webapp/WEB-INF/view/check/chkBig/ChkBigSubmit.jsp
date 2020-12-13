<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>大案审核提交</title>
</head>
<body>
	<div class="table_wrap">
	<input type="hidden" id="checkMainId" value="${checkId}">
	<input type="hidden" id="lossFeeSum" value="${lossFees}">
	<input type="hidden" id=checkId value="${checkId}">
	<input type="hidden" id=flowTaskId value="${flowTaskId}">
	<input type="hidden" id=codeName value="${codeName}">
	<input type="hidden" value="${currentNode}">
	<input type="hidden" id=nextCodeName value="${nextCodeName}">
	<input type="hidden" name="sendMail" >
	<div class="table_cont table_list">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th style="width: 30%">当前节点</th>
					<th style="width: 30%">提交到</th>
					<th style="width: 30%">处理人</th>
				</tr>
			</thead>
				<tbody id="propLossTbody">
					<tr class="text-c">
						<td><h5>${codeName}</h5></td>
						<td>
							<c:choose>
								<c:when test="${nextCodeName eq '完成'}">
									<h5>大案审核通过</h5>
								</c:when>
								<c:otherwise>
									<h5>${nextCodeName}</h5>
								</c:otherwise>
							</c:choose>
						</td>
						<td class="text-r">
							<c:choose>
								<c:when test="${codeName eq '复勘'}">
									<app:codeSelect type="select" codeType="UserCode" dataSource="${taskUserMap}"
									 	name="chkReUser" clazz="must" value="" style="width: 80%" lableType="code-name"/>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</tbody>
			</table>
		<br/><br/>
		<%--<c:if test="${nodeCode eq 'ChkBig' }">
		<input type="radio"  id="sendMail" value="1" onclick="clickSendEMail(this)"/>邮件上报
		</c:if>--%>
		<input type="button" class="btn btn-primary text-c" style="margin-left: 40%;" 
			id="chkBigSubmit" onclick="chkBigSubmit('${checkId}','${flowTaskId}','${currentNode}')" value="提 &nbsp;交">
		<input type="button" class="btn btn-disabled btn-kk"
			id="chkBigCancel" onclick="chkBigCancel(this)" value="返 &nbsp;回">
		<br/><br/>
	</div>
</div>
<script type="text/javascript" src="/claimcar/js/checkEdit/check.js"></script>
<script type="text/javascript" src="/claimcar/js/checkEdit/chkre.js"></script>
</body>
</html>