<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
			<!--  价格合计列表开始-->
			<div class="table_wrap">
				<div class="table_title f14">价格合计列表</div>
				<div class="table_cont table_list">
					<table class="table table-border">
						<thead class="text-c">
							<tr class="text-c">
								<th>项目</th>
								<th>定损总金额</th>
								<th>CE建议总金额</th>
								<th>建议减损总金额</th>
								<th>欺诈风险减损总金额</th>

							</tr>
						</thead>
						<tbody class="text-c" style="font-size: 5px">
						<tr class="text-c">
								<td>配件</td> 
								<td>${prplTestinfoMainVo2.partlossAmount}</td>
								<td>${prplTestinfoMainVo2.partceAmount}</td>
								<td>${prplTestinfoMainVo2.partsavingAmount}</td>
								<td rowspan="2">${prplTestinfoMainVo2.riskSavingAmount}</td>
								
						</tr>
						<tr class="text-c">
								<td>工时</td> 
								<td>${prplTestinfoMainVo2.laborlossAmount}</td>
								<td>${prplTestinfoMainVo2.laborceAmount}</td>
								<td>${prplTestinfoMainVo2.laborsavingAmount}</td>
							    
								
						</tr>
						<tr class="text-c">
								<td>金额合计</td> 
								<td>${prplTestinfoMainVo2.totalPrice}</td>
								<td>${prplTestinfoMainVo2.cetotalPrice}</td>
								<td colspan="2">${prplTestinfoMainVo2.savingTotalPrice}</td>
								
								
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<!--  意见列表结束-->
