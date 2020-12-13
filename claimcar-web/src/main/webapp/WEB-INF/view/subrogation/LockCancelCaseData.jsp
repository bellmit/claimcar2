<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>代位求偿已锁定案件信息</title>

</head>
<body>
<div class="page_wrap">
	<div class="table_title f14">代位求偿已锁定案件信息</div>
		<div class="formtable">
		<form id="lockCancelform" name="lockCancelform" class="form-horizontal" role="form" method="post">
			<input type ="hidden" name="recoveryCode" id="recoveryCode" value="${platLockVo.recoveryCode }" />
			<input type ="hidden" name="registNo" id="registNo"  value="${platLockVo.registNo }" />
			<div class="row mb-3 cl">
				<label class="form_label col-2">报案号</label>
				<div class="form_input col-4">${platLockVo.registNo }</div>
				<label class="form_label col-2">结算码</label>
				<div class="form_input col-4">${platLockVo.recoveryCode }</div>
			</div>
			<div class="row mb-3 cl">
				<label class="form_label col-2">本方理赔编号</label>
				<div class="form_input col-4">${platLockVo.claimSequenceNo }</div>
				<label class="form_label col-2">本方保单号</label>
				<div class="form_input col-4">${platLockVo.policyNo }</div>
			</div>
			
			<div class="row mb-3 cl">
				<label class="form_label col-2">本车车牌号码</label>
				<div class="form_input col-4">${platLockVo.licenseNo }</div>
				<label class="form_label col-2">被保险人</label>
				<div class="form_input col-4">${platLockVo.insureName }</div>
			</div>
			<div class="row mb-3 cl">
				<label class="form_label col-2">责任对方报案号</label>
				<div class="form_input col-4">${platLockVo.oppoentRegistNo }</div>
				<label class="form_label col-2">责任对方保险公司</label>
				<div class="form_input col-4">${platLockVo.oppoentInsurerCode }</div>
			</div>
			
			<div class="row mb-3 cl">
				<label class="form_label col-2">责任对方承保地区</label>
				<div class="form_input col-4">${platLockVo.oppoentInsurerArea }</div>
				<label class="form_label col-2">责任对方保单险种类型</label>
				<div class="form_input col-4">${platLockVo.coverageType }</div>
			</div>

			<div class="row mb-3 cl">
				<label class="form_label col-2">责任对方保单号</label>
				<div class="form_input col-4">${platLockVo.oppoentPolicyNo }</div>
				<label class="form_label col-2">责任对方本车车牌号码</label>
				<div class="form_input col-4">${platLockVo.oppoentLicensePlateNo }</div>
			</div>
			<div class="row mb-3 cl">
				<label class="form_label col-2">任对方本车发动机号</label>
				<div class="form_input col-4">${platLockVo.oppoentEngineNo }</div>
				<label class="form_label col-2">责任对方本车VIN码</label>
				<div class="form_input col-4">${platLockVo.oppoentVin }</div>
			</div>
			
			<div class="row mb-3 cl">
				<label class="form_label col-2">出险时间</label>
				<div class="form_input col-4">${platLockVo.lossTime }</div>
				<label class="form_label col-2">出险地点</label>
				<div class="form_input col-4">${platLockVo.lossArea }</div>
			</div>
			<div class="row mb-3 cl">
				<label class="form_label col-2">出险经过</label>
				<div class="form_input col-8">${platLockVo.lossDesc }</div>
			</div>
			
			<div class="row mb-3 cl">
				<label class="form_label col-2">取消原因</label>
				<div class="form_input col-2">
					<span class="select-box"> 
						<app:codeSelect codeType="FailureCause"  id="failureCause" type="select" name="failureCause"/>
						
					</span>
					
				</div>
			</div>
		</div>
		<div class="line"></div>
		<input type="hidden" id="recoveryCodeStatus" value="${platLockVo.recoveryCodeStatus}"/>
		<input type="hidden" id="recoveryName" value="<app:codetrans codeType='RecoveryCodeStatus' codeCode='${platLockVo.recoveryCodeStatus}'/>"/>
		
		<div class="text-c">
			<c:if test ="${platLockVo.recoveryCodeStatus ==1 }">
				<input type="button" class="btn btn-primary" id="button" onclick="lockCancel()" value="锁定取消" />
			</c:if>
			</span><br />
		</div>
		</form>
	</div>
	<script type="text/javascript" src="/claimcar/js/subrogation/lockCancel.js"></script>	
</body>
</html>
