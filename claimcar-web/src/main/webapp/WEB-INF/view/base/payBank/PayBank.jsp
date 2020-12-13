<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>理赔账户信息修改处理</title>
</head>
<body>
    
    <form id="PayBankEditForm" role="form" method="post">
	<div class="formtable" id="mainInfo">
		<div class="formtable mt-10">
			<div class="row cl">
			    <label class="form_label col-3">业务类型</label>
			    ${prpLPayBank.businessType }
			</div>
			<div class="row cl">
			    <label class="form_label col-3">理算计算书号</label>
			    ${prpLPayBank.compensateNo }
			</div>
			<div class="row cl">
			    <label class="form_label col-3">保单号</label>
			    ${prpLPayBank.policyNo }
			</div>
			<div class="row cl">
			    <label class="form_label col-3">立案号</label>
			    ${prpLPayBank.claimNo }
			</div>
		</div>
		    <div class="line mt-10 mb-10"></div>
		<div class="formtable mt-10">
			<div class="row cl">
			    <label class="form_label col-3">被保险人名称</label>
			    ${ }
			</div>
			<div class="row cl">
			    <label class="form_label col-3">被保险人代码</label>
			    ${ }
			</div>
			<div class="row cl">
			    <label class="form_label col-3">核赔通过日期</label>
			    ${ }
			</div>
			<div class="row cl">
			    <label class="form_label col-3">结案日期</label>
			    ${ }
			</div>
		</div>
		    <div class="line mt-10 mb-10"></div>
		<div class="formtable mt-10">
			<div class="row cl">
			    <label class="form_label col-3">申请人</label>
			    ${ }
			</div>
			<div class="row cl">
			    <label class="form_label col-3">申请时间</label>
		    	${ }
			</div>
		</div>
		    <div class="line mt-10 mb-10"></div>
		<div class="formtable mt-10">
			<div class="row cl">
			    <label class="form_label col-3">支付失败原因</label>
			    ${ }
			</div>
			
			<div class="row cl">
			    <label class="form_label col-3">操作人</label>
			    ${ }
			</div>
			
		</div>
		    <div class="line mt-10 mb-10"></div>
		    <br>
		<div class="btn-footer clearfix text-c">
		    <div class="form_input col-4">
				<a class="btn btn-primary" onclick="lookPayBank('${prpLPayBank.id}')" >查看已处理的银行信息</a>
			</div>
			<div class="form_input col-4">
				<input type="button" class="btn btn-primary" onclick="closeLayer()" value="关闭" />
			</div>
			
	    </div>
	    
	</div>
	</form>
	
	<script type="text/javascript">	
	    function lookPayBank(pid){
	    	index=layer.open({
			    type: 2,
			    title: "已处理的银行信息",
			    shadeClose: true,
			    scrollbar: false,
			    skin: 'yourclass',
			    area: ['1000px', '550px'],
			    content:"/claimcar/payBank/lookPayBank.do?pid="+pid+"",
			    
			});
	    }
	   
		    
</body>
</html>