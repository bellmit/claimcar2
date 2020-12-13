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
	<input type="hidden" name="registNo" id="registNo" value="${registNo}"/>
	<input type="hidden" name="nodeCode" id="nodeCode" value="${nodeCode}"/>
	<input type="hidden" name="viewFlag" id="viewFlag" value="${viewFlag}"/>
	<input type="hidden"  id="profession" value="${prpLPayCustom.profession}"/>
	<input type="hidden"  id="adress" value="${prpLPayCustom.adress}"/>
	<input type="hidden"  id="verifySign" value="${sign}"/>
	<input type="hidden"  id="certiNotModiPay" value="${certiNotModiPay}"/>
	<input type="hidden"  id="reportorPhone" value="${reportorPhone}"/>
	<input type="hidden"  id="flags" value="${flag}"/>
	<input type="hidden"  id="payType" value="${prpLPayCustom.payObjectKind}"/>
	<input type="hidden"  id="payMobile" value="${prpLPayCustom.payeeMobile }" />
	<c:if test="${frostFlags eq '1'}">
		<form id="PayCustomEditFormFrost" role="form" method="post">
	</c:if>
	<c:if test="${frostFlags ne '1'}">
		<form id="PayCustomEditForm" role="form" method="post">
	</c:if>
	<div class="formtable" id="mainInfo">
		<div class="formtable mt-10">
			<div class="row cl">
				<!-- 隐藏域  -->
				<input type="hidden" name="flag" value="${flag}"/>
				<input type="hidden"  id="frostFlag" name="prpLPayCustomVo.frostFlag" value="${prpLPayCustom.frostFlag }" />
				<input type="hidden"  id="frostFlags" name="frostFlags" value="${frostFlags }"/>
				<input type="hidden"  id="payCustomFrostFlags" name="payCustomFrostFlags" value="${payCustomFrostFlags }"/>
				
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
					<input type="hidden" name="prpLPayCustomVo.registNo" value="${registNo}"/>
					<!-- 下拉框  数据字典  个人、机构-->
					<app:codeSelect codeType="PayObjectType" name="prpLPayCustomVo.payObjectType" value="${prpLPayCustom.payObjectType}" id="PayObjectType" type="select" clazz="must"/>
				</c:if>
				</div>
			
				<%--反洗钱 --%>
				<c:if test="${flag eq 'N' || flag eq 'S' }">
					<label class="form_label col-3">客户识别：</label>
					<div class="form_input col-2">
						<app:amlReg userCode="${userVo.userCode }"  comCode="${userVo.comCode }" bussNo="${registNo }" 
						bussType="C" recTime="C" btnName="certifyNo" btnID="appli"/>
					</div>
				</c:if>
			</div>
			<div class="row cl">
			 <label class="form_label col-3"><font class="c-red">*</font>证件类型：</label>
				<div class="form_input col-2">
				<app:codeSelect codeType="CertifyType" name="prpLPayCustomVo.certifyType" value="${prpLPayCustom.certifyType}" type="select" clazz="must"/>	
				</div>
			
			  <label class="form_label col-3"><font class="c-red">*</font>证件号码：</label>
				<div class="form_input col-2">
				<!-- 根据收款人性质自动带出   身份证或机构码  不可编辑-->
					<input type="text" class="input-text" name="prpLPayCustomVo.certifyNo" id="certifyNo" value="${prpLPayCustom.certifyNo }" 
					datatype="idcard" errormsg="请填写证件号码！" nullmsg="请填写证件号码！" maxlength="20" onchange="changeCertifyNo(this)"/>
				</div>
			</div>
			<div class="row cl">
			<c:if test="${flag eq 'Y' }">
				<input type="hidden" name="prpLPayCustomVo.flag" value="1" />
				</c:if>
				<c:if test="${flag ne 'Y' }">
				<input type="hidden" name="prpLPayCustomVo.flag" value="0" />
				</c:if>
				<!-- 需要补充反洗钱信息的时候获取已存在数据需要id字段 -->
				<input type="hidden" name="prpLPayCustomVo.id" value="${prpLPayCustom.id}"/>
				<input type="hidden" name="prpLFxqFavoreeVo.compensateNo" id="compensateNo" value="" />
				<c:set var="createTime">
					<fmt:formatDate value="${prpLPayCustom.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</c:set>
				<input type="hidden" name="prpLPayCustomVo.createUser" value="${prpLPayCustom.createUser}" />
				<input type="hidden"  name="prpLPayCustomVo.createTime" value="${createTime}" />
				<!-- 隐藏域  结束-->
				
				<label class="form_label col-3"><font class="c-red">*</font>收款人类型：</label>
				<c:if test="${flag eq 'S' }">
					<!-- 此时固定收款人类型为被保险人 -->
					<div class="form_input col-2">
						<input type="text" class="input-text" name="PayObjectKind" value="被保险人" readonly />
						<input type="hidden" name="prpLPayCustomVo.payObjectKind" value="2" />
					</div>
				</c:if>
				<c:if test="${flag eq 'Y' }">
					<div class="form_input col-2">
					<!-- 下拉框  数据字典 PayObjectKind 当数据类型为被保险人时，联动带出承保人信息-->
					<app:codeSelect codeType="PayObjectKind" name="prpLPayCustomVo.payObjectKind" value="${prpLPayCustom.payObjectKind}"
					 type="select" id="payObjectKind" clazz="must" onchange="objSelect(this);payObjectKindChange()"/>
					 </div>
					<div id="repairfactoryName">
					 <label class="form_label col-3">修理厂名称：</label>
					 <div class="form_input col-2">
							<app:codeSelect codeType="" type="select" id="intermCode"
											name="prpLPayCustomVo.repairFactoryCode" lableType="code-name"
											value="${prpLPayCustom.repairFactoryCode}" dataSource="${dictMap}"
											onchange="repairFactory()" style="width:170px"/>
					</div>
					</div>
				</c:if>
				<c:if test="${flag eq 'N'}">
					<div class="form_input col-2">
					<!-- 下拉框  数据字典 PayObjectKind 当数据类型为被保险人时，联动带出承保人信息-->
						<app:codeSelect codeType="PayObjectKind" name="prpLPayCustomVo.payObjectKind"
										value="${prpLPayCustom.payObjectKind}"
										type="select" id="payObjectKind" clazz="must"
										onchange="objSelect(this);payObjectKindChange()"/>
					 </div>
					 <div id="repairfactoryName">
					 <label class="form_label col-3">修理厂名称：</label>
					 <div class="form_input col-2">
							<app:codeSelect codeType="" type="select" id="intermCode"
											name="prpLPayCustomVo.repairFactoryCode" lableType="code-name"
											value="${prpLPayCustom.repairFactoryCode}" dataSource="${dictMap}"
											onchange="repairFactory()" style="width:170px"/>
					</div>
					</div>
			</c:if>
				</div>
				<div class="row cl" >
				    <label class="form_label col-3">例外原因：</label>
				    <div class="form_input col-2">
						<app:codeSelect codeType="OtherCase" type="select" id="otherCause" name="prpLPayCustomVo.otherCause" value="${prpLPayCustom.otherCause }"/>
						<input type="hidden" id="otherCauseTemp" name="prpLPayCustomVo.otherCause" value="" disabled="disabled"/>
					</div>
				</div>
				<div class="row cl">
                <label class="form_label col-3"><font class="c-red">*</font>收款人户名：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text" name="prpLPayCustomVo.payeeName" id="payeeName" value="${prpLPayCustom.payeeName }" onkeyup="this.value=this.value.replace(/[, ]/g,'')" datatype="*"  onchange="checkSpecialCharactor(this,'payeeName')"/>
				</div>

				<label class="form_label col-3"><font class="c-red">*</font>收款人账号:</label>
				<div class="form_input col-2 ">
					<input type="text" class="input-text" name="prpLPayCustomVo.accountNo" id="accountNo"
						   nullmsg="请填写收款人账号！" value="${prpLPayCustom.accountNo }" datatype="*"
						   onchange="checkSpecialByAccountNo(this)"/>
				</div>
			</div>
				<div class="row cl">
				  <label class="form_label col-3"><font class="c-red">*</font>公私标志：</label>
				   <div class="form_input col-2">
					<app:codeSelect codeType="publicAndPrivate" name="prpLPayCustomVo.publicAndPrivate"
									value="${prpLPayCustom.publicAndPrivate}" id="publicAndPrivate" type="select"
									clazz="must"/>
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
						<app:codeSelect codeType="" type="select" id="BankName" name="prpLPayCustomVo.bankName" lableType="name" value="${prpLPayCustom.bankName}" dataSource="${bankCodeMap}" />	<!-- style="width:170px" --> 
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
				<input type="text" class="input-text" name="prpLPayCustomVo.payeeMobile" value="${prpLPayCustom.payeeMobile }" errormsg="请填写正确的手机号码！" datatype="m|/^0\d{2,3}-\d{7,8}$/" />
			</div>
		</div>
		
		<div class="row cl">
			<label class="form_label col-3">用途：</label>
			<div class="form_input col-5">
				<input type="text" class="input-text" name="prpLPayCustomVo.purpose" value="${prpLPayCustom.purpose }" datatype="*0-80" />
				<input type="hidden" name="prpLPayCustomVo.summary"  value="${prpLPayCustom.summary}"/>
			</div>
		</div>
		<div class="row cl">
			<c:if test="${payCustomFrostFlags ne '0' && frostFlags eq '1'}">
				<div class="row cl text-c" id="frostReason">
								<textarea class="textarea h100 hidden" id="reason" 
									name="prpLPayCustomVo.reason" datatype="*1-2000" nullmsg="请输入说明！"  datatype="*" 
									placeholder="请输入...">${prpLPayCustom.reason }</textarea>
				</div>
			</c:if>
		</div>
	</div>
	<div class="formtable" id="AML">
	</div>
		<c:if test="${viewFlag ne '1'}">
		<div class="btn-footer clearfix text-c">
			<c:if test="${frostFlags eq '1'}">
				<a class="btn btn-primary ml-5" type="button" id="isFrost" onclick="payCustomFrost('1')">冻结</a>
			</c:if>
			<c:if test="${frostFlags ne '1'}">
				<a class="btn btn-primary ml-5" onclick="getCustRiskInfo()" style="width:60px;height:32px">确定</a>
				<a class="btn btn-primary ml-5" id="cancelBtn" onclick="closeLayer()">取消</a>
			</c:if>
		</div>
		</c:if>
