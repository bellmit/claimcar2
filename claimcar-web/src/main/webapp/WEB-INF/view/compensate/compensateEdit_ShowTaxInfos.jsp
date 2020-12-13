<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>aa</title>
</head>
<body>
<!-- 核赔理算书基本信息 -->
<div class="table_wrap">
 <div class="formtable">
		<div class="formtable mt-10">
		<input type="hidden" name="index" value="${status.index }"/>
				    <div class="row cl">
						<label class="form_label col-2">增值税率：</label>
						<div class="form_input col-3">
						<input type="text" class="input-text"  value="${taxRate }" maxlength="10" readonly="readonly" />
						</div>
					</div>
					
					 <div class="row cl">
						<label class="form_label col-2">增值税额：</label>
						<div class="form_input col-3">
						<input type="text" class="input-text"  value="${taxVlaue}" maxlength="10" readonly="readonly" />
						</div>
					</div>
					
					 <div class="row cl">
						<label class="form_label col-2">不含税金额：</label>
						<div class="form_input col-3">
						<input type="text" class="input-text"  value="${noTaxVlaue }" maxlength="10" readonly="readonly" />
						</div>
					</div>
			<div  class="row cl" >
			<span class=" col-3" ></span>
			 <div class="form_input col-5" >
						<a class="btn  btn-primary" onclick="closeTaxInfos()">关闭</a>
			 </div>
			 <span class=" col-2" ></span>
			</div>
					
			</div>
			</div>
</div>
<script type="text/javascript">
function closeTaxInfos(){
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		   parent.layer.close(index); //再执行关闭           	
}
</script>
</body>
</html>