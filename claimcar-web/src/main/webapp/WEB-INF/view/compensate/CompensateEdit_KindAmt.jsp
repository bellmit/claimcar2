<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
	<div class="formtable">
		<div class="table_cont table_list">
			<table border="1" class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th>险别</th>
						<th>损失类型</th>
						<th>定损金额</th>
						<th>实赔金额</th>
						<th>总实赔金额</th>
					</tr>
				</thead>
				<tbody class="text-c">
				
				<c:forEach var="prpLKindAmtSummary" items="${prpLKindAmtSummaryList}" varStatus="status">
					<c:if test="${prpLKindAmtSummary.kindCode eq 'A' }">
						<tr>
							<td><app:codetrans codeType="KindCode" codeCode="${prpLKindAmtSummary.kindCode }" riskCode="${riskCode }"/></td>
							<td>----</td>
							<td>${prpLKindAmtSummary.sumLoss }</td>
							<td>${prpLKindAmtSummary.sumRealPay }</td>
							<td>${prpLKindAmtSummary.sumRealPay }</td>
						</tr>
					</c:if>
				</c:forEach>
				<c:if test="${k >0}">
					<tr>
						<td rowspan='3'><app:codetrans codeType="KindCode" codeCode="${kAmtBi1.kindCode }" riskCode="${riskCode }"/></td>
						<td>车</td>
						<c:if test="${ !empty kAmtBi1}">
							<td>${kAmtBi1.sumLoss}</td>
						</c:if>
						<c:if test="${empty kAmtBi1}">
							<td>----</td>
						</c:if>
						<c:if test="${ !empty kAmtBi1}">
							<td>${kAmtBi1.sumRealPay}</td>
						</c:if>
						<c:if test="${empty kAmtBi1}">
							<td>----</td>
						</c:if>
						<td rowspan='3'>${SumRealPayAll}</td>
					</tr>
					<c:if test="${ !empty kAmtBi2}">
					<tr class="">
						<td>财</td>
						<c:if test="${ !empty kAmtBi2}">
							<td>${kAmtBi2.sumLoss}</td>
						</c:if>
						<c:if test="${empty kAmtBi2}">
							<td>----</td>
						</c:if>
						<c:if test="${ !empty kAmtBi2}">
							<td>${kAmtBi2.sumRealPay}</td>
						</c:if>
						<c:if test="${empty kAmtBi2}">
							<td>----</td>
						</c:if>
					</tr>
					</c:if>
					<c:if test="${ !empty kAmtBi3}">
					<tr>
						<td>人</td>
						<c:if test="${ !empty kAmtBi3}">
							<td>${kAmtBi3.sumLoss}</td>
						</c:if>
						<c:if test="${empty kAmtBi3}">
							<td>----</td>
						</c:if>
						<c:if test="${ !empty kAmtBi3}">
							<td>${kAmtBi3.sumRealPay}</td>
						</c:if>
						<c:if test="${empty kAmtBi3}">
							<td>----</td>
						</c:if>
					</tr>
				</c:if>
				</c:if>
				
				<c:forEach var="prpLKindAmtSummary" items="${prpLKindAmtSummaryList}" varStatus="status">
					<c:if test="${prpLKindAmtSummary.kindCode eq 'D12' }">
						<tr>
							<td><app:codetrans codeType="KindCode" codeCode="${prpLKindAmtSummary.kindCode }" riskCode="${riskCode }"/></td>
							<td>----</td>
							<td>${prpLKindAmtSummary.sumLoss }</td>
							<td>${prpLKindAmtSummary.sumRealPay }</td>
							<td>${prpLKindAmtSummary.sumRealPay }</td>
						</tr>
					</c:if>
				</c:forEach>
				</tbody>
				</table>
			</div>
		</div>
