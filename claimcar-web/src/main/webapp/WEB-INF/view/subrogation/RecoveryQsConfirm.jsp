<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>代位求偿待结算金额确认信息</title>

</head>
<body>
	<div class="table_title f14">代位求偿待结算金额确认信息</div>
		<div class="formtable">
			<form id="confirmform" name="confirmform" class="form-horizontal" role="form" method="post">
				<input type="hidden" name="recoveryCode" value="${accountsInfoVo.accountsNo }"/>
				<input type="hidden" name="realAmount" value="${accountsInfoVo.compensateAmount }"/>
				<div class="row mb-3 cl">
					<label class="form_label col-2">结算码</label>
					<div class="form_input col-4">${accountsInfoVo.accountsNo }</div>
					<label class="form_label col-2">结算码状态</label>
					<div class="form_input col-4">
						<app:codetrans codeType="RecoveryCodeStatus" codeCode="${accountsInfoVo.accountsNoStatus }"/>
					</div>
				</div>
				<div class="row mb-3 cl">
					<label class="form_label col-2">结算开始时间</label>
					<div class="form_input col-4">
						<fmt:formatDate value="${accountsInfoVo.accountsStartDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
					</div>
					<label class="form_label col-2">代付/清付险种</label>
					<div class="form_input col-4">
						<app:codetrans codeType="DWCoverageType" codeCode="${accountsInfoVo.coverageCode }"/>
					</div>
				</div>
				<div class="row mb-3 cl">
					<label class="form_label col-2">追偿方保险公司代码</label>
					<div class="form_input col-4">
						<app:codetrans codeType="DWInsurerCode" codeCode="${accountsInfoVo.recoverCompanyCode }"/>
					</div>
					<label class="form_label col-2">追偿方承保区域</label>
					<div class="form_input col-4">
						<app:codetrans codeType="DWInsurerArea" codeCode="${accountsInfoVo.recoverAreaCode }"/>
					</div>
				</div>
				<div class="row mb-3 cl">
					<label class="form_label col-2">责任方保险公司</label>
					<div class="form_input col-4">
						<app:codetrans codeType="DWInsurerCode" codeCode="${accountsInfoVo.compensateComCode }"/>
					</div>
					<label class="form_label col-2">责任方承保区域</label>
					<div class="form_input col-4">
						<app:codetrans codeType="DWInsurerArea" codeCode="${accountsInfoVo.compensationAreaCode }"/>
					</div>
				</div>
				<div class="row mb-3 cl">
					<label class="form_label col-2">代付金额</label>
					<div class="form_input col-4">${accountsInfoVo.recoverAmount }</div>
					<label class="form_label col-2">清付金额</label>
					<div class="form_input col-4">${accountsInfoVo.compensateAmount }</div>
				</div>
				
				<div class="row mb-3 cl">
					<label class="form_label col-2">最新清付金额</label>
					<div class="form_input col-8">${accountsInfoVo.lastCompensateAmount }</div>
				</div>
				<div class="line"></div>
		
				<div class="text-c">
					<input type="submit" class="btn btn-primary" id="button" value="待结算金额确认" />
				</span><br />
			</div>
		</form>			
	</div>
	<script type="text/javascript" src="/claimcar/js/subrogation/recoveryConfirm.js"></script>	
	
</body>
</html>
