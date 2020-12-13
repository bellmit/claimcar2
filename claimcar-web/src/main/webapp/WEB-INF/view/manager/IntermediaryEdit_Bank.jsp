<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head><title>
银行信息页面
</title></head>
<body>
<!-- 银行信息  开始 -->
<div class="table_wrap">
<form id="saveBankInfo"  >
	<div class="formtable">
		<div class="formtable mt-10">
			<div class="row cl">
				<input type="hidden" name="prpdIntermBankVo.Id" value="${prpdIntermBank.id}" id="bankVoId"/>
				<input type="hidden" name="prpdIntermBankVo.vaildFlag" value="1" id="vaildFlag"/>
				
				
				<label class="form_label col-3">收款方户名：</label> 
				<div class="form_input col-2">
					<input type="text" class="input-text" name="prpdIntermBankVo.AccountName" value="${prpdIntermBank.accountName}"  id="accountname" onchange="checkSpecialCharactor(this)" />
				</div>
				<span class="c-red col-1">*</span> 
				<label class="form_label col-2">收款方账号：</label>
				<div class="form_input col-2 ">
					<input type="text" class="input-text"
						name="prpdIntermBankVo.AccountNo" value="${prpdIntermBank.accountNo}"  id="account" onchange="checkSpecialByAccountNo(this)" />
				</div>
				<span class="c-red col-1">*</span>
			</div>

			<div class="row cl">
				<label class="form_label col-3">收款方组织机构代码：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text"
						name="prpdIntermBankVo.CertifyNo" value="${prpdIntermBank.certifyNo}" id="certifyNo" onchange="checkCertifyNo()"/>
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
					<input type="hidden" id="Province" name="prpdIntermBankVo.Province" value="${prpdIntermBank.province}" />
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
						onClick="layerShowBankNum()">检索行号</a>
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
		</div>
		 <div class="btn-footer clearfix text-c" id="submitDiv">
			<a class="btn btn-primary " id="saveBtn" onclick="layerHiddenBank()">确定</a> 
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
 var index2 = null;
 // 检索行号
 
 $(function(){
	var flag = parent.$("#flag11").val();
	$("#hideFlag").val(flag);
	if(flag != null && flag == "2"){   //检查并置灰页面   
		$("input").attr("disabled", "disabled");
		$("select").attr("disabled","disabled");
		$("#submitDiv").hide();
	}
	$("#accountname").val(parent.$("#IntermBankAccountName").val());
	$("#account").val(parent.$("#IntermBankAccountNo").val());
	$("#accountcellphone").val(parent.$("#Mobile").val());
	$("#intermBankProvCity").val(parent.$("#City").val());
	$("#BankName").val(parent.$("#BankCodeA").val());
	$("#BankName option:selected").text(parent.$("#IntermBankName").val());
	$("#BankOutlets").val(parent.$("#BankOutlets").val());
	$("#BankNumber").val(parent.$("#BankNumber").val());
	$("#certifyNo").val(parent.$("#certifyNo").val());

 });
 function layerShowBankNum() {//弹出检索行号页面

		Prov = $("#intermBankProvCity_lv1  option:selected").text();
		City = $("#intermBankProvCity_lv2  option:selected").text();
		Bname = $("#BankName  option:selected").text();
		BQuery = $("#BankQueryText").val();
		$("#Province").val($("#intermBankProvCity_lv1  option:selected").val());
		$("#City").val($("#intermBankProvCity_lv2  option:selected").val());

		if (Prov == "" || Bname == "" || City == "") {
			layer.msg("请完善开户省市及银行信息！");

		} else {
			url = "/claimcar/manager/findBankNoQueryList.do";
			if (index2 == null) {
				index2 = layer.open({
					type : 2,
					title : '检索行号',
					shade : false,
					area: ['900px', '350px'],
					content : url,
					end : function() {
						index2 = null;
					}
				});
			}
		}
	};

	
function layerCancelBank(){//点击取消关闭银行信息界面并清空输入信息
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		    parent.layer.close(index); //再执行关闭           	
	}
