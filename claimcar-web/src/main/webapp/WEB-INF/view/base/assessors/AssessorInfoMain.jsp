<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>



<!DOCTYPE HTML>
<html>
<head>
<title>账户信息展示页面</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<div class="table_cont">
			<form id="accountForm" role="form" method="post" name="fm">
			<input type="hidden"  name="intermCode" id="intermCode" value="${intermCode}"/>
			<input type="hidden"  name="accountId" id="accountId" value="${accountId}" />
			<input type="hidden"  name="asseStatus" id="asseStatus" value="${prpDAccRollBackAccountVo.status}" />
			<input type="hidden" id="auditOpinionSign" value="${auditOpinionSign}" />
				<div class="table_wrap">
					<div class="table_title f14">理赔账户信息修改</div>
					<div class="table_cont">
						<div class="formtable">
							<div class="row cl">
								<label class="form_label col-2">申请人：</label>
								<div class="form_input col-2">
								${prpDAccRollBackAccountVo.rollbackCode}
								</div>
								<label class="form_label col-2">申请时间：</label>
								<div class="form_input col-2">${requstime}</div>
								<label class="form_label col-2">支付失败原因:</label>
								<div class="form_input col-2">${prpDAccRollBackAccountVo.errorMessage}</div>
							</div>
							<div class="row cl">
							    <label class="form_label col-2">业务号：</label>
								<div class="form_input col-3">${prpDAccRollBackAccountVo.certiNo}</div>
								<label class="form_label col-2"></label>
								<div class="form_input col-3"></div>
								
							</div>
							   
							
					</div>
							
					
				</div>
				<%@include file="AssessorMoreMessage.jsp"%>
				</div>
				</br>
			<!-- 审核意见-->
			<div id="opnion">
			<%@include file="AuditOpnionView.jsp"%>
			</div>
				</br>
				<div class="text-c mt-10">
				    <input class="btn btn-primary" onclick="assPayCustomView('${accountId}','88','${prpDAccRollBackAccountVo.certiNo}','${prpDAccRollBackAccountVo.id}','${intermCode}','${registNo}')" id="look" type="botton" style="display:none;" value="查看银行信息" />
					<input class="btn btn-primary" id="mod" onclick="assModifiAccInfo('${accountId}','${prpDAccRollBackAccountVo.errorType}','${prpDAccRollBackAccountVo.certiNo}','${prpDAccRollBackAccountVo.id}','${intermCode}','${registNo}')" type="botton" style="display:none;"  value="修改银行信息" />
					
				</div>
				<div class="text-c mt-10">
					<!-- <input class="btn btn-primary " id="save" onclick="saveAcc()" type="button" value="保存"> -->
					<!-- <input class="btn btn-primary ml-5" onclick="" type="button" value="关闭"> -->
				</div>
				
				
			</form>
		</div>
		</div>
		
    <script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript">
	
		$(function(){
			var asseStatus=$("#asseStatus").val();
			//退票审核退回的，展示退票审核意见
			var auditOpinionSign=$("#auditOpinionSign").val();
			if(auditOpinionSign!='1'){
				$("#opnion").attr("style","display:none");
			}
			if(asseStatus!='0'){
				$("#mod").attr("style","display:inline-block");
			 
			}else{
				$("#look").attr("style","display:inline-block");
			}
		});
		
		function assModifiAccInfo(accountId,errorType,certiNo,backaccountId,intermCode,registNo){
		
			if(isBlank(accountId)){
				layer.msg("有效的银行信息Id为空,请检查该公估机构下的银行账号信息!",4000);
				return false;
			}
			paycustom_url="/claimcar/assessors/assessorBankInfoQuery.do?accountId="+accountId+"&errorType="+errorType+"&certiNo="+certiNo+"&backaccountId="+backaccountId+"&intermCode="+intermCode+"&registNo="+registNo;
			layer.open({
				type : 2,
				title : '银行信息修改',
				shade : false,
				area : ['85%','90%'],
				content : paycustom_url,
				end : function() {
					window.location.reload();
				}
			});
			
		}
		
		function assPayCustomView(accountId,errorType,certiNo,backaccountId,intermCode,registNo){
			if(isBlank(accountId)){
				layer.msg("有效的银行信息Id为空,请检查该公估机构下的银行账号信息!",4000);
				return false;
			}
			paycustom_url="/claimcar/assessors/assessorBankInfoQuery.do?accountId="+accountId+"&errorType="+errorType+"&certiNo="+certiNo+"&backaccountId="+backaccountId+"&intermCode="+intermCode+"&registNo="+registNo;
			layer.open({
				type : 2,
				title : '银行信息查看',
				shade : false,
				area : ['85%','90%'],
				content : paycustom_url,
				end : function() {
					window.location.reload();
				}
			});
			
		}
	</script>
</body>

</html>