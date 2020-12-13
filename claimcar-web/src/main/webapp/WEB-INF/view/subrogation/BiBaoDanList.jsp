<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>代位案件理赔信息查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />

</head>
<body>
	<div class="table_title f14">保单信息</div>
		<div class="formtable">
			<div class="row mb-3 cl">
				<label class="form_label col-2">对方保险公司</label>
				
				<c:set var="insurerCode">
					<app:codetrans codeType="DWInsurerCode" codeCode="${resultList.insurerCode }"/>
				</c:set>
				<div class="form_input col-4"> ${insurerCode }	</div>
				<label class="form_label col-2">对方承保区</label>
				
				<c:set var="insurerArea">
				<app:codetrans codeType="DWInsurerArea" codeCode="${resultList.insurerArea }"/>
				</c:set>
				<div class="form_input col-4">${insurerArea }</div>
			</div>
			<div class="row mb-3 cl">
				<label class="form_label col-2">对方保单险种类型</label>
				<c:set var="coverageType">
					<app:codetrans codeType="DWCoverageType" codeCode="${resultList.coverageType }"/>
				</c:set>
				<div class="form_input col-4">${coverageType }</div>
				<label class="form_label col-2">对方保单号</label>
				<div class="form_input col-4">${resultList.policyNo }</div>
			</div>
			
			 <div class="row mb-3 cl">
				<label class="form_label col-2">商三限额</label>
				<div class="form_input col-4">${resultList.limitAmount }</div>
				<label class="form_label col-2">是否承保商三部计免赔</label>
				<c:set var="isInsuredCA">
					<app:codetrans codeType="YN01" codeCode="${resultList.isInsuredCA }"/>
				</c:set>
				<div class="form_input col-4">${isInsuredCA }</div>
			</div> 
			<div class="row mb-3 cl">
				<label class="form_label col-2">对方本车车牌号码</label>
				<div class="form_input col-4">${resultList.licensePlateNo }</div>
				<label class="form_label col-2">对方本车车牌种类</label>
				<div class="form_input col-4">${resultList.licensePlateType }</div>
			</div>
			
		 	<div class="row mb-3 cl">
				<label class="form_label col-2">对方本车发动机号</label>
				<div class="form_input col-4">${resultList.engineNo }</div>
				<label class="form_label col-2">对方本车VIN码</label>
				<div class="form_input col-4">${resultList.vin }</div>
			</div> 

	</div>

</body>
</html>