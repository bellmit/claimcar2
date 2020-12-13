<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>修理厂信息修改</title>
</head>
<body>
<form name="fm" id="form1" class="form-horizontal" role="form">
<input type="hidden"  name="prpDHospitalVo.id"  value="${prpDHospitalVo.id}" >
<div class="fixedmargin page_wrap" style="margin-top:0px">
					<h3 class="panel-title text-c">
						医院信息编辑
					</h3>
				<div class="table_cont ">
					<div class="formtable">
						<div class="row cl">
							 <label class="form_label col-1">归属机构</label>
					 		<div class="form_input col-3">
					 		<input type="hidden" name="prpDHospitalVo.id" value="${prpDHospitalVo.id}" >
					 				<span class="select-box"> 
								 <app:codeSelect codeType="ComCodeSelect" type="select" id="comCode"
										name="prpDHospitalVo.comCode" value="${prpDHospitalVo.comCode}" lableType="code-name" />
								</span>
					 		</div> 
								<label class="form_label col-1">医院代码</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" readonly style="background-color: #ccc" datatype="*" nullmsg="请填写医院代码" name="prpDHospitalVo.hospitalCode" value="${prpDHospitalVo.hospitalCode}" >
							</div>	
							
							<%-- 	<label class="form_label col-1">医院等级</label>
					 		<div class="form_input col-2">
					 			<span class="select-box">
					 			<input type="hidden" id="hospitalLevels" value="${prpDHospitalVo.hospitalLevel}"/>
									
									<select class="select" style="vertical-align:middle"  name="prpDHospitalVo.hospitalLevel" >
										<option value='一级'>一级</option>
										<option value='二级'>二级</option>
										<option value='三级'>三级</option>
										<option value='四级'>四级</option>
									</select>
								</span>
					 		</div> --%>
					 			<label class="form_label col-1">医院等级</label>
					 			<input type="hidden" id="hospitalLevels" value="${prpDHospitalVo.hospitalLevel}"/>
								<input type="hidden" id="hospitalClass" value="${prpDHospitalVo.hospitalClass}"/>
					 		<div class="form_input col-1">
					 			<span class="select-box">
									<select class=" select must" style="vertical-align:middle" 
									name="prpDHospitalVo.hospitalLevel">
										<option value='一级'>一级</option>
										<option value='二级'>二级</option>
										<option value='三级'>三级</option>
										<option value='四级'>四级</option>
									</select>
								</span>
					 		</div>
					 		<div class="form_input col-1">
					 			<span class="select-box">
									<select class=" select must" style="vertical-align:middle" 
									name="prpDHospitalVo.hospitalClass">
										<option value='甲等'>甲等</option>
										<option value='乙等'>乙等</option>
										<option value='丙等'>丙等</option>
									</select>
								</span>
					 		</div>
						</div>
						<div class="row cl">
					 	 	<label class="form_label col-1">所在省市</label>
							<div class="form_input col-3">
							<app:areaSelect targetElmId="areaCode" areaCode="${prpDHospitalVo.areaCode }" showLevel="2" style="width:122px" />
								<input type="hidden" id="areaCode" name="prpDHospitalVo.areaCode" value="${prpDHospitalVo.areaCode }"/>
							</div>	 	
							<label class="form_label col-1">医院名称</label>
							<div class="form_input col-3">
								<input type="text" class="input-text"  name="prpDHospitalVo.hospitalCName" datatype="*" nullmsg="请填写医院名称" value="${prpDHospitalVo.hospitalCName}" >
							</div>	
							<label class="form_label col-1">医院缩写</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.hospitalAbbName" value="${prpDHospitalVo.hospitalAbbName}">
							</div>	
							
						</div>
						<div class="row cl">
							<label class="form_label col-1">医院地址</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.hospitalAddress" datatype="*" value="${prpDHospitalVo.hospitalAddress}" >
							</div>
							<label class="form_label col-1">医院邮编</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.postCode" value="${prpDHospitalVo.postCode}" >
							</div>	
							<label class="form_label col-1">是否指定医院</label>
							<div class="form_input col-3">
							<span class="select-box">
								<input type="hidden" id="appointFlag" value="${prpDHospitalVo.appointFlag}"/>
									<select name="prpDHospitalVo.appointFlag" value="${prpDHospitalVo.appointFlag}" class=" select must" style="vertical-align:middle" 
									>
										<option value="是">是</option>
										<option value="否">否</option>
									</select>
								</span>
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-1">联系人名称</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.contractName" value="${prpDHospitalVo.contractName}" >
							</div>
							<label class="form_label col-1">联系人职务</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.contractDuty" value="${prpDHospitalVo.contractDuty}">
							</div>	
							<label class="form_label col-1">联系人邮件</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.contractEmail" value="${prpDHospitalVo.contractEmail}" >
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-1">联系人电话</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.contractTel" value="${prpDHospitalVo.contractTel}" >
							</div>
						
							<label class="form_label col-1">联系人手机</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.contractMobile" value="${prpDHospitalVo.contractMobile}" >
							</div>	
								<label class="form_label col-1">开户银行</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.bank" value="${prpDHospitalVo.bank}" >
							</div>	
						</div>	
						
						<div class="row cl">
							<label class="form_label col-1">账户名</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.accountName" value="${prpDHospitalVo.accountName}">
							</div>
						
							<label class="form_label col-1">账户号</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.accounts" value="${prpDHospitalVo.accounts}">
							</div>	
								<label class="form_label col-1">有效标志</label>
							<div class="form_input col-3">
										<span class="select-box">
										<input type="hidden" id="validFlags" value="${prpDHospitalVo.validFlag}"/>
									<select class=" select must" style="vertical-align:middle"  id="validFlag"
									name="prpDHospitalVo.validFlag" >
										<option value="有效">有效</option>
										<option value="无效">无效</option>
									</select>
								</span>
							</div>	
						</div>
						
							<div class="row cl">
							<label class="form_label col-1">生效日期</label>
								<c:set var="validDate">
									<fmt:formatDate value="${prpDHospitalVo.validDate}" pattern="yyyy-MM-dd"/>
								</c:set>
							<div class="form_input col-3">
								<input type="text" class="Wdate" id="validDate" style="width: 97%;" datatype="*"
									name="prpDHospitalVo.validDate" value="${validDate }"
									onfocus="WdatePicker({maxDate:'\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd'})"/>
								</div>
						
							<label class="form_label col-1">失效日期</label>
								<c:set var="inValidDate">
									<fmt:formatDate value="${prpDHospitalVo.inValidDate}" pattern="yyyy-MM-dd"/>
								</c:set>
							<div class="form_input col-3">
								<input type="text" class="Wdate" id="inValidDate" style="width: 97%;" datatype="*"
									name="prpDHospitalVo.inValidDate" value="${inValidDate }"
									onfocus="WdatePicker({maxDate:'\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd'})"/>
								</div>	
								<label class="form_label col-1">综合/专科</label>
							<div class="form_input col-3">
										<span class="select-box">
										<input type="hidden" id="kinds" value="${prpDHospitalVo.kind}"/>
									<select name="prpDHospitalVo.kind" class=" select must" style="vertical-align:middle">
										<option value="综合">综合</option>
										<option value="专科">专科</option>
									</select>
								</span>
							</div>	
						</div>
						
						
					 	<div class="row cl">
					 		<label class="form_label col-1">备注</label>
							<div class="form_input col-10">
								<textarea  class="textarea" name="prpDHospitalVo.remark"  placeholder="备注点什么..." datatype="*0-255">${prpDHospitalVo.remark}</textarea>
							</div>
						</div>
						<!--撑开页面  开始  -->
						<div class="row cl"><div class="form_input col-6"></div></div>
						<div class="row cl"><div class="form_input col-6"></div></div>
						<!-- 结束 -->	
							<div class="btn-footer clearfix">
							<a class="btn btn-primary fl" style="margin-left:40%" id="submit1">保存</a>
							<a class="btn btn-danger fl cl" id="closeM" >关闭</a>
						</div>	
				 </div>
			     </div>
		</div>
