<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>立案注销/拒赔处理</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<!-- 按钮组 -->
		<div class="top_btn">
			<div class="top_btn">
				<a class="btn btn-primary" onclick="viewEndorseInfo('${prpLClaimVo.registNo }')">保单批改记录</a>
		    <a class="btn  btn-primary" onclick="openTaskEditWin('报案详细信息','/claimcar/regist/edit.do?registNo=${prpLCMainVo.registNo}')">报案详细信息</a>
		    <a class="btn  btn-primary" onclick="checkSeeMessage('${prpLCMainVo.registNo}')">查勘详细信息</a>
		    <a class="btn  btn-primary" onclick="showFlow('${prpLClaimVo.flowId}')">流程查询</a>

		    </div><br/>
		</div>
		<p>

	
			<div class="table_cont">
				<!-- 基本信息    -->
				<%@include file="claimCancelApplyEdit_Infos.jsp" %>
					<form  id="defossform" role="form" method="post"  name="fm" >
		<input hidden="hidden" name="claimNo" value="${ prpLClaimVo.claimNo}" id="claimNo">
		<input hidden="hidden" name="riskCode" value="${ prpLClaimVo.riskCode}" id="riskCode">
		<input hidden="hidden" name="flowTask" value="${ prpLClaimVo.flowId}">
		<input hidden="hidden" name="taskId" value="${ taskId}">
		<input hidden="hidden" name="id" value="${ id}">
		<input hidden="hidden" name="registNo" value="${prpLCMainVo.registNo }">
		<input hidden="hidden" name="subNodeCode" value="${subNodeCode}">
		
				<!-- 输入信息    -->
				<%@include file="ClaimCancelApplyCheck_Inputs.jsp" %>
			</div>
		</form>

		<div class="text-c mt-10">
			<input type="hidden"  id="handlerStatusFlags" value="${handlerStatus }" /> 
			<c:if test="${handlerStatus!=3 }">
			<div id="yingchan">
			<input class="btn btn-primary ml-5" id="" onclick="zhanCun()" type="button" value="暂存">
			<input class="btn btn-primary ml-5" id="save" onclick="save('提交上级')" type="button" value="提交上级"> 
			<input class="btn btn-primary ml-5" id="back" onclick="back('1')" type="button" value="退回下一级"> 
			<input class="btn btn-primary ml-5"  onclick="back('0')" type="button" value="退回申请"> 
			
			</div>
		</c:if>
		</div>
	</div>

	<script type="text/javascript">
	$(function(){
		var handlerStatusFlags = $("#handlerStatusFlags").val();
		if(handlerStatusFlags==3){
			$("textarea").attr("disabled","disabled");
		}
	});
	var layerIndex = 0;

		function save(a){
			var cancelCode = $("[name='cancelCode']").val();
			if(isBlank(cancelCode)){
				layer.msg("请输入立案注销/拒赔恢复原因！");
				return ;
			}
			var description = $("#description").val(); 
			if(isBlank(description)){
				layer.msg("请输入审核原因！");
				return ;
			}
			var params = $("#defossform").serialize();// http request parameters. 
			//params = decodeURIComponent(params,true);
			params = params+"&opinionName="+a;
			layer.load(0, {shade : [0.8, '#393D49']});
				$.ajax({
					type: "POST",
					url : "/claimcar/claimRecover/claimCancelEnd.do", // 后台校验
					data :params,
					success : function(jsonData) {// 回调方法，可单独定义
						layer.msg("提交成功");
						layer.close(layerIndex);
						$("#yingchan").hide();
						layer.closeAll('loading');
						window.location.reload();
					}
				});
			
			
		}
		
		function zhanCun(){
			var cancelCode = $("[name='cancelCode']").val();
			if(isBlank(cancelCode)){
				layer.msg("请输入立案注销/拒赔恢复原因！");
				return ;
			}
			var description = $("#description").val(); 
			if(isBlank(description)){
				layer.msg("请输入审核/退回意见！");
				return ;
			}
			$.ajax({
				type: "POST",
				url : "/claimcar/claimRecover/claimCancelZhanCun.do", // 后台校验
				data : $("#defossform").serialize(),
				success : function(jsonData) {// 回调方法，可单独定义
					layer.msg("暂存成功");
					layer.close(layerIndex);
					window.location.reload();
				}
			});
		
		
	}
	
		function back(a){
			var cancelCode = $("[name='cancelCode']").val();
			if(isBlank(cancelCode)){
				layer.msg("请输入立案注销/拒赔恢复原因！");
				return ;
			}
			var description = $("#description").val(); 
			if(isBlank(description)){
				layer.msg("请输入退回意见！");
				return ;
			}
			var params = $("#defossform").serialize();
			params = params+"&opinionName="+a;
			layer.load(0, {shade : [0.8, '#393D49']});
			$.ajax({
				type: "POST",
				url : "/claimcar/claimRecover/CancelBack.do", // 后台校验
				data : params,
				success : function(jsonData) {// 回调方法，可单独定义
					layer.msg("退回成功");
					layer.close(layerIndex);
					$("#yingchan").hide();
					layer.closeAll('loading');
					window.location.reload();
				}
			});
		}
		
	</script>
</body>

</html>