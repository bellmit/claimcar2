<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
			<!--  配件列表开始-->
			<div class="table_wrap">
				<div class="table_title f14">配件减损情况</div>
				<div class="table_cont table_list">
					<table class="table table-border">
						<thead class="text-c">
							<tr class="text-c">
								<th>配件id</th>
								<th>配件名称</th>
								<th>单价</th>
								<th>数量</th>
								<th>残值</th>
								<th>价格</th>
								<th>CE建议单价</th>
								<th>CE建议数量</th>
								<th>CE建议残值</th>
								<th>CE建议价格</th>
                                <th>CE建议减损金额</th>
                                <th>是否减损</th>
                                <th>OEM编码</th>
                                <th>减损描述列表</th>
							</tr>
						</thead>
						<tbody class="text-c" style="font-size: 5px">
						<c:forEach var="claimText" items="${prplcecheckResultVo.prplspaReparts}" varStatus="status">
							<tr class="text-c">
								<td>${claimText.insuranceFitId}</td>
								<td>${claimText.fittingName}</td> 
								<td>${claimText.inputPrice}</td>
								<td>${claimText.inputQuantity}</td>
								<td>${claimText.inputRemnant}</td>
								<td>${claimText.inputTotalprice}</td>
								<td>${claimText.feedbackPrice}</td>
								<td>${claimText.feedbackQuantity}</td>
								<td>${claimText.feedbackRemnant}</td>
								<td>${claimText.feedbackTotal}</td>
								<td>${claimText.savingPrice}</td>
								<c:choose>
								<c:when test="${claimText.ceSaving eq 'Y'}">
								<td>有减损</td>
								</c:when>
								<c:otherwise>
								<td>无减损</td>
								</c:otherwise>
								</c:choose>
								<td>${claimText.oemCode}</td>
								<td>${claimText.remark}</td>
							</tr>
						</c:forEach>	
					</tbody>
				</table>
			</div>
		</div>
		<!--  意见列表结束-->
