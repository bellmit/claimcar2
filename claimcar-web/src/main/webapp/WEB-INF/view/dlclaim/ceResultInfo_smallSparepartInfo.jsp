<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
			<!--  辅料列表开始-->
			<div class="table_wrap">
				<div class="table_title f14">辅料减损情况</div>
				<div class="table_cont table_list">
					<table class="table table-border">
						<thead class="text-c">
							<tr class="text-c">
								<th>保险公司辅料ID</th>
								<th>辅料名称</th>
								<th>保险公司提交单价</th>
								<th>保险公司提交数量</th>
								<th>保险公司提交价格</th>
								<th>CE建议单价</th>
								<th>CE建议数量</th>
								<th>CE建议价格</th>
								<th>CE建议减损金额</th>
								<th>是否减损</th>
								<th>减损描述列表</th> 

							</tr>
						</thead>
						<tbody class="text-c" style="font-size: 5px">
						<c:forEach var="claimText" items="${prplcecheckResultVo.prplsmallSpareparts}" varStatus="status">
							<tr class="text-c">
								<td>${claimText.insuranceMaterialId}</td>
								<td>${claimText.materialName}</td>
								<td>${claimText.inputPrice}</td> 
								<td>${claimText.inputCount}</td>
								<td>${claimText.inputTotalprice}</td>
								<td>${claimText.feedbackPrice}</td>
								<td>${claimText.feedbackQuantity}</td>
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
								<td>${claimText.remark}</td>
							</tr>
						</c:forEach>	
					</tbody>
				</table>
			</div>
		</div>
		<!--  意见列表结束-->
