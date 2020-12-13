<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<div class="fixedmargin page_wrap">
	<!-- 预付任务处理 -->
	<!-- 按钮组 -->
	<div class="top_btn">
		<a class="btn  btn-primary" onclick="viewEndorseInfo('${compensateVo.registNo}')">保单批改记录</a>
		<a class="btn  btn-primary" onclick="openTaskEditWin('报案详细信息','/claimcar/regist/edit.do?registNo=${compensateVo.registNo}')">报案详细信息</a>
		<a class="btn  btn-primary" onclick="viewPolicyInfo('${compensateVo.registNo}')">保单详细信息</a>
		<a class="btn  btn-primary" onclick="createRegistMessage('${compensateVo.registNo }','${wfTaskVo.nodeCode}')">案件备注</a>

		<a class="btn  btn-primary" onclick="imageMovieUpload('${wfTaskVo.taskId}')">信雅达影像上传</a>
		<a class="btn  btn-primary" onclick="lawsuit('${compensateVo.registNo }','')">诉讼</a>
		<c:choose>
			<c:when test="${handlerStatus =='2' || handlerStatus == '0'}">
				<a class="btn  btn-primary" onclick="prePayCancel('${flowTaskId}')">预付注销</a>
			</c:when>
			<c:otherwise>
				<a class="btn  btn-disabled">预付注销</a>
			</c:otherwise>
		</c:choose>
		<c:if test="${handlerStatus eq '2' || handlerStatus eq '3' }">
			<a class="btn  btn-primary" onclick="AdjustersPrintPrePad('${compensateVo.registNo }','${compensateVo.compensateNo }')">打印预付申请书</a>
		</c:if>
		<c:if test="${handlerStatus ne '2' && handlerStatus ne '3' }">
			<a class="btn  btn-disabled" >打印预付申请书</a>
		</c:if>
		<a class="btn  btn-primary" onclick="payCustomSearch('N')">收款人信息维护</a>
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
							<input type="hidden" id="registNo" name="compensateVo.registNo" value="${compensateVo.registNo }">
							<input type="hidden" id="policyNo" name="compensateVo.policyNo" value="${compensateVo.policyNo }">
							<input type="hidden" name="compensateVo.riskCode" value="${compensateVo.riskCode }">
							<input type="hidden" name="compensateVo.makeCom" value="${compensateVo.makeCom }">
							<input type="hidden" name="compensateVo.comCode" value="${compensateVo.comCode }">
							<input type="hidden" name="compensateVo.caseType" value="${compensateVo.caseType }">
							<input type="hidden" name="compensateVo.indemnityDuty" value="${compensateVo.indemnityDuty }">
							<input type="hidden" name="compensateVo.indemnityDutyRate" value="${compensateVo.indemnityDutyRate }">
							<input type="hidden" name="compensateVo.compensateType" value="Y">
							<input type="hidden" id="handlerStatus" name="handlerStatus" value="${handlerStatus }">
							<input type="hidden" id="flowTaskId" name="submitNextVo.flowTaskId" value="${flowTaskId }">
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

       		<!-- 反洗钱信息 -->
       		<%@include file="CompensateEdit_AntiMoney.jsp" %>

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
								<%-- <app:codetrans codeType="CaseCode" codeCode="${prpLClaimVo.claimType }" /> --%>
							</div>
							<%-- <label class="form_label col-1">历史出险次数</label>
							<div class="form_input col-3">
								${prpLCMainVo.registTimes }<span>次</span>
								<input class="btn ml-5" id="" onclick="caseDetails('${prpLCMainVo.registNo}')" type="button" value="...">
							</div> --%>
							<label class="form_label col-1">报案人电话</label>
							<div class="form_input col-3">${prpLregistVo.linkerMobile }<c:if test="${!empty prpLregistVo.linkerPhone}">,</c:if>${prpLregistVo.linkerPhone }</div>
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
								<app:date date="${prpLClaimVo.damageTime }" format="yyyy年MM月dd日 HH:mm:ss" />
							</div>
							<label class="form_label col-1">报案日期</label>
							<div class="form_input col-3">
								<app:date date="${prpLClaimVo.reportTime }" format="yyyy年MM月dd日 HH:mm:ss" />
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">报案人</label>
							<div class="form_input col-3">${prpLregistVo.reportorName }</div>
							<label class="form_label col-1">出险经过说明</label>
							<div class="form_input col-3" title="${prpLregistVo.prpLRegistExt.dangerRemark }">${prpLregistVo.prpLRegistExt.dangerRemark }</div>
							
						</div>
						<!-- <div class="row cl">
							
						</div> -->
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
							<div class="form_input col-3">${compensateVo.compensateNo }</div>
							<label class="form_label col-1">立案号</label>
							<div class="form_input col-3">${prpLClaimVo.claimNo }</div>
							<label class="form_label col-1">预付类型</label>
							<div class="form_input col-3">
								<c:if test="${prpLClaimVo.riskCode=='1101' }">交强预付</c:if>
								<c:if test="${prpLClaimVo.riskCode!='1101' }">商业预付</c:if>
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">本次预付金额合计</label>
							<div class="form_input col-3">
								<input type="text" style="border: none" readonly="readonly" class="input-text ready-only" name="compensateVo.sumAmt"
									value="${empty compensateVo.sumAmt?0.00:compensateVo.sumAmt }">
							</div>
							<label class="form_label col-1">已预付金额</label>
							<div class="form_input col-3">${sumAllAmt }</div>
							<label class="form_label col-1">已预付费用</label>
							<div class="form_input col-3">${FeiYong }</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">已预付赔款</label>
							<div class="form_input col-3">${PeiKuan }</div>
						</div>
					</div>
				</div>
			</div>

			<div class="table_wrap">
				<div class="table_title f14">预付赔款</div>
				<div class="table_cont">
					<div class="formtable" id="paymentVo">
						<div class="table_con">
							<table class="table table-bordered table-bg">
								<thead class="text-c">
									<tr>
										<th width="4%">
											<button type="button" class="btn btn-plus Hui-iconfont Hui-iconfont-add" onclick="addPayInfo('IndemnityTbody','indeSize','${handlerStatus }')"></button>
										</th>
										<c:if test="${handlerStatus == '3' }">
											<th width="8%">险别</th>
										</c:if>
										<th width="10%">预付损失类型</th>
										<th width="8%">预付款项类型</th>
										<th width="6%">预付金额</th>
										<th width="6%">收款人</th>
										<th width="4%">例外标志</th>
										<th width="9%">例外原因</th>
										<th width="9%">收款人帐号</th>
										<th width="9%">开户银行</th>
										<th>税收信息</th>
										<th width="8%">摘要<font class="must">*</font></th>
										<th width="7%">是否增值税专票</th>
						                <th width="5%">税率</th>
						                <c:if test="${handlerStatus eq '3' && prpLregistVo.isGBFlag != '2' && showFlag eq '1' }">
						                <th width="9%">发票操作</th>
						                </c:if>
										<!-- <th width="6%">操作</th> -->
									</tr>
								</thead>
								<tbody class="text-c" id="IndemnityTbody">
									<input type="hidden" id="indeSize" value="${fn:length(prePayPVos)}">
									<%@include file="PrePayApplyEdit_IndemnityTr.jsp"%>
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
										<th width="6%">
											<button type="button" class="btn btn-plus Hui-iconfont Hui-iconfont-add" onclick="initChargeType(this)"></button>
										</th>
										<th width="15%">损失险别</th>
										<th width="8%">费用名称</th>
										<th width="10%">费用金额</th>
										<th width="10%">收款人</th>
										<th width="10%">收款人帐号</th>
										<th width="10%">开户银行</th>
										<%--<th width="10%">发票类型</th>--%>
										<th width="5%">税收信息</th>
										<th width="5%">摘要<font class="must">*</font></th>
										<th width="7%">是否增值税专票</th>
						                <th width="7%">税率</th>
						                <c:if test="${handlerStatus eq '3' && prpLregistVo.isGBFlag != '2' && showFlag eq '1' }">
						                <th width="9%">发票操作</th>
						                </c:if>
										<!-- <th width="10%">操作</th> -->
									</tr> 
								</thead>
								<tbody class="text-c" id="FeeTbody">
									<input type="hidden" id="feeSize" value="${fn:length(prePayFVos)}">
										<%@include file="PrePayApplyEdit_FeeTr.jsp"%>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			
				<div class="table_wrap">
					<div class="table_title f14">预支付抢救费报告</div>
					<div class="formtable"> 
						<div class="col-12">
							<textarea class="textarea h100" id="contexts" maxlength="250" name="compensateVo.rescueReport" 
							datatype="*1-250">${compensateVo.rescueReport}</textarea>
						</div>
					</div>
				</div>
			
			<div id="hideDiv" style="display: none"></div>
		</form>
	</div>
	<div id="submitDiv" class="text-c"> 
		<br />
		<input class="btn btn-primary " id="pend" onclick="save('save')" type="submit" value="暂存">
		<input class="btn btn-primary ml-5" id="save" onclick="save('submit')" type="submit" value="提交">
		<input class="btn btn-primary ml-5" id="" onclick="" type="button" value="取消">
	</div>
	<!-- 案件备注功能隐藏域 -->
	<input type="hidden" id="nodeCode" value="PrePay">
</div>

<script type="text/javascript" src="${ctx}/js/prePay/prePayEdit.js"></script>
<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
