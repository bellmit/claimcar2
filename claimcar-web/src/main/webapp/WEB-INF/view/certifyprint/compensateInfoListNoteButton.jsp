<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>理赔计算书打印</title>
</head>
<body>
	<div class="table_wrap">
	<div class="table_cont table_list">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th>业务号</th>
					<th>赔案号</th>
					<th>保单号</th>
					<th>赔款金额</th>
					<th>直付例外</th>
					<th>收款人</th>
					<th>账户号</th>
					<th>开户行</th>
					<th>操作</th>
					</tr>
			</thead>
				<tbody id="propLossTbody">
					<c:forEach var="paymentVo" items="${paymentLists}" varStatus="status">
					<tr class="text-c">
					<td>${paymentVo.compensateNo}</td>
					<td>${paymentVo.claimNo}</td>
					<td>${paymentVo.policyNo}</td>
					<td>${paymentVo.sumRealPay}</td>
					<td>${paymentVo.otherFlag}</td>
					<td>${paymentVo.payeeName}</td>
					<td>${paymentVo.accountNo}</td>
					<td>${paymentVo.bankName}</td>
					<td><input type='button' class='btn btn-primary' value='打印'  onclick="certifyPrintPayFeeSon('${paymentVo.policyNo}','${paymentVo.compensateNo}','${paymentVo.payeeId}')"/></td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
	</div>
</div>

	<script type="text/javascript">
	
	</script>
</body>
</html>