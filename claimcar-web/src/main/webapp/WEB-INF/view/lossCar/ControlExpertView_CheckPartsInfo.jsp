<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
			<!--  配件列表开始-->
			<div class="table_wrap">
				<div class="table_title f14">配件列表</div>
				<div class="table_cont table_list">
					<table class="table table-border">
						<thead class="text-c">
							<tr class="text-c">
								<th>序号</th>
								<th>零部件名称</th>
								<th>定损单价</th>
								<th>定损数量</th>
								<th>定损总价</th>
								<th>CE建议单价</th>
								<th>CE建议数量</th>
								<th>CE建议总价</th>
								<th>建议减损金额</th>
								<th>减损描述</th>

							</tr>
						</thead>
						<tbody class="text-c" style="font-size: 5px">
						<c:forEach var="claimText" items="${prplPartsInfoVos2}" varStatus="status">
							<tr class="text-c">
								<td>${status.index + 1}</td>
								<td>${claimText.partsName}</td> 
								<td>${claimText.partsPrice}</td>
								<td>${claimText.partsCount}</td>
								<td>${claimText.partstotalPrice}</td>
								<td>${claimText.cePrice}</td>
								<td>${claimText.ceCount}</td>
								<td>${claimText.cetotalPrice}</td>
								<td>${claimText.savingPrice}</td>
								<td>${claimText.savingDescs}</td>
								
							</tr>
						</c:forEach>	
					</tbody>
				</table>
			</div>
		</div>
		<!--  意见列表结束-->
