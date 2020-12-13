<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
	<body>
		<div>
			<c:if test="${reportFinish=='1'}">
				<div class="table_wrap">
            	<div class="table_cont">当前业务号为：${registNo}&nbsp;<b>正在提交！请勿重复操作！</b></div>
	            </div>
			</c:if>
			<c:if test="${reportFinish!='1'}">
				<div class="table_wrap">
            	<div class="table_cont">当前业务号为：${registNo}&nbsp;提交成功！</div>
	            </div>
	            <div class="table_wrap">
	            	<div class="table_cont">
	            		可快速处理：调度：${registNo}
	            		<input type="hidden" id="flowTaskId" value="${flowTaskId}" />
	            		<input type="hidden" id="registNo" value="${registNo}" />
	            		<input onclick="submit()" class="btn btn-primary" type="button" value="调度处理" id="submit" />
	            	</div>
				</div>
			</c:if>
		    <!-- 隐藏域 -->
		    <input type="hidden" id="flag" value="${flag}"/>

		</div>
		<script type="text/javascript">
		$(function(){
			var flag = $("#flag").val();
			if(!flag){
				$("#submit").hide();
			}
		});
			function submit() {
				$("#submit").attr("disabled", true);
				$("#submit").removeClass("btn-primary");
				$("#submit").addClass("btn-disabled");
				var goUrl = "/claimcar/schedule/preScheduleEdit.do?flowTaskId="+$("#flowTaskId").val()+"&registNo="+$("#registNo").val();
				parent.openTaskEditWin("调度处理",goUrl);
			}
		</script>
	</body>
</html>