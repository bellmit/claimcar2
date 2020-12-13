<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>反欺诈评分信息</title>
</head>
<body>
	<div class="table_wrap">
	<div class="table_cont table_list">
		<table class="table table-border table-hover">
			<thead class="text-c">
			<div class="row  cl">
			    <label class="form_label col-1"></label>
				<div class="form_input col-2"></div>
			    <label class="form_label col-1">规则大类：</label>
				<div class="form_input col-6"> ${caseVo.caseNo }</div>
			</div>
			<div class="row  cl">	
			    <label class="form_label col-1"></label>
				<div class="form_input col-2"></div>
				<label class="form_label col-1">大类描述：</label>
				<div class="form_input col-6">${caseVo.caseDesc }</div>
			</div>
			<div class="row  cl">
			    <label class="form_label col-1"></label>
				<div class="form_input col-2"></div>
				<label class="form_label col-1">规则编号：</label>
				<div class="form_input col-6">${caseVo.ruleNo }</div>
			</div>
			<div class="row  cl">
			    <label class="form_label col-1"></label>
				<div class="form_input col-2"></div>
				<label class="form_label col-1">规则描述：</label>
				<div class="form_input col-6">${caseVo.ruleDesc }</div>
			</div>
			</thead>
				
			</table>
	</div>
</div>

	<script type="text/javascript">
	</script>
</body>
</html>