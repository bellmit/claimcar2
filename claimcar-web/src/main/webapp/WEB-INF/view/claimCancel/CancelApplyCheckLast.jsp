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
				<%@include file="ClaimCancelApplyCheck_InputLast.jsp" %>
			</div>
		</form>

		<div class="text-c mt-10">
		<c:if test="${handlerStatus!=3 }">
		<div id="yingchan">
			<input class="btn btn-primary ml-5" id="" onclick="zhanCun()" type="button" value="暂存">
			<input class="btn btn-primary ml-5" id="save" onclick="save()" type="button" value="审核通过"> 
			<input class="btn btn-disabled ml-5" id="dsave" type="button" value="审核通过"> 
			<input class="btn btn-primary ml-5" id="back" onclick="back('1')" value="退回下一级" type="button"> 
			<input class="btn btn-disabled ml-5" id="dback"  value="退回下一级" type="button"> 
			<input class="btn btn-primary ml-5" id="backs" onclick="back('0')" type="button" value="退回申请"> 
			<input class="btn btn-disabled ml-5"  id="dbacks" type="button" value="退回申请"> 
		</div>
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
		 var vals=$('input:radio[name="reasonCode"]:checked').val();
		  if(vals=="a"){//同意
			  $("#back").hide();
			$("#backs").hide();
			$("#dback").show();
			$("#dbacks").show();
			 $("#save").show();
			 $("#dsave").hide();
		  }else{
			  $("#save").hide();
			  $("#dsave").show();
			  $("#back").show();
				$("#backs").show();
				$("#dback").hide();
				$("#dbacks").hide();
		  }
});
	var layerIndex = 0;

		function save(){
			var description = $("#description").val(); 
			if(isBlank(description)){
				layer.msg("请输入审核原因！");
				return ;
			}
			layer.load(0, {shade : [0.8, '#393D49']});
				$.ajax({
					type: "POST",
					url : "/claimcar/claim/claimCancel.do", // 后台校验
					data : $("#defossform").serialize(),
					success : function(jsonData) {// 回调方法，可单独定义
						var result = eval(jsonData);
					if(result.status=='200'){
						layer.msg("提交成功");
						layer.close(layerIndex);
						layer.closeAll('loading');
						//window.location.reload();
						$("#yingchan").hide();
						$('input[name="reasonCode"]').attr("name","aaa");
						window.location.reload();
					}else{
					    layer.alert(result.statusText);
					    layer.closeAll('loading');
					}
					
					
						
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
				url : "/claimcar/claim/claimCancelBack.do", // 后台校验
				data : params,
				success : function(jsonData) {// 回调方法，可单独定义
					layer.msg("退回成功");
					layer.close(layerIndex);
					layer.closeAll('loading');
					//window.location.reload();
					$("#yingchan").hide();
					$('input[name="reasonCode"]').attr("name","aaa");
					window.location.reload();
				}
			});
		
		
	}
		
		function zhanCun(){
			$.ajax({
				type: "POST",
				url : "/claimcar/claim/claimCancelZCLast.do", // 后台校验
				data : $("#defossform").serialize(),
				success : function(jsonData) {// 回调方法，可单独定义
					layer.msg("暂存成功");
					layer.close(layerIndex);
					window.location.reload();
					//window.location.reload();
					/* $("#yingchan").hide(); */
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
			  }else{
				  $("#save").hide();
				  $("#dsave").show();
				  $("#back").show();
					$("#backs").show();
					$("#dback").hide();
					$("#dbacks").hide();
			  }
		});
		
	</script>
</body>

</html>