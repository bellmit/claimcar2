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
				<a class="btn btn-primary" onclick="viewEndorseInfo('${wfTaskVo.registNo}')">保单批改记录</a>
		    <a class="btn  btn-primary" onclick="openTaskEditWin('报案详细信息','/claimcar/regist/edit.do?flowId=${wfTaskVo.flowId}&flowTaskId=${wfTaskVo.taskId}&taskInKey=${wfTaskVo.taskInKey}&handlerIdKey=${wfTaskVo.handlerIdKey}&registNo=${wfTaskVo.registNo}')">报案详细信息</a>
            <a class="btn  btn-primary" onclick="checkSeeMessage('${wfTaskVo.registNo}')">查勘详细信息</a>
            <a class="btn  btn-primary" onclick="showFlow('${wfTaskVo.flowId}')">流程查询</a>
            

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
			<input class="btn btn-primary ml-5" id="zhanCun" onclick="zhanCun()" type="button" value="暂存">
			<input class="btn btn-primary ml-5" id="save" onclick="save('提交上级')" type="button" value="提交上级"> 
			<input class="btn btn-disabled ml-5" id="dsave"  type="button" value="提交上级"> 
			<input class="btn btn-primary ml-5" id="back" onclick="back('1')" type="button" value="退回下一级"> 
			<input class="btn btn-disabled ml-5" id="dback"  type="button" value="退回下一级"> 
			<input class="btn btn-primary ml-5" id="backs" onclick="back('0')" type="button" value="退回申请"> 
			<input class="btn btn-disabled ml-5" id="dbacks" type="button" value="退回申请"> 
			<input class="btn btn-primary ml-5" id="bcompel" onclick="bcompel()" type="button" value="强制取消立案申请">
			<input class="btn btn-disabled ml-5" id="dcompel" type="button" value="强制取消立案申请">
		</c:if>
		</div>
	</div>

	<script type="text/javascript">
	$(function(){
			$("#applyReason").attr("disabled","disabled"); 
			$("#dealReasoon").attr("disabled","disabled"); 
			$("#remarks").attr("disabled","disabled"); 
			/* $("#swindleSum").attr("disabled","disabled"); 
			$("#swindleType").attr("disabled","disabled"); 
			$("#swindleReason").attr("disabled","disabled");  */
			var handlerStatusFlags = $("#handlerStatusFlags").val();
			if(handlerStatusFlags==3){
				$("input").attr("disabled","disabled");
				$("textarea").attr("disabled","disabled");
			}
			 var vals=$('input:radio[name="reasonCode"]:checked').val();
			  if(vals=="a"){//同意
				  $("#back").hide();
				  $("#backs").hide();
				  $("#dback").show();
				  $("#dbacks").show();
				  $("#save").show();
				  $("#dsave").hide();
				  $("#bcompel").hide();
				  $("#dcompel").show();
			  }else{
				  $("#save").hide();
				  $("#dsave").show();
				  $("#back").show();
				  $("#backs").show();
				  $("#dback").hide();
				  $("#dbacks").hide();
				  $("#bcompel").show();
				  $("#dcompel").hide();
			  }
	});
	var layerIndex = 0;

		function save(a){
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
					url : "/claimcar/claim/claimCancelEnd.do", // 后台校验
					data :params,
					success : function(jsonData) {// 回调方法，可单独定义
					var result = eval(jsonData);
					if(result.status=='200'){
						layer.msg("提交成功");
						//layer.close(layerIndex);
						layer.closeAll('loading');
						//window.location.reload();
						$("#zhanCun").hide();	
						$("#save").hide();
						$("#back").hide();
						$("#backs").hide();
						$("#dsave").hide();
						$("#dback").hide();
						$("#dbacks").hide();
						$("#bcompel").hide();
						$("#dcompel").hide();
						$("#reasonCodes").hide();
						$("#reasonCodess").show();
						window.location.reload();
					}else{
						layer.alert(result.statusText);
						layer.closeAll('loading');
					}
						
					}
				});
			
			
		}
		
		function zhanCun(){
			$.ajax({
				type: "POST",
				url : "/claimcar/claim/claimCancelZhanCun.do", // 后台校验
				data : $("#defossform").serialize(),
				success : function(jsonData) {// 回调方法，可单独定义
					layer.msg("暂存成功");
					layer.close(layerIndex);
					window.location.reload();
					//window.location.reload();
					/* $("#zhanCun").hide();	
					$("#save").hide();
					$("#back").hide();
					$("#backs").hide(); */
				}
			});
		
		
	}
	
		function back(a){
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
				url : "/claimcar/claim/CancelBack.do", // 后台校验
				data : params,
				success : function(index) {// 回调方法，可单独定义
					layer.msg("退回成功");
					//layer.close(layerIndex);
					layer.closeAll('loading');
					//window.location.reload();
					$("#zhanCun").hide();	
					$("#save").hide();
					$("#back").hide();
					$("#backs").hide();	
					$("#dsave").hide();
					$("#dback").hide();
					$("#dbacks").hide();
					$("#bcompel").hide();
					$("#dcompel").hide();
					$("#reasonCodes").hide();
					$("#reasonCodess").show();
					window.location.reload();
				}
			});
		}
		$('input[name="reasonCode"]').click(function(){
			 var vals=$('input:radio[name="reasonCode"]:checked').val();
			  if(vals=="a"){//同意
				  $("#back").hide();
				  $("#backs").hide();
				  $("#dback").show();
				  $("#dbacks").show();
				  $("#save").show();
				  $("#dsave").hide();
				  $("#bcompel").hide();
				  $("#dcompel").show();
			  }else{
				  $("#save").hide();
				  $("#dsave").show();
				  $("#back").show();
				  $("#backs").show();
				  $("#dback").hide();
			      $("#dbacks").hide();
				  $("#bcompel").show();
			      $("#dcompel").hide();
			  }
		});
		function bcompel(){
			var description = $("#description").val(); 
			if(isBlank(description)){
				layer.msg("请输入强制取消立案申请意见！");
				return ;
			}
			var params = $("#defossform").serialize();
			layer.load(0, {shade : [0.8, '#393D49']});
			$.ajax({
				type: "POST",
				url : "/claimcar/claim/cancelCompel.do", // 后台校验
				data : params,
				success : function(index) {// 回调方法，可单独定义
					layer.msg("强制取消立案申请成功");
					layer.closeAll('loading');
					$("#zhanCun").hide();	
					$("#save").hide();
					$("#back").hide();
					$("#backs").hide();	
					$("#dsave").hide();
					$("#dback").hide();
					$("#dbacks").hide();
					$("#bcompel").hide();
					$("#dcompel").hide();
					$("#reasonCodes").hide();
					$("#reasonCodess").show();
					window.location.reload();
				}
			});
		}
	</script>
</body>

</html>