<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>详细信息</title>
<!-- 查看估损更新轨迹 信息-->
</head>
<script type="text/javascript">
	$(function() {
		$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current",
				"click", "0");
		layer.config({
			extend : 'extend/layer.ext.js'
		});
	});
</script>
<body>
	<div id="tab-system" class="HuiTab">
		<div class="tabBar f_gray4 cl">
			<span><i class="Hui-iconfont handun">&#xe619;</i>交强</span> <span><i
				class="Hui-iconfont handing">&#xe619;</i>商业</span>

		</div>
		<div class="tabCon table_list">
			<!-- 交强   开始 -->
			<!-- <div class="tabCon clearfix">-->
			<table class="table table-border table-hover" cellpadding="0"
				cellspacing="0">
				<thead>

					<tr>
						<th Colspan="2">立案号</th>
						<th Colspan="3"><u>${claimNo1}</u></th>
						<th Colspan="2">保单类型</th>
						<th Colspan="2"><u>交强</u></th>
						<th Colspan="2">估计赔款总金额</th>
						<th Colspan="1"><c:set var="Sum1" value="0" /> <c:forEach
								items="${prpLClaimKindHisList}" var="vo1">
							
									<c:if test="${vo1.riskCode == '1101'}">
										<c:set var="Sum1" value="${Sum1 + vo1.claimLoss}" />
									</c:if>
								
							</c:forEach><u>${Sum1}</u></th>

					</tr>

					<tr>
						<th>历史调整次数</th>
						<th>赔款类别</th>
						<th>险别</th>
						<th>损失类型</th>
						<th>估损金额</th>
						<th>施救费</th>
						<th>估计赔款</th>
						<th>估损金额变化量</th>
						<th>施救费变化量</th>
						<th>估计赔款变化量</th>
						<th>输入时间</th>
						<th>调整原因</th>
					</tr>
				</thead>
				<c:forEach var="vo" items="${prpLClaimKindHisList}">
				     <c:if test="${vo.riskCode == '1101'}">
							<tr>
								<td>第${vo.estiTimes}次调整</td>
								<td><c:if test="${vo.feeType == 'P'}">赔款</c:if> <c:if
										test="${vo.feeType == 'F'}">费用</c:if></td>
								<td><app:codetrans codeCode="${vo.kindCode}" codeType="KindCode"  riskCode="${riskCode }"/></td>
								<td>${vo.lossItemName}</td>
								<td>${vo.defLoss}</td>
								<td>${vo.rescueFee}</td>
								<td>${vo.claimLoss}</td>
								<td>${vo.defLossChg}</td>
								<td>${vo.rescueFeeChg}</td>
								<td>${vo.claimLossChg}</td>
								<td><fmt:formatDate value='${vo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
								<td>${vo.adjustReason}</td>
							</tr>
						</c:if>
				
				</c:forEach>

			</table>
			<!--table   结束-->

		</div>
		<!--</div>  -->
		<div class="tabCon table_list">
			<!-- 商业   开始 -->
			<!--<div class="tabCon clearfix">-->
			<table class="table table-border table-hover" cellpadding="0"
				cellspacing="0">
				<thead>

					<tr>
						<th Colspan="2">立案号</th>
						<th Colspan="3"><u>${claimNo2}</u></th>
						<th Colspan="2">保单类型</th>
						<th Colspan="2"><u>商业</u></th>
						<th Colspan="2">估计赔款总金额</th>
						<th Colspan="1"><c:set var="Sum2" value="0" /> <c:forEach
								items="${prpLClaimKindHisList}" var="vo2">
								
									<c:if test="${vo2.riskCode != '1101'}">
										<c:set var="Sum2" value="${Sum2 + vo2.claimLoss}"/>
									</c:if>
						
							</c:forEach><u>${Sum2}</u></th>

					</tr>

					<tr>
						<th>历史调整次数</th>
						<th>赔款类别</th>
						<th>险别</th>
						<th>损失类型</th>
						<th>估损金额</th>
						<th>施救费</th>
						<th>估计赔款</th>
						<th>估损金额变化量</th>
						<th>施救费变化量</th>
						<th>估计赔款变化量</th>
						<th>输入时间</th>
						<th>调整原因</th>
					</tr>
				</thead>
				<c:forEach var="vo1" items="${prpLClaimKindHisList}">
					
						<c:if test="${vo1.riskCode != '1101'}">
							<tr>
								<td>第${vo1.estiTimes}次调整</td>
								<td><c:if test="${vo1.feeType == 'P'}">赔款</c:if> <c:if
										test="${vo1.feeType == 'F'}">费用</c:if></td>
								<td><app:codetrans codeCode="${vo1.kindCode}" codeType="KindCode" riskCode="${riskCode }" /></td>
								<td>${vo1.lossItemName}</td>
								<td>${vo1.defLoss}</td>
								<td>${vo1.rescueFee}</td>
								<td>${vo1.claimLoss}</td>
								<td>${vo1.defLossChg}</td>
								<td>${vo1.rescueFeeChg}</td>
								<td>${vo1.claimLossChg}</td>
								<td><fmt:formatDate value='${vo1.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
								<td>${vo1.adjustReason}</td>
							</tr>
						</c:if>
		
				</c:forEach>

			</table>
			<!--table   结束-->



		</div>
	</div>
</body>
</html>