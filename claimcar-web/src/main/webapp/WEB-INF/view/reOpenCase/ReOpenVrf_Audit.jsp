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
					action="/claimcar/reOpen/submitNextNode.do" role="form"
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
								<td>${currentName }</td>
								<td><app:codeSelect codeType="nextNode" type="select"
										name="nextNodeMap" clazz="must"
										dataSource="${nextNodeMap }" /> </td>
								<td>${registNo}
									<input type="hidden" name="saveType" value="${saveType }"/>
									<input type="hidden" name="reOpenId" value="${reOpenId }"/>
									<input type="hidden" name="flowTaskId" value="${flowTaskId }"/>
									<input type="hidden" name="nextNode" value="${nextNode }"/>
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