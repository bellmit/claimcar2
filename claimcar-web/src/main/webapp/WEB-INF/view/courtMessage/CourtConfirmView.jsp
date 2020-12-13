<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>司法确认信息</title>
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
				<div class="table_title f14">司法确认信息</div>
				<div class="table_cont ">
					<div class="formtable tableoverlable">
						<div class="row  cl">
							<label class="form_label col-2">申请人证件号码：</label>
							<div class="form_input col-2">
								${courtConfirmVo.zjhm}
							</div>
							<label class="form_label col-2">申请人：</label>
							<div class="form_input col-2">
									${courtConfirmVo.sqr}
							</div>
							<label class="form_label col-2">申请时间：</label>
							<div class="form_input col-2">
								<fmt:formatDate  value="${courtConfirmVo.sqsj}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-2">案号：</label>
							<div class="form_input col-2">
								${courtConfirmVo.ah}
							</div>
							<label class="form_label col-2">受理法院：</label>
							<div class="form_input col-2">
								${courtConfirmVo.slfy}
							</div>
							<label class="form_label col-2">状态：</label>
							<div class="form_input col-2">
									${courtConfirmVo.sqzt}
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-2" >司法确认开始时间：</label>
							<div class="form_input col-2">
								<fmt:formatDate  value="${courtConfirmVo.sfqrkssj}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-2" >司法确认截止时间：</label>
							<div class="form_input col-2">
								<fmt:formatDate  value="${courtConfirmVo.sfqrjzsj}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
						</div>
					</div>
					<div class="row cl">
						<label class="form_label col-2 text-c">司法确认结果：</label>
						<div class="form_input col-10">
							<textarea class="textarea" style="height: 80px;" id="dangerRemark" disabled="disabled"
								value="${courtConfirmVo.sfqrqk}">${courtConfirmVo.sfqrqk}</textarea>
						</div>
					</div>
					<div class="row cl">
						<label class="form_label col-2 text-c">司法确认情况：</label>
						<div class="form_input col-10">
							<textarea class="textarea" style="height: 80px;" id="dangerRemark" disabled="disabled"
								value="${courtConfirmVo.sfqrjg}">${courtConfirmVo.sfqrjg}</textarea>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</body>
</html>