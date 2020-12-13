<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIN码信息修改</title>
<style>
.line{ font-size: 0px; line-height: 0px; border-top: solid 1px #ddd; float: none}
</style>
</head>
<body>
<div class="fixedmargin page_wrap">
	<div class="table_cont">
		<form id="form" role="form" method="post" name="fm">
		<input hidden="hidden" id="handleStatus" value="${handleStatus}"/>
		<input hidden="hidden" id="vinsSize" value="${vinsSize}"/>
		<div class="table_wrap">
			<div class="table_title f14">案件信息</div>
				<div class="table_cont">
					<div class="formtable">
						<div class="row cl">
							<label class="form_label col-3">报案号：</label>
							<div class="form_input col-2">${registVo.registNo }</div>
							<label class="form_label col-2">被保险人：</label>
							<div class="form_input col-3">${registExtVo.insuredName }</div>
						</div>
						<div class="row cl">
							<label class="form_label col-3">保单号：</label>
							<div class="form_input col-2">${registVo.policyNo }</div>
							<label class="form_label col-2">标的车牌号：</label>
							<div class="form_input col-3">${registExtVo.licenseNo }</div>
						</div>
					</div>
				</div>
			<div class="table_title f14">VIN码信息</div>
				<div class="table_cont">
					<div class="formtable">
						<c:forEach var="carVo" items="${PrpLDlossCarMainVoList}" varStatus="status">
							<input hidden="hidden" name="carList[${status.index + size}].serialNo" value="${carVo.serialNo}" disabled="disabled" />
							<input hidden="hidden" name="carList[${status.index + size}].registNo" value="${carVo.registNo}" disabled="disabled" />
							<input hidden="hidden" name="carList[${status.index + size}].licenseNo" value="${carVo.licenseNo}" disabled="disabled" />
							<div class="row cl">
								<label class="form_label col-3">损失方：</label>
								<div class="form_input col-1">${carVo.deflossCarType }</div>
								<div class="form_input col-1">
								<input name="carList[${status.index + size}].checkbox" type="checkbox" onclick="updateFlag(this)"/>修改
								</div>
								<label class="form_label col-2">车牌号：</label>
								<div class="form_input col-3">${carVo.licenseNo }</div>
							</div>
							<div class="row cl">
								<label class="form_label col-3">修改前VIN码：</label>
								<div class="form_input col-2">${carVo.vinNo }</div>
								<label class="form_label col-2">修改后VIN码：</label>
								<div class="form_input col-2">
									<input type="text" class="input-text" name="carList[${status.index + size}].vinNo" disabled="disabled" 
									value="${carVo.vinNo }" errormsg="请输入正确的VIN码！" nullmsg="请输入正确的VIN码！"  maxlength="17" onkeyup="toUpperCase(this)"/>
								</div>
							</div>
							<br/><hr><br/>
						</c:forEach>
					</div>
				</div>
				<div class="btn-footer clearfix" style="margin-left: 40%;">
					<input class="btn btn-primary btn-kk" type="button" id="save" onclick="save1()" value="保存">
					<input class="btn btn-primary " type="reset" name="rest" id="rest" value="重置">
				</div>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript">
$(function() {
	var handleStatus = $("#handleStatus").val();
	if(handleStatus == "3"){
		$("body input").attr("disabled", "disabled");
	}
	$("#save").hide(); 
	$("#rest").hide();
	ajaxEdit = new AjaxEdit($('#form'));
	ajaxEdit.targetUrl = "/claimcar/updateVIN/updateVIN.do";
	ajaxEdit.afterSuccess = function(result) {
		$("#save").hide(); //保存成功防止重复提交
		$("#rest").hide();
		layer.msg("保存成功");
		$("body input").attr("disabled", "disabled");
	};
	// 绑定表单
	ajaxEdit.bindForm();
	
});

function save1(){
	var vinsSize = parseFloat($("#vinsSize").val());
	if(vinsSize > 1){
		for(var i=0;i<vinsSize-1;i++){
			var vinNo = $("input[name='carList["+i+"].vinNo']").val();
			for(var j=i+1;j<vinsSize;j++){
				var nextVinNo = $("input[name='carList["+j+"].vinNo']").val();
				if(vinNo == nextVinNo && vinNo != null && vinNo != ""){
					layer.msg("VIN码不能相同！");
					return false;
				}
			}
		}
	}
	
	$("#form").submit();
}

function updateFlag(element){
	var name = $(element).attr("name");
	var vinName = name.split('.')[0]+".vinNo";
	var serialNo = name.split('.')[0]+".serialNo";
	var registNo = name.split('.')[0]+".registNo";
	var licenseNo = name.split('.')[0]+".licenseNo";
	var flag = "N";
	if($(element).is(':checked')){
		$("input[name='"+vinName+"']").removeAttr("disabled");
		$("input[name='"+vinName+"']").attr("datatype","*");
		$("input[name='"+serialNo+"']").removeAttr("disabled");
		$("input[name='"+registNo+"']").removeAttr("disabled");
		$("input[name='"+licenseNo+"']").removeAttr("disabled");
	}else{
		$("input[name='"+vinName+"']").val(null);
		$("input[name='"+vinName+"']").attr("disabled","disabled");
		$("input[name='"+vinName+"']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
		$("input[name='"+vinName+"']").removeAttr("datatype");
		$("input[name='"+serialNo+"']").attr("disabled","disabled");
		$("input[name='"+registNo+"']").attr("disabled","disabled");
		$("input[name='"+licenseNo+"']").attr("disabled","disabled");
	}
	$("input[name$='checkbox']").each(function(){
		if($(this).is(':checked')){
			flag = "Y";
		}
	});
	if(flag == "Y"){
		$("#save").show();
		$("#rest").show();
	}else{
		$("#save").hide(); 
		$("#rest").hide();
	}
}
</script>
</body>
</html>