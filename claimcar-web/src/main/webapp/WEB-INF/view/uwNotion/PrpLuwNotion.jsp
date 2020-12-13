<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>****</title>
</head>
<body>
	<div class="table_cont ">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th>序号</th>
					<th>审核人员</th>
					<th>审批意见</th>
					<th>意见内容</th>
					<th>审核时间</th>
				</tr>
			</thead>
			<tbody class="" id="claimTextItem">
				<c:forEach var="vo" items="${prpLuwNotionVoList}">
					<tr class="text-c" id="claimTextTr">
						<td>${vo.id}</td>
						<td><app:codetrans codeType="UserCode" codeCode="${vo.auditor}"/></td>
						<td><app:codetrans codeCode="${vo.verifyText}" codeType="VerifyText" /></td>
						<td>${vo.handleText}</td>
						<td><fmt:formatDate value='${vo.pubTime}'
								pattern='yyyy-MM-dd HH:mm:ss' /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="text-c mt-10"><br/>
		<input type="button" class="btn btn-primary" onclick="closeLayer()"
			value="返回" /><br/>
	</div>
	<script type="text/javascript">
		function closeLayer() {
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index);
		}
	</script>
</body>
</html>