</form>	
</div>


<!-- 银行信息  结束 -->
<script type="text/javascript" src="${ctx }/js/flow/payCustomEdit.js"></script>
<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
<script type="text/javascript">
//异步请求校验$("input[name='prpLDlossPersTraceVo.endFlag']:checked").val() == '1'
function objSelect(element){
	var value = $("option:selected",element).val();
	if(value !='6'){
		$("#repairfactoryName").hide();
		$("#payeeName").removeAttr("readonly");
		$("#accountNo").removeAttr("readonly");
		$("#payeeName").val("");
		$("#accountNo").val("");
	}else{
		$("#repairfactoryName").show();
		$("#payeeName").attr("readonly","readonly");
		$("#accountNo").attr("readonly","readonly");
		repairFactory();
		}
	//收款人维护时若客户为被保险人时，联系电话默认带出报案环节的报案人联系电话，可修改。
	var flags = $("#flags").val();
	if(flags=='N'){
		 var payObjectKind = $("[name='prpLPayCustomVo.payObjectKind']").val();//这次操作类型
		 var payType = $("#payType").val();//前一次操作的类型
		 if(payObjectKind==2){//若收款人维护页面，收款人类型为被保险人时，收款人手机号码自动带入报案页面报案人电话，可修改；
			 var reportorPhone = $("#reportorPhone").val();
			 $("[name='prpLPayCustomVo.payeeMobile']").val(reportorPhone); 
		 }else{
			 if(payType==2){//收款人类型若由被保险人换成非被保险人，则清空收款人手机号码
				 $("[name='prpLPayCustomVo.payeeMobile']").val("");
			 }
		 }
		 $("#payType").val(payObjectKind);
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


/**
 * 
 * @param str
 * @returns {Boolean}
 */
function checkSpecialCharactor(element,flag){
	var pattern;
	if(flag=="payeeName"){
		pattern = "^'!#$%*+,/:=?@[\]_`{|}~ ……‘！#￥%*+，。/：；=？@【】——{|}~ ";//特殊字符
	}else{
		pattern = "^'!#$%*+,./:=?@[\]_`{|}~ ……‘！#￥%*+，-。/：；=？@【】——{|}~ ";//特殊字符
	}
	var str = $(element).val();
	//var pattern = "^‘'！!#$%*+，,-./:：=？?@【】[\]_`·{|}~ ";//特殊字符
	
	if(typeof(str) === "undefined" || str === null || str===""){
		return false;
	}
	for(var i = 0,size = pattern.length;i<size;i++){
		var reg = pattern.substring(i, i+1);
		if(str.indexOf(reg)>-1){
			layer.alert("包含特殊字符“"+reg+"”,请核实！");
			return false;
		}
	}
	//对于&quot和&lt和&gt不能同时出现
	var regArr = new Array();
	regArr.push("&quot");
	regArr.push("&lt");
	regArr.push("&gt");
	for(var i = 0,size = regArr.length;i<size;i++){
		var reg = regArr[i];
		if(str.toLowerCase().indexOf(reg)>-1){
			//进行转义提示
			//reg = "\\"+reg;
			alert("包含特殊字符“"+reg+"”,请核实！");
			return false;
		}
	}
	return true;
}

 function checkSpecialByAccountNo(element){
	 	var str = $(element).val();
	 	if(typeof(str) === "undefined" || str === null || str===""){
			return false;
		}
	 	var regs = new RegExp("-{2,}");
		if(regs.test(str)){  
	        layer.alert("收款人账号只能输入数字和-，且-不能连续输入!"); 
	        return false;
	    }  
	 	//str = str.replace(/-/g,"");
	 	str = str.replace(new RegExp('-','gi'),'');
		var reg = new RegExp("^[0-9]*$");
		if(!reg.test(str)){  
	        layer.alert("收款人账号只能输入数字和-，且-不能连续输入!");  
	        return false;
	    } 
		return true;
	}
 
 function changeCertifyNo(obj){
	 toUpperCase(obj);
	 checkCertifyNo();
 }
 
 function checkCertifyNo(){
	 var certifyNo = $("[name='prpLPayCustomVo.certifyNo']").val();
	 var certifyType = $("[name='prpLPayCustomVo.certifyType']").val();
	 var regs = new RegExp("^(.{9}|.{18})$");
	 //证件类型为组织机构代码时，证件号码只能是9位或者18位
	 if(certifyType == "10"){
		 if(!regs.test(certifyNo)){
			 layer.alert("请录入正确的9位或18位组织机构代码!"); 
			 return false;
		 }
	 }
	 return true;
 }
 
  $(function (){
	//收款人维护时若客户为被保险人时，联系电话默认带出报案环节的报案人联系电话，可修改。
	 var payObjectKind = $("[name='prpLPayCustomVo.payObjectKind']").val();
	 if(payObjectKind==2){
		 var reportorPhone = $("#reportorPhone").val();
		 var payMobile = $("#payMobile").val();
		 if(payMobile==""){
			 $("[name='prpLPayCustomVo.payeeMobile']").val(reportorPhone); 
		 }
	 }
	 
	 //反洗钱
	 var frostFlags = $("#frostFlags").val();
	 if(frostFlags=="1" || frostFlags=="0"){
		$("input").attr("readonly","readonly");
		$("select").attr("disabled","disabled"); 
		$("#mainInfo a").removeAttr('onclick');
		$("input[type='radio']").prop("disabled",true);
	 }
	 var payCustomFrostFlags = $("#payCustomFrostFlags").val();
	 if(payCustomFrostFlags=="1"){
		 $("#isFrost").removeAttr('onclick');
		 $("#isFrost").removeClass("btn btn-primary ml-5");
		 $("#isFrost").addClass("btn btn-disabled ml-5");
	 }
	 if(payCustomFrostFlags=="0"){
		 $("#noFrost").removeAttr('onclick');
		 $("#noFrost").removeClass("btn btn-primary ml-5");
		 $("#noFrost").addClass("btn btn-disabled ml-5");
	 }
	 
	 if(payCustomFrostFlags!="0" && frostFlags=="1"){
		 $("#reason").prop("readonly",false);
	 }
	 $("#appli").addClass("btn  btn-primary"); 
	});
</script>

</body>
</html>




