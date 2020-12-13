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
								<td>${prplTestinfoMainVo.partlossAmount}</td>
								<td>${prplTestinfoMainVo.partceAmount}</td>
								<td>${prplTestinfoMainVo.partsavingAmount}</td>
								<td rowspan="2">${prplTestinfoMainVo.riskSavingAmount}</td>
								
						</tr>
						<tr class="text-c">
								<td>工时</td> 
								<td>${prplTestinfoMainVo.laborlossAmount}</td>
								<td>${prplTestinfoMainVo.laborceAmount}</td>
								<td>${prplTestinfoMainVo.laborsavingAmount}</td>
							    
								
						</tr>
						<tr class="text-c">
								<td>金额合计</td> 
								<td>${prplTestinfoMainVo.totalPrice}</td>
								<td>${prplTestinfoMainVo.cetotalPrice}</td>
								<td colspan="2">${prplTestinfoMainVo.savingTotalPrice}</td>
								
								
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<!--  意见列表结束-->
