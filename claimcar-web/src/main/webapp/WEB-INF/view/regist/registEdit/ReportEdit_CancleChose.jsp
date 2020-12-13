<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<html>
<head>
<c:if test="${isclaim eq '0' }">
<title>报案注销发起</title>
</c:if>
<c:if test="${isclaim eq '1' }">
<title>立案注销发起</title>
</c:if>
</head>
<body style="overflow: hidden;">
<div class="table_wrap table_cont">
<form  role="form" method="post">
	<div class="formtable">
		<div class="formtable mt-10">
		<c:if test="${isclaim eq '0' }">
		<div class="row cl">
			<label class="form_label col-2" style="text-align: left;">&nbsp;&nbsp;&nbsp;报案注销</label>
			
		</div>
		<div class="line"></div>
		<div class="row cl">
			<label class="form_label col-2"><font class="c-red">*</font>注销原因：</label>
				<div class="form_input col-4" id="cancelOut">
				<app:codeSelect codeType="ReportCancel" type="select" value="" clazz="mr-5" />
				</div>
		</div>
		</c:if>
		<c:if test="${isclaim eq '1' }">
		<div class="row cl">
			<label class="form_label col-2" style="text-align: left;">&nbsp;&nbsp;&nbsp;立案注销</label>
		</div>
		<div class="line"></div>
		<c:if test="${iscanceCI eq '0'}">
		<div class="row cl">
			<div class="form_input col-4">
			<label>&nbsp;&nbsp;&nbsp;<input type="radio" name="claimCance" value="1"/>&nbsp;&nbsp;&nbsp;交强立案注销</label>
			</div>
		</div>
		</c:if>
		<c:if test="${iscanceBI eq '0'}">
		<div class="row cl">
			<div class="form_input col-4">
			<label>&nbsp;&nbsp;&nbsp;<input type="radio" name="claimCance" value="2"/>&nbsp;&nbsp;&nbsp;商业立案注销</label>
			</div>
		</div>
		</c:if>
		</c:if>
		</div>
		</br>
		<div class="btn-footer clearfix" style="text-align: center;">
		       <input type="hidden" id="CIclaimNo" value="${claimNoCI}"/>
		       <input type="hidden" id="BIclaimNo" value="${claimNoBI}"/>
		       <input type="hidden" id="registNo" value="${registNo}"/>
		       <c:if test="${isclaim eq '0'}">
			   <a class="btn btn-primary ml-5" onclick="registCancle()" style="width:55px;height:30px" id="centain">确定</a>
			   </c:if>
			   <c:if test="${isclaim eq '1' && (iscanceCI eq '0' || iscanceBI eq '0')}">
			   <a class="btn btn-primary ml-5" onclick="claimCancle()" style="width:55px;height:30px" id="centain">确定</a>
			   </c:if>
			   <a class="btn btn-primary ml-5" id="cancelBtn" onclick="closeLayer()" style="width:55px;height:30px">取消</a>
	   </div>
	</div>
	   
		</form>
		</div>
		<script type="text/javascript" src="/claimcar/js/registEdit/ReportEdit_CancleChose.js"></script>
		<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
</body>
</html>