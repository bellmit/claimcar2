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
    <div class="top_btn">
    <a class="btn  btn-primary" onclick="queryBaoDan()">保单信息</a>
    <a class="btn  btn-primary" onclick="query('daiwei')">代位信息列表</a>
    <a class="btn  btn-primary" onclick="query('regist')">报案信息列表</a>
    <a class="btn  btn-primary" onclick="query('check')">查勘信息列表</a>
    <a class="btn  btn-primary" onclick="query('estimate')">定核损信息列表</a>
    <a class="btn  btn-primary" onclick="query('doc')">单证明细列表</a>
    <a class="btn  btn-primary"  onclick="query('underWrite')">理算信息列表</a>
    <a class="btn  btn-primary" onclick="query('claimClose')">结案信息列表</a>
    <a class="btn  btn-primary" >提示信息</a>
    <a class="btn  btn-primary" onclick="query('claimReopen')">重开赔案信息列表</a>
    <a class="btn  btn-primary" onclick="query('recoveryConfirm')">追回款信息列表</a>
    </div><br/>
    <br/>
    <br/>
    <br/>
<div class="formtable">
    <div class="table_wrap">
     <div class="table_title f14">代位案件理赔信息查询</div>
		<div class="table_cont">
		<input hidden="hidden" value="${rRegistNo}" id="registNo">
		<input hidden="hidden" value="${rRiskCodeSub}" id="comCode">
		<input hidden="hidden" value="${rRecoveryCode}" id="recoveryCode">
		<c:if test="${flags =='1' }">
		 			<div class="row mb-3 cl">
			     		<label class="form_label col-2">结算码</label>
						<div class="form_input col-4">
							${resultList.subrogationBasePartVo.recoveryCode }
						</div>
						<label class="form_label col-2">结算码状态</label>
						<c:set var="recoveryCodeStatus">
							<app:codetrans codeType="RecoveryCodeStatus" codeCode="${resultList.subrogationBasePartVo.recoveryCodeStatus }"/>
						</c:set>
						<div class="form_input col-2">
							${recoveryCodeStatus }
						</div>
						
		     		</div>
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">对方案件理赔编码</label>
						<div class="form_input col-4">
							${resultList.subrogationBasePartVo.claimCode }
						</div>
		     			<label class="form_label col-2">对方案件状态</label>
						<div class="form_input col-2">
						<c:set var="claimStatus">
							<app:codetrans codeType="ClaimStatus" codeCode="${resultList.subrogationBasePartVo.claimStatus }"/>
						</c:set>
					       	${claimStatus }
						</div>
						
		     		</div>
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">对方案件注销原因</label>
						<div class="form_input col-9">
							${resultList.subrogationBasePartVo.cancelCause }
						</div>
		     			
		     		</div>
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">保单号</label>
						<div class="form_input col-2">
						
					       ${resultList.subrogationReportDataVo.policyNo }
						</div>
		     			<label class="form_label col-2">报案号</label>
						<div class="form_input col-2">
					       	  ${resultList.subrogationReportDataVo.reportNo }
						</div>
						<label class="form_label col-2">报案时间</label>
						<div class="form_input col-1">
							<c:set var="reportTime">
								<fmt:formatDate value="${resultList.subrogationReportDataVo.reportTime }" pattern="yyyy-MM-dd"/>
							</c:set>
					       	  ${reportTime }
						</div>
		     		</div>
		     	<div class="row mb-3 cl">
		     		<label class="form_label col-2">报案人姓名</label>
						<div class="form_input col-2">
					       	${resultList.subrogationReportDataVo.reporterName}
						</div>
		     			<label class="form_label col-2">出险驾驶员名称</label>
						<div class="form_input col-2">
					       	 	${resultList.subrogationReportDataVo.driverName }
						</div>
						<label class="form_label col-1">出险时间</label>
						<div class="form_input col-2">
						<c:set var="accidentTime">
								<fmt:formatDate value="${resultList.subrogationReportDataVo.accidentTime }" pattern="yyyy-MM-dd"/>
						</c:set>
						${accidentTime }
						</div>
		     		</div>
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">出险地点</label>
						<div class="form_input col-2">
					      	${resultList.subrogationReportDataVo.accidentPlace }
						</div>
		     			
		     		</div>
		     			<div class="row mb-3 cl">
		     		<label class="form_label col-2">出险经过</label>
						<div class="form_input col-5">
					       ${resultList.subrogationReportDataVo.accidentDescription }
						</div>
		     			<label class="form_label col-2">出险标的车车牌号码</label>
						<div class="form_input col-1">
					       	${resultList.subrogationReportDataVo.carMark }
						</div>
		     		</div>
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">出险标的车车牌种类</label>
						<div class="form_input col-2">
						<c:set var="vehicleType">
								<app:codetrans codeType="LicenseKindCode" codeCode="${resultList.subrogationReportDataVo.vehicleType }"/>
						</c:set>
					       ${vehicleType }
						</div>
		     			<label class="form_label col-2">事故责任划分（报案）</label>
						<div class="form_input col-2">
						<c:set var="accidentLiability">
								<app:codetrans codeType="DWAccidentLiability" codeCode="${resultList.subrogationReportDataVo.accidentLiability }"/>
						</c:set>
					       	  ${accidentLiability }
						</div>
						<label class="form_label col-2">事故处理方式（报案）</label>
						<div class="form_input col-1">
						<c:set var="manageType">
						<app:codetrans codeType="DWOptionType" codeCode="${resultList.subrogationReportDataVo.manageType }"/>
						</c:set>
							 ${manageType }
						</div>
		     		</div>
		     		
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">立案号</label>
						<div class="form_input col-2">
					       ${resultList.subrogationClaimDataVo.registrationNo }
						</div>
		     			<label class="form_label col-2">立案时间</label>
						<div class="form_input col-2">
							<c:set var="registrationTime">
								<fmt:formatDate value="${resultList.subrogationClaimDataVo.registrationTime }" pattern="YYYY-MM-DD"/>
					       	</c:set>
					       	${registrationTime }
						</div>
						<label class="form_label col-2">估损金额</label>
						<div class="form_input col-1">
							${resultList.subrogationClaimDataVo.estimateAmount }
						</div>
		     		</div>
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">保险事故分类（查勘）</label>
						<div class="form_input col-2">
						<c:set var="accidentType">
							<app:codetrans codeType="DWAccidentType" codeCode="${resultList.subrogationCheckDataVo.accidentType }"/>
						</c:set>
					       	 ${accidentType }
						</div>
		     			<label class="form_label col-2">事故责任划分（查勘）</label>
						<div class="form_input col-2">
						<c:set var="accidentLiability">
								<app:codetrans codeType="DWAccidentLiability" codeCode="${resultList.subrogationCheckDataVo.accidentLiability }"/>
						</c:set>
					       	${accidentLiability }
						</div>
		     		</div>
		     		
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">事故处理方式（查勘）</label>
						<div class="form_input col-2">
						<c:set var="manageType">
						<app:codetrans codeType="DWOptionType" codeCode="${resultList.subrogationCheckDataVo.manageType }"/>
						</c:set>
					       	 ${manageType }
						</div>
		     			<label class="form_label col-2">责任认定书类型（查勘）</label>
						<div class="form_input col-2">
							${resultList.subrogationCheckDataVo.subCertiType }
						</div>
						<label class="form_label col-2">代位索赔申请书标志（查勘）</label>
						<div class="form_input col-1">
						<c:set var="subClaimFlag">
								<app:codetrans codeType="YN01" codeCode="${resultList.subrogationCheckDataVo.subClaimFlag }"/>
							</c:set>
							${subClaimFlag }
						</div>
		     		</div>
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">保险事故分类（定核损）</label>
						<div class="form_input col-2">
						<c:set var="accidentType">
							<app:codetrans codeType="DWAccidentType" codeCode="${resultList.subrogationEstimateDataVo.accidentType }"/>
						</c:set>
						${accidentType }
						</div>
		     			<label class="form_label col-2">事故责任划分（定核损）</label>
						<div class="form_input col-2">
						<c:set var="accidentLiability">
							<app:codetrans codeType="DWAccidentLiability" codeCode="${resultList.subrogationEstimateDataVo.accidentLiability}"/>
						</c:set>
					       ${accidentLiability }
						</div>
		     		</div>
		     		
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">事故处理方式（定核损）</label>
						<div class="form_input col-2">
						<c:set var="manageType">
						<app:codetrans codeType="DWOptionType" codeCode="${resultList.subrogationEstimateDataVo.manageType }"/>
						</c:set>
					       	 ${manageType }
						</div>
		     			<label class="form_label col-2">责任认定书类型（定核损）</label>
						<div class="form_input col-2">
					       	 	 ${resultList.subrogationEstimateDataVo.subCertiType }
						</div>
						<label class="form_label col-2">代位索赔申请书标志（定核损）</label>
						<div class="form_input col-1">
						<c:set var="subClaimFlag">
								<app:codetrans codeType="YN01" codeCode="${resultList.subrogationEstimateDataVo.subClaimFlag}"/>
							</c:set>
							 ${subClaimFlag }
						</div>
		     		</div>
		     		
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">责任认定书类型（单证）</label>
						<div class="form_input col-2">
					     	 ${resultList.subrogationDocVo.subCertiType }
						</div>
		     			<label class="form_label col-2">代位索赔申请书标志（单证）</label>
						<div class="form_input col-2">
						<c:set var="subClaimFlag">
								<app:codetrans codeType="YN01" codeCode="${resultList.subrogationDocVo.subClaimFlag}"/>
							</c:set>
					        ${subClaimFlag }	
						</div>
						<label class="form_label col-2">保险事故分类（结案）</label>
						<div class="form_input col-1">
						<c:set var="accidentType">
							<app:codetrans codeType="DWAccidentType" codeCode="${resultList.subrogationClaimCloseDataVo.accidentType }"/>
						</c:set>
						${accidentType }
						</div>
		     		</div>
		     		
		     			<div class="row mb-3 cl">
		     		<label class="form_label col-2">结案时间</label>
						<div class="form_input col-2">
						<c:set var="endcaseDate">
								<fmt:formatDate value="${resultList.subrogationClaimCloseDataVo.endcaseDate }" pattern="yyyy-MM-dd"/>
						</c:set>
					       	${endcaseDate }
						</div>
		     			<label class="form_label col-2">赔款总金额</label>
						<div class="form_input col-2">
					       ${resultList.subrogationClaimCloseDataVo.claimAmount }	
						</div>
						<label class="form_label col-2">其他费用</label>
						<div class="form_input col-1">
							${resultList.subrogationClaimCloseDataVo.otherFee }
						</div>
		     		</div>
		     	</c:if>
		     		<c:if test="${flags =='2' }">
		 			<div class="row mb-3 cl">
			     		<label class="form_label col-2">结算码</label>
						<div class="form_input col-4">
							${resultList.subrogationBasePartVo.recoveryCode }
						</div>
						<label class="form_label col-2">结算码状态</label>
						<c:set var="recoveryCodeStatus">
							<app:codetrans codeType="RecoveryCodeStatus" codeCode="${resultList.subrogationBasePartVo.recoveryCodeStatus }"/>
						</c:set>
						<div class="form_input col-2">
							${recoveryCodeStatus }
						</div>
						
		     		</div>
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">对方案件理赔编码</label>
						<div class="form_input col-4">
							${resultList.subrogationBasePartVo.claimSequenceNo }
						</div>
		     			<label class="form_label col-2">对方案件状态</label>
						<div class="form_input col-2">
						<c:set var="claimStatus">
							<app:codetrans codeType="ClaimStatus" codeCode="${resultList.subrogationBasePartVo.claimStatus }"/>
						</c:set>
					       	${claimStatus }
						</div>
						
		     		</div>
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">对方案件注销原因</label>
						<div class="form_input col-9">
							${resultList.subrogationBasePartVo.cancelCause }
						</div>
		     			
		     		</div>
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">保单号</label>
						<div class="form_input col-2">
						
					       ${resultList.subrogationReportDataVo.policyNo }
						</div>
		     			<label class="form_label col-2">报案号</label>
						<div class="form_input col-2">
					       	  ${resultList.subrogationReportDataVo.claimNotificationNo }
						</div>
						<label class="form_label col-2">报案时间</label>
						<div class="form_input col-1">
							<c:set var="reportTime">
								<fmt:formatDate value="${resultList.subrogationReportDataVo.notificationTime }" pattern="yyyy-MM-dd"/>
							</c:set>
					       	  ${reportTime }
						</div>
		     		</div>
		     	<div class="row mb-3 cl">
		     		<label class="form_label col-2">报案人姓名</label>
						<div class="form_input col-2">
					       	${resultList.subrogationReportDataVo.reporter}
						</div>
		     			<label class="form_label col-2">出险驾驶员名称</label>
						<div class="form_input col-2">
					       	 	${resultList.subrogationReportDataVo.driverName }
						</div>
						<label class="form_label col-1">出险时间</label>
						<div class="form_input col-2">
						<c:set var="accidentTime">
								<fmt:formatDate value="${resultList.subrogationReportDataVo.lossTime }" pattern="yyyy-MM-dd"/>
						</c:set>
						${accidentTime }
						</div>
		     		</div>
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">出险地点</label>
						<div class="form_input col-2">
					      	${resultList.subrogationReportDataVo.lossArea }
						</div>
		     			
		     		</div>
		     			<div class="row mb-3 cl">
		     		<label class="form_label col-2">出险经过</label>
						<div class="form_input col-5">
					       ${resultList.subrogationReportDataVo.lossDesc }
						</div>
		     			<label class="form_label col-2">出险标的车车牌号码</label>
						<div class="form_input col-1">
					       	${resultList.subrogationReportDataVo.licensePlateNo }
						</div>
		     		</div>
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">出险标的车车牌种类</label>
						<div class="form_input col-2">
						<c:set var="vehicleType">
								<app:codetrans codeType="LicenseKindCode" codeCode="${resultList.subrogationReportDataVo.licensePlateType }"/>
						</c:set>
					       ${vehicleType }
						</div>
		     			<label class="form_label col-2">事故责任划分（报案）</label>
						<div class="form_input col-2">
						<c:set var="accidentLiability">
								<app:codetrans codeType="DWAccidentLiability" codeCode="${resultList.subrogationReportDataVo.accidentLiability }"/>
						</c:set>
					       	  ${accidentLiability }
						</div>
						<label class="form_label col-2">事故处理方式（报案）</label>
						<div class="form_input col-1">
						<c:set var="manageType">
						<app:codetrans codeType="DWOptionType" codeCode="${resultList.subrogationReportDataVo.optionType }"/>
						</c:set>
							 ${manageType }
						</div>
		     		</div>
		     		
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">立案号</label>
						<div class="form_input col-2">
					       ${resultList.subrogationClaimDataVo.claimRegistrationNo }
						</div>
		     			<label class="form_label col-2">立案时间</label>
						<div class="form_input col-2">
							<c:set var="registrationTime">
								<fmt:formatDate value="${resultList.subrogationClaimDataVo.claimRegistrationTime }" pattern="YYYY-MM-DD"/>
					       	</c:set>
					       	${registrationTime }
						</div>
						<label class="form_label col-2">估损金额</label>
						<div class="form_input col-1">
							${resultList.subrogationClaimDataVo.estimatedLossAmount }
						</div>
		     		</div>
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">保险事故分类（查勘）</label>
						<div class="form_input col-2">
						<c:set var="accidentType">
							<app:codetrans codeType="DWAccidentType" codeCode="${resultList.subrogationCheckDataVo.accidentType }"/>
						</c:set>
					       	 ${accidentType }
						</div>
		     			<label class="form_label col-2">事故责任划分（查勘）</label>
						<div class="form_input col-2">
						<c:set var="accidentLiability">
								<app:codetrans codeType="DWAccidentLiability" codeCode="${resultList.subrogationCheckDataVo.accidentLiability }"/>
						</c:set>
					       	${accidentLiability }
						</div>
		     		</div>
		     		
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">事故处理方式（查勘）</label>
						<div class="form_input col-2">
						<c:set var="manageType">
						<app:codetrans codeType="DWOptionType" codeCode="${resultList.subrogationCheckDataVo.optionType }"/>
						</c:set>
					       	 ${manageType }
						</div>
		     			<label class="form_label col-2">责任认定书类型（查勘）</label>
						<div class="form_input col-2">
							${resultList.subrogationCheckDataVo.subCertiType }
						</div>
						<label class="form_label col-2">代位索赔申请书标志（查勘）</label>
						<div class="form_input col-1">
						<c:set var="subClaimFlag">
								<app:codetrans codeType="YN01" codeCode="${resultList.subrogationCheckDataVo.subClaimFlag }"/>
							</c:set>
							${subClaimFlag }
						</div>
		     		</div>
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">保险事故分类（定核损）</label>
						<div class="form_input col-2">
						<c:set var="accidentType">
							<app:codetrans codeType="DWAccidentType" codeCode="${resultList.subrogationEstimateDataVo.accidentType }"/>
						</c:set>
						${accidentType }
						</div>
		     			<label class="form_label col-2">事故责任划分（定核损）</label>
						<div class="form_input col-2">
						<c:set var="accidentLiability">
							<app:codetrans codeType="DWAccidentLiability" codeCode="${resultList.subrogationEstimateDataVo.accidentLiability}"/>
						</c:set>
					       ${accidentLiability }
						</div>
		     		</div>
		     		
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">事故处理方式（定核损）</label>
						<div class="form_input col-2">
						<c:set var="manageType">
						<app:codetrans codeType="DWOptionType" codeCode="${resultList.subrogationEstimateDataVo.manageType }"/>
						</c:set>
					       	 ${manageType }
						</div>
		     			<label class="form_label col-2">责任认定书类型（定核损）</label>
						<div class="form_input col-2">
					       	 	 ${resultList.subrogationEstimateDataVo.subCertiType }
						</div>
						<label class="form_label col-2">代位索赔申请书标志（定核损）</label>
						<div class="form_input col-1">
						<c:set var="subClaimFlag">
								<app:codetrans codeType="YN01" codeCode="${resultList.subrogationEstimateDataVo.subClaimFlag}"/>
							</c:set>
							 ${subClaimFlag }
						</div>
		     		</div>
		     		
		     		<div class="row mb-3 cl">
		     		<label class="form_label col-2">责任认定书类型（单证）</label>
						<div class="form_input col-2">
					     	 ${resultList.subrogationDocVo.subCertiType }
						</div>
		     			<label class="form_label col-2">代位索赔申请书标志（单证）</label>
						<div class="form_input col-2">
						<c:set var="subClaimFlag">
								<app:codetrans codeType="YN01" codeCode="${resultList.subrogationDocVo.subClaimFlag}"/>
							</c:set>
					        ${subClaimFlag }	
						</div>
						<label class="form_label col-2">保险事故分类（结案）</label>
						<div class="form_input col-1">
						<c:set var="accidentType">
							<app:codetrans codeType="DWAccidentType" codeCode="${resultList.subrogationClaimCloseDataVo.accidentType }"/>
						</c:set>
						${accidentType }
						</div>
		     		</div>
		     		
		     			<div class="row mb-3 cl">
		     		<label class="form_label col-2">结案时间</label>
						<div class="form_input col-2">
						<c:set var="endcaseDate">
								<fmt:formatDate value="${resultList.subrogationClaimCloseDataVo.endcaseDate }" pattern="yyyy-MM-dd"/>
						</c:set>
					       	${endcaseDate }
						</div>
		     			<label class="form_label col-2">赔款总金额</label>
						<div class="form_input col-2">
					       ${resultList.subrogationClaimCloseDataVo.claimAmount }	
						</div>
						<label class="form_label col-2">其他费用</label>
						<div class="form_input col-1">
							${resultList.subrogationClaimCloseDataVo.otherFee }
						</div>
		     		</div>
		     	</c:if>
		     </div>
		     </div>
</div>
	<script type="text/javascript">
			function query(types){
				var a= $("#registNo").val();
				var b =  $("#comCode").val();
				var c =  $("#recoveryCode").val();
				var url  = "?registNo="+a+"&riskCodeSub="+b+"&recoveryCode="+c+"&types="+types;
				
				var goUrl ="/claimcar/subrogationQuery/claimRecoverySearch.do"+url;
				openTaskEditWin("代位求偿理赔查询",goUrl);
		}
			function queryBaoDan(){
				var a= $("#registNo").val();
				var b =  $("#comCode").val();
				var c =  $("#recoveryCode").val();
				var url  = "?registNo="+a+"&riskCodeSub="+b+"&recoveryCode="+c;
				
				var goUrl ="/claimcar/subrogationQuery/claimBaodanSearch.do"+url;
				openTaskEditWin("代位求偿理赔查询",goUrl);
		}
	</script>
</body>
</html>
