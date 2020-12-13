<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>追偿回馈信息</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />

</head>
<body>
	<div class="table_title f14">追偿信息</div>
		<div class="formtable">
		<form id="confirmform" name="confirmform" class="form-horizontal" role="form" method="post">
			<input type="hidden" name="recoveryCode" value="${platLockVo.recoveryCode }"/>
			<input type="hidden" name="registNo" value="${platLockVo.registNo }"/>
			<input type="hidden" id="accountInfoFlag" value="${platLockVo.accountInfoFlag }"/>
			<div class="row mb-3 cl">
				<label class="form_label col-2">报案号</label>
				<div class="form_input col-4">${platLockVo.registNo }</div>
				<label class="form_label col-2">保单号</label>
				<div class="form_input col-4">${platLockVo.policyNo }</div>
			</div>
			<div class="row mb-3 cl">
				<label class="form_label col-2">对方报案号</label>
				<div class="form_input col-4">${platLockVo.oppoentRegistNo }</div>
				<label class="form_label col-2">对方保单号</label>
				<div class="form_input col-4">${platLockVo.oppoentPolicyNo }</div>
			</div>
			
			<div class="row mb-3 cl">
				<label class="form_label col-2">对方保险公司代码</label>
				<div class="form_input col-4">${platLockVo.oppoentInsurerCode }</div>
				<label class="form_label col-2">对方保险公司名称</label>
				<div class="form_input col-4">
					<app:codetrans codeType="DWInsurerCode" codeCode="${platLockVo.oppoentInsurerCode }"/>
				</div>
			</div>
			<div class="row mb-3 cl">
				<label class="form_label col-2">结算码</label>
				<div class="form_input col-4">${platLockVo.recoveryCode }</div>
				<label class="form_label col-2">结算码状态</label>
				<div class="form_input col-4">${platLockVo.recoveryCodeStatus}</div>
			</div>
			 <div class="row mb-3 cl">
				<label class="form_label col-2">理算核定金额</label>
				<div class="form_input col-8">${compensateAmount}</div>
				<input type="hidden" id="compensateAmount" value="${compensateAmount}" />
				
			</div>
			<div class="table_title f14">本次追偿项目</div>
			<table id="resultDataTable" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
			<thead class="text-c">
				<tr>
					<th>本方代付金额</th>
					<th>对方清付金额</th>
				</tr>
			</thead>
			<tbody class="text-c">
			<tr>
				<td>
					<input type="text" class="input-text" name="sumRealAmount" readonly="readonly" style="width: 40%" value="${platLockVo.sumRealAmount}" />
				</td>
				<td>
					<input type="text" class="input-text" readonly="readonly" name="sumRecoveryAmount" id="sumRecoveryAmount"
						style="width: 40%" value="${platLockVo.sumRecoveryAmount }" />
			</td>
				
			</tr>
			</tbody>
		</table>
		
			<div class="line"></div>
		
			<div class="text-c">
				<input type="button" class="btn btn-primary" id="button" onclick="lockReturn()" value="提交" />
				<!-- <input type="submit" class="btn btn-primary" id="button" value="返回" /> -->
				</span><br />
			</div>
		</form>		
	</div>
	
<script type="text/javascript">
$(function(){
	var ajaxEdit = new AjaxEdit($('#confirmform'));
	ajaxEdit.targetUrl = "/claimcar/subrogationEdit/sendRecoveryDataSH.do";
	ajaxEdit.afterSuccess = recoveryConfirm;
	ajaxEdit.bindForm();//绑定表单
	
});


function recoveryConfirm(data){
	var result = eval(data);
	if (result.status == "200") {
		var conIndx=layer.confirm('追偿回馈确认成功', {
			btn: ['返回'] //按钮
		}, function(){
			layer.close(conIndx);
		});
	}
}

function lockReturn(){
	var compensateAmount=$("#compensateAmount").val();
	var sumRecoveryAmount=$("#sumRecoveryAmount").val();
	if(compensateAmount!=sumRecoveryAmount){
		 var conIndx = layer.confirm('清付金额与理算核定金额不一致，是否确认提交？', {
		  btn: ['是','否'] //按钮
		}, function(){
			
		  $("#confirmform").submit();
			layer.close(conIndx);
         
		}, function(){
			layer.close(conIndx);
		});
		 
	}else{
		$("#confirmform").submit();
	}
} 
	
</script>

</body>
</html>
