<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<div class="table_cont">
	<div style="min-height:50px;">
		<div class="table_title f14">补发短信</div>
		<div class="formtable">
			<form id="form">
				<div class="table_cont">
					<div class="row cl">
						<input hidden="hidden" name="businessNo" value="${smsMessage.businessNo }"/>
						<input hidden="hidden" name="sendNodecode" value="${smsMessage.sendNodecode }"/>
						<label class="form_label col-2">补发环节：</label>
						<div class="form_input col-3"><app:codetrans codeType="sendNodecode" codeCode="${smsMessage.sendNodecode}"/></div>
						<label class="form_label col-2">接收短信的号码：</label>
						<div class="form_input col-3">
							<input type="text" class="input-text" id="phoneCode"
								name="phoneCode" value="${smsMessage.phoneCode}"
								datatype="m|/^0\d{2,3}-\d{7,8}$/" nullmsg="请填写接收短信号码！"/>
						</div>
					</div>
					<div class="row cl" id="claimTexts">
							<label class="form_label col-2">短信内容：</label>
							<div class="form_input col-9">
								<textarea class="textarea" style="height: 80px;margin-left: 10px;margin-top:6px"
								name="sendText" datatype="*0-299" value="${smsMessage.sendText}" id="sendText"
								placeholder="请输入...">${smsMessage.sendText}</textarea>
							</div><br/><br/><br/>
						<br/><br/>
					</div>
				</div>
				<br/>
				<div class="row text-c">
					<a class="btn btn-primary ml-5" id=sendMsg>发送短信</a>
				</div>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript" src="/claimcar/js/common/AjaxEdit.js"></script>
<script type="text/javascript">
	$("#sendMsg").click(function(){	//提交表单
		var phoneCode = $("#phoneCode").val();
		var sendText = $("#sendText").val();
		if(phoneCode==null||phoneCode==""){
			layer.msg("接收短信的号码不能为空");
		}else if(sendText==null||sendText==""){
			layer.msg("短信内容不能为空");
		}else{
			$("#form").submit();
		}
	});
	
	$(function (){
		var ajaxEdit = new AjaxEdit($('#form'));
		ajaxEdit.targetUrl = "/claimcar/schedule/sendMsg.do"; 
		ajaxEdit.afterSuccess=function(reMsg){
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
		}; 
		//绑定表单
		ajaxEdit.bindForm();
					
	});
</script>

