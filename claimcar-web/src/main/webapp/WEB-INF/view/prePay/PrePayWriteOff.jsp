<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<div class="fixedmargin page_wrap">
	<!-- 按钮组 -->
	<div class="top_btn">
		<a class="btn  btn-primary" onclick="viewEndorseInfo('${compensateVo.registNo}')">保单批改记录</a>
		<a class="btn  btn-primary" onclick="viewPolicyInfo('${compensateVo.registNo}')">保单详细信息</a>
		<a class="btn  btn-primary" onclick="openTaskEditWin('报案详细信息','/claimcar/regist/edit.do?registNo=${compensateVo.registNo}')">报案详细信息</a>
		<a class="btn  btn-primary" onclick="imageMovieUpload('${wfTaskVo.taskId}')">信雅达影像上传</a>
		<a class="btn btn-primary" onclick="lawsuit('${compensateVo.registNo }','${flowNodeCode}')">诉讼</a>
		<a class="btn  btn-primary" onclick="createRegistMessage('${compensateVo.registNo }','')">案件备注</a>
		<a class="btn  btn-primary">打印预付申请书</a>
		<c:choose>
			<c:when test="${handlerStatus =='0'}">
				<a class="btn  btn-primary" onclick="prePayCancel('${flowTaskId}')">预付注销</a>
			</c:when>
			<c:otherwise>
				<a class="btn  btn-disabled">预付注销</a>
			</c:otherwise>
		</c:choose>
		<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
			
	   </div>
	<br>
	<div class="table_cont">
		<form id="prePayForm" role="form" method="post" name="fm">
			<div class="table_wrap">
				<div class="table_title f14">保单信息</div>
				<div class="table_cont">
					<div class="formtable">
						<div class="row cl">
							<input type="hidden" name="compensateVo.compensateNo" value="${compensateVo.compensateNo }">
							<input type="hidden" name="compensateVo.claimNo" value="${compensateVo.claimNo }">
							<input type="hidden" name="compensateVo.registNo" value="${compensateVo.registNo }">
							<input type="hidden" name="compensateVo.policyNo" value="${compensateVo.policyNo }">
							<input type="hidden" name="compensateVo.riskCode" value="${compensateVo.riskCode }">
							<input type="hidden" name="compensateVo.makeCom" value="${compensateVo.makeCom }">
							<input type="hidden" name="compensateVo.comCode" value="${compensateVo.comCode }">
							<input type="hidden" name="compensateVo.caseType" value="${compensateVo.caseType }">
							<input type="hidden" name="compensateVo.dutyCode" value="${compensateVo.indemnityDuty }">
							<input type="hidden" name="compensateVo.dutyRate" value="${compensateVo.indemnityDutyRate }">
							<input type="hidden" name="compensateVo.compensateType" value="Y">
							<input type="hidden" id="handlerStatus" name="handlerStatus" value="${handlerStatus }">
							<input type="hidden" id="workStatus" name="workStatus" value="${workStatus }">
							<input type="hidden" name="submitNextVo.flowTaskId" value="${flowTaskId }">
							<label class="form_label col-1">保单号</label>
							<div class="form_input col-3">${prpLClaimVo.policyNo }</div>
							<label class="form_label col-1">险种代码</label>
							<div class="form_input col-3">
								<app:codetrans codeType="RiskCode" codeCode="${prpLClaimVo.riskCode }" />
							</div>
							<label class="form_label col-1">被保险人</label>
							<div class="form_input col-3">${prpLCMainVo.insuredName }</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">保险起期</label>
							<div class="form_input col-3">
								<app:date date="${prpLCMainVo.startDate }" format="yyyy年MM月dd日" />
								&nbsp;${prpLCMainVo.startHour }时
							</div>
							<label class="form_label col-1">保险止期</label>
							<div class="form_input col-3">
								<app:date date="${prpLCMainVo.endDate }" format="yyyy年MM月dd日" />
								&nbsp;${prpLCMainVo.endHour }时
							</div>
							<label class="form_label col-1">承保公司</label>
							<div class="form_input col-3">
								<app:codetrans codeType="ComCode" codeCode="${prpLCMainVo.comCode }" />
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">车牌号码</label>
							<div class="form_input col-3">${prpLCMainVo.prpCItemCars[0].licenseNo }</div>
							<label class="form_label col-1">号牌底色</label>
							<div class="form_input col-3">
								<app:codetrans codeType="LicenseColorCode" codeCode="${prpLCMainVo.prpCItemCars[0].licenseColorCode }" />
							</div>
							<label class="form_label col-1">车辆种类</label>
							<div class="form_input col-3">
								<app:codetrans codeType="CarKind" codeCode="${prpLCMainVo.prpCItemCars[0].carKindCode }" />
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">厂牌型号</label>
							<div class="form_input col-3">${prpLCMainVo.prpCItemCars[0].brandName }</div>
						</div>
					</div>
				</div>
			</div>


			<div class="table_wrap">
				<div class="table_title f14">出险信息</div>
				<div class="table_cont">
					<div class="formtable">
						<div class="row cl">
							<label class="form_label col-1">报案号</label>
							<div class="form_input col-3">${prpLClaimVo.registNo }</div>
							<label class="form_label col-1">赔案类别</label>
							<div class="form_input col-3">
							正常赔案
								<!--<app:codetrans codeType="CaseCode" codeCode="${prpLClaimVo.claimType }" />-->
							</div>
							<label class="form_label col-1">报案人</label>
							<div class="form_input col-3">${prpLregistVo.reportorName }</div>
							<%-- <label class="form_label col-1">历史出险次数</label>
							<div class="form_input col-3">
								${prpLCMainVo.registTimes }<span>次</span>
								<input class="btn ml-5" id="" onclick="caseDetails('${prpLClaimVo.registNo}')" type="button" value="...">
							</div> --%>
						</div>
						<div class="row cl">
							<label class="form_label col-1">出险地点</label>
							<div class="form_input col-3">${prpLregistVo.damageAddress }</div>
							<label class="form_label col-1">出险原因</label>
							<div class="form_input col-2">
								<app:codetrans codeType="DamageCode" codeCode="${prpLClaimVo.damageCode }" />
							</div>
							<label class="form_label col-2">报案人与被保险人关系</label>
							<div class="form_input col-3">
								<app:codetrans codeType="InsuredIdentity" codeCode="${prpLregistVo.reportorRelation }" />
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">出险日期</label>
							<div class="form_input col-3">
								<app:date date="${prpLClaimVo.damageTime }" format="yyyy年MM月dd日" />
							</div>
							<label class="form_label col-1">报案日期</label>
							<div class="form_input col-3">
								<app:date date="${prpLClaimVo.reportTime }" format="yyyy年MM月dd日" />
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">出险经过说明</label>
							<div class="form_input col-3" title="${prpLregistVo.prpLRegistExt.dangerRemark }">${prpLregistVo.prpLRegistExt.dangerRemark }</div>
							<label class="form_label col-1">报案人电话</label>
							<div class="form_input col-3">${prpLregistVo.linkerMobile }<c:if test="${!empty prpLregistVo.linkerPhone}">,</c:if>${prpLregistVo.linkerPhone }</div>
						</div>
					</div>
				</div>
			</div>

			<div class="table_wrap">
				<div class="table_title f14">历次预付信息</div>
				<div class="table_cont">
					<div class="formtable">
						<table class="table table-bordered table-bg">
							<thead class="text-c">
								<tr>
									<th>预付计算书号</th>
									<th>保单号</th>
									<th>预付类型</th>
									<th>预付金额</th>
									<th>核赔通过时间</th>
								</tr>
							</thead>
							<tbody class="text-c">
								<tr>
									<c:forEach var="compensateVo" items="${compensateVos }" varStatus="Status">
										<tr>
											<td>${compensateVo.compensateNo }</td>
											<td>${compensateVo.policyNo }</td>
											<td>
											<c:set var="extVo" value="${compensateVo.prpLCompensateExt}" />
												<c:if test="${compensateVo.riskCode eq '1101' && extVo.writeOffFlag eq '0'}">交强预付</c:if>
												<c:if test="${compensateVo.riskCode eq '1101' && extVo.writeOffFlag ne '0'}">交强预付冲销</c:if>
												
												<c:if test="${compensateVo.riskCode ne '1101' && extVo.writeOffFlag eq '0'}">商业预付</c:if>
												<c:if test="${compensateVo.riskCode ne '1101' && extVo.writeOffFlag ne '0'}">商业预付冲销</c:if>
												
											</td>
											<td>${compensateVo.sumAmt }</td>
											<td>
												<fmt:formatDate value='${compensateVo.underwriteDate }' pattern='yyyy-MM-dd HH:mm:ss' />
											</td>
										</tr>
									</c:forEach>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>

			<div class="table_wrap">
				<div class="table_title f14">预付基本信息</div>
				<div class="table_cont">
					<div class="formtable">
						<div class="row cl">
							<label class="form_label col-1">预付号</label>
							<div class="form_input col-3" >${compensateVo.compensateNo }</div>
							<label class="form_label col-1">立案号</label>
							<div class="form_input col-3">${prpLClaimVo.claimNo }</div>
							<label class="form_label col-1">预付类型</label>
							<div class="form_input col-3">
								<c:if test="${prpLClaimVo.riskCode eq '1101' && compensateVo.prpLCompensateExt.writeOffFlag eq '0'}">交强预付</c:if>
								<c:if test="${prpLClaimVo.riskCode eq '1101' && compensateVo.prpLCompensateExt.writeOffFlag ne '0'}">交强预付冲销</c:if>
												
							    <c:if test="${prpLClaimVo.riskCode ne '1101' && compensateVo.prpLCompensateExt.writeOffFlag eq '0'}">商业预付</c:if>
								<c:if test="${prpLClaimVo.riskCode ne '1101' && compensateVo.prpLCompensateExt.writeOffFlag ne '0'}">商业预付冲销</c:if>
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">本次预付金额合计</label>
							<div class="form_input col-3">
								<c:choose>
									<c:when test="${handlerStatus eq '0' && workStatus eq '0'}">
										<input type="text" style="border: none" readonly="readonly" class="input-text ready-only" name="compensateVo.sumAmt"
										value="${sumAmt}">
									</c:when>
									<c:otherwise>
										<input type="text" style="border: none" readonly="readonly" class="input-text ready-only" name="compensateVo.sumAmt"
										value="${empty compensateVo.sumAmt?0.00:compensateVo.sumAmt }">
									</c:otherwise>
								</c:choose>
								<c:if test="">交强预付</c:if>
											
								
							</div>
							<label class="form_label col-1">已预付金额</label>
							<div class="form_input col-3">${sumAllAmt }</div>
						</div>
					</div>
				</div>
			</div>

			<div class="table_wrap">
				<div class="table_title f14">预付赔款</div>
				<div class="table_cont">
					<div class="formtable">
						<div class="table_con">
							<table class="table table-bordered table-bg">
								<thead class="text-c">
									<tr>
										<th width="15%">险别</th>
										<th width="15%">预付损失类型</th>
										<th width="6%">预付款项类型</th>
										<th width="6%">预付金额</th>
										<th width="6%">收款人</th>
										<th width="10%">例外标志</th>
										<th width="10%">例外原因</th>
										<th width="10%">收款人帐号</th>
										<th width="10%">开户银行</th>
										<th >摘要</th>
									</tr>
								</thead>
								<tbody class="text-c" id="IndemnityTbody">
									<input type="hidden" id="indeSize" value="${fn:length(prePayPVos)}">
									<c:forEach var="prePayPVo" items="${prePayPVos }" varStatus="status">
										<tr>
											<td>
												<input type="hidden" value="${prePayPVo.compensateNo }" name="prePayPVo[${status.index + size }].compensateNo">
												<input type="hidden" value="${prePayPVo.id }" name="prePayPVo[${status.index + size }].id">
												<input type="hidden" value="${prePayPVo.payeeId }" name="prePayPVo[${status.index + size }].payeeId">
												<input type="hidden" value="${prePayPVo.feeType }" name="prePayPVo[${status.index + size }].feeType">
												<input type="hidden" value="${prePayPVo.kindCode }" name="prePayPVo[${status.index + size }].kindCode">
												<input type="hidden" value="${prePayPVo.riskCode }" name="prePayPVo[${status.index + size }].riskCode">
												<input type="hidden" value="${prePayPVo.currency }" name="prePayPVo[${status.index + size }].currency">
												<input type="hidden" value="${prePayPVo.invoiceType }" name="prePayPVo[${status.index + size }].invoiceType">
												<input type="hidden" value="${prePayPVo.addTaxRate }" name="prePayPVo[${status.index + size }].addTaxRate">	
												<input type="hidden" value="${prePayPVo.addTaxValue }" name="prePayPVo[${status.index + size }].addTaxValue">
												<input type="hidden" value="${prePayPVo.noTaxValue }" name="prePayPVo[${status.index + size }].noTaxValue">
												<app:codetrans codeType="KindCode" codeCode="${prePayPVo.kindCode }" riskCode="${prpLregistVo.riskCode}" />
											</td>
											<td>
												<input type="hidden" value="${prePayPVo.lossName }" name="prePayPVo[${status.index + size }].lossName">
												${prePayPVo.lossName }
											</td>
											<td>
												<input type="hidden" value="${prePayPVo.chargeCode }" name="prePayPVo[${status.index + size }].chargeCode">
												<app:codetrans codeType="PrePayFeeType" codeCode="${prePayPVo.chargeCode }" />
											</td>
											<td>
											<c:choose>
												<c:when test="${ handlerStatus == 0 || handlerStatus == 1}" >
													<input type="text" class="input-text payPAmt" name = "prePayPVo[${status.index + size }].payAmt" value="${prePayPVo.payAmt <0 ?  -prePayPVo.payAmt:prePayPVo.payAmt}"  max ="${prePayPVo.maxAmt }" datatype="amount"/>
												</c:when>
												 <c:otherwise>
												 	<input type="text" disabled class="input-text payPAmt" name = "prePayPVo[${status.index + size }].payAmt" value="${prePayPVo.payAmt <0 ?  -prePayPVo.payAmt:prePayPVo.payAmt}"  datatype="amount"/>
												 </c:otherwise>
											</c:choose>
											</td>
											<td>
												<input type="hidden" value="${prePayPVo.payeeName }" name="prePayPVo[${status.index + size }].payeeName">
												${prePayPVo.payeeName }
											</td>
											<td>
												<input type="hidden" value="${prePayPVo.otherFlag }" name="prePayPVo[${status.index + size }].otherFlag">
												<app:codetrans codeType="YN10" codeCode="${prePayPVo.otherFlag }" />
											</td>
											<td>
												<input type="hidden" value="${prePayPVo.otherCause }" name="prePayPVo[${status.index + size }].otherCause">
												<app:codetrans codeType="OtherCase" codeCode="${prePayPVo.otherCause }" />
											</td>
											<td>
												<input type="hidden" value="${prePayPVo.accountNo }" name="prePayPVo[${status.index + size }].accountNo">
												${prePayPVo.accountNo }
											</td>
											<td>
												<input type="hidden" value="${prePayPVo.bankName }" name="prePayPVo[${status.index + size }].bankName">
												<app:codetrans codeType="BankCode" codeCode="${prePayPVo.bankName }"/></td>
											<td>
												<input type="hidden" value="${prePayPVo.summary }" name="prePayPVo[${status.index + size }].summary">	
												${prePayPVo.summary }
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>

			<div class="table_wrap">
				<div class="table_title f14">预付费用</div>
				<div class="table_cont">
					<div class="formtable">
						<div class="table_con">
							<table class="table table-bordered table-bg">
								<thead class="text-c">
									<tr>
										<th width="15%">损失险别</th>
										<th width="15%">费用名称</th>
										<th width="14%">费用金额</th>
										<th width="10%">收款人</th>
										<th width="10%">收款人帐号</th>
										<th width="10%">开户银行</th>
										<th width="10%">摘要</th>
									</tr>
								</thead>
								<tbody class="text-c" id="FeeTbody">
									<input type="hidden" id="feeSize" value="${fn:length(prePayFVos)}">
									<c:forEach var="prePayFVo" items="${prePayFVos }" varStatus="status">
										<tr>
											<td>
												<input type="hidden" value="${prePayFVo.compensateNo }" name="prePayFVo[${status.index + size }].compensateNo">	
												<input type="hidden" value="${prePayFVo.id }" name="prePayFVo[${status.index + size }].id">
												<input type="hidden" value="${prePayFVo.payeeId }" name="prePayFVo[${status.index + size }].payeeId">
												<input type="hidden" value="${prePayFVo.kindCode }" name="prePayFVo[${status.index + size }].kindCode">
												<input type="hidden" value="${prePayFVo.feeType }" name="prePayFVo[${status.index + size }].feeType">
												<input type="hidden" value="${prePayFVo.riskCode }" name="prePayFVo[${status.index + size }].riskCode">
												<input type="hidden" value="${prePayFVo.currency }" name="prePayFVo[${status.index + size }].currency">
												<input type="hidden" value="${prePayFVo.invoiceType }" name="prePayFVo[${status.index + size }].invoiceType">
												<input type="hidden" value="${prePayFVo.addTaxRate }" name="prePayFVo[${status.index + size }].addTaxRate">
												<input type="hidden" value="${prePayFVo.addTaxValue }" name="prePayFVo[${status.index + size }].addTaxValue">
												<input type="hidden" value="${prePayFVo.noTaxValue }" name="prePayFVo[${status.index + size }].noTaxValue">
												<app:codetrans codeType="KindCode" codeCode="${prePayFVo.kindCode }" riskCode="${prpLregistVo.riskCode}" />
											</td>
											<td>
												<input type="hidden" value="${prePayFVo.chargeCode }"  name="prePayFVo[${status.index + size }].chargeCode">
												<app:codetrans codeType="ChargeCode" codeCode="${prePayFVo.chargeCode }" />
											</td>
											<td>
												<c:choose>
													<c:when test="${ handlerStatus == 0 || handlerStatus == 1}">
														<input type="text"  name="prePayFVo[${status.index + size }].payAmt" class="input-text payFAmt" value="${prePayFVo.payAmt <0 ? -prePayFVo.payAmt : prePayFVo.payAmt }" max ="${prePayFVo.maxAmt }"  datatype="amount"/>
													</c:when>
													 <c:otherwise>
													 	<input type="text" disabled  name="prePayFVo[${status.index + size }].payAmt" class="input-text payFAmt" value="${prePayFVo.payAmt <0 ? -prePayFVo.payAmt : prePayFVo.payAmt }"  datatype="amount"/>
													</c:otherwise>
												</c:choose>
													</td>
											<td>
												<input type="hidden" value="${prePayFVo.payeeName }"  name="prePayFVo[${status.index + size }].payeeName">
												${prePayFVo.payeeName }</td>
											<td>
												<input type="hidden" value="${prePayFVo.accountNo }"  name="prePayFVo[${status.index + size }].accountNo">
												${prePayFVo.accountNo }
											</td>
											<td>
												<input type="hidden" value="${prePayFVo.accountNo }"  name="prePayFVo[${status.index + size }].bankName">
												<app:codetrans codeType="BankCode" codeCode="${prePayFVo.bankName }"/></td>
											<td>
												<input type="hidden" value="${prePayFVo.summary}"  name="prePayFVo[${status.index + size }].summary">
												${prePayFVo.summary }</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<div id="hideDiv" style="display: none"></div>
		</form>
	</div>
