<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>诉讼处理</title>
</head>
<body>
	<div class="table_wrap">
		<div class="table_title f14">报案基本信息</div>
		<div class="table_cont">
			<div class="formtable">
				
					
					<%-- <input type="hidden"  name="subNodeCode"  value="${subNodeCode }" > --%>
					<div class="row mb-3 cl">
						<label class="form-label col-1 text-c"> 报案号 </label>
						<div class="formControls col-3">
							<input type="text" class="input-text" readonly="readonly" id="registNo"
								name="prpLLawSuitVo.registNo" value="${registvo.registNo }" />
						</div>
						<label class="form-label col-1 text-c"> 报案日期 </label>
						<c:set var="reportTime">
									<fmt:formatDate value="${registvo.reportTime }" pattern="yyyy-MM-dd"/>
								</c:set>
						<div class="formControls col-3">
							<input type="text" class="input-text" readonly="readonly"
								 value="${reportTime }" />
						</div>
						<label class="form-label col-1 text-c"> 客户等级 </label>
						<div class="formControls col-3">
							<input type="text" class="input-text" readonly="readonly"
								value="${registvo.customerLevel}" />
						</div>
					</div>

					<div class="row mb-3 cl">
						<label class="form-label col-1 text-c">出险时间</label>
							<c:set var="damageTime">
									<fmt:formatDate value="${registvo.damageTime}" pattern="yyyy-MM-dd"/>
								</c:set>
						<div class="formControls col-3">
							<input type="text" class="input-text" readonly="readonly"
								value="${damageTime}" />
						</div>
						<label class="form-label col-1 text-c">出险地点</label>
						<div class="formControls col-3">
							<input type="text" class="input-text" readonly="readonly"
								 value="${registvo.damageAddress}" />
						</div>
						<label class="form-label col-1 text-c">出险原因</label>
						<div class="formControls col-3">
							<input type="text" class="input-text" readonly="readonly"
								 value="${registvo.damageCode}" />
						</div>
					</div>
				
			</div>
		</div>
	</div>
	<div class="table_wrap">
		<div class="table_title f14">诉讼任务处理</div>
		</div>
	<div class="text-c">
										<br /> <a class="btn btn-primary"
											onclick="closeL()">关闭</a>
									</div>
			<script type="text/javascript">
			$(function() {
				$.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon",
						"current", "click", "0");
			});
		</script>
		<script type="text/javascript">
			function closeL() {
				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
				parent.layer.close(index); //再执行关闭 
			}
			
			</script>
</body>

</html>