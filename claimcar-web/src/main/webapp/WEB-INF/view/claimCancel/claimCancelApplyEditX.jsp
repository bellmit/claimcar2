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
		<input hidden="hidden" id="flags" name="flags" value="${flags}">
		
			<input hidden="hidden" name="taskId" value="${ taskId}">
		
				<!-- 输入信息    -->
				<%@include file="claimCancelApplyEdit_Inputs.jsp" %>
			</div>
			<div class="text-c mt-10 hidden" id="shenQin">
			<div class="table_wrap">
			<br>
			<div class="table_cont ">
			<table class="table table-striped table-border table-hover">
			
				<thead>
					 			<tr class="text-c">
					 				<th>提交路径选择</th>
					 				<th>
					 				<select class="select" name="updateType" id="updateType" style='width:40%;'>
										<option value="1">注销审核处理</option>
									</select>
									</th>
					 			</tr>
				</thead>
					<tbody>
					</tbody>
			</table>
					<div class="text-c">
						<br/>
						<input class="btn btn-primary ml-5"  onclick="updates()"
						type="button" value="提交任务">
					</div>
			</div>
			</div>
			</div>
			<div class="text-c mt-10 hidden" id="juPei">
				<div class="table_wrap">
			<br>
			<div class="table_cont ">
			<table class="table table-striped table-border table-hover">
			
				<thead>
					 			<tr class="text-c">
					<th>提交路径选择</th>
					<th>
					<select class="select" name="juPeiType" id="juPeiType" style='width:40%;'>
							<option value="1">核赔</option>
						</select>
						</th>
					 			</tr>
				</thead>
					<tbody>
					</tbody>
			</table>
				<div  class="text-c">
					<input class="btn btn-primary ml-5" onclick="juPei()"
						type="button" value="提交任务">
				</div>
			</div>
			</div>
			</div>
		</form>

		<div class="text-c mt-10" id="noCancel">
			<input class="btn btn-primary ml-5" id="" onclick="zhanCun()" type="button" value="暂存">
			<input class="btn btn-primary ml-5" id="save" onclick="save()" type="button" value="提交"> 
			
		</div>
	</div>

	<script type="text/javascript">
	var layerIndex = 0;
		function save(){
			var a = $("#dealReasoon").val();
			if(a=="" ||a == null){
				layer.alert("请选择任务类型!");
				return;
			}
			var applyReason = $("#applyReason").val();
			if(applyReason=="" ||applyReason == null){
				layer.alert("请填写原因!");
				return;
			}
			if(a=="1"||a=="2"){
				layerIndex=layer.open({
				    type: 1,
				    title: false,
				    shade: false,
					shadeClose : true,
					scrollbar : false,
				    skin: 'yourclass',
				    area: ['500px', '150px'],
				    content:$("#shenQin")
				});
				$("#shenQin").removeClass("hidden");  
			}else{
				var swindleReason = $("#swindleReason").val();
				if(swindleReason=="" ||swindleReason == null){
					layer.alert("请填写欺诈标志!");
					return;
				}
				var swindleType = $("#swindleType").val();
				if(swindleType=="" ||swindleType == null){
					layer.alert("请填写欺诈类型!");
					return;
				}
				var swindleSum = $("#swindleSum").val();
				if(swindleSum=="" ||swindleSum == null){
					layer.alert("请填写欺诈挽回损失金额!");
					return;
				}
				
				layerIndex=layer.open({
				    type: 1,
				    title: false,
				    shade: false,
					shadeClose : true,
					scrollbar : false,
				    skin: 'yourclass',
				    area: ['500px', '150px'],
				    content:$("#juPei")
				});
				$("#juPei").removeClass("hidden");  
			}
		}
		function updates(){
			/* var riskCode = $("#riskCode").val();
			var claimNo = $("#claimNo").val(); */
			var updateType = $("#updateType").val();
			/* var flowId = $("#flowId").val();
			alert("123=="+flowId); */
			/* alert(riskCode);
			alert(claimNo); */
			/* var claimNo = $("#claimNo").val(); 
			var taskId = $("#taskId").val();  */
			if(updateType=="1"){
				layer.load(0, {shade : [0.8, '#393D49']});
				$.ajax({
					type: "POST",
					url : "/claimcar/claim/claimCancelF.do", // 后台校验
					data : $("#defossform").serialize(),
					success : function(res) {// 回调方法，可单独定义
						reMsg = res.data;
						
						if(reMsg=="1"){
						layer.confirm('发起成功', {
							btn : [ '确定']
							}, function(index) {
								$("#noCancel").addClass("hidden");  
								layer.close(index);
								layer.close(layerIndex);
								layer.closeAll('loading');
								
								window.location.reload();
							});
					}else{
						layer.confirm('该案件不能发起', {
							btn : [ '确定']
							}, function(index) {
								layer.close(index);
								layer.close(layerIndex);
								layer.closeAll('loading');
								window.location.reload();
							});
					}
					},
					error : function() {
						layer.closeAll('loading');
						layer.msg("发起失败");
					}
				}); 
			}else{
				layer.close(layerIndex);
			}
			
		}
		
		
		
		
		//拒赔
		function juPei(){
			var swindleReason = $("#swindleReason").val();
			if(swindleReason=="" ||swindleReason == null){
				layer.alert("请填写欺诈标志!");
				return;
			}
			var swindleType = $("#swindleType").val();
			if(swindleType=="" ||swindleType == null){
				layer.alert("请填写欺诈类型!");
				return;
			}
			var swindleSum = $("#swindleSum").val();
			if(swindleSum=="" ||swindleSum == null){
				layer.alert("请填写欺诈挽回损失金额!");
				return;
			}
			var juPeiType = $("#juPeiType").val();
			if(juPeiType=="1"){
				layer.load(0, {shade : [0.8, '#393D49']});
				$.ajax({
					type: "POST",
					url : "/claimcar/claim/claimCancelJuPeiF.do", // 后台校验
					data : $("#defossform").serialize(),
			/* 		success : function(jsonData) {
						layer.msg("发起成功");
						layer.close(layerIndex);
					}
				}); */
				success : function(res) {// 回调方法，可单独定义
					reMsg = res.data;
					
					if(reMsg=="1"){
					layer.confirm('发起成功', {
						btn : [ '确定']
						}, function(index) {
							layer.close(index);
							layer.close(layerIndex);
							$("#noCancel").addClass("hidden");  
							layer.closeAll('loading');
							
							window.location.reload();
						});
				}else{
					layer.confirm('该案件不能发起', {
						btn : [ '确定']
						}, function(index) {
							/* var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
							parent.layer.close(index); //再执行关闭  */
							layer.close(index);
							layer.close(layerIndex);
							layer.closeAll('loading');
							window.location.reload();
						});
				}
				},
				error : function() {
					layer.closeAll('loading');
					layer.msg("发起失败");
				}
			}); 
			}else{
				layer.close(layerIndex);
			}
			
		}
		
		function zhanCun(){
			var a = $("#dealReasoon").val();
			if(a=="" ||a == null){
				layer.alert("请选择任务类型!");
				return;
			}
			var applyReason = $("#applyReason").val();
			if(applyReason=="" ||applyReason == null){
				layer.alert("请填写原因!");
				return;
			}
			layer.load(0, {shade : [0.8, '#393D49']});
			$.ajax({
				type: "POST",
				url : "/claimcar/claim/claimInitZhanCun.do", // 后台校验
				data : $("#defossform").serialize(),
				success : function(jsonData) {// 回调方法，可单独定义
					layer.alert("暂存成功",{closeBtn:0},function(){
						window.location.reload();
						layer.closeAll('loading');
					});
				}
			});
		
		
	}
		$("#dealReasoon").change(function(){
			var dealReasoon = $("#dealReasoon").val();
			if(dealReasoon==3||dealReasoon==4){
				$("#zhapian").removeClass("hidden");  
			}else if(dealReasoon==1||dealReasoon==2){
				$("#zhapian").addClass("hidden"); 
			}
		});
		$(document).ready(function(){
			var flags = $("#flags").val();
			if(flags!="1"){
				layer.alert(flags);
				$("#noCancel").addClass("hidden");
				$("input").attr("disabled","disabled");
				$("#applyReason").attr("disabled","disabled"); 
				$("#dealReasoon").attr("disabled","disabled"); 
				$("#remarks").attr("disabled","disabled"); 
				$("#swindleSum").attr("disabled","disabled"); 
				$("#swindleType").attr("disabled","disabled"); 
				$("#swindleReason").attr("disabled","disabled"); 
			}
			var dealReasoon = $("#dealReasoon").val();
			if(dealReasoon==3||dealReasoon==4){
				$("#zhapian").removeClass("hidden");  
			}else if(dealReasoon==1||dealReasoon==2){
				$("#zhapian").addClass("hidden"); 
			}
		}); 
	</script>
</body>

</html>