<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>原银行信息查看</title>
</head>
<body>
<div class="table_wrap table_cont">
<form id="PayCustomEditForm" role="form" method="post">
	<div class="formtable" id="mainInfo">
		<div class="formtable mt-10">
			<div class="row cl">
				<!-- 隐藏域  -->
				<c:if test="${flag eq 'Y' }">
				<input type="hidden" name="prpLPayCustomVo.flag" value="1" />
				</c:if>
				<c:if test="${flag ne 'Y' }">
				<input type="hidden" name="prpLPayCustomVo.flag" value="0" />
				</c:if>
				<input type="hidden" name="prpLPayCustomVo.certifyType" value="${prpLPayCustom.certifyType}"/>
				<!-- 需要补充反洗钱信息的时候获取已存在数据需要id字段 -->
				<input type="hidden" name="prpLPayCustomVo.id" value="${prpLPayCustom.id}"/>
				<input type="hidden" name="prpLPayCustomVo.registNo" value="${registNo}"/>
				<c:set var="createTime">
					<fmt:formatDate value="${prpLPayCustom.createTime }" pattern="yyyy-mm-dd hh:mm:ss"/>
				</c:set>
				<input type="hidden" name="prpLPayCustomVo.createUser" value="${prpLPayCustom.createUser}" />
				<input type="hidden" name="prpLPayCustomVo.createTime" value="${createTime}" />
				<input type="hidden" name="prpLPayCustomVo.bankOutlets" value="${prpLPayCustom.bankOutlets}" />
				<!-- 隐藏域  结束-->
				
				
				<label class="form_label col-3"><font class="c-red">*</font>收款人性质：</label>
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
					<input type="text" class="input-text" name="prpLPayCustomVo.certifyNo" value="${prpLPayCustom.certifyNo }" datatype="idcard" />
				</div>
			</div>
			
			<div class="row cl">
				<label class="form_label col-3"><font class="c-red">*</font>收款人类型：</label>
				<c:if test="${flag eq 'S' }">
					<!-- 此时固定收款人类型为被保险人 -->
					<div class="form_input col-2">
						<input type="text" class="input-text" name="PayObjectKind" value="被保险人" readonly />
						<input type="hidden" name="prpLPayCustomVo.payObjectKind" value="2" />
					</div>
				</c:if>
				<c:if test="${flag eq 'Y' }">
					<!-- 补充反洗钱信息时只需要显示不需修改 -->
					<c:set var="payObjectKind">
						<app:codetrans codeType="PayObjectKind" codeCode="${prpLPayCustom.payObjectKind}"/>
					</c:set>
					<div class="form_input col-2">
						<input type="text" class="input-text" value="${payObjectKind}" readonly />
						<input type="hidden" name="prpLPayCustomVo.payObjectKind" value="${prpLPayCustom.payObjectKind}" />
					</div>
				</c:if>
				<c:if test="${flag eq 'N'}">
					<div class="form_input col-2">
					<!-- 下拉框  数据字典 PayObjectKind 当数据类型为被保险人时，联动带出承保人信息-->
					<app:codeSelect codeType="PayObjectKind" name="prpLPayCustomVo.payObjectKind" value="${prpLPayCustom.payObjectKind}" type="select" clazz="must"/>
					</div>
				</c:if>
				
				<c:if test="${prpLPayCustom.payObjectKind eq '6' }">
					<label class="form_label col-3">修理厂名称：</label>
					 <div class="form_input col-2">
					 	<input type="text" value="${factoryName}" readonly class="input-text"/>
					</div>
				</c:if>
			</div>


			<div class="row cl">
				<label class="form_label col-3"><font class="c-red">*</font>收款人户名：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text" name="prpLPayCustomVo.payeeName" value="${prpLPayCustom.payeeName }" datatype="*" />
				</div>
				<label class="form_label col-3"><font class="c-red">*</font>收款人账号：</label>
				<div class="form_input col-2 ml-5">
					<input type="text" class="input-text" name="prpLPayCustomVo.accountNo" nullmsg="请填写收款人账号" value="${prpLPayCustom.accountNo }" datatype="n" />
				</div>
			</div>
			
			<div class="row cl">
				<label class="form_label col-3"><font class="c-red">*</font>公私标志：</label>
				<div class="form_input col-2">
					<app:codeSelect codeType="publicAndPrivate" name="prpLPayCustomVo.publicAndPrivate" value="${prpLPayCustom.publicAndPrivate}" id="publicAndPrivate" type="select" clazz="must"/>
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
				</c:if>
				<c:if test="${flag eq 'N' || flag eq 'S'}">
					<div class="form_input col-2">
						<span class="select-box" style="width: 100%"> 
						 <%-- <app:codeSelect codeType="BankCode" name="prpLPayCustomVo.bankName" id="BankName" type="select"
								 value="${prpLPayCustom.bankName}" />  --%>
						 <app:codeSelect codeType="" type="select" id="BankName" name="prpLPayCustomVo.bankName" lableType="name" value="${prpLPayCustom.bankName}" dataSource="${bankCodeMap}" /> 
						</span>
					</div>
					<div class="form_input col-3 ml-10">
						<input type="text" class="input-text" id="BankQueryText" value="${prpLPayCustom.bankOutlets}"/>
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
				<input type="text" class="input-text" name="prpLPayCustomVo.purpose" value="${prpLPayCustom.purpose }"  />
			</div>
		</div>
		<div class="row cl">
			<label class="form_label col-3">摘要：</label>
			<div class="form_input col-5">
				<input type="text" class="input-text" name="prpLPayCustomVo.summary" value="${prpLPayCustom.summary }" readonly/>
			</div>
		</div>
	</div>
	<div class="formtable" id="AML">
	</div>
		
		<div class="btn-footer clearfix text-c">
			<a class="btn btn-primary ml-5" id="cancelBtn" onclick="closeLayer()">关闭</a>
		</div>
</form>	
</div>
<script type="text/javascript">
function closeLayer(){
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);	
}
$(function() {
	/* $("#payObjectKind").attr("disabled", "disbaled");
	$("#payObjectType").attr("disabled", "disbaled");
	$("input[name='cityCode']").attr("disabled", "disbaled");
	$("#bankName").attr("disabled", "disbaled");
	$("#priorityFlag").attr("disabled", "disbaled"); */
	$("select").prop("disabled", "disbaled");
	$("input").prop("disabled", "disbaled");
});
</script>
</body>
</html>
