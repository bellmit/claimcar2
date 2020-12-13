<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>

	<!-- /** 1-待互审,2-无需互审,3-互审中,4-待清算,5-已支付,6-零清算,9-失效,10-待支付,11-延期,12-暂缓支付 */
	private String recoveryCodeStatus;// 清算码状态

	private Date failureTime;// 清算码失效时间

	/**
	 * 1-放弃追偿,2-锁定错误, 3-系统自动失效(非保险公司通过“锁定取消”主动选择，由其他功能引发的平台系统自动置为失效，例如：放弃追偿、案件注销等)
	 */
	private String failureCause;// 清算码失效原因代码

	private Date lockedTime;// 锁定时间

	private Date recoveryStartTime;// 开始追偿时间

	private String coverageCode;// 追偿/清付险种

	private String recoverAmount;// 追偿金额-追偿方发起追偿的金额

	private String compensateAmount;// 清付金额-被追偿方清付的金额

	// eq:AAIC01-安信农业保险公司,ABIC01-安邦保险公司,ACIC01-安诚保险公司
	private String insurerCode;// 追偿方保险公司

	private String insurerArea;// 追偿方承保地区

	/** 1-强制三者险,2-商业三者险,3-商业车损险,9-其它 */
	private String coverageType;// 追偿方保单险种类型

	private String PolicyNo;// 追偿方保单号

	private String claimNotificationNo;// 追偿方报案号

	/**
	 * 01-已报案,02-报案注销,03-已立案,04-立案注销,05 -已结案,10-拒赔,11-查勘/定损/核损, 11-重开,12-单证收集,13-厘算核赔,14-赔款支付,15-查勘/定损/核损注销,16-单证收集注销,17-厘算核赔注销
	 */
	private String claimStatus;// 追偿方案件状态

	// 同上
	private String claimProgress;// 追偿方案件进展

	private String licensePlateNo;// 追偿方车辆号牌号码

	private String licensePlateType;// 追偿方车辆号牌种类

	private String engineNo;// 追偿方车辆发动机号

	private String vin;// 追偿方车辆VIN码

	private String berecoveryInsurerCode;// 被追偿方保险公司

	private String berecoveryInsurerArea;// 被追偿方承保地区

	private String berecoveryCoverageType;// 被追偿方保单险种类型

	private String berecoveryPolicyNo;// 被追偿方保单号

	private String berecoveryClaimNotificationNo;// 被追偿方报案号

	private String berecoveryClaimStatus;// 被追偿方案件状态

	private String berecoveryClaimProgress;// 被追偿方案件进展

	private String berecoveryLicensePlateNo;// 被追偿方车辆号牌号码

	private String berecoveryLicensePlateType;// 被追偿方车辆号牌种类

	private String BerecoveryEngineNo;// 被追偿方车辆发动机号

	private String berecoveryVin;// 被追偿方车辆VIN码

 -->







  	<div class="table_wrap">
				<div class="table_title f14">更多信息</div>
					<div class="table_cont basicInforoVer">
						<div class="formtable">	
							<div class="row cl">
								<label class="form_label col-2">结算码失效时间:</label>
								<div class="form_input col-2">
								<app:date  date='${copyInformationSubrogationViewVo.failureTime}'/>	
								</div>
								<label class="form_label col-2">结算码失效原因代码:</label>
								<div class="form_input col-2">
									<c:choose>
									<c:when test="${copyInformationSubrogationViewVo.failureCause eq '1'}">
									 放弃追偿
									</c:when>
									<c:when test="${copyInformationSubrogationViewVo.failureCause eq '2'}">
									 锁定错误
									</c:when>
									<c:when test="${copyInformationSubrogationViewVo.failureCause eq '3'}">
									系统自动失效
									</c:when>
									</c:choose>
								</div>
								<label class="form_label col-2">开始追偿时间:</label>
								<div class="form_input col-2">
									<app:date  date='${copyInformationSubrogationViewVo.recoveryStartTime}'/>	
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">清付金额:</label>
								<div class="form_input col-2">
									${copyInformationSubrogationViewVo.compensateAmount}
								</div>
								<label class="form_label col-2">追偿方承保地区:</label>
								<div class="form_input col-2">
									<app:codetrans codeType="DWInsurerArea" codeCode="${copyInformationSubrogationViewVo.insurerArea}" />
								</div>
								<label class="form_label col-2">追偿方保单号:</label>
								<div class="form_input col-2">
									${copyInformationSubrogationViewVo.policyNo}
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">追偿方报案号:</label>
								<div class="form_input col-2">
									${copyInformationSubrogationViewVo.claimNotificationNo}
								</div>
								<label class="form_label col-2">追偿方案件状态:</label>
								<div class="form_input col-2"> 
									<app:codetrans codeType="ClaimStatus" codeCode="${copyInformationSubrogationViewVo.claimStatus }" />
								</div>
								<label class="form_label col-2">追偿方案件进展:</label>
								<div class="form_input col-2">
									<app:codetrans codeType="ClaimStatus" codeCode="${copyInformationSubrogationViewVo.claimProgress }" />
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">追偿方车牌号码:</label>
								<div class="form_input col-2">
									${copyInformationSubrogationViewVo.licensePlateNo}
								</div>
								<label class="form_label col-2">追偿方车牌种类:</label>
								<div class="form_input col-2 overTextPre">
									${copyInformationSubrogationViewVo.licensePlateType}
								</div>
								<label class="form_label col-2">追偿方车辆发动机号:</label>
								<div class="form_input col-2">
									${copyInformationSubrogationViewVo.engineNo }
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">追偿方车辆VIN码:</label>
								<div class="form_input col-2">
									${copyInformationSubrogationViewVo.vin }
								</div>
								
								<label class="form_label col-2">被追偿方承保地区:</label>
								<div class="form_input col-2" id="offsetVeri">
									<app:codetrans codeType="DWInsurerArea" codeCode="${copyInformationSubrogationViewVo.berecoveryInsurerArea}" />
								</div>
								<label class="form_label col-2">被追偿方保单险种类型:</label>
								<div class="form_input col-2" id="offsetVeriRate">
									<app:codetrans codeType="DWCoverageType" codeCode="${copyInformationSubrogationViewVo.berecoveryCoverageType}" />
									</font>
								</div>
							</div>
							<div class="row cl">
								
									<label class="form_label col-2">被追偿方保单号:</label>
									<div class="form_input col-2">
										${copyInformationSubrogationViewVo.berecoveryPolicyNo}
									</div>
									<label class="form_label col-2">被追偿方报案号:</label>
									<div class="form_input col-2">
										${copyInformationSubrogationViewVo.berecoveryClaimNotificationNo}
									</div>
								
								<label class="form_label col-2">被追偿方案件状态:</label>
								<div class="col-2">
									<div class="form_input col-6 select-acc">
										<app:codetrans codeType="ClaimStatus" codeCode="${copyInformationSubrogationViewVo.berecoveryClaimStatus }"/> &nbsp;&nbsp;&nbsp; ${lossCarMainVo.indemnityDutyRate }
									</div>
								</div>
							</div>
							<div class="row cl">
								
									<label class="form_label col-2">被追偿方车辆发动机号:</label>
									<div class="form_input col-2">
										${copyInformationSubrogationViewVo.berecoveryEngineNo}
									</div>
									<label class="form_label col-2">被追偿方车辆VIN码:</label>
									<div class="form_input col-2">
										${copyInformationSubrogationViewVo.berecoveryVin}
									</div>
							</div>
							
							
							
					</div>	
				</div>
			</div>	
			
	<div class="formtable">
		<div class="table_cont table_list">
		
			<table border="1" class="table table-border table-bg">
			   <c:forEach var="signVo" items="${copyInformationSubrogationViewVo.checkListVo}" varStatus="status">
				<thead class="text-c">
				    
					<tr>
						<th style="width:10%"><a onclick="moreMessage('${status.index}')"><font size='8' color='blue'>+</font></a></th>
						<th style="width: 45%" colspan="3">互审状态</th>
						<th style="width: 45%" colspan="3">互审通过时间</th>
					</tr>
					
				</thead>
				<tbody class="text-c">
					
						<tr>
							<td></td>
							<td colspan="3">
								<app:codetrans codeType="CheckStats" codeCode="${signVo.checkStats}"/>
							</td>
							<td colspan="3">
								<app:date  date='${signVo.checkEndDate}'/>	
							</td>
							
						</tr>
						
						
				 
				      <thead class="text-c showTable${status.index} hidden">
				        
					     <tr>
					     	<th></th>
						    <th>追偿金额</th>
						    <th>清付金额</th>
					      	<th>互审时间</th>
					      	<th>互审意见</th>
						    <th>审核方类型</th>
					      	<th>互审状态</th>
					    </tr>
				       </thead>
				         <tbody class="text-c showTable${status.index} hidden">
						  <c:forEach var="sign1Vo" items="${signVo.checkDetailListVo}" varStatus="status">
						   
						   
						  <tr>
						   <td></td>
						   <td>${sign1Vo.recoverAmount}</td>
						    <td>${sign1Vo.compensateAmount}</td>
						    <td><app:date  date='${sign1Vo.checkDate}'/></td>
						    <td>${sign1Vo.checkOpinion}</td>
						    <td><c:choose>
						    <c:when test="${sign1Vo.checkOwnType eq '1'}">
						           追偿方
						    </c:when>
						    <c:otherwise>
						           责任对方
						    </c:otherwise>
						    
						    </c:choose></td>
						    
						    <td><app:codetrans codeType="CheckStats" codeCode="${sign1Vo.checkStats}"/></td>
						</tr>
					</c:forEach>
				</tbody>    
					
						</c:forEach>
				  
				</tbody>
				
			</table>
		</div>
		
		</div>
		
		<script type="text/javascript">
		function moreMessage(index){
		   // 判断当前是否隐藏
		   var secondTable = $(".showTable"+index);
		   if($(secondTable).hasClass("hidden")){
			   $(secondTable).removeClass("hidden");
		   }else{
			   $(secondTable).addClass("hidden");
		   }
		}
		
		</script>
			
</body>
</html>