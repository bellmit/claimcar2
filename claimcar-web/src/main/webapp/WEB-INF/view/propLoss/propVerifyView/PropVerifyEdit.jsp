<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>报案-案件查询</title>


<style type="text/css">
.text-r {
	background-color: #F5F5F5
}

.btnClass {
	background-color: gray
}
</style>
<script type="text/javascript">
	$(function() {
		//切换选项卡
		$.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current",
				"click", "0");
		$("#returnBack").click(function() {
			history.back();
		});
	});
</script>
</head>
<body>
	<div class="top_btn">
		<a class="btn  btn-primary">历史出险信息（4）</a> <a class="btn  btn-danger">风险提示信息</a>
		<a class="btn  btn-primary">案件备注（4）</a> <a class="btn  btn-disabled">多保单关联与取消</a>
		<a class="btn  btn-primary">无保转有保</a> <a class="btn  btn-primary">客户信息</a>
	</div>
	<div class="fixedmargin page_wrap">
		<div id="tab_demo" class="HuiTab">
			<div class="tabBar cl">
				<span>当前定损</span><span>损失信息</span>
			</div>
			<!--选项卡一 -->
			<div class="tabCon">
				<form action="" method="get" id="lossform" role="form">
					<%@include file="PropVerify_1.jspf"%>
					<%@include file="PropVerify_2.jspf"%>
					<%@include file="PropVerify_3.jspf"%>
			</div>

			<!-- 选项卡二 -->
			<div class="tabCon">
				<!--车俩损失损失列表-->
				<%@include file="../../propLoss/lossInfoView/carLossListInfo.jspf"%>
				<!-- 财产损失损失列表 -->
				<%@include file="../../propLoss/lossInfoView/propLossListInfo.jspf"%>
				<!--人伤损失列表 -->
				<%@include file="../../propLoss/lossInfoView/personLossListInfo.jspf"%>
			</div>
		</div>
		<div class="text-c">
			</br> <input class="btn btn-primary fl" id="returnBack" type="button"
				value="返回" />

		</div>
		</form>
	</div>
	</div>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
</body>

</html>