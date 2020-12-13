<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
			<!--  工时列表开始-->
			<div class="table_wrap">
				<div class="table_title f14">工时减损情况</div>
				<div class="table_cont table_list">
					<table class="table table-border">
						<thead class="text-c">
							<tr class="text-c">
								<th>工时ID</th>
								<th>工种</th>
								<th>配件名称</th>
								<th>单价</th>
								<th>数量</th>
								<th>保险公司提交价格</th>
								<th>CE建议单价</th>
								<th>CE建议数量</th>
								<th>CE建议价格</th>
								<th>CE建议减损金额</th>
								<th>是否减损</th>
								<th>保险公司配件ID</th> 
								<th>减损描述列表</th> 

							</tr>
						</thead>
						<tbody class="text-c" style="font-size: 5px">
						<c:forEach var="claimText" items="${prplcecheckResultVo.prplLabors}" varStatus="status">
							<tr class="text-c">
								<td>${claimText.insuranceLaborId}</td>
								<td>${claimText.repairType}</td>
								<td>${claimText.repairName}</td> 
								<td>${claimText.inputPrice}</td>
								<td>${claimText.inputCount}</td>
								<td>${claimText.inputTotalprice}</td>
								<td>${claimText.feedbackUnitPrice}</td>
								<td>${claimText.feedbackQuantity}</td>
								<td>${claimText.feedbackPrice}</td>
								<td>${claimText.savingPrice}</td>
								<c:choose>
								<c:when test="${claimText.ceSaving eq 'Y'}">
								<td>有减损</td>
								</c:when>
								<c:otherwise>
								<td>无减损</td>
								</c:otherwise>
								</c:choose>
								<td>${claimText.insuranceFitId}</td>
								<td>${claimText.remark}</td>
							</tr>
						</c:forEach>	
					</tbody>
				</table>
			</div>
		</div>
		<!--  意见列表结束-->