function layerHiddenBank() {//银行信息界面点击“保存”关闭	
	var accountname = $("#accountname").val();
	var account = $("#account").val();
	var accountcellphone = $("#accountcellphone").val();
	var certifyNo = $("#certifyNo").val();
	var patten = /^[1][358][0-9]{9}$/;
	if(!checkSpecialChar(accountname)){
		return false;
	}
	if(!checkSpecialByAccount(account)){
		return false;
	}
	if (accountname == null || accountname == "") {
		layer.alert("收款方户名不能为空");
		return false;
	} else if (account == null || account == "") { 
		layer.alert("收款方账号不能为空");
		return false;
	} else if (accountcellphone == null || accountcellphone == "") {
		layer.alert("手机号码不能为空");
		return false;
	} else if (!patten.test(accountcellphone)) {
		layer.alert("手机号码格式输入不正确");
		return false;
	} else if(certifyNo == null || certifyNo == ""){
		layer.alert("收款方组织机构代码不能为空");
		return false;
	} else if(!checkCertifyNo()){
		return false;
	}else {
		if ( $("#BankOutlets").val() == null|| $("#BankOutlets").val() == ""
			|| $("#BankNumber").val() == null|| $("#BankNumber").val() == "") {
			alert("请完善开户信息及银行网点信息！");
			return false;
		} else {
			// 子页面数据写入父页面
			parent.$("#bankId").val($("#bankVoId").val());
			parent.$("#IntermBankAccountName").val($("#accountname").val());
			parent.$("#IntermBankAccountNo").val($("#account").val());
			parent.$("#Mobile").val($("#accountcellphone").val());
			parent.$("#City").val($("#intermBankProvCity").val());
			parent.$("#Provincep").val($("#Province").val());
			parent.$("#BankCodeA").val($("#BankName").val());
			parent.$("#IntermBankName").val($("#BankName option:selected").text());
			parent.$("#BankOutlets").val($("#BankOutlets").val());
			parent.$("#BankNumber").val($("#BankNumber").val());
			parent.$("#certifyNo").val($("#certifyNo").val());
			parent.$("#vaildFlag").val($("#vaildFlag").val());

			// 加判断
			if(parent.$("#IntermBankAccountName").val() != null && parent.$("#IntermBankAccountNo").val() != ""
					&& parent.$("#IntermBankName").val() != null){
				layer.msg("保存成功！");
				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
				parent.layer.close(index);
				//parent.location.reload();
			}else{
				layer.msg("保存失败！");
			}
			
	
			
	  }
}
	$("#IntermBankName").click(function() {
		layer.msg("请点击银行信息按钮设置");      
	});


function layerShowBankNum(title, id) {//弹出检索行号页面

	Prov = $("#intermBankProvCity_lv1  option:selected").text();
	City = $("#intermBankProvCity_lv2  option:selected").text();
	Bname = $("#BankName  option:selected").text();
	BQuery = $("#BankQueryText").val();
	$("#Province").val(
			$("#intermBankProvCity_lv1  option:selected").val());
	$("#City").val($("#intermBankProvCity_lv2  option:selected").val());

	if (Prov == "" || Bname == "" || City == "") {
		layer.msg("请完善开户省市及银行信息！");

	} else {
		document.getElementById("NumProv").innerText = Prov;
		document.getElementById("NumCity").innerText = City;
		document.getElementById("NumBankName").innerText = Bname;
		$("#NumBankQueryT").val(BQuery);
		index2 = layer.open({
			type : 1,
			title : '检索行号',
			area : [ '900px', '350px' ],
			fix : false, //不固定
			scrollbar : false,
			maxmin : false,
			content : $("#" + id)
		});
	}
};
	}
	
/**
 * 
 * @param str
 * @returns {Boolean}
 */
function checkSpecialCharactor(element){
	var str = $(element).val();
	//var pattern = "^‘'！!#$%*+，,-./:：=？?@【】[\]_`·{|}~ ";//特殊字符
	var pattern = "^'!#$%*+,-/:=?@[\]_`{|}~ ……‘！#￥%*+，-。/：；=？@【、】——{|}~ ";//特殊字符
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
 
 /**
  * 
  * @param str
  * @returns {Boolean}
  */
 function checkSpecialChar(str){
 	//var pattern = "^‘'！!#$%*+，,-./:：=？?@【】[\]_`·{|}~ ";//特殊字符
 	var pattern = "^'!#$%*+,-/:=?@[\]_`{|}~ ……‘！#￥%*+，-。/：；=？@【、】——{|}~ ";//特殊字符
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
 			alert("包含特殊字符“"+reg+"”,请核实！");
 			return false;
 		}
 	}
 	return true;
 }
  
  function checkSpecialByAccount(str){
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

  function cleanBankNum(){//切换归属地或银行后，清空原来的行号信息，要求重新检索
		var text = "";
		$("#BankNumber").val(text);
		$("#BankOutlets").val(text);
		$("#BankQueryText").val(text);
		
	}

	$("#intermBankProvCity_lv1").change(function(){
		cleanBankNum();
	});
	$("#intermBankProvCity_lv2").change(function(){
		cleanBankNum();
	});
	$("#BankName").change(function(){
		cleanBankNum();
	});
	
	function checkCertifyNo(){
		 var certifyNo = $("[name='prpdIntermBankVo.CertifyNo']").val();
		 var regs = new RegExp("^(.{9}|.{18})$");
		 //证件类型为组织机构代码时，证件号码只能是9位或者18位
		 if(!regs.test(certifyNo)){
			 layer.alert("请录入正确的9位或18位组织机构代码!"); 
			 return false;
		 }
		 return true;
	 }
</script>
</body>
</html>









