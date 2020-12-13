<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>立案信息</title>
<style>
.tableoverlable label.form_label.col-1{width:9.3333%;padding-right:0;}
.tableoverlable .form_input.col-3{  width: 23%;margin-left: 1%;}
.tableoverlable .form_input.col-2{  width: 15%;margin-left: 0.6%;}
.btn.garage{
  width: 44%;
  line-height: 31px;
  padding: 0;
}
#mns {margin-left: -4px;}
</style>
</head>
<body>
	<div class="page_wrap">
		<form id="saveCar" class="saveForm" role="form" >
			
			<div class="table_wrap">
				<div class="table_title f14">立案信息</div>
				<div class="table_cont ">
					<div class="formtable tableoverlable">
						<div class="row  cl">
							<label class="form_label col-1">立案日期：</label>
							<div class="form_input col-3">
								<fmt:formatDate  value="${courtClaimVo.ladate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-1">立案案由代码：</label>
							<div class="form_input col-3">
									${courtClaimVo.laaydm}
							</div>
							<label class="form_label col-1">经办法院代码：</label>
							<div class="form_input col-3">
								${courtClaimVo.jbfydm}
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">经办法院名称：</label>
							<div class="form_input col-3">
									${courtClaimVo.jbfymc}
							</div>
							<label class="form_label col-1">立案标的金额：</label>
							<div class="form_input col-3">
								${courtClaimVo.labdje}
							</div>
							<label class="form_label col-1">立案案由名称：</label>
							<div class="form_input col-3">
								${courtClaimVo.laaymc}
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">案件来源编码：</label>
							<div class="form_input col-3">
								${courtClaimVo.ajlybm}
							</div>
							<label class="form_label col-1">是否诉前调解：</label>
							<div class="form_input col-3">
								<app:codetrans codeType="ISValid" codeCode="${courtClaimVo.issqtj}" />
							</div>
						</div>
					</div>
					
						<div class="row cl">
								<label class="form_label col-1 text-c">事实与理由：</label>
								<div class="form_input col-10">
									<textarea class="textarea" style="height: 80px;" disabled="disabled"
										value="${courtClaimVo.ssly}">${courtClaimVo.ssly}</textarea>
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-1 text-c">诉讼请求：</label>
								<div class="form_input col-10">
									<textarea class="textarea" style="height: 80px;"   disabled="disabled"
										value="${courtClaimVo.ssqq}">${courtClaimVo.ssqq}</textarea>
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-1 text-c">审查结果：</label>
								<div class="form_input col-10">
									<textarea class="textarea" style="height: 80px;"  disabled="disabled"
										value="${courtClaimVo.scjg}">${courtClaimVo.scjg}</textarea>
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-1 text-c">就医情况：</label>
								<div class="form_input col-10">
									<textarea class="textarea" style="height: 80px;" disabled="disabled"
										value="${courtClaimVo.jyqk}">${courtClaimVo.jyqk}</textarea>
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-1 text-c">给法官留言：</label>
								<div class="form_input col-10">
									<textarea class="textarea" style="height: 80px;"  disabled="disabled"
										value="${courtClaimVo.ly}">${courtClaimVo.ly}</textarea>
								</div>
							</div>
				</div>
			</div>
		</form>
	</div>
</body>
</html>