</form>
<script type="text/javascript">
$(document).ready(function(){ 
	var s2 = document.getElementById("hospitalLevels").value;
	var hospitalClass = document.getElementById("hospitalClass").value;
	var validFlags = document.getElementById("validFlags").value;
	var kinds =  document.getElementById("kinds").value;
	var appointFlag =  document.getElementById("appointFlag").value;
	$("select").each(function (){
	$(this).children("option").each(function(){
		 if($(this).text()==s2){
			  $(this).attr("selected",true);  
		} 
		 if($(this).text()==hospitalClass){
			  $(this).attr("selected",true);  
		} 
		if($(this).text()==validFlags){
			
			  $(this).attr("selected",true);  
		}
		if($(this).text()==kinds){
			  $(this).attr("selected",true);  
		}
		if($(this).text()==appointFlag){
			  $(this).attr("selected",true);  
		}
	});
});

});
$("#re").click(function(){
	$(".input-text").val("");
	$("select").val("1");
	$(".textarea").val("");
});
$("#submit1").click(function(){
	$("#comCode").attr("datatype","*");
	$("#comCode").attr("nullmsg","请填写归属机构");
	$(".areaselect").attr("datatype","*");
	$(".areaselect").attr("nullmsg","请填写省市");
	$("#validFlag").attr("datatype","*");
	$("#validFlag").attr("nullmsg","请填写有效标志");
	//发送ajaxEdit
	var ajaxEdit = new AjaxEdit($('#form1'));
	ajaxEdit.targetUrl="/claimcar/manager/updateHospital.do"; 
	ajaxEdit.afterSuccess=function(result){
		var $result=result.data;
		if($result=="1"){
			layer.alert("修改成功!", function(){
				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					parent.layer.close(index); //再执行关闭 
					});
		}else{
			layer.msg("操作失败！");
			return;
		}
	}; 
	//绑定表单
	ajaxEdit.bindForm();
	$("#form1").submit();
});

$("#closeM").click(function(){
	var a = parent.window.no();
	parent.layer.close(a);// 执行关闭
});
</script>
</body>