<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<div class="table_title f14">反洗钱信息</div>
<div class="table_cont mt-30 md-30">
	<div class="formtable">
		<table class="table table-border table-bordered text-c">
			<!-- <thead class="text-c">
				<tr>
					<th>车牌号</th>
					<th>事故责任比例</th>
				</tr>
			</thead> -->
			<tbody class="text-c">
						<%-- <tr>
							<td width="60%">
								<label class="form_label col-2">客户识别途径:</label>
								<div class="form_input col-9">
									<app:codeSelect codeType="recogniTionWay" name="reasonCode" nullToVal="1" value="${claimTextVoByTaskInUser.reasonCode }" type="radio"/>
								</div>
							</td>
							<td width="40%">
								<label class="form_label col-3">境内境外：</label>
								<div class="form_input col-6">
									<app:codeSelect codeType="territoryInOut" name="reasonCode" nullToVal="1" value="${claimTextVoByTaskInUser.reasonCode }" type="radio"/>								
								</div>
							</td>
						</tr> --%>
						<tr>
							<%-- <td width="60%">
								<label class="form_label col-2">识别发现问题:</label>
							<div class="form_input col-10">
								<app:codeSelect codeType="identifiCation" name="" 
								value="" type="checkbox" />
							</div>
							</td> --%>
							
							<td width="60%">
							<c:choose>
		                <c:when test="${handlerStatus !='3'}">
		                <input type="button" class="btn  btn-primary"  onclick="CustomInfoOpen('${claimNo}','${prpLWfTaskVo.registNo}')" value="反洗钱信息补录"/>
		                <input type="button" class="btn  btn-primary"  onclick="freezeUp('${claimNo}','${prpLWfTaskVo.registNo}','${riskCode}')" value="冻结"/>
		                </c:when>
		                <c:otherwise>
		                <input  type="button" class="btn  btn-disabled" value="反洗钱信息补录"/>
		                <input  type="button" class="btn  btn-disabled" value="冻结"/>
		                 </c:otherwise>
	                     </c:choose>
									<input type="button" class="btn  btn-primary" value="新增风险交易" onclick="getCustomerFxUrl()"/>
									<c:forEach var="prpCInsured" items="${prpLCMain.prpCInsureds}" varStatus="status">
					 					<c:if test="${prpCInsured.insuredFlag eq '1'}">
					 						<input type="hidden" id="identifyFlag" value="${prpCInsured.identifyNumber}">
					 					</c:if>
				 					</c:forEach>
				 					
				 					
									
									<app:amlReg userCode="${userVo.userCode }"  comCode="${userVo.comCode }" bussNo="${prpLCompensate.registNo }" bussType="C" recTime="C" btnName="identifyFlag" btnID="appli"
								/>
									<%-- <%=fxqAPIPanel.loadCustCheckButton(userVo.getUserCode(),userVo.getComCode(),"700020020171101000020058","C","U","flagffff","appli") %> --%>
									<!-- <input type="button" class="btn  btn-primary" value="客户识别" onclick="getCustomerFxqUrl()"/> -->
							</td>
						</tr>
			</tbody>
		</table>
	</div>
</div>
