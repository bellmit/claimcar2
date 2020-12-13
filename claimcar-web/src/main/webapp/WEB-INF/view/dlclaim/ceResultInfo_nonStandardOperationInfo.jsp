<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
			<!--  操作不规范列表开始-->
			<div class="table_wrap">
				<div class="table_title f14">操作不规范列表</div>
				<div class="table_cont table_list">
					<table class="table table-border">
						<thead class="text-c">
							<tr class="text-c">
								<th>序号</th>
								<th>风险描述</th>
								<th>检测建议</th>
							</tr>
						</thead>
						<tbody class="text-c" style="font-size: 5px">
						<c:forEach var="claimText" items="${prplcecheckResultVo.prplnoOperations}" varStatus="status">
							<tr class="text-c">
								<td>${claimText.factCode}</td>
								<td>${claimText.riskDesc}</td>
								<td>${claimText.suggest}</td> 
							</tr>
						</c:forEach>	
					</tbody>
				</table>
			</div>
		</div>
		<!--  意见列表结束-->
