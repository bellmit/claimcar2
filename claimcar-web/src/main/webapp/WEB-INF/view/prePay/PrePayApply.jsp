<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>预付任务发起</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<!-- 按钮组 -->
		<div class="top_btn">
			<a class="btn  btn-primary" onclick="viewEndorseInfo('${registVo.registNo}')">保单批改记录</a>
			<a class="btn  btn-primary" onclick="viewPolicyInfo('${registVo.registNo}')">保单详细信息</a>
			<a class="btn  btn-primary" id="" onclick="createRegistMessage('${registVo.registNo}','','')">案件备注</a>
			<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
		
		</div>
		<form id="prePayform" role="form" method="post" name="fm">
			<div class="table_wrap">
				<div class="table_title f14">案件信息</div>
				<div class="table_cont">
					<div class="formtable">
						<div class="row cl">
							<label class="form_label col-1">报案号</label>
							<div class="form_input col-3">${registVo.registNo }</div>
							<label class="form_label col-1">案件性质</label>
							<div class="form_input col-3">自赔</div>
							<label class="form_label col-1">客户联系电话</label>
							<div class="form_input col-3">${registVo.insuredPhone }</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">被保险人</label>
							<div class="form_input col-3">${registVo.prpLRegistExt.insuredName }</div>
							<label class="form_label col-1">报案人联系电话</label>
							<div class="form_input col-3">${registVo.linkerMobile }<c:if test="${!empty registVo.linkerPhone}">,</c:if>${registVo.linkerPhone }</div>
						</div>
					</div>
				</div>
			</div>

			<div class="table_wrap">
				<div class="table_title f14">已发起交强预付任务</div>
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
								<c:forEach var="compensateCIVo" items="${compensateCIVos }" varStatus="Status">
									<tr>
										<td>${compensateCIVo.compensateNo }</td>
										<td>${compensateCIVo.policyNo }</td>
										<td>
											<c:if test="${compensateCIVo.riskCode =='1101' }">交强预付</c:if>
											<c:if test="${compensateCIVo.riskCode !='1101' }">商业预付</c:if>
										</td>
										<td>${compensateCIVo.sumAmt }</td>
										<td>
											<fmt:formatDate value='${compensateCIVo.underwriteDate }' pattern='yyyy-MM-dd HH:mm:ss' />
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>

			<div class="table_wrap">
				<div class="table_title f14">已发起商业预付任务</div>
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
								<c:forEach var="compensateBIVo" items="${compensateBIVos }" varStatus="Status">
									<tr>
										<td>${compensateBIVo.compensateNo }</td>
										<td>${compensateBIVo.policyNo }</td>
										<td>
											<c:if test="${compensateBIVo.riskCode =='1101' }">交强预付</c:if>
											<c:if test="${compensateBIVo.riskCode !='1101' }">商业预付</c:if>
										</td>
										<td>${compensateBIVo.sumAmt }</td>
										<td>
											<fmt:formatDate value='${compensateBIVo.underwriteDate }' pattern='yyyy-MM-dd HH:mm:ss' />
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>

			<div class="table_wrap">
				<div class="table_title f14">预付任务发起</div>
				<div class="table_cont">
					<div class="formtable">
						<div class="row cl" >
							<label>提示：请选择需要进行预付的保单号，然后点击“产生预付任务”按钮！</label>
						</div>
						<div class="row cl" style="padding-top: 15px">
							<c:forEach var="claimVo" items="${claimVos }" varStatus="recordStatus">
								<input type="checkbox" name="claimNoArr" value="${claimVo.claimNo }" class="checkbox mr-5" <c:if test="${claimVo.flag == '1' }">disabled</c:if>>${claimVo.policyNo }
								<c:if test="${claimVo.riskCode == '1101' }">(交强)</c:if>
								<c:if test="${claimVo.riskCode != '1101' }">(商业)</c:if>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
		</form>
		<div class="text-c">
			<br />
			<input class="btn btn-primary " onclick="prePayTaskLaunch()" type="button" value="产生预付任务">
		</div>
	</div>
	<script type="text/javascript" src="${ctx}/js/prePay/prePay.js"></script>
</body>
</html>