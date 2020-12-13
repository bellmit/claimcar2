<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head><title>
银行信息查看页面
</title></head>
<body>
<!-- 银行信息  开始 -->
<div class="table_wrap">
<form id="saveBankInfo"  >
	<div class="formtable">
		<div class="formtable mt-10">
			<div class="row cl">
				<input type="hidden" name="prpdcheckBankVo.Id" value="${prpdcheckBank.id}" id="bankVoId"/>
				<input type="hidden" value="${errorType}" id="errorType"/>
				<label class="form_label col-3">收款方户名：</label> 
				<div class="form_input col-2">
					<input type="text" class="input-text" name="prpdcheckBankVo.accountName" value="${prpdcheckBank.accountName}"  id="accountname" readonly="readonly"/>
					
				</div>
				<span class="c-red col-1">*</span> 
				<label class="form_label col-2">收款方账号：</label>
				<div class="form_input col-2 ">
					<input type="text" class="input-text"
						name="prpdcheckBankVo.accountNo" value="${prpdcheckBank.accountNo}"  id="account" readonly="readonly"/>
				</div>
				<span class="c-red col-1">*</span>
			</div>

			<div class="row cl">
				<label class="form_label col-3">收款方组织机构代码：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text"
						name="prpdcheckBankVo.certifyNo" value="${prpdcheckBank.certifyNo}" id="certifyNo" readonly="readonly"/>
				</div>
				<span class="c-red col-1">*</span>
                
				<label class="form_label col-2">收款方手机号码：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text"
						name="prpdcheckBankVo.mobile" value="${prpdcheckBank.mobile}"  id="accountcellphone" datatype="m|/^0\d{2,3}-\d{7,8}$/" readonly="readonly"/>
				</div>
				<span class="c-red col-1">*</span>
			</div>
			<div class="row cl">
				  <label class="form_label col-3"><font class="c-red">*</font>公私标志：</label>
				   <div class="form_input col-2">
					  <app:codeSelect codeType="publicAndPrivate" name="prpdcheckBankVo.publicAndPrivate" value="${prpdcheckBank.publicAndPrivate}" id="publicAndPrivate" type="select" clazz="must" disabled="true"/>
				   </div>
		   </div>
		</div>
		<p>
		<div class="line"></div>
		<p>
		<div class="formtable mt-5">
			<div class="row cl">
				<label class="form_label col-3">收款方开户行省/市：</label>
				<div class="form_label col-6" style="text-align: left">
					<%-- <app:areaSelect targetElmId="intermBankProvCity" areaCode="${prpdcheckBank.city}" showLevel="2" style="width:190px"/> --%>
					<app:areaSelect targetElmId="checkBankProvCity" showLevel="2" style="width:190px" areaCode="${prpdcheckBank.city}" isAreaCode="Y" disabled="true"/>
					<!-- <input type="hidden" id="intermBankProvCity" name="areaSelectCode" /> -->
					<%-- <input type="hidden" id="Province" name="prpdcheckBankVo.Province" value="${prpdcheckBank.province}" /> --%>
					<input type="hidden" id="checkBankProvCity" name="prpdcheckBankVo.city" value="${prpdcheckBank.city}" /> 
					<span class="c-red">*</span>

				</div>
				
			</div>

			<div class="row cl">
				<label class="form_label col-3">收款方开户行：</label>
				<div class="form_input col-2">
					<span class="select-box" style="width:100%"> 
					<%-- <app:codeSelect codeType="BankCode" name="prpdcheckBankVo.BankName" id="BankName" type="select"  value="${prpdcheckBank.bankName}" /> --%>
					<app:codeSelect codeType="" type="select" id="BankName" name="prpdcheckBankVo.BankName" lableType="name" value="${prpdcheckBank.bankName}" dataSource="${bankCodeMap}" disabled="true"/>
					</span>
				</div>
                <div class="form_input col-3">
					<input type="text" class="input-text" id="BankQueryText" value="${prpdcheckBank.bankOutlets}" readonly="readonly"/>
				</div>
				<span class="c-red col-1">*</span>
				<div class="form_input col-1 mb-10">
					<a class="btn btn-disabled" >检索行号</a>
				</div>
			</div>
		</div>
		<p>
		<div class="line"></div>
		<p>
		<div class="formtable">
			<div class="row cl">
				<label class="form_label col-3">收款方银行网点名称：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text"
						name="prpdcheckBankVo.BankOutlets" id="BankOutlets" datatype="*"
						readonly value="${prpdcheckBank.bankOutlets}" readonly="readonly"/>
				</div>
				<span class="c-red col-1">*</span> <label class="form_label col-2">收款方银行行号：</label>
				<div class="form_input col-2 ml-5">
					<input type="text" class="input-text"
						name="prpdcheckBankVo.BankNumber" id="BankNumber" datatype="*"
						readonly value="${prpdcheckBank.bankNumber}" readonly="readonly"/>
				</div>
				<span class="c-red col-1">*</span>
			</div>
			
			<div class="row cl">
			  <label class="form_label col-3"><font class="c-red">*</font>摘要：</label>
				<div class="form_input col-5">
				<input type="text" datatype="*1-1000"  class="input-text" value="${prpdcheckBank.remark}" name="prpdcheckBankVo.remark" readonly="readonly"/>
				</div>
			</div>
		</div>
		</br>
		 <div class="btn-footer clearfix text-c" id="submitDiv">
			<a class="btn btn-primary ml-5" id="cancelBtn" onclick="layerCancelBank()">关闭</a>
		</div>
         <input type="hidden" id="hideFlag" value="1"/>
	</div>
</form>
</div>
<%-- <!-- 行号查询界面 -->
		<div style="display: none" id="BankNumQ">
			<%@include file="IntermediaryEdit_BankNum.jspf"%>
		</div> --%>
<!-- 银行信息  结束 -->
<script type = "text/javascript">
function layerCancelBank(){//点击取消关闭银行信息界面并清空输入信息
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		    parent.layer.close(index); //再执行关闭           	
	}

</script>
</body>
</html>









