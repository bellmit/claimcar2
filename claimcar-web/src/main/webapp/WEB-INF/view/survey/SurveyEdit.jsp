<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>调查任务发起</title>
</head>
<body>
	<div class="page_wrap">
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form">
						<input hidden="hidden" name="flowTaskId" id="flowTaskId" value="${flowTaskId}"/>
						<input hidden="hidden" name="surveyVo.flowId" id="flowId" value="${surveyVo.flowId}"/>
						<input hidden="hidden" name="surveyVo.registNo" id="registNo" value="${surveyVo.registNo}"/>
						<input hidden="hidden" name="surveyVo.nodeCode" id="nodeCode" value="${surveyVo.nodeCode}"/>
						<input hidden="hidden" name="surveyVo.createUser" id="createUser" value="${surveyVo.createUser}"/>
						<input hidden="hidden" name="surveyVo.fraudScoreId" id="fraudScoreId" value="${prpLFraudScoreVo.fraudScoreId}"/>
						<div class="row  cl">
							<label class="form_label col-1">报案号</label>
							<div class="formControls col-3">
								<label>${surveyVo.registNo }</label>
					 	 	</div>
					 	 	<label class="form_label col-1">发起环节</label>
					 	 	<div class="formControls col-3">
								<label><app:codetrans codeType="FlowNode" codeCode="${surveyVo.nodeCode }"/></label>
					 	 	</div>
					 	 	<label class="form_label col-1">发起人</label>
					 	 	<div class="formControls col-3">
								<label><app:codetrans codeType="UserCode" codeCode="${surveyVo.createUser }"/></label>
							</div>
					 	</div>
					 	<div class="row cl">
							<label class="form_label col-1">是否包含人伤</label>
							<div class="form_input col-3">
								<span class="radio-box"> <app:codeSelect codeType="IsValid" 
								type="radio" value="${prpLFraudScoreVo.isInjured}"
										name="surveyVo.isInjuryCases" />
								</span><font class="must">*</font>
							</div>
					 	 	<label class="form_label col-1">是否小额</label>
					 	 	<div class="form_input col-3">
								<span class="radio-box"> <app:codeSelect codeType="IsValid" 
								type="radio" value="${prpLFraudScoreVo.isSmallAmount}"
										name="surveyVo.isMinorCases" />
								</span><font class="must">*</font>
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">发起原因</label>
					 	 		<textarea class="textarea h200" name="surveyVo.reasonDesc" id="reasonDesc" style="width:900px"></textarea>
					 	</div>
						<div class="table_wrap">
						<div class="table_title">历史记录</div>
						<div class="table_cont ">
							<table class="table table-border table-hover">
								<thead>
									<tr class="text-c">
										<th>发起时间</th>
										<th>发起人</th>
										<th>发起环节</th>
										<th>是否自动触发</th>
										<th>是否含人伤</th>
										<th>是否小额</th>
										<th>任务状态</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="prpLSurveyVo" items="${prpLSurveyVoList}">
										<tr class="text-c">
											<td><fmt:formatDate value="${prpLSurveyVo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
											<td><app:codetrans codeType="UserCode" codeCode="${prpLSurveyVo.createUser}"/></td>
											<td><app:codetrans codeType="FlowNode" codeCode="${prpLSurveyVo.nodeCode}"/></td>
											<td><app:codetrans codeType="YN10" codeCode="${prpLSurveyVo.isAutoTrigger}"/></td>
											<td><app:codetrans codeType="YN10" codeCode="${prpLSurveyVo.isInjuryCases}"/></td>
											<td><app:codetrans codeType="YN10" codeCode="${prpLSurveyVo.isMinorCases}"/></td>
											<td><app:codetrans codeType="HandlerStatus" codeCode="${prpLSurveyVo.handlerStatus}"/></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
					</div>
						<div class="btn-footer clearfix text-c">
							<a class="btn btn-primary "  
								id="submit">发起调查</a> &nbsp;&nbsp;
						 <a class="btn btn-danger cl" id="close">取消</a>
						</div>
					</form>
				</div>
			</div>

		</div>
	</div>
	<script type="text/javascript">
		$("#submit").click(function() {
			var a = $("#form").serialize();
			var isInjuryCases = $("input[name='surveyVo.isInjuryCases']:checked");
			var isMinorCases = $("input[name='surveyVo.isMinorCases']:checked");
			var reasonDescs = $("#reasonDesc");
			if(isEmpty(isInjuryCases)){
				layer.alert("请选择是否包含人伤！");
				isInjuryCases.focus();
				return;
			}
			if(isEmpty(isMinorCases)){
				layer.alert("请选择是否小额案件！");
				isMinorCases.focus();
				return;
			}
			if(isEmpty(reasonDescs)){
				layer.alert("请填写发起原因详情！");
				reasonDescs.focus();
				return;
			}
		 	$.ajax({
	            type: "POST",
	            url: "/claimcar/survey/save.do",
	            data:a,
	            async: false,
	            success: function(data){
	            	if(data.status==200){
	            		layer.confirm("调查发起成功！", {
	        				btn: ['确定'] //按钮
	        			}, function(index){
	        				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			            	parent.layer.close(index); //再执行关闭 
	        			});
	            	}else{
	            		layer.alert(data.data);
	            	}	            	
	            },
		 		error: function (XMLHttpRequest, textStatus, errorThrown) {
		 			if(XMLHttpRequest.responseText!=""){
						layer.alert(XMLHttpRequest.responseText);
					}else{
						layer.alert("操作失败，请刷新页面！");
					}
					layer.closeAll('loading');
		 		}
	        });
		 });
		
		$("#close").click(function() {
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index); //再执行关闭 
		});
	</script>
</body>
</html>
