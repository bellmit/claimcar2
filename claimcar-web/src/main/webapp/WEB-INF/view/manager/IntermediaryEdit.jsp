<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!-- 公估公司编辑 -->
<!DOCTYPE HTML>
<html>
<head>
<title>修改/增加机构信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

	<form id="IntermEditForm">
		<!-- 查勘信息  开始 -->
		<div class="table_wrap">
			<div class="table_cont mt-30 md-30">
				<div class="formtable">
				   <!--公估机构信息 -->
				   <input type="hidden" name="prpdIntermMainVo.Id" value="${prpdIntermMain.id}" />
					<%@include file="IntermediaryEdit_Interm.jspf" %>
				    <!--员工信息 -->
					<%@include file="IntermdiaryEdit_User.jspf" %>
						<!-- 纳税人信息 -->
					<%-- <%@include file="IntermediaryEdit_TexPayer.jspf" %> --%>
					
					<div class="btn-footer clearfix text-c">
						<input type="submit" class="btn btn-primary" id="submit" value="提交"/>
					</div>
				</div>

			</div>
		</div>
		<input type="hidden" name="prpdIntermBankVo.vaildFlag" value="1" id="vaildFlag"/>
		<input type="hidden" id="bankId" name="prpdIntermBankVo.Id" value="${prpdIntermBank.id }" />
		<input type="hidden" id="Mobile" name="prpdIntermBankVo.Mobile" value="${prpdIntermBank.mobile }" />
		<input type="hidden" id="City" name="prpdIntermBankVo.City" value="${prpdIntermBank.city }" />
		<input type="hidden" id="Provincep" name="prpdIntermBankVo.Province" value="${prpdIntermBank.province }" />
		<input type="hidden" id="BankOutlets" name="prpdIntermBankVo.BankOutlets" value="${prpdIntermBank.bankOutlets }" />
		<input type="hidden" id="BankNumber"name="prpdIntermBankVo.BankNumber" value="${prpdIntermBank.bankNumber }" />
		<input type="hidden" id="certifyNo"name="prpdIntermBankVo.CertifyNo" value="${prpdIntermBank.certifyNo }" />
		<input type="hidden" id="flag" name="flag" value="${flag }" />
		<input type="hidden" id="flag11" name="flag" value="1" />
		
	</form>
	<!-- 服务信息界面 -->
	<div style="display: none" id="ServerInfo">
		<%@include file="IntermediaryEdit_Server.jspf"%>
	</div>

	<script type="text/javascript">
	</script>
	<script type="text/javascript" src="${ctx }/js/flow/intermUser.js"></script>
    <script type="text/javascript" src="/claimcar/js/common/application.js"></script>
</body>
</html>
