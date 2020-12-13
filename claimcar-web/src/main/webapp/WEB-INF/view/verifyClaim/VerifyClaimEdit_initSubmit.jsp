<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>核赔任务处理</title>
</head>
<body>
	<div class="table_wrap">
		<div class="table_cont table_list">
			<input type="hidden" name="action" value="${action}">
			<input type="hidden" name="submit_taskId" value="${taskId}">
			<input type="hidden" name="submit_uwNotionId" value="${uwNotionMainId}">
			<table class="table table-border table-hover">
				<thead>
					<tr class="text-c">
						<th style="width: 50%">当前任务环节</th>
						<th style="width: 50%">提交路径选择</th>
						<!-- <th style="width: 30%">人员代码</th> -->
					</tr>
				</thead>
				<tbody>
					<tr class="text-c">
						<td>
							<input type="hidden" name="tempComperVal" value="99" />
							<input type="hidden" name="submitNextVo.comCode" value="${nextVo.comCode}" />
							<input type="hidden" name="submitNextVo.currentNode" value="${nextVo.currentNode }">
							<input type="hidden" name="submitNextVo.currentName" value="${nextVo.currentName }">
							<input type="hidden" name="submitNextVo.auditStatus" value="${nextVo.auditStatus }">
							${nextVo.currentName}
						</td>
						<td>
							<app:codeSelect codeType="nextNode" type="select" name="nextNode"
							clazz="must" dataSource ="${dataSourceList}" onclick="changeNextNode(this)" style="width: 60%"/>
							<input type="hidden" name="submitNextVo.nextNode" value="${dataSourceList[0].codeCode }">
						</td>
						<%-- <td class="text-r"><select id="" class="select-box"
							name="submitNextVo.assignUser" style="width: 80%">
								<option value="${nextVo.assignUser}">${nextVo.assignUser}</option>
								<option>张三</option>
						</select></td> --%>
					</tr>
				</tbody>
			</table>
			<br/><br/>
			<input type="button" class="btn btn-primary" name="submitTaskName"
				style="margin-left: 40%;" onclick="submitTask()"  value="提交任务">
			<input type="button" class="btn btn-primary" onclick="closeLocalLayer()"
				value="取消"><br/><br/>
		</div>
	</div>
	<script type="text/javascript" src="/claimcar/js/verifyclaim/verifyclaim.js"></script>
	<script type="text/javascript">
		function changeNextNode(element) {
			var nextNode = $(element).val();
			$("input[name='submitNextVo.nextNode']").val(nextNode);
		}
	</script>
</body>
</html>