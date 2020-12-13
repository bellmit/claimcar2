<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>银行账号信息维护</title>
</head>
<body>
<div class="table_wrap table_cont">
    <!-- 隐藏域 用于判断不提交 -->
    <input type="hidden" name="flag" value="${flag}"/>
    <div class="table_title f14">银行站点信息录入</div>
    <form id="PayBankEditForm" role="form" method="post">
	<div class="formtable" id="mainInfo">
		<div class="formtable mt-10">
			<div class="row cl">
			    <label class="form_label col-2">收款方户名【N】</label>
				<div class="form_input col-2">
				    <input type="text" class="input-text" name="prpLPayBank.accountName" value="${prpLPayBank.accountName" readonly datatype="*"/}>
				</div>
				<span class="c-red col-1">*</span>
			</div>
			
			<div class="row cl">
			    <label class="form_label col-2">收款方账号【C】</label>
				<div class="form_input col-2">
				    <input type="text" class="input-text" name="prpLPayBank.accountNo" value="${prpLPayBank.accountNo" datatype="*"/}>
				</div>
				<span class="c-red col-1">*</span>
			</div>
			
			<div class="row cl">
			    <label class="form_label col-2">收款方开户行归属地【A】</label>
				<div class="form_label col-6" style="text-align: left">
						<app:areaSelect targetElmId="payBankAreaCode" showLevel="2" style="width:190px" areaCode="${prpLPayBankVo.areaCode}"/>
						<input type="hidden" id="payBankAreaCode" name="prpLPayBankVo.areaCode" />
						
					</div>
				<span class="c-red col-1">*</span>
			</div>
			
			<div class="row cl">
				<label class="form_label col-3">收款方开户行【B】</label>
				
					<div class="form_input col-2">
						<span class="select-box" style="width: 100%"> 
						<app:codeSelect codeType="BankCode" name="prpLPayBankVo.bankName" id="BankName" type="select"
								 value="${prpLPayBank.bankName}" />
						</span>
					</div>
					<div class="form_input col-3 ml-10">
						<input type="text" class="input-text" id="BankQueryText" />
					</div>
					<span class="c-red col-1">*</span>
					<div class="form_input col-1">
						<a class="btn btn-zd mr-10 fl" onclick="layerShowBankNum()">检索行号</a>
					</div>
				
			</div>
			
			<div class="formtable">
			<div class="row cl">
				<label class="form_label col-3">收款人银行行号</label>
				<div class="form_input col-2">
					<input type="text" class="input-text" name="prpLPayBankVo.bankNo"
						id="BankNumber" datatype="n" placeholder="请检索" readonly value="${prpLPayBank.bankNo}" />
				</div>
				<span class="c-red col-1">*</span>
				<label class="form_label col-2">收款人银行类型</label>
				<div class="form_input col-2">
					<input type="text" class="input-text" name="prpLPayBankVo.bankType"
						id="BankOutlets" readonly value="${prpLPayBank.bankType}" />
				</div>
				<span class="c-red col-1"></span> 
			</div>
		</div>
		
		<div class="row cl">
			<label class="form_label col-3">转账汇款模式【Q】</label>
			
				<div class="form_input col-3">
					<app:codeSelect codeType="PriorityFlag" type="radio" nullToVal="N" name="prpLPayBankVo.priorityFlag"  
										value="${prpLPayBank.priorityFlag}"/>
					<font class="must">*</font>
				</div>
			
		</div>
		
		<div class="row cl">
			<label class="form_label col-3">收款人手机号码</label>
			<div class="form_input col-2">
				<input type="text" class="input-text" name="prpLPayBankVo.payeeMobile" value="${prpLPayBank.payeeMobile }" errormsg="请填写正确的手机号码" datatype="m" ignore="ignore"  />
			</div>
		</div>
		
		<div class="row cl">
			<label class="form_label col-3">用途</label>
			<div class="form_input col-5">
				<input type="text" class="input-text" name="prpLPayBankVo.purpose" value="${prpLPayBank.purpose }"  />
			</div>
		</div>
	</div>
	</div>
	<div class="formtable" id="AML"></div>
		
		<div class="btn-footer clearfix text-c">
			<a class="btn btn-primary ml-5" type="submit" onclick="payBankSubmit()">保存</a>
			<a class="btn btn-primary ml-5" id="cancelBtn" onclick="closeLayer()">取消</a>
	    </div>
 </form>
</div>
	<!-- 行号查询界面 -->
	<div class="hide" id="BankNumQ">
		<%@include file="PayBankEdit_BankNum.jspf"%>
	</div>
	
<script type="text/javascript">
var areaC,bname,bQuery;
var ajaxEdit;

$(function (){
	addAML();//判断是显示还是修改
	ajaxEdit = new AjaxEdit($('#PayBankEditForm'));
	ajaxEdit.targetUrl = "/claimcar/payBank/savePayBankInfo.do"; 
	ajaxEdit.afterSuccess = after;
	
	// 绑定表单
	ajaxEdit.bindForm();
				
});

function payBankSubmit(){
	$("#PayBankEditForm").submit();
}

function closeLayer(){
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);	
}

function layerShowBankNum(){//弹出检索行号页面
	
	areaC = $("#payBankAreaCode  option:selected").text();
	
	
	$("#NumArea").val(areaC);
	
	
	bname = $("#BankName  option:selected").text();
	bQuery = $("#BankQueryText").val();
			
	if(areaC==""||areaC=="undefined"){
		layer.msg("请选择省/直辖市");
		
	}else{
		if(bname==""||bname=="undefined"){
			layer.msg("请选择收款人开户行");
		}else{
			document.getElementById("NumArea").innerText = areaC;
			document.getElementById("NumBankName").innerText = bname;
			$("#NumBankQueryT").val(bQuery);
			index2=layer.open({
		     	type: 1,
		     	title:'检索行号',
		     	area: ['900px', '350px'],
		    	fix: false, //不固定
		    	scrollbar: false,
		     	maxmin: false,
		    	content: $("#BankNumQ")
		 	});
		}
	}			
};

function cleanBankNum(){//切换归属地或银行后，清空原来的行号信息，要求重新检索
	var text = "";
	$("#BankNumber").val(text);
	$("[name='prpLPayBankVo.bankOutlets']").val(text);
	
}

$("#payBankAreaCode").change(function(){
	cleanBankNum();
});
$("#BankName").change(function(){
	cleanBankNum();
});

function getBankNumTab(event,bNumber,bOutlets){//点击选中行号信息行
	
	$($(event).children()[0]).children().each(function(){  
        if(this.type=="radio"){  
            if(!this.checked){  
                this.checked = true;  
            }else{  
                this.checked = false;  
            }  
        }  
    });		
	bankNumber = bNumber;
	bankOutlets = bOutlets;
	
 };
 
 function layerHiddenBN(){//行号检索界面关闭			
		//回传参数到银行信息界面
		$("#BankQueryText").val("请输入检索名称");
		$("#BankNumber").val(bankNumber);
		$("[name='prpLPayCustomVo.bankOutlets']").val(bankOutlets);
		layer.close(index2);
	}
 
 function addAML(){
		var flag = $("input[name='flag']").val();
		if(flag=='s'){//flag用来判断是修改还是显示，m-修改，s-显示
			$("#mainInfo input").attr("readonly","readonly");//所有主页面的信息只读
			
		}
		
	}
	
</body>
</html>