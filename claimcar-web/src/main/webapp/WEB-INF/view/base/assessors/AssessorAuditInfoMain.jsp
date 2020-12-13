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
			<input type="hidden"  name="autidStatus" id="autidStatus" value="${prplInterrmAuditVo.status}" />
			<input type="hidden"  name="auditId" id="auditId" value="${auditId}" />
			<input type="hidden"  name="sign" id="sign" value="" />
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
							    <label class="form_label col-2">公估机构：</label>
								<div class="form_input col-2">${intermname}</div>
						        <label class="form_label col-2">业务号：</label>
								<div class="form_input col-3">${prpDAccRollBackAccountVo.certiNo}</div>
								
							</div>
							   
							
					</div>
							
					
				</div>
				<%@include file="AssessorMoreMessage.jsp"%>
				</div>
				</br>
				<div class="text-c mt-10">
				    <input class="btn btn-primary" onclick="assAditPayCustomView('${auditId}')"  value="查看银行信息" />
					
				</div>
				
			
			
		    </div>
		</div>
		<div class="text-c mt-10">
		<div class="table_title f14" style="float:left;">审核意见<font class="must">*</font></div>
		<textarea class="textarea h100" id="contexts" maxlength="255"
					name="auditOpioion" datatype="*1-150" nullmsg="请输入审核意见！"
					placeholder="请输入...">${prplInterrmAuditVo.auditOpinion}</textarea>
		</div>
		</form>
		</br>
		<div class="text-c mt-10">
		  <input class="btn btn-primary " id="certain" onclick="saveAcc('${auditId}','0')" type="button" value="暂存">
		  &nbsp;&nbsp;&nbsp;&nbsp;
		  <input class="btn btn-primary " id="save" onclick="saveAcc('${auditId}','1')" type="button" value="审核通过">
		  &nbsp;&nbsp;&nbsp;&nbsp;
		  <input class="btn btn-primary ml-5" id="back" onclick="saveAcc('${auditId}','2')" type="button" value="退回"> 
		</div>
		</br>
		</br>
    <script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript">
	
		$(function(){
			var status=$("#autidStatus").val();
			if(status=='1' || status=='2'){
				$("#contexts").attr("disabled","disabled");
				$("#certain").attr("style","display: none");
				$("#save").attr("style","display: none");
				$("#back").attr("style","display: none");
			}
		});
		
		function assAditPayCustomView(auditId){
			if(isBlank(auditId)){
				layer.msg("退票审核表Id为空,请检查该退票审核表信息!",4000);
				return false;
			}
			paycustom_url="/claimcar/assessors/assessorAuditBankInfoQuery.do?auditId="+auditId;
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
		
		function saveAcc(auditId,sign){
			if(sign=='0'){
				$("#sign").val("0");
			}else if(sign=='1'){
				$("#sign").val("1");
			}else{
				$("#sign").val("2");
			}
			var contexts= $("#contexts").val();
			
			if(contexts=='' || contexts=="" || contexts==null){
				layer.msg("审核意见不能为空！",4000);
				return false;
			}
			var params = $("#accountForm").serialize();
			 var url="/claimcar/assessors/auditWays.do";
			$.ajax({
				url : url, // 后台校验
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				data : params, // 要传递的数据
				async : false,
				success : function(jsonData) {// 回调方法，可单独定义
					var result = eval(jsonData);
					if (result.status == "200") {
						layer.msg("捷报信息："+result.statusText,{
							icon:1,
							time:4000
						},function(){
							window.location.reload();
						});
					}
					
					if(result.status == "500"){
						layer.msg("返回信息："+result.statusText,{
							icon:1,
							time:4000
						},function(){
							window.location.reload();
						});
					}
					
				},
				error : function() {
					
					layer.msg("操作失败，请刷新页面后，再试！",4000);
				}
			});
		}
	</script>
</body>

</html>