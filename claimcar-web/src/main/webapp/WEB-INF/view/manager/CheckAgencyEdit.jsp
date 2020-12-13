<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!-- 查勘公司编辑 -->
<!DOCTYPE HTML>
<html>
<head>
<title>修改/增加机构信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

	<form id="IntermEditForm">
		<!-- 公估信息  开始 -->
		<div class="table_wrap">
			<div class="table_cont mt-30 md-30">
				<div class="formtable">
				   <!--查勘机构信息 -->
				   <input type="hidden" name="prpdCheckAgencyMainVo.Id" value="${prpd.id}" />
					<%@include file="CheckAgencyEdit_Interm.jspf" %>
				    <!--员工信息 -->
					<%@include file="CheckAgencyEdit_User.jspf" %>
						<!-- 纳税人信息 -->
					<%-- <%@include file="IntermediaryEdit_TexPayer.jspf" %> --%>
					
					<div class="btn-footer clearfix text-c">
						<input type="submit" class="btn btn-primary" id="submit" value="提交"/>
					</div>
				</div>

			</div>
		</div>
		<input type="hidden" name="prpdcheckBankVo.vaildFlag" value="1" id="vaildFlag"/>
		<input type="hidden" id="bankId" name="prpdcheckBankVo.Id" value="${prpdcheckBank.id }" />
		<input type="hidden" id="Mobile" name="prpdcheckBankVo.Mobile" value="${prpdcheckBank.mobile }" />
		<input type="hidden" id="City" name="prpdcheckBankVo.City" value="${prpdcheckBank.city }" />
		<input type="hidden" id="Provincep" name="prpdcheckBankVo.Province" value="${prpdcheckBank.province }" />
		<input type="hidden" id="BankOutlets" name="prpdcheckBankVo.BankOutlets" value="${prpdcheckBank.bankOutlets }" />
		<input type="hidden" id="BankNumber"name="prpdcheckBankVo.BankNumber" value="${prpdcheckBank.bankNumber }" />
		<input type="hidden" id="certifyNo"name="prpdcheckBankVo.CertifyNo" value="${prpdcheckBank.certifyNo }" />
		<input type="hidden" id="flag" name="flag" value="${flag }" />
		<input type="hidden" id="flag11" name="flag" value="1" />
		
	</form>
	<!-- 服务信息界面 -->
	<div style="display: none" id="ServerInfo">
		<%@include file="CheckAgencyEdit_Server.jspf"%>
	</div>

	<script type="text/javascript">
	</script>
	<script type="text/javascript" src="${ctx }/js/flow/CheckAgencyUser.js"></script>
    <script type="text/javascript" src="/claimcar/js/common/application.js"></script>
</body>
</html>
