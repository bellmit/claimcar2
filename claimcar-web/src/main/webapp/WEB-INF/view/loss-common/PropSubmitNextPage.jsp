<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<body>
	<div>
		<div class="table_wrap">
			<div class="table_title f14">提交下一节点</div>
			<div class="table_cont ">
				<form id="nodeform"
					action="/claimcar/proploss/submitNextNode.do" role="form"
					method="post" name="fm">
					<table class="table table-striped table-border table-hover">
						<thead>
							<tr class="text-c">
								<th>当前环节</th>
								<th>提交任务路径</th>
								<th>业务号</th>
							</tr>
						</thead>
						<tbody>
							<tr class="text-c">
								<td>${nextVo.currentName }</td>
								<td>
								<app:codeSelect codeType="nextNode" type="select" name="nextVo.nextNode" clazz="must"  dataSource="${nextNodeMap }" /> 
						 		</td>
								<td>${nextVo.registNo} 
									<input type="hidden"name="nextVo.taskInKey" value="${nextVo.taskInKey}" /> 
									<input type="hidden" name=".registNo" value="${nextVo.registNo}" />
									<input type="hidden" name="nextVo.currentNode" value="${nextVo.currentNode}" /> 
									<input type="hidden" name="nextVo.flowTaskId" value="${nextVo.flowTaskId}" /> 
									<input type="hidden" name="nextVo.taskInUser" value="${nextVo.taskInUser}" />
									<input type="hidden" name="nextVo.comCode" value="${nextVo.comCode}" />
									<input type="hidden" name="nextVo.submitLevel" value="${nextVo.submitLevel }"/>
									<input type="hidden" name="nextVo.maxLevel" value="${nextVo.maxLevel }"/>
									<input type="hidden" name="nextVo.assignCom" value="${nextVo.assignCom}" />
									<input type="hidden" name="nextVo.assignUser" value="" />
									<input type="hidden" name="nextVo.auditStatus" value="${nextVo.auditStatus}"/>
									<input type="hidden" name="nextVo.autoLossFlag" value="${nextVo.autoLossFlag}"/>
									<input type="hidden" name="nextVo.notModPrice" value="${nextVo.notModPrice}" />
								</td>
							</tr>
						</tbody>
					</table>
					<div class="text-c">
						<br /> <input class="btn btn-primary" id="submit" type="submit"
							value="提交"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
							class="btn btn-primary" id="return" type="button" value="返回">
					</div>
				</form>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(function() {
			$("#submit").click(function() {
				$("#nodeform").submit();
				layer.load(0, {shade : [ 0.8, '#393D49' ]});//防止重复提交 
			});

			$("#return").click(function() {
				parent.location.reload();
				var index = parent.layer.getFrameIndex(window.name);
				parent.layer.close(index);
			});
		});
	</script>
</body>
</html>