<c:if test="${not empty writeOffList}">
	<div class="table_wrap">
		<div class="table_title f14">预付冲销轨迹</div>
		<div class="formtable">
			<div class="table_cont table_list">
				<table border="1" class="table table-border table-bg">
					<thead class="text-c">
						<tr>
							<th style="width: 12%">预付号</th>
							<th style="width: 12%">类型</th>
							<th style="width: 12%">冲减金额</th>
							<th style="width: 15%">收款人</th>
							<th style="witdh: 12%">冲销人员</th>
							<th style="width: 12%">冲销时间</th>
							<th style="width: 12%">核赔人员</th>
							<th style="width: 12%">核赔时间</th>
						</tr>
					</thead>
					<tbody class="text-c">
						<c:forEach var="writeOff" items="${writeOffList}" varStatus="status">
							<tr>
								<td>${writeOff.compensateNo}</td>
								<td>
										<c:choose>
									    <c:when test="${writeOff.feeType eq 'F'}">
									    <app:codetrans codeType="ChargeCode" codeCode="${writeOff.chargeCode}"/>
									    </c:when>
									     <c:when test="${writeOff.feeType eq 'P'}"> 
									   	 <app:codetrans codeType="PrePayFeeType" codeCode="${writeOff.chargeCode}"/>
									    </c:when>
									</c:choose>
								</td>
								<td>${writeOff.payamt}</td>
								<td>${writeOff.payeeInfo}</td>
								<td>
									 <app:codetrans codeType="UserCode" codeCode="${writeOff.createUser}"/>								
								</td>
								<td>
									<fmt:formatDate  value="${writeOff.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</td>
								<td>
									<app:codetrans codeType="UserCode" codeCode="${writeOff.underwriteUser}"/>
								</td>
								<td>
									<fmt:formatDate  value="${writeOff.underwriteDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</c:if>

	<c:if test="${handlerStatus=='0'||handlerStatus=='2' }">
		<div id="submitDiv" class="text-c">
			<br />
			<input class="btn btn-primary ml-5" onclick="submit()" type="submit" value="冲销">
			<input class="btn btn-primary ml-5" onclick="" type="button" value="取消">
		</div>
	</c:if>
	<!-- 案件备注功能隐藏域 -->
	<input type="hidden" id="nodeCode" value="PrePayWf">
</div>

<script type="text/javascript" src="${ctx}/js/prePay/prePayWriteOff.js"></script>
<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
