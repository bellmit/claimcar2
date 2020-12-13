<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<html>
<head>
<title>收款人信息</title>

</head>
<body>
<!-- 收款人信息维护  开始 -->
<div class="table_wrap table_cont">
	<!-- 隐藏域 用于判断不提交 -->
	<input type="hidden" name="insuredName"/>
	<input type="hidden" name="flag" value="${flag}"/>
	<input type="hidden" name="registNo" id="registNo" value="${registNo}"/>
	<input type="hidden" name="nodeCode" id="nodeCode" value="${nodeCode}"/>
<form id="PayCustomEditForm" role="form" method="post">
	<input type="hidden"  name="payBankVo.id"  value="${payBankVo.id }" />
	<input type="hidden"  name="payBankVo.compensateNo"  value="${payBankVo.compensateNo }" />
	<input type="hidden"  name="payBankVo.policyNo"  value="${payBankVo.policyNo }" />
	<input type="hidden"  name="payBankVo.registNo"  value="${payBankVo.registNo }" />
	<input type="hidden"  name="payBankVo.claimNo"  value="${payBankVo.claimNo }" />
	<input type="hidden"  name="payBankVo.lossType"  value="${payBankVo.lossType }" />
	<input type="hidden"  name="payBankVo.payeeId"  value="${payBankVo.payeeId }" />
	<input type="hidden"  name="payBankVo.payType"  value="${payBankVo.payType }" />
	<input type="hidden"  name="payBankVo.chargeCode"  value="${payBankVo.chargeCode }" />
	<%--<input type="hidden"  name="originalAccountNo"  value="${originalAccountNo}" />--%>
	<input type="hidden"  name="payBankVo.insuredName"  value="${payBankVo.insuredName}" />
	<input type="hidden"  name="payBankVo.verifyHandle"  value="${payBankVo.verifyHandle}" />
	<input type="hidden"  name="payBankVo.isAutoPay"  value="${payBankVo.isAutoPay}" />
	<input type="hidden"  name="payBankVo.errorType" value="${payBankVo.errorType }" />
	<input type="hidden"  name="payBankVo.errorMessage" value="${payBankVo.errorMessage }" />
	<input type="hidden"  name="payRefReason" value="${payRefReason }" />
	<input type="hidden"  name="payBankVo.serialNo" value="${payBankVo.serialNo }" />
	<div class="formtable" id="mainInfo">
		<div class="formtable mt-10">
			<div class="row cl">
				<!-- 隐藏域  -->
				<!-- 计算书号  账号信息修改时使用 -->
				<input type="hidden" name="compensateNo" value="${compensateNo }" />
				
				<c:if test="${flag eq 'Y' }">
				<input type="hidden" name="prpLPayCustomVo.flag" value="1" />
				</c:if>
				<c:if test="${flag ne 'Y' }">
				<input type="hidden" name="prpLPayCustomVo.flag" value="0" />
				</c:if>
				<!-- 需要补充反洗钱信息的时候获取已存在数据需要id字段 -->
				<input type="hidden" name="prpLPayCustomVo.id" value="${prpLPayCustom.id}"/>
				<input type="hidden" name="prpLPayCustomVo.registNo" value="${registNo}"/>
				<c:set var="createTime">
					<fmt:formatDate value="${prpLPayCustom.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</c:set>
				<input type="hidden" name="prpLPayCustomVo.createUser" value="${prpLPayCustom.createUser}" />
				<input type="hidden" name="prpLPayCustomVo.createTime" value="${createTime}" />
				<!-- 隐藏域  结束-->
				<label class="form_label col-3"><font class="c-red">*</font>收款人性质:</label>
				<div class="form_input col-2">
				<c:if test="${flag eq 'Y' }">
					<!-- 补充反洗钱信息时只需要显示不需修改 -->
					<c:set var="payObjectType">
						<app:codetrans codeType="PayObjectType" codeCode="${prpLPayCustom.payObjectType}"/>
					</c:set>
					<input type="text" class="input-text" value="${payObjectType}" readonly />
					<input type="hidden" name="prpLPayCustomVo.payObjectType" id="PayObjectType" value="${prpLPayCustom.payObjectType}" />
					
				</c:if>
				<c:if test="${flag eq 'N' || flag eq 'S' }">
					<!-- 下拉框  数据字典  个人、机构-->
					<app:codeSelect codeType="PayObjectType" name="prpLPayCustomVo.payObjectType" value="${prpLPayCustom.payObjectType}" id="PayObjectType" type="select" clazz="must"/>
				</c:if>
				</div>
			</div>

			<div class="row cl">
				<label class="form_label col-3"><font class="c-red">*</font>证件类型：</label>
				<div class="form_input col-2">
				<app:codeSelect codeType="CertifyType" name="prpLPayCustomVo.certifyType" value="${prpLPayCustom.certifyType}" type="select" clazz="must"/>	
				</div>
				<label class="form_label col-3"><font class="c-red">*</font>证件号码：</label>
				<div class="form_input col-2">
				<!-- 根据收款人性质自动带出   身份证或机构码  不可编辑-->
					<input type="text" class="input-text" name="prpLPayCustomVo.certifyNo" value="${prpLPayCustom.certifyNo }" datatype="idcard" 
					errormsg="请填写证件号码！" nullmsg="请填写证件号码！" maxlength="20" onchange="changeCertifyNo(this)"/>
				</div>
			</div>
			
			<div class="row cl">
				<label class="form_label col-3"><font class="c-red">*</font>收款人类型：</label>
				<div class="form_input col-2">
					<%-- <app:codeSelect codeType="PayObjectKind" id="payObjectKind" onchange="setPayObjectKind()" value="${prpLPayCustom.payObjectKind}" type="select" clazz="must"/>
					<input type="hidden" name="prpLPayCustomVo.payObjectKind" value="${prpLPayCustom.payObjectKind}" /> --%>
					<app:codeSelect codeType="PayObjectKind" name="prpLPayCustomVo.payObjectKind" value="${prpLPayCustom.payObjectKind}"
					 type="select" id="payObjectKind" clazz="must" onchange="objSelect(this)"/>
				</div>
				<div id="repairfactoryName">
					 <label class="form_label col-3">修理厂名称：</label>
					 <div class="form_input col-2">
					<app:codeSelect codeType="" type="select" id="intermCode" name="prpLPayCustomVo.repairFactoryCode" lableType="code-name" value="${prpLPayCustom.repairFactoryCode}" dataSource="${dictMap}" onchange="repairFactory()" style="width:170px"/>
					</div>
					
				</div>
			</div>

			<div class="row cl">
				<label class="form_label col-3"><font class="c-red">*</font>收款人户名：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text" name="prpLPayCustomVo.payeeName" id="payeeName" value="${prpLPayCustom.payeeName }" datatype="*" onchange="checkSpecialCharactor(this,'payeeName')" />
				</div>
				<label class="form_label col-3"><font class="c-red">*</font>收款人账号：</label>
				<div class="form_input col-2 ml-5">
					<input type="text" class="input-text" name="prpLPayCustomVo.accountNo" id="accountNo" nullmsg="请填写收款人账号" value="${prpLPayCustom.accountNo }" datatype="*" onchange="checkSpecialByAccountNo(this)" />
				</div>
			</div>
			<div class="row cl">
			    <label class="form_label col-3"><font class="c-red">*</font>公私标志：</label>
				   <div class="form_input col-2">
					  <app:codeSelect codeType="publicAndPrivate" name="prpLPayCustomVo.publicAndPrivate"  value="${prpLPayCustom.publicAndPrivate}" id="publicAndPrivate" type="select" clazz="must" />
				 	</div>
			</div>
		</div>
		<p>
		<div class="line"></div>
		<p>
		<div class="formtable mt-5">
			<div class="row cl">
				<label class="form_label col-3"><font class="c-red">*</font>收款人开户行归属地：</label>
					<c:if test="${flag eq 'Y' }">
					<div class="form_input col-2" style="text-align: left">
						<input type="hidden" id="Province" name="prpLPayCustomVo.provinceCode" value="${prpLPayCustom.provinceCode}" /> 
						<input type="text" class="input-text" id="ProvinceName" name="prpLPayCustomVo.province" value="${prpLPayCustom.province}" />
					</div>
					<div class="form_input col-3" style="text-align: left">
						<input type="hidden" id="City" name="prpLPayCustomVo.cityCode" value="${prpLPayCustom.cityCode}" />
						<input type="text" class="input-text" id="CityName" name="prpLPayCustomVo.city" value="${prpLPayCustom.city}" />
					</div>
					</c:if>
					<c:if test="${flag eq 'N' || flag eq 'S'}">
					<div class="form_label col-6" style="text-align: left">
						<%-- <app:areaSelect targetElmId="payCustomProvCity" showLevel="2" style="width:190px" areaCode="${prpLPayCustom.cityCode}"/> --%>
						<app:areaSelect targetElmId="payCustomProvCity" showLevel="2" style="width:190px" areaCode="${prpLPayCustom.cityCode}" isAreaCode="Y"/>
						<input type="hidden" id="payCustomProvCity" name="areaSelectCode" />
						<input type="hidden" id="Province" name="prpLPayCustomVo.provinceCode" value="${prpLPayCustom.provinceCode}" /> 
						<input type="hidden" id="ProvinceName" name="prpLPayCustomVo.province" value="${prpLPayCustom.province}" />
						<input type="hidden" id="City" name="prpLPayCustomVo.cityCode" value="${prpLPayCustom.cityCode}" />
						<input type="hidden" id="CityName" name="prpLPayCustomVo.city" value="${prpLPayCustom.city}" /> 
					</div>
					</c:if>
			</div> 

			<div class="row cl">
				<label class="form_label col-3"><font class="c-red">*</font>收款方开户行：</label>
				<c:if test="${flag eq 'Y' }">
					<c:set var="BankCode">
						<app:codetrans codeType="BankCode" codeCode="${prpLPayCustom.bankName}"/>
					</c:set>
					<div class="form_input col-2">
					<input type="text" class="input-text" value="${BankCode}" readonly />
					<input type="hidden" name="prpLPayCustomVo.bankName" value="${prpLPayCustom.bankName}" />
					</div>
					<div class="form_input col-3">
						<input type="text" class="input-text" id="BankQueryText" name="prpLPayCustomVo.bankOutlets" value="${prpLPayCustom.bankOutlets}" readonly/>
					</div>
				</c:if>
				<c:if test="${flag eq 'N' || flag eq 'S'}">
					<div class="form_input col-2">
						<span class="select-box" style="width: 100%"> 
						<%-- <app:codeSelect codeType="BankCode" name="prpLPayCustomVo.bankName" id="BankName" type="select"
								 value="${prpLPayCustom.bankName}" /> --%>
						<app:codeSelect codeType="" type="select" id="BankName" name="prpLPayCustomVo.bankName" lableType="name" value="${prpLPayCustom.bankName}" dataSource="${bankCodeMap}" />
						</span>
					</div>
					<div class="form_input col-3 ml-10">
						<input type="text" class="input-text" id="BankQueryText" name="prpLPayCustomVo.bankOutlets" value="${prpLPayCustom.bankOutlets}" readonly/>
					</div>
					<div class="form_input col-1 mb-10">
						<a class="btn btn-zd mr-30 fl" onclick="layerShowBankNum()">检索行号</a>
					</div>
				</c:if>
			</div>
		</div>
		<p>
		<div class="line"></div>
		<p>
		<div class="formtable">
			<div class="row cl">
				<label class="form_label col-3"><font class="c-red">*</font>收款人银行行号：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text" name="prpLPayCustomVo.bankNo"
						id="BankNumber" datatype="n" placeholder="请检索" readonly value="${prpLPayCustom.bankNo}" />
				</div>
				<label class="form_label col-3">收款人银行类型：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text" name="prpLPayCustomVo.bankType"
						id="BankOutlets" readonly value="${prpLPayCustom.bankType}" />
				</div>
			</div>
		</div>
		
		<div class="row cl">
			<label class="form_label col-3"><font class="c-red">*</font>转账汇款模式：</label>
			<c:if test="${flag eq 'N' || flag eq 'S'}">
				<div class="form_input col-3">
					<app:codeSelect codeType="PriorityFlag" type="radio" nullToVal="N" name="prpLPayCustomVo.priorityFlag"  
										value="${prpLPayCustom.priorityFlag}"/>
				</div>
			</c:if>
			<c:if test="${flag eq 'Y' }">
					<c:set var="PriorityFlag">
						<app:codetrans codeType="PriorityFlag" codeCode="${prpLPayCustom.priorityFlag}"/>
					</c:set>
					<div class="form_input col-2">
					<input type="text" class="input-text" value="${PriorityFlag}" readonly />
					<input type="hidden" name="prpLPayCustomVo.priorityFlag" value="${prpLPayCustom.priorityFlag}" />
					</div>
				</c:if>
		</div>
		
		<div class="row cl">
			<label class="form_label col-3"><font class="c-red">*</font>收款人手机号码：</label>
			<div class="form_input col-2">
				<input type="text" class="input-text" name="prpLPayCustomVo.payeeMobile" value="${prpLPayCustom.payeeMobile }" errormsg="请填写正确的手机号码" datatype="m|/^0\d{2,3}-\d{7,8}$/" />
			</div>
		</div>
		
		<div class="row cl">
			<label class="form_label col-3">用途：</label>
			<div class="form_input col-5">
				<input type="text" class="input-text" name="prpLPayCustomVo.purpose" value="${prpLPayCustom.purpose }"  datatype="*0-80" />
			</div>
		</div>
		<div class="row cl">
			<label class="form_label col-3">摘要：</label>
			<div class="form_input col-5">
				<input type="text" class="input-text" name="prpLPayCustomVo.summary" value="${prpLPayCustom.summary }" onchange="checkSpecialCharactor(this,'summary')" datatype="*" />
			</div>
		</div>
	</div>
	<div class="formtable" id="AML">
	</div>
		
		<div class="btn-footer clearfix text-c">
			<!-- <button class="btn btn-primary ml-5" type="submit" onclick="">确定</button> -->
			<input class="btn btn-primary btn-noagree" id="submit" onclick="save('submit')"  value="确定" style="width:60px;height:32px">
			<input class="btn btn-primary btn-noagree" id="bankroll" onclick="save('bankroll')"  value="资金系统支付" style="width:110px;height:32px">
			<input class="btn btn-primary btn-noagree" id="payment" onclick="save('payment')"  value="收付系统支付" style="width:110px;height:32px">
			<a class="btn btn-primary ml-5" id="cancelBtn" onclick="closeLayer()">取消</a>
		</div>
