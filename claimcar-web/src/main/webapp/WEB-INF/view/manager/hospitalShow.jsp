<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>医院信息查看</title>
</head>
<body>
<form name="fm" id="form1" class="form-horizontal" role="form">
<input type="hidden"  name="prpDHospitalVo.id"  value="${prpDHospitalVo.id}" >
<div class="fixedmargin page_wrap" style="margin-top:0px">
					<h3 class="panel-title text-c">
						医院信息查看
					</h3>
				<div class="table_cont ">
					<div class="formtable">
						<div class="row cl">
							<label class="form_label col-2">归属机构</label>
							<div class="form_input col-4">
								<input type="text" class="input-text" readonly name="prpDHospitalVo.comCode" value="${prpDHospitalVo.comCode}" >
							</div>	
								<label class="form_label col-2">医院所在地</label>
							<div class="form_input col-4">
								<app:areaSelect targetElmId="areaCode" disabled="true" areaCode="${prpDHospitalVo.areaCode }" showLevel="2" style="width:122px" />
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-2">医院代码</label>
							<div class="form_input col-4">
								<input type="text" class="input-text" readonly name="prpDHospitalVo.hospitalCode" value="${prpDHospitalVo.hospitalCode}" >
							</div>
								<label class="form_label col-2">医院等级</label>
									<input type="hidden" id="hospitalLevels" value="${prpDHospitalVo.hospitalLevel}"/>
								<input type="hidden" id="hospitalClass" value="${prpDHospitalVo.hospitalClass}"/>
							<div class="form_input col-4">
								<input type="text" class="input-text" id="LevelAndClass" readonly name="prpDHospitalVo.hospitalLevel">
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-2">医院名称</label>
					 		<div class="form_input col-4">
								<input type="text" class="input-text" readonly name="prpDHospitalVo.hospitalCName" value="${prpDHospitalVo.hospitalCName}" >
							</div>	
								<label class="form_label col-2">医院名称缩写</label>
							<div class="form_input col-4">
								<input type="text" class="input-text" readonly  name="prpDHospitalVo.hospitalAbbName" value="${prpDHospitalVo.hospitalAbbName}" >
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-2">医院地址</label>
								<div class="form_input col-4">
								<input type="text" class="input-text" readonly  name="prpDHospitalVo.hospitalAddress" value="${prpDHospitalVo.hospitalAddress}" >
							</div>	
								<label class="form_label col-2">医院邮编</label>
							<div class="form_input col-4">
								<input type="text" class="input-text" readonly name="prpDHospitalVo.postCode" value="${prpDHospitalVo.postCode}" >
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-2">综合/专科</label>
							<div class="form_input col-4">
								<input type="text" class="input-text" readonly name="prpDHospitalVo.kind" value="${prpDHospitalVo.kind}" >
							</div>
								<label class="form_label col-2">是否指定医院</label>
									<c:set var="appointFlag">
							<app:codetrans codeType="YN01" codeCode="${prpDHospitalVo.appointFlag}"/> 
						</c:set>
						<%-- <span>${bankName}</span> --%>
						
							<div class="form_input col-4">
								<input type="text" class="input-text" readonly  name="prpDHospitalVo.appointFlag" value="${appointFlag}" >
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-2">联系人名称</label>
					 		<div class="form_input col-4">
								<input type="text" class="input-text" readonly  name="prpDHospitalVo.contractName" value="${prpDHospitalVo.contractName}" >
							</div>	
								<label class="form_label col-2">联系人职务</label>
							<div class="form_input col-4">
								<input type="text" class="input-text" readonly name="prpDHospitalVo.contractDuty" value="${prpDHospitalVo.contractDuty}" >
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-2">联系人电话</label>
							<div class="form_input col-4">
								<input type="text" class="input-text" readonly name="contractTel" value="${prpDHospitalVo.contractTel}" >
							</div>	
								<label class="form_label col-2">联系人手机</label>
							<div class="form_input col-4">
								<input type="text" class="input-text" readonly name="prpDHospitalVo.contractMobile" value="${prpDHospitalVo.contractMobile}" >
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-2">联系人邮件</label>
					 		<div class="form_input col-4">
								<input type="text" class="input-text" readonly name="prpDHospitalVo.contractEmail" value="${prpDHospitalVo.contractEmail}" >
							</div>
								<label class="form_label col-2">开户银行</label>
							<div class="form_input col-4">
								<input type="text" class="input-text" readonly name="prpDHospitalVo.bank" value="${prpDHospitalVo.bank}" >
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-2">账户名</label>
					 		<div class="form_input col-4">
								<input type="text" class="input-text" readonly name="prpDHospitalVo.accountName" value="${prpDHospitalVo.accountName}" >
							</div>	
								<label class="form_label col-2">账户号</label>
							<div class="form_input col-4">
								<input type="text" class="input-text" readonly  name="prpDHospitalVo.accounts" value="${prpDHospitalVo.accounts}" >
							</div>	
						</div>
					<%-- 	<div class="row cl">
							<label class="form_label col-2">生效日期</label>
					 		<div class="form_input col-4">
								<input type="text" class="input-text" readonly  name="prpDHospitalVo.validDate" value="${prpDHospitalVo.validDate}" >
							</div>	
							<label class="form_label col-2">失效日期</label>
							<div class="form_input col-4">
								<input type="text" class="input-text" readonly  name="prpDHospitalVo.invalidDate" value="${prpDHospitalVo.invalidDate}" >
							</div>	
						</div> --%>
					<div class="row cl">
							<label class="form_label col-2">生效日期</label>
							<c:set var="validDate">
									<fmt:formatDate value="${prpDHospitalVo.validDate}" pattern="yyyy-MM-dd"/>
								</c:set>
					 		<div class="form_input col-4">
								<input type="text" class="input-text" readonly  name="prpDHospitalVo.validDate" value="${validDate}" >
							</div>	
							<label class="form_label col-2">失效日期</label>
							<c:set var="inValidDate">
									<fmt:formatDate value="${prpDHospitalVo.inValidDate}" pattern="yyyy-MM-dd"/>
								</c:set>
							<div class="form_input col-4">
								<input type="text" class="input-text" readonly  name="prpDHospitalVo.inValidDate" value="${inValidDate}" >
							</div>	
						</div>
					<div class="row cl">
						    <label class="form_label col-2">有效标志</label>
							<div class="form_input col-4">
							    <span class="select-box">
								<app:codeSelect 	codeType="validFlag" type="select" id="validFlag"
										value="${prpDHospitalVo.validFlag}"  lableType="code-name" />
								</span>
							</div>
					</div>
						<div class="row cl">
					 		<label class="form_label col-1">备注</label>
							<div class="form_input col-10">
								<textarea  class="textarea" name="prpDHospitalVo.remark" readonly placeholder="备注点什么..." datatype="s0-255">${prpDHospitalVo.remark}</textarea>
							</div>
						</div>
						<!--撑开页面  开始  -->
						<div class="row cl"><div class="form_input col-6"></div></div>
						<div class="row cl"><div class="form_input col-6"></div></div>
						<!-- 结束 -->	
				 </div>
				 <div class="btn-footer clearfix text-c">
				     <a class="btn btn-primary" onclick="closeLayer()" >关闭</a>
				 </div>
			     </div>
			     
		</div>
</form>
<script type="text/javascript">
$(function (){
	$("#validFlag").prop("disabled", "disabled");
});
		$(document).ready(function(){ 
			var hospitalLevels = $("#hospitalLevels").val();
			var hospitalClass = $("#hospitalClass").val();
			var LevelAndClass = hospitalLevels+hospitalClass;
			$("#LevelAndClass").val(LevelAndClass);
		});
		
		function closeLayer(){
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index);
		}
</script>
</body>