<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
			<!--  风险点提示列表开始-->
			<div class="table_wrap">
				<div class="table_title f14">风险点提示列表</div>
				<div class="table_cont table_list">
					<table class="table table-border">
						<thead class="text-c">
							<tr class="text-c">
								<th width="15%">规则编码</th>
								<th width="15%">规则名称</th>
								<th width="35%">风险描述</th>
								<th width="35%">检测建议</th>
								

							</tr>
						</thead>
						<tbody class="text-c" style="font-size: 5px">
						<c:forEach var="claimText" items="${prplTestinfoMainVo1.prplRiskpointInfos}" varStatus="status">
							<tr class="text-c">
								<td>${claimText.factCode}</td> 
								<td>${claimText.factName}</td>
								<td>${claimText.riskDesc}</td>
								<td>${claimText.suggest}</td>
								
								
							</tr>
						</c:forEach>	
					</tbody>
				</table>
			</div>
		</div>
		<!--  意见列表结束-->
