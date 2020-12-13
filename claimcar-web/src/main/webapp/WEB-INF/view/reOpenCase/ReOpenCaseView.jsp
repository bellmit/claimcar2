<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>重开赔案信息表查看界面</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
			<div class="table_cont">
				<!-- 基本信息    -->
				<div class="table_wrap">
					<div class="table_cont">
					<c:forEach var="reCase" items="${reCaseList }" varStatus="recordStatus">
						<div class="formtable">
						<div class="line"></div>
							<div class="row cl">
								<label class="form_label col-2">立案号</label>
								<div class="form_input col-3">
									${reCase.claimNo }
								</div>
								<label class="form_label col-2">重开赔案次数</label>
								<div class="form_input col-3">
									${reCase.seriesNo }
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">重开赔案员</label>
								<div class="form_input col-3">${reCase.openCaseUserName }</div>
								<label class="form_label col-2">重开赔案日期</label>
								<div class="form_input col-3">
								    <c:set var="openCaseDate">
									    <fmt:formatDate value="${reCase.openCaseDate}" pattern="yyyy-MM-dd" />
									</c:set>${openCaseDate }
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">本次结案日期</label>
								<div class="form_input col-3">
								    <c:set var="endCaseDate">
									    <fmt:formatDate value="${reCase.endCaseDate}" pattern="yyyy-MM-dd" />
									</c:set>${endCaseDate }
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">重开赔案原因</label>
								<div class="form_input col-2">
									${reCase.openReasonCode}
								</div>
							</div>
					        <div class="row cl">
					            <label class="form_label col-2">重开原因说明</label>
						        <div class="col-12">
							        <textarea  class="textarea" readonly="readonly" datatype="*0-500" >${reCase.openReasonDetail}</textarea>
						        </div>
					       </div>
					       <div class="line"></div><br>
						</div>
					 </c:forEach>
					</div>
				</div>
					
				
			</div>

		<div class="text-c mt-10">
			<input type="button" class="btn btn-primary" onclick="closeLayer()" value="返回" />
		</div>
	</div>

	<script type="text/javascript">
		$(function() {

		});
		function closeLayer(){
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index);
		}
	</script>
</body>

</html>