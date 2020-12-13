<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>医院信息增加</title>
</head>
<body>
<form name="fm" id="form1" class="form-horizontal" role="form">
<div class="fixedmargin page_wrap" style="margin-top:0px">
					<h3 class="panel-title text-c">
						医院信息增加
					</h3>
				<div class="table_cont ">
					<div class="formtable">
						<div class="row cl">
							<label class="form_label col-1">归属机构</label>
					 		<div class="form_input col-3">
								<span class="select-box"> 
								 <app:codeSelect codeType="ComCodeSelect" type="select" id="comCode"
										name="prpDHospitalVo.comCode" lableType="code-name"/>
								</span>
					 		</div>
								<label class="form_label col-1">医院代码</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.hospitalCode"  datatype="*" nullmsg="请填写医院代码">
							</div>	
								<label class="form_label col-1">医院等级</label>
					 		<div class="form_input col-1">
					 			<span class="select-box">
									<select class=" select must" style="vertical-align:middle" 
									name="prpDHospitalVo.hospitalLevel" datatype="*">
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
									name="prpDHospitalVo.hospitalClass" datatype="*">
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
				<app:areaSelect targetElmId="prpDHospitalarea"  showLevel="2" style="width:122px"/>
				<input type="hidden" id="prpDHospitalarea" name="prpDHospitalVo.areaCode" />
				</div>
							<label class="form_label col-1">医院名称</label>
							<div class="form_input col-3">
								<input type="text" class="input-text"  name="prpDHospitalVo.hospitalCName" datatype="*" nullmsg="请填写医院名称">
							</div>	
							<label class="form_label col-1">医院缩写</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.hospitalAbbName" >
							</div>	
							
						</div>
						<div class="row cl">
							<label class="form_label col-1">医院地址</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.hospitalAddress" datatype="*">
							</div>
							<label class="form_label col-1">医院邮编</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.postCode" >
							</div>	
							<label class="form_label col-1">是否指定医院</label>
							<div class="form_input col-3">
								<span class="select-box">
								<app:codeSelect 	codeType="YN01" type="select" 
										name="prpDHospitalVo.appointFlag" lableType="code-name" />
								</span>
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-1">联系人名称</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.contractName">
							</div>
							<label class="form_label col-1">联系人职务</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.contractDuty" >
							</div>	
							<label class="form_label col-1">联系人邮件</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.contractEmail" >
							</div>	
						</div>
						<div class="row cl">
							<label class="form_label col-1">联系人电话</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.contractTel"  >
							</div>
						
							<label class="form_label col-1">联系人手机</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.contractMobile"  >
							</div>	
								<label class="form_label col-1">开户银行</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.bank" >
							</div>	
						</div>	
						
						<div class="row cl">
							<label class="form_label col-1">账户名</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.accountName" >
							</div>
						
							<label class="form_label col-1">账户号</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" name="prpDHospitalVo.accounts" >
							</div>	
								<label class="form_label col-1">有效标志</label>
							<div class="form_input col-3">
								<span class="select-box">
								<app:codeSelect 	codeType="validFlag" type="select" id="validFlag"
										name="prpDHospitalVo.validFlag"  lableType="code-name" />
								</span>
							</div>	
						</div>
						
							<div class="row cl">
							<label class="form_label col-1">生效日期</label>
							<div class="form_input col-3">
								<input type="text" class="Wdate" style="width: 97%;" name="prpDHospitalVo.validDate"  id="sdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'edate\')}'})" datatype="*"/>
							</div>
						
							<label class="form_label col-1">失效日期</label>
							<div class="form_input col-3">
							<input type="text" class="Wdate" style="width: 97%;" name="prpDHospitalVo.inValidDate" id="edate" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'sdate\')}',startDate:'#F{$dp.$D(\'sdate\',{d:+1})}',dateFmt:'yyyy-MM-dd'})" datatype="*"/>
							</div>	
								<label class="form_label col-1">综合/专科</label>
							<div class="form_input col-3">
										<span class="select-box">
									<select name="prpDHospitalVo.kind" class=" select must" style="vertical-align:middle" >
										
										<option value="专科">专科</option>
										<option value="综合">综合</option>
									</select>
								</span>
							</div>	
						</div>
						
						
					 	 <div class="row cl">
					 		<label class="form_label col-1">备注</label>
							<div class="form_input col-10">
								<textarea  class="textarea" name="prpDHospitalVo.remark"  placeholder="备注点什么..." datatype="*0-255"></textarea>
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
$("#re").click(function(){
	$(".input-text").val("");
	$("select").val("1");
	$(".textarea").val("");
});
$("#submit1").click(function(){
	//发送ajaxEdit
	$("#comCode").attr("datatype","*");
	$("#comCode").attr("nullmsg","请填写归属机构");
	$(".areaselect").attr("datatype","*");
	$(".areaselect").attr("nullmsg","请填写省市");
	$("#validFlag").attr("datatype","*");
	$("#validFlag").attr("nullmsg","请填写有效标志");
	
	var ajaxEdit = new AjaxEdit($('#form1'));
	ajaxEdit.targetUrl="/claimcar/manager/add.do"; 
	ajaxEdit.afterSuccess=function(result){
		var $result=result.data;
		if($result=="1"){
			layer.alert("添加成功!", function(){
				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					parent.layer.close(index); //再执行关闭 
					});
		}else if($result=="2"){
			layer.alert("操作失败!", function(){
				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					parent.layer.close(index); //再执行关闭 
					});
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