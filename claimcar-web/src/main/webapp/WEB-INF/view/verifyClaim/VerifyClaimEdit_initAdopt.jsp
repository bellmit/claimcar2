<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>核赔任务处理</title>
</head>
<body> 
       
        <input type="hidden" name="cisderoAmout" value="${cisderoAmout}">
		<input type="hidden" name="action" value="${action}">
        <input type="hidden" name="submit_taskId" value="${taskId}">
        <input type="hidden" name="submit_uwNotionId" value="${uwNotionMainId}">
        <input type="hidden" name="currentLevel" value="${currentLevel}">
        <input type="hidden" name="backLevel" value="${backLevel}">
	<div class="table_wrap">
		<div class="table_title f14">提交核赔任务</div>
		<div class="table_cont table_list">
			<table class="table table-border table-hover">
				<thead>
					<tr class="text-c">
						<th style="width: 40%">当前任务环节</th>
						<th></th>
						<th style="width: 30%">提交路径选择</th>
					</tr>
				</thead>
				<tbody>
					<tr class="text-c">
						<input type="hidden" name="tempComperVal" value="99" />
						<input type="hidden" name="submitNextVo.comCode" value="${nextVo.comCode}" />
						<input type="hidden" name="submitNextVo.currentNode" value="${nextVo.currentNode }">
						<input type="hidden" name="submitNextVo.currentName" value="${nextVo.currentName }">
						<input type="hidden" name="submitNextVo.auditStatus" value="${nextVo.auditStatus }">
						<td>${currentName}</td>
						<td></td>
						<td class="text-c">
							<c:choose>
								<c:when test="${taskInNode eq 'CompeCI' || taskInNode eq 'CompeBI'
								 || taskInNode eq 'CompeWfBI' || taskInNode eq 'CompeWfCI'}">
									自动结案
								<input type="hidden" name="submitNextVo.nextNode" value="EndCas">
								</c:when>
								<c:otherwise>
									核赔通过
								<input type="hidden" name="submitNextVo.nextNode" value="END">
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<input type="hidden" id="recFlag" value="${recFlag}">
	<c:if test="${recFlag eq '1'}">
	<div class="table_wrap">
		<div class="table_title f14">追偿任务</div>
		<div class="table_cont table_list">
			<table class="table table-border table-hover">
				<thead>
					<tr class="text-c">
						<th style="width: 40%">当前任务环节</th>
						<th style="width: 30%">提交路径</th>
					</tr>
				</thead>
				<tbody>
					<tr class="text-c">
						<td>${currentName}</td>
						<td>追偿</td>
					</tr>
				</tbody>
			</table>
			
		</div>
	</div>
	</c:if>
	<br /><br />
	<%--<input type="button" class="btn btn-primary" style="margin-left: 40%;"--%>
		<%--onclick="submitTaskBefore()" name="submitTaskName" value="提交任务">--%>
		<input type="button" class="btn btn-disabled" style="margin-left: 40%;"
			    name="submitTaskName" value="提交任务">
	<input type="button" class="btn btn-primary" onclick="closeLocalLayer()" value="取消">
	<br />
	<script type="text/javascript" src="/claimcar/js/verifyclaim/verifyclaim.js"></script>
	<script type="text/javascript">
		$(function(){
			var currentLevel = $("input[name='currentLevel']").val();
			var backLevel = $("input[name='backLevel']").val();
			var cisderoAmout=$("input[name='cisderoAmout']").val();

			var flag = true;
			if(Number(currentLevel)<Number(backLevel)){
				// setButton();
				flag = false;
				layer.msg("该案件需要核赔"+backLevel+"级才能核赔通过，请提交上级！",{time:1000*5});
			}else if(Number(backLevel)==0){
				// setButton();
				flag = false;
				layer.msg("该案件调规则错误,未获得返回信息",{time:1000*5});
			}
			
			if((Number(cisderoAmout))>0 && Number(currentLevel)<9){
				// setButton();
				flag = false;
				layer.msg("案件存在减损金额，请提交总公司审核！",{time:1000*5});
			}
			if (flag) {
				setButton();
			}
			function setButton(){
				// $("input[name='submitTaskName']").removeAttr("class").removeAttr("onclick").attr("class","btn btn-disabled");
				$("input[name='submitTaskName']").removeAttr("class").attr("onclick", "submitTaskBefore()").attr("class","btn btn-primary");
			}
		});
	</script>
</body>
</html>