</form>	
</div>


	<!-- 银行信息  结束 -->
	<script type="text/javascript" src="${ctx }/js/flow/modPayCustomEdit.js"></script>
	<script type="text/javascript">
		/* $(function() {
			$("#payObjectKind").prop("disabled", "disabled");
			var payObjectKind = $("#payObjectKind").val();
			if (payObjectKind == '2') {
				$("input[name='prpLPayCustomVo.certifyNo']").attr("readonly","readonly");
				$("input[name='prpLPayCustomVo.payeeName']").attr("readonly","readonly");
			}
		}); */
		$(function(){
			//初始化时，如果被保险人为修理厂，则修理厂名称标签显示，否则隐藏；
			var index=$("select[id='payObjectKind']").find("option:selected").val();
			
			if(index!='6'){
				$("#repairfactoryName").hide();
				/* $("#payeeName").removeAttr("readonly");
				$("#accountNo").removeAttr("readonly"); */
				}else{
					/* $("#payeeName").attr("readonly","readonly");
					$("#accountNo").attr("readonly","readonly"); */
				}
			
		});
		function objSelect(element){
			
			var value = $("option:selected",element).val();
			if(value !='6'){
				$("#repairfactoryName").hide();
				/* $("#payeeName").removeAttr("readonly");
				$("#accountNo").removeAttr("readonly"); */
				$("#payeeName").val("");
				$("#accountNo").val("");
			}else{
				$("#repairfactoryName").show();
				/* $("#payeeName").attr("readonly","readonly");
				$("#accountNo").attr("readonly","readonly"); */
				repairFactory();
				}
		}
		function repairFactory(){
			
			var strText=$("select[id='intermCode']").find("option:selected").val();
			if(strText==''){
				$("#payeeName").val("");
				$("#accountNo").val("");
				return false;
			}else{
				var payName="";//收款人户名
				var payAccountNo="";//收款人账号StatusText
				$.ajax({
					url : "/claimcar/payCustom/repairfactoryInfo.do",
					type : "post",
					data : {"strText" :strText},
					dataType : "json",
					success : function(jsonData) {
						var result = eval(jsonData);
					     
						if(result.data!='' && result.data!=null && result.statusText!='' && result.statusText!=null){
							payAccountNo=result.data;
							payName=result.statusText;
							$("#accountNo").val(payAccountNo);
							$("#payeeName").val(payName);
							
						}else{
							layer.msg('该修理厂未维护银行账号，请去修理厂维护页面维护!', {time:4000});
							};
						
						
						
					}
				});
			}
		}
	</script>

</body>
</html>




