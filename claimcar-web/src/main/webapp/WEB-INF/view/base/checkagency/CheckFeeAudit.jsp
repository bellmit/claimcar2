<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>查勘费审核</title>
</head>
<body id="CheckFees">
	<br>
	<br>

	<!-- 按钮组 -->
	<div class="top_btn">
		<a class="btn  btn-primary mb-10" onclick="imageCheckFeeUpload('${prpLAcheckMainVo.taskId}','${handlerStatus}');">信雅达影像上传</a>
	</div>
	<br/>
	<form name="fm" id="fm" class="form-horizontal" role="form">
	
		<!-- 任务基本信息 -->
		<%@include file="CheckFeeTaskBasicInfoOne.jsp"%>
		<!-- 费用 -->
		<%@include file="CheckFee.jsp"%>
		<!-- 意见 -->
		<%@include file="CheckFeeOpinion.jsp"%>
		<!-- 意见列表 -->
		<%@include file="CheckFeeOpinionsList.jsp"%>
		<br>
		<c:choose>
			<c:when test="${handlerStatus==3 }">
			</c:when>
			<c:otherwise>
				<div id="submitDiv" class="text-c">
					<br />
					<input class="btn btn-primary " id="submit" onclick="submitNextNode(this)" type="submit" value="审核通过">
					&nbsp;&nbsp;&nbsp;
					<input class="btn btn-primary " id="back" onclick="submitNextNode(this)" type="submit" value="退回申请">
				</div>
			</c:otherwise>
		</c:choose>
	</form>
	<script type="text/javascript" src="${ctx}/js/base/checkFeeAudit.js"></script>
</body>
</html>