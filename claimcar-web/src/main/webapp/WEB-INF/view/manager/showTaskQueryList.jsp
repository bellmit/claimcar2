<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>查勘任务处理查询</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form"
						method="post">
						<input type="hidden"  name="prpLHandReasonVo.taskId"  value="${prpLHandReasonVo.taskId }" >
						<input type="hidden"  name="prpLHandReasonVo.flowId"  value="${prpLHandReasonVo.flowId }" >
						<input type="hidden"  name="prpLHandReasonVo.registNo"  value="${prpLHandReasonVo.registNo }" >
						<div class="row cl">
						<b>未付环节原因</b>
						</div>
						<span > 
								 <app:codeSelect codeType="UnHandReasonA" type="checkbox" id="reasonNode" value="${prpLHandReasonVo.reasonNode}"
										name="prpLHandReasonVo.reasonNode" lableType="code-name" upperCode="${nodeCode }" style="width:208px;white-space:nowrap;" />
								</span>
							<hr>
						<div class="row cl">
						<b>未付责任原因</b>
						</div>
						<span > 
								 <app:codeSelect codeType="UnHandReasonB" type="checkbox" id="reasonDuty" value="${prpLHandReasonVo.reasonDuty}"
										name="prpLHandReasonVo.reasonDuty" lableType="code-name" style="width:195px;white-space:nowrap;" />
								</span>
						<div class="row cl">
							<div class="form_input col-6"></div>
						</div>
						<!-- 结束 -->
						<div class="btn-footer clearfix text-c">
							<a class="btn btn-primary   "  
								id="submit">保存</a> &nbsp;&nbsp;
						 <a class="btn btn-danger   cl" id="close">关闭</a>
						</div>
						
					</form>
				</div>
			</div>

		</div>
	</div>
	<script type="text/javascript">
		$("#submit").click(function() {
			var d = $("#form").serialize();
			 $.ajax({
	             type: "POST",
	             url: "/claimcar/common/handReasonEdit.do",
	             data:d,
	             success: function(data){
	                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	            			parent.layer.close(index); //再执行关闭 
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
