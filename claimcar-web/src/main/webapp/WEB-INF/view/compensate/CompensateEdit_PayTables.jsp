<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:if test="${!empty prpLPrePayP }">
<div class="table_wrap">
<c:if test="${flag eq '1' }">
<div class="table_title f14">交强预付赔款明细</div>
</c:if>
<c:if test="${flag eq '2' }">
<div class="table_title f14">商业预付赔款明细</div>
</c:if>
<div class="table_cont">
	<div class="formtable" id="prePaidPTables">
		<div class="table_con">
			<table class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th>损失险别</th>
						<th>预付款项类型</th>
						<th>预付金额</th>
						<th>收款人</th>
						<th>例外标志</th>
						<th>例外原因</th>
						<th>收款方账号</th>
						<th>开户银行</th>
					</tr>
				</thead>
				<tbody class="text-c">
				<c:forEach var="prpLPrePay" items="${prpLPrePayP }" varStatus="status">
					<tr>
						<td><app:codetrans codeType="KindCode" codeCode="${prpLPrePay.kindCode }" riskCode="${riskCode }" /></td>
						<td><app:codetrans codeType="PrePayFeeType" codeCode="${prpLPrePay.chargeCode }"/></td>
						<td><span id="preAmt[${status.index }]" name="preAmt">${prpLPrePay.payAmt }</span></td>
						<td> ${prpLPrePay.payeeName } </td>
						<td><app:codetrans codeType="YN10" codeCode="${prpLPrePay.otherFlag }" /></td>
						<td><app:codetrans codeType="OtherCase" codeCode="${prpLPrePay.otherCause }" /></td>
						<td>${prpLPrePay.accountNo }</td>
						<td><app:codetrans codeType="BankCode" codeCode="${prpLPrePay.bankName }"/></td>
					</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
</c:if>

<c:if test="${!empty prpLPrePayF }">
<div class="table_wrap">
<c:if test="${flag eq '1' }">
	<div class="table_title f14">交强预付费用明细</div>
</c:if>
<c:if test="${flag eq '2' }">
	<div class="table_title f14">商业预付费用明细</div>
</c:if>
<div class="table_cont" id="prePaidFTables">
	<div class="formtable">
		<div class="table_con">
			<table class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th>损失险别</th>
						<th>费用名称</th>
						<th>费用金额</th>
						<th>收款人</th>
						<th>收款方账号</th>
						<th>开户银行</th>
					</tr>
				</thead>
				<tbody class="text-c">
					<c:forEach var="prpLPrePay" items="${prpLPrePayF }" varStatus="status">
					<tr>
						<td><app:codetrans codeType="KindCode" codeCode="${prpLPrePay.kindCode }" riskCode="${riskCode }"/></td>
						<td><app:codetrans codeType="ChargeCode" codeCode="${prpLPrePay.chargeCode }"/></td>
						<td><span id="preAmt[${status.index }]" name="preAmt">${prpLPrePay.payAmt }</span></td>
						<td>${prpLPrePay.payeeName }
						</td>
						<td>${prpLPrePay.accountNo }</td>
						<td><app:codetrans codeType="BankCode" codeCode="${prpLPrePay.bankName }"/></td>
					</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
</c:if>

<c:if test="${flag eq '1' && fn:length(prpLPadPayPersons) > 0}">
<!-- 交强垫付赔款信息界面 -->
<div class="table_title f14">交强垫付赔款明细</div>
<div class="table_cont">
	<div class="formtable" id="PaidPersTables">
		<div class="table_con">
			<table class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th>损失险别</th>
						<th>垫付款项类型</th>
						<th>垫付金额</th>
						<th>收款人</th>
						<th>例外标志</th>
						<th>例外原因</th>
						<th>收款方账号</th>
						<th>开户银行</th>
					</tr>
				</thead>
				<tbody class="text-c">
				<c:forEach var="prpLPadPayPerson" items="${prpLPadPayPersons }" varStatus="status">
					<tr>
						<td>机动车交通事故责任强制险</td>
						<td><app:codetrans codeType="FeeType" codeCode="${prpLPadPayPerson.injuryType }"/></td>
						<td><span id="padAmt[${status.index }]" name="preAmt">${prpLPadPayPerson.costSum }</span></td>
						<td>${prpLPadPayPerson.payeeName }</td>
						<td>
								<app:codetrans codeType="YN10" codeCode="${prpLPadPayPerson.otherFlag }" />
						</td>
						<td>
							<app:codetrans codeType="OtherCause" codeCode="${prpLPadPayPerson.otherCause }" />
						</td>
						<td>${prpLPadPayPerson.accountNo }</td>
						<td><app:codetrans codeType="BankCode" codeCode="${prpLPadPayPerson.bankName }"/></td>
					</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</c:if>
	
	
	
	