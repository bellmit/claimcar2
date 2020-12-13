<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
    <title>拒赔核赔处理</title>
</head>
<body>
    <div class="fixedmargin page_wrap">
    	<!-- 按钮组 -->
      	<%@include file="VerifyClaimEdit_Buttons.jsp" %>
      	<p>
        <form  id="defossform" role="form" method="post"  name="fm" >
        	<div class="table_cont">
        	<!-- 核赔基本信息 -->
        	<%@include file="VerifyClaimEdit_VerifyInfo.jsp" %>
        	<div class="table_wrap">
				<div class="table_title f14">注销/拒赔原因</div>
				<div class="formtable">
					<div class="row cl">
						<div class="col-12">
							<input type="textarea" class="textarea">
						</div>
					</div>
				</div>
			</div>
            <!-- 审批意见 -->
            <%@include file="VerifyClaimEdit_VerifyAdvice.jsp" %>
            </div>
        </form>

    <div class="text-c">
        <br/>
        <input class="btn btn-primary " id="pend" type="submit" value="退回">
        <input class="btn btn-primary ml-5" id="save" onclick="save('submitLoss')" type="submit" value="提交上级">
    	<input class="btn btn-primary " id="pend" type="submit" value="核赔通过">
    </div>
    <!-- 案件备注功能隐藏域 -->
    <input type="hidden" id="nodeCode" value="VClaim">
    <input type="hidden" id="registNo" value="${param.registNo}">
    <input type="hidden" id="flag" value="${flag }">
    </div>

	<script type="text/javascript">
	$(function(){
		
	});

	</script>
</body>

</html>