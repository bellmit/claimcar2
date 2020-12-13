<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head><title>
银行信息修改页面
</title></head>
<body>
<!-- 银行信息  开始 -->
<div class="table_wrap">
<form id="saveBankInfo"  >
	<div class="formtable">
		<div class="formtable mt-10">
			<div class="row cl">
				<input type="hidden" name="bankId" value="${prpdIntermBank.id}" id="bankId"/>
				<input type="hidden" value="${errorType}" id="errorType"/>
				<input type="hidden" name="backaccontId" value="${backaccontId}" id="backaccontId"/>
				<input type="hidden" name="intermCode" value="${intermCode}" id="intermCode"/>
				<input type="hidden" name="isSendMoney" value="" id="isSendMoney"/>
				<input type="hidden" name="registNo" value="${registNo}" id="registNo"/>
				<input type="hidden" name="status" value="${prpDAccRollBackAccountVo.status}" id="status"/>
				<input type="hidden"  value="${oldAccountName}" id="oldAccountName"/>
				<input type="hidden"  value="" id="isNeedAudit" name="isNeedAudit"/>
				
				<label class="form_label col-3">收款方户名：</label> 
				<div class="form_input col-2">
					<input type="text" class="input-text" name="prpdIntermBankVo.AccountName" value="${prpdIntermBank.accountName}"  id="accountname" datatype="*" onchange="checkSpecialCharactorOfAccountName(this)"/>
					
				</div>
				<span class="c-red col-1">*</span> 
				<label class="form_label col-2">收款方账号：</label>
				<div class="form_input col-2 ">
					<input type="text" class="input-text"
						name="prpdIntermBankVo.AccountNo" value="${prpdIntermBank.accountNo}"  id="account" datatype="*" onchange="checkSpecialByAccountNo(this)"/>
				</div>
				<span class="c-red col-1">*</span>
			</div>

			<div class="row cl">
				<label class="form_label col-3">收款方组织机构代码：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text"
						name="prpdIntermBankVo.CertifyNo" value="${prpdIntermBank.certifyNo}" id="certifyNo" datatype="*" onchange="checkCertifyNo()"/>
				</div>
				<span class="c-red col-1">*</span>
                
				<label class="form_label col-2">收款方手机号码：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text"
						name="prpdIntermBankVo.Mobile" value="${prpdIntermBank.mobile}"  id="accountcellphone" datatype="m|/^0\d{2,3}-\d{7,8}$/" />
				</div>
				<span class="c-red col-1">*</span>
			</div>
			<div class="row cl">
				  <label class="form_label col-3"><font class="c-red">*</font>公私标志：</label>
				   <div class="form_input col-2">
					  <%-- <app:codeSelect codeType="publicAndPrivate" name="prpdIntermBankVo.publicAndPrivate" value="${prpdIntermBank.publicAndPrivate}" id="publicAndPrivate" type="select" clazz="must"/> --%>
					  <span class="select-box"> 
							<select name="prpdIntermBankVo.publicAndPrivate" class="select" value="${prpdIntermBank.publicAndPrivate}" >
								<option value='0' selected = "selected">对公</option>
								<option value='1'>对私</option>
								
							</select>
							</span>
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
					<%-- <app:areaSelect targetElmId="intermBankProvCity" areaCode="${prpdIntermBank.city}" showLevel="2" style="width:190px"/> --%>
					<app:areaSelect targetElmId="intermBankProvCity" showLevel="2" style="width:190px" areaCode="${prpdIntermBank.city}" isAreaCode="Y"/>
					<!-- <input type="hidden" id="intermBankProvCity" name="areaSelectCode" /> -->
					<%-- <input type="hidden" id="Province" name="prpdIntermBankVo.Province" value="${prpdIntermBank.province}" /> --%>
					<input type="hidden" id="intermBankProvCity" name="prpdIntermBankVo.City" value="${prpdIntermBank.city}" /> 
					<span class="c-red">*</span>

				</div>
				
			</div>

			<div class="row cl">
				<label class="form_label col-3">收款方开户行：</label>
				<div class="form_input col-2">
					<span class="select-box" style="width:100%"> 
					<%-- <app:codeSelect codeType="BankCode" name="prpdIntermBankVo.BankName" id="BankName" type="select"  value="${prpdIntermBank.bankName}" /> --%>
					<app:codeSelect codeType="" type="select" id="BankName" name="prpdIntermBankVo.BankName" lableType="name" value="${prpdIntermBank.bankName}" dataSource="${bankCodeMap}" />
					</span>
				</div>

				<div class="form_input col-3">
					<input type="text" class="input-text" id="BankQueryText" value="${prpdIntermBank.bankOutlets}" readonly="readonly"/>
				</div>
				<span class="c-red col-1">*</span>
				<div class="form_input col-1 mb-10">
					<a class="btn btn-zd mr-10 fl"
						onClick="layerShowBankNum()" id="searchAocount">检索行号</a>
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
						name="prpdIntermBankVo.BankOutlets" id="BankOutlets" datatype="*"
						readonly value="${prpdIntermBank.bankOutlets}" />
				</div>
				<span class="c-red col-1">*</span> <label class="form_label col-2">收款方银行行号：</label>
				<div class="form_input col-2 ml-5">
					<input type="text" class="input-text"
						name="prpdIntermBankVo.BankNumber" id="BankNumber" datatype="*"
						readonly value="${prpdIntermBank.bankNumber}" />
				</div>
				<span class="c-red col-1">*</span>
			</div>
			
			<div class="row cl">
			  <label class="form_label col-3"><font class="c-red">*</font>摘要：</label>
				<div class="form_input col-5">
				 <input type="text" datatype="*1-1000"  class="input-text" value="${prpdIntermBank.remark}" name="prpdIntermBankVo.remark" onchange="checkSpecialCharactorOfRemark(this)"  id="remark"/>
					
				</div>
			</div>
			
		</div>
		</br>
		 <div class="btn-footer clearfix text-c" id="submitDiv">
		    <a class="btn btn-primary " id="MoneysaveBtn" onclick="save('0')" style="display: none;">资金系统支付</a>
		    &nbsp;&nbsp;&nbsp;&nbsp;
		    <a class="btn btn-primary " id="PaysaveBtn" onclick="save('1')" style="display: none;">收付系统支付</a>
			<a class="btn btn-primary " id="saveBtn" onclick="save('2')" style="display: none;">确定</a> 
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
<script type="text/javascript" src="${ctx}/js/base/assessorFeeBackTicketBankEdit.js"></script>
</body>
</html>









