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
		<input hidden="hidden" name="registNo" value="${prpLCMainVo.registNo }">
		<input hidden="hidden" name="taskId" value="${taskId}">
		<input hidden="hidden" name="flags" value="${prpLcancelTraceVo.flags}">
				<!-- 输入信息    -->
		<%@include file="claimCancelApplyEdit_Inputs.jsp" %>
			</div>
		</form>

		<div class="text-c mt-10" id="submitAfters">
			<input class="btn btn-primary ml-5" id="" onclick="zhanCun()" type="button" value="暂存">
			<input class="btn btn-primary ml-5" id="save" onclick="save()" type="button" value="提交"> 
			
		</div>
	</div>

	<script type="text/javascript">
	var layerIndex = 0;
		function save(){
			var description = $("#description").val(); 
			if(isBlank($.trim(description))){
				layer.msg("请输入注销拒赔回复原因！");
				return ;
			}
			layer.load(0, {shade : [0.8, '#393D49']});
				$.ajax({
					type: "POST",
					url : "/claimcar/claimRecover/claimCancelSave.do", // 后台校验
					data : $("#defossform").serialize(),
					success : function(jsonData) {// 回调方法，可单独定义
						layer.msg("提交成功");
						layer.close(layerIndex);
						$("#submitAfters").addClass("hidden");  
						$("textarea").attr("disabled","disabled");
						layer.closeAll('loading');
						window.location.reload();
					}
				});
			
		}
		
	
		
		function zhanCun(){
			var description = $("#description").val(); 
			if(isBlank($.trim(description))){
				layer.msg("请输入注销拒赔回复原因！");
				return ;
			}
			layer.load(0, {shade : [0.8, '#393D49']});
			$.ajax({
				type: "POST",
				url : "/claimcar/claimRecover/claimInitZhanCun.do", // 后台校验
				data : $("#defossform").serialize(),
				success : function(jsonData) {// 回调方法，可单独定义
					layer.alert("暂存成功",{closeBtn:0},function(){
						window.location.reload();
						layer.closeAll('loading');
					});
				}
			});
		
		
	}
	</script>
</body>

</html>